package http4kbox.endpoints

import http4kbox.FileStorage
import org.http4k.core.Method.DELETE
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.lens.BiDiBodyLens
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.template.ViewModel

fun web(fs: FileStorage, htmlBody: BiDiBodyLens<ViewModel>) = routes(
    "/{id}" bind routes(GET to Get(fs), DELETE to Delete(fs)),
    "/" bind routes(GET to Index(fs, htmlBody), POST to Upload(fs))
)