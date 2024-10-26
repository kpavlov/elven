val enableGraalNative: String by project
val graalHelperVersion: String by project

dependencies {

    api(libs.gdx)
    api(libs.ktx.app)
    api(libs.ktx.actors)
    api(libs.ktx.scene2d)
    api(libs.kotlin.stdlib)

    if (enableGraalNative == "true") {
        implementation(libs.gdx.svmhelper.annotations)
    }
}
