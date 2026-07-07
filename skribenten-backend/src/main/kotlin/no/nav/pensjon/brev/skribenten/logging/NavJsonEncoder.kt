package no.nav.pensjon.brev.skribenten.logging

import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.classic.spi.IThrowableProxy
import ch.qos.logback.core.encoder.EncoderBase
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

/**
 * Logback encoder that produces logstash-logback-encoder-compatible JSON without a Jackson dependency.
 *
 * Output format matches LogstashEncoder defaults so that existing Kibana index mappings work without changes.
 * Fields: @timestamp, @version, message, logger_name, thread_name, level, level_value, MDC fields (root), stack_trace.
 */
import java.time.format.DateTimeFormatterBuilder

class NavJsonEncoder : EncoderBase<ILoggingEvent>() {
    private val formatter = DateTimeFormatterBuilder()
        .append(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        .appendOffset("+HH:MM", "+00:00")
        .toFormatter()
        .withZone(ZoneOffset.UTC)

    override fun headerBytes(): ByteArray = ByteArray(0)
    override fun footerBytes(): ByteArray = ByteArray(0)

    override fun encode(event: ILoggingEvent): ByteArray {
        val sb = StringBuilder("{")
        var first = true

        fun strField(key: String, value: String) {
            if (!first) sb.append(',')
            first = false
            sb.append('"').append(escapeJson(key)).append("\":\"").append(escapeJson(value)).append('"')
        }

        fun intField(key: String, value: Int) {
            if (!first) sb.append(',')
            first = false
            sb.append('"').append(escapeJson(key)).append("\":").append(value)
        }

        strField("@timestamp", formatter.format(Instant.ofEpochMilli(event.timeStamp)))
        strField("@version", "1")
        strField("message", event.formattedMessage ?: "")
        strField("logger_name", event.loggerName)
        strField("thread_name", event.threadName)
        strField("level", event.level.levelStr)
        intField("level_value", event.level.toInt())

        event.mdcPropertyMap?.forEach { (k, v) ->
            if (v != null) strField(k, v)
        }

        event.throwableProxy?.let { proxy ->
            strField("stack_trace", formatThrowable(proxy))
        }

        sb.append("}\n")
        return sb.toString().toByteArray(Charsets.UTF_8)
    }

    private fun escapeJson(s: String): String = buildString {
        for (c in s) {
            when (c) {
                '"' -> append("\\\"")
                '\\' -> append("\\\\")
                '\n' -> append("\\n")
                '\r' -> append("\\r")
                '\t' -> append("\\t")
                '\b' -> append("\\b")
                '\u000C' -> append("\\f")
                else -> if (c.code < 0x20) append("\\u%04x".format(c.code)) else append(c)
            }
        }
    }

    private fun formatThrowable(proxy: IThrowableProxy): String = buildString {
        append(proxy.className)
        if (proxy.message != null) append(": ").append(proxy.message)
        proxy.stackTraceElementProxyArray?.forEach { step ->
            append("\n\tat ").append(step.steAsString)
        }
        proxy.suppressed?.forEach { suppressed ->
            append("\n\tSuppressed: ").append(formatThrowable(suppressed))
        }
        proxy.cause?.let { cause ->
            append("\nCaused by: ").append(formatThrowable(cause))
        }
    }
}
