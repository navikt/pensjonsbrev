package no.nav.pensjon.brev.maler.fraser.vedlegg.maanedligPensjonFoerSkatt

import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDto.GjeldendeBeregnetPensjon
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.AlderspensjonGjeldendeSelectors.andelKap19
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.AlderspensjonGjeldendeSelectors.andelKap20
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.GjeldendeBeregnetPensjonSelectors.barnetilleggFB
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.GjeldendeBeregnetPensjonSelectors.barnetilleggSB
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.GjeldendeBeregnetPensjonSelectors.ektefelletillegg
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.GjeldendeBeregnetPensjonSelectors.familieTillegg
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.GjeldendeBeregnetPensjonSelectors.fasteUtgifter
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.GjeldendeBeregnetPensjonSelectors.garantipensjon
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.GjeldendeBeregnetPensjonSelectors.garantitillegg
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.GjeldendeBeregnetPensjonSelectors.gjenlevendetillegg
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.GjeldendeBeregnetPensjonSelectors.gjenlevendetilleggKap19
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.GjeldendeBeregnetPensjonSelectors.grunnpensjon
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.GjeldendeBeregnetPensjonSelectors.inntektspensjon
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.GjeldendeBeregnetPensjonSelectors.minstenivaIndividuell
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.GjeldendeBeregnetPensjonSelectors.minstenivaPensjonistPar
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.GjeldendeBeregnetPensjonSelectors.pensjonstillegg
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.GjeldendeBeregnetPensjonSelectors.saertillegg
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.GjeldendeBeregnetPensjonSelectors.skjermingstillegg
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.GjeldendeBeregnetPensjonSelectors.tilleggspensjon
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.GjeldendeBeregnetPensjonSelectors.totalPensjon
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.GjeldendeBeregnetPensjonSelectors.virkDatoFom
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.GjeldendeBeregnetPensjonSelectors.virkDatoTom
import no.nav.pensjon.brev.maler.fraser.common.KronerText
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import java.time.LocalDate

data class TabellMaanedligPensjonKap19(
    val beregnetPensjon: Expression<GjeldendeBeregnetPensjon>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {

        includePhrase(
            TabellOverskrift(
                virkFom = beregnetPensjon.virkDatoFom,
                virkTom = beregnetPensjon.virkDatoTom
            )
        )

        paragraph {
            table(header = {
                column(columnSpan = 4) {
                    text(Bokmal to "", Nynorsk to "", English to "")
                }
                column(alignment = RIGHT, columnSpan = 1) {
                    text(
                        Bokmal to "Pensjon per måned", Nynorsk to "Pensjon per månad", English to "Pension per month"
                    )
                }
            }) {
                ifNotNull(beregnetPensjon.grunnpensjon) {
                    row {
                        cell {
                            text(
                                Bokmal to "Grunnpensjon",
                                Nynorsk to "Grunnpensjon",
                                English to "Basic pension",
                            )
                        }
                        cell { includePhrase(KronerText(it)) }
                    }
                }

                ifNotNull(beregnetPensjon.tilleggspensjon) {
                    row {
                        cell {
                            text(
                                Bokmal to "Tilleggspensjon",
                                Nynorsk to "Tilleggspensjon",
                                English to "Supplementary pension",
                            )
                        }
                        cell { includePhrase(KronerText(it)) }
                    }
                }

                ifNotNull(beregnetPensjon.saertillegg) {
                    row {
                        cell {
                            text(
                                Bokmal to "Særtillegg",
                                Nynorsk to "Særtillegg",
                                English to "Special supplement",
                            )
                        }
                        cell { includePhrase(KronerText(it)) }
                    }
                }

                ifNotNull(beregnetPensjon.pensjonstillegg) {
                    row {
                        cell {
                            text(
                                Bokmal to "Pensjonstillegg",
                                Nynorsk to "Pensjonstillegg",
                                English to "Pension supplement",
                            )
                        }
                        cell { includePhrase(KronerText(it)) }
                    }
                }

                ifNotNull(beregnetPensjon.skjermingstillegg) {
                    row {
                        cell {
                            text(
                                Bokmal to "Skjermingstillegg til uføre",
                                Nynorsk to "Skjermingstillegg til uføre",
                                English to "Supplement for the disabled",
                            )
                        }
                        cell { includePhrase(KronerText(it)) }
                    }
                }

                ifNotNull(beregnetPensjon.gjenlevendetilleggKap19) {
                    row {
                        cell {
                            text(
                                Bokmal to "Gjenlevendetillegg",
                                Nynorsk to "Attlevandetillegg",
                                English to "Survivor's supplement",
                            )
                        }
                        cell { includePhrase(KronerText(it)) }
                    }
                }


                ifNotNull(beregnetPensjon.minstenivaPensjonistPar) {
                    row {
                        cell {
                            text(
                                Bokmal to "Minstenivåtillegg pensjonistpar",
                                Nynorsk to "Minstenivåtillegg pensjonistpar",
                                English to "Minimum supplement for pensioner couples",
                            )
                        }
                        cell { includePhrase(KronerText(it)) }
                    }
                }


                ifNotNull(beregnetPensjon.minstenivaIndividuell) {
                    row {
                        cell {
                            text(
                                Bokmal to "Minstenivåtillegg individuelt",
                                Nynorsk to "Minstenivåtillegg individuelt",
                                English to "Minimum pension supplement",
                            )
                        }
                        cell { includePhrase(KronerText(it)) }
                    }
                }

                ifNotNull(beregnetPensjon.fasteUtgifter) {
                    row {
                        cell {
                            text(
                                Bokmal to "Faste utgifter ved institusjonsopphold",
                                Nynorsk to "Faste utgifter ved institusjonsopphald",
                                English to "Fixed costs when institutionalised",
                            )
                        }
                        cell { includePhrase(KronerText(it)) }
                    }
                }

                ifNotNull(beregnetPensjon.familieTillegg) {
                    row {
                        cell {
                            text(
                                Bokmal to "Familietillegg ved institusjonsopphold",
                                Nynorsk to "Familietillegg ved institusjonsopphald",
                                English to "Family supplement when institutionalised",
                            )
                        }
                        cell { includePhrase(KronerText(it)) }
                    }
                }

                ifNotNull(beregnetPensjon.ektefelletillegg) {
                    row {
                        cell {
                            text(
                                Bokmal to "Ektefelletillegg",
                                Nynorsk to "Ektefelletillegg",
                                English to "Spouse supplement",
                            )
                        }
                        cell { includePhrase(KronerText(it)) }
                    }
                }

                ifNotNull(beregnetPensjon.barnetilleggSB) {
                    row {
                        cell {
                            text(
                                Bokmal to "Barnetillegg særkullsbarn",
                                Nynorsk to "Barnetillegg særkullsbarn",
                                English to "Supplement for child(ren) of former marriages/relationships",
                            )
                        }
                        cell { includePhrase(KronerText(it)) }
                    }
                }

                ifNotNull(beregnetPensjon.barnetilleggFB) {
                    row {
                        cell {
                            text(
                                Bokmal to "Barnetillegg fellesbarn",
                                Nynorsk to "Barnetillegg fellesbarn",
                                English to "Supplement for child(ren) of the marriages/relationship",
                            )
                        }
                        cell { includePhrase(KronerText(it)) }
                    }
                }

                row {
                    cell {
                        text(
                            Bokmal to "Sum pensjon før skatt",
                            Nynorsk to "Sum pensjon før skatt",
                            English to "Total pension before tax",
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
    OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            textExpr(
                Bokmal to "Din månedlige pensjon fra ".expr() + virkFom.format(),
                Nynorsk to "Din månadlege pensjon frå ".expr() + virkFom.format(),
                English to "Your monthly pension from ".expr() + virkFom.format(),
            )

            ifNotNull(virkTom) {
                textExpr(
                    Bokmal to " til ".expr() + it.format(),
                    Nynorsk to " til ".expr() + it.format(),
                    English to " to ".expr() + it.format(),
                )
            }
        }

    }

}

data class TabellMaanedligPensjonKap19og20(
    val beregnetPensjon: Expression<GjeldendeBeregnetPensjon>,
    val brukersFoedselsdato: Expression<LocalDate>,
    val alderspensjonGjeldende: Expression<MaanedligPensjonFoerSkattDto.AlderspensjonGjeldende>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        includePhrase(
            TabellOverskrift(
                virkFom = beregnetPensjon.virkDatoFom, virkTom = beregnetPensjon.virkDatoTom
            )
        )
        paragraph {

            val foedselsaar = brukersFoedselsdato.year.toYear().format()
            val andelKap19 = alderspensjonGjeldende.andelKap19.format()
            val andelKap20 = alderspensjonGjeldende.andelKap20.format()
            textExpr(
                Bokmal to "De som er født i perioden 1954–1962 får en kombinasjon av alderspensjon etter gamle og nye regler i folketrygdloven (kapittel 19 og 20). Fordi du er født i ".expr() +
                        foedselsaar + " får du beregnet "
                        + andelKap19 + "/10 av pensjonen etter gamle regler, og " +
                        andelKap20 + "/10 etter nye regler.",

                Nynorsk to "Dei som er fødde i perioden 1954–1962 får ein kombinasjon av alderspensjon etter gamle og nye reglar i folketrygdlova (kapittel 19 og 20). Fordi du er fødd i ".expr() +
                        foedselsaar + ", får du rekna ut "
                        + andelKap19 + "/10 av pensjonen etter gamle reglar, og " +
                        andelKap20 + "/10 etter nye reglar.",

                English to "Individuals born between 1954 and 1962 will receive a combination of retirement pension calculated on the basis of both old and new provisions in the National Insurance Act (Chapters 19 and 20). Because you are born in ".expr() +
                        foedselsaar + ", "
                        + andelKap19 + "/10 of your pension is calculated on the basis of the old provisions, and " +
                        andelKap20 + "/10 is calculated on the basis of new provisions.",
            )


            table(header = {
                column(columnSpan = 4) {
                    text(
                        Bokmal to "Dette får du utbetalt etter gamle regler (kapittel 19)",
                        Nynorsk to "Dette får du utbetalt etter gamle reglar (kapittel 19)",
                        English to "Your pension pursuant to the provisions in Chapter 19 of the National Insurance Act"
                    )
                }
                column(alignment = RIGHT, columnSpan = 1) {
                    text(
                        Bokmal to "Pensjon per måned", Nynorsk to "Pensjon per månad", English to "Pension per month"
                    )
                }
            }) {
                ifNotNull(beregnetPensjon.grunnpensjon) {
                    row {
                        cell {
                            text(
                                Bokmal to "Grunnpensjon",
                                Nynorsk to "Grunnpensjon",
                                English to "Basic pension",
                            )
                        }
                        cell { includePhrase(KronerText(it)) }
                    }
                }

                ifNotNull(beregnetPensjon.tilleggspensjon) {
                    row {
                        cell {
                            text(
                                Bokmal to "Tilleggspensjon",
                                Nynorsk to "Tilleggspensjon",
                                English to "Supplementary pension",
                            )
                        }
                        cell { includePhrase(KronerText(it)) }
                    }
                }

                ifNotNull(beregnetPensjon.pensjonstillegg) {
                    row {
                        cell {
                            text(
                                Bokmal to "Pensjonstillegg",
                                Nynorsk to "Pensjonstillegg",
                                English to "Pension supplement",
                            )
                        }
                        cell { includePhrase(KronerText(it)) }
                    }
                }

                ifNotNull(beregnetPensjon.skjermingstillegg) {
                    row {
                        cell {
                            text(
                                Bokmal to "Skjermingstillegg til uføre",
                                Nynorsk to "Skjermingstillegg til uføre",
                                English to "Supplement for the disabled",
                            )
                        }
                        cell { includePhrase(KronerText(it)) }
                    }
                }

                ifNotNull(beregnetPensjon.gjenlevendetilleggKap19) {
                    row {
                        cell {
                            text(
                                Bokmal to "Gjenlevendetillegg",
                                Nynorsk to "Attlevandetillegg",
                                English to "Survivor's supplement",
                            )
                        }
                        cell { includePhrase(KronerText(it)) }
                    }
                }

                ifNotNull(beregnetPensjon.fasteUtgifter) {
                    row {
                        cell {
                            text(
                                Bokmal to "Faste utgifter ved institusjonsopphold",
                                Nynorsk to "Faste utgifter ved institusjonsopphald",
                                English to "Fixed costs when institutionalised",
                            )
                        }
                        cell { includePhrase(KronerText(it)) }
                    }
                }

                ifNotNull(beregnetPensjon.familieTillegg) {
                    row {
                        cell {
                            text(
                                Bokmal to "Familietillegg ved institusjonsopphold",
                                Nynorsk to "Familietillegg ved institusjonsopphald",
                                English to "Family supplement when institutionalised",
                            )
                        }
                        cell { includePhrase(KronerText(it)) }
                    }
                }

                row {
                    cell {
                        text(
                            Bokmal to "Dette får du utbetalt etter nye regler (kapittel 20)",
                            Nynorsk to "Dette får du utbetalt etter nye reglar (kapittel 20)",
                            English to "Your pension pursuant to the provisions in Chapter 20 of the National Insurance Act",
                            FontType.BOLD
                        )
                    }

                    cell {
                        text(
                            Bokmal to "Pensjon per måned",
                            Nynorsk to "Pensjon per månad",
                            English to "Pension per month",
                            FontType.BOLD
                        )
                    }
                }

                ifNotNull(beregnetPensjon.inntektspensjon) {
                    row {
                        cell {
                            text(
                                Bokmal to "Inntektspensjon",
                                Nynorsk to "Inntektspensjon",
                                English to "Income-based pension",
                            )
                        }
                        cell { includePhrase(KronerText(it)) }
                    }
                }


                ifNotNull(beregnetPensjon.garantipensjon) {
                    row {
                        cell {
                            text(
                                Bokmal to "Garantipensjon",
                                Nynorsk to "Garantipensjon",
                                English to "Pension guarantee",
                            )
                        }
                        cell { includePhrase(KronerText(it)) }
                    }
                }

                ifNotNull(beregnetPensjon.garantitillegg) {
                    row {
                        cell {
                            text(
                                Bokmal to "Garantitillegg for opptjente rettigheter",
                                Nynorsk to "Garantitillegg for opptente rettar",
                                English to "Guarantee supplements for accumulated rights",
                            )
                        }
                        cell { includePhrase(KronerText(it)) }
                    }
                }

                ifNotNull(beregnetPensjon.gjenlevendetillegg) {
                    row {
                        cell {
                            text(
                                Bokmal to "Gjenlevendetillegg",
                                Nynorsk to "Attlevandetillegg",
                                English to "Survivor’s supplement",
                            )
                        }
                        cell { includePhrase(KronerText(it)) }
                    }
                }

                ifNotNull(
                    beregnetPensjon.minstenivaPensjonistPar,
                    beregnetPensjon.minstenivaIndividuell
                ) { minstenivaPensjonistPar, minstenivaIndividuell ->
                    row {
                        cell {
                            text(
                                Bokmal to "Dette får du som minstenivåtillegg",
                                Nynorsk to "Dette får du som minstenivåtillegg",
                                English to "Your minimum pension supplement",
                                FontType.BOLD
                            )
                        }

                        cell {
                            text(
                                Bokmal to "Pensjon per måned",
                                Nynorsk to "Pensjon per månad",
                                English to "Pension per month",
                                FontType.BOLD
                            )
                        }
                    }

                    row {
                        cell {
                            text(
                                Bokmal to "Minstenivåtillegg pensjonistpar",
                                Nynorsk to "Minstenivåtillegg pensjonistpar",
                                English to "Minimum supplement for pensioner couples",
                            )
                        }
                        cell { includePhrase(KronerText(minstenivaPensjonistPar)) }
                    }


                    row {
                        cell {
                            text(
                                Bokmal to "Minstenivåtillegg individuelt",
                                Nynorsk to "Minstenivåtillegg individuelt",
                                English to "Minimum pension supplement",
                            )
                        }
                        cell { includePhrase(KronerText(minstenivaIndividuell)) }
                    }

                }

                showIf(beregnetPensjon.ektefelletillegg.notNull() or beregnetPensjon.barnetilleggSB.notNull() or beregnetPensjon.barnetilleggFB.notNull()) {
                    row {
                        cell {
                            text(
                                Bokmal to "Dette får du som forsørgingstillegg",
                                Nynorsk to "Dette får du som forsørgingstillegg",
                                English to "Your dependant supplement",
                                FontType.BOLD
                            )
                        }

                        cell {
                            text(
                                Bokmal to "Pensjon per måned",
                                Nynorsk to "Pensjon per månad",
                                English to "Pension per month",
                                FontType.BOLD
                            )
                        }
                    }

                    ifNotNull(beregnetPensjon.ektefelletillegg) {
                        row {
                            cell {
                                text(
                                    Bokmal to "Ektefelletillegg",
                                    Nynorsk to "Ektefelletillegg",
                                    English to "Spouse supplement",
                                )
                            }
                            cell { includePhrase(KronerText(it)) }
                        }
                    }

                    ifNotNull(beregnetPensjon.barnetilleggSB) {
                        row {
                            cell {
                                text(
                                    Bokmal to "Barnetillegg særkullsbarn",
                                    Nynorsk to "Barnetillegg særkullsbarn",
                                    English to "Supplement for child(ren) of former marriages/relationships",
                                )
                            }
                            cell { includePhrase(KronerText(it)) }
                        }
                    }

                    ifNotNull(beregnetPensjon.barnetilleggFB) {
                        row {
                            cell {
                                text(
                                    Bokmal to "Barnetillegg fellesbarn",
                                    Nynorsk to "Barnetillegg fellesbarn",
                                    English to "Supplement for child(ren) of the marriages/relationship",
                                )
                            }
                            cell { includePhrase(KronerText(it)) }
                        }
                    }
                }

                row {
                    cell {
                        text(
                            Bokmal to "Sum pensjon før skatt",
                            Nynorsk to "Sum pensjon før skatt",
                            English to "Total pension before tax",
                            FontType.BOLD
                        )
                    }
                    cell { includePhrase(KronerText(beregnetPensjon.totalPensjon, FontType.BOLD)) }
                }
            }
        }
    }
}