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
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_AVSLAG_HENSIKTSMESSIG_ARB_TILTAK_I1
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
object UforeAvslagHensiktsmessigArbTiltakI1 : RedigerbarTemplate<UforeAvslagEnkelDto> {

    override val kode = UT_AVSLAG_HENSIKTSMESSIG_ARB_TILTAK_I1
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
                text(bokmal { +"Vi avslår søknaden din fordi du ikke har forsøkt arbeidsrettede tiltak." },
                    nynorsk { +"Vi avslår søknaden din fordi du ikkje har prøvd arbeidsretta tiltak." })
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
                text(bokmal { +"Du har ikke gjennomført nødvendige arbeidsrettede tiltak eller forsøkt annet arbeid som kan bedre inntektsevnen din." },
                    nynorsk { +"Du har ikkje gjennomført nødvendige arbeidsretta tiltak eller prøvd anna arbeid som kan betre inntektsevna di." })
            }
            paragraph {
                text(bokmal { +"Arbeidsrettede tiltak betyr alle former for tiltak som kan øke din inntektsevne, og hjelpe deg med å skaffe arbeid, øke arbeidsinnsatsen din eller beholde lønnet arbeid." },
                    nynorsk { +"Arbeidsretta tiltak betyr alle former for tiltak som kan auke inntektsevna di, og hjelpe deg med å skaffe arbeid, auke arbeidsinnsatsen din eller behalde lønna arbeid." })
            }
            paragraph {
                text(bokmal { +"Du kan bare la være å prøve ut tiltak hvis det er klare grunner til at du ikke bør gjennomføre dette, eller tiltak ikke er hensiktsmessig. Unntaksregelen er streng, og det skal mye til for å ikke måtte gjennomføre tiltak." },
                    nynorsk { +"Du kan berre la vere å prøve ut tiltak dersom det er klare grunnar til at du ikkje bør gjennomføre dette, eller tiltak ikkje er føremålstenlege. Unntaksregelen er streng, og det skal mykje til for å sleppe å gjennomføre tiltak." })
            }
            paragraph {
                text(bokmal { +"Fordi du ikke har forsøkt arbeidsrettede tiltak, er det for tidlig å ta stilling til i hvor stor grad inntektsevnen din er varig nedsatt, og om dette skyldes sykdom eller skade." },
                    nynorsk { +"Fordi du ikkje har prøvd arbeidsretta tiltak, er det for tidleg å ta stilling til i kor stor grad inntektsevna di er varig nedsett, og om dette skuldast sjukdom eller skade." })
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
