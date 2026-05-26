package no.nav.pensjon.brev.alder.maler.afp

import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerToleransebeloepAutoDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year
import java.time.LocalDate

fun createVedtakAfpEtteroppgjoerToleransebeloepAutoDto(): VedtakAfpEtteroppgjoerToleransebeloepAutoDto =
    VedtakAfpEtteroppgjoerToleransebeloepAutoDto(
        oppgjoersAar = Year(2024),
        pgi = Kroner(412_500),
        ifu = Kroner(85_000),
        ieo = Kroner(120_000),
        iiap = Kroner(207_500),
        fpiberegnet = Kroner(400_000),
        avvik = Kroner(12_500),
        uttaksdato = LocalDate.of(2024, 4, 1),
        opphorsdato = LocalDate.of(2024, 11, 30),
        periode = VedtakAfpEtteroppgjoerToleransebeloepAutoDto.Periode.UTTAK_OG_OPPHOER_I_AARET,
    )
