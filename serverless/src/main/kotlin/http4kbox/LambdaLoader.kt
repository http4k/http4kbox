package http4kbox

import io.github.konfigur8.Property
import org.http4k.client.JavaHttpClient
import org.http4k.core.Credentials
import org.http4k.core.HttpHandler
import org.http4k.core.then
import org.http4k.filter.ServerFilters.BasicAuth
import org.http4k.serverless.AppLoader

object LambdaLoader : AppLoader {
    private val CREDENTIALS = Property("CREDENTIALS", String::toCredentials)

    override fun invoke(env: Map<String, String>): HttpHandler {
        // since we are running in a public environment, add credentials to the app
        val config = Settings.defaults.requiring(CREDENTIALS).reify()

        return BasicAuth("http4k", config[CREDENTIALS])
                .then(Http4kBox(config, JavaHttpClient()))
    }
}

private fun String.toCredentials() = split(":").run { Credentials(get(0), get(1)) }