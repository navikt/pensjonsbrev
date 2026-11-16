package no.nav.pensjon.brev.alder.maler.adhoc

import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat
import no.nav.brev.brevbaker.FellesFactory
import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.pensjon.brev.alder.Fixtures
import no.nav.pensjon.brev.template.Language.*

class FeilUtsendingAvGjenlevenderettAuthoringTest {
    @Test
    fun `source identity and all languages are preserved`() {
        assertThat(FeilUtsendingAvGjenlevenderett.kode.kode()).isEqualTo("PE_ADHOC_2024_FEIL_INFOBREV_AP_SENDT_BRUKER")
        assertThat(FeilUtsendingAvGjenlevenderett.template.language.all()).hasSize(3)
        val data = Fixtures.create(FeilUtsendingAvGjenlevenderett::class)
        LetterTestImpl(FeilUtsendingAvGjenlevenderett.template, data, Bokmal, FellesFactory.fellesAuto).renderTestHtml("FeilUtsendingAvGjenlevenderett_authoring_NB")
        LetterTestImpl(FeilUtsendingAvGjenlevenderett.template, data, Nynorsk, FellesFactory.fellesAuto).renderTestHtml("FeilUtsendingAvGjenlevenderett_authoring_NN")
        LetterTestImpl(FeilUtsendingAvGjenlevenderett.template, data, English, FellesFactory.fellesAuto).renderTestHtml("FeilUtsendingAvGjenlevenderett_authoring_EN")
    }
}
