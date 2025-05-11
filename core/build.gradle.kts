import org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_22

val enableGraalNative: String by project
val graalHelperVersion: String by project

dependencies {

    api(libs.gdx)
    api(libs.kotlin.stdlib)
    api(libs.kotlinx.coroutines.core)
    api(libs.ktx.actors)
    api(libs.ktx.app)
    api(libs.ktx.assets.async)
    api(libs.ktx.freetype.async)
    implementation(project.dependencies.platform(libs.langchain4j.bom))
    api(libs.ktx.log)
    api(libs.ktx.style)
    api(libs.gdx.tools)
    api(libs.ktx.scene2d)
    api(libs.langchain4j)
    api(libs.langchain4j.openai)
    api(libs.langchain4j.easy.rag)
    api(libs.langchain4j.mcp)
    implementation(libs.langchain4j.kotlin)

    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlinx.coroutines.test)

    if (enableGraalNative == "true") {
        implementation(libs.gdx.svmhelper.annotations)
    }
}

kotlin {
    compilerOptions {
        javaParameters = true
        jvmTarget = JVM_22
        progressiveMode = true
    }
}
