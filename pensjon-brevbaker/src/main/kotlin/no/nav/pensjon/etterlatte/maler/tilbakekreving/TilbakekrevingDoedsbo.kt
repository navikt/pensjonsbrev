package no.nav.pensjon.etterlatte.maler.tilbakekreving

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.Hovedmal
import no.nav.pensjon.etterlatte.maler.fraser.common.format
import no.nav.pensjon.etterlatte.maler.konverterElementerTilBrevbakerformat
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingBrevDTOSelectors.bosattUtland
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingBrevDTOSelectors.brukerNavn
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingBrevDTOSelectors.datoTilsvarBruker
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingBrevDTOSelectors.datoVarselEllerVedtak
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingBrevDTOSelectors.innhold
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingBrevDTOSelectors.sakType
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingBrevDTOSelectors.tilbakekreving
import no.nav.pensjon.etterlatte.maler.vedlegg.klageOgAnke

@TemplateModelHelpers
object TilbakekrevingDoedsbo: EtterlatteTemplate<TilbakekrevingBrevDTO>, Hovedmal {
	override val kode: EtterlatteBrevKode = EtterlatteBrevKode.TILBAKEKREVING_DOEDSBO

	override val template = createTemplate(
		name = kode.name,
		letterDataType = TilbakekrevingBrevDTO::class,
		languages = languages(Bokmal, Nynorsk, English),
		letterMetadata = LetterMetadata(
			displayTitle = "Vedtak - Tilbakekreving",
			isSensitiv = true,
			distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
			brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
		),
	) {
		title {
			textExpr(
				Bokmal to "Dødsboet må betale tilbake ".expr() + sakType.format(),
				Nynorsk to "".expr() + sakType.format(),
				English to "".expr() + sakType.format()
			)
		}
		outline {
			includePhrase(
				TilbakekrevingFraser.ViserTilVarselbrevDoedsbo(
					datoVarselEllerVedtak,
					datoTilsvarBruker
				)
			)
			includePhrase(TilbakekrevingFraser.KonklusjonTilbakekrevingDoedsbo(sakType, tilbakekreving, brukerNavn))
			includePhrase(TilbakekrevingFraser.TrukketSkatt)
			includePhrase(TilbakekrevingFraser.VedtakGjortEtterLover(tilbakekreving))
			includePhrase(TilbakekrevingFraser.ReferanseTilVedleggDoedsbo)
			includePhrase(TilbakekrevingFraser.Skatt)

			konverterElementerTilBrevbakerformat(innhold)

		}

		includeAttachment(tilbakekrevingVedlegg, tilbakekreving)
	}
}