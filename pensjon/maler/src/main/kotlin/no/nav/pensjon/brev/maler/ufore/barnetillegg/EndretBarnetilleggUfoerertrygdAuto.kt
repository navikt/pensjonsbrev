package no.nav.pensjon.brev.maler.ufore.barnetillegg

import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.legacy.EndretBarnetilleggUfoeretrygdDto
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.endretBarnetilleggUfoeretrygdDto.maanedligUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.endretBarnetilleggUfoeretrygdDto.orienteringOmRettigheterUfoere
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.endretBarnetilleggUfoeretrygdDto.pe
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.legacy.*
import no.nav.pensjon.brev.maler.legacy.vedlegg.OPPLYSNINGER_OM_BEREGNINGEN_BOKMAL
import no.nav.pensjon.brev.maler.legacy.vedlegg.vedleggOpplysningerBruktIBeregningUTLegacy
import no.nav.pensjon.brev.maler.ufore.fraser.Barnetillegg
import no.nav.pensjon.brev.maler.ufore.fraser.Ufoeretrygd.MeldeFraOmEndringer
import no.nav.pensjon.brev.maler.ufore.fraser.Ufoeretrygd.SjekkUtbetalingene
import no.nav.pensjon.brev.maler.ufore.fraser.Ufoeretrygd.SkattForDegSomBorIUtlandet
import no.nav.pensjon.brev.maler.ufore.fraser.Ufoeretrygd.Skattekort
import no.nav.pensjon.brev.maler.ufore.vedlegg.vedleggDineRettigheterOgPlikterUfoere
import no.nav.pensjon.brev.maler.ufore.vedlegg.vedleggMaanedligUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object EndretBarnetilleggUfoerertrygdAuto : AutobrevTemplate<EndretBarnetilleggUfoeretrygdDto> {

    // PE_UT_06_300
    override val kode = Pesysbrevkoder.AutoBrev.UT_BARNETILLEGG_ENDRET_AUTO

    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Endring av barnetillegg i uføretrygden",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {
        val btfbInnvilget = pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()
        val btsbInnvilget = pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()
        val btfbfradrag = pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag()
        val btsbFradrag = pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag()

        val totalNetto = pe.vedtaksdata_beregningsdata_beregningufore_totalnetto()
        val btsbJusteringsbelopperAr = pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar()
        val btfbJusteringsbelopperAr = pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar()
        val btsbInntektbruktiavkorning = pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning()
        val btsbFribelop = pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop()
        val btfbFribelop = pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbfribelop()
        val btsbNetto = pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto()
        val btfbNetto = pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto()
        val btfbBrutto = pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbbrutto()
        val btsbBrutto = pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbbrutto()
        val btfbInntektbruktiavkortning = pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinntektbruktiavkortning()

        title {
            text(bokmal { + "Endring av barnetillegg i uføretrygden" })
        }
        outline {
            paragraph {
                text(bokmal { + "Barnetillegget ditt er endret fra 1. januar " + pe.ut_virkningfomar().format() + "." })
            }

            paragraph {
                ifNotNull(pe.vedtaksdata_beregningsdata_beregningufore_beregningvirkningdatofom()) {
                    text(bokmal { +"Du får " + totalNetto.format() + " i uføretrygd og barnetillegg per måned før skatt fra " + it.formatMonthYear() + "." })
                }.orShow {
                    text(bokmal { +"Du får " + totalNetto.format() + " i uføretrygd og barnetillegg per måned før skatt." })
                }
            }

            paragraph {
                text(bokmal { + "Uføretrygden blir fortsatt utbetalt senest den 20. hver måned." })
            }

            title1 {
                text (bokmal { + "Begrunnelse for vedtaket" })
            }
            paragraph {
                text(bokmal { + "Barnetillegget ditt er beregnet på nytt fordi det årlige beløpet av uføretrygden er endret som følge av nytt kalenderår. Det betyr at utbetalingen for " + pe.ut_virkningfomar().format() + " endres fordi din årlige reduksjon av barnetillegg er beregnet på nytt." })
            }
            paragraph {
                text(bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-15 og 12-16." })
            }

            title1 {
                text(bokmal { + "Slik påvirker inntekt og fribeløp barnetillegget ditt" })
            }
            paragraph {
                text(bokmal { + "Utbetalingen av uføretrygden i løpet av et kalenderår er en del av inntekten som brukes i behovsprøvingen av barnetillegget. Fra 1. januar " + pe.ut_virkningfomar().format() + " er den årlige uføretrygden lik månedsbeløpet du får i januar multiplisert med 12 måneder. Dette beløpet er større enn den årlige uføretrygden som inngikk i behovsprøvingen i fjor. Fribeløpet er uendret fra 1. januar " + pe.ut_virkningfomar().format() + "." })
            }

            showIf( btsbInnvilget and not(btfbInnvilget)) {
                includePhrase(
                    Barnetillegg.InntektensBetydningForBarnetillegg(
                        harSaerkullstillegg = btsbInnvilget,
                        harFellesbarnstillegg = btfbInnvilget,
                        nettoSaerkullstillegg = btsbNetto,
                        nettoFellesbarnstillegg = pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto(),
                        visAtEpsInntektIkkePaavirkerTillegget = pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().notEqualTo("enslig") and
                            pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().notEqualTo("enslig separert"),
                        fellesbarnOmtale = pe.ut_barnet_barna_felles(),
                        epsBokmal = pe.sivilstand_ektefelle_partner_samboer_bormed_ut(),
                        epsNynorsk = pe.sivilstand_ektefelle_partner_samboer_bormed_ut_nn_entall(),
                    )
                )
                paragraph {
                    showIf(btsbNetto.greaterThan(0) and btsbJusteringsbelopperAr.equalTo(0)) {
                        text(bokmal { + "Inntekten din på " + btsbInntektbruktiavkorning.format() + " er " + ifElse(btsbInntektbruktiavkorning.greaterThan(btsbFribelop), "høyere".expr(), "lavere".expr()) + " enn fribeløpet ditt på " + btsbFribelop.format() + ". Barnetillegget ditt er derfor " })
                    }

                    showIf(btsbNetto.greaterThan(0)
                            and btsbJusteringsbelopperAr.equalTo(0)
                            and btsbInntektbruktiavkorning.lessThanOrEqual(btsbFribelop)) {
                        text(bokmal { + "ikke " })
                    }

                    showIf(btsbNetto.greaterThan(0)
                            and btsbJusteringsbelopperAr.equalTo(0)) {
                        text(bokmal { + "redusert ut fra inntekt. " })
                    }

                    showIf(btsbNetto.equalTo(0)
                            and btsbJusteringsbelopperAr.equalTo(0)) {
                        text(bokmal { + "Inntekten din er " + btsbInntektbruktiavkorning.format() + "." })
                    }
                }
            }.orShow {
                paragraph {
                    text(bokmal { + "Du får barnetillegg for fellesbarn fordi du bor sammen med barnets andre forelder. For fellesbarn, bruker vi begge foreldrenes inntekt (inkludert uføretrygd) når vi beregner størrelsen på barnetillegget." })
                }
                paragraph {
                    text(bokmal { + "Inntekten din er " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbbrukersinntekttilavkortning().format() + " og inntekten til din " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut() + " er " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbinntektannenforelder().format() + ". " })

                    showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbbelopfratrukketannenforeldersinntekt().greaterThan(0))) {
                        text(bokmal { + "Folketrygdens grunnbeløp på inntil " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop().format() + " er holdt utenfor inntekten til din " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut() + ". " })
                    }

                    showIf (not(btsbInnvilget)
                            and btfbJusteringsbelopperAr.equalTo(0)
                            and btfbNetto.notEqualTo(0)) {
                        text(bokmal { + "Til sammen er inntektene " + ifElse(btfbfradrag.greaterThan(0), "høyere".expr(), "lavere".expr()) + " enn fribeløpet ditt på " + btfbFribelop.format() + ". Barnetillegget ditt er derfor " })
                        showIf(btfbfradrag.equalTo(0)) {
                            text(bokmal { + "ikke " })
                        }
                        text(bokmal { + "redusert." })
                    }
                }
            }

            showIf((pe.ut_tbu1286_del1())) { // Både innvilget BTFB og BTSB pluss litt mer logikk
                paragraph {
                    showIf((btsbNetto.notEqualTo(0) and btsbJusteringsbelopperAr.equalTo(0))) {
                        text(bokmal { + "Inntekten din er " + ifElse(btsbInntektbruktiavkorning.greaterThan(btsbFribelop), "høyere".expr(), "lavere".expr()) + " enn " + btsbFribelop.format() + ", som er fribeløpet for barnetillegget til " + pe.ut_barnet_barna_serkull() + " som ikke bor sammen med begge foreldrene. "})
                    }

                    showIf((btsbNetto.notEqualTo(0)
                                and ((btsbFradrag.greaterThan(0)
                                and btfbfradrag.equalTo(0)) or (btsbFradrag.equalTo(0)
                                and btfbfradrag.greaterThan(0)))
                                and btsbJusteringsbelopperAr.equalTo(0))) {
                        text(bokmal { + "Dette barnetillegget er derfor " + ifElse(btsbNetto.equalTo(btsbBrutto), "ikke ".expr(), "".expr()) + "redusert ut fra inntekt. " })
                    }

                    showIf((btfbNetto.notEqualTo(0) and btfbJusteringsbelopperAr.equalTo(0))) {
                        text(bokmal { + "Til sammen er " })
                    }

                    showIf((btfbNetto.notEqualTo(0)
                                and ((btsbFradrag.equalTo(0)
                                and btfbfradrag.equalTo(0)) or (btsbFradrag.greaterThan(0)
                                and btfbfradrag.greaterThan(0)))
                                and btfbJusteringsbelopperAr.equalTo(0))) {
                        text(bokmal { + "også " })
                    }

                    showIf((btfbNetto.notEqualTo(0) and btfbJusteringsbelopperAr.equalTo(0))) {
                        text(bokmal { + "inntektene til deg og din " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut() + " " + ifElse(btfbInntektbruktiavkortning.greaterThan(btfbFribelop), "høyere".expr(), "lavere".expr()) + " enn " + btfbFribelop.format() + ", som er fribeløpet for barnetillegget til " + pe.ut_barnet_barna_felles() + " som bor med begge sine foreldre. " })
                    }

                    showIf((((btsbFradrag.greaterThan(0)
                                and btfbfradrag.equalTo(0)) or (btsbFradrag.equalTo(0)
                                and btfbfradrag.greaterThan(0)))
                                and btfbNetto.notEqualTo(0)
                                and btfbJusteringsbelopperAr.equalTo(0))) {
                        text(bokmal { + "Dette barnetillegget er derfor " })
                    }

                    showIf((btfbNetto.equalTo(btfbBrutto)
                                and ((btsbFradrag.greaterThan(0)
                                and btfbfradrag.equalTo(0)) or (btsbFradrag.equalTo(0)
                                and btfbfradrag.greaterThan(0)))
                                and btfbNetto.notEqualTo(0)
                                and btfbJusteringsbelopperAr.equalTo(0))) {
                        text(bokmal { + "ikke " })
                    }

                    showIf((((btsbFradrag.greaterThan(0)
                                and btfbfradrag.equalTo(0)) or (btsbFradrag.equalTo(0)
                                and btfbfradrag.greaterThan(0)))
                                and btfbNetto.notEqualTo(0)
                                and btfbJusteringsbelopperAr.equalTo(0))) {
                        text(bokmal { + "redusert ut fra inntekt. " })
                    }

                    showIf((btfbNetto.notEqualTo(0)
                                and btsbNetto.notEqualTo(0)
                                and ((btsbFradrag.equalTo(0)
                                and btfbfradrag.equalTo(0)) or (btsbFradrag.greaterThan(0)
                                and btfbfradrag.greaterThan(0)))
                                and btsbJusteringsbelopperAr.equalTo(0)
                                and btfbJusteringsbelopperAr.equalTo(0))) {
                        text(bokmal { + "Barnetilleggene er derfor" })
                    }

                    showIf(((btfbNetto.equalTo(btfbBrutto)
                                and btsbNetto.equalTo(btsbBrutto))
                                and (btfbNetto.notEqualTo(0)
                                and btsbNetto.notEqualTo(0))
                                and ((btsbFradrag.equalTo(0)
                                and btfbfradrag.equalTo(0)) or (btsbFradrag.greaterThan(0)
                                and btfbfradrag.greaterThan(0)))
                                and btsbJusteringsbelopperAr.equalTo(0)
                                and btfbJusteringsbelopperAr.equalTo(0))) {
                        text(bokmal { + " ikke" })
                    }

                    showIf((btfbNetto.notEqualTo(0)
                                and btsbNetto.notEqualTo(0)
                                and ((btsbFradrag.equalTo(0)
                                and btfbfradrag.equalTo(0)) or (btsbFradrag.greaterThan(0)
                                and btfbfradrag.greaterThan(0)))
                                and btsbJusteringsbelopperAr.equalTo(0)
                                and btfbJusteringsbelopperAr.equalTo(0))) {
                        text(bokmal { + " redusert ut fra inntekt." })
                    }
                }
            }

            showIf((btsbInnvilget
                    and btsbNetto.equalTo(0)
                    and btsbJusteringsbelopperAr.equalTo(0)
                    and (btfbNetto.notEqualTo(0) or not(btfbInnvilget)))) {
                paragraph {
                    showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) {
                        text(bokmal { + "Barnetillegget for " + pe.ut_barnet_barna_serkull() + " som ikke bor sammen med begge foreldrene, blir ikke utbetalt fordi du alene har en samlet inntekt som er høyere enn " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_inntektstak().format() + ". Inntekten din er over grensen for å få utbetalt barnetillegg. "})
                    }.orShow {
                        text(bokmal { +"Barnetillegget blir ikke utbetalt fordi du har en samlet inntekt som er høyere enn " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_inntektstak().format() + ". Inntekten din er over grensen for å få utbetalt barnetillegg. "})
                    }
                }
            }

            showIf((btfbInnvilget
                    and btfbNetto.equalTo(0)
                    and btfbJusteringsbelopperAr.equalTo(0)
                    and (btsbNetto.notEqualTo(0) or not(btsbInnvilget)))) {
                paragraph {
                    showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) {
                        text(bokmal { + "Barnetillegget for " + pe.ut_barnet_barna_felles() + " som bor med begge sine foreldre, blir ikke utbetalt fordi dere har en samlet inntekt som er høyere enn " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_inntektstak().format() + ". De samlede inntektene er over grensen for å få utbetalt barnetillegg. "})
                    }.orShow {
                        text(bokmal { + "Barnetillegget blir ikke utbetalt fordi dere har en samlet inntekt som er høyere enn " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_inntektstak().format() + ". De samlede inntektene er over grensen for å få utbetalt barnetillegg."})
                    }
                }
            }

            showIf(btsbInnvilget and btfbInnvilget
                        and btsbNetto.equalTo(0)
                        and btsbJusteringsbelopperAr.equalTo(0)
                        and btfbNetto.equalTo(0)
                        and btfbJusteringsbelopperAr.equalTo(0)) {
                paragraph {
                    text (bokmal { + "Barnetillegget for " + pe.ut_barnet_barna_felles() + " som bor med begge sine foreldre, blir ikke utbetalt fordi de samlede inntektene er høyere enn " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_inntektstak().format() + ". Barnetillegget for " + pe.ut_barnet_barna_serkull() + " som ikke bor sammen med begge foreldrene, blir heller ikke utbetalt fordi inntekten din alene er høyere enn " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_inntektstak().format() + ". Inntektene er over grensen for å få utbetalt barnetillegg." })
                }
            }

            showIf((pe.ut_kravlinjekode_vedtakresultat_forekomst_bt_innv().greaterThan(0))) {
                includePhrase(Barnetillegg.BarnetilleggPavirkesAvUtlandsopphold)
            }

            paragraph {
                text(
                    bokmal { +"Du kan lese mer om beregningen av barnetillegg i vedlegget «${OPPLYSNINGER_OM_BEREGNINGEN_BOKMAL}»." },
                )
            }
            includePhrase(MeldeFraOmEndringer())
            includePhrase(Felles.RettTilAAKlage)
            includePhrase(Felles.RettTilInnsyn(vedleggDineRettigheterOgPlikterUfoere))
            includePhrase(SjekkUtbetalingene)
            includePhrase(Skattekort)
            includePhrase(SkattForDegSomBorIUtlandet(pe.grunnlag_persongrunnlagsliste_personbostedsland().equalTo("nor") or pe.grunnlag_persongrunnlagsliste_personbostedsland().equalTo("")))
            includePhrase(Felles.HarDuSpoersmaal.ufoeretrygd)

        }

        includeAttachmentIfNotNull(vedleggMaanedligUfoeretrygdFoerSkatt, maanedligUfoeretrygdFoerSkatt)
        includeAttachment(vedleggOpplysningerBruktIBeregningUTLegacy, pe, pe.inkluderopplysningerbruktiberegningen())
        includeAttachment(vedleggDineRettigheterOgPlikterUfoere, orienteringOmRettigheterUfoere)
    }
}
