package http4kbox

import io.github.konfigur8.Property
import org.http4k.client.JavaHttpClient
import org.http4k.server.Undertow
import org.http4k.server.asServer

val HTTP_PORT = Property.int("HTTP_PORT")

fun main(args: Array<String>) {
    val config = Settings.defaults.withProp(HTTP_PORT, 10000).reify()
    Http4kBox(config, JavaHttpClient()).asServer(Undertow(config[HTTP_PORT])).start().block()
}