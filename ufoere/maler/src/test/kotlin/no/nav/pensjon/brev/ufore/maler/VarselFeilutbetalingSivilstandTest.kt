package no.nav.pensjon.brev.ufore.maler

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.pensjon.brev.ufore.Fixtures
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.feilutbetaling.FeilutbetalingSpesifikkVarselDto
import no.nav.pensjon.brev.ufore.maler.feilutbetaling.varsel.VarselFeilutbetalingSivilstand12_13_2
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class VarselFeilutbetalingSivilstandTest {
    @Test
    fun testHtml() {
        LetterTestImpl(
            VarselFeilutbetalingSivilstand12_13_2.template,
            Fixtures.create<FeilutbetalingSpesifikkVarselDto>(),
            Language.Bokmal,
            Fixtures.felles
        ).renderTestHtml(VarselFeilutbetalingSivilstand12_13_2.kode.name)
    }
}