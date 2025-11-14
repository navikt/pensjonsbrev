package no.nav.pensjon.brev.api.model.maler

/**
 * Interface for toppnivå-mal-DTO.
 * Ikke utvid denne direkte for en mal-dto, bruk heller de spesifikke interfacene under.
 */
interface BrevbakerBrevdata

interface RedigerbarBrevdata<SaksbehandlerValg : SaksbehandlerValgBrevdata, PesysData : PesysBrevdata> : BrevbakerBrevdata {
    val saksbehandlerValg: SaksbehandlerValg
    val pesysData: PesysData
}

interface AutobrevData : BrevbakerBrevdata, PesysBrevdata

interface PesysBrevdata

interface SaksbehandlerValgBrevdata

interface VedleggData