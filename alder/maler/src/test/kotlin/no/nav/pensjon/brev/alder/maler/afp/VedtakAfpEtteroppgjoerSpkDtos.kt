package no.nav.pensjon.brev.alder.maler.afp

import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingAutoDto
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingDto
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringAutoDto
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringDto
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerToleransebeloepAutoDto
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerToleransebeloepDto
import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year
import java.time.LocalDate

fun createVedtakAfpEtteroppgjoerToleransebeloepDto(): VedtakAfpEtteroppgjoerToleransebeloepDto =
    VedtakAfpEtteroppgjoerToleransebeloepDto(
        saksbehandlerValg = EmptySaksbehandlerValg,
        pesysData = VedtakAfpEtteroppgjoerToleransebeloepDto.PesysData(
            oppgjoersAar = Year(2024),
            pgi = Kroner(280_000),
            ifu = Kroner(40_000),
            ieo = Kroner(0),
            iiap = Kroner(240_000),
            fpiberegnet = Kroner(250_000),
            avvik = Kroner(10_000),
            uttaksdato = LocalDate.of(2024, 3, 1),
            opphorsdato = null,
            periode = VedtakAfpEtteroppgjoerToleransebeloepAutoDto.Periode.UTTAK_I_AARET,
        ),
    )

fun createVedtakAfpEtteroppgjoerEtterbetalingDto(): VedtakAfpEtteroppgjoerEtterbetalingDto =
    VedtakAfpEtteroppgjoerEtterbetalingDto(
        saksbehandlerValg = EmptySaksbehandlerValg,
        pesysData = VedtakAfpEtteroppgjoerEtterbetalingDto.PesysData(
            oppgjoersAar = Year(2024),
            forlitebetalt = Kroner(15_000),
            pgi = Kroner(220_000),
            ifu = Kroner(40_000),
            ieo = Kroner(0),
            iiap = Kroner(180_000),
            fpiberegnet = Kroner(250_000),
            fullafp = Kroner(200_000),
            fradragberegnetai = Kroner(50_000),
            tpiberegnet = Kroner(250_000),
            korrigertafp = Kroner(150_000),
            utbetaltafp = Kroner(135_000),
            uttaksdato = LocalDate.of(2024, 3, 1),
            opphorsdato = null,
            periode = VedtakAfpEtteroppgjoerEtterbetalingAutoDto.Periode.UTTAK_I_AARET,
        ),
    )

fun createVedtakAfpEtteroppgjoerIngenEndringDto(): VedtakAfpEtteroppgjoerIngenEndringDto =
    VedtakAfpEtteroppgjoerIngenEndringDto(
        saksbehandlerValg = EmptySaksbehandlerValg,
        pesysData = VedtakAfpEtteroppgjoerIngenEndringDto.PesysData(
            oppgjoersAar = Year(2024),
            pgi = Kroner(280_000),
            scenario = VedtakAfpEtteroppgjoerIngenEndringAutoDto.Scenario.IKKE_AFP_FULL_INNTEKT,
        ),
    )
