package no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp.OpptjeningUTSelectors.ar
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp.OpptjeningUTSelectors.brukt
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp.OpptjeningUTSelectors.forstegansgstjeneste
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp.OpptjeningUTSelectors.justertbelop
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp.OpptjeningUTSelectors.omsorgsaar
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp.OpptjeningUTSelectors.pgi
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningvirkningdatofom
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagordinar_opptjeningutliste
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner

data class TBU037V_1(
    val pe: Expression<PEgruppe10>
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {

        // det å bruke data som ikke finnes i exstream ville ha ført til at hele tabellen forsvant, så dette oppnår samme resultat.
        ifNotNull(pe.vedtaksdata_beregningsdata_beregningufore_beregningvirkningdatofom()){ virkFom ->

            showIf(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().equalTo("folketrygd")){
                //[TBU037V_1]

                paragraph {
                    text (
                        bokmal { + "Inntekt lagt til grunn for beregning av uføretrygden din fra " + virkFom.format() },
                        nynorsk { + "Inntekt lagd til grunn for berekning av uføretrygda di frå " + virkFom.format() },
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
                            )
                        }
                        column(columnSpan = 2) {
                            text(
                                bokmal { + "Pensjonsgivende inntekt" },
                                nynorsk { + "Pensjonsgivande inntekt" },
                            )
                        }
                        column(columnSpan = 2) {
                            text(
                                bokmal { + "Inntekt justert med folketrygdens grunnbeløp" },
                                nynorsk { + "Inntekt justert med grunnbeløpet i folketrygda" },
                            )
                        }
                        column(columnSpan = 2) {
                            text(
                                bokmal { + "Merknad" },
                                nynorsk { + "Merknad" },
                            )
                        }
                    }){
                        forEach(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagordinar_opptjeningutliste()) {opptjeningUt ->
                            row {
                                cell {
                                    showIf(opptjeningUt.safe { brukt }.ifNull(false)) {
                                        text(
                                            bokmal { + opptjeningUt.safe { ar }.ifNull(0).format() },
                                            nynorsk { + opptjeningUt.safe { ar }.ifNull(0).format() },
                                            BOLD,
                                        )
                                    }.orShow {
                                        text(
                                            bokmal { + opptjeningUt.safe { ar }.ifNull(0).format() },
                                            nynorsk { + opptjeningUt.safe { ar }.ifNull(0).format() },
                                        )
                                    }
                                }
                                cell {
                                    text(
                                        bokmal { + opptjeningUt.safe { pgi }.ifNull(Kroner(0)).format(false) + " kr" },
                                        nynorsk { + opptjeningUt.safe { pgi }.ifNull(Kroner(0)).format(false) + " kr" },
                                    )
                                }
                                cell {
                                    showIf(opptjeningUt.safe { brukt }.ifNull(false)) {
                                        text(
                                        bokmal { + opptjeningUt.safe { justertbelop }.ifNull(Kroner(0)).format(false) + " kr" },
                                        nynorsk { + opptjeningUt.safe { justertbelop }.ifNull(Kroner(0)).format(false) + " kr" },
                                            BOLD,
                                        )
                                    }.orShow {
                                        text(
                                        bokmal { + opptjeningUt.safe { justertbelop }.ifNull(Kroner(0)).format(false) + " kr" },
                                        nynorsk { + opptjeningUt.safe { justertbelop }.ifNull(Kroner(0)).format(false) + " kr" },
                                        )
                                    }
                                }
                                cell {
                                    showIf(opptjeningUt.safe { forstegansgstjeneste }.ifNull(0).notEqualTo(0)){
                                        text (
                                            bokmal { + "Førstegangsteneste * " },
                                            nynorsk { + "Førstegongsteneste * " },
                                        )
                                    }

                                    showIf(opptjeningUt.safe { omsorgsaar }.ifNull(false)){
                                        text (
                                            bokmal { + "Omsorgsår *" },
                                            nynorsk { + "Omsorgsår *" },
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