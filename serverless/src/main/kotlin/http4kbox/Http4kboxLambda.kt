package http4kbox

import org.http4k.client.JavaHttpClient
import org.http4k.config.Environment
import org.http4k.config.Environment.Companion.JVM_PROPERTIES
import org.http4k.serverless.ApiGatewayV1LambdaFunction
import org.http4k.serverless.AppLoader

val Http4kboxLambda = AppLoader { env -> Http4kBox(JVM_PROPERTIES overrides Environment.from(env), JavaHttpClient()) }

object AppLambda : ApiGatewayV1LambdaFunction(Http4kboxLambda)
