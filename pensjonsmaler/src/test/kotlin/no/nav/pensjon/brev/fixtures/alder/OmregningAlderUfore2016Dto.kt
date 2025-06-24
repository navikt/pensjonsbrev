package no.nav.pensjon.brev.fixtures.alder

import no.nav.pensjon.brev.api.model.maler.alderApi.AlderspensjonPerManed
import no.nav.pensjon.brev.api.model.maler.alderApi.InngangOgEksportVurdering
import no.nav.pensjon.brev.api.model.maler.alderApi.OmregningAlderUfore2016Dto
import no.nav.pensjon.brev.api.model.maler.alderApi.PersongrunnlagAvdod
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

fun createOmregningAlderUfore2016Dto() =
        OmregningAlderUfore2016Dto(
            virkFom = LocalDate.now(),
            uttaksgrad = 100,
            totalPensjon = Kroner(1000),
            beregningsperioder = listOf(
                AlderspensjonPerManed(
                    virkFom = LocalDate.now()
                )
            ),
            gjenlevendetilleggKap19Innvilget = true,
            gjenlevenderettAnvendt = true,
            inngangOgEksportVurdering = InngangOgEksportVurdering(
                eksportTrygdeavtaleAvtaleland = true,
                faktiskBostedsland = "Skottland",
                erEksportberegnet = true,
                eksportberegnetUtenGarantipensjon = true,
                borINorge = true,
                erEOSLand = true,
                eksportTrygdeavtaleEOS = true,
                avtaleland = "Australia",
                oppfyltVedSammenleggingKap19 = true,
                oppfyltVedSammenleggingKap20 = true,
                oppfyltVedSammenleggingFemArKap19 = true,
                oppfyltVedSammenleggingFemArKap20 = true,
            ),
            pensjonstilleggInnvilget = true,
            garantipensjonInnvilget = true,
            godkjentYrkesskade = true,
            skjermingstilleggInnvilget = true,
            garantitilleggInnvilget = true,
            innvilgetFor67 = true,
            fullTrygdetid = true,
            persongrunnlagAvdod = PersongrunnlagAvdod(
                avdodNavn = "Per",
                avdodFnr = "13377331"
            )
        )

