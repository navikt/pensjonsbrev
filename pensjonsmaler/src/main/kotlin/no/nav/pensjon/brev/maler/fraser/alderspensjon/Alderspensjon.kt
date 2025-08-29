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
                    bokmal { + "Nav Familie-og pensjonsytelser Oslo 2" },
                    nynorsk { + "Nav Familie-og pensjonsytelser Oslo 2" },
                    english { + "NAV Familie-og pensjonsytelser Oslo 2" },
                )
                newline()
                text(
                    bokmal { + "Postboks 6600 Etterstad" },
                    nynorsk { + "Postboks 6600 Etterstad" },
                    english { + "Postboks 6600 Etterstad" },
                )
                newline()
                text(
                    bokmal { + "0607 Oslo" },
                    nynorsk { + "0607 Oslo" },
                    english { + "0607 Oslo" }
                )
                showIf(Expression.FromScope.Language.equalTo(English.expr())) { newline() }
                text(
                    bokmal { + "" },
                    nynorsk { + "" },
                    english { + "NORWAY" },
                )
            }
        }
    }

}