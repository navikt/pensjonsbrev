package no.nav.pensjon.etterlatte.maler.tilbakekreving

import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.etterlatte.maler.formatMaanedAar
import no.nav.pensjon.etterlatte.maler.fraser.common.SakType
import no.nav.pensjon.etterlatte.maler.fraser.common.format
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingBeloeperSelectors.bruttoTilbakekreving
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingBeloeperSelectors.feilutbetaling
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingBeloeperSelectors.fradragSkatt
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingBeloeperSelectors.nettoTilbakekreving
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingBeloeperSelectors.renteTillegg
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingBeloeperSelectors.sumNettoRenter
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingDTOSelectors.fraOgMed
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
		val varsel: Expression<TilbakekrevingVarsel>,
		val datoVarselEllerVedtak: Expression<LocalDate>,
		val datoTilsvarBruker: Expression<LocalDate?>
	): OutlinePhrase<LangBokmalNynorskEnglish>() {
		override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
			paragraph {
				showIf(varsel.equalTo(TilbakekrevingVarsel.MED_I_ENDRINGSBREV)) {
					textExpr(
						Bokmal to "Vi viser til forhåndsvarselet vårt som fulgte vedtak datert ".expr() +
								datoVarselEllerVedtak.format(),
						Nynorsk to "Vi viser til førehandsvarselet som følgde med vedtaket av ".expr() +
								datoVarselEllerVedtak.format(),
						English to "We refer to the notice attached with the decision dated ".expr() +
								datoVarselEllerVedtak.format(),
					)
				}.orShowIf(varsel.equalTo(TilbakekrevingVarsel.EGET_BREV)) {
					textExpr(
						Bokmal to "Vi viser til forhåndsvarselet vårt om at vi vurderer om du må betale tilbake ".expr() +
								sakType.format() + " av " + datoVarselEllerVedtak.format(),
						Nynorsk to "Vi viser til førehandsvarselet vårt om at vi vurderer om du må betale tilbake ".expr() +
								sakType.format() + " av " + datoVarselEllerVedtak.format(),
						English to "We refer to the notice, where we consider claiming reimbursement of ".expr() +
								sakType.format() + " from " + datoVarselEllerVedtak.format(),
					)
				}.orShowIf(varsel.equalTo(TilbakekrevingVarsel.AAPENBART_UNOEDVENDIG)) {
					textExpr(
						Bokmal to "Vi viser til vedtaket vårt av ".expr() + datoVarselEllerVedtak.format(),
						Nynorsk to "Vi viser til vedtaket vårt av ".expr() + datoVarselEllerVedtak.format(),
						English to "We refer to the decision dated ".expr() + datoVarselEllerVedtak.format(),
					)
				}
				ifNotNull(datoTilsvarBruker) { datoTilsvarBruker ->
					textExpr(
						Bokmal to ", og dine kommentarer til dette som vi mottok den ".expr() + datoTilsvarBruker.format(),
						Nynorsk to ", og kommentarane som vi fekk frå deg på dette ".expr() + datoTilsvarBruker.format(),
						English to ", and your response, which we received on ".expr() + datoTilsvarBruker.format(),
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

	data class ViserTilVarselbrevDoedsbo(
		val datoVarselEllerVedtak: Expression<LocalDate>,
		val datoTilsvarBruker: Expression<LocalDate?>
	): OutlinePhrase<LangBokmalNynorskEnglish>() {
		override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
			paragraph {
				textExpr(
					Bokmal to "Vi viser til forhåndsvarselet vårt av ".expr() + datoVarselEllerVedtak.format(),
					Nynorsk to "Vi viser til førehandsvarselet vårt av ".expr() + datoVarselEllerVedtak.format(),
					English to "We refer to our notice of ".expr() + datoVarselEllerVedtak.format(),
				)
				ifNotNull(datoTilsvarBruker) { datoTilsvarBruker ->
					textExpr(
						Bokmal to ", og kommentarer til dette som vi mottok den ".expr() + datoTilsvarBruker.format(),
						Nynorsk to ", og kommentarane som vi fekk på dette ".expr() + datoTilsvarBruker.format(),
						English to ", and your response, which we received on ".expr() + datoTilsvarBruker.format(),
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

	data class KonklusjonTilbakekreving(
		val sakType: Expression<SakType>,
		val tilbakekreving: Expression<TilbakekrevingDTO>
	): OutlinePhrase<LangBokmalNynorskEnglish>() {
		override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
			val feilutbetaling = tilbakekreving.summer.feilutbetaling
			val nettoTilbakekreving = tilbakekreving.summer.nettoTilbakekreving
			val renteTillegg = tilbakekreving.summer.renteTillegg
			val sumTilbakekreving = tilbakekreving.summer.sumNettoRenter
			val fraOgMed = tilbakekreving.fraOgMed
			val tilOgMed = tilbakekreving.tilOgMed
			val harFradragSkatt = tilbakekreving.summer.fradragSkatt.greaterThan(0)

			paragraph {
				textExpr(
					Bokmal to "Du har fått utbetalt for mye ".expr() + sakType.format() +
							" fra " + fraOgMed.format() + " til " + tilOgMed.format() +
							". Det feilutbetalte beløpet er " + feilutbetaling.format() + " kroner" +
							ifElse(harFradragSkatt, " inkludert skatt.", "."),
					Nynorsk to "Du har fått utbetalt for mykje ".expr() + sakType.format() +
							" frå " + fraOgMed.format() + " til " + tilOgMed.format() +
							". Det feilutbetalte beløpet er " + feilutbetaling.format() + " kroner" +
							ifElse(harFradragSkatt, " inkludert skatt.", "."),
					English to "You received too much in ".expr() + sakType.format() +
							" from " + fraOgMed.format() + " to " + tilOgMed.format() +
							". The incorrect amount totals NOK " + feilutbetaling.format() +
							ifElse(harFradragSkatt, " including tax.", "."),
				)
			}

			paragraph {
				textExpr(
					Bokmal to "Vi har kommet frem til at du skal betale tilbake ".expr() +
							ifElse(tilbakekreving.helTilbakekreving, "hele", "deler av") + " beløpet.",
					Nynorsk to "Vi har kome fram til at du skal betale tilbake ".expr() +
							ifElse(tilbakekreving.helTilbakekreving, "heile", "delar av") + " beløpet.",
					English to "Reimbursement for ".expr() +
							ifElse(tilbakekreving.helTilbakekreving, "all", "part") +
							" of this amount must be paid.",
				)
				showIf(renteTillegg.greaterThan(0)) {
					textExpr(
						Bokmal to " Det blir ".expr() + nettoTilbakekreving.format() + " kroner" +
								ifElse(harFradragSkatt, " etter at skatten er trukket fra.", ".") +
								" Du må også betale " + renteTillegg.format() +
								" kroner i renter. Til sammen skal du betale " + sumTilbakekreving.format() + " kroner.",
						Nynorsk to " Dette utgjer ".expr() + nettoTilbakekreving.format() + " kroner" +
								ifElse(harFradragSkatt, " etter at skatten er trekt frå.", ".") +
								" I tillegg må du betale " + renteTillegg.format() +
								" kroner i renter. Til saman skal du betale " + sumTilbakekreving.format() + " kroner.",
						English to " This is NOK ".expr() + nettoTilbakekreving.format() +
								ifElse(harFradragSkatt, " after tax.", ".") +
								" You must also pay NOK " + renteTillegg.format() + " in interest. In total," +
								" you must pay NOK " + sumTilbakekreving.format() + "."
					)
				}.orShow {
					textExpr(
						Bokmal to " Det blir ".expr() + sumTilbakekreving.format() + " kroner" +
								ifElse(harFradragSkatt, " etter at skatten er trukket fra.", "."),
						Nynorsk to " Dette utgjer ".expr() + sumTilbakekreving.format() + " kroner" +
								ifElse(harFradragSkatt, " etter at skatten er trekt frå.", "."),
						English to " This is NOK ".expr() + sumTilbakekreving.format() +
								ifElse(harFradragSkatt, " after tax.", ".")
					)
				}
			}

		}
	}

	data class KonklusjonTilbakekrevingDoedsbo(
		val sakType: Expression<SakType>,
		val tilbakekreving: Expression<TilbakekrevingDTO>,
		val brukerNavn: Expression<String>
	): OutlinePhrase<LangBokmalNynorskEnglish>() {
		override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
			val feilutbetaling = tilbakekreving.summer.feilutbetaling
			val nettoTilbakekreving = tilbakekreving.summer.nettoTilbakekreving
			val renteTillegg = tilbakekreving.summer.renteTillegg
			val sumTilbakekreving = tilbakekreving.summer.sumNettoRenter
			val fraOgMed = tilbakekreving.fraOgMed
			val tilOgMed = tilbakekreving.tilOgMed
			val harFradragSkatt = tilbakekreving.summer.fradragSkatt.greaterThan(0)

			paragraph {
				textExpr(
					Bokmal to "Det er utbetalt for mye ".expr() + sakType.format() + " i " +
							brukerNavn + "s navn fra " + fraOgMed.format() + " til " + tilOgMed.format() +
							". Det feilutbetalte beløpet er " + feilutbetaling.format() + " kroner"+
							ifElse(harFradragSkatt, " inkludert skatt.", "."),
					Nynorsk to "Det er utbetalt for mykje ".expr() + sakType.format() + " for " +
							brukerNavn + " frå " + fraOgMed.format() + " til " + tilOgMed.format() +
							". Det feilutbetalte beløpet er " + feilutbetaling.format() + " kroner" +
							ifElse(harFradragSkatt, " inkludert skatt.", "."),
					English to "The ".expr() + sakType.format() + " amount paid on behalf of " + brukerNavn +
							" was too high from " + fraOgMed.format() + " to " + tilOgMed.format() +
							". The incorrect amount totals NOK " + feilutbetaling.format() +
							ifElse(harFradragSkatt, " including tax.", "."),
				)
			}

			paragraph {
				textExpr(
					Bokmal to "Vi har kommet frem til at dødsboet skal betale tilbake ".expr() +
							ifElse(tilbakekreving.helTilbakekreving, "hele", "deler av") + " beløpet.",
					Nynorsk to "Vi har kome fram til at dødsbuet skal betale tilbake ".expr() +
							ifElse(tilbakekreving.helTilbakekreving, "heile", "delar av") + " beløpet.",
					English to "Reimbursement for ".expr() +
							ifElse(tilbakekreving.helTilbakekreving, "all", "part") +
							" of this amount must be paid.",
				)
				showIf(renteTillegg.greaterThan(0)) {
					textExpr(
						Bokmal to " Det blir ".expr() + nettoTilbakekreving.format() + " kroner" +
								ifElse(harFradragSkatt," etter at skatten er trukket fra.", ".") +
								" Boet må også betale " + renteTillegg.format() +
								" kroner i renter. Til sammen skal boet betale " + sumTilbakekreving.format() + " kroner.",
						Nynorsk to " Dette utgjer ".expr() + nettoTilbakekreving.format() + " kroner" +
								ifElse(harFradragSkatt, " etter at skatten er trekt frå.", ".") +
								" I tillegg må buet betale " + renteTillegg.format() +
								" kroner i renter. Til saman skal buet betale " + sumTilbakekreving.format() + " kroner.",
						English to " This is NOK ".expr() + nettoTilbakekreving.format() +
								ifElse(harFradragSkatt, " after tax.", ".") +
								" The estate must also pay NOK " + renteTillegg.format() + " in interest. In total," +
								" the estate is liable for NOK " + sumTilbakekreving.format() + "."
					)
				}.orShow {
					textExpr(
						Bokmal to " Det blir ".expr() + sumTilbakekreving.format() + " kroner" +
								ifElse(harFradragSkatt, " etter at skatten er trukket fra.", "."),
						Nynorsk to " Dette utgjer ".expr() + sumTilbakekreving.format() + " kroner" +
								ifElse(harFradragSkatt, " etter at skatten er trekt frå.", "."),
						English to " This is NOK ".expr() + sumTilbakekreving.format() +
								ifElse(harFradragSkatt, " after tax.", ".")
					)
				}
			}

		}
	}

	object TrukketSkatt: OutlinePhrase<LangBokmalNynorskEnglish>() {
		override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
			paragraph {
				text(
					Bokmal to "Beløpet som er trukket i skatt får vi tilbake fra Skatteetaten.",
					Nynorsk to "Beløpet som er trekt i skatt, får vi tilbake frå Skatteetaten.",
					English to "The tax withholding will be reimbursed by the Tax Administration.",
				)
			}
		}
	}

	data class VedtakGjortEtterLover(
		val tilbakekreving: Expression<TilbakekrevingDTO>
	): OutlinePhrase<LangBokmalNynorskEnglish>() {
		override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
			paragraph {
				textExpr(
					Bokmal to "Vedtaket er gjort etter folketrygdloven ".expr() +
							ifElse(tilbakekreving.summer.renteTillegg.greaterThan(0), "§§ 22-15 og og 22-17 a.", "§ 22-15."),
					Nynorsk to "Vedtaket er gjort etter folketrygdlova ".expr() +
							ifElse(tilbakekreving.summer.renteTillegg.greaterThan(0), "§§ 22-15 og og 22-17 a.", "§ 22-15."),
					English to "This decision is made in accordance with ".expr() +
							ifElse(tilbakekreving.summer.renteTillegg.greaterThan(0), "Sections 22-15 and 22-17a", "Section 22-15") +
							" of the National Insurance Act.",
				)
			}
		}
	}

	object ReferanseTilVedlegg: OutlinePhrase<LangBokmalNynorskEnglish>() {
		override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
			paragraph {
				text(
					Bokmal to "I vedleggene til dette brevet finner du en oversikt over periodene med " +
							"feilutbetalinger og beløpet du må betale tilbake, og en oversikt over rettighetene dine.",
					Nynorsk to "I vedlegga til dette brevet finn du ei oversikt over periodane med " +
							"feilutbetalingar, beløpet du må betale tilbake, og informasjon om klage og anke.",
					English to "Please see the attachments to this letter for an overview of periods with " +
							"payment errors and reimbursement amounts, as well as information about " +
							"complaints and appeals."
				)
			}
		}
	}

	object ReferanseTilVedleggDoedsbo: OutlinePhrase<LangBokmalNynorskEnglish>() {
		override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
			paragraph {
				text(
					Bokmal to "I vedlegget til dette brevet finnes en oversikt over periodene med " +
							"feilutbetalinger og beløpet du må betale tilbake.",
					Nynorsk to "I vedlegget til dette brevet finnes ei oversikt over periodane med " +
							"feilutbetalingar og beløpet som skal betalast tilbake.",
					English to "Please see the attachment to this letter for an overview of periods, with " +
							"information about errors and reimbursement amounts."
				)
			}
		}
	}

	object Skatt: OutlinePhrase<LangBokmalNynorskEnglish>() {
		override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
			title2 {
				text(
					Bokmal to "Skatt og skatteoppgjør",
					Nynorsk to "Skatt og skatteoppgjer",
					English to "Tax and tax assessment",
				)
			}
			paragraph {
				text(
					Bokmal to "Vi gir opplysninger til Skatteetaten om dette vedtaket. " +
							"De fastsetter det endelige skattebeløpet, og gjør nødvendige korrigeringer i " +
							"skatteoppgjøret ditt.",
					Nynorsk to "Vi gir opplysningar til Skatteetaten om dette vedtaket. Skatteetaten slår " +
							"fast det endelege skattebeløpet, og gjer nødvendige korrigeringar i skatteoppgjeret ditt.",
					English to "We will notify the Tax Administration about this decision. They will assess " +
							"the final tax amount and make the necessary corrections in your tax settlement.",
					)
			}
		}
	}

	object SkattDoedsbo: OutlinePhrase<LangBokmalNynorskEnglish>() {
		override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
			title2 {
				text(
					Bokmal to "Skatt og skatteoppgjør",
					Nynorsk to "Skatt og skatteoppgjer",
					English to "Tax and tax assessment",
				)
			}
			paragraph {
				text(
					Bokmal to "Vi gir opplysninger til Skatteetaten om dette vedtaket. " +
							"De fastsetter det endelige skattebeløpet, og gjør nødvendige korrigeringer i " +
							"skatteoppgjøret til avdøde.",
					Nynorsk to "Vi gir opplysningar til Skatteetaten om dette vedtaket. Skatteetaten slår " +
							"fast det endelege skattebeløpet, og gjer nødvendige korrigeringar i skatteoppgjeret til " +
							"avdøde.",
					English to "We will notify the Tax Administration about this decision. They will assess " +
							"the final tax amount and make the necessary corrections in the deceased’s tax " +
							"settlement.",
					)
			}
		}
	}

	data class HovedInnholdIngenTilbakekreving(
		val sakType: Expression<SakType>,
		val tilbakekreving: Expression<TilbakekrevingDTO>,
		val doedsbo: Expression<Boolean>
	): OutlinePhrase<LangBokmalNynorskEnglish>() {
		override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
			val feilutbetaling = tilbakekreving.summer.feilutbetaling
			val fraOgMed = tilbakekreving.fraOgMed
			val tilOgMed = tilbakekreving.tilOgMed
			val harFradragSkatt = tilbakekreving.summer.fradragSkatt.greaterThan(0)

			paragraph {
				textExpr(
					Bokmal to "Du har fått utbetalt for mye ".expr() + sakType.format() +
							" fra " + fraOgMed.format() + " til " + tilOgMed.format() +
							". Dette er " + feilutbetaling.format() + " kroner" +
							ifElse(harFradragSkatt, " inkludert skatt.", "."),
					Nynorsk to "Du har fått utbetalt for mykje ".expr() + sakType.format() +
							" frå " + fraOgMed.format() + " til " + tilOgMed.format() +
							". Dette er " + feilutbetaling.format() + " kroner" +
							ifElse(harFradragSkatt, " inkludert skatt.", "."),
					English to "You received too much in ".expr() + sakType.format() +
							" from " + fraOgMed.format() + " to " + tilOgMed.format() +
							". This amounts to NOK " + feilutbetaling.format() +
							ifElse(harFradragSkatt, " including tax.", "."),
				)
			}

			paragraph {
				text(
					Bokmal to "Vi har kommet fram til at du ikke skal betale tilbake beløpet.",
					Nynorsk to "Vi har kome fram til at du ikkje skal betale tilbake beløpet.",
					English to "We have decided to not claim reimbursement of the amount.",
				)
			}

			paragraph {
				text(
					Bokmal to "Vedtaket er gjort etter folketrygdloven § 22-15.",
					Nynorsk to "Vedtaket er gjort etter folketrygdlova § 22-15.",
					English to "This decision is made in accordance with Section 22-15 of the " +
							"National Insurance Act.",
				)
			}

			showIf(doedsbo.not()) {
				paragraph {
					text(
						Bokmal to "I vedlegget til dette brevet finner du en oversikt over rettighetene dine.",
						Nynorsk to "I vedlegget til dette brevet finn du informasjon om klage og anke.",
						English to "Please see the attachment to this letter for information on how to " +
								"file a complaint or appeal.",
					)
				}
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
						column(columnSpan = 2) {
							text(
								Bokmal to "Beløp som skal kreves tilbake i hele feilutbetalingsperioden",
								Nynorsk to "Beløp som skal krevjast tilbake i heile feilutbetalingsperioden",
								English to "Reimbursement amount for entire error period",
							)
						}
						column(columnSpan = 0) {}
					}
				) {
					row {
						cell {
							text(
								Bokmal to "Brutto tilbakekreving",
								Nynorsk to "Brutto tilbakekrevjing",
								English to "Gross reimbursement"
							)
						}
						cell {
							includePhrase(Felles.KronerText(summer.bruttoTilbakekreving))
						}
					}
					row {
						cell {
							text(
								Bokmal to "- Fradrag skatt",
								Nynorsk to "- Frådrag skatt",
								English to "- Tax withholdings"
							)
						}
						cell {
							includePhrase(Felles.KronerText(summer.fradragSkatt))
						}
					}
					row {
						cell {
							text(
								Bokmal to "= Netto tilbakekreving",
								Nynorsk to "= Netto tilbakekrevjing",
								English to "= Net reimbursement",
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
								English to "+ Interest"
							)
						}
						cell {
							includePhrase(Felles.KronerText(summer.renteTillegg))
						}
					}
					row {
						cell {
							text(
								Bokmal to "= Sum tilbakekreving",
								Nynorsk to "= Sum tilbakekrevjing",
								English to "= Total reimbursement amount",
								FontType.BOLD
							)
						}
						cell {
							includePhrase(Felles.KronerText(summer.sumNettoRenter, FontType.BOLD))
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
						column {
							text(
								Bokmal to "Måned / år",
								Nynorsk to "Månad / år",
								English to "Month / year",
							)
						}
						column {
							text(
								Bokmal to "Feilutbetalt beløp",
								Nynorsk to "Feilutbetalt beløp",
								English to "Error amount",
							)
						}
						column {
							text(
								Bokmal to "Resultat",
								Nynorsk to "Resultat",
								English to "Outcome",
							)
						}
						column {
							text(
								Bokmal to "Brutto tilbakekreving",
								Nynorsk to "Brutto tilbakekrevjing",
								English to "Gross reimbursement",
							)
						}
						column {
							text(
								Bokmal to "Netto tilbakekreving",
								Nynorsk to "Netto tilbakekrevjing",
								English to "Net reimbursement",
							)
						}
					}
				) {
					forEach(perioder) { periode ->
						row {
							cell {
								textExpr(
									Bokmal to periode.maaned.formatMaanedAar(),
									Nynorsk to periode.maaned.formatMaanedAar(),
									English to periode.maaned.formatMaanedAar(),
								)
							}
							cell {
								includePhrase(Felles.KronerText(periode.beloeper.feilutbetaling))
							}
							cell {
								textExpr(
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