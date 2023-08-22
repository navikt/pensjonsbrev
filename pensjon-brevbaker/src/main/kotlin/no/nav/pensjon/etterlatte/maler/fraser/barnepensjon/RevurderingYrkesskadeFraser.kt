package no.nav.pensjon.etterlatte.maler.fraser.barnepensjon

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

object RevurderingYrkesskadeFraser {
    data class Begrunnelse(
        val yrkesskadeErDokumentert: Expression<Boolean>,
        val virkningsdato: Expression<LocalDate>,
        val kronebeloep: Expression<Kroner>,
        val stoenadHarOekt: Expression<Boolean>
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            showIf(yrkesskadeErDokumentert) {
                paragraph {
                    val dinForelder = "<din mor/din far>"
                    showIf(stoenadHarOekt) {
                        textExpr(
                            Bokmal to ("Barnepensjonen din øker fra $virkningsdato".expr()
                                + " fordi det nå er dokumentert at $dinForelder døde som følge av yrkesskade. "),
                            Nynorsk to "".expr(),
                            English to "".expr())
                    }.orShow {
                        textExpr(Bokmal to "Det er nå er dokumentert at $dinForelder døde som følge av yrkesskade. ".expr(),
                            Nynorsk to "".expr(),
                            English to "".expr()
                        )
                    }
                    textExpr(
                        Bokmal to "Du får ".expr() + kronebeloep.format() + " kroner hver måned før skatt.",
                        Nynorsk to "".expr(),
                        English to "".expr(),
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Yrkesskaden/yrkessykdommen er dokumentert med vedtak fra NAV. " +
                                "Barnepensjonen er derfor innvilget etter særbestemmelser, se vedlegg om yrkesskade.",
                        Nynorsk to "",
                        English to "",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Vedtaket er gjort etter bestemmelsene om barnepensjon i " +
                                "folketrygdloven § 18-2, § 18-3, § 18-4, § 18-5, § 18-11 og § 22-13.",
                        Nynorsk to "",
                        English to ""
                    )
                }
            }.orShow {
                paragraph {
                    textExpr(
                        Bokmal to ("Barnepensjonen din er vurdert på nytt. " +
                                "Det blir ingen endring av din pensjon. ").expr() +
                                "Du får fortsatt " + kronebeloep.format() + " kroner hver måned før skatt.",
                        Nynorsk to "".expr(),
                        English to "".expr()
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Det er dokumentert med vedtak fra NAV at <din mor/din far> ikke døde som følge " +
                                "av yrkesskade/yrkessykdom. Det blir derfor ingen endring av barnepensjonen.",
                        Nynorsk to "",
                        English to ""
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Vedtaket er gjort etter bestemmelsene om barnepensjon i " +
                                "folketrygdloven § 18-2, § 18-3, § 18-4, § 18-5 og § 18-11.",
                        Nynorsk to "",
                        English to ""
                    )
                }
            }
        }
    }
}
