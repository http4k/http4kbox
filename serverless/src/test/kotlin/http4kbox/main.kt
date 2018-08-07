
import http4kbox.Http4kboxLambda
import org.http4k.base64Encode
import org.http4k.client.JavaHttpClient
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.server.SunHttp
import org.http4k.server.asServer
import org.http4k.serverless.BootstrapAppLoader.HTTP4K_BOOTSTRAP_CLASS
import org.http4k.serverless.lambda.ApiGatewayProxyRequest
import org.http4k.serverless.lambda.LambdaFunction

val basicAuth = "http4kbox:http4kbox"

val settings = mapOf(
        "CREDENTIALS" to basicAuth,
        "AWS_CREDENTIALS" to (System.getenv("AWS_CREDENTIALS") ?: "ACCESS_KEY:SECRET_KEY"),
        "S3_REGION" to "us-east-1",
        "AWS_BUCKET" to "http4kbox"
)

fun main(args: Array<String>) {

    // Launching your Lambda Function locally - by simply providing the operating ENVIRONMENT map as would
    // be configured on AWS.
    fun runLambdaLocally() {
        val app = Http4kboxLambda(settings)
        val localLambda = app.asServer(SunHttp(8000)).start()

        println(JavaHttpClient()(Request(GET, "http://localhost:8000/")))
        localLambda.stop()
    }

    // the following code is purely here for demonstration purposes, to explain exactly what is happening at AWS.
    fun runLambdaAsAwsWould() {
        System.setProperties(settings.toProperties())
        val lambda = LambdaFunction(mapOf(HTTP4K_BOOTSTRAP_CLASS to Http4kboxLambda::class.java.name))
        val response = lambda.handle(ApiGatewayProxyRequest().apply {
            path = "/"
            httpMethod = "GET"
            headers = mapOf("Authorization" to basicAuth.base64Encode())
            queryStringParameters = mapOf()
        })
        println(response.statusCode)
        println(response.headers)
        println(response.body)
    }

    runLambdaLocally()
    runLambdaAsAwsWould()
}