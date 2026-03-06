package no.nav.pensjon.brev.planleggepensjon.simulering

import no.nav.pensjon.brev.api.model.maler.ValgForBrevSomBestillesEksternt
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Percent

data class ApSimuleringDto(
    val alderspensjonListe: List<Alderspensjon>,
    val livsvarigOffentligAfpListe: List<LivsvarigOffentligAfp>,
    val tidsbegrensetOffentligAfp: TidsbegrensetOffentligAfp?,
    val privatAfpListe: List<PrivatAfp>,
    val vilkaarsproevingsresultat: Vilkaarsproevingsresultat,
    val trygdetid: Trygdetid?,
    val pensjonsgivendeInntektListe: List<AarligBeloep>
) : ValgForBrevSomBestillesEksternt

data class Alderspensjon(
    val alderAar: Int,
    val beloep: Kroner,
    val gjenlevendetillegg: Kroner?
)

data class LivsvarigOffentligAfp(
    val alderAar: Int,
    val aarligBeloep: Kroner,
    val maanedligBeloep: Kroner?
)

data class TidsbegrensetOffentligAfp(
    val alderAar: Int,
    val totaltAfpBeloep: Kroner,
    val tidligereArbeidsinntekt: Kroner,
    val grunnbeloep: Kroner,
    val sluttpoengtall: Double,
    val trygdetid: Int,
    val poengaarTom1991: Int,
    val poengaarFom1992: Int,
    val grunnpensjon: Kroner,
    val tilleggspensjon: Kroner,
    val afpTillegg: Kroner,
    val saertillegg: Kroner,
    val afpGrad: Percent,
    val erAvkortet: Boolean
)

data class PrivatAfp(
    val alderAar: Int,
    val aarligBeloep: Kroner,
    val kompensasjonstillegg: Kroner,
    val kronetillegg: Kroner,
    val livsvarig: Kroner,
    val maanedligBeloep: Kroner?
)

data class Vilkaarsproevingsresultat(
    val erInnvilget: Boolean,
    val alternativ: Uttaksparametre?
)

data class Trygdetid(
    val antallAar: Int,
    val erUtilstrekkelig: Boolean
)

data class AarligBeloep(
    val aarstall: Year,
    val beloep: Kroner
)

data class Uttaksparametre(
    val gradertUttakAlder: Alder?,
    val uttaksgrad: Percent?,
    val heltUttakAlder: Alder
)

data class Alder(
    val aar: Int,
    val maaneder: Int
)
