package no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere.legacy

import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.TrygdetidBilateralSelectors.PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeBilateral_TrygdetidsgrunnlagBilateral_TrygdetidBilateralLand
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.TrygdetidBilateralSelectors.PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeBilateral_TrygdetidsgrunnlagBilateral_TrygdetidFomBilateral
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.TrygdetidBilateralSelectors.PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeBilateral_TrygdetidsgrunnlagBilateral_TrygdetidTomBilateral
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr

data class TBU046V_2(
    val PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeBilateral: Expression<List<OpplysningerBruktIBeregningenLegacyDto.TrygdetidBilateral>>
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
                forEach(PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeBilateral){ trygdetidBilateral ->
                    row {
                        cell {
                            textExpr(
                                Bokmal to trygdetidBilateral.PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeBilateral_TrygdetidsgrunnlagBilateral_TrygdetidBilateralLand,
                                Nynorsk to trygdetidBilateral.PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeBilateral_TrygdetidsgrunnlagBilateral_TrygdetidBilateralLand,
                                English to trygdetidBilateral.PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeBilateral_TrygdetidsgrunnlagBilateral_TrygdetidBilateralLand,
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to trygdetidBilateral.PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeBilateral_TrygdetidsgrunnlagBilateral_TrygdetidFomBilateral.format(),
                                Nynorsk to trygdetidBilateral.PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeBilateral_TrygdetidsgrunnlagBilateral_TrygdetidFomBilateral.format(),
                                English to trygdetidBilateral.PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeBilateral_TrygdetidsgrunnlagBilateral_TrygdetidFomBilateral.format(),
                            )
                        }
                        cell {
                            ifNotNull(trygdetidBilateral.PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeBilateral_TrygdetidsgrunnlagBilateral_TrygdetidTomBilateral) { tom ->
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
