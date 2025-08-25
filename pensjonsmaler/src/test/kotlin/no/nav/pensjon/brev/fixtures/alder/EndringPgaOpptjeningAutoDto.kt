package no.nav.pensjon.brev.fixtures.alder

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.BeloepEndring
import no.nav.pensjon.brev.api.model.maler.alderApi.BeregnetPensjonPerMaaned
import no.nav.pensjon.brev.api.model.maler.alderApi.BeregnetPensjonPerMaanedGjeldende
import no.nav.pensjon.brev.api.model.maler.alderApi.BeregnetPensjonPerMaanedVedVirk
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringPgaOpptjeningAutoDto
import no.nav.pensjon.brev.api.model.maler.alderApi.Opptjening
import no.nav.pensjon.brev.api.model.maler.alderApi.OpptjeningType
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

fun createEndringPgaOpptjeningAutoDto() = EndringPgaOpptjeningAutoDto(
    opptjeningType = OpptjeningType.TILVEKST,
    opptjening = Opptjening(
        sisteGyldigeOpptjeningsAar = 2023,
        antallAarEndretOpptjening = 2,
        endretOpptjeningsAar = emptySet()
    ),
    belopEndring = BeloepEndring.ENDR_OKT,
    beregnetPensjonPerMaaned = BeregnetPensjonPerMaaned(antallBeregningsperioderPensjon = 1),
    beregnetPensjonPerMaanedGjeldende = BeregnetPensjonPerMaanedGjeldende(
        totalPensjon = Kroner(20000),
        virkFom = LocalDate.of(2025, 1, 1),
    ),
    beregnetPensjonPerMaanedVedVirk = BeregnetPensjonPerMaanedVedVirk(
        uttaksgrad = 100,
        totalPensjon = Kroner(21000),
        virkFom = LocalDate.of(2025, 1, 1),
        minstenivaIndividuellInnvilget = false,
        pensjonstilleggInnvilget = true,
        minstenivaPensjonistParInnvilget = false,
        gjenlevenderettAnvendt = false,
        garantipensjonInnvilget = false,
    ),
    virkFom = LocalDate.of(2025, 1, 1),
    borINorge = true,
    erFoerstegangsbehandling = false,
    uforeKombinertMedAlder = false,
    regelverkType = AlderspensjonRegelverkType.AP2016,
)
