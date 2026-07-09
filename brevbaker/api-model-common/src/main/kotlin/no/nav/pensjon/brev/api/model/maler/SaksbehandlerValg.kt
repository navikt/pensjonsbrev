package no.nav.pensjon.brev.api.model.maler

interface SaksbehandlervalgIDSL : SaksbehandlerValgBrevdata, Map<String, Any?>

interface SaksbehandlerValgEnum {
    val displayText: String
}