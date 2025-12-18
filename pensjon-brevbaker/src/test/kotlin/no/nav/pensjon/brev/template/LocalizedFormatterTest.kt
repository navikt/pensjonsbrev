package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.template.LocalizedFormatter.MonthYearFormatter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth

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

    @Test
    fun `yearMonth-formatting er lik localdate sin yearmonth-formatting`() {
        val date = LocalDate.of(2023, Month.JANUARY, 15)
        val formattedDate = MonthYearFormatter.apply(date, Language.Nynorsk)
        assertEquals("januar 2023", formattedDate)

        val formattedDate2 = LocalizedFormatter.YearMonthFormatter.apply(YearMonth.of(2023, Month.JANUARY), Language.Nynorsk)
        assertEquals(formattedDate, formattedDate2)
    }

    @Test
    fun `månedsformatering på bokmål, nynorsk og engelsk`() {
        val date = LocalDate.of(2023, Month.JANUARY, 15)
        val formattedDateBokmal = LocalizedFormatter.MonthFormatter.apply(date.month, Language.Bokmal)
        val formattedDateNynorsk = LocalizedFormatter.MonthFormatter.apply(date.month, Language.Nynorsk)
        val formattedDateEnglish = LocalizedFormatter.MonthFormatter.apply(date.month, Language.English)

        assertEquals("januar", formattedDateBokmal)
        assertEquals("januar", formattedDateNynorsk)
        assertEquals("January", formattedDateEnglish)
    }

    @Test
    fun `månedsformatering kort format på bokmål, nynorsk og engelsk`() {
        val date = LocalDate.of(2023, Month.JANUARY, 15)
        val formattedDateBokmal = LocalizedFormatter.MonthFormatterShort.apply(date.month, Language.Bokmal)
        val formattedDateNynorsk = LocalizedFormatter.MonthFormatterShort.apply(date.month, Language.Nynorsk)
        val formattedDateEnglish = LocalizedFormatter.MonthFormatterShort.apply(date.month, Language.English)

        assertEquals("jan.", formattedDateBokmal)
        assertEquals("jan.", formattedDateNynorsk)
        assertEquals("Jan", formattedDateEnglish)
    }
}