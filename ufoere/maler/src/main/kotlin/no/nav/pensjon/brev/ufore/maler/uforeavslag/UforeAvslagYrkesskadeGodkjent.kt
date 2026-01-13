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
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_AVSLAG_YRKESSKADE_GODKJENT
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagEnkelDto
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagEnkelDtoSelectors.SaksbehandlervalgSelectors.VisVurderingFraVilkarvedtak
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagEnkelDtoSelectors.UforeAvslagPendataSelectors.vurdering
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagEnkelDtoSelectors.pesysData
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagEnkelDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.ufore.maler.Brevkategori
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brev.ufore.maler.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VEDTAK

@TemplateModelHelpers
object UforeAvslagYrkesskadeGodkjent : RedigerbarTemplate<UforeAvslagEnkelDto> {

    override val kode = UT_AVSLAG_YRKESSKADE_GODKJENT
    override val kategori = Brevkategori.VEDTAK_ENDRING_OG_REVURDERING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.UFOREP)


    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Avslag uføretrygd - 12-17",
            distribusjonstype = VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        ),
    )
    {
        title {
            text (bokmal { + "Nav har vurdert at vi ikke kan beregne uføretrygden din etter reglene for yrkesskade og yrkessykdom"},
                nynorsk { + "Nav har vurdert at vi ikkje kan berekne uføretrygda di etter reglane for yrkesskade og yrkessjukdom"})
        }
        outline {
            paragraph {
                text(bokmal { +"Du får utbetalt " + fritekst("beløp uføretrygd") + " kroner i måneden, slik det står i vedtaket av " + fritekst("dato") + "." },
                    nynorsk { +"Du får utbetalt " + fritekst("beløp uføretrygd") + " kroner i månaden, slik det står i vedtaket av " + fritekst("dato") + "." })
            }
            title1 {
                text(bokmal { +"Derfor får du ikke uføretrygd beregnet etter dette regelverket" },
                    nynorsk { +"Derfor får du ikkje uføretrygd berekna etter dette regelverket" })
            }
            paragraph {
                text( bokmal { + "Du får ikke beregning etter regler for yrkesskade eller yrkessykdom fordi det er andre sykdomsforhold enn din godkjente yrkesskade som er årsaken til den nedsatte inntektsevnen din. " +
                        "Du har en godkjent yrkesskade eller yrkessykdom, dette finner du i vedtaket ditt av " + fritekst("dato") + ". " },
                    nynorsk { + "Du får ikkje berekning etter reglar for yrkesskade eller yrkessjukdom fordi det er andre sjukdomsforhold enn den godkjente yrkesskaden din som er årsaka til den reduserte inntektsevna di. " +
                            "Du har ein godkjent yrkesskade eller yrkessjukdom, dette finn du i vedtaket ditt av " + fritekst("dato") + ". " })
            }

            showIf(saksbehandlerValg.VisVurderingFraVilkarvedtak) {
                paragraph {
                    text(bokmal { +pesysData.vurdering },
                        nynorsk { +pesysData.vurdering })
                }
            }
            paragraph {
                text(bokmal { + fritekst("Individuell vurdering") },
                    nynorsk { + fritekst("Individuell vurdering") } )
            }

            paragraph {
                text(bokmal { + "Du oppfyller ikke vilkårene, og vi avslår derfor søknaden din."},
                    nynorsk { + "Du oppfyller ikkje vilkåra, og vi avslår derfor søknaden din." })
            }
            paragraph {
                text(bokmal { +"Vedtaket har vi gjort etter folketrygdloven § 12-17." },
                    nynorsk { +"Vedtaket har vi gjort etter folketrygdlova § 12-17." })
            }

            includePhrase(Felles.RettTilAKlageLang)
            includePhrase(Felles.RettTilInnsynRefVedlegg)
            includePhrase(Felles.HarDuSporsmal)
        }
        includeAttachment(vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk)
    }
}
