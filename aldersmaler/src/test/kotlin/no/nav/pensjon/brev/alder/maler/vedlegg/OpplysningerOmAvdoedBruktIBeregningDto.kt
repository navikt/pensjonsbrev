package no.nav.pensjon.brev.alder.maler.vedlegg

import no.nav.brev.brevbaker.vilkaarligDato
import no.nav.pensjon.brev.alder.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.alder.model.Beregningsmetode
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDto
import no.nav.pensjon.brevbaker.api.model.Broek
import no.nav.pensjon.brevbaker.api.model.Foedselsnummer

fun createOpplysningerOmAvdoedBruktIBeregningDto() = OpplysningerOmAvdoedBruktIBeregningDto(
    bruker = OpplysningerOmAvdoedBruktIBeregningDto.Bruker(
        foedselsdato = vilkaarligDato
    ),
    beregnetPensjonPerManedVedVirk = OpplysningerOmAvdoedBruktIBeregningDto.BeregnetPensjonPerManedVedVirk(
        virkDatoFom = vilkaarligDato,
        avdoedFlyktningstatusErBrukt = false
    ),
    avdoedTrygdetidsdetaljerKap19VedVirk = OpplysningerOmAvdoedBruktIBeregningDto.AvdoedTrygdetidsdetaljerKap19VedVirk(
        anvendtTT = 40,
        beregningsMetode = Beregningsmetode.FOLKETRYGD,
        framtidigTTNorsk = 12,
        faktiskTTNordiskKonv = 0,
        trygdetidEOSBroek = Broek(40, 19),
        framtidigTTEOS = 32,
        framtidigTTBilateral = 4,
        proRataBroek = Broek(4, 13)
    ),
    avdoed = OpplysningerOmAvdoedBruktIBeregningDto.Avdoed(
        navn = "Peder Ås",
        avdoedFnr = Foedselsnummer("12345678901")
    ),
    alderspensjonVedVirk = OpplysningerOmAvdoedBruktIBeregningDto.AlderspensjonVedVirk(
        regelverkType = AlderspensjonRegelverkType.AP2025,
        gjenlevenderettAnvendt = true,
        tilleggspensjonInnvilget = true
    ),
    avdoedTrygdetidsdetaljerVedVirkNokkelInfo = OpplysningerOmAvdoedBruktIBeregningDto.AvdoedTrygdetidsdetaljerVedVirkNokkelInfo(
        beregningsMetode = Beregningsmetode.FOLKETRYGD,
        framtidigTTEOS = null,
        framtidigTTBilateral = null,
        anvendtTT = null,
        faktiskTTNordiskKonv = null,
        framtidigTTNorsk = null,
        trygdetidEOSBroek = null,
        proRataBroek = null
    ),
    tilleggspensjonVedVirk = OpplysningerOmAvdoedBruktIBeregningDto.TilleggspensjonVedVirk(
        kombinertMedAvdoed = true,
        pgaUngUforeAvdod = true
    ),
    avdoedBeregningKap19VedVirk = OpplysningerOmAvdoedBruktIBeregningDto.AvdoedBeregningKap19VedVirk(
        faktiskPoengArAvtale = 30,
        faktiskPoengArNorge = 9,
        framtidigPoengAr = 12,
        poengAr = 38,
        poengArBroek = Broek(38, 3),
        poengAre91 = 35,
        poengArf92 = 3,
        sluttpoengtall = 12.3,
        sluttpoengtallMedOverkomp = 12.5,
        sluttpoengtallUtenOverkomp = 12.1
    ),
    avdoedYrkesskadedetaljerVedVirk = OpplysningerOmAvdoedBruktIBeregningDto.AvdoedYrkesskadedetaljerVedVirk(
        sluttpoengtall = 12.3,
        poengAr = 36,
        poengAre91 = 12,
        poengArf92 = 24,
        yrkesskadeUforegrad = 80
    ),
    avdodBeregningKap3 = OpplysningerOmAvdoedBruktIBeregningDto.AvdodBeregningKap3(
        sluttpoengtall = 20.2,
        sluttpoengtallMedOverkomp = 19.1,
        sluttpoengtallUtenOverkomp = 21.3,
        poengAr = 22,
        poengAre91 = 10,
        poengArf92 = 12,
        poengArBroek = Broek(12, 22),
        framtidigPoengAr = 40
    ),
    avdoedTrygdetidNorge = listOf(),
    avdoedTrygdetidEOS = listOf(),
    avdoedTrygdetidAvtaleland = listOf(),
    avdoedPoengrekkeVedVirk = OpplysningerOmAvdoedBruktIBeregningDto.AvdoedPoengrekkeVedVirk(
        inneholderFramtidigPoeng = true,
        inneholderOmsorgspoeng = true,
        pensjonspoeng = listOf()
    )
)