package env

import http4kbox.Http4kBox
import http4kbox.Settings.AWS_BUCKET
import http4kbox.Settings.AWS_CREDENTIALS
import http4kbox.Settings.S3_CREDENTIAL_SCOPE
import org.http4k.aws.AwsCredentialScope
import org.http4k.aws.AwsCredentials
import org.http4k.client.JavaHttpClient
import org.http4k.cloudnative.env.Environment
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.filter.ClientFilters.SetHostFrom
import org.http4k.server.SunHttp
import org.http4k.server.asServer

fun main() {
    FakeS3().asServer(SunHttp(9000)).start()

    Http4kBox(
        Environment.defaults(
            AWS_CREDENTIALS of AwsCredentials("accessKey", "secretKey"),
            AWS_BUCKET of "localhost",
            S3_CREDENTIAL_SCOPE of AwsCredentialScope("us-east-5", "s3")
        ),
        SetHostFrom(Uri.of("http://localhost:9000")).then(JavaHttpClient())
    ).asServer(SunHttp(8000)).start().block()
}