package no.nav.pensjon.brev.api.model.maler

/**
 * Interface for toppnivå-mal-DTO.
 * Ikke utvid denne direkte for en mal-dto, bruk heller de spesifikke interfacene under.
 */
interface BrevbakerBrevdata

interface RedigerbarBrevdata<SaksbehandlerValg : SaksbehandlerValgBrevdata, PesysData : FagsystemBrevdata> : BrevbakerBrevdata {
    val saksbehandlerValg: SaksbehandlerValg
    val pesysData: PesysData
}

interface AutobrevData : BrevbakerBrevdata, FagsystemBrevdata

interface FagsystemBrevdata

interface SaksbehandlerValgBrevdata

interface VedleggData