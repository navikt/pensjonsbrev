package no.nav.pensjon.brev.alder.maler.afp

import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpAutoDto
import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpDto
import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

fun createInnvilgelseAvAfpAutoDto(): InnvilgelseAvAfpAutoDto =
    InnvilgelseAvAfpAutoDto(
        kravMottattDato = LocalDate.of(2026, 1, 15),
        virkningFom = LocalDate.of(2026, 3, 1),
        brukerUnder70Aar = true,
        bosattINorge = true,
        afpBeregning = InnvilgelseAvAfpAutoDto.AfpBeregning(
            totalPensjon = Kroner(18500),
            livsvarigBrutto = Kroner(12000),
            kronetilleggBrutto = Kroner(2000),
            kompensasjonstilleggBrutto = Kroner(4500),
        ),
    )

fun createInnvilgelseAvAfpDto(): InnvilgelseAvAfpDto =
    InnvilgelseAvAfpDto(
        saksbehandlerValg = EmptySaksbehandlerValg,
        pesysData = createInnvilgelseAvAfpAutoDto(),
    )


