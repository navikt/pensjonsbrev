package no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp.OpptjeningUTSelectors.ar
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp.OpptjeningUTSelectors.brukt
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp.OpptjeningUTSelectors.forstegansgstjeneste
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp.OpptjeningUTSelectors.justertbelop
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp.OpptjeningUTSelectors.omsorgsaar
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp.OpptjeningUTSelectors.pgi
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
                    text (
                        bokmal { + "Inntekt lagt til grunn for beregning av avdødes uføretrygd fra " + virkFom.format() },
                        nynorsk { + "Inntekt lagd til grunn for berekning av avdødes uføretrygd frå " + virkFom.format() },
                        english { + "Income on which to calculate the disability benefit for the decedent of " + virkFom.format() },
                        BOLD
                    )
                }
                //[TBU037V_3]

                paragraph {
                    table(header = {
                        column {
                            text(
                                bokmal { + "År" },
                                nynorsk { + "År" },
                                english { + "Year" },
                            )
                        }
                        column {
                            text(
                                bokmal { + "Pensjonsgivende inntekt" },
                                nynorsk { + "Pensjonsgivande inntekt" },
                                english { + "Pensionable income" },
                            )
                        }
                        column {
                            text(
                                bokmal { + "Inntekt justert med folketrygdens grunnbeløp" },
                                nynorsk { + "Inntekt justert med grunnbeløpet i folketrygda" },
                                english { + "Income adjusted in accordance with the National Insurance basic amount" },
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
                                    text(
                                        bokmal { + opptjeningUt.safe { pgi }.ifNull(Kroner(0)).format(false) + " kr" },
                                        nynorsk { + opptjeningUt.safe { pgi }.ifNull(Kroner(0)).format(false) + " kr" },
                                        english { + opptjeningUt.safe { pgi }.ifNull(Kroner(0)).format(false) + " NOK" },
                                    )
                                }
                                cell {
                                    showIf(opptjeningUt.safe { brukt }.ifNull(false)) {
                                        text(
                                            bokmal { + opptjeningUt.safe { justertbelop }.ifNull(Kroner(0)).format(false) + " kr" },
                                            nynorsk { + opptjeningUt.safe { justertbelop }.ifNull(Kroner(0)).format(false) + " kr" },
                                            english { + opptjeningUt.safe { justertbelop }.ifNull(Kroner(0)).format(false) + " NOK" },
                                            BOLD,
                                        )
                                    }.orShow {
                                        text(
                                            bokmal { + opptjeningUt.safe { justertbelop }.ifNull(Kroner(0)).format(false) + " kr" },
                                            nynorsk { + opptjeningUt.safe { justertbelop }.ifNull(Kroner(0)).format(false) + " kr" },
                                            english { + opptjeningUt.safe { justertbelop }.ifNull(Kroner(0)).format(false) + " NOK" },
                                        )
                                    }
                                }
                                cell {
                                    showIf(opptjeningUt.safe { forstegansgstjeneste }.ifNull(0).notEqualTo(0)){
                                        text (
                                            bokmal { + "Førstegangsteneste * " },
                                            nynorsk { + "Førstegongsteneste * " },
                                            english { + "Initial service * " },
                                        )
                                    }

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
