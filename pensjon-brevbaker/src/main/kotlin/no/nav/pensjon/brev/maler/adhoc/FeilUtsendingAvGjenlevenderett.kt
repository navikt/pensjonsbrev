package no.nav.pensjon.brev.maler.adhoc

import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object FeilUtsendingAvGjenlevenderett : AutobrevTemplate<EmptyBrevdata> {

    override val kode = Brevkode.AutoBrev.PE_ADHOC_2024_FEIL_INFOBREV_AP_SENDT_BRUKER

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

            title1 {
                text(
                    Bokmal to "Har du spørsmål?",
                    Nynorsk to "Har du spørsmål?",
                    English to "Do you have questions?"
                )
            }
            paragraph {
                text(
                    Bokmal to "Du finner mer informasjon på ${Constants.ALDERSPENSJON_GJENLEVENDE_URL}.",
                    Nynorsk to "Du finn meir informasjon på ${Constants.ALDERSPENSJON_GJENLEVENDE_URL}.",
                    English to "You can find more information at ${Constants.ALDERSPENSJON_GJENLEVENDE_URL}."
                )
                text(
                    Bokmal to " På ${Constants.KONTAKT_URL} kan du chatte eller skrive til oss.",
                    Nynorsk to " På ${Constants.KONTAKT_URL} kan du chatte eller skrive til oss.",
                    English to " At ${Constants.KONTAKT_URL} you can chat or write to us."
                )
                text(
                    Bokmal to " Hvis du ikke finner svar på ${Constants.NAV_URL}, kan du ringe oss på telefon ${Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON},"
                            + " hverdager ${Constants.NAV_KONTAKTSENTER_AAPNINGSTID}.",
                    Nynorsk to " Om du ikkje finn svar på ${Constants.NAV_URL}, kan du ringe oss på telefon ${Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON},"
                            + " kvardagar ${Constants.NAV_KONTAKTSENTER_AAPNINGSTID}.",
                    English to " If you do not find the answer at ${Constants.NAV_URL}, you can call us at: +47 ${Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON},"
                            + " weekdays ${Constants.NAV_KONTAKTSENTER_AAPNINGSTID}."
                )
            }
        }
    }
}
