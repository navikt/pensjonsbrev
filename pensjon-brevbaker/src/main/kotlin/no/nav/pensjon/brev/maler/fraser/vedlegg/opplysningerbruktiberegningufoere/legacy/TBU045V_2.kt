package no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere.legacy

import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.trygdetidsgrunnlagbilateral.TrygdetidsgrunnlagEOSSelectors.TrygdetidEOSLand
import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.trygdetidsgrunnlagbilateral.TrygdetidsgrunnlagEOSSelectors.TrygdetidFomEOS_safe
import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.trygdetidsgrunnlagbilateral.TrygdetidsgrunnlagEOSSelectors.TrygdetidTomEOS_safe
import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.trygdetidsgrunnlagbilateral.TrygdetidsgrunnlagListeEOS
import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.trygdetidsgrunnlagbilateral.TrygdetidsgrunnlagListeEOSSelectors.TrygdetidsgrunnlagEOS

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr

data class TBU045V_2(
    val PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeEOS: Expression<TrygdetidsgrunnlagListeEOS>
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
                forEach(PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeEOS.TrygdetidsgrunnlagEOS){ trygdetidEOS ->
                    row {
                        cell {
                            textExpr(
                                Bokmal to trygdetidEOS.TrygdetidEOSLand.ifNull(""),
                                Nynorsk to trygdetidEOS.TrygdetidEOSLand.ifNull(""),
                                English to trygdetidEOS.TrygdetidEOSLand.ifNull(""),
                            )
                        }
                        cell {
                            ifNotNull(trygdetidEOS.TrygdetidFomEOS_safe) {
                                textExpr(
                                    Bokmal to it.format(),
                                    Nynorsk to it.format(),
                                    English to it.format(),
                                )
                            }
                        }
                        cell {
                            ifNotNull(trygdetidEOS.TrygdetidTomEOS_safe) {
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
