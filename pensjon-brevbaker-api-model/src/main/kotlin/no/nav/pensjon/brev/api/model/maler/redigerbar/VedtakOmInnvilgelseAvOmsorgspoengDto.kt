package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata


@Suppress("unused")
data class VedtakOmInnvilgelseAvOmsorgspoengDto(
    override val saksbehandlerValg: EmptySaksbehandlerValg,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<EmptySaksbehandlerValg, VedtakOmInnvilgelseAvOmsorgspoengDto.PesysData> {

    data class PesysData(
        val omsorgspersonNavn: String, //PE_Grunnlag_OmsorgGodskrGrunnlagListe_Godskrivesliste_OGPersonPleieTrengFornavn/Mellomnavn/Etternavn
        val omsorgsopptjeningsaar: String,
    ) : BrevbakerBrevdata
}