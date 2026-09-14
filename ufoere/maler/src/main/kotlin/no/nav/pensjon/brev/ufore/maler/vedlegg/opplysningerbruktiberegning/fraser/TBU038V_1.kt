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

/** Portert fra legacy TBU038V_1. Inntektstabell (ikke-folketrygd/utland) for bruker, basert paa DTO. */
data class TBU038V_1(
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
                    column { text(bokmal { + "År" }, nynorsk { + "År" }) }
                    column { text(bokmal { + "Inntekt i utlandet" }, nynorsk { + "Inntekt i utlandet" }) }
                    column { text(bokmal { + "Pensjonsgivende inntekt" }, nynorsk { + "Pensjonsgivande inntekt" }) }
                    column { text(bokmal { + "Inntekt brukt i beregningen" }, nynorsk { + "Inntekt brukt i berekninga" }) }
                    column { text(bokmal { + "Merknad" }, nynorsk { + "Merknad" }) }
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
                            showIf(opptjeningUt.inntektIAvtaleland) {
                                text(bokmal { + "Ja" }, nynorsk { + "Ja" })
                            }.orShow {
                                text(bokmal { + "Nei" }, nynorsk { + "Nei" })
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
                                    bokmal { + opptjeningUt.avkortetBeloep.format(false) + " kr" },
                                    nynorsk { + opptjeningUt.avkortetBeloep.format(false) + " kr" },
                                    BOLD,
                                )
                            }.orShow {
                                text(
                                    bokmal { + opptjeningUt.avkortetBeloep.format(false) + " kr" },
                                    nynorsk { + opptjeningUt.avkortetBeloep.format(false) + " kr" },
                                )
                            }
                            text(bokmal { + " **" }, nynorsk { + " **" })
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
