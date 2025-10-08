package brev.sivilstand

import brev.felles.ArbeidsinntektOgAlderspensjon
import brev.felles.HarDuSpoersmaal
import brev.felles.InformasjonOmAlderspensjon
import brev.felles.MeldeFraOmEndringer
import brev.felles.RettTilAAKlage
import brev.felles.RettTilInnsyn
import brev.felles.UfoereAlder
import brev.felles.Utbetalingsinformasjon
import brev.sivilstand.fraser.BetydningForUtbetaling
import brev.sivilstand.fraser.DuFaarAP
import brev.sivilstand.fraser.EndringYtelseEPS
import brev.sivilstand.fraser.InnvilgetYtelseEPS
import brev.sivilstand.fraser.OmregningGarantiPen
import brev.sivilstand.fraser.OpphoerYtelseEPS
import brev.sivilstand.fraser.OpphoerYtelseEPSOver2G
import brev.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlage
import brev.vedlegg.vedleggMaanedligPensjonFoerSkatt
import brev.vedlegg.vedleggOrienteringOmRettigheterOgPlikter
import no.nav.pensjon.brev.api.model.maler.Aldersbrevkoder
import no.nav.pensjon.brev.api.model.maler.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.maler.BeloepEndring
import no.nav.pensjon.brev.api.model.maler.KravArsakType
import no.nav.pensjon.brev.api.model.maler.MetaforceSivilstand
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvAlderspensjonSivilstandAutoDto
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvAlderspensjonSivilstandAutoDtoSelectors.AlderspensjonVedVirkSelectors.garantipensjonInnvilget
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvAlderspensjonSivilstandAutoDtoSelectors.AlderspensjonVedVirkSelectors.innvilgetFor67
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvAlderspensjonSivilstandAutoDtoSelectors.AlderspensjonVedVirkSelectors.minstenivaaIndividuellInnvilget
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvAlderspensjonSivilstandAutoDtoSelectors.AlderspensjonVedVirkSelectors.minstenivaaPensjonsistParInnvilget
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvAlderspensjonSivilstandAutoDtoSelectors.AlderspensjonVedVirkSelectors.pensjonstilleggInnvilget
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvAlderspensjonSivilstandAutoDtoSelectors.AlderspensjonVedVirkSelectors.saertilleggInnvilget
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvAlderspensjonSivilstandAutoDtoSelectors.AlderspensjonVedVirkSelectors.ufoereKombinertMedAlder
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvAlderspensjonSivilstandAutoDtoSelectors.AlderspensjonVedVirkSelectors.uttaksgrad
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvAlderspensjonSivilstandAutoDtoSelectors.BeregnetPensjonPerManedVedVirkSelectors.grunnpensjon
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvAlderspensjonSivilstandAutoDtoSelectors.BeregnetPensjonPerManedVedVirkSelectors.totalPensjon
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvAlderspensjonSivilstandAutoDtoSelectors.EpsVedVirkSelectors.harInntektOver2G
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvAlderspensjonSivilstandAutoDtoSelectors.alderspensjonVedVirk
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvAlderspensjonSivilstandAutoDtoSelectors.beloepEndring
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvAlderspensjonSivilstandAutoDtoSelectors.beregnetPensjonPerManedVedVirk
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvAlderspensjonSivilstandAutoDtoSelectors.epsVedVirk
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvAlderspensjonSivilstandAutoDtoSelectors.kravAarsak
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvAlderspensjonSivilstandAutoDtoSelectors.kravVirkDatoFom
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvAlderspensjonSivilstandAutoDtoSelectors.maanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvAlderspensjonSivilstandAutoDtoSelectors.maanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvAlderspensjonSivilstandAutoDtoSelectors.orienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvAlderspensjonSivilstandAutoDtoSelectors.regelverkType
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvAlderspensjonSivilstandAutoDtoSelectors.sivilstand
import no.nav.pensjon.brev.felles.Vedtak
import no.nav.pensjon.brev.maler.fraser.alderspensjon.VedtakAlderspensjon
import no.nav.pensjon.brev.maler.vedlegg.vedleggMaanedligPensjonFoerSkattAp2025
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.expression.isNotAnyOf
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object EndringAvAlderspensjonSivilstandAuto :
    AutobrevTemplate<EndringAvAlderspensjonSivilstandAutoDto> {
    override val kode = Aldersbrevkoder.AutoBrev.PE_AP_ENDRING_AV_ALDERSPENSJON_SIVILSTAND_AUTO

    override val template =
        createTemplate(
            languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
            letterMetadata =
                LetterMetadata(
                    displayTitle = "Vedtak - endring av alderspensjon (sivilstand)",
                    isSensitiv = false,
                    distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
                    brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
                ),
        ) {
            val harInntektOver2G = epsVedVirk.harInntektOver2G
            val saertilleggInnvilget = alderspensjonVedVirk.saertilleggInnvilget
            val pensjonstilleggInnvilget = alderspensjonVedVirk.pensjonstilleggInnvilget
            val minstenivaaIndividuellInnvilget =
                alderspensjonVedVirk.minstenivaaIndividuellInnvilget
            val minstenivaaPensjonsistParInnvilget =
                alderspensjonVedVirk.minstenivaaPensjonsistParInnvilget
            val garantipensjonInnvilget = alderspensjonVedVirk.garantipensjonInnvilget
            val grunnpensjon = beregnetPensjonPerManedVedVirk.grunnpensjon.ifNull(then = Kroner(0))

            title {
                text(
                    bokmal { +"Vi har beregnet alderspensjonen din på nytt fra " + kravVirkDatoFom.format() },
                    nynorsk { +"Vi har berekna alderspensjonen din på nytt frå " + kravVirkDatoFom.format() },
                    english { +"We have recalculated your retirement pension from " + kravVirkDatoFom.format() },
                )
            }
            outline {
                includePhrase(Vedtak.Overskrift)

                showIf(
                    kravAarsak.isOneOf(
                        KravArsakType.EPS_NY_YTELSE,
                        KravArsakType.EPS_NY_YTELSE_UT,
                    ),
                ) {
                    includePhrase(InnvilgetYtelseEPS(sivilstand))
                }.orShowIf(kravAarsak.isOneOf(KravArsakType.TILSTOT_ENDR_YTELSE)) {
                    includePhrase(EndringYtelseEPS(sivilstand))
                }.orShowIf(
                    kravAarsak.isOneOf(
                        KravArsakType.EPS_OPPH_YTELSE_UT,
                        KravArsakType.TILSTOT_OPPHORT,
                    ),
                ) {
                    showIf(not(harInntektOver2G)) {
                        includePhrase(OpphoerYtelseEPS(sivilstand))
                    } orShow {
                        includePhrase(OpphoerYtelseEPSOver2G(sivilstand))
                    }
                }

                showIf(
                    regelverkType.isNotAnyOf(AlderspensjonRegelverkType.AP2025)
                        or grunnpensjon.greaterThan(0),
                ) {
                    showIf(
                        (minstenivaaPensjonsistParInnvilget or minstenivaaIndividuellInnvilget)
                            and not(saertilleggInnvilget)
                            and not(pensjonstilleggInnvilget),
                    ) {
                        showIf(garantipensjonInnvilget) {
                            // omregningGP_GarantiPen_MNT
                            paragraph {
                                text(
                                    bokmal { +"Derfor har vi beregnet grunnpensjonen og garantipensjonen din på nytt." },
                                    nynorsk { +"Derfor har vi berekna grunnpensjonen og garantipensjonen din på nytt." },
                                    english { +"We have therefore recalculated your basic pension and guaranteed pension." },
                                )
                            }
                        }.orShow {
                            // omregningGP_MNT
                            paragraph {
                                text(
                                    bokmal { +"Derfor har vi beregnet grunnpensjonen og minstenivåtillegget ditt på nytt." },
                                    nynorsk { +"Derfor har vi berekna grunnpensjonen og minstenivåtillegget ditt på nytt." },
                                    english { +"We have therefore recalculated your basic pension and minimum pension supplement." },
                                )
                            }
                        }
                    }.orShowIf(
                        (minstenivaaPensjonsistParInnvilget or minstenivaaIndividuellInnvilget) and pensjonstilleggInnvilget
                            and not(saertilleggInnvilget),
                    ) {
                        // omregningGP_PenT_MNT
                        showIf(garantipensjonInnvilget) {
                            // omregningGP_PenT_Garanti_MNT
                            paragraph {
                                text(
                                    bokmal {
                                        +"Derfor har vi beregnet grunnpensjonen, pensjonstillegget," +
                                            " garantipensjonen og minstenivåtillegget ditt på nytt."
                                    },
                                    nynorsk {
                                        +"Derfor har vi berekna grunnpensjonen, pensjonstillegget," +
                                            " garantipensjonen og minstenivåtillegget ditt på nytt."
                                    },
                                    english {
                                        +"We have therefore recalculated your basic pension," +
                                            " supplementary pension, guaranteed pension and minimum pension supplement."
                                    },
                                )
                            }
                        }.orShow {
                            paragraph {
                                text(
                                    bokmal {
                                        +"Derfor har vi beregnet grunnpensjonen, pensjonstillegget" +
                                            " og minstenivåtillegget ditt på nytt."
                                    },
                                    nynorsk {
                                        +"Derfor har vi berekna grunnpensjonen, pensjonstillegget" +
                                            " og minstenivåtillegget ditt på nytt."
                                    },
                                    english {
                                        +"We have therefore recalculated your basic pension," +
                                            " supplementary pension and minimum pension supplement."
                                    },
                                )
                            }
                        }
                    }.orShowIf(
                        not(saertilleggInnvilget) and not(minstenivaaPensjonsistParInnvilget)
                            and not(minstenivaaIndividuellInnvilget) and
                            not(pensjonstilleggInnvilget),
                    ) {
                        showIf(garantipensjonInnvilget) {
                            // omregningGP_GarantiPen
                            paragraph {
                                text(
                                    bokmal { +"Derfor har vi beregnet grunnpensjonen og garantipensjonen din på nytt." },
                                    nynorsk { +"Derfor har vi berekna grunnpensjonen og garantipensjonen din på nytt." },
                                    english { +"We have therefore recalculated your basic pension and guaranteed pension." },
                                )
                            }
                        }.orShow {
                            // omregningGP
                            paragraph {
                                text(
                                    bokmal { +"Derfor har vi beregnet grunnpensjonen din på nytt." },
                                    nynorsk { +"Derfor har vi berekna grunnpensjonen din på nytt." },
                                    english { +"We have therefore recalculated your basic pension." },
                                )
                            }
                        }
                    }.orShowIf(
                        pensjonstilleggInnvilget
                            and not(saertilleggInnvilget)
                            and not(minstenivaaPensjonsistParInnvilget)
                            and not(minstenivaaIndividuellInnvilget),
                    ) {
                        showIf(garantipensjonInnvilget) {
                            // omregningGP_PenT_GarantiPen_MNT
                            paragraph {
                                text(
                                    bokmal {
                                        +"Derfor har vi beregnet grunnpensjonen, pensjonstillegget og garantipensjonen din på nytt."
                                    },
                                    nynorsk {
                                        +"Derfor har vi berekna grunnpensjonen, pensjonstillegget og garantipensjonen din på nytt."
                                    },
                                    english {
                                        +"We have therefore recalculated your basic pension," +
                                            " supplementary pension and guaranteed pension."
                                    },
                                )
                            }
                        }.orShow {
                            // omregningGP_PenT
                            paragraph {
                                text(
                                    bokmal { +"Derfor har vi beregnet grunnpensjonen og pensjonstillegget ditt på nytt." },
                                    nynorsk { +"Derfor har vi berekna grunnpensjonen og pensjonstillegget ditt på nytt." },
                                    english { +"We have therefore recalculated your basic pension and pension supplement." },
                                )
                            }
                        }
                    }
                    showIf(
                        regelverkType.isOneOf(AlderspensjonRegelverkType.AP1967) and saertilleggInnvilget,
                    ) {
                        showIf(
                            not(minstenivaaPensjonsistParInnvilget) and
                                not(minstenivaaIndividuellInnvilget),
                        ) {
                            // omregningGPST
                            paragraph {
                                text(
                                    bokmal { +"Derfor har vi beregnet grunnpensjonen og særtillegget ditt på nytt." },
                                    nynorsk { +"Derfor har vi berekna grunnpensjonen og særtillegget ditt på nytt." },
                                    english { +"We have therefore recalculated your basic pension and the special supplement." },
                                )
                            }
                        }.orShowIf((minstenivaaPensjonsistParInnvilget or minstenivaaIndividuellInnvilget)) {
                            // omregningGPSTMNT
                            paragraph {
                                text(
                                    bokmal {
                                        +"Derfor har vi beregnet grunnpensjonen, særtillegget og minstenivåtillegget ditt på nytt."
                                    },
                                    nynorsk {
                                        +"Derfor har vi berekna grunnpensjonen, særtillegget og minstenivåtillegget ditt på nytt."
                                    },
                                    english {
                                        +"We have therefore recalculated your basic pension, the" +
                                            " special supplement and the minimum level supplement."
                                    },
                                )
                            }
                        }
                    }
                }

                includePhrase(OmregningGarantiPen(regelverkType))

                includePhrase(BetydningForUtbetaling(regelverkType, beloepEndring))

                showIf(alderspensjonVedVirk.ufoereKombinertMedAlder) {
                    includePhrase(
                        UfoereAlder.DuFaar(
                            beregnetPensjonPerManedVedVirk.totalPensjon,
                            kravVirkDatoFom,
                        ),
                    )
                }.orShow {
                    includePhrase(
                        DuFaarAP(
                            kravVirkDatoFom = kravVirkDatoFom,
                            totalPensjon = beregnetPensjonPerManedVedVirk.totalPensjon,
                        ),
                    )
                }

                includePhrase(Utbetalingsinformasjon)

                showIf(regelverkType.equalTo(AlderspensjonRegelverkType.AP2025)) {
                    // hjemmelSivilstandAP2025
                    paragraph {
                        text(
                            bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 20-9, 20-17 femte avsnitt og 22-12." },
                            nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 20-9, 20-17 femte avsnitt og 22-12." },
                            english {
                                +"This decision was made pursuant to the provisions of §§ 20-9, 20-17 fifth paragraph, and 22-12 of the National Insurance Act."
                            },
                        )
                    }
                }.orShow {
                    paragraph {
                        text(
                            bokmal { +"Vedtaket er gjort etter folketrygdloven §§ " },
                            nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ " },
                            english { +"This decision was made pursuant to the provisions of §§ " },
                        )
                        showIf(sivilstand.isOneOf(MetaforceSivilstand.SAMBOER_1_5)) {
                            text(
                                bokmal { +"1-5, " },
                                nynorsk { +"1-5, " },
                                english { +"1-5, " },
                            )
                        }
                        text(
                            bokmal { +"3-2" },
                            nynorsk { +"3-2" },
                            english { +"3-2" },
                        )

                        showIf(regelverkType.isOneOf(AlderspensjonRegelverkType.AP1967) and saertilleggInnvilget) {
                            text(
                                bokmal { +", 3-3" },
                                nynorsk { +", 3-3" },
                                english { +", 3-3" },
                            )
                        }
                        showIf(
                            pensjonstilleggInnvilget or minstenivaaIndividuellInnvilget or
                                minstenivaaPensjonsistParInnvilget,
                        ) {
                            text(
                                bokmal { +", 19-8" },
                                nynorsk { +", 19-8" },
                                english { +", 19-8" },
                            )
                        }
                        showIf(pensjonstilleggInnvilget) {
                            text(
                                bokmal { +", 19-9" },
                                nynorsk { +", 19-9" },
                                english { +", 19-9" },
                            )
                        }
                        showIf(garantipensjonInnvilget) {
                            text(
                                bokmal { +", 20-9" },
                                nynorsk { +", 20-9" },
                                english { +", 20-9" },
                            )
                        }
                        text(
                            bokmal { +" og 22-12." },
                            nynorsk { +" og 22-12." },
                            english { +" and 22-12." },
                        )
                    }
                }

                showIf(beloepEndring.isOneOf(BeloepEndring.ENDR_RED, BeloepEndring.ENDR_OKT)) {
                    includePhrase(VedtakAlderspensjon.EndringKanHaBetydningForSkatt)
                }

                includePhrase(
                    ArbeidsinntektOgAlderspensjon(
                        innvilgetFor67 = alderspensjonVedVirk.innvilgetFor67,
                        uttaksgrad = alderspensjonVedVirk.uttaksgrad.ifNull(then = (0)),
                        uforeKombinertMedAlder = alderspensjonVedVirk.ufoereKombinertMedAlder,
                    ),
                )

                includePhrase(InformasjonOmAlderspensjon)
                includePhrase(MeldeFraOmEndringer)
                includePhrase(RettTilAAKlage(vedlegg = vedleggDineRettigheterOgMulighetTilAaKlage))
                includePhrase(RettTilInnsyn(vedlegg = vedleggDineRettigheterOgMulighetTilAaKlage))
                includePhrase(HarDuSpoersmaal.alder)
            }

            includeAttachment(
                vedleggOrienteringOmRettigheterOgPlikter,
                orienteringOmRettigheterOgPlikterDto,
            )

            includeAttachmentIfNotNull(
                vedleggMaanedligPensjonFoerSkatt,
                maanedligPensjonFoerSkattDto,
            )

            includeAttachmentIfNotNull(
                vedleggMaanedligPensjonFoerSkattAp2025,
                maanedligPensjonFoerSkattAP2025Dto,
            )
        }
}
