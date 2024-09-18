import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import dev.forkhandles.mock4k.mock
import http4kbox.AppLambda
import http4kbox.Http4kboxLambda
import org.http4k.client.JavaHttpClient
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.format.AwsLambdaMoshi
import org.http4k.server.SunHttp
import org.http4k.server.asServer
import java.io.ByteArrayOutputStream

private val defaults = mapOf(
    "AWS_ACCESS_KEY_ID" to "dummy",
    "AWS_SECRET_ACCESS_KEY" to "dummy",
    "AWS_BUCKET" to "http4kbox",
    "AWS_REGION" to "us-east-1",
)

/**
 * Run this main with the AWS_CREDENTIALS environment variable in the format "<ACCESS_KEY>:<SECRET_KEY>"
 */
fun main() {
    // the following code is purely here for demonstration purposes, to explain exactly what is happening at AWS.
    fun runLambdaAsAwsWould() {
        defaults.forEach { System.setProperty(it.key, it.value) }

        val lambda = AppLambda
        val buffer = ByteArrayOutputStream()
        lambda.handleRequest(AwsLambdaMoshi.asInputStream(APIGatewayProxyRequestEvent().apply {
            path = "/"
            httpMethod = "GET"
            queryStringParameters = mapOf()
        }), buffer, mock())

        val response = AwsLambdaMoshi.asA<APIGatewayProxyResponseEvent>(String(buffer.toByteArray()))
        println(response.statusCode)
        println(response.headers)
        println(response.body)
    }

    // Launching your Lambda Function locally as a server - by simply providing the operating ENVIRONMENT map as would
    // be configured on AWS.
    fun runLambdaLocally() {
        val app = Http4kboxLambda(defaults)
        val localLambda = app.asServer(SunHttp(8000)).start()

        println(JavaHttpClient()(Request(GET, "http://localhost:8000/")))
        localLambda.stop()
    }
    runLambdaAsAwsWould()

    runLambdaLocally()

    Thread.currentThread().join()
}
