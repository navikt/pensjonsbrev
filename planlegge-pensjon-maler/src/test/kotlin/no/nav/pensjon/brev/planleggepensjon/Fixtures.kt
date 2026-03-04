package no.nav.pensjon.brev.planleggepensjon

import no.nav.brev.brevbaker.FellesFactory
import no.nav.brev.brevbaker.LetterDataFactory
import no.nav.pensjon.brev.api.model.maler.EmptyAutobrevdata
import no.nav.pensjon.brev.api.model.maler.EmptyFagsystemdata
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.planleggepensjon.simulering.LagreSimuleringDto
import no.nav.pensjon.brev.planleggepensjon.simulering.LagreVilkaarsproevingsresultat
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringBrev
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringBrevDto
import kotlin.math.ceil
import kotlin.reflect.KClass

object Fixtures : LetterDataFactory {

    val felles = FellesFactory.felles

    inline fun <reified T : Any> create(): T = create(T::class)

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> create(letterDataType: KClass<T>): T =
        when (letterDataType) {
            SimuleringBrevDto::class -> createSimuleringBrevDto() as T
            EmptyRedigerbarBrevdata::class -> EmptyRedigerbarBrevdata as T
            EmptyAutobrevdata::class -> EmptyAutobrevdata as T
            else -> throw IllegalArgumentException("Don't know how to construct: ${letterDataType.qualifiedName}")
        }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> createVedlegg(letterDataType: KClass<T>): T = when (letterDataType) {
        else -> throw IllegalArgumentException("Don't know how to construct: ${letterDataType.qualifiedName}")
    }

    private fun createSimuleringBrevDto() = SimuleringBrevDto(
        saksbehandlerValg = createLagreSimuleringDto(),
        pesysData = EmptyFagsystemdata,
    )

    private fun createLagreSimuleringDto() =
        LagreSimuleringDto(
            alderspensjonListe = emptyList(),
            livsvarigOffentligAfpListe = emptyList(),
            tidsbegrensetOffentligAfp = null,
            privatAfpListe = emptyList(),
            vilkaarsproevingsresultat = LagreVilkaarsproevingsresultat(erInnvilget = true, alternativ = null),
            trygdetid = null,
            pensjonsgivendeInntektListe = emptyList(),
        )
}