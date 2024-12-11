package no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.legacy.pebrevkode
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_kravhode_brukerkonvertertup
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_nyttgjenlevendetillegg
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_kravhode_kravarsaktype
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.notEqualTo
import no.nav.pensjon.brev.template.dsl.text

data class TBUxx2V(
    val pe: Expression<PE>,
): OutlinePhrase<LangBokmalNynorskEnglish>(){
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {

        //IF(PE_pebrevkode <> "PE_UT_04_300" AND PE_pebrevkode <> "PE_UT_14_300" AND PE_Vedtaksbrev_Vedtaksdata_Kravhode_BrukerKonvertertUP = true AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_pebrevkode <> "PE_UT_05_100" AND PE_pebrevkode <> "PE_UT_07_100" AND PE_pebrevkode <> "PE_UT_04_108" AND PE_pebrevkode <> "PE_UT_04_109" AND PE_pebrevkode <> "PE_UT_07_100" AND PE_pebrevkode <> "PE_UT_07_200" AND PE_pebrevkode <> "PE_UT_06_300") THEN      INCLUDE ENDIF
        showIf((pe.pebrevkode().notEqualTo("PE_UT_04_300") and pe.pebrevkode().notEqualTo("PE_UT_14_300") and pe.vedtaksbrev_vedtaksdata_kravhode_brukerkonvertertup() and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.pebrevkode().notEqualTo("PE_UT_05_100") and pe.pebrevkode().notEqualTo("PE_UT_07_100") and pe.pebrevkode().notEqualTo("PE_UT_04_108") and pe.pebrevkode().notEqualTo("PE_UT_04_109") and pe.pebrevkode().notEqualTo("PE_UT_07_100") and pe.pebrevkode().notEqualTo("PE_UT_07_200") and pe.pebrevkode().notEqualTo("PE_UT_06_300"))){
            //[TBUxx2V]

            title1 {
                text (
                    Bokmal to "Slik beregner vi uføretrygden din",
                    Nynorsk to "Slik bereknar vi uføretrygda di",
                    English to "This is how your disability benefit is calculated",
                )
            }
            //[TBUxx2V]

            paragraph {
                text (
                    Bokmal to "Uførepensjonen din har tidligere blitt regnet om til uføretrygd og er justert ut fra trygdetid og uføregrad.",
                    Nynorsk to "Uførepensjonen din har tidligare blitt rekna om til uføretrygd og er justert ut frå trygdetid og uføregrad.",
                    English to "We have at an earlier time converted your disability pension to a disability benefit. It was then adjusted on the basis of your national insurance coverage and your degree of disability.",
                )
            }

            //IF(PE_pebrevkode = "PE_UT_04_102") THEN      INCLUDE ENDIF
            showIf((pe.pebrevkode().equalTo("PE_UT_04_102"))){
                //[TBUxx2V]

                paragraph {
                    text (
                        Bokmal to "Når uføretrygden din endres, kan dette medføre at beregningsgrunnlaget har blitt endret.",
                        Nynorsk to "Når uføretrygda di blir endra, kan dette føre til at berekningsgrunnlaget har blitt endra.",
                        English to "Your basis for calculation may be changed when your disability benefit is changed."
                    )

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_NyttGjenlevendetillegg = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true) THEN      INCLUDE ENDIF
                    showIf((not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_nyttgjenlevendetillegg()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget())){
                        text (
                            Bokmal to " Dette gjelder også for gjenlevendetillegget du mottar i uføretrygden.",
                            Nynorsk to " Dette gjeld også for attlevandetillegget du får i uføretrygda di.",
                            English to " This also applies to the survivor's supplement you receive in your disability benefit.",

                        )
                    }
                }
            }

            //IF(PE_pebrevkode = "PE_UT_04_114") THEN      INCLUDE ENDIF
            showIf((pe.pebrevkode().equalTo("PE_UT_04_114"))){
                //[TBUxx2V]

                paragraph {
                    text (
                        Bokmal to "Når uføregraden din øker, sammenligner vi det tidligere beregningsgrunnlaget på uførepensjonen med beregningsgrunnlaget på uføretrygd. Du får alternativet som gir deg høyest uføretrygd.",
                        Nynorsk to "Når uføregraden din aukar, samanliknar vi det tidligare berekningsgrunnlaget på uførepensjonen med berekningsgrunnlaget på uføretrygd. Du får det alternativet som gjer deg høgast uføretrygd.",
                        English to "On increasing your disability degree, your former basis of calculation of disability pension will be compared with your disability benefit calculation. You will be granted the alternative that gives you the best calculation.",
                    )

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget())){
                        text (
                            Bokmal to " Gjenlevendetillegget ditt vil øke med samme grad som uføregraden din.",
                            Nynorsk to " Attlevandetillegget ditt vil auke med same grad som uføregraden din.",
                            English to " Your survivor's supplement will increase with the same degree as your degree of disability.",
                        )
                    }
                }
            }
        }
    }

}
