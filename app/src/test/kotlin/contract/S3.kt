package contract

import com.natpryce.hamkrest.absent
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.throws
import env.FakeS3
import env.TestSettings
import http4kbox.S3
import http4kbox.S3Error
import http4kbox.S3Key
import org.http4k.client.JavaHttpClient
import org.http4k.cloudnative.env.Environment
import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status.Companion.INTERNAL_SERVER_ERROR
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.then
import org.http4k.filter.DebuggingFilters
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.util.UUID

abstract class S3Contract(http: HttpHandler, protected val env: Environment) {

    private val TEST_KEY = S3Key(UUID.randomUUID().toString())
    private val CONTENT = "hello!"

    private val s3 = S3.configured(env, http)

    @Test
    fun `get unknown file returns null`() {
        assertThat(S3.configured(env) { Response(NOT_FOUND) }[S3Key("unknown")], absent())
    }

    @Test
    fun `has no files initially`() {
        assertThat(s3.list(), equalTo(emptyList()))
    }

    @Test
    fun `inserted key is listed`() {
        s3[TEST_KEY] = CONTENT.byteInputStream()

        assertThat(s3.list(), equalTo(listOf(TEST_KEY)))
    }

    @Test
    fun `unknown key returns null`() {
        assertThat(s3[TEST_KEY], absent())
    }

    @Test
    fun `inserted key can be retrieved`() {
        s3[TEST_KEY] = CONTENT.byteInputStream()

        assertThat(s3[TEST_KEY]!!.reader().readText(), equalTo(CONTENT))
    }

    @Test
    fun `inserted key can be deleted`() {
        s3[TEST_KEY] = CONTENT.byteInputStream()

        s3.delete(TEST_KEY)

        assertThat(s3.list(), equalTo(emptyList()))
    }
}

class FakeS3Test : S3Contract(DebuggingFilters.PrintRequestAndResponse().then(FakeS3()), TestSettings) {

    @Test
    fun `endpoints failing produce S3Error`() {
        val errorProne = S3.configured(env) { Response(INTERNAL_SERVER_ERROR) }
        assertThat({ errorProne[S3Key("unknown")] }, throws<S3Error>())
        assertThat({ errorProne.delete(S3Key("unknown")) }, throws<S3Error>())
        assertThat({ errorProne.list() }, throws<S3Error>())
    }
}

@Disabled("You need to set real S3 credentials in your ENV to run this")
class RealS3Test : S3Contract(JavaHttpClient(), Environment.ENV)