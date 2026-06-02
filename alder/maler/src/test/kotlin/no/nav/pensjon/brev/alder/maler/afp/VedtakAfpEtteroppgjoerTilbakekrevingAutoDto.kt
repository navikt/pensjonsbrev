package no.nav.pensjon.brev.alder.maler.afp

import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingAutoDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year

fun createVedtakAfpEtteroppgjoerTilbakekrevingAutoDto(): VedtakAfpEtteroppgjoerTilbakekrevingAutoDto =
    VedtakAfpEtteroppgjoerTilbakekrevingAutoDto(
        oppgjoersAar = Year(2024),
        formyebetalt = Kroner(38_500),
        pgi = Kroner(412_500),
        ifu = Kroner(85_000),
        ieo = Kroner(120_000),
        iiap = Kroner(207_500),
        avvik = Kroner(45_000),
        fullafp = Kroner(220_000),
        fradragberegnetai = Kroner(63_500),
        korrigertafp = Kroner(156_500),
        tpiberegnet = Kroner(300_000),
        utbetaltafp = Kroner(195_000),
        periode = VedtakAfpEtteroppgjoerTilbakekrevingAutoDto.Periode.UTTAK_OG_OPPHOER_I_AARET,
    )
