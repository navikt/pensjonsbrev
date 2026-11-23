package no.nav.pensjon.brev.ufore.maler.fraser

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.PlainTextOnlyPhrase
import no.nav.pensjon.brev.template.dsl.PlainTextOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.TilbakekrevingResultat


data class ResultatAvVurderingenTextMappingStorBokstav(
    val resultatAvVurderingen: Expression<TilbakekrevingResultat>
) : PlainTextOnlyPhrase<LangBokmalNynorsk>() {
    override fun PlainTextOnlyScope<LangBokmalNynorsk, Unit>.template() {
        showIf(resultatAvVurderingen.isOneOf(TilbakekrevingResultat.FULL_TILBAKEKREV)) {
            text(
                bokmal { + "Full tilbakekreving" },
                nynorsk { + "Full tilbakekrevjing" },
            )
        }.orShowIf(resultatAvVurderingen.isOneOf(TilbakekrevingResultat.DELVIS_TILBAKEKREV)) {
            text(
                bokmal { + "Delvis tilbakekreving" },
                nynorsk { + "Delvis tilbakekrevjing" },
            )
        }.orShowIf(resultatAvVurderingen.isOneOf(TilbakekrevingResultat.INGEN_TILBAKEKREV)) {
            text(
                bokmal { + "Ingen tilbakekreving" },
                nynorsk { + "Ingen tilbakekrevjing" },
            )
        }.orShowIf(resultatAvVurderingen.isOneOf(TilbakekrevingResultat.FORELDET)) {
            text(
                bokmal { + "Foreldet" },
                nynorsk { + "Foreldet" },
            )
        }.orShowIf(resultatAvVurderingen.isOneOf(TilbakekrevingResultat.FEILREGISTRERT)) {
            text(
                bokmal { + "Feil registrert" },
                nynorsk { + "Feil registrert" },
            )
        }
    }
}