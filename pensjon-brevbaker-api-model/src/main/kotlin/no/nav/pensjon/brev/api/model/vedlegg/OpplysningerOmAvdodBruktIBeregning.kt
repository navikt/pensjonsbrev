package no.nav.pensjon.brev.api.model.vedlegg

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.Beregningsmetode
import java.time.LocalDate

@Suppress("unused")
data class OpplysningerOmAvdoedBruktIBeregningDto(
    val bruker: Bruker,
    val beregnetPensjonPerManedVedVirk: BeregnetPensjonPerManedVedVirk,
    val avdoedTrygdetidsdetaljerKap19VedVirk: AvdoedTrygdetidsdetaljerKap19VedVirk?,
    val avdoed: Avdoed,
    val alderspensjonVedVirk: AlderspensjonVedVirk,
    val avdoedTrygdetidsdetaljerVedVirkNokkelInfo: AvdoedTrygdetidsdetaljerVedVirkNokkelInfo,
    val tilleggspensjonVedVirk: TilleggspensjonVedVirk,
    val avdoedBeregningKap19VedVirk: AvdoedBeregningKap19VedVirk?,
    val avdoedYrkesskadedetaljerVedVirk: AvdoedYrkesskadedetaljerVedVirk?,
    val avdodBeregningKap3: AvdodBeregningKap3?,
) {

    data class AvdodBeregningKap3(
        val sluttpoengtall: Double?,
        val sluttpoengtallMedOverkomp: Double?,
        val sluttpoengtallUtenOverkomp: Double?,
        val poengAr: Int?,
        val poengAre91: Int?,
        val poengArf92: Int?,
        val framtidigPoengAr: Int?,
    )
    data class AvdoedYrkesskadedetaljerVedVirk(
        val sluttpoengtall: Double?,
        val poengAr: Int?,
        val poengAre91: Int?,
        val poengArf92: Int?,
        val yrkesskadeUforegrad: Int?,
    )

    data class AvdoedBeregningKap19VedVirk(
        val faktiskPoengArAvtale: Int?,
        val faktiskPoengArNorge: Int?,
        val framtidigPoengAr: Int?,
        val poengAr: Int?,
        val poengArNevner: Int?,
        val poengArTeller: Int?,
        val poengAre91: Int?,
        val poengArf92: Int?,
        val sluttpoengtall: Double?,
        val sluttpoengtallMedOverkomp: Double?,
        val sluttpoengtallUtenOverkomp: Double?,
    )

    data class TilleggspensjonVedVirk(
        val kombinertMedAvdoed: Boolean,
        val pgaUngUforeAvdod: Boolean,
    )

    data class AvdoedTrygdetidsdetaljerVedVirkNokkelInfo(
        val beregningsMetode: Beregningsmetode,
        val anvendtTT: Int?,
        val faktiskTTNordiskKonv: Int?,
        val framtidigTTNorsk: Int?,
        val tellerTTEOS: Int?,
        val nevnerTTEOS: Int?,
    )

    data class AlderspensjonVedVirk(
        val regelverkType: AlderspensjonRegelverkType,
        val gjenlevenderettAnvendt: Boolean,
        val tilleggspensjonInnvilget: Boolean,
    )

    data class AvdoedTrygdetidsdetaljerKap19VedVirk(
        val anvendtTT: Int?,
        val beregningsMetode: Beregningsmetode,
        val framtidigTTNorsk: Int?,
        val faktiskTTNordiskKonv: Int?,
        val tellerTTEOS: Int?,
        val nevnerTTEOS: Int?,
        val framtidigTTEOS: Int?,
        val framtidigTTBilateral: Int?,
        val tellerProRata: Int?,
        val nevnerProRata: Int?,
    )

    data class Avdoed(
        val navn: String,
        val avdoedFnr: Foedselsnummer,
    )

    data class Bruker(
        val foedselsdato: LocalDate,
    )

    data class BeregnetPensjonPerManedVedVirk(
        val virkDatoFom: LocalDate,
        val avdoedFlyktningstatusErBrukt: Boolean,
    )

    data class Foedselsnummer(override val value: String) : no.nav.pensjon.brevbaker.api.model.Foedselsnummer

}