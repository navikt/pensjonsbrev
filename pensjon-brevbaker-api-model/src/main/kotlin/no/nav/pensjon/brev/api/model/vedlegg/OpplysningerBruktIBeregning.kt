package no.nav.pensjon.brev.api.model.vedlegg

import no.nav.pensjon.brev.api.model.Beregningsmetode
import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.api.model.Sivilstand
import java.time.LocalDate

data class OpplysningerBruktIBeregningUTDto(
    val barnetilleggGjeldende: BarnetilleggGjeldende?,
    val beregnetUTPerManedGjeldende: BeregnetUTPerManedGjeldende,
    val inntektEtterUfoereGjeldende_beloepIEU: Kroner,
    val inntektFoerUfoereGjeldende: InntektFoerUfoereGjeldende,
    val inntektsAvkortingGjeldende: InntektsAvkortingGjeldende,
    val minsteytelseGjeldende_sats: Double,
    val trygdetidsdetaljerGjeldende: TrygdetidsdetaljerGjeldende,
    val ufoeretrygdGjeldende: UfoeretrygdGjeldende,
    val ungUfoerGjeldende_erUnder20Aar: Boolean?,
    val yrkesskadeGjeldende: YrkesskadeGjeldende?,
) {
    constructor() : this(
        barnetilleggGjeldende = BarnetilleggGjeldende(),
        beregnetUTPerManedGjeldende = BeregnetUTPerManedGjeldende(),
        inntektEtterUfoereGjeldende_beloepIEU = Kroner(0),
        inntektFoerUfoereGjeldende = InntektFoerUfoereGjeldende(),
        inntektsAvkortingGjeldende = InntektsAvkortingGjeldende(),
        minsteytelseGjeldende_sats = 0.0,
        trygdetidsdetaljerGjeldende = TrygdetidsdetaljerGjeldende(),
        ufoeretrygdGjeldende = UfoeretrygdGjeldende(),
        ungUfoerGjeldende_erUnder20Aar = false,
        yrkesskadeGjeldende = YrkesskadeGjeldende(),
    )

    data class YrkesskadeGjeldende(
        val beregningsgrunnlagBeloepAar: Kroner,
        val inntektVedSkadetidspunkt: Kroner,
        val skadetidspunkt: LocalDate,
        val yrkesskadegrad: Int,
    ) {
        constructor() : this(
            beregningsgrunnlagBeloepAar = Kroner(0),
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
            val avkortningsbeloepAar: Kroner,
            val beloep: Kroner,
            val beloepAar: Kroner,
            val beloepAarFoerAvkort: Kroner,
            val erRedusertMotinntekt: Boolean,
            val fribeloep: Kroner,
            val fribeloepEllerInntektErPeriodisert: Boolean,
            val inntektBruktIAvkortning: Kroner,
            val inntektOverFribeloep: Kroner,
            val inntektstak: Kroner,
            val justeringsbeloepAar: Kroner,
        ) {
            constructor() : this(
                avkortningsbeloepAar = Kroner(0),
                beloep = Kroner(0),
                beloepAar = Kroner(0),
                beloepAarFoerAvkort = Kroner(0),
                erRedusertMotinntekt = false,
                fribeloep = Kroner(0),
                fribeloepEllerInntektErPeriodisert = false,
                inntektBruktIAvkortning = Kroner(0),
                inntektOverFribeloep = Kroner(0),
                inntektstak = Kroner(0),
                justeringsbeloepAar = Kroner(0),
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
        val grunnbeloep: Kroner,
        val virkDatoFom: LocalDate,
    ) {
        constructor() : this(
            brukerErFlyktning = false,
            brukersSivilstand = Sivilstand.ENSLIG,
            grunnbeloep = Kroner(0),
            virkDatoFom = LocalDate.of(2020, 1, 1),
        )
    }

    data class UfoeretrygdGjeldende(
        val beloepsgrense: Kroner,
        val beregningsgrunnlagBeloepAar: Kroner,
        val erKonvertert: Boolean,
        val kompensasjonsgrad: Double,
        val ufoeregrad: Int,
        val ufoeretidspunkt: LocalDate,
    ) {
        constructor() : this(
            beloepsgrense = Kroner(0),
            beregningsgrunnlagBeloepAar = Kroner(0),
            erKonvertert = false,
            kompensasjonsgrad = 0.0,
            ufoeregrad = 0,
            ufoeretidspunkt = LocalDate.of(2020, 1, 1),
        )
    }

    data class InntektsAvkortingGjeldende(
        val forventetInntektAar: Kroner,
        val inntektsgrenseAar: Kroner,
        val inntektstak: Kroner,
    ) {
        constructor() : this(
            forventetInntektAar = Kroner(0),
            inntektsgrenseAar = Kroner(0),
            inntektstak = Kroner(0),
        )
    }

    data class InntektFoerUfoereGjeldende(
        val erSannsynligEndret: Boolean,
        val ifuInntekt: Kroner,
    ) {
        constructor() : this(
            erSannsynligEndret = false,
            ifuInntekt = Kroner(0),
        )
    }
}
