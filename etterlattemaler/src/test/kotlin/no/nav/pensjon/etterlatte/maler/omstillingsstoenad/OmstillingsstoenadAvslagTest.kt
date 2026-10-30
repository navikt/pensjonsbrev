package no.nav.pensjon.etterlatte.maler.omstillingsstoenad

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.Fixtures
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.avslag.OmstillingsstoenadAvslagRedigerbartUtfall
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.avslag.OmstillingstoenadAvslagRedigerbartUtfallDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.avslag.OmstillingstoenadAvslagRedigerbartUtfallData
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.time.LocalDate

@Tag(TestTags.INTEGRATION_TEST)
internal class OmstillingsstoenadAvslagTest {

    private val languages = listOf(Language.Bokmal, Language.Nynorsk, Language.English)

    @Test
    fun `redigerbart utfall render as HTML`() {
        languages.forEach { language ->
            renderUtfall(language, "ordinaer")
            renderUtfall(language, "ordinaer_uten_doedsdato", avdoedDoedsdato = null)
            renderUtfall(language, "sluttbehandling", erSluttbehandling = true)
            renderUtfall(language, "tidligere_familiepleier", tidligereFamiliepleier = true)
        }
    }

    private fun renderUtfall(
        language: Language,
        variant: String,
        erSluttbehandling: Boolean = false,
        tidligereFamiliepleier: Boolean = false,
        avdoedDoedsdato: LocalDate? = LocalDate.of(2024, 1, 1),
    ) {
        LetterTestImpl(
            OmstillingsstoenadAvslagRedigerbartUtfall.template,
            OmstillingstoenadAvslagRedigerbartUtfallDTO(
                data = OmstillingstoenadAvslagRedigerbartUtfallData(
                    avdoedNavn = "Ola Nordmann",
                    erSluttbehandling = erSluttbehandling,
                    tidligereFamiliepleier = tidligereFamiliepleier,
                    avdoedDoedsdato = avdoedDoedsdato,
                ),
            ),
            language,
            Fixtures.felles,
        ).renderTestHtml(filnavn(EtterlatteBrevKode.OMSTILLINGSSTOENAD_AVSLAG_UTFALL, variant, language))
    }

    private fun filnavn(kode: EtterlatteBrevKode, variant: String, language: Language) =
        "${kode.name}_${variant}_${language.javaClass.simpleName}"
}
