package no.nav.pensjon.brev.alder.maler.endring

import no.nav.brev.brevbaker.vilkaarligDato
import no.nav.pensjon.brev.alder.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.alder.model.Beregningsmetode
import no.nav.pensjon.brev.alder.model.endring.OpplysningerBruktIBeregningenEndretUttaksgradDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Percent
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year

fun createOpplysningerBruktIBeregningenEndretUttaksgradDto(alderspensjonRegelverkType: AlderspensjonRegelverkType = AlderspensjonRegelverkType.AP2025) =
    OpplysningerBruktIBeregningenEndretUttaksgradDto(
        alderspensjonVedVirk = OpplysningerBruktIBeregningenEndretUttaksgradDto.AlderspensjonVedVirk(
            uttaksgrad = Percent(80),
            regelverkType = alderspensjonRegelverkType,
            andelKap19 = 40,
            andelKap20 = 60
        ),
        oppfrisketOpptjeningVedVirk = OpplysningerBruktIBeregningenEndretUttaksgradDto.OppfrisketOpptjeningVedVirk(
            sisteGyldigeOpptjeningsAr = Year(2022),
            poenggivendeInntektSisteGyldigeOpptjeningsAr = Kroner(987),
            poengtallSisteGyldigeOpptjeningsAr = 4.0,
            opptjeningTilfortKap20 = Kroner(234)
        ),
        bruker = OpplysningerBruktIBeregningenEndretUttaksgradDto.Bruker(
            fodselsdato = vilkaarligDato
        ),
        krav = OpplysningerBruktIBeregningenEndretUttaksgradDto.Krav(
            virkDatoFom = vilkaarligDato
        ),
        trygdetidsdetaljerKap19VedVirk = OpplysningerBruktIBeregningenEndretUttaksgradDto.TrygdetidsdetaljerKap19VedVirk(
            anvendtTT = 40,
            beregningsmetode = Beregningsmetode.FOLKETRYGD
        ),
        beregningKap19VedVirk = OpplysningerBruktIBeregningenEndretUttaksgradDto.BeregningKap19VedVirk(
            sluttpoengtall = 10.0,
            poengAr = 50,
            poengArf92 = 1,
            poengAre91 = 3,
            forholdstallLevealder = 2.5
        ),
        endretUttaksgradVedVirk = OpplysningerBruktIBeregningenEndretUttaksgradDto.EndretUttaksgradVedVirk(
            restGrunnpensjon = Kroner(299),
            restTilleggspensjon = Kroner(50),
            pensjonsbeholdning = Kroner(1000),
            garantipensjonsBeholdning = Kroner(10)
        ),
        trygdetidsdetaljerKap20VedVirk = OpplysningerBruktIBeregningenEndretUttaksgradDto.TrygdetidsdetaljerKap20VedVirk(
            anvendtTT = 40
        ),
        beregningKap20VedVirk = OpplysningerBruktIBeregningenEndretUttaksgradDto.BeregningKap20VedVirk(
            delingstallLevealder = 1.0
        )
    )