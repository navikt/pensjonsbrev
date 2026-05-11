package no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.grunnlag.trygdetidsgrunnlageos.TrygdetidsgrunnlagEOS
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.grunnlag.trygdetidsgrunnlageos.TrygdetidsgrunnlagEOSSelectors.trygdetideosland
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.grunnlag.trygdetidsgrunnlageos.TrygdetidsgrunnlagEOSSelectors.trygdetidfomeos
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.grunnlag.trygdetidsgrunnlageos.TrygdetidsgrunnlagEOSSelectors.trygdetidtomeos
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.text

data class TrygdetidsListeEOSTabell(
    val trygdetidsgrunnlagListeEOS: Expression<List<TrygdetidsgrunnlagEOS>>
): OutlinePhrase<LangBokmalNynorsk>(){
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
                forEach(trygdetidsgrunnlagListeEOS){ trygdetidEOS ->
                    row {
                        cell {
                            text(
                                bokmal { + trygdetidEOS.trygdetideosland.ifNull("") },
                                nynorsk { + trygdetidEOS.trygdetideosland.ifNull("") },
                            )
                        }
                        cell {
                            ifNotNull(trygdetidEOS.trygdetidfomeos) {
                                text(
                                    bokmal { + it.format() },
                                    nynorsk { + it.format() },
                                )
                            }
                        }
                        cell {
                            ifNotNull(trygdetidEOS.trygdetidtomeos) {
                                text(
                                    bokmal { + it.format() },
                                    nynorsk { + it.format() },
                                )
                            }
                        }
                    }
                }
            }
        }
    }

}
