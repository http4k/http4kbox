package http4kbox

import io.github.konfigur8.ConfigurationTemplate
import io.github.konfigur8.ExposeMode.Private
import io.github.konfigur8.Property
import org.http4k.aws.AwsCredentialScope
import org.http4k.aws.AwsCredentials

object Settings {
    val AWS_CREDENTIALS = Property("AWS_CREDENTIALS", String::toAwsCredentials, AwsCredentials::fromCredentials, Private)
    val AWS_BUCKET = Property.string("AWS_BUCKET")
    val S3_CREDENTIAL_SCOPE = Property("S3_CREDENTIAL_SCOPE", { AwsCredentialScope(it, "s3") }, AwsCredentialScope::region)

    val defaults = ConfigurationTemplate()
            .requiring(S3_CREDENTIAL_SCOPE)
            .requiring(AWS_CREDENTIALS)
            .requiring(AWS_BUCKET)
}

fun AwsCredentials.fromCredentials() = "$accessKey:$secretKey"
fun String.toAwsCredentials() = split(":").run { AwsCredentials(get(0), get(1)) }