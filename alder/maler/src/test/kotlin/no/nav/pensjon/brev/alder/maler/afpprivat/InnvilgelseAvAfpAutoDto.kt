package no.nav.pensjon.brev.alder.maler.afpprivat

import no.nav.pensjon.brev.alder.maler.vedlegg.createDinAfpPrivatBeregningDto
import no.nav.pensjon.brev.alder.model.afpprivat.InnvilgelseAvAfpAutoDto
import no.nav.pensjon.brev.alder.model.afpprivat.InnvilgelseAvAfpDto
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
            opptjening = Kroner(11500),
            forholdstallUttak = 15.5,
            justeringsbeloep = Kroner(1830),
            referansebeloep = Kroner(11122),
            kompensasjonstilleggForholdstall = 11.7,
        ),
        harEtterbetaling = true,
        dinAfpPrivatBeregning = createDinAfpPrivatBeregningDto(),
    )



