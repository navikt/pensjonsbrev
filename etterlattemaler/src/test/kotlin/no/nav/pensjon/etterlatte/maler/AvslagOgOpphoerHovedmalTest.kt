package no.nav.pensjon.etterlatte.maler

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.Fixtures
import no.nav.pensjon.etterlatte.fixtures.createBarnepensjonAvslagDTO
import no.nav.pensjon.etterlatte.fixtures.createBarnepensjonOpphoerDTO
import no.nav.pensjon.etterlatte.fixtures.createOmstillingsstoenadAvslagDTO
import no.nav.pensjon.etterlatte.fixtures.createOmstillingsstoenadOpphoerDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.avslag.BarnepensjonAvslag
import no.nav.pensjon.etterlatte.maler.barnepensjon.opphoer.BarnepensjonOpphoer
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.avslag.OmstillingsstoenadAvslag
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.opphoer.OmstillingsstoenadOpphoer
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.INTEGRATION_TEST)
internal class AvslagOgOpphoerHovedmalTest {

    private val languages = listOf(Language.Bokmal, Language.Nynorsk, Language.English)

    @Test
    fun `avslag og opphoer for BP og OMS render as HTML`() {
        languages.forEach { language ->
            LetterTestImpl(BarnepensjonAvslag.template, createBarnepensjonAvslagDTO(), language, Fixtures.felles)
                .renderTestHtml(filnavn(EtterlatteBrevKode.BARNEPENSJON_AVSLAG, language))
            LetterTestImpl(BarnepensjonOpphoer.template, createBarnepensjonOpphoerDTO(), language, Fixtures.felles)
                .renderTestHtml(filnavn(EtterlatteBrevKode.BARNEPENSJON_OPPHOER, language))
            LetterTestImpl(OmstillingsstoenadAvslag.template, createOmstillingsstoenadAvslagDTO(), language, Fixtures.felles)
                .renderTestHtml(filnavn(EtterlatteBrevKode.OMSTILLINGSSTOENAD_AVSLAG, language))
            LetterTestImpl(OmstillingsstoenadOpphoer.template, createOmstillingsstoenadOpphoerDTO(), language, Fixtures.felles)
                .renderTestHtml(filnavn(EtterlatteBrevKode.OMSTILLINGSSTOENAD_OPPHOER, language))
        }
    }

    private fun filnavn(kode: EtterlatteBrevKode, language: Language) =
        "${kode.name}_hovedmal_${language.javaClass.simpleName}"
}
