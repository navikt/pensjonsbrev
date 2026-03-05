package no.nav.pensjon.brev.planleggepensjon.simulering

import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgBrevdata
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Percent
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
    val beloep: Kroner,
    @DisplayText("placeholder")
    val gjenlevendetillegg: Kroner?
)

data class LivsvarigOffentligAfp(
    @DisplayText("placeholder")
    val alderAar: Int,
    @DisplayText("placeholder")
    val aarligBeloep: Kroner,
    @DisplayText("placeholder")
    val maanedligBeloep: Kroner?
)

data class TidsbegrensetOffentligAfp(
    @DisplayText("placeholder")
    val alderAar: Int,
    @DisplayText("placeholder")
    val totaltAfpBeloep: Kroner,
    @DisplayText("placeholder")
    val tidligereArbeidsinntekt: Kroner,
    @DisplayText("placeholder")
    val grunnbeloep: Kroner,
    @DisplayText("placeholder")
    val sluttpoengtall: Double,
    @DisplayText("placeholder")
    val trygdetid: Int,
    @DisplayText("placeholder")
    val poengaarTom1991: Int,
    @DisplayText("placeholder")
    val poengaarFom1992: Int,
    @DisplayText("placeholder")
    val grunnpensjon: Kroner,
    @DisplayText("placeholder")
    val tilleggspensjon: Kroner,
    @DisplayText("placeholder")
    val afpTillegg: Kroner,
    @DisplayText("placeholder")
    val saertillegg: Kroner,
    @DisplayText("placeholder")
    val afpGrad: Percent,
    @DisplayText("placeholder")
    val erAvkortet: Boolean
)

data class PrivatAfp(
    @DisplayText("placeholder")
    val alderAar: Int,
    @DisplayText("placeholder")
    val aarligBeloep: Kroner,
    @DisplayText("placeholder")
    val kompensasjonstillegg: Kroner,
    @DisplayText("placeholder")
    val kronetillegg: Kroner,
    @DisplayText("placeholder")
    val livsvarig: Kroner,
    @DisplayText("placeholder")
    val maanedligBeloep: Kroner?
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
    val aarstall: Year,
    @DisplayText("placeholder")
    val beloep: Kroner
)

data class Uttaksparametre(
    @DisplayText("placeholder")
    val gradertUttakAlder: Alder?,
    @DisplayText("placeholder")
    val uttaksgrad: Percent?,
    @DisplayText("placeholder")
    val heltUttakAlder: Alder
)

data class Alder(
    @DisplayText("placeholder")
    val aar: Int,
    @DisplayText("placeholder")
    val maaneder: Int
)
