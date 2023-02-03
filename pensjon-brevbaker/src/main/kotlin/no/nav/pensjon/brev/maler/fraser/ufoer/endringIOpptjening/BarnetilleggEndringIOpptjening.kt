package no.nav.pensjon.brev.maler.fraser.ufoer.endringIOpptjening

import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.api.model.KronerSelectors.value_safe
import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.beloepBrutto
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.beloepNetto
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.beloepNetto_safe
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.fribeloep
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.gjelderFlereBarn
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.gjelderFlereBarn_safe
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.harFradrag
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.harFratrukketBeloepFraAnnenForelder
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.harJusteringsbeloep
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.inntektAnnenForelder
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.inntektBruktIAvkortning
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.inntektstak
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.inntektstak_safe
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnSelectors.beloepBrutto
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnSelectors.beloepNetto
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnSelectors.beloepNetto_safe
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnSelectors.fribeloep
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnSelectors.gjelderFlereBarn
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnSelectors.gjelderFlereBarn_safe
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnSelectors.harFradrag
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnSelectors.harJusteringsbeloep
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnSelectors.inntektBruktIAvkortning
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnSelectors.inntektstak
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnSelectors.inntektstak_safe
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningAutoDto
import no.nav.pensjon.brev.maler.fraser.ufoer.Barnetillegg
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text

data class BarnetilleggEndringIOpptjening(
    val barnetilleggFellesbarn: Expression<EndringIOpptjeningAutoDto.FellesbarnTillegg>,
    val barnetilleggSaerkullsbarn: Expression<EndringIOpptjeningAutoDto.SaerkullsbarnTillegg>,
    val grunnbeloep: Expression<Kroner>,
    val sivilstand: Expression<Sivilstand>,

    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        val harBarnetilleggFellesbarn = barnetilleggFellesbarn.notNull()
        val harBarnetilleggSaerkullsbarn = barnetilleggSaerkullsbarn.notNull()
        val harBarnetillegg = harBarnetilleggFellesbarn or harBarnetilleggSaerkullsbarn
        showIf(harBarnetillegg) {
            title1 {
                text(
                    Language.Bokmal to "Slik påvirker inntekt barnetillegget ditt",
                    Language.Nynorsk to "Slik verkar inntekt inn på barnetillegget ditt",
                    Language.English to "Income will affect your child supplement"
                )
            }

            ifNotNull(harBarnetilleggSaerkullsbarn) { harBarnetilleggSaerkullsbarn ->
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
