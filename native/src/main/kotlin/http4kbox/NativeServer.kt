package http4kbox

import io.github.konfigur8.Property
import org.http4k.client.JavaHttpClient
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.filter.ClientFilters.SetHostFrom
import org.http4k.server.ApacheServer
import org.http4k.server.asServer

// GraalVm does not currently support https, so we need to use a http-https proxy to switch protocol
val PROXY_HOST = Property.string("PROXY_HOST")
val HTTP_PORT = Property.int("HTTP_PORT")

fun main(args: Array<String>) {
    val config = Settings.defaults.requiring(HTTP_PORT).requiring(PROXY_HOST).reify()

    val useLocalProxyForSsl = SetHostFrom(Uri.of("http://${config[PROXY_HOST]}")).then(JavaHttpClient())

    Http4kBox(config, useLocalProxyForSsl).asServer(ApacheServer(config[HTTP_PORT])).start().block()
}