package http4kbox.endpoints

import http4kbox.FileName
import http4kbox.FileStorage
import org.http4k.core.ContentType.Companion.OCTET_STREAM
import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with
import org.http4k.lens.Header.CONTENT_TYPE
import org.http4k.lens.Path
import org.http4k.lens.value

fun Get(fs: FileStorage): HttpHandler = {
    fs[fileName(it)]?.let { Response(OK).with(CONTENT_TYPE of OCTET_STREAM).body(it) } ?: Response(NOT_FOUND)
}

private val fileName = Path.value(FileName).of("id")
