package no.nav.pensjon.brev.maler.fraser.ufoer.endringIOpptjening

import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.api.model.KronerSelectors.value_safe
import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningAutoDto
import no.nav.pensjon.brev.api.model.maler.Fellesbarn1Selectors.beloepBrutto
import no.nav.pensjon.brev.api.model.maler.Fellesbarn1Selectors.beloepNetto
import no.nav.pensjon.brev.api.model.maler.Fellesbarn1Selectors.beloepNetto_safe
import no.nav.pensjon.brev.api.model.maler.Fellesbarn1Selectors.fribeloep
import no.nav.pensjon.brev.api.model.maler.Fellesbarn1Selectors.gjelderFlereBarn
import no.nav.pensjon.brev.api.model.maler.Fellesbarn1Selectors.gjelderFlereBarn_safe
import no.nav.pensjon.brev.api.model.maler.Fellesbarn1Selectors.harFradrag
import no.nav.pensjon.brev.api.model.maler.Fellesbarn1Selectors.harFratrukketBeloepFraAnnenForelder
import no.nav.pensjon.brev.api.model.maler.Fellesbarn1Selectors.harJusteringsbeloep
import no.nav.pensjon.brev.api.model.maler.Fellesbarn1Selectors.inntektAnnenForelder
import no.nav.pensjon.brev.api.model.maler.Fellesbarn1Selectors.inntektBruktIAvkortning
import no.nav.pensjon.brev.api.model.maler.Fellesbarn1Selectors.inntektstak
import no.nav.pensjon.brev.api.model.maler.Fellesbarn1Selectors.inntektstak_safe
import no.nav.pensjon.brev.api.model.maler.Saerkullsbarn1Selectors.beloepBrutto
import no.nav.pensjon.brev.api.model.maler.Saerkullsbarn1Selectors.beloepNetto
import no.nav.pensjon.brev.api.model.maler.Saerkullsbarn1Selectors.beloepNetto_safe
import no.nav.pensjon.brev.api.model.maler.Saerkullsbarn1Selectors.fribeloep
import no.nav.pensjon.brev.api.model.maler.Saerkullsbarn1Selectors.gjelderFlereBarn
import no.nav.pensjon.brev.api.model.maler.Saerkullsbarn1Selectors.gjelderFlereBarn_safe
import no.nav.pensjon.brev.api.model.maler.Saerkullsbarn1Selectors.harFradrag
import no.nav.pensjon.brev.api.model.maler.Saerkullsbarn1Selectors.harJusteringsbeloep
import no.nav.pensjon.brev.api.model.maler.Saerkullsbarn1Selectors.inntektBruktIAvkortning
import no.nav.pensjon.brev.api.model.maler.Saerkullsbarn1Selectors.inntektstak
import no.nav.pensjon.brev.api.model.maler.Saerkullsbarn1Selectors.inntektstak_safe
import no.nav.pensjon.brev.maler.fraser.ufoer.Barnetillegg
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text

data class BarnetilleggEndringIOpptjening(
    val barnetilleggFellesbarn: Expression<EndringIOpptjeningAutoDto.Fellesbarn1?>,
    val barnetilleggSaerkullsbarn: Expression<EndringIOpptjeningAutoDto.Saerkullsbarn1?>,
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
        }

        ifNotNull(barnetilleggSaerkullsbarn) { barnetilleggSaerkullsbarn ->
            includePhrase(
                Barnetillegg.InntektHarBetydningForSaerkullsbarnTillegg(
                    faarUtbetaltBarnetilleggSaerkullsbarn = barnetilleggSaerkullsbarn.beloepNetto.greaterThan(0),
                    harBarnetilleggFellesbarn = harBarnetilleggFellesbarn,
                    harBarnetilleggSaerkullsbarn = harBarnetilleggSaerkullsbarn,
                    sivilstand = sivilstand,
                )
            )
        }

        ifNotNull(barnetilleggFellesbarn) { barnetilleggFellesbarn ->
            includePhrase(
                Barnetillegg.InntektHarBetydningForFellesbarnTillegg(
                    faarUtbetaltBarnetilleggFellesbarn = barnetilleggFellesbarn.beloepNetto.greaterThan(0),
                    harBarnetilleggFellesbarn = harBarnetilleggFellesbarn,
                    harBarnetilleggSaerkullsbarn = harBarnetilleggSaerkullsbarn,
                    harFlereFellesbarn = barnetilleggFellesbarn.gjelderFlereBarn,
                    sivilstand = sivilstand
                )
            )
        }

            includePhrase(
                Barnetillegg.BetydningAvInntektEndringer(
                    harBarnetilleggFellesbarn = harBarnetilleggFellesbarn.notNull(),
                    harBarnetilleggSaerkullsbarn = harBarnetilleggSaerkullsbarn.notNull(),
                    sivilstand = sivilstand
                )
            )

        ifNotNull(barnetilleggFellesbarn) { barnetilleggFellesbarn ->
            includePhrase(
                Barnetillegg.InntektTilAvkortningFellesbarn(
                    faarUtbetaltBarnetilleggFellesBarn = barnetilleggFellesbarn.beloepNetto.greaterThan(0),
                    fribeloepFellesbarn = barnetilleggFellesbarn.fribeloep,
                    grunnbeloep = grunnbeloep,
                    harBarnetilleggSaerkullsbarn = harBarnetilleggSaerkullsbarn.notNull(),
                    harBeloepFratrukketAnnenForelder = barnetilleggFellesbarn.harFratrukketBeloepFraAnnenForelder,
                    harFradragFellesbarn = barnetilleggFellesbarn.harFradrag,
                    harJusteringsbeloepFellesbarn = barnetilleggFellesbarn.harJusteringsbeloep,
                    inntektAnnenForelderFellesbarn = barnetilleggFellesbarn.inntektAnnenForelder,
                    inntektBruktiAvkortningFellesbarn = barnetilleggFellesbarn.inntektBruktIAvkortning,
                    sivilstand = sivilstand,
                )
            )
        }

        ifNotNull(barnetilleggSaerkullsbarn) { barnetilleggSaerkullsbarn ->
            includePhrase(
                Barnetillegg.InntektTilAvkortningSaerkullsbarn(
                    beloepNettoSaerkullsbarn = barnetilleggSaerkullsbarn.beloepNetto,
                    fribeloepSaerkullsbarn = barnetilleggSaerkullsbarn.fribeloep,
                    harJusteringsbeloepSaerkullsbarn = barnetilleggSaerkullsbarn.harJusteringsbeloep,
                    inntektBruktIAvkortningSaerkullsbarn = barnetilleggSaerkullsbarn.inntektBruktIAvkortning,
                )
            )
        }

        ifNotNull(
            barnetilleggFellesbarn,
            barnetilleggSaerkullsbarn
        ) { barnetilleggFellesbarn, barnetilleggSaerkullsbarn ->
            includePhrase(
                Barnetillegg.BarnetilleggReduksjonSaerkullsbarnFellesbarn(
                    beloepBruttoFellesbarn = barnetilleggFellesbarn.beloepBrutto,
                    beloepBruttoSaerkullsbarn = barnetilleggSaerkullsbarn.beloepBrutto,
                    beloepNettoFellesbarn = barnetilleggFellesbarn.beloepNetto,
                    beloepNettoSaerkullsbarn = barnetilleggSaerkullsbarn.beloepNetto,
                    fribeloepFellesbarn = barnetilleggFellesbarn.fribeloep,
                    fribeloepSaerkullsbarn = barnetilleggSaerkullsbarn.fribeloep,
                    harBarnetilleggFellesbarn = harBarnetilleggFellesbarn,
                    harBarnetilleggSaerkullsbarn = harBarnetilleggSaerkullsbarn,
                    harFradragFellesbarn = barnetilleggFellesbarn.harFradrag,
                    harFradragSaerkullsbarn = barnetilleggSaerkullsbarn.harFradrag,
                    harJusteringsbeloepFellesbarn = barnetilleggFellesbarn.harJusteringsbeloep,
                    harJusteringsbeloepSaerkullsbarn = barnetilleggSaerkullsbarn.harJusteringsbeloep,
                    harFlereFellesbarn = barnetilleggFellesbarn.gjelderFlereBarn,
                    harFlereSaerkullsbarn = barnetilleggSaerkullsbarn.gjelderFlereBarn,
                    inntektBruktIAvkortningSaerkullsbarn = barnetilleggSaerkullsbarn.inntektBruktIAvkortning,
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
                    harFlereFellesbarn = barnetilleggFellesbarn.gjelderFlereBarn,
                    harFlereSaerkullsbarn = barnetilleggSaerkullsbarn.gjelderFlereBarn,
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
                harBarnetilleggFellesbarn = harBarnetilleggFellesbarn.notNull(),
                harBarnetilleggSaerkullsbarn = harBarnetilleggSaerkullsbarn.notNull(),
            )
        )
    }
}

