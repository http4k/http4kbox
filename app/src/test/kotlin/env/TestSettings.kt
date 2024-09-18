package env

import http4kbox.Settings.AWS_BUCKET
import org.http4k.config.Environment
import org.http4k.connect.amazon.AWS_ACCESS_KEY_ID
import org.http4k.connect.amazon.AWS_REGION
import org.http4k.connect.amazon.AWS_SECRET_ACCESS_KEY
import org.http4k.connect.amazon.core.model.AccessKeyId
import org.http4k.connect.amazon.core.model.Region
import org.http4k.connect.amazon.core.model.SecretAccessKey
import org.http4k.connect.amazon.s3.model.BucketName

val TestSettings = Environment.defaults(
    AWS_ACCESS_KEY_ID of AccessKeyId.of("accessKey"),
    AWS_SECRET_ACCESS_KEY of SecretAccessKey.of("secretKey"),
    AWS_REGION of Region.US_EAST_1,
    AWS_BUCKET of BucketName.of("http4kbox")
)