package no.nav.pensjon.brev.maler.uforeavslag

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.maler.fraser.Felles.*
import no.nav.pensjon.brev.maler.uforeavslag.UforeAvslagIFUOktStilling.fritekst
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagEnkelDto
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagEnkelDtoSelectors.SaksbehandlervalgSelectors.VisVurderingFraVilkarvedtak
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagEnkelDtoSelectors.UforeAvslagPendataSelectors.kravMottattDato
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagEnkelDtoSelectors.UforeAvslagPendataSelectors.vurdering
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagEnkelDtoSelectors.pesysData
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagEnkelDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VEDTAK

@TemplateModelHelpers
object UforeAvslagUngUforVarig : RedigerbarTemplate<UforeAvslagEnkelDto> {

    override val kode = Ufoerebrevkoder.Redigerbar.UT_AVSLAG_UNG_UFOR_VARIG
    override val kategori = TemplateDescription.Brevkategori.VEDTAK_ENDRING_OG_REVURDERING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.UFOREP)


    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Avslag uføretrygd - 12-13",
            isSensitiv = false,
            distribusjonstype = VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        ),
    )
    {
        title {
            text (bokmal { + "Nav har avslått søknaden din om rettighet som ung ufør"})
        }
        outline {
            paragraph {
                text(bokmal { +"Vi har avslått søknaden din om rettighet som ung ufør som vi fikk den " + pesysData.kravMottattDato.format() + "." })
            }
            title1 {
                text(bokmal { +"Derfor får du ikke uføretrygd med ung ufør fordel" })
            }
            paragraph {
                text(bokmal { +"Vi avslår søknaden fordi vi ikke har dokumentasjon som viser at du hadde en varig og alvorlig sykdom før fylte 26 år." })
            }
            paragraph {
                text(bokmal { + "Kravet til alvorlighet er strengere enn for vanlig uføretrygd. Det betyr at du må ha en " +
                        "betydelig mer alvorlig sykdom enn det som normalt gir rett til uføretrygd." })
            }
            paragraph {
                text(bokmal { + "Vi har vurdert all dokumentasjon i saken din, med særlig vekt på opplysninger fra tiden før du fylte 26 år. " +
                        "Det er dokumentert at du ble varig ufør før fylte 26 år på grunn av " + fritekst("diagnose") +
                        ", men sykdommen er ikke vurdert som alvorlig nok til å oppfylle vilkårene for ung ufør. " +
                        "Selv om sykdommen din har blitt betydelig forverret etter at du fylte 26 år, " +
                        "vil ikke det gjøre at du oppfyller vilkårene for å få ung ufør." })
            }

            showIf(saksbehandlerValg.VisVurderingFraVilkarvedtak) {
                paragraph {
                    text(bokmal { +pesysData.vurdering })
                }
            }
            paragraph {
                text(bokmal { + fritekst("Individuell vurdering") })
            }

            paragraph {
                text(bokmal { + "Den medisinske dokumentasjonen viser ikke at sykdommen var både alvorlig og varig før fylte 26 år." })
            }
            paragraph {
                text(bokmal { + "Du oppfyller ikke vilkårene, og vi avslår derfor søknaden din om rettighet som ung uføre." })
            }
            paragraph {
                text(bokmal { +"Vedtaket er gjort etter folketrygdloven § 12-13 tredje ledd." })
            }

            includePhrase(RettTilAKlageLang)
            includePhrase(RettTilInnsynRefVedlegg)
            includePhrase(HarDuSporsmal)
        }
        includeAttachment(vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk)
    }
}
