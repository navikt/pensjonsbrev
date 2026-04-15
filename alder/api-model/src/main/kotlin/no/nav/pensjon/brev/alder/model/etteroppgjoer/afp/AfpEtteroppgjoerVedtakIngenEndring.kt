package no.nav.pensjon.brev.alder.model.etteroppgjoer.afp

import no.nav.pensjon.brev.api.model.maler.AutobrevData
import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import java.time.LocalDate

@Suppress("unused")
data class AfpEtteroppgjoerVedtakIngenEndringAutoDto(
    val afpEtteroppgjoerVedtak: AfpEtteroppgjoerVedtakDto,
    val innsenderEnhetNavn: String
) : AutobrevData

@Suppress("unused")
data class AfpEtteroppgjoerVedtakIngenEndringDto(
    override val pesysData: AfpEtteroppgjoerVedtakDto,
    override val saksbehandlerValg: EmptySaksbehandlerValg,
) : RedigerbarBrevdata<EmptySaksbehandlerValg, AfpEtteroppgjoerVedtakDto>

data class AfpEtteroppgjoerVedtakDto(
    val oppgjoersAar: Int,
    val afpEtteroppgjoer: AfpEtteroppgjoer,
    val afpGrunnlag: AfpGrunnlag,
) : FagsystemBrevdata

data class AfpEtteroppgjoer(
    val afpAvvik: Double?,
    val forventetPensjonsgivendeInntektBeregnet: Double?,
    val iiap: Double?
)

data class AfpGrunnlag(
    val opphoersDato: LocalDate?,
    val uttaksDato: LocalDate,
    val inntektEtterOpphoer: Double?,
    val inntektFoerUttak: Double?,
    val pensjonsgivendeInntekt: Double?,
    val uttakFoerFristen: Boolean,
    val opphoerForFristen: Boolean,
)