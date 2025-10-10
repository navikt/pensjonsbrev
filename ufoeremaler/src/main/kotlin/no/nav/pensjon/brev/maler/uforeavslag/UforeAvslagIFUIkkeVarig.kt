package no.nav.pensjon.brev.maler.uforeavslag

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.maler.fraser.Felles.*
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_AVSLAG_IFU_IKKE_VARIG
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagEnkelDto
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagEnkelDtoSelectors.SaksbehandlervalgSelectors.VisVurderingFraVilkarvedtak
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagEnkelDtoSelectors.UforeAvslagPendataSelectors.kravMottattDato
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagEnkelDtoSelectors.UforeAvslagPendataSelectors.vurdering
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagEnkelDtoSelectors.pesysData
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagEnkelDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VEDTAK

@TemplateModelHelpers
object UforeAvslagIFUIkkeVarig : RedigerbarTemplate<UforeAvslagEnkelDto> {

    override val kode = UT_AVSLAG_IFU_IKKE_VARIG
    override val kategori = TemplateDescription.Brevkategori.VEDTAK_ENDRING_OG_REVURDERING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.UFOREP)


    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Avslag uføretrygd - 12-9",
            isSensitiv = false,
            distribusjonstype = VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        ),
    )
    {
        title {
            text (bokmal { + "Nav har avslått søknaden din om endring av inntekten din før uførhet"})
        }
        outline {
            paragraph {
                text(bokmal { +"Vi har avslått søknaden din om å endre den fastsatte inntekten din før du ble ufør, som vi fikk den " + pesysData.kravMottattDato.format() + "." })
            }
            title1 {
                text(bokmal { + "Derfor endres ikke inntekten din før du ble ufør"})
            }
            paragraph {
                text(bokmal { + "Du har ikke hatt en varig inntektsøkning i din stilling på " + fritekst("prosentandel") + " prosent."})
            }
            paragraph {
                text(bokmal { +"For å ha rett til å endre den fastsatte inntekten din før du ble ufør, " +
                        "må du ha hatt en varig inntektsøkning uten at stillingsandelen din har økt. " +
                        "Inntekt før uførhet kan bare endres dersom du mottar gradert uføretrygd." })
            }

            showIf(saksbehandlerValg.VisVurderingFraVilkarvedtak) {
                paragraph {
                    text(bokmal { + pesysData.vurdering })
                }
            }
            paragraph {
                text(bokmal { +fritekst("Lim inn teksten fra vilkårsvurderingen her") })
            }


            paragraph {
                text(bokmal { + "Du oppfyller ikke vilkåret, og vi avslår derfor søknaden din om endring av inntekten din før uførhet."})
            }
            paragraph {
                text(bokmal { +"Vedtaket er gjort etter folketrygdloven § 12-9 og forskrift om uføretrygd fra folketrygden § 2-2." })
            }

            includePhrase(RettTilAKlageKort)
            includePhrase(RettTilInnsynRefVedlegg)
            includePhrase(HarDuSporsmal)
        }
        includeAttachment(vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk)
    }
}
