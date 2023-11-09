package no.nav.pensjon.etterlatte.maler.barnepensjon.migrering

data class BarnepensjonOmregnetNyttRegelverkDTO(
    val utbetaltFoerReform: Int,
    val utbetaltEtterReform: Int,
    val grunnbeloep: Int,
    val anvendtTrygdetid: Int,
)