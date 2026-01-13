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
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_AVSLAG_HENSIKTSMESSIG_ARB_TILTAK_I2
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
object UforeAvslagHensiktsmessigArbTiltakI2 : RedigerbarTemplate<UforeAvslagEnkelDto> {

    override val kode = UT_AVSLAG_HENSIKTSMESSIG_ARB_TILTAK_I2
    override val kategori = Brevkategori.FOERSTEGANGSBEHANDLING
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
                nynorsk { + "Nav har avslått søknaden din om uføretrygd"})
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
                text(bokmal { +"Vi avslår søknaden din fordi du ikke har gjennomført hensiktsmessige arbeidsrettede tiltak, eller forsøkt annet arbeid som kan bedre inntektsevnen din." },
                    nynorsk { +"Vi avslår søknaden din fordi du ikkje har gjennomført føremålstenlege arbeidsretta tiltak, eller prøvd anna arbeid som kan betre inntektsevna di." })
            }
            paragraph {
                text(bokmal { +"Arbeidsrettede tiltak betyr alle former for tiltak som kan øke din inntektsevne, og hjelpe deg med å skaffe arbeid, øke arbeidsinnsatsen din eller beholde lønnet arbeid. " },
                    nynorsk { +"Arbeidsretta tiltak betyr alle former for tiltak som kan auke inntektsevna di, og hjelpe deg med å skaffe arbeid, auke arbeidsinnsatsen din eller behalde lønna arbeid. " })
            }
            paragraph {
                text(bokmal { +"Du kan bare la være å prøve ut tiltak hvis det er klare grunner til at du ikke bør gjennomføre dette, eller tiltak ikke er hensiktsmessig. Unntaksregelen er streng, og det skal mye til for å ikke måtte gjennomføre tiltak. " },
                    nynorsk { +"Du kan berre la vere å prøve ut tiltak dersom det er klare grunnar til at du ikkje bør gjennomføre dette, eller tiltak ikkje er føremålstenlege. Unntaksregelen er streng, og det skal mykje til for å ikkje måtte gjennomføre tiltak. " })
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
                text(bokmal { +"Før vi kan ta stilling til om inntektsevnen din er varig nedsatt, må du gjennomføre flere tiltak. Det er derfor for tidlig å vurdere til om inntektsevnen din er varig nedsatt som følge av sykdom eller skade. "},
                    nynorsk { +"Før vi kan ta stilling til om inntektsevna di er varig nedsett, må du gjennomføre fleire tiltak. Det er derfor for tidleg å vurdere til om inntektsevna di er varig nedsett som følgje av sjukdom eller skade. " })
            }
            paragraph {
                text(bokmal { + "Du oppfyller ikke vilkårene, og vi avslår derfor søknaden din om uføretrygd."},
                    nynorsk { + "Du oppfyller ikkje vilkåra, og vi avslår derfor søknaden din om uføretrygd." })
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