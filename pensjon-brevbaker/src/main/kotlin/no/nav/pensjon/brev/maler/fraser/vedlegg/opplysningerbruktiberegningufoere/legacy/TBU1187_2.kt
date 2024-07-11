package no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere.legacy

import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.TrygdetidNorSelectors.PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeNor_Trygdetidsgrunnlag_TrygdetidFom
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.TrygdetidNorSelectors.PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeNor_Trygdetidsgrunnlag_TrygdetidTom
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr

data class TBU1187_2(
    val PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeNor: Expression<List<OpplysningerBruktIBeregningenLegacyDto.TrygdetidNor>>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
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
                forEach(PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeNor) { trygdetidNor ->
                    row {
                        cell {
                            textExpr(
                                Bokmal to trygdetidNor.PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeNor_Trygdetidsgrunnlag_TrygdetidFom.format(),
                                Nynorsk to trygdetidNor.PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeNor_Trygdetidsgrunnlag_TrygdetidFom.format(),
                                English to trygdetidNor.PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeNor_Trygdetidsgrunnlag_TrygdetidFom.format(),
                            )
                        }
                        cell {
                            ifNotNull(trygdetidNor.PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeNor_Trygdetidsgrunnlag_TrygdetidTom) { tom ->
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
