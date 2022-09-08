package no.nav.pensjon.brev.model

import no.nav.pensjon.brev.api.model.*
import no.nav.pensjon.brev.api.model.Sivilstand.*
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.expression.*

fun Telefonnummer.format() =
    "([0-9][0-9])".toRegex().replace(value, "$1 ").trim()

fun Foedselsnummer.format() =
    "([0-9]{6})([0-9]{5})".toRegex().replace(value, "$1 $2")

fun Expression<Telefonnummer>.format() =
    Expression.UnaryInvoke(this, UnaryOperation.FormatPhoneNumber)

@JvmName("formatKroner")
fun Expression<Kroner>.format() =
    Expression.BinaryInvoke(
        select(intValueSelector),
        Expression.FromScope.language(ExpressionScope<Any, *>::language),
        BinaryOperation.LocalizedCurrencyFormat
    )

@JvmName("formatIntValue")
fun Expression<IntValue>.format() =
    select(intValueSelector).format()

@JvmName("formatSivilstand")
fun Expression<Sivilstand>.tableFormat() =
    Expression.BinaryInvoke(
        this,
        Expression.FromScope.language(ExpressionScope<Any, *>::language),
        FormatSivilstandTabell
    )


object FormatSivilstandTabell : BinaryOperation<Sivilstand, Language, String>() {
    override fun apply(first: Sivilstand, second: Language): String =
        when (first) {
            ENSLIG -> when (second) {
                Bokmal -> "Enke/Enkemann"
                Nynorsk -> "Enkje/Enkjemann"
                English -> "Widow/widower"
            }
            ENKE -> when (second) {
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
                Bokmal -> "Samboer (jf. Folketrygdloven § 1-5)"
                Nynorsk -> "Sambuar (jf. folketrygdlova § 1-5)"
                English -> "Cohabitation (cf. Section 1-5 of the National Insurance Act)"
            }
            SAMBOER3_2 -> when (second) {
                Bokmal -> "Samboer (jf. Folketrygdloven § 12-13)"
                Nynorsk -> "Sambuar (jf. folketrygdlova § 12-13)"
                English -> "Cohabitation (cf. Section 12-13 of the National Insurance Act)"
            }
        }

    // TODO denne fungerer ikke som infix
    override val symbol = " .formatSivilstand(language)"
}