package no.nav.pensjon.brev.model

import no.nav.pensjon.brev.api.model.*
import no.nav.pensjon.brev.api.model.Sivilstand.*
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.*

fun Telefonnummer.format() =
    "([0-9][0-9])".toRegex().replace(value, "$1 ").trim()

fun Expression<Telefonnummer>.format() =
    Expression.UnaryInvoke(this, UnaryOperation.FormatPhoneNumber)


fun Foedselsnummer.format() =
    "([0-9]{6})([0-9]{5})".toRegex().replace(value, "$1 $2")

@JvmName("formatFoedselsnummer")
fun Expression<Foedselsnummer>.format() =
    Expression.UnaryInvoke(this, UnaryOperation.FormatFoedselsnummer)


@JvmName("formatKroner")
fun Expression<Kroner>.format() =
    Expression.BinaryInvoke(
        select(intValueSelector),
        Expression.FromScope(ExpressionScope<Any, *>::language),
        BinaryOperation.LocalizedCurrencyFormat
    )

@JvmName("formatIntValue")
fun Expression<IntValue>.format() =
    select(intValueSelector).format()

@JvmName("formatSivilstand")
fun Expression<Sivilstand>.tableFormat() =
    Expression.BinaryInvoke(
        this,
        Expression.FromScope(ExpressionScope<Any, *>::language),
        FormatSivilstandTabell,
    )


object FormatSivilstandTabell : BinaryOperation<Sivilstand, Language, String>() {
    override fun apply(first: Sivilstand, second: Language): String =
        when (first) {
            ENKE -> when (second) {
                Bokmal -> "Enke/Enkemann"
                Nynorsk -> "Enkje/Enkjemann"
                English -> "Widow/widower"
            }

            ENSLIG -> when (second) {
                Bokmal -> "Enslig"
                Nynorsk -> "Einsleg"
                English -> "Single"
            }

            GIFT,
            GIFT_LEVER_ADSKILT,
            SEPARERT -> when (second) {
                Bokmal, Nynorsk -> "Gift"
                English -> "Married"
            }

            PARTNER,
            PARTNER_LEVER_ADSKILT,
            SEPARERT_PARTNER -> when (second) {
                Bokmal -> "Partner"
                Nynorsk -> "Partnar"
                English -> "Partnership"
            }

            SAMBOER1_5 -> when (second) {
                Bokmal -> "Samboer (jf. Folketrygdloven § 1-5)"
                Nynorsk -> "Sambuar (jf. folketrygdlova § 1-5)"
                English -> "Cohabitation (cf. Section 1-5 of the National Insurance Act)"
            }

            SAMBOER3_2 -> when (second) {
                Bokmal -> "Samboer (jf. Folketrygdloven § 12-13)"
                Nynorsk -> "Sambuar (jf. Folketrygdlova § 12-13)"
                English -> "Cohabitation (cf. Section 12-13 of the National Insurance Act)"
            }
        }
}

@JvmName("formatSivilstandBestemtForm")
fun Expression<Sivilstand>.bestemtForm() =
    Expression.BinaryInvoke(this, Expression.FromScope(ExpressionScope<Any, *>::language), SivilstandEpsBestemt)

@JvmName("formatSivilstandUbestemtForm")
fun Expression<Sivilstand>.ubestemtForm() =
    Expression.BinaryInvoke(this, Expression.FromScope(ExpressionScope<Any, *>::language), SivilstandEpsUbestemt)

object SivilstandEpsUbestemt : BinaryOperation<Sivilstand, Language, String>() {
    override fun apply(first: Sivilstand, second: Language): String = sivilstand(first, second, false)
}

object SivilstandEpsBestemt : BinaryOperation<Sivilstand, Language, String>() {
    override fun apply(first: Sivilstand, second: Language): String = sivilstand(first, second, true)
}

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
        SEPARERT -> ""
    }