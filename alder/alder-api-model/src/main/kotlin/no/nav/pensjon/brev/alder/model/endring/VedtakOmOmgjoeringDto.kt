package no.nav.pensjon.brev.alder.model.endring

import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.alder.model.vedlegg.OrienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgEnum
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import java.time.LocalDate

data class VedtakOmOmgjoeringDto(
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<VedtakOmOmgjoeringDto.PesysData> {
    data class PesysData(
        val ytelse: String,
        val ugyldigVedtakInformasjon: UgyldigVedtakInformasjon,
        val maanedligPensjonFoerSkattDto: MaanedligPensjonFoerSkattDto?,
        val maanedligPensjonFoerSkattAP2025Dto: MaanedligPensjonFoerSkattAP2025Dto?,
        val orienteringOmRettigheterOgPlikterDto: OrienteringOmRettigheterOgPlikterDto
    ) : FagsystemBrevdata

    data class UgyldigVedtakInformasjon(
        val vedtakDatoFom: LocalDate,
    )

    enum class Aarsak(override val displayText: String) : SaksbehandlerValgEnum {
        feilSivilstand("Feil sivilstand"),
        feilEPSInntekt("Feil i EPS inntekt"),
        endretOpptjening("Endret opptjening (på grunn av uføre eller bare fra skatt)"),
        feilTrygdetid("Feil trygdetid"),
    }
}
