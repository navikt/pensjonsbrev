package no.nav.pensjon.brev.api.model.maler

/**
 * Interface for toppnivå-mal-DTO.
 * Ikke utvid denne direkte for en mal-dto, bruk heller de spesifikke interfacene under.
 */
interface BrevbakerBrevdata

interface RedigerbarBrevdata<Valg : SaksbehandlerValgBrevdata, Data : FagsystemBrevdata> : BrevbakerBrevdata {
    val saksbehandlerValg: Valg
    val pesysData: Data
}

interface AutobrevData : BrevbakerBrevdata, FagsystemBrevdata

interface FagsystemBrevdata

interface SaksbehandlerValgBrevdata

interface VedleggData {
    fun alltidValgbart(): Boolean = false
}

interface AlltidValgbartVedleggData : VedleggData {
    override fun alltidValgbart(): Boolean = true
    fun kode(): String
}