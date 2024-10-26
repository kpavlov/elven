val enableGraalNative: String by project
val graalHelperVersion: String by project

dependencies {

    api(libs.gdx)
    api(libs.ktx.app)
    api(libs.ktx.log)
    api(libs.ktx.actors)
    api(libs.ktx.scene2d)
    api(libs.kotlin.stdlib)
    api(libs.langchain4j)
    api(libs.langchain4j.openai)
    api(libs.kotlinx.coroutines.core)

    if (enableGraalNative == "true") {
        implementation(libs.gdx.svmhelper.annotations)
    }
}
