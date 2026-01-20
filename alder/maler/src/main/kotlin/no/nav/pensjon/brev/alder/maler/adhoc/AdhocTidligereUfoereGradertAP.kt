package no.nav.pensjon.brev.alder.maler.adhoc

import no.nav.pensjon.brev.alder.maler.felles.Constants.DIN_PENSJON_URL
import no.nav.pensjon.brev.alder.maler.felles.Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON
import no.nav.pensjon.brev.alder.maler.felles.Constants.SKATTEETATEN_PENSJONIST_URL
import no.nav.pensjon.brev.alder.maler.felles.HarDuSpoersmaalAlder
import no.nav.pensjon.brev.alder.model.Aldersbrevkoder
import no.nav.pensjon.brev.alder.model.adhoc.AdhocTidligereUfoereGradertAPAutoDto
import no.nav.pensjon.brev.alder.model.adhoc.AdhocTidligereUfoereGradertAPAutoDtoSelectors.uttaksgrad
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object AdhocTidligereUfoereGradertAP : AutobrevTemplate<AdhocTidligereUfoereGradertAPAutoDto> {
    override val kode = Aldersbrevkoder.AutoBrev.INFO_ADHOC_TIDLIGERE_UFOERE_GRADERT_AP_AUTO
    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Tidligere uføre med gradert alderspensjon",
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        title {
            text(
                bokmal { +"Du har rett til hel (100 prosent) alderspensjon" },
                nynorsk { +"Du har rett til heil (100 prosent) alderspensjon" },
                english { +"You are entitled to a full (100 percent) retirement pension " }
            )
        }

        outline {
            paragraph {
                text(
                    bokmal { +"Du har nå gradert alderspensjon med en uttaksgrad på " + uttaksgrad.format() + " prosent." },
                    nynorsk { +"Du har no gradert alderspensjon med ein uttaksgrad på " + uttaksgrad.format() + " prosent." },
                    english { +"Your retirement pension withdrawal rate is currently " + uttaksgrad.format() + " percent." }
                )
            }

            paragraph {
                text(
                    bokmal { +"Hvis du ønsker en høyere uttaksgrad, må du melde fra til Nav. Det kan du enkelt gjøre ved å endre pensjonen på $DIN_PENSJON_URL. Hvis du ikke kan logge inn, finner du søknadsskjema for å endre pensjonen på nav.no." },
                    nynorsk { +"Om du ønskjer ein høgare uttaksgrad, må du melde frå til Nav. Det kan du enkelt gjere ved å endre pensjonen på $DIN_PENSJON_URL. Om du ikkje kan logge inn, finn du søknadsskjema for å endre pensjonen på nav.no." },
                    english { +"If you want to increase your withdrawal rate, you must notify Nav. You can change your withdrawal rate at $DIN_PENSJON_URL (service in Norwegian). If you are unable to log in, you will find the application form for changing your retirement pension at nav.no. " }
                )
            }

            paragraph {
                text(
                    bokmal { +"En endring kan tidligst skje måneden etter at vi har fått søknaden. I pensjonskalkulatoren på nav.no kan du sjekke hva pensjonen blir med ulike uttaksgrader. Hvis du trenger hjelp til beregningen eller søknaden, kan du kontakte oss på telefon $NAV_KONTAKTSENTER_TELEFON_PENSJON." },
                    nynorsk { +"Ei endring kan tidlegast skje månaden etter at vi har fått søknaden. I pensjonskalkulatoren på nav.no kan du sjekke kva pensjonen blir med ulike uttaksgradar. Om du treng hjelp til berekninga eller søknaden, kan du kontakte oss på telefon $NAV_KONTAKTSENTER_TELEFON_PENSJON." },
                    english { +"Any changes can take place no earlier than the month after we receive the application. In the pension calculator at nav.no, you can check how much your pension will be at different withdrawal rates. If you need help with the calculation or the application, you can contact us by phone at $NAV_KONTAKTSENTER_TELEFON_PENSJON." }
                )
            }

            title2 {
                text(
                    bokmal { +"Arbeidsinntekt ved siden av alderspensjonen kan gi høyere pensjon" },
                    nynorsk { +"Arbeidsinntekt ved sida av alderspensjonen kan gi høgare pensjon" },
                    english { +"Work income alongside retirement pension can increase your pension" }
                )
            }

            paragraph {
                text(
                    bokmal { +"Du kan ha arbeidsinntekt uten at pensjonen din blir redusert. Fram til og med det året du fyller 75 år, kan arbeidsinntekt gjøre at pensjonen din øker." },
                    nynorsk { +"Du kan ha arbeidsinntekt utan at pensjonen din blir redusert. Fram til og med det året du fyller 75 år, kan arbeidsinntekt gjere at pensjonen din aukar." },
                    english { +"You can have work income without your pension being reduced. Up to and including the year you turn 75, work income can increase your pension." }
                )
            }

            title2 {
                text(
                    bokmal { +"Hvis du endrer pensjonen, bør du sjekke skattekortet ditt" },
                    nynorsk { +"Om du endrar pensjonen, bør du sjekke skattekortet ditt" },
                    english { +"Please check your tax card if you change your pension" }
                )
            }

            paragraph {
                text(
                    bokmal { +"Du kan endre skattekortet og få mer informasjon på $SKATTEETATEN_PENSJONIST_URL. Nav får skattekortet elektronisk. Du skal derfor ikke sende det til oss." },
                    nynorsk { +"Du kan endre skattekortet og få meir informasjon på $SKATTEETATEN_PENSJONIST_URL. Nav får skattekortet elektronisk. Du skal derfor ikkje sende det til oss." },
                    english { +"You can change your tax card at $SKATTEETATEN_PENSJONIST_URL. Nav receives your tax card electronically. Therefore, you should not send it to us." }
                )
            }

            includePhrase(HarDuSpoersmaalAlder)
        }
    }
}