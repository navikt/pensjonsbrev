package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brevbaker.api.model.Felles

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

interface VedleggData

class FellesVedleggData(val felles: Felles) : VedleggData