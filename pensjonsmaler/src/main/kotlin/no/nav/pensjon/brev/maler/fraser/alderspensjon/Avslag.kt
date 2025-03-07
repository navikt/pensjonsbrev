package no.nav.pensjon.brev.maler.fraser.alderspensjon

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.expression.plus
import java.time.LocalDate


data class AvslagAPTidligUttakInnledning(
    val kravDato: Expression<LocalDate>,
    val uttaksgrad: Expression<Int>
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            textExpr(
                Bokmal to "Du har for lav pensjonsopptjening til at du kan ta ut ".expr() +
                        uttaksgrad.format() + " prosent pensjon fra ".expr() +
                        kravDato.format() + ". Derfor har vi avslått søknaden din.",
                Nynorsk to "Du har for låg pensjonsopptening til at du kan ta ut ".expr() +
                        uttaksgrad.format() + " prosent pensjon frå ".expr() +
                        kravDato.format() + ". Derfor har vi avslått søknaden din.",
                English to "Your accumulated pension capital is not sufficient for you to draw a retirement pension at ".expr() +
                        uttaksgrad.format() + " percent from ".expr() +
                        kravDato.format() + ". Therefore, we have declined your application."
            )
        }
    }
}

object AvslagAPTidligUttak : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        paragraph {
            text(
                Bokmal to "Våre beregninger viser at du ikke har rett til å ta ut alderspensjonen din før du blir 67 år.",
                Nynorsk to "Våre berekningar viser at du ikkje har rett til å ta ut alderspensjon før du blir 67 år.",
                English to "Our calculations shows that you are not eligible for retirement pension before the age of 67."
            )
        }
}

data class EuArt6Hjemmel(
    val proRataErBrukt: Expression<Boolean>,
    val avtalelandErEOSLand: Expression<Boolean>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            showIf(proRataErBrukt and avtalelandErEOSLand) {
                text(
                    Bokmal to "Vedtaket er også gjort etter EØS-avtalens regler i forordning 883/2004, artikkel 6.",
                    Nynorsk to "",
                    English to "Eu art6 hjemmel avslag."
                )
            }
        }
    }
}

data class AvtaleFritekstHjemmel(
    val proRataErBrukt: Expression<Boolean>,
    val avtalelandNavn: Expression<String>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            showIf(proRataErBrukt) {
                textExpr(
                    Bokmal to "Vedtaket er også gjort etter artikkel [FRITEKST; Legg inn aktuelle artikler om sammenlegging og eksport] i trygdeavtalen med ".expr() + avtalelandNavn + ".",
                    Nynorsk to "Vedtaket er også gjort etter artikkel [FRITEKST; Legg inn aktuelle artikler om sammenlegging og eksport] i trygdeavtale med ".expr() + avtalelandNavn + ".",
                    English to "This decision is also made pursuant to the provisions of Article [FRITEKST; Legg inn aktuelle artikler om sammenlegging og eksport] in the social security agreement with ".expr() + avtalelandNavn + "."
                )
            }
        }
    }
}


