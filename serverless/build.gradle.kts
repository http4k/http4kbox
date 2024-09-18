import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

application {
    mainClass.set("http4kbox.LambdaLoader")
}

tasks.named<ShadowJar>("shadowJar") {
    archiveBaseName.set("serverless")
    archiveClassifier.set(null as String?)
    archiveVersion.set(null as String?)
    mergeServiceFiles()
}

dependencies {
    implementation(project(":app"))
    implementation("org.http4k:http4k-serverless-lambda")
    implementation("com.amazonaws:aws-lambda-java-events:3.14.0")

    // testImplementation("io.github.s4nchez:deployer:0.1-SNAPSHOT") // Uncomment if needed for deployment tasks
}

tasks.register<JavaExec>("deployLambda") {
    mainClass.set("io.github.s4nchez.DeployLambdaKt")
    environment("DEPLOYMENTSPEC_MODULE", project.name)
    environment("DEPLOYMENTSPEC_LAMBDAFILE", "build/libs/serverless.jar")
    environment("DEPLOYMENTSPEC_LAMBDATIMEOUT", "20")
    environment("DEPLOYMENTSPEC_HANDLERFUNCTION", "org.http4k.serverless.lambda.LambdaFunction")
    environment("HTTP4K_BOOTSTRAP_CLASS", "http4kbox.Http4kboxLambda")
    environment("CREDENTIALS", "http4kbox:http4kbox")

    classpath(sourceSets["main"].resources)
    classpath(sourceSets["test"].compileClasspath)
    dependsOn("build")
}

tasks.register<JavaExec>("deployApi") {
    mainClass.set("io.github.s4nchez.DeployApiKt")
    environment("DEPLOYMENTSPEC_MODULE", project.name)

    classpath(sourceSets["main"].resources)
    classpath(sourceSets["test"].compileClasspath)
}