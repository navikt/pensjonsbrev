package no.nav.pensjon.brev.alder.maler.adhoc

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.alder.Fixtures
import no.nav.pensjon.brev.api.model.maler.EmptyAutobrevdata
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class AdhocGjenlevendEtter1970Test {

    @Test
    fun `testAdhocGjenlevendEtter1970 pdf`() {
        listOf(Language.Bokmal, Language.Nynorsk, Language.English).forEach {
            LetterTestImpl(
                AdhocGjenlevendEtter1970.template,
                EmptyAutobrevdata,
                it,
                Fixtures.fellesAuto,
            ).renderTestPDF("PE_ADHOC_2024_VEDTAK_GJENLEVENDETTER1970_${it}")
        }
    }
}
