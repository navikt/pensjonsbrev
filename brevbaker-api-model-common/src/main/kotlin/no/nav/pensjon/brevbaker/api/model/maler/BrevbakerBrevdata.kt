package no.nav.pensjon.brev.api.model.maler

/**
 * Top-level interface for all Brevbaker Dto classes.
 */
interface BrevbakerBrevdata

interface RedigerbarBrevdata<SaksbehandlerValg : SaksbehandlerValgBrevdata, PesysData : BrevbakerBrevdata> : BrevbakerBrevdata {
    val saksbehandlerValg: SaksbehandlerValg
    val pesysData: PesysData
}

interface SaksbehandlerValgBrevdata : BrevbakerBrevdata

interface Vedlegg : BrevbakerBrevdata