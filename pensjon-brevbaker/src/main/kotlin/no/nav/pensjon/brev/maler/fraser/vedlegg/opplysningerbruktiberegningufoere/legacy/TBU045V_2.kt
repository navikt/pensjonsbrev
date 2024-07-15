package no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere.legacy

import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.TrygdetidEOSSelectors.fom
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.TrygdetidEOSSelectors.land
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.TrygdetidEOSSelectors.tom
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr

data class TBU045V_2(
    val PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeEOS: Expression<List<OpplysningerBruktIBeregningenLegacyDto.TrygdetidEOS>>
): OutlinePhrase<LangBokmalNynorskEnglish>(){
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
                }
            ) {
                forEach(PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeEOS){ trygdetidEOS ->
                    row {
                        cell {
                            textExpr(
                                Bokmal to trygdetidEOS.land,
                                Nynorsk to trygdetidEOS.land,
                                English to trygdetidEOS.land,
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to trygdetidEOS.fom.format(),
                                Nynorsk to trygdetidEOS.fom.format(),
                                English to trygdetidEOS.fom.format(),
                            )
                        }
                        cell {
                            ifNotNull(trygdetidEOS.tom) { tom ->
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
