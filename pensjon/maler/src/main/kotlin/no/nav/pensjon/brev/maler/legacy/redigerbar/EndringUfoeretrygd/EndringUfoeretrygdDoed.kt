package no.nav.pensjon.brev.maler.legacy.redigerbar.EndringUfoeretrygd

import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoeretrygdDtoSelectors.PesysDataSelectors.pe
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoeretrygdDtoSelectors.pesysData
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope

/*PE_Vedtaksdata_Kravhode_kravArsakType = "tilst_dod" AND NOT
 (PE_Vedtaksdata_Kravhode_KravGjelder = "revurd" AND
 PE_Vedtaksdata_Kravhode_KravArsakType = "soknad_bt" AND
  PE_UT_KravLinjeKode_Og_PaaFolkgende_bt_avslag_v2()
 */

object EndringUfoeretrygdDoed {
    data class TBU2287til2297(
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            val pe = pesysData.pe
        }
}