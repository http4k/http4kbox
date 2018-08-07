package env

import http4kbox.Http4kBox
import http4kbox.Settings
import http4kbox.Settings.AWS_BUCKET
import http4kbox.Settings.AWS_CREDENTIALS
import http4kbox.Settings.S3_CREDENTIAL_SCOPE
import org.http4k.aws.AwsCredentialScope
import org.http4k.aws.AwsCredentials
import org.http4k.client.JavaHttpClient
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.filter.ClientFilters.SetHostFrom
import org.http4k.server.SunHttp
import org.http4k.server.asServer

fun main(args: Array<String>) {
    FakeS3().asServer(SunHttp(9000)).start()

    Http4kBox(
            Settings.defaults
                    .withProp(AWS_CREDENTIALS, AwsCredentials("accessKey", "secretKey"))
                    .withProp(AWS_BUCKET, "localhost")
                    .withProp(S3_CREDENTIAL_SCOPE, AwsCredentialScope("us-east-5", "s3"))
                    .reify(),
            SetHostFrom(Uri.of("http://localhost:9000")).then(JavaHttpClient())
    ).asServer(SunHttp(8000)).start().block()
}