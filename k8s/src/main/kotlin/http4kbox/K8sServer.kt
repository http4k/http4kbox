package http4kbox

import org.http4k.client.JavaHttpClient
import org.http4k.cloudnative.asK8sServer
import org.http4k.cloudnative.env.Environment
import org.http4k.server.Jetty

fun main() {
    Http4kBox(Environment.ENV, JavaHttpClient()).asK8sServer(::Jetty).start().block()
}
