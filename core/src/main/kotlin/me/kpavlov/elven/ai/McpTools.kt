package me.kpavlov.elven.ai

import dev.langchain4j.mcp.McpToolProvider
import dev.langchain4j.mcp.client.DefaultMcpClient
import dev.langchain4j.mcp.client.transport.stdio.StdioMcpTransport
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import ktx.log.logger
import me.kpavlov.elven.utils.Secrets
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

object McpTools {
    private val logger = logger<McpTools>()

    /**
     * Represents the timeout duration for the initialization process within the application.
     * This value determines the maximum amount of time allowed for an operation to complete
     * during the initialization phase before a timeout occurs.
     */
    private val INIT_TIMEOUT = 15.seconds

    val toolProvider: McpToolProvider? =
        runBlocking {
            try {
                withTimeout(INIT_TIMEOUT) {
                    val timeMcpClient =
                        async {
                            val timeTransport =
                                StdioMcpTransport
                                    .Builder()
                                    .command(
                                        listOf("docker", "run", "--rm", "-i", "mcp/time"),
                                    ).logEvents(true)
                                    .build()

                            DefaultMcpClient
                                .Builder()
                                .transport(timeTransport)
                                .toolExecutionTimeout(3.seconds.toJavaDuration())
                                .toolExecutionTimeoutErrorMessage(
                                    "There was a timeout executing calling Time API ",
                                ).build()
                        }
                    val gmapsMcpClient =
                        async {
                            val gmapsTransport =
                                StdioMcpTransport
                                    .Builder()
                                    .environment(
                                        mapOf(
                                            "GOOGLE_MAPS_API_KEY" to Secrets.get("GOOGLE_MAPS_API_KEY"),
                                        ),
                                    ).command(
                                        listOf(
                                            "docker",
                                            "run",
                                            "--rm",
                                            "-e",
                                            "GOOGLE_MAPS_API_KEY",
                                            "-i",
                                            "mcp/google-maps",
                                        ),
                                    ).logEvents(true)
                                    .build()

                            DefaultMcpClient
                                .Builder()
                                .transport(gmapsTransport)
                                .toolExecutionTimeout(15.seconds.toJavaDuration())
                                .toolExecutionTimeoutErrorMessage(
                                    "There was a timeout executing calling Google Maps API ",
                                ).build()
                        }
                    McpToolProvider
                        .builder()
                        .mcpClients(timeMcpClient.await(), gmapsMcpClient.await())
                        .build()
                }
            } catch (e: TimeoutCancellationException) {
                logger.error(
                    e,
                ) {
                    """🛑Failed to initialize McpToolProvider in $INIT_TIMEOUT.
                    |Will run without MCP tools.
                    |Make sure that Docker is Up! 🐳
                    |
                    """.trimMargin()
                }
                return@runBlocking null
            }
        }
}
