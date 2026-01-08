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
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_AVSLAG_OKT_GRAD_HENSIKTSMESSIG_ARB_TILTAK_I2
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagEnkelDto
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagEnkelDtoSelectors.SaksbehandlervalgSelectors.VisVurderingFraVilkarvedtak
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagEnkelDtoSelectors.UforeAvslagPendataSelectors.kravMottattDato
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagEnkelDtoSelectors.UforeAvslagPendataSelectors.vurdering
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagEnkelDtoSelectors.pesysData
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagEnkelDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.ufore.maler.Brevkategori
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brev.ufore.maler.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VEDTAK

@TemplateModelHelpers
object UforegradAvslagHensiktsmessigArbTiltakI2 : RedigerbarTemplate<UforeAvslagEnkelDto> {

    override val kode = UT_AVSLAG_OKT_GRAD_HENSIKTSMESSIG_ARB_TILTAK_I2
    override val kategori = Brevkategori.VEDTAK_ENDRING_OG_REVURDERING
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
                    nynorsk { +"Difor får du ikkje auka uføregrad" })
            }
            paragraph {
                text(bokmal { +"Vi avslår søknaden din fordi du ikke har gjennomført alle hensiktsmessige arbeidsrettede tiltak." },
                    nynorsk { +"Vi avslår søknaden din fordi du ikkje har gjennomført alle føremålstenlege arbeidsretta tiltak." })
            }
            paragraph {
                text(bokmal { +"Arbeidsrettede tiltak betyr alle former for tiltak som kan øke din inntektsevne, og hjelpe deg med å skaffe arbeid, øke arbeidsinnsatsen din eller beholde lønnet arbeid." },
                    nynorsk { +"Arbeidsretta tiltak tyder alle former for tiltak som kan auke inntektsevna di, og hjelpe deg med å skaffe arbeid, auke arbeidsinnsatsen din eller behalde lønna arbeid." })
            }
            paragraph {
                text(bokmal { +"Du kan bare la være å prøve ut tiltak hvis det er klare grunner til at du ikke bør gjennomføre dette, eller tiltak ikke er hensiktsmessig. Unntaksregelen er streng, og det skal mye til for å ikke måtte gjennomføre tiltak." },
                    nynorsk { +"Du kan berre la vere å prøve ut tiltak dersom det er klare grunnar til at du ikkje bør gjennomføre dette, eller tiltak ikkje er føremålstenlege. Unntaksregelen er streng, og det skal mykje til for å sleppe å måtte gjennomføre tiltak." })
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
                text(bokmal { + "Vi vurderer at du har deltatt i noen arbeidsrettede tiltak, men du har ikke gjennomført alle hensiktsmessige arbeidsrettede tiltak. Du har heller ikke prøvd annet arbeid som kan bedre inntektsevnen din. " +
                        "Før vi kan ta stilling til om inntektsevnen din er varig nedsatt, må du gjennomføre flere arbeidsrettede tiltak. "},
                    nynorsk { + "Vi vurderer at du har delteke i nokre arbeidsretta tiltak, men du har ikkje gjennomført alle føremålstenlege arbeidsretta tiltak. Du har heller ikkje prøvd anna arbeid som kan betre inntektsevna di. " +
                            "Før vi kan ta stilling til om inntektsevna di er varig nedsett, må du gjennomføre fleire arbeidsretta tiltak. " })
            }
            paragraph {
                text(bokmal { + "Vi kan derfor ikke vurdere om inntektsevnen din er varig nedsatt med mer enn " + fritekst("Nåværende uføregrad") + " prosent. "},
                    nynorsk { + "Vi kan derfor ikkje vurdere om inntektsevna di er varig nedsett med meir enn " + fritekst("Nåværende uføregrad") + " prosent. " })
            }
            paragraph {
                text(bokmal { + "Du oppfyller ikke vilkårene, og vi avslår derfor søknaden din om økt uføregrad."},
                    nynorsk { + "Du oppfyller ikkje vilkåra, og vi avslår difor søknaden din om auka uføregrad." })
            }
            paragraph {
                text(bokmal { +"Vedtaket har vi gjort etter folketrygdloven §§ 12-5 til 12-7 og 12-10." },
                    nynorsk { +"Vedtaket har vi gjort etter folketrygdlova §§ 12-5 til 12-7 og 12-10." })
            }

            includePhrase(Felles.RettTilAKlageLang)
            includePhrase(Felles.RettTilInnsynRefVedlegg)
            includePhrase(Felles.HarDuSporsmal)
        }
        includeAttachment(vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk)
    }
}