val enableGraalNative: String by project
val graalHelperVersion: String by project

dependencies {

    api(libs.gdx)
    api(libs.ktx.app)
    api(libs.kotlin.stdlib)

    if (enableGraalNative == "true") {
        implementation(libs.gdx.svmhelper.annotations)
    }
}
