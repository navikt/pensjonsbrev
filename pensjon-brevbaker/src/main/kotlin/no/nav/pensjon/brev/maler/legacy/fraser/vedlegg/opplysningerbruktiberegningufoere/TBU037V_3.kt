package no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp.OpptjeningUTSelectors.ar_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp.OpptjeningUTSelectors.brukt_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp.OpptjeningUTSelectors.forstegansgstjeneste_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp.OpptjeningUTSelectors.justertbelop_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp.OpptjeningUTSelectors.omsorgsaar_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp.OpptjeningUTSelectors.pgi_safe
import no.nav.pensjon.brev.maler.legacy.pebrevkode
import no.nav.pensjon.brev.maler.legacy.ut_avdod
import no.nav.pensjon.brev.maler.legacy.ut_inntektslandtruehvorbruktliktrue_avdod
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningvirkningdatofom
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gjenlevendetillegginformasjon_beregningsgrunnlagavdodordiner_opptjeningutliste
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_kravhode_kravarsaktype
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
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
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.expression.notEqualTo
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.Kroner


data class TBU037V_3(
    val pe: Expression<PE>,
): OutlinePhrase<LangBokmalNynorskEnglish>(){
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {

        // det å bruke data som ikke finnes i exstream ville ha ført til at hele tabellen forsvant, så dette oppnår samme resultat.
        ifNotNull(pe.vedtaksdata_beregningsdata_beregningufore_beregningvirkningdatofom()){ virkFom ->

            //IF( PE_UT_Avdod()= true  AND  (PE_pebrevkode <> "PE_UT_05_100"  AND PE_pebrevkode <> "PE_UT_04_300"  AND PE_pebrevkode <> "PE_UT_07_100"  AND PE_pebrevkode <> "PE_UT_14_300"  AND  PE_pebrevkode <> "PE_UT_04_500"  AND PE_pebrevkode <> "PE_UT_06_300"  AND (PE_pebrevkode <> "PE_UT_04_102"      OR (PE_pebrevkode = "PE_UT_04_102"     AND PE_Vedtaksdata_Kravhode_KravArsakType <> "tilst_dod"))  AND PE_UT_InntektslandTrueHvorBruktLikTrue_Avdod() AND PE_Vedtaksdata_Kravhode_KravArsakType<>"soknad_bt")  ) THEN      INCLUDE ENDIF
            showIf((pe.ut_avdod() and (pe.pebrevkode().notEqualTo("PE_UT_05_100") and pe.pebrevkode().notEqualTo("PE_UT_04_300") and pe.pebrevkode().notEqualTo("PE_UT_07_100") and pe.pebrevkode().notEqualTo("PE_UT_14_300") and pe.pebrevkode().notEqualTo("PE_UT_04_500") and pe.pebrevkode().notEqualTo("PE_UT_06_300") and (pe.pebrevkode().notEqualTo("PE_UT_04_102") or (pe.pebrevkode().equalTo("PE_UT_04_102") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("tilst_dod"))) and pe.ut_inntektslandtruehvorbruktliktrue_avdod() and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt")))){
                //[TBU037V_3]

                paragraph {
                    textExpr (
                        Bokmal to "Inntekt lagt til grunn for beregning av avdødes uføretrygd fra ".expr() + virkFom.format(),
                        Nynorsk to "Inntekt lagd til grunn for berekning av avdødes uføretrygd frå ".expr() + virkFom.format(),
                        English to "Income on which to calculate the disability benefit for the decedent of ".expr() + virkFom.format(),
                        BOLD
                    )
                }
                //[TBU037V_3]

                paragraph {
                    table(header = {
                        column {
                            text(
                                Bokmal to "År",
                                Nynorsk to "År",
                                English to "Year",
                            )
                        }
                        column {
                            text(
                                Bokmal to "Pensjonsgivende inntekt",
                                Nynorsk to "Pensjonsgivande inntekt",
                                English to "Pensionable income",
                            )
                        }
                        column {
                            text(
                                Bokmal to "Inntekt justert med folketrygdens grunnbeløp",
                                Nynorsk to "Inntekt justert med grunnbeløpet i folketrygda",
                                English to "Income adjusted in accordance with the National Insurance basic amount",
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
                        forEach(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gjenlevendetillegginformasjon_beregningsgrunnlagavdodordiner_opptjeningutliste()) {opptjeningUt ->
                            row {
                                cell {
                                    showIf(opptjeningUt.brukt_safe.ifNull(false)) {
                                        textExpr(
                                            Bokmal to opptjeningUt.ar_safe.ifNull(0).format(),
                                            Nynorsk to opptjeningUt.ar_safe.ifNull(0).format(),
                                            English to opptjeningUt.ar_safe.ifNull(0).format(),
                                            BOLD,
                                        )
                                    }.orShow {
                                        textExpr(
                                            Bokmal to opptjeningUt.ar_safe.ifNull(0).format(),
                                            Nynorsk to opptjeningUt.ar_safe.ifNull(0).format(),
                                            English to opptjeningUt.ar_safe.ifNull(0).format(),
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
                                            Bokmal to opptjeningUt.justertbelop_safe.ifNull(Kroner(0)).format() + " kr",
                                            Nynorsk to opptjeningUt.justertbelop_safe.ifNull(Kroner(0)).format() + " kr",
                                            English to opptjeningUt.justertbelop_safe.ifNull(Kroner(0)).format() + " NOK",
                                            BOLD,
                                        )
                                    }.orShow {
                                        textExpr(
                                            Bokmal to opptjeningUt.justertbelop_safe.ifNull(Kroner(0)).format() + " kr",
                                            Nynorsk to opptjeningUt.justertbelop_safe.ifNull(Kroner(0)).format() + " kr",
                                            English to opptjeningUt.justertbelop_safe.ifNull(Kroner(0)).format() + " NOK",
                                        )
                                    }
                                }
                                cell {
                                    showIf(opptjeningUt.forstegansgstjeneste_safe.ifNull(0).notEqualTo(0)){
                                        text (
                                            Bokmal to "Førstegangsteneste * ",
                                            Nynorsk to "Førstegongsteneste * ",
                                            English to "Initial service * ",
                                        )
                                    }

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