package no.nav.pensjon.brev.alder.maler.sivilstand.fraser

import no.nav.pensjon.brev.alder.maler.felles.Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON
import no.nav.pensjon.brev.alder.maler.felles.Constants.OMSTILLINGSSTOENAD_URL
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

data object RettTilOmstillingsstoenad : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        title2 {
            text(
                bokmal { +"Hvem kan ha rett til ytelse etter avdøde?" },
                nynorsk { +"Kven kan ha rett til yting etter avdøde?" },
            )
        }

        paragraph {
            text(
                bokmal {
                    +"Du kan ha rett til omstillingsstønad fra folketrygden. Omstillingsstønad kan imidlertid " +
                        "ikke kombineres med AFP. Hvis du har rett til omstillingsstønad, må du velge en av " +
                        "ytelsene. Du må selv undersøke om et slikt valg vil få konsekvenser for fremtidige " +
                        "rettigheter fra tjenestepensjonsordningen."
                },
                nynorsk {
                    +"Du kan ha rett til omstillingsstønad frå folketrygda. Omstillingsstønad kan likevel " +
                        "ikkje kombinerast med AFP. Om du har rett til omstillingsstønad, må du velje ei av " +
                        "ytingane. Du må sjølv undersøkje om eit slikt val vil få konsekvensar for framtidige " +
                        "rettar frå tenestepensjonsordninga."
                },
            )
        }

        paragraph {
            text(
                bokmal {
                    +"Vi gjør også oppmerksom på at det er krav til aktivitet 6 måneder etter dødsfallet når " +
                        "man mottar omstillingsstønad. Retten til omstillingsstønad har man i 3 år fra " +
                        "dødsfallet."
                },
                nynorsk {
                    +"Vi gjer også merksam på at det er krav til aktivitet 6 månader etter dødsfallet når " +
                        "ein får omstillingsstønad. Retten til omstillingsstønad har ein i 3 år frå " +
                        "dødsfallet."
                },
            )
        }

        paragraph {
            text(
                bokmal {
                    +"Retten til å få omstillingsstønad avhenger av ditt forhold til avdøde. " +
                        "Hvis dere var gift, partnere, samboere eller separert da dødsfallet skjedde, kan du få " +
                        "omstillingsstønad hvis noe av dette gjelder:"
                },
                nynorsk {
                    +"Retten til å få omstillingsstønad avheng av forholdet ditt til avdøde. Om de var gifte, " +
                        "partnarar, sambuarar eller separerte då dødsfallet skjedde, kan du få " +
                        "omstillingsstønad om noko av dette gjeld: "
                },
            )
        }

        paragraph {
            list {
                item {
                    text(
                        bokmal { +"ekteskapet varte i minst 5 år." },
                        nynorsk { +"ekteskapet varte i minst 5 år." },
                    )
                }
                item {
                    text(
                        bokmal { +"du har vært gift eller samboer med den avdøde, og har eller har hatt barn med den avdøde." },
                        nynorsk { +"du har vore gift eller sambuar med den avdøde, og har eller har hatt barn med den avdøde." },
                    )
                }
                item {
                    text(
                        bokmal {
                            +"har omsorg for barn under 18 år i minst 50 prosent dersom dere var gift i " +
                                "mindre enn 5 år. Dette gjelder alle barn du har omsorg for, ikke bare felles " +
                                "barn."
                        },
                        nynorsk {
                            +"har omsorg for barn under 18 år i minst 50 prosent dersom de var gifte i " +
                                "mindre enn 5 år. Dette gjeld alle barn du har omsorg for, ikkje berre felles " +
                                "barn."
                        },
                    )
                }
            }
        }

        paragraph {
            text(
                bokmal {
                    +"Du må som hovedregel være medlem i folketrygden, og avdøde må ha vært medlem i " +
                        "folketrygden de siste fem årene fram til dødsfallet. I noen tilfeller kan medlemskap i " +
                        "trygdeordninger i andre EØS-land telle likt som medlemskap i folketrygden."
                },
                nynorsk {
                    +"Du må som hovudregel vere medlem i folketrygda, og avdøde må ha vore medlem i " +
                        "folketrygda dei siste fem åra fram til dødsfallet. I nokre tilfelle kan medlemskap i " +
                        "trygdeordningar i andre EØS-land telje likt som medlemskap i folketrygda. "
                },
            )
        }

        title3 {
            text(
                bokmal { +"Hvordan søker du?" },
                nynorsk { +"Korleis søkjar du?" },
            )
        }

        paragraph {
            text(
                bokmal {
                    +"Du kan kontakte Nav på telefon $NAV_KONTAKTSENTER_TELEFON_PENSJON hvis du ønsker veiledning angående " +
                        "omstillingsstønad. Du kan lese mer om rettighetene dine og finne søknadsskjema på " +
                        "$OMSTILLINGSSTOENAD_URL."
                },
                nynorsk {
                    +"Du kan kontakte Nav på telefon $NAV_KONTAKTSENTER_TELEFON_PENSJON om du ønskjer rettleiing rundt " +
                        "omstillingsstønad. Du kan lese meir om rettane dine og finne søknadsskjema på " +
                        "$OMSTILLINGSSTOENAD_URL."
                },
            )
        }
    }
}
