package no.nav.pensjon.etterlatte

import no.nav.pensjon.brevbaker.api.model.Bruker
import no.nav.pensjon.brevbaker.api.model.Felles
import no.nav.pensjon.brevbaker.api.model.Foedselsnummer
import no.nav.pensjon.brevbaker.api.model.NAVEnhet
import no.nav.pensjon.brevbaker.api.model.SignerendeSaksbehandlere
import no.nav.pensjon.brevbaker.api.model.Telefonnummer
import no.nav.pensjon.etterlatte.fixtures.*
import no.nav.pensjon.etterlatte.maler.ManueltBrevDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.BarnepensjonInnvilgelseDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.avslag.BarnepensjonAvslagIkkeYrkesskadeDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.endring.EndringHovedmalDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.endring.BarnepensjonEndringInstitusjonsoppholdDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.ny.BarnepensjonInnvilgelseEnkelDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.ny.BarnepensjonInnvilgelseNyDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingAdopsjonDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingOmgjoeringAvFarskapDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingSoeskenjusteringDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingYrkesskadeDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.OMSInnvilgelseDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.foerstegangsvedtak.FoerstegangsvedtakUtfallDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.foerstegangsvedtak.OMSInnvilgelseFoerstegangsvedtakDTO
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
            BarnepensjonInnvilgelseNyDTO::class -> createBarnepensjonInnvilgelseNyDTO() as T
            BarnepensjonInnvilgelseEnkelDTO::class -> createBarnepensjonInnvilgelseEnkelDTO() as T
            ManueltBrevDTO::class -> createManueltBrevDTO() as T
            OMSInnvilgelseDTO::class -> createOMSInnvilgelseDTO() as T
            BarnepensjonEndringInstitusjonsoppholdDTO::class -> createBarnepensjonEndringInstitusjonsoppholdDTO() as T
            EndringHovedmalDTO::class -> createEndringHovedmalDTO() as T
            OMSInnvilgelseFoerstegangsvedtakDTO::class -> createOMSInnvilgelseFoerstegangsvedtakDTO() as T
            FoerstegangsvedtakUtfallDTO::class -> createOMSFoerstegangsvedtakUtfallDTO() as T
            BarnepensjonRevurderingAdopsjonDTO::class -> createBarnepensjonRevurderingAdopsjonDTO() as T
            BarnepensjonRevurderingSoeskenjusteringDTO::class -> createBarnepensjonRevurderingSoeskenjusteringDTO() as T
            BarnepensjonRevurderingOmgjoeringAvFarskapDTO::class -> createBarnepensjonRevurderingOmgjoeringAvFarskapDTO() as T
            BarnepensjonRevurderingYrkesskadeDTO::class -> createBarnepensjonRevurderingYrkesskadeDTO() as T
            BarnepensjonAvslagIkkeYrkesskadeDTO::class -> createBarnepensjonAvslagIkkeYrkesskadeDTO() as T
            Unit::class -> Unit as T

            else -> throw IllegalArgumentException("Don't know how to construct: ${letterDataType.qualifiedName}")
        }
}
