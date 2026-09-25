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

    //START FELLESTEKSTER
    object HvaKlagesakenGjelderOverskrift : RedigerbarOutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, RedigerbarPhraseBrevdata>.template() {
            title1 { text(bokmal { +"Hva klagesaken gjelder" }) }
        }
    }

    object InnvilgetUfoeretrygd : RedigerbarOutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, RedigerbarPhraseBrevdata>.template() {
            title1 { text(bokmal { +"Hva klagesaken gjelder" }) }
            paragraph {
                text(
                    bokmal {
                        +"Vi viser til klagen av "
                        +fritekst("dato") + " på vedtak av "
                        +fritekst("dato") + " der klager ble innvilget "
                        +fritekst("X prosent") + " uføretrygd fra "
                        +fritekst("dato") + ". "
                        +"Klagefristen er overholdt."
                    }
                )
            }
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

    object NedsattInntektsevneHvaKlagesakenGjelder : RedigerbarOutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, RedigerbarPhraseBrevdata>.template() {
            paragraph {
                text(
                    bokmal {
                        +"Vi viser til klagen av "
                        +fritekst("dato") + " på vedtak av "
                        +fritekst("dato") +
                                +" der klager fikk avslag på søknad om uføretrygd fordi inntektsevnen ikke var nedsatt med minst 30/40/50 prosent. "
                        +"Klagefristen er overholdt."
                    }
                )
            }
        }
    }

    object NedsattInntektsevneVurderingAvKlagen : RedigerbarOutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, RedigerbarPhraseBrevdata>.template() {
            title1 { text(bokmal { +"Vurdering av klagen" }) }
            paragraph {
                text(
                    bokmal {
                        +"Uføregraden fastsettes ved å sammenligne inntektsevne før og etter uførhet. "
                        +"Som hovedregel må inntektsevnen være varig nedsatt med minst 50 prosent. "
                        +"For personer som mottar arbeidsavklaringspenger når søknaden om uføretrygd blir fremsatt, er det tilstrekkelig at inntektsevnen er varig nedsatt med minst 40 prosent. "
                    }
                )
                text(bokmal { +"Hvis uførheten skyldes en godkjent yrkesskade er det tilstrekkelig at inntektsevnen er varig nedsatt med minst 30 prosent." }, ITALIC)
            }
        }
    }
//SLUTT FELLESTEKSTER

    //START BREVMALER
    object Generisk : RedigerbarOutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, RedigerbarPhraseBrevdata>.template() {

            includePhrase(HvaKlagesakenGjelderOverskrift)
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

            includePhrase(HvaKlagesakenGjelderOverskrift)
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

            includePhrase(HvaKlagesakenGjelderOverskrift)
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

            includePhrase(HvaKlagesakenGjelderOverskrift)
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

            includePhrase(HvaKlagesakenGjelderOverskrift)
            paragraph {
                text(
                    bokmal {
                        +"Vi viser til klagen av "
                        +fritekst("dato") + " på vedtak av "
                        +fritekst("dato") +
                                +" der klager fikk avslag på søknad om uføretrygd fordi sykdom ikke er hovedårsaken til arbeidsuførheten. "
                        +"Klagefristen er overholdt."
                    }
                )
            }
            paragraph { text(bokmal { +"Klagen vurderes etter folketrygdloven § 12-6 - sykdom og årsakssammenheng." }) }

            includePhrase(ViHarVurdertKlagen)
            includePhrase(KlagersAnfoersler)

            title1 { text(bokmal { +"Vurdering av klagen" }) }
            paragraph {
                text(
                    bokmal {
                        +"Det er et vilkår at den medisinske lidelsen må ha medført en varig funksjonsnedsettelse av en slik art og grad at den utgjør hovedårsaken til nedsettelsen av inntektsevnen. "
                        +"Kravet til årsakssammenheng medfører at det aktuelle sykdomsforholdet må utgjøre minst 50 prosent av det samlede årsaksbildet. "
                        +"Dersom utenforliggende forhold (ikke sykdom) er den dominerende årsaken til at personen ikke er i arbeid, vil kravet til årsakssammenheng ikke være oppfylt."
                    }
                )
            }
            includePhrase(KlippInnFraVedtak)

            paragraph {
                text(
                    bokmal {
                        +"Vi vurderer at klagers helseplager er en medvirkende årsak til at klager ikke er i arbeid, men at sykdom ikke er hovedårsaken. "
                        +"Det legges avgjørende vekt på at andre årsaker enn de rent medisinske synes å utgjøre vesentlige begrensninger med hensyn til at klager ikke fungerer i inntektsgivende arbeid."
                    }
                )
            }

            includePhrase(KlippInnFraVedtak)
            includePhrase(VedtaketBlirIkkeEndret)
            includePhrase(VedtaketOpprettholdes)
        }
    }

    //§ 12-7 Nedsatt innteksevne
    object NedsattInntektsevne : RedigerbarOutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, RedigerbarPhraseBrevdata>.template() {

            includePhrase(HvaKlagesakenGjelderOverskrift)
            includePhrase(NedsattInntektsevneHvaKlagesakenGjelder)

            paragraph { text(bokmal { +"Klagen vurderes etter folketrygdloven §§ 12-7 – nedsatt inntektsevne." }) }

            includePhrase(ViHarVurdertKlagen)
            includePhrase(KlagersAnfoersler)

            includePhrase(NedsattInntektsevneVurderingAvKlagen)
            paragraph {
                text(
                    bokmal {
                        +"Inntekt etter uførhet skal fastsettes til den inntekten personen forutsettes å kunne skaffe seg ved å utnytte restinntektsevnen. "
                        +"Inntekten skal dermed angi det inntektsnivået personen forutsettes å kunne ha etter uførhet. "
                        +"Dette vil ikke nødvendigvis tilsvare den faktiske inntekten på virkningstidspunktet. "
                        +"Hvis vedkommende har inntektsmuligheter som ikke utnyttes, skal disse medregnes ved fastsettelsen av inntekt etter uførhet. "
                        +"Dette betyr at inntekt etter uførhet kan settes til et høyere nivå enn den faktiske inntekten. "
                    }
                )
            }

            includePhrase(KlippInnFraVedtak)
            includePhrase(VedtaketBlirIkkeEndret)
            includePhrase(VedtaketOpprettholdes)
        }
    }

    //§ 12-7 Kombinasjon nedsatt inntektsevne
    object KombinasjonNedsattInntektsevne : RedigerbarOutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, RedigerbarPhraseBrevdata>.template() {

            includePhrase(HvaKlagesakenGjelderOverskrift)
            includePhrase(NedsattInntektsevneHvaKlagesakenGjelder)

            paragraph { text(bokmal { +"Klagen vurderes etter folketrygdloven §§ 12-7, 12-9 og 12-10 – nedsatt inntektsevne." }) }

            includePhrase(ViHarVurdertKlagen)
            includePhrase(KlagersAnfoersler)

            includePhrase(NedsattInntektsevneVurderingAvKlagen)
            paragraph {
                text(
                    bokmal {
                        +"Uføretidspunktet skal som hovedregel fastsettes til det tidspunktet da personens inntektsevne ble varig nedsatt med minst halvparten på grunn av sykdom, skade eller lyte. "
                        +"Uføretidspunktet er fastsatt til XXX. "
                    }
                )
                text(bokmal { +"Hvis uføretidspunktet er påklaget, hent mal fra §12-8." }, ITALIC)
            }
            paragraph {
                text(
                    bokmal {
                        +"Inntekt før uførhet (IFU) skal fastsettes til vedkommende sin normale inntektssituasjon før uføretidspunktet i full stilling. "
                        +"IFU er fastsatt til XXX. Oppjustert til i dag utgjør dette XXX kroner."
                    }
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Inntekt etter uførhet (IEU) skal fastsettes til den inntekten som vedkommende forutsetter å kunne skaffe seg ved å utnytte restinntektsevnen. "
                        +"Inntekten skal dermed angi det inntektsnivået personen forutsettes å kunne ha etter uførhet. "
                        +"Dette vil ikke nødvendigvis tilsvare den faktiske inntekten på virkningstidspunktet. "
                        +"Hvis vedkommende har inntektsmuligheter som ikke utnyttes, skal disse medregnes ved fastsettelsen av IEU. "
                        +"Dette betyr at IEU kan settes til et høyere nivå enn den faktiske inntekten. "
                        +"IEU er fastsatt til XXX"
                    }
                )
            }

            includePhrase(KlippInnFraVedtak)
            includePhrase(VedtaketBlirIkkeEndret)
            includePhrase(VedtaketOpprettholdes)
        }
    }

    //§ 12-8 Uføretidspunkt
    object Ufoeretidspunkt : RedigerbarOutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, RedigerbarPhraseBrevdata>.template() {

            includePhrase(HvaKlagesakenGjelderOverskrift)
            paragraph {
                text(
                    bokmal {
                        +"Vi viser til klagen av "
                        +fritekst("dato") + " på vedtak av "
                        +fritekst("dato") + " der klager ble innvilget uføretrygd. "
                        +"Klagen gjelder uføretidspunktet. "
                        +"Klagefristen er overholdt."
                    }
                )
            }
            paragraph { text(bokmal { +"Klagen vurderes etter folketrygdloven §12-8 – uføretidspunkt." }) }

            includePhrase(ViHarVurdertKlagen)
            includePhrase(KlagersAnfoersler)

            title1 { text(bokmal { +"Vurdering av klagen" }) }
            paragraph {
                text(
                    bokmal {
                        +"Uføretidspunktet skal som hovedregel fastsettes til det tidspunktet da personens inntektsevne ble varig nedsatt med minst halvparten på grunn av sykdom, skade eller lyte. "
                        +"Det er ikke tilstrekkelig at vedkommende en kortere periode har vært helt eller delvis arbeidsufør. "
                        +"For personer som var i arbeid frem til sykmelding, vil uføretidspunktet ofte settes til sykmeldingstidspunktet. "
                        +"Det er ikke noe krav om at nedsettelsen av inntektsevnen må gjelde samme sykdommen som senere gir grunnlag for uføretrygd så lenge inntektsevnen er varig nedsatt."
                    }
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"I vurderingen av uføretidspunktet legges det stor vekt på om det foreligger et klart skjæringstidspunkt, ofte et sykmeldingstidspunkt, som gir uttrykk for når arbeidsuførheten inntrådte. "
                        +"I fravær av et klart skjæringstidspunkt, for eksempel på grunn av arbeidsløshet eller flere sykmeldingstidspunkter, blir vurderingen mer skjønnsmessig."
                    }
                )
            }

            includePhrase(KlippInnFraVedtak)
            includePhrase(VedtaketBlirIkkeEndret)
            includePhrase(VedtaketOpprettholdes)
        }
    }

    //§ 12-9 Fastsettelse av Inntekt Før Uførhet IFU
    object FastsettelseIFU : RedigerbarOutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, RedigerbarPhraseBrevdata>.template() {

            includePhrase(InnvilgetUfoeretrygd)

            paragraph { text(bokmal { +"Klagen vurderes etter folketrygdloven §12-9 – fastsettelse av inntekt før uføhet." }) }

            includePhrase(ViHarVurdertKlagen)
            includePhrase(KlagersAnfoersler)

            title1 { text(bokmal { +"Vurdering av klagen" }) }
            paragraph {
                text(
                    bokmal {
                        +"Inntekt før uførhet skal være et uttrykk for personens normale inntektssituasjon før uføretidspunktet. "
                        +"Hva som skal anses som normal årsinntekt må vurderes konkret i hvert tilfelle. "
                        +"Utgangspunktet er at inntekt før uførhet skal fastsettes til det vedkommende hadde i inntekt umiddelbart før uføretidspunktet. "
                        +"Dette kan være inntekten på uføretidspunktet eller året før."
                    }
                )
            }

            includePhrase(KlippInnFraVedtak)
            includePhrase(VedtaketBlirIkkeEndret)
            includePhrase(VedtaketOpprettholdes)
        }
    }

    //§ 12-9 Fastsettelse av Inntekt Etter Uførehet IEU
    object FastsettelseIEU : RedigerbarOutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, RedigerbarPhraseBrevdata>.template() {

            includePhrase(InnvilgetUfoeretrygd)

            paragraph { text(bokmal { +"Klagen vurderes etter folketrygdloven §12-9 – fastsettelse av inntekt før uføhet." }) }

            includePhrase(ViHarVurdertKlagen)
            includePhrase(KlagersAnfoersler)

            title1 { text(bokmal { +"Vurdering av klagen" }) }
            paragraph {
                text(
                    bokmal {
                        +"Inntekt etter uførhet skal fastsettes til den inntekten som personen forutsetter å kunne skaffe seg ved å utnytte restinntektsevnen. "
                        +"Utgangspunktet for fastsettelsen av inntekt etter uførhet (IEU) er personens fremtidige pensjonsgivende inntekt. "
                        +"Hvis vedkommende har inntektsmuligheter som ikke utnyttes skal disse medregnes. "
                        +"Inntekten etter uførhet kan derfor settes høyere enn faktisk pensjonsgivende inntekt."
                    }
                )
            }

            includePhrase(KlippInnFraVedtak)
            includePhrase(VedtaketBlirIkkeEndret)
            includePhrase(VedtaketOpprettholdes)
        }
    }

    //§ 12-10 Fastsettelse av Uføregrad
    object FastsettelseUfoeregrad : RedigerbarOutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, RedigerbarPhraseBrevdata>.template() {

            includePhrase(InnvilgetUfoeretrygd)

            paragraph { text(bokmal { +"Klagen vurderes etter folketrygdloven §12-10 – fastsettelse av uføregrad." }) }

            includePhrase(ViHarVurdertKlagen)
            includePhrase(KlagersAnfoersler)

            title1 { text(bokmal { +"Vurdering av klagen" }) }
            paragraph {
                text(
                    bokmal {
                        +"Uføregraden skal fastsettes ved å sammenligne inntektsevne før og etter uførhet. "
                        +"Dersom vedkommende har tapt hele inntektsevnen, skal uføregraden settes til 100 prosent. "
                        +"Dersom deler av inntektsevnen er tapt, skal uføregraden svare til den delen som er tapt. "
                        +"Det skal alltid vurderes om uføregraden kan settes lavere enn 100 prosent."
                    }
                )
            }
            paragraph { text(bokmal { +"Inntekt før uførhet (IFU) skal fastsettes til vedkommende sin normale inntektssituasjon før uføretidspunktet i full stilling" }) }
            paragraph {
                text(
                    bokmal {
                        +"Inntekt etter uførhet (IEU) skal fastsettes til den inntekten som vedkommende forutsetter å kunne skaffe seg ved å utnytte restinntektsevnen. "
                        +"Inntekten skal dermed angi det inntektsnivået personen forutsettes å kunne ha etter uførhet. "
                        +"Dette vil ikke nødvendigvis tilsvare den faktiske inntekten på virkningstidspunktet. "
                        +"Hvis vedkommende har inntektsmuligheter som ikke utnyttes, skal disse medregnes ved fastsettelsen av IEU. "
                        +"Dette betyr at IEU kan settes til et høyere nivå enn den faktiske inntekten."
                    }
                )
            }

            includePhrase(KlippInnFraVedtak)
            includePhrase(VedtaketBlirIkkeEndret)
            includePhrase(VedtaketOpprettholdes)
        }
    }

    //§ 12-11 Beregning av uføretrygd
    object BeregningAvUfoeretrygd : RedigerbarOutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, RedigerbarPhraseBrevdata>.template() {

            includePhrase(HvaKlagesakenGjelderOverskrift)
            paragraph {
                text(
                    bokmal {
                        +"Vi viser til klagen av "
                        +fritekst("dato") + " på vedtak av "
                        +fritekst("dato") + " der "
                        +fritekst("kort om resultatet i vedtaket") + ". "
                        +"Klagefristen er overholdt."
                    }
                )
            }
            paragraph { text(bokmal { +"Klagen vurderes etter folketrygdloven §12-11 – beregning av uføretrygd." }) }

            includePhrase(ViHarVurdertKlagen)
            includePhrase(KlagersAnfoersler)

            title1 { text(bokmal { +"Vurdering av klagen" }) }
            paragraph {
                text(
                    bokmal {
                        +"Uføretrygden beregnes på grunnlag av pensjonsgivende inntekt i de fem siste kalenderårene før uføretidspunktet. "
                        +"Gjennomsnittlig inntekt i de tre beste inntektsårene legges til grunn. "
                        +" Pensjonsgivende inntekt over 6 ganger grunnbeløpet regnes ikke med i grunnlaget. "
                        +" Inntekten i de aktuelle årene oppjusteres til virkningstidspunktet på bakgrunn av grunnbeløpet."
                    }
                )
            }

            includePhrase(KlippInnFraVedtak)
            includePhrase(VedtaketBlirIkkeEndret)
            includePhrase(VedtaketOpprettholdes)
        }
    }

    //§ 12-12 Trygdetid
    object Trygdetid : RedigerbarOutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, RedigerbarPhraseBrevdata>.template() {

            includePhrase(HvaKlagesakenGjelderOverskrift)
            paragraph {
                text(
                    bokmal {
                        +"Vi viser til klagen av "
                        +fritekst("dato") + " på vedtak av "
                        +fritekst("dato") + " der "
                        +fritekst("beskriv kort hva avslaget gjelder/vedtaket gjelder. Eks.. der klager fikk avslag på sitt krav om uføretrygd") + ". "
                        +"Klagefristen er overholdt."
                    }
                )
            }
            paragraph { text(bokmal { +"Klagen vurderes etter folketrygdloven §§ 12-12 og 12-13 – trygdetid og uføretrygdens størrelse." }) }

            includePhrase(ViHarVurdertKlagen)
            includePhrase(KlagersAnfoersler)

            title1 { text(bokmal { +"Vurdering av klagen" }) }
            paragraph {
                text(
                    bokmal {
                        +"Trygdetid regnes fra fylte 16 år, eller fra en ble medlem av folketrygden til og med året en fyller 66 år, for at det skal innvilges full uføretrygd må samlet trygdetid tilsvare 40 år. "
                        +"Uføretrygdens størrelse skal imidlertid avkortes dersom trygdetiden er kortere enn 40 år. "
                        +"Ved fastsettelse av trygdetid skille det mellom faktisk trygdetid som er perioden før uføretidspunktet, og fremtidig trygdetid som er fra uføretidspunktet og frem til fylte 66 år."
                    }
                )
            }
            paragraph { text(bokmal { +"Dersom mindre enn 4/5 av opptjeningstiden kan regnes som trygdetid reduseres den fremtidige trygdetiden." }) }
            paragraph { text(bokmal { +"Faktisk trygdetid beregnes i antall hele år, måneder og dager, dager avrundes opp til hel måned." }) }
            paragraph {
                text(
                    bokmal {
                        +"I det påklagde vedtaket er klager gitt en trygdetid på "
                        +fritekst("xxxx år") + "."
                        +"Dersom den fastsatte trygdetiden er mindre enn 40 år reduseres uføretrygden størrelse. "
                        +"Det vil si at klager som har en trygdetid på "
                        +fritekst("xxxx år") + ", vil får en uføretrygd med en uføregrad på 100 prosent som tilsvarer "
                        +fritekst("xx/40") + "."
                    }
                )
            }
            paragraph { text(bokmal { +"<Ved bruk av unntaksreglene, se verktøykassen>" }, ITALIC) }

            includePhrase(KlippInnFraVedtak)
            includePhrase(VedtaketBlirIkkeEndret)
            includePhrase(VedtaketOpprettholdes)
        }
    }

    //§ 12-13 Ung ufør
    object UngUfoer : RedigerbarOutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, RedigerbarPhraseBrevdata>.template() {

            includePhrase(HvaKlagesakenGjelderOverskrift)
            paragraph {
                text(
                    bokmal {
                        +"Vi viser til klagen av "
                        +fritekst("dato") + " på vedtak av "
                        +fritekst("dato") + " der klager fikk avslag på søknad om ung ufør. "
                        +"Klagefristen er overholdt."
                    }
                )
            }
            paragraph { text(bokmal { +"Klagen vurderes etter folketrygdloven § 12-13 tredje ledd – ung ufør." }) }

            includePhrase(ViHarVurdertKlagen)
            includePhrase(KlagersAnfoersler)

            title1 { text(bokmal { +"Vurdering av klagen" }) }
            paragraph {
                text(bokmal { +"For å ha rett til å få uføretrygden beregnet etter reglene for unge uføre må:" })
                list {
                    item { bokmal { +"du ha blitt ufør før du ble 26 år." } }
                    item { bokmal { +"uførheten skyldes alvorlig og varig sykdom." } }
                    item { bokmal { +"sykdommen være klart dokumentert." } }
                }
            }
            paragraph { text(bokmal { +"Dette går fram av folketrygdloven § 12-13 tredje ledd. " }) }

            title1 { text(bokmal { +"Uføretidspunkt før fylte 26 år" }) }
            paragraph {
                text(
                    bokmal {
                        +"I det påklagde vedtaket er uføretidspunktet fastsatt til "
                        +fritekst("xxxx") + ". Fra dette tidspunktet er det vurdert at klagers inntektsevne er varig nedsatt med minst 50 prosent grunnet sykdom. "
                        +"Uføretidspunktet er fastsatt til før fylte 26 år, forutsetningen for å vurdere om klager har rettigheter som ung ufør er altså oppfylt."
                    }
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Klager fylte 26 år den "
                        +fritekst("xxxx") + ", dokumentasjon som er tidsnær til tidspunktet klager fylte 26 år vil være avgjørende i vurderingen av hvorvidt klagers helseplager kvalifiserer til rettigheter som ung ufør."
                    }
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"I nyere rettspraksis fra Høyesterett er det lagt til grunn at det skal tas utgangspunkt i den medisinske lidelsen og dens alvorlighet. "
                        +"Det faktiske funksjonsnivået som selvstendig moment får bare betydning der det er noe tvil om den medisinske lidelsen i seg selv er alvorlig nok. "
                        +"Da kan et ekstraordinært lavt fungeringsnivå likevel tilsi at vilkåret er oppfylt."
                    }
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"I retningslinjene til folketrygdloven § 12-13 er det angitt en liste over diagnoser som kan anses som alvorlige. "
                        +"Selv om denne listen ikke er uttømmende gir den klare holdepunkter for hva som kan anses som alvorlig sykdom."
                    }
                )
            }

            title1 { text(bokmal { +"Uføretidspunkt etter fylte 26 år" }) }
            paragraph {
                text(
                    bokmal {
                        +"I det påklagde vedtaket er uføretidspunktet fastsatt til "
                        +fritekst("xxxx") + ". Uføretidspunktet skal fastsettes til det tidspunktet da inntektsevnene ble varig nedsatt med minst 50 prosent. "
                        +"Det er altså ikke avgjørende når man er diagnostiert med sykdom. "
                        +"For at klager skal kunne vurderes mot rettigheter som ung ufør etter folketrygdlovens § 12-13 tredje ledd må uføretidspunktet være satt til før "
                        +fritekst("xxxx") + " da klager fylte 26 år."
                    }
                )
            }

            includePhrase(KlippInnFraVedtak)
            includePhrase(VedtaketBlirIkkeEndret)
            includePhrase(VedtaketOpprettholdes)
        }
    }

    //§ 12-14 Reduksjon på grunn av inntekt
    object AutomatiskInntektsreduksjon : RedigerbarOutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, RedigerbarPhraseBrevdata>.template() {

            includePhrase(HvaKlagesakenGjelderOverskrift)
            paragraph {
                text(
                    bokmal {
                        +"Vi viser til klagen av "
                        +fritekst("dato") + " på vedtak av "
                        +fritekst("dato") + " der uføretrygden ble redusert mot arbeidsinntekt. "
                        +"Klagefristen er overholdt."
                    }
                )
            }
            paragraph { text(bokmal { +"Klagen vurderes etter folketrygdloven § 12-14, samt kapittel 3 i forskrift om uføretrygd fra folketrygden." }) }

            includePhrase(ViHarVurdertKlagen)
            includePhrase(KlagersAnfoersler)

            title1 { text(bokmal { +"Vurdering av klagen" }) }
            paragraph {
                text(
                    bokmal {
                        +"Det framgår av folketrygdloven § 12-14 at utbetalingen av uføretrygden skal reduseres dersom inntektsgrensen overskrides. "
                        +"Mottakere av uføretrygd skal i utgangspunktet selv melde fra om pensjonsgivende inntekt som har betydning for størrelsen på uføretrygden."
                    }
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Nav kan imidlertid legge til grunn inntektsopplysninger fra A-ordningen for reduksjon av uføretrygd. "
                        +"Dette gjelder i tilfeller hvor opplysninger om inntekt hittil i år som er mottatt fra A-ordningen er høyere enn inntektsgrensen eller en forventet inntekt personen selv har meldt inn. "
                        +"A-ordningen er en samordnet måte for arbeidsgivere å rapportere opplysninger om inntekt til Nav, Statistisk sentralbyrå og Skatteetaten."
                    }
                )
            }

            includePhrase(KlippInnFraVedtak)
            includePhrase(VedtaketBlirIkkeEndret)
            includePhrase(VedtaketOpprettholdes)
        }
    }

    //§ 12-14 Reduksjon på grunn av inntekt
    object Etteroppgjoer : RedigerbarOutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, RedigerbarPhraseBrevdata>.template() {

            includePhrase(HvaKlagesakenGjelderOverskrift)
            paragraph {
                text(
                    bokmal {
                        +"Vi viser til klagen av "
                        +fritekst("dato") + " på varsel/vedtak av "
                        +fritekst("dato") + " der resultatet av etteroppgjøret viser at klager har fått "
                        +fritekst("antall kr") + " kroner for mye i uføretrygd i "
                        +fritekst("år") + "."
                        +"Klagefristen er overholdt."
                    }
                )
            }
            paragraph { text(bokmal { +"Klagen vurderes etter folketrygdloven § 12-14, samt etter kapittel 3 i forskrift om uføretrygd fra folketrygden." }) }
            paragraph { text(bokmal { +"<Husk å bruke ny hjemmel § 12-14 EO - arbeidsforsøk, der det er aktuelt!>" }) }

            includePhrase(ViHarVurdertKlagen)
            includePhrase(KlagersAnfoersler)

            title1 { text(bokmal { +"Vurdering av klagen" }) }
            paragraph {
                text(
                    bokmal {
                        +"Det skal foretas et etteroppgjør når den uføretrygdede i løpet av et kalenderår har fått utbetalt for lite eller for mye uføretrygd. "
                        +"Dette gjøres etter at inntekten er skattefastsatt. "
                        +"Uføretrygd i kalenderåret sammenlignes da med ny fastsatt årlig uføretrygd basert på den fastsatte inntekten fra Skatteetaten."
                    }
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Dersom det er oppgitt feil inntekt til Skatteetaten, eller en er uenig i den fastsatte inntekten må Skatteetaten kontaktes. "
                        +"Dersom den pensjonsgivende inntekten blir endret, vil Nav revurdere saken og eventuelle beløp som er krevd inn bli etterbetalt. "
                        +"Utgangspunktet er at all pensjonsgivende inntekt medfører reduksjon av uføretrygd. "
                        +"Det er gjort noen unntak som gjelder blant annet erstatning for påført inntektstap og etterslepsinntekter som for eksempel feriepenger eller salg av produksjonsmidler i forbindelse med avslutning av arbeid."
                    }
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Det kan også gjøres et unntak i etteroppgjøret dersom arbeidsinntekten i løpet av året har oversteget 80 prosent av oppjustert inntekt før uførhet, og dette kan vurderes som et arbeidsforsøk. "
                        +"Vi har vurdert om klager kommer inn under bestemmelsen for arbeidsforsøk, men vi kan ikke se at dette er mulig."
                    }
                )
            }

            includePhrase(KlippInnFraVedtak)
            includePhrase(VedtaketBlirIkkeEndret)
            includePhrase(VedtaketOpprettholdes)
        }
    }

    //§ 12-14 Reduksjon på grunn av inntekt
    object EtteroppgjoerBarnetillegg : RedigerbarOutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, RedigerbarPhraseBrevdata>.template() {

            includePhrase(HvaKlagesakenGjelderOverskrift)
            paragraph {
                text(
                    bokmal {
                        +"Vi viser til klagen av "
                        +fritekst("dato") + " på varsel/vedtak av "
                        +fritekst("dato") + " der resultatet av etteroppgjøret viser at klager har fått "
                        +fritekst("antall kr") + " kroner for mye i barnetillegg i "
                        +fritekst("år") + ". Vedtaket ble iverksatt "
                        +fritekst("dato") + ". Klagefristen er overholdt."
                    }
                )
            }
            paragraph { text(bokmal { +"Klagen vurderes etter folketrygdloven §§ 12-14 og 12-16, samt etter kapittel 3 og 4 i forskrift om uføretrygd fra folketrygden." }) }

            includePhrase(ViHarVurdertKlagen)
            includePhrase(KlagersAnfoersler)

            title1 { text(bokmal { +"Vurdering av klagen" }) }
            paragraph {
                text(
                    bokmal {
                        +"Det skal foretas et etteroppgjør når den uføretrygdede i løpet av et kalenderår har fått utbetalt for lite eller for mye uføretrygd og barnetillegg. "
                        +"Dette gjøres etter at personinntekten er skattefastsatt."
                    }
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Det er personinntekten til klager og annen forelder etter skattelovens § 12-2 som har betydning for størrelsen på barnetillegget. "
                        +"Utbetalt barnetillegg i kalenderåret sammenlignes med ny fastsatt årlig personinntekt basert på den skattefastsatte inntekten."
                    }
                )
            }

            includePhrase(KlippInnFraVedtak)
            includePhrase(VedtaketBlirIkkeEndret)
            includePhrase(VedtaketOpprettholdes)
        }
    }

    //§ 12-15 Barnetillegg
}
