package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse
import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.*

data class TBU1139_Generated(
    val pe: Expression<PE>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU1139NN, TBU1139EN, TBU1139]

		paragraph {
			text (
				bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-2 til 12-14 og " },
				nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-2 til 12-14 og " },
				english { + "The decision has been made pursuant to Section 12-2 to 12-14 and " },
			)

			//IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
			showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
				text (
					bokmal { + "22-12." },
					nynorsk { + "22-12." },
					english { + "22-12 of the Norwegian National Insurance Act." },
				)
			}

			//IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
			showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
				text (
					bokmal { + "22-13." },
					nynorsk { + "22-13." },
					english { + "22-13 of the Norwegian National Insurance Act." },
				)
			}
		}
    }
}
        