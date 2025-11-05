package brev.aldersovergang

import brev.felles.Constants
import brev.felles.HarDuSpoersmaalAlder
import no.nav.pensjon.brev.model.alder.Aldersbrevkoder
import no.nav.pensjon.brev.model.alder.aldersovergang.InfoAldersovergangEps62AarAutoDtoSelectors.ytelse
import no.nav.pensjon.brev.model.alder.aldersovergang.InfoAldersovergangEps62AarAutoDto
import no.nav.pensjon.brev.model.alder.aldersovergang.YtelseType
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

// MF_000240
@TemplateModelHelpers
object InfoAldersovergangEps62AarAuto : AutobrevTemplate<InfoAldersovergangEps62AarAutoDto> {
    override val kode = Aldersbrevkoder.AutoBrev.INFO_EPS_62_AAR_AUTO

    override val template =
        createTemplate(
            languages = languages(Language.Bokmal),
            letterMetadata =
                LetterMetadata(
                    displayTitle = "Informasjon til deg som har ektefelle/partner/samboer som snart fyller 62 år",
                    isSensitiv = false,
                    distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
                    brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
                ),
        ) {
            title {
                text(bokmal { +"Informasjon til deg som har ektefelle/partner/samboer som snart fyller 62 år" })
            }

            outline {
                paragraph {
                    text(bokmal { +"Vi skal vurdere om du fortsatt skal få utbetalt det samme i " })
                    showIf(ytelse.equalTo(YtelseType.ALDER)) {
                        text(bokmal { +"alderspensjon" })
                    }.orShow {
                        text(bokmal { +"AFP" })
                    }
                    text(bokmal { +" som i dag." })
                }
                paragraph {
                    showIf(ytelse.equalTo(YtelseType.ALDER)) {
                        text(bokmal { +"Alderspensjonen din er beregnet med minste pensjonsnivå etter en særskilt sats fordi du forsørger ektefelle/partner/samboer som har inntekt lavere enn folketrygdens grunnbeløp." })
                    }.orShow {
                        text(bokmal { +"AFPen din er beregnet med minste pensjonsnivå etter en særskilt sats fordi du forsørger ektefelle/partner/samboer som har inntekt lavere enn folketrygdens grunnbeløp." })
                    }
                }

                title2 {
                    text(bokmal { +"Dette er grunnen til at vi skriver til deg:" })
                }
                paragraph {
                    text(bokmal { +"Ektefellen/partneren/samboeren din kan ha rett til å ta ut full alderspensjon fra folketrygden fra måneden etter fylte 62 år. Retten avhenger blant annet av trygdetid, som er tid med medlemskap i folketrygden etter fylte 16 år." })
                }
                paragraph {
                    text(bokmal { +"Hvis ektefellen/partneren/samboeren din har rett til å ta ut full alderspensjon fra måneden etter fylte 62 år, skal " })
                    showIf(ytelse.equalTo(YtelseType.ALDER)) {
                        text(bokmal { +"alderspensjon" })
                    }.orShow {
                        text(bokmal { +"AFPen" })
                    }
                    text(bokmal { +" din ikke lenger bli beregnet med denne satsen. Dette skal vi nå vurdere, og du vil få et nytt vedtak fra oss." })
                }

                title2 {
                    text(bokmal { +"Dette må du gjøre:" })
                }
                paragraph {
                    text(bokmal { +"Har ektefellen/partneren/samboeren din bodd og/eller arbeidet utenfor Norge? Da må du sende oss disse opplysningene:" })
                    newline()
                    list {
                        item {
                            text(bokmal { +"navn og fødselsnummer til ektefellen/partneren/samboeren din" })
                        }
                        item {
                            text(bokmal { +"hvilke land ektefellen/partneren/samboeren din bodde og/eller arbeidet i" })
                        }
                        item {
                            text(bokmal { +"hvilke perioder dette var" })
                        }
                    }
                }
                paragraph {
                    text(bokmal { +"Opplysningene må du sende oss innen 14 dager fra du mottar dette brevet:" })
                    newline()
                    text(bokmal { +"Nav Familie- og pensjonsytelser" })
                    newline()
                    text(bokmal { +"PB 6600 Etterstad" })
                    newline()
                    text(bokmal { +"0607 OSLO" })
                }
                paragraph {
                    text(bokmal { +"Du kan også gi oss beskjed på ${Constants.KONTAKT_URL}" })
                }
                paragraph {
                    text(bokmal { +"Hvis du ikke sender inn opplysninger, forutsetter vi at ektefellen/partneren/samboeren din har full trygdetid i Norge." })
                }

                includePhrase(HarDuSpoersmaalAlder)
            }
        }
}