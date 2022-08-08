package no.nav.pensjon.brev.maler.fraser.omregning.ufoeretrygd

import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.api.model.maler.InnvilgetBarnetilleggSelectors.antallBarn
import no.nav.pensjon.brev.api.model.maler.InnvilgetBarnetilleggSelectors.inntektstak
import no.nav.pensjon.brev.api.model.maler.InnvilgetBarnetilleggSelectors.utbetalt_safe
import no.nav.pensjon.brev.api.model.maler.UngUfoerAutoDto
import no.nav.pensjon.brev.maler.fraser.Constants
import no.nav.pensjon.brev.maler.fraser.common.*
import no.nav.pensjon.brev.maler.fraser.omregning.ufoeretrygd.BarnetilleggIkkeUtbetaltDtoSelectors.fellesbarn
import no.nav.pensjon.brev.maler.fraser.omregning.ufoeretrygd.BarnetilleggIkkeUtbetaltDtoSelectors.saerkullsbarn
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers

object Ufoeretrygd {
    /**
     * TBU3007
     */
    val ungUfoer20aar_001 = OutlinePhrase<LangBokmalNynorsk, KravVirkningFraOgMed> {
        val formatertDato = it.format()
        paragraph {
            textExpr(
                Bokmal to "Vi har økt uføretrygden din fra ".expr() + formatertDato + " fordi du fyller 20 år. Du vil nå få utbetalt uføretrygd med rettighet som ung ufør.",
                Nynorsk to "Vi har auka uføretrygda di frå ".expr() + formatertDato + " fordi du fyller 20 år. Du får no utbetalt uføretrygd med rett som ung ufør.",
            )
        }
    }

    /**
     * TBU1120, TBU1121, TBU1122, TBU1254, TBU1253, TBU1123
     */
    data class BeloepPerMaaned(
        val perMaaned: Kroner,
        val ektefelle: Boolean,
        val gjenlevende: Boolean,
        val fellesbarn: Boolean,
        val saerkullsbarn: Boolean,
    )

    val beloep = OutlinePhrase<LangBokmalNynorsk, BeloepPerMaaned> { beloepPerMaaned ->
        val kroner = beloepPerMaaned.select(BeloepPerMaaned::perMaaned).format()

        paragraph {
            showIf(beloepPerMaaned.map { !it.fellesbarn && !it.saerkullsbarn && !it.ektefelle && !it.gjenlevende }) {
                textExpr(
                    Bokmal to "Du får ".expr() + kroner + " kroner i uføretrygd per måned før skatt.",
                    Nynorsk to "Du får ".expr() + kroner + " kroner i uføretrygd per månad før skatt.",
                )
            }.orShowIf(beloepPerMaaned.map { (it.fellesbarn || it.saerkullsbarn) && !it.gjenlevende && !it.ektefelle }) {
                textExpr(
                    Bokmal to "Du får ".expr() + kroner + " kroner i uføretrygd og barnetillegg per måned før skatt.",
                    Nynorsk to "Du får ".expr() + kroner + " kroner i uføretrygd og barnetillegg per månad før skatt.",
                )
            }.orShowIf(beloepPerMaaned.map { !it.fellesbarn && !it.saerkullsbarn && !it.ektefelle && it.gjenlevende }) {
                textExpr(
                    Bokmal to "Du får ".expr() + kroner + " kroner i uføretrygd og gjenlevendetillegg per måned før skatt.",
                    Nynorsk to "Du får ".expr() + kroner + " kroner i uføretrygd og attlevandetillegg per månad før skatt.",
                )
            }.orShowIf(beloepPerMaaned.map { !it.fellesbarn && !it.saerkullsbarn && it.ektefelle && !it.gjenlevende }) {
                textExpr(
                    Bokmal to "Du får ".expr() + kroner + " kroner i uføretrygd og ektefelletillegg per måned før skatt.",
                    Nynorsk to "Du får ".expr() + kroner + " kroner i uføretrygd og ektefelletillegg per månad før skatt.",
                )

            }.orShowIf(beloepPerMaaned.map { (it.fellesbarn || it.saerkullsbarn) && it.ektefelle && !it.gjenlevende }) {
                textExpr(
                    Bokmal to "Du får ".expr() + kroner + " kroner i uføretrygd, barne- og ektefelletillegg per måned før skatt.",
                    Nynorsk to "Du får ".expr() + kroner + " kroner i uføretrygd, barne- og ektefelletillegg per månad før skatt.",
                )

            }.orShowIf(beloepPerMaaned.map { (it.fellesbarn || it.saerkullsbarn) && !it.ektefelle && it.gjenlevende }) {
                textExpr(
                    Bokmal to "Du får ".expr() + kroner + " kroner i uføretrygd, barne- og gjenlevendetillegg per måned før skatt.",
                    Nynorsk to "Du får ".expr() + kroner + " kroner i uføretrygd, barne- og attlevandetillegg per månad før skatt.",
                )
            }
        }
    }

    /**
     * TBU1286.1, TBU1286.2
     */
    data class BarnetilleggIkkeUtbetaltDto(
        val fellesbarn: UngUfoerAutoDto.InnvilgetBarnetillegg?,
        val saerkullsbarn: UngUfoerAutoDto.InnvilgetBarnetillegg?,
    )

    @TemplateModelHelpers
    val barnetilleggIkkeUtbetalt = OutlinePhrase<LangBokmalNynorsk, BarnetilleggIkkeUtbetaltDto> {
        paragraph {

            val saerkullInnvilget = it.saerkullsbarn.notNull()
            val saerkullUtbetalt = it.saerkullsbarn.utbetalt_safe.ifNull(false)
            val fellesInnvilget = it.fellesbarn.notNull()
            val fellesUtbetalt = it.fellesbarn.utbetalt_safe.ifNull(false)


            ifNotNull(it.saerkullsbarn) { saerkullsbarn ->
                val barnFlertall = saerkullsbarn.antallBarn.greaterThan(1)
                val inntektstak = saerkullsbarn.inntektstak.format()

                showIf(saerkullInnvilget and not(saerkullUtbetalt) and fellesUtbetalt and fellesInnvilget) {
                    textExpr(
                        Bokmal to "Barnetillegget for ".expr() + ifElse(barnFlertall, "barna", "barnet") + " som ikke bor sammen med begge foreldrene, blir ikke utbetalt fordi du alene har en samlet inntekt som er høyere enn " +
                                inntektstak + " kroner. Inntekten din er over grensen for å få utbetalt barnetillegg.",

                        Nynorsk to "Barnetillegget for ".expr() + ifElse(barnFlertall, "barna", "barnet") + "som ikkje bur saman med begge foreldra sine, blir ikkje utbetalt fordi du åleine har ei samla inntekt som er høgare enn " +
                                inntektstak + " kroner. Inntekta di er over grensa for å få utbetalt barnetillegg.",
                    )

                }.orShowIf(saerkullInnvilget and not(saerkullUtbetalt) and not(fellesInnvilget)) {
                    textExpr(
                        Bokmal to "Barnetillegget blir ikke utbetalt fordi du har en samlet inntekt som er høyere enn ".expr() +
                                inntektstak + " kroner. Inntekten din er over grensen for å få utbetalt barnetillegg.",

                        Nynorsk to "Barnetillegget blir ikkje utbetalt fordi du åleine har ei samla inntekt som er høgare enn ".expr() +
                                inntektstak + " kroner. Inntekten din er over grensen for å få utbetalt barnetillegg.",
                    )
                }
            }

            ifNotNull(it.fellesbarn) { fellesbarn ->
                val barnFlertall = fellesbarn.antallBarn.greaterThan(1)
                val inntektstak = fellesbarn.inntektstak.format()

                showIf(fellesInnvilget and not(fellesUtbetalt) and saerkullUtbetalt and saerkullInnvilget) {
                    textExpr(
                        Bokmal to "Barnetillegget for ".expr() + ifElse(barnFlertall, "barna", "barnet") + " som bor med begge sine foreldre, blir ikke utbetalt fordi dere har en samlet inntekt som er høyere enn " +
                                inntektstak + " kroner. De samlede inntektene er over grensen for å få utbetalt barnetillegg.",

                        Nynorsk to "Barnetillegget for ".expr() + ifElse(barnFlertall, "barna", "barnet") + " som bur saman med begge foreldra sine, blir ikkje utbetalt fordi dei har ei samla inntekt som er høgare enn " +
                                inntektstak + " kroner. Dei samla inntektene er over grensa for å få utbetalt barnetillegg.",
                    )

                }.orShowIf(fellesInnvilget and not(fellesUtbetalt) and not(saerkullInnvilget)) {
                    textExpr(
                        Bokmal to "Barnetillegget blir ikke utbetalt fordi dere har en samlet inntekt som er høyere enn ".expr() +
                                inntektstak + " kroner. De samlede inntektene er over grensen for å få utbetalt barnetillegg.",

                        Nynorsk to "Barnetillegget blir ikkje utbetalt fordi dei har ei samla inntekt som er høgare enn ".expr() +
                                inntektstak + " kroner. Dei samla inntektene er over grensa for å få utbetalt barnetillegg.",
                    )
                }
            }
        }
    }

    /**
     * TBU3008, TBU3009, TBU3010
     */
    val ungUfoerHoeyereVed20aar = OutlinePhrase<LangBokmalNynorsk, GrunnbeloepSats> {
        paragraph {
            text(
                Bokmal to "Du er tidligere innvilget rettighet som ung ufør i uføretrygden din. Denne rettigheten gir deg høyere utbetaling fra og med den måneden du fyller 20 år.",
                Nynorsk to "Du har tidlegare fått innvilga rett som ung ufør i uføretrygda di. Denne retten gir deg høgare utbetaling frå og med den månaden du fyller 20 år.",
            )
        }

        paragraph {
            val minsteytelseVedVirkSats = it.format()
            textExpr(
                Bokmal to "Sivilstanden påvirker størrelsen på den årlige uføretrygden og du får derfor en årlig ytelse som utgjør ".expr()
                        + minsteytelseVedVirkSats + " ganger grunnbeløpet.",

                Nynorsk to "Sivilstanden påverkar storleiken på den årlege uføretrygda di, og du får derfor ei årleg yting som utgjer ".expr()
                        + minsteytelseVedVirkSats + " gonger grunnbeløpet.",
            )
        }
    }

    /**
     * TBU3011
     */
    val hjemmelSivilstand = OutlinePhrase<LangBokmalNynorsk, Unit> {
        paragraph {
            text(
                Bokmal to "Vedtaket er gjort etter folketrygdloven § 12-13 og § 22-12.",
                Nynorsk to "Vedtaket har vi gjort etter folketrygdlova § 12-13 og § 22-12.",
            )
        }
    }

    /**
     * TBU1174
     */
    val virkningFomOverskrift = OutlinePhrase<LangBokmalNynorsk, Unit> {
        title1 {
            text(
                Bokmal to "Dette er virkningstidspunktet ditt",
                Nynorsk to "Dette er verknadstidspunktet ditt",
            )
        }
    }

    /**
     * TBU2529x
     */
    val virkningFraOgMed = OutlinePhrase<LangBokmalNynorsk, KravVirkningFraOgMed> {
        val dato = it.format()
        paragraph {
            textExpr(
                Bokmal to "Uføretrygden din er endret fra ".expr() + dato + ". Dette kaller vi virkningstidspunktet. Du vil derfor få en ny utbetaling fra og med måneden vilkåret er oppfylt.",
                Nynorsk to "Uføretrygda di er endra frå ".expr() + dato + ". Dette kallar vi verknadstidspunktet. Du vil derfor få ny utbetaling frå og med månaden vilkåret er oppfylt.",
            )
        }
    }

    /**
     * TBU1227, sjekkUtbetalingeneOverskrift_001, sjekkUtbetalingeneUT_001
     */
    val sjekkUtbetalingene = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
        title1 {
            text(
                Bokmal to "Sjekk utbetalingene dine",
                Nynorsk to "Sjekk utbetalingane dine",
                English to "Information about your payments",
            )
        }

        paragraph {
            text(
                Bokmal to "Du får uføretrygd utbetalt den 20. hver måned, eller senest siste virkedag før denne datoen. " +
                        "Du kan se alle utbetalingene du har mottatt på ${Constants.DITT_NAV}. Her kan du også endre kontonummeret ditt.",

                Nynorsk to "Du får uføretrygd utbetalt den 20. kvar månad, eller seinast siste yrkedag før denne datoen. " +
                        "Du kan sjå alle utbetalingar du har fått på ${Constants.DITT_NAV}. Her kan du også endre kontonummeret ditt.",

                English to "Your disability benefit will be paid on the 20th of each month or no later than the last business day before this date. " +
                        "To see all the payments you have received, go to: ${Constants.DITT_NAV}. You may also change your account number here.",
            )
        }
    }
}
