package no.nav.pensjon.etterlatte.maler.tilbakekreving

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.etterlatte.maler.formatMaanedAar
import no.nav.pensjon.etterlatte.maler.fraser.common.KronerText
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
					text(
						bokmal { +"Vi viser til forhåndsvarselet vårt som fulgte vedtak datert " +
								datoVarselEllerVedtak.format() },
						nynorsk { +"Vi viser til førehandsvarselet som følgde med vedtaket av " +
								datoVarselEllerVedtak.format() },
						english { +"We refer to the notice attached with the decision dated " +
								datoVarselEllerVedtak.format() },
					)
				}.orShowIf(varsel.equalTo(TilbakekrevingVarsel.EGET_BREV)) {
					text(
						bokmal { +"Vi viser til forhåndsvarselet vårt om at vi vurderer om du må betale tilbake " +
								sakType.format() + " av " + datoVarselEllerVedtak.format() },
						nynorsk { +"Vi viser til førehandsvarselet vårt om at vi vurderer om du må betale tilbake " +
								sakType.format() + " av " + datoVarselEllerVedtak.format() },
						english { +"We refer to the notice, where we consider claiming reimbursement of " +
								sakType.format() + " from " + datoVarselEllerVedtak.format() },
					)
				}.orShowIf(varsel.equalTo(TilbakekrevingVarsel.AAPENBART_UNOEDVENDIG)) {
					text(
						bokmal { +"Vi viser til vedtaket vårt av " + datoVarselEllerVedtak.format() },
						nynorsk { +"Vi viser til vedtaket vårt av " + datoVarselEllerVedtak.format() },
						english { +"We refer to the decision dated " + datoVarselEllerVedtak.format() },
					)
				}
				ifNotNull(datoTilsvarBruker) { datoTilsvarBruker ->
					text(
						bokmal { +", og dine kommentarer til dette som vi mottok den " + datoTilsvarBruker.format() },
						nynorsk { +", og kommentarane som vi fekk frå deg på dette " + datoTilsvarBruker.format() },
						english { +", and your response, which we received on " + datoTilsvarBruker.format() },
					)
				}
				text(
					bokmal { +"." },
					nynorsk { +"." },
					english { +"." },
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
				text(
					bokmal { +"Vi viser til forhåndsvarselet vårt av " + datoVarselEllerVedtak.format() },
					nynorsk { +"Vi viser til førehandsvarselet vårt av " + datoVarselEllerVedtak.format() },
					english { +"We refer to our notice of " + datoVarselEllerVedtak.format() },
				)
				ifNotNull(datoTilsvarBruker) { datoTilsvarBruker ->
					text(
						bokmal { +", og kommentarer til dette som vi mottok den " + datoTilsvarBruker.format() },
						nynorsk { +", og kommentarane som vi fekk på dette " + datoTilsvarBruker.format() },
						english { +", and your response, which we received on " + datoTilsvarBruker.format() },
					)
				}
				text(
					bokmal { +"." },
					nynorsk { +"." },
					english { +"." },
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
				text(
					bokmal { +"Du har fått utbetalt for mye " + sakType.format() +
							" fra " + fraOgMed.format() + " til " + tilOgMed.format() +
							". Det feilutbetalte beløpet er " + feilutbetaling.format() +
							ifElse(harFradragSkatt, " inkludert skatt.", ".") },
					nynorsk { +"Du har fått utbetalt for mykje " + sakType.format() +
							" frå " + fraOgMed.format() + " til " + tilOgMed.format() +
							". Det feilutbetalte beløpet er " + feilutbetaling.format() +
							ifElse(harFradragSkatt, " inkludert skatt.", ".") },
					english { +"You received too much in " + sakType.format() +
							" from " + fraOgMed.format() + " to " + tilOgMed.format() +
							". The incorrect amount totals " + feilutbetaling.format() +
							ifElse(harFradragSkatt, " including tax.", ".") },
				)
			}

			paragraph {
				text(
					bokmal { +"Vi har kommet frem til at du skal betale tilbake " +
							ifElse(tilbakekreving.helTilbakekreving, "hele", "deler av") + " beløpet." },
					nynorsk { +"Vi har kome fram til at du skal betale tilbake " +
							ifElse(tilbakekreving.helTilbakekreving, "heile", "delar av") + " beløpet." },
					english { +"Reimbursement for " +
							ifElse(tilbakekreving.helTilbakekreving, "all", "part") +
							" of this amount must be paid." },
				)
				showIf(renteTillegg.greaterThan(0)) {
					text(
						bokmal { +" Det blir " + nettoTilbakekreving.format() +
								ifElse(harFradragSkatt, " etter at skatten er trukket fra.", ".") +
								" Du må også betale " + renteTillegg.format() +
								" i renter. Til sammen skal du betale " + sumTilbakekreving.format() + "." },
						nynorsk { +" Dette utgjer " + nettoTilbakekreving.format() +
								ifElse(harFradragSkatt, " etter at skatten er trekt frå.", ".") +
								" I tillegg må du betale " + renteTillegg.format() +
								" i renter. Til saman skal du betale " + sumTilbakekreving.format() + "." },
						english { +" This is " + nettoTilbakekreving.format() +
								ifElse(harFradragSkatt, " after tax.", ".") +
								" You must also pay " + renteTillegg.format() + " in interest. In total," +
								" you must pay " + sumTilbakekreving.format() + "." }
					)
				}.orShow {
					text(
						bokmal { +" Det blir " + sumTilbakekreving.format() +
								ifElse(harFradragSkatt, " etter at skatten er trukket fra.", ".") },
						nynorsk { +" Dette utgjer " + sumTilbakekreving.format() +
								ifElse(harFradragSkatt, " etter at skatten er trekt frå.", ".") },
						english { +" This is " + sumTilbakekreving.format() +
								ifElse(harFradragSkatt, " after tax.", ".") }
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
				text(
					bokmal { +"Det er utbetalt for mye " + sakType.format() + " i " +
							brukerNavn + "s navn fra " + fraOgMed.format() + " til " + tilOgMed.format() +
							". Det feilutbetalte beløpet er " + feilutbetaling.format() +
							ifElse(harFradragSkatt, " inkludert skatt.", ".") },
					nynorsk { +"Det er utbetalt for mykje " + sakType.format() + " for " +
							brukerNavn + " frå " + fraOgMed.format() + " til " + tilOgMed.format() +
							". Det feilutbetalte beløpet er " + feilutbetaling.format() +
							ifElse(harFradragSkatt, " inkludert skatt.", ".") },
					english { +"The " + sakType.format() + " amount paid on behalf of " + brukerNavn +
							" was too high from " + fraOgMed.format() + " to " + tilOgMed.format() +
							". The incorrect amount totals " + feilutbetaling.format() +
							ifElse(harFradragSkatt, " including tax.", ".") },
				)
			}

			paragraph {
				text(
					bokmal { +"Vi har kommet frem til at dødsboet skal betale tilbake " +
							ifElse(tilbakekreving.helTilbakekreving, "hele", "deler av") + " beløpet." },
					nynorsk { +"Vi har kome fram til at dødsbuet skal betale tilbake " +
							ifElse(tilbakekreving.helTilbakekreving, "heile", "delar av") + " beløpet." },
					english { +"Reimbursement for " +
							ifElse(tilbakekreving.helTilbakekreving, "all", "part") +
							" of this amount must be paid." },
				)
				showIf(renteTillegg.greaterThan(0)) {
					text(
						bokmal { +" Det blir " + nettoTilbakekreving.format() +
								ifElse(harFradragSkatt," etter at skatten er trukket fra.", ".") +
								" Boet må også betale " + renteTillegg.format() +
								" i renter. Til sammen skal boet betale " + sumTilbakekreving.format() + "." },
						nynorsk { +" Dette utgjer " + nettoTilbakekreving.format() +
								ifElse(harFradragSkatt, " etter at skatten er trekt frå.", ".") +
								" I tillegg må buet betale " + renteTillegg.format() +
								" i renter. Til saman skal buet betale " + sumTilbakekreving.format() + "." },
						english { +" This is " + nettoTilbakekreving.format() +
								ifElse(harFradragSkatt, " after tax.", ".") +
								" The estate must also pay " + renteTillegg.format() + " in interest. In total," +
								" the estate is liable for " + sumTilbakekreving.format() + "." }
					)
				}.orShow {
					text(
						bokmal { +" Det blir " + sumTilbakekreving.format() +
								ifElse(harFradragSkatt, " etter at skatten er trukket fra.", ".") },
						nynorsk { +" Dette utgjer " + sumTilbakekreving.format() +
								ifElse(harFradragSkatt, " etter at skatten er trekt frå.", ".") },
						english { +" This is " + sumTilbakekreving.format() +
								ifElse(harFradragSkatt, " after tax.", ".") }
					)
				}
			}

		}
	}

	object TrukketSkatt: OutlinePhrase<LangBokmalNynorskEnglish>() {
		override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
			paragraph {
				text(
					bokmal { +"Beløpet som er trukket i skatt får vi tilbake fra Skatteetaten." },
					nynorsk { +"Beløpet som er trekt i skatt, får vi tilbake frå Skatteetaten." },
					english { +"The tax withholding will be reimbursed by the Tax Administration." },
				)
			}
		}
	}

	data class VedtakGjortEtterLover(
		val tilbakekreving: Expression<TilbakekrevingDTO>
	): OutlinePhrase<LangBokmalNynorskEnglish>() {
		override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
			paragraph {
				text(
					bokmal { +"Vedtaket er gjort etter folketrygdloven " +
							ifElse(tilbakekreving.summer.renteTillegg.greaterThan(0), "§§ 22-15 og og 22-17 a.", "§ 22-15.") },
					nynorsk { +"Vedtaket er gjort etter folketrygdlova " +
							ifElse(tilbakekreving.summer.renteTillegg.greaterThan(0), "§§ 22-15 og og 22-17 a.", "§ 22-15.") },
					english { +"This decision is made in accordance with " +
							ifElse(tilbakekreving.summer.renteTillegg.greaterThan(0), "Sections 22-15 and 22-17a", "Section 22-15") +
							" of the National Insurance Act." },
				)
			}
		}
	}

	object ReferanseTilVedlegg: OutlinePhrase<LangBokmalNynorskEnglish>() {
		override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
			paragraph {
				text(
					bokmal { +"I vedleggene til dette brevet finner du en oversikt over periodene med " +
							"feilutbetalinger og beløpet du må betale tilbake, og en oversikt over rettighetene dine." },
					nynorsk { +"I vedlegga til dette brevet finn du ei oversikt over periodane med " +
							"feilutbetalingar, beløpet du må betale tilbake, og informasjon om klage og anke." },
					english { +"Please see the attachments to this letter for an overview of periods with " +
							"payment errors and reimbursement amounts, as well as information about " +
							"complaints and appeals." }
				)
			}
		}
	}

	object ReferanseTilVedleggDoedsbo: OutlinePhrase<LangBokmalNynorskEnglish>() {
		override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
			paragraph {
				text(
					bokmal { +"I vedlegget til dette brevet finnes en oversikt over periodene med " +
							"feilutbetalinger og beløpet du må betale tilbake." },
					nynorsk { +"I vedlegget til dette brevet finnes ei oversikt over periodane med " +
							"feilutbetalingar og beløpet som skal betalast tilbake." },
					english { +"Please see the attachment to this letter for an overview of periods, with " +
							"information about errors and reimbursement amounts." }
				)
			}
		}
	}

	object Skatt: OutlinePhrase<LangBokmalNynorskEnglish>() {
		override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
			title2 {
				text(
					bokmal { +"Skatt og skatteoppgjør" },
					nynorsk { +"Skatt og skatteoppgjer" },
					english { +"Tax and tax assessment" },
				)
			}
			paragraph {
				text(
					bokmal { +"Vi gir opplysninger til Skatteetaten om dette vedtaket. " +
							"De fastsetter det endelige skattebeløpet, og gjør nødvendige korrigeringer i " +
							"skatteoppgjøret ditt." },
					nynorsk { +"Vi gir opplysningar til Skatteetaten om dette vedtaket. Skatteetaten slår " +
							"fast det endelege skattebeløpet, og gjer nødvendige korrigeringar i skatteoppgjeret ditt." },
					english { +"We will notify the Tax Administration about this decision. They will assess " +
							"the final tax amount and make the necessary corrections in your tax settlement." },
					)
			}
		}
	}

	object SkattDoedsbo: OutlinePhrase<LangBokmalNynorskEnglish>() {
		override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
			title2 {
				text(
					bokmal { +"Skatt og skatteoppgjør" },
					nynorsk { +"Skatt og skatteoppgjer" },
					english { +"Tax and tax assessment" },
				)
			}
			paragraph {
				text(
					bokmal { +"Vi gir opplysninger til Skatteetaten om dette vedtaket. " +
							"De fastsetter det endelige skattebeløpet, og gjør nødvendige korrigeringer i " +
							"skatteoppgjøret til avdøde." },
					nynorsk { +"Vi gir opplysningar til Skatteetaten om dette vedtaket. Skatteetaten slår " +
							"fast det endelege skattebeløpet, og gjer nødvendige korrigeringar i skatteoppgjeret til " +
							"avdøde." },
					english { +"We will notify the Tax Administration about this decision. They will assess " +
							"the final tax amount and make the necessary corrections in the deceased’s tax " +
							"settlement." },
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
				text(
					bokmal { +"Du har fått utbetalt for mye " + sakType.format() +
							" fra " + fraOgMed.format() + " til " + tilOgMed.format() +
							". Dette er " + feilutbetaling.format() +
							ifElse(harFradragSkatt, " inkludert skatt.", ".") },
					nynorsk { +"Du har fått utbetalt for mykje " + sakType.format() +
							" frå " + fraOgMed.format() + " til " + tilOgMed.format() +
							". Dette er " + feilutbetaling.format() +
							ifElse(harFradragSkatt, " inkludert skatt.", ".") },
					english { +"You received too much in " + sakType.format() +
							" from " + fraOgMed.format() + " to " + tilOgMed.format() +
							". This amounts to " + feilutbetaling.format() +
							ifElse(harFradragSkatt, " including tax.", ".") },
				)
			}

			paragraph {
				text(
					bokmal { +"Vi har kommet fram til at du ikke skal betale tilbake beløpet." },
					nynorsk { +"Vi har kome fram til at du ikkje skal betale tilbake beløpet." },
					english { +"We have decided to not claim reimbursement of the amount." },
				)
			}

			paragraph {
				text(
					bokmal { +"Vedtaket er gjort etter folketrygdloven § 22-15." },
					nynorsk { +"Vedtaket er gjort etter folketrygdlova § 22-15." },
					english { +"This decision is made in accordance with Section 22-15 of the " +
							"National Insurance Act." },
				)
			}

			showIf(doedsbo.not()) {
				paragraph {
					text(
						bokmal { +"I vedlegget til dette brevet finner du en oversikt over rettighetene dine." },
						nynorsk { +"I vedlegget til dette brevet finn du informasjon om klage og anke." },
						english { +"Please see the attachment to this letter for information on how to " +
								"file a complaint or appeal." },
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
								bokmal { +"Beløp som skal kreves tilbake i hele feilutbetalingsperioden" },
								nynorsk { +"Beløp som skal krevjast tilbake i heile feilutbetalingsperioden" },
								english { +"Reimbursement amount for entire error period" },
							)
						}
						column(columnSpan = 0) {}
					}
				) {
					row {
						cell {
							text(
								bokmal { +"Brutto tilbakekreving" },
								nynorsk { +"Brutto tilbakekrevjing" },
								english { +"Gross reimbursement" }
							)
						}
						cell {
							includePhrase(KronerText(summer.bruttoTilbakekreving))
						}
					}
					row {
						cell {
							text(
								bokmal { +"- Fradrag skatt" },
								nynorsk { +"- Frådrag skatt" },
								english { +"- Tax withholdings" }
							)
						}
						cell {
							includePhrase(KronerText(summer.fradragSkatt))
						}
					}
					row {
						cell {
							text(
								bokmal { +"= Netto tilbakekreving" },
								nynorsk { +"= Netto tilbakekrevjing" },
								english { +"= Net reimbursement" },
							)
						}
						cell {
							includePhrase(KronerText(summer.nettoTilbakekreving))
						}
					}
					row {
						cell {
							text(
								bokmal { +"+ Rentetillegg" },
								nynorsk { +"+ Rentetillegg" },
								english { +"+ Interest" }
							)
						}
						cell {
							includePhrase(KronerText(summer.renteTillegg))
						}
					}
					row {
						cell {
							text(
								bokmal { +"= Sum tilbakekreving" },
								nynorsk { +"= Sum tilbakekrevjing" },
								english { +"= Total reimbursement amount" },
								FontType.BOLD
							)
						}
						cell {
							includePhrase(KronerText(summer.sumNettoRenter, FontType.BOLD))
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
								bokmal { +"Måned / år" },
								nynorsk { +"Månad / år" },
								english { +"Month / year" },
							)
						}
						column {
							text(
								bokmal { +"Feilutbetalt beløp" },
								nynorsk { +"Feilutbetalt beløp" },
								english { +"Error amount" },
							)
						}
						column {
							text(
								bokmal { +"Resultat" },
								nynorsk { +"Resultat" },
								english { +"Outcome" },
							)
						}
						column {
							text(
								bokmal { +"Brutto tilbakekreving" },
								nynorsk { +"Brutto tilbakekrevjing" },
								english { +"Gross reimbursement" },
							)
						}
						column {
							text(
								bokmal { +"Netto tilbakekreving" },
								nynorsk { +"Netto tilbakekrevjing" },
								english { +"Net reimbursement" },
							)
						}
					}
				) {
					forEach(perioder) { periode ->
						row {
							cell {
								text(
									bokmal { +periode.maaned.formatMaanedAar() },
									nynorsk { +periode.maaned.formatMaanedAar() },
									english { +periode.maaned.formatMaanedAar() },
								)
							}
							cell {
								includePhrase(KronerText(periode.beloeper.feilutbetaling))
							}
							cell {
								text(
									bokmal { +periode.resultat.format() },
									nynorsk { +periode.resultat.format() },
									english { +periode.resultat.format() },
								)
							}
							cell {
								includePhrase(KronerText(periode.beloeper.bruttoTilbakekreving))
							}
							cell {
								includePhrase(KronerText(periode.beloeper.nettoTilbakekreving))
							}
						}
					}

				}
			}
		}
	}

}