package http4kbox.endpoints

import http4kbox.FileStorage
import org.http4k.contract.contract
import org.http4k.contract.openapi.ApiInfo
import org.http4k.contract.openapi.v3.OpenApi3
import org.http4k.contract.security.ApiKeySecurity
import org.http4k.contract.ui.swagger.swaggerUiWebjar
import org.http4k.lens.Header
import org.http4k.routing.routes

fun Api(apiKey: String, fs: FileStorage) = routes(
    swaggerUiWebjar {
        pageTitle = "http4kbox API"
        url = "/api/openapi.json"
    },
    contract {
        descriptionPath = "/openapi.json"
        security = ApiKeySecurity(Header.required("http4k-api-key"), { it == apiKey })
        renderer = OpenApi3(ApiInfo("http4kbox", "0.0"))
        routes += ListFiles(fs)
    }
)