package no.nav.pensjon.etterlatte.maler


/* Ikke utvid denne direkte,
utvid heller FerdigstillingBrevDTO eller RedigerbartUtfallBrevDTO,
avhengig av hva du lager
*/
interface BrevDTO {
    val innhold: List<Element>
}

interface FerdigstillingBrevDTO : BrevDTO

interface RedigerbartUtfallBrevDTO