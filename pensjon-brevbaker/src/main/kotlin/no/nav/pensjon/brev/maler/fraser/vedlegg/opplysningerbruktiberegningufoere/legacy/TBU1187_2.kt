package no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere.legacy

import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.trygdetidsgrunnlagnorge.Trygdetidsgrunnlag
import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.trygdetidsgrunnlagnorge.TrygdetidsgrunnlagSelectors.TrygdetidFom
import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.trygdetidsgrunnlagnorge.TrygdetidsgrunnlagSelectors.TrygdetidTom
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr

data class TBU1187_2(
    val PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeNor: Expression<List<Trygdetidsgrunnlag>?>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        ifNotNull(PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeNor) { trygdetidsliste ->
            paragraph {
                table(
                    header = {
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
                    }
                ) {
                    forEach(trygdetidsliste) { trygdetidNor ->
                        row {
                            cell {
                                ifNotNull(trygdetidNor.TrygdetidFom) {
                                    textExpr(
                                        Bokmal to it.format(),
                                        Nynorsk to it.format(),
                                        English to it.format(),
                                    )
                                }
                            }
                            cell {
                                ifNotNull(trygdetidNor.TrygdetidTom) {
                                    textExpr(
                                        Bokmal to it.format(),
                                        Nynorsk to it.format(),
                                        English to it.format(),
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
