package env

import http4kbox.Settings
import http4kbox.Settings.AWS_BUCKET
import http4kbox.Settings.AWS_CREDENTIALS
import http4kbox.Settings.S3_CREDENTIAL_SCOPE
import org.http4k.aws.AwsCredentialScope
import org.http4k.aws.AwsCredentials

val TestSettings = Settings.defaults
        .withProp(AWS_BUCKET, "mybucket")
        .withProp(AWS_CREDENTIALS, AwsCredentials("key", "secretKey"))
        .withProp(S3_CREDENTIAL_SCOPE, AwsCredentialScope("us-east-1", "S3"))
        .reify()
