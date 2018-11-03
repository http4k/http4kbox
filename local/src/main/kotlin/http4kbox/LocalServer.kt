package http4kbox

import org.http4k.client.JavaHttpClient
import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.EnvironmentKey
import org.http4k.lens.int
import org.http4k.server.Undertow
import org.http4k.server.asServer

val HTTP_PORT = EnvironmentKey.int().defaulted("HTTP_PORT", 8000)

fun main(args: Array<String>) {
    val env = Environment.ENV
    Http4kBox(env, JavaHttpClient()).asServer(Undertow(HTTP_PORT(env))).start().block()
}
