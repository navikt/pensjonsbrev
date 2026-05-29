package no.nav.pensjon.brev.planleggepensjon

import no.nav.brev.brevbaker.FellesFactory
import no.nav.brev.brevbaker.LetterDataFactory
import no.nav.pensjon.brev.api.model.maler.EmptyAutobrevdata
import no.nav.pensjon.brev.api.model.maler.EmptyFagsystemdata
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.planleggepensjon.simulering.AfpOffentligLivsvarigSimulering
import no.nav.pensjon.brev.planleggepensjon.simulering.AfpPrivatSimulering
import no.nav.pensjon.brev.planleggepensjon.simulering.Alder
import no.nav.pensjon.brev.planleggepensjon.simulering.ApSimuleringDto
import no.nav.pensjon.brev.planleggepensjon.simulering.ApSimuleringBrevDto
import no.nav.pensjon.brev.planleggepensjon.simulering.ForbeholdAvsnitt
import no.nav.pensjon.brev.planleggepensjon.simulering.ForbeholdInnhold
import no.nav.pensjon.brev.planleggepensjon.simulering.ForbeholdSeksjon
import no.nav.pensjon.brev.planleggepensjon.simulering.LivsvarigOffentligAfp
import no.nav.pensjon.brev.planleggepensjon.simulering.PrivatAfp
import no.nav.pensjon.brev.planleggepensjon.simulering.Simulering
import no.nav.pensjon.brev.planleggepensjon.simulering.Kull
import no.nav.pensjon.brev.planleggepensjon.simulering.NormertPensjonsalderPlassering
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
        ForbeholdInnhold::class -> createForbeholdInnhold() as T
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
            simuleringsinformasjon = createSimuleringsinformasjon().copy(
                heltUttaksalder = Alder(67,0),
                gradertUttaksalder = Alder(63,2)
            ),
            simulering = createSimulering().copy(
                maanedligAlderspensjonForKnekkpunkter = SimuleringV1MaanedligAlderspensjonForKnekkpunkter(
                    vedHeltUttak = createSimuleringV1MaanedligAlderspensjon(),
                    vedGradertUttak = null,
                    vedNormertPensjonsalder = null
                ),
                afpPrivat = null,
                afpOffentligTidsbegrenset =
                    TidsbegrensetOffentligAfp(
                        alderAar = 63,
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
        pesysData = EmptyFagsystemdata,
    )

    private fun createLagreSimuleringDto() =
        ApSimuleringDto(
            simulering = createSimulering(),
            vilkaarsproevingsresultat = Vilkaarsproevingsresultat(erInnvilget = true, alternativ = null),
            pensjonsgivendeInntektListe = emptyList(),
            simuleringsinformasjon = createSimuleringsinformasjon(),
            trygdetid = null,
            forbehold = createForbeholdInnhold(),
        )

    private fun createForbeholdInnhold() = ForbeholdInnhold(
        seksjoner = listOf(
            ForbeholdSeksjon(
                tittel = null,
                avsnitt = listOf(
                    ForbeholdAvsnitt(
                        tekst = "Pensjonen123 er beregnet med de opplysningene vi har om deg, i tillegg til de opplysningene du har oppgitt, på tidspunktet for beregningen. Dette er derfor et foreløpig estimat på hva du kan forvente deg i pensjon. Pensjonsberegningen er vist i dagens kroneverdi før skatt. Vi har benyttet dagens satser for beregning av minstepensjon. Satsene reguleres hvert år og blir ikke fastsatt før de skal brukes. Fremtidige reguleringer kan ha betydning for når du tidligst kan starte uttak av alderspensjon.",
                        punktliste = null,
                    ),
                    ForbeholdAvsnitt(
                        tekst = "Vi anbefaler at du får en ny beregning når du nærmer deg ønsket pensjonsalder hvis det er lenge til du skal ta ut pensjon. Det vil blant annet kunne skje endringer i din opptjening og endringer i regelverket.",
                        punktliste = null,
                    ),
                ),
            ),
            ForbeholdSeksjon(
                tittel = "Inntekt og godskriving av pensjonsopptjening",
                avsnitt = listOf(
                    ForbeholdAvsnitt(
                        tekst = "I beregningen benytter vi din siste registrerte pensjonsgivende årsinntekt som Nav har mottatt fra Skatteetaten. Den blir brukt som din fremtidige inntekt frem til du starter uttak av alderspensjon, med mindre du har oppgitt annen inntekt.",
                        punktliste = null,
                    ),
                    ForbeholdAvsnitt(
                        tekst = "Pensjonsgivende inntekt blir først gjeldende i pensjon fra januar året etter den er mottatt fra Skatteetaten. Alderspensjonen vil derfor normalt øke som følge av ny opptjening de to første årene etter uttak. Dette vil fremgå i tabell med årlig inntekt og pensjon.",
                        punktliste = null,
                    ),
                    ForbeholdAvsnitt(
                        tekst = "I enkelte tilfeller kan man få avslag på en søknad om alderspensjon selv om man har fått beregnet alderspensjon fra samme tidspunkt. Det kan skje hvis du søker om uttak av pensjon fra neste år. I disse tilfellene må man vente med å søke til midten av desember før man søker for å få med siste gjeldende år med pensjonsgivende inntekt.",
                        punktliste = null,
                    ),
                ),
            ),
            ForbeholdSeksjon(
                tittel = "Opphold utenfor Norge",
                avsnitt = listOf(
                    ForbeholdAvsnitt(
                        tekst = "Når du ikke har opplyst om utenlandsopphold, forutsetter beregningen at du har bodd i Norge fra fylte 16 år og frem til du tar ut pensjon.",
                        punktliste = null,
                    ),
                    ForbeholdAvsnitt(
                        tekst = "Vi bruker opplysningene du har gitt om botid og arbeid i andre land i beregningen. Beregningen og tidspunktet du tidligst kan ta ut den norske pensjonen din kan være unøyaktig hvis du har oppgitt opphold utenfor Norge.",
                        punktliste = listOf(
                            "Det er opptjeningen din i Norge som brukes når Nav beregner alderspensjonen din. Har du oppgitt fremtidige perioder med opphold utenfor Norge, settes inntekt i Norge til null kroner i disse periodene. ",
                            "Medlemstid i land Norge har trygdeavtale med kan også påvirke om du kan starte uttaket før du er 67 år. Beregningen er derfor gjort med forbehold om at de opplysningene vi bruker er riktige. Merk at det kan komme endringer i trygdeavtaler med andre land.",
                        ),
                    ),
                ),
            ),
        ),
    )

    private fun createSimulering() = Simulering(
        alderspensjonListe = emptyList(),
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
                alderAar = 70,
                aarligBeloep = Kroner(60000),
                kompensasjonstillegg = Kroner(1500),
                kronetillegg = null,
                livsvarig = Kroner(2500),
                maanedligBeloep = Kroner(5000),
            ),
            vedNormertPensjonsalder = PrivatAfp(
                alderAar = 67,
                aarligBeloep = Kroner(60000),
                kompensasjonstillegg = Kroner(1500),
                kronetillegg = null,
                livsvarig = Kroner(2500),
                maanedligBeloep = Kroner(5000),
            ),
        ),
        afpOffentligLivsvarig = null,
        afpOffentligTidsbegrenset = null,
    )

    private fun createSimuleringsinformasjon() = Simuleringsinformasjon(
        gradertUttaksalder = Alder(aar = 62, maaneder = 0),
        heltUttaksalder = Alder(aar = 70, maaneder = 0),
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
        kull = Kull.OVERGANG,
        normertPensjonsalderPlassering = NormertPensjonsalderPlassering.MELLOM_GRADERT_OG_HELT,
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
