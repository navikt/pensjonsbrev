package no.nav.pensjon.etterlatte.maler.fraser.barnepensjon

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.etterlatte.maler.BarnepensjonSoeskenjusteringGrunn
import java.time.LocalDate

object Soeskenjustering {
    data class RevurderingSoeskenjusteringBehandlingsvedtak(
        val virkningsdato: Expression<LocalDate>,
        val soeskenjusteringType: Expression<BarnepensjonSoeskenjusteringGrunn>,
        val beloep: Expression<Kroner>,
    ) : OutlinePhrase<LangBokmal>() {
        private val formatertVirkningsdato = virkningsdato.format()

        override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
            paragraph {
                showIf(soeskenjusteringType.isOneOf(BarnepensjonSoeskenjusteringGrunn.NYTT_SOESKEN)) {
                    textExpr(
                        Language.Bokmal to "Barnepensjonen din reduseres fra ".expr() + formatertVirkningsdato +
                            " fordi du har et søsken som er innvilget barnepensjon. Søsken skal tas med i " +
                            "beregningen av pensjonen din. Du får " + beloep.format() + " kroner hver måned før skatt.",
                    )
                }

                showIf(soeskenjusteringType.isOneOf(BarnepensjonSoeskenjusteringGrunn.SOESKEN_DOER)) {
                    textExpr(
                        Language.Bokmal to "Barnepensjonen din reduseres fra ".expr() + formatertVirkningsdato +
                            " fordi du har et søsken som er innvilget barnepensjon. Søsken skal tas med i beregningen" +
                            " av pensjonen din. Du får " + beloep.format() + " kroner hver måned før skatt.",
                    )
                }

                showIf(soeskenjusteringType.isOneOf(BarnepensjonSoeskenjusteringGrunn.SOESKEN_INN_INSTITUSJON_ENDRING)) {
                    textExpr(
                        Language.Bokmal to "Barnepensjonen din økes fra ".expr() + formatertVirkningsdato +
                            " fordi et søsken har fått opphold i institusjon og skal ikke lenger være med i " +
                            "beregningen. Du får " + beloep.format() + " kroner hver måned før skatt.",
                    )
                }

                showIf(soeskenjusteringType.isOneOf(BarnepensjonSoeskenjusteringGrunn.SOESKEN_INN_INSTITUSJON_INGEN_ENDRING)) {
                    textExpr(
                        Language.Bokmal to "Barnepensjonen din er vurdert fra ".expr() + formatertVirkningsdato +
                            " fordi et søsken har fått opphold i institusjon. Institusjonsoppholdet fører ikke til" +
                            " endring i pensjonen din. Du får fortsatt " + beloep.format() + " kroner hver måned før skatt.",
                    )
                }

                showIf(soeskenjusteringType.isOneOf(BarnepensjonSoeskenjusteringGrunn.FORPLEID_ETTER_BARNEVERNSLOVEN)) {
                    textExpr(
                        Language.Bokmal to "Barnepensjonen din økes fra ".expr() + formatertVirkningsdato +
                            " fordi søsken blir forpleid etter barnevernsloven. Søsken skal ikke tas med i " +
                            "beregningen av pensjonen. Du får " + beloep.format() + " kroner hver måned før skatt.",
                    )
                }

                showIf(soeskenjusteringType.isOneOf(BarnepensjonSoeskenjusteringGrunn.SOESKEN_UT_INSTITUSJON)) {
                    textExpr(
                        Language.Bokmal to "Barnepensjonen din reduseres fra ".expr() + formatertVirkningsdato +
                            " fordi et søsken er ute av et institusjonsopphold, og skal nå " +
                            "være med i beregningen. Du får " + beloep.format() + " kroner hver måned før skatt.",
                    )
                }

                showIf(soeskenjusteringType.isOneOf(BarnepensjonSoeskenjusteringGrunn.SOESKEN_BLIR_ADOPTERT)) {
                    textExpr(
                        Language.Bokmal to "Barnepensjonen din økes fra ".expr() + formatertVirkningsdato +
                            " fordi du har et søsken som er blitt adoptert. Søsken skal derfor ikke tas med i" +
                            " beregningen av pensjonen. Du får " + beloep.format() + " kroner hver måned før skatt.",
                    )
                }
            }

            paragraph {
                text(
                    Language.Bokmal to "Barnepensjonen din utbetales til og med den kalendermåneden du fyller " +
                        "18 år. Vedtaket er gjort etter folketrygdloven kapittel 18 og 22.",
                )
            }
        }
    }
}
