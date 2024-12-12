package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.aktivitetsplikt.Aktivitetsgrad
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.aktivitetsplikt.NasjonalEllerUtland
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.aktivitetsplikt.OmstillingsstoenadAktivitetspliktInformasjon10mndInnholdDTO

fun createOmstillingsstoenadAktivitetspliktInformasjon10mndInnholdDTO() =
    OmstillingsstoenadAktivitetspliktInformasjon10mndInnholdDTO(
        aktivitetsgrad = Aktivitetsgrad.OVER_50_PROSENT,
        utbetaling = true,
        redusertEtterInntekt = true,
        nasjonalEllerUtland = NasjonalEllerUtland.NASJONAL
    )