package no.nav.pensjon.brev.maler.fraser.common

import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.maler.fraser.Constants
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.TextOnlyPhrase
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr

object Felles {

    /**
     * TBU1223, TBU1224
     */
    val meldEndringerPesys_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
        title1 {
            text(
                Bokmal to "Du må melde fra om endringer",
                Nynorsk to "Du må melde frå om endringar",
                English to "You must notify NAV if anything changes",
            )
        }
        paragraph {
            text(
                Bokmal to "Skjer det endringer, må du melde fra til oss med en gang. I vedlegget ser du hvilke endringer du må si fra om.",
                Nynorsk to "Skjer det endringar, må du melde frå til oss med ein gong. I vedlegget ser du kva endringar du må seie frå om.",
                English to "If your circumstances change, you must inform NAV immediately. The appendix includes information on how to proceed.",
            )
        }

        paragraph {
            text(
                Bokmal to "Hvis du har fått utbetalt for mye fordi du ikke har gitt oss beskjed, må du vanligvis betale tilbake pengene. " +
                        "Du er selv ansvarlig for å holde deg orientert om bevegelser på kontoen din, og du må melde fra om eventuelle feil til NAV.",

                Nynorsk to "Dersom du har fått utbetalt for mykje fordi du ikkje har gitt oss beskjed, må du vanlegvis betale tilbake pengane. " +
                        "Du er sjølv ansvarleg for å halde deg orientert om rørsler på kontoen din, og du må melde frå om eventuelle feil til NAV.",
                English to "If your payments have been too high as a result of you failing to notify us of a change, the incorrect payment must normally be repaid. " +
                        "It is your responsibility to keep yourself informed of movements in your account, and you are obligated to report any and all errors to NAV.",
            )
        }
    }

    /**
     * TBU1100
     */
    val rettTilKlagePesys_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
        title1 {
            text(
                Bokmal to "Du har rett til å klage",
                Nynorsk to "Du har rett til å klage",
                English to "You have the right to appeal",
            )
        }

        paragraph {
            text(
                Bokmal to "Hvis du mener vedtaket er feil, kan du klage innen seks uker fra den datoen du mottok vedtaket. Klagen skal være skriftlig. " +
                        "Du finner skjema og informasjon på ${Constants.KLAGE_URL}.",
                Nynorsk to "Dersom du meiner at vedtaket er feil, kan du klage innan seks veker frå den datoen du fekk vedtaket. Klaga skal vera skriftleg. " +
                        "Du finn skjema og informasjon på ${Constants.KLAGE_URL}.",
                English to "If you think the decision is wrong, you may appeal the decision within six weeks of the date on which you received notice of the decision. " +
                        "Your appeal must be made in writing. You will find a form you can use and more information about appeals at ${Constants.KLAGE_URL}.",
            )
        }

        paragraph {
            text(
                Bokmal to "I vedlegget får du vite mer om hvordan du går fram.",
                Nynorsk to "I vedlegget får du vite meir om korleis du går fram.",
                English to "The appendix includes information on how to proceed.",
            )
        }
    }

    /**
     * TBU1074, TBU1075
     */
    val rettTilInnsynPesys_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
        title1 {
            text(
                Bokmal to "Du har rett til innsyn",
                Nynorsk to "Du har rett til innsyn",
                English to "You have the right to access your file",
            )
        }

        paragraph {
            text(
                Bokmal to "Du har rett til å se dokumentene i saken din. I vedlegget får du vite hvordan du går fram.",
                Nynorsk to "Du har rett til å sjå dokumenta i saka di. I vedlegget får du vite korleis du går fram.",
                English to "You have the right to access all documents pertaining to your case. The appendix includes information on how to proceed.",
            )
        }
    }

    val kroner = TextOnlyPhrase<LangBokmalNynorskEnglish, Kroner> { kroner ->
        textExpr(
            Bokmal to kroner.format() + " kr",
            Nynorsk to kroner.format() + " kr",
            English to kroner.format() + " NOK",
        )
    }

    val maaneder = TextOnlyPhrase<LangBokmalNynorskEnglish, Int> { maaned ->
        textExpr(
            Bokmal to maaned.format() + " måneder",
            Nynorsk to maaned.format() + " måneder",
            English to maaned.format() + " months"
        )
    }
}