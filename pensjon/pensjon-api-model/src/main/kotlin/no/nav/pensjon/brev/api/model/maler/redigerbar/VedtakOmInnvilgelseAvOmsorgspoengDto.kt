package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.BrevdataMedSaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL


@Suppress("unused")
data class VedtakOmInnvilgelseAvOmsorgspoengDto(
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
    override val pesysData: PesysData,
) : BrevdataMedSaksbehandlerValg<VedtakOmInnvilgelseAvOmsorgspoengDto.PesysData> {

    data class PesysData(
        val omsorgspersonNavn: String, //PE_Grunnlag_OmsorgGodskrGrunnlagListe_Godskrivesliste_OGPersonPleieTrengFornavn/Mellomnavn/Etternavn
        val omsorgsopptjeningsaar: String, //PE_Grunnlag_OmsorgGodskrGrunnlagListe:OmsorgGodskrGrunnlagAr
    ) : FagsystemBrevdata
}