package no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.api.model.KronerSelectors.value
import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.api.model.vedlegg.BarnetilleggGjeldendeSelectors.fellesbarn_safe
import no.nav.pensjon.brev.api.model.vedlegg.BarnetilleggGjeldendeSelectors.saerkullsbarn_safe
import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.avkortningsbeloepAar
import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.beloepAarBrutto
import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.beloepAarNetto
import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.beloepNetto
import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.erRedusertMotinntekt
import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.erRedusertMotinntekt_safe
import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.fribeloep
import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.fribeloepEllerInntektErPeriodisert
import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.harFlereBarn
import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.inntektBruktIAvkortning
import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.inntektOverFribeloep
import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.inntektstak
import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.justeringsbeloepAar
import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.justeringsbeloepAar_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDto
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.avkortningsbeloepAar
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.beloepAarBrutto
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.beloepAarNetto
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.beloepNetto
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.erRedusertMotinntekt
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.erRedusertMotinntekt_safe
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.fribeloep
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.fribeloepEllerInntektErPeriodisert
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.harFlereBarn
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.inntektBruktIAvkortning
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.inntektOverFribeloep
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.inntektstak
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.justeringsbeloepAar
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.justeringsbeloepAar_safe
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr

data class OpplysningerOmBarnetillegg(
    val barnetillegg: Expression<OpplysningerBruktIBeregningUTDto.BarnetilleggGjeldende>,
    val sivilstand: Expression<Sivilstand>,
    val anvendtTrygdetid: Expression<Int>,
    val harYrkesskade: Expression<Boolean>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        val harAnvendtTrygdetidUnder40 = anvendtTrygdetid.lessThan(40)
        val harTilleggFellesBarn = barnetillegg.notNull()
        val harTilleggSaerkullsbarn = barnetillegg.notNull()
        val barnetilleggFellesbarnErRedusertMotInntekt =
            barnetillegg.fellesbarn_safe.erRedusertMotinntekt_safe.ifNull(false)
        val barnetillegSaerkullsbarnErRedusertMotInntekt =
            barnetillegg.saerkullsbarn_safe.erRedusertMotinntekt_safe.ifNull(false)
        val erRedusertMotInntekt = barnetilleggFellesbarnErRedusertMotInntekt or
                barnetillegSaerkullsbarnErRedusertMotInntekt
        val harJusteringsbeloepFellesbarn =
            barnetillegg.fellesbarn_safe.justeringsbeloepAar_safe.ifNull(Kroner(0)).value.greaterThan(0)
        val harJusteringsbeloepSaerkullsbarn =
            barnetillegg.saerkullsbarn_safe.justeringsbeloepAar_safe.ifNull(Kroner(0)).value.greaterThan(0)
        val harJusteringsbeloep = harJusteringsbeloepFellesbarn or harJusteringsbeloepSaerkullsbarn

        includePhrase(SlikBeregnBTOverskrift)
        includePhrase(VedleggBeregnUTInnlednBT)

        showIf(harTilleggFellesBarn and not(harTilleggSaerkullsbarn)) {
            includePhrase(
                VedleggOpplysningerBruktIBeregningUTFraser.FastsetterStoerelsenPaaBTFellesbarn(
                    harAnvendtTrygdetidUnder40 = harAnvendtTrygdetidUnder40,
                    harYrkesskade = harYrkesskade,
                )
            )
        }

        showIf(harTilleggSaerkullsbarn and not(harTilleggFellesBarn)) {
            includePhrase(
                VedleggOpplysningerBruktIBeregningUTFraser.FastsetterStoerelsenPaaBTSaerkullsbarn(
                    harAnvendtTrygdetidUnder40
                )
            )
        }

        ifNotNull(barnetillegg.fellesbarn_safe, barnetillegg.saerkullsbarn_safe) { felles, saerkull ->
            includePhrase(
                VedleggOpplysningerBruktIBeregningUTFraser.FastsetterStoerelsenPaaBTFellesbarnOgSaerkullsbarn(
                    harAnvendtTrygdetidUnder40 = harAnvendtTrygdetidUnder40,
                    harTilleggForFlereFellesbarn = felles.harFlereBarn,
                    harTilleggForFlereSaerkullsbarn = saerkull.harFlereBarn,
                    sivilstand = sivilstand
                )
            )
        }

        showIf(erRedusertMotInntekt) {
            includePhrase(
                VedleggOpplysningerBruktIBeregningUTFraser.PeriodisertInntektInnledning(
                    harJusteringsbeloep = harJusteringsbeloep,
                    sivilstand = sivilstand
                )
            )

            ifNotNull(barnetillegg.fellesbarn_safe) { barnetilleggFellesBarn ->
                showIf(
                    barnetilleggFellesBarn.erRedusertMotinntekt and not(
                        barnetillegSaerkullsbarnErRedusertMotInntekt
                    )
                ) {
                    includePhrase(
                        PeriodisertInntektFellesbarnA(
                            barnetilleggFellesBarn.avkortningsbeloepAar,
                            barnetilleggFellesBarn.fribeloepEllerInntektErPeriodisert,
                            barnetilleggFellesBarn.justeringsbeloepAar,
                            sivilstand = sivilstand,
                        )
                    )
                }

            }
            ifNotNull(barnetillegg.saerkullsbarn_safe) { saerkullTillegg ->
                showIf(
                    barnetillegSaerkullsbarnErRedusertMotInntekt and not(
                        barnetilleggFellesbarnErRedusertMotInntekt
                    )
                ) {
                    includePhrase(
                        PeriodisertInntekSaerkullsbarnA(
                            saerkullTillegg.avkortningsbeloepAar,
                            saerkullTillegg.fribeloepEllerInntektErPeriodisert,
                            saerkullTillegg.justeringsbeloepAar,
                        )
                    )
                }
            }

            ifNotNull(barnetillegg.fellesbarn_safe) { fellesTillegg ->
                showIf(fellesTillegg.erRedusertMotinntekt) {
                    includePhrase(
                        PeriodisertInntektFellesbarnB(
                            avkortningsbeloepAar_barnetilleggFBGjeldende = fellesTillegg.avkortningsbeloepAar,
                            fribeloepEllerInntektErPeriodisert_barnetilleggFBGjeldende = fellesTillegg.fribeloepEllerInntektErPeriodisert,
                            harTilleggForFlereFellesbarn = fellesTillegg.harFlereBarn,
                            justeringsbeloepAar_barnetilleggFBGjeldende = fellesTillegg.justeringsbeloepAar
                        )
                    )
                    includePhrase(
                        PeriodisertInntektFellesbarnC(
                            justeringsbeloepAar_barnetilleggFBGjeldende = fellesTillegg.justeringsbeloepAar
                        )
                    )
                }

            }
            ifNotNull(barnetillegg.saerkullsbarn_safe) { saerkullTillegg ->
                showIf(barnetillegSaerkullsbarnErRedusertMotInntekt) {
                    includePhrase(
                        PeriodisertInntektSaerkullsbarnB(
                            avkortningsbeloepAar_barnetilleggSBGjeldende = saerkullTillegg.avkortningsbeloepAar,
                            fribeloepEllerInntektErPeriodisert_barnetilleggSBGjeldende = saerkullTillegg.fribeloepEllerInntektErPeriodisert,
                            harTilleggForFlereSaerkullsbarn = saerkullTillegg.harFlereBarn,
                            justeringsbeloepAar_barnetilleggSBGjeldende = saerkullTillegg.justeringsbeloepAar,
                            sivilstand = sivilstand,
                            erRedusertMotInntektSaerkullsbarn = barnetillegSaerkullsbarnErRedusertMotInntekt,
                        )
                    )
                    includePhrase(
                        PeriodisertInntektSaerkullsbarnC(
                            justeringsbeloepAar_barnetilleggSBGjeldende = saerkullTillegg.justeringsbeloepAar
                        )
                    )
                }
            }

            ifNotNull(barnetillegg.fellesbarn_safe) { fellesTillegg ->

                showIf(fellesTillegg.justeringsbeloepAar.greaterThan(0)) {
                    includePhrase(
                        VedleggOpplysningerBruktIBeregningUTFraser.VedleggBeregnUTJusterBelopOver0BTFB(
                            fellesTillegg.justeringsbeloepAar
                        )
                    )
                }

                showIf(fellesTillegg.justeringsbeloepAar.lessThan(0)) {
                    includePhrase(
                        VedleggOpplysningerBruktIBeregningUTFraser.VedleggBeregnUTJusterBelopUnder0BTFB(
                            fellesTillegg.justeringsbeloepAar
                        )
                    )
                }

                showIf(fellesTillegg.erRedusertMotinntekt) {
                    title1 {
                        text(
                            Language.Bokmal to "Reduksjon av barnetillegg for fellesbarn før skatt i årfor neste år",
                            Language.Nynorsk to "Reduksjon av barnetillegg for fellesbarn før skatt i årfor neste år",
                            Language.English to "Reduction of child supplement payment for joint children before tax"
                        )
                    }
                    includePhrase(
                        OpplysningerOmBarnetilleggTabell(
                            fellesTillegg.avkortningsbeloepAar,
                            fellesTillegg.beloepAarBrutto,
                            fellesTillegg.beloepAarNetto,
                            fellesTillegg.erRedusertMotinntekt,
                            fellesTillegg.fribeloep,
                            fellesTillegg.inntektBruktIAvkortning,
                            fellesTillegg.inntektOverFribeloep,
                            fellesTillegg.inntektstak,
                            fellesTillegg.justeringsbeloepAar,
                            fellesTillegg.beloepNetto,
                        )
                    )
                }

                showIf(fellesTillegg.beloepNetto.greaterThan(0)) {
                    includePhrase(
                        VedleggOpplysningerBruktIBeregningUTFraser.MaanedligTilleggFellesbarn(
                            beloep_barnetilleggFBGjeldende = fellesTillegg.beloepNetto,
                            harFlereBarn = fellesTillegg.harFlereBarn,
                        )
                    )
                }.orShow {
                    includePhrase(
                        VedleggOpplysningerBruktIBeregningUTFraser.FaaIkkeUtbetaltTilleggFellesbarn(
                            beloep_barnetilleggFBGjeldende = fellesTillegg.beloepNetto,
                            justeringsbeloepAar_barnetilleggFBGjeldende = fellesTillegg.justeringsbeloepAar,
                            harFlereBarn = fellesTillegg.harFlereBarn,
                        )
                    )
                }
            }

            ifNotNull(barnetillegg.saerkullsbarn_safe) { saerkullTillegg ->
                showIf(saerkullTillegg.erRedusertMotinntekt) {
                    title1 {
                        text(
                            Language.Bokmal to "Reduksjon av barnetillegg for særkullsbarn før skatt i årfor neste år",
                            Language.Nynorsk to "Reduksjon av barnetillegg for særkullsbarn før skatt i årfor neste år",
                            Language.English to "Reduction of child supplement payment for joint children before tax"
                        )
                    }
                    includePhrase(
                        OpplysningerOmBarnetilleggTabell(
                            saerkullTillegg.avkortningsbeloepAar,
                            saerkullTillegg.beloepAarBrutto,
                            saerkullTillegg.beloepAarNetto,
                            saerkullTillegg.erRedusertMotinntekt,
                            saerkullTillegg.fribeloep,
                            saerkullTillegg.inntektBruktIAvkortning,
                            saerkullTillegg.inntektOverFribeloep,
                            saerkullTillegg.inntektstak,
                            saerkullTillegg.justeringsbeloepAar,
                            saerkullTillegg.beloepNetto,
                        )
                    )
                }
                showIf(saerkullTillegg.beloepNetto.greaterThan(0)) {
                    includePhrase(
                        VedleggOpplysningerBruktIBeregningUTFraser.VedleggBeregnUTredusBTSBPgaInntekt(
                            saerkullTillegg.beloepNetto
                        )
                    )
                }.orShow {
                    showIf(saerkullTillegg.justeringsbeloepAar.equalTo(0)) {
                        includePhrase(VedleggOpplysningerBruktIBeregningUTFraser.VedleggBeregnUTIkkeUtbetaltBTSBPgaInntekt)
                    } orShow {
                        includePhrase(VedleggOpplysningerBruktIBeregningUTFraser.VedleggBeregnUTJusterBelopIkkeUtbetalt)
                    }
                }
            }
        }
    }

    data class OpplysningerOmBarnetilleggTabell(
        val avkortningsbeloepAar: Expression<Kroner>,
        val beloepAarBrutto: Expression<Kroner>,
        val beloepAarNetto: Expression<Kroner>,
        val erRedusertMotinntekt: Expression<Boolean>,
        val fribeloep: Expression<Kroner>,
        val inntektBruktIAvkortning: Expression<Kroner>,
        val inntektOverFribeloep: Expression<Kroner>,
        val inntektstak: Expression<Kroner>,
        val justeringsbeloepAar: Expression<Kroner>,
        val beloepNetto: Expression<Kroner>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            val faarUtbetaltTillegg = beloepNetto.greaterThan(0)
            val harJusteringsbeloep = justeringsbeloepAar.notEqualTo(0)
            val harNettoBeloep = beloepNetto.notEqualTo(0)
            table(
                header = {
                    column(columnSpan = 2) {
                        text(
                            Language.Bokmal to "Beskrivelse",
                            Language.Nynorsk to "Beskrivelse",
                            Language.English to "Description",
                            Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
                        )
                    }
                    column(alignment = Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT) {
                        text(
                            Language.Bokmal to "Beløp",
                            Language.Nynorsk to "Beløp",
                            Language.English to "Amount",
                            Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
                        )
                    }
                }
            ) {
                showIf(faarUtbetaltTillegg and harJusteringsbeloep) {
                    row {
                        cell {
                            text(
                                Language.Bokmal to "Årlig barnetillegg før reduksjon ut fra inntekt",
                                Language.Nynorsk to "Årleg barnetillegg før reduksjon ut frå inntekt",
                                Language.English to "Yearly child supplement before income reduction"
                            )
                        }
                        cell {
                            includePhrase(Felles.KronerText(beloepAarBrutto))
                        }
                    }
                }
                showIf(erRedusertMotinntekt) {
                    row {
                        cell {
                            text(
                                Language.Bokmal to "Samlet inntekt brukt i fastsettelse av barnetillegget er ",
                                Language.Nynorsk to "Samla inntekt brukt i fastsetjinga av barnetillegget er ",
                                Language.English to "Total income applied in calculation of reduction in child supplement is "
                            )
                        }
                        cell {
                            includePhrase(Felles.KronerText(inntektBruktIAvkortning))
                        }
                    }
                }
                showIf(faarUtbetaltTillegg and harJusteringsbeloep) {
                    row {
                        cell {
                            text(
                                Language.Bokmal to "Fribeløp brukt i fastsettelsen av barnetillegget er",
                                Language.Nynorsk to "Fribeløp brukt i fastsetjinga av barnetillegget er",
                                Language.English to "Exemption amount applied in calculation of reduction in child supplement is",
                            )
                        }
                        cell {
                            includePhrase(Felles.KronerText(fribeloep))
                        }
                    }
                }
                showIf(harNettoBeloep or (not(harNettoBeloep) and harJusteringsbeloep)) {
                    row {
                        cell {
                            text(
                                Language.Bokmal to "Inntekt over fribeløpet er",
                                Language.Nynorsk to "Inntekt over fribeløpet er",
                                Language.English to "Income exceeding the exemption amount is",
                            )
                        }
                        cell {
                            includePhrase(Felles.KronerText(inntektOverFribeloep))
                        }
                    }
                }
                showIf(
                    harNettoBeloep
                            and harJusteringsbeloep
                            and avkortningsbeloepAar.greaterThan(0)
                ) {
                    row {
                        cell {
                            text( // TODO finn en fornuftig måte å vise regnestykket på
                                Language.Bokmal to "- 50 prosent av inntekt som overstiger fribeløpet (oppgitt som et årlig beløep)",
                                Language.Nynorsk to "- 50 prosent av inntekta som overstig fribeløpet (oppgitt som eit årleg beløp)",
                                Language.English to "- 50 percent of income exceeding the allowance amount (calculated to an annual amount)"
                            )
                        }
                        cell {
                            includePhrase(Felles.KronerText(avkortningsbeloepAar))
                        }
                    }
                }
                showIf(harJusteringsbeloep) {
                    row {
                        cell {
                            text(
                                Language.Bokmal to "+ Beløp som er brukt for å justere reduksjonen av barnetillegget",
                                Language.Nynorsk to "+ Beløp som er brukt for å justere reduksjonen av barnetillegget",
                                Language.English to "+ Amount which is used to adjust the reduction of child supplement"
                            )
                        }
                        cell {
                            includePhrase(Felles.KronerText(justeringsbeloepAar))
                        }
                    }
                }
                showIf(
                    harNettoBeloep or (beloepNetto.equalTo(0) and beloepAarNetto.notEqualTo(
                        0
                    ))
                ) {
                    row {
                        cell {
                            text(
                                Language.Bokmal to "= Årlig barnetillegg etter reduksjon ut fra inntekt",
                                Language.Nynorsk to "= Årleg barnetillegg etter reduksjon ut frå inntekt",
                                Language.English to "= Yearly child supplement after income reduction",
                                Element.OutlineContent.ParagraphContent.Text.FontType.BOLD,
                            )
                        }
                        cell {
                            includePhrase(Felles.KronerText(beloepAarNetto))
                        }
                    }
                }
                showIf(
                    faarUtbetaltTillegg or beloepNetto.equalTo(0)
                ) {
                    row {
                        cell {
                            text(
                                Language.Bokmal to "Utbetaling av barnetillegg per måned",
                                Language.Nynorsk to "Utbetaling av barnetillegg per månad",
                                Language.English to "Child supplement payment for the remaining months of the year"
                            )
                        }
                        cell {
                            includePhrase(Felles.KronerText(beloepNetto))
                        }
                    }
                }
                showIf(
                    beloepNetto.equalTo(0) and justeringsbeloepAar.equalTo(0)
                ) {
                    row {
                        cell {
                            text(
                                Language.Bokmal to "Grensen for å få utbetalt barnetillegg",
                                Language.Nynorsk to "Grensa for å få utbetalt barnetillegg",
                                Language.English to "The income limit for receiving child supplement"
                            )
                        }
                        cell {
                            includePhrase(Felles.KronerText(inntektstak))
                        }
                    }
                }
            }
        }
    }

    object SlikBeregnBTOverskrift : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
            title1 {
                text(
                    Language.Bokmal to "Slik beregner vi størrelsen på barnetillegget",
                    Language.Nynorsk to "Slik reknar vi ut storleiken på barnetillegget",
                    Language.English to "How we calculate the amount of child supplement"
                )
            }
    }

    object VedleggBeregnUTInnlednBT : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
            paragraph {
                text(
                    Language.Bokmal to "Størrelsen på barnetillegget er avhengig av samlet inntekt. ",
                    Language.Nynorsk to "Storleiken på barnetillegget er avhengig av samla inntekt. ",
                    Language.English to "The amount of child supplement is dependent on your total income. "
                )
                text(
                    Language.Bokmal to "Barnetillegget kan bli redusert ut fra:",
                    Language.Nynorsk to "Barnetillegget kan bli redusert ut frå:",
                    Language.English to "Child supplement can be reduced based on:"
                )
                list {
                    item {
                        text(
                            Language.Bokmal to "uføretrygd",
                            Language.Nynorsk to "uføretrygd",
                            Language.English to "disability benefits"
                        )
                    }
                    item {
                        text(
                            Language.Bokmal to "arbeidsinntekt",
                            Language.Nynorsk to "arbeidsinntekt",
                            Language.English to "income from employment"
                        )
                    }
                    item {
                        text(
                            Language.Bokmal to "næringsinntekt",
                            Language.Nynorsk to "næringsinntekt ",
                            Language.English to "income from self-employment"
                        )
                    }
                    item {
                        text(
                            Language.Bokmal to "inntekt fra utlandet",
                            Language.Nynorsk to "inntekt frå utlandet",
                            Language.English to "income from overseas"
                        )
                    }
                    item {
                        text(
                            Language.Bokmal to "ytelser/pensjon fra Norge",
                            Language.Nynorsk to "ytingar/pensjon frå Noreg",
                            Language.English to "payments/pensions from Norway"
                        )
                    }
                    item {
                        text(
                            Language.Bokmal to "pensjon fra utlandet",
                            Language.Nynorsk to "pensjon frå utlandet",
                            Language.English to "pensions from overseas"
                        )
                    }
                }
            }
    }

    // TBU605V
    data class PeriodisertInntektFellesbarnA(
        val avkortningsbeloepAar_barnetilleggFBGjeldende: Expression<Kroner>,
        val fribeloepEllerInntektFellesbarn: Expression<Boolean>,
        val justeringsbeloepAar_barnetilleggFBGjeldende: Expression<Kroner>,
        val sivilstand: Expression<Sivilstand>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                val oekeJusteringsbeloepFellesbarn = justeringsbeloepAar_barnetilleggFBGjeldende.greaterThan(0)
                text(
                    Language.Bokmal to "Når inntekten din eller til din ",
                    Language.Nynorsk to "Når inntekta di eller til di ",
                    Language.English to "When your or your "
                )

                includePhrase(Felles.SivilstandEPSUbestemtForm(sivilstand))

                text(
                    Language.Bokmal to " endrer seg, blir reduksjonen av barnetillegget vurdert på nytt. 50 prosent av den inntekten som overstiger fribeløpet ",
                    Language.Nynorsk to " endrar seg, blir reduksjonen av barnetillegget vurdert på nytt. 50 prosent av inntekta som overstig fribeløpet ",
                    Language.English to "'s income has been changed, your child supplement will be recalculated. 50 percent of income that exceeds the exemption amount "
                )

                showIf(fribeloepEllerInntektFellesbarn) {
                    textExpr(
                        Language.Bokmal to " blir omregnet til et årlig beløp som tilsvarer ".expr() + avkortningsbeloepAar_barnetilleggFBGjeldende.format() + " kroner.".expr(),
                        Language.Nynorsk to " blir rekna om til et årleg beløp som svarer til ".expr() + avkortningsbeloepAar_barnetilleggFBGjeldende.format() + " kroner.".expr(),
                        Language.English to " is recalculated to an annual amount of NOK ".expr() + avkortningsbeloepAar_barnetilleggFBGjeldende.format() + ".".expr()
                    )
                }.orShowIf(not(fribeloepEllerInntektFellesbarn)) {
                    textExpr(
                        Language.Bokmal to " er ".expr() + avkortningsbeloepAar_barnetilleggFBGjeldende.format() + " kroner.".expr(),
                        Language.Nynorsk to " er ".expr() + avkortningsbeloepAar_barnetilleggFBGjeldende.format() + " kroner.".expr(),
                        Language.English to " is NOK ".expr() + avkortningsbeloepAar_barnetilleggFBGjeldende.format() + ".".expr()
                    )
                }
                showIf(justeringsbeloepAar_barnetilleggFBGjeldende.equalTo(0)) {
                    text(
                        Language.Bokmal to " Dette beløpet bruker vi til å redusere barnetillegget ditt for heile året.",
                        Language.Nynorsk to " Dette beløpet bruker vi til å redusere barnetillegget ditt for heile året.",
                        Language.English to " This amount will be used to reduce your child supplement during the calendar year."
                    )
                }
                showIf(justeringsbeloepAar_barnetilleggFBGjeldende.notEqualTo(0)) {
                    textExpr(
                        Language.Bokmal to "Vi tar hensyn til hvordan barnetillegget eventuelt har vært redusert tidligere, og vi har derfor ".expr() +
                                ifElse(
                                    oekeJusteringsbeloepFellesbarn,
                                    "lagt til",
                                    "trukket fra"
                                ) + " ".expr() + justeringsbeloepAar_barnetilleggFBGjeldende.format() + " kroner i beløpet vi reduserer barnetillegget med for resten av året".expr(),
                        Language.Nynorsk to "Vi tek omsyn til korleis eit barnetillegg eventuelt har vore redusert tidlegare, og har derfor ".expr() +
                                ifElse(
                                    oekeJusteringsbeloepFellesbarn,
                                    "lagt til",
                                    "trekt frå"
                                ) + " ".expr() + justeringsbeloepAar_barnetilleggFBGjeldende.format() + " kroner i beløpet vi reduserer barnetillegget med for resten av året.".expr(),
                        Language.English to "We take into account how the child supplement has been reduced earlier this year. The amount with which your child supplement will be reduced for the rest of the year has therefore been ".expr() +
                                ifElse(
                                    oekeJusteringsbeloepFellesbarn,
                                    "increased",
                                    "reduced"
                                ) + " with NOK ".expr() + justeringsbeloepAar_barnetilleggFBGjeldende.format() + ".".expr()
                    )
                }
            }
        }
    }

    // TBU605V - saerkullsbarn
    data class PeriodisertInntekSaerkullsbarnA(
        val avkortningsbeloepAar_barnetilleggSBGjeldende: Expression<Kroner>,
        val fribeloepEllerInntektSaerkullsbarn: Expression<Boolean>,
        val justeringsbeloepAar_barnetilleggSBGjeldende: Expression<Kroner>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                val oekeJusteringsbeloepSaerkullsbarn = justeringsbeloepAar_barnetilleggSBGjeldende.greaterThan(0)
                text(
                    Language.Bokmal to "Når inntekten din endrer seg, blir reduksjonen av barnetillegget vurdert på nytt. 50 prosent av den inntekten som overstiger fribeløpet ",
                    Language.Nynorsk to "Når inntekta di endrar seg, blir reduksjonen av barnetillegget vurdert på nytt. 50 prosent av inntekta som overstig fribeløpet ",
                    Language.English to "When your income has been changed, your child supplement will be recalculated. 50 percent of income that exceeds the exemption amount "
                )
                showIf(fribeloepEllerInntektSaerkullsbarn) {
                    textExpr(
                        Language.Bokmal to " blir omregnet til et årlig beløp som tilsvarer ".expr() + avkortningsbeloepAar_barnetilleggSBGjeldende.format() + " kroner.".expr(),
                        Language.Nynorsk to " blir rekna om til et årleg beløp som svarer til ".expr() + avkortningsbeloepAar_barnetilleggSBGjeldende.format() + " kroner.".expr(),
                        Language.English to " is recalculated to an annual amount of NOK ".expr() + avkortningsbeloepAar_barnetilleggSBGjeldende.format() + ".".expr()
                    )
                }.orShowIf(not(fribeloepEllerInntektSaerkullsbarn)) {
                    textExpr(
                        Language.Bokmal to " er ".expr() + avkortningsbeloepAar_barnetilleggSBGjeldende.format() + " kroner.".expr(),
                        Language.Nynorsk to " er ".expr() + avkortningsbeloepAar_barnetilleggSBGjeldende.format() + " kroner.".expr(),
                        Language.English to " is NOK ".expr() + avkortningsbeloepAar_barnetilleggSBGjeldende.format() + ".".expr()
                    )
                }
                showIf(justeringsbeloepAar_barnetilleggSBGjeldende.equalTo(0)) {
                    text(
                        Language.Bokmal to " Dette beløpet bruker vi til å redusere barnetillegget ditt for heile året.",
                        Language.Nynorsk to " Dette beløpet bruker vi til å redusere barnetillegget ditt for heile året.",
                        Language.English to " This amount will be used to reduce your child supplement during the calendar year."
                    )
                }
                showIf(justeringsbeloepAar_barnetilleggSBGjeldende.notEqualTo(0)) {
                    textExpr(
                        Language.Bokmal to "Vi tar hensyn til hvordan barnetillegget eventuelt har vært redusert tidligere, og vi har derfor ".expr() +
                                ifElse(
                                    oekeJusteringsbeloepSaerkullsbarn,
                                    "lagt til",
                                    "trukket fra"
                                ) + " ".expr() + justeringsbeloepAar_barnetilleggSBGjeldende.format() + " kroner i beløpet vi reduserer barnetillegget med for resten av året".expr(),
                        Language.Nynorsk to "Vi tek omsyn til korleis eit barnetillegg eventuelt har vore redusert tidlegare, og har derfor ".expr() +
                                ifElse(
                                    oekeJusteringsbeloepSaerkullsbarn,
                                    "lagt til",
                                    "trekt frå"
                                ) + " ".expr() + justeringsbeloepAar_barnetilleggSBGjeldende.format() + " kroner i beløpet vi reduserer barnetillegget med for resten av året.".expr(),
                        Language.English to "We take into account how the child supplement has been reduced earlier this year. The amount with which your child supplement will be reduced for the rest of the year has therefore been ".expr() +
                                ifElse(
                                    oekeJusteringsbeloepSaerkullsbarn,
                                    "increased",
                                    "reduced"
                                ) + " with NOK ".expr() + justeringsbeloepAar_barnetilleggSBGjeldende.format() + ".".expr()
                    )
                    showIf(justeringsbeloepAar_barnetilleggSBGjeldende.equalTo(0)) {
                        text(
                            Language.Bokmal to " Dette beløpet bruker vi til å redusere barnetillegget ditt for heile året.",
                            Language.Nynorsk to " Dette beløpet bruker vi til å redusere barnetillegget ditt for heile året.",
                            Language.English to " This amount will be used to reduce your child supplement during the calendar year."
                        )
                    }
                }
            }
        }
    }

    // TBU613V - fellesbarn
    data class PeriodisertInntektFellesbarnB(
        val avkortningsbeloepAar_barnetilleggFBGjeldende: Expression<Kroner>,
        val fribeloepEllerInntektErPeriodisert_barnetilleggFBGjeldende: Expression<Boolean>,
        val harTilleggForFlereFellesbarn: Expression<Boolean>,
        val justeringsbeloepAar_barnetilleggFBGjeldende: Expression<Kroner>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                val harFribeloepEllerInntektPeriodisert = fribeloepEllerInntektErPeriodisert_barnetilleggFBGjeldende
                textExpr(
                    Language.Bokmal to "For ".expr() +
                            ifElse(harTilleggForFlereFellesbarn, "barna", "barnet")
                            + " som bor med begge sine foreldre, 50 prosent av den inntekten som overstiger fribeløpet ".expr() +
                            ifElse(
                                harFribeloepEllerInntektPeriodisert,
                                "blir omregnet til et årlig beløp som tilsvarer",
                                "er"
                            )
                            + " ".expr() + avkortningsbeloepAar_barnetilleggFBGjeldende.format() + ".".expr(),
                    Language.Nynorsk to "For ".expr() +
                            ifElse(harTilleggForFlereFellesbarn, "barna", "barnet")
                            + " som bur med begge foreldra sine, 50 prosent av inntekta som overstig fribeløpet ".expr() +
                            ifElse(
                                harFribeloepEllerInntektPeriodisert,
                                "rekna om til et årleg beløp som svarer til",
                                "er"
                            )
                            + " ".expr() + avkortningsbeloepAar_barnetilleggFBGjeldende.format() + ".".expr(),
                    Language.English to "For ".expr() +
                            ifElse(
                                harTilleggForFlereFellesbarn,
                                "children who live",
                                "a child who lives"
                            )
                            + " with both of their parents, 50 percent of the income that exceeds the exemption amount ".expr() +
                            ifElse(
                                harFribeloepEllerInntektPeriodisert,
                                "is recalculated to an annual amount of",
                                "is"
                            )
                            + " NOK ".expr() + avkortningsbeloepAar_barnetilleggFBGjeldende.format() + ".".expr()
                )
                showIf(justeringsbeloepAar_barnetilleggFBGjeldende.equalTo(0)) {
                    text(
                        Language.Bokmal to " Dette beløpet bruker vi til å redusere barnetillegget ditt for heile året.",
                        Language.Nynorsk to " Dette beløpet bruker vi til å redusere barnetillegget ditt for heile året.",
                        Language.English to " This amount will be used to reduce your child supplement during the calendar year."
                    )
                }
            }
        }
    }

    // TBU613V - fellesbarn
    data class PeriodisertInntektFellesbarnC(
        val justeringsbeloepAar_barnetilleggFBGjeldende: Expression<Kroner>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                val oekeReduksjonenAvTilleggetFellesbarn = justeringsbeloepAar_barnetilleggFBGjeldende.greaterThan(0)
                showIf(justeringsbeloepAar_barnetilleggFBGjeldende.notEqualTo(0)) {
                    textExpr(
                        Language.Bokmal to "Vi tar hensyn til hvordan barnetillegget eventuelt har vært redusert tidligere, og vi har derfor ".expr() +
                                ifElse(
                                    oekeReduksjonenAvTilleggetFellesbarn,
                                    "lagt til",
                                    "trukket fra"
                                )
                                + " ".expr() + justeringsbeloepAar_barnetilleggFBGjeldende.format() + " kroner i beløpet vi reduserer barnetillegget med for resten av året".expr(),
                        Language.Nynorsk to "Vi tek omsyn til korleis eit barnetillegg eventuelt har vore redusert tidlegare, og har derfor ".expr() +
                                ifElse(oekeReduksjonenAvTilleggetFellesbarn, "lagt til", "trekt frå")
                                + " ".expr() + justeringsbeloepAar_barnetilleggFBGjeldende.format() + " kroner i beløpet vi reduserer barnetillegget med for resten av året.".expr(),
                        Language.English to "We take into account how the child supplement has been reduced earlier this year. The amount with which your child supplement will be reduced for the rest of the year has therefore been ".expr() +
                                ifElse(oekeReduksjonenAvTilleggetFellesbarn, "increased", "reduced")
                                + " with NOK ".expr() + justeringsbeloepAar_barnetilleggFBGjeldende.format() + ".".expr()
                    )
                }

            }
        }
    }

    // TBU613V - saerkullsbarn
    data class PeriodisertInntektSaerkullsbarnB(
        val avkortningsbeloepAar_barnetilleggSBGjeldende: Expression<Kroner>,
        val fribeloepEllerInntektErPeriodisert_barnetilleggSBGjeldende: Expression<Boolean>,
        val harTilleggForFlereSaerkullsbarn: Expression<Boolean>,
        val justeringsbeloepAar_barnetilleggSBGjeldende: Expression<Kroner>,
        val sivilstand: Expression<Sivilstand>,
        val erRedusertMotInntektSaerkullsbarn: Expression<Boolean>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                val fribeloepEllerInntektErPeriodisert = fribeloepEllerInntektErPeriodisert_barnetilleggSBGjeldende
                textExpr(
                    Language.Bokmal to "For ".expr() +
                            ifElse(harTilleggForFlereSaerkullsbarn, "barna", "barnet")
                            + " som ikke bor med begge sine foreldre, 50 prosent av den inntekten som overstiger fribeløpet ".expr() +
                            ifElse(
                                fribeloepEllerInntektErPeriodisert,
                                "blir omregnet til et årlig beløp som tilsvarer",
                                "er"
                            )
                            + " ".expr() + avkortningsbeloepAar_barnetilleggSBGjeldende.format() + ".".expr(),
                    Language.Nynorsk to "For ".expr() +
                            ifElse(harTilleggForFlereSaerkullsbarn, "barna", "barnet")
                            + " som ikkje bur med begge foreldra sine, 50 prosent av inntekta som overstig fribeløpet ".expr() +
                            ifElse(
                                fribeloepEllerInntektErPeriodisert,
                                "rekna om til et årleg beløp som svarer til",
                                "er"
                            )
                            + " ".expr() + avkortningsbeloepAar_barnetilleggSBGjeldende.format() + ".".expr(),
                    Language.English to "For ".expr() +
                            ifElse(
                                harTilleggForFlereSaerkullsbarn,
                                "children who do not",
                                "the child who does not"
                            )
                            + " live with both of their parents, 50 percent of the income that exceeds the exemption amount ".expr() +
                            ifElse(
                                fribeloepEllerInntektErPeriodisert,
                                "is recalculated to an annual amount of",
                                "is"
                            )
                            + " NOK ".expr() + avkortningsbeloepAar_barnetilleggSBGjeldende.format() + ".".expr()
                )

                showIf(justeringsbeloepAar_barnetilleggSBGjeldende.equalTo(0)) {
                    text(
                        Language.Bokmal to " Dette beløpet bruker vi til å redusere barnetillegget ditt for heile året.",
                        Language.Nynorsk to " Dette beløpet bruker vi til å redusere barnetillegget ditt for heile året.",
                        Language.English to " This amount will be used to reduce your child supplement during the calendar year."
                    )
                }
            }
        }
    }


    // TBU613 - saerkullsbarn
    data class PeriodisertInntektSaerkullsbarnC(
        val justeringsbeloepAar_barnetilleggSBGjeldende: Expression<Kroner>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                val oekeReduksjonenAvTilleggetSaerkullsbarn = justeringsbeloepAar_barnetilleggSBGjeldende.greaterThan(0)
                showIf(justeringsbeloepAar_barnetilleggSBGjeldende.notEqualTo(0)) {
                    textExpr(
                        Language.Bokmal to "Vi tar hensyn til hvordan barnetillegget eventuelt har vært redusert tidligere, og vi har derfor ".expr() +
                                ifElse(
                                    oekeReduksjonenAvTilleggetSaerkullsbarn,
                                    "lagt til",
                                    "trukket fra"
                                )
                                + " ".expr() + justeringsbeloepAar_barnetilleggSBGjeldende.format() + " kroner i beløpet vi reduserer barnetillegget med for resten av året".expr(),
                        Language.Nynorsk to "Vi tek omsyn til korleis eit barnetillegg eventuelt har vore redusert tidlegare, og har derfor ".expr() +
                                ifElse(
                                    oekeReduksjonenAvTilleggetSaerkullsbarn,
                                    "lagt til",
                                    "trekt frå"
                                )
                                + " ".expr() + justeringsbeloepAar_barnetilleggSBGjeldende.format() + " kroner i beløpet vi reduserer barnetillegget med for resten av året.".expr(),
                        Language.English to "We take into account how the child supplement has been reduced earlier this year. The amount with which your child supplement will be reduced for the rest of the year has therefore been ".expr() +
                                ifElse(
                                    oekeReduksjonenAvTilleggetSaerkullsbarn,
                                    "increased",
                                    "reduced"
                                )
                                + " with NOK ".expr() + justeringsbeloepAar_barnetilleggSBGjeldende.format() + ".".expr()
                    )
                }
            }
        }
    }

}