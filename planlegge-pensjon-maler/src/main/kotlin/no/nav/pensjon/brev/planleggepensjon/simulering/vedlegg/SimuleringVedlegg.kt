package no.nav.pensjon.brev.planleggepensjon.simulering.vedlegg

import no.nav.brev.InternKonstruktoer
import no.nav.pensjon.brev.planleggepensjon.simulering.ApSimuleringDto
import no.nav.pensjon.brev.planleggepensjon.simulering.Kull
import no.nav.pensjon.brev.planleggepensjon.simulering.NormertPensjonsalderPlassering
import no.nav.pensjon.brev.planleggepensjon.simulering.Sivilstatus
import no.nav.pensjon.brev.planleggepensjon.simulering.selectors.afpOffentligLivsvarigSimulering.vedGradertUttak
import no.nav.pensjon.brev.planleggepensjon.simulering.selectors.afpOffentligLivsvarigSimulering.vedHeltUttak
import no.nav.pensjon.brev.planleggepensjon.simulering.selectors.afpPrivatSimulering.vedGradertUttak
import no.nav.pensjon.brev.planleggepensjon.simulering.selectors.afpPrivatSimulering.vedHeltUttak
import no.nav.pensjon.brev.planleggepensjon.simulering.selectors.afpPrivatSimulering.vedNormertPensjonsalder
import no.nav.pensjon.brev.planleggepensjon.simulering.selectors.apSimuleringDto.*
import no.nav.pensjon.brev.planleggepensjon.simulering.selectors.forbeholdInnhold.seksjoner
import no.nav.pensjon.brev.planleggepensjon.simulering.selectors.forbeholdSeksjon.avsnitt
import no.nav.pensjon.brev.planleggepensjon.simulering.selectors.forbeholdSeksjon.tittel
import no.nav.pensjon.brev.planleggepensjon.simulering.selectors.kortforbehold.avsnitt
import no.nav.pensjon.brev.planleggepensjon.simulering.selectors.simulering.afpOffentligLivsvarig
import no.nav.pensjon.brev.planleggepensjon.simulering.selectors.simulering.afpOffentligTidsbegrenset
import no.nav.pensjon.brev.planleggepensjon.simulering.selectors.simulering.afpPrivat
import no.nav.pensjon.brev.planleggepensjon.simulering.selectors.simulering.maanedligAlderspensjonForKnekkpunkter
import no.nav.pensjon.brev.planleggepensjon.simulering.selectors.simuleringUtenlandsperiode.arbeidetUtenlands
import no.nav.pensjon.brev.planleggepensjon.simulering.selectors.simuleringUtenlandsperiode.fom
import no.nav.pensjon.brev.planleggepensjon.simulering.selectors.simuleringUtenlandsperiode.landkode
import no.nav.pensjon.brev.planleggepensjon.simulering.selectors.simuleringUtenlandsperiode.tom
import no.nav.pensjon.brev.planleggepensjon.simulering.selectors.simuleringV1MaanedligAlderspensjonForKnekkpunkter.vedGradertUttak
import no.nav.pensjon.brev.planleggepensjon.simulering.selectors.simuleringV1MaanedligAlderspensjonForKnekkpunkter.vedHeltUttak
import no.nav.pensjon.brev.planleggepensjon.simulering.selectors.simuleringV1MaanedligAlderspensjonForKnekkpunkter.vedNormertPensjonsalder
import no.nav.pensjon.brev.planleggepensjon.simulering.selectors.simuleringsinformasjon.*
import no.nav.pensjon.brev.planleggepensjon.simulering.selectors.uttaksinformasjon.grad
import no.nav.pensjon.brev.planleggepensjon.simulering.selectors.uttaksinformasjon.uttaksdato
import no.nav.pensjon.brev.planleggepensjon.simulering.tabeller.*
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.SimpleSelector
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.text

@OptIn(InternKonstruktoer::class)
private val sivilstatusValueSelector = SimpleSelector<Sivilstatus, String>(
    className = "no.nav.pensjon.brev.planleggepensjon.simulering.Sivilstatus",
    propertyName = "value",
    propertyType = "String",
    selector = Sivilstatus::value
)

private val Expression<Sivilstatus>.value: Expression<String>
    get() = select(sivilstatusValueSelector)

@TemplateModelHelpers
val simuleringVedlegg = createAttachment<LangBokmal, ApSimuleringDto>(
    title = {
        text(bokmal { +"Pensjonsberegningen din med detaljer og forbehold" })
    },
    includeSakspart = false,
) {
    ifNotNull(kortforbehold) { kortforbeholdVerdi ->
        includePhrase(ForbeholdAvsnittPhrase(kortforbeholdVerdi.avsnitt))
    }

    ifNotNull(aarligInntektOgPensjonListe) {
        title1 {
            text(bokmal { +"Årlig inntekt og pensjon før skatt" })
        }
        paragraph {
            text(
                bokmal {
                    +"Eventuell tilvekst av alderspensjon er inkludert i beløpene"
                }
            )
        }
        includePhrase(AarligInntektOgPensjonTabell(it))
    }
    title1 {
        text(bokmal { +"Månedlig pensjon før skatt" })
    }
    ifNotNull(simulering.afpOffentligTidsbegrenset) { afp ->
        ifNotNull(simuleringsinformasjon.gradertUttakInformasjon) { informasjon ->
            title2 {
                includePhrase(VedAlderPhrase(informasjon))
            }
        }.orShow {
            title2 {
                text(bokmal { +"Ved gradert uttak" })
            }

        }
        includePhrase(AfpOffentligTidsbegrensetTabell(afp))
    }

    showIf(simuleringsinformasjon.simulererEndringMedAfpPrivat) {
        ifNotNull(simulering.afpPrivat) { afp ->
            ifNotNull(afp.vedGradertUttak) { gradertUttak ->
                ifNotNull(simuleringsinformasjon.gradertUttakInformasjon) { informasjon ->
                    title2 {
                        includePhrase(VedAlderPhrase(informasjon))
                    }
                }.orShow {
                    title2 {
                        text(bokmal { +"Ved gradert uttak" })
                    }
                }
                includePhrase(AfpPrivatTabell(gradertUttak))
            }
            ifNotNull(afp.vedNormertPensjonsalder) { normertPensjonsalder ->
                ifNotNull(simuleringsinformasjon.normertPensjonsalderPlassering) { plassering ->
                    showIf(plassering.isOneOf(NormertPensjonsalderPlassering.MELLOM_GRADERT_OG_HELT)) {
                        ifNotNull(simuleringsinformasjon.normertUttakInformasjon) { informasjon ->
                            title2 {
                                includePhrase(VedAlderPhrase(informasjon))
                            }
                        }.orShow {
                            title2 {
                                text(bokmal { +"Ved 67 år" })
                            }
                        }
                        includePhrase(AfpPrivatTabell(normertPensjonsalder))
                    }
                }
            }
        }
    }

    ifNotNull(simulering.maanedligAlderspensjonForKnekkpunkter) { knekkpunkter ->
        ifNotNull(knekkpunkter.vedGradertUttak) { gradertUttak ->
            ifNotNull(simuleringsinformasjon.gradertUttakInformasjon) { informasjon ->
                title2 {
                    includePhrase(VedAlderPhrase(informasjon))
                }
                includePhrase(AlderspensjonTabell(gradertUttak, informasjon.grad))
            }.orShow {
                title2 {
                    text(bokmal { +"Ved gradert uttak" })
                }
            }

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
        }


        ifNotNull(knekkpunkter.vedNormertPensjonsalder) { normPensjonsalder ->
            ifNotNull(simuleringsinformasjon.normertPensjonsalderPlassering) { plassering ->
                ifNotNull(simuleringsinformasjon.normertUttakInformasjon) { informasjon ->
                    showIf(plassering.isOneOf(NormertPensjonsalderPlassering.MELLOM_GRADERT_OG_HELT)) {
                        title2 {
                            text(bokmal { +"Ved 67 år (" + informasjon.uttaksdato + ")" })
                        }

                        includePhrase(AlderspensjonTabell(normPensjonsalder, informasjon.grad))

                        ifNotNull(simulering.afpPrivat) { afpPrivatSim ->
                            ifNotNull(afpPrivatSim.vedNormertPensjonsalder) { afp ->
                                includePhrase(AfpPrivatTabell(afp))
                                ifNotNull(knekkpunkter.vedNormertPensjonsalder) { normPensjonsalder ->
                                    includePhrase(SumTabell(normPensjonsalder, afp))
                                }
                            }
                        }
                    }
                }
            }
        }

        title2 {
            includePhrase(VedAlderPhrase(simuleringsinformasjon.heltUttakInformasjon))
        }
        includePhrase(AlderspensjonTabell(knekkpunkter.vedHeltUttak, simuleringsinformasjon.heltUttakInformasjon.grad))

        ifNotNull(simulering.afpPrivat) { afpPrivatSim ->
            includePhrase(AfpPrivatTabell(afpPrivatSim.vedHeltUttak))
            includePhrase(SumTabell(knekkpunkter.vedHeltUttak, afpPrivatSim.vedHeltUttak))
        }
        ifNotNull(simulering.afpOffentligLivsvarig) { afpLivsvarigSim ->
            includePhrase(AfpOffentligLivsvarigTabell(afpLivsvarigSim.vedHeltUttak))
            includePhrase(SumOffentligLivsvarigTabell(knekkpunkter.vedHeltUttak, afpLivsvarigSim.vedHeltUttak))
        }

        ifNotNull(simuleringsinformasjon.normertPensjonsalderPlassering) { plassering ->
            ifNotNull(simuleringsinformasjon.normertUttakInformasjon) { informasjon ->
                showIf(plassering.isOneOf(NormertPensjonsalderPlassering.ETTER_HELT)) {
                    title2 {
                        text(bokmal { +"Ved 67 år (" + informasjon.uttaksdato + ")" })
                    }
                    ifNotNull(knekkpunkter.vedNormertPensjonsalder) { normPensjonsalder ->
                        includePhrase(AlderspensjonTabell(normPensjonsalder, informasjon.grad))
                        ifNotNull(simulering.afpPrivat) { afpPrivatSim ->
                            ifNotNull(afpPrivatSim.vedNormertPensjonsalder) { afp ->
                                includePhrase(AfpPrivatTabell(afp))
                                includePhrase(SumTabell(normPensjonsalder, afp))
                            }
                        }
                    }
                }
            }
        }
    }
    title1 {
        text(bokmal { +"Opplysninger brukt i pensjonsberegningen" })
    }

    ifNotNull(simuleringsinformasjon.utenlandsperioder) { utenlandsperioder ->
        title2 {
            text(bokmal { +"Opphold utenfor Norge" })
        }
        paragraph {
            table(header = {
                column {
                    text(bokmal { +"Land" })
                }
                column {
                    text(bokmal { +"Periode" })
                }
                column {
                    text(bokmal { +"Jobbet" })
                }
            }) {
                forEach(utenlandsperioder) { periode ->
                    row {
                        cell {
                            text(bokmal { +periode.landkode })
                        }
                        cell {
                            ifNotNull(periode.tom) { tomDato ->
                                text(bokmal { +periode.fom.format(short = true) + "–" + tomDato.format(short = true) })
                            }.orShow {
                                text(bokmal { +periode.fom.format(short = true) + " (Varig opphold)" })
                            }
                        }
                        cell {
                            ifNotNull(periode.arbeidetUtenlands) { arbeidet ->
                                text(bokmal { +ifElse(arbeidet, "Ja", "Nei") })
                            }
                        }
                    }
                }
            }
        }
    }

    title2 {
        text(bokmal { +"Sivilstatus: " + simuleringsinformasjon.sivilstatus.value })
    }
    paragraph {
        text(
            bokmal {
                +"Hvis du bor sammen med noen kan inntekten til den du bor med ha betydning for hva du får i alderspensjon. Når du mottar alderspensjon må du derfor melde fra til Nav ved endring i sivilstand."
            },
        )
    }
    ifNotNull(simulering.maanedligAlderspensjonForKnekkpunkter) { knekkpunkter ->
        title2 {
            text(bokmal { +"Ditt opptjeningsgrunnlag i folketrygden" })
        }
        ifNotNull(simulering.afpOffentligTidsbegrenset) { afp ->
            ifNotNull(simuleringsinformasjon.gradertUttakInformasjon) { informasjon ->
                title3 {
                    includePhrase(VedAlderPhrase(informasjon))
                }
                includePhrase(OpptjeningTidsbegrensetAFPTabell(afp))
            }
        }

        ifNotNull(knekkpunkter.vedGradertUttak) { alderspensjon ->
            ifNotNull(simuleringsinformasjon.gradertUttakInformasjon) { informasjon ->
                title3 {
                    includePhrase(VedAlderPhrase(informasjon))
                }
                showIf(simuleringsinformasjon.kull.isOneOf(Kull.KAP19, Kull.OVERGANG)) {
                    includePhrase(OpptjeningKapittel19Tabell(alderspensjon))
                }
                showIf(simuleringsinformasjon.kull.isOneOf(Kull.KAP20, Kull.OVERGANG)) {
                    includePhrase(OpptjeningKapittel20Tabell(alderspensjon))
                }
            }
        }
        ifNotNull(knekkpunkter.vedNormertPensjonsalder) { normPensjonsalder ->
            showIf(simuleringsinformasjon.normertPensjonsalderPlassering.equalTo(NormertPensjonsalderPlassering.MELLOM_GRADERT_OG_HELT)) {
                ifNotNull(simuleringsinformasjon.normertUttakInformasjon) { informasjon ->
                    title3 {
                        includePhrase(VedAlderPhrase(informasjon))
                    }
                }
                showIf(simuleringsinformasjon.kull.isOneOf(Kull.KAP19, Kull.OVERGANG)) {
                    includePhrase(OpptjeningKapittel19Tabell(normPensjonsalder))
                }
                showIf(simuleringsinformasjon.kull.isOneOf(Kull.KAP20, Kull.OVERGANG)) {
                    includePhrase(OpptjeningKapittel20Tabell(normPensjonsalder))
                }
            }
        }

        ifNotNull(simuleringsinformasjon.heltUttakInformasjon) { informasjon ->
            title3 {
                includePhrase(VedAlderPhrase(informasjon))
            }
        }
        showIf(simuleringsinformasjon.kull.isOneOf(Kull.KAP19, Kull.OVERGANG)) {
            includePhrase(OpptjeningKapittel19Tabell(knekkpunkter.vedHeltUttak))
        }
        showIf(simuleringsinformasjon.kull.isOneOf(Kull.KAP20, Kull.OVERGANG)) {
            includePhrase(OpptjeningKapittel20Tabell(knekkpunkter.vedHeltUttak))
        }
        ifNotNull(knekkpunkter.vedNormertPensjonsalder) { normPensjonsalder ->
            showIf(simuleringsinformasjon.normertPensjonsalderPlassering.equalTo(NormertPensjonsalderPlassering.ETTER_HELT)) {
                ifNotNull(simuleringsinformasjon.normertUttakInformasjon) { informasjon ->
                    title3 {
                        includePhrase(VedAlderPhrase(informasjon))
                    }
                }
                showIf(simuleringsinformasjon.kull.isOneOf(Kull.KAP19, Kull.OVERGANG)) {
                    includePhrase(OpptjeningKapittel19Tabell(normPensjonsalder))
                }
                showIf(simuleringsinformasjon.kull.isOneOf(Kull.KAP20, Kull.OVERGANG)) {
                    includePhrase(OpptjeningKapittel20Tabell(normPensjonsalder))
                }
            }
        }
    }



    ifNotNull(pensjonsopptjeningListe) {
        title2 {
            text(bokmal { +"Pensjonsgivende inntekt og pensjonsopptjening" })
        }
        includePhrase(PensjonsopptjeningTabell(it))
    }

    title1 {
        text(bokmal { +"Forbehold" })
    }

    ifNotNull(forbehold.seksjoner) { seksjonerVerdi ->
        forEach(seksjonerVerdi) { seksjon ->
            ifNotNull(seksjon.tittel) { tittelVerdi ->
                title2 {
                    eval(tittelVerdi)
                }
            }
            includePhrase(ForbeholdAvsnittPhrase(seksjon.avsnitt))
        }
    }

}
