

package no.nav.pensjon.brev.maler.fraser.generated

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.legacy.ut_uforetrygdetteroppgjor_periodefom_year
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr


data class TBU4051_Generated(
    val pe: Expression<PE>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
		//[TBU4051_NN, TBU4051]

		paragraph {

			//IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT = 0  AND PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0  AND PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB = 0) THEN 	INCLUDE ENDIF
			showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().equalTo(0) and pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0) and pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().equalTo(0))){
				text (
					Bokmal to "Uføretrygden din og barnetillegg",
					Nynorsk to "Uføretrygda di og barnetillegg",
				)
			}

			//IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT <> 0  AND PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB = 0  AND PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN 	INCLUDE ENDIF
			showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().notEqualTo(0) and pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().equalTo(0) and pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))){
				text (
					Bokmal to "Barnetillegg",
					Nynorsk to "Barnetillegg",
				)
			}
			textExpr (
				Bokmal to " for barn som ikke bor med begge foreldre har vært riktig beregnet ut fra inntekt i ".expr() + pe.ut_uforetrygdetteroppgjor_periodefom_year().format() + ".",
				Nynorsk to " for barn som ikkje bur saman med begge foreldra sine har vore rett berekna ut frå inntekta i ".expr() + pe.ut_uforetrygdetteroppgjor_periodefom_year().format() + ".",
			)
		}
    }
}
        