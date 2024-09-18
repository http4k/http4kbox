package http4kbox

import http4kbox.endpoints.Delete
import http4kbox.endpoints.Get
import http4kbox.endpoints.Index
import http4kbox.endpoints.Upload
import org.http4k.config.Environment
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.then
import org.http4k.filter.ServerFilters.CatchAll
import org.http4k.filter.debug
import org.http4k.routing.bind
import org.http4k.routing.routes

fun Http4kBox(env: Environment, s3Http: HttpHandler): HttpHandler {
    val fs = FileStorage(env, s3Http.debug())
    return CatchAll().then(
        routes(
            "/{id}/delete" bind POST to Delete(fs),
            "/{id}" bind GET to Get(fs),
            "/" bind routes(POST to Upload(fs), GET to Index(fs))
        )
    )
}