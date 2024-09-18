package http4kbox

import http4kbox.endpoints.Delete
import http4kbox.endpoints.Get
import http4kbox.endpoints.Index
import http4kbox.endpoints.Upload
import org.http4k.config.Environment
import org.http4k.core.HttpHandler
import org.http4k.core.Method.DELETE
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.then
import org.http4k.filter.ServerFilters.CatchAll
import org.http4k.routing.bind
import org.http4k.routing.htmxWebjars
import org.http4k.routing.routes

fun Http4kBox(env: Environment, http: HttpHandler): HttpHandler {
    val fs = FileStorage(env, http)
    return CatchAll().then(
        routes(
            htmxWebjars(),
            "/{id}" bind routes(GET to Get(fs), DELETE to Delete(fs)),
            "/" bind routes(GET to Index(fs), POST to Upload(fs))
        )
    )
}