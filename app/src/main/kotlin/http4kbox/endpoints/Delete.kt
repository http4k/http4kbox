package http4kbox.endpoints

import http4kbox.FileName
import http4kbox.FileStorage
import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.lens.Path
import org.http4k.lens.value

fun Delete(fs: FileStorage): HttpHandler {
    val fileName = Path.value(FileName).of("id")
    return { fs.delete(fileName(it)).run { Response(Status.SEE_OTHER).header("location", "/") } }
}