package no.nav.pensjon.etterlatte

import no.nav.pensjon.brevbaker.api.model.*
import no.nav.pensjon.etterlatte.fixtures.createBarnepensjonInnvilgelseDTO
import no.nav.pensjon.etterlatte.fixtures.createBarnepensjonRevurderingAdopsjonDTO
import no.nav.pensjon.etterlatte.fixtures.createBarnepensjonRevurderingOmgjoeringAvFarskapDTO
import no.nav.pensjon.etterlatte.fixtures.createBarnepensjonRevurderingSoeskenjusteringDTO
import no.nav.pensjon.etterlatte.fixtures.createManueltBrevDTO
import no.nav.pensjon.etterlatte.fixtures.createOMSInnvilgelseDTO
import no.nav.pensjon.etterlatte.maler.*
import no.nav.pensjon.etterlatte.maler.barnepensjon.BarnepensjonInnvilgelseDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingAdopsjonDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingOmgjoeringAvFarskapDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingSoeskenjusteringDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.OMSInnvilgelseDTO
import java.time.LocalDate
import kotlin.reflect.KClass

object Fixtures {

    val felles = Felles(
        dokumentDato = LocalDate.of(2020, 1, 1),
        saksnummer = "1337123",
        avsenderEnhet = NAVEnhet(
            nettside = "nav.no",
            navn = "NAV Familie- og pensjonsytelser Porsgrunn",
            telefonnummer = Telefonnummer("55553334"),
        ),
        bruker = Bruker(
            fornavn = "Test",
            mellomnavn = "bruker",
            etternavn = "Testerson",
            foedselsnummer = Foedselsnummer("01019878910"),
        ),
        signerendeSaksbehandlere = SignerendeSaksbehandlere(
            saksbehandler = "Ole Saksbehandler",
            attesterendeSaksbehandler = "Per Attesterende",
        ),
        vergeNavn = null,
    )

    val fellesAuto = felles.copy(signerendeSaksbehandlere = null)

    inline fun <reified T : Any> create(): T = create(T::class)

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> create(letterDataType: KClass<T>): T =
        when (letterDataType) {
            BarnepensjonInnvilgelseDTO::class -> createBarnepensjonInnvilgelseDTO() as T
            ManueltBrevDTO::class -> createManueltBrevDTO() as T
            OMSInnvilgelseDTO::class -> createOMSInnvilgelseDTO() as T
            BarnepensjonRevurderingSoeskenjusteringDTO::class -> createBarnepensjonRevurderingSoeskenjusteringDTO() as T
            BarnepensjonRevurderingAdopsjonDTO::class -> createBarnepensjonRevurderingAdopsjonDTO() as T
            BarnepensjonRevurderingOmgjoeringAvFarskapDTO::class -> createBarnepensjonRevurderingOmgjoeringAvFarskapDTO() as T
            Unit::class -> Unit as T

            else -> throw IllegalArgumentException("Don't know how to construct: ${letterDataType.qualifiedName}")
        }
}