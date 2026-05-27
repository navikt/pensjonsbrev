package no.nav.pensjon.brev.alder.maler.afp

import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingAutoDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year

fun createVedtakAfpEtteroppgjoerTilbakekrevingAutoDto(): VedtakAfpEtteroppgjoerTilbakekrevingAutoDto =
    VedtakAfpEtteroppgjoerTilbakekrevingAutoDto(
        oppgjoersAar = Year(2024),
        formyebetalt = Kroner(38_500),
        pensjonsgivendeInntekt = Kroner(412_500),
        inntektEtterOpphoer = Kroner(85_000),
        inntektFoerUttak = Kroner(120_000),
        inntektIAfpPerioden = Kroner(207_500),
        avvik = Kroner(45_000),
        fullAfp = Kroner(220_000),
        fradragBeregnetArbeidsInntekt = Kroner(63_500),
        korrigertAfp = Kroner(156_500),
        tidligereArbeidsInntektBeregnet = Kroner(300_000),
        utbetaltAfp = Kroner(195_000),
        periode = VedtakAfpEtteroppgjoerTilbakekrevingAutoDto.Periode.UTTAK_OG_OPPHOER_I_AARET,
    )
