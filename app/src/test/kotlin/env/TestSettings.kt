package env

import http4kbox.Settings.AWS_BUCKET
import http4kbox.Settings.AWS_CREDENTIALS
import http4kbox.Settings.S3_CREDENTIAL_SCOPE
import org.http4k.aws.AwsCredentialScope
import org.http4k.aws.AwsCredentials
import org.http4k.cloudnative.env.Environment

val TestSettings = Environment.defaults(
    AWS_CREDENTIALS of AwsCredentials("key", "secretKey"),
    AWS_BUCKET of "mybucket",
    S3_CREDENTIAL_SCOPE of AwsCredentialScope("us-east-1", "s3")
)