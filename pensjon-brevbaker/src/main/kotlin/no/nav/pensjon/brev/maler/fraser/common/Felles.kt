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
     * TBU1074, TBU2242NB, TBU1075NN, TBU2242EN
     */
    object RettTilInnsynPesys : OutlinePhrase<LangBokmalNynorskEnglish>() {
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