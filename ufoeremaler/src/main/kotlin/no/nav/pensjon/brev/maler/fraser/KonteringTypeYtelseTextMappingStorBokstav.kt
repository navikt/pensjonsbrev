package no.nav.pensjon.brev.maler.fraser

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.PlainTextOnlyPhrase
import no.nav.pensjon.brev.template.dsl.PlainTextOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.KonteringType


data class KonteringTypeYtelseTextMappingStorBokstav(
    val ytelsenMedFeilutbetaling: Expression<KonteringType>
) : PlainTextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun PlainTextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        showIf(ytelsenMedFeilutbetaling.isOneOf(KonteringType.AFP_KOMP_TILLEGG)) {
            text(
                bokmal { + "AFP kompensasjonstillegg" },
                nynorsk { + "AFP kompensasjonstillegg" },
                english { + "AFP compensation supplement" }
            )
        }.orShowIf(ytelsenMedFeilutbetaling.isOneOf(KonteringType.AFP_KRONETILLEGG)) {
            text(
                bokmal { + "AFP kronetillegg" },
                nynorsk { + "AFP kronetillegg" },
                english { + "AFP krone supplement" }
            )
        }.orShowIf(ytelsenMedFeilutbetaling.isOneOf(KonteringType.AFP_LIVSVARIG)) {
            text(
                bokmal { + "AFP livsvarig del" },
                nynorsk { + "AFP livsvarig del" },
                english { + "AFP life long part" }
            )
        }.orShowIf(ytelsenMedFeilutbetaling.isOneOf(KonteringType.AFP_T)) {
            text(
                bokmal { + "AFP-tillegg" },
                nynorsk { + "AFP-tillegg" },
                english { + "AFP supplement" }
            )
        }.orShowIf(ytelsenMedFeilutbetaling.isOneOf(KonteringType.BARNEPENSJON)) {
            text(
                bokmal { + "barnepensjon" },
                nynorsk { + "barnepensjon" },
                english { + "children's pension" },
            )
        }.orShowIf(ytelsenMedFeilutbetaling.isOneOf(KonteringType.BARNETILSYN)) {
            text(
                bokmal { + "Barnetillegg" },
                nynorsk { + "Barnetillegg" },
                english { + "Child supplement" }
            )
        }.orShowIf(ytelsenMedFeilutbetaling.isOneOf(KonteringType.ET)) {
            text(
                bokmal { + "Ektefelletillegg" },
                nynorsk { + "Ektefelletillegg" },
                english { + "Spouse supplement" }
            )
        }.orShowIf(ytelsenMedFeilutbetaling.isOneOf(KonteringType.FAM_T)) {
            text(
                bokmal { + "Familietillegg" },
                nynorsk { + "Familietillegg" },
                english { + "Family supplement" }
            )
        }.orShowIf(ytelsenMedFeilutbetaling.isOneOf(KonteringType.GAP)) {
            text(
                bokmal { + "Garantipensjon" },
                nynorsk { + "Garantipensjon" },
                english { + "Guarantee pension" }
            )
        }.orShowIf(ytelsenMedFeilutbetaling.isOneOf(KonteringType.GARANTITILLEGG, KonteringType.GAT)) {
            text(
                bokmal { + "Garantitillegg" },
                nynorsk { + "Garantitillegg" },
                english { + "Guarantee supplement" }
            )
        }.orShowIf(ytelsenMedFeilutbetaling.isOneOf(KonteringType.AP_GJT_KAP19, KonteringType.AP_GJT)) {
            text(
                bokmal { + "Gjenlevendetillegg" },
                nynorsk { + "Attlevandetillegg" },
                english { + "Survivor's supplement" }
            )
        }.orShowIf(ytelsenMedFeilutbetaling.isOneOf(KonteringType.GP)) {
            text(
                bokmal { + "Grunnpensjon" },
                nynorsk { + "Grunnpensjon" },
                english { + "Basic pension" }
            )
        }.orShowIf(ytelsenMedFeilutbetaling.isOneOf(KonteringType.IP)) {
            text(
                bokmal { + "Inntektspensjon" },
                nynorsk { + "Inntektspensjon" },
                english { + "Income pensjon" }
            )
        }.orShowIf(ytelsenMedFeilutbetaling.isOneOf(KonteringType.KRIG_GY)) {
            text(
                bokmal { + "Krigspensjon / gammel yrkesskade" },
                nynorsk { + "Krigspensjon / gammel yrkesskade" },
                english { + "War pension / old work injury" }

            )
        }.orShowIf(ytelsenMedFeilutbetaling.isOneOf(KonteringType.MIN_NIVA_TILL_INDV)) {
            text(
                bokmal { + "Minstenivåtillegg individuelt" },
                nynorsk { + "Minstenivåtillegg individuelt" },
                english { + "Minimum supplement individual" },
            )
        }.orShowIf(ytelsenMedFeilutbetaling.isOneOf(KonteringType.MIN_NIVA_TILL_PPAR)) {
            text(
                bokmal { + "Minstenivåtillegg pensjonistpar" },
                nynorsk { + "Minstenivåtillegg pensjonistpar" },
                english { + "Minimum supplement couple" }
            )
        }.orShowIf(ytelsenMedFeilutbetaling.isOneOf(KonteringType.MISK)) {
            text(
                bokmal { + "Militær skadekur" },
                nynorsk { + "Militær skadekur" },
                english { + "Military injury treatment" }
            )
        }.orShowIf(ytelsenMedFeilutbetaling.isOneOf(KonteringType.PT)) {
            text(
                bokmal { + "Pensjonstillegg" },
                nynorsk { + "Pensjonstillegg" },
                english { + "Pension supplement" }
            )
        }.orShowIf(ytelsenMedFeilutbetaling.isOneOf(KonteringType.P_8_5_1_T)) {
            text(
                bokmal { + "Tillegg" },
                nynorsk { + "Tillegg" },
                english { + "Supplement" }
            )
        }.orShowIf(ytelsenMedFeilutbetaling.isOneOf(KonteringType.SKATT_F_GP)) {
            text(
                bokmal { + "Skattefri grunnpensjon" },
                nynorsk { + "Skattefri grunnpensjon" },
                english { + "Tax free basic pension" }
            )
        }.orShowIf(ytelsenMedFeilutbetaling.isOneOf(KonteringType.SKJERMT)) {
            text(
                bokmal { + "Skjermingstillegg" },
                nynorsk { + "Skjermingstillegg" },
                english { + "Supplement for the disabled" }
            )
        }.orShowIf(ytelsenMedFeilutbetaling.isOneOf(KonteringType.ST)) {
            text(
                bokmal { + "Særtillegg" },
                nynorsk { + "Særtillegg" },
                english { + "Special supplement" }
            )
        }.orShowIf(ytelsenMedFeilutbetaling.isOneOf(KonteringType.TFB)) {
            text(
                bokmal { + "Barnetillegg fellesbarn" },
                nynorsk { + "Barnetillegg fellesbarn" },
                english { + "Child supplement" }
            )
        }.orShowIf(ytelsenMedFeilutbetaling.isOneOf(KonteringType.TJENESTEPENSJON)) {
            text(
                bokmal { + "Tjenestepensjon" },
                nynorsk { + "Tjenestepensjon" },
                english { + "Occupational pension" }
            )
        }.orShowIf(ytelsenMedFeilutbetaling.isOneOf(KonteringType.TP)) {
            text(
                bokmal { + "Tilleggspensjon" },
                nynorsk { + "Tilleggspensjon" },
                english { + "Supplementary pension" }
            )
        }.orShowIf(ytelsenMedFeilutbetaling.isOneOf(KonteringType.TSB)) {
            text(
                bokmal { + "Barnetillegg særkullsbarn" },
                nynorsk { + "Barnetillegg særkullsbarn" },
                english { + "Child supplement" }
            )
        }
    }
}