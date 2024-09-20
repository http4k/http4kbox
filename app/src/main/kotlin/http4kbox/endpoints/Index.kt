package http4kbox.endpoints

import http4kbox.FileStorage
import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with
import org.http4k.lens.BiDiBodyLens
import org.http4k.template.ViewModel

fun Index(fs: FileStorage, htmlBody: BiDiBodyLens<ViewModel>): HttpHandler =
    { Response(OK).with(htmlBody of ListFiles().files(fs.list())) }
