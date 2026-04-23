package no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.maler.legacy.*
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import java.time.LocalDate

data class TBU080V_TBU027V(
    val pe: Expression<PEgruppe10>,
): OutlinePhrase<LangBokmalNynorsk>(){
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {

        val bosattUtland = pe.pebrevkode().equalTo("f_bh_bo_utland") or (pe.grunnlag_persongrunnlagsliste_personbostedsland().notEqualTo("nor") and pe.grunnlag_persongrunnlagsliste_personbostedsland().notEqualTo(""))

        //IF(PE_pebrevkode <> "PE_UT_07_100" AND PE_pebrevkode <> "PE_UT_05_100" AND PE_pebrevkode <> "PE_UT_04_300" AND PE_pebrevkode <> "PE_UT_14_300" AND PE_pebrevkode <> "PE_UT_04_103" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_pebrevkode <> "PE_UT_04_108" AND PE_pebrevkode <> "PE_UT_04_109" AND PE_pebrevkode <> "PE_UT_07_200" AND PE_pebrevkode <> "PE_UT_06_300") THEN      INCLUDE ENDIF
        showIf(pe.pebrevkode().isNotAnyOf("PE_UT_07_100", "PE_UT_05_100", "PE_UT_04_300", "PE_UT_14_300", "PE_UT_04_103", "PE_UT_04_108", "PE_UT_07_200", "PE_UT_06_300")
                and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt")) {
            showIf(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_mottarminsteytelse()) {

                title1 {
                    text(
                        bokmal { +"For deg som har rett til minsteytelse" },
                        nynorsk { +"For deg som har rett til minsteyting" },
                    )
                }

                //IF( PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningVirkningDatoFom < DateValue("01/09/2016") AND PE_Vedtaksdata_Kravhode_onsketVirkningsDato >=  DateValue("01/07/2024") AND (PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Mottarminsteytelse = true OR ( PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Minsteytelse_OppfyltUngUfor = true  AND  PE_UT_VilkarGjelderPersonAlder < 20  AND  PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Mottarminsteytelse = true ))   ) THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksdata_beregningsdata_beregningufore_beregningvirkningdatofom().legacyLessThan(LocalDate.of(2016, 9, 1)) and pe.vedtaksdata_kravhode_onsketvirkningsdato().legacyGreaterThanOrEqual(LocalDate.of(2024, 7, 1)))
                            or (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_minsteytelse_oppfyltungufor() and pe.ut_vilkargjelderpersonalder().lessThan(20))
                ) {
                    //[TBU080V-TBU027V]

                    paragraph {
                        text(
                            bokmal { +"Du er sikret minsteytelse fordi beregningen ut fra din egenopptjente inntekt er lavere enn minstenivået for uføretrygd. Minste årlige ytelse er 2,329 ganger folketrygdens grunnbeløp for personer som lever sammen med ektefelle, partner eller er i et samboerforhold som har vart i minst 12 av de siste 18 månedene. Er du enslig utgjør minste årlige ytelse 2,529 ganger grunnbeløpet. " },
                            nynorsk { +"Du er sikra minsteyting fordi berekninga ut frå den eigenopptente inntekta di er lågare enn minstenivået for uføretrygd. Minste årlege yting er 2,329 gonger grunnbeløpet i folketrygda for personar som lever saman med ektefelle, partnar eller er i eit sambuarforhold som har vart i minst 12 av dei siste 18 månadene. Er du einsleg, utgjer minste årlege yting 2,529 gonger grunnbeløpet. " },
                        )

                        //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Minsteytelse_OppfyltUngUfor = true AND PE_UT_VilkarGjelderPersonAlder < 20 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Mottarminsteytelse = true) THEN      INCLUDE ENDIF
                        showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_minsteytelse_oppfyltungufor() and pe.ut_vilkargjelderpersonalder().lessThan(20))) {
                            text(
                                bokmal { +"Er du innvilget rettighet som ung ufør, er minste årlige ytelse, fra fylte 20 år, 2,709 ganger folketrygdens grunnbeløp hvis du lever sammen med ektefelle, partner eller er i et samboerforhold som har vart i minst 12 av de siste 18 månedene. Er du enslig utgjør minste årlige ytelse 2,959 ganger grunnbeløpet." },
                                nynorsk { +"Er du innvilga rett som ung ufør, er minste årlege yting, frå fylte 20 år, 2,709 gonger grunnbeløpet i folketrygda dersom du lever saman med ektefelle, partnar eller er i eit sambuarforhold som har vart i minst 12 av de siste 18 månadene. Er du einsleg, utgjer minste årlege yting 2,959 gonger grunnbeløpet." },
                            )
                        }
                    }
                }

                //IF( PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningVirkningDatoFom < DateValue("01/09/2016") AND PE_Vedtaksdata_Kravhode_onsketVirkningsDato <  DateValue("01/07/2024") AND (PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Mottarminsteytelse = true OR ( PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Minsteytelse_OppfyltUngUfor = true  AND  PE_UT_VilkarGjelderPersonAlder < 20  AND  PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Mottarminsteytelse = true ))   ) THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksdata_beregningsdata_beregningufore_beregningvirkningdatofom().legacyLessThan(LocalDate.of(2016, 9, 1)) and pe.vedtaksdata_kravhode_onsketvirkningsdato().legacyLessThan(LocalDate.of(2024, 7, 1)))
                            or (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_minsteytelse_oppfyltungufor() and pe.ut_vilkargjelderpersonalder().lessThan(20))
                ) {
                    //[TBU080V-TBU027V]

                    paragraph {
                        text(
                            bokmal { +"Du er sikret minsteytelse fordi beregningen ut fra din egenopptjente inntekt er lavere enn minstenivået for uføretrygd. Minste årlige ytelse er 2,28 ganger folketrygdens grunnbeløp for personer som lever sammen med ektefelle, partner eller er i et samboerforhold som har vart i minst 12 av de siste 18 månedene. Fra 1. juli 2024 øker denne til 2,329. Er du enslig utgjør minste årlige ytelse 2,48 ganger grunnbeløpet. Fra 1. juli 2024 øker denne til 2,529. " },
                            nynorsk { +"Du er sikra minsteyting fordi berekninga ut frå den eigenopptente inntekta di er lågare enn minstenivået for uføretrygd. Minste årlege yting er 2,28 gonger grunnbeløpet i folketrygda for personar som lever saman med ektefelle, partnar eller er i eit sambuarforhold som har vart i minst 12 av dei siste 18 månadene. Frå 1. juli 2024 aukar denne til 2,329. Er du einsleg, utgjer minste årlege yting 2,48 gonger grunnbeløpet. Frå 1. juli 2024 aukar denne til 2,529. " },
                        )

                        //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Minsteytelse_OppfyltUngUfor = true AND PE_UT_VilkarGjelderPersonAlder < 20 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Mottarminsteytelse = true) THEN      INCLUDE ENDIF
                        showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_minsteytelse_oppfyltungufor() and pe.ut_vilkargjelderpersonalder().lessThan(20))) {
                            text(
                                bokmal { +"Er du innvilget rettighet som ung ufør, er minste årlige ytelse, fra fylte 20 år, 2,66 ganger folketrygdens grunnbeløp hvis du lever sammen med ektefelle, partner eller er i et samboerforhold som har vart i minst 12 av de siste 18 månedene. Fra 1. juli 2024 øker denne til 2,709. Er du enslig utgjør minste årlige ytelse 2,91 ganger grunnbeløpet. Fra 1. juli øker denne til 2,959." },
                                nynorsk { +"Er du innvilga rett som ung ufør, er minste årlege yting, frå fylte 20 år, 2,66 gonger grunnbeløpet i folketrygda dersom du lever saman med ektefelle, partnar eller er i eit sambuarforhold som har vart i minst 12 av de siste 18 månadene. Frå 1. juli 2024 aukar denne til 2,709. Er du einsleg, utgjer minste årlege yting 2,91 gonger grunnbeløpet. Frå 1. juli 2024 aukar denne til 2,959." },
                            )
                        }
                    }
                }

                //IF( PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningVirkningDatoFom >= DateValue("01/09/2016") AND PE_Vedtaksdata_Kravhode_onsketVirkningsDato >=  DateValue("01/07/2024") AND (PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Mottarminsteytelse = true OR ( PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Minsteytelse_OppfyltUngUfor = true  AND  PE_UT_VilkarGjelderPersonAlder < 20  AND  PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Mottarminsteytelse = true ))   ) THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksdata_beregningsdata_beregningufore_beregningvirkningdatofom().legacyGreaterThanOrEqual(LocalDate.of(2016, 9, 1)) and pe.vedtaksdata_kravhode_onsketvirkningsdato().legacyGreaterThanOrEqual(LocalDate.of(2024, 7, 1)))
                            or (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_minsteytelse_oppfyltungufor() and pe.ut_vilkargjelderpersonalder().lessThan(20))
                ) {
                    //[TBU080V-TBU027V]

                    paragraph {
                        text(
                            bokmal { +"Du er sikret minsteytelse, fordi beregningen ut fra din egenopptjente inntekt er lavere enn minstenivået for uføretrygd. Din sivilstatus påvirker minstesatsen. " },
                            nynorsk { +"Du er sikra minsteyting, fordi berekninga ut frå den eigenopptente inntekta di er lågare enn minstenivået for uføretrygd. Din sivilstatus påverkar minstesatsen. " },
                        )
                        list {
                            item {
                                text(
                                    bokmal { +"Personer som lever sammen med ektefelle, partner eller samboer har en ordinær minstesats lik 2,329 ganger folketrygdens grunnbeløp (G). " },
                                    nynorsk { +"Personar som lever saman med ektefelle, partnar eller sambuar har ein ordinær minstesats lik 2,329 gonger grunnbeløpet i folketrygda (G). " },
                                )
                            }
                            item {
                                text(
                                    bokmal { +"For enslige personer er høy minstesats 2,529 ganger folketrygdens grunnbeløp (G). " },
                                    nynorsk { +"For einslege personar er høg minstesats 2,529 gonger grunnbeløpet i folketrygda (G). " },
                                )
                            }
                            item {
                                text(
                                    bokmal { +"Hvis du har vært samboer i minst 12 av de siste 18 månedene, skal du ha ordinær minstesats 2,329 (G). " },
                                    nynorsk { +"Om du har vore sambuar i minst 12 av dei siste 18 månadene, skal du ha ordinær minstesats 2,329 (G). " },
                                )
                            }
                            item {
                                text(
                                    bokmal { +"Hvis du får barn med samboeren din, skal du ha ordinær minstesats 2,329 (G) fra måneden etter barnet er født. " },
                                    nynorsk { +"Om du får barn med sambuaren din, skal du ha ordinær minstesats 2,329 (G) frå månaden etter barnet er født. " },
                                )
                            }
                        }
                    }

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Minsteytelse_OppfyltUngUfor = true AND PE_UT_VilkarGjelderPersonAlder < 20 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Mottarminsteytelse = true) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_minsteytelse_oppfyltungufor() and pe.ut_vilkargjelderpersonalder().lessThan(20))) {
                        paragraph {
                            text(
                                bokmal { +"Er du innvilget rettighet som ung ufør og lever sammen med ektefelle, partner eller er i et samboerforhold som har vart i minst 12 av de siste 18 månedene, utgjør minste årlige ytelse 2,709 ganger folketrygdens grunnbeløp fra fylte 20 år. Er du enslig og innvilget rettighet som ung ufør, utgjør minste årlige ytelse 2,959 ganger folketrygdens grunnbeløp fra fylte 20 år." },
                                nynorsk { +"Er du innvilga rett som ung ufør og lever saman med ektefelle, partnar eller er i eit sambuarforhold som har vart i minst 12 av dei siste 18 månadene, utgjer minste årlege yting 2,709 gonger grunnbeløpet i folketrygda frå fylte 20 år. Er du einsleg og innvilga rett som ung ufør, utgjer minste årlege yting 2,959 gonger grunnbeløpet i folketrygda frå fylte 20 år." },
                            )
                        }
                    }
                }

                //IF( PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningVirkningDatoFom >= DateValue("01/09/2016") AND PE_Vedtaksdata_Kravhode_onsketVirkningsDato <  DateValue("01/07/2024") AND (PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Mottarminsteytelse = true OR ( PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Minsteytelse_OppfyltUngUfor = true  AND  PE_UT_VilkarGjelderPersonAlder < 20  AND  PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Mottarminsteytelse = true ))   ) THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksdata_beregningsdata_beregningufore_beregningvirkningdatofom().legacyGreaterThanOrEqual(LocalDate.of(2016, 9, 1)) and pe.vedtaksdata_kravhode_onsketvirkningsdato().legacyLessThan(LocalDate.of(2024, 7, 1)))
                            or (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_minsteytelse_oppfyltungufor() and pe.ut_vilkargjelderpersonalder().lessThan(20))
                ) {
                    //[TBU080V-TBU027V]

                    paragraph {

                        text(
                            bokmal { +"Du er sikret minsteytelse fordi beregningen ut fra din egenopptjente inntekt er lavere enn minstenivået for uføretrygd. Bor du sammen med ektefelle, partner eller er i et samboerforhold som har vart i minst 12 av de siste 18 månedene, utgjør minste årlige ytelse 2,28 ganger folketrygdens grunnbeløp. Hvis du får barn med samboeren din, skal uføretrygden utgjøre 2,28 ganger folketrygdens grunnbeløp fra måneden etter at barnet er født. Fra 1. juli 2024 øker denne til 2,329. Bor du sammen med ektefelle, partner eller er i et samboerforhold som har vart i minst 12 av de siste 18 månedene og har uføretrygd beregnet ut fra uførepensjon per 31. desember 2014, utgjør minste årlige ytelse 2,33 ganger folketrygdens grunnbeløp. Fra 1. juli 2024 øker denne til 2,379. Er du enslig, utgjør minste årlige ytelse 2,48 ganger folketrygdens grunnbeløp. Fra 1. juli 2024 øker denne til 2,529. " },
                            nynorsk { +"Du er sikra minsteyting fordi berekninga ut frå den eigenopptente inntekta di er lågare enn minstenivået for uføretrygd. Bur du saman med ektefelle, partnar eller er i eit sambuarforhold som har vart i minst 12 av dei siste 18 månadene, utgjer minste årlege yting 2,28 gonger grunnbeløpet i folketrygda. Dersom du får barn med sambuaren din, skal uføretrygda utgjere 2,28 gonger folketrygdas grunnbeløp frå månaden etter at barnet er født. Frå 1. juli 2024 aukar denne til 2,329. Bur du saman med ektefelle, partnar eller er i eit sambuarforhold som har vart i minst 12 av dei siste 18 månadene og har uføretrygd berekna ut frå uførepensjon per 31. desember 2014, utgjer minste årlege yting 2,33 gonger grunnbeløpet i folketrygda. Frå 1. juli 2024 aukar denne til 2,379. Er du einsleg, utgjer minste årlege yting 2,48 gonger grunnbeløpet. Frå 1. juli 2024 aukar denne til 2,529. " },
                        )

                        //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Minsteytelse_OppfyltUngUfor = true AND PE_UT_VilkarGjelderPersonAlder < 20 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Mottarminsteytelse = true) THEN      INCLUDE ENDIF
                        showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_minsteytelse_oppfyltungufor() and pe.ut_vilkargjelderpersonalder().lessThan(20))) {
                            text(
                                bokmal { +"Er du innvilget rettighet som ung ufør og lever sammen med ektefelle, partner eller er i et samboerforhold som har vart i minst 12 av de siste 18 månedene, utgjør minste årlige ytelse 2,66 ganger folketrygdens grunnbeløp fra fylte 20 år. Fra 1. juli 2024 øker denne til 2,709. Er du enslig og innvilget rettighet som ung ufør, utgjør minste årlige ytelse 2,91 ganger folketrygdens grunnbeløp fra fylte 20 år. Fra 1. juli øker denne til 2,959." },
                                nynorsk { +"Er du innvilga rett som ung ufør og lever saman med ektefelle, partnar eller er i eit sambuarforhold som har vart i minst 12 av dei siste 18 månadene, utgjer minste årlege yting 2,66 gonger grunnbeløpet i folketrygda frå fylte 20 år. Frå 1. juli 2024 aukar denne til 2,709. Er du einsleg og innvilga rett som ung ufør, utgjer minste årlege yting 2,91 gonger grunnbeløpet i folketrygda frå fylte 20 år.  Frå 1. juli 2024 aukar denne til 2,959." },
                            )
                        }
                    }
                }

                //IF(PE_UT_VilkarGjelderPersonAlder >= 20 AND PE_Vedtaksdata_Kravhode_onsketVirkningsDato >= DateValue("01/07/2024") AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Mottarminsteytelse = true AND PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_UngUforResultat(1) = "oppfylt") THEN      INCLUDE ENDIF
                showIf((pe.ut_vilkargjelderpersonalder().greaterThanOrEqual(20) and pe.vedtaksdata_kravhode_onsketvirkningsdato().legacyGreaterThanOrEqual(LocalDate.of(2024, 7, 1)) and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_unguforresultat().equalTo("oppfylt"))) {
                    //[TBU080V-TBU027V]

                    paragraph {
                        text(
                            bokmal { +"Du er innvilget rettighet som ung ufør. Minste årlige ytelse er 2,709 ganger folketrygdens grunnbeløp hvis du lever sammen med ektefelle, partner eller er i et samboerforhold som har vart i minst 12 av de siste 18 månedene. Er du enslig utgjør minste årlige ytelse 2,959 ganger grunnbeløpet." },
                            nynorsk { +"Du er innvilga rett som ung ufør. Minste årlege yting er 2,709 gonger grunnbeløpet i folketrygda dersom du lever saman med ektefelle, partnar eller er i eit sambuarforhold som har vart i minst 12 av de siste 18 månadene. Er du einsleg, utgjer minste årlege yting 2,959 gonger grunnbeløpet." },
                        )
                    }
                }

                //IF(PE_UT_VilkarGjelderPersonAlder >= 20 AND PE_Vedtaksdata_Kravhode_onsketVirkningsDato < DateValue("01/07/2024") AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Mottarminsteytelse = true AND PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_UngUforResultat(1) = "oppfylt") THEN      INCLUDE ENDIF
                showIf((pe.ut_vilkargjelderpersonalder().greaterThanOrEqual(20) and pe.vedtaksdata_kravhode_onsketvirkningsdato().legacyLessThan(LocalDate.of(2024, 7, 1)) and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_unguforresultat().equalTo("oppfylt"))) {
                    //[TBU080V-TBU027V]

                    paragraph {
                        text(
                            bokmal { +"Du er innvilget rettighet som ung ufør. Minste årlige ytelse er 2,66 ganger folketrygdens grunnbeløp hvis du lever sammen med ektefelle, partner eller er i et samboerforhold som har vart i minst 12 av de siste 18 månedene. Fra 1. juli 2024 øker denne til 2,709. Er du enslig utgjør minste årlige ytelse 2,91 ganger grunnbeløpet. Fra 1. juli 2024 øker denne til 2,959." },
                            nynorsk { +"Du er innvilga rett som ung ufør. Minste årlege yting er 2,66 gonger grunnbeløpet i folketrygda dersom du lever saman med ektefelle, partnar eller er i eit sambuarforhold som har vart i minst 12 av de siste 18 månadene. Er du einsleg, utgjer minste årlege yting 2,91 gonger grunnbeløpet. Frå 1. juli 2024 aukar denne til 2,959." },
                        )
                    }
                }
                //[TBU080V-TBU027V]

                showIf(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().equalTo("eos") and bosattUtland) {
                    paragraph {
                        text(
                            bokmal { +"For deg vil minsteytelse utgjøre "+ pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_minsteytelse_sats().format(3) + " ganger folketrygdens grunnbeløp multiplisert med forholdstallet mellom norsk trygdetid og samlet EØS-trygdetid, " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_tttellereos().format() + "/" + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_ttnevnereos().format() + ". Er uføregraden din under 100 prosent, vil minsteytelsen bli justert ut fra uføregraden. Vi justerer også minsteytelsen ut fra trygdetid hvis du har mindre enn 40 års trygdetid. Dersom sivilstanden din endrer seg, kan det medføre at uføretrygden endres. " },
                            nynorsk { +"For deg vil minsteyting utgjere "+ pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_minsteytelse_sats().format(3) + " gonger grunnbeløpet i folketrygda multiplisert med forholdstallet mellom norsk trygdetid og samla EØS-trygdetid, " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_tttellereos().format() + "/" + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_ttnevnereos().format() + ". Er uføregraden din under 100 prosent, vil minsteytinga bli justert ut frå uføregraden. Vi justerer også minsteytinga ut frå trygdetida dersom du har mindre enn 40 års trygdetid. Dersom sivilstanden din endrar seg, kan det gjere at uføretrygda blir endra. " },
                        )
                    }
                }.orShow {
                    paragraph {
                        text(
                            bokmal { +"For deg vil minsteytelse utgjøre " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_minsteytelse_sats().format(3) + " ganger folketrygdens grunnbeløp. Er uføregraden din under 100 prosent, vil minsteytelsen bli justert ut fra uføregraden. Vi justerer også minsteytelsen ut fra trygdetid hvis du har mindre enn 40 års trygdetid. Du må melde fra til Nav dersom sivilstanden din endrer seg, fordi dette kan medføre at uføretrygden endres." },
                            nynorsk { +"For deg vil minsteyting utgjere " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_minsteytelse_sats().format(3) + " gonger grunnbeløpet i folketrygda. Er uføregraden din under 100 prosent, vil misteytinga bli justert ut frå uføregraden. Vi justerer også minsteytinga ut frå trygdetida, dersom du har mindre enn 40 års trygdetid. Du må melde frå til Nav om sivilstanden din endrar seg, dette kan gjere at uføretrygda blir endra." },
                        )
                    }
                }

                //IF(PE_Vedtaksdata_Kravhode_onsketVirkningsDato >= DateValue("01/07/2024") AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Minsteytelse_OppfyltUngUfor = true AND PE_UT_VilkarGjelderPersonAlder < 20 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Mottarminsteytelse = true) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_kravhode_onsketvirkningsdato().legacyGreaterThanOrEqual(LocalDate.of(2024, 7, 1)) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_minsteytelse_oppfyltungufor() and pe.ut_vilkargjelderpersonalder().lessThan(20))) {
                    //[TBU080V-TBU027V]

                    paragraph {
                        text(
                            bokmal { +"Fra måneden du fyller 20 år, vil uføretrygden bli beregnet med rettighet som ung ufør. Beregningen vil da tilsvare " },
                            nynorsk { +"Frå månaden du fyller 20 år, blir uføretrygda berekna med rett som ung ufør. Berekninga tilsvarer då " },
                        )

                        //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = PE_SivilstandAnvendt_bormed_1Bindestrek5 OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = PE_SivilstandAnvendt_bormed_3_2 OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = PE_SivilstandAnvendt_bormed_ektefelle OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = PE_SivilstandAnvendt_bormed_registrert_partner) THEN      INCLUDE ENDIF
                        showIf((pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("bormed 1-5") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("bormed 3-2") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("bormed ektefelle") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("bormed registrert partner"))) {
                            text(
                                bokmal { +"2,709" },
                                nynorsk { +"2,709" },
                            )
                        }
                        text(
                            bokmal { +" " },
                            nynorsk { +" " },
                        )

                        //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt <> PE_SivilstandAnvendt_bormed_1Bindestrek5 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt <> PE_SivilstandAnvendt_bormed_3_2 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt <> PE_SivilstandAnvendt_bormed_ektefelle AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt <> PE_SivilstandAnvendt_bormed_registrert_partner) THEN      INCLUDE ENDIF
                        showIf((pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().notEqualTo("bormed 1-5") and pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().notEqualTo("bormed 3-2") and pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().notEqualTo("bormed ektefelle") and pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().notEqualTo("bormed registrert partner"))) {
                            text(
                                bokmal { +"2,959" },
                                nynorsk { +"2,959" },
                            )
                        }
                        text(
                            bokmal { +" ganger folketrygdens grunnbeløp. Denne minsteytelsen vil også bli justert ut fra uføregraden din." },
                            nynorsk { +" gonger grunnbeløpet i folketrygda. Denne minsteytinga vil også bli justert ut frå uføregraden din." },
                        )
                    }
                }

                //IF(PE_Vedtaksdata_Kravhode_onsketVirkningsDato < DateValue("01/07/2024") AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Minsteytelse_OppfyltUngUfor = true AND PE_UT_VilkarGjelderPersonAlder < 20 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Mottarminsteytelse = true) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_kravhode_onsketvirkningsdato().legacyLessThan(LocalDate.of(2024, 7, 1)) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_minsteytelse_oppfyltungufor() and pe.ut_vilkargjelderpersonalder().lessThan(20))) {
                    //[TBU080V-TBU027V]

                    paragraph {
                        text(
                            bokmal { +"Fra måneden du fyller 20 år, vil uføretrygden bli beregnet med rettighet som ung ufør. Beregningen vil da tilsvare " },
                            nynorsk { +"Frå månaden du fyller 20 år, blir uføretrygda berekna med rett som ung ufør. Berekninga tilsvarer då " },
                        )

                        //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = PE_SivilstandAnvendt_bormed_1Bindestrek5 OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = PE_SivilstandAnvendt_bormed_3_2 OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = PE_SivilstandAnvendt_bormed_ektefelle OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = PE_SivilstandAnvendt_bormed_registrert_partner) THEN      INCLUDE ENDIF
                        showIf((pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("bormed 1-5") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("bormed 3-2") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("bormed ektefelle") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("bormed registrert partner"))) {
                            text(
                                bokmal { +"2,66" },
                                nynorsk { +"2,66" },
                            )
                        }

                        //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt <> PE_SivilstandAnvendt_bormed_1Bindestrek5 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt <> PE_SivilstandAnvendt_bormed_3_2 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt <> PE_SivilstandAnvendt_bormed_ektefelle AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt <> PE_SivilstandAnvendt_bormed_registrert_partner) THEN      INCLUDE ENDIF
                        showIf((pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().notEqualTo("bormed 1-5") and pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().notEqualTo("bormed 3-2") and pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().notEqualTo("bormed ektefelle") and pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().notEqualTo("bormed registrert partner"))) {
                            text(
                                bokmal { +"2,91" },
                                nynorsk { +"2,91" },
                            )
                        }
                        text(
                            bokmal { +" ganger folketrygdens grunnbeløp. Denne minsteytelsen vil også bli justert ut fra uføregraden din. Fra 1.juli 2024 vil beregningen tilsvare 2,709/2,959 ganger folketrygdens grunnbeløp." },
                            nynorsk { +" gonger grunnbeløpet i folketrygda. Denne minsteytinga vil også bli justert ut frå uføregraden din. Frå 1. juli 2024 aukar denne til 2,709/2,959 gonger grunnbeløpet i folketrygda." },
                        )
                    }
                }
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_FortsattMedlemskap_InngangUnntak = "halv_minpen_ufp_ut") THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_fortsattmedlemskap_inngangunntak().equalTo("halv_minpen_ufp_ut"))) {
                //[TBU080V-TBU027V]

                paragraph {
                    text(
                        bokmal { +"Du er innvilget uføretrygd fordi du har tjent opp rett til halvparten av full minsteytelse for uføretrygd. Uføretrygden din er derfor beregnet etter den faktiske trygdetiden din i Norge. Du kan se den faktiske trygdetiden din i tabellen " + quoted("Trygdetiden din i Norge") + ", og hvilke opplysninger vi har brukt i beregningen i tabellen " + quoted("Opplysninger vi har brukt i beregningen") + "." },
                        nynorsk { +"Du er innvilga uføretrygd fordi du har tent opp rett til halvparten av full minsteyting for uføretrygd. Uføretrygda di er derfor berekna etter den faktiske trygdetida di i Noreg. Du kan sjå den faktiske trygdetida di i tabellen " + quoted("Trygdetida di i Noreg") + ", og kva opplysningar vi har brukt i berekninga i tabellen " + quoted("Opplysningar vi har brukt i berekninga") + "." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode = "eos" AND PE_Vedtaksbrev_Vedtaksdata_VilkarsVedtak_Vilkar_MedlemskapForUTEtterTrygdeavtaler_OppfyltVedSammenlegging = false) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().equalTo("eos")
                    and not(pe.vedtaksbrev_vedtaksdata_vilkarsvedtak_vilkar_medlemskapforutettertrygdeavtaler_oppfyltvedsammenlegging()))) {
                //[TBU080V-TBU027V]

                paragraph {
                    text(
                        bokmal { +"Vi beregner uføretrygden din etter bestemmelsene i EØS-avtalen fordi det gir deg en høyere uføretrygd enn en beregning etter folketrygdlovens regler. Uføretrygden din er først beregnet etter samlet opptjening i Norge og andre EØS-land. Deretter er uføretrygden multiplisert med et forholdstall, som angir forholdet mellom faktisk opptjeningstid i Norge og samlet faktisk opptjening i Norge og andre EØS-land. Du kan se hvilke opplysninger vi har brukt i beregningen i tabellen " + quoted("Opplysninger vi har brukt i beregningen") + "." },
                        nynorsk { +"Vi bereknar uføretrygda di etter reglane i EØS-avtalen fordi det gir deg ei høgare uføretrygd enn ei berekning etter reglane i folketrygdlova. Uføretrygda di er først berekna etter samla opptening i Noreg og andre EØS-land. Deretter er uføretrygda multiplisert med eit forholdstal som angir forholdet mellom faktisk oppteningstid i Noreg og samla faktisk opptening i Noreg og andre EØS-land. Du kan sjå kva opplysningar vi har brukt i berekninga i tabellen " + quoted("Opplysningar vi har brukt i berekninga") + "." },
                    )
                }
            }

            //IF( PE_Vedtaksbrev_Vedtaksdata_VilkarsVedtak_Vilkar_MedlemskapForUTEtterTrygdeavtaler_OppfyltVedSammenlegging = true  AND   (PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode = "eos"  OR  PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode = "nordisk")  ) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_vilkarsvedtak_vilkar_medlemskapforutettertrygdeavtaler_oppfyltvedsammenlegging()
                    and (pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().equalTo("eos") or pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().equalTo("nordisk")))) {
                //[TBU080V-TBU027V]

                paragraph {
                    text(
                        bokmal { +"Vi beregner uføretrygden din etter bestemmelsene i EØS-avtalen fordi vilkårene for rett til uføretrygd er oppfylt ved sammenlegging av perioder med opptjening i Norge og andre EØS-land. Uføretrygden din er først beregnet etter samlet opptjening i Norge og andre EØS-land. Deretter er uføretrygden multiplisert med et forholdstall, som angir forholdet mellom faktisk opptjeningstid i Norge og samlet faktisk opptjening i Norge og andre EØS-land. Du kan se hvilke opplysninger vi har brukt i beregningen i tabellen " + quoted("Opplysninger vi har brukt i beregningen") + "." },
                        nynorsk { +"Vi bereknar uføretrygda di etter reglane i EØS-avtalen fordi vilkåra for rett til uføretrygd er oppfylte når vi legg saman periodar med opptening i Noreg og andre EØS-land. Uføretrygda di er først berekna etter samla opptening i Noreg og andre EØS-land. Deretter er uføretrygda multiplisert med eit forholdstal som angir forholdet mellom faktisk oppteningstid i Noreg og samla faktisk opptening i Noreg og andre EØS-land. Du kan sjå kva opplysningar vi har brukt i berekninga i tabellen " + quoted("Opplysningar vi har brukt i berekninga") + "." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode <> "eos" AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode <> "folketrygd" AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode <> "nordisk" AND PE_Vedtaksbrev_Vedtaksdata_VilkarsVedtak_Vilkar_MedlemskapForUTEtterTrygdeavtaler_OppfyltVedSammenlegging = true) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().notEqualTo("eos")
                    and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().notEqualTo("folketrygd")
                    and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().notEqualTo("nordisk")
                    and pe.vedtaksbrev_vedtaksdata_vilkarsvedtak_vilkar_medlemskapforutettertrygdeavtaler_oppfyltvedsammenlegging())) {
                //[TBU080V-TBU027V]

                paragraph {
                    text(
                        bokmal { +"Vi beregner uføretrygden din etter bestemmelsene i trygdeavtalen med " + pe.grunnlag_persongrunnlagsliste_trygdeavtaler_avtaleland() + " fordi vilkårene for rett til uføretrygd er oppfylt ved sammenlegging av opptjeningstid i Norge og " + pe.grunnlag_persongrunnlagsliste_trygdeavtaler_avtaleland() + ". Uføretrygden din er først beregnet etter samlet opptjening i Norge og " + pe.grunnlag_persongrunnlagsliste_trygdeavtaler_avtaleland() + ". Deretter er uføretrygden multiplisert med et forholdstall, som angir forholdet mellom faktisk opptjeningstid i Norge og samlet faktisk opptjening i Norge og " + pe.grunnlag_persongrunnlagsliste_trygdeavtaler_avtaleland() + ". Du kan se hvilke opplysninger vi har brukt i beregningen i tabellen " + quoted("Opplysninger vi har brukt i beregningen") + "." },
                        nynorsk { +"Vi bereknar uføretrygda di etter reglane i trygdeavtalen med " + pe.grunnlag_persongrunnlagsliste_trygdeavtaler_avtaleland() + " fordi vilkåra for rett til uføretrygd er oppfylt når ein legg saman oppteningstid i Noreg og " + pe.grunnlag_persongrunnlagsliste_trygdeavtaler_avtaleland() + ". Uføretrygda di er først berekna etter samla opptening i Noreg og " + pe.grunnlag_persongrunnlagsliste_trygdeavtaler_avtaleland() + ". Deretter er uføretrygda multiplisert med eit forholdstal som angir forholdet mellom faktisk oppteningstid i Noreg og samla faktisk opptening i Noreg og " + pe.grunnlag_persongrunnlagsliste_trygdeavtaler_avtaleland() + ". Du kan sjå kva opplysningar vi har brukt i berekninga i tabellen " + quoted("Opplysningar vi har brukt i berekninga") + "." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode = "folketrygd" AND PE_Vedtaksdata_Kravhode_VurdereTrygdeavtale = true) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().equalTo("folketrygd") and pe.vedtaksdata_kravhode_vurderetrygdeavtale())) {
                //[TBU080V-TBU027V]

                paragraph {
                    text(
                        bokmal { +"Vi beregner uføretrygden din etter folketrygdlovens regler fordi dette gir deg en høyere beregning enn etter trygdeavtale. Uføretrygden din er beregnet etter samlet opptjening i Norge. Du kan se hvilke opplysninger vi har brukt i beregningen i tabellen " + quoted("Opplysninger vi har brukt i beregningen") + "." },
                        nynorsk { +"Vi bereknar uføretrygda di etter reglane i folketrygdlova fordi dette gir deg ei høgare berekning enn etter trygdeavtale. Uføretrygda di er berekna etter samla opptening i Noreg. Du kan sjå kva opplysningar vi har brukt i berekninga i tabellen " + quoted("Opplysningar vi har brukt i berekninga") + "." },
                    )
                }
            }
        }
    }
}
