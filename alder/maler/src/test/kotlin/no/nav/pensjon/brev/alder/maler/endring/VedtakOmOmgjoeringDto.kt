package no.nav.pensjon.brev.alder.maler.endring

import no.nav.brev.brevbaker.lagSaksbehandlervalg
import no.nav.brev.brevbaker.vilkaarligDato
import no.nav.pensjon.brev.alder.maler.vedlegg.createMaanedligPensjonFoerSkatt
import no.nav.pensjon.brev.alder.model.endring.VedtakOmOmgjoeringDto
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brev.maler.vedlegg.createOrienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner

fun createVedtakOmOmgjoeringDto() = VedtakOmOmgjoeringDto(
    saksbehandlerValg = lagSaksbehandlervalg("aarsak" to VedtakOmOmgjoeringDto.Aarsak.feilSivilstand.name),
    pesysData = VedtakOmOmgjoeringDto.PesysData(
        ytelse = "Alderspensjon",
        ugyldigVedtakInformasjon = VedtakOmOmgjoeringDto.UgyldigVedtakInformasjon(
            vedtakDatoFom = java.time.LocalDate.of(2023, 1, 1)
        ),
        maanedligPensjonFoerSkattDto = createMaanedligPensjonFoerSkatt(),
        maanedligPensjonFoerSkattAP2025Dto =
            createMaanedligPensjonFoerSkattAP2025Dto(),
        orienteringOmRettigheterOgPlikterDto = createOrienteringOmRettigheterOgPlikterDto(),
    )
)

internal fun createMaanedligPensjonFoerSkattAP2025Dto() = MaanedligPensjonFoerSkattAP2025Dto(
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
)