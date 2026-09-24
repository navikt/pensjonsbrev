package no.nav.pensjon.brev.maler.klageOgAnke.tekstNAY

import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType.ITALIC
import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.RedigerbarOutlinePhrase
import no.nav.pensjon.brev.template.RedigerbarPhraseBrevdata
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.text

@TemplateModelHelpers
object OversendelsesbrevTilKlageinstansTekst {

    object HvaKlagesakenGjelder : RedigerbarOutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, RedigerbarPhraseBrevdata>.template() {
            title1 { text(bokmal { +"Hva klagesaken gjelder" }) }
        }
    }

    object ViHarVurdertKlagen : RedigerbarOutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, RedigerbarPhraseBrevdata>.template() {
            paragraph { text(bokmal { +"Vi har vurdert klagen, men ikke funnet grunnlag for å gjøre om det påklagede vedtaket." }) }
        }
    }

    object KlagersAnfoersler : RedigerbarOutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, RedigerbarPhraseBrevdata>.template() {
            title1 { text(bokmal { +"Klagers anførsler" }) }
            paragraph { text(bokmal { +"Det anføres i klagen <gjengi hovedinnholdet i klagers anførsler i tekst eller punktvis>" }) }
        }
    }

    object KlippInnFraVedtak : RedigerbarOutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, RedigerbarPhraseBrevdata>.template() {
            paragraph { text(bokmal { +"<Klipp inn fra vedtak eller vilkårsvurdering og svar ut anførslene konkret>" }, ITALIC) }
        }
    }

    object VedtaketBlirIkkeEndret : RedigerbarOutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, RedigerbarPhraseBrevdata>.template() {
            paragraph { text(bokmal { +"Klagen har ikke ført til at vedtak blir endret." }) }
        }
    }

    object VedtaketOpprettholdes : RedigerbarOutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, RedigerbarPhraseBrevdata>.template() {
            paragraph { text(bokmal { +"Vedtaket opprettholdes og klagen oversendes til Nav klageinstans for videre behandling." }) }
        }
    }

    object Generisk : RedigerbarOutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, RedigerbarPhraseBrevdata>.template() {

            includePhrase(HvaKlagesakenGjelder)
            paragraph {
                text(
                    bokmal {
                        +"Vi viser til klagen av "
                        +fritekst("dato") + " på vedtak av "
                        +fritekst("dato") +
                                +", der <beskrive kort hva avslaget gjelder/vedtaket gjelder. Eks: (...), der klager fikk avslag på sitt krav om uføretrygd>"
                        +". Klagefristen er overholdt."
                    }
                )
            }
            paragraph { text(bokmal { +"Klagen behandles etter folketrygdloven § " + fritekst("aktuell hjemmel/hjemler") + "." }) }

            includePhrase(ViHarVurdertKlagen)
            includePhrase(KlagersAnfoersler)

            title1 { text(bokmal { +"Vurdering av klagen" }) }
            paragraph {
                text(
                    bokmal {
                        +"<Gjør rede for problemstillingen i saken. "
                        +"Det skal komme frem hvilke lovbestemmelser som gjøres gjeldende i saken / hva sier loven. "
                        +"Det skal skrives en begrunnelse for hvorfor klager ikke fyller vilkårene. "
                        +"Klagers anførsler skal kommenteres / imøtegås. "
                        +"Drøftelsen skal avsluttes med en konklusjon.>"
                    }
                )
            }
            includePhrase(KlippInnFraVedtak)
            includePhrase(VedtaketBlirIkkeEndret)
            includePhrase(VedtaketOpprettholdes)
        }
    }

    //§12-2 Medlemskap
    object Medlemskap : RedigerbarOutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, RedigerbarPhraseBrevdata>.template() {

            includePhrase(HvaKlagesakenGjelder)
            paragraph {
                text(
                    bokmal {
                        +"Vi viser til klagen av "
                        +fritekst("dato") + " på vedtak av "
                        +fritekst("dato") +
                                +", der klager fikk avslag på søknad om uføretrygd, fordi vilkåret om forutgående medlemskap ikke er oppfylt. "
                        +"Klagefristen er overholdt."
                    }
                )
            }
            paragraph { text(bokmal { +"Klagen vurderes etter folketrygdloven § 12-2 – forutgående medlemskap." }) }

            includePhrase(ViHarVurdertKlagen)
            includePhrase(KlagersAnfoersler)

            title1 { text(bokmal { +"Vurdering av klagen" }) }
            paragraph { text(bokmal { +"For å ha rett til uføretrygd, må man ha vært medlem av folketrygden i de siste fem årene fram til uføretidspunktet." }) }
            paragraph {
                text(bokmal { +"Vi kan gjøre unntak fra hovedregelen dersom:" })
                list {
                    item { text(bokmal { +"Uførheten skyldes godkjent yrkesskade eller yrkessykdom" }) }
                }
            }
            paragraph {
                text(bokmal { +"Vi kan også gjøre unntak dersom man har vært medlem av folketrygden i minst ett år umiddelbart før man setter fram krav om uføretrygd, og" })
                list {
                    item { text(bokmal { +"Ble uføre før man fylte 26 år og da var medlem av trygden eller" }) }
                    item { text(bokmal { +"Etter fylte 16 år har vært medlem i trygden med unntak av maksimum fem år" }) }
                }
            }
            paragraph {
                text(bokmal { +"Vi kan også gjøre unntak dersom man var medlem av folketrygden på uføretidspunktet, og:" })
                list {
                    item { text(bokmal { +"Har tjent opp rett til minst halvparten av minsteytelsen for uføretrygd." }) }
                }
            }
            paragraph {
                text(
                    bokmal {
                        +"I det påklagde vedtaket mottok klager avslag på uføretrygd som følge av at medlemsvilkåret ikke var oppfylt. "
                        +"Det ble vurdert at klager ikke hadde vært medlem av folketrygden i de fem siste årene fram til uføretidspunktet, "
                        +"eller var omfattet av unntaksreglene. "
                        +"Uføretidspunktet er fastsatt til "
                        +fritekst("dato") + ", og det er vurdert at klager ble medlem av folketrygden den "
                        +fritekst("dato") + "."
                    }
                )
            }
            paragraph {
                text(bokmal { +"Begrunn fastsatt uføretidspunkt / Klager har ikke påklaget fastsatt uføretidspunkt." }, ITALIC)
                newline()
                text(bokmal { +"Begrunn fastsatt tidspunkt for medlemskap." }, ITALIC)
            }
            includePhrase(KlippInnFraVedtak)
            includePhrase(VedtaketBlirIkkeEndret)
            includePhrase(VedtaketOpprettholdes)
        }
    }

    //§ 12-5 Hensiktsmessig behandling og tiltak
    object HensiktsmessigBehandlingOgTiltak : RedigerbarOutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, RedigerbarPhraseBrevdata>.template() {

            includePhrase(HvaKlagesakenGjelder)
            paragraph {
                text(
                    bokmal {
                        +"Vi viser til klagen av "
                        +fritekst("dato") + " på vedtak av "
                        +fritekst("dato") +
                                +" der klager fikk avslag på søknad om uføretrygd fordi all hensiktsmessig behandling og arbeidsrettede tiltak ikke er forsøkt. "
                        +"Klagefristen er overholdt."
                    }
                )
            }
            paragraph { text(bokmal { +"Klagen vurderes etter folketrygdloven § 12-5." }) }

            includePhrase(ViHarVurdertKlagen)
            includePhrase(KlagersAnfoersler)

            title1 { text(bokmal { +"Vurdering av klagen" }) }
            paragraph {
                text(
                    bokmal {
                        +"For å ha rett til uføretrygd må personen ha gjennomført hensiktsmessig utredning og behandling som kan bedre inntektsmulighetene. "
                        +"Når vi vurderer om et behandlingstiltak er hensiktsmessig, legger vi vekt på alder, evner, utdanning, yrkesbakgrunn og arbeidsmuligheter. "
                        +"Hensiktsmessig behandling innebærer at all behandling som kan bedre funksjonsevnen og mulighetene for å komme i arbeid, "
                        +"må være forsøkt så langt at det er mulig å ta stilling til om inntektsevnen er varig nedsatt grunnet varig sykdom."
                    }
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"For å ha rett til uføretrygd må personen også ha gjennomført individuelle og hensiktsmessig arbeidsrettede tiltak, "
                        +"som kan hjelpe for å beholde eller skaffe arbeid. "
                        +"Man kan bare la være å prøve ut tiltak dersom det er åpenbare grunner til at det ikke vil være hensiktsmessig. "
                        +"Unntak praktiseres altså strengt."
                    }
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Siden klager ikke er ferdig behandlet, "
                        +"er det for tidlig å ta stilling til om inntektsevnen kan bedres ved at klager gjennomfører "
                    }
                )
                text(bokmal { +"(ev. ytterligere)" }, ITALIC)
                text(bokmal { +" arbeidsrettede tiltak." })
            }
            includePhrase(KlippInnFraVedtak)
            includePhrase(VedtaketBlirIkkeEndret)
            includePhrase(VedtaketOpprettholdes)
        }
    }

    //§ 12-5 Kun arbeidsrettede tiltak
    object KunArbeidsrettedeTiltak : RedigerbarOutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, RedigerbarPhraseBrevdata>.template() {

            includePhrase(HvaKlagesakenGjelder)
            paragraph {
                text(
                    bokmal {
                        +"Vi viser til klagen av "
                        +fritekst("dato") + " på vedtak av "
                        +fritekst("dato") +
                                +" der klager fikk avslag på søknad om uføretrygd fordi arbeidsrettede tiltak ikke er forsøkt i tilstrekkelig grad. "
                        +"Klagefristen er overholdt."
                    }
                )
            }
            paragraph { text(bokmal { +"Klagen vurderes etter folketrygdloven § 12-5." }) }

            includePhrase(ViHarVurdertKlagen)
            includePhrase(KlagersAnfoersler)

            title1 { text(bokmal { +"Vurdering av klagen" }) }
            paragraph {
                text(
                    bokmal {
                        +"For å ha rett til uføretrygd må personen ha gjennomført hensiktsmessige arbeidsrettede tiltak som kan bedre inntektsmulighetene. "
                        +"Med hensiktsmessige arbeidsrettede tiltak menes alle former for arbeidsrettede tiltak som kan øke inntektsevnen og hjelpe personen til å skaffe seg arbeid, øke arbeidsinnsatsen eller beholde lønnet arbeid. "
                        +"Når vi vurderer om et arbeidsrettet tiltak er hensiktsmessig, legger vi vekt på alder, evner, utdanning, yrkesbakgrunn og arbeidsmuligheter. "
                        +"Det kan ses bort fra dette kravet hvis det er åpenbare grunner som tilsier at tiltak ikke er hensiktsmessig. "
                        +"Arbeidsrettede tiltak skal tilpasses helsen. All avklaring må være gjennomført og avsluttet før uføretrygd kan innvilges."
                    }
                )
            }

            includePhrase(KlippInnFraVedtak)
            includePhrase(VedtaketBlirIkkeEndret)
            includePhrase(VedtaketOpprettholdes)
        }
    }

    //§ 12-6 Hovedårsak til sykdom
    object HovedAarakTilSykdom : RedigerbarOutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, RedigerbarPhraseBrevdata>.template() {

            includePhrase(HvaKlagesakenGjelder)


        }
    }
}
