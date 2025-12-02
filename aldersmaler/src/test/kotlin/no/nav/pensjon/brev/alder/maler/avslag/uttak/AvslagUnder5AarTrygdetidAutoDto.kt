package no.nav.pensjon.brev.alder.maler.avslag.uttak

import no.nav.brev.brevbaker.vilkaarligDato
import no.nav.pensjon.brev.alder.maler.vedlegg.createDineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brev.alder.model.avslag.AvslagUnder5AarTrygdetidAutoDto
import no.nav.pensjon.brev.alder.model.vedlegg.Trygdetid
import java.time.LocalDate

fun createUnder5AarTrygdetidAutoDto() = AvslagUnder5AarTrygdetidAutoDto(
    regelverkType = no.nav.pensjon.brev.alder.model.AlderspensjonRegelverkType.AP2011,
    borINorge = true,
    trygdeperioderNorge = listOf(
        Trygdetid(fom = vilkaarligDato.minusYears(10), tom = vilkaarligDato.minusYears(9), land = "Norge"),
        Trygdetid(fom = vilkaarligDato.minusYears(5), tom = vilkaarligDato.minusYears(3), land = "Norge"),
    ),
    dineRettigheterOgMulighetTilAaKlageDto = createDineRettigheterOgMulighetTilAaKlageDto()
)