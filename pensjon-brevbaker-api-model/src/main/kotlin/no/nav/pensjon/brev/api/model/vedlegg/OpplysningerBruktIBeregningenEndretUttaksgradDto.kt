package no.nav.pensjon.brev.api.model.vedlegg

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.Beregningsmetode
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.Percent
import no.nav.pensjon.brevbaker.api.model.Year
import java.time.LocalDate

data class OpplysningerBruktIBeregningenEndretUttaksgradDto(
    val alderspensjonVedVirk: AlderspensjonVedVirk,
    val oppfrisketOpptjeningVedVirk: OppfrisketOpptjeningVedVirk,
    val bruker: Bruker,
    val krav: Krav,
    val trygdetidsdetaljerKap19VedVirk: TrygdetidsdetaljerKap19VedVirk,
    val beregningKap19VedVirk: BeregningKap19VedVirk,
    val endretUttaksgradVedVirk: EndretUttaksgradVedVirk,
    val trygdetidsdetaljerKap20VedVirk: TrygdetidsdetaljerKap20VedVirk,
    val alderspensjon: Alderspensjon,
    val beregningKap20VedVirk: BeregningKap20VedVirk
) {
    data class AlderspensjonVedVirk(
        val uttaksgrad: Percent,
        val regelverkType: AlderspensjonRegelverkType,
        val andelKap19: Int,
        val andelKap20: Int
    )

    data class OppfrisketOpptjeningVedVirk(
        val sisteGyldigeOpptjeningsAr: Year?,
        val PGISisteGyldigeOpptjeningsAr: Kroner?,
        val poengtallSisteGyldigeOpptjeningsAr: Double?, // TODO: BigDecimal?
        val opptjeningTilfortKap20: Kroner,
    )

    data class Bruker(
        val fodselsdato: LocalDate
    )

    data class Krav(
        val virkDatoFom: LocalDate,
    )

    data class TrygdetidsdetaljerKap19VedVirk(
        val anvendtTT: Int,
        val beregningsmetode: Beregningsmetode
    )

    data class BeregningKap19VedVirk(
        val sluttpoengtall: Int,
        val poengAr: Int,
        val poengArf92: Int,
        val poengAre91: Int,
        val forholdstallLevealder: Double
    )

    data class EndretUttaksgradVedVirk(
        val restGrunnpensjon: Kroner?,
        val restTilleggspensjon: Kroner?,
        val pensjonsbeholdning: Kroner,
        val garantipensjonsBeholdning: Kroner,
    )

    data class TrygdetidsdetaljerKap20VedVirk(
        val anvendtTT: Int
    )

    data class Alderspensjon(
        val regelverkType: AlderspensjonRegelverkType
    )

    data class BeregningKap20VedVirk(
        val delingstallLevealder: Double
    )
}