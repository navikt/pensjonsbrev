package no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.maler.fraser.common.Constants.SKATTEETATEN_URL
import no.nav.pensjon.brev.maler.legacy.*
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text

data class TBU052V_TBU073V_SlikRedusererViBarnetilleggetUtFraInntekt(
    val pe: Expression<PEgruppe10>
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {

        title1 {
            text(
                bokmal { + "Slik reduserer vi barnetillegget ut fra inntekt" },
                nynorsk { + "Slik reduserer vi barnetillegget ut frå inntekt" },
            )
        }

        paragraph {
            text(
                bokmal { + "Størrelsen på barnetillegget er avhengig av inntekt." },
                nynorsk { + "Storleiken på barnetillegget er avhengig av inntekt." },
            )
        }
        paragraph {
            text(
                bokmal { + "Barnetillegget blir redusert ut fra personinntekt. Dette kan for eksempel være:" },
                nynorsk { + "Barnetillegget kan bli redusert ut frå personinntekt. Dette kan til dømes være:" },
            )
            list {
                item {
                    text(
                        bokmal { + "uføretrygd" },
                        nynorsk { + "uføretrygd" },
                    )
                }
                item {
                    text(
                        bokmal { + "arbeidsinntekt" },
                        nynorsk { + "arbeidsinntekt" },
                    )
                }
                item {
                    text(
                        bokmal { + "næringsinntekt" },
                        nynorsk { + "næringsinntekt" },
                    )
                }
                item {
                    text(
                        bokmal { + "inntekt fra utlandet" },
                        nynorsk { + "inntekt frå utlandet" },
                    )
                }
                item {
                    text(
                        bokmal { + "ytelser/pensjon fra Norge" },
                        nynorsk { + "ytingar/pensjon frå Noreg" },
                    )
                }
                item {
                    text(
                        bokmal { + "pensjon fra utlandet" },
                        nynorsk { + "pensjon frå utlandet" },
                    )
                }
            }
            text(
                bokmal { + "Du kan lese mer om personinntekt på $SKATTEETATEN_URL." },
                nynorsk { + "Du kan lese meir om personinntekt på $SKATTEETATEN_URL." },
            )
        }

        paragraph {
            text(
                bokmal { + "Det er inntekten " },
                nynorsk { + "Det er inntekta " },
            )

            //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
            showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) {
                text(
                    bokmal { + "til deg og din " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut() + " " },
                    nynorsk { + "til deg og " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_nn_entall() + " din " },
                )
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false) THEN      INCLUDE ENDIF
            showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget())) {
                text(
                    bokmal { + "din " },
                    nynorsk { + "di " },
                )
            }
            text(
                bokmal { + "som avgjør hva du får utbetalt i barnetillegg i løpet av året. Er inntekten høyere enn fribeløpet blir barnetillegget redusert. " },
                nynorsk { + "som avgjer kva du får utbetalt i barnetillegg i løpet av året. Er inntekta høgare enn fribeløpet blir barnetillegget redusert. " },
            )
        }

        paragraph {

            //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
            showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) {
                text(
                    bokmal { + "For barn som bor sammen med begge sine foreldre, er fribeløpet 4,6 ganger folketrygdens grunnbeløp. " },
                    nynorsk { + "For barn som bur saman med begge foreldra sine, er fribeløpet 4,6 gonger grunnbeløpet i folketrygda. " },
                )
            }

            //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true
            showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) {
                text(
                    bokmal { + "For barn som ikke bor sammen med begge sine foreldre, er fribeløpet 3,1 ganger folketrygdens grunnbeløp. " },
                    nynorsk { + "For barn som ikkje bur saman med begge foreldra, er fribeløpet 3,1 gonger grunnbeløpet i folketrygda. " },
                )
            }
            text(
                bokmal { + "Fribeløpet øker med 0,4 ganger folketrygdens grunnbeløp for hvert ekstra barn. " },
                nynorsk { + "Fribeløpet aukar med 0,4 gonger grunnbeløpet i folketrygda for kvart ekstra barn. " },
            )
        }


        //IF(pe_ut_tbu601v_tbu604v() = true AND ((PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0)  OR (PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0))  ) THEN      INCLUDE ENDIF
        showIf(
            (pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar()
                .equalTo(
                    0
                )
                    and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()
                    and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().equalTo(0))
                or (pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar()
                .equalTo(
                    0
                )
                    and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto()
                .equalTo(
                    0
                ))
        ) {
            //[TBU052V-TBU073V]

            paragraph {
                text(
                    bokmal { + "Barnetillegget blir redusert med 50 prosent av inntekten som overstiger fribeløpet. Er inntekten " },
                    nynorsk { + "Barnetillegget blir redusert med 50 prosent av inntekta som overstig fribeløpet. Er inntekta " },
                )

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0) THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar()
                        .equalTo(
                            0
                        ))
                ) {
                    text(
                        bokmal { + "til deg og din " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut() + " " },
                        nynorsk { + "til deg og " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_nn_entall() + " din " },
                    )
                }

                //IF(
                // PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false
                // OR (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr > 0)) THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()))
                            or (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()
                            and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()
                            and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar()
                        .equalTo(
                            0
                        )
                            and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar()
                        .greaterThan(
                            0
                        ))) {
                    text(
                        bokmal { + "din " },
                        nynorsk { + "di " },
                    )
                }
                text(
                    bokmal { + "over grensen for å få utbetalt barnetillegg, blir ikke barnetillegget utbetalt. " },
                    nynorsk { + "over grensa for å få utbetalt barnetillegg, blir ikkje barnetillegget utbetalt." },
                )
            }
        }

    }
}