package no.nav.pensjon.brev.model

import no.nav.pensjon.brev.api.model.*
import no.nav.pensjon.brev.api.model.Sivilstand.*
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.*

fun Telefonnummer.format() =
    "([0-9][0-9])".toRegex().replace(value, "$1 ").trim()

fun Foedselsnummer.format() =
    "([0-9]{6})([0-9]{5})".toRegex().replace(value, "$1 $2")

fun Expression<Telefonnummer>.format() =
    Expression.UnaryInvoke(this, UnaryOperation.FormatPhoneNumber)

@JvmName("formatKroner")
fun Expression<Kroner>.format() = select(intValueSelector).format(formatter = BinaryOperation.LocalizedCurrencyFormat)

@JvmName("formatIntValue")
fun Expression<IntValue>.format() = select(intValueSelector).format()

@JvmName("formatBormedSivilstandTabell")
fun Expression<BorMedSivilstand>.tableFormat() = format(formatter = FormatBorMedSivilstandTabell)

object FormatBorMedSivilstandTabell : LocalizedFormatter<BorMedSivilstand>() {
    override fun apply(first: BorMedSivilstand, second: Language): String =
        when (first) {
            BorMedSivilstand.GIFT_LEVER_ADSKILT,
            BorMedSivilstand.EKTEFELLE -> when (second) {
                Bokmal, Nynorsk -> "Gift"
                English -> "Married"
            }

            BorMedSivilstand.PARTNER, BorMedSivilstand.PARTNER_LEVER_ADSKILT -> when (second) {
                Bokmal -> "Partner"
                Nynorsk -> "Partnar"
                English -> "Partnership"
            }

            BorMedSivilstand.SAMBOER1_5 -> when (second) {
                Bokmal -> "Samboer (jf. Folketrygdloven § 1-5)"
                Nynorsk -> "Sambuar (jf. folketrygdlova § 1-5)"
                English -> "Cohabitation (cf. Section 1-5 of the National Insurance Act)"
            }

            BorMedSivilstand.SAMBOER3_2 -> when (second) {
                Bokmal -> "Samboer (jf. Folketrygdloven § 12-13)"
                Nynorsk -> "Sambuar (jf. Folketrygdlova § 12-13)"
                English -> "Cohabitation (cf. Section 12-13 of the National Insurance Act)"
            }
        }
}


@JvmName("formatSivilstandBestemtForm")
fun Expression<Sivilstand>.bestemtForm() =
    Expression.BinaryInvoke(this, Expression.FromScope(ExpressionScope<Any, *>::language), SivilstandEpsBestemt)

@Deprecated("Bruk bormed sivilstand istedenfor")
object SivilstandEpsBestemt : LocalizedFormatter<Sivilstand>() {
    override fun apply(first: Sivilstand, second: Language): String = sivilstand(first, second, true)
}

@Deprecated("bruk bormedSivilstand")
private fun sivilstand(sivilstand: Sivilstand, language: Language, bestemtForm: Boolean): String =
    when (sivilstand) {
        GIFT,
        GIFT_LEVER_ADSKILT -> when (language) {
            Bokmal, Nynorsk -> if (bestemtForm) "ektefellen" else "ektefelle"
            English -> "spouse"
        }

        PARTNER,
        PARTNER_LEVER_ADSKILT,
        SEPARERT_PARTNER -> when (language) {
            Bokmal -> if (bestemtForm) "partneren" else "partner"
            Nynorsk -> if (bestemtForm) "partnaren" else "partnar"
            English -> "partner"
        }

        SAMBOER1_5, SAMBOER3_2 -> when (language) {
            Bokmal -> if (bestemtForm) "samboeren" else "samboer"
            Nynorsk -> if (bestemtForm) "sambuaren" else "sambuar"
            English -> "cohabitant"
        }

        //TODO lag en egen SivilstandEps enum slik at vi kan garantere at teksten blir riktig.
        ENSLIG,
        ENKE,
        SEPARERT-> ""
    }


@JvmName("formatBorMedSivilstandBestemtForm")
fun Expression<BorMedSivilstand>.bestemtForm() = format(formatter = BorMedSivilstandBestemt)

@JvmName("formatBorMedSivilstandUbestemtForm")
fun Expression<BorMedSivilstand>.ubestemtForm() = format(formatter = BorMedSivilstandUbestemt)

object BorMedSivilstandUbestemt : LocalizedFormatter<BorMedSivilstand>() {
    override fun apply(first: BorMedSivilstand, second: Language): String = borMedSivilstand(first, second, false)
}

object BorMedSivilstandBestemt : LocalizedFormatter<BorMedSivilstand>() {
    override fun apply(first: BorMedSivilstand, second: Language): String = borMedSivilstand(first, second, true)
}
private fun borMedSivilstand(sivilstand: BorMedSivilstand, language: Language, bestemtForm: Boolean): String =
    when (sivilstand) {
        BorMedSivilstand.EKTEFELLE,
        BorMedSivilstand.GIFT_LEVER_ADSKILT -> when (language) {
            Bokmal, Nynorsk -> if (bestemtForm) "ektefellen" else "ektefelle"
            English -> "spouse"
        }

        BorMedSivilstand.PARTNER,
        BorMedSivilstand.PARTNER_LEVER_ADSKILT -> when (language) {
            Bokmal -> if (bestemtForm) "partneren" else "partner"
            Nynorsk -> if (bestemtForm) "partnaren" else "partnar"
            English -> "partner"
        }

        BorMedSivilstand.SAMBOER1_5, BorMedSivilstand.SAMBOER3_2 -> when (language) {
            Bokmal -> if (bestemtForm) "samboeren" else "samboer"
            Nynorsk -> if (bestemtForm) "sambuaren" else "sambuar"
            English -> "cohabitant"
        }
    }