package no.nav.pensjon.brev.planleggepensjon.simulering.vedlegg

import no.nav.brev.InternKonstruktoer
import no.nav.pensjon.brev.planleggepensjon.simulering.AfpOffentligLivsvarigSimuleringSelectors.vedGradertUttak
import no.nav.pensjon.brev.planleggepensjon.simulering.AfpOffentligLivsvarigSimuleringSelectors.vedHeltUttak
import no.nav.pensjon.brev.planleggepensjon.simulering.AfpPrivatSimuleringSelectors.vedGradertUttak
import no.nav.pensjon.brev.planleggepensjon.simulering.AfpPrivatSimuleringSelectors.vedHeltUttak
import no.nav.pensjon.brev.planleggepensjon.simulering.AfpPrivatSimuleringSelectors.vedNormertPensjonsalder
import no.nav.pensjon.brev.planleggepensjon.simulering.AlderSelectors.aar
import no.nav.pensjon.brev.planleggepensjon.simulering.ApSimuleringDto
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
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringsinformasjonSelectors.gradertUttaksalder
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringsinformasjonSelectors.heltUttaksalder
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringsinformasjonSelectors.kull
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringsinformasjonSelectors.normertPensjonsalderPlassering
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringsinformasjonSelectors.sivilstatus
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringsinformasjonSelectors.utenlandsperioder
import no.nav.pensjon.brev.planleggepensjon.simulering.Sivilstatus
import no.nav.pensjon.brev.planleggepensjon.simulering.tabeller.AfpOffentligLivsvarigTabell
import no.nav.pensjon.brev.planleggepensjon.simulering.tabeller.AfpOffentligTidsbegrensetTabell
import no.nav.pensjon.brev.planleggepensjon.simulering.tabeller.AfpPrivatTabell
import no.nav.pensjon.brev.planleggepensjon.simulering.tabeller.AlderspensjonTabell
import no.nav.pensjon.brev.planleggepensjon.simulering.tabeller.OpptjeningKapittel19Tabell
import no.nav.pensjon.brev.planleggepensjon.simulering.tabeller.OpptjeningKapittel20Tabell
import no.nav.pensjon.brev.planleggepensjon.simulering.tabeller.SumOffentligLivsvarigTabell
import no.nav.pensjon.brev.planleggepensjon.simulering.tabeller.SumOffentligTidsbegrensetTabell
import no.nav.pensjon.brev.planleggepensjon.simulering.tabeller.SumTabell
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.SimpleSelector
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.ifElse
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.expression.select
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
                +"Pensjonen er beregnet på grunnlag av de opplysningene vi har om deg, i tillegg til de opplysningene du har oppgitt selv. Dette er derfor en foreløpig beregning av hva du kan forvente deg i pensjon. Pensjonsberegningen er vist i dagens kroneverdi. Beregningen er ikke juridisk bindende."
            },
        )
    }

    ifNotNull(simulering.afpOffentligTidsbegrenset) { afp ->
        ifNotNull(simuleringsinformasjon.gradertUttaksalder) { alder ->
            title1 {
                text(bokmal { +"Din estimerte månedlige pensjon før skatt ved " + alder.aar.format() + " år" })
            }
        }.orShow {
            title1 {
                text(bokmal { +"Din estimerte månedlige pensjon før skatt ved gradert uttak" })
            }
        }
        includePhrase(AfpOffentligTidsbegrensetTabell(afp))
    }

    ifNotNull(simulering.maanedligAlderspensjonForKnekkpunkter) { knekkpunkter ->
        ifNotNull(knekkpunkter.vedGradertUttak) { gradertUttak ->
            ifNotNull(simuleringsinformasjon.gradertUttaksalder) { alder ->
                title1 {
                    text(bokmal { +"Din estimerte månedlige pensjon før skatt ved " + alder.aar.format() + " år" })
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
        }

        ifNotNull(knekkpunkter.vedNormertPensjonsalder) {


        ifNotNull(simuleringsinformasjon.normertPensjonsalderPlassering) { plassering ->
            showIf(plassering.isOneOf(NormertPensjonsalderPlassering.MELLOM_GRADERT_OG_HELT)) {
                title1 {
                    text(bokmal { +"Din estimerte månedlige pensjon før skatt ved 67 år" })
                }
                ifNotNull(knekkpunkter.vedNormertPensjonsalder) { normPensjonsalder ->
                    includePhrase(AlderspensjonTabell(normPensjonsalder))
                }
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

        ifNotNull(simuleringsinformasjon.normertPensjonsalderPlassering) { plassering ->
            showIf(plassering.isOneOf(NormertPensjonsalderPlassering.ETTER_HELT)) {
                title1 {
                    text(bokmal { +"Din estimerte månedlige pensjon før skatt ved 67 år" })
                }
                ifNotNull(knekkpunkter.vedNormertPensjonsalder) { normPensjonsalder ->
                    includePhrase(AlderspensjonTabell(normPensjonsalder))
                }
            }
        }
    }

    ifNotNull(simulering.maanedligAlderspensjonForKnekkpunkter) { knekkpunkter ->
        title1 {
            text(bokmal { +"Slik har vi beregnet pensjonen din" })
         }
        ifNotNull(knekkpunkter.vedGradertUttak) { alderspensjon ->
            showIf(simuleringsinformasjon.kull.isOneOf(Kull.KAP19, Kull.OVERGANG)) {
                includePhrase(OpptjeningKapittel19Tabell(alderspensjon))
            }
            showIf(simuleringsinformasjon.kull.isOneOf(Kull.KAP20, Kull.OVERGANG)) {
                includePhrase(OpptjeningKapittel20Tabell(alderspensjon))
            }
        }
        includePhrase(OpptjeningKapittel19Tabell(knekkpunkter.vedHeltUttak))
        includePhrase(OpptjeningKapittel20Tabell(knekkpunkter.vedHeltUttak))
    }

    ifNotNull(simuleringsinformasjon.utenlandsperioder) { utenlandsperioder ->
        title1 {
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

    title1 {
        text(bokmal { +"Sivilstatus: " + simuleringsinformasjon.sivilstatus.value })
    }
    paragraph {
        text(
            bokmal {
                +"Hvis du bor sammen med noen kan inntekten til den du bor med ha betydning for hva du får i alderspensjon. Når du mottar alderspensjon må du derfor melde fra til Nav ved endring i sivilstand."
            },
        )
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
