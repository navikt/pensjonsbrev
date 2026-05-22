package no.nav.pensjon.brev.planleggepensjon.simulering

import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgBrevdata
import no.nav.pensjon.brev.api.model.maler.VedleggData
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Percent
import no.nav.pensjon.brevbaker.api.model.DisplayText
import java.time.LocalDate

data class ApSimuleringDto(
    @DisplayText("Simulering")
    val simulering: Simulering,
    @DisplayText("Simuleringsinformasjon")
    val simuleringsinformasjon: Simuleringsinformasjon,
    @DisplayText("Vilkårsprøvingsresultat")
    val vilkaarsproevingsresultat: Vilkaarsproevingsresultat?,
    @DisplayText("Trygdetid")
    val trygdetid: Trygdetid?,
    @DisplayText("Pensjonsgivende inntekt")
    val pensjonsgivendeInntektListe: List<AarligBeloep>?,
    @DisplayText("Forbehold")
    val forbehold: ForbeholdInnhold,
) : SaksbehandlerValgBrevdata, VedleggData

data class Simulering(
    @DisplayText("Alderspensjon")
    val alderspensjonListe: List<Alderspensjon>,
    @DisplayText("Månedlig alderspensjon for knekkpunkter")
    val maanedligAlderspensjonForKnekkpunkter: SimuleringV1MaanedligAlderspensjonForKnekkpunkter?,
    @DisplayText("AFP privat")
    val afpPrivat: AfpPrivatSimulering?,
    @DisplayText("AFP offentlig livsvarig")
    val afpOffentligLivsvarig: AfpOffentligLivsvarigSimulering?,
    @DisplayText("AFP offentlig tidsbegrenset")
    val afpOffentligTidsbegrenset: AfpOffentligTidsbegrensetSimulering?,
)

data class AfpPrivatSimulering(
    @DisplayText("Ved gradert uttak")
    val vedGradertUttak: PrivatAfp?,
    @DisplayText("Ved helt uttak")
    val vedHeltUttak: PrivatAfp,
)

data class AfpOffentligLivsvarigSimulering(
    @DisplayText("Ved gradert uttak")
    val vedGradertUttak: LivsvarigOffentligAfp?,
    @DisplayText("Ved helt uttak")
    val vedHeltUttak: LivsvarigOffentligAfp,
)

data class AfpOffentligTidsbegrensetSimulering(
    @DisplayText("Ved gradert uttak")
    val vedGradertUttak: TidsbegrensetOffentligAfp?,
    @DisplayText("Ved helt uttak")
    val vedHeltUttak: TidsbegrensetOffentligAfp,
)

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
    val maanedligBeloep: Kroner
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
    val maanedligBeloep: Kroner
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

data class Simuleringsinformasjon(
    @DisplayText("Gradert uttaksalder")
    val gradertUttaksalder: Alder?,
    @DisplayText("Helt uttaksalder")
    val heltUttaksalder: Alder,
    val sivilstatus: Sivilstatus,
    val utenlandsperioder: List<SimuleringUtenlandsperiode>?
) : VedleggData

data class SimuleringV1MaanedligAlderspensjonForKnekkpunkter(
    @DisplayText("Ved gradert uttak")
    val vedGradertUttak: SimuleringV1MaanedligAlderspensjon?,
    @DisplayText("Ved helt uttak")
    val vedHeltUttak: SimuleringV1MaanedligAlderspensjon,
    @DisplayText("Ved normert pensjonsalder")
    val vedNormertPensjonsalder: SimuleringV1MaanedligAlderspensjon
)

data class SimuleringV1MaanedligAlderspensjon(
    @DisplayText("Beløp")
    val beloep: Kroner,
    @DisplayText("Inntektspensjon beløp")
    val inntektspensjonBeloep: Kroner?,
    @DisplayText("Delingstall")
    val delingstall: Double?,
    @DisplayText("Pensjonsbeholdning før uttak")
    val pensjonsbeholdningFoerUttakBeloep: Kroner?,
    @DisplayText("Pensjonsbeholdning etter uttak")
    val pensjonsbeholdningEtterUttakBeloep: Kroner?,
    @DisplayText("Sluttpoengtall")
    val sluttpoengtall: Double?,
    @DisplayText("Poengår t.o.m. 1991")
    val poengaarTom1991: Int?,
    @DisplayText("Poengår f.o.m. 1992")
    val poengaarFom1992: Int?,
    @DisplayText("Forholdstall")
    val forholdstall: Double?,
    @DisplayText("Grunnpensjon beløp")
    val grunnpensjonBeloep: Kroner?,
    @DisplayText("Tilleggspensjon beløp")
    val tilleggspensjonBeloep: Kroner?,
    @DisplayText("Pensjonstillegg")
    val pensjonstillegg: Kroner?,
    @DisplayText("Skjermingstillegg")
    val skjermingstillegg: Kroner?,
    @DisplayText("Kapittel 19 andelsbrøk teller")
    val kapittel19AndelTeller: Int?,
    @DisplayText("Kapittel 19 trygdetid")
    val kapittel19Trygdetid: Int?,
    @DisplayText("Basispensjon beløp")
    val basispensjonBeloep: Kroner?,
    @DisplayText("Restpensjon beløp")
    val restpensjonBeloep: Kroner?,
    @DisplayText("Gjenlevendetillegg")
    val gjenlevendetillegg: Kroner?,
    @DisplayText("Minste pensjonsnivå sats")
    val minstePensjonsnivaaSats: Double?,
    @DisplayText("Minste pensjonsnivå beløp")
    val minstePensjonsnivaaBeloep: Kroner?,
    @DisplayText("Kapittel 20 andelsbrøk teller")
    val kapittel20AndelTeller: Int?,
    @DisplayText("Kapittel 20 trygdetid")
    val kapittel20Trygdetid: Int?,
    @DisplayText("Garantipensjon beløp")
    val garantipensjonBeloep: Kroner?,
    @DisplayText("Garantipensjonsnivå beløp")
    val garantipensjonsnivaaBeloep: Kroner?,
    @DisplayText("Garantipensjon sats")
    val garantipensjonSats: Double?,
    @DisplayText("Garantitillegg beløp")
    val garantitilleggBeloep: Kroner?,
    @DisplayText("Grunnbeløp")
    val grunnbeloep: Kroner?
)

data class SimuleringUtenlandsperiode(
    val fom: LocalDate,
    val tom: LocalDate? = null,
    val landkode: String,
    val arbeidetUtenlands: Boolean?
)

enum class Sivilstatus(val value: String = "None") {
    UNKNOWN,
    UOPPGITT,
    UGIFT("Ugift"),
    GIFT("Gift"),
    ENKE_ELLER_ENKEMANN,
    SKILT("Skilt"),
    SEPARERT("Separert"),
    REGISTRERT_PARTNER,
    SEPARERT_PARTNER,
    SKILT_PARTNER,
    GJENLEVENDE_PARTNER,
    SAMBOER("Samboer")
}

data class ForbeholdInnhold(
    @DisplayText("Seksjoner")
    val seksjoner: List<ForbeholdSeksjon>?,
) : VedleggData

data class ForbeholdSeksjon(
    @DisplayText("Tittel")
    val tittel: String?,
    @DisplayText("Avsnitt")
    val avsnitt: List<ForbeholdAvsnitt>,
)

data class ForbeholdAvsnitt(
    @DisplayText("Tekst")
    val tekst: String,
    @DisplayText("Punktliste")
    val punktliste: List<String>?,
)