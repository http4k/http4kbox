package http4kbox

import io.github.konfigur8.Property
import org.http4k.client.JavaHttpClient
import org.http4k.core.Credentials
import org.http4k.core.then
import org.http4k.filter.ServerFilters.BasicAuth
import org.http4k.server.Jetty
import org.http4k.server.asServer

// since we are running in a public environment, add credentials to the app
val BASIC_AUTH_CREDENTIALS = Property("BASIC_AUTH_CREDENTIALS", String::toCredentials)

fun main(args: Array<String>) {
    val config = Settings.defaults.requiring(BASIC_AUTH_CREDENTIALS).reify()

    val port = if (args.isNotEmpty()) args[0].toInt() else 5000

    BasicAuth("http4k", config[BASIC_AUTH_CREDENTIALS])
            .then(Http4kBox(config, JavaHttpClient()))
            .asServer(Jetty(port)).start().block()
}

private fun String.toCredentials() = split(":").run { Credentials(get(0), get(1)) }