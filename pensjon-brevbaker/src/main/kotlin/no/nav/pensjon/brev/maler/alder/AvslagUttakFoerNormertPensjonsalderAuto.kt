package no.nav.pensjon.brev.maler.alder

import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAutoDto
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.afpBruktIBeregning
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.dinPensjonsutbetaling
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.minstePensjonssats
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.normertPensjonsalder
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.totalPensjonMedAFP
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.uttaksgrad
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.virkFom
import no.nav.pensjon.brev.maler.fraser.alderspensjon.NormertPensjonsalderFormatering
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Brevtype.VEDTAKSBREV

@TemplateModelHelpers
object AvslagUttakFoerNormertPensjonsalderAuto : AutobrevTemplate<AvslagUttakFoerNormertPensjonsalderAutoDto> {

    override val kode = Pesysbrevkoder.AutoBrev.PE_AP_AVSLAG_UTTAK_FOER_NORMERT_PENSJONSALDER_AUTO

    override val template = createTemplate(
        name = kode.name,
        letterDataType = AvslagUttakFoerNormertPensjonsalderAutoDto::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Nav har avslått søknaden din om alderspensjon før normert pensjonsalder",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = VEDTAKSBREV,
        )
    ) {
        title {
            text(
                Bokmal to "Nav har avslått søknaden din om alderspensjon før normert pensjonsalder",
                Nynorsk to "",
                English to ""
            )
        }

        outline {
            title2 {
                text(
                    Bokmal to "Vedtak",
                    Nynorsk to "",
                    English to ""
                )
            }
            paragraph {
                textExpr(
                    Bokmal to "Du har for lav pensjonsopptjening til at du kan ta ut ".expr() + uttaksgrad.format()+ " prosent pensjon fra " + virkFom.format() + ". Derfor har vi avslått søknaden din.",
                    Nynorsk to "".expr(),
                    English to "".expr()
                )
            }
            paragraph {
                text(
                    Bokmal to "Vedtaket er gjort etter folketrygdloven § 20-15.",
                    Nynorsk to "",
                    English to ""
                )
            }

            title2 {
                text(
                    Bokmal to "Slik har vi beregnet",
                    Nynorsk to "",
                    English to ""
                )
            }
            paragraph {
                showIf(uttaksgrad.equalTo(100)) {
                list {
                    item {
                        textExpr(
                            Bokmal to "For å kunne ta ut alderspensjon før du fyller normert pensjonsalder, må pensjonen din minst utgjøre ".expr() + minstePensjonssats.format() + " kroner i året.",
                            Nynorsk to "".expr(),
                            English to "".expr()
                        )
                    }
                    item {

                        textExpr(
                            Bokmal to "Dersom du hadde tatt ut ".expr() + uttaksgrad.format() + " prosent alderspensjon fra " + virkFom.format() + " ville du fått ".expr() + totalPensjonMedAFP.format() + " kroner årlig i pensjon. ",
                            Nynorsk to "".expr(),
                            English to "".expr()
                        )
                        showIf(afpBruktIBeregning) {
                            text(
                                Bokmal to "I denne beregningen har vi inkludert AFP.",
                                Nynorsk to "",
                                English to "")
                        }
                    }
                }
                }.orShow {
                    list {
                        item {
                            textExpr(
                                Bokmal to "For å kunne ta ut alderspensjon før du fyller normert pensjonsalder, må pensjonen din minst utgjøre ".expr() + minstePensjonssats.format() + " kroner i året." +
                                        "Vi beregner den delen du ønsker å ta ut nå og hva du ville ha fått hvis du tar resten av pensjonen ved normert pensjonsalder.",
                                Nynorsk to "".expr(),
                                English to "".expr()
                            )
                            includePhrase(NormertPensjonsalderFormatering(normertPensjonsalder))
                        }

                        item {
                            textExpr(
                                Bokmal to "Hvis du hadde tatt ut ".expr() + uttaksgrad.format() + " prosent alderspensjon fra " + virkFom.format() + " ville du fått ".expr() + dinPensjonsutbetaling.format() +
                                        " kroner årlig i full pensjon ved normert pensjonsalder. ",
                                Nynorsk to "".expr(),
                                English to "".expr()
                            )

                            showIf(afpBruktIBeregning) {
                                text(
                                    Bokmal to "I denne beregningen har vi inkludert AFP.",
                                    Nynorsk to "",
                                    English to "")
                            }
                        }
                    }
                }

            }

            paragraph {
                text(
                    Bokmal to "Beregningen er uavhengig av din faktisk sivilstand.",
                    Nynorsk to "",
                    English to ""
                )
            }
            title2 {
                text(
                    Bokmal to "Du kan fremdeles ha mulighet til å ta ut alderspensjon før du når normert pensjonsalder",
                    Nynorsk to "",
                    English to ""
                )
            }
            paragraph {
                text(
                    Bokmal to "Selv om vi har avslått denne søknaden, kan du likevel ha rett til å ta ut alderspensjon før du når normert pensjonsalder. " +
                            "Da må du kunne velge en lavere uttaksgrad eller ta ut pensjonen senere. " +
                            "I Din pensjon på nav.no/dinpensjon kan du sjekke når du tidligst kan ta ut alderspensjon. " +
                            "Du kan også se hva pensjonen din blir, avhengig av når og hvor mye du tar ut.",
                    Nynorsk to "",
                    English to ""
                )
            }

            paragraph {
                text(
                    Bokmal to "Du må sende oss en ny søknad når du ønsker å ta ut alderspensjon. En eventuell endring kan tidligst skje måneden etter at vi har mottatt søknaden.",
                    Nynorsk to "",
                    English to ""
                )
            }

            title2 {
                text(
                    Bokmal to "Du har rett til å klage ",
                    Nynorsk to "",
                    English to ""
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvis du mener vedtaket er feil, kan du klage innen seks uker fra den datoen du mottok vedtaket. Klagen skal være skriftlig. Du finner skjema og informasjon på nav.no/klage. " +
                            "I vedlegget får du vite mer om hvordan du går fram.",
                    Nynorsk to "",
                    English to ""
                )
            }

            title2 {
                text(
                    Bokmal to "Du har rett til innsyn",
                    Nynorsk to "",
                    English to ""
                )
            }
            paragraph {
                text(
                    Bokmal to "Du har rett til å se dokumentene i saken din. I vedlegget får du vite hvordan du går fram.",
                    Nynorsk to "",
                    English to ""
                )
            }


            includePhrase(Felles.HarDuSpoersmaal.alder)
        }
    }
}
