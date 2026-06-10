package no.nav.pensjon.brev.ufore.maler.uforeavslag

import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_AVSLAG_MEDLEMSKAP_12MND
import no.nav.pensjon.brev.ufore.api.model.maler.Sakstype
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagDto
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagDtoSelectors.UforeAvslagPendataSelectors.kravMottattDato
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagDtoSelectors.pesysData
import no.nav.pensjon.brev.ufore.maler.Brevkategori
import no.nav.pensjon.brev.ufore.maler.FeatureToggles
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brev.ufore.maler.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VEDTAK

@TemplateModelHelpers
object UforeAvslagMedlemskapMindre12Mnd : RedigerbarTemplate<UforeAvslagDto> {

    override val featureToggle = FeatureToggles.avslagMedlemskapUtland.toggle

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
                text(bokmal { +"Du bor i " + fritekst("BostedslandBeskrivelse") + ". Dette er en land Norge har trygdeavtale med. " })
            }
            paragraph {
                text(bokmal { +"For å ha rett til uføretrygd etter " + fritekst("trygdeavtale") + " må du ha vært medlem av folketrygden i minst ett år. " })
            }
            paragraph {
                text(bokmal { +"Du bodde eller arbeidet i Norge fra " + fritekst("FOM medlemsperiode") + " til " + fritekst("TOM medlemsperiode") + ". Du har ikke vært medlem i folketrygden i minst ett år. Vi avslår derfor søknaden din om uføretrygd. " })
            }
            paragraph {
                text(bokmal { +"Vedtaket har vi gjort etter " + fritekst("trygdeavtale artikkel") + ". " })
            }

            title1 {
                text(bokmal { + "Vurdering av andre vilkår for uføretrygd" })
            }
            paragraph {
                text(bokmal { +"Du har mindre enn ett års medlemskap i folketrygden, og fyller ikke vilkårene for uføretrygd. Vi har derfor ikke vurdert andre vilkår, som for eksempel vilkåret om varig sykdom. " })
            }

            includePhrase(Felles.RettTilAKlageLang)
            includePhrase(Felles.RettTilInnsynRefVedlegg)
            includePhrase(Felles.HarDuSporsmal)
        }
        includeAttachment(vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk)
    }
}
