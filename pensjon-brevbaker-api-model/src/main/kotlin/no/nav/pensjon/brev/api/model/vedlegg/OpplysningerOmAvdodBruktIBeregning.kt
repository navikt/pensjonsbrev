package no.nav.pensjon.brev.api.model.vedlegg

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.Beregningsmetode
import no.nav.pensjon.brevbaker.api.model.Broek
import java.time.LocalDate

@Suppress("unused")
data class OpplysningerOmAvdoedBruktIBeregningDto(
    val bruker: Bruker,
    val beregnetPensjonPerManedVedVirk: BeregnetPensjonPerManedVedVirk,
    val avdoedTrygdetidsdetaljerKap19VedVirk: AvdoedTrygdetidsdetaljerKap19VedVirk?,
    val avdoed: Avdoed,
    val alderspensjonVedVirk: AlderspensjonVedVirk,
    val avdoedTrygdetidsdetaljerVedVirkNokkelInfo: AvdoedTrygdetidsdetaljerVedVirkNokkelInfo?,
    val tilleggspensjonVedVirk: TilleggspensjonVedVirk?,
    val avdoedBeregningKap19VedVirk: AvdoedBeregningKap19VedVirk?,
    val avdoedYrkesskadedetaljerVedVirk: AvdoedYrkesskadedetaljerVedVirk?,
    val avdodBeregningKap3: AvdodBeregningKap3?,
    val avdoedTrygdetidNorge: List<Trygdetid>,
    val avdoedTrygdetidEOS: List<Trygdetid>,
    val avdoedTrygdetidAvtaleland: List<Trygdetid>,
    val avdoedPoengrekkeVedVirk: AvdoedPoengrekkeVedVirk
) {
    data class AvdoedPoengrekkeVedVirk(
        val inneholderFramtidigPoeng: Boolean,
        val inneholderOmsorgspoeng: Boolean,
        val pensjonspoeng: List<Pensjonspoeng>,
    )
    data class AvdodBeregningKap3(
        val sluttpoengtall: Double?,
        val sluttpoengtallMedOverkomp: Double?,
        val sluttpoengtallUtenOverkomp: Double?,
        val poengAr: Int?,
        val poengAre91: Int?,
        val poengArf92: Int?,
        val poengArBroek: Broek?,
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
        val poengArBroek: Broek?,
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
        val framtidigTTEOS: Int?,
        val framtidigTTBilateral: Int?,
        val anvendtTT: Int?,
        val faktiskTTNordiskKonv: Int?,
        val framtidigTTNorsk: Int?,
        val trygdetidEOSBroek: Broek?,
        val proRataBroek: Broek?,
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
        val trygdetidEOSBroek: Broek?,
        val framtidigTTEOS: Int?,
        val framtidigTTBilateral: Int?,
        val proRataBroek: Broek?,
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