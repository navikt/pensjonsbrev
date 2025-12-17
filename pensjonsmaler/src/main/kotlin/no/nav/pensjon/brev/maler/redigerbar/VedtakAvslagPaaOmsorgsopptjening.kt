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
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakAvslagPaaOmsorgsopptjeningDtoSelectors.SaksbehandlerValgSelectors.omsorgsarbeidMindreEnn22Timer
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakAvslagPaaOmsorgsopptjeningDtoSelectors.SaksbehandlerValgSelectors.omsorgsarbeidMindreEnn22TimerOgMindreEnn6Maaneder
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakAvslagPaaOmsorgsopptjeningDtoSelectors.SaksbehandlerValgSelectors.omsorgsarbeidMindreEnn6Maaneder
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakAvslagPaaOmsorgsopptjeningDtoSelectors.SaksbehandlerValgSelectors.omsorgsopptjeningenGodskrevetEktefellen
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakAvslagPaaOmsorgsopptjeningDtoSelectors.SaksbehandlerValgSelectors.privatAFPavslaat
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakAvslagPaaOmsorgsopptjeningDtoSelectors.pesysData
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakAvslagPaaOmsorgsopptjeningDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.common.Vedtak
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlage
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
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
                        +pesysData.navEnhet + "viser til søknaden din om å få godskriven pensjonsopptening for " + fritekst(
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
                    english { +"The application has been denied because you do not meet the requirements for receiving acquired rights for care work." }
                )
            }

            includePhrase(Vedtak.BegrunnelseOverskrift)

            /*            paragraph {
                            val hjemmel = fritekst(
                                "Velg et alternativ eller fyller inn tekst selv. "
                                        + "Vær oppmerksom på at hjemmelen for avslag kan være § 3-16 og tilhørende forskrift, "
                                        + "men også § 20-21 (for personer født etter 1953 og opptjeningsår før 2009) "
                                        + "eller § 20-8 (for personer født etter 1953 og opptjeningsår fom 2010). "
                                        + "Benyttes hjemlene i kapittel 20 skal OMSORGSPOENG erstattes med OMSORGSOPPTJENING. "
                                        + "Hjemmelen kan også være AFP-tilskottsloven hvis vedtaket gjelder pensjonsopptjening for omsorg for barn under 7 år før 1992."
                            )
                        }
                        */

            showIf(saksbehandlerValg.omsorgsarbeidFoer1992) {
                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter forskriften til folketrygdloven paragraf 3-16 fjerdeledd om godskriving av pensjonspoeng (omsorgspoeng) for omsorgsarbeid for en syk, en funksjonshemmet eller en eldre person." },
                        nynorsk { +"Vedtaket er gjort etter forskrifta til folketrygdlova paragraf 3-16 fjerde ledd om godskriving av pensjonspoeng (omsorgspoeng) for omsorgsarbeid for ein sjuk, ein funksjonshemma eller ein eldre person." },
                        english { +"The decision is made inn accordance with the National Insurance Act paragraph 3-16 fourth sub-section on the accreditng of acquired rights for care work for an ill, disabled or old person." }
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
                            +"The decision has been taken under § 3-16, second paragraph of the National Insurance Act. The provision reads: " +
                                    "The decision has been taken under Section 3-16, second paragraph of the National Insurance Act. " +
                                    "You cannot be credited with points for care work after the year in which you turn 69."
                        }
                    )
                }
            }

            showIf(saksbehandlerValg.omsorgsarbeidMindreEnn22TimerOgMindreEnn6Maaneder) {
                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter folketrygdloven § 3-16 første led bokstav b og forskriften til § 3-16." },
                        nynorsk { +"Vedtaket er gjort etter folketrygdlova § 3-16 første ledd bokstav b og forskrifta til § 3-16." },
                        english { +"The decision has been made pursuant to the National Insurance Act § 3-16, first paragraph, letter b, and the regulations to § 3-16." }
                    )
                }
            }
            paragraph {
                text(
                    bokmal { +"I følge disse bestemmelsene må du ha utført pleie- og omsorgsarbeid i minst 22 timer per uke inkludert reisetid, " +
                            "for å få godskrevet pensjonspoeng for pleie- og omsorgsarbeid for eldre, syke eller funksjonshemmede. " +
                            "Arbeidet må som hovedregel også ha vart i til sammen minst seks måneder i kalenderåret." },
                    nynorsk { +"Ifølgje desse føresegnene må du ha utført pleie- og omsorgsarbeid i minst 22 timar per veke, inkludert reisetid, " +
                            "for å få godskrive pensjonspoeng for pleie- og omsorgsarbeid for eldre, sjuke eller funksjonshemma. " +
                            "Arbeidet må som hovudregel òg ha vara i til saman minst seks månader i kalenderåret." },
                    english { +"In accordance with these provisions, you must have carried out care work for at least 22 hours per week, " +
                            "including travel time, in order to have pension points credited for the care of the elderly, sick or disabled. " +
                            "As a general rule, the work must also have totaled at least six months in the calendar year." }
                )
            }
            paragraph {
                text(
                    bokmal { +"På bakgrunn av opplysningene som vi har mottatt fra kommunen, " +
                            "finner vi at pleie- og omsorgsarbeidet ikke har utgjort minst 22 timer per uke inkludert reisetid. " +
                            "Det har heller ikke vart i til sammen minst 6 måneder i " + fritekst("år") + ". " +
                            "Kommunen oppgir at omsorgsarbeidet har hatt et omfang på om lag " + fritekst("antall timer") +
                            " timer per uke og har vart i om lag <måneder> måneder i " + fritekst("år") + "." },
                    nynorsk { +"" },
                    english { +"Based on the information we have received from the local authority, " +
                            "we conclude that the care work has not amounted to at least 22 hours per week, including travel time. " +
                            "Furthermore, we find that the care work has not been carried out for a minimum of 6 months during " + fritekst("år") + ". " +
                            "The local authority states that the extent of the care work has been about " + fritekst("timer") +
                            " per week and was carried out for about " + fritekst("månder") + " months during " + fritekst("år") + "." },
                )
            }

            showIf(saksbehandlerValg.omsorgsarbeidMindreEnn22Timer) {
                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter folketrygdloven § 3-16 første led bokstav b og forskriften til § 3-16." },
                        nynorsk { +"Vedtaket er gjort etter folketrygdlova § 3-16 første ledd bokstav b og forskrifta til § 3-16." },
                        english { +"The decision has been made pursuant to the National Insurance Act § 3-16, first paragraph, letter b, and the regulations to § 3-16." }
                    )
                }
                paragraph {
                    text(
                        bokmal {
                            +"I følge disse bestemmelsene må du ha utført pleie- og omsorgsarbeid i minst 22 timer per uke inkludert reisetid, " +
                                    "for å få godskrevet pensjonspoeng for pleie- og omsorgsarbeid for eldre, syke eller funksjonshemmede. " +
                                    "Arbeidet må som hovedregel også ha vart i til sammen minst seks måneder i kalenderåret."
                        },
                        nynorsk {
                            +"Ifølgje desse føresegnene må du ha utført pleie- og omsorgsarbeid i minst 22 timar per veke, inkludert reisetid, " +
                                    "for å få godskrive pensjonspoeng for pleie- og omsorgsarbeid for eldre, sjuke eller funksjonshemma. " +
                                    "Arbeidet må som hovudregel òg ha vara i til saman minst seks månader i kalenderåret."
                        },
                        english {
                            +"In accordance with these provisions, you must have carried out care work for at least 22 hours per week, including travel time, " +
                                    "in order to have pension points credited for the care of the elderly, ill or disabled. " +
                                    "As a general rule, the work must also have totaled at least six months in the calendar year."
                        }
                    )
                }
                paragraph {
                    text(
                        bokmal {
                            +"På bakgrunn av opplysninger som vi har mottatt fra kommunen, " +
                                    "finner vi at pleie- og omsorgsarbeidet ikke har utgjort minst 22 timer per uke inkludert reisetid. " +
                                    "Kommunen oppgir at omsorgsarbeidet har hatt et omfang på cirka <antall timer> timer per uke"
                        },
                        nynorsk {
                            +"På bakgrunn av opplysningane som vi har motteke frå kommunen, " +
                                    "finn vi at pleie- og omsorgsarbeidet ikkje har utgjort minst 22 timar per veke inkludert reisetid. " +
                                    "Kommunen opplyser at omsorgsarbeidet har hatt eit omfang på om lag " + fritekst("antall timer") + " timar per veke."
                        },
                        english {
                            +"Based on the information we have received from the local authority, " +
                                    "we find that the care work has not amounted to at least 22 hours per week including travel time. " +
                                    "The local authority states that the extent of the care work has been " + fritekst("antall timer") + " hours per week."
                        },
                    )
                }
            }

            showIf(saksbehandlerValg.omsorgsarbeidMindreEnn6Maaneder) {
                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter folketrygdloven § 3-16 første led bokstav b og forskriften til § 3-16." },
                        nynorsk { +"Vedtaket er gjort etter folketrygdlova § 3-16 første ledd bokstav b og forskrifta til § 3-16." },
                        english { +"The decision has been made pursuant to the National Insurance Act § 3-16, first paragraph, letter b, and the regulations to § 3-16." }
                    )
                }
                paragraph {
                    text(
                        bokmal {
                            +"I følge disse bestemmelsene må du må ha utført pleie- og omsorgsarbeid i minst 22 timer per uke inkludert reisetid, " +
                                    "for å få godskrevet pensjonspoeng for pleie- og omsorgsarbeid for eldre, syke eller funksjonshemmede. " +
                                    "Arbeidet må som hovedregel også ha vart i til sammen minst seks måneder i kalenderåret."
                        },
                        nynorsk {
                            +"Ifølgje desse føresegnene må du ha utført pleie- og omsorgsarbeid i minst 22 timar per veke inkludert reisetid, " +
                                    "for å få godskrive pensjonspoeng for pleie- og omsorgsarbeid for eldre, sjuke eller funksjonshemma. " +
                                    "Arbeidet må som hovudregel òg ha vara i til saman minst seks månader i kalenderåret."
                        },
                        english {
                            +"In accordance with these provisions, you must have carried out care work for at least 22 hours per week including travel time, " +
                                    "in order to have pension points credited for the care of the elderly, ill or disabled. " +
                                    "As a general rule, the work must also have totaled at least six months in the calendar year."
                        }
                    )
                }
                paragraph {
                    text(
                        bokmal {
                            +"På bakgrunn av opplysningene som vi har mottatt fra kommunen, " +
                                    "finner vi at pleie- og omsorgsarbeidet ikke har vart i til sammen minst seks måneder i " + fritekst(
                                "år"
                            ) + ". " +
                                    "Kommunen oppgir at omsorgsarbeidet bare har vart i cirka " + fritekst("antall måneder") + " måneder i " + fritekst(
                                "år"
                            ) + "."
                        },
                        nynorsk {
                            +"På bakgrunn av opplysningar som vi har motteke frå kommunen, " +
                                    "finn vi at pleie- og omsorgsarbeidet ikkje har vara i til saman seks månader i " + fritekst(
                                "år"
                            ) + ". " +
                                    "Kommunen oppgjev at omsorgsarbeidet berre har vara i om lag " + fritekst("antall måneder") + " månader i " + fritekst(
                                "år"
                            ) + "."
                        },
                        english {
                            +"Based on the information we have received from the local authority, we find that the care work has not amounted to at least 6 months during " + fritekst(
                                "år"
                            ) + ". " +
                                    "The local authority states that the extent of the care work has been about " + fritekst("antall måneder") + " months during " + fritekst("år") + "."
                        }
                    )
                }
            }

            showIf(saksbehandlerValg.privatAFPavslaat) {
                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter AFP-tilskottsloven § 6 og forskrift til denne bestemmelsen." },
                        nynorsk { +"" },
                        english { +"" }
                    )
                }
                paragraph {
                    text(
                        bokmal {
                            +"Vi har mottatt melding fra Fellesordningen om at du har fått avslag på søknaden din om AFP i privat sektor. " +
                                    "Søknaden din om pensjonsopptjening for omsorg for barn under sju år før 1992 kan derfor ikke innvilges."
                        },
                        nynorsk { +"" },
                        english { +"" }
                    )
                }
            }

            showIf(saksbehandlerValg.omsorgsopptjeningenGodskrevetEktefellen) {
                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter AFP - tilskottsloven § 6 og forskrift til denne bestemmelsen." },
                        nynorsk { +"" },
                        english { +"" }
                    )
                }
                paragraph {
                    text(
                        bokmal {
                            +"Omsorgsopptjening for " + fritekst("år") + " inngår allerede i beregningsgrunnlaget for AFP i privat sektor til ektefellen din. " +
                                    "Søknaden din om pensjonsopptjening for omsorg for barn under sju år før 1992 kan derfor ikke innvilges."
                        },
                        nynorsk { +"" },
                        english { +"" }
                    )
                }
            }

            showIf(saksbehandlerValg.brukerFoedtFoer1948) {
                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter AFP - tilskottsloven § 6 og forskrift til denne bestemmelsen." },
                        nynorsk { +"" },
                        english { +"" }
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Du er født tidligere enn 1948 og vilkårene for å få pensjonsopptjening for omsorg for barn under 7 år før 1992 er derfor ikke oppfylt." },
                        nynorsk { +"" },
                        english { +"" }
                    )
                }
            }

            includePhrase(Felles.RettTilAAKlage)
            includePhrase(Felles.RettTilInnsyn(vedleggDineRettigheterOgMulighetTilAaKlage))
            includePhrase(Felles.HarDuSpoersmaal.omsorg)
        }

        includeAttachment(vedleggDineRettigheterOgMulighetTilAaKlage, pesysData.dineRettigheterOgMulighetTilAaKlageDto)
    }
}