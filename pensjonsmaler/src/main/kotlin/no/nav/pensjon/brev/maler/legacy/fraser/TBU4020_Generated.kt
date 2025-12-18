package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.legacy.*
import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*


data class TBU4020_Generated(
    val pe: Expression<PE>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
		//[TBU4020_NN, TBU4020]

		paragraph {
			text (
				bokmal { + "Du skulle ha fått " + pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_totalbeloput().format() + " i uføretrygd i " + pe.ut_uforetrygdetteroppgjor_periodefom_year().format() + ". Du fikk imidlertid " + pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_tidligerebeloput().format() + ". Du har derfor fått " + pe.ut_avviksbelopututenminus().format() + " for " },
				nynorsk { + "Du skulle ha fått " + pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_totalbeloput().format() + " i uføretrygd i " + pe.ut_uforetrygdetteroppgjor_periodefom_year().format() + ". Du fekk derimot " + pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_tidligerebeloput().format() + ". Du har derfor fått " + pe.ut_avviksbelopututenminus().format() + " for " },
			)

			//IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT < 0) THEN      INCLUDE ENDIF
			showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().lessThan(0))){
				text (
					bokmal { + "mye" },
					nynorsk { + "mykje" },
				)
			}

			//IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT > 0) THEN      INCLUDE ENDIF
			showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().greaterThan(0))){
				text (
					bokmal { + "lite" },
					nynorsk { + "lite" },
				)
			}
			text (
				bokmal { + " i uføretrygd." },
				nynorsk { + " i uføretrygd." },
			)
		}
    }
}
