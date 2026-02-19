package no.nav.pensjon.brev.alder.maler.sivilstand

import no.nav.brev.brevbaker.vilkaarligDato
import no.nav.pensjon.brev.alder.maler.vedlegg.createMaanedligPensjonFoerSkatt
import no.nav.pensjon.brev.alder.maler.vedlegg.createOrienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.alder.model.sivilstand.EndringAvAlderspensjonGarantitilleggDto
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brevbaker.api.model.BrevWrappers.Kroner
import java.time.LocalDate

fun createEndringAvAlderspensjonGarantitilleggDto() =
    EndringAvAlderspensjonGarantitilleggDto(
        saksbehandlerValg = EmptySaksbehandlerValg,
        pesysData =
            EndringAvAlderspensjonGarantitilleggDto.PesysData(
                alderspensjonVedVirk =
                    EndringAvAlderspensjonGarantitilleggDto.AlderspensjonVedVirk(
                        innvilgetFor67 = false,
                        ufoereKombinertMedAlder = false,
                        uttaksgrad = 100,
                    ),
                beregnetPensjonPerManedVedVirk =
                    EndringAvAlderspensjonGarantitilleggDto.BeregnetPensjonPerManedVedVirk(
                        garantitillegg = null,
                        totalPensjon = Kroner(340000),
                    ),
                kravVirkDatoFom = LocalDate.of(2025, 6, 1),
                maanedligPensjonFoerSkattDto = createMaanedligPensjonFoerSkatt(),
                maanedligPensjonFoerSkattAP2025Dto =
                    MaanedligPensjonFoerSkattAP2025Dto(
                        beregnetPensjonPerManedGjeldende =
                            MaanedligPensjonFoerSkattAP2025Dto.AlderspensjonPerManed(
                                inntektspensjon = Kroner(1000),
                                totalPensjon = Kroner(2000),
                                garantipensjon = Kroner(1000),
                                minstenivaIndividuell = Kroner(1000),
                                virkDatoFom = vilkaarligDato,
                                virkDatoTom = null,
                            ),
                        beregnetPensjonperManed = listOf(),
                        kravVirkFom = vilkaarligDato,
                    ),
                orienteringOmRettigheterOgPlikterDto = createOrienteringOmRettigheterOgPlikterDto(),
            ),
    )
