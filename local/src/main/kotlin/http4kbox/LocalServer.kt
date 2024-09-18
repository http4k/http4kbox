package http4kbox

import org.http4k.client.JavaHttpClient
import org.http4k.config.Environment
import org.http4k.config.EnvironmentKey
import org.http4k.lens.int
import org.http4k.server.Undertow
import org.http4k.server.asServer

val HTTP_PORT = EnvironmentKey.int().defaulted("HTTP_PORT", 8000)

fun main() {
    val env = Environment.fromResource("http4kbox.properties")
    Http4kBox(env, JavaHttpClient()).asServer(Undertow(HTTP_PORT(env))).start().block()
}
