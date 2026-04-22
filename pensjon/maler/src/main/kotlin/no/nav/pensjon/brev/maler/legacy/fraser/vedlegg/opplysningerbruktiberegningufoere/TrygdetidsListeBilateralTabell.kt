package no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.grunnlag.trygdetidsgrunnlagbilateral.TrygdetidsgrunnlagBilateral
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.grunnlag.trygdetidsgrunnlagbilateral.TrygdetidsgrunnlagBilateralSelectors.trygdetidbilateralland
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.grunnlag.trygdetidsgrunnlagbilateral.TrygdetidsgrunnlagBilateralSelectors.trygdetidfombilateral
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.grunnlag.trygdetidsgrunnlagbilateral.TrygdetidsgrunnlagBilateralSelectors.trygdetidtombilateral
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.text

data class TrygdetidsListeBilateralTabell(
    val trygdetidsgrunnlagListeBilateral: Expression<List<TrygdetidsgrunnlagBilateral>>
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        paragraph {
            table(
                header = {
                    column {
                        text(
                            bokmal { + "Land" },
                            nynorsk { + "Land" },
                        )
                    }
                    column {
                        text(
                            bokmal { + "Fra og med" },
                            nynorsk { + "Frå og med" },
                        )
                    }
                    column {
                        text(
                            bokmal { + "Til og med" },
                            nynorsk { + "Til og med" },
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
                                )
                            }
                        }
                        cell {
                            ifNotNull(trygdetidBilateral.trygdetidfombilateral) {
                                text(
                                    bokmal { + it.format() },
                                    nynorsk { + it.format() },
                                )
                            }
                        }
                        cell {
                            ifNotNull(trygdetidBilateral.trygdetidtombilateral) { tom ->
                                text(
                                    bokmal { + tom.format() },
                                    nynorsk { + tom.format() },
                                )
                            }
                        }
                    }
                }
            }
        }
    }

}
