package http4kbox

import http4kbox.Settings.API_KEY
import http4kbox.Settings.OAUTH_BASE_URI
import http4kbox.Settings.OAUTH_CALLBACK_URI
import http4kbox.Settings.OAUTH_CLIENT
import http4kbox.endpoints.Api
import http4kbox.endpoints.web
import org.http4k.config.Environment
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.then
import org.http4k.filter.ServerFilters.CatchAll
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.security.InsecureCookieBasedOAuthPersistence
import java.time.Clock

fun Http4kBox(env: Environment, http: HttpHandler, clock: Clock): HttpHandler {
    val oAuthProvider = OAuthProvider(
        OAUTH_BASE_URI(env), OAUTH_CALLBACK_URI(env),
        http,
        InsecureCookieBasedOAuthPersistence(clock.instant().toEpochMilli().toString(), clock = clock),
        OAUTH_CLIENT(env)
    )

    val fs = FileStorage(env, http)

    return CatchAll().then(
        routes(
            OAUTH_CALLBACK_URI(env).path bind GET to oAuthProvider.callback,
            "/api" bind Api(API_KEY(env), fs),
            oAuthProvider.authFilter.then(web(fs))
        )
    )
}

