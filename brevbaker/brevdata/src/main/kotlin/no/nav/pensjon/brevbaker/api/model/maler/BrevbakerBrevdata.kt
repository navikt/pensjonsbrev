package no.nav.pensjon.brev.api.model.maler

/**
 * Interface for toppnivå-mal-DTO.
 * Ikke utvid denne direkte for en mal-dto, bruk heller de spesifikke interfacene under.
 */
interface BrevbakerBrevdata

interface RedigerbarBrevdata<Data : FagsystemBrevdata> : BrevbakerBrevdata {
    val saksbehandlerValg: SaksbehandlervalgIDSL
    val pesysData: Data
}

interface AutobrevData : BrevbakerBrevdata, FagsystemBrevdata

interface FagsystemBrevdata

interface SaksbehandlervalgIDSL : Map<String, Any?>

interface VedleggData