package no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere.trygdetid

import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.maler.legacy.*
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.notEqualTo
import no.nav.pensjon.brev.template.dsl.text

data class TrygdetidEOSAvdoedInnledning(
    val pe: Expression<PEgruppe10>
): OutlinePhrase<LangBokmalNynorsk>(){
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        showIf((pe.ut_trygdetid_avdod() and (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_trygdetidavdod_fatteos().greaterThan(0)))){
            paragraph {
                text (
                    bokmal { + "Trygdetiden til avdøde i andre EØS-land er fastsatt ut fra følgende perioder:" },
                    nynorsk { + "Trygdetida til den avdøde i andre EØS-land er fastsett ut frå følgjande periodar:" },
                )
            }
            includePhrase(TrygdetidsListeEOSTabell(pe.grunnlag_persongrunnlagavdod_trygdetidsgrunnlageos_trygdetidsgrunnlageos()))
        }
    }

}
