package http4kbox

import org.http4k.client.Java8HttpClient
import org.http4k.config.Environment.Companion.ENV
import org.http4k.config.EnvironmentKey
import org.http4k.lens.int
import org.http4k.server.ApacheServer
import org.http4k.server.asServer

val HTTP_PORT = EnvironmentKey.int().required("HTTP_PORT")

fun main() {
    Http4kBox(ENV, Java8HttpClient()).asServer(ApacheServer(HTTP_PORT(ENV))).start().block()
}