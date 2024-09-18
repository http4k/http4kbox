package http4kbox.endpoints

import http4kbox.FileStorage
import org.http4k.core.Body
import org.http4k.core.ContentType.Companion.TEXT_HTML
import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.template.RockerTemplates
import org.http4k.template.viewModel

fun Index(fs: FileStorage): HttpHandler {
    val htmlBody = Body.viewModel(RockerTemplates().Caching(), TEXT_HTML).toLens()
    return { Response(Status.OK).with(htmlBody of ListFiles().files(fs.list())) }
}
