package no.nav.pensjon.brev.api.model.vedlegg

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.Beregningsmetode
import no.nav.pensjon.brev.api.model.MetaforceSivilstand
import no.nav.pensjon.brev.api.model.PoengTallsType
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

@Suppress("unused")
data class OpplysningerBruktIBeregningenAlderDto(
    val beregnetPensjonPerManedVedVirk: AlderspensjonPerManed,
    val beregnetSomEnsligPgaInstitusjon : Boolean, //aldersEllerSykehjem ,ensligPgaInst ,epsPaInstitusjon ,fengsel eller helseinstitusjon
    val alderspensjonVedVirk: AlderspensjonVedVirk,
    val trygdetidsdetaljerKap19VedVirk: TrygdetidsdetaljerKap19VedVirk,
    val trygdetidsdetaljerKap20VedVirk: TrygdetidsdetaljerKap20VedVirk?,
    val beregningKap19VedVirk: BeregningKap19VedVirk,
    val beregningKap20VedVirk: BeregningKap20VedVirk?,
    val tilleggspensjonVedVirk: TilleggspensjonVedVirk?,
    val yrkesskadeDetaljerVedVirk: YrkesskadeDetaljerVedVirk?,
    val inngangOgEksportVurdering: InngangOgEksportVurdering?,
    val epsVedVirk: EPSvedVirk?,
    val trygdetidNorge: List<Trygdetid>,
    val trygdetidEOS: List<Trygdetid>,
    val trygdetidAvtaleland: List<Trygdetid>,
    val bruker: Bruker,
    val krav: Krav,
    val poengrekkeVedVirk: PoengrekkeVedVirk,
    val skalSkjuleTrygdetidstabellerPgaAldersovergang: Boolean,
) : BrevbakerBrevdata {

    data class PoengrekkeVedVirk(
        val inneholderOmsorgspoeng: Boolean,
        val pensjonspoeng: List<Pensjonspoeng>,
    ) {
        data class Pensjonspoeng(
            val pensjonsgivendeinntekt: Kroner,
            val grunnbelopVeiet: Kroner,
            val arstall: Int,
            val pensjonspoeng: Double,
            val poengtallstype: PoengTallsType?,
            val bruktIBeregningen: Boolean,
        )
    }
    data class Trygdetid(
        val fom: LocalDate?,
        val tom: LocalDate?,
        val land: String?,
    )
    data class InngangOgEksportVurdering(
        val eksportBeregnetUtenGarantipensjon: Boolean,
    )
    data class Krav(
        val erForstegangsbehandling: Boolean,
    )
    data class YrkesskadeDetaljerVedVirk(
        val yrkesskadeUforegrad: Int,
        val sluttpoengtall: Double,
        val poengAr: Int,
        val poengArf92: Int,
        val poengAre91: Int,
    )

    data class TilleggspensjonVedVirk(
        val pgaUngUfore: Boolean,
    )
    data class BeregningKap19VedVirk(
        val redusertTrygdetid: Boolean,
        val sluttpoengtall: Double?,
        val poengAar: Int,
        val poengArf92: Int?,
        val poengAre91: Int?,
        val poengarNevner: Int?,
        val poengArTeller: Int?,
        val skjermingsgrad: Int,
        val forholdstall67Soeker: Double,
        val uforegradVed67: Int,
        val forholdstallLevealder: Double,
    )
    data class BeregningKap20VedVirk(
        val redusertTrygdetid: Boolean,
        val beholdningForForsteUttak: Kroner?,
        val delingstallLevealder: Double,
        val nyOpptjening: Kroner?,
    )
    data class TrygdetidsdetaljerKap19VedVirk(
        val beregningsmetode: Beregningsmetode,
        val anvendtTT: Int,
        val tellerTTEOS : Int?,
        val nevnerTTEOS : Int?,
        val tellerProRata : Int?,
        val nevnerProRata : Int?,

    )
    data class TrygdetidsdetaljerKap20VedVirk(
        val anvendtTT: Int,
        val tellerTTEOS : Int?,
        val nevnerTTEOS : Int?,
        val tellerProRata : Int?,
        val nevnerProRata : Int?,

        val beregningsmetode: Beregningsmetode?,

    )
    data class Bruker(
        val foedselsdato: LocalDate,
    )

    data class AlderspensjonVedVirk(
        val regelverkType: AlderspensjonRegelverkType,
        val andelKap19: Int?,
        val andelKap20: Int?,
        val uttaksgrad: Int,
        val skjermingstilleggInnvilget : Boolean,
        val erEksportberegnet : Boolean,
    )

    data class EPSvedVirk(
        val borSammenMedBruker: Boolean,
        val mottarPensjon: Boolean,
        val harInntektOver2G: Boolean,
    )

    data class InstitusjonsoppholdVedVirk(
        val aldersEllerSykehjem: Boolean,
        val ensligPgaInst: Boolean,
        val epsPaInstitusjon: Boolean,
        val fengsel: Boolean,
        val helseinstitusjon: Boolean,
    )

    data class AlderspensjonPerManed(
        val virkDatoFom: LocalDate,
        val brukersSivilstand: MetaforceSivilstand,
        val flyktningstatusErBrukt: Boolean,
        val tilleggspensjon: Kroner?,
    )

}
