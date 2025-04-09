package no.nav.pensjon.brev.maler.fraser.common

import no.nav.pensjon.brev.api.model.KonteringType
import no.nav.pensjon.brev.maler.fraser.alderspensjon.InfoAFPprivatAP
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.PlainTextOnlyPhrase
import no.nav.pensjon.brev.template.dsl.PlainTextOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.Language.*


data class KonteringTypeYtelseTextMappingStorBokstav(
    val ytelsenMedFeilutbetaling: Expression<KonteringType>
) : PlainTextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun PlainTextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        showIf(ytelsenMedFeilutbetaling.isOneOf(KonteringType.AFP_KOMP_TILLEGG)) {
            text(
                Bokmal to "AFP kompensasjonstillegg",
                Nynorsk to "AFP kompensasjonstillegg",
                English to "AFP compensation supplement"
            )
        }.orShowIf(ytelsenMedFeilutbetaling.isOneOf(KonteringType.AFP_KRONETILLEGG)) {
            text(
                Bokmal to "AFP kronetillegg",
                Nynorsk to "AFP kronetillegg",
                English to "AFP krone supplement"
            )
        }.orShowIf(ytelsenMedFeilutbetaling.isOneOf(KonteringType.AFP_LIVSVARIG)) {
            text(
                Bokmal to "AFP livsvarig del",
                Nynorsk to "AFP livsvarig del",
                English to "AFP life long part"
            )
        }.orShowIf(ytelsenMedFeilutbetaling.isOneOf(KonteringType.AFP_T)) {
            text(
                Bokmal to "AFP-tillegg",
                Nynorsk to "AFP-tillegg",
                English to "AFP supplement"
            )
        }.orShowIf(ytelsenMedFeilutbetaling.isOneOf(KonteringType.BARNETILSYN)) {
            text(
                Bokmal to "Barnetillegg",
                Nynorsk to "Barnetillegg",
                English to "Child supplement"
            )
        }.orShowIf(ytelsenMedFeilutbetaling.isOneOf(KonteringType.ET)) {
            text(
                Bokmal to "Ektefelletillegg",
                Nynorsk to "Ektefelletillegg",
                English to "Spouse supplement"
            )
        }.orShowIf(ytelsenMedFeilutbetaling.isOneOf(KonteringType.FAM_T)) {
            text(
                Bokmal to "Familietillegg",
                Nynorsk to "Familietillegg",
                English to "Family supplement"
            )
        }.orShowIf(ytelsenMedFeilutbetaling.isOneOf(KonteringType.GAP)) {
            text(
                Bokmal to "Garantipensjon",
                Nynorsk to "Garantipensjon",
                English to "Guarantee pension"
            )
        }.orShowIf(ytelsenMedFeilutbetaling.isOneOf(KonteringType.GARANTITILLEGG, KonteringType.GAT)) {
            text(
                Bokmal to "Garantitillegg",
                Nynorsk to "Garantitillegg",
                English to "Guarantee supplement"
            )
        }.orShowIf(ytelsenMedFeilutbetaling.isOneOf(KonteringType.AP_GJT_KAP19, KonteringType.AP_GJT)) {
            text(
                Bokmal to "Gjenlevendetillegg",
                Nynorsk to "Attlevandetillegg",
                English to "Survivor's supplement"
            )
        }.orShowIf(ytelsenMedFeilutbetaling.isOneOf(KonteringType.GP)) {
            text(
                Bokmal to "Grunnpensjon",
                Nynorsk to "Grunnpensjon",
                English to "Basic pension"
            )
        }.orShowIf(ytelsenMedFeilutbetaling.isOneOf(KonteringType.IP)) {
            text(
                Bokmal to "Inntektspensjon",
                Nynorsk to "Inntektspensjon",
                English to "Income pensjon"
            )
        }.orShowIf(ytelsenMedFeilutbetaling.isOneOf(KonteringType.KRIG_GY)) {
            text(
                Bokmal to "Krigspensjon / gammel yrkesskade",
                Nynorsk to "Krigspensjon / gammel yrkesskade",
                English to "War pension / old work injury"

            )
        }.orShowIf(ytelsenMedFeilutbetaling.isOneOf(KonteringType.MIN_NIVA_TILL_INDV)) {
            text(
                Bokmal to "Minstenivåtillegg individuelt",
                Nynorsk to "Minstenivåtillegg individuelt",
                English to "Minimum supplement individual",
            )
        }.orShowIf(ytelsenMedFeilutbetaling.isOneOf(KonteringType.MIN_NIVA_TILL_PPAR)) {
            text(
                Bokmal to "Minstenivåtillegg pensjonistpar",
                Nynorsk to "Minstenivåtillegg pensjonistpar",
                English to "Minimum supplement couple"
            )
        }.orShowIf(ytelsenMedFeilutbetaling.isOneOf(KonteringType.MISK)) {
            text(
                Bokmal to "Militær skadekur",
                Nynorsk to "Militær skadekur",
                English to "Military injury treatment"
            )
        }.orShowIf(ytelsenMedFeilutbetaling.isOneOf(KonteringType.PT)) {
            text(
                Bokmal to "Pensjonstillegg",
                Nynorsk to "Pensjonstillegg",
                English to "Pension supplement"
            )
        }.orShowIf(ytelsenMedFeilutbetaling.isOneOf(KonteringType.SKATT_F_GP)) {
            text(
                Bokmal to "Skattefri grunnpensjon",
                Nynorsk to "Skattefri grunnpensjon",
                English to "Tax free basic pension"
            )
        }.orShowIf(ytelsenMedFeilutbetaling.isOneOf(KonteringType.SKJERMT)) {
            text(
                Bokmal to "Skjermingstillegg",
                Nynorsk to "Skjermingstillegg",
                English to "Supplement for the disabled"
            )
        }.orShowIf(ytelsenMedFeilutbetaling.isOneOf(KonteringType.ST)) {
            text(
                Bokmal to "Særtillegg",
                Nynorsk to "Særtillegg",
                English to "Special supplement"
            )
        }.orShowIf(ytelsenMedFeilutbetaling.isOneOf(KonteringType.TFB)) {
            text(
                Bokmal to "Barnetillegg fellesbarn",
                Nynorsk to "Barnetillegg fellesbarn",
                English to "Child supplement"
            )
        }.orShowIf(ytelsenMedFeilutbetaling.isOneOf(KonteringType.TJENESTEPENSJON)) {
            text(
                Bokmal to "Tjenestepensjon",
                Nynorsk to "Tjenestepensjon",
                English to "Occupational pension"
            )
        }.orShowIf(ytelsenMedFeilutbetaling.isOneOf(KonteringType.TP)) {
            text(
                Bokmal to "Tilleggspensjon",
                Nynorsk to "Tilleggspensjon",
                English to "Supplementary pension"
            )
        }.orShowIf(ytelsenMedFeilutbetaling.isOneOf(KonteringType.TSB)) {
            text(
                Bokmal to "Barnetillegg særkullsbarn",
                Nynorsk to "Barnetillegg særkullsbarn",
                English to "Child supplement"
            )
        }
    }
}