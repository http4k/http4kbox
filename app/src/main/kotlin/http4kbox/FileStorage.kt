package http4kbox

import http4kbox.Settings.AWS_BUCKET
import org.http4k.config.Environment
import org.http4k.connect.amazon.AWS_REGION
import org.http4k.connect.amazon.CredentialsProvider
import org.http4k.connect.amazon.Environment
import org.http4k.connect.amazon.s3.Http
import org.http4k.connect.amazon.s3.S3Bucket
import org.http4k.connect.amazon.s3.deleteObject
import org.http4k.connect.amazon.s3.getObject
import org.http4k.connect.amazon.s3.listObjectsV2
import org.http4k.connect.amazon.s3.model.BucketKey
import org.http4k.connect.amazon.s3.putObject
import org.http4k.connect.orThrow
import org.http4k.core.HttpHandler
import java.io.InputStream

class FileStorage(environment: Environment, http: HttpHandler) {
    private val s3 = S3Bucket.Http(
        AWS_BUCKET(environment), AWS_REGION(environment), CredentialsProvider.Environment(environment), http
    )

    operator fun get(key: FileName) = s3.getObject(BucketKey.of(key.value)).orThrow()

    operator fun set(key: FileName, content: InputStream) = s3.putObject(BucketKey.of(key.value), content).orThrow()

    fun list() = s3.listObjectsV2().orThrow().items.map { it.Key.value }.map(FileName::of)

    fun delete(key: FileName): Unit = s3.deleteObject(BucketKey.of(key.value)).orThrow() ?: Unit
}