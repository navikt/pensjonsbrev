package no.nav.pensjon.brev.ufore.maler.uforeavslag

import no.nav.pensjon.brev.ufore.api.model.maler.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.ufore.maler.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_AVSLAG_HENSIKTSMESSIG_BEHANDLING
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagEnkelDto
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagEnkelDtoSelectors.SaksbehandlervalgSelectors.VisVurderingFraVilkarvedtak
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagEnkelDtoSelectors.UforeAvslagPendataSelectors.kravMottattDato
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagEnkelDtoSelectors.UforeAvslagPendataSelectors.vurdering
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagEnkelDtoSelectors.pesysData
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagEnkelDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VEDTAK

@TemplateModelHelpers
object UforeAvslagHensiktsmessigBehandling : RedigerbarTemplate<UforeAvslagEnkelDto> {

    override val kode = UT_AVSLAG_HENSIKTSMESSIG_BEHANDLING
    override val kategori = TemplateDescription.Brevkategori.FOERSTEGANGSBEHANDLING
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
            text (bokmal { + "Nav har avslått søknaden din om uføretrygd"},
                nynorsk { + "Nav har avslått søknaden din om uføretrygd" })
        }
        outline {
            paragraph {
                text(bokmal { +"Vi har avslått søknaden din om uføretrygd som vi fikk den " + pesysData.kravMottattDato.format() + "." },
                    nynorsk { +"Vi har avslått søknaden din om uføretrygd som vi fekk den " + pesysData.kravMottattDato.format() + "." })
            }
            title1 {
                text(bokmal { +"Derfor får du ikke uføretrygd" },
                    nynorsk { +"Derfor får du ikkje uføretrygd" })
            }
            paragraph {
                text(bokmal { +"Vi avslår søknaden din fordi du ikke har gjennomført all hensiktsmessig behandling, som kan bedre inntektsevnen din." },
                    nynorsk { +"Vi avslår søknaden din fordi du ikkje har gjennomført all føremålstenleg behandling, som kan betre inntektsevna di." })
            }
            paragraph {
                text(bokmal { +"Å ha gjennomført «all hensiktsmessig behandling» betyr at all medisinsk behandling som kan bidra til at du blir friskere og kommer i arbeid, skal være forsøkt." },
                    nynorsk { +"Å ha gjennomført «all føremålstenleg behandling» betyr at all medisinsk behandling som kan bidra til at du blir friskare og kjem i arbeid, skal vere forsøkt." })
            }
            paragraph {
                text(bokmal { +"Som hovedregel vil vi vurderer at all hensiktsmessig behandling ikke er gjennomført dersom " },
                    nynorsk { +"Som hovudregel vil vi vurderer at all føremålstenleg behandling ikkje er gjennomført dersom " })
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
                            nynorsk { +"du er henvist til utgreiing/behandling" }
                        )
                    }
                    item {
                        text(
                            bokmal { +"du er anbefalt utredning/behandling" },
                            nynorsk { +"du er anbefalt utgreiing/behandling" }
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
                    nynorsk { + fritekst("Individuell vurdering") }
                )
            }

            paragraph {
                text(bokmal { +"Hvor mye og hva du kan jobbe med kan ikke avklares før vi ser hvordan videre behandling virker." },
                    nynorsk { +"Kor mykje og kva du kan jobbe med kan ikkje avklarast før vi ser korleis vidare behandling verkar." })
            }
            paragraph {
                text(bokmal { +"Vi kan derfor ikke vurdere om sykdom eller skade har ført til at inntektsevnen din er varig nedsatt. Du oppfyller ikke vilkårene, og vi avslår derfor søknaden din om uføretrygd." },
                    nynorsk { +"Vi kan derfor ikkje vurdere om sjukdom eller skade har ført til at inntektsevna di er varig nedsett. Du oppfyller ikkje vilkåra, og vi avslår derfor søknaden din om uføretrygd." })
            }
            paragraph {
                text(bokmal { +"Vedtaket har vi gjort etter folketrygdloven §§ 12-5 til 12-7." },
                    nynorsk { +"Vedtaket har vi gjort etter folketrygdlova §§ 12-5 til 12-7." })
            }

            includePhrase(Felles.HvaSkjerNa)
            includePhrase(Felles.RettTilAKlageLang)
            includePhrase(Felles.RettTilInnsynRefVedlegg)
            includePhrase(Felles.HarDuSporsmal)
        }
        includeAttachment(vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk)
    }
}