package http4kbox

import org.http4k.aws.AwsCredentialScope
import org.http4k.aws.AwsCredentials
import org.http4k.config.EnvironmentKey
import org.http4k.connect.amazon.s3.model.BucketName
import org.http4k.lens.value

object Settings {
    val AWS_CREDENTIALS =
        EnvironmentKey.map(String::toAwsCredentials, AwsCredentials::fromCredentials).required("AWS_CREDENTIALS")
    val AWS_BUCKET = EnvironmentKey.value(BucketName).defaulted("AWS_BUCKET", BucketName.of("http4kbox"))
    val S3_CREDENTIAL_SCOPE =
        EnvironmentKey.map({ AwsCredentialScope(it, "s3") }, AwsCredentialScope::region).required("S3_REGION")
}

fun AwsCredentials.fromCredentials() = "$accessKey:$secretKey"
fun String.toAwsCredentials() = split(":").run { AwsCredentials(get(0), get(1)) }