package no.nav.pensjon.brev.fixtures.alder

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.BeloepEndring
import no.nav.pensjon.brev.api.model.maler.alderApi.BeregnetPensjonPerMaaned
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringPgaOpptjeningAutoDto
import no.nav.pensjon.brev.api.model.maler.alderApi.Opptjening
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

fun createEndringPgaOpptjeningAutoDto() = EndringPgaOpptjeningAutoDto(
    opptjening = Opptjening.TILVEKST,
    sisteGyldigeOpptjeningsAar = 2023,
    antallAarEndretOpptjening = 2,
    belopEndring = BeloepEndring.ENDR_OKT,
    beregnetPensjonPerMaaned = beregnetPensjonPerMaaned(),
    beregnetPensjonPerMaanedGjeldende = beregnetPensjonPerMaaned(),
    beregnetPensjonPerMaanedVedVirk = beregnetPensjonPerMaaned(),
    virkFom = LocalDate.of(2025, 1, 1),
    borINorge = true,
    erFoerstegangsbehandling = false,
    uforeKombinertMedAlder = false,
    regelverkType = AlderspensjonRegelverkType.AP2016,
    endretOpptjeningsAar = emptySet()
)

private fun beregnetPensjonPerMaaned() = BeregnetPensjonPerMaaned(
    uttaksgrad = 100,
    totalPensjon = Kroner(21000),
    virkFom = LocalDate.of(2025, 1, 1),
    antallBeregningsperioderPensjon = 1,
    minstenivaIndividuellInnvilget = false,
    pensjonstilleggInnvilget = true,
    minstenivaPensjonistParInnvilget = false,
    gjenlevenderettAnvendt = false,
    garantipensjonInnvilget = false,
)