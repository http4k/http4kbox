package http4kbox.endpoints

import http4kbox.FileStorage
import org.http4k.core.Method.DELETE
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.routing.bind
import org.http4k.routing.routes

fun web(fs: FileStorage) = routes(
    "/{id}" bind routes(GET to Get(fs), DELETE to Delete(fs)),
    "/" bind routes(GET to Index(fs), POST to Upload(fs))
)