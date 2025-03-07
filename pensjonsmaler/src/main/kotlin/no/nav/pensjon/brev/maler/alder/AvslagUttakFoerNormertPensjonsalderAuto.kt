package no.nav.pensjon.brev.maler.alder

import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAutoDto
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.afpBruktIBeregning
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.minstePensjonssats
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.normertPensjonsalder
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.opplysningerBruktIBeregningen
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.totalPensjon
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.virkFom
import no.nav.pensjon.brev.api.model.maler.alderApi.OpplysningerBruktIBeregningenSelectors.uttaksgrad
import no.nav.pensjon.brev.maler.adhoc.vedlegg.dineRettigheterOgMulighetTilAaKlagePensjonStatisk
import no.nav.pensjon.brev.maler.alder.vedlegg.opplysningerBruktIBeregningenAP
import no.nav.pensjon.brev.maler.fraser.alderspensjon.aarOgMaanederFormattert
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
            textExpr(
                Bokmal to "Nav har avslått søknaden din om alderspensjon fra ".expr() + virkFom.format(),
                Nynorsk to "".expr(),
                English to "".expr()
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
                    Bokmal to
                            "For å ta ut alderspensjon før du er ".expr() + normertPensjonsalder.aarOgMaanederFormattert() + ", må du ha høy nok pensjonsopptjening. " +
                            "Du har for lav pensjonsopptjening til at du kan ta ut ".expr() + opplysningerBruktIBeregningen.uttaksgrad.format() +
                            " prosent pensjon fra " + virkFom.format() + ". Derfor har vi avslått søknaden din.",
                    Nynorsk to "".expr(),
                    English to "".expr()
                )
            }
            paragraph {
                text(
                    Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 20-15 og 22-13.",
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
                showIf(opplysningerBruktIBeregningen.uttaksgrad.equalTo(100)) {
                    list {
                        item {
                            textExpr(
                                Bokmal to "For å kunne ta ut alderspensjon før du fyller ".expr() + normertPensjonsalder.aarOgMaanederFormattert() +
                                        ", må pensjonen din minst utgjøre ".expr() + minstePensjonssats.format() + " kroner i året.",
                                Nynorsk to "".expr(),
                                English to "".expr()
                            )
                        }
                        item {
                            textExpr(
                                Bokmal to "Hvis du hadde tatt ut ".expr() + opplysningerBruktIBeregningen.uttaksgrad.format() + " prosent alderspensjon fra "
                                        + virkFom.format() + ", ville du fått ".expr() + totalPensjon.format() + " kroner årlig i pensjon. ",
                                Nynorsk to "".expr(),
                                English to "".expr()
                            )
                            showIf(afpBruktIBeregning) {
                                text(
                                    Bokmal to "I denne beregningen har vi inkludert AFP.",
                                    Nynorsk to "",
                                    English to ""
                                )
                            }
                        }
                    }
                }.orShow {
                    list {
                        item {
                            textExpr(
                                Bokmal to "For å kunne ta ut alderspensjon før du fyller ".expr() + normertPensjonsalder.aarOgMaanederFormattert() +
                                        ", må pensjonen din minst være ".expr() + minstePensjonssats.format() + " kroner i året. " +
                                        "Vi beregner den delen du ønsker å ta ut nå og hva du ville ha fått hvis du tar resten av pensjonen ved ".expr() +
                                        normertPensjonsalder.aarOgMaanederFormattert() + ".",
                                Nynorsk to "".expr(),
                                English to "".expr()
                            )
                        }

                        item {
                            textExpr(
                                Bokmal to "Hvis du hadde tatt ut ".expr() + opplysningerBruktIBeregningen.uttaksgrad.format() + " prosent alderspensjon fra " + virkFom.format() +
                                        ", ville du fått ".expr() + totalPensjon.format() + " kroner årlig i full pensjon når du blir ".expr() +
                                        normertPensjonsalder.aarOgMaanederFormattert() + ". ",
                                Nynorsk to "".expr(),
                                English to "".expr()
                            )
                            showIf(afpBruktIBeregning) {
                                text(
                                    Bokmal to "I denne beregningen har vi inkludert AFP.",
                                    Nynorsk to "",
                                    English to ""
                                )
                            }
                        }
                    }
                }
            }

            paragraph {
                text(
                    Bokmal to "Beregningen er uavhengig av sivilstanden din.",
                    Nynorsk to "",
                    English to ""
                )
            }
            title2 {
                text(
                    Bokmal to "Se når du kan ta ut alderspensjon",
                    Nynorsk to "",
                    English to ""
                )
            }
            paragraph {
                textExpr(
                    Bokmal to "Selv om vi har avslått denne søknaden, kan du likevel ha rett til å ta ut alderspensjon før du fyller ".expr() +
                            normertPensjonsalder.aarOgMaanederFormattert() + ". " +
                            "Da må du kunne velge en lavere uttaksgrad eller ta ut pensjonen senere. " +
                            "På nav.no/dinpensjon kan du sjekke når du tidligst kan ta ut alderspensjon. " +
                            "Du kan også se hva pensjonen din blir, avhengig av når og hvor mye du tar ut.",
                    Nynorsk to "".expr(),
                    English to "".expr()
                )
            }

            paragraph {
                text(
                    Bokmal to "Du må sende oss en ny søknad når du ønsker å ta ut alderspensjon. " +
                            "En eventuell endring kan tidligst skje måneden etter at vi har mottatt søknaden.",
                    Nynorsk to "",
                    English to ""
                )
            }

            title2 {
                text(
                    Bokmal to "Du har rett til å klage",
                    Nynorsk to "",
                    English to ""
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvis du mener vedtaket er feil, kan du klage innen seks uker fra den datoen du mottok vedtaket. " +
                            "Klagen skal være skriftlig. Du finner skjema og informasjon på nav.no/klage. " +
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

        includeAttachment(dineRettigheterOgMulighetTilAaKlagePensjonStatisk)
        includeAttachment(opplysningerBruktIBeregningenAP, opplysningerBruktIBeregningen)
    }
}
