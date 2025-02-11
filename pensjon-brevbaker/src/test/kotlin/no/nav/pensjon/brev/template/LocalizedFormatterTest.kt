package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.template.LocalizedFormatter.MonthYearFormatter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDate

class LocalizedFormatterTest {
    @Test
    fun `test formatting in English locale`() {
        val date = LocalDate.of(2023, 1, 15)
        val formattedDate = MonthYearFormatter.apply(date, Language.English)
        assertEquals("January 2023", formattedDate)
    }

    @Test
    fun `test formatting in Norwegian Bokmal locale`() {
        val date = LocalDate.of(2023, 1, 15)
        val formattedDate = MonthYearFormatter.apply(date, Language.Bokmal)
        assertEquals("januar 2023", formattedDate)
    }

    @Test
    fun `test formatting in Norwegian Nynorsk locale`() {
        val date = LocalDate.of(2023, 1, 15)
        val formattedDate = MonthYearFormatter.apply(date, Language.Nynorsk)
        assertEquals("januar 2023", formattedDate)
    }
}
