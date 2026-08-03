package no.nav.pensjon.brev.alder.maler.vedlegg

import no.nav.pensjon.brev.alder.maler.felles.KronerText
import no.nav.pensjon.brev.alder.model.vedlegg.OversiktOverPensjonenAfpDto
import no.nav.pensjon.brev.alder.model.vedlegg.OversiktOverPensjonenAfpDto.Endringsaarsaker
import no.nav.pensjon.brev.alder.model.vedlegg.OversiktOverPensjonenAfpDto.Periode
import no.nav.pensjon.brev.alder.model.vedlegg.OversiktOverPensjonenAfpDto.PeriodeBeregning
import no.nav.pensjon.brev.alder.model.vedlegg.OversiktOverPensjonenAfpDto.SistePeriode
import no.nav.pensjon.brev.alder.model.vedlegg.OversiktOverPensjonenAfpDto.YtelsesKomponent
import no.nav.pensjon.brev.alder.model.vedlegg.selectors.oversiktOverPensjonenAfpDto.endringsaarsaker.*
import no.nav.pensjon.brev.alder.model.vedlegg.selectors.oversiktOverPensjonenAfpDto.periodeBeregning.*
import no.nav.pensjon.brev.alder.model.vedlegg.selectors.oversiktOverPensjonenAfpDto.periode.beregning as periodeBeregning
import no.nav.pensjon.brev.alder.model.vedlegg.selectors.oversiktOverPensjonenAfpDto.periode.endringsaarsaker as periodeEndringsaarsaker
import no.nav.pensjon.brev.alder.model.vedlegg.selectors.oversiktOverPensjonenAfpDto.periode.framtidigArligInntekt as periodeFramtidigArligInntekt
import no.nav.pensjon.brev.alder.model.vedlegg.selectors.oversiktOverPensjonenAfpDto.periode.grunnbeloep as periodeGrunnbeloep
import no.nav.pensjon.brev.alder.model.vedlegg.selectors.oversiktOverPensjonenAfpDto.periode.virkDatoFom as periodeVirkDatoFom
import no.nav.pensjon.brev.alder.model.vedlegg.selectors.oversiktOverPensjonenAfpDto.periode.virkDatoTom as periodeVirkDatoTom
import no.nav.pensjon.brev.alder.model.vedlegg.selectors.oversiktOverPensjonenAfpDto.sistePeriode.beregning as sisteBeregning
import no.nav.pensjon.brev.alder.model.vedlegg.selectors.oversiktOverPensjonenAfpDto.sistePeriode.endringsaarsaker as sisteEndringsaarsaker
import no.nav.pensjon.brev.alder.model.vedlegg.selectors.oversiktOverPensjonenAfpDto.sistePeriode.framtidigArligInntekt as sisteFramtidigArligInntekt
import no.nav.pensjon.brev.alder.model.vedlegg.selectors.oversiktOverPensjonenAfpDto.sistePeriode.grunnbeloep as sisteGrunnbeloep
import no.nav.pensjon.brev.alder.model.vedlegg.selectors.oversiktOverPensjonenAfpDto.sistePeriode.virkDatoFom as sisteVirkDatoFom
import no.nav.pensjon.brev.alder.model.vedlegg.selectors.oversiktOverPensjonenAfpDto.ytelsesKomponent.brutto as komponentBrutto
import no.nav.pensjon.brev.alder.model.vedlegg.selectors.oversiktOverPensjonenAfpDto.ytelsesKomponent.netto as komponentNetto
import no.nav.pensjon.brev.alder.model.vedlegg.selectors.oversiktOverPensjonenAfpDto.*
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.TableScope
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.notEqualTo
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.text

// PE_AF_oversikt_over_pensjonen_MR71 — vises kun når antall beregningsperioder > 1.
@TemplateModelHelpers
val vedleggOversiktOverPensjonenAfp =
    createAttachment<LangBokmalNynorsk, OversiktOverPensjonenAfpDto>(
        title = {
            text(
                bokmal { +"Oversikt over pensjonens størrelse" },
                nynorsk { +"Oversikt over storleiken på pensjonen" },
            )
        },
        includeSakspart = false,
        outline = {
            paragraph {
                text(
                    bokmal {
                        +"Nedenfor følger en oversikt over de månedlige pensjonsbeløpene. Hvis det har vært " +
                            "endringer i grunnbeløpet eller i noen av opplysningene som ligger til grunn for " +
                            "beregningen i perioden, kan dette føre til en endring i størrelsen på pensjonen. " +
                            "Årsaken til endringen vil framgå i tabellen."
                    },
                    nynorsk {
                        +"Nedanfor følgjer ei oversikt over dei månadlege pensjonsbeløpa. Dersom det har vore " +
                            "endringar i grunnbeløpet eller i nokon av opplysningane som ligg til grunn for " +
                            "berekninga i perioden, kan dette føre til ei endring i storleiken på pensjonen. " +
                            "Årsaka til endringa går fram av tabellen."
                    },
                )
            }

            forEach(perioder) {
                includePhrase(OversiktForPeriode(it))
            }

            includePhrase(OversiktForSistePeriode(sistePeriode))
        },
    )

private data class OversiktForPeriode(
    val periode: Expression<Periode>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        title1 {
            text(
                bokmal {
                    +"Den månedlige pensjonen i perioden " +
                        periode.periodeVirkDatoFom.format() +
                        " - " +
                        periode.periodeVirkDatoTom.format()
                },
                nynorsk {
                    +"Den månadlege pensjonen i perioden " +
                        periode.periodeVirkDatoFom.format() +
                        " - " +
                        periode.periodeVirkDatoTom.format()
                },
            )
        }
        includePhrase(GrunnbeloepOgInntektParagraf(periode.periodeGrunnbeloep, periode.periodeFramtidigArligInntekt))
        includePhrase(EndringsaarsakerListe(periode.periodeEndringsaarsaker))
        includePhrase(KomponentTabell(periode.periodeBeregning))
    }
}

private data class OversiktForSistePeriode(
    val periode: Expression<SistePeriode>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        title1 {
            text(
                bokmal { +"Den månedlige pensjonen fra " + periode.sisteVirkDatoFom.format() },
                nynorsk { +"Den månadlege pensjonen frå " + periode.sisteVirkDatoFom.format() },
            )
        }
        includePhrase(GrunnbeloepOgInntektParagraf(periode.sisteGrunnbeloep, periode.sisteFramtidigArligInntekt))
        includePhrase(EndringsaarsakerListe(periode.sisteEndringsaarsaker))
        includePhrase(KomponentTabell(periode.sisteBeregning))
    }
}

private data class GrunnbeloepOgInntektParagraf(
    val grunnbeloep: Expression<no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner>,
    val framtidigArligInntekt: Expression<no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        paragraph {
            text(
                bokmal {
                    +"Folketrygdens grunnbeløp (G) benyttet i beregningen er " + grunnbeloep.format() + ". " +
                        "Framtidig årlig inntekt benyttet i beregningen er " + framtidigArligInntekt.format() + "."
                },
                nynorsk {
                    +"Grunnbeløpet (G) i folketrygda nytta i berekninga er " + grunnbeloep.format() + ". " +
                        "Forventa årleg inntekt nytta i berekninga er " + framtidigArligInntekt.format() + "."
                },
            )
        }
    }
}

private data class EndringsaarsakerListe(
    val arsaker: Expression<Endringsaarsaker>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        paragraph {
            text(
                bokmal { +"Pensjonen din er endret fordi" },
                nynorsk { +"Pensjonen din er endra fordi" },
            )
            list {
                // TODO: fritekst — må re-evalueres (endr_vedtakhistorikk: "Forklar kort hva som er endret i vedtak på bruker eller ektefelle/partner/samboer")
                // showIf(arsaker.endringIVedtak) { item { fritekst(...) } }

                // TODO: fritekst — må re-evalueres (endr_regel: "Forklar hvilke regler/satser som er endret")
                // showIf(arsaker.endringIRegelEllerSats) { item { fritekst(...) } }

                // TODO: fritekst — må re-evalueres (endr_personinfo: "Forklar kort hvilke familieforhold som er endret")
                // showIf(arsaker.endringIFamilieforhold) { item { fritekst(...) } }

                // TODO: fritekst — må re-evalueres (endr_inntekt: "Forklar kort hvilke inntekter som er endret")
                // showIf(arsaker.endringIInntekt) { item { fritekst(...) } }

                // TODO: fritekst — må re-evalueres (endr_inst_justering: "Forklar kort hvilke endringer mht institusjonsopphold som ligger til grunn")
                // showIf(arsaker.endringIInstitusjonsopphold) { item { fritekst(...) } }

                showIf(arsaker.endringIFasteUtgifterInstitusjon) {
                    item {
                        text(
                            bokmal { +"faste utgifter ved institusjonsopphold er endret" },
                            nynorsk { +"faste utgifter ved institusjonsopphald er endra" },
                        )
                    }
                }

                // TODO: fritekst — må re-evalueres (endr_aldersovergang: "Forklar kort hva slags aldersovergang som ligger til grunn for endringen")
                // showIf(arsaker.aldersovergang) { item { fritekst(...) } }

                showIf(arsaker.endringIUttaksgrad) {
                    item {
                        text(
                            bokmal { +"uttaksgraden er endret" },
                            nynorsk { +"uttaksgraden er endra" },
                        )
                    }
                }
                showIf(arsaker.endringIOpptjeningsgrunnlag) {
                    item {
                        text(
                            bokmal { +"opptjeningsgrunnlaget er endret" },
                            nynorsk { +"oppteningsgrunnlaget er endra" },
                        )
                    }
                }
            }
        }
    }
}

private data class KomponentTabell(
    val beregning: Expression<PeriodeBeregning>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        showIf(beregning.brutto.notEqualTo(beregning.netto)) {
            paragraph {
                table(
                    header = {
                        column { text(bokmal { +"" }, nynorsk { +"" }) }
                        column(alignment = RIGHT) {
                            text(
                                bokmal { +"Pensjon per måned før fradrag for inntekt" },
                                nynorsk { +"Pensjon per månad før frådrag for inntekt" },
                            )
                        }
                        column(alignment = RIGHT) {
                            text(
                                bokmal { +"Pensjon per måned etter fradrag for inntekt" },
                                nynorsk { +"Pensjon per månad etter frådrag for inntekt" },
                            )
                        }
                    },
                ) {
                    row {
                        cell { text(bokmal { +"Grunnpensjon" }, nynorsk { +"Grunnpensjon" }) }
                        cell { includePhrase(KronerText(beregning.grunnpensjon.komponentBrutto)) }
                        cell { includePhrase(KronerText(beregning.grunnpensjon.komponentNetto)) }
                    }
                    komponentRowMedFradrag(beregning.tilleggspensjon, "Tilleggspensjon", "Tilleggspensjon")
                    komponentRowMedFradrag(beregning.saertillegg, "Særtillegg", "Særtillegg")
                    komponentRowMedFradrag(
                        beregning.minstenivaatilleggIndividuelt,
                        "Minstenivåtillegg individuelt",
                        "Minstenivåtillegg individuelt",
                    )
                    komponentRowMedFradrag(beregning.afpTillegg, "AFP-tillegg", "AFP-tillegg")
                    komponentRowMedFradrag(beregning.ektefelletillegg, "Ektefelletillegg", "Ektefelletillegg")
                    komponentRowMedFradrag(
                        beregning.fasteUtgifterInstitusjon,
                        "Faste utgifter ved institusjonsopphold",
                        "Faste utgifter ved institusjonsopphald",
                    )
                    komponentRowMedFradrag(beregning.familietillegg, "Familietillegg", "Familietillegg")
                    row {
                        cell {
                            text(
                                bokmal { +"Sum pensjon før skatt" },
                                nynorsk { +"Sum pensjon før skatt" },
                            )
                        }
                        cell { includePhrase(KronerText(beregning.brutto)) }
                        cell { includePhrase(KronerText(beregning.netto)) }
                    }
                }
            }
        }.orShow {
            paragraph {
                table(
                    header = {
                        column { text(bokmal { +"" }, nynorsk { +"" }) }
                        column(alignment = RIGHT) {
                            text(
                                bokmal { +"Pensjon per måned" },
                                nynorsk { +"Pensjon per månad" },
                            )
                        }
                    },
                ) {
                    row {
                        cell { text(bokmal { +"Grunnpensjon" }, nynorsk { +"Grunnpensjon" }) }
                        cell { includePhrase(KronerText(beregning.grunnpensjon.komponentNetto)) }
                    }
                    komponentRowUtenFradrag(beregning.tilleggspensjon, "Tilleggspensjon", "Tilleggspensjon")
                    komponentRowUtenFradrag(beregning.saertillegg, "Særtillegg", "Særtillegg")
                    komponentRowUtenFradrag(
                        beregning.minstenivaatilleggIndividuelt,
                        "Minstenivåtillegg individuelt",
                        "Minstenivåtillegg individuelt",
                    )
                    komponentRowUtenFradrag(beregning.afpTillegg, "AFP-tillegg", "AFP-tillegg")
                    komponentRowUtenFradrag(beregning.ektefelletillegg, "Ektefelletillegg", "Ektefelletillegg")
                    komponentRowUtenFradrag(
                        beregning.fasteUtgifterInstitusjon,
                        "Faste utgifter ved institusjonsopphold",
                        "Faste utgifter ved institusjonsopphald",
                    )
                    komponentRowUtenFradrag(beregning.familietillegg, "Familietillegg", "Familietillegg")
                    row {
                        cell {
                            text(
                                bokmal { +"Sum pensjon før skatt" },
                                nynorsk { +"Sum pensjon før skatt" },
                            )
                        }
                        cell { includePhrase(KronerText(beregning.netto)) }
                    }
                }
            }
        }
    }
}

private fun TableScope<LangBokmalNynorsk, Unit>.komponentRowMedFradrag(
    komponent: Expression<YtelsesKomponent?>,
    bokmalLabel: String,
    nynorskLabel: String,
) {
    ifNotNull(komponent) { k ->
        row {
            cell { text(bokmal { +bokmalLabel }, nynorsk { +nynorskLabel }) }
            cell { includePhrase(KronerText(k.komponentBrutto)) }
            cell { includePhrase(KronerText(k.komponentNetto)) }
        }
    }
}

private fun TableScope<LangBokmalNynorsk, Unit>.komponentRowUtenFradrag(
    komponent: Expression<YtelsesKomponent?>,
    bokmalLabel: String,
    nynorskLabel: String,
) {
    ifNotNull(komponent) { k ->
        row {
            cell { text(bokmal { +bokmalLabel }, nynorsk { +nynorskLabel }) }
            cell { includePhrase(KronerText(k.komponentNetto)) }
        }
    }
}
