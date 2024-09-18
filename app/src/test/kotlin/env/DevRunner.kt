package env

import http4kbox.Http4kBox
import http4kbox.Settings.AWS_BUCKET
import http4kbox.Settings.AWS_CREDENTIALS
import http4kbox.Settings.S3_CREDENTIAL_SCOPE
import org.http4k.aws.AwsCredentialScope
import org.http4k.aws.AwsCredentials
import org.http4k.client.JavaHttpClient
import org.http4k.config.Environment
import org.http4k.connect.amazon.AWS_REGION
import org.http4k.connect.amazon.core.model.Region
import org.http4k.connect.amazon.s3.FakeS3
import org.http4k.connect.amazon.s3.createBucket
import org.http4k.connect.amazon.s3.model.BucketName
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.filter.ClientFilters.SetHostFrom
import org.http4k.server.SunHttp
import org.http4k.server.asServer

private val bucketName = BucketName.of("http4kBucket")
private val region = Region.US_EAST_1

fun main() {
    FakeS3()
        .apply { s3Client().createBucket(bucketName, region) }
        .asServer(SunHttp(9000)).start()

    Http4kBox(
        Environment.defaults(
            AWS_CREDENTIALS of AwsCredentials("accessKey", "secretKey"),
            AWS_REGION of region,
            AWS_BUCKET of bucketName,
            S3_CREDENTIAL_SCOPE of AwsCredentialScope("us-east-5", "s3")
        ),
        SetHostFrom(Uri.of("http://localhost:9000")).then(JavaHttpClient())
    ).asServer(SunHttp(8000)).start().block()
}