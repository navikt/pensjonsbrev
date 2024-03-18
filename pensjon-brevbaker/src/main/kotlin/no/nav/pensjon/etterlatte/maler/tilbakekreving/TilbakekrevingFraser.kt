package no.nav.pensjon.etterlatte.maler.tilbakekreving


import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.ifElse
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.etterlatte.maler.fraser.common.format
import no.nav.pensjon.etterlatte.maler.fraser.common.SakType
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingBeloeperSelectors.bruttoTilbakekreving
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingBeloeperSelectors.feilutbetaling
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingBeloeperSelectors.fradragSkatt
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingBeloeperSelectors.nettoTilbakekreving
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingBeloeperSelectors.renteTillegg
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingDTOSelectors.fraOgMed
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingDTOSelectors.harRenteTillegg
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingDTOSelectors.helTilbakekreving
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingDTOSelectors.summer
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingDTOSelectors.tilOgMed
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingPeriodeSelectors.beloeper
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingPeriodeSelectors.maaned
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingPeriodeSelectors.resultat
import java.time.LocalDate

object TilbakekrevingFraser {

	data class ViserTilVarselbrev(
		val sakType: Expression<SakType>,
		val varselVedlagt: Expression<Boolean>,
		val datoVarselEllerVedtak: Expression<LocalDate>,
		val datoTilsvarBruker: Expression<LocalDate?>
	): OutlinePhrase<LangBokmalNynorskEnglish>() {
		override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
			paragraph {
				showIf(varselVedlagt) {
					textExpr(
						Bokmal to "Vi viser til forhåndsvarselet vårt som fulgte vedtak datert ".expr() +
								datoVarselEllerVedtak.format(),
						Nynorsk to "Vi viser til forhåndsvarselet vårt som fulgte vedtak datert ".expr() +
								datoVarselEllerVedtak.format(),
						English to "Vi viser til forhåndsvarselet vårt som fulgte vedtak datert ".expr() +
								datoVarselEllerVedtak.format(),
					)
				}.orShow {
					textExpr(
						Bokmal to "Vi viser til forhåndsvarselet vårt om at vi vurderer om du må betale tilbake ".expr() +
								sakType.format() + " av " + datoVarselEllerVedtak.format(),
						Nynorsk to "Vi viser til forhåndsvarselet vårt om at vi vurderer om du må betale tilbake ".expr() +
								sakType.format() + " av " + datoVarselEllerVedtak.format(),
						English to "Vi viser til forhåndsvarselet vårt om at vi vurderer om du må betale tilbake ".expr() +
								sakType.format() + " av " + datoVarselEllerVedtak.format(),
					)
				}
				ifNotNull(datoTilsvarBruker) { datoTilsvarBruker ->
					textExpr(
						Bokmal to ", og dine kommentarer til dette som vi mottok den".expr() + datoTilsvarBruker.format(),
						Nynorsk to ", og dine kommentarer til dette som vi mottok den".expr() + datoTilsvarBruker.format(),
						English to ", og dine kommentarer til dette som vi mottok den".expr() + datoTilsvarBruker.format(),
					)
				}
				text(
					Bokmal to ".",
					Nynorsk to ".",
					English to ".",
				)
			}
		}
	}

	data class HovedInnholdSkalTilbakekreve(
		val sakType: Expression<SakType>,
		val tilbakekreving: Expression<TilbakekrevingDTO>
	): OutlinePhrase<LangBokmalNynorskEnglish>() {
		override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
			val feilutbetaling = tilbakekreving.summer.feilutbetaling
			val nettoTilbakekreving = tilbakekreving.summer.nettoTilbakekreving
			val renteTillegg = tilbakekreving.summer.renteTillegg
			val fraOgMed = tilbakekreving.fraOgMed
			val tilOgMed = tilbakekreving.tilOgMed

			paragraph {
				textExpr(
					Bokmal to "Du har fått utbetalt for mye ".expr() + sakType.format() +
							" fra " + fraOgMed.format() + " til " + tilOgMed.format() +
							". Det feilutbetalte beløpet er " + feilutbetaling.format() + " kroner inkludert skatt.",
					Nynorsk to "Du har fått utbetalt for mye ".expr() + sakType.format() +
							" fra " + fraOgMed.format() + " til " + tilOgMed.format() +
							". Det feilutbetalte beløpet er " + feilutbetaling.format() + " kroner inkludert skatt.",
					English to "Du har fått utbetalt for mye ".expr() + sakType.format() +
							" fra " + fraOgMed.format() + " til " + tilOgMed.format() +
							". Det feilutbetalte beløpet er " + feilutbetaling.format() + " kroner inkludert skatt.",
				)
			}

			paragraph {
				textExpr(
					Bokmal to "Vi har kommet frem til at du skal betale tilbake ".expr() +
							ifElse(tilbakekreving.helTilbakekreving, "hele", "deler av") + " beløpet.",
					Nynorsk to "Vi har kommet frem til at du skal betale tilbake ".expr() +
							ifElse(tilbakekreving.helTilbakekreving, "hele", "deler av") + " beløpet.",
					English to "Vi har kommet frem til at du skal betale tilbake ".expr() +
							ifElse(tilbakekreving.helTilbakekreving, "hele", "deler av") + " beløpet.",
				)
				showIf(tilbakekreving.harRenteTillegg) {
					textExpr(
						Bokmal to " Du må også betale ".expr() + renteTillegg.format() +
								" kroner i renter. Til sammen skal du betale " + nettoTilbakekreving.format() +
								" kroner etter at skatten er trukket fra.",
						Nynorsk to " Du må også betale ".expr() + renteTillegg.format() +
								" kroner i renter. Til sammen skal du betale " + nettoTilbakekreving.format() +
								" kroner etter at skatten er trukket fra.",
						English to " Du må også betale ".expr() + renteTillegg.format() +
								" kroner i renter. Til sammen skal du betale " + nettoTilbakekreving.format() +
								" kroner etter at skatten er trukket fra.",
					)
				}
			}

			paragraph {
				text(
					Bokmal to "Beløpet som er trukket i skatt får vi tilbake fra Skatteetaten.",
					Nynorsk to "Beløpet som er trukket i skatt får vi tilbake fra Skatteetaten.",
					English to "Beløpet som er trukket i skatt får vi tilbake fra Skatteetaten.",
				)
			}

			paragraph {
				text(
					Bokmal to "Vedtaket er gjort etter folketrygdloven § 22-15.",
					Nynorsk to "Vedtaket er gjort etter folketrygdloven § 22-15.",
					English to "Vedtaket er gjort etter folketrygdloven § 22-15.",
				)
				showIf(tilbakekreving.harRenteTillegg) {
					text(
						Bokmal to " §§ 22-15 og 22-17 a.",
						Nynorsk to " §§ 22-15 og 22-17 a.",
						English to " §§ 22-15 og 22-17 a.",
					)
				}
			}

			paragraph {
				text(
					Bokmal to "I vedleggene til dette brevet finner du en oversikt over periodene med " +
							"feilutbetalinger og beløpet du må betale tilbake, og en oversikt over rettighetene dine.",
					Nynorsk to "I vedleggene til dette brevet finner du en oversikt over periodene med " +
							"feilutbetalinger og beløpet du må betale tilbake, og en oversikt over rettighetene dine.",
					English to "I vedleggene til dette brevet finner du en oversikt over periodene med " +
							"feilutbetalinger og beløpet du må betale tilbake, og en oversikt over rettighetene dine.",
				)
			}
		}
	}

	object Skatt: OutlinePhrase<LangBokmalNynorskEnglish>() {
		override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
			title2 {
				text(
					Bokmal to "Skatt og skatteoppgjør",
					Nynorsk to "Skatt og skatteoppgjør",
					English to "Skatt og skatteoppgjør",
				)
			}
			paragraph {
				text(
					Bokmal to "Vi gir opplysninger til Skatteetaten om dette vedtaket. " +
							"De fastsetter det endelige skattebeløpet, og gjør nødvendige korrigeringer i skatteoppgjøret ditt.",
					Nynorsk to "Vi gir opplysninger til Skatteetaten om dette vedtaket. " +
							"De fastsetter det endelige skattebeløpet, og gjør nødvendige korrigeringer i skatteoppgjøret ditt.",
					English to "Vi gir opplysninger til Skatteetaten om dette vedtaket. " +
							"De fastsetter det endelige skattebeløpet, og gjør nødvendige korrigeringer i skatteoppgjøret ditt.",

					)
			}
		}
	}

	data class HovedInnholdIngenTilbakekreving(
		val sakType: Expression<SakType>,
		val tilbakekreving: Expression<TilbakekrevingDTO>
	): OutlinePhrase<LangBokmalNynorskEnglish>() {
		override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
			val feilutbetaling = tilbakekreving.summer.feilutbetaling
			val fraOgMed = tilbakekreving.fraOgMed
			val tilOgMed = tilbakekreving.tilOgMed

			paragraph {
				textExpr(
					Bokmal to "Du har fått utbetalt for mye ".expr() + sakType.format() +
							" fra " + fraOgMed.format() + " til " + tilOgMed.format() +
							". Dette er " + feilutbetaling.format() + " kroner inkludert skatt.",
					Nynorsk to "Du har fått utbetalt for mye ".expr() + sakType.format() +
							" fra " + fraOgMed.format() + " til " + tilOgMed.format() +
							". Dette er " + feilutbetaling.format() + " kroner inkludert skatt.",
					English to "Du har fått utbetalt for mye ".expr() + sakType.format() +
							" fra " + fraOgMed.format() + " til " + tilOgMed.format() +
							". Dette er " + feilutbetaling.format() + " kroner inkludert skatt.",
				)
			}

			paragraph {
				text(
					Bokmal to "Vi har kommet fram til at du ikke skal betale tilbake beløpet.",
					Nynorsk to "Vi har kommet fram til at du ikke skal betale tilbake beløpet.",
					English to "Vi har kommet fram til at du ikke skal betale tilbake beløpet.",
				)
			}

			paragraph {
				text(
					Bokmal to "Vedtaket er gjort etter folketrygdloven § 22-15.",
					Nynorsk to "Vedtaket er gjort etter folketrygdloven § 22-15.",
					English to "Vedtaket er gjort etter folketrygdloven § 22-15.",
				)
			}

			paragraph {
				text(
					Bokmal to "I vedlegget til dette brevet finner du en oversikt over rettighetene dine.",
					Nynorsk to "I vedlegget til dette brevet finner du en oversikt over rettighetene dine.",
					English to "I vedlegget til dette brevet finner du en oversikt over rettighetene dine.",
				)
			}
		}
	}

}

object TilbakekrevingVedleggFraser {

	data class OversiktOverFeilutbetalinger(
		val summer: Expression<TilbakekrevingBeloeper>
	): OutlinePhrase<LangBokmalNynorskEnglish>() {
		override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
			paragraph {
				table(
					header = {
						column(1) {
							text(
								Bokmal to "Beløp som skal kreves tilbake i hele feilutbetalingsperioden",
								Nynorsk to "Beløp som skal kreves tilbake i hele feilutbetalingsperioden",
								English to "Beløp som skal kreves tilbake i hele feilutbetalingsperioden",
							)
						}
						column(2) {}
					}
				) {
					row {
						cell {
							text(
								Bokmal to "Brutto tilbakekreving",
								Nynorsk to "Brutto tilbakekreving",
								English to "Brutto tilbakekreving",
							)
						}
						cell {
							includePhrase(Felles.KronerText(summer.bruttoTilbakekreving))
						}
					}
					row {
						cell {
							text(
								Bokmal to "- fradrag skatt",
								Nynorsk to "- fradrag skatt",
								English to "- fradrag skatt"
							)
						}
						cell {
							includePhrase(Felles.KronerText(summer.fradragSkatt))
						}
					}
					row {
						cell {
							text(
								Bokmal to "Netto tilbakekreving",
								Nynorsk to "Netto tilbakekreving",
								English to "Netto tilbakekreving",
							)
						}
						cell {
							includePhrase(Felles.KronerText(summer.nettoTilbakekreving))
						}
					}
					row {
						cell {
							text(
								Bokmal to "+ Rentetillegg",
								Nynorsk to "+ Rentetillegg",
								English to "+ Rentetillegg"
							)
						}
						cell {
							includePhrase(Felles.KronerText(summer.renteTillegg))
						}
					}
				}
			}
		}
	}

	data class PeriodeTabell(
		val perioder: Expression<List<TilbakekrevingPeriode>>
	): OutlinePhrase<LangBokmalNynorskEnglish>() {
		override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
			paragraph {
				table(
					header = {
						column(1) {
							text(
								Bokmal to "Måned/år",
								Nynorsk to "",
								English to "",
							)
						}
						column(2) {
							text(
								Bokmal to "Feilutbetalt beløp",
								Nynorsk to "",
								English to "",
							)
						}
						column(3) {
							text(
								Bokmal to "Resultat",
								Nynorsk to "",
								English to "",
							)
						}
						column(4) {
							text(
								Bokmal to "Brutto tilbakekreving",
								Nynorsk to "",
								English to "",
							)
						}
						column(5) {
							text(
								Bokmal to "Netto tilbakekreving",
								Nynorsk to "",
								English to "",
							)
						}
					}
				) {
					forEach(perioder) { periode ->
						row {
							cell {
								textExpr (
									Bokmal to periode.maaned.format(),
									Nynorsk to periode.maaned.format(),
									English to periode.maaned.format(),
								)
							}
							cell {
								includePhrase(Felles.KronerText(periode.beloeper.feilutbetaling))
							}
							cell {
								textExpr (
									Bokmal to periode.resultat.format(),
									Nynorsk to periode.resultat.format(),
									English to periode.resultat.format(),
								)
							}
							cell {
								includePhrase(Felles.KronerText(periode.beloeper.bruttoTilbakekreving))
							}
							cell {
								includePhrase(Felles.KronerText(periode.beloeper.nettoTilbakekreving))
							}
						}
					}

				}
			}
		}
	}

}