import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import http4kbox.AppLambda
import http4kbox.Http4kboxLambda
import org.http4k.client.JavaHttpClient
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.format.AwsLambdaMoshi
import org.http4k.server.SunHttp
import org.http4k.server.asServer
import java.io.ByteArrayOutputStream
import java.lang.reflect.Proxy

private val defaults = mapOf(
    "AWS_CREDENTIALS" to "dummy:dummy",
    "AWS_BUCKET" to "http4kbox",
    "AWS_REGION" to "us-east-1",
    "S3_REGION" to "us-east-1"
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
        }), buffer, proxy())

        val response2 = AwsLambdaMoshi.asA<APIGatewayProxyResponseEvent>(String(buffer.toByteArray()))
        println(response2.statusCode)
        println(response2.headers)
        println(response2.body)
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

inline fun <reified T> proxy(): T =
    Proxy.newProxyInstance(T::class.java.classLoader, arrayOf(T::class.java)) { _, _, _ -> TODO() } as T
