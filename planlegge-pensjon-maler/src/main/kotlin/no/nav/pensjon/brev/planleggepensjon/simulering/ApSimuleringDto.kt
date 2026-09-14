package no.nav.pensjon.brev.planleggepensjon.simulering

import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.VedleggData
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Percent
import java.time.LocalDate

data class ApSimuleringDtoData(
    val simulering: Simulering,
    val simuleringsinformasjon: Simuleringsinformasjon,
    val vilkaarsproevingsresultat: Vilkaarsproevingsresultat?,
    val trygdetid: Trygdetid?,
    val pensjonsgivendeInntektListe: List<AarligBeloep>?,
    val aarligInntektOgPensjonListe: List<AarligInntektOgPensjon>?,
    val pensjonsopptjeningListe: List<Pensjonsopptjening>?,
    val forbehold: ForbeholdInnhold,
    val kortforbehold: Kortforbehold?,
) : FagsystemBrevdata, VedleggData

data class Simulering(
    val alderspensjonListe: List<Alderspensjon>,
    val maanedligAlderspensjonForKnekkpunkter: SimuleringV1MaanedligAlderspensjonForKnekkpunkter?,
    val afpPrivat: AfpPrivatSimulering?,
    val afpOffentligLivsvarig: AfpOffentligLivsvarigSimulering?,
    val afpOffentligTidsbegrenset: TidsbegrensetOffentligAfp?,
)

data class AfpPrivatSimulering(
    val vedGradertUttak: PrivatAfp?,
    val vedHeltUttak: PrivatAfp,
    val vedNormertPensjonsalder: PrivatAfp?,
)

data class AfpOffentligLivsvarigSimulering(
    val vedGradertUttak: LivsvarigOffentligAfp?,
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
    val uttaksdato: String,
    val grad: Int
)

data class Simuleringsinformasjon(
    val gradertUttakInformasjon: Uttaksinformasjon?,
    val heltUttakInformasjon: Uttaksinformasjon,
    val normertUttakInformasjon: Uttaksinformasjon?,
    val sivilstatus: Sivilstatus,
    val utenlandsperioder: List<SimuleringUtenlandsperiode>?,
    val kull: Kull,
    val normertPensjonsalderPlassering: NormertPensjonsalderPlassering?,
    val simulererEndringMedAfpPrivat: Boolean,
) : VedleggData

enum class NormertPensjonsalderPlassering {
    MELLOM_GRADERT_OG_HELT,
    ETTER_HELT
}

data class SimuleringV1MaanedligAlderspensjonForKnekkpunkter(
    val vedGradertUttak: SimuleringV1MaanedligAlderspensjon?,
    val vedHeltUttak: SimuleringV1MaanedligAlderspensjon,
    val vedNormertPensjonsalder: SimuleringV1MaanedligAlderspensjon?
)

data class SimuleringV1MaanedligAlderspensjon(
    val beloep: Kroner,
    val inntektspensjonBeloep: Kroner?,
    val delingstall: Double?,
    val pensjonsbeholdningFoerUttakBeloep: Kroner?,
    val pensjonsbeholdningEtterUttakBeloep: Kroner?,
    val sluttpoengtall: Double?,
    val poengaarTom1991: Int?,
    val poengaarFom1992: Int?,
    val forholdstall: Double?,
    val grunnpensjonBeloep: Kroner?,
    val tilleggspensjonBeloep: Kroner?,
    val pensjonstillegg: Kroner?,
    val skjermingstillegg: Kroner?,
    val kapittel19AndelTeller: Int?,
    val kapittel19Trygdetid: Int?,
    val basispensjonBeloep: Kroner?,
    val restpensjonBeloep: Kroner?,
    val gjenlevendetillegg: Kroner?,
    val minstePensjonsnivaaSats: Double?,
    val minstePensjonsnivaaBeloep: Kroner?,
    val kapittel20AndelTeller: Int?,
    val kapittel20Trygdetid: Int?,
    val garantipensjonBeloep: Kroner?,
    val garantipensjonSats: Kroner?,
    val garantitilleggBeloep: Kroner?,
    val grunnbeloep: Kroner?
)

data class AarligInntektOgPensjon(
    val alderLabel: String,
    val alderspensjon: Kroner,
    val avtalefestetPensjon: Kroner,
    val pensjonsgivendeInntekt: Kroner,
)

data class Pensjonsopptjening(
    val aarstall: Year,
    val pensjonsgivendeInntekt: Kroner?,
    val pensjonspoeng: Double?,
    val pensjonsbeholdning: Kroner?,
    val merknad: String?,
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
    ENKE_ELLER_ENKEMANN("Enke/enkemann"),
    SKILT("Skilt"),
    SEPARERT("Separert"),
    REGISTRERT_PARTNER("Registrert partner"),
    SEPARERT_PARTNER("Separert partner"),
    SKILT_PARTNER("Skilt partner"),
    GJENLEVENDE_PARTNER("Gjenlevende partner"),
    SAMBOER("Samboer")
}

enum class Kull {
    KAP19,
    KAP20,
    OVERGANG
}

data class ForbeholdInnhold(
    val seksjoner: List<ForbeholdSeksjon>?,
) : VedleggData

data class ForbeholdSeksjon(
    val tittel: String?,
    val avsnitt: List<ForbeholdAvsnitt>,
)

data class Kortforbehold(
    val avsnitt: List<ForbeholdAvsnitt>,
)

data class ForbeholdAvsnitt(
    val tekst: String,
    val punktliste: List<String>?,
)
