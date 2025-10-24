package brev.aldersovergang

import brev.felles.HarDuSpoersmaal
import brev.felles.RettTilAAKlage
import brev.felles.RettTilInnsyn
import brev.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlage
import brev.vedlegg.vedleggMaanedligPensjonFoerSkattAFP
import no.nav.pensjon.brev.model.alder.Aldersbrevkoder
import no.nav.pensjon.brev.model.alder.BeloepEndring
import no.nav.pensjon.brev.model.alder.aldersovergang.AFPPrivatBeregningSelectors.totalPensjon
import no.nav.pensjon.brev.model.alder.aldersovergang.OpptjeningInfoSelectors.antallAarEndretOpptjening
import no.nav.pensjon.brev.model.alder.aldersovergang.OpptjeningInfoSelectors.endretOpptjeningsAar
import no.nav.pensjon.brev.model.alder.aldersovergang.OpptjeningInfoSelectors.sisteGyldigeOpptjeningsAar
import no.nav.pensjon.brev.model.alder.aldersovergang.OpptjeningType
import no.nav.pensjon.brev.model.alder.aldersovergang.VedtakEndringAFPEndretOpptjeningAutoDto
import no.nav.pensjon.brev.model.alder.aldersovergang.VedtakEndringAFPEndretOpptjeningAutoDtoSelectors.afpPrivatBeregningGjeldende
import no.nav.pensjon.brev.model.alder.aldersovergang.VedtakEndringAFPEndretOpptjeningAutoDtoSelectors.afpPrivateBeregningVedVirk
import no.nav.pensjon.brev.model.alder.aldersovergang.VedtakEndringAFPEndretOpptjeningAutoDtoSelectors.belopEndring
import no.nav.pensjon.brev.model.alder.aldersovergang.VedtakEndringAFPEndretOpptjeningAutoDtoSelectors.endretOpptjening
import no.nav.pensjon.brev.model.alder.aldersovergang.VedtakEndringAFPEndretOpptjeningAutoDtoSelectors.maanedligPensjonFoerSkattAFP
import no.nav.pensjon.brev.model.alder.aldersovergang.VedtakEndringAFPEndretOpptjeningAutoDtoSelectors.opptjeningType
import no.nav.pensjon.brev.model.alder.aldersovergang.VedtakEndringAFPEndretOpptjeningAutoDtoSelectors.virkFom
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

// MF_000240
@TemplateModelHelpers
object VedtakEndringAFPEndretOpptjeningAuto :
    AutobrevTemplate<VedtakEndringAFPEndretOpptjeningAutoDto> {
    override val kode = Aldersbrevkoder.AutoBrev.VEDTAK_ENDRING_AV_AFP_ENDRET_OPPTJENING_AUTO

    override val template =
        createTemplate(
            languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
            letterMetadata =
                LetterMetadata(
                    displayTitle = "Vedtak - endring av AFP fordi opptjening er endret",
                    isSensitiv = false,
                    distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
                    brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
                ),
        ) {
            title {
                text(
                    bokmal { +"Vi har beregnet din AFP i privatsektor på nytt" },
                    nynorsk { +"Vi har berekna din AFP i privat sektor på nytt" },
                    english { +"We have recalculated your contractual pension (AFP) in the private sector" },
                )
            }

            outline {
                title2 {
                    text(
                        bokmal { +"Vedtak" },
                        nynorsk { +"Vedtak" },
                        english { +"Decision" },
                    )
                }

                showIf(opptjeningType.equalTo(OpptjeningType.TILVEKST)) {
                    paragraph {
                        text(
                            bokmal {
                                +"Skatteoppgjøret for " +
                                    endretOpptjening.sisteGyldigeOpptjeningsAar.format() +
                                    " er klart. " +
                                    "Derfor har vi tatt med pensjonsopptjeningen for dette året, " +
                                    "når vi har beregnet den avtalefestede pensjonen din på nytt."
                            },
                            nynorsk {
                                +"Skatteoppgjeret for " +
                                    endretOpptjening.sisteGyldigeOpptjeningsAar.format() +
                                    " er klart. " +
                                    "Derfor har vi tatt med pensjonsoppteninga for dette året " +
                                    "og berekna den avtalefesta pensjonen din på nytt."
                            },
                            english {
                                +"The tax settlement for " +
                                    endretOpptjening.sisteGyldigeOpptjeningsAar.format() +
                                    " has been completed. " +
                                    "Therefore, we have included this year’s pension earnings " +
                                    "and recalculated your AFP."
                            },
                        )
                    }

                    paragraph {
                        text(
                            bokmal {
                                +"Fra " + virkFom.format() + " får du " + afpPrivateBeregningVedVirk.totalPensjon.format() +
                                    " kroner fær skatt i AFP."
                            },
                            nynorsk {
                                +"Frå " + virkFom.format() + " får du " + afpPrivateBeregningVedVirk.totalPensjon.format() +
                                    " kroner fær skatt i AFP."
                            },
                            english {
                                +"From " + virkFom.format() + " you will receive NOK " + afpPrivateBeregningVedVirk.totalPensjon.format() +
                                    " before tax as AFP."
                            },
                        )
                    }
                }.orShowIf(
                    opptjeningType.equalTo(OpptjeningType.KORRIGERING),
                ) {
                    showIf(endretOpptjening.antallAarEndretOpptjening.greaterThan(0)) {
                        paragraph {
                            text(
                                bokmal { +"Pensjonsopptjeningen din er endret for:" },
                                nynorsk { +"Pensjonsoppteninga di er endra for:" },
                                english { +"Your pension earnings have been changed for the following income year(-s):" },
                            )
                        }

                        paragraph {
                            list {
                                forEach(endretOpptjening.endretOpptjeningsAar) {
                                    item {
                                        eval(it.format())
                                    }
                                }
                            }
                        }
                    }

                    showIf(belopEndring.equalTo(BeloepEndring.ENDR_OKT)) {
                        paragraph {
                            text(
                                bokmal { +"Derfor har vi økt den avtalefestede pensjonen din fra " + virkFom.format() + "." },
                                nynorsk { +"Derfor har vi auka den avtalefesta pensjonen din frå " + virkFom.format() + "." },
                                english { +"Therefore, we have increased your AFP from " + virkFom.format() + "." },
                            )
                        }
                    }.orShowIf(belopEndring.equalTo(BeloepEndring.ENDR_RED)) {
                        paragraph {
                            text(
                                bokmal { +"Derfor har vi redusert den avtalefestede pensjonen din." },
                                nynorsk { +"Derfor har vi redusert den avtalefesta pensjonen din." },
                                english { +"Therefore, we have reduced your AFP." },
                            )
                        }
                    }

                    showIf(belopEndring.isOneOf(BeloepEndring.ENDR_OKT, BeloepEndring.ENDR_RED)) {
                        paragraph {
                            text(
                                bokmal {
                                    +"Fra neste måned får du " + afpPrivatBeregningGjeldende.totalPensjon.format() +
                                        " kroner før skatt."
                                },
                                nynorsk {
                                    +"Frå neste måned får du " + afpPrivatBeregningGjeldende.totalPensjon.format() + " kroner før skatt."
                                },
                                english {
                                    +"From next month you will receive NOK " + afpPrivatBeregningGjeldende.totalPensjon.format() +
                                        " before tax."
                                },
                            )
                        }
                    }
                }

                paragraph {
                    text(
                        bokmal { +"Pensjonen din utbetales innen den 20. hver måned." },
                        nynorsk { +"Pensjonen din blir betalt ut innan den 20. i kvar månad." },
                        english { +"Your pension will be paid at the latest on the 20th of each month." },
                    )
                }

                paragraph {
                    text(
                        bokmal {
                            +"Vedtaket er gjort etter § 6 i lov om statstilskott til arbeidstakere som " +
                                "tar ut avtalefestet pensjon i privat sektor (AFP-tilskottsloven)."
                        },
                        nynorsk {
                            +"Vedtaket er gjort etter § 6 i lov om statstilskott til arbeidstakere som " +
                                "tar ut avtalefestet pensjon i privat sektor (AFP-tilskottsloven)."
                        },
                        english {
                            +"This decision was made pursuant to the provisions of § 6 of the Act (No. 5 of 2010) " +
                                "on state subsidies to employees who draw contractual pension within the private sector."
                        },
                    )
                }

                // TODO - etterbetaling

                includePhrase(RettTilAAKlage(vedleggDineRettigheterOgMulighetTilAaKlage))
                includePhrase(RettTilInnsyn(vedleggDineRettigheterOgMulighetTilAaKlage))
                includePhrase(HarDuSpoersmaal.alder)
            }

            includeAttachmentIfNotNull(
                vedleggMaanedligPensjonFoerSkattAFP,
                maanedligPensjonFoerSkattAFP,
            )
        }
}
