package no.nav.pensjon.etterlatte.maler.tilbakekreving

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.BrevDTO
import no.nav.pensjon.etterlatte.maler.Element
import no.nav.pensjon.etterlatte.maler.Hovedmal
import no.nav.pensjon.etterlatte.maler.fraser.common.SakType
import no.nav.pensjon.etterlatte.maler.fraser.common.format
import no.nav.pensjon.etterlatte.maler.konverterElementerTilBrevbakerformat
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingBrevDTOSelectors.bosattUtland
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingBrevDTOSelectors.brukerNavn
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingBrevDTOSelectors.datoTilsvarBruker
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingBrevDTOSelectors.datoVarselEllerVedtak
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingBrevDTOSelectors.doedsbo
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingBrevDTOSelectors.innhold
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingBrevDTOSelectors.sakType
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingBrevDTOSelectors.tilbakekreving
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingBrevDTOSelectors.varselVedlagt
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingDTOSelectors.skalTilbakekreve
import no.nav.pensjon.etterlatte.maler.vedlegg.klageOgAnke
import java.time.LocalDate

data class TilbakekrevingBrevDTO(
	override val innhold: List<Element>,
	val sakType: SakType,
	val bosattUtland: Boolean,
	val brukerNavn: String,
	val doedsbo: Boolean,

	val varselVedlagt: Boolean,
	val datoVarselEllerVedtak: LocalDate,
	val datoTilsvarBruker: LocalDate?,

	val tilbakekreving: TilbakekrevingDTO
): BrevDTO

data class TilbakekrevingDTO(
	val fraOgMed: LocalDate,
	val tilOgMed: LocalDate,
	val skalTilbakekreve: Boolean,
	val helTilbakekreving: Boolean,
	val perioder: List<TilbakekrevingPeriode>,
	val summer: TilbakekrevingBeloeper
)

data class TilbakekrevingPeriode(
	val maaned: LocalDate,
	val beloeper: TilbakekrevingBeloeper,
	val resultat: TilbakekrevingResultat
)

data class TilbakekrevingBeloeper(
	val feilutbetaling: Kroner,
	val bruttoTilbakekreving: Kroner,
	val nettoTilbakekreving: Kroner,
	val fradragSkatt: Kroner,
	val renteTillegg: Kroner,
	val sumNettoRenter: Kroner
)

@TemplateModelHelpers
object TilbakekrevingFerdig: EtterlatteTemplate<TilbakekrevingBrevDTO>, Hovedmal {
	override val kode: EtterlatteBrevKode = EtterlatteBrevKode.TILBAKEKREVING_FERDIG

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
			showIf(tilbakekreving.skalTilbakekreve) {
				showIf(doedsbo) {
					textExpr(
						Bokmal to "Dødsboet må betale tilbake ".expr() + sakType.format(),
						Nynorsk to "".expr() + sakType.format(),
						English to "".expr() + sakType.format()
					)
				}.orShow {
					textExpr(
						Bokmal to "Du må betale tilbake ".expr() + sakType.format(),
						Nynorsk to "Du må betale tilbake ".expr() + sakType.format(),
						English to "Du må betale tilbake ".expr() + sakType.format()
					)
				}
			}.orShow {
				textExpr(
					Bokmal to "Du skal ikke betale tilbake ".expr() + sakType.format(),
					Nynorsk to "Du skal ikke betale tilbake ".expr() + sakType.format(),
					English to "Du skal ikke betale tilbake ".expr() + sakType.format(),
				)
			}

		}
		outline {
			showIf(doedsbo) {
				includePhrase(TilbakekrevingFraser.ViserTilVarselbrevDoedsbo(datoVarselEllerVedtak, datoTilsvarBruker))
			}.orShow {
				includePhrase(
					TilbakekrevingFraser.ViserTilVarselbrev(
						sakType,
						varselVedlagt,
						datoVarselEllerVedtak,
						datoTilsvarBruker
					)
				)
			}

			showIf(tilbakekreving.skalTilbakekreve) {
				showIf(doedsbo) {
					includePhrase(
						TilbakekrevingFraser.KonklusjonTilbakekrevingDoedsbo(sakType, tilbakekreving, brukerNavn)
					)
					includePhrase(TilbakekrevingFraser.TrukketSkatt)
					includePhrase(TilbakekrevingFraser.VedtakGjortEtterLover(tilbakekreving))
					includePhrase(TilbakekrevingFraser.ReferanseTilVedleggDoedsbo)
					includePhrase(TilbakekrevingFraser.SkattDoedsbo)
				}.orShow {
					includePhrase(TilbakekrevingFraser.KonklusjonTilbakekreving(sakType, tilbakekreving))
					includePhrase(TilbakekrevingFraser.TrukketSkatt)
					includePhrase(TilbakekrevingFraser.VedtakGjortEtterLover(tilbakekreving))
					includePhrase(TilbakekrevingFraser.ReferanseTilVedlegg)
					includePhrase(TilbakekrevingFraser.Skatt)
				}
			}.orShow {
				includePhrase(TilbakekrevingFraser.HovedInnholdIngenTilbakekreving(sakType, tilbakekreving, doedsbo))
			}

			konverterElementerTilBrevbakerformat(innhold)

		}

		includeAttachment(tilbakekrevingVedlegg, tilbakekreving)

		// Nasjonal
		includeAttachment(
			klageOgAnke(bosattUtland = false, tilbakekreving = true),
			innhold,
			bosattUtland.not().and(doedsbo.not())
		)
		// Bosatt utland
		includeAttachment(
			klageOgAnke(bosattUtland = true, tilbakekreving = true),
			innhold,
			bosattUtland.and(doedsbo.not())
		)
	}
}