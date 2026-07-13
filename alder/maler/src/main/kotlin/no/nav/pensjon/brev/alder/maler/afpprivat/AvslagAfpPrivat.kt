package no.nav.pensjon.brev.alder.maler.afpprivat

import no.nav.pensjon.brev.alder.maler.Brevkategori
import no.nav.pensjon.brev.alder.maler.afpprivat.fraser.AfpPrivatFraser
import no.nav.pensjon.brev.alder.maler.brev.FeatureToggles
import no.nav.pensjon.brev.alder.maler.felles.Constants
import no.nav.pensjon.brev.alder.maler.felles.HarDuSpoersmaal
import no.nav.pensjon.brev.alder.maler.felles.Vedtak
import no.nav.pensjon.brev.alder.maler.vedlegg.vedleggFolketrygden
import no.nav.pensjon.brev.alder.model.Aldersbrevkoder
import no.nav.pensjon.brev.alder.model.Sakstype
import no.nav.pensjon.brev.alder.model.afpprivat.AvslagAfpPrivatDto
import no.nav.pensjon.brev.alder.model.afpprivat.AvslagAfpPrivatDto.Begrunnelse
import no.nav.pensjon.brev.alder.model.afpprivat.selectors.avslagAfpPrivatDto.pesysData.*
import no.nav.pensjon.brev.alder.model.afpprivat.selectors.avslagAfpPrivatDto.*
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.TemplateDescription.ISakstype
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

/**
 * Vedtak — avslag på AFP i privat sektor (redigerbar).
 *
 * Konvertert fra Exstream-malen `PE_AF_04_112`. Begrunnelsen for avslaget
 * kommer fra vilkårsvedtaket i PESYS (rådatastrengen
 * `PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse[1]`) og er
 * representert som [Begrunnelse]. Brevkroppen viser standardteksten for
 * den begrunnelsen pluss tilhørende hjemmelshenvisning til
 * AFP-tilskottsloven.
 */
@TemplateModelHelpers
object AvslagAfpPrivat : RedigerbarTemplate<AvslagAfpPrivatDto> {

    override val kode = Aldersbrevkoder.Redigerbar.PE_AFP_AVSLAG

    override val featureToggle = FeatureToggles.avslagAfpPrivat.toggle

    override val kategori = Brevkategori.FOERSTEGANGSBEHANDLING

    override val brevkontekst: TemplateDescription.Brevkontekst = TemplateDescription.Brevkontekst.VEDTAK

    override val sakstyper: Set<ISakstype> = setOf(Sakstype.AFP_PRIVAT)

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - avslag på AFP i privat sektor",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title {
            text(
                bokmal { +"Søknaden din om avtalefestet pensjon (AFP) i privat sektor er avslått - melding om vedtak" },
                nynorsk { +"Søknaden din om avtalefesta pensjon (AFP) i privat sektor er avslått - melding om vedtak" },
                english { +"Your application for contractual pension (AFP) in the private sector has been denied - notification of decision" },
            )
        }

        outline {
            paragraph {
                text(
                    bokmal {
                        +"Nav viser til søknaden din, mottatt " + pesysData.kravMottattDato.format() +
                            ", om AFP i privat sektor. Søknaden din er avslått."
                    },
                    nynorsk {
                        +"Nav viser til søknaden din, motteken " + pesysData.kravMottattDato.format() +
                            ", om AFP i privat sektor. Søknaden din er avslått."
                    },
                    english {
                        +"Nav makes reference to your application which we received on " +
                            pesysData.kravMottattDato.format() +
                            " for contractual pension in the private sector. Your application has been denied."
                    },
                )
            }

            includePhrase(Vedtak.BegrunnelseOverskrift)

            paragraph {
                text(
                    bokmal {
                        +"AFP i privat sektor gis etter bestemmelsene i lov om statstilskott til " +
                            "arbeidstakere som tar ut avtalefestet pensjon i privat sektor " +
                            "(AFP-tilskottsloven). Fellesordningen for AFP har funnet at du oppfyller " +
                            "de avtalemessige vilkårene for rett til AFP. Nav har avgjort andre " +
                            "spørsmål om retten til pensjon."
                    },
                    nynorsk {
                        +"AFP i privat sektor blir gitt etter reglane i lov om statstilskot til " +
                            "arbeidstakarar som tek ut avtalefesta pensjon i privat sektor " +
                            "(AFP-tilskotslova). Fellesordninga for AFP har funne at du oppfyller " +
                            "dei avtalemessige vilkåra for rett til AFP. Nav har avgjort andre " +
                            "spørsmål om retten til pensjon."
                    },
                    english {
                        +"Private sector contractual pension is granted pursuant to the regulations " +
                            "in the act relating to state subsidies to employees who draw an early " +
                            "retirement pension in the private sector (the Early Retirement Pension " +
                            "Subsidy Act - AFP-tilskottsloven). The Common Scheme for Contractual " +
                            "Pension has ascertained that you meet the contractual terms for the " +
                            "right to contractual pension. Nav has made a decision on other issues " +
                            "regarding the right to pension."
                    },
                )
            }

            // Hjemmelshenvisning — gruppert per § i AFP-tilskottsloven.
            showIf(pesysData.begrunnelse.isOneOf(Begrunnelse.UNDER_62)) {
                paragraph {
                    text(
                        bokmal { +"Dette vedtaket er gjort etter AFP-tilskottsloven paragraf 5 første ledd bokstav a." },
                        nynorsk { +"Dette vedtaket er gjort etter AFP-tilskotslova paragraf 5 første ledd bokstav a." },
                        english { +"This decision was made pursuant to section 5, subsection 1, letter a of the Early Retirement Pension Subsidy Act." },
                    )
                }
            }

            showIf(pesysData.begrunnelse.isOneOf(Begrunnelse.BRUKER_AVSLAG_AP, Begrunnelse.BRUKER_IKKE_AP)) {
                paragraph {
                    text(
                        bokmal { +"Dette vedtaket er gjort etter AFP-tilskottsloven paragraf 5 første ledd bokstav b." },
                        nynorsk { +"Dette vedtaket er gjort etter AFP-tilskotslova paragraf 5 første ledd bokstav b." },
                        english { +"This decision was made pursuant to section 5, subsection 1, letter b of the Early Retirement Pension Subsidy Act." },
                    )
                }
            }

            showIf(pesysData.begrunnelse.isOneOf(Begrunnelse.BRUKER_UP_E_62, Begrunnelse.BRUKER_LOP_UP, Begrunnelse.BRUKER_UT_E_62)) {
                paragraph {
                    text(
                        bokmal { +"Dette vedtaket er gjort etter AFP-tilskottsloven paragraf 8 første ledd." },
                        nynorsk { +"Dette vedtaket er gjort etter AFP-tilskotslova paragraf 8 første ledd." },
                        english { +"This decision was made pursuant to section 8, subsection 1 of the Early Retirement Pension Subsidy Act." },
                    )
                }
            }

            showIf(pesysData.begrunnelse.isOneOf(Begrunnelse.BRUKER_AVSLAG_AP)) {
                paragraph {
                    text(
                        bokmal {
                            +"AFP i privat sektor før fylte 70 år må tas ut sammen med alderspensjon " +
                                "fra folketrygden. Søknaden din om avtalefestet pensjon er avslått " +
                                "fordi du har fått avslag på søknaden din om alderspensjon."
                        },
                        nynorsk {
                            +"AFP i privat sektor før fylte 70 år må takast ut saman med alderspensjon " +
                                "frå folketrygda. Søknaden din om avtalefesta pensjon er avslått fordi " +
                                "du har fått avslag på søknaden din om alderspensjon."
                        },
                        english {
                            +"Contractual pension in the private sector before the age of 70 must be " +
                                "drawn together with national insurance retirement pension. Your " +
                                "application for contractual pension has been denied because your " +
                                "application for retirement pension has been denied."
                        },
                    )
                }
            }

            // TODO denne kan komme dobbelt opp i exstream i dag dersom
            //   PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse, 1) = "bruker_ikke_ap
            //   Her må vi ta stilling til når teksten skal komme, enten med eller uten betingelsen.
            includePhrase(AfpPrivatFraser.NySoknadVedSenereRettTilAlderspensjon)

            showIf(pesysData.begrunnelse.isOneOf(Begrunnelse.BRUKER_IKKE_AP)) {
                paragraph {
                    text(
                        bokmal {
                            +"AFP i privat sektor før fylte 70 år må tas ut sammen med alderspensjon " +
                                "fra folketrygden. Søknaden din om avtalefestet pensjon er avslått " +
                                "fordi du verken mottar eller har søkt om å ta ut alderspensjon."
                        },
                        nynorsk {
                            +"AFP i privat sektor før fylte 70 år må takast ut saman med alderspensjon " +
                                "frå folketrygda. Søknaden din om avtalefesta pensjon er avslått fordi " +
                                "du verken får eller har søkt om å ta ut alderspensjon."
                        },
                        english {
                            +"Contractual pension in the private sector before the age of 70 must be " +
                                "drawn together with national insurance retirement pension. Your " +
                                "application for contractual pension has been denied because you are " +
                                "neither receiving nor have applied for retirement pension."
                        },
                    )
                }
            }

            showIf(pesysData.begrunnelse.isOneOf(Begrunnelse.BRUKER_UP_E_62)) {
                paragraph {
                    text(
                        bokmal {
                            +"AFP i privat sektor kan ikke gis til personer som etter fylte 62 år " +
                                "har mottatt uførepensjon fra folketrygden. Søknaden din er avslått " +
                                "fordi du har mottatt uførepensjon etter at du fylte 62 år. Du vil " +
                                "derfor heller ikke senere få rett til AFP."
                        },
                        nynorsk {
                            +"AFP i privat sektor kan ikkje gjevast til personar som etter fylte 62 " +
                                "år har fått uførepensjon frå folketrygda. Søknaden din er avslått " +
                                "fordi du har fått uførepensjon etter at du fylte 62 år. Du vil heller " +
                                "ikkje seinare kunne få rett til AFP i privat sektor."
                        },
                        english {
                            +"Contractual pension in the private sector cannot be granted to people " +
                                "who have received national insurance disability pension after turning " +
                                "62. Your application has been denied because you have received " +
                                "disability pension after turning 62. You will not be entitled to " +
                                "contractual pension in the private sector in the future either."
                        },
                    )
                }
            }

            showIf(pesysData.begrunnelse.isOneOf(Begrunnelse.BRUKER_UT_E_62)) {
                paragraph {
                    text(
                        bokmal {
                            +"AFP i privat sektor kan ikke gis til personer som etter fylte 62 år " +
                                "har mottatt uføretrygd fra folketrygden. Søknaden din er avslått " +
                                "fordi du har mottatt uføretrygd etter at du fylte 62 år. Som " +
                                "hovedregel vil du heller ikke kunne få rett til AFP senere, selv om " +
                                "du skulle si ifra deg uføretrygden. Det finnes enkelte unntak fra " +
                                "denne regelen. Du finner mer informasjon om dette på " +
                                Constants.AFP_PRIVAT_UFORETRYGD_URL + "."
                        },
                        nynorsk {
                            +"AFP i privat sektor kan ikkje gjevast til personar som etter fylte 62 " +
                                "år har fått uføretrygd frå folketrygda. Søknaden din er avslått " +
                                "fordi du har fått uføretrygd etter at du fylte 62 år. Som hovudregel " +
                                "vil du heller ikkje kunne få rett til AFP seinare, sjølv om du " +
                                "skulle seie ifrå deg uføretrygda. Det fins enkelte unntak frå denne " +
                                "regelen. Du finn meir informasjon om dette på " +
                                Constants.AFP_PRIVAT_UFORETRYGD_URL + "."
                        },
                        english {
                            +"Contractual pension in the private sector cannot be granted to people " +
                                "who have received national insurance disability benefit after turning " +
                                "62. Your application has been denied because you have received " +
                                "disability benefit after turning 62. As a general rule, you will not " +
                                "be entitled to contractual pension in the private sector in the " +
                                "future, even if you would waive the right to the disability benefit. " +
                                "There are some exceptions from this rule. You will find more " +
                                "information about this on " + Constants.AFP_PRIVAT_UFORETRYGD_URL + "."
                        },
                    )
                }
            }

            showIf(pesysData.begrunnelse.isOneOf(Begrunnelse.BRUKER_LOP_UP)) {
                paragraph {
                    text(
                        bokmal {
                            +"AFP i privat sektor kan ikke gis til personer som etter fylte 62 år " +
                                "har mottatt uførepensjon eller uføretrygd fra folketrygden. Søknaden " +
                                "din er avslått fordi du mottar uførepensjon eller uføretrygd. Du vil " +
                                "derfor heller ikke senere kunne få rett til AFP i privat sektor."
                        },
                        nynorsk {
                            +"AFP i privat sektor kan ikkje gjevast til personar som etter fylte 62 " +
                                "år har fått uførepensjon frå folketrygda. Søknaden din er avslått " +
                                "fordi du får uførepensjon. Du vil derfor heller ikkje seinare kunne " +
                                "få rett til AFP i privat sektor."
                        },
                        english {
                            +"Contractual pension in the private sector cannot be granted to people " +
                                "who have received national insurance disability pension after turning " +
                                "62. Your application has been denied because you are receiving " +
                                "disability pension. You will therefore not be entitled to contractual " +
                                "pension in the private sector either."
                        },
                    )
                }
            }

            showIf(pesysData.begrunnelse.isOneOf(Begrunnelse.UNDER_62)) {
                paragraph {
                    text(
                        bokmal { +"AFP i privat sektor kan ikke innvilges fordi du ikke har fylt 62 år på virkningstidspunktet." },
                        nynorsk { +"AFP i privat sektor kan ikkje innvilgast fordi du ikkje har fylt 62 år på verknadstidspunktet." },
                        english { +"Your application for contractual pension in the private sector has been denied because you have applied to start drawing your pension before the age of 62." },
                    )
                }
            }

            title1 {
                text(
                    bokmal { +"Din rett til innsyn og klage" },
                    nynorsk { +"Retten din til innsyn og klage" },
                    english { +"Your right to inspection and to file an appeal" },
                )
            }

            includePhrase(AfpPrivatFraser.InnsynForvaltningsloven18)

            includePhrase(AfpPrivatFraser.KlagerettAvslagFolketrygdloven2112)

            includePhrase(HarDuSpoersmaal.afpPrivat)
        }

        includeAttachment(vedleggFolketrygden)
    }
}
