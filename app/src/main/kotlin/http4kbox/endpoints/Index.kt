package http4kbox.endpoints

import http4kbox.FileName
import http4kbox.FileStorage
import org.http4k.core.Body
import org.http4k.core.ContentType
import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.template.HandlebarsTemplates
import org.http4k.template.ViewModel
import org.http4k.template.viewModel

fun Index(fs: FileStorage): HttpHandler {
    val htmlBody = Body.viewModel(HandlebarsTemplates().CachingClasspath(), ContentType.TEXT_HTML).toLens()
    return { Response(Status.OK).with(htmlBody of ListFiles(fs.list())) }
}

data class ListFiles(val files: List<FileName>) : ViewModel
