package http4kbox.endpoints

import http4kbox.FileName
import http4kbox.FileStorage
import org.http4k.core.ContentType
import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.Header
import org.http4k.lens.Path
import org.http4k.lens.value

fun Get(fs: FileStorage): HttpHandler {
    val fileName = Path.value(FileName).of("id")
    return {
        fs[fileName(it)]?.let { Response(Status.OK).with(Header.CONTENT_TYPE of ContentType.OCTET_STREAM).body(it) }
            ?: Response(
                Status.NOT_FOUND
            )
    }
}