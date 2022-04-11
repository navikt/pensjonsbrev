package no.nav.pensjon.brev.maler.fraser.common

import no.nav.pensjon.brev.maler.fraser.Constants
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.map
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.expression.str

object Felles {

    /**
     * TBU1223, TBU1224
     */
    val meldEndringerPesys_001 = OutlinePhrase<LangBokmalNynorsk, Unit> {
        title1 {
            text(
                Bokmal to "Du må melde fra om endringer",
                Nynorsk to "Du må melde fra om endringer",
            )
        }
        paragraph {
            text(
                Bokmal to "Skjer det endringer, må du melde fra til oss med en gang. I vedlegget ser du hvilke endringer du må si fra om.",
                Nynorsk to "Skjer det endringar, må du melde frå til oss med ein gong. I vedlegget ser du kva endringar du må seie frå om.",
            )
        }

        paragraph {
            text(
                Bokmal to "Hvis du har fått utbetalt for mye fordi du ikke har gitt oss beskjed, må du vanligvis betale tilbake pengene. " +
                        "Du er selv ansvarlig for å holde deg orientert om bevegelser på kontoen din, og du må melde fra om eventuelle feil til NAV.",

                Nynorsk to "Dersom du har fått utbetalt for mykje fordi du ikkje har gitt oss beskjed, må du vanlegvis betale tilbake pengane. " +
                        "Du er sjølv ansvarleg for å halde deg orientert om rørsler på kontoen din, og du må melde frå om eventuelle feil til NAV.",
            )
        }
    }

    /**
     * TBU1100
     */
    val rettTilKlagePesys_001 = OutlinePhrase<LangBokmalNynorsk, Unit> {
        title1 {
            text(
                Bokmal to "Du har rett til å klage",
                Nynorsk to "Du har rett til å klage",
            )
        }

        paragraph {
            text(
                Bokmal to "Hvis du mener vedtaket er feil, kan du klage innen seks uker fra den datoen du mottok vedtaket. Klagen skal være skriftlig. Du finner skjema og informasjon på ${Constants.KLAGE_URL}.",
                Nynorsk to "Dersom du meiner at vedtaket er feil, kan du klage innan seks veker frå den datoen du fekk vedtaket. Klaga skal vera skriftleg. Du finn skjema og informasjon på ${Constants.KLAGE_URL}.",
            )
        }

        paragraph {
            text(
                Bokmal to "I vedlegget får du vite mer om hvordan du går fram.",
                Nynorsk to "I vedlegget får du vite meir om korleis du går fram.",
            )
        }
    }

    /**
     * TBU1074, TBU1075
     */
    val rettTilInnsynPesys_001 = OutlinePhrase<LangBokmalNynorsk, Unit> {
        title1 {
            text(
                Bokmal to "Du har rett til innsyn",
                Nynorsk to "Du har rett til innsyn",
            )
        }

        paragraph {
            text(
                Bokmal to "Du har rett til å se dokumentene i saken din. I vedlegget får du vite hvordan du går fram.",
                Nynorsk to "Du har rett til å sjå dokumenta i saka di. I vedlegget får du vite korleis du går fram.",
            )
        }
    }

    val kroner = TextOnlyPhrase<LangBokmalNynorskEnglish, Int> { beloep ->
        val kroner = beloep.map{Kroner(it)}
        textExpr(
            Bokmal to kroner.format() + " kr",
            Nynorsk to kroner.format() + " kr",
            English to kroner.format() + " NOK"
        )
    }

    val maaneder = TextOnlyPhrase<LangBokmalNynorskEnglish, Int> { maaned ->
        textExpr(
            Bokmal to maaned.str() + " måneder",
            Nynorsk to maaned.str() + " måneder",
            English to maaned.str() + " months"
        )
    }
}