package env

import org.http4k.config.Environment
import org.http4k.connect.amazon.core.model.Region.Companion.US_EAST_1

val TestSettings = Environment.from(
    "AWS_ACCESS_KEY_ID" to "accessKey",
    "AWS_SECRET_ACCESS_KEY" to "secretKey",
    "AWS_REGION" to US_EAST_1.value,
    "AWS_BUCKET" to "http4kbox",
    "OAUTH_CLIENT" to "user:password",
    "API_KEY" to "http4kbox"
)