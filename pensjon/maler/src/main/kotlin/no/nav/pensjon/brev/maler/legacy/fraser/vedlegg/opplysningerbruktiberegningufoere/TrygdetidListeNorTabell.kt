package no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.grunnlag.trygdetidsgrunnlagnorge.Trygdetidsgrunnlag
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.grunnlag.trygdetidsgrunnlagnorge.TrygdetidsgrunnlagSelectors.trygdetidfom
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.grunnlag.trygdetidsgrunnlagnorge.TrygdetidsgrunnlagSelectors.trygdetidtom
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.text

data class TrygdetidListeNorTabell(val trygdetidsGrunnlagsListe: Expression<List<Trygdetidsgrunnlag>>): OutlinePhrase<LangBokmalNynorsk>(){
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        paragraph {
            table(
                header = {
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
                forEach(trygdetidsGrunnlagsListe){ trygdetidsgrunnlag ->
                    row {
                        cell {
                            ifNotNull(trygdetidsgrunnlag.trygdetidfom){
                                text(
                                    bokmal { + it.format() },
                                    nynorsk { + it.format() },
                                )
                            }
                        }
                        cell {
                            ifNotNull(trygdetidsgrunnlag.trygdetidtom){
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