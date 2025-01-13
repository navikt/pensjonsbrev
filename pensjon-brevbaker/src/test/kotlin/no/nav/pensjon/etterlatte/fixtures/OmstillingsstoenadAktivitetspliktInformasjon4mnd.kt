package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.aktivitetsplikt.Aktivitetsgrad
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.aktivitetsplikt.NasjonalEllerUtland
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.aktivitetsplikt.OmstillingsstoenadAktivitetspliktInformasjon4mndInnholdDTO


fun createOmstillingsstoenadAktivitetspliktInformasjon4mndInnholdDTO() =
    OmstillingsstoenadAktivitetspliktInformasjon4mndInnholdDTO(
        aktivitetsgrad = Aktivitetsgrad.OVER_50_PROSENT,
        utbetaling = true,
        redusertEtterInntekt = false,
        nasjonalEllerUtland = NasjonalEllerUtland.NASJONAL
    )