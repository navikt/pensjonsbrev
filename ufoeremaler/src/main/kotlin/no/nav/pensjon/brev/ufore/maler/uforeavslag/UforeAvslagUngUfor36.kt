package no.nav.pensjon.brev.ufore.maler.uforeavslag

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.ufore.maler.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_AVSLAG_UNG_UFOR_36
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagUforetidspunkt26DtoSelectors.SaksbehandlervalgSelectors.VisVurderingFraVilkarvedtak
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagUforetidspunkt26DtoSelectors.UforeAvslagPendataSelectors.kravMottattDato
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagUforetidspunkt26DtoSelectors.UforeAvslagPendataSelectors.vurdering
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagUforetidspunkt26DtoSelectors.pesysData
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagUforetidspunkt26DtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagUforetidspunkt26Dto
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagUforetidspunkt26DtoSelectors.SaksbehandlervalgSelectors.visUforetidspunktEtter26
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VEDTAK

@TemplateModelHelpers
object UforeAvslagUngUfor36 : RedigerbarTemplate<UforeAvslagUforetidspunkt26Dto> {

    override val kode = UT_AVSLAG_UNG_UFOR_36
    override val kategori = TemplateDescription.Brevkategori.VEDTAK_ENDRING_OG_REVURDERING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.UFOREP)


    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Avslag uføretrygd - 12-13",
            distribusjonstype = VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        ),
    )
    {
        title {
            text (bokmal { + "Nav har avslått søknaden din om rettighet som ung ufør"})
        }
        outline {
            paragraph {
                text(bokmal { +"Vi har avslått søknaden din om rettighet som ung ufør som vi fikk den " + pesysData.kravMottattDato.format() + "." })
            }
            title1 {
                text(bokmal { +"Derfor får du ikke rettigheter som ung ufør" })
            }
            paragraph {
                text(bokmal { +"Vi avslår søknaden fordi du har søkt om ung ufør etter at du fylte 36 år, og har jobbet mer enn 50 prosent etter du fylte 26 år." })
            }
            paragraph {
                text(bokmal { +"For å få innvilget rettighet som ung ufør, er det et krav at du ble ufør før du fylte 26 år på grunn av en alvorlig og varig sykdom eller skade, som er klart dokumentert. " +
                        "Søker du etter at du fylte 36 år, må du har jobbet mindre enn 50 prosent etter du fylte 26 år. Du har jobbet mer enn 50 prosent over en lengre periode etter at du fylte 26 år. " +
                        "Derfor må vi vurdere om dette tyder på en reell inntektsevne på mer enn 50 prosent. Vi vurderer at det du har arbeidet etter at du fylte 26 år ikke kan anses som et mislykket arbeidsforsøk." })
            }

            showIf(saksbehandlerValg.VisVurderingFraVilkarvedtak) {
                paragraph {
                    text(bokmal { +pesysData.vurdering })
                }
            }
            paragraph {
                text(bokmal { + fritekst("Individuell vurdering") })
            }

            showIf(saksbehandlerValg.visUforetidspunktEtter26) {
                paragraph {
                    text(bokmal { +"Vi viser til at uføretidspunktet er satt til etter 26 år og du fyller derfor ikke vilkårene for beregning som ung ufør." })
                }
            }

            paragraph {
                text(bokmal { +"Vedtaket er gjort etter folketrygdloven § 12-13 tredje ledd." })
            }

            includePhrase(Felles.RettTilAKlageLang)
            includePhrase(Felles.RettTilInnsynRefVedlegg)
            includePhrase(Felles.HarDuSporsmal)
        }
        includeAttachment(vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk)
    }
}
