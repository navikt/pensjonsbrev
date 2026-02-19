package no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp.OpptjeningUTSelectors.ar
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp.OpptjeningUTSelectors.avkortetbelop
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp.OpptjeningUTSelectors.brukt
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp.OpptjeningUTSelectors.forstegansgstjeneste
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp.OpptjeningUTSelectors.inntektiavtaleland
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp.OpptjeningUTSelectors.omsorgsaar
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp.OpptjeningUTSelectors.pgi
import no.nav.pensjon.brev.maler.fraser.common.Ja
import no.nav.pensjon.brev.maler.fraser.common.Nei
import no.nav.pensjon.brev.maler.legacy.pebrevkode
import no.nav.pensjon.brev.maler.legacy.ut_avdod
import no.nav.pensjon.brev.maler.legacy.ut_inntektslandtruehvorbruktlikfalse_avdod
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningvirkningdatofom
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gjenlevendetillegginformasjon_beregningsgrunnlagavdodordiner_opptjeningutliste
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_kravhode_kravarsaktype
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.expression.notEqualTo
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.expression.safe
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.BrevWrappers.Kroner


data class TBU038V_3(
    val pe: Expression<PE>,
): OutlinePhrase<LangBokmalNynorskEnglish>(){
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        // det å bruke data som ikke finnes i exstream ville ha ført til at hele tabellen forsvant, så dette oppnår samme resultat.
        ifNotNull(pe.vedtaksdata_beregningsdata_beregningufore_beregningvirkningdatofom()){ virkFom ->
            //IF(  PE_UT_Avdod() = true AND  (PE_pebrevkode <> "PE_UT_05_100"  AND PE_pebrevkode <> "PE_UT_07_100" AND PE_pebrevkode <> "PE_UT_14_300"  AND PE_pebrevkode <> "PE_UT_04_300"  AND PE_pebrevkode <> "PE_UT_04_500"  AND PE_pebrevkode <> "PE_UT_06_300"  AND (PE_pebrevkode <> "PE_UT_04_102"      OR (PE_pebrevkode = "PE_UT_04_102"     AND PE_Vedtaksdata_Kravhode_KravArsakType <> "tilst_dod")) AND PE_UT_InntektslandTrueHvorBruktLikFalse_Avdod())  ) THEN      INCLUDE ENDIF
            showIf((pe.ut_avdod() and (pe.pebrevkode().notEqualTo("PE_UT_05_100") and pe.pebrevkode().notEqualTo("PE_UT_07_100") and pe.pebrevkode().notEqualTo("PE_UT_14_300") and pe.pebrevkode().notEqualTo("PE_UT_04_300") and pe.pebrevkode().notEqualTo("PE_UT_04_500") and pe.pebrevkode().notEqualTo("PE_UT_06_300") and (pe.pebrevkode().notEqualTo("PE_UT_04_102") or (pe.pebrevkode().equalTo("PE_UT_04_102") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("tilst_dod"))) and pe.ut_inntektslandtruehvorbruktlikfalse_avdod()))){
                paragraph {
                    text (
                        bokmal { + "Inntekt lagt til grunn for beregning av avdødes uføretrygd fra " + virkFom.format() },
                        nynorsk { + "Inntekt lagd til grunn for berekning av avdødes uføretrygd frå " + virkFom.format() },
                        english { + "Income on which to calculate the disability benefit for the decedent of " + virkFom.format() },
                        BOLD
                    )
                }

                paragraph {
                    table(header = {
                        column {
                            text(
                                bokmal { + "År" },
                                nynorsk { + "År" },
                                english { + "Year" },
                            )
                        }
                        column{
                            text (
                                bokmal { + "Inntekt i utlandet" },
                                nynorsk { + "Inntekt i utlandet" },
                                english { + "Income from abroad" },
                            )
                        }
                        column {
                            text (
                                bokmal { + "Pensjonsgivende inntekt" },
                                nynorsk { + "Pensjonsgivande inntekt" },
                                english { + "Pensionable income" },
                            )
                        }
                        column {
                            text (
                                bokmal { + "Inntekt brukt i beregningen" },
                                nynorsk { + "Inntekt brukt i berekninga" },
                                english { + "Income applied in the calculation" },
                            )
                        }
                        column {
                            text(
                                bokmal { + "Merknad" },
                                nynorsk { + "Merknad" },
                                english { + "Comments" },
                            )
                        }
                    }){
                        forEach(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gjenlevendetillegginformasjon_beregningsgrunnlagavdodordiner_opptjeningutliste()) {opptjeningUt ->
                            row {
                                //IF(FF_GetArrayElement_Boolean(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_Brukt,SYS_TableRow) = false) THEN      INCLUDE ENDIF
                                cell {
                                    showIf(opptjeningUt.safe { brukt }.ifNull(false)) {
                                        text(
                                            bokmal { + opptjeningUt.safe { ar }.ifNull(0).format() },
                                            nynorsk { + opptjeningUt.safe { ar }.ifNull(0).format() },
                                            english { + opptjeningUt.safe { ar }.ifNull(0).format() },
                                            BOLD,
                                        )
                                    }.orShow {
                                        text(
                                            bokmal { + opptjeningUt.safe { ar }.ifNull(0).format() },
                                            nynorsk { + opptjeningUt.safe { ar }.ifNull(0).format() },
                                            english { + opptjeningUt.safe { ar }.ifNull(0).format() },
                                        )
                                    }
                                }
                                cell {
                                    showIf(opptjeningUt.safe { inntektiavtaleland }.ifNull(false)){
                                        includePhrase(Ja)
                                    }.orShow {
                                        includePhrase(Nei)
                                    }
                                }
                                cell {
                                    text(
                                        bokmal { + opptjeningUt.safe { pgi }.ifNull(Kroner(0)).format(false) + " kr" },
                                        nynorsk { + opptjeningUt.safe { pgi }.ifNull(Kroner(0)).format(false) + " kr" },
                                        english { + opptjeningUt.safe { pgi }.ifNull(Kroner(0)).format(false) + " NOK" },
                                    )
                                }
                                cell {
                                    showIf(opptjeningUt.safe { brukt }.ifNull(false)) {
                                        text(
                                            bokmal { + opptjeningUt.safe { avkortetbelop }.ifNull(Kroner(0)).format(false) + " kr" },
                                            nynorsk { + opptjeningUt.safe { avkortetbelop }.ifNull(Kroner(0)).format(false) + " kr" },
                                            english { + opptjeningUt.safe { avkortetbelop }.ifNull(Kroner(0)).format(false) + " NOK" },
                                            BOLD,
                                        )
                                    }.orShow {
                                        text(
                                            bokmal { + opptjeningUt.safe { avkortetbelop }.ifNull(Kroner(0)).format(false) + " kr" },
                                            nynorsk { + opptjeningUt.safe { avkortetbelop }.ifNull(Kroner(0)).format(false) + " kr" },
                                            english { + opptjeningUt.safe { avkortetbelop }.ifNull(Kroner(0)).format(false) + " NOK" },
                                        )
                                    }
                                    text (
                                        bokmal { + " **" },
                                        nynorsk { + " **" },
                                        english { + " **" },
                                    )
                                }
                                cell {
                                    //IF(FF_GetArrayElement_Float(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_Forstegansgstjeneste,SYS_TableRow) <> 0) THEN      INCLUDE ENDIF
                                    showIf(opptjeningUt.safe { forstegansgstjeneste }.ifNull(0).notEqualTo(0)){
                                        text (
                                            bokmal { + "Førstegangsteneste * " },
                                            nynorsk { + "Førstegongsteneste * " },
                                            english { + "Initial service * " },
                                        )
                                    }

                                    //IF(FF_GetArrayElement_Boolean(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_Omsorgsaar,SYS_TableRow) = true) THEN      INCLUDE ENDIF
                                    showIf(opptjeningUt.safe { omsorgsaar }.ifNull(false)){
                                        text (
                                            bokmal { + "Omsorgsår *" },
                                            nynorsk { + "Omsorgsår *" },
                                            english { + "Care Work *" },
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
