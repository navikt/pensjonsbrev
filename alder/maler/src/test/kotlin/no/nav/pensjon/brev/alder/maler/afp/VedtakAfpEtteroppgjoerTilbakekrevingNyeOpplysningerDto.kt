package no.nav.pensjon.brev.alder.maler.afp

import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingNyeOpplysningerDto
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingNyeOpplysningerDto.Scenario
import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year

fun createVedtakAfpEtteroppgjoerTilbakekrevingNyeOpplysningerDto(): VedtakAfpEtteroppgjoerTilbakekrevingNyeOpplysningerDto =
    VedtakAfpEtteroppgjoerTilbakekrevingNyeOpplysningerDto(
        saksbehandlerValg = EmptySaksbehandlerValg,
        pesysData = VedtakAfpEtteroppgjoerTilbakekrevingNyeOpplysningerDto.PesysData(
            oppgjoersAar = Year(2024),
            pensjonsgivendeInntekt = Kroner(412_500),
            inntektFoerUttak = Kroner(85_000),
            inntektEtterOpphoer = Kroner(120_000),
            inntektIAfpPerioden = Kroner(207_500),
            avvik = Kroner(45_000),
            fullAfp = Kroner(220_000),
            fradragberegnetai = Kroner(63_500),
            korrigertAfp = Kroner(156_500),
            tpiberegnet = Kroner(300_000),
            utbetaltAfp = Kroner(195_000),
            formyebetalt = Kroner(38_500),
            scenario = Scenario.IFU_OG_IEO_OVERSTYRT,
        ),
    )
