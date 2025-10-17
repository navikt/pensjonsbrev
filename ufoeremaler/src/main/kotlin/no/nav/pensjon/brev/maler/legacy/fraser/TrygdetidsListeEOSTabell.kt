package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.trygdetidsgrunnlageos.TrygdetidsgrunnlagEOS
import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.trygdetidsgrunnlageos.TrygdetidsgrunnlagEOSSelectors.trygdetideosland
import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.trygdetidsgrunnlageos.TrygdetidsgrunnlagEOSSelectors.trygdetidfomeos_safe
import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.trygdetidsgrunnlageos.TrygdetidsgrunnlagEOSSelectors.trygdetidtomeos_safe

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.text

data class TrygdetidsListeEOSTabell(
    val trygdetidsgrunnlagListeEOS: Expression<List<TrygdetidsgrunnlagEOS>>
): OutlinePhrase<LangBokmalNynorskEnglish>(){
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
                forEach(trygdetidsgrunnlagListeEOS){ trygdetidEOS ->
                    row {
                        cell {
                            text(
                                bokmal { + trygdetidEOS.trygdetideosland.ifNull("") },
                                nynorsk { + trygdetidEOS.trygdetideosland.ifNull("") },
                                english { + trygdetidEOS.trygdetideosland.ifNull("") },
                            )
                        }
                        cell {
                            ifNotNull(trygdetidEOS.trygdetidfomeos_safe) {
                                text(
                                    bokmal { + it.format() },
                                    nynorsk { + it.format() },
                                    english { + it.format() },
                                )
                            }
                        }
                        cell {
                            ifNotNull(trygdetidEOS.trygdetidtomeos_safe) {
                                text(
                                    bokmal { + it.format() },
                                    nynorsk { + it.format() },
                                    english { + it.format() },
                                )
                            }
                        }
                    }
                }
            }
        }
    }

}
