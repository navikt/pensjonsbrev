package no.nav.pensjon.brev.maler.fraser.common

import no.nav.pensjon.brev.api.model.TilbakekrevingResultat
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.PlainTextOnlyPhrase
import no.nav.pensjon.brev.template.dsl.PlainTextOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.Language.*


data class ResultatAvVurderingenTextMappingStorBokstav(
    val resultatAvVurderingen: Expression<TilbakekrevingResultat>
) : PlainTextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun PlainTextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        showIf(resultatAvVurderingen.isOneOf(TilbakekrevingResultat.FULL_TILBAKEKREV)) {
            text(
                Bokmal to "Full tilbakekreving",
                Nynorsk to "Full tilbakekrevjing",
                English to "Full repayment"
            )
        }.orShowIf(resultatAvVurderingen.isOneOf(TilbakekrevingResultat.DELVIS_TILBAKEKREV)) {
            text(
                Bokmal to "Delvis tilbakekreving",
                Nynorsk to "Delvis tilbakekrevjing",
                English to "Part repayment"
            )
        }.orShowIf(resultatAvVurderingen.isOneOf(TilbakekrevingResultat.INGEN_TILBAKEKREV)) {
            text(
                Bokmal to "Ingen tilbakekreving",
                Nynorsk to "Ingen tilbakekrevjing",
                English to "No repayment"
            )
        }.orShowIf(resultatAvVurderingen.isOneOf(TilbakekrevingResultat.FORELDET)) {
            text(
                Bokmal to "Foreldet",
                Nynorsk to "Foreldet",
                English to "Outdated"
            )
        }.orShowIf(resultatAvVurderingen.isOneOf(TilbakekrevingResultat.FEILREGISTRERT)) {
            text(
                Bokmal to "Feil registrert",
                Nynorsk to "Feil registrert",
                English to "Incorrectly registered"
            )
        }
    }
}