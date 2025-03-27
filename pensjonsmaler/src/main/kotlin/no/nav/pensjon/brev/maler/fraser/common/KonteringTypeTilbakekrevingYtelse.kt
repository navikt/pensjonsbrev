package no.nav.pensjon.brev.maler.fraser.common

import no.nav.pensjon.brev.api.model.KonteringType
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.PlainTextOnlyPhrase
import no.nav.pensjon.brev.template.dsl.PlainTextOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.Language.*


data class KonteringTypeTilbakekrevingYtelse(
    val ytelsenMedFeilutbetaling: Expression<KonteringType>
) : PlainTextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun PlainTextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        showIf(ytelsenMedFeilutbetaling.isOneOf(KonteringType.AAP)) {
            text(
                Bokmal to "Arbeidsavklaringspenger",
                Nynorsk to "Arbeidsavklaringspenger",
                English to "Work assessment allowance"
            )
        }.orShowIf(ytelsenMedFeilutbetaling.isOneOf(KonteringType.AFP_KOMP_TILLEGG)) {
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
        }.orShowIf(ytelsenMedFeilutbetaling.isOneOf(KonteringType.ANNET)) {
            text(
                Bokmal to "Annet",
                Nynorsk to "Annet",
                English to "Other"
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
        }.orShowIf(ytelsenMedFeilutbetaling.isOneOf(KonteringType.FAST_UTGIFT_T)) {
            text(
                Bokmal to "Tillegg faste utgifter",
                Nynorsk to "Tillegg faste utgifter",
                English to "Supplement for regular expenses"
            )
        }.orShowIf(ytelsenMedFeilutbetaling.isOneOf(KonteringType.FEILKONTO)) {
            text(
                Bokmal to "Feilkonto",
                Nynorsk to "Feilkonto",
                English to "Incorrect account"
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
        }.orShowIf(ytelsenMedFeilutbetaling.isOneOf(KonteringType.GP)) {}

    }

}