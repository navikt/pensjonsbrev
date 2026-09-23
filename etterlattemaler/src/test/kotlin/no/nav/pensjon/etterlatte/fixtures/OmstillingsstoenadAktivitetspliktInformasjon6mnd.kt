package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.aktivitetsplikt.NasjonalEllerUtland
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.aktivitetsplikt.OmstillingsstoenadAktivitetspliktInformasjon6mndInnholdDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.aktivitetsplikt.OmstillingsstoenadAktivitetspliktInformasjon6mndInnholdData

fun createOmstillingsstoenadAktivitetspliktInformasjon6mndDto(
    nasjonalEllerUtland: NasjonalEllerUtland = NasjonalEllerUtland.NASJONAL,
) =
    OmstillingsstoenadAktivitetspliktInformasjon6mndInnholdDTO(
        data = OmstillingsstoenadAktivitetspliktInformasjon6mndInnholdData(
            redusertEtterInntekt = true,
            nasjonalEllerUtland = nasjonalEllerUtland,
            halvtGrunnbeloep = Kroner(130160 / 2),
        )
    )