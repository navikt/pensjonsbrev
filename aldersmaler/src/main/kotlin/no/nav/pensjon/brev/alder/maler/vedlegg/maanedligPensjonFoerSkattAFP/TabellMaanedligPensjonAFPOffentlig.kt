package no.nav.pensjon.brev.alder.maler.vedlegg.maanedligPensjonFoerSkattAFP

import no.nav.pensjon.brev.alder.maler.felles.KronerText
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAFPOffentligDto
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAFPOffentligDtoSelectors.AFPStatBeregningListeSelectors.afpStatBeregningListe
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAFPOffentligDtoSelectors.AFPStatBeregningSelectors.afpTillegg
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAFPOffentligDtoSelectors.AFPStatBeregningSelectors.afpTilleggForAvkort
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAFPOffentligDtoSelectors.AFPStatBeregningSelectors.familietillegg
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAFPOffentligDtoSelectors.AFPStatBeregningSelectors.familietilleggForAvkort
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAFPOffentligDtoSelectors.AFPStatBeregningSelectors.fasteUtgifterVedInstiitusjonsopphold
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAFPOffentligDtoSelectors.AFPStatBeregningSelectors.fasteUtgifterVedInstiitusjonsoppholdForAvkort
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAFPOffentligDtoSelectors.AFPStatBeregningSelectors.fremtidigInntekt
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAFPOffentligDtoSelectors.AFPStatBeregningSelectors.grunnpensjon
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAFPOffentligDtoSelectors.AFPStatBeregningSelectors.grunnpensjonForAvkort
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAFPOffentligDtoSelectors.AFPStatBeregningSelectors.minstenivaIndividuell
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAFPOffentligDtoSelectors.AFPStatBeregningSelectors.minstenivaIndividuellForAvkort
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAFPOffentligDtoSelectors.AFPStatBeregningSelectors.sartillegg
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAFPOffentligDtoSelectors.AFPStatBeregningSelectors.sartilleggForAvkort
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAFPOffentligDtoSelectors.AFPStatBeregningSelectors.tilleggspensjon
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAFPOffentligDtoSelectors.AFPStatBeregningSelectors.tilleggspensjonForAvkort
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAFPOffentligDtoSelectors.AFPStatBeregningSelectors.totalPensjon
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAFPOffentligDtoSelectors.AFPStatBeregningSelectors.totalPensjonForAvkort
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAFPOffentligDtoSelectors.AFPStatBeregningSelectors.virkDatoFom
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAFPOffentligDtoSelectors.AFPStatBeregningSelectors.virkDatoTom
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.PlainTextOnlyPhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.PlainTextOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.expression.notEqualTo
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

data class TabellMaanedligPensjonAFPOffentlig(
    val beregnetPensjonPerManedGjeldende: Expression<MaanedligPensjonFoerSkattAFPOffentligDto.AFPStatBeregning>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            includePhrase(
                TabellOverskriftOffentlig(
                    datoFom = beregnetPensjonPerManedGjeldende.virkDatoFom,
                    datoTil = beregnetPensjonPerManedGjeldende.virkDatoTom,
                ),
            )
        }

        includePhrase(TabellUnderskriftOffentlig(beregnetPensjonPerManedGjeldende))

        showIf(
            beregnetPensjonPerManedGjeldende.totalPensjon.notEqualTo(
                beregnetPensjonPerManedGjeldende.totalPensjonForAvkort,
            ),
        ) {
            includePhrase(TabellInnholdOffentligMedAvkort(beregnetPensjonPerManedGjeldende))
        }.orShow {
            includePhrase(TabellInnholdOffentligUtenAvkort(beregnetPensjonPerManedGjeldende))
        }
    }
}

data class TabellDinMaanedligAFPOffentlig(
    val afpStatBeregning: Expression<MaanedligPensjonFoerSkattAFPOffentligDto.AFPStatBeregningListe>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        forEach(afpStatBeregning.afpStatBeregningListe) { beregning ->

            title2 {
                includePhrase(
                    TabellDetaljOverskriftOffentlig(
                        datoFom = beregning.virkDatoFom,
                        datoTil = beregning.virkDatoTom,
                    ),
                )
            }

            includePhrase(TabellUnderskriftOffentlig(beregning))

            showIf(beregning.totalPensjon.notEqualTo(beregning.totalPensjonForAvkort)) {
                includePhrase(TabellInnholdOffentligMedAvkort(beregning))
            }.orShow {
                includePhrase(TabellInnholdOffentligUtenAvkort(beregning))
            }
        }
    }
}

private data class TabellUnderskriftOffentlig(
    val beregning: Expression<MaanedligPensjonFoerSkattAFPOffentligDto.AFPStatBeregning>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        ifNotNull(beregning.fremtidigInntekt) { fremtidigInntekt ->
            paragraph {
                text(
                    bokmal { +"Forventet årlig inntekt benyttet i beregningen er " + fremtidigInntekt.format() + "." },
                    nynorsk { +"Forventa årleg inntekt nytta i utrekninga er " + fremtidigInntekt.format() + "." },
                    english { +"" },
                )
            }
        }
    }
}

private data class TabellOverskriftOffentlig(
    val datoFom: Expression<LocalDate>,
    val datoTil: Expression<LocalDate?>,
) : PlainTextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun PlainTextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        text(
            bokmal { +"Den månedlige AFP fra " + datoFom.format() },
            nynorsk { +"Den månadlege AFP frå " + datoFom.format() },
            english { +"Your monthly AFP from " + datoFom.format() },
        )

        ifNotNull(datoTil) {
            text(
                bokmal { +" til " + it.format() },
                nynorsk { +" til " + it.format() },
                english { +" to " + it.format() },
            )
        }
    }
}

private data class TabellDetaljOverskriftOffentlig(
    val datoFom: Expression<LocalDate>,
    val datoTil: Expression<LocalDate?>,
) : PlainTextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun PlainTextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        text(
            bokmal { +"Den månedlige AFP fra " + datoFom.format() },
            nynorsk { +"Den månadlege AFP frå " + datoFom.format() },
            english { +"Your monthly AFP from " + datoFom.format() },
        )

        ifNotNull(datoTil) {
            text(
                bokmal { +" til " + it.format() },
                nynorsk { +" til " + it.format() },
                english { +" to " + it.format() },
            )
        }
    }
}

data class TabellInnholdOffentligUtenAvkort(
    val afpStatBeregning: Expression<MaanedligPensjonFoerSkattAFPOffentligDto.AFPStatBeregning>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            table(header = {
                column {
                    text(bokmal { +"" }, nynorsk { +"" }, english { +"" })
                }
                column(alignment = RIGHT) {
                    text(
                        bokmal { +"Pensjon per måned" },
                        nynorsk { +"Pensjon per månad" },
                        english { +"Pension per month" },
                    )
                }
            }) {
                ifNotNull(afpStatBeregning.grunnpensjon) {
                    row {
                        cell {
                            text(
                                bokmal { +"Grunnpensjon" },
                                nynorsk { +"Grunnpensjon" },
                                english { +"Basic pension" },
                            )
                        }
                        cell { includePhrase(KronerText(it)) }
                    }
                }

                ifNotNull(afpStatBeregning.tilleggspensjon) {
                    row {
                        cell {
                            text(
                                bokmal { +"Tilleggspensjon" },
                                nynorsk { +"Tilleggspensjo" },
                                english { +"Supplementary pension" },
                            )
                        }
                        cell { includePhrase(KronerText(it)) }
                    }
                }

                ifNotNull(afpStatBeregning.sartillegg) {
                    row {
                        cell {
                            text(
                                bokmal { +"Særtillegg" },
                                nynorsk { +"Særtillegg" },
                                english { +"Special supplement" },
                            )
                        }
                        cell { includePhrase(KronerText(it)) }
                    }
                }

                ifNotNull(afpStatBeregning.afpTillegg) {
                    row {
                        cell {
                            text(
                                bokmal { +"AFP-tillegg" },
                                nynorsk { +"AFP-tillegg" },
                                english { +"" },
                            )
                        }
                        cell { includePhrase(KronerText(it)) }
                    }
                }

                ifNotNull(afpStatBeregning.fasteUtgifterVedInstiitusjonsopphold) {
                    row {
                        cell {
                            text(
                                bokmal { +"Faste utgifter ved institusjonsopphold" },
                                nynorsk { +"Faste utgifter ved institusjonsopphald" },
                                english { +"Fixed costs when institutionalised" },
                            )
                        }
                        cell { includePhrase(KronerText(it)) }
                    }
                }

                ifNotNull(afpStatBeregning.familietillegg) {
                    row {
                        cell {
                            text(
                                bokmal { +"Familietillegg ved institusjonsopphold" },
                                nynorsk { +"Familietillegg ved institusjonsopphald" },
                                english { +"Family supplement when institutionalised" },
                            )
                        }
                        cell { includePhrase(KronerText(it)) }
                    }
                }

                ifNotNull(afpStatBeregning.familietillegg) {
                    row {
                        cell {
                            text(
                                bokmal { +"Minstenivåtillegg individuelt" },
                                nynorsk { +"Minstenivåtillegg individuelt" },
                                english { +"Minimum pension supplement" },
                            )
                        }
                        cell { includePhrase(KronerText(it)) }
                    }
                }

                row {
                    cell {
                        text(
                            bokmal { +"Sum pensjon før skatt" },
                            nynorsk { +"Sum pensjon før skatt" },
                            english { +"Total pension before tax" },
                            FontType.BOLD,
                        )
                    }
                    cell {
                        includePhrase(
                            KronerText(
                                afpStatBeregning.totalPensjon.ifNull(
                                    Kroner(0),
                                ),
                                FontType.BOLD,
                            ),
                        )
                    }
                }
            }
        }
    }
}

data class TabellInnholdOffentligMedAvkort(
    val afpStatBeregning: Expression<MaanedligPensjonFoerSkattAFPOffentligDto.AFPStatBeregning>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            table(header = {
                column {
                    text(bokmal { +"" }, nynorsk { +"" }, english { +"" })
                }
                column(alignment = RIGHT) {
                    text(
                        bokmal { +"Pensjon per måned før fradrag for inntekt" },
                        nynorsk { +"Pensjon per månad før frådrag for inntekt" },
                        english { +"Pension per month" },
                    )
                }
                column(alignment = RIGHT) {
                    text(
                        bokmal { +"Pensjon per måned etter fradrag for inntekt" },
                        nynorsk { +"Pensjon per månad etter frådrag for inntekt" },
                        english { +"Pension per month" },
                    )
                }
            }) {
                ifNotNull(afpStatBeregning.grunnpensjon) {
                    row {
                        cell {
                            text(
                                bokmal { +"Grunnpensjon" },
                                nynorsk { +"Grunnpensjon" },
                                english { +"Basic pension" },
                            )
                        }
                        cell { includePhrase(KronerText(it)) }
                        cell {
                            includePhrase(
                                KronerText(
                                    afpStatBeregning.grunnpensjonForAvkort.ifNull(
                                        Kroner(0),
                                    ),
                                ),
                            )
                        }
                    }
                }

                ifNotNull(afpStatBeregning.tilleggspensjon) {
                    row {
                        cell {
                            text(
                                bokmal { +"Tilleggspensjon" },
                                nynorsk { +"Tilleggspensjo" },
                                english { +"Supplementary pension" },
                            )
                        }
                        cell { includePhrase(KronerText(it)) }
                        cell {
                            includePhrase(
                                KronerText(
                                    afpStatBeregning.tilleggspensjonForAvkort.ifNull(
                                        Kroner(0),
                                    ),
                                ),
                            )
                        }
                    }
                }

                ifNotNull(afpStatBeregning.sartillegg) {
                    row {
                        cell {
                            text(
                                bokmal { +"Særtillegg" },
                                nynorsk { +"Særtillegg" },
                                english { +"Special supplement" },
                            )
                        }
                        cell { includePhrase(KronerText(it)) }
                        cell {
                            includePhrase(
                                KronerText(
                                    afpStatBeregning.sartilleggForAvkort.ifNull(
                                        Kroner(0),
                                    ),
                                ),
                            )
                        }
                    }
                }

                ifNotNull(afpStatBeregning.afpTillegg) {
                    row {
                        cell {
                            text(
                                bokmal { +"AFP-tillegg" },
                                nynorsk { +"AFP-tillegg" },
                                english { +"" },
                            )
                        }
                        cell { includePhrase(KronerText(it)) }
                        cell {
                            includePhrase(
                                KronerText(
                                    afpStatBeregning.afpTilleggForAvkort.ifNull(
                                        Kroner(0),
                                    ),
                                ),
                            )
                        }
                    }
                }

                ifNotNull(afpStatBeregning.fasteUtgifterVedInstiitusjonsopphold) {
                    row {
                        cell {
                            text(
                                bokmal { +"Faste utgifter ved institusjonsopphold" },
                                nynorsk { +"Faste utgifter ved institusjonsopphald" },
                                english { +"Fixed costs when institutionalised" },
                            )
                        }
                        cell { includePhrase(KronerText(it)) }
                        cell {
                            includePhrase(
                                KronerText(
                                    afpStatBeregning.fasteUtgifterVedInstiitusjonsoppholdForAvkort.ifNull(
                                        Kroner(0),
                                    ),
                                ),
                            )
                        }
                    }
                }

                ifNotNull(afpStatBeregning.familietillegg) {
                    row {
                        cell {
                            text(
                                bokmal { +"Familietillegg ved institusjonsopphold" },
                                nynorsk { +"Familietillegg ved institusjonsopphald" },
                                english { +"Family supplement when institutionalised" },
                            )
                        }
                        cell { includePhrase(KronerText(it)) }
                        cell {
                            includePhrase(
                                KronerText(
                                    afpStatBeregning.familietilleggForAvkort.ifNull(
                                        Kroner(0),
                                    ),
                                ),
                            )
                        }
                    }
                }

                ifNotNull(afpStatBeregning.minstenivaIndividuell) {
                    row {
                        cell {
                            text(
                                bokmal { +"Minstenivåtillegg individuelt" },
                                nynorsk { +"Minstenivåtillegg individuelt" },
                                english { +"Minimum pension supplement" },
                            )
                        }
                        cell { includePhrase(KronerText(it)) }
                        cell {
                            includePhrase(
                                KronerText(
                                    afpStatBeregning.minstenivaIndividuellForAvkort.ifNull(
                                        Kroner(0),
                                    ),
                                ),
                            )
                        }
                    }
                }

                row {
                    cell {
                        text(
                            bokmal { +"Sum pensjon før skatt" },
                            nynorsk { +"Sum pensjon før skatt" },
                            english { +"Total pension before tax" },
                            FontType.BOLD,
                        )
                    }
                    cell {
                        includePhrase(
                            KronerText(
                                afpStatBeregning.totalPensjon.ifNull(
                                    Kroner(0),
                                ),
                                FontType.BOLD,
                            ),
                        )
                    }
                    cell {
                        includePhrase(
                            KronerText(
                                afpStatBeregning.totalPensjonForAvkort.ifNull(
                                    Kroner(0),
                                ),
                                FontType.BOLD,
                            ),
                        )
                    }
                }
            }
        }
    }
}
