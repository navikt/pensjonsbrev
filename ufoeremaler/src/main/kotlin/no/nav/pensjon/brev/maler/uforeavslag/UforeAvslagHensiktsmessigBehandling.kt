package no.nav.pensjon.brev.maler.uforeavslag

import no.nav.pensjon.brev.FeatureToggles
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
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.*
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagDto
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagDtoSelectors.SaksbehandlervalgSelectors.VisVurderingFraVilkarvedtak
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagDtoSelectors.SaksbehandlervalgSelectors.brukVurderingFraVilkarsvedtak
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagDtoSelectors.UforeAvslagPendataSelectors.kravMottattDato
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagDtoSelectors.UforeAvslagPendataSelectors.vurdering
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagDtoSelectors.pesysData
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VEDTAK

@TemplateModelHelpers
object UforeAvslagHensiktsmessigBehandling : RedigerbarTemplate<UforeAvslagDto> {

    override val featureToggle = FeatureToggles.uforeAvslag.toggle

    override val kode = UT_AVSLAG_HENSIKTSMESSIG_BEHANDLING
    override val kategori = TemplateDescription.Brevkategori.FOERSTEGANGSBEHANDLING
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
            showIf(saksbehandlerValg.VisVurderingFraVilkarvedtak) {
                paragraph {
                    text(
                        bokmal { + pesysData.vurdering }
                    )
                }
            }.orShowIf(saksbehandlerValg.brukVurderingFraVilkarsvedtak) {
                paragraph {
                    text(bokmal { + fritekst("Lim inn teksten fra vilkårsvurderingen her") })
                }
            }.orShow {
                paragraph {
                    text(bokmal { +"Funksjonsnedsettelsen vises i form av " + fritekst("X") })
                    text(bokmal {
                        +". Følgene av dette er at du ikke klarer å fungere i arbeid. Det er forsøkt behandling i form av " + fritekst(
                            "X"
                        )
                    })
                    text(bokmal {
                        +". Det er for tidlig å ta stilling til effekten av behandlingen fordi du " +
                                fritekst("fortsatt er under behandling/det finnes behandling som ikke er forsøkt.")
                    })
                }
                paragraph {
                    text(bokmal { +"Fastlegen din vurderer at videre behandling i form av " + fritekst("X") })
                    text(bokmal { +"kan bedre inntektsevnen din. Rådgivende lege/spesialist vurderer " + fritekst("X") + "." })
                }
                paragraph {
                    text(bokmal {
                        +"Vi mener at du ikke har fått all hensiktsmessig behandling som kan bedre inntektsevnen. " +
                                fritekst(
                                    "(Individuell begrunnelse, vær konkret, f.eks utredning, samarbeidende spesialist, " +
                                            "anbefalt konkret behandling som ikke er forsøkt, våre retningslinjer ved spesielle sykdomstilstander, alder)."
                                )
                    })

                }
            }
            paragraph {
                text(bokmal {
                    +"Det kan ikke utelukkes at behandlingen kan bedre funksjons- og inntektsevnen. " +
                            "Samlet sett vurderer vi det som hensiktsmessig at behandlingen forsøkes. Fordi du ikke har fått " +
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

            includePhrase(HvaSkjerNa)
            includePhrase(RettTilAKlage)
            includePhrase(RettTilInnsyn)
            includePhrase(HarDuSporsmal)
        }
        includeAttachment(vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk)
    }
}