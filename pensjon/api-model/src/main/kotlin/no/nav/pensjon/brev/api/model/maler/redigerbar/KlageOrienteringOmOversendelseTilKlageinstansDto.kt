package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Foedselsnummer

data class KlageOrienteringOmOversendelseTilKlageinstansDto(
    override val pesysData: PesysData, override val saksbehandlerValg: EmptySaksbehandlerValg
) : RedigerbarBrevdata<EmptySaksbehandlerValg, KlageOrienteringOmOversendelseTilKlageinstansDto.PesysData> {

    data class PesysData(
        val foedselsnummer: Foedselsnummer, // PE_PersonSak_PSfnr_tssid
        val navn: String, // PE_PersonSak_PSNavn
        val navnAvsenderEnhet: String // PE_kontaktinformasjon_NavnAvsenderEnhet
    ) : FagsystemBrevdata

}