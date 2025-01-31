package no.nav.pensjon.brev.template

class PreventToStringForExpressionException : Exception(
    "Expression.toString should not be used. " +
            "In most cases this means that a template contains string concatenation of a string literal with an Expression-object, e.g:" +
            "text(Bokmal to \"The year is \${year.format()} \")"
)