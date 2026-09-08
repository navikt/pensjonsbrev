package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata

@Suppress("unused")
data class VedtakOmInnvilgelseAvOmsorgspoengDto(
    val omsorgspersonNavn: String, //PE_Grunnlag_OmsorgGodskrGrunnlagListe_Godskrivesliste_OGPersonPleieTrengFornavn/Mellomnavn/Etternavn
    val omsorgsopptjeningsaar: String, //PE_Grunnlag_OmsorgGodskrGrunnlagListe:OmsorgGodskrGrunnlagAr
) : FagsystemBrevdata