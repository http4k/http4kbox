package http4kbox.endpoints

import http4kbox.FileStorage
import org.http4k.contract.contract
import org.http4k.contract.openapi.ApiInfo
import org.http4k.contract.openapi.v3.OpenApi3
import org.http4k.contract.security.ApiKeySecurity
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with
import org.http4k.lens.BiDiBodyLens
import org.http4k.lens.Header
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.template.ViewModel

fun Api(apiKey: String, fs: FileStorage, htmlBody: BiDiBodyLens<ViewModel>) = routes(
    contract {
        descriptionPath = "/openapi.json"
        security = ApiKeySecurity(Header.required("http4k-api-key"), { it == apiKey })
        renderer = OpenApi3(ApiInfo("http4kbox", "0.0"))
        routes += ListFiles(fs)
    },
    "/" bind GET to { _: Request -> Response(OK).with(htmlBody of OpenAPI().apply { title("http4kbox")}) }
)
