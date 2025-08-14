package no.nav.pensjon.brev.template.dsl.expression

import no.nav.pensjon.brev.template.BinaryOperation
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brevbaker.api.model.IntValue
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.Percent
import no.nav.pensjon.brevbaker.api.model.Year

// Comparable
@JvmName("notEqualToComparable")
infix fun <T : Comparable<T>> Expression<T?>.notEqualTo(other: T?): Expression<Boolean> =
    not(equalTo(other))

@JvmName("notEqualToComparable")
infix fun <T : Comparable<T>> Expression<T?>.notEqualTo(other: Expression<T?>): Expression<Boolean> =
    not(equalTo(other))

@JvmName("equalToComparable")
infix fun <T : Comparable<T>> Expression<T?>.equalTo(other: T?): Expression<Boolean> =
    equalTo(other.expr())

@JvmName("equalToComparable")
infix fun <T : Comparable<T>> Expression<T?>.equalTo(other: Expression<T?>): Expression<Boolean> =
    BinaryOperation.Equal<T?>().invoke(this, other)

// Language
@JvmName("equalToLanguage")
infix fun Expression<Language?>.equalTo(other: Expression<Language?>): Expression<Boolean> =
    BinaryOperation.Equal<Language?>().invoke(this, other)

infix fun Expression<Language?>.equalTo(other: Language?): Expression<Boolean> =
    equalTo(other.expr())

@JvmName("notEqualToLanguage")
infix fun Expression<Language?>.notEqualTo(other: Expression<Language?>): Expression<Boolean> =
    not(equalTo(other))

infix fun Expression<Language?>.notEqualTo(other: Language?): Expression<Boolean> =
    not(equalTo(other))


// IntValue equals literal
@JvmName("equalToIntValue")
infix fun Expression<IntValue?>.equalTo(compareTo: Int?): Expression<Boolean> = safe { value }.equalTo(compareTo)
@JvmName("notEqualToIntValue")
infix fun Expression<IntValue?>.notEqualTo(compareTo: Int?): Expression<Boolean> = safe { value }.notEqualTo(compareTo)


// IntValue classes equals
@JvmName("equalToKroner")
infix fun Expression<Kroner?>.equalTo(compareTo: Expression<Kroner?>): Expression<Boolean> = BinaryOperation.Equal<Kroner?>().invoke(this, compareTo)
@JvmName("notEqualToKroner")
infix fun Expression<Kroner?>.notEqualTo(compareTo: Expression<Kroner?>): Expression<Boolean> = equalTo(compareTo).not()
@JvmName("equalToYear")
infix fun Expression<Year?>.equalTo(compareTo: Expression<Year?>): Expression<Boolean> = BinaryOperation.Equal<Year?>().invoke(this, compareTo)
@JvmName("notEqualToYear")
infix fun Expression<Year?>.notEqualTo(compareTo: Expression<Year?>): Expression<Boolean> = equalTo(compareTo).not()
@JvmName("equalToPercent")
infix fun Expression<Percent?>.equalTo(compareTo: Expression<Percent?>): Expression<Boolean> = BinaryOperation.Equal<Percent?>().invoke(this, compareTo)
@JvmName("notEqualToPercent")
infix fun Expression<Percent?>.notEqualTo(compareTo: Expression<Percent?>): Expression<Boolean> = equalTo(compareTo).not()
