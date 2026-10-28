package no.nav.pensjon.brev.api.model.maler

interface SaksbehandlerValgEnum {
    val displayText: String
    val rekkefoelge: Int?
        get() = null
}