package brev.aldersovergang

import brev.felles.HarDuSpoersmaalAlder
import no.nav.pensjon.brev.api.model.maler.Aldersbrevkoder
import no.nav.pensjon.brev.api.model.maler.aldersovergang.InfoAldersovergangEps60AarAutoDto
import no.nav.pensjon.brev.api.model.maler.aldersovergang.InfoAldersovergangEps60AarAutoDtoSelectors.ytelse
import no.nav.pensjon.brev.api.model.maler.aldersovergang.Ytelse
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

// MF_000238
@TemplateModelHelpers
object InfoAldersovergangEps60AarAuto : AutobrevTemplate<InfoAldersovergangEps60AarAutoDto> {
    override val kode = Aldersbrevkoder.AutoBrev.INFO_EPS_60_AAR_AUTO

    override val template =
        createTemplate(
            languages = languages(Language.Bokmal),
            letterMetadata =
                LetterMetadata(
                    displayTitle = "Informasjon til deg som har ektefelle/partner/samboer som snart fyller 60 år",
                    isSensitiv = false,
                    distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
                    brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
                ),
        ) {
            title {
                text(bokmal { +"Informasjon til deg som har ektefelle/partner/samboer som snart fyller 60 år" })
            }

            outline {
                paragraph {
                    text(bokmal { +"Er inntekten til ektefellen/partneren/samboeren din lavere enn folketrygdens grunnbeløp (G)?" })
                }
                paragraph {
                    showIf(ytelse.equalTo(Ytelse.ALDER)) {
                        text(bokmal { +"Hvis du forsørger ektefelle/partner/samboer over 60 år som har en inntekt lavere enn 1 G, har du rett til minste pensjonsnivå etter særskilt sats. Da kan du ha rett til høyere alderspensjon." })
                    }.orShow {
                        text(bokmal { +"Hvis du forsørger ektefelle/partner/samboer over 60 år som har en inntekt lavere enn 1 G, har du rett til minste pensjonsnivå etter særskilt sats. Da kan du ha rett til høyere AFP." })
                    }
                }

                title2 {
                    text(bokmal { +"Dette er grunnen til at vi skriver til deg:" })
                }
                paragraph {
                    showIf(ytelse.equalTo(Ytelse.ALDER)) {
                        text(bokmal { +"Alderspensjonen" })
                    }.orShow {
                        text(bokmal { +"AFPen" })
                    }
                    text(bokmal { +" du får utbetalt i dag er beregnet ut ifra at ektefellen/partneren/samboeren din har en inntekt som er lavere enn to ganger folketrygdens grunnbeløp (2 G)." })
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
                    showIf(ytelse.equalTo(Ytelse.ALDER)) {
                        text(bokmal { +"Alderspensjonen" })
                    }.orShow {
                        text(bokmal { +"AFPen" })
                    }
                    text(bokmal { +" din blir vurdert på nytt etter vi har mottatt dokumentasjonen." })
                }

                includePhrase(HarDuSpoersmaalAlder())
            }
        }
}