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

fun main(args: Array<String>) {
    val config = Settings.defaults.requiring(HTTP_PORT).requiring(PROXY_HOST).reify()

    val httpViaProxy = Filter { next ->
        {
            next(it.header("Host", it.uri.host)
                    .uri(it.uri.copy(scheme = "http").host(config[PROXY_HOST])))
        }
    }

    val useLocalProxyForSsl = httpViaProxy.then(JavaHttpClient())

    Http4kBox(config, useLocalProxyForSsl).asServer(ApacheServer(config[HTTP_PORT])).start().block()
}