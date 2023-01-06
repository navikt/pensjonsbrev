package no.nav.pensjon.brev.maler.fraser.common

import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*

object Felles {

    /**
     * TBU1223, TBU1224
     */
    object MeldEndringerPesys_001 : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
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
    }

    /**
     * TBU1100
     */
    object RettTilKlagePesys_001 : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
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
    }

    /**
     * TBU1074, TBU2242NB, TBU1075NN, TBU2242EN  // Updated texst in the paragraph, approved by Ingrid
     */
    object RettTilInnsynPesys_001 : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title1 {
                text(
                    Bokmal to "Du har rett til innsyn",
                    Nynorsk to "Du har rett til innsyn",
                    English to "You have the right to access your file",
                )
            }

            paragraph {
                text(
                    Bokmal to "Du har rett til å se dokumentene i saken din. Se vedlegg “Orientering om rettigheter og plikter“ for informasjon om hvordan du går fram.",
                    Nynorsk to "Du har rett til å sjå dokumenta i saka di. Sjå vedlegg “Orientering om rettar og plikter“ for informasjon om korleis du går fram.",
                    English to "You are entitled to see your case documents. Refer to the attachment “Rights and obligations” for information about how to proceed.",
                )
            }
        }
    }

    data class KronerText(val kroner: Expression<Kroner>, val fontType: FontType = FontType.PLAIN) : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
        override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
            textExpr(
                Bokmal to kroner.format() + " kr",
                Nynorsk to kroner.format() + " kr",
                English to kroner.format() + " NOK",
                fontType
            )
    }

    data class MaanederText(val antall: Expression<Int>) : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
        override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
            textExpr(
                Bokmal to antall.format() + " måneder",
                Nynorsk to antall.format() + " måneder",
                English to antall.format() + " months"
            )
    }
    data class SivilstandEPSBestemtForm(val sivilstand: Expression<Sivilstand>) :
        ParagraphPhrase<LangBokmalNynorskEnglish>() {
        override fun ParagraphOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            showIf(sivilstand.isOneOf(Sivilstand.GIFT, Sivilstand.GIFT_LEVER_ADSKILT)) {
                text(
                    Bokmal to "ektefellen",
                    Nynorsk to "ektefellen",
                    English to "spouse"
                )
            }.orShowIf(sivilstand.isOneOf(Sivilstand.PARTNER, Sivilstand.PARTNER_LEVER_ADSKILT)) {
                text(
                    Bokmal to "partneren",
                    Nynorsk to "partnaren",
                    English to "partner"
                )
            }.orShowIf(sivilstand.isOneOf(Sivilstand.SAMBOER1_5, Sivilstand.SAMBOER3_2)) {
                text(
                    Bokmal to "samboeren",
                    Nynorsk to "sambuaren",
                    English to "cohabitant"
                )
            }
        }
    }

    data class SivilstandEPSUbestemtForm(val sivilstand: Expression<Sivilstand>) :
        ParagraphPhrase<LangBokmalNynorskEnglish>() {
        override fun ParagraphOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            showIf(sivilstand.isOneOf(Sivilstand.GIFT, Sivilstand.GIFT_LEVER_ADSKILT)) {
                text(
                    Bokmal to "ektefelle",
                    Nynorsk to "ektefelle",
                    English to "spouse"
                )
            }.orShowIf(sivilstand.isOneOf(Sivilstand.PARTNER, Sivilstand.PARTNER_LEVER_ADSKILT)) {
                text(
                    Bokmal to "partner",
                    Nynorsk to "partnar",
                    English to "partner"
                )
            }.orShowIf(sivilstand.isOneOf(Sivilstand.SAMBOER1_5, Sivilstand.SAMBOER3_2)) {
                text(
                    Bokmal to "samboer",
                    Nynorsk to "sambuar",
                    English to "cohabitant"
                )
            }
        }
    }
}