package no.nav.pensjon.brev.planleggepensjon.simulering

import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgBrevdata
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Percent
import no.nav.pensjon.brevbaker.api.model.DisplayText

data class ApSimuleringDto(
    @DisplayText("Alderspensjon liste")
    val alderspensjonListe: List<Alderspensjon>,
    @DisplayText("Livsvarig Offentlig AFP liste")
    val livsvarigOffentligAfpListe: List<LivsvarigOffentligAfp>,
    @DisplayText("Tidsbegrenset offentlig AFP")
    val tidsbegrensetOffentligAfp: TidsbegrensetOffentligAfp?,
    @DisplayText("Privat AFP liste")
    val privatAfpListe: List<PrivatAfp>,
    @DisplayText("Vilårsprøvingsresultat")
    val vilkaarsproevingsresultat: Vilkaarsproevingsresultat,
    @DisplayText("Trygdetid")
    val trygdetid: Trygdetid?,
    @DisplayText("Pensjonsgivende inntekt liste")
    val pensjonsgivendeInntektListe: List<AarligBeloep>
) : SaksbehandlerValgBrevdata

data class Alderspensjon(
    @DisplayText("Alder (antall år)")
    val alderAar: Int,
    @DisplayText("Beløp")
    val beloep: Kroner,
    @DisplayText("Gjenlevendetillegg")
    val gjenlevendetillegg: Kroner?
)

data class LivsvarigOffentligAfp(
    @DisplayText("Alder (antall år)")
    val alderAar: Int,
    @DisplayText("Årlig beløp")
    val aarligBeloep: Kroner,
    @DisplayText("Månedlig beløp")
    val maanedligBeloep: Kroner?
)

data class TidsbegrensetOffentligAfp(
    @DisplayText("Alder (antall år)")
    val alderAar: Int,
    @DisplayText("Totalt AFP beløp")
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
    @DisplayText("Alder (anttall år)")
    val alderAar: Int,
    @DisplayText("Årlig beløp")
    val aarligBeloep: Kroner,
    @DisplayText("Kompensasjonstillegg")
    val kompensasjonstillegg: Kroner,
    @DisplayText("Kronetillegg")
    val kronetillegg: Kroner,
    @DisplayText("Livsvarig beløp")
    val livsvarig: Kroner,
    @DisplayText("Månedlig beløp")
    val maanedligBeloep: Kroner?
)

data class Vilkaarsproevingsresultat(
    @DisplayText("Er innvilget")
    val erInnvilget: Boolean,
    @DisplayText("Alternativ")
    val alternativ: Uttaksparametre?
)

data class Trygdetid(
    @DisplayText("Antall år")
    val antallAar: Int,
    @DisplayText("Er utilstrekkelig")
    val erUtilstrekkelig: Boolean
)

data class AarligBeloep(
    @DisplayText("Årstall")
    val aarstall: Year,
    @DisplayText("Beløp")
    val beloep: Kroner
)

data class Uttaksparametre(
    @DisplayText("Gradert uttak alder")
    val gradertUttakAlder: Alder?,
    @DisplayText("Uttaksgrad")
    val uttaksgrad: Percent?,
    @DisplayText("Helt uttak alder")
    val heltUttakAlder: Alder
)

data class Alder(
    @DisplayText("År")
    val aar: Int,
    @DisplayText("Måneder")
    val maaneder: Int
)
