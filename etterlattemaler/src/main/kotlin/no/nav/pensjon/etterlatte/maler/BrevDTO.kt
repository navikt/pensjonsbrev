package no.nav.pensjon.etterlatte.maler

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata


/* Ikke utvid denne direkte,
utvid heller FerdigstillingBrevDTO eller RedigerbartUtfallBrevDTO,
avhengig av hva du lager
*/
interface BrevDTO : BrevbakerBrevdata {
    val innhold: ElementListe
}

interface FerdigstillingBrevDTO : BrevDTO

interface RedigerbartUtfallBrevDTO : BrevbakerBrevdata