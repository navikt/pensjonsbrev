package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.VedtaksBegrunnelse
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.vedlegg.DineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brev.api.model.vedlegg.Trygdetid

@Suppress("unused")

data class AvslagForLiteTrygdetidAP2016Dto(
    override val pesysData: PesysData,
    override val saksbehandlerValg: EmptyBrevdata,
) : RedigerbarBrevdata<EmptyBrevdata, AvslagForLiteTrygdetidAP2016Dto.PesysData> {


    data class PesysData(
        val avtaleland: String,
        val bostedsland: String,
        val erAvtaleland: Boolean,
        val erEOSland: Boolean,
        val trygdeperioderNorge: List<Trygdetid>,
        val trygdeperioderEOSland: List<Trygdetid>,
        val trygdeperioderAvtaleland: List<Trygdetid>,
        val vedtaksBegrunnelse: VedtaksBegrunnelse,
        val dineRettigheterOgMulighetTilAaKlageDto: DineRettigheterOgMulighetTilAaKlageDto,
    ) : BrevbakerBrevdata

}