package no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.BorMedSivilstand
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.BarnetilleggGjeldendeSelectors.FellesbarnSelectors.avkortningsbeloepAar
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.BarnetilleggGjeldendeSelectors.FellesbarnSelectors.beloepAarBrutto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.BarnetilleggGjeldendeSelectors.FellesbarnSelectors.beloepAarNetto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.BarnetilleggGjeldendeSelectors.FellesbarnSelectors.beloepBrutto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.BarnetilleggGjeldendeSelectors.FellesbarnSelectors.beloepNetto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.BarnetilleggGjeldendeSelectors.FellesbarnSelectors.beloepNetto_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.BarnetilleggGjeldendeSelectors.FellesbarnSelectors.borMedSivilstand
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.BarnetilleggGjeldendeSelectors.FellesbarnSelectors.erRedusertMotinntekt
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.BarnetilleggGjeldendeSelectors.FellesbarnSelectors.fribeloep
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.BarnetilleggGjeldendeSelectors.FellesbarnSelectors.fribeloepErPeriodisert
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.BarnetilleggGjeldendeSelectors.FellesbarnSelectors.harFlereBarn
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.BarnetilleggGjeldendeSelectors.FellesbarnSelectors.inntektErPeriodisert
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.BarnetilleggGjeldendeSelectors.FellesbarnSelectors.inntektOverFribeloep
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.BarnetilleggGjeldendeSelectors.FellesbarnSelectors.inntektstak
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.BarnetilleggGjeldendeSelectors.FellesbarnSelectors.justeringsbeloepAar
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.BarnetilleggGjeldendeSelectors.FellesbarnSelectors.justeringsbeloepAar_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.BarnetilleggGjeldendeSelectors.FellesbarnSelectors.samletInntektBruktIAvkortning
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.BarnetilleggGjeldendeSelectors.SaerkullsbarnSelectors.avkortningsbeloepAar
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.BarnetilleggGjeldendeSelectors.SaerkullsbarnSelectors.beloepAarBrutto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.BarnetilleggGjeldendeSelectors.SaerkullsbarnSelectors.beloepAarNetto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.BarnetilleggGjeldendeSelectors.SaerkullsbarnSelectors.beloepBrutto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.BarnetilleggGjeldendeSelectors.SaerkullsbarnSelectors.beloepNetto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.BarnetilleggGjeldendeSelectors.SaerkullsbarnSelectors.beloepNetto_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.BarnetilleggGjeldendeSelectors.SaerkullsbarnSelectors.erRedusertMotinntekt
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.BarnetilleggGjeldendeSelectors.SaerkullsbarnSelectors.fribeloep
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.BarnetilleggGjeldendeSelectors.SaerkullsbarnSelectors.fribeloepErPeriodisert
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.BarnetilleggGjeldendeSelectors.SaerkullsbarnSelectors.harFlereBarn
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.BarnetilleggGjeldendeSelectors.SaerkullsbarnSelectors.inntektBruktIAvkortning
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.BarnetilleggGjeldendeSelectors.SaerkullsbarnSelectors.inntektErPeriodisert
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.BarnetilleggGjeldendeSelectors.SaerkullsbarnSelectors.inntektOverFribeloep
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.BarnetilleggGjeldendeSelectors.SaerkullsbarnSelectors.inntektstak
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.BarnetilleggGjeldendeSelectors.SaerkullsbarnSelectors.justeringsbeloepAar
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.BarnetilleggGjeldendeSelectors.SaerkullsbarnSelectors.justeringsbeloepAar_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.BarnetilleggGjeldendeSelectors.fellesbarn_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.BarnetilleggGjeldendeSelectors.foedselsdatoPaaBarnTilleggetGjelder
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.BarnetilleggGjeldendeSelectors.saerkullsbarn_safe
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.common.KronerText
import no.nav.pensjon.brev.model.bestemtForm
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.model.ubestemtForm
import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LocalizedFormatter
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.absoluteValue
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.ifElse
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.expression.lessThan
import no.nav.pensjon.brev.template.dsl.expression.map
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.notEqualTo
import no.nav.pensjon.brev.template.dsl.expression.notNull
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.expression.size
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

data class OpplysningerOmBarnetillegg(
    val barnetillegg: Expression<OpplysningerBruktIBeregningUTDto.BarnetilleggGjeldende>,
    val anvendtTrygdetid: Expression<Int>,
    val harYrkesskade: Expression<Boolean>,
    val harKravaarsakEndringInntekt: Expression<Boolean>,
    val fraOgMedDatoErNesteAar: Expression<Boolean>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        val harAnvendtTrygdetidUnder40 = anvendtTrygdetid.lessThan(40)
        val harInnvilgetBarnetilleggFellesbarn = barnetillegg.fellesbarn_safe.notNull()
        val harInnvilgetBarnetilleggSaerkullsbarn = barnetillegg.saerkullsbarn_safe.notNull()

        val justeringsbeloepFelles = barnetillegg.fellesbarn_safe.justeringsbeloepAar_safe.ifNull(Kroner(0))
        val justeringsbeloepSaerkull = barnetillegg.saerkullsbarn_safe.justeringsbeloepAar_safe.ifNull(Kroner(0))
        val harJusteringsbeloepFellesBarn = justeringsbeloepFelles.notEqualTo(0)
        val harJusteringsbeloepSaerkullsbarn = justeringsbeloepSaerkull.notEqualTo(0)
        val harJusteringsbeloep = harJusteringsbeloepFellesBarn or harJusteringsbeloepSaerkullsbarn

        val faarUtbetaltBarnetilleggFellesBarn =
            barnetillegg.fellesbarn_safe.beloepNetto_safe.ifNull(Kroner(0)).greaterThan(0)
        val faarUtbetaltbarnetilleggSaerkullsbarn =
            barnetillegg.saerkullsbarn_safe.beloepNetto_safe.ifNull(Kroner(0)).greaterThan(0)
        val faarUtbetaltBarnetillegg = faarUtbetaltBarnetilleggFellesBarn or faarUtbetaltbarnetilleggSaerkullsbarn


        includePhrase(
            ForDegSomHarRettTilBarnetillegg(
                harAnvendtTrygdetidUnder40 = harAnvendtTrygdetidUnder40,
                harYrkesskade = harYrkesskade,
                foedselsdatoPaaBarnTilleggetGjelder = barnetillegg.foedselsdatoPaaBarnTilleggetGjelder,
            )
        )

        title1 {
            text(
                Language.Bokmal to "Slik beregner vi størrelsen på barnetillegget",
                Language.Nynorsk to "Slik reknar vi ut storleiken på barnetillegget",
                Language.English to "How we calculate the amount of child supplement"
            )
        }

        includePhrase(VedleggBeregnUTInnlednBT)

        ifNotNull(barnetillegg.fellesbarn_safe, barnetillegg.saerkullsbarn_safe) { felles, saerkull ->
            includePhrase(
                FastsetterStoerelsenPaaBTFellesbarnOgSaerkullsbarn(
                    harAnvendtTrygdetidUnder40 = harAnvendtTrygdetidUnder40,
                    harTilleggForFlereFellesbarn = felles.harFlereBarn,
                    harTilleggForFlereSaerkullsbarn = saerkull.harFlereBarn,
                    borMedSivilstand = felles.borMedSivilstand,
                    harYrkesskade = harYrkesskade,
                )
            )
        }.orShowIf(harInnvilgetBarnetilleggFellesbarn) {
            includePhrase(
                FastsetterStoerelsenPaaBTFellesbarn(
                    harAnvendtTrygdetidUnder40 = harAnvendtTrygdetidUnder40,
                    harYrkesskade = harYrkesskade,
                )
            )
        }.orShowIf(harInnvilgetBarnetilleggSaerkullsbarn) {
            includePhrase(
                FastsetterStoerelsenPaaBTSaerkullsbarn(
                    harAnvendtTrygdetidUnder40 = harAnvendtTrygdetidUnder40,
                    harYrkesskade = harYrkesskade,
                )
            )
        }

        showIf(harJusteringsbeloep or faarUtbetaltBarnetillegg) {
            ifNotNull(barnetillegg.fellesbarn_safe, barnetillegg.saerkullsbarn_safe) { fellesTillegg, saerkullTillegg ->
                includePhrase(
                    EndringInntektFlereTillegg(
                        fellesTillegg = fellesTillegg,
                        saerkullTillegg = saerkullTillegg,
                        harKravaarsakEndringInntekt = harKravaarsakEndringInntekt,
                    )
                )
            }.orIfNotNull(barnetillegg.fellesbarn_safe) { fellesTillegg ->
                includePhrase(
                    EndringInntektEttVedlegg(
                        harKravaarsakEndringInntekt = harKravaarsakEndringInntekt,
                        beloepNetto = fellesTillegg.beloepNetto,
                        beloepBrutto = fellesTillegg.beloepBrutto,
                        fribeloepEllerInntektErPeriodisert = fellesTillegg.fribeloepErPeriodisert or fellesTillegg.inntektErPeriodisert,
                        avkortningsbeloepAar = fellesTillegg.avkortningsbeloepAar,
                        justeringsbeloep = fellesTillegg.justeringsbeloepAar,
                        borMedSivilstandFelles = fellesTillegg.borMedSivilstand,
                    ))
            }.orIfNotNull(barnetillegg.saerkullsbarn_safe){ saerkullsTillegg->
                includePhrase(
                    EndringInntektEttVedlegg(
                        harKravaarsakEndringInntekt = harKravaarsakEndringInntekt,
                        beloepNetto = saerkullsTillegg.beloepNetto,
                        beloepBrutto = saerkullsTillegg.beloepBrutto,
                        fribeloepEllerInntektErPeriodisert = saerkullsTillegg.fribeloepErPeriodisert or saerkullsTillegg.inntektErPeriodisert,
                        avkortningsbeloepAar = saerkullsTillegg.avkortningsbeloepAar,
                        justeringsbeloep = saerkullsTillegg.justeringsbeloepAar,
                        borMedSivilstandFelles = null.expr(),
                    )
                )

            }
        }

        ifNotNull(barnetillegg.fellesbarn_safe) { fellesTillegg ->
            showIf(fellesTillegg.erRedusertMotinntekt) {
                title1 {
                    textExpr(
                        Language.Bokmal to "Reduksjon av barnetillegg for fellesbarn før skatt ".expr()
                                + ifElse(fraOgMedDatoErNesteAar, "for neste år", "i år"),
                        Language.Nynorsk to "Reduksjon av barnetillegg for fellesbarn før skatt ".expr()
                                + ifElse(fraOgMedDatoErNesteAar, "for neste år", "i år"),
                        Language.English to "Reduction of child supplement payment for joint children before tax, ".expr()
                                + ifElse(fraOgMedDatoErNesteAar, "for next year", "for this year"),
                    )
                }

                showIf(faarUtbetaltBarnetilleggFellesBarn or harJusteringsbeloepFellesBarn) {
                    includePhrase(
                        OpplysningerOmBarnetilleggTabell(
                            avkortningsbeloepAar = fellesTillegg.avkortningsbeloepAar,
                            beloepAarBrutto = fellesTillegg.beloepAarBrutto,
                            beloepAarNetto = fellesTillegg.beloepAarNetto,
                            erRedusertMotinntekt = fellesTillegg.erRedusertMotinntekt,
                            fribeloep = fellesTillegg.fribeloep,
                            inntektBruktIAvkortning = fellesTillegg.samletInntektBruktIAvkortning,
                            inntektOverFribeloep = fellesTillegg.inntektOverFribeloep,
                            inntektstak = fellesTillegg.inntektstak,
                            justeringsbeloepAar = fellesTillegg.justeringsbeloepAar,
                            beloepNetto = fellesTillegg.beloepNetto,
                            beloepBrutto = fellesTillegg.beloepBrutto,
                            fribeloepErPeriodisert = fellesTillegg.fribeloepErPeriodisert,
                        )
                    )
                }.orShow {
                    includePhrase(InntektsGrenseBarnetillegg(
                        inntektstak = fellesTillegg.inntektstak,
                        samletInntektBruktIAvkortning = fellesTillegg.samletInntektBruktIAvkortning
                    ))
                }
                showIf(faarUtbetaltBarnetilleggFellesBarn) {
                    includePhrase(
                        MaanedligTilleggFellesbarn(
                            barnetilleggFellesbarn = fellesTillegg.beloepNetto,
                            harFlereBarn = fellesTillegg.harFlereBarn,
                            harInnvilgetBarnetilleggSaerkullsbarn = harInnvilgetBarnetilleggSaerkullsbarn
                        )
                    )
                }.orShow {
                    includePhrase(
                        FaarIkkeUtbetaltTilleggFellesbarn(
                            nettoBeloepFellesbarn = fellesTillegg.beloepNetto,
                            harJusteringsbeloep = harJusteringsbeloepFellesBarn,
                            harFlereBarn = fellesTillegg.harFlereBarn,
                            harInnvilgetBarnetilleggSaerkullsbarn = harInnvilgetBarnetilleggSaerkullsbarn
                        )
                    )
                }
            }
        }

        ifNotNull(barnetillegg.saerkullsbarn_safe) { saerkullTillegg ->
            showIf(saerkullTillegg.erRedusertMotinntekt) {
                title1 {
                    textExpr(
                        Language.Bokmal to "Reduksjon av barnetillegg for særkullsbarn før skatt ".expr()
                                + ifElse(fraOgMedDatoErNesteAar, "for neste år", "i år"),
                        Language.Nynorsk to "Reduksjon av barnetillegg for særkullsbarn før skatt ".expr()
                                + ifElse(fraOgMedDatoErNesteAar, "for neste år", "i år"),
                        Language.English to "Reduction of child supplement payment for children from a previous relationship before tax, ".expr()
                                + ifElse(fraOgMedDatoErNesteAar, "for next year", "for this year"),
                    )
                }

                showIf(faarUtbetaltbarnetilleggSaerkullsbarn or harJusteringsbeloepSaerkullsbarn){
                    includePhrase(
                        OpplysningerOmBarnetilleggTabell(
                            avkortningsbeloepAar = saerkullTillegg.avkortningsbeloepAar,
                            beloepAarBrutto = saerkullTillegg.beloepAarBrutto,
                            beloepAarNetto = saerkullTillegg.beloepAarNetto,
                            erRedusertMotinntekt = saerkullTillegg.erRedusertMotinntekt,
                            fribeloep = saerkullTillegg.fribeloep,
                            inntektBruktIAvkortning = saerkullTillegg.inntektBruktIAvkortning,
                            inntektOverFribeloep = saerkullTillegg.inntektOverFribeloep,
                            inntektstak = saerkullTillegg.inntektstak,
                            justeringsbeloepAar = saerkullTillegg.justeringsbeloepAar,
                            beloepNetto = saerkullTillegg.beloepNetto,
                            beloepBrutto = saerkullTillegg.beloepBrutto,
                            fribeloepErPeriodisert = saerkullTillegg.fribeloepErPeriodisert,
                        )
                    )
                }.orShow {
                    includePhrase(InntektsGrenseBarnetillegg(
                        inntektstak = saerkullTillegg.inntektstak,
                        samletInntektBruktIAvkortning = saerkullTillegg.inntektBruktIAvkortning
                    ))
                }


                showIf(faarUtbetaltbarnetilleggSaerkullsbarn) {
                    includePhrase(
                        MaanedligTilleggSaerkullsbarn(
                            saerkullTilleggNetto = saerkullTillegg.beloepNetto,
                            harFlereSaerkullsbarn = saerkullTillegg.harFlereBarn,
                            harInnvilgetBarnetilleggFellesbarn = harInnvilgetBarnetilleggFellesbarn,
                        )
                    )
                }.orShow {
                    includePhrase(
                        FaarIkkeUtbetaltTilleggSaerkullsbarn(
                            harJusteringsbeloep = harJusteringsbeloepSaerkullsbarn,
                            harInnvilgetBarnetilleggFellesbarn = harInnvilgetBarnetilleggFellesbarn,
                            harFlereSaerkullsbarn = saerkullTillegg.harFlereBarn,
                        )
                    )
                }
            }
        }
    }


    //TBU 613V
    data class EndringInntektFlereTillegg(
        val fellesTillegg: Expression<OpplysningerBruktIBeregningUTDto.BarnetilleggGjeldende.Fellesbarn>,
        val saerkullTillegg: Expression<OpplysningerBruktIBeregningUTDto.BarnetilleggGjeldende.Saerkullsbarn>,
        val harKravaarsakEndringInntekt: Expression<Boolean>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>(){
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {

            includePhrase(
                PeriodisertInntektInnledning(
                    harKravaarsakEndringInntekt = harKravaarsakEndringInntekt,
                    harFlereTillegg = true.expr(),
                    borMedSivilstandFelles = fellesTillegg.borMedSivilstand
                )
            )
            showIf(fellesTillegg.beloepNetto.notEqualTo(fellesTillegg.beloepBrutto)) {
                includePhrase(
                    PeriodisertInntektOverFribeloepFellesTillegg(
                        inntektEllerFribeloepErPeriodisert = fellesTillegg.inntektErPeriodisert or fellesTillegg.fribeloepErPeriodisert,
                        avkortningsbeloepAar = fellesTillegg.avkortningsbeloepAar,
                        harJusteringsbeloep = fellesTillegg.justeringsbeloepAar.notEqualTo(0),
                        harTilleggForFlereFellesbarn = fellesTillegg.harFlereBarn,
                    )
                )
            }

            includePhrase(JusteringBarnetillegg(fellesTillegg.justeringsbeloepAar))

            showIf(saerkullTillegg.erRedusertMotinntekt) {
                includePhrase(
                    PeriodisertInntektSaerkullsbarn(
                        avkortningsbeloepAar = saerkullTillegg.avkortningsbeloepAar,
                        fribeloepEllerInntektErPeriodisert = saerkullTillegg.fribeloepErPeriodisert or saerkullTillegg.inntektErPeriodisert,
                        harFlereSaerkullsbarn = saerkullTillegg.harFlereBarn,
                        harJusteringsbeloep = saerkullTillegg.justeringsbeloepAar.notEqualTo(0),
                    )
                )
                includePhrase(JusteringBarnetillegg(saerkullTillegg.justeringsbeloepAar))
            }
        }
    }

    //TBU605V (kun ett tillegg)
    data class EndringInntektEttVedlegg(
        val harKravaarsakEndringInntekt: Expression<Boolean>,
        val beloepNetto: Expression<Kroner>,
        val beloepBrutto: Expression<Kroner>,
        val fribeloepEllerInntektErPeriodisert: Expression<Boolean>,
        val avkortningsbeloepAar: Expression<Kroner>,
        val justeringsbeloep: Expression<Kroner>,
        val borMedSivilstandFelles: Expression<BorMedSivilstand?>

    ): OutlinePhrase<LangBokmalNynorskEnglish>(){
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            val harJusteringsbeloep = justeringsbeloep.notEqualTo(0)
            includePhrase(
                PeriodisertInntektInnledning(
                    harKravaarsakEndringInntekt = harKravaarsakEndringInntekt,
                    harFlereTillegg = false.expr(),
                    borMedSivilstandFelles = borMedSivilstandFelles,
                )
            )

            showIf(beloepNetto.notEqualTo(beloepBrutto)) {
                includePhrase(
                    PeriodisertInntektOverFribeloepEttTillegg(
                        inntektEllerFribeloepErPeriodisert = fribeloepEllerInntektErPeriodisert,
                        avkortningsbeloepAar = avkortningsbeloepAar,
                        harJusteringsbeloep = harJusteringsbeloep,
                    )
                )
            }

            showIf(harJusteringsbeloep) {
                includePhrase(JusteringBarnetillegg(justeringsbeloep))
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
        val beloepBrutto: Expression<Kroner>,
        val fribeloepErPeriodisert: Expression<Boolean>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            val faarUtbetaltTillegg = beloepNetto.greaterThan(0)
            val harJusteringsbeloep = justeringsbeloepAar.notEqualTo(0)
            val harAvkortningsbeloep = avkortningsbeloepAar.greaterThan(0)
            paragraph {
                table(
                    header = {
                        column(columnSpan = 2) {
                        }
                        column(alignment = Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT) {
                            text(
                                Language.Bokmal to "Beløp",
                                Language.Nynorsk to "Beløp",
                                Language.English to "Amount",
                            )
                        }
                    }
                ) {
                    row {
                        cell {
                            text(
                                Language.Bokmal to "Årlig barnetillegg før reduksjon ut fra inntekt",
                                Language.Nynorsk to "Årleg barnetillegg før reduksjon ut frå inntekt",
                                Language.English to "Yearly child supplement before income reduction"
                            )
                        }
                        cell {
                            includePhrase(KronerText(beloepAarBrutto))
                        }
                    }

                    showIf(harAvkortningsbeloep) {
                        row {
                            cell {
                                text(
                                    Language.Bokmal to "- 50 prosent av inntekt som overstiger fribeløpet *",
                                    Language.Nynorsk to "- 50 prosent av inntekta som overstig fribeløpet *",
                                    Language.English to "- 50 percent of income exceeding the allowance amount *"
                                )

                                showIf(fribeloepErPeriodisert) {
                                    text(
                                        Language.Bokmal to " (oppgitt som et årlig beløp)",
                                        Language.Nynorsk to " (oppgitt som eit årleg beløp)",
                                        Language.English to " (calculated to an annual amount)"
                                    )
                                }
                            }
                            cell {
                                includePhrase(KronerText(avkortningsbeloepAar))
                            }
                        }
                    }

                    showIf(harJusteringsbeloep) {
                        val fortegn = ifElse(justeringsbeloepAar.greaterThan(0), "-", "+")
                        row {
                            cell {
                                textExpr(
                                    Language.Bokmal to fortegn + " Beløp som er brukt for å justere reduksjonen av barnetillegget",
                                    Language.Nynorsk to fortegn + " Beløp som er brukt for å justere reduksjonen av barnetillegget",
                                    Language.English to fortegn + " Amount which is used to adjust the reduction of child supplement",
                                )
                            }
                            cell {
                                includePhrase(KronerText(justeringsbeloepAar.absoluteValue()))
                            }
                        }
                    }

                    showIf(harAvkortningsbeloep or harJusteringsbeloep) {
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
                                includePhrase(
                                    KronerText(
                                        beloepAarNetto,
                                        Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
                                    )
                                )
                            }
                        }
                    }

                    showIf(faarUtbetaltTillegg) {
                        row {
                            cell {
                                text(
                                    Language.Bokmal to "Utbetaling av barnetillegg per måned",
                                    Language.Nynorsk to "Utbetaling av barnetillegg per månad",
                                    Language.English to "Child supplement payment for the remaining months of the year"
                                )
                            }
                            cell {
                                includePhrase(KronerText(beloepNetto))
                            }
                        }
                    }
                }
            }
            paragraph {
                textExpr(
                    Language.Bokmal to "*) Samlet inntekt brukt i fastsettelse av barnetillegget er ".expr() +
                            inntektBruktIAvkortning.format() + " kroner",
                    Language.Nynorsk to "*) Samla inntekt brukt i fastsetjinga av barnetillegget er ".expr() +
                            inntektBruktIAvkortning.format() + " kroner",
                    Language.English to "*) Total income applied in calculation of reduction in child supplement is NOK".expr() +
                            inntektBruktIAvkortning.format(),
                )
            }

            paragraph {
                textExpr(
                    Language.Bokmal to "*) Fribeløp brukt i fastsettelsen av barnetillegget er ".expr() +
                            fribeloep.format() + " kroner",
                    Language.Nynorsk to "*) Fribeløp brukt i fastsetjinga av barnetillegget er ".expr() +
                            fribeloep.format() + " kroner",
                    Language.English to "*) Exemption amount applied in calculation of reduction in child supplement is NOK ".expr() +
                            fribeloep.format(),
                )
            }
            paragraph {
                textExpr(
                    Language.Bokmal to "*) Inntekt over fribeløpet er ".expr() +
                            inntektOverFribeloep.format() + " kroner",
                    Language.Nynorsk to "*) Inntekt over fribeløpet er ".expr() +
                            inntektOverFribeloep.format() + " kroner",
                    Language.English to "*) Income exceeding the exemption amount is NOK ".expr() +
                            inntektOverFribeloep.format(),
                )
            }
        }
    }

    data class InntektsGrenseBarnetillegg(
        val inntektstak: Expression<Kroner>,
        val samletInntektBruktIAvkortning: Expression<Kroner>,
    ): OutlinePhrase<LangBokmalNynorskEnglish>(){
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                textExpr(
                    Language.Bokmal to "Grensen for å få utbetalt barnetillegg er ".expr() + inntektstak.format() +
                            " kroner. Samlet inntekt brukt i fastsettelse av barnetillegget er " + samletInntektBruktIAvkortning.format() + " kroner.",

                    Language.Nynorsk to "Grensa for å få utbetalt barnetillegg er ".expr() + inntektstak.format() +
                            " kroner. Samla inntekt brukt i fastsetjinga av barnetillegget er " + samletInntektBruktIAvkortning.format() + " kroner.",

                    Language.English to "The income threshold for receiving child supplement is NOK ".expr() + inntektstak.format() +
                            ". Total income used in determining child supplement is NOK" + samletInntektBruktIAvkortning.format() + ".",
                )
            }
        }
    }

    // TBU 501V
    data class ForDegSomHarRettTilBarnetillegg(
        val harAnvendtTrygdetidUnder40: Expression<Boolean>,
        val harYrkesskade: Expression<Boolean>,
        val foedselsdatoPaaBarnTilleggetGjelder: Expression<List<LocalDate>>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title1 {
                text(
                    Language.Bokmal to "For deg som har rett til barnetillegg",
                    Language.Nynorsk to "For deg som har rett til barnetillegg",
                    Language.English to "For you who are eligible for child supplement",
                )
            }
            val barnFlertall = foedselsdatoPaaBarnTilleggetGjelder.size().greaterThan(1)

            paragraph {
                textExpr(
                    Language.Bokmal to "Du har rett til barnetillegg for barn født".expr(),
                    Language.Nynorsk to "Du har rett til barnetillegg for barn fødd".expr(),
                    Language.English to "You are entitled to child supplement for the".expr() +
                            ifElse(barnFlertall, "children", "child") + " born",
                )
                includePhrase(Felles.TextOrList(foedselsdatoPaaBarnTilleggetGjelder.map(LocalizedFormatter.DateFormat)))
            }

            paragraph {
                text(
                    Language.Bokmal to "Barnetillegget kan utgjøre opptil 40 prosent av folketrygdens grunnbeløp for hvert barn du forsørger. Du har rett til barnetillegg så lenge du forsørger barn som er under 18 år. Barnetillegget opphører når barnet fyller 18 år.",
                    Language.Nynorsk to "Barnetillegget kan utgjere opptil 40 prosent av grunnbeløpet i folketrygda for kvart barn du forsørgjer. Du har rett til barnetillegg så lenge du forsørgjer barn som er under 18 år.  Barnetillegget opphøyrer når barnet fyller 18 år.",
                    Language.English to "The child supplement may be up to 40 percent of the national insurance basic amount for each child you support. You are entitled to child supplement as long as you support children under 18 years of age. Payment of child supplement stops when the child turns 18."
                )

                showIf(harAnvendtTrygdetidUnder40 and not(harYrkesskade)) {
                    text(
                        Language.Bokmal to " Hvor mye du får i barnetillegg er også avhengig av trygdetiden din. Fordi trygdetiden din er kortere enn 40 år, blir barnetillegget ditt redusert.",
                        Language.Nynorsk to " Kor mykje du får i barnetillegg, er også avhengig av trygdetida di. Fordi trygdetida di er kortare enn 40 år, blir barnetillegget ditt redusert.",
                        Language.English to " How much child supplement you receive depends on your period of national insurance cover. As your period of national insurance cover is less than 40 years, your child supplement will be reduced."
                    )
                }
            }

            paragraph {
                text(
                    Language.Bokmal to "Dersom et barn får egen inntekt eller kapitalinntekt i løpet av et år som er høyere enn folketrygdens grunnbeløp, så har du ikke rett til barnetillegg for dette barnet.",
                    Language.Nynorsk to "Dersom eit barn får eiga inntekt eller kapitalinntekt som er høgare enn grunnbeløpet i folketrygda gjennom eit år, så har du ikkje rett til barnetillegg for dette barnet.",
                    Language.English to "If a child has its own income or capital income in a year that is higher than the national insurance basic amount, you are not entitled to child supplement for this child."
                )
            }
        }

    }

    object VedleggBeregnUTInnlednBT : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    Language.Bokmal to "Størrelsen på barnetillegget er avhengig av samlet inntekt.",
                    Language.Nynorsk to "Storleiken på barnetillegget er avhengig av samla inntekt.",
                    Language.English to "The amount of child supplement is dependent on your total income."
                )
            }
            paragraph {
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
    }

    data class PeriodisertInntektSaerkullsbarn(
        val avkortningsbeloepAar: Expression<Kroner>,
        val fribeloepEllerInntektErPeriodisert: Expression<Boolean>,
        val harFlereSaerkullsbarn: Expression<Boolean>,
        val harJusteringsbeloep: Expression<Boolean>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                textExpr(
                    Language.Bokmal to "For ".expr() +
                            ifElse(harFlereSaerkullsbarn, "barna", "barnet")
                            + " som ikke bor med begge sine foreldre " +
                            ifElse(
                                fribeloepEllerInntektErPeriodisert,
                                "blir 50 prosent av den inntekten som overstiger fribeløpet omregnet til et årlig beløp som tilsvarer",
                                "er 50 prosent av den inntekten som overstiger fribeløpet"
                            )
                            + " " + avkortningsbeloepAar.format() + " kroner.",

                    Language.Nynorsk to "For ".expr() +
                            ifElse(harFlereSaerkullsbarn, "barna", "barnet")
                            + " som ikkje bur med begge foreldra " +
                            ifElse(
                                fribeloepEllerInntektErPeriodisert,
                                "blir 50 prosent av den inntekta som overstig fribeløpet regna om til et årleg beløp som svarer til",
                                "er 50 prosent av den inntekta som overstig fribeløpet"
                            )
                            + " " + avkortningsbeloepAar.format() + " kroner.",

                    Language.English to "For ".expr() +
                            ifElse(
                                harFlereSaerkullsbarn,
                                "children who do not",
                                "the child who does not"
                            )
                            + " live with both parents 50 percent of the income that exceeds the exemption amount " +
                            ifElse(
                                fribeloepEllerInntektErPeriodisert,
                                "is recalculated to an annual amount of",
                                "is"
                            )
                            + " NOK " + avkortningsbeloepAar.format() + "."
                )

                showIf(not(harJusteringsbeloep)) {
                    text(
                        Language.Bokmal to " Dette beløpet bruker vi til å redusere barnetillegget ditt for hele året.",
                        Language.Nynorsk to " Dette beløpet bruker vi til å redusere barnetillegget ditt for heile året.",
                        Language.English to " This amount will be used to reduce your child supplement during the calendar year."
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
                        Language.Bokmal to "Vi tar hensyn til hvordan barnetillegget eventuelt har vært redusert tidligere, og vi har derfor ".expr() +
                                ifElse(oekeReduksjonenAvTilleggetSaerkullsbarn, "lagt til", "trukket fra")
                                + " " + justeringsbeloep.format() + " kroner i beløpet vi reduserer barnetillegget med for resten av året.",

                        Language.Nynorsk to "Vi tek omsyn til korleis eit barnetillegg eventuelt har vore redusert tidlegare, og har derfor ".expr() +
                                ifElse(oekeReduksjonenAvTilleggetSaerkullsbarn, "lagt til", "trekt frå")
                                + " " + justeringsbeloep.format() + " kroner i beløpet vi reduserer barnetillegget med for resten av året.".expr(),

                        Language.English to "We take into account how the child supplement has been reduced earlier this year. The amount with which your child supplement will be reduced for the rest of the year has therefore been ".expr() +
                                ifElse(oekeReduksjonenAvTilleggetSaerkullsbarn, "increased", "reduced")
                                + " with NOK " + justeringsbeloep.format() + "."
                    )
                }
            }
        }
    }


    data class MaanedligTilleggSaerkullsbarn(
        val saerkullTilleggNetto: Expression<Kroner>,
        val harFlereSaerkullsbarn: Expression<Boolean>,
        val harInnvilgetBarnetilleggFellesbarn: Expression<Boolean>,
        ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                showIf(harInnvilgetBarnetilleggFellesbarn) {
                    textExpr(
                        Language.Bokmal to "Du vil få utbetalt ".expr() + saerkullTilleggNetto.format() + " kroner i måneden før skatt i barnetillegg for " +
                                ifElse(harFlereSaerkullsbarn, "barna", "barnet")
                                + " som ikke bor med begge sine foreldre.",

                        Language.Nynorsk to "Du vil få utbetalt ".expr() + saerkullTilleggNetto.format() + " kroner i månaden før skatt i barnetillegg for " +
                                ifElse(harFlereSaerkullsbarn, "barna", "barnet")
                                + " som ikkje bur saman med begge foreldra.",

                        Language.English to "You will receive a monthly child supplement payment of NOK ".expr() + saerkullTilleggNetto.format() +
                                " before tax for the " + ifElse(harFlereSaerkullsbarn, "children", "child")
                                + " who do not live together with both parents."
                    )
                }.orShow {
                    textExpr(
                        Language.Bokmal to "Du vil få utbetalt ".expr() + saerkullTilleggNetto.format() + " kroner i måneden før skatt i barnetillegg.",
                        Language.Nynorsk to "Du vil få utbetalt ".expr() + saerkullTilleggNetto.format() + " kroner i månaden før skatt i barnetillegg.",
                        Language.English to "You will receive a monthly child supplement payment of NOK ".expr() + saerkullTilleggNetto.format() + " before tax."
                    )
                }
            }
        }
    }

    data class FaarIkkeUtbetaltTilleggSaerkullsbarn(
        val harJusteringsbeloep: Expression<Boolean>,
        val harInnvilgetBarnetilleggFellesbarn: Expression<Boolean>,
        val harFlereSaerkullsbarn: Expression<Boolean>,
        ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                showIf(harJusteringsbeloep) {
                    text(
                        Language.Bokmal to "Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året.",
                        Language.Nynorsk to "Du har allereie fått utbetalt det du har rett til i år, og får difor ikkje utbetalt barnetillegg for resten av året.",
                        Language.English to "You have already received what you are entitled to this year, therefore you will not receive any child supplement for the remainder of the year."
                    )
                }.orShowIf(harInnvilgetBarnetilleggFellesbarn) {
                    textExpr(
                        Language.Bokmal to "Du får ikke utbetalt barnetillegget for ".expr()
                                + ifElse(harFlereSaerkullsbarn, "barna", "barnet")
                                + " som ikke bor med begge sine foreldre fordi samlet inntekt er over grensen for å få utbetalt barnetillegg.",

                        Language.Nynorsk to "Du får ikkje utbetalt barnetillegget for ".expr()
                                + ifElse(harFlereSaerkullsbarn, "barna", "barnet")
                                + " som ikkje bur saman med begge foreldra fordi samla inntekt er over grensa for å få utbetalt barnetillegg.",

                        Language.English to "You will not receive a child supplement for the ".expr()
                                + ifElse(harFlereSaerkullsbarn, "children", "child")
                                + " who do not live together with both parents because your income is over the income limit for receiving a child supplement."
                    )
                }.orShow {
                    text(
                        Language.Bokmal to "Du får ikke utbetalt barnetillegget fordi samlet inntekt er over grensen for å få utbetalt barnetillegg.",
                        Language.Nynorsk to "Du får ikkje utbetalt barnetillegget fordi samla inntekt er over grensa for å få utbetalt barnetillegg.",
                        Language.English to "You will not receive a child supplement because your income is over the income limit for receiving a child supplement."
                    )
                }
            }
        }
    }

    // TBU607V
    data class MaanedligTilleggFellesbarn(
        val barnetilleggFellesbarn: Expression<Kroner>,
        val harFlereBarn: Expression<Boolean>,
        val harInnvilgetBarnetilleggSaerkullsbarn: Expression<Boolean>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                showIf(harInnvilgetBarnetilleggSaerkullsbarn) {
                    textExpr(
                        Language.Bokmal to "Du vil få utbetalt ".expr() + barnetilleggFellesbarn.format() + " kroner i måneden før skatt i barnetillegg for " +
                                ifElse(harFlereBarn, "barna", "barnet") +
                                " som bor med begge sine foreldre.",

                        Language.Nynorsk to "Du vil få utbetalt ".expr() + barnetilleggFellesbarn.format() + " kroner i månaden før skatt i barnetillegg for " +
                                ifElse(harFlereBarn, "barna", "barnet") +
                                " som bur saman med begge foreldra sine.",

                        Language.English to "You will receive a monthly child supplement payment of NOK ".expr() + barnetilleggFellesbarn.format() + " for the " +
                                ifElse(harFlereBarn, "children who live", "child who lives") +
                                " together with both parents."
                    )
                }.orShow {
                    textExpr(
                        Language.Bokmal to "Du vil få utbetalt ".expr() + barnetilleggFellesbarn.format() + " kroner i måneden før skatt i barnetillegg.",
                        Language.Nynorsk to "Du vil få utbetalt ".expr() + barnetilleggFellesbarn.format() + " kroner i månaden før skatt i barnetillegg.",
                        Language.English to "You will receive a monthly child supplement payment of NOK ".expr() + barnetilleggFellesbarn.format() + "."
                    )
                }
            }
        }
    }

    // TBU608V
    data class FaarIkkeUtbetaltTilleggFellesbarn(
        val nettoBeloepFellesbarn: Expression<Kroner>,
        val harFlereBarn: Expression<Boolean>,
        val harJusteringsbeloep: Expression<Boolean>,
        val harInnvilgetBarnetilleggSaerkullsbarn: Expression<Boolean>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                showIf(harJusteringsbeloep) {
                    text(
                        Language.Bokmal to "Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året.",
                        Language.Nynorsk to "Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året.",
                        Language.English to "You have already received what you are entitled to this year, therefore you will not receive any child supplement for the remainder of the year."
                    )
                }.orShowIf(harInnvilgetBarnetilleggSaerkullsbarn) {
                    textExpr(
                        Language.Bokmal to "Du får ikke utbetalt barnetillegget for ".expr() +
                                ifElse(harFlereBarn, "barna", "barnet") +
                                " som bor med begge sine foreldre fordi samlet inntekt er over grensen for å få utbetalt barnetillegg.",

                        Language.Nynorsk to "Du får ikkje utbetalt barnetillegget for ".expr() +
                                ifElse(harFlereBarn, "barna", "barnet") +
                                " som bur saman med begge foreldra sine fordi samla inntekt er over grensa for å få utbetalt barnetillegg.",

                        Language.English to "You will not receive a child supplement for the ".expr() +
                                ifElse(harFlereBarn, "children who live", "child who lives") +
                                " together with both parents because your income is over the income limit for receiving a child supplement."
                    )
                }.orShow {
                    text(
                        Language.Bokmal to "Du får ikke utbetalt barnetillegget fordi samlet inntekt er over grensen for å få utbetalt barnetillegg.",
                        Language.Nynorsk to "Du får ikkje utbetalt barnetillegget fordi samla inntekt er over grensa for å få utbetalt barnetillegg.",
                        Language.English to "You will not receive a child supplement for the because your income is over the income limit for receiving a child supplement."
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
                    Language.Bokmal to "Vi fastsetter størrelsen på barnetillegget ut fra den samlede inntekten til begge foreldrene.",
                    Language.Nynorsk to "Vi fastset storleiken på barnetillegget ut frå den samla inntekta til begge foreldra.",
                    Language.English to "We determine the amount of child supplement based on the total income of both parents."
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Barnetillegget blir redusert dersom den samlede inntekten er høyere enn fribeløpet. Fribeløpet for et barn er 4,6 ganger folketrygdens grunnbeløp og det øker med 40 prosent av folketrygdens grunnbeløp for hvert ekstra barn.",
                    Language.Nynorsk to "Barnetillegget blir redusert dersom den samla inntekta di er høgare enn fribeløpet. Fribeløpet for eit barn er 4,6 gonger grunnbeløpet i folketrygda og det aukar med 40 prosent av grunnbeløpet for kvart ekstra barn.",
                    Language.English to "The child supplement will be reduced if your total income is greater than the exemption amount. The exemption amount is 4.6 times the National Insurance basic amount and it increases with 40 percent of the National Insurance basic amount for each extra child."
                )
                showIf(harAnvendtTrygdetidUnder40 and not(harYrkesskade)) {
                    text(
                        Language.Bokmal to " Siden trygdetiden din er kortere enn 40 år, blir fribeløpet redusert ut fra den trygdetiden du har. ",
                        Language.Nynorsk to " Sidan trygdetida di er kortare enn 40 år, blir fribeløpet redusert ut frå den trygdetida du har. ",
                        Language.English to " Since you have less than 40 years National Insurance membership, the exemption amount is ruduced correspondingly in relation to the the length of time of you National Insurance membership. "
                    )
                }
            }
            paragraph {
                text(
                    Language.Bokmal to "Dersom begge foreldrene mottar uføretrygd blir barnetillegget gitt til den som har rett til det høyeste tillegget.",
                    Language.Nynorsk to "Dersom begge foreldra får uføretrygd, blir barnetillegget gitt til den som har rett til det høgaste tillegget.",
                    Language.English to "If both parents recieve disbaility benefit, child supplement will be paid to the parent with the highest disability benefit."
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
                    Language.Bokmal to "Vi fastsetter størrelsen på barnetillegget ut fra inntekten din. Inntekt til en ektefelle/partner/samboer som ikke er forelder til barnet, har ikke betydning for størrelsen på barnetillegget.",
                    Language.Nynorsk to "Vi fastset storleiken på barnetillegget ut frå inntekta di. Inntekt til ein ektefelle/partnar/sambuar som ikkje er forelder til barnet, har ikkje betydning for storleiken på barnetillegget.",
                    Language.English to "We determine the amount of child supplement based on your income. The income of a spouse/partner/cohabitant who is not the child's parent, is not taken into consideration."
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Barnetillegget blir redusert dersom den samlede inntekten din er høyere enn fribeløpet. Fribeløpet for et barn er 3,1 ganger folketrygdens grunnbeløp og det øker med 40 prosent av folketrygdens grunnbeløp for hvert ekstra barn.",
                    Language.Nynorsk to "Barnetillegget blir redusert dersom den samla inntekta di er høgare enn fribeløpet. Fribeløpet for eit barn er 3,1 gonger grunnbeløpet i folketrygda og det aukar med 40 prosent av grunnbeløpet for kvart ekstra barn.",
                    Language.English to "The child supplement will be reduced if your total income is greater than the exemption amount. The exemption amount is 3.1 times the National Insurance basic amount and it increases with 40 percent of the National Insurance basic amount for each extra child."
                )
            }
            showIf(harAnvendtTrygdetidUnder40 and not(harYrkesskade)) {
                paragraph {
                    text(
                        Language.Bokmal to "Siden trygdetiden din er kortere enn 40 år, blir fribeløpet redusert ut fra den trygdetiden du har. ",
                        Language.Nynorsk to "Sidan trygdetida di er kortare enn 40 år, blir fribeløpet redusert ut frå den trygdetida du har. ",
                        Language.English to "Since you have less than 40 years National Insurance membership, the exemption amount is ruduced correspondingly in relation to the the length of time of you National Insurance membership. "
                    )
                }
            }
            paragraph {
                text(
                    Language.Bokmal to "Dersom begge foreldrene mottar uføretrygd blir barnetillegget gitt til den som har den daglige omsorgen for barnet. Har foreldrene delt omsorg for barnet, blir barnetillegget gitt til den forelderen som bor på samme folkeregistrerte adresse som barnet.",
                    Language.Nynorsk to "Dersom begge foreldra får uføretrygd, blir barnetillegget gitt til den som har den daglege omsorga for barnet. Har foreldra delt omsorg for barnet, blir barnetillegget gitt til den forelderen som bur på same folkeregistrerte adresse som barnet.",
                    Language.English to "If both parents recieve disbaility benefit, child supplement will be paid to the parent with guardianship of the child. If both parents have guardianship of the child, child supplement is given to the parent who lives at the same registered address as the child."
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
        val borMedSivilstand: Expression<BorMedSivilstand>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                textExpr(
                    Language.Bokmal to "Vi fastsetter størrelsen på barnetillegget ut fra inntekten til deg og din ".expr() +
                            borMedSivilstand.ubestemtForm() + " for " +
                            ifElse(harTilleggForFlereFellesbarn, "barna", "barnet") +
                            " som bor med begge sine foreldre. Barnetillegget blir redusert dersom den samlede inntekten er høyere enn fribeløpet. Fribeløpet for et barn som bor med begge foreldrene er 4,6 ganger folketrygdens grunnbeløp, og øker med 40 prosent av folketrygdens grunnbeløp for hvert ekstra barn.",

                    Language.Nynorsk to "Vi fastset storleiken på barnetillegget ut frå inntekta til deg og din ".expr() +
                            borMedSivilstand.ubestemtForm() + " for " +
                            ifElse(harTilleggForFlereFellesbarn, "barna", "barnet") +
                            " som bur med begge foreldra sine. Barnetillegget blir redusert dersom den samla inntekta er høgare enn fribeløpet. Fribeløpet for eit barn som bur med begge foreldra, er 4,6 gonger grunnbeløpet i folketrygda, og aukar med 40 prosent av grunnbeløpet i folketrygda for kvart ekstra barn.",

                    Language.English to "We determine the amount of child supplement based on the total income for you and your ".expr() +
                            borMedSivilstand.ubestemtForm() + " for the " +
                            ifElse(harTilleggForFlereFellesbarn, "children who live", "child who lives") +
                            " with both parents. The child supplement will be reduced if your total income is greater than the exemption amount. The exemption amount is 4.6 times the National Insurance basic amount and it increases with 40 percent of the National Insurance basic amount for each extra child.",
                )
            }
            paragraph {
                textExpr(
                    Language.Bokmal to "For ".expr() +
                            ifElse(harTilleggForFlereSaerkullsbarn, "barna", "barnet")
                            + " som ikke bor sammen med begge foreldre, fastsetter vi størrelsen på barnetillegget ut fra inntekten din. Inntekt til en ektefelle/partner/samboer som ikke er forelder til barnet, har ikke betydning for størrelsen på barnetillegget. Barnetillegget blir redusert dersom den samlede inntekten din er høyere enn fribeløpet. Fribeløpet for et barn som ikke bor sammen med begge foreldrene er 3,1 ganger folketrygdens grunnbeløp, og øker med 40 prosent av folketrygdens grunnbeløp for hvert ekstra barn.",
                    Language.Nynorsk to "For ".expr() +
                            ifElse(harTilleggForFlereSaerkullsbarn, "barna", "barnet")
                            + " som ikkje bur saman med begge foreldra, fastset vi storleiken på barnetillegget ut frå inntekta di. Inntekt til ein ektefelle/partnar/sambuar som ikkje er forelder til barnet, har ikkje betydning for storleiken på barnetillegget. Barnetillegget blir redusert dersom den samla inntekta di er høgare enn fribeløpet. Fribeløpet for eit barn som ikkje bur saman med begge foreldra, er 3,1 gonger grunnbeløpet i folketrygda, og aukar med 40 prosent av grunnbeløpet i folketrygda for kvart ekstra barn.",
                    Language.English to "For the ".expr() +
                            ifElse(harTilleggForFlereSaerkullsbarn, "children who do", "child who does")
                            + " not live together with both parents, the amount of child supplement is based on your income. The income of a spouse/partner/cohabitant who is not the child's parent, is not taken into consideration. The child supplement will be reduced if your total income is greater than the exemption amount. The exemption amount is 3.1 times the National Insurance basic amount and it increases with 40 percent of the National Insurance basic amount for each extra child.",
                )
            }
            showIf(harAnvendtTrygdetidUnder40 and not(harYrkesskade)) {
                paragraph {
                    text(
                        Language.Bokmal to " Siden trygdetiden din er kortere enn 40 år, blir fribeløpet redusert ut fra den trygdetiden du har.",
                        Language.Nynorsk to " Sidan trygdetida di er kortare enn 40 år, blir fribeløpet redusert ut frå den trygdetida du har.",
                        Language.English to " Since you have less than 40 years National Insurance membership, the exemption amount is ruduced correspondingly in relation to how long you have had National Insurance membership."
                    )
                }
            }
        }
    }

    // TBU605V
    data class PeriodisertInntektInnledning(
        val harKravaarsakEndringInntekt: Expression<Boolean>,
        val harFlereTillegg: Expression<Boolean>,
        val borMedSivilstandFelles: Expression<BorMedSivilstand?>
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                showIf(harKravaarsakEndringInntekt) {
                    text(Language.Bokmal to "Når inntekten ", Language.Nynorsk to "Når inntekta ", Language.English to "When ")
                    ifNotNull(borMedSivilstandFelles) { borMedSivilstandFelles ->
                        textExpr(
                            Language.Bokmal to "til deg eller ".expr() + borMedSivilstandFelles.bestemtForm() + " din",
                            Language.Nynorsk to "di eller til ".expr() + borMedSivilstandFelles.bestemtForm() + " din",
                            Language.English to "your or your ".expr() + borMedSivilstandFelles.ubestemtForm() + "'s",
                        )
                    }.orShow {
                        text(
                            Language.Bokmal to "din",
                            Language.Nynorsk to "di",
                            Language.English to "your",
                        )
                    }
                    text(
                        Language.Bokmal to " endrer seg",
                        Language.Nynorsk to " endrar seg",
                        Language.English to "",
                    )
                }.orShow {
                    text(
                        Language.Bokmal to "Har det vært en endring i inntekten ",
                        Language.Nynorsk to "Har det vore ei endring i inntekta ",
                        Language.English to "If ",
                    )
                    ifNotNull(borMedSivilstandFelles) { borMedSivilstandFelles ->
                        textExpr(
                            Language.Bokmal to "til deg eller ".expr() + borMedSivilstandFelles.bestemtForm() + " din",
                            Language.Nynorsk to "til deg eller ".expr() + borMedSivilstandFelles.bestemtForm() + " din",
                            Language.English to "your or your ".expr() + borMedSivilstandFelles.ubestemtForm() + "'s",
                        )
                    }.orShow {
                        text(
                            Language.Bokmal to "din",
                            Language.Nynorsk to "di",
                            Language.English to "your",
                        )
                    }
                }

                textExpr(
                    Language.Bokmal to " blir reduksjonen av ".expr() +
                            ifElse(harFlereTillegg, "barnetilleggene", "barnetillegget") +
                            " vurdert på nytt.",
                    Language.Nynorsk to " blir reduksjonen av ".expr() +
                            ifElse(harFlereTillegg, "barnetilleggene", "barnetillegget") +
                            " vurdert på nytt.",
                    Language.English to " income has been changed, your child supplement will be recalculated.".expr(),
                )
            }
        }
    }

    data class PeriodisertInntektOverFribeloepFellesTillegg(
        val avkortningsbeloepAar: Expression<Kroner>,
        val harJusteringsbeloep: Expression<Boolean>,
        val inntektEllerFribeloepErPeriodisert: Expression<Boolean>,
        val harTilleggForFlereFellesbarn: Expression<Boolean>
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                textExpr(
                    Language.Bokmal to "50 prosent av den inntekten som overstiger fribeløpet for ".expr()
                            + ifElse(harTilleggForFlereFellesbarn, "barna", "barnet") +
                            " som bor med begge sine foreldre ",

                    Language.Nynorsk to "50 prosent av inntekta som overstig fribeløpet for ".expr()
                            + ifElse(harTilleggForFlereFellesbarn, "barna", "barnet") +
                            " som bur med begge foreldra sine ",

                    Language.English to "50 percent of income that exceeds the exemption amount for the ".expr()
                            + ifElse(harTilleggForFlereFellesbarn, "children", "child") +
                            " that " + ifElse(harTilleggForFlereFellesbarn, "live", "lives") +
                            " with both of their parents, ",
                )
                showIf(inntektEllerFribeloepErPeriodisert) {
                    text(
                        Language.Bokmal to "blir omregnet til et årlig beløp som tilsvarer",
                        Language.Nynorsk to "blir rekna om til et årleg beløp som svarer til",
                        Language.English to "is recalculated to an annual amount of",
                    )
                }.orShow {
                    text(
                        Language.Bokmal to "er",
                        Language.Nynorsk to "er",
                        Language.English to "is",
                    )
                }

                showIf(avkortningsbeloepAar.greaterThan(0)) {
                    textExpr(
                        Language.Bokmal to " ".expr() + avkortningsbeloepAar.format() + " kroner.",
                        Language.Nynorsk to " ".expr() + avkortningsbeloepAar.format() + " kroner.",
                        Language.English to " NOK ".expr() + avkortningsbeloepAar.format() + ".",
                    )
                }
                showIf(not(harJusteringsbeloep)) {
                    text(
                        Language.Bokmal to " Dette beløpet bruker vi til å redusere dette barnetillegget for hele året.",
                        Language.Nynorsk to " Dette beløpet bruker vi til å redusere dette barnetillegget for heile året.",
                        Language.English to " This amount will be used to reduce your child supplement during the calendar year."
                    )
                }
            }
        }
    }

    data class PeriodisertInntektOverFribeloepEttTillegg(
        val avkortningsbeloepAar: Expression<Kroner>,
        val harJusteringsbeloep: Expression<Boolean>,
        val inntektEllerFribeloepErPeriodisert: Expression<Boolean>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    Language.Bokmal to "50 prosent av den inntekten som overstiger fribeløpet ",
                    Language.Nynorsk to "50 prosent av inntekta som overstig fribeløpet ",
                    Language.English to "50 percent of income that exceeds the exemption amount ",
                )
                showIf(inntektEllerFribeloepErPeriodisert) {
                    text(
                        Language.Bokmal to "blir omregnet til et årlig beløp som tilsvarer",
                        Language.Nynorsk to "blir rekna om til et årleg beløp som svarer til",
                        Language.English to "is recalculated to an annual amount of",
                    )
                }.orShow {
                    text(
                        Language.Bokmal to "er",
                        Language.Nynorsk to "er",
                        Language.English to "is",
                    )
                }

                textExpr(
                    Language.Bokmal to " ".expr() + avkortningsbeloepAar.format() + " kroner.",
                    Language.Nynorsk to " ".expr() + avkortningsbeloepAar.format() + " kroner.",
                    Language.English to " NOK ".expr() + avkortningsbeloepAar.format() + ".",
                )

                showIf(not(harJusteringsbeloep)) {
                    text(
                        Language.Bokmal to " Dette beløpet bruker vi til å redusere barnetillegget ditt for hele året.",
                        Language.Nynorsk to " Dette beløpet bruker vi til å redusere barnetillegget ditt for heile året.",
                        Language.English to " This amount will be used to reduce your child supplement during the calendar year."
                    )
                }
            }
        }
    }
}