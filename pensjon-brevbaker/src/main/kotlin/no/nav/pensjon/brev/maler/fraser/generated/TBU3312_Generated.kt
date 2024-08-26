

package no.nav.pensjon.brev.maler.fraser.generated

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.fraser.pebrevkode
import no.nav.pensjon.brev.maler.fraser.vedtaksbrev_vedtaksdata_etteroppgjorresultat_etteroppgjorresultattype
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.*
import java.time.LocalDate


data class TBU3312_Generated(
    val pe: Expression<PE>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
		//[TBU3312_NN, TBU3312]

		paragraph {
			text (
				Bokmal to "Vedlegg:",
				Nynorsk to "Vedlegg:",
			)

			//IF(PE_pebrevkode = 'PE_UT_04_401'     AND (PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_EtteroppgjorResultatType = "ikke_avvik"         OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_EtteroppgjorResultatType = "etterbet_tolgr"         OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_EtteroppgjorResultatType = "tilbakekr_tolgr" 	   )  ) THEN      EXCLUDE ENDIF
			showIf(not((pe.pebrevkode().equalTo("PE_UT_04_401") and (pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_etteroppgjorresultattype().equalTo("ikke_avvik") or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_etteroppgjorresultattype().equalTo("etterbet_tolgr") or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_etteroppgjorresultattype().equalTo("tilbakekr_tolgr"))))){
				text (
					Bokmal to "Opplysninger om etteroppgjøret ",
					Nynorsk to "Opplysningar om etteroppgjeret ",
				)
			}
			text (
				Bokmal to "Orientering om rettigheter og plikter",
				Nynorsk to "Orientering om rettar og plikter",
			)
		}
    }
}
        