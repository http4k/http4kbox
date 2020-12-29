import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import http4kbox.AppLambda
import http4kbox.Http4kboxLambda
import org.http4k.client.JavaHttpClient
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.server.SunHttp
import org.http4k.server.asServer
import java.lang.reflect.Proxy

val defaults = mapOf("S3_REGION" to "us-east-1", "AWS_BUCKET" to "http4kbox")

/**
 * Run this main with the AWS_CREDENTIALS environment variable in the format "<ACCESS_KEY>:<SECRET_KEY>"
 */
fun main() {

    // the following code is purely here for demonstration purposes, to explain exactly what is happening at AWS.
    fun runLambdaAsAwsWould() {
        defaults.forEach { System.setProperty(it.key, it.value) }

        val lambda = AppLambda
        val response = lambda.handleRequest(APIGatewayProxyRequestEvent().apply {
            path = "/"
            httpMethod = "GET"
            queryStringParameters = mapOf()
        }, proxy())
        println(response["statusCode"])
        println(response["headers"])
        println(response["body"])
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

inline fun <reified T> proxy(): T = Proxy.newProxyInstance(T::class.java.classLoader, arrayOf(T::class.java)) { _, _, _ -> TODO() } as T
