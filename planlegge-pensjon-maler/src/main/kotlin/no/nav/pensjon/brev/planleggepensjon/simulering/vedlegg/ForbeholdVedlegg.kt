package no.nav.pensjon.brev.planleggepensjon.simulering.vedlegg

import no.nav.pensjon.brev.planleggepensjon.simulering.ForbeholdAvsnittSelectors.punktliste
import no.nav.pensjon.brev.planleggepensjon.simulering.ForbeholdAvsnittSelectors.tekst
import no.nav.pensjon.brev.planleggepensjon.simulering.ForbeholdInnhold
import no.nav.pensjon.brev.planleggepensjon.simulering.ForbeholdInnholdSelectors.seksjoner
import no.nav.pensjon.brev.planleggepensjon.simulering.ForbeholdSeksjonSelectors.avsnitt
import no.nav.pensjon.brev.planleggepensjon.simulering.ForbeholdSeksjonSelectors.tittel
import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.text

@TemplateModelHelpers
val forbeholdVedlegg = createAttachment<LangBokmal, ForbeholdInnhold>(
    title = {
        text(bokmal { +"Forbehold" })
    },
    includeSakspart = false,
) {
    forEach(seksjoner) { seksjon ->
        ifNotNull(seksjon.tittel) { tittelVerdi ->
            title1 {
                eval(tittelVerdi)
            }
        }
        forEach(seksjon.avsnitt) { avsnittItem ->
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
