package env

import http4kbox.Http4kBox
import http4kbox.Settings.AWS_BUCKET
import org.http4k.client.JavaHttpClient
import org.http4k.connect.amazon.AWS_REGION
import org.http4k.connect.amazon.s3.FakeS3
import org.http4k.connect.amazon.s3.createBucket
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.filter.ClientFilters.SetHostFrom
import org.http4k.server.SunHttp
import org.http4k.server.asServer

fun main() {
    FakeS3()
        .apply { s3Client().createBucket(TestSettings[AWS_BUCKET], TestSettings[AWS_REGION]) }
        .asServer(SunHttp(9000)).start()

    Http4kBox(TestSettings, SetHostFrom(Uri.of("http://localhost:9000")).then(JavaHttpClient()))
        .asServer(SunHttp(8000))
        .start()
}