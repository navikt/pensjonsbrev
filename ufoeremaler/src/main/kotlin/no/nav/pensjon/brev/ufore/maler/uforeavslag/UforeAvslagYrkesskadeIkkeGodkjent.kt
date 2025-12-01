package no.nav.pensjon.brev.ufore.maler.uforeavslag

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.ufore.maler.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_AVSLAG_YRKESSKADE_IKKE_GODKJENT
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagUtenVurderingDto
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagUtenVurderingDtoSelectors.UforeAvslagPendataSelectors.kravMottattDato
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagUtenVurderingDtoSelectors.pesysData
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VEDTAK

@TemplateModelHelpers
object UforeAvslagYrkesskadeIkkeGodkjent : RedigerbarTemplate<UforeAvslagUtenVurderingDto> {

    override val kode = UT_AVSLAG_YRKESSKADE_IKKE_GODKJENT
    override val kategori = TemplateDescription.Brevkategori.VEDTAK_ENDRING_OG_REVURDERING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.UFOREP)


    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Avslag uføretrygd - 12-17",
            isSensitiv = false,
            distribusjonstype = VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        ),
    )
    {
        title {
            text (bokmal { + "Nav har avslått søknaden din om å få beregnet uføretrygden etter reglene for yrkesskade eller yrkessykdom"})
        }
        outline {
            paragraph {
                text(bokmal { +"Vi har avslått søknaden din som vi fikk den " + pesysData.kravMottattDato.format() + "." })
            }
            title1 {
                text(bokmal { +"Derfor får du ikke uføretrygd beregnet etter dette regelverket" })
            }
            paragraph {
                text(bokmal { + "Vi avslår søknaden din fordi du ikke har en godkjent yrkesskade eller yrkessykdom." })
            }
            paragraph {
                text(bokmal { + "For å ha rett til uføretrygd etter reglende for yrkesskade eller yrkessykdom, må uførheten din skyldes en yrkesskade eller yrkessykdom som er godkjent av Nav." })
            }
            paragraph {
                text(bokmal { + "Du oppfyller ikke vilkårene, og vi avslår derfor søknaden din."})
            }
            paragraph {
                text(bokmal { +"Vedtaket har vi gjort etter folketrygdloven § 12-17." })
            }

            includePhrase(Felles.RettTilAKlageLang)
            includePhrase(Felles.RettTilInnsynRefVedlegg)
            includePhrase(Felles.HarDuSporsmal)
        }
        includeAttachment(vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk)
    }
}
