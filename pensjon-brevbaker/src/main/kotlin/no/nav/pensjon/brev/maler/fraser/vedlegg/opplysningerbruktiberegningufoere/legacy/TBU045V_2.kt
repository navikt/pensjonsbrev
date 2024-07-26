package no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere.legacy

import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.trygdetidsgrunnlagbilateral.TrygdetidsgrunnlagEOSSelectors.trygdetideosland
import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.trygdetidsgrunnlagbilateral.TrygdetidsgrunnlagEOSSelectors.trygdetidfomeos_safe
import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.trygdetidsgrunnlagbilateral.TrygdetidsgrunnlagEOSSelectors.trygdetidtomeos_safe
import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.trygdetidsgrunnlagbilateral.TrygdetidsgrunnlagListeEOS
import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.trygdetidsgrunnlagbilateral.TrygdetidsgrunnlagListeEOSSelectors.trygdetidsgrunnlageos

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
                forEach(PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeEOS.trygdetidsgrunnlageos){ trygdetidEOS ->
                    row {
                        cell {
                            textExpr(
                                Bokmal to trygdetidEOS.trygdetideosland.ifNull(""),
                                Nynorsk to trygdetidEOS.trygdetideosland.ifNull(""),
                                English to trygdetidEOS.trygdetideosland.ifNull(""),
                            )
                        }
                        cell {
                            ifNotNull(trygdetidEOS.trygdetidfomeos_safe) {
                                textExpr(
                                    Bokmal to it.format(),
                                    Nynorsk to it.format(),
                                    English to it.format(),
                                )
                            }
                        }
                        cell {
                            ifNotNull(trygdetidEOS.trygdetidtomeos_safe) {
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
