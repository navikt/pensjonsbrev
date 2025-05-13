package no.nav.pensjon.brev.api.model.vedlegg

import no.nav.pensjon.brev.api.model.GarantipensjonSatsType
import no.nav.pensjon.brev.api.model.MetaforceSivilstand
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

data class OpplysningerBruktIBeregningenAlderAP2025Dto(
    val alderspensjonVedVirk: AlderspensjonVedVirk,
    val beregnetPensjonPerManedVedVirk: BeregnetPensjonPerManedVedVirk,
    val beregningKap20VedVirk: BeregningKap20VedVirk,
    val vilkarsVedtak: VilkaarsVedtak,
    val vedtak: Vedtak,
    val garantipensjonVedVirk: GarantipensjonVedVirk?,
    val trygdetidsdetaljerKap20VedVirk: TrygdetidsdetaljerKap20VedVirk,
    val epsVedVirk: EpsVedVirk?,
    val erBeregnetSomEnsligPgaInstitusjonsopphold: Boolean,
    val trygdetidNorge: List<Trygdetid>,
    val trygdetidEOS: List<Trygdetid>,
    val trygdetidAvtaleland: List<Trygdetid>,
    val pensjonsopptjeningKap20VedVirk: PensjonsopptjeningKap20VedVirk,
) {

    data class PensjonsopptjeningKap20VedVirk(
        val harOmsorgsopptjeningFOM2010: Boolean,
        val harOmsorgsopptjeningTOM2009: Boolean,
        val harDagpenger: Boolean,
        val harUforetrygd: Boolean,
        val harUforepensjonKonvertertTilUforetrygd: Boolean,
        val harUforepensjon: Boolean,
        val harMerknadType: Boolean,
        val pensjonsopptjeninger: List<Pensjonsopptjening>,
    )

    data class Pensjonsopptjening(
        val aarstall: Int,
        val pensjonsgivendeinntekt: Kroner,
        val gjennomsnittligG: Kroner,
        val merknader: List<Merknad>,
    ) {
        enum class Merknad {
            DAGPENGER,
            OMSORGSOPPTJENING,
            UFORETRYGD,
            UFOREPENSJON,
        }
    }

    data class EpsVedVirk(
        val borSammenMedBruker: Boolean,
        val harInntektOver2G: Boolean,
        val mottarPensjon: Boolean,

        )

    data class Vedtak(
        val sisteOpptjeningsAr: Int
    )

    data class TrygdetidsdetaljerKap20VedVirk(
        val anvendtTT: Int,
    )

    data class GarantipensjonVedVirk(
        val delingstalletVed67Ar: Double,
        val satsType: GarantipensjonSatsType,
        val garantipensjonSatsPerAr: Kroner,
        val nettoUtbetaltPerManed: Kroner,
        val beholdningForForsteUttak: Kroner,
        val garantipensjonInnvilget: Boolean,
    )

    data class VilkaarsVedtak(
        val avslattGarantipensjon: Boolean,
    )

    data class BeregningKap20VedVirk(
        val beholdningForForsteUttak: Kroner,
        val delingstallLevealder: Double,
        val redusertTrygdetid: Boolean,
    )

    data class AlderspensjonVedVirk(
        val uttaksgrad: Int,
        val garantipensjonInnvilget: Boolean,
        val nettoUtbetaltPerManed: Kroner,
    )

    data class BeregnetPensjonPerManedVedVirk(
        val virkDatoFom: LocalDate,
        val brukersSivilstand: MetaforceSivilstand,
    )
}