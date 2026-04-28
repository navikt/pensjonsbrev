package no.nav.pensjon.brev.maler.legacy

import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class HjemmelFormatterTest {

    @Test
    fun ingenting() {
        assertEquals(
            "",
            HjemmelFormatter(true).apply(listOf(), Language.Bokmal)
        )
    }

    @Test
    fun `bare en hjemmel`() {
        assertEquals(
            "§ 9-1",
            HjemmelFormatter(true).apply(listOf("9-1"), Language.Bokmal)
        )
    }

    @Test
    fun `hjemler listes opp med komma`() {
        assertEquals(
            "§§ 9-1, 9-3, 9-5, 9-7, 10-1, 11-1",
            HjemmelFormatter(false).apply(listOf("9-1", "9-3", "9-5", "9-7", "10-1", "11-1"), Language.Bokmal)
        )
    }

    @Test
    fun `hjemler listes opp med og til slutt`() {
        assertEquals(
            "§§ 9-1, 9-3, 9-5, 9-7, 10-1 og 11-1",
            HjemmelFormatter(true).apply(listOf("9-1", "9-3", "9-5", "9-7", "10-1", "11-1"), Language.Bokmal)
        )
    }

    @Test
    fun `hjemler slås sammen`() {
        assertEquals(
            "§§ 9-1 til 9-3, 9-5 til 9-6, 10-1 til 10-2 og 11-1",
            HjemmelFormatter(true).apply(listOf("9-1", "9-2", "9-3", "9-5", "9-6", "10-1", "10-2", "11-1"), Language.Bokmal)
        )
    }

    @Test
    fun `hjemler sorteres`() {
        assertEquals(
            "§§ 9-1 til 9-3, 9-5 til 9-6, 10-1 til 10-2 og 11-1",
            HjemmelFormatter(true).apply(listOf("10-1", "10-2", "11-1", "9-1", "9-2", "9-3", "9-5", "9-6"), Language.Bokmal)
        )
    }
}