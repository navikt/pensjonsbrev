package no.nav.pensjon.brev.ufore.maler.lovendringer2026

import no.nav.pensjon.brev.api.model.maler.EmptyAutobrevdata
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.AutoBrev.UT_VARSEL_LAVERE_MINSTESATS_2026
import no.nav.pensjon.brev.ufore.maler.fraser.Constants.KONTAKT_URL
import no.nav.pensjon.brev.ufore.maler.fraser.Constants.NAV_KONTAKTSENTER_TELEFON_UFORE
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Brevtype.INFORMASJONSBREV
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VIKTIG

@TemplateModelHelpers
object VarselOmLavereMinstesats : AutobrevTemplate<EmptyAutobrevdata> {

    override val kode = UT_VARSEL_LAVERE_MINSTESATS_2026

    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Varsel - endring av minstesats fom 1. juli 2026",
            distribusjonstype = VIKTIG,
            brevtype = INFORMASJONSBREV
        )
    ) {
        title {
            text(
                bokmal { + "Varsel om lavere utbetaling av uføretrygd" },
            )
        }
        outline {
            paragraph {
                text(
                    bokmal { + "Fra 1. juli 2026 vil du få en lavere utbetaling av uføretrygd fordi stortinget har vedtatt at minstesatsen endres. Din nye årlige beregnede uføretrygd før skatt vil være 2,329 G."},
                )
            }
            paragraph {
                text(
                    bokmal { + "Grunnbeløpet (G) justeres 1. mai hvert år. I vedtaksbrevet du vil få fra oss, kommer det mer informasjon og beregninger om hvordan dette vil påvirke uføretrygden din."},
                )
            }

            title1 {
                text(
                    bokmal { + "Derfor får du lavere utbetaling av uføretrygd" },
                )
            }
            paragraph {
                text(
                    bokmal { + "Fra 1. juli 2026 endres minstesatsen for gifte/samboende fra 2,379 G til 2,329 G." },
                )
            }
            paragraph {
                text(
                    bokmal { + "Denne lovendringen gjelder for deg som mottar uføretrygd som er en omregnet uførepensjon med virkning fra 1. januar 2015. Det fører til at minstesatsen for gifte og samboere nå blir lik for alle." },
                )
            }

            title1 {
                text(
                    bokmal { + "Slik påvirkes du av lovendringen" },
                )
            }
            paragraph {
                text(
                    bokmal { + "I vedtaksbrevet du vil få fra oss, får du mer informasjon og beregninger av hvordan dette påvirker din uføretrygd." },
                )
            }
            paragraph {
                text(
                    bokmal { + "Når du får en lavere utbetaling vil det også føre til en lavere reduksjonsprosent." },
                )
            }
            paragraph {
                text(
                    bokmal { + "Har du barnetillegg, kan denne endringen påvirke utbetalingen av barnetillegget ditt. Uføretrygd regnes med som inntekt når vi beregner størrelsen på barnetillegget ditt. Derfor kan en lavere utbetaling av uføretrygd føre til en høyere utbetaling av barnetillegget. " },
                )
            }

            title1 {
                text(
                    bokmal { + "Dette kan du gjøre nå" },
                )
            }
            paragraph {
                text(
                    bokmal { + "Dette er kun et varsel. Mener du vi har feil opplysninger om saken din, kan du melde fra til oss før vi fatter et vedtak. Fristen for å komme med tilbakemeldinger er 2 uker fra du får dette brevet. Du kan skrive til oss på $KONTAKT_URL eller ringe oss på telefon $NAV_KONTAKTSENTER_TELEFON_UFORE. " },
                )
            }

            includePhrase(Felles.RettTilInnsyn)
            includePhrase(Felles.HarDuSporsmal)

            title1 {
                text(
                    bokmal { + "Dette er endringene i loven:" },
                )
            }
        }
    }
}