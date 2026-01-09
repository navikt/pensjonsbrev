package no.nav.pensjon.brev.ufore.maler.uforeavslag

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_AVSLAG_UNG_UFOR_36
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagUforetidspunkt26Dto
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagUforetidspunkt26DtoSelectors.SaksbehandlervalgSelectors.VisVurderingFraVilkarvedtak
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagUforetidspunkt26DtoSelectors.UforeAvslagPendataSelectors.vurdering
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagUforetidspunkt26DtoSelectors.pesysData
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagUforetidspunkt26DtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brev.ufore.maler.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VEDTAK

@TemplateModelHelpers
object UforeAvslagUngUfor36 : RedigerbarTemplate<UforeAvslagUforetidspunkt26Dto> {

    override val kode = UT_AVSLAG_UNG_UFOR_36
    override val kategori = TemplateDescription.Brevkategori.VEDTAK_ENDRING_OG_REVURDERING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.UFOREP)


    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Avslag uføretrygd - 12-13",
            distribusjonstype = VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        ),
    )
    {
        title {
            text (bokmal { + "Nav har vurdert at du ikke har rett til beregning som ung ufør"},
                nynorsk { + "Nav har vurdert at du ikkje har rett til berekning som ung ufør"})
        }
        outline {
            paragraph {
                text(bokmal { +"Du får utbetalt " + fritekst("beløp uføretrygd") + " kroner i måneden, slik det står i vedtaket av " + fritekst("dato") + "." },
                    nynorsk { +"Du får utbetalt " + fritekst("beløp uføretrygd") + " kroner i månaden, slik det står i vedtaket av " + fritekst("dato") + "." })
            }
            title1 {
                text(bokmal { +"Derfor får du ikke rettighet som ung ufør" },
                    nynorsk { +"Derfor får du ikkje rett som ung ufør" })
            }
            paragraph {
                text(bokmal { +"Du får ikke beregning som ung ufør fordi du har jobbet mer enn 50 prosent etter du fylte 26 år og du søkte om rettighet som ung ufør etter at du fylte 36 år." },
                    nynorsk { +"Du får ikkje berekning som ung ufør fordi du har jobba meir enn 50 prosent etter du fylte 26 år og du søkte om rett som ung ufør etter at du fylte 36 år." })
            }
            paragraph {
                text(bokmal { +"For å få uføretrygden beregnet med rettighet som ung ufør, er det et krav at du ble ufør før du fylte 26 år på grunn av en alvorlig og varig sykdom eller skade, som er klart dokumentert. " },
                    nynorsk { +"For å få uføretrygda berekna med rett som ung ufør, er det eit krav at du blei ufør før du fylte 26 år på grunn av ei alvorleg og varig sjukdom eller skade, som er tydeleg dokumentert. " })
            }
            paragraph {
                text(bokmal { +"Søker du etter at du fylte 36 år, må du har jobbet mindre enn 50 prosent etter du fylte 26 år. Du har jobbet mer enn 50 prosent over en lengre periode etter at du fylte 26 år. " },
                    nynorsk { +"Søkjer du etter at du fylte 36 år, må du har jobba mindre enn 50 prosent etter du fylte 26 år. Du har jobba meir enn 50 prosent over ein lengre periode etter at du fylte 26 år. " })
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
                text(bokmal { +"Du oppfyller derfor ikke vilkårene for rettighet som ung ufør. " },
                    nynorsk { +"Du oppfyller derfor ikkje vilkåra for rett som ung ufør. " })
            }
            paragraph {
                text(bokmal { +"Vedtaket har vi gjort etter folketrygdloven § 12-13 tredje ledd." },
                    nynorsk { +"Vedtaket har vi gjort etter folketrygdlova § 12-13 tredje ledd." })
            }

            includePhrase(Felles.RettTilAKlageLang)
            includePhrase(Felles.RettTilInnsynRefVedlegg)
            includePhrase(Felles.HarDuSporsmal)
        }
        includeAttachment(vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk)
    }
}
