package no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.legacy.grunnlag_persongrunnlagavdod_trygdetidsgrunnlageos_trygdetidsgrunnlageos
import no.nav.pensjon.brev.maler.legacy.ut_trygdetid_avdod
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_kravhode_kravarsaktype
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_trygdetidavdod_fatteos
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.notEqualTo
import no.nav.pensjon.brev.template.dsl.text

data class TBU1382(
    val pe: Expression<PE>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        // IF( PE_UT_Trygdetid_Avdod() = true  AND  (PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true  AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND  FF_GetArrayElement_Float(PE_Vedtaksdata_TrygdetidAvdod_FaTTEOS, 1) > 0))   THEN      INCLUDE ENDIF
        showIf((pe.ut_trygdetid_avdod() and (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_trygdetidavdod_fatteos().greaterThan(0)))) {
            paragraph {
                text(
                    Bokmal to "Trygdetiden til avdøde i andre EØS-land er fastsatt ut fra følgende perioder:",
                    Nynorsk to "Trygdetida til den avdøde i andre EØS-land er fastsett ut frå følgjande periodar:",
                    English to "The period of national insurance coverage for the decedent in other EEA countries has been establsihed on the basis of the following periods:",
                )
            }
            includePhrase(TrygdetidsListeEOSTabell(pe.grunnlag_persongrunnlagavdod_trygdetidsgrunnlageos_trygdetidsgrunnlageos()))
        }
    }
}
