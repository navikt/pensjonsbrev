package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.FeatureToggles
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.maler.fraser.Constants.KLAGE_URL
import no.nav.pensjon.brev.maler.fraser.Constants.KONTAKT_URL
import no.nav.pensjon.brev.maler.fraser.Constants.NAV_KONTAKTSENTER_AAPNINGSTID
import no.nav.pensjon.brev.maler.fraser.Constants.NAV_KONTAKTSENTER_TELEFON_UFORE
import no.nav.pensjon.brev.maler.fraser.Constants.NAV_URL
import no.nav.pensjon.brev.maler.fraser.Constants.UFORE_URL
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagHensiktsmessigBehandlingDto
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagHensiktsmessigBehandlingDtoSelectors.SaksbehandlervalgSelectors.brukVurderingFraVilkarsvedtak
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagHensiktsmessigBehandlingDtoSelectors.UforeAvslagHensiktsmessigBehandlingPendataSelectors.kravMottattDato
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagHensiktsmessigBehandlingDtoSelectors.UforeAvslagHensiktsmessigBehandlingPendataSelectors.vurdering
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagHensiktsmessigBehandlingDtoSelectors.pesysData
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagHensiktsmessigBehandlingDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VEDTAK

@TemplateModelHelpers
object UforeAvslagHensiktsmessigBehandling : RedigerbarTemplate<UforeAvslagHensiktsmessigBehandlingDto> {

    override val featureToggle = FeatureToggles.uforeAvslagDemo.toggle

    override val kode = Ufoerebrevkoder.Redigerbar.UT_AVSLAG_HENSIKTSMESSIG_BEHANDLING
    override val kategori = TemplateDescription.Brevkategori.FOERSTEGANGSBEHANDLING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.UFOREP)


    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Avslag uføretrygd - Hensiktsmessig behandling",
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
                text(bokmal { + "Vi fikk din søknad om uføretrygd den " + pesysData.kravMottattDato.format() + ". Vi har avslått søknaden"})
            }
            title1 {
                text(bokmal { + "Derfor får du ikke uføretrygd"})
            }
            paragraph {
                text(bokmal { + "Vi avslår søknaden din fordi du ikke har gjennomført all hensiktsmessig behandling, som kan bedre inntektsevnen din."})
            }
            showIf(saksbehandlerValg.brukVurderingFraVilkarsvedtak) {
                paragraph {
                    text( bokmal { + pesysData.vurdering })
                }
            }.orShow {
                paragraph {
                    text(bokmal { +"Funksjonsnedsettelsen vises i form av " + fritekst("X") })
                    text(bokmal {
                        +". Følgene av dette er at du ikke klarer å fungere i arbeid. Det er forsøkt behandling i form av " + fritekst(
                            "X"
                        )
                    })
                    text(bokmal {
                        +". Det er for tidlig å ta stilling til effekten av behandlingen fordi du " +
                                fritekst("fortsatt er under behandling/det finnes behandling som ikke er forsøkt.")
                    })
                }
                paragraph {
                    text(bokmal { +"Fastlegen din vurderer at videre behandling i form av " + fritekst("X") })
                    text(bokmal { +"kan bedre inntektsevnen din. Rådgivende lege/spesialist vurderer " + fritekst("X") + "." })
                }
                paragraph {
                    text(bokmal {
                        +"Vi mener at du ikke har fått all hensiktsmessig behandling som kan bedre inntektsevnen. " +
                                fritekst(
                                    "(Individuell begrunnelse, vær konkret, f.eks utredning, samarbeidende spesialist, " +
                                            "anbefalt konkret behandling som ikke er forsøkt, våre retningslinjer ved spesielle sykdomstilstander, alder)."
                                )
                    })

                }
                paragraph {
                    text(bokmal {
                        +"Det kan ikke utelukkes at behandlingen kan bedre funksjons- og inntektsevnen. " +
                                "Samlet sett vurderer vi det som hensiktsmessig at behandlingen forsøkes. Fordi du har ikke fått " +
                                "all hensiktsmessig behandling, er det for tidlig å ta stilling til om hensiktsmessig arbeidsrettede tiltak er prøvd."
                    })
                }
                paragraph {
                    text(bokmal {
                        +"Vi kan derfor ikke vurdere om sykdom eller skade har ført til at inntektsevnen din er varig nedsatt. " +
                                "Du oppfyller ikke vilkårene, og vi avslår derfor søknaden din om uføretrygd."
                    })
                }
            }
            paragraph {
                text(bokmal {+ "Vedtaket er gjort etter folketrygdloven §§ 12-5 til 12-7."})
            }

            includePhrase(RettTilAKlage)
            includePhrase(RettTilInnsyn)
            includePhrase(HarDuSporsmal)

            //includeAttachment(Felles.Vedlegg.DineRettigheterOgPlikterUfoere)
        }
    }

    private object RettTilAKlage : OutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
            title1 {
                text(
                    bokmal { + "Du har rett til å klage" },
                )
            }
            paragraph {
                text(
                    bokmal { +
                    "Hvis du mener vedtaket er feil, kan du klage. Fristen for å klage er seks uker fra den datoen du mottok vedtaket. I vedlegget «Dine rettigheter og plikter»"
                        + " får du vite mer om hvordan du går fram. Du finner skjema og informasjon på $KLAGE_URL."},
                )
            }
        }
    }

    private object RettTilInnsyn : OutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
            title1 {
                text(
                    bokmal { + "Du har rett til innsyn" },
                )
            }
            paragraph {
                text(
                    bokmal { + "Du har rett til å se dokumentene i saken din. Se vedlegg «Dine rettigheter og plikter» for informasjon om hvordan du går fram."}
                )
            }
        }
    }

    private object HarDuSporsmal : OutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
            title1 {
                text(
                    bokmal { + "Har du spørsmål?" },
                )
            }
            paragraph {
                text(
                    bokmal { + "Du finner mer informasjon på $UFORE_URL. " +
                            "På $KONTAKT_URL kan du chatte eller skrive til oss. " +
                            "Hvis du ikke finner svar på $NAV_URL, kan du ringe oss på telefon $NAV_KONTAKTSENTER_TELEFON_UFORE " +
                            "hverdager kl. $NAV_KONTAKTSENTER_AAPNINGSTID."
                    }
                )
            }
        }
    }
}
