package no.nav.pensjon.brev.maler.fraser.common

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.Kroner

object Felles {

    /**
     * TBU1074, TBU2242NB, TBU1075NN, TBU2242EN, RettTilInnsynPesys_001
     */
    data class RettTilInnsynForVedlegg(val vedlegg: AttachmentTemplate<LangBokmalNynorskEnglish, *>) : OutlinePhrase<LangBokmalNynorskEnglish>() {
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
                    Bokmal to "Du har rett til å se dokumentene i saken din. Se vedlegg ",
                    Nynorsk to "Du har rett til å sjå dokumenta i saka di. Sjå vedlegg ",
                    English to "You are entitled to see your case documents. Refer to the attachment ",
                )
                namedReference(vedlegg)
                text(
                    Bokmal to  " for informasjon om hvordan du går fram.",
                    Nynorsk to " for informasjon om korleis du går fram.",
                    English to " for information about how to proceed.",
                )
            }
        }
    }

    object HarDuSpoersmaalOmsorgsarbeid : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title1 {
                text(
                    Bokmal to "Har du spørsmål?",
                    Nynorsk to "Har du spørsmål?",
                    English to "Do you have questions?"
                )
            }
            paragraph {
                text(
                    Bokmal to "Du finner mer informasjon på ${Constants.OMSORGSOPPTJENING_URL}."
                            + " På ${Constants.KONTAKT_URL} kan du chatte eller skrive til oss."
                            + " Hvis du ikke finner svar på ${Constants.NAV_URL}, kan du ringe oss på telefon ${Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON},"
                            + " hverdager kl. ${Constants.NAV_KONTAKTSENTER_AAPNINGSTID}.",
                    Nynorsk to "Du finn meir informasjon på ${Constants.OMSORGSOPPTJENING_URL}."
                            + " Om du ikkje finn svar på ${Constants.NAV_URL}, kan du ringe oss på telefon ${Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON},"
                            + " kvardagar kl. ${Constants.NAV_KONTAKTSENTER_AAPNINGSTID}.",
                    English to "You can find more information at ${Constants.OMSORGSOPPTJENING_URL}."
                            + " At ${Constants.KONTAKT_URL}, you can chat or write to us."
                            + " If you do not find the answer at ${Constants.NAV_URL}, you can call us at: +47 ${Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON},"
                            + " weekdays ${Constants.NAV_KONTAKTSENTER_AAPNINGSTID}."
                )
            }
        }
    }

    data class KronerText(val kroner: Expression<Kroner>, val fontType: FontType = FontType.PLAIN) :
        TextOnlyPhrase<LangBokmalNynorskEnglish>() {
        override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
            textExpr(
                Bokmal to kroner.format() + " kr",
                Nynorsk to kroner.format() + " kr",
                English to "NOK ".expr() + kroner.format(),
                fontType,
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

    data class TextOrList(val foedselsdatoer: Expression<Collection<String>>, val limit: Int = 2) :
        ParagraphPhrase<LangBokmalNynorskEnglish>() {
        override fun ParagraphOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            showIf(foedselsdatoer.size().lessThanOrEqual(limit)) {
                val foedselsDato = foedselsdatoer.format()
                textExpr(
                    Bokmal to " ".expr() + foedselsDato + ".",
                    Nynorsk to " ".expr() + foedselsDato + ".",
                    English to " ".expr() + foedselsDato + ".",
                )
            }.orShow {
                text(Bokmal to ":", Nynorsk to ":", English to ":")
                list {
                    forEach(foedselsdatoer) {
                        item { textExpr(Bokmal to it, Nynorsk to it, English to it) }
                    }
                }
            }
        }
    }
}