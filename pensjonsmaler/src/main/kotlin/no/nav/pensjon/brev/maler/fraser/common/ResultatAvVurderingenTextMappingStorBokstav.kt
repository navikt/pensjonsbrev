package no.nav.pensjon.brev.maler.fraser.common

import no.nav.pensjon.brev.api.model.TilbakekrevingResultat
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.PlainTextOnlyPhrase
import no.nav.pensjon.brev.template.dsl.PlainTextOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.text


data class ResultatAvVurderingenTextMappingStorBokstav(
    val resultatAvVurderingen: Expression<TilbakekrevingResultat>
) : PlainTextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun PlainTextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        showIf(resultatAvVurderingen.isOneOf(TilbakekrevingResultat.FULL_TILBAKEKREV)) {
            text(
                bokmal { + "Full tilbakekreving" },
                nynorsk { + "Full tilbakekrevjing" },
                english { + "Full repayment" }
            )
        }.orShowIf(resultatAvVurderingen.isOneOf(TilbakekrevingResultat.DELVIS_TILBAKEKREV)) {
            text(
                bokmal { + "Delvis tilbakekreving" },
                nynorsk { + "Delvis tilbakekrevjing" },
                english { + "Part repayment" }
            )
        }.orShowIf(resultatAvVurderingen.isOneOf(TilbakekrevingResultat.INGEN_TILBAKEKREV)) {
            text(
                bokmal { + "Ingen tilbakekreving" },
                nynorsk { + "Ingen tilbakekrevjing" },
                english { + "No repayment" }
            )
        }.orShowIf(resultatAvVurderingen.isOneOf(TilbakekrevingResultat.FORELDET)) {
            text(
                bokmal { + "Foreldet" },
                nynorsk { + "Foreldet" },
                english { + "Outdated" }
            )
        }.orShowIf(resultatAvVurderingen.isOneOf(TilbakekrevingResultat.FEILREGISTRERT)) {
            text(
                bokmal { + "Feil registrert" },
                nynorsk { + "Feil registrert" },
                english { + "Incorrectly registered" }
            )
        }
    }
}