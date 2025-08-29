package no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.trygdetidsgrunnlagnorge.Trygdetidsgrunnlag
import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.trygdetidsgrunnlagnorge.TrygdetidsgrunnlagSelectors.trygdetidfom
import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.trygdetidsgrunnlagnorge.TrygdetidsgrunnlagSelectors.trygdetidtom
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.text

data class TrygdetidListeNorTabell(val trygdetidsGrunnlagsListe: Expression<List<Trygdetidsgrunnlag>>): OutlinePhrase<LangBokmalNynorskEnglish>(){
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            table(
                header = {
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
                forEach(trygdetidsGrunnlagsListe){ trygdetidsgrunnlag ->
                    row {
                        cell {
                            ifNotNull(trygdetidsgrunnlag.trygdetidfom){
                                text(
                                    bokmal { + it.format() },
                                    nynorsk { + it.format() },
                                    english { + it.format() },
                                )
                            }
                        }
                        cell {
                            ifNotNull(trygdetidsgrunnlag.trygdetidtom){
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