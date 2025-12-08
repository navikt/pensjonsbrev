package no.nav.pensjon.brev.alder.model.avslag

import no.nav.pensjon.brev.alder.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.alder.model.vedlegg.DineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brev.alder.model.vedlegg.Trygdetid
import no.nav.pensjon.brev.api.model.maler.AutobrevData

data class AvslagUnder5AarTrygdetidAutoDto(
    val regelverkType: AlderspensjonRegelverkType,
    val borINorge: Boolean,
    val trygdeperioderNorge: List<Trygdetid>,
    val dineRettigheterOgMulighetTilAaKlageDto: DineRettigheterOgMulighetTilAaKlageDto,
) : AutobrevData