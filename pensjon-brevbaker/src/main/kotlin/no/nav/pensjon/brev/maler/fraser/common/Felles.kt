package no.nav.pensjon.brev.maler.fraser.common

import no.nav.pensjon.brev.maler.fraser.common.Constants.KONTAKT_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_URL
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
                    Bokmal to "Du har rett til å se dokumentene i saken din. Se vedlegg «Dine rettigheter og plikter» for informasjon om hvordan du går fram.",
                    Nynorsk to "Du har rett til å sjå dokumenta i saka di. Sjå vedlegg «Dine rettar og plikter» for informasjon om korleis du går fram.",
                    English to "You are entitled to see your case documents. Refer to the attachment “Your rights and obligations” for information about how to proceed.",
                )
            }
        }
    }

    object HarDuSpoersmaalPesys : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title1 {
                text(
                    Bokmal to "Har du spørsmål?",
                    Nynorsk to "Har du spørsmål?",
                    English to "Do you have questions?",
                )
            }
            paragraph {
                text(
                    Bokmal to "Du finner mer informasjon på $NAV_URL. Hvis du ikke finner svar på spørsmålet ditt, kontakt oss på $KONTAKT_URL.",
                    Nynorsk to "Du finn meir informasjon på $NAV_URL. Om du ikkje finn svar på spørsmålet ditt, kontakt oss på $KONTAKT_URL.",
                    English to "You can find more information at $NAV_URL. If you do not find the answer to your question, contact us at $KONTAKT_URL.",
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

    data class AarText(val antall: Expression<Int>) : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
        override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            textExpr(
                Bokmal to antall.format() + " år",
                Nynorsk to antall.format() + " år",
                English to antall.format() + " years"
            )
        }
    }

    data class ProsentText(val antall: Expression<Int>) : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
        override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            textExpr(
                Bokmal to antall.format() + " %",
                Nynorsk to antall.format() + " %",
                English to antall.format() + " %"
            )
        }
    }

    data class TextOrList(val foedselsdatoer: Expression<Collection<String>>, val limit: Int = 2): ParagraphPhrase<LangBokmalNynorskEnglish>(){
        override fun ParagraphOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            showIf(foedselsdatoer.size().lessThanOrEqual(limit)){
                val foedselsDato = foedselsdatoer.format()
                textExpr(
                    Bokmal to " ".expr() + foedselsDato + ".",
                    Nynorsk to " ".expr() + foedselsDato + ".",
                    English to " ".expr() + foedselsDato + ".",
                )
            }.orShow {
                text(Bokmal to ":", Nynorsk to ":", English to ":")
                list {
                    forEach(foedselsdatoer){
                        item { textExpr(Bokmal to it, Nynorsk to it, English to it) }
                    }
                }
            }
        }
    }
}