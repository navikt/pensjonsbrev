package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.aktivitetsplikt.Aktivitetsgrad
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.aktivitetsplikt.NasjonalEllerUtland
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.aktivitetsplikt.OmstillingsstoenadAktivitetspliktInformasjon4mndInnholdDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.aktivitetsplikt.OmstillingsstoenadAktivitetspliktInformasjon4mndInnholdData


fun createOmstillingsstoenadAktivitetspliktInformasjon4mndInnholdDTO(
    nasjonalEllerUtland: NasjonalEllerUtland = NasjonalEllerUtland.NASJONAL,
) =
    OmstillingsstoenadAktivitetspliktInformasjon4mndInnholdDTO(
        data = OmstillingsstoenadAktivitetspliktInformasjon4mndInnholdData(
            aktivitetsgrad = Aktivitetsgrad.OVER_50_PROSENT,
            utbetaling = true,
            redusertEtterInntekt = false,
            nasjonalEllerUtland = nasjonalEllerUtland,
            halvtGrunnbeloep = Kroner(130160 / 2),
        )
    )