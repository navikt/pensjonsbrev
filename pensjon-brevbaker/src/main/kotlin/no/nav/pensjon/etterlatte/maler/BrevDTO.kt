package no.nav.pensjon.etterlatte.maler

interface BrevDTO {
    val innhold: List<Element>
}

interface FerdigstillingBrevDTO : BrevDTO

interface RedigerbartUtfallBrevDTO