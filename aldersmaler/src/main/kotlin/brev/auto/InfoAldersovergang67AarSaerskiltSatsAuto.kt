package brev.auto

import brev.felles.HarDuSpoersmaal
import no.nav.pensjon.brev.api.model.maler.Aldersbrevkoder
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

// MF_000239
@TemplateModelHelpers
object InfoFyller67AarSaerskiltSats : AutobrevTemplate<EmptyBrevdata> {
    override val kode = Aldersbrevkoder.AutoBrev.INFO_FYLLER_67_AAR_SAERSKILT_SATS

    override val template =
        createTemplate(
            languages = languages(Language.Bokmal),
            letterMetadata =
                LetterMetadata(
                    displayTitle = "Informasjon til deg som fyller 67 år",
                    isSensitiv = false,
                    distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
                    brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
                ),
        ) {
            title {
                text(bokmal { +"Informasjon til deg som fyller 67 år" })
            }

            outline {
                paragraph {
                    text(bokmal { +"Er inntekten til ektefellen/partneren/samboeren din lavere enn folketrygdens grunnbeløp (G)?" })
                    newline()
                    text(bokmal { +"Da kan du ha rett til høyere alderspensjon." })
                }
                paragraph {
                    text(bokmal { +"Hvis ektefellen/partneren/samboeren har en inntekt lavere enn 1 G og ikke har rett til full alderspensjon, kan du ha rett til minste pensjonsnivå etter særskilt sats." })
                }

                title2 {
                    text(bokmal { +"Dette er grunnen til at vi skriver til deg:" })
                }
                paragraph {
                    text(bokmal { +"Alderspensjonen du får utbetalt i dag er beregnet ut ifra at ektefellen/partneren/samboeren din har en inntekt som er lavere enn to ganger folketrygdens grunnbeløp (2 G)." })
                }
                paragraph {
                    text(bokmal { +"Hvis denne inntekten også er under 1 G, må du dokumentere dette." })
                }
                paragraph {
                    text(bokmal { +"Du kan lese mer om grunnbeløp på nav.no/grunnbeløp." })
                }

                title2 {
                    text(bokmal { +"Dette må du gjøre:" })
                }
                paragraph {
                    text(bokmal { +"Du må sende oss dokumentasjon på all inntekt. Med inntekt menes:" })
                    newline()
                    list {
                        item {
                            text(bokmal { +"arbeidsinntekt i Norge, og eventuelt andre land" })
                        }
                        item {
                            text(bokmal { +"inntekt fra andre private og offentlige pensjonsordninger" })
                        }
                        item {
                            text(bokmal { +"pensjoner fra andre land" })
                        }
                        item {
                            text(bokmal { +"ytelser fra Nav, blant annet sykepenger og arbeidsavklaringspenger (APP)" })
                        }
                        item {
                            text(bokmal { +"kapitalinntekt" })
                        }
                        item {
                            text(bokmal { +"livrente" })
                        }
                    }
                }
                paragraph {
                    text(bokmal { +"Som dokumentasjon kan du sende kopi av skatteoppgjøret for siste år. Vi godtar også bekreftelse fra regnskapsfører, årsoppgave fra bank eller kopier av lønns- og trekkoppgaver." })
                }
                paragraph {
                    text(bokmal { +"Har ektefellen/partneren/samboeren din fylt 62 år og bodd og/eller arbeidet i utlandet? Da må du sende oss disse opplysningene:" })
                    newline()
                    list {
                        item {
                            text(bokmal { +"hvilke land ektefellen/partneren/samboeren din bodde og/eller arbeidet i" })
                        }
                        item {
                            text(bokmal { +"hvilke perioder dette var" })
                        }
                    }
                }
                paragraph {
                    text(bokmal { +"Husk å legge ved både ditt og din ektefelle/partner/samboer sitt navn og fødselsnummer." })
                }
                paragraph {
                    text(bokmal { +"Du bør sende inn dokumentasjonen innen 14 dager fra du mottar dette brevet, til:" })
                    newline()
                    text(bokmal { +"Nav Familie- og pensjonsytelser" })
                    newline()
                    text(bokmal { +"PB 6600 Etterstad" })
                    newline()
                    text(bokmal { +"0607 OSLO" })
                }
                paragraph {
                    text(bokmal { +"Alderspensjonen din blir vurdert på nytt etter vi har mottatt dokumentasjonen." })
                }

                includePhrase(HarDuSpoersmaal())
            }
        }
}