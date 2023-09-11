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
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

object RevurderingYrkesskadeFraser {
    data class Begrunnelse(
        val dinForelder: Expression<String>,
        val yrkesskadeErDokumentert: Expression<Boolean>,
        val virkningsdato: Expression<LocalDate>,
        val kronebeloep: Expression<Kroner>,
        val stoenadHarOekt: Expression<Boolean>
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            showIf(yrkesskadeErDokumentert) {
                yrkesskadeDokumentert()
            }.orShow {
                yrkesskadeIkkeDokumentert()
            }
        }

        private fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.yrkesskadeDokumentert() {
            showIf(stoenadHarOekt) {
                paragraph {
                    textExpr(
                        Bokmal to "Barnepensjonen din øker fra ".expr() + virkningsdato.format()
                                + " fordi det nå er dokumentert at " + dinForelder
                                + " døde som følge av yrkesskade eller yrkessykdom. "
                                + "Du får " + kronebeloep.format() + " kroner hver måned før skatt.",
                        Nynorsk to "".expr(),
                        English to "".expr())
                }
                paragraph {
                    text(
                        Bokmal to "Yrkesskaden eller yrkessykdommen er dokumentert med vedtak fra NAV. " +
                                "Barnepensjonen er derfor innvilget etter særbestemmelser, fordi avdøde døde som " +
                                "følge av skade eller sykdom som går inn under folketrygdloven kapittel 13.",
                        Nynorsk to "",
                        English to "")
                }
            }
            .orShow {
                paragraph {
                    textExpr(
                        Bokmal to "Barnepensjonen din er vurdert på nytt fra ".expr() + virkningsdato.format()
                                + " fordi det nå er dokumentert at " + dinForelder
                                + " døde som følge av yrkesskade eller yrkessykdom. Du får fortsatt "
                                + kronebeloep.format() + " kroner hver måned før skatt.",
                        Nynorsk to "".expr(),
                        English to "".expr())
                }
                paragraph {
                    text(
                        Bokmal to "Yrkesskaden eller yrkessykdommen er dokumentert med vedtak fra NAV.",
                        Nynorsk to "",
                        English to "")
                }
                paragraph {
                    text(Bokmal to "For å få full barnepensjon må avdøde ha vært medlem i folketrygden, eller " +
                            "mottatt pensjon eller uføretrygd fra folketrygden de siste fem årene før dødsfallet. " +
                            "I tillegg må avdøde ha hatt minst 40 års trygdetid. Trygdetiden tilsvarer det antall år " +
                            "avdøde har vært medlem i folketrygden etter fylte 16 år.",
                        Nynorsk to "",
                        English to "")
                }
                paragraph {
                    text(Bokmal to "Når dødsfall skyldes yrkesskade eller yrkessykdom som dokumenteres " +
                            "med vedtak fra NAV, er det særbestemmelser som gir unntak for disse kravene " +
                            "om medlemstid i folketrygden.",
                        Nynorsk to "",
                        English to "")
                }
                paragraph {
                    textExpr(Bokmal to "Du får full barnepensjon uten disse særbestemmelsene, men det er ".expr() +
                            "likevel registrert i saken din at dødsfallet til ".expr() + dinForelder +
                            " skyldes yrkesskade eller yrkessykdom.",
                        Nynorsk to "".expr(),
                        English to "".expr())
                }
                paragraph {
                    text(Bokmal to "Barnepensjonen er derfor innvilget etter særbestemmelser, fordi avdøde " +
                            "døde som følge av skade eller sykdom som går inn under folketrygdloven kapittel 13. " +
                            "Dette fører imidlertid ikke til endring av din pensjon da vilkårene for full " +
                            "barnepensjon er oppfylt uavhengig av at dødsfallet skyldes yrkesskade/yrkessykdom.",
                        Nynorsk to "",
                        English to "")
                }
            }
            paragraph {
                text(
                    Bokmal to "Vedtaket er gjort etter bestemmelsene om barnepensjon i " +
                            "folketrygdloven § 18-2, § 18-3, § 18-4, § 18-5, § 18-11, § 22-14 fjerde ledd " +
                            "og § 22-12 fjerde ledd.",
                    Nynorsk to "",
                    English to ""
                )
            }
        }
        private fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.yrkesskadeIkkeDokumentert() {
            paragraph {
                textExpr(
                    Bokmal to "Barnepensjonen din er vurdert på nytt. ".expr()
                            + "Det blir ingen endring av din pensjon. "
                            + "Du får fortsatt " + kronebeloep.format() + " kroner hver måned før skatt.",
                    Nynorsk to "".expr(),
                    English to "".expr()
                )
            }
            paragraph {
                textExpr(
                    Bokmal to "Det er dokumentert med vedtak fra NAV at ".expr() + dinForelder
                            + " ikke døde som følge av yrkesskade/yrkessykdom. " +
                            "Det blir derfor ingen endring av barnepensjonen.",
                    Nynorsk to "".expr(),
                    English to "".expr()
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
