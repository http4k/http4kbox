package http4kbox.endpoints

import http4kbox.FileName
import http4kbox.FileStorage
import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.lens.MultipartFormFile
import org.http4k.lens.Validator
import org.http4k.lens.multipartForm

fun Upload(fs: FileStorage): HttpHandler {
    val files = MultipartFormFile.multi.required("file")
    val form = Body.multipartForm(Validator.Strict, files).toLens()

    return {
        files(form(it)).forEach { fs[FileName.of(it.filename)] = it.content }
            .run { Response(Status.SEE_OTHER).header("location", "/") }
    }
}