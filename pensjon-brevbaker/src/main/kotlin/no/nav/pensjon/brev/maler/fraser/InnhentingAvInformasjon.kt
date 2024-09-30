package no.nav.pensjon.brev.maler.fraser

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.textExpr

object InnhentingAvInformasjon {
    data class Returadresse(
        val avsenderEnhetNavn: Expression<String>,
        val avsenderEnhetAdresselinje1: Expression<String>,
        val avsenderEnhetAdresselinje2: Expression<String>,
        val avsenderEnhetLand: Expression<String?>,

        ) : OutlinePhrase<LangBokmalEnglish>() {
        override fun OutlineOnlyScope<LangBokmalEnglish, Unit>.template() {
            paragraph {
                textExpr(
                    Bokmal to avsenderEnhetNavn,
                    English to avsenderEnhetNavn,
                )
            }
            paragraph {
                textExpr(
                    Bokmal to avsenderEnhetAdresselinje1,
                    English to avsenderEnhetAdresselinje1,
                )
            }
            paragraph {
                textExpr(
                    Bokmal to avsenderEnhetAdresselinje2,
                    English to avsenderEnhetAdresselinje2,
                )
            }
            ifNotNull(avsenderEnhetLand) { land ->
                paragraph {
                    textExpr(
                        Bokmal to land,
                        English to land,
                    )
                }
            }
        }

    }
}