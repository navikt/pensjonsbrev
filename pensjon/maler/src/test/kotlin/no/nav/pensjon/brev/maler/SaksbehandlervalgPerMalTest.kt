package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder.Redigerbar
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brev.template.BrevbakerDSLInternal
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties

/**
 * Dokumenterer og verifiserer hvilke saksbehandlervalg (felter på SaksbehandlerValg, med datatype)
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
                mal.template.saksbehandlervalg.orEmpty().entries.map { (id, verdi) -> "$id: ${verdi.typename}" }
            } else {
                saksbehandlerValgType.memberProperties.map { "${it.name}: ${it.returnType}" }
            }

            (mal.kode as Redigerbar) to valg.sorted()
        }

        val forventet = mapOf(
            Redigerbar.BRUKERTEST_BREV_PENSJON_2025 to listOf(
                "denBesteKaken: no.nav.pensjon.brev.api.model.maler.redigerbar.BrukerTestBrevDto.DenBesteKaken",
                "kaffemaskinensTilgjengelighet: Boolean",
                "kontorplantenTorlill: Boolean",
                "utsiktenFraKontoret: no.nav.pensjon.brev.api.model.maler.redigerbar.BrukerTestBrevDto.UtsiktenFraKontoret",
            ),
            Redigerbar.GP_AVSLAG_GJENLEVENDEPENSJON to listOf(
                "folketrygdlovenParagraf: no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagGjenlevendepensjonDto.FolketrygdlovenParagraf",
            ),
            Redigerbar.GP_AVSLAG_GJENLEVENDEPENSJON_UTLAND to emptyList(),
            Redigerbar.GP_INNVILGELSE_BOSATT_NORGE_ETTER_UTLAND to emptyList(),
            Redigerbar.GP_OPPHOER_GJENLEVENDEPENSJON to listOf(
                "folketrygdlovenAlternativ: no.nav.pensjon.brev.api.model.maler.redigerbar.OpphoerGjenlevendepensjonDto.FolketrygdlovenAlternativ",
                "opphoerMedTilbakekreving: Boolean",
            ),
            Redigerbar.INFORMASJON_OM_SAKSBEHANDLINGSTID to listOf(
                "soeknadMottattFraUtland: Boolean",
                "venterPaaSvarAFP: Boolean",
            ),
            Redigerbar.P1_SAMLET_MELDING_OM_PENSJONSVEDTAK to emptyList(),
            Redigerbar.P1_SAMLET_MELDING_OM_PENSJONSVEDTAK_V2 to emptyList(),
            Redigerbar.PE_ANKE_TILSVAR_TIL_ANKENDE_PART to emptyList(),
            Redigerbar.PE_AP_AVSLAG_FOR_LITE_TRYGDETID to emptyList(),
            Redigerbar.PE_AP_AVSLAG_GJENLEVENDERETT to listOf(
                "avdoedNavn: kotlin.String",
                "ekteskapUnderFemAar: kotlin.Boolean",
                "harTrygdetid: kotlin.Boolean",
                "hjemmelAvtaleland: kotlin.Boolean",
                "hjemmelEOES: kotlin.Boolean",
                "samboerUtenFellesBarn: kotlin.Boolean",
                "under20AarBotid: kotlin.Boolean",
                "underEttAarsMedlemstidEOESEllerAvtaleland: kotlin.Boolean",
                "underTreFemAarsMedlemstidEOESSak: kotlin.Boolean",
                "underTreFemAarsMedlemstidNasjonalSak: kotlin.Boolean",
                "underTrefemAarsMedlemstidAvtalesak: kotlin.Boolean",
            ),
            Redigerbar.PE_AP_ENDRET_UTTAKSGRAD to listOf(
                "etterbetaling: kotlin.Boolean?",
            ),
            Redigerbar.PE_AP_ENDRET_UTTAKSGRAD_STANS_BRUKER_ELLER_VERGE to emptyList(),
            Redigerbar.PE_AP_ENDRET_UTTAKSGRAD_STANS_IKKE_BRUKER_VERGE to listOf(
                "aarsak: no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvUttaksgradStansIkkeBrukerEllerVergeDto.Aarsak",
            ),
            Redigerbar.PE_AP_ENDRING_FLYTTING_MELLOM_LAND to listOf(
                "aarsakTilAtPensjonenOeker: no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDto.AarsakTilAtPensjonenOeker",
                "endringIPensjonen: kotlin.Boolean",
                "etterbetaling: kotlin.Boolean",
                "innvandret: kotlin.Boolean",
                "reduksjonTilbakeITid: kotlin.Boolean",
            ),
            Redigerbar.PE_AP_ENDRING_GJENLEVENDERETT to listOf(
                "avdoedeHarRedusertTrygdetidEllerPoengaar: kotlin.Boolean?",
                "brukerUnder67OgAvdoedeHarRedusertTrygdetidEllerPoengaar: kotlin.Boolean?",
                "etterbetaling: kotlin.Boolean?",
                "ingenEndringBelop: kotlin.Boolean?",
                "okningBelop: kotlin.Boolean?",
                "oktTilleggMpn: kotlin.Boolean?",
                "omregnetTilEnsligISammeVedtak: kotlin.Boolean?",
            ),
            Redigerbar.PE_AP_ENDRING_INSTITUSJONSOPPHOLD to listOf(
                "alderspensjonRedusert: kotlin.Boolean",
                "alderspensjonStanset: kotlin.Boolean",
                "alderspensjonUnderOppholdIInstitusjon: kotlin.Boolean",
                "alderspensjonUnderSoning: kotlin.Boolean",
                "alderspensjonVedVaretektsfengsling: kotlin.Boolean",
                "etterbetaling: kotlin.Boolean?",
                "hvisReduksjonTilbakeITid: kotlin.Boolean",
                "informasjonOmSivilstandVedInstitusjonsopphold: kotlin.Boolean",
            ),
            Redigerbar.PE_AP_ENDRING_PGA_OPPTJENING to emptyList(),
            Redigerbar.PE_AP_INNHENTING_DOKUMENTASJON_FRA_BRUKER to emptyList(),
            Redigerbar.PE_AP_INNHENTING_INFORMASJON_FRA_BRUKER to listOf(
                "amerikanskSocialSecurityNumber: Boolean",
                "bankopplysninger: Boolean",
                "boOgArbeidsperioder: Boolean",
                "bosattIEoesLandSedErEoesBlanketter: Boolean",
                "eps60aarOgInntektUnder1g: Boolean",
                "eps62aarOgInntektUnder1gBoddArbeidUtland: Boolean",
                "epsInntektUnder2g: Boolean",
                "forsoergerEpsBosattIUtlandet: Boolean",
                "inntektsopplysninger: Boolean",
                "manglendeOpptjening: Boolean",
                "registreringAvSivilstand: Boolean",
                "tidspunktForUttak: Boolean",
            ),
            Redigerbar.PE_AP_INNHENTING_OPPLYSNINGER_FRA_BRUKER to emptyList(),
            Redigerbar.PE_AP_INNVILGELSE to listOf(
                "etterbetaling: kotlin.Boolean?",
                "kravVirkDatoFomSenereEnnOensketUttakstidspunkt: kotlin.Boolean?",
                "vanligSkattetrekk: kotlin.Boolean?",
            ),
            Redigerbar.PE_AP_INNVILGELSE_TRYGDEAVTALE to listOf(
                "etterbetaling: kotlin.Boolean?",
                "medfoererInnvilgelseAvAPellerOektUttaksgrad: kotlin.Boolean",
                "nyBeregningAvInnvilgetAP: kotlin.Boolean",
            ),
            Redigerbar.PE_BEKREFTELSE_PAA_FLYKTNINGSTATUS to emptyList(),
            Redigerbar.PE_BEKREFTELSE_PAA_PENSJON to emptyList(),
            Redigerbar.PE_FORESPOERSELOMDOKUMENTASJONAVBOTIDINORGE_ALDER to listOf(
                "opplystOmBotid: Boolean",
            ),
            Redigerbar.PE_FORESPOERSEL_DOKUM_BOTIDINORGE_ETTERLATTE to listOf(
                "opplystOmBotid: Boolean",
            ),
            Redigerbar.PE_INFORMASJON_OM_GJENLEVENDERETTIGHETER to listOf(
                "gjenlevendeHarBarnUnder18MedAvdoed: Boolean",
                "gjenlevenderHarEllerKanHaAFPIOffentligSektor: Boolean",
                "gjenlevevendeHarAfpOgUttaksgradPaaApSattTilNull: Boolean",
                "hvorBorBruker: no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmGjenlevenderettigheterDto.HvorBorBruker",
                "infoHvordanSoekeOmstillingsstoenad: Boolean",
                "infoOmstillingsstoenad: Boolean",
                "infoVilkaarSkiltGjenlevende: Boolean",
                "vilkarForGjenlevendeytelsen: no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmGjenlevenderettigheterDto.VilkarForGjenlevendeytelsen",
            ),
            Redigerbar.PE_KLAGE_ORIENTERING_OM_OVERSENDELSE_KLAGEINSTANS to emptyList(),
            Redigerbar.PE_KLAGE_ORIENTERING_OM_SAKSBEHANDLINGSTID to emptyList(),
            Redigerbar.PE_OMSORG_EGEN_MANUELL to listOf(
                "aarEgenerklaringOmsorgspoeng: no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year",
                "aarInnvilgetOmsorgspoeng: no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year",
            ),
            Redigerbar.PE_ORIENTERING_OM_FORLENGET_SAKSBEHANDLINGSTID to emptyList(),
            Redigerbar.PE_OVERSETTELSE_AV_DOKUMENTER to emptyList(),
            Redigerbar.PE_TILBAKEKREVING_AV_FEILUTBETALT_BELOEP to emptyList(),
            Redigerbar.PE_VARSEL_OM_MULIG_AVSLAG to emptyList(),
            Redigerbar.PE_VARSEL_OM_TILBAKEKREVING_FEILUTBETALT_BELOEP to listOf(
                "hvisAktueltAaIleggeRentetillegg: Boolean",
            ),
            Redigerbar.PE_VARSEL_REVURDERING_AV_PENSJON to listOf(
                "tittelValg: no.nav.pensjon.brev.api.model.maler.redigerbar.VarselRevurderingAvPensjonDto.TittelValg",
            ),
            Redigerbar.PE_VEDTAK_AVSLAG_PAA_OMSORGSOPPTJENING to listOf(
                "brukerFoedtFoer1948: Boolean",
                "omsorgsarbeidEtter69Aar: Boolean",
                "omsorgsarbeidFoer1992: Boolean",
                "omsorgsarbeidForBarnUnder7aarFoer1992: Boolean",
                "omsorgsarbeidMindreEnn22Timer: Boolean",
                "omsorgsarbeidMindreEnn22TimerOgMindreEnn6Maaneder: Boolean",
                "omsorgsarbeidMindreEnn6Maaneder: Boolean",
                "omsorgsopptjeningenGodskrevetEktefellen: Boolean",
                "privatAFPavslaat: Boolean",
            ),
            Redigerbar.PE_VEDTAK_OM_FJERNING_AV_OMSORGSOPPTJENING to listOf(
                "aktuelleAar: String",
            ),
            Redigerbar.PE_VEDTAK_OM_INNVILGELSE_AV_OMSORGSPOENG to emptyList(),
            Redigerbar.UP_AVSLAG_UFOEREPENSJON to emptyList(),
            Redigerbar.UT_AVSLAG_UFOERETRYGD to emptyList(),
            Redigerbar.UT_BEKREFTELSE_PAA_UFOERETRYGD to emptyList(),
            Redigerbar.UT_DELVIS_EKSPORT_AV_UFORETRYGD to emptyList(),
            Redigerbar.UT_ENDRING_UFOERETRYGD to emptyList(),
            Redigerbar.UT_INFORMASJON_OM_SAKSBEHANDLINGSTID to listOf(
                "forlengetSaksbehandlingstid: Boolean",
            ),
            Redigerbar.UT_INNVILGELSE_UFOERETRYGD to listOf(
                "barnetilleggInfo: kotlin.Boolean",
                "periodisertInntekt: no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.PeriodisertInntektBarnetillegg?",
            ),
            Redigerbar.UT_INNVILGELSE_UFOERETRYGD_MED_ENDRING to listOf(
                "periodisertInntekt: no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.PeriodisertInntektBarnetillegg?",
            ),
            Redigerbar.UT_INNVILGELSE_UFOERETRYGD_MELLOMBEHANDLING to listOf(
                "barnetilleggInfo: kotlin.Boolean",
                "innvilgetEtter12_2Andreledd: kotlin.Boolean",
                "innvilgetEtter12_2Tredjeledd: kotlin.Boolean",
                "periodisertInntekt: no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.PeriodisertInntektBarnetillegg?",
                "refusjon: kotlin.Boolean",
            ),
            Redigerbar.UT_INNVILGELSE_UFOERETRYGD_NORGE_UTLAND to emptyList(),
            Redigerbar.UT_INNVILGELSE_UFOERETRYGD_UTLAND to listOf(
                "barnetilleggInfo: kotlin.Boolean",
                "innvilgetEtter12_2Andreledd: kotlin.Boolean",
                "innvilgetEtter12_2Tredjeledd: kotlin.Boolean",
                "periodisertInntekt: no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.PeriodisertInntektBarnetillegg?",
                "refusjon: kotlin.Boolean",
            ),
            Redigerbar.UT_OKNING_UFOREGRAD to listOf(
                "periodisertInntekt: no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.PeriodisertInntektBarnetillegg?",
            ),
            Redigerbar.UT_OMREGNING_UFOEREPENSJON_TIL_UFOERETRYGD to emptyList(),
            Redigerbar.UT_ORIENTERING_OM_SAKSBEHANDLINGSTID to listOf(
                "soeknadOversendesTilUtlandet: Boolean",
            ),
            Redigerbar.UT_VEDTAK_ETTERBETALING_OPPHOR_2026_RED to emptyList(),
            Redigerbar.UT_VEDTAK_MINSTE_IFU_2026_RED to emptyList(),
            Redigerbar.UT_VEDTAK_MINSTE_IFU_REDUKSJONSPROSENT_2026_RED to emptyList(),
            Redigerbar.UT_VEDTAK_OKT_BUNNFRADRAG_2026_RED to emptyList(),
            Redigerbar.UT_VEDTAK_OM_LAVERE_MINSTESATS_2026 to emptyList(),
            Redigerbar.UT_VEDTAK_REDUKSJONSPROSENT_2026_RED to emptyList()
        )

        assertEquals(
            forventet.keys,
            faktiske.keys,
        ) { "Sjekk om det er nye eller fjernede redigerbare maler i pensjon-modulen: forventet=${forventet.keys}, faktisk=${faktiske.keys}" }
        assertEquals(forventet, faktiske)
    }
}
