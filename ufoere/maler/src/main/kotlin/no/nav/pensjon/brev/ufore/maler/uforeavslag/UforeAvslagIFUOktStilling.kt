package no.nav.pensjon.brev.ufore.maler.uforeavslag

import no.nav.pensjon.brev.ufore.maler.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_AVSLAG_IFU_OKT_STILLING
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagEnkelDto
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagEnkelDtoSelectors.SaksbehandlervalgSelectors.VisVurderingFraVilkarvedtak
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagEnkelDtoSelectors.UforeAvslagPendataSelectors.kravMottattDato
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagEnkelDtoSelectors.UforeAvslagPendataSelectors.vurdering
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagEnkelDtoSelectors.pesysData
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagEnkelDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brev.ufore.maler.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VEDTAK

@TemplateModelHelpers
object UforeAvslagIFUOktStilling : RedigerbarTemplate<UforeAvslagEnkelDto> {

    override val kode = UT_AVSLAG_IFU_OKT_STILLING
    override val kategori = TemplateDescription.Brevkategori.VEDTAK_ENDRING_OG_REVURDERING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.UFOREP)


    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Avslag uføretrygd - 12-9",
            distribusjonstype = VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        ),
    )
    {
        title {
            text (bokmal { + "Nav har avslått søknaden din om endring av inntektsgrensen din"},
                nynorsk { + "Nav har avslått søknaden din om endring av inntektsgrensa di"})
        }
        outline {
            paragraph {
                text(bokmal { +"Vi har avslått søknaden din om å endre inntektsgrensen din, som vi fikk den " + pesysData.kravMottattDato.format() + "." },
                    nynorsk { +"Vi har avslått søknaden din om å endre inntektsgrensa di, som vi fekk den " + pesysData.kravMottattDato.format() + "." })
            }
            title1 {
                text(bokmal { + "Derfor endrer vi ikke inntektsgrensen din"},
                    nynorsk { + "Derfor endrar vi ikkje inntektsgrensa di"})
            }
            paragraph {
                text(bokmal { + "Du får ikke endret inntektsgrensen din, fordi økningen du har hatt i inntekt skyldes at du har jobbet mer. " +
                        "Du har hatt en varig inntektsøkning, men det er fordi stillingsprosenten din har økt."},
                    nynorsk { + "Du får ikkje endra inntektsgrensa di, fordi auken du har hatt i inntekt kjem av at du har jobba meir. " +
                            "Du har hatt ein varig inntektsauke, men det er fordi stillingsprosenten din har auka."})
            }
            paragraph {
                text(bokmal { +"For å ha rett til høyere inntektsgrense, " +
                        "må du ha hatt en varig inntektsøkning som ikke skyldes at stillingsprosenten din har økt eller at du har jobbet mer. " },
                    nynorsk { +"For å ha rett til høgare inntektsgrense, " +
                            "må du ha hatt ein varig inntektsauke som ikkje kjem av at stillingsprosenten din har auka eller at du har jobba meir. " }
                )
            }

            showIf(saksbehandlerValg.VisVurderingFraVilkarvedtak) {
                paragraph {
                    text(bokmal { +pesysData.vurdering },
                        nynorsk { +pesysData.vurdering } )
                }
            }
            paragraph {
                text(bokmal { + fritekst("Individuell vurdering") },
                    nynorsk { + fritekst("Individuell vurdering") } )
            }

            paragraph {
                text(bokmal { + "Du oppfyller ikke vilkåret for å endre inntektsgrensen, og vi avslår derfor søknaden din."},
                    nynorsk { + "Du oppfyller ikkje vilkåret for å endre inntektsgrensa, og vi avslår derfor søknaden din."})
            }
            paragraph {
                text(bokmal { +"Vedtaket har vi gjort etter folketrygdloven § 12-9 og forskrift om uføretrygd fra folketrygden § 2-2." },
                    nynorsk { +"Vedtaket har vi gjort etter folketrygdlova § 12-9 og forskrift om uføretrygd frå folketrygda § 2-2." })
            }

            includePhrase(Felles.RettTilAKlageLang)
            includePhrase(Felles.RettTilInnsynRefVedlegg)
            includePhrase(Felles.HarDuSporsmal)
        }
        includeAttachment(vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk)
    }
}
