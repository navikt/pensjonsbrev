package no.nav.pensjon.brev.maler.ufore.fraser

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.EmptyAutobrevdata
import no.nav.pensjon.brev.template.ExpressionScope
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.expression.expr
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.time.LocalDate

class ErUforetidspunktMaanedEtterFoedselTest {
    private val scope = ExpressionScope(EmptyAutobrevdata, Fixtures.felles, Language.Bokmal)

    private fun evaluer(uforetidspunkt: LocalDate, foedselsdato: LocalDate): Boolean =
        erUforetidspunktMaanedEtterFoedsel(uforetidspunkt.expr(), foedselsdato.expr()).eval(scope)

    @ParameterizedTest(name = "fødsel {1}, uføretidspunkt {0} → true")
    @CsvSource(
        "2000-06-01, 2000-05-15",
        "2000-06-30, 2000-05-15",
        "2000-06-01, 2000-05-01",
        "2001-01-01, 2000-12-15",
        "2000-02-01, 2000-01-31",
        "2001-03-01, 2001-02-28",
    )
    fun `er true naar uforetidspunkt er i maaneden etter foedsel`(uforetidspunkt: LocalDate, foedselsdato: LocalDate) {
        assertEquals(true, evaluer(uforetidspunkt, foedselsdato))
    }

    @ParameterizedTest(name = "fødsel {1}, uføretidspunkt {0} → false")
    @CsvSource(
        "2000-05-20, 2000-05-15",
        "2000-07-01, 2000-05-15",
        "2000-04-30, 2000-05-15",
        "2001-06-01, 2000-05-15",
        "2000-01-01, 2000-12-15",
        "2002-01-01, 2000-12-15",
    )
    fun `er false naar uforetidspunkt ikke er i maaneden etter foedsel`(
        uforetidspunkt: LocalDate,
        foedselsdato: LocalDate,
    ) {
        assertEquals(false, evaluer(uforetidspunkt, foedselsdato))
    }
}
