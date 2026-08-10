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

data class StoerrelsePaaBarnetillegg_EndringIInntekt(
    val pe: Expression<PEgruppe10>
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        // Velger å ikke slå sammen denne teksten ettersom det er vesentlige forskjeller i styringen på de ulike språklagene.
        //IF(PE_UT_TBU613V() = true AND PE_UT_TBU613V_1_3() = true) THEN      INCLUDE ENDIF
        showIf((pe.ut_tbu613v() and pe.ut_tbu613v_1_3())) {
            //[TBU052V-TBU073V]

            paragraph {

                //IF(PE_Vedtaksdata_Kravhode_KravArsakType <> "endret_inntekt") THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("endret_inntekt"))) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "Har det vore ei endring i inntekta " },
                    )
                }

                //IF(PE_Vedtaksdata_Kravhode_KravArsakType <> "endret_inntekt"  AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed ektefelle"  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed registrert"  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 1-5"  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 1_5"  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 3-2")) THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksdata_kravhode_kravarsaktype()
                        .notEqualTo("endret_inntekt") and (pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo("bormed ektefelle") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo("bormed registrert") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo(
                        "bormed 1-5"
                    ) or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo("bormed 1_5") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("bormed 3-2")))
                ) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "til deg eller " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_nn_entall() + " " },
                    )
                }

                //IF(PE_Vedtaksdata_Kravhode_KravArsakType <> "endret_inntekt") THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("endret_inntekt"))) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "di" },
                    )
                }

                //IF(PE_Vedtaksdata_Kravhode_KravArsakType <> "endret_inntekt"  AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed ektefelle"  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed registrert"  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 1-5"  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 1_5"  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 3-2")) THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksdata_kravhode_kravarsaktype()
                        .notEqualTo("endret_inntekt") and (pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo("bormed ektefelle") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo("bormed registrert") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo(
                        "bormed 1-5"
                    ) or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo("bormed 1_5") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("bormed 3-2")))
                ) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "n" },
                    )
                }

                //IF(PE_Vedtaksdata_Kravhode_KravArsakType <> "endret_inntekt") THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("endret_inntekt"))) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "," },
                    )
                }

                //PE_Vedtaksdata_Kravhode_KravArsakType = "endret_inntekt"
                showIf(pe.vedtaksdata_kravhode_kravarsaktype().equalTo("endret_inntekt")) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "Når inntekta di " },
                    )
                }

                //IF(PE_Vedtaksdata_Kravhode_KravArsakType = "endret_inntekt"  AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed ektefelle"  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed registrert"  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 1-5"  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 1_5"  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 3-2")) THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksdata_kravhode_kravarsaktype()
                        .equalTo("endret_inntekt") and (pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo("bormed ektefelle") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo("bormed registrert") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo(
                        "bormed 1-5"
                    ) or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo("bormed 1_5") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("bormed 3-2")))
                ) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "eller til " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_nn_entall() + " din " },
                    )
                }

                //PE_Vedtaksdata_Kravhode_KravArsakType = "endret_inntekt"
                showIf(pe.vedtaksdata_kravhode_kravarsaktype().equalTo("endret_inntekt")) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "endrar seg," },
                    )
                }
                text(
                    bokmal { + "" },
                    nynorsk { + " blir reduksjonen av barnetilleggene vurdert på nytt." },
                )
            }
        }

        // Velger å ikke slå sammen denne teksten ettersom det er vesentlige forskjeller i styringen på de ulike språklagene.
        //IF(PE_UT_TBU613V() = true AND PE_UT_TBU613V_1_3() = true) THEN      INCLUDE ENDIF
        showIf((pe.ut_tbu613v() and pe.ut_tbu613v_1_3())) {
            //[TBU052V-TBU073V]

            paragraph {

                //IF(( PE_UT_TBU605V_eller_til_din() )) THEN    INCLUDE ENDIF
                showIf(((pe.ut_tbu605v_eller_til_din()))) {
                    text(
                        bokmal { + "Har det vært en endring i inntekten din" },
                        nynorsk { + "" },
                    )
                }

                //IF(( PE_UT_TBU605V_eller_til_din() AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed ektefelle" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed registrert partner" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 1-5" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 1_5" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 3-2") )) THEN      INCLUDE ENDIF
                showIf(
                    ((pe.ut_tbu605v_eller_til_din() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and (pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo(
                            "bormed ektefelle"
                        ) or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo("bormed registrert partner") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo(
                        "bormed 1-5"
                    ) or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo("bormed 1_5") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo(
                        "bormed 3-2"
                    ))))
                ) {
                    text(
                        bokmal { + " eller til din" },
                        nynorsk { + "" },
                    )
                }

                //IF(( PE_UT_TBU605V_eller_til_din() )) THEN    INCLUDE ENDIF
                showIf(((pe.ut_tbu605v_eller_til_din()))) {
                    text(
                        bokmal { + " " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut() + "," },
                        nynorsk { + "" },
                    )
                }

                //PE_Vedtaksdata_Kravhode_KravArsakType = "endret_inntekt"
                showIf(pe.vedtaksdata_kravhode_kravarsaktype().equalTo("endret_inntekt")) {
                    text(
                        bokmal { + "Når inntekten din " },
                        nynorsk { + "" },
                    )
                }

                //IF(PE_Vedtaksdata_Kravhode_KravArsakType = "endret_inntekt"  AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed ektefelle"  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed registrert"  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 1-5"  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 1_5"  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 3-2")) THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksdata_kravhode_kravarsaktype()
                        .equalTo("endret_inntekt") and (pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo("bormed ektefelle") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo("bormed registrert") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo(
                        "bormed 1-5"
                    ) or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo("bormed 1_5") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("bormed 3-2")))
                ) {
                    text(
                        bokmal { + "eller til din " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut() + " " },
                        nynorsk { + "" },
                    )
                }

                //PE_Vedtaksdata_Kravhode_KravArsakType = "endret_inntekt"
                showIf(pe.vedtaksdata_kravhode_kravarsaktype().equalTo("endret_inntekt")) {
                    text(
                        bokmal { + "endrer seg," },
                        nynorsk { + "" },
                    )
                }
                text(
                    bokmal { + " blir reduksjonen av barnetilleggene vurdert på nytt. " },
                    nynorsk { + "" },
                )
            }
        }

        // Velger å ikke slå sammen denne teksten ettersom det er vesentlige forskjeller i styringen på de ulike språklagene.
        //IF(PE_UT_TBU613V() = true AND PE_UT_TBU613V_1_3() = true) THEN      INCLUDE ENDIF
        showIf((pe.ut_tbu613v() and pe.ut_tbu613v_1_3())) {
            //[TBU052V-TBU073V]

            paragraph {

                //IF(PE_Vedtaksdata_Kravhode_KravArsakType <> "endret_inntekt") THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("endret_inntekt"))) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "" },
                    )
                }

                //PE_Vedtaksdata_Kravhode_KravArsakType = "endret_inntekt"
                showIf(pe.vedtaksdata_kravhode_kravarsaktype().equalTo("endret_inntekt")) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "" },
                    )
                }
                text(
                    bokmal { + "" },
                    nynorsk { + "" },
                )

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed ektefelle" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed registrert partner" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 1-5" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 3-2" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 1_5") THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo("bormed ektefelle") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo("bormed registrert partner") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo(
                        "bormed 1-5"
                    ) or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo("bormed 3-2") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("bormed 1_5"))
                ) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "" },
                    )
                }
                text(
                    bokmal { + "" },
                    nynorsk { + "" },
                )
            }
        }
        //IF(PE_UT_TBU613V() = true AND PE_UT_TBU613V_1_3() = true) THEN      INCLUDE ENDIF

        //IF(PE_UT_TBU613V() = true AND PE_UT_TBU613V_1_3() = true) THEN      INCLUDE ENDIF

        //IF(PE_UT_TBU613V() = true AND PE_UT_TBU613V_1_3() = true) THEN      INCLUDE ENDIF
        showIf((pe.ut_tbu613v() and pe.ut_tbu613v_1_3())) {
            //[TBU052V-TBU073V]

            paragraph {
                text(
                    bokmal { + "50 prosent av den inntekten som overstiger fribeløpet for " + pe.ut_barnet_barna_felles() + " som bor med begge sine foreldre " },
                    nynorsk { + "50 prosent av inntekta som overstig fribeløpet for " + pe.ut_barnet_barna_felles() + " som bur med begge foreldra " },
                )

//PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_AntallBarnFelles = 1
                showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_antallbarnfelles().equalTo(1)) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "" },
                    )
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_AntallBarnFelles > 1) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_antallbarnfelles().greaterThan(1))) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "" },
                    )
                }
                text(
                    bokmal { + "" },
                    nynorsk { + "" },
                )
                //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = true
                showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_fribelopperiodisert()) {
                    text(
                        bokmal { + "blir omregnet til et årlig beløp som tilsvarer " },
                        nynorsk { + "sine blir rekna om til et årleg beløp som svarer til " },
                    )
                }

                //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = false
                showIf(not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_fribelopperiodisert())) {
                    text(
                        bokmal { + "er " },
                        nynorsk { + "er " },
                    )
                }
                text(
                    bokmal { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_avkortingsbelopperar()
                        .format() + ". " },
                    nynorsk { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_avkortingsbelopperar()
                        .format() + ". " },
                )

                //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0
                showIf(
                    pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar()
                        .equalTo(0)
                ) {
                    text(
                        bokmal { + "Dette beløpet bruker vi til å redusere dette barnetillegget for hele året." },
                        nynorsk { + "Dette beløpet bruker vi til å redusere dette barnetillegget for hele året." },
                    )
                }
            }
        }

        //IF(PE_UT_TBU613V() = true AND PE_UT_TBU613V_1_3() = true AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr <> 0) THEN      INCLUDE ENDIF
        showIf(
            (pe.ut_tbu613v() and pe.ut_tbu613v_1_3() and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar()
                .notEqualTo(
                    0
                ))
        ) {
            //[TBU052V-TBU073V]

            paragraph {
                text(
                    bokmal { + "Vi tar hensyn til hvordan barnetillegget eventuelt har vært redusert tidligere, og vi har derfor " },
                    nynorsk { + "Vi tek omsyn til korleis eit barnetillegg eventuelt har vore redusert tidlegare, og har derfor " },
                )

                //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr > 0) THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar()
                        .greaterThan(0))
                ) {
                    text(
                        bokmal { + "lagt til" },
                        nynorsk { + "lagt til" },
                    )
                }

                //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr < 0) THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar()
                        .lessThan(0))
                ) {
                    text(
                        bokmal { + "trukket fra" },
                        nynorsk { + "trekt frå" },
                    )
                }
                text(
                    bokmal { + " " + pe.barnetilleggfelles_justeringsbelopperarutenminus()
                        .format() + " i beløpet vi reduserer barnetillegget med for resten av året." },
                    nynorsk { + " " + pe.barnetilleggfelles_justeringsbelopperarutenminus()
                        .format() + " i beløpet vi reduserer barnetillegget med for resten av året." },
                )
            }
        }

        //IF(PE_UT_TBU613V() = true AND PE_UT_TBU613V_4_5() = true) THEN      INCLUDE ENDIF
        showIf((pe.ut_tbu613v() and pe.ut_tbu613v_4_5())) {
            //[TBU052V-TBU073V]

            paragraph {
                text(
                    bokmal { + "For " + pe.ut_barnet_barna_serkull() + " som ikke bor sammen med begge foreldrene " },
                    nynorsk { + "For " + pe.ut_barnet_barna_serkull() + " som ikkje bur med begge foreldra " },
                )
                showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_antallbarnserkull().equalTo(1)) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "" },
                    )
                }
                text(
                    bokmal { + "" },
                    nynorsk { + "" },
                )

                showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_antallbarnserkull().equalTo(1)) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "" },
                    )
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_AntallBarnSerkull > 1) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_antallbarnserkull().greaterThan(1))) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "" },
                    )
                }
                text(
                    bokmal { + "" },
                    nynorsk { + "" },
                )
                //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert = false
                showIf(not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_fribelopperiodisert())) {
                    text(
                        bokmal { + "er 50 prosent av den inntekten som overstiger fribeløpet " },
                        nynorsk { + "er 50 prosent av den inntekta som overstig fribeløpet " },
                    )
                }

                //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert = true
                showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_fribelopperiodisert()) {
                    text(
                        bokmal { + "blir 50 prosent av den inntekten som overstiger fribeløpet omregnet til et årlig beløp som tilsvarer " },
                        nynorsk { + "blir 50 prosent av den inntekta som overstig fribeløpet regna om til et årleg beløp som svarer til " },

                        )
                }
                showIf(not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_fribelopperiodisert())) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "" },
                    )
                }

                text(
                    bokmal { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_avkortingsbelopperar()
                        .format() + ". " },
                    nynorsk { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_avkortingsbelopperar()
                        .format() + ". " },
                )

                //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0
                showIf(
                    pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar()
                        .equalTo(0)
                ) {
                    text(
                        bokmal { + "Dette beløpet bruker vi til å redusere dette barnetillegget for hele året. " },
                        nynorsk { + "Dette beløpet bruker vi til å redusere dette barnetillegget for heile året. " },
                    )
                }
            }
        }

        //IF(PE_UT_TBU613V() = true AND PE_UT_TBU613V_4_5() = true AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr <> 0) THEN      INCLUDE ENDIF
        showIf(
            (pe.ut_tbu613v() and pe.ut_tbu613v_4_5() and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar()
                .notEqualTo(
                    0
                ))
        ) {
            //[TBU052V-TBU073V]

            paragraph {
                text(
                    bokmal { + "Vi tar hensyn til hvordan barnetillegget eventuelt har vært redusert tidligere, og vi har derfor " },
                    nynorsk { + "Vi tek omsyn til korleis eit barnetillegg eventuelt har vore redusert tidlegare, og har derfor " },
                )

                //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr > 0) THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar()
                        .greaterThan(0))
                ) {
                    text(
                        bokmal { + "lagt til" },
                        nynorsk { + "lagt til" },
                    )
                }

                //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr < 0) THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar()
                        .lessThan(0))
                ) {
                    text(
                        bokmal { + "trukket fra" },
                        nynorsk { + "trekt frå" },
                    )
                }
                text(
                    bokmal { + " " + pe.barnetilleggserkull_justeringsbelopperarutenminus()
                        .format() + " i beløpet vi reduserer barnetillegget med for resten av året." },
                    nynorsk { + " " + pe.barnetilleggserkull_justeringsbelopperarutenminus()
                        .format() + " i beløpet vi reduserer barnetillegget med for resten av året." },
                )
            }
        }
    }
}
