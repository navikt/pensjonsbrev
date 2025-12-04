package no.nav.pensjon.brev.api.model.vedlegg

import no.nav.pensjon.brev.api.model.MetaforceSivilstand
import no.nav.pensjon.brev.api.model.maler.VedleggData
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

data class OpplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjeningDto(
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
) : VedleggData {

    data class PensjonsopptjeningKap20VedVirk(
        val harOmsorgsopptjeningFOM2010: Boolean,
        val harOmsorgsopptjeningTOM2009: Boolean,
        val harDagpenger: Boolean,
        val harUforetrygd: Boolean,
        val harUforepensjonKonvertertTilUforetrygd: Boolean,
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
        val nettoUtbetaltPerManed: Kroner,
    )

    data class VilkaarsVedtak(
        val avslattGarantipensjon: Boolean,
    )

    data class BeregningKap20VedVirk(
        val beholdningForForsteUttak: Kroner,
        val delingstallLevealder: Double,
        val redusertTrygdetid: Boolean,
        val nyOpptjening: Kroner?,
    )

    data class AlderspensjonVedVirk(
        val uttaksgrad: Int,
        val garantipensjonInnvilget: Boolean,
    )

    data class BeregnetPensjonPerManedVedVirk(
        val virkDatoFom: LocalDate,
        val brukersSivilstand: MetaforceSivilstand,
    )
}