package no.nav.pensjon.brev.alder.maler.afp

import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingAutoDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year
import java.time.LocalDate

fun createVedtakAfpEtteroppgjoerEtterbetalingAutoDto(): VedtakAfpEtteroppgjoerEtterbetalingAutoDto =
    VedtakAfpEtteroppgjoerEtterbetalingAutoDto(
        oppgjoersAar = Year(2024),
        forlitebetalt = Kroner(28_500),
        pgi = Kroner(412_500),
        ifu = Kroner(85_000),
        ieo = Kroner(120_000),
        iiap = Kroner(207_500),
        fpiberegnet = Kroner(250_000),
        fullafp = Kroner(220_000),
        fradragberegnetai = Kroner(63_500),
        korrigertafp = Kroner(156_500),
        tpiberegnet = Kroner(300_000),
        utbetaltafp = Kroner(128_000),
        uttaksdato = LocalDate.of(2024, 3, 1),
        opphorsdato = LocalDate.of(2024, 10, 31),
        periode = VedtakAfpEtteroppgjoerEtterbetalingAutoDto.Periode.UTTAK_OG_OPPHOER_I_AARET,
    )
