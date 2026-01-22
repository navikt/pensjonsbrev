package no.nav.pensjon.brev.ufore.maler.uforeavslag

import no.nav.pensjon.brev.ufore.maler.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagForverrelseEtter26Dto
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagForverrelseEtter26DtoSelectors.SaksbehandlervalgSelectors.VisVurderingFraVilkarvedtak
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagForverrelseEtter26DtoSelectors.SaksbehandlervalgSelectors.visForverrelseEtter26
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagForverrelseEtter26DtoSelectors.UforeAvslagPendataSelectors.vurdering
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagForverrelseEtter26DtoSelectors.pesysData
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagForverrelseEtter26DtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brev.ufore.maler.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VEDTAK

@TemplateModelHelpers
object UforeAvslagUngUforVarig : RedigerbarTemplate<UforeAvslagForverrelseEtter26Dto> {

    override val kode = Ufoerebrevkoder.Redigerbar.UT_AVSLAG_UNG_UFOR_VARIG
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
                text(bokmal { +"Du får ikke beregning som ung ufør fordi det ikke er dokumentert at du hadde en varig og alvorlig sykdom før fylte 26 år. " },
                    nynorsk { +"Du får ikkje berekning som ung ufør fordi det ikkje er dokumentert at du hadde ein varig og alvorleg sjukdom før du fylte 26 år. " })
            }
            paragraph {
                text(bokmal { + "Kravet til alvorlighet er strengere enn for vanlig uføretrygd. Det betyr at du må ha en " +
                        "betydelig mer alvorlig sykdom enn det som normalt gir rett til uføretrygd." },
                    nynorsk { + "Kravet til alvorlegheit er strengare enn for vanleg uføretrygd. Det betyr at du må ha ein " +
                            "betydeleg meir alvorleg sjukdom enn det som normalt gir rett til uføretrygd." })
            }
            paragraph {
                text(bokmal { + "Vi har vurdert all dokumentasjon i saken din, med særlig vekt på opplysninger fra tiden før du fylte 26 år. " +
                        "Det er dokumentert at du ble varig ufør før fylte 26 år på grunn av " + fritekst("diagnose") +
                        ", men sykdommen er ikke vurdert som alvorlig nok til å oppfylle vilkårene for ung ufør. " },
                    nynorsk { + "Vi har vurdert all dokumentasjon i saka di, med særleg vekt på opplysningar frå tida før du fylte 26 år. " +
                            "Det er dokumentert at du blei varig ufør før fylte 26 år på grunn av " + fritekst("diagnose") +
                            ", men sjukdomen er ikkje vurdert som alvorleg nok til å oppfylle vilkåra for ung ufør. " }
                )
                showIf(saksbehandlerValg.visForverrelseEtter26) {
                    text(bokmal {
                        +"Vi ser at sykdommen din har blitt betydelig forverret etter at du fylte 26 år. " +
                                "Det er helsesituasjonen din før du fylte 26 år som avgjør om du kan få rettighet som ung ufør."
                    },
                        nynorsk {
                            +"Vi ser at sjukdomen din har blitt vesentleg forverra etter at du fylte 26 år. " +
                                    "Det er helsesituasjonen din før du fylte 26 år som avgjer om du kan få rett som ung ufør."
                        }
                    )
                }
            }

            paragraph {
                text(bokmal { + "I vurderingen har vi lagt vekt på: " + fritekst("Sett inn konkret argument med kilde og dato") },
                    nynorsk { + "I vurderinga har vi lagt vekt på: " + fritekst("Sett inn konkret argument med kilde og dato") })
            }

            showIf(saksbehandlerValg.VisVurderingFraVilkarvedtak) {
                paragraph {
                    text(bokmal { +pesysData.vurdering },
                        nynorsk { +pesysData.vurdering })
                }
            }

            paragraph {
                text(bokmal { + "Vi har kommet frem til at det ikke er klart dokumentert at du hadde varig og alvorlig sykdom før fylte 26 år." },
                    nynorsk { + "Vi har kome fram til at det ikkje er klart dokumentert at du hadde varig og alvorleg sjukdom før du fylte 26 år." })
            }
            paragraph {
                text(bokmal { + "Du oppfyller ikke vilkårene, og vi avslår derfor søknaden din om rettighet som ung ufør." },
                    nynorsk { + "Du oppfyller ikkje vilkåra, og vi avslår derfor søknaden din om rett som ung ufør." })
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
