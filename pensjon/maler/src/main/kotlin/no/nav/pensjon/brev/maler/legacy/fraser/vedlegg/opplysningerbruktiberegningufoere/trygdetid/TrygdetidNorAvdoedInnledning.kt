package no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere.trygdetid

import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.maler.legacy.*
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text

data class TrygdetidNorAvdoedInnledning(
    val pe: Expression<PEgruppe10>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        ifNotNull(pe.grunnlag_persongrunnlagavdod_trygdetidsgrunnlaglistenor_trygdetidsgrunnlag_trygdetidfom()) {trygdetidFom ->
            showIf((pe.ut_trygdetid_avdod() and ((pe.ut_sum_fattnorge_framtidigttnorge_div_12_avdod().lessThan(40) and not(pe.grunnlag_persongrunnlagavdod_brukerflyktning())) or (pe.vedtaksdata_kravhode_boddarbeidutlandavdod() and not(pe.grunnlag_persongrunnlagavdod_brukerflyktning()))) and pe.grunnlag_persongrunnlagavdod_trygdetidsgrunnlaglistenor_trygdetidsgrunnlag_trygdetidfom().notNull())){
                paragraph {
                    text (
                        bokmal { + "Den faktiske norske trygdetiden til avdøde er fastsatt ut fra følgende perioder:" },
                        nynorsk { + "Den faktiske norske trygdetida til den avdøde er fastsett ut frå følgjande periodar:" },
                    )
                }
                includePhrase(TrygdetidListeNorTabell(pe.grunnlag_persongrunnlagavdod_trygdetidsgrunnlaglistenor_trygdetidsgrunnlag()))
            }
        }
    }
}