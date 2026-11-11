package no.nav.pensjon.brev.ufore.maler.vedlegg

import no.nav.pensjon.brev.api.model.maler.EmptyVedleggData
import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.text

val informasjonsskrivOmHelsetjenester =
    createAttachment<LangBokmal, EmptyVedleggData>(
        title = {
            text(bokmal { +"Informasjonsskriv om helsetjenester" })
        },
        includeSakspart = false,
    ) {
        title1 {
            text(bokmal { +"Informasjon til pensjonister og uføretrygdede som flytter INNEN EØS-OMRÅDET og er omfattet av EØS-avtalens regler om trygd" })
        }
        paragraph {
            list {
                item { text(bokmal { +"om medlemskap og rett til helsetjenester" }) }
            }
        }
        title2 {
            text(bokmal { +" Medlemskap i folketrygden" })
        }
        paragraph {
            text(bokmal { +"Pensjonister/uføretrygdede som bor i utlandet, er ikke automatisk medlemmer i folketrygden." })
        }
        paragraph {
            text(bokmal { +"Som EØS borger regnes du som bosatt der du har ditt senter for livsinteresser. I vurderingen av hvor du har ditt bosted vektlegges blant annet" })
            list {
                item { text(bokmal { +"varigheten og kontinuiteten av ditt opphold i de aktuelle landene" }) }
                item { text(bokmal { +"dersom du er i arbeid, vil arbeidets art og karakter" }) }
                item { text(bokmal { +"din familiesituasjon og familiebånd," }) }
                item { text(bokmal { +"eventuell ulønnet aktivitet," }) }
                item { text(bokmal { +"din boligsituasjon, særlig hvor permanent den er" }) }
                item { text(bokmal { +"i hvilket land du er skattemessig bosatt" }) }
            }
        }
        paragraph {
            text(bokmal { +"Registrering i folkeregisteret og/eller i Arbeidsgiver- og arbeidstakerregisteret er i seg selv ikke avgjørende for hvor arbeidstakeren er bosatt. Dette vil imidlertid kunne være momenter som kan vektes i en helhetsvurdering." })
        }
        title2 {
            text(bokmal { +"Rett til helsetjenester" })
        }
        paragraph {
            text(bokmal { +"EØS-avtalen inneholder blant annet en egen del som omhandler trygderettigheter. Som følge av bestemmelser i EØS-avtalens trygdedel har pensjonister/ uføretrygdede som flytter til et annet EØS-land - og deres forsørgede familiemedlemmer - rett til å få dekket utgifter til helsetjenester i det landet de bosetter seg. Bestemmelsene gjelder for personer med pensjon/uføretrygd fra folketrygden, Statens Pensjonskasse, Pensjonstrygden for sjømenn, Pensjonstrygden for fiskere, Pensjonstrygden for skogsarbeidere og Pensjonsordning for sykepleiere." })
        }
        paragraph {
            text(bokmal { +"Retten til helsetjenester dokumenteres med dokumentet S1 som utstedes av Helseøkonomiforvaltningen HELFO. Du må kontakte HELFO for alle henvendelser som gjelder helsetjenester under utenlandsopphold, helsetrygdkort og S1." })
        }
        paragraph {
            text(bokmal { +"Internett: www.helsenorge.no" })
            newline()
            text(bokmal { +"Telefon 23 32 70 00" })
        }
        paragraph {
            text(bokmal { +"Rettighetsdokument S1 brukes ikke innen Norden." })
        }
        title2 {
            text(bokmal { +"Trygdeavgift" })
        }
        paragraph {
            text(bokmal { +"Du skal betale trygdeavgift til Norge av pensjonen/uføretrygden også etter at du har flyttet ut dersom du har rettighetsdokument S1 fra Norge. Som nevnt utstedes ikke S1 ved flytting innen Norden. Selv om S1 ikke er utstedt, skal du likevel betale trygdeavgift til Norge av din pensjon/uføretrygd. Bakgrunnen for dette er at det er Norge som er ansvarlige for dine utgifter til helsetjenester selv om du ikke lenger bor her." })
        }
        paragraph {
            text(bokmal { +"Trygdeavgift skal du imidlertid kun betale til ett land av gangen. Dersom du også betaler slik avgift i bostedslandet, ber vi deg ta dette opp med trygdemyndighetene der." })
        }
        title2 {
            text(bokmal { +"Særlig om avtalefestet pensjon" })
        }
        paragraph {
            text(bokmal { +"Dersom du mottar avtalefestet pensjon, har du ikke rett til dokumentet S1. Du kan søke om frivillig medlemskap, se neste side." })
        }
        title1 {
            text(bokmal { +"Informasjon til pensjonister og uføretrygdede som bosetter seg UTENFOR EØS-OMRÅDET og pensjonister med AVTALEFESTET PENSJON som bosetter seg innen EØS-området" })
        }
        paragraph {
            list {
                item { text(bokmal { +"om frivillig medlemskap i folketrygden" }) }
            }
        }
        title2 {
            text(bokmal { +"Medlemskap i folketrygden" })
        }
        paragraph {
            text(bokmal { +"Pensjonister/uføretrygdede som bor i utlandet, er ikke automatisk medlemmer i folketrygden. Du regnes som bosatt i utlandet dersom du har til hensikt, eller faktisk har, oppholdt deg i 12 måneder i utlandet. Du vil også regnet som bosatt i utlandet dersom du oppholder deg i mer enn 6 måneder i to eller flere år på rad. Det at du mottar pensjon eller uføretrygd fra folketrygden, medfører ikke at du automatisk er medlem i folketrygden." })
        }
        paragraph {
            text(bokmal { +"Skatteplikt til Norge innebærer heller ikke at du er automatisk medlem i folketrygden. En pensjonist/uføretrygdet som ikke er medlem i folketrygden har ikke rett til å få dekket utgifter til helsetjenester fra folketrygden, verken når det gjelder sykdom som oppstår under utenlandsoppholdet eller under eventuelle ferieopphold i Norge." })
        }
        paragraph {
            text(bokmal { +"Pensjonister og uføretrygdede kan få frivillig medlemskap i folketrygdens helsedel når de søker om det og fyller følgende vilkår:" })
            list {
                item { text(bokmal { +"har vært medlemmer i folketrygden i minst tre hele kalenderår av de siste fem, og har nær tilknytning til det norske samfunnet." }) }
                item { text(bokmal { +"mottar pensjon eller uføretrygd fra folketrygden" }) }
                item { text(bokmal { +"har minst 30 års medlemskap i folketrygden etter fylte 16 år. Minst 10 av disse må være umiddelbart før søknadstidspunktet." }) }
            }
        }
        paragraph {
            text(bokmal { +"Selv om de to siste vilkårene ikke er oppfylt, kan pensjonister/uføretrygdede likevel bli medlem i folketrygden hvis særlige grunner gjør det rimelig. I denne forbindelse kan helsemessige grunner ha betydning." })
        }
        paragraph {
            text(bokmal { +"Søknadsskjema for frivillig medlemskap settes frem for NAV Medlemskap og avgift. Du finner søknadsskjema på www.nav.no." })
        }
        paragraph {
            text(bokmal { +"Dersom medlemskapet har opphørt, må du ha til hensikt å oppholde deg, eller faktisk ha oppholdt deg i Norge i minst 12 sammenhengende måneder, før du på nytt skal kunne regnes som bosatt i Norge etter folketrygdloven. Du vil da regnes som bosatt fra innreisedatoen, eller fra det tidspunktet det ble klart at oppholdet ditt i Norge ville bli på mer enn 12 måneder." })
        }
        title2 {
            text(bokmal { +"Trygdeavgift" })
        }
        paragraph {
            text(bokmal { +"En pensjonist/uføretrygdet som blir frivillig medlem, må betale avgift til NAV Medlemskap og avgift. Vi beregner avgiften av brutto pensjoner (offentlige og private) i den perioden medlemskapet gjelder for. I 2023 er avgiftssatsen 9,1 prosent av pensjonen dersom du ikke betaler skatt til Norge av den. Dersom du betaler skatt til Norge, er avgiftssatsen 2,3 prosent." })
        }
        paragraph {
            text(bokmal { +"Dersom du betaler kildeskatt, skal du betale en avgift på 7,4 prosent av avgiftsgrunnlaget til Nav." })
        }
        title2 {
            text(bokmal { +"Utgifter til helsetjenester – hva dekkes" })
        }
        paragraph {
            text(bokmal { +"Alle henvendelser om hva som dekkes av helsetjenester nå du er frivillig medlem i folketrygden, skal rettes til Helseøkonomiforvaltningen HELFO. Internett: www.helsenorge.no. Telefon: 23 32 70 00" })
        }
        paragraph {
            text(bokmal { +"Kontaktinformasjon NAV Medlemskap og avgift //" })
            newline()
            text(bokmal { +"Postadresse: Postboks 6600 Etterstad. // N-0607 Oslo" })
            newline()
            text(bokmal { +"Telefon: +47 21 07 37 00 // Fax: +47 21 06 91 01 //www.nav.no" })
        }
    }
