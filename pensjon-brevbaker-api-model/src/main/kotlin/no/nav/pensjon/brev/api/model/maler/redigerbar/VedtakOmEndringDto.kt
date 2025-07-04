package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brevbaker.api.model.Kroner

@Suppress("unused")
data class VedtakOmEndringDto(
    override val saksbehandlerValg: EmptyBrevdata,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<EmptyBrevdata, VedtakOmEndringDto.PesysData> {
    data class PesysData(
        val grunnbeloep: Kroner,
        val maanedligPensjonFoerSkattDto: MaanedligPensjonFoerSkattDto,
        val orienteringOmRettigheterOgPlikterDto: OrienteringOmRettigheterOgPlikterDto
    ) : BrevbakerBrevdata
}