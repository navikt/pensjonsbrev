package no.nav.pensjon.etterlatte.maler.fraser.common

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.ifElse

fun mottakersFoedselsnummer(
    harVerge: Expression<Boolean>,
    under18Aar: Expression<Boolean>,
    language: Language
): Expression<String> =
    when (language) {
        is Language.English ->
            ifElse(
                harVerge,
                "the national identity number of your ward".expr(),
                ifElse(
                    under18Aar, "your child's national identity number",
                    "your national identity number"
                )
            )

        is Language.Bokmal ->
            ifElse(
                harVerge,
                "fødselsnummeret til den du er verge for".expr(),
                ifElse(under18Aar, "fødselsnummeret til barnet", "fødselsnummer")
            )

        is Language.Nynorsk -> ifElse(
            harVerge,
            "fødselsnummeret til den du er verje for".expr(),
            ifElse(under18Aar, "fødselsnummeret til barnet", "fødselsnummer")
        )
    }

