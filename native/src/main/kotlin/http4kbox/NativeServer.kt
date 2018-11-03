package http4kbox

import org.http4k.client.JavaHttpClient
import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.EnvironmentKey
import org.http4k.core.Filter
import org.http4k.core.then
import org.http4k.lens.int
import org.http4k.server.ApacheServer
import org.http4k.server.asServer

// GraalVm does not currently support https, so we need to use a http->https proxy to switch protocol
val PROXY_HOST = EnvironmentKey.required("PROXY_HOST")
val HTTP_PORT = EnvironmentKey.int().required("HTTP_PORT")

fun main(args: Array<String>) {
//    System.setProperty("sun.net.http.allowRestrictedHeaders", "true")

    val env = Environment.ENV

    val httpViaProxy = Filter { next ->
        {
            next(it.header("Host", it.uri.host).uri(it.uri.copy(scheme = "http").host(PROXY_HOST(env))))
        }
    }

    Http4kBox(env, httpViaProxy.then(JavaHttpClient())).asServer(ApacheServer(HTTP_PORT(env))).start().block()
}