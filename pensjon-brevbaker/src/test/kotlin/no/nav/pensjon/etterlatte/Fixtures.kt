package no.nav.pensjon.etterlatte

import no.nav.pensjon.brevbaker.api.model.Bruker
import no.nav.pensjon.brevbaker.api.model.Felles
import no.nav.pensjon.brevbaker.api.model.Foedselsnummer
import no.nav.pensjon.brevbaker.api.model.NAVEnhet
import no.nav.pensjon.brevbaker.api.model.SignerendeSaksbehandlere
import no.nav.pensjon.brevbaker.api.model.Telefonnummer
import no.nav.pensjon.etterlatte.fixtures.createBarnepensjonInnvilgelseDTO
import no.nav.pensjon.etterlatte.fixtures.createBarnepensjonInnvilgelseEnkelDTO
import no.nav.pensjon.etterlatte.fixtures.createBarnepensjonInnvilgelseNyDTO
import no.nav.pensjon.etterlatte.fixtures.createBarnepensjonOmregnetNyttRegelverkDTO
import no.nav.pensjon.etterlatte.fixtures.createBarnepensjonRevurderingAdopsjonDTO
import no.nav.pensjon.etterlatte.fixtures.createBarnepensjonRevurderingOmgjoeringAvFarskapDTO
import no.nav.pensjon.etterlatte.fixtures.createBarnepensjonRevurderingSoeskenjusteringDTO
import no.nav.pensjon.etterlatte.fixtures.createEndringHovedmalDTO
import no.nav.pensjon.etterlatte.fixtures.createManueltBrevDTO
import no.nav.pensjon.etterlatte.fixtures.createOMSAvslagDTO
import no.nav.pensjon.etterlatte.fixtures.createOMSFoerstegangsvedtakUtfallDTO
import no.nav.pensjon.etterlatte.fixtures.createOMSInnvilgelseDTO
import no.nav.pensjon.etterlatte.fixtures.createOMSInnvilgelseFoerstegangsvedtakDTO
import no.nav.pensjon.etterlatte.fixtures.createOMSOpphoerDTO
import no.nav.pensjon.etterlatte.fixtures.createOMSRevurderingEndringDTO
import no.nav.pensjon.etterlatte.fixtures.createTilbakekrevingFerdigDTO
import no.nav.pensjon.etterlatte.fixtures.createTilbakekrevingInnholdDTO
import no.nav.pensjon.etterlatte.fixtures.createTomMal
import no.nav.pensjon.etterlatte.maler.ManueltBrevDTO
import no.nav.pensjon.etterlatte.maler.TomMal
import no.nav.pensjon.etterlatte.maler.barnepensjon.BarnepensjonInnvilgelseDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.endring.EndringHovedmalDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.ny.BarnepensjonInnvilgelseEnkelDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.migrering.BarnepensjonOmregnetNyttRegelverkDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.ny.BarnepensjonInnvilgelseNyDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingAdopsjonDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingOmgjoeringAvFarskapDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingSoeskenjusteringDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.OMSInnvilgelseDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.avslag.OMSAvslagDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.foerstegangsvedtak.FoerstegangsvedtakUtfallDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.foerstegangsvedtak.OMSInnvilgelseFoerstegangsvedtakDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.opphoer.OMSOpphoerDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OMSRevurderingEndringDTO
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingFerdigDTO
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingInnholdDTO
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
            EndringHovedmalDTO::class -> createEndringHovedmalDTO() as T
            OMSInnvilgelseFoerstegangsvedtakDTO::class -> createOMSInnvilgelseFoerstegangsvedtakDTO() as T
            OMSRevurderingEndringDTO::class -> createOMSRevurderingEndringDTO() as T
            OMSOpphoerDTO::class -> createOMSOpphoerDTO() as T
            OMSAvslagDTO::class -> createOMSAvslagDTO() as T
            FoerstegangsvedtakUtfallDTO::class -> createOMSFoerstegangsvedtakUtfallDTO() as T
            BarnepensjonRevurderingAdopsjonDTO::class -> createBarnepensjonRevurderingAdopsjonDTO() as T
            BarnepensjonRevurderingSoeskenjusteringDTO::class -> createBarnepensjonRevurderingSoeskenjusteringDTO() as T
            BarnepensjonRevurderingOmgjoeringAvFarskapDTO::class -> createBarnepensjonRevurderingOmgjoeringAvFarskapDTO() as T
            BarnepensjonOmregnetNyttRegelverkDTO::class -> createBarnepensjonOmregnetNyttRegelverkDTO() as T
            TilbakekrevingInnholdDTO::class -> createTilbakekrevingInnholdDTO() as T
            TilbakekrevingFerdigDTO::class -> createTilbakekrevingFerdigDTO() as T
            TomMal::class -> createTomMal() as T
            Unit::class -> Unit as T

            else -> throw IllegalArgumentException("Don't know how to construct: ${letterDataType.qualifiedName}")
        }
}
