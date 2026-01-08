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
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_AVSLAG_ALDER
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
object UforeAvslagAlder : RedigerbarTemplate<UforeAvslagEnkelDto> {

    override val kode = UT_AVSLAG_ALDER
    override val kategori = TemplateDescription.Brevkategori.FOERSTEGANGSBEHANDLING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.UFOREP)


    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Avslag uføretrygd - 12-4",
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
                    nynorsk { +"Difor får du ikkje uføretrygd" })
            }
            paragraph {
                text(bokmal { +"Vi avslår søknaden din fordi du ikke oppfyller kravet om inntekt for søkere mellom 62 og 67 år. " },
                    nynorsk { +"Vi avslår søknaden din fordi du ikkje oppfyller kravet om inntekt for søkjarar mellom 62 og 67 år. " })
            }
            paragraph {
                text(bokmal { +"For å få uføretrygd når du er mellom fylte 62 og 67 år på søknadstidspunktet må " },
                    nynorsk { +"For å få uføretrygd når du er mellom fylte 62 og 67 år på søknadstidspunktet må " })
                list {
                    item {
                        text(bokmal { +"din pensjonsgivende inntekt ha vært minst folketrygdens grunnbeløp i året før uføretidspunktet eller " },
                            nynorsk { +"den pensjonsgivande inntekt di ha vore minst folketrygda sitt grunnbeløp i året før uføretidspunktet eller " } )
                    }
                    item {
                        text(bokmal { +"du ha tjent minst tre ganger folketrygdens grunnbeløp i løpet av de tre siste årene før uføretidspunktet " },
                            nynorsk { +"du ha tent minst tre gonger folketrygda sitt grunnbeløp i løpet av dei tre siste åra før uføretidspunktet " } )
                    }
                }
            }
            paragraph {
                text(bokmal { +"Grunnbeløpet er per i dag " + fritekst("beløp") + " kroner. " },
                    nynorsk { +"Grunnbeløpet er per i dag " + fritekst("beløp") + " kroner. " } )
            }
            paragraph {
                text(bokmal { +"Hvis du ikke oppfyller ett av de to vilkårene over, kan du likevel få uføretrygd hvis du ikke har rett til hel alderspensjon. " },
                    nynorsk { +"Dersom du ikkje oppfyller eitt av dei to vilkåra over, kan du likevel få uføretrygd dersom du ikkje har rett til heil alderspensjon. " } )
            }

            showIf(saksbehandlerValg.VisVurderingFraVilkarvedtak) {
                paragraph {
                    text(bokmal { +pesysData.vurdering },
                        nynorsk { +pesysData.vurdering } )
                }
            }
            paragraph {
                text(bokmal { +fritekst("Individuell vurdering") },
                    nynorsk { +fritekst("Individuell vurdering") } )
            }

            paragraph {
                text(bokmal { +"Din pensjonsgivende inntekt er lavere enn kravene om inntekt for søkere mellom 62 og 67 år. I tillegg har du rett til å ta ut hel alderspensjon."},
                    nynorsk { +"Den pensjonsgivande inntekta di er lågare enn krava om inntekt for søkjarar mellom 62 og 67 år. I tillegg har du rett til å ta ut heil alderspensjon." })
            }
            paragraph {
                text(bokmal { + "Du oppfyller ikke vilkårene, og vi avslår derfor søknaden din om uføretrygd."},
                    nynorsk { + "Du oppfyller ikkje vilkåra, og vi avslår difor søknaden din om uføretrygd." })
            }
            paragraph {
                text(bokmal { +"Vedtaket har vi gjort etter folketrygdloven § 12-4." },
                    nynorsk { +"Vedtaket har vi gjort etter folketrygdlova § 12-4." })
            }

            includePhrase(Felles.HvaSkjerNa)
            includePhrase(Felles.RettTilAKlageLang)
            includePhrase(Felles.RettTilInnsynRefVedlegg)
            includePhrase(Felles.HarDuSporsmal)
        }
        includeAttachment(vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk)
    }
}
