package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp.OpptjeningUTSelectors.brukt_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp.OpptjeningUTSelectors.ar_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp.OpptjeningUTSelectors.forstegansgstjeneste_safe
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
import no.nav.pensjon.brevbaker.api.model.Kroner

data class TBU037V_1(
    val pe: Expression<PE>
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {

        // det å bruke data som ikke finnes i exstream ville ha ført til at hele tabellen forsvant, så dette oppnår samme resultat.
        ifNotNull(pe.vedtaksdata_beregningsdata_beregningufore_beregningvirkningdatofom()){ virkFom ->

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode = "folketrygd"  AND PE_pebrevkode <> "PE_UT_05_100"  AND PE_pebrevkode <> "PE_UT_14_300"  AND PE_pebrevkode <> "PE_UT_07_100"  AND PE_pebrevkode <> "PE_UT_04_300"  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Belopsgrense <> 60000  AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt"  AND PE_pebrevkode <> "PE_UT_04_108"  AND PE_pebrevkode <> "PE_UT_04_109"  AND PE_pebrevkode <> "PE_UT_07_200"   AND PE_pebrevkode <> "PE_UT_06_300" AND PE_pebrevkode <> "PE_UT_04_500" AND (PE_pebrevkode <> "PE_UT_04_102"      OR (PE_pebrevkode = "PE_UT_04_102"     AND PE_Vedtaksdata_Kravhode_KravArsakType <> "tilst_dod"))) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().equalTo("folketrygd") and pe.pebrevkode().notEqualTo("PE_UT_05_100") and pe.pebrevkode().notEqualTo("PE_UT_14_300") and pe.pebrevkode().notEqualTo("PE_UT_07_100") and pe.pebrevkode().notEqualTo("PE_UT_04_300") and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_belopsgrense().notEqualTo(60000) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.pebrevkode().notEqualTo("PE_UT_04_108") and pe.pebrevkode().notEqualTo("PE_UT_04_109") and pe.pebrevkode().notEqualTo("PE_UT_07_200") and pe.pebrevkode().notEqualTo("PE_UT_06_300") and pe.pebrevkode().notEqualTo("PE_UT_04_500") and (pe.pebrevkode().notEqualTo("PE_UT_04_102") or (pe.pebrevkode().equalTo("PE_UT_04_102") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("tilst_dod"))))){
                //[TBU037V_1]

                paragraph {
                    text (
                        bokmal { + "Inntekt lagt til grunn for beregning av uføretrygden din fra " + virkFom.format() },
                        nynorsk { + "Inntekt lagd til grunn for berekning av uføretrygda di frå " + virkFom.format() },
                        english { + "Income on which to calculate your disability benefit of " + virkFom.format() },
                        BOLD
                    )
                }
                //[TBU037V_1]

                paragraph {
                    table(header = {
                        column(columnSpan = 1) {
                            text(
                                bokmal { + "År" },
                                nynorsk { + "År" },
                                english { + "Year" },
                            )
                        }
                        column(columnSpan = 2) {
                            text(
                                bokmal { + "Pensjonsgivende inntekt" },
                                nynorsk { + "Pensjonsgivande inntekt" },
                                english { + "Pensionable income" },
                            )
                        }
                        column(columnSpan = 2) {
                            text(
                                bokmal { + "Inntekt justert med folketrygdens grunnbeløp" },
                                nynorsk { + "Inntekt justert med grunnbeløpet i folketrygda" },
                                english { + "Income adjusted in accordance with the National Insurance basic amount" },
                            )
                        }
                        column(columnSpan = 2) {
                            text(
                                bokmal { + "Merknad" },
                                nynorsk { + "Merknad" },
                                english { + "Comments" },
                            )
                        }
                    }){
                        forEach(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagordinar_opptjeningutliste()) {opptjeningUt ->
                            row {
                                cell {
                                    showIf(opptjeningUt.brukt_safe.ifNull(false)) {
                                        text(
                                            bokmal { + opptjeningUt.ar_safe.ifNull(0).format() },
                                            nynorsk { + opptjeningUt.ar_safe.ifNull(0).format() },
                                            english { + opptjeningUt.ar_safe.ifNull(0).format() },
                                            BOLD,
                                        )
                                    }.orShow {
                                        text(
                                            bokmal { + opptjeningUt.ar_safe.ifNull(0).format() },
                                            nynorsk { + opptjeningUt.ar_safe.ifNull(0).format() },
                                            english { + opptjeningUt.ar_safe.ifNull(0).format() },
                                        )
                                    }
                                }
                                cell {
                                    text(
                                        bokmal { + opptjeningUt.pgi_safe.ifNull(Kroner(0)).format(false) + " kr" },
                                        nynorsk { + opptjeningUt.pgi_safe.ifNull(Kroner(0)).format(false) + " kr" },
                                        english { + opptjeningUt.pgi_safe.ifNull(Kroner(0)).format(false) + " NOK" },
                                    )
                                }
                                cell {
                                    showIf(opptjeningUt.brukt_safe.ifNull(false)) {
                                        text(
                                        bokmal { + opptjeningUt.justertbelop_safe.ifNull(Kroner(0)).format(false) + " kr" },
                                        nynorsk { + opptjeningUt.justertbelop_safe.ifNull(Kroner(0)).format(false) + " kr" },
                                        english { + opptjeningUt.justertbelop_safe.ifNull(Kroner(0)).format(false) + " NOK" },
                                            BOLD,
                                        )
                                    }.orShow {
                                        text(
                                        bokmal { + opptjeningUt.justertbelop_safe.ifNull(Kroner(0)).format(false) + " kr" },
                                        nynorsk { + opptjeningUt.justertbelop_safe.ifNull(Kroner(0)).format(false) + " kr" },
                                        english { + opptjeningUt.justertbelop_safe.ifNull(Kroner(0)).format(false) + " NOK" },
                                        )
                                    }
                                }
                                cell {
                                    showIf(opptjeningUt.forstegansgstjeneste_safe.ifNull(0).notEqualTo(0)){
                                        text (
                                            bokmal { + "Førstegangsteneste * " },
                                            nynorsk { + "Førstegongsteneste * " },
                                            english { + "Initial service * " },
                                        )
                                    }

                                    showIf(opptjeningUt.omsorgsaar_safe.ifNull(false)){
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