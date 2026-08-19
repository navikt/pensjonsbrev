package no.nav.pensjon.brev.alder.maler.afpprivat

import no.nav.pensjon.brev.alder.maler.vedlegg.createOversiktOverPensjonenAfpPrivatDto
import no.nav.pensjon.brev.alder.model.afpprivat.InnvilgelseAvAfpDto
import no.nav.brev.brevbaker.lagSaksbehandlervalg
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

fun createInnvilgelseAvAfpDto(): InnvilgelseAvAfpDto =
    InnvilgelseAvAfpDto(
        saksbehandlerValg = lagSaksbehandlervalg(),
        pesysData = createPesysdata(),
    )


fun createPesysdata(): InnvilgelseAvAfpDto.PesysData =
    InnvilgelseAvAfpDto.PesysData(
        kravMottattDato = LocalDate.of(2026, 1, 15),
        virkningFom = LocalDate.of(2026, 3, 1),
        brukerUnder70Aar = true,
        bosattINorge = true,
        afpBeregning = InnvilgelseAvAfpDto.PesysData.AfpBeregning(
            totalPensjon = Kroner(18500),
            livsvarigBrutto = Kroner(12000),
            kronetilleggBrutto = Kroner(2000),
            kompensasjonstilleggBrutto = Kroner(4500),
        ),
        oversiktOverPensjonen = createOversiktOverPensjonenAfpPrivatDto(),
    )

