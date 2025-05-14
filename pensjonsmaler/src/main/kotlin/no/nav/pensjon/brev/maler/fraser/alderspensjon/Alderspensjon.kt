package no.nav.pensjon.brev.maler.fraser.alderspensjon

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.text

class Alderspensjon {
    object Returadresse : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    Bokmal to "Nav Familie-og pensjonsytelser Oslo 2",
                    Nynorsk to "Nav Familie-og pensjonsytelser Oslo 2",
                    English to "NAV Familie-og pensjonsytelser Oslo 2",
                )
                newline()
                text(
                    Bokmal to "Postboks 6600 Etterstad",
                    Nynorsk to "Postboks 6600 Etterstad",
                    English to "Postboks 6600 Etterstad",
                )
                newline()
                text(
                    Bokmal to "0607 Oslo",
                    Nynorsk to "0607 Oslo",
                    English to "0607 Oslo"
                )
                showIf(Expression.FromScope.Language.equalTo(English.expr())) { newline() }
                text(
                    Bokmal to "",
                    Nynorsk to "",
                    English to "NORWAY",
                )
            }
        }
    }

}