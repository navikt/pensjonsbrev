package no.nav.pensjon.brev.ufore.maler.opphor

import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_OPPHOR
import no.nav.pensjon.brev.ufore.api.model.maler.Sakstype
import no.nav.pensjon.brev.ufore.maler.Brevkategori
import no.nav.pensjon.brev.ufore.maler.FeatureToggles
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brev.ufore.maler.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VIKTIG

@TemplateModelHelpers
object Opphor : RedigerbarTemplate<EmptyRedigerbarBrevdata> {

    override val featureToggle = FeatureToggles.innhentingOpplysninger.toggle

    override val kode = UT_OPPHOR
    override val kategori = Brevkategori.SLUTTBEHANDLING
    override val brevkontekst = TemplateDescription.Brevkontekst.SAK
    override val sakstyper = setOf(Sakstype.UFOREP)


    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Nav har opphørt uføretrygden din",
            distribusjonstype = VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV
        ),
    )
    {
        title {
            text (bokmal { + "Nav har opphørt uføretrygden din" })
        }
        outline {
            paragraph {
                text(bokmal { +"Vi har opphørt uføretrygden din fra " + fritekst("dato") + ". " })
            }
            paragraph {
                text(bokmal { +"Utbetalingen av uføretrygden din er stanset fra " + fritekst("første måned etter vedtaksdato") + ". " })
            }
            paragraph { fritekst("Slett det som ikke passer") }

            title1 { text(bokmal { +"Derfor opphører uføretrygden din" }) }
            paragraph {
                text(bokmal { +"Vi har opphørt uføretrygden din fordi du har tatt kontakt med oss " + fritekst("dato bruker tok kontakt") + " med ønske om at uføretrygden din opphøres fra " + fritekst("ønsket opphørsdato") + "." })
            }
            paragraph { fritekst("fritekst") }
            paragraph { fritekst("I dette brevet forklarer vi hvilke rettigheter og plikter du har. Det er derfor viktig at du leser hele brevet.") }

            title1 { text(bokmal { +"Honnørkort" }) }
            paragraph {
                text(bokmal {+"Du har ikke lenger rett på uføretrygd, og må derfor levere tilbake honnørkortet til Nav-kontoret ditt. "})
            }

            // TODO: Toggle for å vise denne delen av brevet
            title1 { text(bokmal { +"Du kan ha fått utbetalt for mye i uføretrygd" }) }
            paragraph {
                text(bokmal {+"Uføretrygden din er opphørt tilbake i tid. Det betyr at du har fått for mye utbetalt i uføretrygd. Vi vil vurdere om beløpet skal betales tilbake. "})
            }

            includePhrase(Felles.MeldFraOmEndringer)
            includePhrase(Felles.HarDuSporsmal)
        }
        includeAttachment(vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk)
    }
}
