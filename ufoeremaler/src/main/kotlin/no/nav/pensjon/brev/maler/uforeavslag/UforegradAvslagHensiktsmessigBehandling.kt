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
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_AVSLAG_OKT_GRAD_HENSIKTSMESSIG_BEHANDLING
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagEnkelDto
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagEnkelDtoSelectors.SaksbehandlervalgSelectors.VisVurderingFraVilkarvedtak
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagEnkelDtoSelectors.UforeAvslagPendataSelectors.kravMottattDato
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagEnkelDtoSelectors.UforeAvslagPendataSelectors.vurdering
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagEnkelDtoSelectors.pesysData
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagEnkelDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VEDTAK

@TemplateModelHelpers
object UforegradAvslagHensiktsmessigBehandling : RedigerbarTemplate<UforeAvslagEnkelDto> {

    override val kode = UT_AVSLAG_OKT_GRAD_HENSIKTSMESSIG_BEHANDLING
    override val kategori = TemplateDescription.Brevkategori.VEDTAK_ENDRING_OG_REVURDERING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.UFOREP)


    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Avslag uføretrygd - 12-5",
            isSensitiv = false,
            distribusjonstype = VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        ),
    )
    {
        title {
            text (bokmal { + "Nav har avslått søknaden din om økt uføregrad"})
        }
        outline {
            paragraph {
                text(bokmal { +"Vi har avslått din søknad om økt uføregrad som vi fikk den " + pesysData.kravMottattDato.format() + "." })
            }
            title1 {
                text(bokmal { +"Derfor får du ikke økt uføregrad" })
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
                    +"Vi kan ikke utelukke at behandlingen kan bedre funksjons- og inntektsevnen din. " +
                            "Derfor vurderer vi at du må gjennomføre hensiktsmessig behandling. " +
                            "Før du har gått gjennom all hensiktsmessig behandling, er det for tidlig å ta stilling til om arbeidsrettede tiltak er prøvd. "
                })
            }
            paragraph {
                text(bokmal {+"Fordi behandling og arbeidsrettede tiltak ikke er gjennomført, " +
                        "kan vi ikke ta stilling til om inntektsevnen din er varig nedsatt med mer enn " +
                        fritekst("Nåværende uføregrad") + " prosent."
                })
            }
            paragraph {
                text(bokmal {
                    +"Du oppfyller ikke vilkårene, og vi avslår derfor søknaden din om økt uføregrad."
                })
            }
            paragraph {
                text(bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-5 til 12-7 og 12-10." })
            }

            includePhrase(RettTilAKlageLang)
            includePhrase(RettTilInnsynRefVedlegg)
            includePhrase(HarDuSporsmal)
        }
        includeAttachment(vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk)
    }
}