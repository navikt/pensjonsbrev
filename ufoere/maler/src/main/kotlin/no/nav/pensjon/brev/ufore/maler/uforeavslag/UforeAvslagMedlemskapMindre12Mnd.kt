package no.nav.pensjon.brev.ufore.maler.uforeavslag

import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.saksbehandlervalg
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_AVSLAG_MEDLEMSKAP_12MND
import no.nav.pensjon.brev.ufore.api.model.maler.Sakstype
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagDto
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.selectors.uforeAvslagDto.pesysData
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.selectors.uforeAvslagDto.uforeAvslagPendata.kravMottattDato
import no.nav.pensjon.brev.ufore.maler.Brevkategori
import no.nav.pensjon.brev.ufore.maler.FeatureToggles
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brev.ufore.maler.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VEDTAK

@TemplateModelHelpers
object UforeAvslagMedlemskapMindre12Mnd : RedigerbarTemplate<UforeAvslagDto> {

    override val featureToggle = FeatureToggles.avslagMedlemskapUtland12mnd.toggle

    override val kode = UT_AVSLAG_MEDLEMSKAP_12MND
    override val kategori = Brevkategori.FOERSTEGANGSBEHANDLING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.UFOREP)


    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Avslag uføretrygd - 12-2",
            distribusjonstype = VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        ),
    )
    {
        val ikkeYrkesaktiv = saksbehandlervalg("EttEllerTreAr", "Ikke vært yrkesaktiv i Norge eller andre EØS-land").bool()
        val txtEttEllerTreAr = if (ikkeYrkesaktiv.equals(true)) "tre" else "ett"

        title {
            text (bokmal { + "Nav har avslått søknaden din om uføretrygd"})
        }
        outline {
            paragraph {
                text(bokmal { +"Vi har avslått søknaden din om uføretrygd som vi fikk den " + pesysData.kravMottattDato.format() + "." })
            }

            title1 {
                text(bokmal { +"Derfor får du ikke uføretrygd" })
            }
            paragraph {
                text(bokmal { +"Vi avslår søknaden din fordi du har vært medlem i folketrygden i mindre enn " + txtEttEllerTreAr + " år. " })
            }
            paragraph {
                text(bokmal { +"For å ha rett til uføretrygd må du ha vært medlem i folketrygden eller i en trygdeordning i et annet EØS-land, i de siste fem årene frem til uføretidspunktet ditt, eller ha rett til uføretrygd som følge av en godkjent yrkesskade. " })
            }
            paragraph {
                text(bokmal { +"For at trygdetid i annet EØS-land kan brukes, må du ha minst ett års medlemskap i folketrygden før uføretidspunktet, forutsatt at du har vært yrkesaktiv i Norge eller andre EØS-land. " })
            }
            paragraph {
                text(bokmal { +"Har du ikke vært yrkesaktiv i Norge eller andre EØS-land, må du ha minst tre års medlemskap i folketrygden før uføretidspunktet. " })
            }
            paragraph {
                text(bokmal { +"Du bodde eller arbeidet i Norge fra " + fritekst("FOM medlemsperiode") + " til " + fritekst("TOM medlemsperiode") + ". " })
            }
            paragraph {
                text(bokmal { +"Du har ikke vært medlem i folketrygden i minst " + txtEttEllerTreAr + " år, og fyller dermed ikke minstekravet til medlemskap i Norge. Vi avslår derfor søknaden din om uføretrygd. " })
            }
            paragraph {
                text(bokmal { +"Vedtaket har vi gjort etter EØS-forordning 883/2004 artikkel 57. " })
            }

            title1 {
                text(bokmal { + "Vurdering av andre vilkår for uføretrygd" })
            }
            paragraph {
                text(bokmal { +"Du har mindre enn " + txtEttEllerTreAr + " års medlemskap i folketrygden, og fyller ikke vilkårene for uføretrygd, uavhengig av når uføretidspunktet ditt er. Vi har derfor ikke vurdert andre vilkår, som for eksempel om alle medisinske og arbeidsrettede tiltak er utprøvd. " })
            }

            includePhrase(Felles.RettTilAKlageLang)
            includePhrase(Felles.RettTilInnsynRefVedlegg)
            includePhrase(Felles.HarDuSporsmal)
        }
        includeAttachment(vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk)
    }
}