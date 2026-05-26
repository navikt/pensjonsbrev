package no.nav.pensjon.brev.planleggepensjon.simulering.vedlegg

import no.nav.pensjon.brev.planleggepensjon.simulering.AfpOffentligLivsvarigSimuleringSelectors.vedGradertUttak
import no.nav.pensjon.brev.planleggepensjon.simulering.AfpOffentligLivsvarigSimuleringSelectors.vedHeltUttak
import no.nav.pensjon.brev.planleggepensjon.simulering.AfpOffentligTidsbegrensetSimuleringSelectors.vedGradertUttak
import no.nav.pensjon.brev.planleggepensjon.simulering.AfpOffentligTidsbegrensetSimuleringSelectors.vedHeltUttak
import no.nav.pensjon.brev.planleggepensjon.simulering.AfpPrivatSimuleringSelectors.vedGradertUttak
import no.nav.pensjon.brev.planleggepensjon.simulering.AfpPrivatSimuleringSelectors.vedHeltUttak
import no.nav.pensjon.brev.planleggepensjon.simulering.AlderSelectors.aar
import no.nav.pensjon.brev.planleggepensjon.simulering.ApSimuleringDto
import no.nav.pensjon.brev.planleggepensjon.simulering.ApSimuleringDtoSelectors.simulering
import no.nav.pensjon.brev.planleggepensjon.simulering.ApSimuleringDtoSelectors.simuleringsinformasjon
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringSelectors.afpOffentligLivsvarig
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringSelectors.afpOffentligTidsbegrenset
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringSelectors.afpPrivat
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringSelectors.maanedligAlderspensjonForKnekkpunkter
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringV1MaanedligAlderspensjonForKnekkpunkterSelectors.vedGradertUttak
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringV1MaanedligAlderspensjonForKnekkpunkterSelectors.vedHeltUttak
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringsinformasjonSelectors.gradertUttaksalder
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringsinformasjonSelectors.heltUttaksalder
import no.nav.pensjon.brev.planleggepensjon.simulering.tabeller.AfpOffentligLivsvarigTabell
import no.nav.pensjon.brev.planleggepensjon.simulering.tabeller.AfpOffentligTidsbegrensetTabell
import no.nav.pensjon.brev.planleggepensjon.simulering.tabeller.AfpPrivatTabell
import no.nav.pensjon.brev.planleggepensjon.simulering.tabeller.AlderspensjonTabell
import no.nav.pensjon.brev.planleggepensjon.simulering.tabeller.SumOffentligLivsvarigTabell
import no.nav.pensjon.brev.planleggepensjon.simulering.tabeller.SumOffentligTidsbegrensetTabell
import no.nav.pensjon.brev.planleggepensjon.simulering.tabeller.SumTabell
import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.text

@TemplateModelHelpers
val simuleringVedlegg = createAttachment<LangBokmal, ApSimuleringDto>(
    title = {
        text(bokmal { +"Detaljer om beregningen" })
    },
    includeSakspart = false,
) {
    paragraph {
        text(
            bokmal {
                +"Pensjonen er beregnet på grunnlag av de opplysningene vi har om deg, i tillegg til de opplysningene du har oppgitt selv. Dette er derfor en foreløpig beregning av hva du kan forvente deg i pensjon. Pensjonsberegningen er vist i dagens kroneverdi. Beregningen er ikke juridisk bindende."
            },
        )
    }

    ifNotNull(simulering.maanedligAlderspensjonForKnekkpunkter) { knekkpunkter ->
        ifNotNull(knekkpunkter.vedGradertUttak) { gradertUttak ->
            ifNotNull(simuleringsinformasjon.gradertUttaksalder) { alder ->
                title1 {
                    text(bokmal { +"Din estimerte månedlige pensjon før skatt ved " + alder.aar.format() + " år (gradert uttak)" })
                }
            }.orShow {
                title1 {
                    text(bokmal { +"Din estimerte månedlige pensjon før skatt ved gradert uttak" })
                }
            }
            includePhrase(AlderspensjonTabell(gradertUttak))

            ifNotNull(simulering.afpPrivat) { afpPrivatSim ->
                ifNotNull(afpPrivatSim.vedGradertUttak) { afp ->
                    includePhrase(AfpPrivatTabell(afp))
                    includePhrase(SumTabell(gradertUttak, afp))
                }
            }
            ifNotNull(simulering.afpOffentligLivsvarig) { afpLivsvarigSim ->
                ifNotNull(afpLivsvarigSim.vedGradertUttak) { afp ->
                    includePhrase(AfpOffentligLivsvarigTabell(afp))
                    includePhrase(SumOffentligLivsvarigTabell(gradertUttak, afp))
                }
            }
            ifNotNull(simulering.afpOffentligTidsbegrenset) { afpTidsbegrensetSim ->
                ifNotNull(afpTidsbegrensetSim.vedGradertUttak) { afp ->
                    includePhrase(AfpOffentligTidsbegrensetTabell(afp))
                    includePhrase(SumOffentligTidsbegrensetTabell(gradertUttak, afp))
                }
            }
        }

        title1 {
            text(bokmal { +"Din estimerte månedlige pensjon før skatt ved " + simuleringsinformasjon.heltUttaksalder.aar.format() + " år (helt uttak)" })
        }
        includePhrase(AlderspensjonTabell(knekkpunkter.vedHeltUttak))

        ifNotNull(simulering.afpPrivat) { afpPrivatSim ->
            includePhrase(AfpPrivatTabell(afpPrivatSim.vedHeltUttak))
            includePhrase(SumTabell(knekkpunkter.vedHeltUttak, afpPrivatSim.vedHeltUttak))
        }
        ifNotNull(simulering.afpOffentligLivsvarig) { afpLivsvarigSim ->
            includePhrase(AfpOffentligLivsvarigTabell(afpLivsvarigSim.vedHeltUttak))
            includePhrase(SumOffentligLivsvarigTabell(knekkpunkter.vedHeltUttak, afpLivsvarigSim.vedHeltUttak))
        }
        ifNotNull(simulering.afpOffentligTidsbegrenset) { afpTidsbegrensetSim ->
            includePhrase(AfpOffentligTidsbegrensetTabell(afpTidsbegrensetSim.vedHeltUttak))
            includePhrase(SumOffentligTidsbegrensetTabell(knekkpunkter.vedHeltUttak, afpTidsbegrensetSim.vedHeltUttak))
        }
    }
}
