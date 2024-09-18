package http4kbox

import com.natpryce.hamkrest.absent
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.throws
import env.TestSettings
import http4kbox.Settings.AWS_BUCKET
import org.http4k.chaos.ChaosBehaviours.ReturnStatus
import org.http4k.connect.amazon.AWS_REGION
import org.http4k.connect.amazon.s3.FakeS3
import org.http4k.connect.amazon.s3.createBucket
import org.http4k.core.Status.Companion.INTERNAL_SERVER_ERROR
import org.junit.jupiter.api.Test
import java.util.UUID

class FileStorageTest {

    private val TEST_KEY = FileName.of(UUID.randomUUID().toString())
    private val CONTENT = "hello!"

    private val s3 = FakeS3().apply {
        s3Client().createBucket(TestSettings[AWS_BUCKET], TestSettings[AWS_REGION])
    }
    private val fs = FileStorage(TestSettings, s3)

    @Test
    fun `get unknown file returns null`() {
        assertThat(fs[TEST_KEY], absent())
    }

    @Test
    fun `has no files initially`() {
        assertThat(fs.list(), equalTo(emptyList()))
    }

    @Test
    fun `inserted key is listed`() {
        fs[TEST_KEY] = CONTENT.byteInputStream()

        assertThat(fs.list(), equalTo(listOf(TEST_KEY)))
    }

    @Test
    fun `unknown key returns null`() {
        assertThat(fs[TEST_KEY], absent())
    }

    @Test
    fun `inserted key can be retrieved`() {
        fs[TEST_KEY] = CONTENT.byteInputStream()

        assertThat(fs[TEST_KEY]!!.reader().readText(), equalTo(CONTENT))
    }

    @Test
    fun `inserted key can be deleted`() {
        fs[TEST_KEY] = CONTENT.byteInputStream()

        fs.delete(TEST_KEY)

        assertThat(fs.list(), equalTo(emptyList()))
    }

    @Test
    fun `endpoints failing produce Exception`() {
        s3.misbehave(ReturnStatus(INTERNAL_SERVER_ERROR))

        assertThat({ fs[TEST_KEY] }, throws<Exception>())
        assertThat({ fs.delete(TEST_KEY) }, throws<Exception>())
        assertThat({ fs.list() }, throws<Exception>())
    }
}
