package no.nav.pensjon.brev.maler.legacy.fraser

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

data class TrygdetidsListeBilateralTabell(
    val trygdetidsgrunnlagListeBilateral: Expression<List<TrygdetidsgrunnlagBilateral>>
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            table(
                header = {
                    column {
                        text(
                            bokmal { + "Land" },
                            nynorsk { + "Land" },
                            english { + "Country" },
                        )
                    }
                    column {
                        text(
                            bokmal { + "Fra og med" },
                            nynorsk { + "Frå og med" },
                            english { + "From (and including)" },
                        )
                    }
                    column {
                        text(
                            bokmal { + "Til og med" },
                            nynorsk { + "Til og med" },
                            english { + "To (and including)" },
                        )
                    }
                }
            ) {
                forEach(trygdetidsgrunnlagListeBilateral) { trygdetidBilateral ->
                    row {
                        cell {
                            ifNotNull(trygdetidBilateral.trygdetidbilateralland) {
                                text(
                                    bokmal { + it },
                                    nynorsk { + it },
                                    english { + it },
                                )
                            }
                        }
                        cell {
                            ifNotNull(trygdetidBilateral.trygdetidfombilateral) {
                                text(
                                    bokmal { + it.format() },
                                    nynorsk { + it.format() },
                                    english { + it.format() },
                                )
                            }
                        }
                        cell {
                            ifNotNull(trygdetidBilateral.trygdetidtombilateral) { tom ->
                                text(
                                    bokmal { + tom.format() },
                                    nynorsk { + tom.format() },
                                    english { + tom.format() },
                                )
                            }
                        }
                    }
                }
            }
        }
    }

}
