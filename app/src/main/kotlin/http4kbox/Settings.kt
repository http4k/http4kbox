package http4kbox

import org.http4k.config.EnvironmentKey
import org.http4k.connect.amazon.s3.model.BucketName
import org.http4k.lens.value

object Settings {
    val AWS_BUCKET = EnvironmentKey.value(BucketName).defaulted("AWS_BUCKET", BucketName.of("http4kbox"))
}
