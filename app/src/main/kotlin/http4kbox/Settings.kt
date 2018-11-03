package http4kbox

import org.http4k.aws.AwsCredentialScope
import org.http4k.aws.AwsCredentials
import org.http4k.cloudnative.env.EnvironmentKey

object Settings {
    val AWS_CREDENTIALS = EnvironmentKey.map(String::toAwsCredentials, AwsCredentials::fromCredentials).required("AWS_CREDENTIALS")
    val AWS_BUCKET = EnvironmentKey.defaulted("AWS_BUCKET", "http4kbox")
    val S3_CREDENTIAL_SCOPE = EnvironmentKey.map({ AwsCredentialScope(it, "s3") }, AwsCredentialScope::region).required("S3_REGION")
}

fun AwsCredentials.fromCredentials() = "$accessKey:$secretKey"
fun String.toAwsCredentials() = split(":").run { AwsCredentials(get(0), get(1)) }