package http4kbox.endpoints

import http4kbox.FileStorage
import org.http4k.contract.meta
import org.http4k.contract.openapi.OpenAPIJackson.auto
import org.http4k.core.Body
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with

fun ListFiles(fs: FileStorage) = "/list" meta {
    summary = "List all files in your http4kbox"
    returning(OK, lens to listOf("file1"))
} bindContract GET to { _: Request -> Response(OK).with(lens of fs.list().map { it.value }) }

private val lens = Body.auto<List<String>>().toLens()