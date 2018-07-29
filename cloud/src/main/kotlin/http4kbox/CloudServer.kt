package http4kbox

import io.github.konfigur8.Property
import org.http4k.client.JavaHttpClient
import org.http4k.core.Credentials
import org.http4k.core.then
import org.http4k.filter.ServerFilters.BasicAuth
import org.http4k.server.SunHttp
import org.http4k.server.asServer

fun main(args: Array<String>) {
    val CREDENTIALS = Property("CREDENTIALS", String::toCredentials)

    // since we are running in a public environment, add credentials to the app
    val config = Settings.defaults.requiring(CREDENTIALS).reify()

    val port = if (args.isNotEmpty()) args[0].toInt() else 5000

    BasicAuth("http4k", config[CREDENTIALS])
            .then(Http4kBox(config, JavaHttpClient()))
            .asServer(SunHttp(port)).start().block()
}

private fun String.toCredentials() = split(":").run { Credentials(get(0), get(1)) }