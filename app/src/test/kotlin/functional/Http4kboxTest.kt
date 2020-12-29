package functional

import com.natpryce.hamkrest.and
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import env.FakeS3
import env.TestSettings
import http4kbox.Http4kBox
import org.http4k.core.ContentType.Companion.MultipartFormWithBoundary
import org.http4k.core.ContentType.Companion.TEXT_PLAIN
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.MultipartFormBody
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.OK
import org.http4k.core.Status.Companion.SEE_OTHER
import org.http4k.core.with
import org.http4k.hamkrest.hasBody
import org.http4k.hamkrest.hasStatus
import org.http4k.lens.Header.CONTENT_TYPE
import org.http4k.lens.MultipartFormFile
import org.junit.jupiter.api.Test

class Http4kboxTest {
    private val http4kbox = Http4kBox(TestSettings, FakeS3())

    @Test
    fun `can list files`() {
        val list = http4kbox(Request(GET, "/"))
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


    private fun Response.countFiles() = bodyString().split("""id="file-""").size - 1
    private fun Response.containsFile(name: String) = bodyString().contains("""id="file-$name""")

    private fun getFile(key: String) = http4kbox(Request(GET, "/$key"))
    private fun listFiles() = http4kbox(Request(GET, "/"))
    private fun deleteFile(key: String) = http4kbox(Request(POST, "/$key/delete"))

    private fun uploadFile(name: String, content: String) {
        val file = MultipartFormFile(name, TEXT_PLAIN, content.byteInputStream())
        val body = MultipartFormBody().plus("file" to file)
        val upload = http4kbox(Request(POST, "/")
            .with(CONTENT_TYPE of MultipartFormWithBoundary(body.boundary))
            .body(body))

        assertThat(upload, hasStatus(SEE_OTHER))
    }
}