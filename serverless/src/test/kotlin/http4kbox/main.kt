
import http4kbox.Http4kboxLambda
import org.http4k.client.JavaHttpClient
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.server.SunHttp
import org.http4k.server.asServer
import org.http4k.serverless.BootstrapAppLoader
import org.http4k.serverless.lambda.ApiGatewayProxyRequest
import org.http4k.serverless.lambda.LambdaFunction

fun main(args: Array<String>) {

    // Launching your Lambda Function locally - by simply providing the operating ENVIRONMENT map as would
    // be configured on AWS.
    fun runLambdaLocally() {
        val app = Http4kboxLambda(mapOf())
        val localLambda = app.asServer(SunHttp(8000)).start()

        println(JavaHttpClient()(Request(GET, "http://localhost:8000/")))
        localLambda.stop()
    }

    // the following code is purely here for demonstration purposes, to explain exactly what is happening at AWS.
    fun runLambdaAsAwsWould() {
        val lambda = LambdaFunction(mapOf(BootstrapAppLoader.HTTP4K_BOOTSTRAP_CLASS to Http4kboxLambda::class.java.name))
        val response = lambda.handle(ApiGatewayProxyRequest().apply {
            path = "/"
            body = "hello hello hello, i suppose this isn't 140 characters anymore.."
            httpMethod = "GET"
            headers = mapOf()
            queryStringParameters = mapOf()
        })
        println(response.statusCode)
        println(response.headers)
        println(response.body)
    }

    runLambdaLocally()
    runLambdaAsAwsWould()

}