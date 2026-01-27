package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brevbaker.api.model.Year


@Suppress("unused")
data class VedtakGodskrivingPensjonsopptjeningDto(
    override val saksbehandlerValg: EmptySaksbehandlerValg,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<EmptySaksbehandlerValg, VedtakGodskrivingPensjonsopptjeningDto.PesysData> {

    data class PesysData(
        val brukersFoedselsdato: Year,
        val omsorgspersonNavn: String, //PE_Grunnlag_OmsorgGodskrGrunnlagListe_Godskrivesliste_OGPersonPleieTrengFornavn/Mellomnavn/Etternavn
        val omsorgsopptjeningsaar: String, //PE_Grunnlag_OmsorgGodskrGrunnlagListe:OmsorgGodskrGrunnlagAr
    ) : FagsystemBrevdata
}