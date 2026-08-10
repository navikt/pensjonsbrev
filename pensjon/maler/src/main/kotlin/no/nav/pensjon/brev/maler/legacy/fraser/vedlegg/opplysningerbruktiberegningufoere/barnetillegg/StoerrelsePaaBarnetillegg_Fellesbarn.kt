package no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere.barnetillegg

import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.maler.legacy.*
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text

data class StoerrelsePaaBarnetillegg_Fellesbarn(
    val pe: Expression<PEgruppe10>
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        showIf((pe.ut_tbu606v_tbu611v() and pe.ut_tbu606v_tbu608v())) {
            paragraph {
                table(header = {
                    column(columnSpan = 4) {
                        text(
                            bokmal { + "Reduksjon av barnetillegg for fellesbarn før skatt " },
                            nynorsk { + "Reduksjon av barnetillegg for fellesbarn før skatt " },
                        )

                        showIf((not(FUNKSJON_FF_CheckIfFirstDayAndMonthOfYear(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkarvirkningfom())))) {
                            text(
                                bokmal { + "i år" },
                                nynorsk { + "i år" },
                            )
                        }

                        showIf((FUNKSJON_FF_CheckIfFirstDayAndMonthOfYear(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkarvirkningfom()))) {
                            text(
                                bokmal { + "for neste år" },
                                nynorsk { + "for neste år" },
                            )
                        }
                    }
                    column(columnSpan = 1,alignment = ColumnAlignment.RIGHT) {}
                }) {
                    row {
                        cell {
                            text(
                                bokmal { + "Årlig barnetillegg før reduksjon ut fra inntekt" },
                                nynorsk { + "Årleg barnetillegg før reduksjon ut frå inntekt" },
                            )
                        }
                        cell {
                            text(
                                bokmal { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbbruttoperar()
                                    .format(false) + " kr" },
                                nynorsk { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbbruttoperar()
                                    .format(false) + " kr" },
                            )
                        }
                    }
                    row {
                        cell {
                            text(
                                bokmal { + "Samlet inntekt brukt i fastsettelse av barnetillegget er " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinntektbruktiavkortning()
                                    .format(false) + " kr" },
                                nynorsk { + "Samla inntekt brukt i fastsetting av barnetillegget er " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinntektbruktiavkortning()
                                    .format(false) + " kr" },
                            )
                        }
                        cell {

                        }
                    }
                    showIf(
                        (pe.ut_tbu606v_tbu611v() and pe.ut_tbu606v_tbu608v() and (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto()
                            .greaterThan(
                                0
                            ) or (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto()
                            .equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar()
                            .notEqualTo(
                                0
                            ))))
                    ) {
                        row {
                            cell {
                                text(
                                    bokmal { + "Fribeløp brukt i fastsettelsen av barnetillegget er " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbfribelop()
                                        .format(false) + " kr" },
                                    nynorsk { + "Fribeløp brukt i fastsetting av barnetillegget er " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbfribelop()
                                        .format(false) + " kr" },
                                )
                            }
                            cell { }
                        }
                    }

                    showIf(
                        pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto()
                            .notEqualTo(0)
                                or (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().equalTo(0)
                                and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar()
                            .notEqualTo(0))
                    ) {
                        row {
                            cell {
                                text(
                                    bokmal { + "Inntekt over fribeløpet er " + pe.ut_btfbinntektbruktiavkortningminusbtfbfribelop()
                                        .format(false) + " kr" },
                                    nynorsk { + "Inntekt over fribeløpet er " + pe.ut_btfbinntektbruktiavkortningminusbtfbfribelop()
                                        .format(false) + " kr" },
                                )
                            }
                            cell {}
                        }
                    }

                    showIf(
                        ((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().notEqualTo(0)
                                or (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().equalTo(0)
                                and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar()
                            .notEqualTo(0)))
                                and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_avkortingsbelopperar()
                            .greaterThan(0))
                    ) {
                        row {
                            cell {
                                text(
                                    bokmal { + "- 50 prosent av inntekt som overstiger fribeløpet" },
                                    nynorsk { + "- 50 prosent av inntekta som overstig fribeløpet" },
                                    FontType.BOLD
                                )

                                showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_fribelopperiodisert()) {
                                    text(
                                        bokmal { + "(oppgitt som et årlig beløp)" },
                                        nynorsk { + "(oppgitt som eit årleg beløp)" },
                                        FontType.BOLD
                                    )
                                }
                            }
                            cell {
                                text(
                                    bokmal { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_avkortingsbelopperar()
                                        .format(false) + " kr" },
                                    nynorsk { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_avkortingsbelopperar()
                                        .format(false) + " kr" },
                                    FontType.BOLD
                                )
                            }
                        }
                    }

                    showIf(
                        (pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar()
                            .notEqualTo(0))
                    ) {
                        row {
                            cell {
                                showIf(
                                    (pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar()
                                        .greaterThan(0))
                                ) {
                                    text(
                                        bokmal { + "-" },
                                        nynorsk { + "-" },
                                    )
                                }

                                showIf(
                                    (pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar()
                                        .lessThan(0))
                                ) {
                                    text(
                                        bokmal { + "+" },
                                        nynorsk { + "+" },
                                    )
                                }
                                text(
                                    bokmal { + " Beløp som er brukt for å justere reduksjonen av barnetillegget" },
                                    nynorsk { + " Beløp som er brukt for å justere reduksjonen av barnetillegget" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar_utenminus()
                                        .format(false) + " kr" },
                                    nynorsk { + pe.barnetilleggfelles_justeringsbelopperarutenminus().format(false) + " kr" },
                                )
                            }
                        }
                    }

                    showIf(
                        (pe.ut_tbu606v_tbu611v() and pe.ut_tbu606v_tbu608v() and (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto()
                            .notEqualTo(0)
                                or (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().equalTo(0)
                                and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().notEqualTo(0))))
                    ) {
                        row {
                            cell {
                                text(
                                    bokmal { + "= Årlig barnetillegg etter reduksjon ut fra inntekt" },
                                    nynorsk { + "= Årleg barnetillegg etter reduksjon ut frå inntekt" },
                                    FontType.BOLD
                                )
                            }
                            cell {
                                text(
                                    bokmal { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbnettoperar().format(false) + " kr" },
                                    nynorsk { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbnettoperar().format(false) + " kr" },
                                    FontType.BOLD
                                )
                            }
                        }
                    }



                    showIf(
                        (pe.ut_tbu606v_tbu611v() and pe.ut_tbu606v_tbu608v() and (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto()
                            .notEqualTo(0)
                                or (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().equalTo(0)
                                and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().notEqualTo(0))))
                    ) {
                        row {
                            cell {
                                text(
                                    bokmal { + "Utbetaling av barnetillegg per måned " },
                                    nynorsk { + "Utbetaling av barnetillegg per månad " },
                                )
                            }
                            cell {
                                text(
                                    bokmal { + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto()
                                        .format(false) + " kr" },
                                    nynorsk { + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto()
                                        .format(false) + " kr" },
                                )
                            }
                        }
                    }

                    showIf((pe.ut_tbu606v_tbu611v() and pe.ut_tbu606v_tbu608v() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().equalTo(0)
                            and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().equalTo(0))){
                        row {
                            cell {
                                text(
                                    bokmal { + "Grensen for å få utbetalt barnetillegg" },
                                    nynorsk { + "Grensa for å få utbetalt barnetillegg" },
                                    FontType.BOLD
                                )
                            }
                            cell {
                                text(
                                    bokmal { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_inntektstak().format(false) + " kr" },
                                    nynorsk { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_inntektstak().format(false) + " kr" },
                                    FontType.BOLD
                                )
                            }
                        }
                    }
                }
            }
        }

        showIf(
            (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto()
                .greaterThan(
                    0
                ) and pe.ut_tbu606v_tbu611v() and pe.ut_tbu606v_tbu608v())){
            paragraph {
                text (
                    bokmal { + "Du vil få utbetalt " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto()
                        .format() + " i måneden før skatt i barnetillegg" },
                    nynorsk { + "Du vil få utbetalt " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto()
                        .format() + " i månaden før skatt i barnetillegg" },
                )

                showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() and pe.ut_etteroppgjor_bt_utbetalt())){
                    text (
                        bokmal { + " for " + pe.ut_barnet_barna_felles() + " som bor med begge sine foreldre" },
                        nynorsk { + " for " + pe.ut_barnet_barna_felles() + " som bur saman med begge foreldra sine" },
                    )
                }
                text (
                    bokmal { + ". " },
                    nynorsk { + "." },
                )
            }
        }

        showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto()
            .equalTo(0) and pe.ut_tbu606v_tbu611v() and pe.ut_tbu606v_tbu608v())){
            paragraph {

                showIf((pe.ut_tbu608_far_ikke())){
                    text (
                        bokmal { + "Du får ikke utbetalt barnetillegget " },
                        nynorsk { + "Du får ikkje utbetalt barnetillegget " },
                    )
                }

                showIf((pe.ut_tbu608_far_ikke() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget())){
                    text (
                        bokmal { + "for " + pe.ut_barnet_barna_felles() + " som bor med begge sine foreldre " },
                        nynorsk { + "for " + pe.ut_barnet_barna_felles() + " som bur saman med begge foreldra sine " },
                    )
                }

                showIf((pe.ut_tbu608_far_ikke())){
                    text (
                        bokmal { + "fordi samlet inntekt er over grensen for å få utbetalt barnetillegg. " },
                        nynorsk { + "fordi samla inntekt er over grensa for å få utbetalt barnetillegg. " },
                    )
                }

                showIf(
                    (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto()
                        .equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar()
                        .notEqualTo(0))){
                    text (
                        bokmal { + "Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året. " },
                        nynorsk { + "Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året. " },
                    )
                }
            }
        }
    }
}
