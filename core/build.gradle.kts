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
    api(libs.ktx.log)
    api(libs.ktx.scene2d)
    api(libs.langchain4j)
    api(libs.langchain4j.openai)

//    implementation("com.badlogicgames.gdx:gdx-freetype-platform:${libs.versions.gdx}:natives-desktop")

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
