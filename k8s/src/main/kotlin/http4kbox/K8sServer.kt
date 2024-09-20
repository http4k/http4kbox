package http4kbox

import org.http4k.client.JavaHttpClient
import org.http4k.cloudnative.asK8sServer
import org.http4k.config.Environment
import org.http4k.server.Jetty
import java.time.Clock

fun main() {
    Http4kBox(Environment.ENV, JavaHttpClient(), Clock.systemUTC()).asK8sServer(::Jetty).start().block()
}
