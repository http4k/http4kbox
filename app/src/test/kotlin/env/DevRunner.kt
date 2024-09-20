package env

import http4kbox.Http4kBox
import http4kbox.Settings.AWS_BUCKET
import http4kbox.Settings.OAUTH_BASE_URI
import http4kbox.Settings.OAUTH_CALLBACK_URI
import org.http4k.client.JavaHttpClient
import org.http4k.connect.amazon.AWS_REGION
import org.http4k.connect.amazon.s3.FakeS3
import org.http4k.connect.amazon.s3.createBucket
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.core.with
import org.http4k.filter.ClientFilters.SetBaseUriFrom
import org.http4k.routing.reverseProxy
import org.http4k.server.SunHttp
import org.http4k.server.asServer
import java.time.Clock

fun main() {
    val clock = Clock.systemDefaultZone()

    val testSettings = TestSettings.with(
        OAUTH_BASE_URI of Uri.of("http://localhost:10000"),
        OAUTH_CALLBACK_URI of Uri.of("http://localhost:8000/oauth/callback"),
    )

    val s3 = FakeS3()
        .apply { s3Client().createBucket(testSettings[AWS_BUCKET], testSettings[AWS_REGION]) }
        .asServer(SunHttp(9000)).start()

    val github = FakeGitHub(clock).asServer(SunHttp(10000)).start()

    val http = reverseProxy(
        "localhost" to SetBaseUriFrom(Uri.of("http://localhost:${github.port()}")).then(JavaHttpClient()),
        "s3" to SetBaseUriFrom(Uri.of("http://localhost:${s3.port()}")).then(JavaHttpClient()),
    )

    Http4kBox(testSettings, http, clock)
        .asServer(SunHttp(8000))
        .start()
}