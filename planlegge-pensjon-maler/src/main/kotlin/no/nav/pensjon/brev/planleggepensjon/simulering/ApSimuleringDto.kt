package no.nav.pensjon.brev.planleggepensjon.simulering

import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgBrevdata
import no.nav.pensjon.brevbaker.api.model.DisplayText

data class ApSimuleringDto(
    @DisplayText("placeholder")
    val alderspensjonListe: List<Alderspensjon>,
    @DisplayText("placeholder")
    val livsvarigOffentligAfpListe: List<LivsvarigOffentligAfp>,
    @DisplayText("placeholder")
    val tidsbegrensetOffentligAfp: TidsbegrensetOffentligAfp?,
    @DisplayText("placeholder")
    val privatAfpListe: List<PrivatAfp>,
    @DisplayText("placeholder")
    val vilkaarsproevingsresultat: Vilkaarsproevingsresultat,
    @DisplayText("placeholder")
    val trygdetid: Trygdetid?,
    @DisplayText("placeholder")
    val pensjonsgivendeInntektListe: List<AarligBeloep>
) : SaksbehandlerValgBrevdata

data class Alderspensjon(
    @DisplayText("placeholder")
    val alderAar: Int,
    @DisplayText("placeholder")
    val beloep: Int,
    @DisplayText("placeholder")
    val gjenlevendetillegg: Int?
)

data class LivsvarigOffentligAfp(
    @DisplayText("placeholder")
    val alderAar: Int,
    @DisplayText("placeholder")
    val aarligBeloep: Int,
    @DisplayText("placeholder")
    val maanedligBeloep: Int?
)

data class TidsbegrensetOffentligAfp(
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

data class PrivatAfp(
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

data class Vilkaarsproevingsresultat(
    @DisplayText("placeholder")
    val erInnvilget: Boolean,
    @DisplayText("placeholder")
    val alternativ: Uttaksparametre?
)

data class Trygdetid(
    @DisplayText("placeholder")
    val antallAar: Int,
    @DisplayText("placeholder")
    val erUtilstrekkelig: Boolean
)

data class AarligBeloep(
    @DisplayText("placeholder")
    val aarstall: Int,
    @DisplayText("placeholder")
    val beloep: Int
)

data class Uttaksparametre(
    @DisplayText("placeholder")
    val gradertUttakAlder: Alder?,
    @DisplayText("placeholder")
    val uttaksgrad: Int?,
    @DisplayText("placeholder")
    val heltUttakAlder: Alder
)

data class Alder(
    @DisplayText("placeholder")
    val aar: Int,
    @DisplayText("placeholder")
    val maaneder: Int
)
