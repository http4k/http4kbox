package http4kbox

import org.http4k.config.Environment.Companion.ENV
import org.http4k.config.EnvironmentKey
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.lens.int
import org.http4k.server.ApacheServer
import org.http4k.server.asServer

val HTTP_PORT = EnvironmentKey.int().required("HTTP_PORT")

fun main() {
    val handler = { req: Request -> Response(Status.OK).body("Hello, world!") }
    handler.asServer(ApacheServer(HTTP_PORT(ENV))).start().block()
}