package no.nav.pensjon.brev.planleggepensjon.simulering.vedlegg

import no.nav.pensjon.brev.planleggepensjon.simulering.Simuleringsinformasjon
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringsinformasjonSelectors.gradertUttaksalder
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringsinformasjonSelectors.heltUttaksalder
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringsinformasjonSelectors.maanedligAlderspensjonForKnekkpunkter
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringsinformasjonSelectors.privatAfpVedGradertUttak
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringsinformasjonSelectors.privatAfpVedHeltUttak
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringV1MaanedligAlderspensjonForKnekkpunkterSelectors.vedGradertUttak
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringV1MaanedligAlderspensjonForKnekkpunkterSelectors.vedHeltUttak
import no.nav.pensjon.brev.planleggepensjon.simulering.AlderSelectors.aar
import no.nav.pensjon.brev.planleggepensjon.simulering.tabeller.AfpPrivatTabell
import no.nav.pensjon.brev.planleggepensjon.simulering.tabeller.AlderspensjonTabell
import no.nav.pensjon.brev.planleggepensjon.simulering.tabeller.SumTabell
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
    paragraph {
        text(
            bokmal {
                +"Pensjonen er beregnet på grunnlag av de opplysningene vi har om deg, i tillegg til de opplysningene du har oppgitt selv. Dette er derfor en foreløpig beregning av hva du kan forvente deg i pensjon. Pensjonsberegningen er vist i dagens kroneverdi. Beregningen er ikke juridisk bindende."
            },
        )
    }

    ifNotNull(maanedligAlderspensjonForKnekkpunkter) { knekkpunkter ->
        ifNotNull(knekkpunkter.vedGradertUttak) { gradertUttak ->
            ifNotNull(gradertUttaksalder) { alder ->
                title1 {
                    text(bokmal { +"Din estimerte månedlige pensjon før skatt ved " + alder.aar.format() + " år (gradert uttak)" })
                }
            }.orShow {
                title1 {
                    text(bokmal { +"Din estimerte månedlige pensjon før skatt ved gradert uttak" })
                }
            }
            includePhrase(AlderspensjonTabell(gradertUttak))
            ifNotNull(privatAfpVedGradertUttak) { afp ->
                includePhrase(AfpPrivatTabell(afp))
                includePhrase(SumTabell(gradertUttak, afp))
            }
        }

        ifNotNull(heltUttaksalder) { alder ->
            title1 {
                text(bokmal { +"Din estimerte månedlige pensjon før skatt ved " + alder.aar.format() + " år (helt uttak)" })
            }
        }.orShow {
            title1 {
                text(bokmal { +"Din estimerte månedlige pensjon før skatt ved helt uttak" })
            }
        }
        includePhrase(AlderspensjonTabell(knekkpunkter.vedHeltUttak))
        ifNotNull(privatAfpVedHeltUttak) { afp ->
            includePhrase(AfpPrivatTabell(afp))
            includePhrase(SumTabell(knekkpunkter.vedHeltUttak, afp))
        }
    }
}
