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
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_AVSLAG_OKT_GRAD_HENSIKTSMESSIG_ARB_TILTAK_I2
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
object UforegradAvslagHensiktsmessigArbTiltakI2 : RedigerbarTemplate<UforeAvslagDto> {

    override val kode = UT_AVSLAG_OKT_GRAD_HENSIKTSMESSIG_ARB_TILTAK_I2
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
                text(bokmal { +"Vi avslår søknaden din fordi du ikke har vært gjennom alle passende arbeidsrettede tiltak." })
            }

            showIf(saksbehandlerValg.VisVurderingFraVilkarvedtak) {
                paragraph {
                    text(bokmal { +pesysData.vurdering })
                }
            }
            showIf(saksbehandlerValg.brukVurderingFraVilkarsvedtak) {
                paragraph {
                    text(bokmal { + fritekst("Lim inn teksten fra vilkårsvurderingen her") })
                }
            }.orShow {
                paragraph {
                    text(bokmal { +
                    "Som en del av oppfølgingen er det forsøkt tiltak, blant annet ved " +
                            fritekst("navn på tiltaksarrangør") +
                            " i perioden " + fritekst("dato") +
                            ". Sluttrapporten, referat fra dialogmøte og/eller uttalelse fra arbeidsgiver konkluderer med " +
                            fritekst("kort oppsummering av vurdering") + "."
                    })
                }

                paragraph {
                    text(bokmal { +
                    "Du har utdanning som " + fritekst("utdanning") +
                            ", og har tidligere arbeidet som " + fritekst("yrke") +
                            ". Fastlegen/behandlende lege vurderer " + fritekst("vurdering") +
                            ", mens rådgivende lege i Nav vurderer " + fritekst("vurdering") +
                            ". Det lokale Nav-kontoret har konkludert med " + fritekst("konklusjon") +
                            ". Vi har ut fra sakens opplysninger vurdert at du har gjennomført hensiktsmessig behandling."
                    })
                }

                paragraph {
                    text(bokmal { +
                    "Sett i sammenheng med " + fritekst("X (f.eks. utdanning, arbeidserfaring, alder)") +
                            " og funksjonsnedsettelsen, vurderer vi at flere arbeidsrettede tiltak er hensiktsmessig" +
                            " for å bedre og/eller avklare din inntektsevne."
                    })
                }
            }

            paragraph {
                text(bokmal { +
                "Vi vurderer at du har gjennomført relevant behandling, men ikke alle passende arbeidsrettede tiltak. Du har heller ikke prøvd annet arbeid som kan bedre inntektsevnen din. " +
                        "Før vi kan ta stilling til om inntektsevnen din er varig nedsatt, må du delta i flere passende tiltak. "})
            }
            paragraph {
                text(bokmal { + "Vi kan derfor ikke ta stilling til om inntektsevnen din er varig nedsatt med mer enn " + fritekst("Nåværende uføregrad") + " prosent. "})
            }
            paragraph {
                text(bokmal { + "Du oppfyller ikke vilkårene, og vi avslår derfor søknaden din om økt uføregrad."})
            }
            paragraph {
                text(bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-5 til 12-7 og 12-10." })
            }

            includePhrase(RettTilAKlageLang)
            includePhrase(RettTilInnsyn)
            includePhrase(HarDuSporsmal)
        }
        includeAttachment(vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk)
    }
}