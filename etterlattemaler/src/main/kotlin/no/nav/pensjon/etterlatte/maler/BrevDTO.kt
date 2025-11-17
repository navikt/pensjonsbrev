package no.nav.pensjon.etterlatte.maler

import no.nav.pensjon.brev.api.model.maler.AutobrevData


/* Ikke utvid denne direkte,
utvid heller FerdigstillingBrevDTO eller RedigerbartUtfallBrevDTO,
avhengig av hva du lager
*/
interface BrevDTO : AutobrevData {
    val innhold: List<Element>
}

interface FerdigstillingBrevDTO : BrevDTO

interface RedigerbartUtfallBrevDTO : AutobrevData