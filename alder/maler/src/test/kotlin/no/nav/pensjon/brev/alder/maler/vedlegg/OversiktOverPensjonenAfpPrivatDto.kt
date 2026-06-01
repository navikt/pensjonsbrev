package no.nav.pensjon.brev.alder.maler.vedlegg

import no.nav.pensjon.brev.alder.model.vedlegg.OversiktOverPensjonenAfpPrivatDto
import no.nav.pensjon.brev.alder.model.vedlegg.OversiktOverPensjonenAfpPrivatDto.AfpBeregning
import no.nav.pensjon.brev.alder.model.vedlegg.OversiktOverPensjonenAfpPrivatDto.Periode
import no.nav.pensjon.brev.alder.model.vedlegg.OversiktOverPensjonenAfpPrivatDto.SistePeriode
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

fun createOversiktOverPensjonenAfpPrivatDto() =
    OversiktOverPensjonenAfpPrivatDto(
        virkningFom = LocalDate.of(2026, 3, 1),
        perioder = listOf(
            Periode(
                virkDatoFom = LocalDate.of(2026, 3, 1),
                virkDatoTom = LocalDate.of(2026, 4, 30),
                beregning = AfpBeregning(
                    totalPensjon = Kroner(18_000),
                    livsvarigBrutto = Kroner(11_800),
                    kronetilleggBrutto = Kroner(2_000),
                    kompensasjonstilleggBrutto = Kroner(4_200),
                ),
            ),
            Periode(
                virkDatoFom = LocalDate.of(2026, 5, 1),
                virkDatoTom = LocalDate.of(2026, 8, 31),
                beregning = AfpBeregning(
                    totalPensjon = Kroner(18_300),
                    livsvarigBrutto = Kroner(12_000),
                    kronetilleggBrutto = Kroner(2_000),
                    kompensasjonstilleggBrutto = Kroner(4_300),
                ),
            ),
        ),
        sistePeriode = SistePeriode(
            virkDatoFom = LocalDate.of(2026, 9, 1),
            beregning = AfpBeregning(
                totalPensjon = Kroner(18_500),
                livsvarigBrutto = Kroner(12_000),
                kronetilleggBrutto = Kroner(2_000),
                kompensasjonstilleggBrutto = Kroner(4_500),
            ),
        ),
    )
