package http4kbox

import io.github.konfigur8.Property
import org.http4k.client.JavaHttpClient
import org.http4k.core.Credentials
import org.http4k.core.HttpHandler
import org.http4k.core.then
import org.http4k.filter.ServerFilters.BasicAuth
import org.http4k.serverless.AppLoader

object Http4kboxLambda : AppLoader {
    // since we are running in a public environment, add credentials to the app
    private val CREDENTIALS = Property("CREDENTIALS", String::toCredentials)

    override fun invoke(env: Map<String, String>): HttpHandler {
        System.setProperties(env.toProperties())

        val config = Settings.defaults.requiring(CREDENTIALS).reify()

        return BasicAuth("http4k", config[CREDENTIALS]).then(Http4kBox(config, JavaHttpClient()))
    }
}

private fun String.toCredentials() = split(":").run { Credentials(get(0), get(1)) }