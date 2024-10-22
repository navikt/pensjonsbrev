package no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp.OpptjeningUTSelectors.aar_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp.OpptjeningUTSelectors.avkortetbelop_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp.OpptjeningUTSelectors.brukt_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp.OpptjeningUTSelectors.forstegansgstjeneste_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp.OpptjeningUTSelectors.inntektiavtaleland_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp.OpptjeningUTSelectors.justertbelop_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp.OpptjeningUTSelectors.omsorgsaar_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp.OpptjeningUTSelectors.pgi_safe
import no.nav.pensjon.brev.maler.legacy.pebrevkode
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningvirkningdatofom
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_belopsgrense
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagordinar_opptjeningutliste
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_kravhode_kravarsaktype
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.Kroner

data class TBU038V_1(
    val pe: Expression<PE>,
): OutlinePhrase<LangBokmalNynorskEnglish>(){
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {

        // det å bruke data som ikke finnes i exstream ville ha ført til at hele tabellen forsvant, så dette oppnår samme resultat.
        ifNotNull(pe.vedtaksdata_beregningsdata_beregningufore_beregningvirkningdatofom()){ virkFom ->

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode = "folketrygd"  AND PE_pebrevkode <> "PE_UT_05_100"  AND PE_pebrevkode <> "PE_UT_14_300"  AND PE_pebrevkode <> "PE_UT_07_100"  AND PE_pebrevkode <> "PE_UT_04_300"  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Belopsgrense <> 60000  AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt"  AND PE_pebrevkode <> "PE_UT_04_108"  AND PE_pebrevkode <> "PE_UT_04_109"  AND PE_pebrevkode <> "PE_UT_07_200"   AND PE_pebrevkode <> "PE_UT_06_300" AND PE_pebrevkode <> "PE_UT_04_500" AND (PE_pebrevkode <> "PE_UT_04_102"      OR (PE_pebrevkode = "PE_UT_04_102"     AND PE_Vedtaksdata_Kravhode_KravArsakType <> "tilst_dod"))) THEN      INCLUDE ENDIF
            showIf((pe.pebrevkode().notEqualTo("PE_UT_07_100") and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().notEqualTo("folketrygd") and pe.pebrevkode().notEqualTo("PE_UT_05_100") and pe.pebrevkode().notEqualTo("PE_UT_14_300") and pe.pebrevkode().notEqualTo("PE_UT_04_300") and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_belopsgrense().notEqualTo(60000) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.pebrevkode().notEqualTo("PE_UT_04_108") and pe.pebrevkode().notEqualTo("PE_UT_04_109") and pe.pebrevkode().notEqualTo("PE_UT_07_200") and pe.pebrevkode().notEqualTo("PE_UT_06_300") and pe.pebrevkode().notEqualTo("PE_UT_04_500") and (pe.pebrevkode().notEqualTo("PE_UT_04_102") or (pe.pebrevkode().equalTo("PE_UT_04_102") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("tilst_dod"))))){

                paragraph {
                    textExpr (
                        Bokmal to "Inntekt lagt til grunn for beregning av uføretrygden din fra ".expr() + virkFom.format(),
                        Nynorsk to "Inntekt lagd til grunn for berekning av uføretrygda di frå ".expr() + virkFom.format(),
                        English to "Income on which to calculate your disability benefit of ".expr() + virkFom.format(),
                        BOLD
                    )
                }

                paragraph {
                    table(header = {
                        column {
                            text(
                                Bokmal to "År",
                                Nynorsk to "År",
                                English to "Year",
                            )
                        }
                        column{
                            text (
                                Bokmal to "Inntekt i utlandet",
                                Nynorsk to "Inntekt i utlandet",
                                English to "Income from abroad",
                            )
                        }
                        column {
                            text (
                                Bokmal to "Pensjonsgivende inntekt",
                                Nynorsk to "Pensjonsgivande inntekt",
                                English to "Pensionable income",
                            )
                        }
                        column {
                            text (
                                Bokmal to "Inntekt brukt i beregningen",
                                Nynorsk to "Inntekt brukt i berekninga",
                                English to "Income applied in the calculation",
                            )
                        }
                        column {
                            text(
                                Bokmal to "Merknad",
                                Nynorsk to "Merknad",
                                English to "Comments",
                            )
                        }
                    }){
                        forEach(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagordinar_opptjeningutliste()) {opptjeningUt ->
                            row {
                                //IF(FF_GetArrayElement_Boolean(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_Brukt,SYS_TableRow) = false) THEN      INCLUDE ENDIF
                                cell {
                                    showIf(opptjeningUt.brukt_safe.ifNull(false)) {
                                        textExpr(
                                            Bokmal to opptjeningUt.aar_safe.ifNull(0).format(),
                                            Nynorsk to opptjeningUt.aar_safe.ifNull(0).format(),
                                            English to opptjeningUt.aar_safe.ifNull(0).format(),
                                            BOLD,
                                        )
                                    }.orShow {
                                        textExpr(
                                            Bokmal to opptjeningUt.aar_safe.ifNull(0).format(),
                                            Nynorsk to opptjeningUt.aar_safe.ifNull(0).format(),
                                            English to opptjeningUt.aar_safe.ifNull(0).format(),
                                        )
                                    }
                                }
                                cell {
                                    showIf(opptjeningUt.inntektiavtaleland_safe.ifNull(false)){
                                        text (
                                            Bokmal to "Ja",
                                            Nynorsk to "Ja",
                                            English to "Yes",
                                        )
                                    }.orShow {
                                        text (
                                            Bokmal to "Nei",
                                            Nynorsk to "Nei",
                                            English to "No",
                                        )
                                    }
                                }
                                cell {
                                    textExpr(
                                        Bokmal to opptjeningUt.pgi_safe.ifNull(Kroner(0)).format() + " kr",
                                        Nynorsk to opptjeningUt.pgi_safe.ifNull(Kroner(0)).format() + " kr",
                                        English to opptjeningUt.pgi_safe.ifNull(Kroner(0)).format() + " NOK",
                                    )
                                }
                                cell {
                                    showIf(opptjeningUt.brukt_safe.ifNull(false)) {
                                        textExpr(
                                            Bokmal to opptjeningUt.avkortetbelop_safe.ifNull(Kroner(0)).format() + " kr",
                                            Nynorsk to opptjeningUt.avkortetbelop_safe.ifNull(Kroner(0)).format() + " kr",
                                            English to opptjeningUt.avkortetbelop_safe.ifNull(Kroner(0)).format() + " NOK",
                                            BOLD,
                                        )
                                    }.orShow {
                                        textExpr(
                                            Bokmal to opptjeningUt.avkortetbelop_safe.ifNull(Kroner(0)).format() + " kr",
                                            Nynorsk to opptjeningUt.avkortetbelop_safe.ifNull(Kroner(0)).format() + " kr",
                                            English to opptjeningUt.avkortetbelop_safe.ifNull(Kroner(0)).format() + " NOK",
                                        )
                                    }
                                    text (
                                        Bokmal to " **",
                                        Nynorsk to " **",
                                        English to " **",
                                    )
                                }
                                cell {
                                    //IF(FF_GetArrayElement_Float(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_Forstegansgstjeneste,SYS_TableRow) <> 0) THEN      INCLUDE ENDIF
                                    showIf(opptjeningUt.forstegansgstjeneste_safe.ifNull(0).notEqualTo(0)){
                                        text (
                                            Bokmal to "Førstegangsteneste * ",
                                            Nynorsk to "Førstegongsteneste * ",
                                            English to "Initial service * ",
                                        )
                                    }

                                    //IF(FF_GetArrayElement_Boolean(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_Omsorgsaar,SYS_TableRow) = true) THEN      INCLUDE ENDIF
                                    showIf(opptjeningUt.omsorgsaar_safe.ifNull(false)){
                                        text (
                                            Bokmal to "Omsorgsår *",
                                            Nynorsk to "Omsorgsår *",
                                            English to "Care Work *",
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
