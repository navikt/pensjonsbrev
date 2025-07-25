package no.nav.pensjon.brev.maler.adhoc

import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object FeilUtsendingAvGjenlevenderett : AutobrevTemplate<EmptyBrevdata> {

    override val kode = Pesysbrevkoder.AutoBrev.PE_ADHOC_2024_FEIL_INFOBREV_AP_SENDT_BRUKER

    override val template = createTemplate(
        name = kode.name,
        letterDataType = EmptyBrevdata::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Nav har sendt deg feil brev",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.ANNET,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        title {
            text(
                Bokmal to "Nye regler for gjenlevenderett i alderspensjon",
                Nynorsk to "Nye reglar for attlevanderett i alderspensjon",
                English to "New rules for survivor's rights in retirement pension"
            )
        }
        outline {
            paragraph {
                text(
                    Bokmal to "I januar 2024 sendte Nav deg et brev om nye regler for gjenlevenderett i alderspensjonen.",
                    Nynorsk to "I januar 2024 sendte Nav deg eit brev om nye reglar for attlevenderett i alderspensjonen.",
                    English to "In January 2024, Nav sent a letter to you informing about the new rules for survivor's rights in the retirement pension.",
                )
            }
            paragraph {
                text(
                    Bokmal to "De nye reglene gjelder ikke for deg som har alderspensjon basert på egen opptjening. Du kan derfor se bort fra det brevet du fikk.",
                    Nynorsk to "Dei nye reglane gjeld ikkje for deg som har alderspensjon basert på eiga opptening. Du kan difor sjå vekk frå det brevet du fekk.",
                    English to "The new rules do not apply to you, as your current retirement pension is based on your earned pension rights. You can therefore disregard the letter you received.",
                )
            }
            paragraph {
                text(
                    Bokmal to "Vi beklager feilen!",
                    Nynorsk to "Vi orsakar feilen!",
                    English to "We apologise for the error."
                )
            }

            includePhrase(Felles.HarDuSpoersmaal(merInformasjonUrl = Constants.ALDERSPENSJON_GJENLEVENDE_URL, telefonnummer = Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON))
        }
    }
}
