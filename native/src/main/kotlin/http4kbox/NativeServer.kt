package http4kbox

import http4kbox.Settings.AWS_BUCKET
import http4kbox.Settings.AWS_CREDENTIALS
import http4kbox.Settings.S3_CREDENTIAL_SCOPE
import org.http4k.aws.AwsCredentialScope
import org.http4k.client.JavaHttpClient
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.filter.ClientFilters.SetHostFrom
import org.http4k.server.ApacheServer
import org.http4k.server.asServer

fun main(args: Array<String>) {
    val port = if (args.isNotEmpty()) args[0].toInt() else 10000

    val defaults = Settings.defaults
            .withProp(AWS_BUCKET, args[1])
            .withProp(AWS_CREDENTIALS, args[2].toAwsCredentials())
            .withProp(S3_CREDENTIAL_SCOPE, AwsCredentialScope(args[3], "S3"))

    // GraalVm does not currently support https, so we use a local Docker to port-forward
    val useLocalProxyForSsl = SetHostFrom(Uri.of("http://localhost:${args[4]}/")).then(JavaHttpClient())

    Http4kBox(defaults.reify(), useLocalProxyForSsl).asServer(ApacheServer(port)).start().block()
}