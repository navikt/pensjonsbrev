package no.nav.pensjon.brev.planleggepensjon

import no.nav.brev.brevbaker.FellesFactory
import no.nav.brev.brevbaker.LetterDataFactory
import no.nav.pensjon.brev.api.model.maler.EmptyAutobrevdata
import no.nav.pensjon.brev.api.model.maler.EmptyFagsystemdata
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.planleggepensjon.simulering.Alder
import no.nav.pensjon.brev.planleggepensjon.simulering.ApSimuleringDto
import no.nav.pensjon.brev.planleggepensjon.simulering.ApSimuleringBrevDto
import no.nav.pensjon.brev.planleggepensjon.simulering.Simuleringsinformasjon
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringV1MaanedligAlderspensjon
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringV1MaanedligAlderspensjonForKnekkpunkter
import no.nav.pensjon.brev.planleggepensjon.simulering.Vilkaarsproevingsresultat
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
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
        Simuleringsinformasjon::class -> createSimuleringsinformasjon() as T
        else -> throw IllegalArgumentException("Don't know how to construct: ${letterDataType.qualifiedName}")
    }

    private fun createSimuleringBrevDto() = ApSimuleringBrevDto(
        saksbehandlerValg = createLagreSimuleringDto(),
        pesysData = EmptyFagsystemdata,
    )

    private fun createLagreSimuleringDto() =
        ApSimuleringDto(
            alderspensjonListe = emptyList(),
            livsvarigOffentligAfpListe = emptyList(),
            tidsbegrensetOffentligAfp = null,
            privatAfpListe = emptyList(),
            vilkaarsproevingsresultat = Vilkaarsproevingsresultat(erInnvilget = true, alternativ = null),
            trygdetid = null,
            pensjonsgivendeInntektListe = emptyList(),
            simuleringsinformasjon = createSimuleringsinformasjon(),
        )

    private fun createSimuleringsinformasjon() = Simuleringsinformasjon(
        gradertUttaksalder = null,
        heltUttaksalder = Alder(aar = 67, maaneder = 0),
        maanedligAlderspensjonForKnekkpunkter = SimuleringV1MaanedligAlderspensjonForKnekkpunkter(
            vedGradertUttak = null,
            vedHeltUttak = createSimuleringV1MaanedligAlderspensjon(),
            vedNormertPensjonsalder = createSimuleringV1MaanedligAlderspensjon(),
        ),
    )

    private fun createSimuleringV1MaanedligAlderspensjon() = SimuleringV1MaanedligAlderspensjon(
        beloep = Kroner(29133),
        inntektspensjonBeloep = Kroner(1982),
        delingstall = 15.33,
        pensjonsbeholdningFoerUttakBeloep = Kroner(500000),
        pensjonsbeholdningEtterUttakBeloep = Kroner(0),
        sluttpoengtall = null,
        poengaarTom1991 = null,
        poengaarFom1992 = null,
        forholdstall = null,
        grunnpensjonBeloep = Kroner(14017),
        tilleggspensjonBeloep = Kroner(4872),
        pensjonstillegg = Kroner(4872),
        skjermingstillegg = null,
        kapittel19Andel = null,
        kapittel19Trygdetid = null,
        basispensjonBeloep = null,
        restpensjonBeloep = null,
        gjenlevendetillegg = null,
        minstePensjonsnivaaSats = null,
        kapittel20Andel = null,
        kapittel20Trygdetid = null,
        garantipensjonBeloep = Kroner(3390),
        garantipensjonSats = null,
        garantitilleggBeloep = null,
    )
}