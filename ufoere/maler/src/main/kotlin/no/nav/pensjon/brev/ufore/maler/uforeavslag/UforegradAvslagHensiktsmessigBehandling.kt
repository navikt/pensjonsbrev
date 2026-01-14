package no.nav.pensjon.brev.ufore.maler.uforeavslag

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_AVSLAG_OKT_GRAD_HENSIKTSMESSIG_BEHANDLING
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
object UforegradAvslagHensiktsmessigBehandling : RedigerbarTemplate<UforeAvslagEnkelDto> {

    override val kode = UT_AVSLAG_OKT_GRAD_HENSIKTSMESSIG_BEHANDLING
    override val kategori = TemplateDescription.Brevkategori.VEDTAK_ENDRING_OG_REVURDERING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.UFOREP)


    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Avslag uføretrygd - 12-5",
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
                text(bokmal { +"Vi avslår søknaden din fordi du ikke har gjennomført all hensiktsmessig behandling som kan bedre inntektsevnen din." },
                    nynorsk { +"Vi avslår søknaden din fordi du ikkje har gjennomført all føremålstenleg behandling som kan betre inntektsevna di." })
            }
            paragraph {
                text(bokmal { +"Å ha gjennomført «all hensiktsmessig behandling» betyr at all medisinsk behandling som kan bidra til at du blir friskere og kommer i arbeid, skal være forsøkt." },
                    nynorsk { +"Å ha gjennomført «all føremålstenleg behandling» betyr at all medisinsk behandling som kan bidra til at du blir friskare og kjem i arbeid, skal vere forsøkt." })
            }
            paragraph {
                text(bokmal { +"Som hovedregel vil vi vurdere at all hensiktsmessig behandling ikke er gjennomført dersom " },
                    nynorsk { +"Som hovudregel vil vi vurdere at all føremålstenleg behandling ikkje er gjennomført dersom " })
                list {
                    item {
                        text(
                            bokmal { +"du er i behandling eller under utredning" },
                            nynorsk { +"du er i behandling eller under utgreiing" }
                        )
                    }
                    item {
                        text(
                            bokmal { +"du er henvist til utredning/behandling" },
                            nynorsk { +"du er vist til utgreiing/behandling" }
                        )
                    }
                    item {
                        text(
                            bokmal { +"du er anbefalt utredning/behandling" },
                            nynorsk { +"du er tilrådd utgreiing/behandling" }
                        )
                    }
                }
            }

            showIf(saksbehandlerValg.VisVurderingFraVilkarvedtak) {
                paragraph {
                    text(bokmal { + pesysData.vurdering },
                        nynorsk { + pesysData.vurdering })
                }
            }
            paragraph {
                text(bokmal { + fritekst("Individuell vurdering") },
                    nynorsk { + fritekst("Individuell vurdering") })
            }

            paragraph {
                text(bokmal {
                    +"Vi kan ikke utelukke at behandling kan bedre funksjons- og inntektsevnen din. " +
                            "Før du har forsøkt all hensiktsmessig behandling, er det for tidlig å ta stilling til om arbeidsrettede tiltak er prøvd. "
                },
                    nynorsk {
                        +"Vi kan ikkje utelukke at behandling kan betre funksjons- og inntektsevna di. " +
                                "Før du har forsøkt all føremålstenleg behandling, er det for tidleg å ta stilling til om arbeidsretta tiltak er prøvd. "
                    }
                )
            }
            paragraph {
                text(bokmal {+"Fordi behandling og arbeidsrettede tiltak ikke er gjennomført, " +
                        "kan vi ikke ta stilling til om inntektsevnen din er varig nedsatt med mer enn " +
                        fritekst("Nåværende uføregrad") + " prosent."
                },
                    nynorsk {
                        +"Fordi behandling og arbeidsretta tiltak ikkje er gjennomført, " +
                                "kan vi ikkje ta stilling til om inntektsevna di er varig nedsett med meir enn " +
                                fritekst("Nåværende uføregrad") + " prosent."
                    }
                )
            }
            paragraph {
                text(bokmal {+ "Du oppfyller ikke vilkårene, og vi avslår derfor søknaden din om økt uføregrad."},
                    nynorsk {+ "Du oppfyller ikkje vilkåra, og vi avslår derfor søknaden din om auka uføregrad."})
            }
            paragraph {
                text(bokmal { + "Vedtaket har vi gjort etter folketrygdloven §§ 12-5 til 12-7 og 12-10." },
                    nynorsk { + "Vedtaket har vi gjort etter folketrygdlova §§ 12-5 til 12-7 og 12-10." })
            }

            includePhrase(Felles.RettTilAKlageLang)
            includePhrase(Felles.RettTilInnsynRefVedlegg)
            includePhrase(Felles.HarDuSporsmal)
        }
        includeAttachment(vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk)
    }
}