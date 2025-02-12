package no.nav.pensjon.brev.maler.fraser.alderspensjon

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.text

class Alderspensjon {
    object Returadresse : OutlinePhrase<LangBokmalEnglish>() {
        override fun OutlineOnlyScope<LangBokmalEnglish, Unit>.template() {
            paragraph {
                text(
                    Bokmal to "Nav Familie-og pensjonsytelser Oslo 2",
                    English to "NAV Familie-og pensjonsytelser Oslo 2",
                )
                newline()
                text(
                    Bokmal to "Postboks 6600 Etterstad",
                    English to "Postboks 6600 Etterstad",
                )
                newline()
                text(
                    Bokmal to "0607 Oslo",
                    English to "0607 Oslo",
                )
                showIf(Expression.FromScope.Language.equalTo(English.expr())) { newline() }
                text(
                    Bokmal to "",
                    English to "NORWAY",
                )
            }
        }
    }
}
