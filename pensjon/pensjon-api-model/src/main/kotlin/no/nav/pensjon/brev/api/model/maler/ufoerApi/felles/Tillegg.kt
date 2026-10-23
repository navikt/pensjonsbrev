package no.nav.pensjon.brev.api.model.maler.ufoerApi.felles

data class Tillegg(
    val ektefelletilleggInnvilget: Boolean,
    val gjenlevendetilleggInnvilget: Boolean,
    val nyttGjenlevendetillegg: Boolean,
)