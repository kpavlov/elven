package me.kpavlov.elven.ai

import dev.langchain4j.mcp.McpToolProvider
import dev.langchain4j.mcp.client.DefaultMcpClient
import dev.langchain4j.mcp.client.transport.stdio.StdioMcpTransport
import me.kpavlov.elven.utils.Secrets
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

object McpTools {
    private val timeTransport =
        StdioMcpTransport
            .Builder()
            .command(
                listOf("docker", "run", "--rm", "-i", "mcp/time"),
            ).logEvents(true)
            .build()

    private val timeMcpClient =
        DefaultMcpClient
            .Builder()
            .transport(timeTransport)
            .toolExecutionTimeout(1.seconds.toJavaDuration())
            .toolExecutionTimeoutErrorMessage(
                "There was a timeout executing calling Time API ",
            ).build()

    private val gmapsTransport =
        StdioMcpTransport
            .Builder()
            .environment(
                mapOf(
                    "GOOGLE_MAPS_API_KEY" to Secrets.get("GOOGLE_MAPS_API_KEY"),
                ),
            ).command(
                listOf("docker", "run", "--rm", "-e", "GOOGLE_MAPS_API_KEY", "-i", "mcp/google-maps"),
            ).logEvents(true)
            .build()

    private val gmapsMcpClient =
        DefaultMcpClient
            .Builder()
            .transport(gmapsTransport)
            .toolExecutionTimeout(5.seconds.toJavaDuration())
            .toolExecutionTimeoutErrorMessage(
                "There was a timeout executing calling Google Maps API ",
            ).build()

    val toolProvider =
        McpToolProvider
            .builder()
            .mcpClients(timeMcpClient, gmapsMcpClient)
            .build()
}
