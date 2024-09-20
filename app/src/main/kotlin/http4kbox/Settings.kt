package http4kbox

import org.http4k.config.EnvironmentKey
import org.http4k.connect.amazon.s3.model.BucketName
import org.http4k.core.Credentials
import org.http4k.core.Uri
import org.http4k.lens.of
import org.http4k.lens.uri
import org.http4k.lens.value

object Settings {
    val OAUTH_BASE_URI by EnvironmentKey.uri().of().defaulted(Uri.of("https://github.com"))
    val OAUTH_CLIENT by EnvironmentKey.map { it.split(":") }.map { Credentials(it[0], it[1]) }.of().required()
    val OAUTH_CALLBACK_URI by EnvironmentKey.uri().of().defaulted(Uri.of("https://http4kbox.http4k.org/oauth/callback"))
    val API_KEY by EnvironmentKey.of().required()
    val AWS_BUCKET by EnvironmentKey.value(BucketName).of().required()
}
