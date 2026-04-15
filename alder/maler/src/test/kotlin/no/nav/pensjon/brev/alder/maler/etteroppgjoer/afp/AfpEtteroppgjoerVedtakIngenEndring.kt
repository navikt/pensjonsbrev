package no.nav.pensjon.brev.alder.maler.etteroppgjoer.afp

import no.nav.pensjon.brev.alder.model.etteroppgjoer.afp.AfpEtteroppgjoer
import no.nav.pensjon.brev.alder.model.etteroppgjoer.afp.AfpEtteroppgjoerVedtakDto
import no.nav.pensjon.brev.alder.model.etteroppgjoer.afp.AfpEtteroppgjoerVedtakIngenEndringAutoDto
import no.nav.pensjon.brev.alder.model.etteroppgjoer.afp.AfpEtteroppgjoerVedtakIngenEndringDto
import no.nav.pensjon.brev.alder.model.etteroppgjoer.afp.AfpGrunnlag
import no.nav.pensjon.brev.alder.model.etteroppgjoer.afp.UttaksPeriode
import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import java.time.LocalDate

fun createAfpEtteroppgjoerVedtakIngenEndringAutoDto() = AfpEtteroppgjoerVedtakIngenEndringAutoDto(
    afpEtteroppgjoerVedtak = createAfpEtteroppgjoerVedtakDto(),
    innsenderEnhetNavn = "Nav Familie- og pensjonsytelser Porsgrunn"
)

fun createAfpEtteroppgjoerVedtakIngenEndringDto() = AfpEtteroppgjoerVedtakIngenEndringDto(
    pesysData = createAfpEtteroppgjoerVedtakDto(),
    saksbehandlerValg = EmptySaksbehandlerValg
)

private fun createAfpEtteroppgjoerVedtakDto() = AfpEtteroppgjoerVedtakDto(
    oppgjoersAar = 2025,
    afpEtteroppgjoer = AfpEtteroppgjoer(
        afpAvvik = 0.0,
        forventetPensjonsgivendeInntektBeregnet = 10000.0,
        inntektIAfpPerioden = 50.0
    ),
    afpGrunnlag = AfpGrunnlag(
        opphoersDato = LocalDate.of(2026, 1, 1),
        uttaksDato = LocalDate.of(2025, 1, 1),
        inntektEtterOpphoer = 10.0,
        inntektFoerUttak = 20.0,
        pensjonsgivendeInntekt = 10000.0,
        uttaksPeriode = UttaksPeriode.UTTAK_FOER_FOERSTE_FEB_OPPHOER_ETTER_31_DES_ELLER_NULL,
    )
)