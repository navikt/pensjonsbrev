package no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.legacy.grunnlag_persongrunnlagavdod_trygdetidsgrunnlaglistebilateral_trygdetidsgrunnlagbilateral
import no.nav.pensjon.brev.maler.legacy.grunnlag_persongrunnlagavdod_trygdetidsgrunnlaglistebilateral_trygdetidsgrunnlagbilateral_trygdetidfombilateral
import no.nav.pensjon.brev.maler.legacy.ut_trygdetid_avdod
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.notNull
import no.nav.pensjon.brev.template.dsl.text

data class TBU1384(
    val pe: Expression<PE>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        // IF( PE_UT_Trygdetid_Avdod() = true  AND  (FF_GetArrayElement_Date_Boolean(PE_Grunnlag_PersongrunnlagAvdod_TrygdetidsgrunnlagListeBilateral_TrygdetidsgrunnlagBilateral_TrygdetidFomBilateral) = true)) THEN      INCLUDE ENDIF
        showIf((pe.ut_trygdetid_avdod() and (pe.grunnlag_persongrunnlagavdod_trygdetidsgrunnlaglistebilateral_trygdetidsgrunnlagbilateral_trygdetidfombilateral().notNull()))) {
            // [TB1384_h]

            paragraph {
                text(
                    Bokmal to "Trygdetiden til avdøde i land som Norge har trygdeavtale med, er fastsatt ut fra følgende perioder:",
                    Nynorsk to "Trygdetida til den avdøde i land som Noreg har trygdeavtale med, er fastsett ut frå følgjande periodar:",
                    English to "The period of national insurance coverage for the decedent in countries with which Norway has a national insurance agreement, has been established on the basis of the following periods:",
                )
            }

            includePhrase(TrygdetidsListeBilateralTabell(pe.grunnlag_persongrunnlagavdod_trygdetidsgrunnlaglistebilateral_trygdetidsgrunnlagbilateral()))
        }
    }
}
