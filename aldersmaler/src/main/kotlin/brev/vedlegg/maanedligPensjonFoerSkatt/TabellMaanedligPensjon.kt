package brev.vedlegg.maanedligPensjonFoerSkatt

import brev.felles.KronerText
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattAP2025DtoSelectors.AlderspensjonPerManedSelectors.garantipensjon
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattAP2025DtoSelectors.AlderspensjonPerManedSelectors.inntektspensjon
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattAP2025DtoSelectors.AlderspensjonPerManedSelectors.minstenivaIndividuell
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattAP2025DtoSelectors.AlderspensjonPerManedSelectors.totalPensjon
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattAP2025DtoSelectors.AlderspensjonPerManedSelectors.virkDatoFom
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattAP2025DtoSelectors.AlderspensjonPerManedSelectors.virkDatoTom
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattTabell
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattTabellSelectors.AlderspensjonPerManedSelectors.barnetilleggFB
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattTabellSelectors.AlderspensjonPerManedSelectors.barnetilleggFB_safe
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattTabellSelectors.AlderspensjonPerManedSelectors.barnetilleggSB
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattTabellSelectors.AlderspensjonPerManedSelectors.barnetilleggSB_safe
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattTabellSelectors.AlderspensjonPerManedSelectors.ektefelletillegg
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattTabellSelectors.AlderspensjonPerManedSelectors.ektefelletillegg_safe
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattTabellSelectors.AlderspensjonPerManedSelectors.familieTillegg
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattTabellSelectors.AlderspensjonPerManedSelectors.fasteUtgifter
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattTabellSelectors.AlderspensjonPerManedSelectors.garantipensjon
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattTabellSelectors.AlderspensjonPerManedSelectors.garantitillegg
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattTabellSelectors.AlderspensjonPerManedSelectors.gjenlevendetillegg
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattTabellSelectors.AlderspensjonPerManedSelectors.gjenlevendetilleggKap19
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattTabellSelectors.AlderspensjonPerManedSelectors.grunnpensjon
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattTabellSelectors.AlderspensjonPerManedSelectors.inntektspensjon
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattTabellSelectors.AlderspensjonPerManedSelectors.minstenivaIndividuell
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattTabellSelectors.AlderspensjonPerManedSelectors.minstenivaPensjonistPar
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattTabellSelectors.AlderspensjonPerManedSelectors.pensjonstillegg
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattTabellSelectors.AlderspensjonPerManedSelectors.saertillegg
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattTabellSelectors.AlderspensjonPerManedSelectors.skjermingstillegg
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattTabellSelectors.AlderspensjonPerManedSelectors.tilleggspensjon
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattTabellSelectors.AlderspensjonPerManedSelectors.totalPensjon
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattTabellSelectors.AlderspensjonPerManedSelectors.virkDatoFom
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattTabellSelectors.AlderspensjonPerManedSelectors.virkDatoTom
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

data class TabellMaanedligPensjonKap19(
    val beregnetPensjon: Expression<MaanedligPensjonFoerSkattTabell.AlderspensjonPerManed>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {

        title1 {
            includePhrase(
                TabellOverskrift(
                    virkFom = beregnetPensjon.virkDatoFom,
                    virkTom = beregnetPensjon.virkDatoTom
                )
            )
        }

        paragraph {
            table(header = {
                column(columnSpan = 3) {
                    text(bokmal { + "" }, nynorsk { + "" }, english { + "" })
                }
                column(alignment = RIGHT, columnSpan = 1) {
                    includePhrase(PensjonPerMaaned)
                }
            }) {
                ifNotNull(beregnetPensjon.grunnpensjon) {
                    row {
                        cell {
                            text(
                                bokmal { + "Grunnpensjon" },
                                nynorsk { + "Grunnpensjon" },
                                english { + "Basic pension" },
                            )
                        }
                        cell { includePhrase(KronerText(it)) }
                    }
                }

                ifNotNull(beregnetPensjon.tilleggspensjon) {
                    row {
                        cell {
                            text(
                                bokmal { + "Tilleggspensjon" },
                                nynorsk { + "Tilleggspensjon" },
                                english { + "Supplementary pension" },
                            )
                        }
                        cell { includePhrase(KronerText(it)) }
                    }
                }

                ifNotNull(beregnetPensjon.saertillegg) {
                    row {
                        cell {
                            text(
                                bokmal { + "Særtillegg" },
                                nynorsk { + "Særtillegg" },
                                english { + "Special supplement" },
                            )
                        }
                        cell { includePhrase(KronerText(it)) }
                    }
                }

                ifNotNull(beregnetPensjon.pensjonstillegg) {
                    row {
                        cell {
                            text(
                                bokmal { + "Pensjonstillegg" },
                                nynorsk { + "Pensjonstillegg" },
                                english { + "Pension supplement" },
                            )
                        }
                        cell { includePhrase(KronerText(it)) }
                    }
                }

                ifNotNull(beregnetPensjon.skjermingstillegg) {
                    row {
                        cell {
                            text(
                                bokmal { + "Skjermingstillegg til uføre" },
                                nynorsk { + "Skjermingstillegg til uføre" },
                                english { + "Supplement for the disabled" },
                            )
                        }
                        cell { includePhrase(KronerText(it)) }
                    }
                }

                ifNotNull(beregnetPensjon.gjenlevendetilleggKap19) {
                    row {
                        cell {
                            text(
                                bokmal { + "Gjenlevendetillegg" },
                                nynorsk { + "Attlevandetillegg" },
                                english { + "Survivor's supplement" },
                            )
                        }
                        cell { includePhrase(KronerText(it)) }
                    }
                }


                ifNotNull(beregnetPensjon.minstenivaPensjonistPar) {
                    row {
                        cell {
                            text(
                                bokmal { + "Minstenivåtillegg pensjonistpar" },
                                nynorsk { + "Minstenivåtillegg pensjonistpar" },
                                english { + "Minimum supplement for pensioner couples" },
                            )
                        }
                        cell { includePhrase(KronerText(it)) }
                    }
                }


                ifNotNull(beregnetPensjon.minstenivaIndividuell) {
                    row {
                        cell {
                            text(
                                bokmal { + "Minstenivåtillegg individuelt" },
                                nynorsk { + "Minstenivåtillegg individuelt" },
                                english { + "Minimum pension supplement" },
                            )
                        }
                        cell { includePhrase(KronerText(it)) }
                    }
                }

                ifNotNull(beregnetPensjon.fasteUtgifter) {
                    row {
                        cell {
                            text(
                                bokmal { + "Faste utgifter ved institusjonsopphold" },
                                nynorsk { + "Faste utgifter ved institusjonsopphald" },
                                english { + "Fixed costs when institutionalised" },
                            )
                        }
                        cell { includePhrase(KronerText(it)) }
                    }
                }

                ifNotNull(beregnetPensjon.familieTillegg) {
                    row {
                        cell {
                            text(
                                bokmal { + "Familietillegg ved institusjonsopphold" },
                                nynorsk { + "Familietillegg ved institusjonsopphald" },
                                english { + "Family supplement when institutionalised" },
                            )
                        }
                        cell { includePhrase(KronerText(it)) }
                    }
                }
                showIf(beregnetPensjon.virkDatoFom.year.lessThan(2025)) {
                    ifNotNull(beregnetPensjon.ektefelletillegg) {
                        row {
                            cell {
                                text(
                                    bokmal { + "Ektefelletillegg" },
                                    nynorsk { + "Ektefelletillegg" },
                                    english { + "Spouse supplement" },
                                )
                            }
                            cell { includePhrase(KronerText(it)) }
                        }
                    }

                    ifNotNull(beregnetPensjon.barnetilleggSB) {
                        showIf(it.greaterThan(0)) {
                            row {
                                cell {
                                    text(
                                        bokmal { + "Barnetillegg særkullsbarn" },
                                        nynorsk { + "Barnetillegg særkullsbarn" },
                                        english { + "Supplement for child(ren) of former marriages/relationships" },
                                    )
                                }
                                cell { includePhrase(KronerText(it)) }
                            }
                        }
                    }

                    ifNotNull(beregnetPensjon.barnetilleggFB) {
                        showIf(it.greaterThan(0)) {
                            row {
                                cell {
                                    text(
                                        bokmal { + "Barnetillegg fellesbarn" },
                                        nynorsk { + "Barnetillegg fellesbarn" },
                                        english { + "Supplement for child(ren) of the marriages/relationship" },
                                    )
                                }
                                cell { includePhrase(KronerText(it)) }
                            }
                        }
                    }
                }

                row {
                    cell {
                        text(
                            bokmal { + "Sum pensjon før skatt" },
                            nynorsk { + "Sum pensjon før skatt" },
                            english { + "Total pension before tax" },
                            FontType.BOLD
                        )
                    }
                    cell { includePhrase(KronerText(beregnetPensjon.totalPensjon, FontType.BOLD)) }
                }
            }
        }
    }
}

private data class TabellOverskrift(val virkFom: Expression<LocalDate>, val virkTom: Expression<LocalDate?>) :
    PlainTextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun PlainTextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        text(
            bokmal { + "Din månedlige pensjon fra " + virkFom.format() },
            nynorsk { + "Din månadlege pensjon frå " + virkFom.format() },
            english { + "Your monthly pension from " + virkFom.format() },
        )

        ifNotNull(virkTom) {
            text(
                bokmal { + " til " + it.format() },
                nynorsk { + " til " + it.format() },
                english { + " to " + it.format() },
            )
        }
    }
}

private object PensjonPerMaaned : PlainTextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun PlainTextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        text(
            bokmal { + "Pensjon per måned" }, nynorsk { + "Pensjon per månad" }, english { + "Pension per month" }
        )
    }

}

data class TabellMaanedligPensjonKap20(
    val alderspensjonPerManed: Expression<MaanedligPensjonFoerSkattAP2025Dto.AlderspensjonPerManed>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            table({
                column(columnSpan = 3) {
                    includePhrase(
                        TabellOverskrift(
                            virkFom = alderspensjonPerManed.virkDatoFom,
                            virkTom = alderspensjonPerManed.virkDatoTom
                        )
                    )
                }
                column(alignment = RIGHT, columnSpan = 1) { }
            }) {
                inntektspensjon(alderspensjonPerManed.inntektspensjon)
                garantipensjon(alderspensjonPerManed.garantipensjon)
                minstenivaaIndividuell(alderspensjonPerManed.minstenivaIndividuell)
                row {
                    cell {
                        text(
                            bokmal { + "Sum pensjon før skatt" },
                            nynorsk { + "Sum pensjon før skatt" },
                            english { + "Total pension before tax" },
                            FontType.BOLD
                        )

                    }
                    cell {
                        includePhrase(KronerText(alderspensjonPerManed.totalPensjon, FontType.BOLD))
                    }
                }
            }
        }
    }

}

data class TabellMaanedligPensjonFlerePerioderInnledning(
    val kravVirkFom: Expression<LocalDate>
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        //vedleggBelopFlerePerioderTittel_001
        title1 {
            text(
                bokmal { + "Oversikt over pensjonen fra " + kravVirkFom.format() },
                nynorsk { + "Oversikt over pensjonen frå " + kravVirkFom.format() },
                english { + "Pension specifications as of " + kravVirkFom.format() },
            )
        }

        // veldeggBelopFlerePerioder_001
        paragraph {
            text(
                bokmal { + "Hvis det har vært endringer i noen av opplysningene som ligger til grunn for beregningen eller pensjonen har vært regulert, kan dette føre til en endring i hvor mye du får utbetalt. Nedenfor følger en oversikt over de månedlige pensjonsbeløpene dine." },
                nynorsk { + "Dersom det har vore endringar i nokre av opplysningane som ligg til grunn for utrekninga eller pensjonen har vore regulert, kan det føre til ei endring i kor mykje du får utbetalt. Nedanfor fylgjer ei oversikt over dei månadlege pensjonsbeløpa dine." },
                english { + "If there have been changes affecting how your pension is calculated in the period or amendments in the National Insurance basic amount, your pension may be adjusted accordingly. Below is a list of your monthly pension payments." },
            )
        }
    }

}

data class TabellMaanedligPensjonKap19og20(
    val beregnetPensjon: Expression<MaanedligPensjonFoerSkattTabell.AlderspensjonPerManed>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            includePhrase(
                TabellOverskrift(virkFom = beregnetPensjon.virkDatoFom, virkTom = beregnetPensjon.virkDatoTom)
            )
        }
        paragraph {
            table(header = {
                column(columnSpan = 3) {
                    text(
                        bokmal { + "Dette får du utbetalt etter gamle regler (kapittel 19)" },
                        nynorsk { + "Dette får du utbetalt etter gamle reglar (kapittel 19)" },
                        english { + "Your pension pursuant to the provisions in Chapter 19 of the National Insurance Act" },
                    )
                }
                column(alignment = RIGHT, columnSpan = 1) {
                    includePhrase(PensjonPerMaaned)
                }
            }) {
                ifNotNull(beregnetPensjon.grunnpensjon) {
                    row {
                        cell {
                            text(
                                bokmal { + "Grunnpensjon" },
                                nynorsk { + "Grunnpensjon" },
                                english { + "Basic pension" },
                            )
                        }
                        cell { includePhrase(KronerText(it)) }
                    }
                }

                ifNotNull(beregnetPensjon.tilleggspensjon) {
                    row {
                        cell {
                            text(
                                bokmal { + "Tilleggspensjon" },
                                nynorsk { + "Tilleggspensjon" },
                                english { + "Supplementary pension" },
                            )
                        }
                        cell { includePhrase(KronerText(it)) }
                    }
                }

                ifNotNull(beregnetPensjon.pensjonstillegg) {
                    row {
                        cell {
                            text(
                                bokmal { + "Pensjonstillegg" },
                                nynorsk { + "Pensjonstillegg" },
                                english { + "Pension supplement" },
                            )
                        }
                        cell { includePhrase(KronerText(it)) }
                    }
                }

                ifNotNull(beregnetPensjon.skjermingstillegg) {
                    row {
                        cell {
                            text(
                                bokmal { + "Skjermingstillegg til uføre" },
                                nynorsk { + "Skjermingstillegg til uføre" },
                                english { + "Supplement for the disabled" },
                            )
                        }
                        cell { includePhrase(KronerText(it)) }
                    }
                }

                ifNotNull(beregnetPensjon.gjenlevendetilleggKap19) {
                    row {
                        cell {
                            text(
                                bokmal { + "Gjenlevendetillegg" },
                                nynorsk { + "Attlevandetillegg" },
                                english { + "Survivor's supplement" },
                            )
                        }
                        cell { includePhrase(KronerText(it)) }
                    }
                }

                ifNotNull(beregnetPensjon.fasteUtgifter) {
                    row {
                        cell {
                            text(
                                bokmal { + "Faste utgifter ved institusjonsopphold" },
                                nynorsk { + "Faste utgifter ved institusjonsopphald" },
                                english { + "Fixed costs when institutionalised" },
                            )
                        }
                        cell { includePhrase(KronerText(it)) }
                    }
                }

                ifNotNull(beregnetPensjon.familieTillegg) {
                    row {
                        cell {
                            text(
                                bokmal { + "Familietillegg ved institusjonsopphold" },
                                nynorsk { + "Familietillegg ved institusjonsopphald" },
                                english { + "Family supplement when institutionalised" },
                            )
                        }
                        cell { includePhrase(KronerText(it)) }
                    }
                }
            }

            table(header = {
                column(columnSpan = 3) {
                    text(
                        bokmal { + "Dette får du utbetalt etter nye regler (kapittel 20)" },
                        nynorsk { + "Dette får du utbetalt etter nye reglar (kapittel 20)" },
                        english { + "Your pension pursuant to the provisions in Chapter 20 of the National Insurance Act" },
                    )
                }
                column(alignment = RIGHT, columnSpan = 1) {
                    includePhrase(PensjonPerMaaned)
                }
            }) {

                inntektspensjon(beregnetPensjon.inntektspensjon)
                garantipensjon(beregnetPensjon.garantipensjon)

                ifNotNull(beregnetPensjon.garantitillegg) {
                    showIf(it.greaterThan(0)) {
                        row {
                            cell {
                                text(
                                    bokmal { + "Garantitillegg for opptjente rettigheter" },
                                    nynorsk { + "Garantitillegg for opptente rettar" },
                                    english { + "Guarantee supplements for accumulated rights" },
                                )
                            }
                            cell { includePhrase(KronerText(it)) }
                        }
                    }
                }

                ifNotNull(beregnetPensjon.gjenlevendetillegg) {
                    row {
                        cell {
                            text(
                                bokmal { + "Gjenlevendetillegg" },
                                nynorsk { + "Attlevandetillegg" },
                                english { + "Survivor’s supplement" },
                            )
                        }
                        cell { includePhrase(KronerText(it)) }
                    }
                }
            }

            showIf(beregnetPensjon.minstenivaPensjonistPar.notNull() or beregnetPensjon.minstenivaIndividuell.notNull()) {
                table(header = {
                    column(columnSpan = 3) {
                        text(
                            bokmal { + "Dette får du som minstenivåtillegg" },
                            nynorsk { + "Dette får du som minstenivåtillegg" },
                            english { + "Your minimum pension supplement" },
                        )
                    }
                    column(alignment = RIGHT, columnSpan = 1) {
                        includePhrase(PensjonPerMaaned)
                    }
                }) {
                    ifNotNull(beregnetPensjon.minstenivaPensjonistPar) {
                        row {
                            cell {
                                text(
                                    bokmal { + "Minstenivåtillegg pensjonistpar" },
                                    nynorsk { + "Minstenivåtillegg pensjonistpar" },
                                    english { + "Minimum supplement for pensioner couples" },
                                )
                            }
                            cell { includePhrase(KronerText(it)) }
                        }
                    }

                    minstenivaaIndividuell(beregnetPensjon.minstenivaIndividuell)
                }
            }
            showIf(
                (beregnetPensjon.ektefelletillegg_safe .ifNull(Kroner(0)).greaterThan(0)
                        or beregnetPensjon.barnetilleggSB_safe.ifNull(Kroner(0)).greaterThan(0)
                        or beregnetPensjon.barnetilleggFB_safe.ifNull(Kroner(0)).greaterThan(0))
                        and beregnetPensjon.virkDatoFom.year.lessThan(2025)
            ) {
                table(header = {
                    column(columnSpan = 3) {
                        text(
                            bokmal { + "Dette får du som forsørgingstillegg" },
                            nynorsk { + "Dette får du som forsørgingstillegg" },
                            english { + "Your dependant supplement" },
                        )
                    }
                    column(alignment = RIGHT, columnSpan = 1) {
                        includePhrase(PensjonPerMaaned)
                    }
                }) {
                    ifNotNull(beregnetPensjon.ektefelletillegg) {
                        showIf(it.greaterThan(0)) {
                            row {
                                cell {
                                    text(
                                        bokmal { + "Ektefelletillegg" },
                                        nynorsk { + "Ektefelletillegg" },
                                        english { + "Spouse supplement" },
                                    )
                                }
                                cell { includePhrase(KronerText(it)) }
                            }
                        }
                    }

                    ifNotNull(beregnetPensjon.barnetilleggSB) {
                        showIf(it.greaterThan(0)) {
                            row {
                                cell {
                                    text(
                                        bokmal { + "Barnetillegg særkullsbarn" },
                                        nynorsk { + "Barnetillegg særkullsbarn" },
                                        english { + "Supplement for child(ren) of former marriages/relationships" },
                                    )
                                }
                                cell { includePhrase(KronerText(it)) }
                            }
                        }
                    }

                    ifNotNull(beregnetPensjon.barnetilleggFB) {
                        showIf(it.greaterThan(0)) {
                            row {
                                cell {
                                    text(
                                        bokmal { + "Barnetillegg fellesbarn" },
                                        nynorsk { + "Barnetillegg fellesbarn" },
                                        english { + "Supplement for child(ren) of the marriages/relationship" },
                                    )
                                }
                                cell { includePhrase(KronerText(it)) }
                            }
                        }
                    }
                }
            }

            text(
                bokmal { + "Sum pensjon før skatt: " },
                nynorsk { + "Sum pensjon før skatt: " },
                english { + "Total pension before tax: " },
                FontType.BOLD
            )
            includePhrase(KronerText(beregnetPensjon.totalPensjon, FontType.BOLD))
        }
    }

}

private fun TableScope<LangBokmalNynorskEnglish, Unit>.inntektspensjon(inntektspensjon: Expression<Kroner?>) {
    ifNotNull(inntektspensjon) {
        row {
            cell {
                text(
                    bokmal { + "Inntektspensjon" },
                    nynorsk { + "Inntektspensjon" },
                    english { + "Income-based pension" },
                )
            }
            cell { includePhrase(KronerText(it)) }
        }
    }
}

private fun TableScope<LangBokmalNynorskEnglish, Unit>.garantipensjon(garantipensjon: Expression<Kroner?>) {
    ifNotNull(garantipensjon) {
        row {
            cell {
                text(
                    bokmal { + "Garantipensjon" },
                    nynorsk { + "Garantipensjon" },
                    english { + "Guaranteed pension" },
                )
            }
            cell { includePhrase(KronerText(it)) }
        }
    }
}

private fun TableScope<LangBokmalNynorskEnglish, Unit>.minstenivaaIndividuell(minstenivaaIndividuell: Expression<Kroner?>) {
    ifNotNull(minstenivaaIndividuell) {
        row {
            cell {
                text(
                    bokmal { + "Minstenivåtillegg individuelt" },
                    nynorsk { + "Minstenivåtillegg individuelt" },
                    english { + "Minimum pension supplement" },
                )
            }
            cell { includePhrase(KronerText(it)) }
        }
    }
}
