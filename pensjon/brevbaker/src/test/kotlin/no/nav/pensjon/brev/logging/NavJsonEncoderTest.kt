package no.nav.pensjon.brev.logging

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.classic.spi.IThrowableProxy
import ch.qos.logback.classic.spi.ThrowableProxy
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.slf4j.Marker
import org.slf4j.event.KeyValuePair

class NavJsonEncoderTest {
    private val encoder = NavJsonEncoder()
    private val mapper = ObjectMapper()

    private fun event(
        message: String = "test message",
        level: Level = Level.INFO,
        throwable: Throwable? = null,
        mdc: Map<String, String> = emptyMap(),
        loggerName: String = "test.logger",
        threadName: String = "test-thread",
        timestamp: Long = 1_000_000_001_234L,
    ): ILoggingEvent = object : ILoggingEvent {
        override fun getThreadName() = threadName
        override fun getLevel() = level
        override fun getMessage() = message
        override fun getArgumentArray() = null
        override fun getFormattedMessage() = message
        override fun getLoggerName() = loggerName
        override fun getLoggerContextVO() = null
        override fun getThrowableProxy(): IThrowableProxy? = throwable?.let { ThrowableProxy(it) }
        override fun getCallerData() = emptyArray<StackTraceElement>()
        override fun hasCallerData() = false
        override fun getMarkerList(): List<Marker> = emptyList()
        override fun getMDCPropertyMap() = mdc
        @Suppress("DEPRECATION") override fun getMdc() = mdc
        override fun getTimeStamp() = timestamp
        override fun getNanoseconds() = 0
        override fun getSequenceNumber() = 0L
        override fun getKeyValuePairs(): List<KeyValuePair> = emptyList()
        override fun prepareForDeferredProcessing() {}
    }

    @Suppress("UNCHECKED_CAST")
    private fun encode(event: ILoggingEvent): Map<String, Any?> =
        mapper.readValue(encoder.encode(event).toString(Charsets.UTF_8).trimEnd('\n'), Map::class.java)
            as Map<String, Any?>

    @Test
    fun `should include all standard logstash fields`() {
        val result = encode(event())
        assertThat(result.keys).contains("@timestamp", "@version", "message", "logger_name", "thread_name", "level", "level_value")
    }

    @Test
    fun `should set @version to 1`() {
        assertThat(encode(event())["@version"]).isEqualTo("1")
    }

    @Test
    fun `should format @timestamp as ISO 8601 UTC`() {
        val timestamp = encode(event())["@timestamp"] as String
        assertThat(timestamp).matches("""\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}\.\d+\+00:00""")
    }

    @Test
    fun `should set message`() {
        assertThat(encode(event(message = "hello world"))["message"]).isEqualTo("hello world")
    }

    @Test
    fun `should set logger_name from logger`() {
        assertThat(encode(event())["logger_name"]).isEqualTo("test.logger")
    }

    @Test
    fun `should set level INFO with correct level_value`() {
        val result = encode(event(level = Level.INFO))
        assertThat(result["level"]).isEqualTo("INFO")
        assertThat(result["level_value"]).isEqualTo(20000)
    }

    @Test
    fun `should set level WARN with correct level_value`() {
        val result = encode(event(level = Level.WARN))
        assertThat(result["level"]).isEqualTo("WARN")
        assertThat(result["level_value"]).isEqualTo(30000)
    }

    @Test
    fun `should set level ERROR with correct level_value`() {
        val result = encode(event(level = Level.ERROR))
        assertThat(result["level"]).isEqualTo("ERROR")
        assertThat(result["level_value"]).isEqualTo(40000)
    }

    @Test
    fun `should include MDC fields at root level`() {
        val result = encode(event(mdc = mapOf("requestId" to "abc-123", "userId" to "u42")))
        assertThat(result["requestId"]).isEqualTo("abc-123")
        assertThat(result["userId"]).isEqualTo("u42")
    }

    @Test
    fun `should not include stack_trace when no throwable`() {
        assertThat(encode(event())).doesNotContainKey("stack_trace")
    }

    @Test
    fun `should include stack_trace when throwable is present`() {
        val result = encode(event(throwable = RuntimeException("boom")))
        assertThat(result["stack_trace"] as String).contains("RuntimeException: boom")
    }

    @Test
    fun `should include cause chain in stack_trace`() {
        val cause = IllegalStateException("root cause")
        val stackTrace = encode(event(throwable = RuntimeException("wrapper", cause)))["stack_trace"] as String
        assertThat(stackTrace)
            .contains("Caused by:")
            .contains("IllegalStateException: root cause")
    }

    @Test
    fun `should include suppressed exceptions in stack_trace`() {
        val suppressed = IllegalArgumentException("suppressed problem")
        val throwable = RuntimeException("main").also { it.addSuppressed(suppressed) }
        val stackTrace = encode(event(throwable = throwable))["stack_trace"] as String
        assertThat(stackTrace)
            .contains("Suppressed:")
            .contains("IllegalArgumentException: suppressed problem")
    }

    @Test
    fun `should escape double quotes in message`() {
        assertThat(encode(event(message = """say "hello" please"""))["message"])
            .isEqualTo("""say "hello" please""")
    }

    @Test
    fun `should escape backslash in message`() {
        assertThat(encode(event(message = """path\to\file"""))["message"])
            .isEqualTo("""path\to\file""")
    }

    @Test
    fun `should escape newline in message`() {
        assertThat(encode(event(message = "line1\nline2"))["message"])
            .isEqualTo("line1\nline2")
    }

    @Test
    fun `should escape control characters in message`() {
        assertThat(encode(event(message = "before\u0001after"))["message"])
            .isEqualTo("before\u0001after")
    }

    @Test
    fun `output should end with newline`() {
        assertThat(encoder.encode(event()).toString(Charsets.UTF_8)).endsWith("\n")
    }

    @Test
    fun `headerBytes should be empty`() {
        assertThat(encoder.headerBytes()).isEmpty()
    }

    @Test
    fun `footerBytes should be empty`() {
        assertThat(encoder.footerBytes()).isEmpty()
    }
}
