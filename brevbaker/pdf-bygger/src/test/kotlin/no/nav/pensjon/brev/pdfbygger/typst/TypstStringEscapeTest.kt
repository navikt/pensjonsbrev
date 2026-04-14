package no.nav.pensjon.brev.pdfbygger.typst

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TypstStringEscapeTest {

    @Test
    fun `plain text is unchanged`() {
        assertEquals("hello world", "hello world".typstStringEscape())
    }

    @Test
    fun `multiple special characters are all escaped`() {
        assertEquals("\\\\\\\"\\n\\r\\t", "\\\"\n\r\t".typstStringEscape())
    }

    @Test
    fun `C0 control characters are stripped`() {
        // 0x00-0x08 are blocklisted
        val controlChars = (0x00..0x08).map { it.toChar() }.joinToString("")
        assertEquals("", controlChars.typstStringEscape())
    }

    @Test
    fun `DEL and C1 control characters are stripped`() {
        // 0x7F-0x9F are blocklisted
        val delAndC1 = (0x7F..0x9F).map { it.toChar() }.joinToString("")
        assertEquals("", delAndC1.typstStringEscape())
    }

    @Test
    fun `normal ASCII printable characters are unchanged`() {
        val printable = (0x20..0x7E).map { it.toChar() }.joinToString("")
        // Among printable ASCII, only '\' and '"' need escaping
        val expected = printable
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
        assertEquals(expected, printable.typstStringEscape())
    }

    @Test
    fun `Norwegian letters are unchanged`() {
        assertEquals("æøåÆØÅ", "æøåÆØÅ".typstStringEscape())
    }

    @Test
    fun `empty string returns empty string`() {
        assertEquals("", "".typstStringEscape())
    }

    @Test
    fun `CJK characters are stripped`() {
        // 0x2E80-0x9FFF is blocklisted (CJK block)
        assertEquals("", "\u4E2D\u6587".typstStringEscape())
    }

    @Test
    fun `private use area characters are stripped`() {
        // 0xE000-0xFAFF is blocklisted
        assertEquals("", "\uE000\uF000".typstStringEscape())
    }

    @Test
    fun `mixed content escapes correctly`() {
        assertEquals("hello\\\\world\\\"test\\n", "hello\\world\"test\n".typstStringEscape())
    }
}

