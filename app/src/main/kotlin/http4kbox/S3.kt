package http4kbox

import http4kbox.Settings.AWS_BUCKET
import http4kbox.Settings.AWS_CREDENTIALS
import http4kbox.Settings.S3_CREDENTIAL_SCOPE
import io.github.konfigur8.Configuration
import org.http4k.core.Body
import org.http4k.core.ContentType.Companion.APPLICATION_XML
import org.http4k.core.Filter
import org.http4k.core.HttpHandler
import org.http4k.core.Method.DELETE
import org.http4k.core.Method.GET
import org.http4k.core.Method.PUT
import org.http4k.core.Request
import org.http4k.core.Status
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.filter.AwsAuth
import org.http4k.filter.ClientFilters
import org.http4k.lens.string
import java.io.InputStream

class S3Error(status: Status) : Exception("S3 returned $status")

data class S3Key(val value: String) {
    companion object {
        fun parseFiles(value: String) = Regex("""Key>(.+?)</Key""").findAll(value).map { S3Key(it.groupValues[1]) }.toList()
    }
}

class S3(private val aws: HttpHandler) {

    private val listFiles = Body.string(APPLICATION_XML).map(S3Key.Companion::parseFiles).toLens()

    fun list(): List<S3Key> = aws(Request(GET, "/")).run {
        if (status.successful) listFiles(this) else throw S3Error(status)
    }

    operator fun get(file: S3Key): InputStream? = aws(Request(GET, "/${file.value}")).run {
        when {
            status.successful -> body.stream
            status == Status.NOT_FOUND -> null
            else -> throw S3Error(status)
        }
    }

    operator fun set(file: S3Key, content: InputStream) {
        aws(Request(PUT, "/${file.value}").body(content)).run {
            if (!status.successful) throw S3Error(status)
        }
    }

    fun delete(file: S3Key) {
        aws(Request(DELETE, "/${file.value}")).run {
            if (!status.successful) throw S3Error(status)
        }
    }

    companion object {
        private fun SetBucketHost(uri: Uri) = Filter { next ->
            { next(it.uri(it.uri.scheme(uri.scheme).host(uri.host).port(uri.port))) }
        }

        fun configured(config: Configuration, http: HttpHandler) =
                S3(SetBucketHost(Uri.of("https://${config[AWS_BUCKET]}.s3.amazonaws.com"))
                        .then(ClientFilters.AwsAuth(config[S3_CREDENTIAL_SCOPE], config[AWS_CREDENTIALS]))
                        .then(http))
    }
}