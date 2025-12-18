package no.nav.pensjon.brev.ufore.maler.uforeavslag

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.template.Language.Bokmal
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
import no.nav.pensjon.brev.ufore.maler.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VEDTAK

@TemplateModelHelpers
object UforeAvslagHensiktsmessigBehandling : RedigerbarTemplate<UforeAvslagEnkelDto> {

    override val kode = UT_AVSLAG_HENSIKTSMESSIG_BEHANDLING
    override val kategori = TemplateDescription.Brevkategori.FOERSTEGANGSBEHANDLING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.UFOREP)


    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Avslag uføretrygd - 12-5",
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
                text(bokmal { +"Vi har avslått din søknad om uføretrygd som vi fikk den " + pesysData.kravMottattDato.format() + "." })
            }
            title1 {
                text(bokmal { +"Derfor får du ikke uføretrygd" })
            }
            paragraph {
                text(bokmal { +"Vi avslår søknaden din fordi du ikke har gjennomført all hensiktsmessig behandling, som kan bedre inntektsevnen din." })
            }
            paragraph {
                text(bokmal { +"Å ha gjennomført «all hensiktsmessig behandling» betyr at all medisinsk behandling som kan bidra til at du blir friskere og kommer i arbeid, skal være forsøkt." })
            }
            paragraph {
                text(bokmal { +"Som hovedregel vil vi vurderer at all hensiktsmessig behandling ikke er gjennomført dersom " })
                list {
                    item {
                        text(
                            bokmal { +"du er i behandling" },
                        )
                    }
                    item {
                        text(
                            bokmal { +"du er henvist til behandling" },
                        )
                    }
                    item {
                        text(
                            bokmal { +"du er anbefalt behandling" },
                        )
                    }
                }
            }

            showIf(saksbehandlerValg.VisVurderingFraVilkarvedtak) {
                paragraph {
                    text(bokmal { + pesysData.vurdering })
                }
            }
            paragraph {
                text(bokmal { + fritekst("Individuell vurdering") })
            }

            paragraph {
                text(bokmal {
                    +"Vi kan ikke utelukke at behandling kan bedre din funksjons- og inntektsevne. " +
                            "Samlet sett vurderer vi det som hensiktsmessig at du forsøker behandling. Fordi du ikke har fått " +
                            "all hensiktsmessig behandling, er det for tidlig å ta stilling til om hensiktsmessig arbeidsrettede tiltak er prøvd."
                })
            }
            paragraph {
                text(bokmal {
                    +"Vi kan derfor ikke vurdere om sykdom eller skade har ført til at inntektsevnen din er varig nedsatt. " +
                            "Du oppfyller ikke vilkårene, og vi avslår derfor søknaden din om uføretrygd."
                })
            }
            paragraph {
                text(bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-5 til 12-7." })
            }

            includePhrase(Felles.HvaSkjerNa)
            includePhrase(Felles.RettTilAKlageLang)
            includePhrase(Felles.RettTilInnsynRefVedlegg)
            includePhrase(Felles.HarDuSporsmal)
        }
        includeAttachment(vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk)
    }
}