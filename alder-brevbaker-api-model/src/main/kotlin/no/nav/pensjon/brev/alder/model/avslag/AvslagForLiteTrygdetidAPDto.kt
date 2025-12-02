package no.nav.pensjon.brev.alder.model.avslag

import no.nav.pensjon.brev.alder.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.alder.model.VedtaksBegrunnelse
import no.nav.pensjon.brev.alder.model.avslag.AvslagForLiteTrygdetidAPDto.PesysData
import no.nav.pensjon.brev.alder.model.vedlegg.DineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brev.alder.model.vedlegg.Trygdetid
import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata

@Suppress("unused")
data class AvslagForLiteTrygdetidAPDto(
    override val pesysData: PesysData,
    override val saksbehandlerValg: EmptySaksbehandlerValg,
) : RedigerbarBrevdata<EmptySaksbehandlerValg, PesysData> {

    data class PesysData(
        val avtaleland: String?,
        val bostedsland: String?,
        val erEOSland: Boolean,
        val regelverkType: AlderspensjonRegelverkType,
        val borINorge: Boolean,
        val trygdeperioderNorge: List<Trygdetid>,
        val trygdeperioderEOSland: List<Trygdetid>,
        val trygdeperioderAvtaleland: List<Trygdetid>,
        val vedtaksBegrunnelse: VedtaksBegrunnelse,
        val dineRettigheterOgMulighetTilAaKlageDto: DineRettigheterOgMulighetTilAaKlageDto,
    ) : FagsystemBrevdata
}
