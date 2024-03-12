package no.nav.pensjon.etterlatte.maler.tilbakekreving

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.BrevDTO
import no.nav.pensjon.etterlatte.maler.Element
import no.nav.pensjon.etterlatte.maler.Hovedmal
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants
import no.nav.pensjon.etterlatte.maler.fraser.common.SakType
import no.nav.pensjon.etterlatte.maler.fraser.common.format
import no.nav.pensjon.etterlatte.maler.konverterElementerTilBrevbakerformat
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingFerdigDTOSelectors.data
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingFerdigDTOSelectors.innhold
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingInnholdDTOSelectors.bosattUtland
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingInnholdDTOSelectors.sakType
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingInnholdDTOSelectors.skalBetaleTilbake
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingInnholdDTOSelectors.varselVedlagt
import no.nav.pensjon.etterlatte.maler.vedlegg.klageOgAnkeNasjonal
import no.nav.pensjon.etterlatte.maler.vedlegg.klageOgAnkeUtland
import java.time.LocalDate

data class TilbakekrevingFerdigDTO(
	override val innhold: List<Element>,
	val data: TilbakekrevingInnholdDTO
): BrevDTO

data class TilbakekrevingInnholdDTO(
	val sakType: SakType,
	val skalBetaleTilbake: Boolean,

	val varselVedlagt: Boolean,
	val datoVarselEllerVedtak: LocalDate,

	val tilsvarfraBruker: Boolean,
	val datoTilsvarBruker: LocalDate?,

	val bosattUtland: Boolean,

	val harStrafferettslig: Boolean,
	val harForeldelse: Boolean,
	val perioder: List<TilbakekrevingPeriode>,
	val summer: TilbakekrevingBeloeper
)

data class TilbakekrevingPeriode(
	val maaned: LocalDate,
	val beloeper: TilbakekrevingBeloeper,
	val resultat: String
)

data class TilbakekrevingBeloeper(
	val feilutbetaling: Kroner,
	val bruttoTilbakekreving: Kroner,
	val nettoTilbakekreving: Kroner,
	val fradragSkatt: Kroner,
	val renteTillegg: Kroner
)


@TemplateModelHelpers
object TilbakekrevingFerdig: EtterlatteTemplate<TilbakekrevingFerdigDTO>, Hovedmal {
	override val kode: EtterlatteBrevKode = EtterlatteBrevKode.TILBAKEKREVING_FERDIG

	override val template = createTemplate(
		name = kode.name,
		letterDataType = TilbakekrevingFerdigDTO::class,
		languages = languages(Bokmal, Nynorsk, English),
		letterMetadata = LetterMetadata(
			displayTitle = "Vedtak - Tilbakekreving",
			isSensitiv = true,
			distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
			brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
		),
	) {
		title {
			showIf(data.skalBetaleTilbake) {
				textExpr(
					Bokmal to "Du må betale tilbake ".expr() + data.sakType.format(),
					Nynorsk to "Du må betale tilbake barnepensjon".expr() + data.sakType.format(),
					English to "Du må betale tilbake barnepensjon".expr() + data.sakType.format()
				)

			}.orShow {
				textExpr(
					Bokmal to "Du skal ikke betale tilbake barnepensjon".expr() + data.sakType.format(),
					Nynorsk to "Du skal ikke tilbake barnepensjon".expr() + data.sakType.format(),
					English to "Du skal ikke tilbake barnepensjon".expr() + data.sakType.format()
				)
			}

		}
		outline {
			showIf(data.varselVedlagt) {
				// TODO Vi viser til forhåndsvarsele osv..
			}.orShow {
				// TODO Vi viser til forhåndsvarsele osv..
			}

			showIf(data.skalBetaleTilbake) {
				// TODO Du har fått utbetalt for my osv
			}.orShow {
				// TODO Du har fått utbetalt for my osv
			}

			konverterElementerTilBrevbakerformat(innhold)

			// TODO med vennlig hilsen? Allerede inkludert?
		}

		includeAttachment(tilbakekrevingVedlegg, data, data.skalBetaleTilbake)
		// Nasjonal
		includeAttachment(klageOgAnkeNasjonal, innhold, data.bosattUtland.not())
		// Bosatt utland
		includeAttachment(klageOgAnkeUtland, innhold, data.bosattUtland)
	}
}