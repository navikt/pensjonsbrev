package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder.Redigerbar
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brev.template.BrevbakerDSLInternal
import no.nav.pensjon.brevbaker.api.model.DisplayText
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

/**
 * Dokumenterer og verifiserer hvilke saksbehandlervalg (felter på SaksbehandlerValg, med datatype og displayText)
 * som er tilgjengelige for hvert redigerbare brev i pensjon-modulen. Testen gjør ingen rendring —
 * den sjekker kun strukturen på `saksbehandlerValg`-typen til hver mal, slik at endringer i
 * hvilke valg saksbehandler kan gjøre blir synlige i en PR-diff.
 */
class SaksbehandlervalgPerMalTest {

    @Test
    @OptIn(BrevbakerDSLInternal::class)
    fun `hvert redigerbart brev har forventede saksbehandlervalg`() {
        val faktiske = ProductionTemplates.hentRedigerbareMaler().associate { mal ->
            val saksbehandlerValgType = mal.template.letterDataType.members
                .single { it.name == "saksbehandlerValg" }
                .returnType.classifier as KClass<*>

            // Maler migrert til saksbehandlervalg-DSL-en deklarerer valgene sine i template-body
            // (`saksbehandlervalg("id", "...")`) i stedet for som felter på en egen SaksbehandlerValg-type,
            // og har `saksbehandlerValg: SaksbehandlervalgIDSL` på Dto-en. De registrerte valgene finnes da i
            // `mal.template.saksbehandlervalg`. Ikke-migrerte maler har fremdeles en typet SaksbehandlerValg-klasse,
            // som vi da faller tilbake til å reflektere over.
            val valg = if (saksbehandlerValgType == SaksbehandlervalgIDSL::class) {
                mal.template.saksbehandlervalg.orEmpty().entries.map { (id, verdi) -> "$id: ${verdi.typename} (${verdi.displayText})" }
            } else {
                saksbehandlerValgType.memberProperties.map { prop ->
                    val displayText = prop.findAnnotation<DisplayText>()?.text ?: "<mangler>"
                    "${prop.name}: ${prop.returnType} ($displayText)"
                }
            }

            (mal.kode as Redigerbar) to valg.sorted()
        }

        val forventet = mapOf(
            Redigerbar.BRUKERTEST_BREV_PENSJON_2025 to listOf(
                "denBesteKaken: no.nav.pensjon.brev.api.model.maler.redigerbar.BrukerTestBrevDto.DenBesteKaken (Den beste kaken)",
                "kaffemaskinensTilgjengelighet: Boolean (Kaffemaskinens tilgjengelighet)",
                "kontorplantenTorlill: Boolean (Kontorplanten TorLill)",
                "utsiktenFraKontoret: no.nav.pensjon.brev.api.model.maler.redigerbar.BrukerTestBrevDto.UtsiktenFraKontoret (Utsikten fra kontoret)",
            ),
            Redigerbar.GP_AVSLAG_GJENLEVENDEPENSJON to listOf(
                "folketrygdlovenParagraf: no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagGjenlevendepensjonDto.FolketrygdlovenParagraf (Velg folketrygdloven paragraf:)",
            ),
            Redigerbar.GP_AVSLAG_GJENLEVENDEPENSJON_UTLAND to emptyList(),
            Redigerbar.GP_INNVILGELSE_BOSATT_NORGE_ETTER_UTLAND to emptyList(),
            Redigerbar.GP_OPPHOER_GJENLEVENDEPENSJON to listOf(
                "folketrygdlovenAlternativ: no.nav.pensjon.brev.api.model.maler.redigerbar.OpphoerGjenlevendepensjonDto.FolketrygdlovenAlternativ (Velg § 17-11 alternativ:)",
                "opphoerMedTilbakekreving: Boolean (Hvis opphør med tilbakekreving)",
            ),
            Redigerbar.INFORMASJON_OM_SAKSBEHANDLINGSTID to listOf(
                "soeknadMottattFraUtland: Boolean (Søknad mottatt fra utland)",
                "venterPaaSvarAFP: Boolean (Venter på svar AFP)",
            ),
            Redigerbar.P1_SAMLET_MELDING_OM_PENSJONSVEDTAK to emptyList(),
            Redigerbar.P1_SAMLET_MELDING_OM_PENSJONSVEDTAK_V2 to emptyList(),
            Redigerbar.PE_ANKE_TILSVAR_TIL_ANKENDE_PART to emptyList(),
            Redigerbar.PE_AP_AVSLAG_FOR_LITE_TRYGDETID to emptyList(),
            Redigerbar.PE_AP_AVSLAG_GJENLEVENDERETT to listOf(
                "avdoedNavn: String (Avdød navn)",
                "ekteskapUnderFemAar: Boolean (Ekteskap under fem år)",
                "harTrygdetid: Boolean (Inkluder tekst om trygdetid)",
                "hjemmelAvtaleland: Boolean (Hjemmel avtaleland)",
                "hjemmelEOES: Boolean (Hjemmel EØS)",
                "samboerUtenFellesBarn: Boolean (Samboer uten felles barn)",
                "under20AarBotid: Boolean (Under 20 år botid)",
                "underEttAarsMedlemstidEOESEllerAvtaleland: Boolean (Under ett års medlemstid EØS eller avtaleland)",
                "underTreFemAarsMedlemstidEOESSak: Boolean (Under tre/fem års medlemstid EØS-sak)",
                "underTreFemAarsMedlemstidNasjonalSak: Boolean (Under tre/fem års medlemstid nasjonal sak)",
                "underTrefemAarsMedlemstidAvtalesak: Boolean (Under tre/fem års medlemstid avtalesak)",
            ),
            Redigerbar.PE_AP_ENDRET_UTTAKSGRAD to listOf(
                "etterbetaling: Boolean (Hvis etterbetaling)",
            ),
            Redigerbar.PE_AP_ENDRET_UTTAKSGRAD_STANS_BRUKER_ELLER_VERGE to emptyList(),
            Redigerbar.PE_AP_ENDRET_UTTAKSGRAD_STANS_IKKE_BRUKER_VERGE to listOf(
                "aarsak: no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvUttaksgradStansIkkeBrukerEllerVergeDto.Aarsak (Årsak)",
            ),
            Redigerbar.PE_AP_ENDRING_FLYTTING_MELLOM_LAND to listOf(
                "aarsakTilAtPensjonenOeker: no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDto.AarsakTilAtPensjonenOeker (Relevant hvis innvandret)",
                "endringIPensjonen: Boolean (Endring i pensjon)",
                "etterbetaling: Boolean (Etterbetaling)",
                "reduksjonTilbakeITid: Boolean (Reduksjon tilbake i tid)",
            ),
            Redigerbar.PE_AP_ENDRING_GJENLEVENDERETT to listOf(
                "avdoedeHarRedusertTrygdetidEllerPoengaar: Boolean (Hvis avdøde har redusert trygdetid/poengår)",
                "brukerUnder67OgAvdoedeHarRedusertTrygdetidEllerPoengaar: Boolean (Hvis bruker under 67 år og avdøde har redusert trygdetid/poengår)",
                "etterbetaling: Boolean (Hvis etterbetaling av pensjon)",
                "ingenEndringBelop: Boolean (Hvis ingen endring av pensjon)",
                "okningBelop: Boolean (Hvis økning av pensjon)",
                "oktTilleggMpn: Boolean (Hvis økt tilleggspensjon, men ingen endring i total alderspensjon)",
                "omregnetTilEnsligISammeVedtak: Boolean (Omregnet til enslig i samme vedtak)",
            ),
            Redigerbar.PE_AP_ENDRING_INSTITUSJONSOPPHOLD to listOf(
                "alderspensjonRedusert: Boolean (Alderspensjon redusert)",
                "alderspensjonStanset: Boolean (Alderspensjon stanset)",
                "alderspensjonUnderOppholdIInstitusjon: Boolean (Alderspensjon under opphold i institusjon)",
                "alderspensjonUnderSoning: Boolean (Alderspensjon under soning)",
                "alderspensjonVedVaretektsfengsling: Boolean (Alderspensjon ved varetektsfengsling)",
                "etterbetaling: Boolean (Hvis etterbetaling)",
                "hvisReduksjonTilbakeITid: Boolean (Hvis reduksjon tilbake i tid)",
                "informasjonOmSivilstandVedInstitusjonsopphold: Boolean (Informasjon om sivilstand ved institusjonsopphold)",
            ),
            Redigerbar.PE_AP_ENDRING_PGA_OPPTJENING to emptyList(),
            Redigerbar.PE_AP_INNHENTING_DOKUMENTASJON_FRA_BRUKER to emptyList(),
            Redigerbar.PE_AP_INNHENTING_INFORMASJON_FRA_BRUKER to listOf(
                "amerikanskSocialSecurityNumber: Boolean (Amerikansk social security number)",
                "bankopplysninger: Boolean (Bankopplysninger)",
                "boOgArbeidsperioder: Boolean (Bo- og arbeidsperioder)",
                "bosattIEoesLandSedErEoesBlanketter: Boolean (Bosatt i EØS-land. SED-er/EØS-blanketter)",
                "eps60aarOgInntektUnder1g: Boolean (Ektefelle/partner/samboer 60 år og inntekt under 1G)",
                "eps62aarOgInntektUnder1gBoddArbeidUtland: Boolean (Ektefelle/partner/samboer 62 år og bodd og/eller arbeidet i utlandet)",
                "epsInntektUnder2g: Boolean (Ektefelles/partners/samboers inntekt under 2G)",
                "forsoergerEpsBosattIUtlandet: Boolean (Forsørger ektefellen/partneren/samboeren som er bosatt i utlandet)",
                "inntektsopplysninger: Boolean (Inntektsopplysninger)",
                "manglendeOpptjening: Boolean (Manglende opptjening)",
                "registreringAvSivilstand: Boolean (Registrering av sivilstand)",
                "tidspunktForUttak: Boolean (Tidspunkt for uttak / ønsket uttaksgrad)",
            ),
            Redigerbar.PE_AP_INNHENTING_OPPLYSNINGER_FRA_BRUKER to emptyList(),
            Redigerbar.PE_AP_INNVILGELSE to listOf(
                "etterbetaling: Boolean (Hvis etterbetaling av pensjon)",
                "kravVirkDatoFomSenereEnnOensketUttakstidspunkt: Boolean (Virkningstidspunktet er senere enn ønsket uttakstidspunkt)",
                "vanligSkattetrekk: Boolean (Bruk vanlig skattetrekk)",
            ),
            Redigerbar.PE_AP_INNVILGELSE_TRYGDEAVTALE to listOf(
                "etterbetaling: Boolean (Hvis etterbetaling av pensjon)",
                "nyBeregningAvInnvilgetAP: Boolean (Tittel - Ny beregning av innvilget alderspensjon. Ingen endring av uttaksgraden)",
            ),
            Redigerbar.PE_BEKREFTELSE_PAA_FLYKTNINGSTATUS to emptyList(),
            Redigerbar.PE_BEKREFTELSE_PAA_PENSJON to emptyList(),
            Redigerbar.PE_FORESPOERSELOMDOKUMENTASJONAVBOTIDINORGE_ALDER to listOf(
                "opplystOmBotid: Boolean (Opplyst om botid)",
            ),
            Redigerbar.PE_FORESPOERSEL_DOKUM_BOTIDINORGE_ETTERLATTE to listOf(
                "opplystOmBotid: Boolean (Opplyst om botid)",
            ),
            Redigerbar.PE_INFORMASJON_OM_GJENLEVENDERETTIGHETER to listOf(
                "gjenlevendeHarBarnUnder18MedAvdoed: Boolean (Gjenlevende har barn under 18 år sammen med avdøde)",
                "gjenlevenderHarEllerKanHaAFPIOffentligSektor: Boolean (Gjenlevende har eller kan ha AFP i offentlig sektor)",
                "gjenlevevendeHarAfpOgUttaksgradPaaApSattTilNull: Boolean (Gjenlevende har AFP privat og uttaksgrad på AP satt til 0)",
                "hvorBorBruker: no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmGjenlevenderettigheterDto.HvorBorBruker (Hvor bor bruker)",
                "infoHvordanSoekeOmstillingsstoenad: Boolean (Hvis gradert uføretrygd, info søke omstillingsstønad)",
                "infoOmstillingsstoenad: Boolean (Hvis gradert uføretrygd, info omstillingsstønad)",
                "infoVilkaarSkiltGjenlevende: Boolean (Hvis gradert uføretrygd, info vilkår skilt gjenlevende)",
                "vilkarForGjenlevendeytelsen: no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmGjenlevenderettigheterDto.VilkarForGjenlevendeytelsen (Vilkår for gjenlevendeytelsen)",
            ),
            Redigerbar.PE_KLAGE_ORIENTERING_OM_OVERSENDELSE_KLAGEINSTANS to emptyList(),
            Redigerbar.PE_KLAGE_ORIENTERING_OM_SAKSBEHANDLINGSTID to emptyList(),
            Redigerbar.PE_OMSORG_EGEN_MANUELL to listOf(
                "aarEgenerklaringOmsorgspoeng: kotlin.Int (År egenerklæring omsorgspoeng)",
                "aarInnvilgetOmsorgspoeng: kotlin.Int (År innvilget omsorgspoeng)",
            ),
            Redigerbar.PE_ORIENTERING_OM_FORLENGET_SAKSBEHANDLINGSTID to emptyList(),
            Redigerbar.PE_OVERSETTELSE_AV_DOKUMENTER to emptyList(),
            Redigerbar.PE_TILBAKEKREVING_AV_FEILUTBETALT_BELOEP to emptyList(),
            Redigerbar.PE_VARSEL_OM_MULIG_AVSLAG to emptyList(),
            Redigerbar.PE_VARSEL_OM_TILBAKEKREVING_FEILUTBETALT_BELOEP to listOf(
                "hvisAktueltAaIleggeRentetillegg: Boolean (Hvis aktuelt å ilegge rentetillegg)",
            ),
            Redigerbar.PE_VARSEL_REVURDERING_AV_PENSJON to listOf(
                "tittelValg: no.nav.pensjon.brev.api.model.maler.redigerbar.VarselRevurderingAvPensjonDto.TittelValg (Tittelvalg)",
            ),
            Redigerbar.PE_VEDTAK_AVSLAG_PAA_OMSORGSOPPTJENING to listOf(
                "brukerFoedtFoer1948: Boolean (Hvis bruker er født før 1948)",
                "omsorgsarbeidEtter69Aar: Boolean (Omsorgsarbeid utført etter 69 år)",
                "omsorgsarbeidFoer1992: Boolean (Omsorgsarbeid utført for en syk, funksjonshemmet eller eldre person før 1992)",
                "omsorgsarbeidForBarnUnder7aarFoer1992: Boolean (Hvis det søkes om omsorgsopptjeningen for omsorg for barn under 7 år før 1992 uten at det er søkt om AFP privat)",
                "omsorgsarbeidMindreEnn22Timer: Boolean (Pleie- og omsorgsarbeid mindre enn 22 timer)",
                "omsorgsarbeidMindreEnn22TimerOgMindreEnn6Maaneder: Boolean (Pleie- og omsorgsarbeid mindre enn 22 timer  og mindre enn 6 måneder)",
                "omsorgsarbeidMindreEnn6Maaneder: Boolean (Pleie- og omsorgsarbeid mindre enn 6 måneder)",
                "omsorgsopptjeningenGodskrevetEktefellen: Boolean (Hvis omsorgsopptjening før 1992 allerede er godskrevet ektefellen)",
                "privatAFPavslaat: Boolean (Hvis søknad om AFP privat er avslått av Fellesordningen)",
            ),
            Redigerbar.PE_VEDTAK_OM_FJERNING_AV_OMSORGSOPPTJENING to listOf(
                "aktuelleAar: String (Aktuelle år)",
            ),
            Redigerbar.PE_VEDTAK_OM_INNVILGELSE_AV_OMSORGSPOENG to emptyList(),
            Redigerbar.UP_AVSLAG_UFOEREPENSJON to emptyList(),
            Redigerbar.UT_AVSLAG_UFOERETRYGD to emptyList(),
            Redigerbar.UT_BEKREFTELSE_PAA_UFOERETRYGD to emptyList(),
            Redigerbar.UT_DELVIS_EKSPORT_AV_UFORETRYGD to emptyList(),
            Redigerbar.UT_ENDRING_UFOERETRYGD to emptyList(),
            Redigerbar.UT_INFORMASJON_OM_SAKSBEHANDLINGSTID to listOf(
                "forlengetSaksbehandlingstid: Boolean (Forlenget saksbehandlingstid)",
            ),
            Redigerbar.UT_INNVILGELSE_UFOERETRYGD to listOf(
                "barnetilleggInfo: Boolean (Info om rett til barnetillegg)",
                "periodisertInntekt: no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.PeriodisertInntektBarnetillegg (Periodisert inntekt barnetillegg)",
            ),
            Redigerbar.UT_INNVILGELSE_UFOERETRYGD_MED_ENDRING to listOf(
                "periodisertInntekt: no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.PeriodisertInntektBarnetillegg (Periodisert inntekt barnetillegg)",
            ),
            Redigerbar.UT_INNVILGELSE_UFOERETRYGD_MELLOMBEHANDLING to listOf(
                "barnetilleggInfo: Boolean (Info om rett til barnetillegg)",
                "innvilgetEtter12_2Andreledd: Boolean (Innvilget etter 12-2 2.ledd)",
                "innvilgetEtter12_2Tredjeledd: Boolean (Innvilget etter 12-2 3.ledd)",
                "periodisertInntekt: no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.PeriodisertInntektBarnetillegg (Periodisert inntekt barnetillegg)",
            ),
            Redigerbar.UT_INNVILGELSE_UFOERETRYGD_NORGE_UTLAND to emptyList(),
            Redigerbar.UT_INNVILGELSE_UFOERETRYGD_UTLAND to listOf(
                "barnetilleggInfo: Boolean (Info om rett til barnetillegg)",
                "innvilgetEtter12_2Andreledd: Boolean (Innvilget etter 12-2 2.ledd)",
                "innvilgetEtter12_2Tredjeledd: Boolean (Innvilget etter 12-2 3.ledd)",
                "periodisertInntekt: no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.PeriodisertInntektBarnetillegg (Periodisert inntekt barnetillegg)",
                "refusjon: Boolean (Refusjon)",
            ),
            Redigerbar.UT_OKNING_UFOREGRAD to listOf(
                "periodisertInntekt: no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.PeriodisertInntektBarnetillegg (Periodisert inntekt barnetillegg)",
            ),
            Redigerbar.UT_OMREGNING_UFOEREPENSJON_TIL_UFOERETRYGD to emptyList(),
            Redigerbar.UT_ORIENTERING_OM_SAKSBEHANDLINGSTID to listOf(
                "soeknadOversendesTilUtlandet: Boolean (Søknad oversendes til utlandet)",
            ),
            Redigerbar.UT_VEDTAK_ETTERBETALING_OPPHOR_2026_RED to emptyList(),
            Redigerbar.UT_VEDTAK_MINSTE_IFU_2026_RED to emptyList(),
            Redigerbar.UT_VEDTAK_MINSTE_IFU_REDUKSJONSPROSENT_2026_RED to emptyList(),
            Redigerbar.UT_VEDTAK_OKT_BUNNFRADRAG_2026_RED to emptyList(),
            Redigerbar.UT_VEDTAK_OM_LAVERE_MINSTESATS_2026 to emptyList(),
            Redigerbar.UT_VEDTAK_REDUKSJONSPROSENT_2026_RED to emptyList()
        )

        assertEquals(forventet.size, faktiske.size)
        forventet.entries.forEach { (kode, forventetValg) ->
            val faktiskeValg = faktiske[kode]
            assertEquals(
                forventetValg,
                faktiskeValg,
                "Sjekk om saksbehandlervalg for mal ${kode} er endret: forventet=$forventetValg, faktisk=$faktiskeValg"
            )
        }
    }
}
