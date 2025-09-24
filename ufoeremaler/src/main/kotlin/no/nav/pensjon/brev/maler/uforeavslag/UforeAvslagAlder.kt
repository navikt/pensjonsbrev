package no.nav.pensjon.brev.maler.uforeavslag

import no.nav.pensjon.brev.FeatureToggles
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.maler.fraser.Felles.*
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.LocalizedFormatter.CurrencyFormat
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagGrunnbelopDto
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagGrunnbelopDtoSelectors.SaksbehandlervalgGrunnbelopSelectors.brukVurderingFraVilkarsvedtak
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagGrunnbelopDtoSelectors.UforeAvslagGrunnbelopPendataSelectors.grunnbelop
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagGrunnbelopDtoSelectors.UforeAvslagGrunnbelopPendataSelectors.kravMottattDato
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagGrunnbelopDtoSelectors.UforeAvslagGrunnbelopPendataSelectors.vurdering
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagGrunnbelopDtoSelectors.pesysData
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagGrunnbelopDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VEDTAK

@TemplateModelHelpers
object UforeAvslagAlder : RedigerbarTemplate<UforeAvslagGrunnbelopDto> {

    override val featureToggle = FeatureToggles.uforeAvslag.toggle

    override val kode = Ufoerebrevkoder.Redigerbar.UT_AVSLAG_ALDER
    override val kategori = TemplateDescription.Brevkategori.FOERSTEGANGSBEHANDLING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.UFOREP)


    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Avslag uføretrygd - 12-4",
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
                text(bokmal { +"Vi avslår søknaden din fordi du ikke oppfyller kravet om inntekt for søkere mellom 62 og 67 år. " +
                        "I tillegg har du rett til å ta ut hel alderspensjon fra folketrygden." })
            }
            paragraph {
                text(bokmal { +"år du søker om uføretrygd mellom fylte 62 og 67 år, " +
                        "må din pensjonsgivende inntekt ha vært minst folketrygdens grunnbeløp i året før uføretidspunktet. " +
                        "Hvis du ikke oppfyller dette vilkåret, må du ha tjent minst tre ganger folketrygdens grunnbeløp i løpet av de tre siste årene før uføretidspunktet. " +
                        "Grunnbeløpet utgjør " + pesysData.grunnbelop.format(CurrencyFormat) + " kroner.  I tillegg kan du ikke få gjenlevendepensjon, eller ha rett til å ta ut hel alderspensjon." })
            }
            showIf(saksbehandlerValg.brukVurderingFraVilkarsvedtak) {
                paragraph {
                    text(bokmal { +pesysData.vurdering })
                }
            }.orShow {
                paragraph {
                    text(bokmal {+ fritekst("fritekst for utfyllende opplysninger om bruker") })
                }
            }
            paragraph {
                text(bokmal { +
                "Din pensjonsgivende inntekt er lavere enn kravene om inntekt for søkere mellom 62 og 67 år. I tillegg har du rett til å ta ut hel alderspensjon."})
            }
            paragraph {
                text(bokmal { + "Du oppfyller ikke vilkårene, og vi avslår derfor søknaden din om uføretrygd."})
            }
            paragraph {
                text(bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-6 og 12-7." })
            }

            includePhrase(HvaSkjerNa)
            includePhrase(RettTilAKlage)
            includePhrase(RettTilInnsyn)
            includePhrase(HarDuSporsmal)
        }
        includeAttachment(vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk)
    }
}
