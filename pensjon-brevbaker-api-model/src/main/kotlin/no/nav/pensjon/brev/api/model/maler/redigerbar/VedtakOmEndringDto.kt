package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brevbaker.api.model.Kroner

@Suppress("unused")
data class VedtakOmEndringDto(
    override val saksbehandlerValg: SaksbehandlerValg,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<VedtakOmEndringDto.SaksbehandlerValg, VedtakOmEndringDto.PesysData> {
    enum class Relasjon(val bestemtForm: String) {
        EKTEFELLE("ektefellen"), PARTNER("partneren"), SAMBOER("samboeren");

        constructor(relasjon: Relasjon) : this(relasjon.bestemtForm)
    }
    data class SaksbehandlerValg(
        val relasjon: Relasjon
    ) : BrevbakerBrevdata
    data class PesysData(
        val grunnbeloep: Kroner,
        val maanedligPensjonFoerSkattDto: MaanedligPensjonFoerSkattDto,
        val orienteringOmRettigheterOgPlikterDto: OrienteringOmRettigheterOgPlikterDto
    ) : BrevbakerBrevdata
}