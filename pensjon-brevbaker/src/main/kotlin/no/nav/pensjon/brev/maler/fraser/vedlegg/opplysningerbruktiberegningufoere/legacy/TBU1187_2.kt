package no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere.legacy

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.api.model.maler.legacy.PESelectors.vedtaksbrev_safe
import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.GrunnlagSelectors.persongrunnlagsliste_safe
import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.PersongrunnlagSelectors.trygdetidsgrunnlaglistenor_safe
import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.trygdetidsgrunnlagnorge.TrygdetidsgrunnlagListeNorSelectors.trygdetidsgrunnlag_safe
import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.trygdetidsgrunnlagnorge.TrygdetidsgrunnlagSelectors.trygdetidfom
import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.trygdetidsgrunnlagnorge.TrygdetidsgrunnlagSelectors.trygdetidtom
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.VedtaksbrevSelectors.grunnlag_safe
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr

data class TBU1187_2(
    val pe: Expression<PE>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        ifNotNull(pe.vedtaksbrev_safe.grunnlag_safe.persongrunnlagsliste_safe.getOrNull().trygdetidsgrunnlaglistenor_safe.trygdetidsgrunnlag_safe) { trygdetidsliste ->
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
                                ifNotNull(trygdetidNor.trygdetidfom) {
                                    textExpr(
                                        Bokmal to it.format(),
                                        Nynorsk to it.format(),
                                        English to it.format(),
                                    )
                                }
                            }
                            cell {
                                ifNotNull(trygdetidNor.trygdetidtom) {
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
