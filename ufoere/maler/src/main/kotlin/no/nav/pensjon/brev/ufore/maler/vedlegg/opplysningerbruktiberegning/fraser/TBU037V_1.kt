package no.nav.pensjon.brev.ufore.maler.vedlegg.opplysningerbruktiberegning.fraser

import no.nav.pensjon.brev.ufore.api.model.vedlegg.OpplysningerBruktIBeregningUTLegacyDto.Ytelsesgrunnlag.OpptjeningUT
import no.nav.pensjon.brev.ufore.api.model.vedlegg.selectors.opplysningerBruktIBeregningUTLegacyDto.ytelsesgrunnlag.opptjeningUT.*
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text
import java.time.LocalDate

/** Portert fra legacy TBU037V_1. Inntektstabell (folketrygd-metode) for bruker, basert paa DTO. */
data class TBU037V_1(
    val virkningFom: Expression<LocalDate>,
    val opptjening: Expression<List<OpptjeningUT>>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        paragraph {
            text(
                bokmal { + "Inntekt lagt til grunn for beregning av uføretrygden din fra " + virkningFom.format() },
                nynorsk { + "Inntekt lagd til grunn for berekning av uføretrygda di frå " + virkningFom.format() },
                BOLD,
            )
        }
        paragraph {
            table(
                header = {
                    column(columnSpan = 1) { text(bokmal { + "År" }, nynorsk { + "År" }) }
                    column(columnSpan = 2) {
                        text(bokmal { + "Pensjonsgivende inntekt" }, nynorsk { + "Pensjonsgivande inntekt" })
                    }
                    column(columnSpan = 2) {
                        text(bokmal { + "Inntekt justert med folketrygdens grunnbeløp" }, nynorsk { + "Inntekt justert med grunnbeløpet i folketrygda" })
                    }
                    column(columnSpan = 2) { text(bokmal { + "Merknad" }, nynorsk { + "Merknad" }) }
                },
            ) {
                forEach(opptjening) { opptjeningUt ->
                    row {
                        cell {
                            showIf(opptjeningUt.brukt) {
                                text(bokmal { + opptjeningUt.aar.format() }, nynorsk { + opptjeningUt.aar.format() }, BOLD)
                            }.orShow {
                                text(bokmal { + opptjeningUt.aar.format() }, nynorsk { + opptjeningUt.aar.format() })
                            }
                        }
                        cell {
                            text(
                                bokmal { + opptjeningUt.pensjonsgivendeInntekt.format(false) + " kr" },
                                nynorsk { + opptjeningUt.pensjonsgivendeInntekt.format(false) + " kr" },
                            )
                        }
                        cell {
                            showIf(opptjeningUt.brukt) {
                                text(
                                    bokmal { + opptjeningUt.justertBeloep.format(false) + " kr" },
                                    nynorsk { + opptjeningUt.justertBeloep.format(false) + " kr" },
                                    BOLD,
                                )
                            }.orShow {
                                text(
                                    bokmal { + opptjeningUt.justertBeloep.format(false) + " kr" },
                                    nynorsk { + opptjeningUt.justertBeloep.format(false) + " kr" },
                                )
                            }
                        }
                        cell {
                            showIf(opptjeningUt.foerstegangstjeneste) {
                                text(bokmal { + "Førstegangsteneste * " }, nynorsk { + "Førstegongsteneste * " })
                            }
                            showIf(opptjeningUt.omsorgsaar) {
                                text(bokmal { + "Omsorgsår *" }, nynorsk { + "Omsorgsår *" })
                            }
                        }
                    }
                }
            }
        }
    }
}
