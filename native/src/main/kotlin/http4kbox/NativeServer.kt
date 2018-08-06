package http4kbox

import io.github.konfigur8.Property
import org.http4k.client.JavaHttpClient
import org.http4k.core.Filter
import org.http4k.core.then
import org.http4k.server.ApacheServer
import org.http4k.server.asServer

// GraalVm does not currently support https, so we need to use a http->https proxy to switch protocol
val PROXY_HOST = Property.string("PROXY_HOST")
val HTTP_PORT = Property.int("HTTP_PORT")

val ConvertToHttp = Filter { next -> { next(it.uri(it.uri.copy(scheme = "http"))) } }

fun main(args: Array<String>) {
    val config = Settings.defaults.requiring(HTTP_PORT).requiring(PROXY_HOST).reify()

    System.setProperty("http.proxyHost", config[PROXY_HOST])

    val useLocalProxyForSsl = ConvertToHttp.then(JavaHttpClient())

    Http4kBox(config, useLocalProxyForSsl).asServer(ApacheServer(config[HTTP_PORT])).start().block()
}