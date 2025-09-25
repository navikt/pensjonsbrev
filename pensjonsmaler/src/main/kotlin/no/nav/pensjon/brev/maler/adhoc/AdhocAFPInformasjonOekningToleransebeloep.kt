package no.nav.pensjon.brev.maler.adhoc

import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.maler.fraser.common.Constants.AFP_OFFENTLIG_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON
import no.nav.pensjon.brev.maler.fraser.common.Constants.SEND_BESKJED_URL
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

object AdhocAFPInformasjonOekningToleransebeloep : AutobrevTemplate<EmptyBrevdata> {
    override val kode = Pesysbrevkoder.AutoBrev.PE_AFP_2024_INFO_TOLERANSEBELOP
    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Toleransebeløpet øker fra januar 2025",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV
        )
    ) {
        title {
            text(
                bokmal { + "Din AFP kan bli endret" },
                nynorsk { + "Din AFP kan bli endra" }
            )
        }

        outline {
            paragraph {
                text(
                    bokmal { + "Neste år øker grensen for hvor mye du kan tjene ved siden av avtalefestet pensjon (AFP) uten at pensjonen reduseres." },
                    nynorsk { + "Neste år aukar grensa for kor mykje du kan tene ved sida av avtalefesta pensjon (AFP) utan at pensjonen blir redusert." }
                )
            }
            title1 {
                text(
                    bokmal { + "Hva betyr dette for deg?" },
                    nynorsk { + "Kva betyr dette for deg?" }
                )
            }
            title2 {
                text(
                    bokmal { + "Du kan tjene mer uten at AFP blir redusert" },
                    nynorsk { + "Du kan tene meir utan at AFP blir redusert" }
                )
            }
            paragraph {
                text(
                    bokmal { + "Du kan ha inntekt inntil et toleransebeløp uten at AFP blir redusert. Toleransebeløpet øker fra 15 000 kroner til 32 247 kroner fra 1. januar 2025. Hvis inntekten er høyere enn toleransebeløpet, regnes hele inntekten med når Nav beregner hvor mye AFP skal reduseres." },
                    nynorsk { + "Du kan ha inntekt inntil eit toleransebeløp utan at AFP blir redusert. Toleransebeløpet aukar frå 15 000 kroner til 32 247 kroner frå 1. januar 2025. Viss inntekta er høgare enn toleransebeløpet, blir heile inntekta rekna med når Nav bereknar kor mykje AFP skal reduserast." }
                )
            }

            title2 {
                text(
                    bokmal { + "AFP kan bli omregnet" },
                    nynorsk { + "AFP kan bli rekna om" }
                )
            }
            paragraph {
                text(
                    bokmal { + "Har du oppgitt at du forventer en årlig inntekt på mellom 15 000 kroner og 32 247 kroner, blir AFP omregnet fra 1. januar. Har du oppgitt at du forventer å ha en inntekt under 15 000 kroner per år, eller over 32 247 kroner per år, regner vi ikke om din AFP." },
                    nynorsk { + "Har du oppgitt at du forventar ei årleg inntekt på mellom 15 000 kroner og 32 247 kroner, blir AFP rekna om frå 1. januar. Har du oppgitt at du forventar å ha ei inntekt under 15 000 kroner per år, eller over 32 247 kroner per år, reknar vi ikkje om din AFP." }
                )
            }
            paragraph {
                text(
                    bokmal { + "Hvis Nav omregner pensjonen din, sender vi deg et eget vedtaksbrev om dette." },
                    nynorsk { + "Viss Nav reknar om pensjonen din, sender vi deg eit eige vedtaksbrev om det." }
                )
            }
            paragraph {
                text(
                    bokmal { + "Du kan melde fra om ny forventet inntekt på $SEND_BESKJED_URL" },
                    nynorsk { + "Du kan melde frå om ny forventa inntekt på $SEND_BESKJED_URL" },
                )
            }

            title1 {
                text(
                    bokmal { + "Toleransebeløpet vil øke i takt med lønnsveksten" },
                    nynorsk { + "Toleransebeløpet vil auke i takt med lønnsveksten" }
                )
            }
            paragraph {
                text(
                    bokmal { + "Fra 1. januar 2025 er toleransebeløpet 0,26 ganger folketrygdens grunnbeløp. Toleransebeløpet vil bli justert 1. januar hvert år med grunnbeløpet som gjelder da." },
                    nynorsk { + "Frå 1. januar 2025 er toleransebeløpet 0,26 gonger grunnbeløpet i folketrygda. Toleransebeløpet vil bli justert 1. januar kvart år med grunnbeløpet som gjeld då." }
                )
            }

            includePhrase(Felles.HarDuSpoersmaal(AFP_OFFENTLIG_URL, NAV_KONTAKTSENTER_TELEFON_PENSJON))
        }
    }
}