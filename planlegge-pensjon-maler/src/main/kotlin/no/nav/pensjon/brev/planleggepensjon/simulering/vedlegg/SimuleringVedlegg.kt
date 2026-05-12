package no.nav.pensjon.brev.planleggepensjon.simulering.vedlegg

import no.nav.pensjon.brev.planleggepensjon.simulering.Simuleringsinformasjon
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringsinformasjonSelectors.gradertUttaksalder
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringsinformasjonSelectors.heltUttaksalder
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringsinformasjonSelectors.maanedligAlderspensjonForKnekkpunkter
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringV1MaanedligAlderspensjonForKnekkpunkterSelectors.vedGradertUttak
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringV1MaanedligAlderspensjonForKnekkpunkterSelectors.vedHeltUttak
import no.nav.pensjon.brev.planleggepensjon.simulering.AlderSelectors.aar
import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.text

@TemplateModelHelpers
val simuleringVedlegg = createAttachment<LangBokmal, Simuleringsinformasjon>(
    title = {
        text(bokmal { +"Detaljer om beregningen" })
    },
    includeSakspart = false,
) {
    ifNotNull(maanedligAlderspensjonForKnekkpunkter) { knekkpunkter ->
        // Show table for vedGradertUttak if defined, otherwise for vedHeltUttak
        ifNotNull(knekkpunkter.vedGradertUttak) { gradertUttak ->
            ifNotNull(gradertUttaksalder) { alder ->
                title1 {
                    text(bokmal { +"Din estimerte månedlige pensjon før skatt ved " + alder.aar.format() + " år" })
                }
            }.orShow {
                title1 {
                    text(bokmal { +"Din estimerte månedlige pensjon før skatt ved gradert uttak" })
                }
            }
            includePhrase(AlderspensjonTabell(gradertUttak))
        }.orShow {
            ifNotNull(heltUttaksalder) { alder ->
                title1 {
                    text(bokmal { +"Din estimerte månedlige pensjon før skatt ved " + alder.aar.format() + " år" })
                }
            }.orShow {
                title1 {
                    text(bokmal { +"Din estimerte månedlige pensjon før skatt ved helt uttak" })
                }
            }
            includePhrase(AlderspensjonTabell(knekkpunkter.vedHeltUttak))
        }
    }
}
