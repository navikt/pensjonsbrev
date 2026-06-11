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
    @DisplayText("Årlig inntekt og pensjon")
    val aarligInntektOgPensjonListe: List<AarligInntektOgPensjon>?,
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
    val afpOffentligTidsbegrenset: TidsbegrensetOffentligAfp?,
)

data class AfpPrivatSimulering(
    @DisplayText("Ved gradert uttak")
    val vedGradertUttak: PrivatAfp?,
    @DisplayText("Ved helt uttak")
    val vedHeltUttak: PrivatAfp,
    @DisplayText("Ved 67 år")
    val vedNormertPensjonsalder: PrivatAfp?,
)

data class AfpOffentligLivsvarigSimulering(
    @DisplayText("Ved gradert uttak")
    val vedGradertUttak: LivsvarigOffentligAfp?,
    @DisplayText("Ved helt uttak")
    val vedHeltUttak: LivsvarigOffentligAfp,
)

data class Alderspensjon(
    val alderAar: Int,
    val beloep: Kroner,
    val gjenlevendetillegg: Kroner?
)

data class LivsvarigOffentligAfp(
    val alderAar: Int,
    val aarligBeloep: Kroner,
    val maanedligBeloep: Kroner
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
    val kronetillegg: Kroner?,
    val livsvarig: Kroner,
    val maanedligBeloep: Kroner
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

data class Uttaksinformasjon (
    val alder: Alder,
    val uttaksdato: String
)

data class Simuleringsinformasjon(
    @DisplayText("Gradert uttaksalder")
    val gradertUttakInformasjon: Uttaksinformasjon?,
    @DisplayText("Helt uttaksalder")
    val heltUttakInformasjon: Uttaksinformasjon,
    val normertUttakInformasjon: Uttaksinformasjon?,
    val sivilstatus: Sivilstatus,
    val utenlandsperioder: List<SimuleringUtenlandsperiode>?,
    val kull: Kull,
    val normertPensjonsalderPlassering: NormertPensjonsalderPlassering?
) : VedleggData

enum class NormertPensjonsalderPlassering {
    MELLOM_GRADERT_OG_HELT,
    ETTER_HELT
}

data class SimuleringV1MaanedligAlderspensjonForKnekkpunkter(
    @DisplayText("Ved gradert uttak")
    val vedGradertUttak: SimuleringV1MaanedligAlderspensjon?,
    @DisplayText("Ved helt uttak")
    val vedHeltUttak: SimuleringV1MaanedligAlderspensjon,
    @DisplayText("Ved normert pensjonsalder")
    val vedNormertPensjonsalder: SimuleringV1MaanedligAlderspensjon?
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

data class AarligInntektOgPensjon(
    val alderLabel: String,
    val alderspensjon: Kroner,
    val avtalefestetPensjon: Kroner,
    val pensjonsgivendeInntekt: Kroner,
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

enum class Kull {
    KAP19,
    KAP20,
    OVERGANG
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