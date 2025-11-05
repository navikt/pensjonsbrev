

package no.nav.pensjon.brev.maler.fraser.generated

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.*
import java.time.LocalDate


data class TBU1142_Generated(
    val pe: Expression<PE>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU1142NN, TBU1142EN, TBU1142]

		paragraph {
			text (
				bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-2 til 12-14, 12-18 og " },
				nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-2 til 12-14, 12-18 og " },
				english { + "The decision has been made pursuant to Section 12-2 to 12-14, 12-18 and " },
			)

			//IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) <> "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
			showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).notEqualTo("stdbegr_22_12_1_5"))){
				text (
					bokmal { + "22-12" },
					nynorsk { + "22-12" },
					english { + "22-12" },
				)
			}

			//IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_VirkningBegrunnelse) = "stdbegr_22_12_1_5") THEN      INCLUDE ENDIF
			showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
				text (
					bokmal { + "22-13" },
					nynorsk { + "22-13" },
					english { + "22-13" },
				)
			}
			text (
				bokmal { + "." },
				nynorsk { + "." },
				english { + " of the Norwegian National Insurance Act." },
			)
		}
    }
}
        