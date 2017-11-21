package org.http4k.example

import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Jetty
import org.http4k.server.asServer


fun main(args: Array<String>) {
    val port = if (args.isNotEmpty()) args[0].toInt() else 5000

    val app = routes("/" bind GET to { _: Request -> Response(OK).body("Hello World!") })

    app.asServer(Jetty(port)).start().block()
}
