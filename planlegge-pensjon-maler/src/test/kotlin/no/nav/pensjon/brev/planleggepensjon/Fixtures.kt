package no.nav.pensjon.brev.planleggepensjon

import no.nav.brev.brevbaker.FellesFactory
import no.nav.brev.brevbaker.LetterDataFactory
import no.nav.pensjon.brev.api.model.maler.EmptyAutobrevdata
import no.nav.pensjon.brev.api.model.maler.EmptyFagsystemdata
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.planleggepensjon.simulering.AfpOffentligLivsvarigSimulering
import no.nav.pensjon.brev.planleggepensjon.simulering.AfpOffentligTidsbegrensetSimulering
import no.nav.pensjon.brev.planleggepensjon.simulering.AfpPrivatSimulering
import no.nav.pensjon.brev.planleggepensjon.simulering.Alder
import no.nav.pensjon.brev.planleggepensjon.simulering.Alderspensjon
import no.nav.pensjon.brev.planleggepensjon.simulering.ApSimuleringDto
import no.nav.pensjon.brev.planleggepensjon.simulering.ApSimuleringBrevDto
import no.nav.pensjon.brev.planleggepensjon.simulering.LivsvarigOffentligAfp
import no.nav.pensjon.brev.planleggepensjon.simulering.PrivatAfp
import no.nav.pensjon.brev.planleggepensjon.simulering.Simulering
import no.nav.pensjon.brev.planleggepensjon.simulering.Simuleringsinformasjon
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringUtenlandsperiode
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringV1MaanedligAlderspensjon
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringV1MaanedligAlderspensjonForKnekkpunkter
import no.nav.pensjon.brev.planleggepensjon.simulering.Sivilstatus
import no.nav.pensjon.brev.planleggepensjon.simulering.TidsbegrensetOffentligAfp
import no.nav.pensjon.brev.planleggepensjon.simulering.Vilkaarsproevingsresultat
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Percent
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate
import kotlin.reflect.KClass

object Fixtures : LetterDataFactory {

    val felles = FellesFactory.felles

    inline fun <reified T : Any> create(): T = create(T::class)

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> create(letterDataType: KClass<T>): T =
        when (letterDataType) {
            ApSimuleringBrevDto::class -> createSimuleringBrevDto() as T
            EmptyRedigerbarBrevdata::class -> EmptyRedigerbarBrevdata as T
            EmptyAutobrevdata::class -> EmptyAutobrevdata as T
            else -> throw IllegalArgumentException("Don't know how to construct: ${letterDataType.qualifiedName}")
        }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> createVedlegg(letterDataType: KClass<T>): T = when (letterDataType) {
        ApSimuleringDto::class -> createLagreSimuleringDto() as T
        Simuleringsinformasjon::class -> createSimuleringsinformasjon() as T
        else -> throw IllegalArgumentException("Don't know how to construct: ${letterDataType.qualifiedName}")
    }

    private fun createSimuleringBrevDto() = createBrevDtoMedAfpPrivat()

    fun createBrevDtoMedAfpPrivat() = ApSimuleringBrevDto(
        saksbehandlerValg = createLagreSimuleringDto(),
        pesysData = EmptyFagsystemdata,
    )

    fun createBrevDtoMedAfpOffentligLivsvarig() = ApSimuleringBrevDto(
        saksbehandlerValg = createLagreSimuleringDto().copy(
            simulering = createSimulering().copy(
                afpPrivat = null,
                afpOffentligLivsvarig = AfpOffentligLivsvarigSimulering(
                    vedGradertUttak = LivsvarigOffentligAfp(
                        alderAar = 62,
                        aarligBeloep = Kroner(36000),
                        maanedligBeloep = Kroner(3000),
                    ),
                    vedHeltUttak = LivsvarigOffentligAfp(
                        alderAar = 67,
                        aarligBeloep = Kroner(48000),
                        maanedligBeloep = Kroner(4000),
                    ),
                ),
            ),
        ),
        pesysData = EmptyFagsystemdata,
    )

    fun createBrevDtoMedAfpOffentligTidsbegrenset() = ApSimuleringBrevDto(
        saksbehandlerValg = createLagreSimuleringDto().copy(
            simulering = createSimulering().copy(
                afpPrivat = null,
                afpOffentligTidsbegrenset = AfpOffentligTidsbegrensetSimulering(
                    vedGradertUttak = TidsbegrensetOffentligAfp(
                        alderAar = 62,
                        totaltAfpBeloep = Kroner(8500),
                        tidligereArbeidsinntekt = Kroner(550000),
                        grunnbeloep = Kroner(130160),
                        sluttpoengtall = 4.73,
                        trygdetid = 40,
                        poengaarTom1991 = 4,
                        poengaarFom1992 = 36,
                        grunnpensjon = Kroner(3500),
                        tilleggspensjon = Kroner(2800),
                        afpTillegg = Kroner(1600),
                        saertillegg = Kroner(600),
                        afpGrad = Percent(100),
                        erAvkortet = false,
                    ),
                    vedHeltUttak = TidsbegrensetOffentligAfp(
                        alderAar = 67,
                        totaltAfpBeloep = Kroner(10200),
                        tidligereArbeidsinntekt = Kroner(550000),
                        grunnbeloep = Kroner(130160),
                        sluttpoengtall = 4.73,
                        trygdetid = 40,
                        poengaarTom1991 = 4,
                        poengaarFom1992 = 36,
                        grunnpensjon = Kroner(4200),
                        tilleggspensjon = Kroner(3300),
                        afpTillegg = Kroner(1900),
                        saertillegg = Kroner(800),
                        afpGrad = Percent(100),
                        erAvkortet = false,
                    ),
                ),
            ),
        ),
        pesysData = EmptyFagsystemdata,
    )

    private fun createLagreSimuleringDto() =
        ApSimuleringDto(
            simulering = createSimulering(),
            vilkaarsproevingsresultat = Vilkaarsproevingsresultat(erInnvilget = true, alternativ = null),
            pensjonsgivendeInntektListe = emptyList(),
            simuleringsinformasjon = createSimuleringsinformasjon(),
            trygdetid = null,
        )

    private fun createSimulering() = Simulering(
        alderspensjonListe = listOf(
            Alderspensjon(alderAar = 62, beloep = Kroner(18500), gjenlevendetillegg = null),
            Alderspensjon(alderAar = 67, beloep = Kroner(25000), gjenlevendetillegg = null),
            Alderspensjon(alderAar = 70, beloep = Kroner(29133), gjenlevendetillegg = Kroner(3000)),
        ),
        maanedligAlderspensjonForKnekkpunkter = SimuleringV1MaanedligAlderspensjonForKnekkpunkter(
            vedGradertUttak = createSimuleringV1MaanedligAlderspensjon().copy(
                beloep = Kroner(18500),
                inntektspensjonBeloep = Kroner(1200),
                grunnpensjonBeloep = Kroner(9000),
                tilleggspensjonBeloep = Kroner(3100),
                pensjonstillegg = Kroner(2800),
                garantipensjonBeloep = Kroner(2400),
            ),
            vedHeltUttak = createSimuleringV1MaanedligAlderspensjon(),
            vedNormertPensjonsalder = createSimuleringV1MaanedligAlderspensjon(),
        ),
        afpPrivat = AfpPrivatSimulering(
            vedGradertUttak = PrivatAfp(
                alderAar = 62,
                aarligBeloep = Kroner(48000),
                kompensasjonstillegg = Kroner(1200),
                kronetillegg = Kroner(800),
                livsvarig = Kroner(2000),
                maanedligBeloep = Kroner(4000),
            ),
            vedHeltUttak = PrivatAfp(
                alderAar = 67,
                aarligBeloep = Kroner(60000),
                kompensasjonstillegg = Kroner(1500),
                kronetillegg = Kroner(1000),
                livsvarig = Kroner(2500),
                maanedligBeloep = Kroner(5000),
            ),
        ),
        afpOffentligLivsvarig = null,
        afpOffentligTidsbegrenset = null,
    )

    private fun createSimuleringsinformasjon() = Simuleringsinformasjon(
        gradertUttaksalder = Alder(aar = 62, maaneder = 0),
        heltUttaksalder = Alder(aar = 67, maaneder = 0),
        sivilstatus = Sivilstatus.UGIFT,
        utenlandsperioder = listOf(
            SimuleringUtenlandsperiode(
                fom = LocalDate.of(2019, 2, 2),
                tom = LocalDate.of(2020, 12, 11),
                landkode = "SWE",
                arbeidetUtenlands = true,
            ),
            SimuleringUtenlandsperiode(
                fom = LocalDate.of(2024, 2, 1),
                tom = null,
                landkode = "ESP",
                arbeidetUtenlands = false,
            ),
        ),
    )

    private fun createSimuleringV1MaanedligAlderspensjon() = SimuleringV1MaanedligAlderspensjon(
        beloep = Kroner(29133),
        inntektspensjonBeloep = Kroner(1982),
        delingstall = 16.26,
        pensjonsbeholdningFoerUttakBeloep = Kroner(1092923),
        pensjonsbeholdningEtterUttakBeloep = Kroner(604923),
        sluttpoengtall = 4.73,
        poengaarTom1991 = 4,
        poengaarFom1992 = 36,
        forholdstall = 1.137,
        grunnpensjonBeloep = Kroner(14017),
        tilleggspensjonBeloep = Kroner(4872),
        pensjonstillegg = Kroner(4872),
        skjermingstillegg = null,
        kapittel19AndelTeller = 1,
        kapittel19Trygdetid = 40,
        basispensjonBeloep = null,
        restpensjonBeloep = null,
        gjenlevendetillegg = null,
        minstePensjonsnivaaSats = null,
        minstePensjonsnivaaBeloep = Kroner(279933),
        kapittel20AndelTeller = 9,
        kapittel20Trygdetid = 40,
        garantipensjonBeloep = Kroner(3390),
        garantipensjonsnivaaBeloep = Kroner(224248),
        garantipensjonSats = null,
        garantitilleggBeloep = Kroner(34223),
        grunnbeloep = Kroner(130160),
    )
}
