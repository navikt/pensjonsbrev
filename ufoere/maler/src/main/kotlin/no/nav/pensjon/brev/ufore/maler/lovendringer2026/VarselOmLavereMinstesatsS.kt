package no.nav.pensjon.brev.ufore.maler.lovendringer2026

import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_S_VARSEL_LAVERE_MINSTESATS_2026
import no.nav.pensjon.brev.ufore.api.model.maler.Sakstype
import no.nav.pensjon.brev.ufore.maler.Brevkategori
import no.nav.pensjon.brev.ufore.maler.FeatureToggles
import no.nav.pensjon.brev.ufore.maler.fraser.Constants.KONTAKT_URL
import no.nav.pensjon.brev.ufore.maler.fraser.Constants.NAV_KONTAKTSENTER_TELEFON_UFORE
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object VarselOmLavereMinstesatsS : RedigerbarTemplate<EmptyRedigerbarBrevdata> {

    override val featureToggle = FeatureToggles.varsellavereminstesats

    override val kode = UT_S_VARSEL_LAVERE_MINSTESATS_2026
    override val kategori = Brevkategori.ETTEROPPGJOER
    override val brevkontekst = TemplateDescription.Brevkontekst.ALLE
    override val sakstyper = setOf(Sakstype.UFOREP)

    override val template = createTemplate(
        languages = languages(Bokmal, Language.Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Varsel - endring av minstesats fom 1. juli 2026",
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        title {
            text(
                bokmal { + "Varsel om endring av uføretrygd" },
                nynorsk { +"Varsel om endring av uføretrygd" },
            )
        }
        outline {
            paragraph {
                text(
                    bokmal { + "Fra 1. juli 2026 vil du få en endring i uføretrygden fordi Stortinget har vedtatt at minstesatsen endres."},
                    nynorsk { + "Frå 1. juli 2026 vil du få ei endring i uføretrygda fordi Stortinget har vedteke at minstesatsen vert endra." },
                )
            }
            paragraph {
                text(
                    bokmal { + "Grunnbeløpet (G) justeres 1. mai hvert år. Du vil få et vedtak fra oss etter justeringen av grunnbeløpet. Da kan vi lage riktige beregninger av hvordan endringen påvirker uføretrygden din."},
                    nynorsk { + "Grunnbeløpet (G) vert justert 1. mai kvart år. Du vil få eit vedtak frå oss etter justeringa av grunnbeløpet. Då kan vi lage rette berekningar av korleis endringa påverkar uføretrygda di." },
                )
            }

            title1 {
                text(
                    bokmal { + "Derfor får du endring i uføretrygden" },
                    nynorsk { + "Difor får du endring i uføretrygda" },
                )
            }
            paragraph {
                text(
                    bokmal { + "Fra 1. juli 2026 endres minstesatsen for gifte/samboende fra 2,379 G til 2,329 G." },
                    nynorsk { + "Frå 1. juli 2026 vert minstesatsen for gifte/sambuande endra frå 2,379 G til 2,329 G." },
                )
            }
            paragraph {
                text(
                    bokmal { + "Lovendringen gjelder for deg som fikk uførepensjonen din omregnet til uføretrygd i 2015. Endringen fører til at alle gifte og samboende nå får lik minstesats." },
                    nynorsk { + "Lovendringa gjeld for deg som fekk uførepensjonen din omrekna til uføretrygd i 2015. Endringa fører til at alle gifte og sambuande no får lik minstesats." },
                )
            }
            paragraph {
                text(
                    bokmal { + "Se folketrygdlovens § 12-13 andre ledd andre punktum." },
                    nynorsk { + "Sjå folketrygdlova § 12-13 andre ledd andre punktum." },
                )
            }
            paragraph {
                text(
                    bokmal { + "I vedtaksbrevet du vil få fra oss, kommer det mer informasjon og beregninger av hvordan dette påvirker uføretrygden din." },
                    nynorsk { +"I vedtaksbrevet du vil få frå oss, kjem det meir informasjon og utrekningar av korleis dette påverkar uføretrygda di." },
                )
            }

            title1 {
                text(
                    bokmal { + "Slik kan lovendringen påvirke deg  " },
                    nynorsk { + "Slik kan lovendringa påverke deg" },
                )
            }
            paragraph {
                list {
                    item {
                        text(
                            bokmal { +"Endringen kan føre til at du får en endret reduksjonsprosent(kompensasjonsgrad)." },
                            nynorsk { +"Endringa kan føre til at du får ein endra reduksjonsprosent (kompensasjonsgrad)." },
                        )
                    }
                    item {
                        text(
                            bokmal { +"Har du barnetillegg, kan denne endringen påvirke utbetalingen av barnetillegget ditt. Uføretrygd regnes med som inntekt når vi beregner størrelsen på barnetillegget ditt. Derfor kan en endring av uføretrygd føre til en endring av barnetillegget." },
                            nynorsk { +"Har du barnetillegg, kan denne endringa påverke utbetalinga av barnetillegget ditt. Uføretrygd vert rekna med som inntekt når vi reknar ut storleiken på barnetillegget ditt. Difor kan ei endring av uføretrygd føre til ei endring av barnetillegget." },
                        )
                    }
                    item {
                        text(
                            bokmal { +"Har du en egenopptjening som er høyere enn 2,329 G, vil vi bruke denne i beregningen av uføretrygden din." },
                            nynorsk { +"Har du ei eigenopptening som er høgare enn 2,329 G, vil vi bruke denne i utrekninga av uføretrygda di." },
                        )
                    }
                    item {
                        text(
                            bokmal { +"Denne endringen vil også gjelde for deg som får avkortet uføretrygd på grunn av redusert trygdetid." },
                            nynorsk { +"Denne endringa vil òg gjelde for deg som får avkorta uføretrygd på grunn av redusert trygdetid." },
                        )
                    }
                    item {
                        text(
                            bokmal { +"Denne endringen påvirker også deg som har gradert uføretrygd." },
                            nynorsk { +"Denne endringa påverkar òg deg som har gradert uføretrygd." },
                        )
                    }
                }
            }

            title1 {
                text(
                    bokmal { + "Dette kan du gjøre nå" },
                    nynorsk { + "Dette kan du gjere no" },
                )
            }
            paragraph {
                text(
                    bokmal { + "Dette er kun et varsel. Mener du vi har feil opplysninger om saken din, kan du melde fra til oss før vi fatter et vedtak. Fristen for å komme med tilbakemeldinger er 2 uker fra du får dette brevet. Du kan skrive til oss på $KONTAKT_URL eller ringe oss på telefon $NAV_KONTAKTSENTER_TELEFON_UFORE." },
                    nynorsk { + "Dette er berre eit varsel. Meiner du vi har feil opplysningar om saka di, kan du melde frå til oss før vi fattar eit vedtak. Fristen for å komme med tilbakemeldingar er 2 veker frå du får dette brevet. Du kan skrive til oss på $KONTAKT_URL eller ringje oss på telefon $NAV_KONTAKTSENTER_TELEFON_UFORE." },
                )
            }

            includePhrase(Felles.RettTilInnsyn)
            includePhrase(Felles.HarDuSporsmal)
        }
    }
}
