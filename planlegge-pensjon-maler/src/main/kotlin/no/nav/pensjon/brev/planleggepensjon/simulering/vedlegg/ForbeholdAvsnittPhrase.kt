package no.nav.pensjon.brev.planleggepensjon.simulering.vedlegg

import no.nav.pensjon.brev.planleggepensjon.simulering.ForbeholdAvsnitt
import no.nav.pensjon.brev.planleggepensjon.simulering.selectors.forbeholdAvsnitt.punktliste
import no.nav.pensjon.brev.planleggepensjon.simulering.selectors.forbeholdAvsnitt.tekst
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope

data class ForbeholdAvsnittPhrase(
    val avsnitt: Expression<List<ForbeholdAvsnitt>>,
) : OutlinePhrase<LangBokmal>() {
    override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
        forEach(avsnitt) { avsnittItem ->
            paragraph {
                eval(avsnittItem.tekst)
                ifNotNull(avsnittItem.punktliste) { punkter ->
                    list {
                        forEach(punkter) { punkt ->
                            item {
                                eval(punkt)
                            }
                        }
                    }
                }
            }
        }
    }
}
