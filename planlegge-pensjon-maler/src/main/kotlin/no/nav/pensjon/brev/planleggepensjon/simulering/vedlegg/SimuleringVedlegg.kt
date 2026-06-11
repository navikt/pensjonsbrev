package no.nav.pensjon.brev.planleggepensjon.simulering.vedlegg

import no.nav.brev.InternKonstruktoer
import no.nav.pensjon.brev.planleggepensjon.simulering.AfpOffentligLivsvarigSimuleringSelectors.vedGradertUttak
import no.nav.pensjon.brev.planleggepensjon.simulering.AfpOffentligLivsvarigSimuleringSelectors.vedHeltUttak
import no.nav.pensjon.brev.planleggepensjon.simulering.AfpPrivatSimuleringSelectors.vedGradertUttak
import no.nav.pensjon.brev.planleggepensjon.simulering.AfpPrivatSimuleringSelectors.vedHeltUttak
import no.nav.pensjon.brev.planleggepensjon.simulering.AfpPrivatSimuleringSelectors.vedNormertPensjonsalder
import no.nav.pensjon.brev.planleggepensjon.simulering.AlderSelectors.aar
import no.nav.pensjon.brev.planleggepensjon.simulering.AlderSelectors.maaneder
import no.nav.pensjon.brev.planleggepensjon.simulering.ApSimuleringDto
import no.nav.pensjon.brev.planleggepensjon.simulering.ApSimuleringDtoSelectors.aarligInntektOgPensjonListe
import no.nav.pensjon.brev.planleggepensjon.simulering.ApSimuleringDtoSelectors.forbehold
import no.nav.pensjon.brev.planleggepensjon.simulering.ApSimuleringDtoSelectors.simulering
import no.nav.pensjon.brev.planleggepensjon.simulering.ApSimuleringDtoSelectors.simuleringsinformasjon
import no.nav.pensjon.brev.planleggepensjon.simulering.ForbeholdAvsnittSelectors.punktliste
import no.nav.pensjon.brev.planleggepensjon.simulering.ForbeholdAvsnittSelectors.tekst
import no.nav.pensjon.brev.planleggepensjon.simulering.ForbeholdInnholdSelectors.seksjoner
import no.nav.pensjon.brev.planleggepensjon.simulering.ForbeholdSeksjonSelectors.avsnitt
import no.nav.pensjon.brev.planleggepensjon.simulering.ForbeholdSeksjonSelectors.tittel
import no.nav.pensjon.brev.planleggepensjon.simulering.Kull
import no.nav.pensjon.brev.planleggepensjon.simulering.NormertPensjonsalderPlassering
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringSelectors.afpOffentligLivsvarig
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringSelectors.afpOffentligTidsbegrenset
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringSelectors.afpPrivat
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringSelectors.maanedligAlderspensjonForKnekkpunkter
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringUtenlandsperiodeSelectors.arbeidetUtenlands
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringUtenlandsperiodeSelectors.fom
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringUtenlandsperiodeSelectors.landkode
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringUtenlandsperiodeSelectors.tom
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringV1MaanedligAlderspensjonForKnekkpunkterSelectors.vedGradertUttak
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringV1MaanedligAlderspensjonForKnekkpunkterSelectors.vedHeltUttak
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringV1MaanedligAlderspensjonForKnekkpunkterSelectors.vedNormertPensjonsalder
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringsinformasjonSelectors.gradertUttakInformasjon
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringsinformasjonSelectors.heltUttakInformasjon
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringsinformasjonSelectors.kull
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringsinformasjonSelectors.normertPensjonsalderPlassering
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringsinformasjonSelectors.normertUttakInformasjon
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringsinformasjonSelectors.sivilstatus
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringsinformasjonSelectors.utenlandsperioder
import no.nav.pensjon.brev.planleggepensjon.simulering.Sivilstatus
import no.nav.pensjon.brev.planleggepensjon.simulering.UttaksinformasjonSelectors.alder
import no.nav.pensjon.brev.planleggepensjon.simulering.UttaksinformasjonSelectors.uttaksdato
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
        text(bokmal { +"Detaljer om beregningen" })
    },
    includeSakspart = false,
) {
    paragraph {
        text(
            bokmal {
                +"Pensjonen er beregnet med opplysninger Nav har om deg og opplysninger du har gitt på beregningstidspunktet. Dette er et foreløpig estimat. Det er ikke et vedtak og gir ikke rett til pensjon. Beregningen er vist i dagens kroneverdi før skatt. Den er gjort etter gjeldende regelverk og satser."
            },
        )
    }
    paragraph {
        text(
            bokmal {
                +"Endringer i opplysninger, opptjening eller regelverk kan påvirke resultatet. Det kan også påvirke når du tidligst kan starte uttak av alderspensjon. Vi anbefaler å gjøre en ny beregning når du nærmer deg tidspunktet for uttak av pensjon."
            }
        )
    }

    ifNotNull(simulering.afpOffentligTidsbegrenset) { afp ->
        ifNotNull(simuleringsinformasjon.gradertUttakInformasjon) { informasjon ->
            title2 {
                text(bokmal { +"Månedlig pensjon før skatt ved " + informasjon.alder.aar.format() + " år" })
                showIf(informasjon.alder.maaneder greaterThan 1) {
                    text(bokmal { +" og " + informasjon.alder.maaneder.format() + " måneder" })
                }.orShowIf(informasjon.alder.maaneder greaterThan 0) {
                    text(bokmal { +" og 1 måned" })
                }
                text(bokmal { +" (" + informasjon.uttaksdato + ")" })
            }
        }.orShow {
            title2 {
                text(bokmal { +"Månedlig pensjon før skatt ved gradert uttak" })
            }

        }
        includePhrase(AfpOffentligTidsbegrensetTabell(afp))
    }

    ifNotNull(simulering.maanedligAlderspensjonForKnekkpunkter) { knekkpunkter ->
        ifNotNull(knekkpunkter.vedGradertUttak) { gradertUttak ->
            ifNotNull(simuleringsinformasjon.gradertUttakInformasjon) { informasjon ->
                title2 {
                    text(bokmal { +"Månedlig pensjon før skatt ved " + informasjon.alder.aar.format() + " år" })
                    showIf(informasjon.alder.maaneder greaterThan 1) {
                        text(bokmal { +" og " + informasjon.alder.maaneder.format() + " måneder" })
                    }.orShowIf(informasjon.alder.maaneder greaterThan 0) {
                        text(bokmal { +" og 1 måned" })
                    }
                    text(bokmal { +" (" + informasjon.uttaksdato + ")" })
                }
            }.orShow {
                title2 {
                    text(bokmal { +"Månedlig pensjon før skatt ved gradert uttak" })
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
        }

        ifNotNull(knekkpunkter.vedNormertPensjonsalder) { normPensjonsalder ->
            ifNotNull(simuleringsinformasjon.normertPensjonsalderPlassering) { plassering ->
                ifNotNull(simuleringsinformasjon.normertUttakInformasjon) { informasjon ->
                    showIf(plassering.isOneOf(NormertPensjonsalderPlassering.MELLOM_GRADERT_OG_HELT)) {
                        title2 {
                            text(bokmal { +"Månedlig pensjon før skatt ved 67 år (" + informasjon.uttaksdato + ")" })
                        }

                        includePhrase(AlderspensjonTabell(normPensjonsalder))

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
            text(bokmal { +"Månedlig pensjon før skatt ved " + simuleringsinformasjon.heltUttakInformasjon.alder.aar.format() + " år" })
            showIf(simuleringsinformasjon.heltUttakInformasjon.alder.maaneder greaterThan 1) {
                text(bokmal { +" og " + simuleringsinformasjon.heltUttakInformasjon.alder.maaneder.format() + " måneder" })
            }.orShowIf(simuleringsinformasjon.heltUttakInformasjon.alder.maaneder greaterThan 0) {
                text(bokmal { +" og 1 måned" })
            }
            text(bokmal { +" (" + simuleringsinformasjon.heltUttakInformasjon.uttaksdato + ")" })
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

        ifNotNull(simuleringsinformasjon.normertPensjonsalderPlassering) { plassering ->
            ifNotNull(simuleringsinformasjon.normertUttakInformasjon) { informasjon ->
                showIf(plassering.isOneOf(NormertPensjonsalderPlassering.ETTER_HELT)) {
                    title2 {
                        text(bokmal { +"Månedlig pensjon før skatt ved 67 år (" + informasjon.uttaksdato + ")" })
                    }
                    ifNotNull(knekkpunkter.vedNormertPensjonsalder) { normPensjonsalder ->
                        includePhrase(AlderspensjonTabell(normPensjonsalder))
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
    ifNotNull(aarligInntektOgPensjonListe) {
        title2 {
            text(bokmal { +"Årlig inntekt og pensjon" })
        }
        includePhrase(AarligInntektOgPensjonTabell(it))
    }
    ifNotNull(simulering.maanedligAlderspensjonForKnekkpunkter) { knekkpunkter ->
        title2 {
            text(bokmal { +"Ditt opptjeningsgrunnlag i folketrygden" })
        }
        ifNotNull(knekkpunkter.vedGradertUttak) { alderspensjon ->
            ifNotNull(simuleringsinformasjon.gradertUttakInformasjon) { informasjon ->
                title2 {
                    text(bokmal { +"Ved " + informasjon.alder.aar.format() + " år" })
                    showIf(informasjon.alder.maaneder greaterThan 1) {
                        text(bokmal { +" og " + informasjon.alder.maaneder.format() + " måneder" })
                    }.orShowIf(informasjon.alder.maaneder greaterThan 0) {
                        text(bokmal { +" og 1 måned" })
                    }
                    text(bokmal { +" (" + informasjon.uttaksdato + ")" })
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
                    title2 {
                        text(bokmal { +"Ved " + informasjon.alder.aar.format() + " år" })
                        showIf(informasjon.alder.maaneder greaterThan 1) {
                            text(bokmal { +" og " + informasjon.alder.maaneder.format() + " måneder" })
                        }.orShowIf(informasjon.alder.maaneder greaterThan 0) {
                            text(bokmal { +" og 1 måned" })
                        }
                        text(bokmal { +" (" + informasjon.uttaksdato + ")" })
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
            title2 {
                text(bokmal { +"Ved " + informasjon.alder.aar.format() + " år" })
                showIf(informasjon.alder.maaneder greaterThan 1) {
                    text(bokmal { +" og " + informasjon.alder.maaneder.format() + " måneder" })
                }.orShowIf(informasjon.alder.maaneder greaterThan 0) {
                    text(bokmal { +" og 1 måned" })
                }
                text(bokmal { +" (" + informasjon.uttaksdato + ")" })
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
                    title2 {
                        text(bokmal { +"Ved " + informasjon.alder.aar.format() + " år" })
                        showIf(informasjon.alder.maaneder greaterThan 1) {
                            text(bokmal { +" og " + informasjon.alder.maaneder.format() + " måneder" })
                        }.orShowIf(informasjon.alder.maaneder greaterThan 0) {
                            text(bokmal { +" og 1 måned" })
                        }
                        text(bokmal { +" (" + informasjon.uttaksdato + ")" })
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

    title1 {
        text(bokmal { +"Opplysninger brukt i beregningen" })
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

    title2 {
        text(bokmal { +"Forbehold" })
    }

    ifNotNull(forbehold.seksjoner) { seksjonerVerdi ->
        forEach(seksjonerVerdi) { seksjon ->
            ifNotNull(seksjon.tittel) { tittelVerdi ->
                title2 {
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

}
