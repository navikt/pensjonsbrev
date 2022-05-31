package no.nav.pensjon.brev.api.model.vedlegg

import no.nav.pensjon.brev.api.model.Beregningsmetode
import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.api.model.Sivilstand
import java.time.LocalDate

data class OpplysningerBruktIBeregningUTDto(
    val barnetilleggGjeldende: BarnetilleggGjeldende?,
    val beregnetUTPerManedGjeldende: BeregnetUTPerManedGjeldende,
    val inntektEtterUforeGjeldende_belopIEU: Kroner,
    val inntektForUforeGjeldende: InntektForUforeGjeldende,
    val inntektsAvkortingGjeldende: InntektsAvkortingGjeldende,
    val minsteytelseGjeldende_sats: Double,
    val trygdetidsdetaljerGjeldende: TrygdetidsdetaljerGjeldende,
    val uforetrygdGjeldende: UforetrygdGjeldende,
    val ungUforGjeldende_erUnder20Ar: Boolean?,
    val yrkesskadeGjeldende: YrkesskadeGjeldende?,
) {
    constructor() : this(
        barnetilleggGjeldende = BarnetilleggGjeldende(),
        beregnetUTPerManedGjeldende = BeregnetUTPerManedGjeldende(),
        inntektEtterUforeGjeldende_belopIEU = Kroner(0),
        inntektForUforeGjeldende = InntektForUforeGjeldende(),
        inntektsAvkortingGjeldende = InntektsAvkortingGjeldende(),
        minsteytelseGjeldende_sats = 0.0,
        trygdetidsdetaljerGjeldende = TrygdetidsdetaljerGjeldende(),
        uforetrygdGjeldende = UforetrygdGjeldende(),
        ungUforGjeldende_erUnder20Ar = false,
        yrkesskadeGjeldende = YrkesskadeGjeldende(),
    )

    data class YrkesskadeGjeldende(
        val beregningsgrunnlagBelopAr: Kroner,
        val inntektVedSkadetidspunkt: Kroner,
        val skadetidspunkt: LocalDate,
        val yrkesskadegrad: Int,
    ) {
        constructor() : this(
            beregningsgrunnlagBelopAr = Kroner(0),
            inntektVedSkadetidspunkt = Kroner(0),
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
            val gradertOIFU: Kroner,
            val prosentsatsGradertOIFU: Int,
            val totaltAntallBarn: Int,
        ) {
            constructor() : this(
                erIkkeUtbetaltpgaTak = false,
                erRedusertMotTak = false,
                gradertOIFU = Kroner(0),
                prosentsatsGradertOIFU = 0,
                totaltAntallBarn = 0,
            )
        }

        data class Saerkullsbarn(
            val avkortningsbelopAr: Kroner,
            val belop: Kroner,
            val belopAr: Kroner,
            val belopArForAvkort: Kroner,
            val erRedusertMotinntekt: Boolean,
            val fribelop: Kroner,
            val fribelopEllerInntektErPeriodisert: Boolean,
            val inntektBruktIAvkortning: Kroner,
            val inntektOverFribelop: Kroner,
            val inntektstak: Kroner,
            val justeringsbelopAr: Kroner,
        ) {
            constructor() : this(
                avkortningsbelopAr = Kroner(0),
                belop = Kroner(0),
                belopAr = Kroner(0),
                belopArForAvkort = Kroner(0),
                erRedusertMotinntekt = false,
                fribelop = Kroner(0),
                fribelopEllerInntektErPeriodisert = false,
                inntektBruktIAvkortning = Kroner(0),
                inntektOverFribelop = Kroner(0),
                inntektstak = Kroner(0),
                justeringsbelopAr = Kroner(0),
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
        val grunnbelop: Kroner,
        val virkDatoFom: LocalDate,
    ) {
        constructor() : this(
            brukerErFlyktning = false,
            brukersSivilstand = Sivilstand.ENSLIG,
            grunnbelop = Kroner(0),
            virkDatoFom = LocalDate.of(2020, 1, 1),
        )
    }

    data class UforetrygdGjeldende(
        val belopsgrense: Kroner,
        val beregningsgrunnlagBelopAr: Kroner,
        val erKonvertert: Boolean,
        val kompensasjonsgrad: Double,
        val uforegrad: Int,
        val uforetidspunkt: LocalDate,
    ) {
        constructor() : this(
            belopsgrense = Kroner(0),
            beregningsgrunnlagBelopAr = Kroner(0),
            erKonvertert = false,
            kompensasjonsgrad = 0.0,
            uforegrad = 0,
            uforetidspunkt = LocalDate.of(2020, 1, 1),
        )
    }

    data class InntektsAvkortingGjeldende(
        val forventetInntektAr: Kroner,
        val inntektsgrenseAr: Kroner,
        val inntektstak: Kroner,
    ) {
        constructor() : this(
            forventetInntektAr = Kroner(0),
            inntektsgrenseAr = Kroner(0),
            inntektstak = Kroner(0),
        )
    }

    data class InntektForUforeGjeldende(
        val erSannsynligEndret: Boolean,
        val ifuInntekt: Kroner,
    ) {
        constructor() : this(
            erSannsynligEndret = false,
            ifuInntekt = Kroner(0),
        )
    }
}
