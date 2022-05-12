package no.nav.pensjon.brev.api.model.vedlegg

import no.nav.pensjon.brev.api.model.Beregningsmetode
import no.nav.pensjon.brev.api.model.Sivilstand
import java.time.LocalDate

data class OpplysningerBruktIBeregningUTDto(
    val barnetilleggGjeldende: BarnetilleggGjeldende?,
    val beregnetUTPerManedGjeldende: BeregnetUTPerManedGjeldende,
    val inntektEtterUforeGjeldende_belopIEU: Int,
    val inntektForUforeGjeldende: InntektForUforeGjeldende,
    val inntektsAvkortingGjeldende: InntektsAvkortingGjeldende,
    val minsteytelseGjeldende_sats: Double,
    val trygdetidsdetaljerGjeldende: TrygdetidsdetaljerGjeldende,
    val uforetrygdGjeldende: UforetrygdGjeldende,
    val ungUforGjeldende_erUnder20Ar: Boolean,
    val yrkesskadeGjeldende: YrkesskadeGjeldende?,
) {
    constructor() : this(
        barnetilleggGjeldende = BarnetilleggGjeldende(),
        beregnetUTPerManedGjeldende = BeregnetUTPerManedGjeldende(),
        inntektEtterUforeGjeldende_belopIEU = 0,
        inntektForUforeGjeldende = InntektForUforeGjeldende(),
        inntektsAvkortingGjeldende = InntektsAvkortingGjeldende(),
        minsteytelseGjeldende_sats = 0.0,
        trygdetidsdetaljerGjeldende = TrygdetidsdetaljerGjeldende(),
        uforetrygdGjeldende = UforetrygdGjeldende(),
        ungUforGjeldende_erUnder20Ar = false,
        yrkesskadeGjeldende = YrkesskadeGjeldende(),
    )

    data class YrkesskadeGjeldende(
        val beregningsgrunnlagBelopAr: Int,
        val inntektVedSkadetidspunkt: Int,
        val skadetidspunkt: LocalDate,
        val yrkesskadegrad: Int,
    ) {
        constructor() : this(
            beregningsgrunnlagBelopAr = 0,
            inntektVedSkadetidspunkt = 0,
            skadetidspunkt = LocalDate.of(2020, 1, 1),
            yrkesskadegrad = 0,
        )
    }

    data class BarnetilleggGjeldende(
        val grunnlag: Grunnlag,
        val saerkullsbarn: Saerkullsbarn?
    ) {
        constructor() : this(
            saerkullsbarn = Saerkullsbarn(),
            grunnlag = Grunnlag()
        )

        data class Grunnlag(
            val erIkkeUtbetaltpgaTak: Boolean,
            val erRedusertMotTak: Boolean,
            val gradertOIFU: Int,
            val prosentsatsGradertOIFU: Int,
            val totaltAntallBarn: Int,
        ) {
            constructor() : this(
                erIkkeUtbetaltpgaTak = false,
                erRedusertMotTak = false,
                gradertOIFU = 0,
                prosentsatsGradertOIFU = 0,
                totaltAntallBarn = 0,
            )
        }

        data class Saerkullsbarn(
            val avkortningsbelopAr: Int,
            val belop: Int,
            val belopAr: Int,
            val belopArForAvkort: Int,
            val erRedusertMotinntekt: Boolean,
            val fribelop: Int,
            val fribelopEllerInntektErPeriodisert: Boolean,
            val inntektBruktIAvkortning: Int,
            val inntektOverFribelop: Int,
            val inntektstak: Int,
            val justeringsbelopAr: Int,
        ) {
            constructor() : this(
                avkortningsbelopAr = 0,
                belop = 0,
                belopAr = 0,
                belopArForAvkort = 0,
                erRedusertMotinntekt = false,
                fribelop = 0,
                fribelopEllerInntektErPeriodisert = false,
                inntektBruktIAvkortning = 0,
                inntektOverFribelop = 0,
                inntektstak = 0,
                justeringsbelopAr = 0,
            )
        }
    }

    data class TrygdetidsdetaljerGjeldende(
        val anvendtTT: Int,
        val beregningsmetode: Beregningsmetode,
        val faktiskTTEOS: Int?,
        val faktiskTTNordiskKonv: Int?,
        val faktiskTTNorge: Int?,
        val framtidigTTNorsk: Int?,
        val nevnerTTEOS: Int?,
        val nevnerTTNordiskKonv: Int?,
        val samletTTNordiskKonv: Int?,
        val tellerTTEOS: Int?,
        val tellerTTNordiskKonv: Int?,
        val utenforEOSogNorden: UtenforEOSogNorden?,
    ) {
        constructor() : this(
            anvendtTT = 0,
            beregningsmetode = Beregningsmetode.FOLKETRYGD,
            faktiskTTEOS = 0,
            faktiskTTNordiskKonv = 0,
            faktiskTTNorge = 0,
            framtidigTTNorsk = 0,
            nevnerTTEOS = 0,
            nevnerTTNordiskKonv = 0,
            samletTTNordiskKonv = 0,
            tellerTTEOS = 0,
            tellerTTNordiskKonv = 0,
            utenforEOSogNorden = UtenforEOSogNorden()
        )

        data class UtenforEOSogNorden(
            val faktiskTTBilateral: Int,
            val tellerProRata: Int,
            val nevnerProRata: Int,
        ) {
            constructor() : this(
                faktiskTTBilateral = 0,
                nevnerProRata = 0,
                tellerProRata = 0,
            )
        }
    }

    data class BeregnetUTPerManedGjeldende(
        val brukerErFlyktning: Boolean,
        val brukersSivilstand: Sivilstand,
        val grunnbelop: Int,
        val virkDatoFom: LocalDate,
    ) {
        constructor() : this(
            brukerErFlyktning = false,
            brukersSivilstand = Sivilstand.ENSLIG,
            grunnbelop = 0,
            virkDatoFom = LocalDate.of(2020, 1, 1),
        )
    }

    data class UforetrygdGjeldende(
        val belopsgrense: Int,
        val beregningsgrunnlagBelopAr: Int,
        val erKonvertert: Boolean,
        val kompensasjonsgrad: Double,
        val uforegrad: Int,
        val uforetidspunkt: LocalDate,
    ) {
        constructor() : this(
            belopsgrense = 0,
            beregningsgrunnlagBelopAr = 0,
            erKonvertert = false,
            kompensasjonsgrad = 0.0,
            uforegrad = 0,
            uforetidspunkt = LocalDate.of(2020, 1, 1),
        )
    }

    data class InntektsAvkortingGjeldende(
        val forventetInntektAr: Int,
        val inntektsgrenseAr: Int,
        val inntektstak: Int,
    ) {
        constructor() : this(
            forventetInntektAr = 0,
            inntektsgrenseAr = 0,
            inntektstak = 0,
        )
    }

    data class InntektForUforeGjeldende(
        val erSannsynligEndret: Boolean,
        val ifuInntekt: Int,
    ) {
        constructor() : this(
            erSannsynligEndret = false,
            ifuInntekt = 0,
        )
    }
}
