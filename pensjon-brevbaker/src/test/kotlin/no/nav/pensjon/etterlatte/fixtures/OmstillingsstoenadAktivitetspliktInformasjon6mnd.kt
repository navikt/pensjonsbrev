package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.aktivitetsplikt.NasjonalEllerUtland
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.aktivitetsplikt.OmstillingsstoenadAktivitetspliktInformasjon6mndInnholdDTO

fun createOmstillingsstoenadAktivitetspliktInformasjon6mndDto() =
    OmstillingsstoenadAktivitetspliktInformasjon6mndInnholdDTO(
        redusertEtterInntekt = true,
        nasjonalEllerUtland = NasjonalEllerUtland.NASJONAL
    )