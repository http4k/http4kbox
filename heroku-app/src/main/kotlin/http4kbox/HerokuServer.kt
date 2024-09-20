package http4kbox

import org.http4k.client.HelidonClient
import org.http4k.config.Environment.Companion.ENV
import org.http4k.server.Helidon
import org.http4k.server.asServer
import java.time.Clock

fun main(args: Array<String>) {
    val port = if (args.isNotEmpty()) args[0].toInt() else 5000

    Http4kBox(ENV, HelidonClient(), Clock.systemUTC())
        .asServer(Helidon(port)).start().block()
}
