package no.nav.pensjon.brev.api.model.maler

/**
 * Top-level interface for all Brevbaker Dto classes.
 */
interface BrevbakerBrevdata

interface RedigerbarBrevdata<SaksbehandlerValg : SaksbehandlerValgBrevdata, PesysData : PesysBrevdata> : BrevbakerBrevdata {
    val saksbehandlerValg: SaksbehandlerValg
    val pesysData: PesysData
}

interface AutobrevData : BrevbakerBrevdata, PesysBrevdata

interface PesysBrevdata

interface SaksbehandlerValgBrevdata

interface VedleggData : BrevbakerBrevdata