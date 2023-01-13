package no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.KravAarsak
import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.api.model.vedlegg.BarnetilleggGjeldendeSelectors.fellesbarn_safe
import no.nav.pensjon.brev.api.model.vedlegg.BarnetilleggGjeldendeSelectors.saerkullsbarn_safe
import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.avkortningsbeloepAar
import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.avkortningsbeloepAar_safe
import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.beloepAarBrutto
import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.beloepAarNetto
import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.beloepNetto
import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.erRedusertMotinntekt
import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.fribeloep
import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.fribeloepEllerInntektErPeriodisert_safe
import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.harFlereBarn
import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.inntektBruktIAvkortning
import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.inntektOverFribeloep
import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.inntektstak
import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.justeringsbeloepAar
import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.justeringsbeloepAar_safe
import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.virkdatoErNesteAar
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDto
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.avkortningsbeloepAar
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.avkortningsbeloepAar_safe
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.beloepAarBrutto
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.beloepAarNetto
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.beloepNetto
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.erRedusertMotinntekt
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.erRedusertMotinntekt_safe
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.fribeloep
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.fribeloepEllerInntektErPeriodisert
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.fribeloepEllerInntektErPeriodisert_safe
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.harFlereBarn
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.inntektBruktIAvkortning
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.inntektOverFribeloep
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.inntektstak
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.justeringsbeloepAar
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.justeringsbeloepAar_safe
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.virkdatoErNesteAar
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr

data class OpplysningerOmBarnetillegg(
    val barnetillegg: Expression<OpplysningerBruktIBeregningUTDto.BarnetilleggGjeldende>,
    val sivilstand: Expression<Sivilstand>,
    val anvendtTrygdetid: Expression<Int>,
    val harYrkesskade: Expression<Boolean>,
    val kravAarsak: Expression<KravAarsak>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        val harAnvendtTrygdetidUnder40 = anvendtTrygdetid.lessThan(40)
        val harTilleggFellesBarn = barnetillegg.notNull()
        val harTilleggSaerkullsbarn = barnetillegg.notNull()
        val barnetillegSaerkullsbarnErRedusertMotInntekt =
            barnetillegg.saerkullsbarn_safe.erRedusertMotinntekt_safe.ifNull(false)
        val inntektEllerFribeloepErPeriodisert =
            barnetillegg.fellesbarn_safe.fribeloepEllerInntektErPeriodisert_safe.ifNull(false) or
                    barnetillegg.saerkullsbarn_safe.fribeloepEllerInntektErPeriodisert_safe.ifNull(false)
        val justeringsbeloepFelles = barnetillegg.fellesbarn_safe.justeringsbeloepAar_safe.ifNull(Kroner(0))
        val justeringsbeloepSaerkull = barnetillegg.saerkullsbarn_safe.justeringsbeloepAar_safe.ifNull(Kroner(0))
        val harJusteringsbeloep = justeringsbeloepFelles.greaterThan(0) or
                justeringsbeloepSaerkull.greaterThan(0)

        includePhrase(SlikBeregnBTOverskrift)
        includePhrase(VedleggBeregnUTInnlednBT)

        showIf(harTilleggFellesBarn and not(harTilleggSaerkullsbarn)) {
            includePhrase(
                FastsetterStoerelsenPaaBTFellesbarn(
                    harAnvendtTrygdetidUnder40 = harAnvendtTrygdetidUnder40,
                    harYrkesskade = harYrkesskade,
                )
            )
        }

        showIf(harTilleggSaerkullsbarn and not(harTilleggFellesBarn)) {
            includePhrase(
                FastsetterStoerelsenPaaBTSaerkullsbarn(
                    harAnvendtTrygdetidUnder40 = harAnvendtTrygdetidUnder40,
                    harYrkesskade = harYrkesskade,
                )
            )
        }

        ifNotNull(barnetillegg.fellesbarn_safe, barnetillegg.saerkullsbarn_safe) { felles, saerkull ->
            includePhrase(
                FastsetterStoerelsenPaaBTFellesbarnOgSaerkullsbarn(
                    harAnvendtTrygdetidUnder40 = harAnvendtTrygdetidUnder40,
                    harTilleggForFlereFellesbarn = felles.harFlereBarn,
                    harTilleggForFlereSaerkullsbarn = saerkull.harFlereBarn,
                    sivilstand = sivilstand,
                    harYrkesskade = harYrkesskade,
                )
            )
        }

        ifNotNull(barnetillegg.fellesbarn_safe, barnetillegg.saerkullsbarn_safe) { fellesTillegg, saerkullTillegg ->
            //TBU613V
            includePhrase(
                PeriodisertInntektInnledning(
                    sivilstand = sivilstand,
                    kravAarsak = kravAarsak,
                    harFlereTillegg = true.expr()
                )
            )
            includePhrase(
                PeriodisertInntektOverFribeloepFlereTillegg(
                    inntektEllerFribeloepErPeriodisert = inntektEllerFribeloepErPeriodisert,
                    avkortningsbeloepAarSaerkull = saerkullTillegg.avkortningsbeloepAar,
                    avkortningsbeloepAarFelles = fellesTillegg.avkortningsbeloepAar,
                    harJusteringsbeloep = harJusteringsbeloep,
                    harTilleggForFlereFellesbarn = fellesTillegg.harFlereBarn,
                )
            )

            includePhrase(JusteringBarnetillegg(fellesTillegg.justeringsbeloepAar))

            showIf(barnetillegSaerkullsbarnErRedusertMotInntekt) {
                includePhrase(
                    PeriodisertInntektSaerkullsbarn(
                        avkortningsbeloepAarSaerkullsbarn = saerkullTillegg.avkortningsbeloepAar,
                        fribeloepEllerInntektErPeriodisertSaerkullsbarn = saerkullTillegg.fribeloepEllerInntektErPeriodisert,
                        harTilleggForFlereSaerkullsbarn = saerkullTillegg.harFlereBarn,
                        justeringsbeloepAarSaerkullsbarn = saerkullTillegg.justeringsbeloepAar,
                        sivilstand = sivilstand,
                    )
                )
                includePhrase(JusteringBarnetillegg(saerkullTillegg.justeringsbeloepAar))
            }
        }.orShow {
            //TBU605V
            includePhrase(
                PeriodisertInntektInnledning(
                    sivilstand = sivilstand,
                    kravAarsak = kravAarsak,
                    harFlereTillegg = false.expr(),
                )
            )
            includePhrase(
                PeriodisertInntektOverFribeloepEttTillegg(
                    inntektEllerFribeloepErPeriodisert = inntektEllerFribeloepErPeriodisert,
                    avkortningsbeloepAarSaerkull = barnetillegg.saerkullsbarn_safe.avkortningsbeloepAar_safe.ifNull(
                        Kroner(0)
                    ),
                    avkortningsbeloepAarFelles = barnetillegg.fellesbarn_safe.avkortningsbeloepAar_safe.ifNull(
                        Kroner(0)
                    ),
                    harJusteringsbeloep = harJusteringsbeloep,
                )
            )

            showIf(justeringsbeloepFelles.greaterThan(0)) {
                includePhrase(JusteringBarnetillegg(justeringsbeloepFelles))
            }.orShowIf(justeringsbeloepSaerkull.greaterThan(0)) {
                includePhrase(JusteringBarnetillegg(justeringsbeloepSaerkull))
            }
        }

        ifNotNull(barnetillegg.fellesbarn_safe) { fellesTillegg ->
            showIf(fellesTillegg.erRedusertMotinntekt) {
                title1 {
                    textExpr(
                        Bokmal to "Reduksjon av barnetillegg for fellesbarn før skatt ".expr()
                                + ifElse(fellesTillegg.virkdatoErNesteAar, "for neste år", "i år"),
                        Nynorsk to "Reduksjon av barnetillegg for fellesbarn før skatt ".expr()
                                + ifElse(fellesTillegg.virkdatoErNesteAar, "for neste år", "i år"),
                        English to "Reduction of child supplement payment for joint children before tax, ".expr()
                                + ifElse(fellesTillegg.virkdatoErNesteAar, "for next year", "for this year"),
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

                showIf(fellesTillegg.beloepNetto.greaterThan(0)) {
                    includePhrase(
                        MaanedligTilleggFellesbarn(
                            beloep_barnetilleggFBGjeldende = fellesTillegg.beloepNetto,
                            harFlereBarn = fellesTillegg.harFlereBarn,
                        )
                    )
                }.orShow {
                    includePhrase(
                        FaaIkkeUtbetaltTilleggFellesbarn(
                            beloep_barnetilleggFBGjeldende = fellesTillegg.beloepNetto,
                            justeringsbeloepAar_barnetilleggFBGjeldende = fellesTillegg.justeringsbeloepAar,
                            harFlereBarn = fellesTillegg.harFlereBarn,
                        )
                    )
                }
            }
        }

        ifNotNull(barnetillegg.saerkullsbarn_safe) { saerkullTillegg ->
            showIf(saerkullTillegg.erRedusertMotinntekt) {
                title1 {
                    textExpr(
                        Bokmal to "Reduksjon av barnetillegg for særkullsbarn før skatt ".expr()
                                + ifElse(saerkullTillegg.virkdatoErNesteAar, "for neste år", "i år"),
                        Nynorsk to "Reduksjon av barnetillegg for særkullsbarn før skatt ".expr()
                                + ifElse(saerkullTillegg.virkdatoErNesteAar, "for neste år", "i år"),
                        English to "Reduction of child supplement payment for children from a previous relationship before tax, ".expr()
                                + ifElse(saerkullTillegg.virkdatoErNesteAar, "for next year", "for this year"),
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

                showIf(saerkullTillegg.beloepNetto.greaterThan(0)) {
                    includePhrase(
                        VedleggBeregnUTredusBTSBPgaInntekt(
                            saerkullTillegg.beloepNetto
                        )
                    )
                }.orShow {
                    showIf(saerkullTillegg.justeringsbeloepAar.equalTo(0)) {
                        includePhrase(VedleggBeregnUTIkkeUtbetaltBTSBPgaInntekt)
                    } orShow {
                        includePhrase(VedleggBeregnUTJusterBelopIkkeUtbetalt)
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
                            Bokmal to "Beskrivelse",
                            Nynorsk to "Beskrivelse",
                            English to "Description",
                            Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
                        )
                    }
                    column(alignment = Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT) {
                        text(
                            Bokmal to "Beløp",
                            Nynorsk to "Beløp",
                            English to "Amount",
                            Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
                        )
                    }
                }
            ) {
                showIf(faarUtbetaltTillegg and harJusteringsbeloep) {
                    row {
                        cell {
                            text(
                                Bokmal to "Årlig barnetillegg før reduksjon ut fra inntekt",
                                Nynorsk to "Årleg barnetillegg før reduksjon ut frå inntekt",
                                English to "Yearly child supplement before income reduction"
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
                                Bokmal to "Samlet inntekt brukt i fastsettelse av barnetillegget er ",
                                Nynorsk to "Samla inntekt brukt i fastsetjinga av barnetillegget er ",
                                English to "Total income applied in calculation of reduction in child supplement is "
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
                                Bokmal to "Fribeløp brukt i fastsettelsen av barnetillegget er",
                                Nynorsk to "Fribeløp brukt i fastsetjinga av barnetillegget er",
                                English to "Exemption amount applied in calculation of reduction in child supplement is",
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
                                Bokmal to "Inntekt over fribeløpet er",
                                Nynorsk to "Inntekt over fribeløpet er",
                                English to "Income exceeding the exemption amount is",
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
                                Bokmal to "- 50 prosent av inntekt som overstiger fribeløpet (oppgitt som et årlig beløep)",
                                Nynorsk to "- 50 prosent av inntekta som overstig fribeløpet (oppgitt som eit årleg beløp)",
                                English to "- 50 percent of income exceeding the allowance amount (calculated to an annual amount)"
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
                                Bokmal to "+ Beløp som er brukt for å justere reduksjonen av barnetillegget",
                                Nynorsk to "+ Beløp som er brukt for å justere reduksjonen av barnetillegget",
                                English to "+ Amount which is used to adjust the reduction of child supplement"
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
                                Bokmal to "= Årlig barnetillegg etter reduksjon ut fra inntekt",
                                Nynorsk to "= Årleg barnetillegg etter reduksjon ut frå inntekt",
                                English to "= Yearly child supplement after income reduction",
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
                                Bokmal to "Utbetaling av barnetillegg per måned",
                                Nynorsk to "Utbetaling av barnetillegg per månad",
                                English to "Child supplement payment for the remaining months of the year"
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
                                Bokmal to "Grensen for å få utbetalt barnetillegg",
                                Nynorsk to "Grensa for å få utbetalt barnetillegg",
                                English to "The income limit for receiving child supplement"
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
                    Bokmal to "Slik beregner vi størrelsen på barnetillegget",
                    Nynorsk to "Slik reknar vi ut storleiken på barnetillegget",
                    English to "How we calculate the amount of child supplement"
                )
            }
    }

    object VedleggBeregnUTInnlednBT : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
            paragraph {
                text(
                    Bokmal to "Størrelsen på barnetillegget er avhengig av samlet inntekt. ",
                    Nynorsk to "Storleiken på barnetillegget er avhengig av samla inntekt. ",
                    English to "The amount of child supplement is dependent on your total income. "
                )
                text(
                    Bokmal to "Barnetillegget kan bli redusert ut fra:",
                    Nynorsk to "Barnetillegget kan bli redusert ut frå:",
                    English to "Child supplement can be reduced based on:"
                )
                list {
                    item {
                        text(
                            Bokmal to "uføretrygd",
                            Nynorsk to "uføretrygd",
                            English to "disability benefits"
                        )
                    }
                    item {
                        text(
                            Bokmal to "arbeidsinntekt",
                            Nynorsk to "arbeidsinntekt",
                            English to "income from employment"
                        )
                    }
                    item {
                        text(
                            Bokmal to "næringsinntekt",
                            Nynorsk to "næringsinntekt ",
                            English to "income from self-employment"
                        )
                    }
                    item {
                        text(
                            Bokmal to "inntekt fra utlandet",
                            Nynorsk to "inntekt frå utlandet",
                            English to "income from overseas"
                        )
                    }
                    item {
                        text(
                            Bokmal to "ytelser/pensjon fra Norge",
                            Nynorsk to "ytingar/pensjon frå Noreg",
                            English to "payments/pensions from Norway"
                        )
                    }
                    item {
                        text(
                            Bokmal to "pensjon fra utlandet",
                            Nynorsk to "pensjon frå utlandet",
                            English to "pensions from overseas"
                        )
                    }
                }
            }
    }

    data class PeriodisertInntektSaerkullsbarn(
        val avkortningsbeloepAarSaerkullsbarn: Expression<Kroner>,
        val fribeloepEllerInntektErPeriodisertSaerkullsbarn: Expression<Boolean>,
        val harTilleggForFlereSaerkullsbarn: Expression<Boolean>,
        val justeringsbeloepAarSaerkullsbarn: Expression<Kroner>,
        val sivilstand: Expression<Sivilstand>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                val fribeloepEllerInntektErPeriodisert = fribeloepEllerInntektErPeriodisertSaerkullsbarn
                textExpr(
                    Bokmal to "For ".expr() +
                            ifElse(harTilleggForFlereSaerkullsbarn, "barna", "barnet")
                            + " som ikke bor med begge sine foreldre ".expr() +
                            ifElse(
                                fribeloepEllerInntektErPeriodisert,
                                "blir 50 prosent av den inntekten som overstiger fribeløpet omregnet til et årlig beløp som tilsvarer",
                                "er 50 prosent av den inntekten som overstiger fribeløpet"
                            )
                            + " ".expr() + avkortningsbeloepAarSaerkullsbarn.format() + " kroner.".expr(),
                    Nynorsk to "For ".expr() +
                            ifElse(harTilleggForFlereSaerkullsbarn, "barna", "barnet")
                            + " som ikkje bur med begge foreldra ".expr() +
                            ifElse(
                                fribeloepEllerInntektErPeriodisert,
                                "blir 50 prosent av den inntekta som overstig fribeløpet regna om til et årleg beløp som svarer til",
                                "er 50 prosent av den inntekta som overstig fribeløpet"
                            )
                            + " ".expr() + avkortningsbeloepAarSaerkullsbarn.format() + " kroner.".expr(),
                    English to "For ".expr() +
                            ifElse(
                                harTilleggForFlereSaerkullsbarn,
                                "children who do not",
                                "the child who does not"
                            )
                            + " live with both parents 50 percent of the income that exceeds the exemption amount ".expr() +
                            ifElse(
                                fribeloepEllerInntektErPeriodisert,
                                "is recalculated to an annual amount of",
                                "is"
                            )
                            + " NOK ".expr() + avkortningsbeloepAarSaerkullsbarn.format() + ".".expr()
                )

                showIf(justeringsbeloepAarSaerkullsbarn.equalTo(0)) {
                    text(
                        Bokmal to " Dette beløpet bruker vi til å redusere barnetillegget ditt for heile året.",
                        Nynorsk to " Dette beløpet bruker vi til å redusere barnetillegget ditt for heile året.",
                        English to " This amount will be used to reduce your child supplement during the calendar year."
                    )
                }
            }
        }
    }

    data class JusteringBarnetillegg(val justeringsbeloep: Expression<Kroner>) :
        OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            val oekeReduksjonenAvTilleggetSaerkullsbarn = justeringsbeloep.greaterThan(0)
            showIf(justeringsbeloep.notEqualTo(0)) {
                paragraph {
                    textExpr(
                        Bokmal to "Vi tar hensyn til hvordan barnetillegget eventuelt har vært redusert tidligere, og vi har derfor ".expr() +
                                ifElse(
                                    oekeReduksjonenAvTilleggetSaerkullsbarn,
                                    "lagt til",
                                    "trukket fra"
                                )
                                + " ".expr() + justeringsbeloep.format() + " kroner i beløpet vi reduserer barnetillegget med for resten av året.".expr(),
                        Nynorsk to "Vi tek omsyn til korleis eit barnetillegg eventuelt har vore redusert tidlegare, og har derfor ".expr() +
                                ifElse(
                                    oekeReduksjonenAvTilleggetSaerkullsbarn,
                                    "lagt til",
                                    "trekt frå"
                                )
                                + " ".expr() + justeringsbeloep.format() + " kroner i beløpet vi reduserer barnetillegget med for resten av året.".expr(),
                        English to "We take into account how the child supplement has been reduced earlier this year. The amount with which your child supplement will be reduced for the rest of the year has therefore been ".expr() +
                                ifElse(
                                    oekeReduksjonenAvTilleggetSaerkullsbarn,
                                    "increased",
                                    "reduced"
                                )
                                + " with NOK ".expr() + justeringsbeloep.format() + ".".expr()
                    )
                }
            }
        }
    }


    data class VedleggBeregnUTredusBTSBPgaInntekt(
        val beloep_barnetilleggSBGjeldende: Expression<Kroner>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
            paragraph {
                textExpr(
                    Bokmal to "Du vil få utbetalt ".expr() + beloep_barnetilleggSBGjeldende.format() + " kroner i måneden før skatt i barnetillegg.",
                    Nynorsk to "Du vil få utbetalt ".expr() + beloep_barnetilleggSBGjeldende.format() + " kroner i månaden før skatt i barnetillegg.",
                    English to "You will receive a monthly child supplement payment of NOK ".expr() + beloep_barnetilleggSBGjeldende.format() + " before tax."
                )
            }
    }

    object VedleggBeregnUTIkkeUtbetaltBTSBPgaInntekt : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
            paragraph {
                text(
                    Bokmal to "Du får ikke utbetalt barnetillegget fordi samlet inntekt er over grensen for å få utbetalt barnetillegg.",
                    Nynorsk to "Du får ikkje utbetalt barnetillegget fordi samla inntekt er over grensa for å få utbetalt barnetillegg.",
                    English to "You will not receive a child supplement because your income is over the income limit for receiving a child supplement."
                )
            }
    }

    object VedleggBeregnUTJusterBelopIkkeUtbetalt : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
            paragraph {
                text(
                    Bokmal to "Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året.",
                    Nynorsk to "Du har allereie fått utbetalt det du har rett til i år, og får difor ikkje utbetalt barnetillegg for resten av året",
                    English to "You have already received what you are entitled to this year, therefore you will not receive any child supplement for the remainder of the year."
                )
            }
    }

    // TBU607V
    data class MaanedligTilleggFellesbarn(
        val beloep_barnetilleggFBGjeldende: Expression<Kroner>,
        val harFlereBarn: Expression<Boolean>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                textExpr(
                    Bokmal to "Du vil få utbetalt ".expr() + beloep_barnetilleggFBGjeldende.format() + " kroner i måneden før skatt i barnetillegg for ".expr() +
                            ifElse(harFlereBarn, "barna", "barnet") +
                            " som bor med begge sine foreldre".expr(),
                    Nynorsk to "Du vil få utbetalt ".expr() + beloep_barnetilleggFBGjeldende.format() + " kroner i månaden før skatt i barnetillegg for ".expr() +
                            ifElse(harFlereBarn, "barna", "barnet") +
                            " som bur saman med begge foreldra sine.".expr(),
                    English to "You will receive a monthly child supplement payment of NOK ".expr() + beloep_barnetilleggFBGjeldende.format() + " for the ".expr() +
                            ifElse(harFlereBarn, "children who live", "child who lives") +
                            " together with both parents.".expr()
                )
            }
        }
    }

    // TBU608V
    data class FaaIkkeUtbetaltTilleggFellesbarn(
        val beloep_barnetilleggFBGjeldende: Expression<Kroner>,
        val harFlereBarn: Expression<Boolean>,
        val justeringsbeloepAar_barnetilleggFBGjeldende: Expression<Kroner>
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {

            showIf(
                justeringsbeloepAar_barnetilleggFBGjeldende.equalTo(0)
            ) {
                paragraph {
                    textExpr(
                        Bokmal to "Du får ikke utbetalt barnetillegget for ".expr() +
                                ifElse(harFlereBarn, "barna", "barnet") +
                                " som bor med begge sine foreldre fordi samlet inntekt er over grensen for å få utbetalt barnetillegg. Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året.".expr(),
                        Nynorsk to "Du får ikkje utbetalt barnetillegget for ".expr() +
                                ifElse(harFlereBarn, "barna", "barnet") +
                                " som bur saman med begge foreldra sine fordi samla inntekt er over grensa for å få utbetalt barnetillegg. Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året. ".expr(),
                        English to "You will not receive a child supplement for the ".expr() +
                                ifElse(harFlereBarn, "children who live", "child who lives") +
                                " together with both parents because your income is over the income limit for receiving a child supplement. You have already received what you are entitled to this year, therefore you will not receive any child supplement for the remainder of the year.".expr()
                    )
                }
            }
            showIf(
                justeringsbeloepAar_barnetilleggFBGjeldende.notEqualTo(0) and beloep_barnetilleggFBGjeldende.equalTo(0)
            ) {
                paragraph {
                    text(
                        Bokmal to "Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året.",
                        Nynorsk to "Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året.",
                        English to "You have already received what you are entitled to this year, therefore you will not receive any child supplement for the remainder of the year."
                    )
                }
            }
        }
    }


    // TBU069V - Fellesbarn
    data class FastsetterStoerelsenPaaBTFellesbarn(
        val harAnvendtTrygdetidUnder40: Expression<Boolean>,
        val harYrkesskade: Expression<Boolean>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    Bokmal to "Vi fastsetter størrelsen på barnetillegget ut fra den samlede inntekten til begge foreldrene.",
                    Nynorsk to "Vi fastset storleiken på barnetillegget ut frå den samla inntekta til begge foreldra.",
                    English to "We determine the amount of child supplement based on the total income of both parents."
                )
            }
            paragraph {
                textExpr(
                    Bokmal to "Barnetillegget blir redusert dersom den samlede inntekten er høyere enn fribeløpet. Fribeløpet for et barn er 4,6 ganger folketrygdens grunnbeløp og det øker med 40 prosent av folketrygdens grunnbeløp for hvert ekstra barn.".expr(),
                    Nynorsk to "Barnetillegget blir redusert dersom den samla inntekta di er høgare enn fribeløpet. Fribeløpet for eit barn er 4,6 gonger grunnbeløpet i folketrygda og det aukar med 40 prosent av grunnbeløpet for kvart ekstra barn.".expr(),
                    English to "The child supplement will be reduced if your total income is greater than the exemption amount. The exemption amount is 4.6 times the National Insurance basic amount and it increases with 40 percent of the National Insurance basic amount for each extra child.".expr()
                )
                showIf(harAnvendtTrygdetidUnder40 and not(harYrkesskade)) {
                    text(
                        Bokmal to " Siden trygdetiden din er kortere enn 40 år, blir fribeløpet redusert ut fra den trygdetiden du har. ",
                        Nynorsk to " Sidan trygdetida di er kortare enn 40 år, blir fribeløpet redusert ut frå den trygdetida du har. ",
                        English to " Since you have less than 40 years National Insurance membership, the exemption amount is ruduced correspondingly in relation to the the length of time of you National Insurance membership. "
                    )
                }
            }
            paragraph {
                text(
                    Bokmal to "Dersom begge foreldrene mottar uføretrygd blir barnetillegget gitt til den som har rett til det høyeste tillegget. Dette gjelder også dersom den ene forelderen mottar alderspensjon.",
                    Nynorsk to "Dersom begge foreldra får uføretrygd, blir barnetillegget gitt til den som har rett til det høgaste tillegget. Dette gjeld også dersom den eine forelderen får alderspensjon.",
                    English to "If both parents recieve disbaility benefit, child supplement will be paid to the parent with the highest disability benefit. This applies also if one of the parents receives retirement pension."
                )
            }
        }
    }

    // TBU069V - Saerkullsbarn
    data class FastsetterStoerelsenPaaBTSaerkullsbarn(
        val harAnvendtTrygdetidUnder40: Expression<Boolean>,
        val harYrkesskade: Expression<Boolean>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    Bokmal to "Vi fastsetter størrelsen på barnetillegget ut fra inntekten din. Inntekt til en ektefelle/partner/samboer som ikke er forelder til barnet, har ikke betydning for størrelsen på barnetillegget.",
                    Nynorsk to "Vi fastset storleiken på barnetillegget ut frå inntekta di. Inntekt til ein ektefelle/partnar/sambuar som ikkje er forelder til barnet, har ikkje betydning for storleiken på barnetillegget.",
                    English to "We determine the amount of child supplement based on your income. The income of a spouse/partner/cohabitant who is not the child's parent, is not taken into consideration."
                )
            }
            paragraph {
                textExpr(
                    Bokmal to "Barnetillegget blir redusert dersom den samlede inntekten din er høyere enn fribeløpet. Fribeløpet for et barn er 3,1 ganger folketrygdens grunnbeløp og det øker med 40 prosent av folketrygdens grunnbeløp for hvert ekstra barn.".expr(),
                    Nynorsk to "Barnetillegget blir redusert dersom den samla inntekta di er høgare enn fribeløpet. Fribeløpet for eit barn er 3,1 gonger grunnbeløpet i folketrygda og det aukar med 40 prosent av grunnbeløpet for kvart ekstra barn.".expr(),
                    English to "The child supplement will be reduced if your total income is greater than the exemption amount. The exemption amount is 3.1 times the National Insurance basic amount and it increases with 40 percent of the National Insurance basic amount for each extra child.".expr()
                )
            }
            showIf(harAnvendtTrygdetidUnder40 and not(harYrkesskade)) {
                text(
                    Bokmal to "Siden trygdetiden din er kortere enn 40 år, blir fribeløpet redusert ut fra den trygdetiden du har. ",
                    Nynorsk to "Sidan trygdetida di er kortare enn 40 år, blir fribeløpet redusert ut frå den trygdetida du har. ",
                    English to "Since you have less than 40 years National Insurance membership, the exemption amount is ruduced correspondingly in relation to the the length of time of you National Insurance membership. "
                )
            }
            paragraph {
                text(
                    Bokmal to "Dersom begge foreldrene mottar uføretrygd blir barnetillegget gitt til den som har den daglige omsorgen for barnet. Dette gjelder også dersom den ene forelderen mottar alderspensjon. Har foreldrene delt omsorg for barnet, blir barnetillegget gitt til den forelderen som bor på samme folkeregistrete adresse som barnet. ",
                    Nynorsk to "Dersom begge foreldra får uføretrygd, blir barnetillegget gitt til den som har den daglege omsorga for barnet. Dette gjeld også dersom den eine forelderen får alderspensjon. Har foreldra delt omsorg for barnet, blir barnetillegget gitt til den forelderen som bur på same folkeregistrerte adresse som barnet.",
                    English to "If both parents recieve disbaility benefit, child supplement will be paid to the parent with guardianship of the child. This applies also if one of the parents receives retirement pension. If both parents have guardianship of the child, child supplement is given to the parent who lives at the same registered adresse as the child."
                )
            }
        }
    }

    // TBU069V - Fellesbarn OG Saerkullsbarn
    data class FastsetterStoerelsenPaaBTFellesbarnOgSaerkullsbarn(
        val harAnvendtTrygdetidUnder40: Expression<Boolean>,
        val harTilleggForFlereFellesbarn: Expression<Boolean>,
        val harTilleggForFlereSaerkullsbarn: Expression<Boolean>,
        val harYrkesskade: Expression<Boolean>,
        val sivilstand: Expression<Sivilstand>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                textExpr(
                    Bokmal to "Vi fastsetter størrelsen på barnetillegget ut fra inntekten til deg og din ".expr(),
                    Nynorsk to "Vi fastset storleiken på barnetillegget ut frå inntekta til deg og din ".expr(),
                    English to "We determine the amount of child supplement based on the total income for you and your ".expr()
                )

                includePhrase(Felles.SivilstandEPSUbestemtForm(sivilstand))

                textExpr(
                    Bokmal to " for ".expr() +
                            ifElse(harTilleggForFlereFellesbarn, "barna", "barnet")
                            + " som bor med begge sine foreldre. Barnetillegget blir redusert dersom den samlede inntekten er høyere enn fribeløpet. Fribeløpet for et barn som bor med begge foreldrene er 4,6 ganger folketrygdens grunnbeløp, og øker med 40 prosent av folketrygdens grunnbeløp for hvert ekstra barn.".expr(),
                    Nynorsk to " for ".expr() +
                            ifElse(harTilleggForFlereFellesbarn, "barna", "barnet")
                            + " som bur med begge foreldra sine. Barnetillegget blir redusert dersom den samla inntekta er høgare enn fribeløpet. Fribeløpet for eit barn som bur med begge foreldra, er 4,6 gonger grunnbeløpet i folketrygda, og aukar med 40 prosent av grunnbeløpet i folketrygda for kvart ekstra barn.".expr(),
                    English to " for the ".expr() +
                            ifElse(harTilleggForFlereFellesbarn, "children who live", "child who lives")
                            + " with both parents. The child supplement will be reduced if your total income is greater than the exemption amount. The exemption amount is 4.6 times the National Insurance basic amount and it increases with 40 percent of the National Insurance basic amount for each extra child.".expr()
                )
            }
            paragraph {
                textExpr(
                    Bokmal to "For ".expr() +
                            ifElse(harTilleggForFlereSaerkullsbarn, "barna", "barnet")
                            + " som ikke bor sammen med begge foreldre, fastsetter vi størrelsen på barnetillegget ut fra inntekten din. Inntekt til en ektefelle/partner/samboer som ikke er forelder til barnet, har ikke betydning for størrelsen på barnetillegget. Barnetillegget blir redusert dersom den samlede inntekten din er høyere enn fribeløpet. Fribeløpet for et barn som ikke bor sammen med begge foreldrene er 3,1 ganger folketrygdens grunnbeløp, og øker med 40 prosent av folketrygdens grunnbeløp for hvert ekstra barn.".expr(),
                    Nynorsk to "For ".expr() +
                            ifElse(harTilleggForFlereSaerkullsbarn, "barna", "barnet")
                            + " som ikkje bur saman med begge foreldra, fastset vi storleiken på barnetillegget ut frå inntekta di. Inntekt til ein ektefelle/partnar/sambuar som ikkje er forelder til barnet, har ikkje betydning for storleiken på barnetillegget. Barnetillegget blir redusert dersom den samla inntekta di er høgare enn fribeløpet. Fribeløpet for eit barn som ikkje bur saman med begge foreldra, er 3,1 gonger grunnbeløpet i folketrygda, og aukar med 40 prosent av grunnbeløpet i folketrygda for kvart ekstra barn.".expr(),
                    English to "For the ".expr() +
                            ifElse(harTilleggForFlereSaerkullsbarn, "children who do", "child who does")
                            + " not live together with both parents, the amount of child supplement is based on your income. The income of a spouse/partner/cohabitant who is not the child's parent, is not taken into consideration. The child supplement will be reduced if your total income is greater than the exemption amount. The exemption amount is 3.1 times the National Insurance basic amount and it increases with 40 percent of the National Insurance basic amount for each extra child.".expr(),
                )
            }
            showIf(harAnvendtTrygdetidUnder40 and not(harYrkesskade)) {
                paragraph {
                    text(
                        Bokmal to " Siden trygdetiden din er kortere enn 40 år, blir fribeløpet redusert ut fra den trygdetiden du har.",
                        Nynorsk to " Sidan trygdetida di er kortare enn 40 år, blir fribeløpet redusert ut frå den trygdetida du har.",
                        English to " Since you have less than 40 years National Insurance membership, the exemption amount is ruduced correspondingly in relation to how long you have had National Insurance membership."
                    )
                }
            }
        }
    }

    // TBU605V
    data class PeriodisertInntektInnledning(
        val sivilstand: Expression<Sivilstand>,
        val kravAarsak: Expression<KravAarsak>,
        val harFlereTillegg: Expression<Boolean>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                val kravAarsakErEndretInntekt = kravAarsak.isOneOf(
                    KravAarsak.ENDRET_INNTEKT, KravAarsak.EPS_ENDRET_INNTEKT, KravAarsak.BEGGE_FOR_END_IN
                )
                val foreldreBorSammen =
                    sivilstand.isOneOf(
                        Sivilstand.GIFT,
                        Sivilstand.PARTNER,
                        Sivilstand.SAMBOER1_5,
                        Sivilstand.SAMBOER3_2
                    )
                textExpr(
                    Bokmal to ifElse(
                        kravAarsakErEndretInntekt,
                        "Når inntekten ",
                        "Har det vært en endring i inntekten ",
                    ),
                    Nynorsk to ifElse(
                        kravAarsakErEndretInntekt,
                        "Når inntekta ",
                        "Har det vore ie endring i inntekta ",
                    ),
                    English to ifElse(kravAarsakErEndretInntekt, "When ", "If ")
                )
                textExpr(
                    Bokmal to ifElse(foreldreBorSammen, "din eller til din", "din"),
                    Nynorsk to ifElse(foreldreBorSammen, "di eller til di", "di"),
                    English to ifElse(foreldreBorSammen, "your or your", "your"),
                )

                showIf(foreldreBorSammen) {
                    includePhrase(Felles.SivilstandEPSUbestemtForm(sivilstand))
                    text(Bokmal to "", Nynorsk to "", English to "'s")
                }

                textExpr(
                    Bokmal to " endrer seg, blir reduksjonen av ".expr() +
                            ifElse(harFlereTillegg, "barnetilleggene", "barnetilleggene") +
                            " vurdert på nytt.",
                    Nynorsk to " endrar seg, blir reduksjonen av ".expr() +
                            ifElse(harFlereTillegg, "barnetilleggene", "barnetilleggene") +
                            " vurdert på nytt.",
                    English to " income has been changed, your child supplement will be recalculated.".expr(),
                )
            }
        }
    }

    data class PeriodisertInntektOverFribeloepFlereTillegg(
        val avkortningsbeloepAarFelles: Expression<Kroner>,
        val avkortningsbeloepAarSaerkull: Expression<Kroner>,
        val harJusteringsbeloep: Expression<Boolean>,
        val inntektEllerFribeloepErPeriodisert: Expression<Boolean>,
        val harTilleggForFlereFellesbarn: Expression<Boolean>
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                textExpr(
                    Bokmal to "50 prosent av den inntekten som overstiger fribeløpet for ".expr()
                            + ifElse(harTilleggForFlereFellesbarn, "barna", "barnet") +
                            " som bor med begge sine foreldre ".expr() +
                            ifElse(
                                inntektEllerFribeloepErPeriodisert,
                                "blir omregnet til et årlig beløp som tilsvarer",
                                "er",
                            ),
                    Nynorsk to "50 prosent av inntekta som overstig fribeløpet for ".expr()
                            + ifElse(harTilleggForFlereFellesbarn, "barna", "barnet") +
                            " som bur med begge foreldra sine ".expr() +
                            ifElse(
                                inntektEllerFribeloepErPeriodisert,
                                "blir rekna om til et årleg beløp som svarer til",
                                "er",
                            ),
                    English to "50 percent of income that exceeds the exemption amount ".expr() +
                            ifElse(
                                inntektEllerFribeloepErPeriodisert,
                                "is recalculated to an annual amount of",
                                "is",
                            ),
                )

                showIf(avkortningsbeloepAarFelles.greaterThan(0)) {
                    textExpr(
                        Bokmal to " ".expr() + avkortningsbeloepAarFelles.format() + " kroner.",
                        Nynorsk to " ".expr() + avkortningsbeloepAarFelles.format() + " kroner.",
                        English to " NOK ".expr() + avkortningsbeloepAarFelles.format() + ".",
                    )
                }.orShow {
                    textExpr(
                        Bokmal to " ".expr() + avkortningsbeloepAarSaerkull.format() + " kroner.",
                        Nynorsk to " ".expr() + avkortningsbeloepAarSaerkull.format() + " kroner.",
                        English to " NOK ".expr() + avkortningsbeloepAarSaerkull.format() + ".",
                    )
                }
                showIf(not(harJusteringsbeloep)) {
                    text(
                        Bokmal to " Dette beløpet bruker vi til å redusere dette barnetillegget for hele året.",
                        Nynorsk to " Dette beløpet bruker vi til å redusere dette barnetillegget for heile året.",
                        English to " This amount will be used to reduce your child supplement during the calendar year."
                    )
                }
            }
        }
    }

    data class PeriodisertInntektOverFribeloepEttTillegg(
        val avkortningsbeloepAarFelles: Expression<Kroner>,
        val avkortningsbeloepAarSaerkull: Expression<Kroner>,
        val harJusteringsbeloep: Expression<Boolean>,
        val inntektEllerFribeloepErPeriodisert: Expression<Boolean>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                textExpr(
                    Bokmal to "50 prosent av den inntekten som overstiger fribeløpet ".expr() +
                            ifElse(
                                inntektEllerFribeloepErPeriodisert,
                                "blir omregnet til et årlig beløp som tilsvarer",
                                "er",
                            ),
                    Nynorsk to "50 prosent av inntekta som overstig fribeløpet ".expr() +
                            ifElse(
                                inntektEllerFribeloepErPeriodisert,
                                "blir rekna om til et årleg beløp som svarer til",
                                "er",
                            ),
                    English to "50 percent of income that exceeds the exemption amount ".expr() +
                            ifElse(
                                inntektEllerFribeloepErPeriodisert,
                                "is recalculated to an annual amount of",
                                "is",
                            ),
                )

                showIf(avkortningsbeloepAarFelles.greaterThan(0)) {
                    textExpr(
                        Bokmal to " ".expr() + avkortningsbeloepAarFelles.format() + " kroner.",
                        Nynorsk to " ".expr() + avkortningsbeloepAarFelles.format() + " kroner.",
                        English to " NOK ".expr() + avkortningsbeloepAarFelles.format() + ".",
                    )
                }.orShow {
                    textExpr(
                        Bokmal to " ".expr() + avkortningsbeloepAarSaerkull.format() + " kroner.",
                        Nynorsk to " ".expr() + avkortningsbeloepAarSaerkull.format() + " kroner.",
                        English to " NOK ".expr() + avkortningsbeloepAarSaerkull.format() + ".",
                    )
                }
                showIf(not(harJusteringsbeloep)) {
                    text(
                        Bokmal to " Dette beløpet bruker vi til å redusere barnetillegget ditt for hele året.",
                        Nynorsk to " Dette beløpet bruker vi til å redusere barnetillegget ditt for heile året.",
                        English to " This amount will be used to reduce your child supplement during the calendar year."
                    )
                }
            }
        }
    }
}