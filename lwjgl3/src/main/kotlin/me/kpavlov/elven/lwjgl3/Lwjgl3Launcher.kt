package me.kpavlov.elven.lwjgl3

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import me.kpavlov.elven.Main

/** Launches the desktop (LWJGL3) application.  */
object Lwjgl3Launcher {
    @JvmStatic
    fun main(args: Array<String>) {
        if (StartupHelper.startNewJvmIfRequired()) return  // This handles macOS support and helps on Windows.

        createApplication()
    }

    private fun createApplication(): Lwjgl3Application {
        return Lwjgl3Application(Main(), defaultConfiguration)
    }

    private val defaultConfiguration: Lwjgl3ApplicationConfiguration
        get() {
            val configuration = Lwjgl3ApplicationConfiguration()
            configuration.setTitle("Elven")
            //// Vsync limits the frames per second to what your hardware can display, and helps eliminate
            //// screen tearing. This setting doesn't always work on Linux, so the line after is a safeguard.
            configuration.useVsync(true)
            //// Limits FPS to the refresh rate of the currently active monitor, plus 1 to try to match fractional
            //// refresh rates. The Vsync setting above should limit the actual FPS to match the monitor.
            configuration.setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate + 1)
            //// If you remove the above line and set Vsync to false, you can get unlimited FPS, which can be
            //// useful for testing performance, but can also be very stressful to some hardware.
            //// You may also need to configure GPU drivers to fully disable Vsync; this can cause screen tearing.
            configuration.setWindowedMode(640, 480)
            //// You can change these files; they are in lwjgl3/src/main/resources/ .
            configuration.setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png")
            return configuration
        }
}