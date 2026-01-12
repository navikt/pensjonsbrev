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
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_AVSLAG_OKT_GRAD_SYKDOM
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
object UforegradAvslagSykdom : RedigerbarTemplate<UforeAvslagEnkelDto> {

    override val kode = UT_AVSLAG_OKT_GRAD_SYKDOM
    override val kategori = TemplateDescription.Brevkategori.VEDTAK_ENDRING_OG_REVURDERING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.UFOREP)


    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Avslag uføretrygd - 12-6",
            distribusjonstype = VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        ),
    )
    {
        title {
            text (bokmal { + "Nav har avslått søknaden din om økt uføregrad"},
                nynorsk { + "Nav har avslått søknaden din om auka uføregrad"})
        }
        outline {
            paragraph {
                text(bokmal { +"Vi har avslått søknaden din om økt uføregrad som vi fikk den " + pesysData.kravMottattDato.format() + "." },
                    nynorsk { +"Vi har avslått søknaden din om auka uføregrad som vi fekk den " + pesysData.kravMottattDato.format() + "." })
            }
            title1 {
                text(bokmal { +"Derfor får du ikke økt uføregrad" },
                    nynorsk { +"Derfor får du ikkje auka uføregrad" })
            }
            paragraph {
                text(bokmal { +"Vi avslår søknaden din fordi vi vurderer at det er andre forhold enn sykdom eller skade som er hovedårsaken til at inntektsevnen din er nedsatt utover den uføregraden som du har fra før. " },
                    nynorsk { +"Vi avslår søknaden din fordi vi vurderer at det er andre forhold enn sjukdom eller skade som er hovudårsaka til at inntektsevna di er nedsett utover den uføregraden som du har frå før. " })
            }
            paragraph {
                text(bokmal { +"For å få innvilget økt uføregrad, må den varige nedsatte inntektsevnen din i hovedsak skyldes varig sykdom eller skade. " },
                    nynorsk { +"For å få innvilga auka uføregrad, må den varige nedsette inntektsevna di i hovudsak skuldast varig sjukdom eller skade. " })
            }

            showIf(saksbehandlerValg.VisVurderingFraVilkarvedtak) {
                paragraph {
                    text(bokmal { +pesysData.vurdering },
                        nynorsk { +pesysData.vurdering })
                }
            }
            paragraph {
                text(bokmal { + fritekst("Individuell vurdering") },
                    nynorsk { + fritekst("Individuell vurdering") })
            }

            paragraph {
                text(bokmal { +"Vi vurderer derfor at inntektsevnen din ikke er varig nedsatt med mer enn " + fritekst("Nåværende uføregrad") + " prosent."},
                    nynorsk { +"Vi vurderer derfor at inntektsevna di ikkje er varig nedsett med meir enn " + fritekst("Nåværende uføregrad") + " prosent."})
            }
            paragraph {
                text(bokmal { + "Du oppfyller ikke vilkårene, og vi avslår derfor søknaden din om økt uføregrad."},
                    nynorsk { + "Du oppfyller ikkje vilkåra, og vi avslår derfor søknaden din om auka uføregrad."})
            }
            paragraph {
                text(bokmal { +"Vedtaket har vi gjort etter folketrygdloven §§ 12-6, 12-7 og 12-10." },
                    nynorsk { +"Vedtaket har vi gjort etter folketrygdlova §§ 12-6, 12-7 og 12-10." })
            }

            includePhrase(Felles.RettTilAKlageLang)
            includePhrase(Felles.RettTilInnsynRefVedlegg)
            includePhrase(Felles.HarDuSporsmal)
        }
        includeAttachment(vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk)
    }
}
