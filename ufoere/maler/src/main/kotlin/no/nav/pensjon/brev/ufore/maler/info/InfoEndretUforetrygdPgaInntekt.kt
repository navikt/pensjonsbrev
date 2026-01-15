package no.nav.pensjon.brev.ufore.maler.info

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.lessThan
import no.nav.pensjon.brev.template.dsl.expression.lessThanOrEqual
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder
import no.nav.pensjon.brev.ufore.api.model.maler.info.InfoEndretUTPgaInntektDto
import no.nav.pensjon.brev.ufore.api.model.maler.info.InfoEndretUTPgaInntektDtoSelectors.LoependeInntektsAvkortningSelectors.forventetInntektAar
import no.nav.pensjon.brev.ufore.api.model.maler.info.InfoEndretUTPgaInntektDtoSelectors.LoependeInntektsAvkortningSelectors.inntektsgrenseAar
import no.nav.pensjon.brev.ufore.api.model.maler.info.InfoEndretUTPgaInntektDtoSelectors.UforetrygdSelectors.ufoeregrad
import no.nav.pensjon.brev.ufore.api.model.maler.info.InfoEndretUTPgaInntektDtoSelectors.UforetrygdSelectors.utbetalingsgrad
import no.nav.pensjon.brev.ufore.api.model.maler.info.InfoEndretUTPgaInntektDtoSelectors.loependeInntektsAvkortning
import no.nav.pensjon.brev.ufore.api.model.maler.info.InfoEndretUTPgaInntektDtoSelectors.uforetrygdInnevarendeAr
import no.nav.pensjon.brev.ufore.maler.fraser.Constants.NAV_URL
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brev.ufore.maler.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VIKTIG

@TemplateModelHelpers
object InfoEndretUforetrygdPgaInntekt : AutobrevTemplate<InfoEndretUTPgaInntektDto> {
    override val kode = Ufoerebrevkoder.AutoBrev.UT_INFO_ENDRET_PGA_INNTEKT

    override val template = createTemplate(
        letterDataType = InfoEndretUTPgaInntektDto::class, languages = languages(Language.Bokmal), letterMetadata = LetterMetadata(
            displayTitle = "Endring av uføretrygd på grunn av inntekt",
            distribusjonstype = VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV
        )
    ) {


        val forventetInntekt = loependeInntektsAvkortning.forventetInntektAar
        val inntektsgrense = loependeInntektsAvkortning.inntektsgrenseAar

        val utbetalinggradErLikUfoeregrad = uforetrygdInnevarendeAr.utbetalingsgrad.equalTo(uforetrygdInnevarendeAr.ufoeregrad)

        val ikkeAvkortet = utbetalinggradErLikUfoeregrad and forventetInntekt.lessThanOrEqual(inntektsgrense)
        val avkortet = uforetrygdInnevarendeAr.utbetalingsgrad.lessThan(uforetrygdInnevarendeAr.ufoeregrad) or (utbetalinggradErLikUfoeregrad and forventetInntekt.greaterThan(inntektsgrense))

        title {
            text(
                bokmal { +"Endring av uføretrygd på grunn av inntekt" })
        }

        outline {
            paragraph {
                text(
                    bokmal { +"Nav har mottatt opplysninger om inntekten din." })
            }

            paragraph {
                text(
                    bokmal {
                        +"Opplysninger fra arbeidsgiver viser at den årlige arbeidsinntekten din kan bli høyere enn inntekten vi brukte til å beregne utbetalingen av uføretrygden din."
                    })
            }

            showIf(ikkeAvkortet) {
                paragraph {
                    text(
                        bokmal {
                            +"Dersom inntekten din i år overstiger " + inntektsgrense.format() + " skal utbetalingen av uføretrygden reduseres. Det lønner seg likevel å jobbe, fordi inntekt og uføretrygd vil være høyere enn uføretrygd alene. Selv om vi reduserer uføretrygden din, beholder du uføregraden du har fått innvilget."
                        })
                }
            }.orShowIf(avkortet) {
                paragraph {
                    text(
                        bokmal {
                            +"Dersom inntekten din i år overstiger " + forventetInntekt.format() + " skal utbetalingen av uføretrygden reduseres. Det lønner seg likevel å jobbe, fordi inntekt og uføretrygd vil være høyere enn uføretrygd alene. Selv om vi reduserer uføretrygden din, beholder du uføregraden du har fått innvilget."
                        })
                }
            }

            title2 {
                text(
                    bokmal { +"Dette kan du gjøre" }
                )
            }

            paragraph {
                text(
                    bokmal { +"Vi sender deg dette brevet slik at du så tidlig som mulig kan melde fra om eventuell ny årlig forventet inntekt." }
                )
            }

            showIf(ikkeAvkortet) {
                paragraph {
                    text(
                        bokmal { +"Forventer du at inntekten din i år ikke overstiger " + inntektsgrense.format() + ", trenger du ikke gjøre noe. " }
                    )
                }

                paragraph {
                    text(
                        bokmal { +"Dersom du forventer at inntekten i år overstiger " + inntektsgrense.format() + ", må du melde fra om den nye inntekten din. Dette kan du gjøre under menyvalget «Uføretrygd» når du logger deg inn på ${NAV_URL}. Det er viktig at du melder fra om ny årlig inntekt slik at du får riktig utbetaling av uføretrygd." }
                    )
                }
            }.orShowIf(avkortet) {
                paragraph {
                    text(
                        bokmal { +"Forventer du at inntekten din i år ikke overstiger " + forventetInntekt.format() + ", trenger du ikke gjøre noe. " }
                    )
                }

                paragraph {
                    text(
                        bokmal { +"Dersom du forventer at inntekten i år overstiger " + forventetInntekt.format() + ", må du melde fra om den nye inntekten din. Dette kan du gjøre under menyvalget «Uføretrygd» når du logger deg inn på ${NAV_URL}. Det er viktig at du melder fra om ny årlig inntekt slik at du får riktig utbetaling av uføretrygd." }
                    )
                }
            }

            paragraph {
                text(
                    bokmal { +"Dersom du har barnetillegg, vær oppmerksom på at det er egne grenser for når barnetillegget blir redusert ut fra inntekt." }
                )
            }

            title2 {
                text(
                    bokmal { +"Dette kan Nav gjøre" }
                )
            }

            showIf(ikkeAvkortet) {
                paragraph {
                    text(
                        bokmal {
                            +"Dersom opplysninger fra arbeidsgiver på et senere tidspunkt viser at inntekten din har oversteget " + inntektsgrense.format() + ", reduserer vi uføretrygden. Dette kan imidlertid være for sent til at du får riktig årlig utbetaling av uføretrygden din."
                        }
                    )
                }
            }.orShowIf(avkortet) {
                paragraph {
                    text(
                        bokmal {
                            +"Dersom opplysninger fra arbeidsgiver på et senere tidspunkt viser at inntekten din har oversteget " + forventetInntekt.format() + ", reduserer vi uføretrygden. Dette kan imidlertid være for sent til at du får riktig årlig utbetaling av uføretrygden din."
                        }
                    )
                }
            }

            paragraph {
                text(
                    bokmal { +"Du vil motta vedtaksbrev fra Nav dersom vi justerer uføretrygden din på grunn av endret inntekt." }
                )
            }

            paragraph {
                text(
                    bokmal { +"Dersom du har fått for lite eller for mye utbetalt i uføretrygd, foretar vi et etteroppgjør. Dette gjør vi på høsten når skatteoppgjøret for året før er klart." }
                )
            }

            includePhrase(Felles.RettTilAKlageKort)
            includePhrase(Felles.RettTilInnsynRefVedlegg)
            includePhrase(Felles.HarDuSporsmal)
        }

        includeAttachment(vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk)
    }
}