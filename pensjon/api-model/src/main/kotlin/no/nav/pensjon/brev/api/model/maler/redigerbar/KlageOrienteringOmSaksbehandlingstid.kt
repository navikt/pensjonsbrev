package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Foedselsnummer

class KlageOrienteringOmSaksbehandlingstid(
    override val pesysData: PesysData, override val saksbehandlerValg: EmptySaksbehandlerValg
) : RedigerbarBrevdata<EmptySaksbehandlerValg, KlageOrienteringOmSaksbehandlingstid.PesysData> {

    data class PesysData(
        val foedselsdato: Foedselsnummer, // PE_PersonSak_PSfnr_tssid
        val navn: String, // PE_PersonSak_PSNavn
        val kontaktinformasjon: String // PE_kontaktinformasjon_NavnAvsenderEnhet
    ) : FagsystemBrevdata
}