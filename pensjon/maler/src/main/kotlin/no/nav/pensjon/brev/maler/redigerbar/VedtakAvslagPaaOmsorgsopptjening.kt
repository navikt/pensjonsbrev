package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakAvslagPaaOmsorgsopptjeningDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakAvslagPaaOmsorgsopptjeningDtoSelectors.PesysDataSelectors.dineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakAvslagPaaOmsorgsopptjeningDtoSelectors.PesysDataSelectors.navEnhet
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakAvslagPaaOmsorgsopptjeningDtoSelectors.PesysDataSelectors.omsorgGodskrevetAar
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakAvslagPaaOmsorgsopptjeningDtoSelectors.SaksbehandlerValgSelectors.brukerFoedtFoer1948
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakAvslagPaaOmsorgsopptjeningDtoSelectors.SaksbehandlerValgSelectors.omsorgsarbeidEtter69Aar
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakAvslagPaaOmsorgsopptjeningDtoSelectors.SaksbehandlerValgSelectors.omsorgsarbeidFoer1992
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakAvslagPaaOmsorgsopptjeningDtoSelectors.SaksbehandlerValgSelectors.omsorgsarbeidForBarnUnder7aarFoer1992
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakAvslagPaaOmsorgsopptjeningDtoSelectors.SaksbehandlerValgSelectors.omsorgsarbeidMindreEnn22Timer
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakAvslagPaaOmsorgsopptjeningDtoSelectors.SaksbehandlerValgSelectors.omsorgsarbeidMindreEnn22TimerOgMindreEnn6Maaneder
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakAvslagPaaOmsorgsopptjeningDtoSelectors.SaksbehandlerValgSelectors.omsorgsarbeidMindreEnn6Maaneder
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakAvslagPaaOmsorgsopptjeningDtoSelectors.SaksbehandlerValgSelectors.omsorgsopptjeningenGodskrevetEktefellen
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakAvslagPaaOmsorgsopptjeningDtoSelectors.SaksbehandlerValgSelectors.privatAFPavslaat
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakAvslagPaaOmsorgsopptjeningDtoSelectors.pesysData
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakAvslagPaaOmsorgsopptjeningDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.common.Vedtak
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlage
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

// PE_IY_04_010

@TemplateModelHelpers
object VedtakAvslagPaaOmsorgsopptjening : RedigerbarTemplate<VedtakAvslagPaaOmsorgsopptjeningDto> {

    //override val featureToggle = FeatureToggles.vedtakOmInnvilgelseAvOmsorgspoeng.toggle
    override val kode = Pesysbrevkoder.Redigerbar.PE_VEDTAK_AVSLAG_PAA_OMSORGSOPPTJENING
    override val kategori = TemplateDescription.Brevkategori.OMSORGSOPPTJENING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.OMSORG)

    override val template = createTemplate(
        languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - avslag på omsorgsopptjening",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {
        title {
            text(
                bokmal { +"Du har ikke rett til omsorgsopptjening" },
                nynorsk { +"Du har ikkje rett til omsorgsopptening" },
                english { +"You are not entitled to pension rights accrual for care work" }
            )
        }
        outline {
            includePhrase(Vedtak.Overskrift)

            paragraph {
                text(
                    bokmal {
                        +pesysData.navEnhet + " viser til søknaden din om å få godskrevet pensjonsopptjening for " + fritekst(
                            "pleie- og omsorgsarbeid/omsorg for barn under 7 år før 1992"
                        ) + " for følgende år:"
                    },
                    nynorsk {
                        +pesysData.navEnhet + " viser til søknaden din om å få godskriven pensjonsopptening for " + fritekst(
                            "pleie- og omsorgsarbeid/omsorg for barn under 7 år før 1992"
                        ) + " for følgjande år:"
                    },
                    english {
                        +pesysData.navEnhet + " refers to your application to be accredited pension rights for " + fritekst(
                            "nursing and care work/care work for children under 7 years of age before 1992 "
                        ) + " for the following years:"
                    },
                )
                list {
                    forEach(pesysData.omsorgGodskrevetAar) {
                        item {
                            eval(it.format())

                        }
                    }
                }
            }

            paragraph {
                text(
                    bokmal { +"Søknaden er avslått, fordi vilkårene for å få omsorgsopptjening ikke er oppfylt i ditt tilfelle." },
                    nynorsk { +"Søknaden er avslått, fordi vilkåra for å få omsorgsopptening ikkje er oppfylte i ditt tilfelle." },
                    english { +"The application has been denied because you do not meet the requirements for receiving acquired pension rights for care work." }
                )
            }

            includePhrase(Vedtak.BegrunnelseOverskrift)

            showIf(saksbehandlerValg.omsorgsarbeidFoer1992) {
                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter forskriften til folketrygdloven paragraf 3-16 fjerdeledd om godskriving av pensjonspoeng (omsorgspoeng) for omsorgsarbeid for en syk, en funksjonshemmet eller en eldre person." },
                        nynorsk { +"Vedtaket er gjort etter forskrifta til folketrygdlova paragraf 3-16 fjerde ledd om godskriving av pensjonspoeng (omsorgspoeng) for omsorgsarbeid for ein sjuk, ein funksjonshemma eller ein eldre person." },
                        english { +"The decision is made in accordance with the National Insurance Act paragraph 3-16, fourth subsection, concerning the accreditation of acquired pension rights for care work performed for an ill, disabled, or elderly person." }
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Ordningen med pensjonsopptjening for omsorgsarbeid ble innført fra og med 1992. Du kan ikke få godskrevet opptjening for omsorgsarbeid for en syk funksjonshemmet eller eldre person som er utført før 1992." },
                        nynorsk { +"Ordninga med pensjonsopptening for omsorgsarbeid vart innført frå og med 1992. Du kan ikkje få godskrive opptening for omsorgsarbeid for ein sjuk, ein funksjonshemma eller ein eldre person som er utført før 1992. " },
                        english { +"The scheme for pension accrual for care work was introduced as of 1992. You cannot acquire rights for care work for an ill, disabled, or elderly person that was carried out before 1992." }
                    )
                }
            }

            showIf(saksbehandlerValg.omsorgsarbeidEtter69Aar) {
                paragraph {
                    text(
                        bokmal {
                            +"Vedtaket er gjort etter folketrygdloven § 3-16 andre ledd. " +
                                    "Det kan godskrives pensjonspoeng etter første ledd fra og med det året vedkommende fyller 17 år, til og med det året vedkommende fyller 69 år. " +
                                    "Du kan ikke få godskrevet omsorgsopptjening for år etter at du fylte 69 år."
                        },
                        nynorsk {
                            +"Vedtaket er gjort etter folketrygdlova § 3-16 andre ledd. " +
                                    "Det kan godskrivast pensjonspoeng etter første ledd frå og med det året vedkomande fyller 17 år, til og med det året vedkomande fyller 69 år. " +
                                    "Du kan ikkje få godskrive omsorgsopptening for år etter at du fylte 69 år."
                        },
                        english {
                            +"This decision was made pursuant to Section 3-16, second paragraph of the National Insurance Act. The provision reads: " +
                                    "You cannot be credited with points for care work after the year in which you turn 69."
                        }
                    )
                }
            }

            showIf(saksbehandlerValg.omsorgsarbeidMindreEnn22TimerOgMindreEnn6Maaneder or saksbehandlerValg.omsorgsarbeidMindreEnn22Timer or saksbehandlerValg.omsorgsarbeidMindreEnn6Maaneder) {
                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter folketrygdloven § 3-16 første led bokstav b og forskriften til § 3-16." },
                        nynorsk { +"Vedtaket er gjort etter folketrygdlova § 3-16 første ledd bokstav b og forskrifta til § 3-16." },
                        english { +"This decision was made pursuant to Section 3-16, first paragraph (b), and the regulation issued under Section 3-16 of the National Insurance Act." }
                    )
                }

                paragraph {
                    text(
                        bokmal {
                            +"Ifølge disse bestemmelsene må du ha utført pleie- og omsorgsarbeid i minst 22 timer per uke, inkludert reisetid, " +
                                    "for å få godskrevet pensjonspoeng for omsorg av eldre, syke eller funksjonshemmede. " +
                                    "Arbeidet må som hovedregel ha vart i til sammen minst seks måneder i kalenderåret."
                        },
                        nynorsk {
                            +"Ifølgje desse føresegnene må du ha utført pleie- og omsorgsarbeid i minst 22 timar per veke, inkludert reisetid, " +
                                    "for å få godskrive pensjonspoeng for omsorg av eldre, sjuke eller funksjonshemma. " +
                                    "Arbeidet må som hovudregel ha vart i til saman minst seks månader i kalenderåret."
                        },
                        english {
                            +"According to these provisions, you must have performed care work for at least 22 hours per week, including travel time, " +
                                    "to receive pension points for the care of elderly, ill or disabled persons. " +
                                    "As a general rule, the work must also have amounted to at least six months in the calendar year."
                        }
                    )
                }

                showIf(saksbehandlerValg.omsorgsarbeidMindreEnn22Timer) {
                    paragraph {
                        text(
                            bokmal {
                                +"På bakgrunn av opplysninger som vi har mottatt fra kommunen, " +
                                        "finner vi at pleie- og omsorgsarbeidet ikke har utgjort minst 22 timer per uke inkludert reisetid. " +
                                        "Kommunen oppgir at omsorgsarbeidet har hatt utgjort om lag "
                                +fritekst("antall timer") + " timer per uke."
                            },
                            nynorsk {
                                +"På bakgrunn av opplysningane som vi har motteke frå kommunen, " +
                                        "finn vi at pleie- og omsorgsarbeidet ikkje har utgjort minst 22 timar per veke inkludert reisetid. " +
                                        "Kommunen oppgir at omsorgsarbeidet har utgjort om lag "
                                +fritekst("antall timer") + " timar per veke."
                            },
                            english {
                                +"Based on the information we have received from the local authority, " +
                                        "we find that the care work has not amounted to at least 22 hours per week, including travel time. " +
                                        "The local authority states that the extent of the care work has been about "
                                +fritekst("antall timer") + " hours per week."
                            },
                        )
                    }
                }.orShowIf(saksbehandlerValg.omsorgsarbeidMindreEnn6Maaneder) {
                    paragraph {
                        text(
                            bokmal {
                                +"På bakgrunn av opplysningene som vi har mottatt fra kommunen, " +
                                        "finner vi at pleie- og omsorgsarbeidet ikke har vart i til sammen minst seks måneder i "
                                +fritekst("år") + ". " + "Kommunen oppgir at omsorgsarbeidet bare har vart i cirka "
                                +fritekst("antall måneder") + " måneder i "
                                +fritekst("år") + "."
                            },
                            nynorsk {
                                +"På bakgrunn av opplysningar som vi har motteke frå kommunen, " +
                                        "finn vi at pleie- og omsorgsarbeidet ikkje har vara i til saman seks månader i "
                                +fritekst("år") + ". " + "Kommunen oppgjev at omsorgsarbeidet berre har vara i om lag "
                                +fritekst("antall måneder") + " månader i "
                                +fritekst("år") + "."
                            },
                            english {
                                +"Based on the information we have received from the local authority, we find that the care work has not amounted to at least 6 months during "
                                +fritekst("år") + ". " + "The local authority states that the extent of the care work has been about "
                                +fritekst("antall måneder") + " months during "
                                +fritekst("år") + "."
                            }
                        )
                    }
                }.orShowIf(saksbehandlerValg.omsorgsarbeidMindreEnn22TimerOgMindreEnn6Maaneder) {
                    paragraph {
                        text(
                            bokmal {
                                +"På bakgrunn av opplysningene vi har mottatt fra kommunen, " +
                                        "finner vi at pleie- og omsorgsarbeidet ikke har utgjort minst 22 timer per uke, inkludert reisetid. " +
                                        "Videre har arbeidet heller ikke vart i tilsamen minst 6 måneder i "
                                +fritekst("år") + ". " + "Kommunen oppgir at omsorgsarbeidet har utgjort om lag "
                                +fritekst("antall timer") + " timer per uke og har vart i omtrent "
                                +fritekst("antall måneder") + " måneder i "
                                +fritekst("år") + "."
                            },
                            nynorsk {
                                +"På bakgrunn av opplysningane vi har motteke frå kommunen, " +
                                        "finn vi at pleie- og omsorgsarbeidet ikkje har utgjort minst 22 timar per veke, inkludert reisetid. " +
                                        "Vidare har arbeidet heller ikkje vart i til saman minst seks månader i "
                                +fritekst("år") + ". " + "Kommunen oppgir at omsorgsarbeidet har utgjort om lag "
                                +fritekst("antall timer") + " timar per veke og har vart i omtrent "
                                +fritekst("antall måneder") + " månader i <år>."
                            },
                            english {
                                +"Based on the information we have received from the local authority, " +
                                        "we find that the care work has not amounted to at least 22 hours per week, including travel time. " +
                                        "Furthermore, the work has also not lasted a total of at least 6 months in "
                                +fritekst("år") + ". " + "The local authority states that the extent of the care work has been about "
                                +fritekst("antall timer") + " hours per week and has lasted for about "
                                +fritekst("antall måneder") + " months during "
                                +fritekst("år") + "."
                            },
                        )
                    }
                }
            }

            showIf(saksbehandlerValg.privatAFPavslaat or saksbehandlerValg.omsorgsarbeidForBarnUnder7aarFoer1992 or saksbehandlerValg.omsorgsopptjeningenGodskrevetEktefellen or saksbehandlerValg.brukerFoedtFoer1948) {
                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter AFP-tilskottsloven § 6 og forskrift til denne bestemmelsen." },
                        nynorsk { +"Vedtaket er gjort etter AFP‑tilskotslova § 6 og forskrift til denne føresegna." },
                        english { +"This decision is made in accordance with the Contractual Pension Subsidy Act (AFP-tilskottsloven) paragraph 6 and the regulations for this provision." }
                    )
                }

                showIf(saksbehandlerValg.privatAFPavslaat) {
                    paragraph {
                        text(
                            bokmal {
                                +"Vi har mottatt melding fra Fellesordningen om at du har fått avslag på søknaden din om AFP i privat sektor. " +
                                        "Søknaden din om pensjonsopptjening for omsorg for barn under 7 år før 1992 kan derfor ikke innvilges."
                            },
                            nynorsk {
                                +"Vi har motteke melding frå Fellesordninga om at du har fått avslag på søknaden din om AFP i privat sektor. " +
                                        "Søknaden din om pensjonsopptening for omsorg for barn under 7 år før 1992 kan difor ikkje innvilgast."
                            },
                            english {
                                +"We have received notification from Fellesordningen (the 'Common Scheme') that your application for a contractual pension in the private sector has been denied. " +
                                        "Consequently, your application to be credited acquired pension rights for the care of children under the age of 7 before 1992 cannot be granted."
                            }
                        )
                    }
                }

                showIf(saksbehandlerValg.omsorgsarbeidForBarnUnder7aarFoer1992) {
                    paragraph {
                        text(
                            bokmal {
                                +"For personer som er født i 1948 eller senere kan pensjonsopptjening for omsorg for barn under 7 år før 1992 benyttes i beregning av avtalefestet pensjon (AFP) i privat sektor. " +
                                        "Dette forutsetter at Fellesordningen har innvilget AFP i privat sektor."
                            },
                            nynorsk {
                                +"For personar som er fødde i 1948 eller seinare kan pensjonsopptening for omsorg for barn under 7 år før 1992 nyttast i utrekning av avtalefesta pensjon (AFP) i privat sektor. " +
                                        "Dette føreset at Fellesordninga har innvilga AFP i privat sektor."
                            },
                            english {
                                +"If you were born in or after 1948, care for children under the age of 7 before 1992 may be included in calculating a contractual pension in the private sector. " +
                                        "This applies only if Fellesordningen (the ‘Common Scheme’) has granted you such a pension."
                            }
                        )
                    }
                    paragraph {
                        text(
                            bokmal {
                                +"Vi kan ikke se at du har søkt om AFP i privat sektor. " +
                                        "Søknaden din om pensjonsopptjening for omsorg for barn under 7 år før 1992 kan derfor ikke innvilges."
                            },
                            nynorsk {
                                +"Vi kan ikkje sjå at du har søkt om AFP i privat sektor. " +
                                        "Søknaden din om pensjonsopptening for omsorg for barn under 7 år før 1992 kan difor ikkje innvilgast."
                            },
                            english {
                                +"We have no information indicating that you have applied for AFP in the private sector. " +
                                        "Your application to be accredited with acquired pension rights for the care of children under the age of 7 before 1992 cannot be granted."
                            },
                        )
                    }
                }

                showIf(saksbehandlerValg.omsorgsopptjeningenGodskrevetEktefellen or saksbehandlerValg.brukerFoedtFoer1948) {
                    paragraph {
                        text(
                            bokmal {
                                +"For personer som er født i 1948 eller senere kan pensjonsopptjening for omsorg for barn under 7 år før 1992 benyttes i beregning av avtalefestet pensjon (AFP) i privat sektor. "
                            },
                            nynorsk {
                                +"For personar som er fødde i 1948 eller seinare kan pensjonsopptening for omsorg for barn under 7 år før 1992 nyttast i utrekning av avtalefesta pensjon (AFP) i privat sektor. "
                            },
                            english {
                                +"If you were born in or after 1948, care for children under the age of 7 before 1992 may be included in calculating a contractual pension in the private sector. "
                            }
                        )
                    }

                    showIf(saksbehandlerValg.omsorgsopptjeningenGodskrevetEktefellen) {
                        paragraph {
                            text(
                                bokmal {
                                    +"Det er bare en av foreldrene som kan få godskrevet omsorgsopptjening for hvert enkelt år. " +
                                            "Foreldrene kan be om at omsorgspoeng blir godskrevet den andre forelderen hvis de var sammen om omsorgen for barna. " + "" +
                                            "Dette gjelder likevel ikke når det inngår i beregningsgrunnlaget for en avtalefestet pensjon som allerede har blitt eller blir utbetalt."
                                },
                                nynorsk {
                                    +"Det er berre éin av foreldra som kan få godskrive omsorgsopptening for kvart enkelt år. " +
                                            "Foreldra kan be om at omsorgspoeng blir godskrivne den andre av dei dersom dei hadde felles omsorg for barna. " +
                                            "Dette gjeld likevel ikkje når poenga inngår i berekningsgrunnlaget for ei avtalefesta pensjon som allereie er eller blir utbetalt."
                                },
                                english {
                                    +"Only one parent may acquire rights for care work in any given year. " +
                                            "The parents may choose which of them the rights are assigned to, provided they both participated in child care. " +
                                            "This does not apply, however, when the rights are included in the calculation of a contractual pension that has already been paid or will be paid."
                                }
                            )
                        }
                        paragraph {
                            text(
                                bokmal {
                                    +"Omsorgsopptjening for " + fritekst("år") + " inngår allerede i beregningsgrunnlaget for AFP i privat sektor til ektefellen din. " +
                                            "Søknaden din om pensjonsopptjening for omsorg for barn under 7 år før 1992 kan derfor ikke innvilges."
                                },
                                nynorsk {
                                    +"Omsorgsopptening for " + fritekst("år") + " inngår allereie i berekningsgrunnlaget for AFP i privat sektor til ektefellen din. " +
                                            "Søknaden din om pensjonsopptening for omsorg for barn under 7 år før 1992 kan difor ikkje innvilgast."
                                },
                                english {
                                    +"Acquired pension rights for " + fritekst("år") + " are already included in the calculation of the contractual pension in the private sector that your spouse receives. " +
                                            "Your application to be accredited with acquired pensjon rights for the care of children under the age of 7 prior to 1992 therefore cannot be granted."
                                }
                            )
                        }
                    }

                    showIf(saksbehandlerValg.brukerFoedtFoer1948) {
                        paragraph {
                            text(
                                bokmal { +"Du er født tidligere enn 1948 og vilkårene for å få pensjonsopptjening for omsorg for barn under 7 år før 1992 er derfor ikke oppfylt." },
                                nynorsk { +"Du er fødd før 1948, og vilkåra for å få pensjonsopptening for omsorg for barn under 7 år før 1992 er difor ikkje oppfylte." },
                                english { +"You were born prior to 1948, and the requirements for acquiring pension rights for the care of children under the age of 7 before 1992 have therefore not been met." }
                            )
                        }
                    }
                }
            }

            includePhrase(Felles.RettTilAAKlage)
            includePhrase(Felles.RettTilInnsyn(vedleggDineRettigheterOgMulighetTilAaKlage))
            includePhrase(Felles.HarDuSpoersmaal.omsorg)
        }

        includeAttachment(
            vedleggDineRettigheterOgMulighetTilAaKlage,
            pesysData.dineRettigheterOgMulighetTilAaKlageDto
        )
    }
}
