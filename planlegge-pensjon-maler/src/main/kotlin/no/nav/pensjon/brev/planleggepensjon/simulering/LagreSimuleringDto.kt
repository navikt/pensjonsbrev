package no.nav.pensjon.brev.planleggepensjon.simulering

import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgBrevdata
import no.nav.pensjon.brevbaker.api.model.DisplayText

data class LagreSimuleringDto(
    @DisplayText("placeholder")
    val alderspensjonListe: List<LagreAlderspensjon>,
    @DisplayText("placeholder")
    val livsvarigOffentligAfpListe: List<LagreAfpOffentlig>,
    @DisplayText("placeholder")
    val tidsbegrensetOffentligAfp: LagreTidsbegrensetOffentligAfp?,
    @DisplayText("placeholder")
    val privatAfpListe: List<LagreAfpPrivat>,
    @DisplayText("placeholder")
    val vilkaarsproevingsresultat: LagreVilkaarsproevingsresultat,
    @DisplayText("placeholder")
    val trygdetid: LagreTrygdetid?,
    @DisplayText("placeholder")
    val pensjonsgivendeInntektListe: List<LagreAarligBeloep>
) : SaksbehandlerValgBrevdata

data class LagreAlderspensjon(
    @DisplayText("placeholder")
    val alderAar: Int,
    @DisplayText("placeholder")
    val beloep: Int,
    @DisplayText("placeholder")
    val gjenlevendetillegg: Int?
)

data class LagreAfpOffentlig(
    @DisplayText("placeholder")
    val alderAar: Int,
    @DisplayText("placeholder")
    val aarligBeloep: Int,
    @DisplayText("placeholder")
    val maanedligBeloep: Int?
)

data class LagreTidsbegrensetOffentligAfp(
    @DisplayText("placeholder")
    val alderAar: Int,
    @DisplayText("placeholder")
    val totaltAfpBeloep: Int,
    @DisplayText("placeholder")
    val tidligereArbeidsinntekt: Int,
    @DisplayText("placeholder")
    val grunnbeloep: Int,
    @DisplayText("placeholder")
    val sluttpoengtall: Double,
    @DisplayText("placeholder")
    val trygdetid: Int,
    @DisplayText("placeholder")
    val poengaarTom1991: Int,
    @DisplayText("placeholder")
    val poengaarFom1992: Int,
    @DisplayText("placeholder")
    val grunnpensjon: Int,
    @DisplayText("placeholder")
    val tilleggspensjon: Int,
    @DisplayText("placeholder")
    val afpTillegg: Int,
    @DisplayText("placeholder")
    val saertillegg: Int,
    @DisplayText("placeholder")
    val afpGrad: Int,
    @DisplayText("placeholder")
    val erAvkortet: Boolean
)

data class LagreAfpPrivat(
    @DisplayText("placeholder")
    val alderAar: Int,
    @DisplayText("placeholder")
    val aarligBeloep: Int,
    @DisplayText("placeholder")
    val kompensasjonstillegg: Int,
    @DisplayText("placeholder")
    val kronetillegg: Int,
    @DisplayText("placeholder")
    val livsvarig: Int,
    @DisplayText("placeholder")
    val maanedligBeloep: Int?
)

data class LagreVilkaarsproevingsresultat(
    @DisplayText("placeholder")
    val erInnvilget: Boolean,
    @DisplayText("placeholder")
    val alternativ: LagreUttaksparametre?
)

data class LagreTrygdetid(
    @DisplayText("placeholder")
    val antallAar: Int,
    @DisplayText("placeholder")
    val erUtilstrekkelig: Boolean
)

data class LagreAarligBeloep(
    @DisplayText("placeholder")
    val aarstall: Int,
    @DisplayText("placeholder")
    val beloep: Int
)

data class LagreUttaksparametre(
    @DisplayText("placeholder")
    val gradertUttakAlder: LagreAlder?,
    @DisplayText("placeholder")
    val uttaksgrad: Int?,
    @DisplayText("placeholder")
    val heltUttakAlder: LagreAlder
)

data class LagreAlder(
    @DisplayText("placeholder")
    val aar: Int,
    @DisplayText("placeholder")
    val maaneder: Int
)
