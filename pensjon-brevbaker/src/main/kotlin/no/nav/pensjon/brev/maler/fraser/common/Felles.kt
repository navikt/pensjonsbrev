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
                    Bokmal to "Du har rett til å se dokumentene i saken din. Se vedlegg «Dine rettigheter og plikter» for informasjon om hvordan du går fram.",
                    Nynorsk to "Du har rett til å sjå dokumenta i saka di. Sjå vedlegg «Dine rettar og plikter» for informasjon om korleis du går fram.",
                    English to "You are entitled to see your case documents. Refer to the attachment «Your rights and obligations» for information about how to proceed.",
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
}