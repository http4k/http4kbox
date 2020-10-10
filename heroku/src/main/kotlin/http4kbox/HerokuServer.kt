package http4kbox

import org.http4k.client.Java8HttpClient
import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.EnvironmentKey
import org.http4k.core.Credentials
import org.http4k.core.then
import org.http4k.filter.ServerFilters.BasicAuth
import org.http4k.server.ApacheServer
import org.http4k.server.asServer

// since we are running in a public environment, add credentials to the app
val BASIC_AUTH_CREDENTIALS = EnvironmentKey.map(String::toCredentials).required("BASIC_AUTH_CREDENTIALS")

fun main(args: Array<String>) {
    val env = Environment.ENV

    val port = if (args.isNotEmpty()) args[0].toInt() else 5000

    BasicAuth("http4k", BASIC_AUTH_CREDENTIALS(env))
        .then(Http4kBox(env, Java8HttpClient()))
        .asServer(ApacheServer(port)).start().block()
}

private fun String.toCredentials() = split(":").run { Credentials(get(0), get(1)) }