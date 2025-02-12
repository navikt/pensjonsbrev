package no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.trygdetidsgrunnlagbilateral.TrygdetidsgrunnlagBilateral
import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.trygdetidsgrunnlagbilateral.TrygdetidsgrunnlagBilateralSelectors.trygdetidbilateralland
import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.trygdetidsgrunnlagbilateral.TrygdetidsgrunnlagBilateralSelectors.trygdetidfombilateral
import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.trygdetidsgrunnlagbilateral.TrygdetidsgrunnlagBilateralSelectors.trygdetidtombilateral
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr

data class TrygdetidsListeBilateralTabell(
    val trygdetidsgrunnlagListeBilateral: Expression<List<TrygdetidsgrunnlagBilateral>>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            table(
                header = {
                    column {
                        text(
                            Bokmal to "Land",
                            Nynorsk to "Land",
                            English to "Country",
                        )
                    }
                    column {
                        text(
                            Bokmal to "Fra og med",
                            Nynorsk to "Frå og med",
                            English to "From (and including)",
                        )
                    }
                    column {
                        text(
                            Bokmal to "Til og med",
                            Nynorsk to "Til og med",
                            English to "To (and including)",
                        )
                    }
                },
            ) {
                forEach(trygdetidsgrunnlagListeBilateral) { trygdetidBilateral ->
                    row {
                        cell {
                            ifNotNull(trygdetidBilateral.trygdetidbilateralland) {
                                textExpr(
                                    Bokmal to it,
                                    Nynorsk to it,
                                    English to it,
                                )
                            }
                        }
                        cell {
                            ifNotNull(trygdetidBilateral.trygdetidfombilateral) {
                                textExpr(
                                    Bokmal to it.format(),
                                    Nynorsk to it.format(),
                                    English to it.format(),
                                )
                            }
                        }
                        cell {
                            ifNotNull(trygdetidBilateral.trygdetidtombilateral) { tom ->
                                textExpr(
                                    Bokmal to tom.format(),
                                    Nynorsk to tom.format(),
                                    English to tom.format(),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
