package env

import http4kbox.S3Key
import org.http4k.core.Body
import org.http4k.core.ContentType.Companion.OCTET_STREAM
import org.http4k.core.HttpHandler
import org.http4k.core.Method.DELETE
import org.http4k.core.Method.GET
import org.http4k.core.Method.PUT
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.ACCEPTED
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with
import org.http4k.lens.Path
import org.http4k.lens.binary
import org.http4k.routing.bind
import org.http4k.routing.routes

class FakeS3 : HttpHandler, MutableMap<S3Key, String> by mutableMapOf() {
    private val s3Key = Path.map(::S3Key, S3Key::value).of("key")
    private val content = Body.binary(OCTET_STREAM).toLens()

    private val app = routes(
        "/{key}" bind routes(
            GET to { req ->
                this[s3Key(req)]
                    ?.let { Response(OK).with(content of it.byteInputStream()) }
                    ?: Response(NOT_FOUND)
            },
            PUT to { req ->
                this[s3Key(req)] = req.bodyString()
                Response(ACCEPTED)
            },
            DELETE to { req ->
                this.remove(s3Key(req))?.let { Response(ACCEPTED) } ?: Response(NOT_FOUND)
            }
        ),
        "/" bind GET to {
            val files = keys.joinToString("") { """<Key>${it.value}</Key>""" }
            Response(OK).body("<List>$files</List>")
        }
    )

    override fun invoke(request: Request) = app(request)
}