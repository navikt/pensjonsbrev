package no.nav.pensjon.brev.ufore.maler.uforeavslag

import no.nav.pensjon.brev.ufore.api.model.maler.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.LocalizedFormatter.CurrencyFormat
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_AVSLAG_INNTEKTSEVNE_50
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagInntektDto
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagInntektDtoSelectors.SaksbehandlervalgInntektSelectors.visUnntaksregelFremtidigInntekt
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagInntektDtoSelectors.SaksbehandlervalgInntektSelectors.visVurderingIEU
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagInntektDtoSelectors.SaksbehandlervalgInntektSelectors.visVurderingIFU
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagInntektDtoSelectors.UforeAvslagInntektPendataSelectors.inntektEtterUforhet
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagInntektDtoSelectors.UforeAvslagInntektPendataSelectors.inntektForUforhet
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagInntektDtoSelectors.UforeAvslagInntektPendataSelectors.kravMottattDato
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagInntektDtoSelectors.UforeAvslagInntektPendataSelectors.uforetidspunkt
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagInntektDtoSelectors.UforeAvslagInntektPendataSelectors.vurderingIEU
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagInntektDtoSelectors.UforeAvslagInntektPendataSelectors.vurderingIFU
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagInntektDtoSelectors.pesysData
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagInntektDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brev.ufore.maler.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VEDTAK

@TemplateModelHelpers
object UforeAvslagInntektsevne50 : RedigerbarTemplate<UforeAvslagInntektDto> {

    override val kode = UT_AVSLAG_INNTEKTSEVNE_50
    override val kategori = TemplateDescription.Brevkategori.FOERSTEGANGSBEHANDLING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.UFOREP)


    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Avslag uføretrygd - 12-7",
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
                text(bokmal { +"Vi har avslått søknaden din om uføretrygd fordi inntektsevnen din er varig nedsatt med mindre enn 50 prosent." },
                    nynorsk { +"Vi har avslått søknaden din om uføretrygd fordi inntektsevna di er varig redusert med mindre enn 50 prosent." })
            }

            title1 {
                text(bokmal { +"Slik har vi beregnet inntektsevnen din" },
                    nynorsk { +"Slik har vi berekna inntektsevna di" })
            }
            paragraph {
                text(bokmal { +"Vi sammenligner inntekten din før og etter at du ble ufør for å avgjøre i hvor stor grad inntektsevnen din er nedsatt." },
                    nynorsk { +"Vi samanliknar inntekta di før og etter at du blei ufør for å avgjere i kor stor grad inntektsevna di er nedsett." })
            }
            paragraph {
                text(bokmal { +"Uføretidspunktet ditt er satt til " + pesysData.uforetidspunkt.format() + ". " +
                        "På dette tidspunktet vurderer vi at dine helseutfordringer førte til at din arbeidsevne ble varig nedsatt. " },
                    nynorsk { +"Uføretidspunktet ditt er sett til " + pesysData.uforetidspunkt.format() + ". " +
                        "På dette tidspunktet vurderer vi at helseutfordringane dine førte til at arbeidsevna di blei varig nedsett. " })
            }

            paragraph {
                text(bokmal { +"Inntekten din før du ble ufør er fastsatt til " + pesysData.inntektForUforhet.format(CurrencyFormat) + " kroner." },
                    nynorsk { +"Inntekta di før du blei ufør er fastsett til " + pesysData.inntektForUforhet.format(CurrencyFormat) + " kroner." }
                )
                showIf(saksbehandlerValg.visVurderingIFU) {
                    text( bokmal { + pesysData.vurderingIFU },
                        nynorsk { + pesysData.vurderingIFU } )
                }.orShow {
                    text( bokmal { + fritekst("Begrunnelse for IFU") },
                        nynorsk { + fritekst("Begrunnelse for IFU") } )
                }
                text(bokmal { + " Oppjustert til dagens verdi tilsvarer dette en inntekt på " + fritekst("oppjustert IFU") + " kroner. " +
                    "Inntekt etter uførhet er satt til " + pesysData.inntektEtterUforhet.format(CurrencyFormat) + " kroner. " },
                    nynorsk { + " Oppjustert til dagens verdi tilsvarar dette ein inntekt på " + fritekst("oppjustert IFU") + " kroner. " +
                        "Inntekt etter uførleik er satt til " + pesysData.inntektEtterUforhet.format(CurrencyFormat) + " kroner. " }
                )
                showIf(saksbehandlerValg.visVurderingIEU) {
                    text( bokmal { + pesysData.vurderingIEU },
                        nynorsk { + pesysData.vurderingIEU } )
                }.orShow {
                    text( bokmal { + fritekst("Begrunnelse for IEU") },
                        nynorsk { + fritekst("Begrunnelse for IEU") } )
                }
            }
            paragraph {
                text(bokmal { + "For å få uføretrygd må inntektsevnen din som hovedregel være varig nedsatt med minst 50 prosent."},
                    nynorsk { + "For å få uføretrygd må inntektsevna di som hovudregel vere varig nedsett med minst 50 prosent." })
            }
            paragraph {
                text(bokmal { + "Vi har sammenlignet inntekt din før og etter at du ble ufør og kommet til at din uføregrad er " + fritekst("grad før avrunding") + " prosent."},
                    nynorsk { + "Vi har samanlikna inntekta di før og etter at du blei ufør og kome fram til at uføregraden din er " + fritekst("grad før avrunding") + " prosent." })
            }
            paragraph {
                text(bokmal { + "Uføregraden din er under 50 prosent, og du omfattes heller ikke av unntaksreglene ved yrkesskade, yrkessykdom eller for personer som mottar arbeidsavklaringspenger på søknadstidspunktet."},
                    nynorsk { + "Uføregraden din er under 50 prosent, og du omfattast heller ikkje av unntaksreglane ved yrkesskade, yrkessjukdom eller for personar som mottek arbeidsavklaringspengar på søknadstidspunktet." })
            }

            showIf(saksbehandlerValg.visUnntaksregelFremtidigInntekt) {
                title1 {
                    text(bokmal { +"Unntaksregel om fremtidig inntekt" },
                        nynorsk { +"Unntaksregel om framtidig inntekt" })
                }
                paragraph {
                    text(bokmal { +"Etter unntaksregel kan inntekt før uførhet settes ut fra den fremtidige inntekten. Unntaksbestemmelsen skal sikre at personen får korrekt uføregrad ut fra hvilken stillingsandel vedkommende klarer å arbeide i. " },
                        nynorsk { +"Etter unntaksregel kan inntekt før uførleik setjast ut frå den framtidige inntekta. Unntaksføresegna skal sikre at personen får korrekt uføregrad ut frå kva stillingsandel vedkommande klarer å arbeide i." })
                }
                paragraph {
                    text(bokmal { +"Arbeidsforholdet må være dokumentert med en arbeidsavtale med en klart angitt stillingsprosent og oppstartsdato. " },
                        nynorsk { +"Arbeidsforholdet må vere dokumentert med ein arbeidsavtale med ein klart angitt stillingsprosent og oppstartsdato. " })
                }
                paragraph {
                    text(bokmal { +"Vi vurderer at denne unntaksbestemmelsen ikke kan brukes i ditt tilfelle, fordi " + fritekst("X (begrunnelse, f.eks. ikke klart angitt stillingsprosent, jobber utover stillingsprosenten, ekstravakter/overtid, bonus, selvstendig næringsdrivende etc)") + ". " },
                        nynorsk { +"Vi vurderer at denne unntaksføresegna ikkje kan brukast i ditt tilfelle, fordi " + fritekst("X (begrunnelse, f.eks. ikkje klart angitt stillingsprosent, jobbar utover stillingsprosenten, ekstravakter/overtid, bonus, sjølvstendig næringsdrivande etc)") + ". " } )
                }
            }

            paragraph {
                text(bokmal { + "Du oppfyller ikke vilkårene, og vi avslår derfor søknaden din om uføretrygd."},
                    nynorsk { + "Du oppfyller ikkje vilkåra, og vi avslår derfor søknaden din om uføretrygd." })
            }
            paragraph {
                text(bokmal { +"Vedtaket har vi gjort etter folketrygdloven § 12-7." },
                    nynorsk { +"Vedtaket har vi gjort etter folketrygdlova § 12-7." })
            }

            includePhrase(Felles.HvaSkjerNa)
            includePhrase(Felles.RettTilAKlageLang)
            includePhrase(Felles.RettTilInnsynRefVedlegg)
            includePhrase(Felles.HarDuSporsmal)
        }
        includeAttachment(vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk)
    }
}
