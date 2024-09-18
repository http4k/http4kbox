package env

import http4kbox.Settings.AWS_BUCKET
import http4kbox.Settings.AWS_CREDENTIALS
import http4kbox.Settings.S3_CREDENTIAL_SCOPE
import org.http4k.aws.AwsCredentialScope
import org.http4k.aws.AwsCredentials
import org.http4k.config.Environment
import org.http4k.connect.amazon.AWS_REGION
import org.http4k.connect.amazon.core.model.Region
import org.http4k.connect.amazon.s3.model.BucketName

val TestSettings = Environment.defaults(
    AWS_CREDENTIALS of AwsCredentials("key", "secretKey"),
    AWS_BUCKET of BucketName.of("mybucket"),
    S3_CREDENTIAL_SCOPE of AwsCredentialScope(Region.US_EAST_1.toString(), "s3"),
    AWS_REGION of Region.US_EAST_1
)