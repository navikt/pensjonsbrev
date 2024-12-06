package no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.legacy.grunnlag_persongrunnlagsliste_trygdeavtaler_avtaleland
import no.nav.pensjon.brev.maler.legacy.pebrevkode
import no.nav.pensjon.brev.maler.legacy.ut_vilkargjelderpersonalder
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_vilkarsvedtak_vilkar_medlemskapforutettertrygdeavtaler_oppfyltvedsammenlegging
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_fortsattmedlemskap_inngangunntak
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningvirkningdatofom
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_belopsgrense
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_minsteytelse_oppfyltungufor
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_minsteytelse_sats
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_mottarminsteytelse
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_kravhode_kravarsaktype
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_kravhode_onsketvirkningsdato
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_kravhode_vurderetrygdeavtale
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_unguforresultat
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.greaterThanOrEqual
import no.nav.pensjon.brev.template.dsl.expression.legacyGreaterThanOrEqual
import no.nav.pensjon.brev.template.dsl.expression.legacyLessThan
import no.nav.pensjon.brev.template.dsl.expression.lessThan
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.notEqualTo
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import java.time.LocalDate

data class TBU080V_TBU027V(
    val pe: Expression<PE>,
): OutlinePhrase<LangBokmalNynorskEnglish>(){
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {

        //IF(PE_pebrevkode <> "PE_UT_07_100" AND PE_pebrevkode <> "PE_UT_05_100" AND PE_pebrevkode <> "PE_UT_04_300" AND PE_pebrevkode <> "PE_UT_14_300" AND PE_pebrevkode <> "PE_UT_04_103" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_pebrevkode <> "PE_UT_04_108" AND PE_pebrevkode <> "PE_UT_04_109" AND PE_pebrevkode <> "PE_UT_07_200" AND PE_pebrevkode <> "PE_UT_06_300") THEN      INCLUDE ENDIF
        showIf((pe.pebrevkode().notEqualTo("PE_UT_07_100") and pe.pebrevkode().notEqualTo("PE_UT_05_100") and pe.pebrevkode().notEqualTo("PE_UT_04_300") and pe.pebrevkode().notEqualTo("PE_UT_14_300") and pe.pebrevkode().notEqualTo("PE_UT_04_103") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.pebrevkode().notEqualTo("PE_UT_04_108") and pe.pebrevkode().notEqualTo("PE_UT_04_109") and pe.pebrevkode().notEqualTo("PE_UT_07_200") and pe.pebrevkode().notEqualTo("PE_UT_06_300"))){


            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Belopsgrense = 60000 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Mottarminsteytelse = true) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_belopsgrense().equalTo(60000) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_mottarminsteytelse())){
                //[TBU080V-TBU027V]

                title1 {
                    text (
                        Bokmal to "For deg som har rett til minsteytelse",
                        Nynorsk to "For deg som har rett til minsteyting",
                        English to "You have been granted minimum benefit",
                    )
                }
            }

            //IF( PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningVirkningDatoFom < DateValue("01/09/2016") AND PE_Vedtaksdata_Kravhode_onsketVirkningsDato >=  DateValue("01/07/2024") AND (PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Mottarminsteytelse = true OR ( PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Minsteytelse_OppfyltUngUfor = true  AND  PE_UT_VilkarGjelderPersonAlder < 20  AND  PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Mottarminsteytelse = true ))   ) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningvirkningdatofom().legacyLessThan(LocalDate.of(2016,9,1)) and pe.vedtaksdata_kravhode_onsketvirkningsdato().legacyGreaterThanOrEqual(LocalDate.of(2024,7,1)) and (pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_mottarminsteytelse() or (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_minsteytelse_oppfyltungufor() and pe.ut_vilkargjelderpersonalder().lessThan(20) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_mottarminsteytelse())))){
                //[TBU080V-TBU027V]

                paragraph {

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Mottarminsteytelse = true) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_mottarminsteytelse())){
                        text (
                            Bokmal to "Du er sikret minsteytelse fordi beregningen ut fra din egenopptjente inntekt er lavere enn minstenivået for uføretrygd. Minste årlige ytelse er 2,329 ganger folketrygdens grunnbeløp for personer som lever sammen med ektefelle, partner eller er i et samboerforhold som har vart i minst 12 av de siste 18 månedene. Er du enslig utgjør minste årlige ytelse 2,529 ganger grunnbeløpet.",
                            Nynorsk to "Du er sikra minsteyting fordi berekninga ut frå den eigenopptente inntekta di er lågare enn minstenivået for uføretrygd. Minste årlege yting er 2,329 gonger grunnbeløpet i folketrygda for personar som lever saman med ektefelle, partnar eller er i eit sambuarforhold som har vart i minst 12 av dei siste 18 månadene. Er du einsleg, utgjer minste årlege yting 2,529 gonger grunnbeløpet.",
                            English to "You are eligible for the minimum benefit, because the calculated benefit based on your income is lower than the minimum benefit. The minimum benefit is 2.329 times the National Insurance basic amount for individuals living with a spouse or partner, or in a cohabitant relationship that has lasted no less than 12 of the last 18 months. If you are single, the minimum benefit is 2.529 times the basic amount. ",
                        )
                    }

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Minsteytelse_OppfyltUngUfor = true AND PE_UT_VilkarGjelderPersonAlder < 20 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Mottarminsteytelse = true) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_minsteytelse_oppfyltungufor() and pe.ut_vilkargjelderpersonalder().lessThan(20) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_mottarminsteytelse())){
                        text (
                            Bokmal to " Er du innvilget rettighet som ung ufør, er minste årlige ytelse, fra fylte 20 år, 2,709 ganger folketrygdens grunnbeløp hvis du lever sammen med ektefelle, partner eller er i et samboerforhold som har vart i minst 12 av de siste 18 månedene. Er du enslig utgjør minste årlige ytelse 2,959 ganger grunnbeløpet.",
                            Nynorsk to " Er du innvilga rett som ung ufør, er minste årlege yting, frå fylte 20 år, 2,709 gonger grunnbeløpet i folketrygda dersom du lever saman med ektefelle, partnar eller er i eit sambuarforhold som har vart i minst 12 av de siste 18 månadene. Er du einsleg, utgjer minste årlege yting 2,959 gonger grunnbeløpet.",
                            English to "If you are a young disabled individual, you are entitled to a minimum benefit 2.709 times the National Insurance basic amount from the age of 20, if you are living with a spouse or partner, or in a cohabitant relationship that has lasted no less than 12 of the last 18 months. If you are single, the minimum benefit is 2.959 times the basic amount.",
                        )
                    }
                }
            }

            //IF( PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningVirkningDatoFom < DateValue("01/09/2016") AND PE_Vedtaksdata_Kravhode_onsketVirkningsDato <  DateValue("01/07/2024") AND (PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Mottarminsteytelse = true OR ( PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Minsteytelse_OppfyltUngUfor = true  AND  PE_UT_VilkarGjelderPersonAlder < 20  AND  PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Mottarminsteytelse = true ))   ) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningvirkningdatofom().legacyLessThan(LocalDate.of(2016,9,1)) and pe.vedtaksdata_kravhode_onsketvirkningsdato().legacyLessThan(LocalDate.of(2024,7,1)) and (pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_mottarminsteytelse() or (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_minsteytelse_oppfyltungufor() and pe.ut_vilkargjelderpersonalder().lessThan(20) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_mottarminsteytelse())))){
                //[TBU080V-TBU027V]

                paragraph {

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Mottarminsteytelse = true) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_mottarminsteytelse())){
                        text (
                            Bokmal to "Du er sikret minsteytelse fordi beregningen ut fra din egenopptjente inntekt er lavere enn minstenivået for uføretrygd. Minste årlige ytelse er 2,28 ganger folketrygdens grunnbeløp for personer som lever sammen med ektefelle, partner eller er i et samboerforhold som har vart i minst 12 av de siste 18 månedene. Fra 1. juli 2024 øker denne til 2,329. Er du enslig utgjør minste årlige ytelse 2,48 ganger grunnbeløpet. Fra 1. juli 2024 øker denne til 2,529.",
                            Nynorsk to "Du er sikra minsteyting fordi berekninga ut frå den eigenopptente inntekta di er lågare enn minstenivået for uføretrygd. Minste årlege yting er 2,28 gonger grunnbeløpet i folketrygda for personar som lever saman med ektefelle, partnar eller er i eit sambuarforhold som har vart i minst 12 av dei siste 18 månadene. Frå 1. juli 2024 aukar denne til 2,329. Er du einsleg, utgjer minste årlege yting 2,48 gonger grunnbeløpet. Frå 1. juli 2024 aukar denne til 2,529.",
                            English to "You are eligible for the minimum benefit, because the calculated benefit based on your income is lower than the minimum benefit. The minimum benefit is 2.28 times the National Insurance basic amount for individuals living with a spouse or partner, or in a cohabitant relationship that has lasted no less than 12 of the last 18 months. From 1 July 2024 this increases to 2,329. If you are single, the minimum benefit is 2.48 times the basic amount. From 1 July 2024 this increases to 2,529. ",
                        )
                    }

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Minsteytelse_OppfyltUngUfor = true AND PE_UT_VilkarGjelderPersonAlder < 20 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Mottarminsteytelse = true) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_minsteytelse_oppfyltungufor() and pe.ut_vilkargjelderpersonalder().lessThan(20) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_mottarminsteytelse())){
                        text (
                            Bokmal to " Er du innvilget rettighet som ung ufør, er minste årlige ytelse, fra fylte 20 år, 2,66 ganger folketrygdens grunnbeløp hvis du lever sammen med ektefelle, partner eller er i et samboerforhold som har vart i minst 12 av de siste 18 månedene. Fra 1. juli 2024 øker denne til 2,709. Er du enslig utgjør minste årlige ytelse 2,91 ganger grunnbeløpet. Fra 1. juli øker denne til 2,959.",
                            Nynorsk to " Er du innvilga rett som ung ufør, er minste årlege yting, frå fylte 20 år, 2,66 gonger grunnbeløpet i folketrygda dersom du lever saman med ektefelle, partnar eller er i eit sambuarforhold som har vart i minst 12 av de siste 18 månadene. Frå 1. juli 2024 aukar denne til 2,709. Er du einsleg, utgjer minste årlege yting 2,91 gonger grunnbeløpet. Frå 1. juli 2024 aukar denne til 2,959.",
                            English to "If you are a young disabled individual, you are entitled to a minimum benefit 2.66 times the National Insurance basic amount from the age of 20, if you are living with a spouse or partner, or in a cohabitant relationship that has lasted no less than 12 of the last 18 months. If you are single, the minimum benefit is 2.91 times the basic amount.  From 1 July 2024 this increases to 2,959.",
                        )
                    }
                }
            }

            //IF( PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningVirkningDatoFom >= DateValue("01/09/2016") AND PE_Vedtaksdata_Kravhode_onsketVirkningsDato >=  DateValue("01/07/2024") AND (PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Mottarminsteytelse = true OR ( PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Minsteytelse_OppfyltUngUfor = true  AND  PE_UT_VilkarGjelderPersonAlder < 20  AND  PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Mottarminsteytelse = true ))   ) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningvirkningdatofom().legacyGreaterThanOrEqual(LocalDate.of(2016,9,1)) and pe.vedtaksdata_kravhode_onsketvirkningsdato().legacyGreaterThanOrEqual(LocalDate.of(2024,7,1)) and (pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_mottarminsteytelse() or (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_minsteytelse_oppfyltungufor() and pe.ut_vilkargjelderpersonalder().lessThan(20) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_mottarminsteytelse())))){
                //[TBU080V-TBU027V]

                paragraph {

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Mottarminsteytelse = true) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_mottarminsteytelse())){
                        text (
                            Bokmal to "Du er sikret minsteytelse fordi beregningen ut fra din egenopptjente inntekt er lavere enn minstenivået for uføretrygd. Bor du sammen med ektefelle, partner eller er i et samboerforhold som har vart i minst 12 av de siste 18 månedene, utgjør minste årlige ytelse 2,329 ganger folketrygdens grunnbeløp. Hvis du får barn med samboeren din, skal uføretrygden utgjøre 2,329 ganger folketrygdens grunnbeløp fra måneden etter at barnet er født.Bor du sammen med ektefelle, partner eller er i et samboerforhold som har vart i minst 12 av de siste 18 månedene og har uføretrygd beregnet ut fra uførepensjon per 31. desember 2014, utgjør minste årlige ytelse 2,379 ganger folketrygdens grunnbeløp. Er du enslig, utgjør minste årlige ytelse 2,529 ganger folketrygdens grunnbeløp. ",
                            Nynorsk to "Du er sikra minsteyting fordi berekninga ut frå den eigenopptente inntekta di er lågare enn minstenivået for uføretrygd. Bur du saman med ektefelle, partnar eller er i eit sambuarforhold som har vart i minst 12 av dei siste 18 månadene, utgjer minste årlege yting 2,329 gonger grunnbeløpet i folketrygda. Dersom du får barn med sambuaren din, skal uføretrygda utgjere 2,329 gonger folketrygdas grunnbeløp frå månaden etter at barnet er født.Bur du saman med ektefelle, partnar eller er i eit sambuarforhold som har vart i minst 12 av dei siste 18 månadene og har uføretrygd berekna ut frå uførepensjon per 31. desember 2014, utgjer minste årlege yting 2,379 gonger grunnbeløpet i folketrygda. Er du einsleg, utgjer minste årlege yting 2,529 gonger grunnbeløpet. ",
                            English to "You are eligible for the minimum benefit, because the calculated benefit based on your income is lower than the minimum benefit. The minimum benefit is 2.329 times the National Insurance basic amount for individuals living with a spouse or partner, or in a cohabitant relationship that has lasted no less than 12 of the last 18 months. If you have a child with your cohabitant, the minimum benefit is 2.329 times the National Insurance basic amount from the month after the child is born.The minimum benefit is 2.379 times the National Insurance basic amount for individuals living with a spouse or partner, or in a cohabitant relationship that has lasted no less than 12 of the last 18 months, and where the disability benefit is calculated pursuant to Section 3 of the Norwegian National Insurance Act. If you are single, the minimum benefit is 2.529 times the basic amount. ",
                        )
                    }

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Minsteytelse_OppfyltUngUfor = true AND PE_UT_VilkarGjelderPersonAlder < 20 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Mottarminsteytelse = true) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_minsteytelse_oppfyltungufor() and pe.ut_vilkargjelderpersonalder().lessThan(20) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_mottarminsteytelse())){
                        text (
                            Bokmal to "Er du innvilget rettighet som ung ufør og lever sammen med ektefelle, partner eller er i et samboerforhold som har vart i minst 12 av de siste 18 månedene, utgjør minste årlige ytelse 2,709 ganger folketrygdens grunnbeløp fra fylte 20 år. Er du enslig og innvilget rettighet som ung ufør, utgjør minste årlige ytelse 2,959 ganger folketrygdens grunnbeløp fra fylte 20 år.",
                            Nynorsk to "Er du innvilga rett som ung ufør og lever saman med ektefelle, partnar eller er i eit sambuarforhold som har vart i minst 12 av dei siste 18 månadene, utgjer minste årlege yting 2,709 gonger grunnbeløpet i folketrygda frå fylte 20 år. Er du einsleg og innvilga rett som ung ufør, utgjer minste årlege yting 2,959 gonger grunnbeløpet i folketrygda frå fylte 20 år.",
                            English to "If you are a young disabled individual, and you are living with a spouse or partner, or in a cohabitant relationship that has lasted no less than 12 of the last 18 months, you are entitled to a minimum benefit 2.709 times the National Insurance basic amount from the age of 20. If you are young disabled and single, the minimum benefit is 2.959 times the basic amount from the age of 20.",
                        )
                    }
                }
            }

            //IF( PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningVirkningDatoFom >= DateValue("01/09/2016") AND PE_Vedtaksdata_Kravhode_onsketVirkningsDato <  DateValue("01/07/2024") AND (PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Mottarminsteytelse = true OR ( PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Minsteytelse_OppfyltUngUfor = true  AND  PE_UT_VilkarGjelderPersonAlder < 20  AND  PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Mottarminsteytelse = true ))   ) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningvirkningdatofom().legacyGreaterThanOrEqual(LocalDate.of(2016,9,1)) and pe.vedtaksdata_kravhode_onsketvirkningsdato().legacyLessThan(LocalDate.of(2024,7,1)) and (pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_mottarminsteytelse() or (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_minsteytelse_oppfyltungufor() and pe.ut_vilkargjelderpersonalder().lessThan(20) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_mottarminsteytelse())))){
                //[TBU080V-TBU027V]

                paragraph {

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Mottarminsteytelse = true) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_mottarminsteytelse())){
                        text (
                            Bokmal to "Du er sikret minsteytelse fordi beregningen ut fra din egenopptjente inntekt er lavere enn minstenivået for uføretrygd. Bor du sammen med ektefelle, partner eller er i et samboerforhold som har vart i minst 12 av de siste 18 månedene, utgjør minste årlige ytelse 2,28 ganger folketrygdens grunnbeløp. Hvis du får barn med samboeren din, skal uføretrygden utgjøre 2,28 ganger folketrygdens grunnbeløp fra måneden etter at barnet er født. Fra 1. juli 2024 øker denne til 2,329.Bor du sammen med ektefelle, partner eller er i et samboerforhold som har vart i minst 12 av de siste 18 månedene og har uføretrygd beregnet ut fra uførepensjon per 31. desember 2014, utgjør minste årlige ytelse 2,33 ganger folketrygdens grunnbeløp. Fra 1. juli 2024 øker denne til 2,379. Er du enslig, utgjør minste årlige ytelse 2,48 ganger folketrygdens grunnbeløp. Fra 1. juli 2024 øker denne til 2,529. ",
                            Nynorsk to "Du er sikra minsteyting fordi berekninga ut frå den eigenopptente inntekta di er lågare enn minstenivået for uføretrygd. Bur du saman med ektefelle, partnar eller er i eit sambuarforhold som har vart i minst 12 av dei siste 18 månadene, utgjer minste årlege yting 2,28 gonger grunnbeløpet i folketrygda. Dersom du får barn med sambuaren din, skal uføretrygda utgjere 2,28 gonger folketrygdas grunnbeløp frå månaden etter at barnet er født. Frå 1. juli 2024 aukar denne til 2,329.Bur du saman med ektefelle, partnar eller er i eit sambuarforhold som har vart i minst 12 av dei siste 18 månadene og har uføretrygd berekna ut frå uførepensjon per 31. desember 2014, utgjer minste årlege yting 2,33 gonger grunnbeløpet i folketrygda. Frå 1. juli 2024 aukar denne til 2,379. Er du einsleg, utgjer minste årlege yting 2,48 gonger grunnbeløpet. Frå 1. juli 2024 aukar denne til 2,529. ",
                            English to "You are eligible for the minimum benefit, because the calculated benefit based on your income is lower than the minimum benefit. The minimum benefit is 2.28 times the National Insurance basic amount for individuals living with a spouse or partner, or in a cohabitant relationship that has lasted no less than 12 of the last 18 months. If you have a child with your cohabitant, the minimum benefit is 2.28 times the National Insurance basic amount from the month after the child is born. From 1 July 2024 this increases to 2,329.The minimum benefit is 2.33 times the National Insurance basic amount for individuals living with a spouse or partner, or in a cohabitant relationship that has lasted no less than 12 of the last 18 months, and where the disability benefit is calculated pursuant to Section 3 of the Norwegian National Insurance Act. From 1 July 2024 this increases to 2,379. If you are single, the minimum benefit is 2.48 times the basic amount. From 1 July 2024 this increases to 2,529. ",
                        )
                    }

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Minsteytelse_OppfyltUngUfor = true AND PE_UT_VilkarGjelderPersonAlder < 20 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Mottarminsteytelse = true) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_minsteytelse_oppfyltungufor() and pe.ut_vilkargjelderpersonalder().lessThan(20) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_mottarminsteytelse())){
                        text (
                            Bokmal to "Er du innvilget rettighet som ung ufør og lever sammen med ektefelle, partner eller er i et samboerforhold som har vart i minst 12 av de siste 18 månedene, utgjør minste årlige ytelse 2,66 ganger folketrygdens grunnbeløp fra fylte 20 år. Fra 1. juli 2024 øker denne til 2,709. Er du enslig og innvilget rettighet som ung ufør, utgjør minste årlige ytelse 2,91 ganger folketrygdens grunnbeløp fra fylte 20 år. Fra 1. juli øker denne til 2,959.",
                            Nynorsk to "Er du innvilga rett som ung ufør og lever saman med ektefelle, partnar eller er i eit sambuarforhold som har vart i minst 12 av dei siste 18 månadene, utgjer minste årlege yting 2,66 gonger grunnbeløpet i folketrygda frå fylte 20 år. Frå 1. juli 2024 aukar denne til 2,709. Er du einsleg og innvilga rett som ung ufør, utgjer minste årlege yting 2,91 gonger grunnbeløpet i folketrygda frå fylte 20 år.  Frå 1. juli 2024 aukar denne til 2,959.",
                            English to "If you are a young disabled individual, and you are living with a spouse or partner, or in a cohabitant relationship that has lasted no less than 12 of the last 18 months, you are entitled to a minimum benefit 2.66 times the National Insurance basic amount from the age of 20. From 1 July 2024 this increases to 2,709. If you are young disabled and single, the minimum benefit is 2.91 times the basic amount from the age of 20. From 1 July 2024 this increases to 2,959.",
                        )
                    }
                }
            }

            //IF(PE_UT_VilkarGjelderPersonAlder >= 20 AND PE_Vedtaksdata_Kravhode_onsketVirkningsDato >= DateValue("01/07/2024") AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Mottarminsteytelse = true AND PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_UngUforResultat(1) = "oppfylt") THEN      INCLUDE ENDIF
            showIf((pe.ut_vilkargjelderpersonalder().greaterThanOrEqual(20) and pe.vedtaksdata_kravhode_onsketvirkningsdato().legacyGreaterThanOrEqual(LocalDate.of(2024,7,1)) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_mottarminsteytelse() and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_unguforresultat().equalTo("oppfylt"))){
                //[TBU080V-TBU027V]

                paragraph {
                    text (
                        Bokmal to "Du er innvilget rettighet som ung ufør. Minste årlige ytelse er 2,709 ganger folketrygdens grunnbeløp hvis du lever sammen med ektefelle, partner eller er i et samboerforhold som har vart i minst 12 av de siste 18 månedene. Er du enslig utgjør minste årlige ytelse 2,959 ganger grunnbeløpet.",
                        Nynorsk to "Du er innvilga rett som ung ufør. Minste årlege yting er 2,709 gonger grunnbeløpet i folketrygda dersom du lever saman med ektefelle, partnar eller er i eit sambuarforhold som har vart i minst 12 av de siste 18 månadene. Er du einsleg, utgjer minste årlege yting 2,959 gonger grunnbeløpet.",
                        English to "As a young disabled individual, you have certain rights. The minimum benefit is 2.709 times the National Insurance basic amount if you are living with a spouse or partner, or in a cohabitant relationship that has lasted no less than 12 of the last 18 months. If you are single, the minimum benefit is 2.959 times the basic amount.",
                    )
                }
            }

            //IF(PE_UT_VilkarGjelderPersonAlder >= 20 AND PE_Vedtaksdata_Kravhode_onsketVirkningsDato < DateValue("01/07/2024") AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Mottarminsteytelse = true AND PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_UngUforResultat(1) = "oppfylt") THEN      INCLUDE ENDIF
            showIf((pe.ut_vilkargjelderpersonalder().greaterThanOrEqual(20) and pe.vedtaksdata_kravhode_onsketvirkningsdato().legacyLessThan(LocalDate.of(2024,7,1)) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_mottarminsteytelse() and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_unguforresultat().equalTo("oppfylt"))){
                //[TBU080V-TBU027V]

                paragraph {
                    text (
                        Bokmal to "Du er innvilget rettighet som ung ufør. Minste årlige ytelse er 2,66 ganger folketrygdens grunnbeløp hvis du lever sammen med ektefelle, partner eller er i et samboerforhold som har vart i minst 12 av de siste 18 månedene. Fra 1. juli 2024 øker denne til 2,709. Er du enslig utgjør minste årlige ytelse 2,91 ganger grunnbeløpet. Fra 1. juli 2024 øker denne til 2,959.",
                        Nynorsk to "Du er innvilga rett som ung ufør. Minste årlege yting er 2,66 gonger grunnbeløpet i folketrygda dersom du lever saman med ektefelle, partnar eller er i eit sambuarforhold som har vart i minst 12 av de siste 18 månadene. Er du einsleg, utgjer minste årlege yting 2,91 gonger grunnbeløpet. Frå 1. juli 2024 aukar denne til 2,959.",
                        English to "As a young disabled individual, you have certain rights. The minimum benefit is 2.66 times the National Insurance basic amount if you are living with a spouse or partner, or in a cohabitant relationship that has lasted no less than 12 of the last 18 months. From 1 July 2024 this increases to 2,709. If you are single, the minimum benefit is 2.91 times the basic amount. From 1 July 2024 this increases to 2,959.",
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Mottarminsteytelse = true) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_mottarminsteytelse())){
                //[TBU080V-TBU027V]

                paragraph {
                    textExpr (
                        Bokmal to "For deg vil minsteytelse utgjøre ".expr() + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_minsteytelse_sats().format() + " ganger folketrygdens grunnbeløp. Er uføregraden din under 100 prosent, vil minsteytelsen bli justert ut fra uføregraden. Vi justerer også minsteytelsen ut fra trygdetid hvis du har mindre enn 40 års trygdetid. Du må melde fra til NAV dersom sivilstanden din endrer seg, fordi dette kan medføre at uføretrygden endres.",
                        Nynorsk to "For deg vil minsteyting utgjere ".expr() + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_minsteytelse_sats().format() + " gonger grunnbeløpet i folketrygda. Er uføregraden din under 100 prosent, vil misteytinga bli justert ut frå uføregraden. Vi justerer også minsteytinga ut frå trygdetida, dersom du har mindre enn 40 års trygdetid. Du må melde frå til NAV om sivilstanden din endrar seg, dette kan gjere at uføretrygda blir endra.",
                        English to "For you, the minimum benefit is equal to ".expr() + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_minsteytelse_sats().format() + " times the National Insurance basic amount. This will be adjusted based on your degree of disability and your insurance period in Norway. In case of changes in your marital status, you are obliged to inform NAV because your disability benefit may change.",
                    )
                }
            }

            //IF(PE_Vedtaksdata_Kravhode_onsketVirkningsDato >= DateValue("01/07/2024") AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Minsteytelse_OppfyltUngUfor = true AND PE_UT_VilkarGjelderPersonAlder < 20 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Mottarminsteytelse = true) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_kravhode_onsketvirkningsdato().legacyGreaterThanOrEqual(LocalDate.of(2024,7,1)) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_minsteytelse_oppfyltungufor() and pe.ut_vilkargjelderpersonalder().lessThan(20) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_mottarminsteytelse())){
                //[TBU080V-TBU027V]

                paragraph {
                    text (
                        Bokmal to "Fra måneden du fyller 20 år, vil uføretrygden bli beregnet med rettighet som ung ufør. Beregningen vil da tilsvare ",
                        Nynorsk to "Frå månaden du fyller 20 år, blir uføretrygda berekna med rett som ung ufør. Berekninga tilsvarer då ",
                        English to "From the month you turn 20 years old, your disability benefit will be calculated in accordance with your rights as a young disabled person. Your benefit will then be ",
                    )

                    //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = PE_SivilstandAnvendt_bormed_1Bindestrek5 OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = PE_SivilstandAnvendt_bormed_3_2 OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = PE_SivilstandAnvendt_bormed_ektefelle OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = PE_SivilstandAnvendt_bormed_registrert_partner) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("bormed 1-5") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("bormed 3-2") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("bormed ektefelle") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("bormed registrert partner"))){
                        text (
                            Bokmal to "2,709",
                            Nynorsk to "2,709",
                            English to "2.709",
                        )
                    }
                    text (
                        Bokmal to " ",
                        Nynorsk to " ",
                        English to " ",
                    )

                    //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt <> PE_SivilstandAnvendt_bormed_1Bindestrek5 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt <> PE_SivilstandAnvendt_bormed_3_2 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt <> PE_SivilstandAnvendt_bormed_ektefelle AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt <> PE_SivilstandAnvendt_bormed_registrert_partner) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().notEqualTo("bormed 1-5") and pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().notEqualTo("bormed 3-2") and pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().notEqualTo("bormed ektefelle") and pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().notEqualTo("bormed registrert partner"))){
                        text (
                            Bokmal to "2,959",
                            Nynorsk to "2,959",
                            English to "2.959",
                        )
                    }
                    text (
                        Bokmal to " ganger folketrygdens grunnbeløp. Denne minsteytelsen vil også bli justert ut fra uføregraden din.",
                        Nynorsk to " gonger grunnbeløpet i folketrygda. Denne minsteytinga vil også bli justert ut frå uføregraden din.",
                        English to " times the National Insurance basic amount. Your minimum benefit will be adjusted based on your degree of disability.",
                    )
                }
            }

            //IF(PE_Vedtaksdata_Kravhode_onsketVirkningsDato < DateValue("01/07/2024") AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Minsteytelse_OppfyltUngUfor = true AND PE_UT_VilkarGjelderPersonAlder < 20 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Mottarminsteytelse = true) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_kravhode_onsketvirkningsdato().legacyLessThan(LocalDate.of(2024,7,1)) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_minsteytelse_oppfyltungufor() and pe.ut_vilkargjelderpersonalder().lessThan(20) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_mottarminsteytelse())){
                //[TBU080V-TBU027V]

                paragraph {
                    text (
                        Bokmal to "Fra måneden du fyller 20 år, vil uføretrygden bli beregnet med rettighet som ung ufør. Beregningen vil da tilsvare ",
                        Nynorsk to "Frå månaden du fyller 20 år, blir uføretrygda berekna med rett som ung ufør. Berekninga tilsvarer då ",
                        English to "From the month you turn 20 years old, your disability benefit will be calculated in accordance with your rights as a young disabled person. Your benefit will then be ",
                    )

                    //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = PE_SivilstandAnvendt_bormed_1Bindestrek5 OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = PE_SivilstandAnvendt_bormed_3_2 OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = PE_SivilstandAnvendt_bormed_ektefelle OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = PE_SivilstandAnvendt_bormed_registrert_partner) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("bormed 1-5") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("bormed 3-2") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("bormed ektefelle") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("bormed registrert partner"))){
                        text (
                            Bokmal to "2,66",
                            Nynorsk to "2,66",
                            English to "2.66",
                        )
                    }

                    //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt <> PE_SivilstandAnvendt_bormed_1Bindestrek5 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt <> PE_SivilstandAnvendt_bormed_3_2 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt <> PE_SivilstandAnvendt_bormed_ektefelle AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt <> PE_SivilstandAnvendt_bormed_registrert_partner) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().notEqualTo("bormed 1-5") and pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().notEqualTo("bormed 3-2") and pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().notEqualTo("bormed ektefelle") and pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().notEqualTo("bormed registrert partner"))){
                        text (
                            Bokmal to "2,91",
                            Nynorsk to "2,91",
                            English to "2.91",
                        )
                    }
                    text (
                        Bokmal to " ganger folketrygdens grunnbeløp. Denne minsteytelsen vil også bli justert ut fra uføregraden din. Fra 1.juli 2024 vil beregningen tilsvare 2,709/2,959 ganger folketrygdens grunnbeløp.",
                        Nynorsk to " gonger grunnbeløpet i folketrygda. Denne minsteytinga vil også bli justert ut frå uføregraden din. Frå 1. juli 2024 aukar denne til 2,709/2,959 gonger grunnbeløpet i folketrygda.",
                        English to " times the National Insurance basic amount. Your minimum benefit will be adjusted based on your degree of disability. From 1 July 2024 your benefit will be 2,709/2,959 times the National Insurance basic amount.",
                    )
                }
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_FortsattMedlemskap_InngangUnntak = "halv_minpen_ufp_ut") THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_fortsattmedlemskap_inngangunntak().equalTo("halv_minpen_ufp_ut"))){
                //[TBU080V-TBU027V]

                paragraph {
                    text (
                        Bokmal to "Du er innvilget uføretrygd fordi du har tjent opp rett til halvparten av full minsteytelse for uføretrygd. Uføretrygden din er derfor beregnet etter den faktiske trygdetiden din i Norge. Du kan se den faktiske trygdetiden din i tabellen «Trygdetiden din i Norge», og hvilke opplysninger vi har brukt i beregningen i tabellen «Opplysninger vi har brukt i beregningen».",
                        Nynorsk to "Du er innvilga uføretrygd fordi du har tent opp rett til halvparten av full minsteyting for uføretrygd. Uføretrygda di er derfor berekna etter den faktiske trygdetida di i Noreg. Du kan sjå den faktiske trygdetida di i tabellen «Trygdetida di i Noreg», og kva opplysningar vi har brukt i berekninga i tabellen «Opplysningar vi har brukt i berekninga».",
                        English to "You have been granted a disability benefit, because you have earned the right to half of the full minimum benefit for disability. Consequently, your disability benefit has been calculated on the basis of your actual insurance period in Norway. You can see your actual insurance period in the table \"Your insurance period in Norway\", and the data we have used in our calculations in the table \"Data used in calculations\".",
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode = "eos" AND PE_Vedtaksbrev_Vedtaksdata_VilkarsVedtak_Vilkar_MedlemskapForUTEtterTrygdeavtaler_OppfyltVedSammenlegging = false) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().equalTo("eos") and not(pe.vedtaksbrev_vedtaksdata_vilkarsvedtak_vilkar_medlemskapforutettertrygdeavtaler_oppfyltvedsammenlegging()))){
                //[TBU080V-TBU027V]

                paragraph {
                    text (
                        Bokmal to "Vi beregner uføretrygden din etter bestemmelsene i EØS-avtalen fordi det gir deg en høyere uføretrygd enn en beregning etter folketrygdlovens regler. Uføretrygden din er først beregnet etter samlet opptjening i Norge og andre EØS-land. Deretter er uføretrygden multiplisert med et forholdstall, som angir forholdet mellom faktisk opptjeningstid i Norge og samlet faktisk opptjening i Norge og andre EØS-land. Du kan se hvilke opplysninger vi har brukt i beregningen i tabellen «Opplysninger vi har brukt i beregningen».",
                        Nynorsk to "Vi bereknar uføretrygda di etter reglane i EØS-avtalen fordi det gir deg ei høgare uføretrygd enn ei berekning etter reglane i folketrygdlova. Uføretrygda di er først berekna etter samla opptening i Noreg og andre EØS-land. Deretter er uføretrygda multiplisert med eit forholdstal som angir forholdet mellom faktisk oppteningstid i Noreg og samla faktisk opptening i Noreg og andre EØS-land. Du kan sjå kva opplysningar vi har brukt i berekninga i tabellen «Opplysningar vi har brukt i berekninga».",
                        English to "We have calculated your disability benefit in accordance with the provisions of the EEA agreement, because this will result in a higher benefit for you than if Norwegian National Insurance provisions were applied. Your disability benefit has first been calculated on the basis of your total pensionable income in Norway and other EEA countries. Then, your disability benefit has been multiplied by the ratio between your actual insurance periods in Norway and total actual insurance periods in Norway and other EEA countries. You can see the data we have used in our calculations in the table \"Data used in calculations\". ",
                    )
                }
            }

            //IF( PE_Vedtaksbrev_Vedtaksdata_VilkarsVedtak_Vilkar_MedlemskapForUTEtterTrygdeavtaler_OppfyltVedSammenlegging = true  AND   (PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode = "eos"  OR  PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode = "nordisk")  ) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_vilkarsvedtak_vilkar_medlemskapforutettertrygdeavtaler_oppfyltvedsammenlegging() and (pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().equalTo("eos") or pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().equalTo("nordisk")))){
                //[TBU080V-TBU027V]

                paragraph {
                    text (
                        Bokmal to "Vi beregner uføretrygden din etter bestemmelsene i EØS-avtalen fordi vilkårene for rett til uføretrygd er oppfylt ved sammenlegging av perioder med opptjening i Norge og andre EØS-land. Uføretrygden din er først beregnet etter samlet opptjening i Norge og andre EØS-land. Deretter er uføretrygden multiplisert med et forholdstall, som angir forholdet mellom faktisk opptjeningstid i Norge og samlet faktisk opptjening i Norge og andre EØS-land. Du kan se hvilke opplysninger vi har brukt i beregningen i tabellen «Opplysninger vi har brukt i beregningen».",
                        Nynorsk to "Vi bereknar uføretrygda di etter reglane i EØS-avtalen fordi vilkåra for rett til uføretrygd er oppfylte når vi legg saman periodar med opptening i Noreg og andre EØS-land. Uføretrygda di er først berekna etter samla opptening i Noreg og andre EØS-land. Deretter er uføretrygda multiplisert med eit forholdstal som angir forholdet mellom faktisk oppteningstid i Noreg og samla faktisk opptening i Noreg og andre EØS-land. Du kan sjå kva opplysningar vi har brukt i berekninga i tabellen «Opplysningar vi har brukt i berekninga».",
                        English to "We have calculated your disability benefit in accordance with the conditions of the EEA agreement, because the conditions for disability benefit eligibility have been met by combining insurance periods in Norway and other EEA countries. Your disability benefit has first been calculated on the basis of your total pensionable income in Norway and other EEA countries. Then, your disability benefit has been multiplied by the ratio between your actual insurance periods in Norway and total actual insurance periods in Norway and other EEA countries. You can see the data we have used in our calculations in the table \"Data used in calculations\".",
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode <> "eos" AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode <> "folketrygd" AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode <> "nordisk" AND PE_Vedtaksbrev_Vedtaksdata_VilkarsVedtak_Vilkar_MedlemskapForUTEtterTrygdeavtaler_OppfyltVedSammenlegging = true) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().notEqualTo("eos") and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().notEqualTo("folketrygd") and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().notEqualTo("nordisk") and pe.vedtaksbrev_vedtaksdata_vilkarsvedtak_vilkar_medlemskapforutettertrygdeavtaler_oppfyltvedsammenlegging())){
                //[TBU080V-TBU027V]

                paragraph {
                    textExpr (
                        Bokmal to "Vi beregner uføretrygden din etter bestemmelsene i trygdeavtalen med ".expr() + pe.grunnlag_persongrunnlagsliste_trygdeavtaler_avtaleland() + " fordi vilkårene for rett til uføretrygd er oppfylt ved sammenlegging av opptjeningstid i Norge og " + pe.grunnlag_persongrunnlagsliste_trygdeavtaler_avtaleland() + ". Uføretrygden din er først beregnet etter samlet opptjening i Norge og " + pe.grunnlag_persongrunnlagsliste_trygdeavtaler_avtaleland() + ". Deretter er uføretrygden multiplisert med et forholdstall, som angir forholdet mellom faktisk opptjeningstid i Norge og samlet faktisk opptjening i Norge og " + pe.grunnlag_persongrunnlagsliste_trygdeavtaler_avtaleland() + ". Du kan se hvilke opplysninger vi har brukt i beregningen i tabellen «Opplysninger vi har brukt i beregningen».",
                        Nynorsk to "Vi bereknar uføretrygda di etter reglane i trygdeavtalen med ".expr() + pe.grunnlag_persongrunnlagsliste_trygdeavtaler_avtaleland() + " fordi vilkåra for rett til uføretrygd er oppfylt når ein legg saman oppteningstid i Noreg og " + pe.grunnlag_persongrunnlagsliste_trygdeavtaler_avtaleland() + ". Uføretrygda di er først berekna etter samla opptening i Noreg og " + pe.grunnlag_persongrunnlagsliste_trygdeavtaler_avtaleland() + ". Deretter er uføretrygda multiplisert med eit forholdstal som angir forholdet mellom faktisk oppteningstid i Noreg og samla faktisk opptening i Noreg og " + pe.grunnlag_persongrunnlagsliste_trygdeavtaler_avtaleland() + ". Du kan sjå kva opplysningar vi har brukt i berekninga i tabellen «Opplysningar vi har brukt i berekninga».",
                        English to "We have calculated your disability benefit in accordance with the conditions of the national insurance agreement with ".expr() + pe.grunnlag_persongrunnlagsliste_trygdeavtaler_avtaleland() + ", because the conditions for disability benefit eligibility have been met by combining insurance periods in Norway and " + pe.grunnlag_persongrunnlagsliste_trygdeavtaler_avtaleland() + ". Your disability benefit has first been calculated on the basis of your total pensionable income in Norway and " + pe.grunnlag_persongrunnlagsliste_trygdeavtaler_avtaleland() + ". Then, your disability benefit has been multiplied by the ratio between your actual insurance periods in Norway and total actual insurance periods in Norway and " + pe.grunnlag_persongrunnlagsliste_trygdeavtaler_avtaleland() + ". You can see the data we have used in our calculations in the table \"Data used in calculations\".",
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode = "folketrygd" AND PE_Vedtaksdata_Kravhode_VurdereTrygdeavtale = true) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().equalTo("folketrygd") and pe.vedtaksdata_kravhode_vurderetrygdeavtale())){
                //[TBU080V-TBU027V]

                paragraph {
                    text (
                        Bokmal to "Vi beregner uføretrygden din etter folketrygdlovens regler fordi dette gir deg en høyere beregning enn etter trygdeavtale. Uføretrygden din er beregnet etter samlet opptjening i Norge. Du kan se hvilke opplysninger vi har brukt i beregningen i tabellen «Opplysninger vi har brukt i beregningen».",
                        Nynorsk to "Vi bereknar uføretrygda di etter reglane i folketrygdlova fordi dette gir deg ei høgare berekning enn etter trygdeavtale. Uføretrygda di er berekna etter samla opptening i Noreg. Du kan sjå kva opplysningar vi har brukt i berekninga i tabellen «Opplysningar vi har brukt i berekninga».",
                        English to "We have calculated your disability benefit in accordance with the provisions of the National Insurance Act, because this will result in a higher payout for you than calculations based on the social security agreement. Your disability benefit has first been calculated on the basis of your total pensionable income in Norway. You can see the data we have used in our calculations in the table \"Data used in calculations\".",
                    )
                }
            }
        }
    }

}
