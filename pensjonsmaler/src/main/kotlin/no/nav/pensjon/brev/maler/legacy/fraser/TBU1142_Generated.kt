package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.notEqualTo
import no.nav.pensjon.brev.template.dsl.text


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
