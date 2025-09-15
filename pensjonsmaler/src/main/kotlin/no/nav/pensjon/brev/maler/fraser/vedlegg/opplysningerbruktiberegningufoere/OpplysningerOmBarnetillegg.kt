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
import no.nav.pensjon.brev.template.Language.*
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
                bokmal { + "Slik beregner vi størrelsen på barnetillegget" },
                nynorsk { + "Slik reknar vi ut storleiken på barnetillegget" },
                english { + "How we calculate the amount of child supplement" }
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
                    text(
                        bokmal { + "Reduksjon av barnetillegg for fellesbarn før skatt "
                                + ifElse(fraOgMedDatoErNesteAar, "for neste år", "i år") },
                        nynorsk { + "Reduksjon av barnetillegg for fellesbarn før skatt "
                                + ifElse(fraOgMedDatoErNesteAar, "for neste år", "i år") },
                        english { + "Reduction of child supplement payment for joint children before tax, "
                                + ifElse(fraOgMedDatoErNesteAar, "for next year", "for this year") },
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
                    text(
                        bokmal { + "Reduksjon av barnetillegg for særkullsbarn før skatt "
                                + ifElse(fraOgMedDatoErNesteAar, "for neste år", "i år") },
                        nynorsk { + "Reduksjon av barnetillegg for særkullsbarn før skatt "
                                + ifElse(fraOgMedDatoErNesteAar, "for neste år", "i år") },
                        english { + "Reduction of child supplement payment for children from a previous relationship before tax, "
                                + ifElse(fraOgMedDatoErNesteAar, "for next year", "for this year") },
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
                                bokmal { + "Beløp" },
                                nynorsk { + "Beløp" },
                                english { + "Amount" },
                            )
                        }
                    }
                ) {
                    row {
                        cell {
                            text(
                                bokmal { + "Årlig barnetillegg før reduksjon ut fra inntekt" },
                                nynorsk { + "Årleg barnetillegg før reduksjon ut frå inntekt" },
                                english { + "Yearly child supplement before income reduction" }
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
                                    bokmal { + "- 50 prosent av inntekt som overstiger fribeløpet *" },
                                    nynorsk { + "- 50 prosent av inntekta som overstig fribeløpet *" },
                                    english { + "- 50 percent of income exceeding the allowance amount *" }
                                )

                                showIf(fribeloepErPeriodisert) {
                                    text(
                                        bokmal { + " (oppgitt som et årlig beløp)" },
                                        nynorsk { + " (oppgitt som eit årleg beløp)" },
                                        english { + " (calculated to an annual amount)" }
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
                                text(
                                    bokmal { + fortegn + " Beløp som er brukt for å justere reduksjonen av barnetillegget" },
                                    nynorsk { + fortegn + " Beløp som er brukt for å justere reduksjonen av barnetillegget" },
                                    english { + fortegn + " Amount which is used to adjust the reduction of child supplement" },
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
                                    bokmal { + "= Årlig barnetillegg etter reduksjon ut fra inntekt" },
                                    nynorsk { + "= Årleg barnetillegg etter reduksjon ut frå inntekt" },
                                    english { + "= Yearly child supplement after income reduction" },
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
                                    bokmal { + "Utbetaling av barnetillegg per måned" },
                                    nynorsk { + "Utbetaling av barnetillegg per månad" },
                                    english { + "Child supplement payment for the remaining months of the year" }
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
                text(
                    bokmal { + "*) Samlet inntekt brukt i fastsettelse av barnetillegget er " +
                            inntektBruktIAvkortning.format() },
                    nynorsk { + "*) Samla inntekt brukt i fastsetjinga av barnetillegget er " +
                            inntektBruktIAvkortning.format() },
                    english { + "*) Total income applied in calculation of reduction in child supplement is " +
                            inntektBruktIAvkortning.format() },
                )
            }

            paragraph {
                text(
                    bokmal { + "*) Fribeløp brukt i fastsettelsen av barnetillegget er " +
                            fribeloep.format() },
                    nynorsk { + "*) Fribeløp brukt i fastsetjinga av barnetillegget er " +
                            fribeloep.format() },
                    english { + "*) Exemption amount applied in calculation of reduction in child supplement is " +
                            fribeloep.format() },
                )
            }
            paragraph {
                text(
                    bokmal { + "*) Inntekt over fribeløpet er " +
                            inntektOverFribeloep.format() + "." },
                    nynorsk { + "*) Inntekt over fribeløpet er " +
                            inntektOverFribeloep.format() + "." },
                    english { + "*) Income exceeding the exemption amount is " +
                            inntektOverFribeloep.format() +"." },
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
                text(
                    bokmal { + "Grensen for å få utbetalt barnetillegg er " + inntektstak.format() +
                            ". Samlet inntekt brukt i fastsettelse av barnetillegget er " + samletInntektBruktIAvkortning.format() + "." },

                    nynorsk { + "Grensa for å få utbetalt barnetillegg er " + inntektstak.format() +
                            ". Samla inntekt brukt i fastsetjinga av barnetillegget er " + samletInntektBruktIAvkortning.format() + "." },

                    english { + "The income threshold for receiving child supplement is " + inntektstak.format() +
                            ". Total income used in determining child supplement is " + samletInntektBruktIAvkortning.format() + "." },
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
                    bokmal { + "For deg som har rett til barnetillegg" },
                    nynorsk { + "For deg som har rett til barnetillegg" },
                    english { + "For you who are eligible for child supplement" },
                )
            }
            val barnFlertall = foedselsdatoPaaBarnTilleggetGjelder.size().greaterThan(1)

            paragraph {
                text(
                    bokmal { + "Du har rett til barnetillegg for barn født" },
                    nynorsk { + "Du har rett til barnetillegg for barn fødd" },
                    english { + "You are entitled to child supplement for the" +
                            ifElse(barnFlertall, "children", "child") + " born" },
                )
                includePhrase(Felles.TextOrList(foedselsdatoPaaBarnTilleggetGjelder.map(LocalizedFormatter.DateFormat)))
            }

            paragraph {
                text(
                    bokmal { + "Barnetillegget kan utgjøre opptil 40 prosent av folketrygdens grunnbeløp for hvert barn du forsørger. Du har rett til barnetillegg så lenge du forsørger barn som er under 18 år. Barnetillegget opphører når barnet fyller 18 år." },
                    nynorsk { + "Barnetillegget kan utgjere opptil 40 prosent av grunnbeløpet i folketrygda for kvart barn du forsørgjer. Du har rett til barnetillegg så lenge du forsørgjer barn som er under 18 år.  Barnetillegget opphøyrer når barnet fyller 18 år." },
                    english { + "The child supplement may be up to 40 percent of the national insurance basic amount for each child you support. You are entitled to child supplement as long as you support children under 18 years of age. Payment of child supplement stops when the child turns 18." }
                )

                showIf(harAnvendtTrygdetidUnder40 and not(harYrkesskade)) {
                    text(
                        bokmal { + " Hvor mye du får i barnetillegg er også avhengig av trygdetiden din. Fordi trygdetiden din er kortere enn 40 år, blir barnetillegget ditt redusert." },
                        nynorsk { + " Kor mykje du får i barnetillegg, er også avhengig av trygdetida di. Fordi trygdetida di er kortare enn 40 år, blir barnetillegget ditt redusert." },
                        english { + " How much child supplement you receive depends on your period of national insurance cover. As your period of national insurance cover is less than 40 years, your child supplement will be reduced." }
                    )
                }
            }

            paragraph {
                text(
                    bokmal { + "Dersom et barn får egen inntekt eller kapitalinntekt i løpet av et år som er høyere enn folketrygdens grunnbeløp, så har du ikke rett til barnetillegg for dette barnet." },
                    nynorsk { + "Dersom eit barn får eiga inntekt eller kapitalinntekt som er høgare enn grunnbeløpet i folketrygda gjennom eit år, så har du ikkje rett til barnetillegg for dette barnet." },
                    english { + "If a child has its own income or capital income in a year that is higher than the national insurance basic amount, you are not entitled to child supplement for this child." }
                )
            }
        }

    }

    object VedleggBeregnUTInnlednBT : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    bokmal { + "Størrelsen på barnetillegget er avhengig av samlet inntekt." },
                    nynorsk { + "Storleiken på barnetillegget er avhengig av samla inntekt." },
                    english { + "The amount of child supplement is dependent on your total income." }
                )
            }
            paragraph {
                text(
                    bokmal { + "Barnetillegget kan bli redusert ut fra:" },
                    nynorsk { + "Barnetillegget kan bli redusert ut frå:" },
                    english { + "Child supplement can be reduced based on:" }
                )
                list {
                    item {
                        text(
                            bokmal { + "uføretrygd" },
                            nynorsk { + "uføretrygd" },
                            english { + "disability benefits" }
                        )
                    }
                    item {
                        text(
                            bokmal { + "arbeidsinntekt" },
                            nynorsk { + "arbeidsinntekt" },
                            english { + "income from employment" }
                        )
                    }
                    item {
                        text(
                            bokmal { + "næringsinntekt" },
                            nynorsk { + "næringsinntekt " },
                            english { + "income from self-employment" }
                        )
                    }
                    item {
                        text(
                            bokmal { + "inntekt fra utlandet" },
                            nynorsk { + "inntekt frå utlandet" },
                            english { + "income from overseas" }
                        )
                    }
                    item {
                        text(
                            bokmal { + "ytelser/pensjon fra Norge" },
                            nynorsk { + "ytingar/pensjon frå Noreg" },
                            english { + "payments/pensions from Norway" }
                        )
                    }
                    item {
                        text(
                            bokmal { + "pensjon fra utlandet" },
                            nynorsk { + "pensjon frå utlandet" },
                            english { + "pensions from overseas" }
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
                text(
                    bokmal { + "For " +
                            ifElse(harFlereSaerkullsbarn, "barna", "barnet")
                            + " som ikke bor med begge sine foreldre " +
                            ifElse(
                                fribeloepEllerInntektErPeriodisert,
                                "blir 50 prosent av den inntekten som overstiger fribeløpet omregnet til et årlig beløp som tilsvarer",
                                "er 50 prosent av den inntekten som overstiger fribeløpet"
                            )
                            + " " + avkortningsbeloepAar.format() + "." },

                    nynorsk { + "For " +
                            ifElse(harFlereSaerkullsbarn, "barna", "barnet")
                            + " som ikkje bur med begge foreldra " +
                            ifElse(
                                fribeloepEllerInntektErPeriodisert,
                                "blir 50 prosent av den inntekta som overstig fribeløpet regna om til et årleg beløp som svarer til",
                                "er 50 prosent av den inntekta som overstig fribeløpet"
                            )
                            + " " + avkortningsbeloepAar.format() + "." },

                    english { + "For " +
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
                            + " " + avkortningsbeloepAar.format() + "." }
                )

                showIf(not(harJusteringsbeloep)) {
                    text(
                        bokmal { + " Dette beløpet bruker vi til å redusere barnetillegget ditt for hele året." },
                        nynorsk { + " Dette beløpet bruker vi til å redusere barnetillegget ditt for heile året." },
                        english { + " This amount will be used to reduce your child supplement during the calendar year." }
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
                    text(
                        bokmal { + "Vi tar hensyn til hvordan barnetillegget eventuelt har vært redusert tidligere, og vi har derfor " +
                                ifElse(oekeReduksjonenAvTilleggetSaerkullsbarn, "lagt til", "trukket fra")
                                + " " + justeringsbeloep.format() + " i beløpet vi reduserer barnetillegget med for resten av året." },

                        nynorsk { + "Vi tek omsyn til korleis eit barnetillegg eventuelt har vore redusert tidlegare, og har derfor " +
                                ifElse(oekeReduksjonenAvTilleggetSaerkullsbarn, "lagt til", "trekt frå")
                                + " " + justeringsbeloep.format() + " i beløpet vi reduserer barnetillegget med for resten av året." },

                        english { + "We take into account how the child supplement has been reduced earlier this year. The amount with which your child supplement will be reduced for the rest of the year has therefore been " +
                                ifElse(oekeReduksjonenAvTilleggetSaerkullsbarn, "increased", "reduced")
                                + " with " + justeringsbeloep.format() + "." }
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
                    text(
                        bokmal { + "Du vil få utbetalt " + saerkullTilleggNetto.format() + " i måneden før skatt i barnetillegg for " +
                                ifElse(harFlereSaerkullsbarn, "barna", "barnet")
                                + " som ikke bor med begge sine foreldre." },

                        nynorsk { + "Du vil få utbetalt " + saerkullTilleggNetto.format() + " i månaden før skatt i barnetillegg for " +
                                ifElse(harFlereSaerkullsbarn, "barna", "barnet")
                                + " som ikkje bur saman med begge foreldra." },

                        english { + "You will receive a monthly child supplement payment of " + saerkullTilleggNetto.format() +
                                " before tax for the " + ifElse(harFlereSaerkullsbarn, "children", "child")
                                + " who do not live together with both parents." }
                    )
                }.orShow {
                    text(
                        bokmal { + "Du vil få utbetalt " + saerkullTilleggNetto.format() + " i måneden før skatt i barnetillegg." },
                        nynorsk { + "Du vil få utbetalt " + saerkullTilleggNetto.format() + " i månaden før skatt i barnetillegg." },
                        english { + "You will receive a monthly child supplement payment of " + saerkullTilleggNetto.format() + " before tax." }
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
                        bokmal { + "Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året." },
                        nynorsk { + "Du har allereie fått utbetalt det du har rett til i år, og får difor ikkje utbetalt barnetillegg for resten av året." },
                        english { + "You have already received what you are entitled to this year, therefore you will not receive any child supplement for the remainder of the year." }
                    )
                }.orShowIf(harInnvilgetBarnetilleggFellesbarn) {
                    text(
                        bokmal { + "Du får ikke utbetalt barnetillegget for "
                                + ifElse(harFlereSaerkullsbarn, "barna", "barnet")
                                + " som ikke bor med begge sine foreldre fordi samlet inntekt er over grensen for å få utbetalt barnetillegg." },

                        nynorsk { + "Du får ikkje utbetalt barnetillegget for "
                                + ifElse(harFlereSaerkullsbarn, "barna", "barnet")
                                + " som ikkje bur saman med begge foreldra fordi samla inntekt er over grensa for å få utbetalt barnetillegg." },

                        english { + "You will not receive a child supplement for the "
                                + ifElse(harFlereSaerkullsbarn, "children", "child")
                                + " who do not live together with both parents because your income is over the income limit for receiving a child supplement." }
                    )
                }.orShow {
                    text(
                        bokmal { + "Du får ikke utbetalt barnetillegget fordi samlet inntekt er over grensen for å få utbetalt barnetillegg." },
                        nynorsk { + "Du får ikkje utbetalt barnetillegget fordi samla inntekt er over grensa for å få utbetalt barnetillegg." },
                        english { + "You will not receive a child supplement because your income is over the income limit for receiving a child supplement." }
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
                    text(
                        bokmal { + "Du vil få utbetalt " + barnetilleggFellesbarn.format() + " i måneden før skatt i barnetillegg for " +
                                ifElse(harFlereBarn, "barna", "barnet") +
                                " som bor med begge sine foreldre." },

                        nynorsk { + "Du vil få utbetalt " + barnetilleggFellesbarn.format() + " i månaden før skatt i barnetillegg for " +
                                ifElse(harFlereBarn, "barna", "barnet") +
                                " som bur saman med begge foreldra sine." },

                        english { + "You will receive a monthly child supplement payment of " + barnetilleggFellesbarn.format() + " for the " +
                                ifElse(harFlereBarn, "children who live", "child who lives") +
                                " together with both parents." }
                    )
                }.orShow {
                    text(
                        bokmal { + "Du vil få utbetalt " + barnetilleggFellesbarn.format() + " i måneden før skatt i barnetillegg." },
                        nynorsk { + "Du vil få utbetalt " + barnetilleggFellesbarn.format() + " i månaden før skatt i barnetillegg." },
                        english { + "You will receive a monthly child supplement payment of " + barnetilleggFellesbarn.format() + "." }
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
                        bokmal { + "Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året." },
                        nynorsk { + "Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året." },
                        english { + "You have already received what you are entitled to this year, therefore you will not receive any child supplement for the remainder of the year." }
                    )
                }.orShowIf(harInnvilgetBarnetilleggSaerkullsbarn) {
                    text(
                        bokmal { + "Du får ikke utbetalt barnetillegget for " +
                                ifElse(harFlereBarn, "barna", "barnet") +
                                " som bor med begge sine foreldre fordi samlet inntekt er over grensen for å få utbetalt barnetillegg." },

                        nynorsk { + "Du får ikkje utbetalt barnetillegget for " +
                                ifElse(harFlereBarn, "barna", "barnet") +
                                " som bur saman med begge foreldra sine fordi samla inntekt er over grensa for å få utbetalt barnetillegg." },

                        english { + "You will not receive a child supplement for the " +
                                ifElse(harFlereBarn, "children who live", "child who lives") +
                                " together with both parents because your income is over the income limit for receiving a child supplement." }
                    )
                }.orShow {
                    text(
                        bokmal { + "Du får ikke utbetalt barnetillegget fordi samlet inntekt er over grensen for å få utbetalt barnetillegg." },
                        nynorsk { + "Du får ikkje utbetalt barnetillegget fordi samla inntekt er over grensa for å få utbetalt barnetillegg." },
                        english { + "You will not receive a child supplement for the because your income is over the income limit for receiving a child supplement." }
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
                    bokmal { + "Vi fastsetter størrelsen på barnetillegget ut fra den samlede inntekten til begge foreldrene." },
                    nynorsk { + "Vi fastset storleiken på barnetillegget ut frå den samla inntekta til begge foreldra." },
                    english { + "We determine the amount of child supplement based on the total income of both parents." }
                )
            }
            paragraph {
                text(
                    bokmal { + "Barnetillegget blir redusert dersom den samlede inntekten er høyere enn fribeløpet. Fribeløpet for et barn er 4,6 ganger folketrygdens grunnbeløp og det øker med 40 prosent av folketrygdens grunnbeløp for hvert ekstra barn." },
                    nynorsk { + "Barnetillegget blir redusert dersom den samla inntekta di er høgare enn fribeløpet. Fribeløpet for eit barn er 4,6 gonger grunnbeløpet i folketrygda og det aukar med 40 prosent av grunnbeløpet for kvart ekstra barn." },
                    english { + "The child supplement will be reduced if your total income is greater than the exemption amount. The exemption amount is 4.6 times the National Insurance basic amount and it increases with 40 percent of the National Insurance basic amount for each extra child." }
                )
                showIf(harAnvendtTrygdetidUnder40 and not(harYrkesskade)) {
                    text(
                        bokmal { + " Siden trygdetiden din er kortere enn 40 år, blir fribeløpet redusert ut fra den trygdetiden du har. " },
                        nynorsk { + " Sidan trygdetida di er kortare enn 40 år, blir fribeløpet redusert ut frå den trygdetida du har. " },
                        english { + " Since you have less than 40 years National Insurance membership, the exemption amount is ruduced correspondingly in relation to the the length of time of you National Insurance membership. " }
                    )
                }
            }
            paragraph {
                text(
                    bokmal { + "Dersom begge foreldrene mottar uføretrygd blir barnetillegget gitt til den som har rett til det høyeste tillegget." },
                    nynorsk { + "Dersom begge foreldra får uføretrygd, blir barnetillegget gitt til den som har rett til det høgaste tillegget." },
                    english { + "If both parents recieve disbaility benefit, child supplement will be paid to the parent with the highest disability benefit." }
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
                    bokmal { + "Vi fastsetter størrelsen på barnetillegget ut fra inntekten din. Inntekt til en ektefelle/partner/samboer som ikke er forelder til barnet, har ikke betydning for størrelsen på barnetillegget." },
                    nynorsk { + "Vi fastset storleiken på barnetillegget ut frå inntekta di. Inntekt til ein ektefelle/partnar/sambuar som ikkje er forelder til barnet, har ikkje betydning for storleiken på barnetillegget." },
                    english { + "We determine the amount of child supplement based on your income. The income of a spouse/partner/cohabitant who is not the child's parent, is not taken into consideration." }
                )
            }
            paragraph {
                text(
                    bokmal { + "Barnetillegget blir redusert dersom den samlede inntekten din er høyere enn fribeløpet. Fribeløpet for et barn er 3,1 ganger folketrygdens grunnbeløp og det øker med 40 prosent av folketrygdens grunnbeløp for hvert ekstra barn." },
                    nynorsk { + "Barnetillegget blir redusert dersom den samla inntekta di er høgare enn fribeløpet. Fribeløpet for eit barn er 3,1 gonger grunnbeløpet i folketrygda og det aukar med 40 prosent av grunnbeløpet for kvart ekstra barn." },
                    english { + "The child supplement will be reduced if your total income is greater than the exemption amount. The exemption amount is 3.1 times the National Insurance basic amount and it increases with 40 percent of the National Insurance basic amount for each extra child." }
                )
            }
            showIf(harAnvendtTrygdetidUnder40 and not(harYrkesskade)) {
                paragraph {
                    text(
                        bokmal { + "Siden trygdetiden din er kortere enn 40 år, blir fribeløpet redusert ut fra den trygdetiden du har. " },
                        nynorsk { + "Sidan trygdetida di er kortare enn 40 år, blir fribeløpet redusert ut frå den trygdetida du har. " },
                        english { + "Since you have less than 40 years National Insurance membership, the exemption amount is ruduced correspondingly in relation to the the length of time of you National Insurance membership. " }
                    )
                }
            }
            paragraph {
                text(
                    bokmal { + "Dersom begge foreldrene mottar uføretrygd blir barnetillegget gitt til den som har den daglige omsorgen for barnet. Har foreldrene delt omsorg for barnet, blir barnetillegget gitt til den forelderen som bor på samme folkeregistrerte adresse som barnet." },
                    nynorsk { + "Dersom begge foreldra får uføretrygd, blir barnetillegget gitt til den som har den daglege omsorga for barnet. Har foreldra delt omsorg for barnet, blir barnetillegget gitt til den forelderen som bur på same folkeregistrerte adresse som barnet." },
                    english { + "If both parents recieve disbaility benefit, child supplement will be paid to the parent with guardianship of the child. If both parents have guardianship of the child, child supplement is given to the parent who lives at the same registered address as the child." }
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
                text(
                    bokmal { + "Vi fastsetter størrelsen på barnetillegget ut fra inntekten til deg og din " +
                            borMedSivilstand.ubestemtForm() + " for " +
                            ifElse(harTilleggForFlereFellesbarn, "barna", "barnet") +
                            " som bor med begge sine foreldre. Barnetillegget blir redusert dersom den samlede inntekten er høyere enn fribeløpet. Fribeløpet for et barn som bor med begge foreldrene er 4,6 ganger folketrygdens grunnbeløp, og øker med 40 prosent av folketrygdens grunnbeløp for hvert ekstra barn." },

                    nynorsk { + "Vi fastset storleiken på barnetillegget ut frå inntekta til deg og din " +
                            borMedSivilstand.ubestemtForm() + " for " +
                            ifElse(harTilleggForFlereFellesbarn, "barna", "barnet") +
                            " som bur med begge foreldra sine. Barnetillegget blir redusert dersom den samla inntekta er høgare enn fribeløpet. Fribeløpet for eit barn som bur med begge foreldra, er 4,6 gonger grunnbeløpet i folketrygda, og aukar med 40 prosent av grunnbeløpet i folketrygda for kvart ekstra barn." },

                    english { + "We determine the amount of child supplement based on the total income for you and your " +
                            borMedSivilstand.ubestemtForm() + " for the " +
                            ifElse(harTilleggForFlereFellesbarn, "children who live", "child who lives") +
                            " with both parents. The child supplement will be reduced if your total income is greater than the exemption amount. The exemption amount is 4.6 times the National Insurance basic amount and it increases with 40 percent of the National Insurance basic amount for each extra child." },
                )
            }
            paragraph {
                text(
                    bokmal { + "For " +
                            ifElse(harTilleggForFlereSaerkullsbarn, "barna", "barnet")
                            + " som ikke bor sammen med begge foreldre, fastsetter vi størrelsen på barnetillegget ut fra inntekten din. Inntekt til en ektefelle/partner/samboer som ikke er forelder til barnet, har ikke betydning for størrelsen på barnetillegget. Barnetillegget blir redusert dersom den samlede inntekten din er høyere enn fribeløpet. Fribeløpet for et barn som ikke bor sammen med begge foreldrene er 3,1 ganger folketrygdens grunnbeløp, og øker med 40 prosent av folketrygdens grunnbeløp for hvert ekstra barn." },
                    nynorsk { + "For " +
                            ifElse(harTilleggForFlereSaerkullsbarn, "barna", "barnet")
                            + " som ikkje bur saman med begge foreldra, fastset vi storleiken på barnetillegget ut frå inntekta di. Inntekt til ein ektefelle/partnar/sambuar som ikkje er forelder til barnet, har ikkje betydning for storleiken på barnetillegget. Barnetillegget blir redusert dersom den samla inntekta di er høgare enn fribeløpet. Fribeløpet for eit barn som ikkje bur saman med begge foreldra, er 3,1 gonger grunnbeløpet i folketrygda, og aukar med 40 prosent av grunnbeløpet i folketrygda for kvart ekstra barn." },
                    english { + "For the " +
                            ifElse(harTilleggForFlereSaerkullsbarn, "children who do", "child who does")
                            + " not live together with both parents, the amount of child supplement is based on your income. The income of a spouse/partner/cohabitant who is not the child's parent, is not taken into consideration. The child supplement will be reduced if your total income is greater than the exemption amount. The exemption amount is 3.1 times the National Insurance basic amount and it increases with 40 percent of the National Insurance basic amount for each extra child." },
                )
            }
            showIf(harAnvendtTrygdetidUnder40 and not(harYrkesskade)) {
                paragraph {
                    text(
                        bokmal { + " Siden trygdetiden din er kortere enn 40 år, blir fribeløpet redusert ut fra den trygdetiden du har." },
                        nynorsk { + " Sidan trygdetida di er kortare enn 40 år, blir fribeløpet redusert ut frå den trygdetida du har." },
                        english { + " Since you have less than 40 years National Insurance membership, the exemption amount is ruduced correspondingly in relation to how long you have had National Insurance membership." }
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
                    text(bokmal { + "Når inntekten " }, nynorsk { + "Når inntekta " }, english { + "When " })
                    ifNotNull(borMedSivilstandFelles) { borMedSivilstandFelles ->
                        text(
                            bokmal { + "til deg eller " + borMedSivilstandFelles.bestemtForm() + " din" },
                            nynorsk { + "di eller til " + borMedSivilstandFelles.bestemtForm() + " din" },
                            english { + "your or your " + borMedSivilstandFelles.ubestemtForm() + "'s" },
                        )
                    }.orShow {
                        text(
                            bokmal { + "din" },
                            nynorsk { + "di" },
                            english { + "your" },
                        )
                    }
                    text(
                        bokmal { + " endrer seg" },
                        nynorsk { + " endrar seg" },
                        english { + "" },
                    )
                }.orShow {
                    text(
                        bokmal { + "Har det vært en endring i inntekten " },
                        nynorsk { + "Har det vore ei endring i inntekta " },
                        english { + "If " },
                    )
                    ifNotNull(borMedSivilstandFelles) { borMedSivilstandFelles ->
                        text(
                            bokmal { + "til deg eller " + borMedSivilstandFelles.bestemtForm() + " din" },
                            nynorsk { + "til deg eller " + borMedSivilstandFelles.bestemtForm() + " din" },
                            english { + "your or your " + borMedSivilstandFelles.ubestemtForm() + "'s" },
                        )
                    }.orShow {
                        text(
                            bokmal { + "din" },
                            nynorsk { + "di" },
                            english { + "your" },
                        )
                    }
                }

                text(
                    bokmal { + " blir reduksjonen av " +
                            ifElse(harFlereTillegg, "barnetilleggene", "barnetillegget") +
                            " vurdert på nytt." },
                    nynorsk { + " blir reduksjonen av " +
                            ifElse(harFlereTillegg, "barnetilleggene", "barnetillegget") +
                            " vurdert på nytt." },
                    english { + " income has been changed, your child supplement will be recalculated." },
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
                text(
                    bokmal { + "50 prosent av den inntekten som overstiger fribeløpet for "
                            + ifElse(harTilleggForFlereFellesbarn, "barna", "barnet") +
                            " som bor med begge sine foreldre " },

                    nynorsk { + "50 prosent av inntekta som overstig fribeløpet for "
                            + ifElse(harTilleggForFlereFellesbarn, "barna", "barnet") +
                            " som bur med begge foreldra sine " },

                    english { + "50 percent of income that exceeds the exemption amount for the "
                            + ifElse(harTilleggForFlereFellesbarn, "children", "child") +
                            " that " + ifElse(harTilleggForFlereFellesbarn, "live", "lives") +
                            " with both of their parents, " },
                )
                showIf(inntektEllerFribeloepErPeriodisert) {
                    text(
                        bokmal { + "blir omregnet til et årlig beløp som tilsvarer" },
                        nynorsk { + "blir rekna om til et årleg beløp som svarer til" },
                        english { + "is recalculated to an annual amount of" },
                    )
                }.orShow {
                    text(
                        bokmal { + "er" },
                        nynorsk { + "er" },
                        english { + "is" },
                    )
                }

                showIf(avkortningsbeloepAar.greaterThan(0)) {
                    text(
                        bokmal { + " " + avkortningsbeloepAar.format() + "." },
                        nynorsk { + " " + avkortningsbeloepAar.format() + "." },
                        english { + " " + avkortningsbeloepAar.format() + "." },
                    )
                }
                showIf(not(harJusteringsbeloep)) {
                    text(
                        bokmal { + " Dette beløpet bruker vi til å redusere dette barnetillegget for hele året." },
                        nynorsk { + " Dette beløpet bruker vi til å redusere dette barnetillegget for heile året." },
                        english { + " This amount will be used to reduce your child supplement during the calendar year." }
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
                    bokmal { + "50 prosent av den inntekten som overstiger fribeløpet " },
                    nynorsk { + "50 prosent av inntekta som overstig fribeløpet " },
                    english { + "50 percent of income that exceeds the exemption amount " },
                )
                showIf(inntektEllerFribeloepErPeriodisert) {
                    text(
                        bokmal { + "blir omregnet til et årlig beløp som tilsvarer" },
                        nynorsk { + "blir rekna om til et årleg beløp som svarer til" },
                        english { + "is recalculated to an annual amount of" },
                    )
                }.orShow {
                    text(
                        bokmal { + "er" },
                        nynorsk { + "er" },
                        english { + "is" },
                    )
                }

                text(
                    bokmal { + " " + avkortningsbeloepAar.format() + "." },
                    nynorsk { + " " + avkortningsbeloepAar.format() + "." },
                    english { + " " + avkortningsbeloepAar.format() + "." },
                )

                showIf(not(harJusteringsbeloep)) {
                    text(
                        bokmal { + " Dette beløpet bruker vi til å redusere barnetillegget ditt for hele året." },
                        nynorsk { + " Dette beløpet bruker vi til å redusere barnetillegget ditt for heile året." },
                        english { + " This amount will be used to reduce your child supplement during the calendar year." }
                    )
                }
            }
        }
    }
}