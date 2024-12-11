package no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.legacy.grunnlag_persongrunnlagavdod_brukerflyktning
import no.nav.pensjon.brev.maler.legacy.grunnlag_persongrunnlagavdod_trygdetidsgrunnlaglistenor_trygdetidsgrunnlag
import no.nav.pensjon.brev.maler.legacy.grunnlag_persongrunnlagavdod_trygdetidsgrunnlaglistenor_trygdetidsgrunnlag_trygdetidfom
import no.nav.pensjon.brev.maler.legacy.ut_sum_fattnorge_framtidigttnorge_div_12_avdod
import no.nav.pensjon.brev.maler.legacy.ut_trygdetid_avdod
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_kravhode_boddarbeidutlandavdod
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.lessThan
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.notNull
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.text

data class TBU1187(
    val pe: Expression<PE>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        ifNotNull(pe.grunnlag_persongrunnlagavdod_trygdetidsgrunnlaglistenor_trygdetidsgrunnlag_trygdetidfom()) {trygdetidFom ->
            //IF( PE_UT_Trygdetid_Avdod() = true  AND  ((PE_UT_SUM_FaTTNorge_FramtidigTTNorge_DIV_12_avdod < 40  AND  PE_Grunnlag_PersongrunnlagAvdod_BrukerFlyktning = false) OR ( PE_Vedtaksdata_Kravhode_BoddArbeidUtlandAvdod = true AND PE_Grunnlag_PersongrunnlagAvdod_BrukerFlyktning = false))  AND FF_GetArrayElement_Date_Boolean(PE_Grunnlag_PersongrunnlagAvdod_TrygdetidsgrunnlagListeNor_Trygdetidsgrunnlag_TrygdetidFom) = true ) THEN      INCLUDE ENDIF
            showIf((pe.ut_trygdetid_avdod() and ((pe.ut_sum_fattnorge_framtidigttnorge_div_12_avdod().lessThan(40) and not(pe.grunnlag_persongrunnlagavdod_brukerflyktning())) or (pe.vedtaksdata_kravhode_boddarbeidutlandavdod() and not(pe.grunnlag_persongrunnlagavdod_brukerflyktning()))) and pe.grunnlag_persongrunnlagavdod_trygdetidsgrunnlaglistenor_trygdetidsgrunnlag_trygdetidfom().notNull())){
                //[TB1187_h]

                paragraph {
                    text (
                        Bokmal to "Den faktiske norske trygdetiden til avdøde er fastsatt ut fra følgende perioder:",
                        Nynorsk to "Den faktiske norske trygdetida til den avdøde er fastsett ut frå følgjande periodar:",
                        English to "The actual Norwegian period of national insurance coverage for the decedent has been established on the basis of the following periods:",
                    )
                }
                includePhrase(TrygdetidListeNorTabell(pe.grunnlag_persongrunnlagavdod_trygdetidsgrunnlaglistenor_trygdetidsgrunnlag()))
            }
        }
    }
}