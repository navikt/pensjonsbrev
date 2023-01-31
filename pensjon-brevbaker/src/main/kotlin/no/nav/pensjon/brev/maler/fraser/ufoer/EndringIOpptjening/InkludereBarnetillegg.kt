package no.nav.pensjon.brev.maler.fraser.ufoer.EndringIOpptjening

import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.api.model.KronerSelectors.value
import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.api.model.vedlegg.BarnetilleggGjeldendeSelectors.fellesbarn_safe
import no.nav.pensjon.brev.api.model.vedlegg.BarnetilleggGjeldendeSelectors.saerkullsbarn_safe
import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.avkortningsbeloepAar
import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.avkortningsbeloepAar_safe
import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.beloepAarBrutto
import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.beloepAarNetto
import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.beloepBrutto
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
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDto
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.avkortningsbeloepAar
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.avkortningsbeloepAar_safe
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.beloepAarBrutto
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.beloepAarNetto
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.beloepBrutto
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
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.ufoer.Barnetillegg
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr

data class InkludereBarnetillegg(
    val barnetillegg: Expression<OpplysningerBruktIBeregningUTDto.BarnetilleggGjeldende>,
    val sivilstand: Expression<Sivilstand>,

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


        showIf(harBarnetillegg) {
            title1 {
                text(
                    Language.Bokmal to "Slik påvirker inntekt barnetillegget ditt",
                    Language.Nynorsk to "Slik verkar inntekt inn på barnetillegget ditt",
                    Language.English to "Income will affect your child supplement"
                )
            }

            ifNotNull(barnetilleggSaerkullsbarn) { barnetilleggSaerkullsbarn ->
                includePhrase(
                    Barnetillegg.InntektHarBetydningForSaerkullsbarnTillegg(
                        harBarnetilleggFellesbarn = harBarnetilleggFellesbarn,
                        harBarnetilleggSaerkullsbarn = harBarnetilleggSaerkullsbarn,
                        sivilstand = sivilstand,
                        faarUtbetaltBarnetilleggSaerkullsbarn = barnetilleggSaerkullsbarn.beloepNetto.greaterThan(0),
                    )
                )
            }

            ifNotNull(barnetilleggFellesbarn) { barnetilleggFellesBarn ->
                includePhrase(
                    Barnetillegg.InntektHarBetydningForFellesbarnTillegg(
                        faarUtbetaltBarnetilleggFellesbarn = barnetilleggFellesBarn.beloepNetto.greaterThan(0),
                        harBarnetilleggFellesbarn = harBarnetilleggFellesbarn,
                        harBarnetilleggSaerkullsbarn = harBarnetilleggSaerkullsbarn,
                        sivilstand = sivilstand
                    )
                )
            }

            includePhrase(
                Barnetillegg.BetydningAvInntektEndringer(
                    harBarnetilleggFellesbarn = harBarnetilleggFellesbarn,
                    harBarnetilleggSaerkullsbarn = harBarnetilleggSaerkullsbarn,
                    sivilstand = sivilstand
                )
            )

            ifNotNull(barnetilleggFellesbarn) { barnetilleggFellesbarn ->
                includePhrase(
                    Barnetillegg.InntektTilAvkortningFellesbarn(
                        harBeloepFratrukketAnnenForelder = barnetilleggFellesbarn.harFratrukketBeloepFraAnnenForelder,
                        faarUtbetaltBarnetilleggFellesBarn = barnetilleggFellesbarn.beloepNetto.greaterThan(0),
                        harFradragFellesbarn = barnetilleggFellesbarn.harFradrag,
                        fribeloepFellesbarn = barnetilleggFellesbarn.fribeloep,
                        inntektAnnenForelderFellesbarn = barnetilleggFellesbarn.inntektAnnenForelder,
                        inntektBruktiAvkortningFellesbarn = barnetilleggFellesbarn.inntektBruktIAvkortning,
                        harJusteringsbeloepFellesbarn = barnetilleggFellesbarn.harJusteringsbeloep,
                        grunnbeloep = grunnbeloep,
                        harBarnetilleggSaerkullsbarn = harBarnetilleggSaerkullsbarn,
                        sivilstand = sivilstand,
                    )
                )
            }

            ifNotNull(barnetilleggSaerkullsbarn) { barnetilleggSaerkullsbarn ->
                includePhrase(
                    Barnetillegg.InntektTilAvkortningSaerkullsbarn(
                        beloepNettoSaerkullsbarn = barnetilleggSaerkullsbarn.beloepNetto,
                        fribeloepSaerkullsbarn = barnetilleggSaerkullsbarn.fribeloep,
                        inntektBruktIAvkortningSaerkullsbarn = barnetilleggSaerkullsbarn.inntektBruktIAvkortning,
                        harJusteringsbeloepSaerkullsbarn = barnetilleggSaerkullsbarn.harJusteringsbeloep,
                    )
                )
            }

            ifNotNull(
                barnetilleggFellesbarn,
                barnetilleggSaerkullsbarn
            ) { barnetilleggFellesbarn, barnetilleggSaerkullsbarn ->
                includePhrase(
                    Barnetillegg.BarnetilleggReduksjonSaerkullsbarnFellesbarn(
                        beloepNettoSaerkullsbarn = barnetilleggSaerkullsbarn.beloepNetto,
                        beloepBruttoSaerkullsbarn = barnetilleggSaerkullsbarn.beloepBrutto,
                        harFradragSaerkullsbarn = barnetilleggSaerkullsbarn.harFradrag,
                        fribeloepSaerkullsbarn = barnetilleggSaerkullsbarn.fribeloep,
                        harJusteringsbeloepSaerkullsbarn = barnetilleggSaerkullsbarn.harJusteringsbeloep,
                        harTilleggForFlereSaerkullsbarn = barnetilleggSaerkullsbarn.gjelderFlereBarn,
                        harBarnetilleggSaerkullsbarn = harBarnetilleggSaerkullsbarn,
                        inntektBruktIAvkortningSaerkullsbarn = barnetilleggSaerkullsbarn.inntektBruktIAvkortning,
                        harFradragFellesbarn = barnetilleggFellesbarn.harFradrag,
                        beloepBruttoFellesbarn = barnetilleggFellesbarn.beloepBrutto,
                        harBarnetilleggFellesbarn = harBarnetilleggFellesbarn,
                        beloepNettoFellesbarn = barnetilleggFellesbarn.beloepNetto,
                        fribeloepFellesbarn = barnetilleggFellesbarn.fribeloep,
                        harJusteringsbeloepFellesbarn = barnetilleggFellesbarn.harJusteringsbeloep,
                        harTilleggForFlereFellesbarn = barnetilleggFellesbarn.gjelderFlereBarn,
                        inntektBruktiAvkortningFellesbarn = barnetilleggFellesbarn.inntektBruktIAvkortning,
                        sivilstand = sivilstand,
                    )
                )
            }

            includePhrase(
                Barnetillegg.BarnetilleggIkkeUtbetalt(
                    fellesInnvilget = barnetilleggFellesbarn.notNull(),
                    fellesUtbetalt = barnetilleggFellesbarn.beloepNetto_safe.value_safe.ifNull(0).greaterThan(0),
                    harFlereFellesBarn = barnetilleggFellesbarn.gjelderFlereBarn_safe.ifNull(false),
                    harFlereSaerkullsbarn = barnetilleggSaerkullsbarn.gjelderFlereBarn_safe.ifNull(false),
                    inntektstakFellesbarn = barnetilleggFellesbarn.inntektstak_safe.ifNull(Kroner(0)),
                    inntektstakSaerkullsbarn = barnetilleggSaerkullsbarn.inntektstak_safe.ifNull(Kroner(0)),
                    saerkullInnvilget = barnetilleggSaerkullsbarn.notNull(),
                    saerkullUtbetalt = barnetilleggSaerkullsbarn.beloepNetto_safe.value_safe.ifNull(0)
                        .greaterThan(0),
                )
            )

            ifNotNull(
                barnetilleggFellesbarn,
                barnetilleggSaerkullsbarn
            ) { barnetilleggFellesbarn, barnetilleggSaerkullsbarn ->
                includePhrase(
                    Barnetillegg.InnvilgetOgIkkeUtbetalt(
                        fellesInnvilget = barnetilleggFellesbarn.notNull(),
                        fellesUtbetalt = barnetilleggFellesbarn.beloepNetto_safe.value_safe.ifNull(0)
                            .greaterThan(0),
                        harTilleggForFlereFellesbarn = barnetilleggFellesbarn.gjelderFlereBarn,
                        harTilleggForFlereSaerkullsbarn = barnetilleggSaerkullsbarn.gjelderFlereBarn,
                        inntektstakFellesbarn = barnetilleggFellesbarn.inntektstak,
                        inntektstakSaerkullsbarn = barnetilleggSaerkullsbarn.inntektstak,
                        saerkullInnvilget = barnetilleggSaerkullsbarn.notNull(),
                        saerkullUtbetalt = barnetilleggSaerkullsbarn.beloepNetto_safe.value_safe.ifNull(0)
                            .greaterThan(0),
                    )
                )
            }

            includePhrase(
                Barnetillegg.HenvisningTilVedleggOpplysningerOmBeregning(
                    harBarnetilleggSaerkullsbarn = harBarnetilleggSaerkullsbarn,
                    harBarnetilleggFellesbarn = harBarnetilleggFellesbarn
                )
            )
        }
    }
}
