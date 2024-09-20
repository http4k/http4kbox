package functional

import com.natpryce.hamkrest.and
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import env.FakeGitHub
import env.TestSettings
import http4kbox.Http4kBox
import http4kbox.Settings.AWS_BUCKET
import org.http4k.connect.amazon.AWS_REGION
import org.http4k.connect.amazon.s3.FakeS3
import org.http4k.connect.amazon.s3.createBucket
import org.http4k.core.ContentType.Companion.MultipartFormWithBoundary
import org.http4k.core.ContentType.Companion.TEXT_PLAIN
import org.http4k.core.Method.DELETE
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.MultipartFormBody
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.OK
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.core.with
import org.http4k.filter.ClientFilters
import org.http4k.filter.RequestFilters
import org.http4k.hamkrest.hasBody
import org.http4k.hamkrest.hasStatus
import org.http4k.lens.Header.CONTENT_TYPE
import org.http4k.lens.MultipartFormFile
import org.http4k.routing.reverseProxy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Clock

class Http4kboxTest {

    private val clock = Clock.systemUTC()

    private val github = FakeGitHub(clock)

    private val s3 = FakeS3().apply {
        s3Client().createBucket(TestSettings[AWS_BUCKET], TestSettings[AWS_REGION])
    }

    private val http4kbox = Http4kBox(TestSettings, reverseProxy("s3" to s3, "github" to github), clock)

    private val client =
        ClientFilters.SetBaseUriFrom(Uri.of("http://http4kbox"))
            .then(ClientFilters.FollowRedirects())
            .then(ClientFilters.Cookies(clock))
            .then(
                reverseProxy(
                    "github" to github,
                    "http4kbox" to http4kbox
                )
            )

    @BeforeEach
    fun login() {
        client(Request(GET, "/"))
    }

    @Test
    fun `can list files`() {
        val list = client(Request(GET, "/"))
        assertThat(list, hasStatus(OK))
        assertThat(list.countFiles(), equalTo(0))
    }

    @Test
    fun `can upload file and it appears in list`() {
        uploadFile("bob.txt", "content")

        val list = listFiles()
        assertThat(list.countFiles(), equalTo(1))
        assertThat(list.containsFile("bob.txt"), equalTo(true))
    }

    @Test
    fun `can upload and retrieve file`() {
        uploadFile("bob.txt", "content")

        val file = getFile("bob.txt")

        assertThat(file, hasStatus(OK).and(hasBody("content")))
    }

    @Test
    fun `retrieve unknown file`() {
        val file = getFile("bob.txt")

        assertThat(file, hasStatus(NOT_FOUND))
    }

    @Test
    fun `can delete an uploaded file`() {
        uploadFile("bob.txt", "content")

        deleteFile("bob.txt")

        assertThat(listFiles().countFiles(), equalTo(0))
    }

    @Test
    fun `can list files through the API`() {
        uploadFile("bob.txt", "content")
        val apiClient = RequestFilters.SetHeader("http4k-api-key", "http4kbox").then(client)
        assertThat(apiClient(Request(GET, "/api/list")).bodyString(), equalTo("""["bob.txt"]"""))
    }

    private fun Response.countFiles() = bodyString().split("""id="file-""").size - 1
    private fun Response.containsFile(name: String) = bodyString().contains("""id="file-$name""")

    private fun getFile(key: String) = client(Request(GET, "/$key"))
    private fun listFiles() = client(Request(GET, "/"))
    private fun deleteFile(key: String) = client(Request(DELETE, "/$key"))

    private fun uploadFile(name: String, content: String) {
        val file = MultipartFormFile(name, TEXT_PLAIN, content.byteInputStream())
        val body = MultipartFormBody().plus("file" to file)
        val upload = client(
            Request(POST, "/")
                .with(CONTENT_TYPE of MultipartFormWithBoundary(body.boundary))
                .body(body)
        )

        assertThat(upload, hasStatus(OK))
    }
}