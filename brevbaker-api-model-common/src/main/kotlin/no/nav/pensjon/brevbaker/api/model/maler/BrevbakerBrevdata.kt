package no.nav.pensjon.brev.api.model.maler

/**
 * Top-level interface for all Brevbaker Dto classes.
 */
interface BrevbakerBrevdata

interface RedigerbarBrevdata<SaksbehandlerValg : BrevbakerBrevdata, PesysData : BrevbakerBrevdata> : BrevbakerBrevdata {
    val saksbehandlerValg: SaksbehandlerValg
    val pesysData: PesysData
}
