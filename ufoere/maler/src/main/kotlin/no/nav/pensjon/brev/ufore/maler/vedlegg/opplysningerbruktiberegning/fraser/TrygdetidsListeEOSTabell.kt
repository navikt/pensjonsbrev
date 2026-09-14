package no.nav.pensjon.brev.ufore.maler.vedlegg.opplysningerbruktiberegning.fraser

import no.nav.pensjon.brev.ufore.api.model.vedlegg.OpplysningerBruktIBeregningUTLegacyDto.Trygdetid.PeriodeMedLand
import no.nav.pensjon.brev.ufore.api.model.vedlegg.selectors.opplysningerBruktIBeregningUTLegacyDto.trygdetid.periodeMedLand.*
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.text

data class TrygdetidsListeEOSTabell(val perioder: Expression<List<PeriodeMedLand>>) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        paragraph {
            table(
                header = {
                    column {
                        text(bokmal { + "Land" }, nynorsk { + "Land" })
                    }
                    column {
                        text(bokmal { + "Fra og med" }, nynorsk { + "Frå og med" })
                    }
                    column {
                        text(bokmal { + "Til og med" }, nynorsk { + "Til og med" })
                    }
                }
            ) {
                forEach(perioder) { periode ->
                    row {
                        cell {
                            text(
                                bokmal { + periode.land.ifNull("") },
                                nynorsk { + periode.land.ifNull("") },
                            )
                        }
                        cell {
                            ifNotNull(periode.fom) {
                                text(bokmal { + it.format() }, nynorsk { + it.format() })
                            }
                        }
                        cell {
                            ifNotNull(periode.tom) {
                                text(bokmal { + it.format() }, nynorsk { + it.format() })
                            }
                        }
                    }
                }
            }
        }
    }
}
