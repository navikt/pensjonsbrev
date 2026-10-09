package no.nav.pensjon.brev.maler.ufore.innvilgelse

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.felles.selectors.uforeVedtaksinfo.virkningsdatoTidligereMnd
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUforetrygdMedEndringDto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.PeriodisertInntektBarnetillegg
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.selectors.innvilgelseUforetrygdMedEndringDto.pesysData.*
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.selectors.innvilgelseUforetrygdMedEndringDto.*
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_URL
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.ufore.fraser.Ufoeretrygd
import no.nav.pensjon.brev.maler.ufore.fraser.Ufoeretrygd.InntektBarnetillegg
import no.nav.pensjon.brev.maler.legacy.*
import no.nav.pensjon.brev.maler.legacy.fraser.*
import no.nav.pensjon.brev.maler.legacy.vedlegg.vedleggOpplysningerBruktIBeregningUTLegacy
import no.nav.pensjon.brev.maler.ufore.vedlegg.vedleggDineRettigheterOgPlikterUfoere
import no.nav.pensjon.brev.maler.ufore.vedlegg.vedleggDineRettigheterOgPlikterUfore
import no.nav.pensjon.brev.maler.ufore.vedlegg.vedleggMaanedligUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.model.Brevkategori
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.TemplateRootScope.RedigerbartVedlegg
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.saksbehandlervalg
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import java.time.LocalDate
import no.nav.pensjon.brev.template.dsl.expression.localDateNow
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.VedleggId

@TemplateModelHelpers
object InnvilgelseUforetrygdMedEndringRedigerbar : RedigerbarTemplate<InnvilgelseUforetrygdMedEndringDto> {

    override val featureToggle = FeatureToggles.brevmalUtInnvilgelseMedEndring.toggle

    override val kode = Pesysbrevkoder.Redigerbar.UT_INNVILGELSE_UFOERETRYGD_MED_ENDRING
    override val kategori = Brevkategori.SLUTTBEHANDLING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.UFOREP)

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - innvilgelse av uføretrygd med endring (bosatt Norge - etter utland)",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        val periodisertInntekt = saksbehandlervalg("periodisertInntekt", "Periodisert inntekt barnetillegg").enum<PeriodisertInntektBarnetillegg>()

        title {
            text(
                bokmal { +"Nav har gjort en endelig beregning av uføretrygden din" },
                nynorsk { +"Nav har gjort ein endeleg berekning av uføretrygda di" },
            )
        }
        outline {
            val pe = pesysData.pe

            val uforetidspunkt = pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforetidspunkt().ifNull(localDateNow)
            val trygdetidfombilateral = pe.grunnlag_persongrunnlagsliste_trygdetidsgrunnlaglistebilateral_trygdetidsgrunnlagbilateral_trygdetidfombilateral().ifNull(localDateNow)
            val trygdetidtombilateral = pe.grunnlag_persongrunnlagsliste_trygdetidsgrunnlaglistebilateral_trygdetidsgrunnlagbilateral_trygdetidtombilateral().ifNull(localDateNow)
            val trygdetidfom = pe.grunnlag_persongrunnlagsliste_trygdetidsgrunnlaglistenor_trygdetidsgrunnlag_trygdetidfom().ifNull(localDateNow)
            val trygdetidtom = pe.grunnlag_persongrunnlagsliste_trygdetidsgrunnlaglistenor_trygdetidsgrunnlag_trygdetidtom().ifNull(localDateNow)
            val trygdetideosland = pe.vedtaksbrev_grunnlag_persongrunnlagsliste_trygdetidsgrunnlageos_trygdetidsgrunnlageos_trygdetideosland().notEqualTo("")

            val uforegrad = pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()
            val barnetilleggSerkullInnvilget = pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()
            val barnetilleggFellesInnvilget = pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()
            val ektefelletilleggInnvilget = pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget()
            val gjenlevendetilleggInnvilget = pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()
            val instoppholdType = pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype()

            val txtOgEllerEktefelle = if (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget().equals(true)) " og/eller ektefelle" else ""

            showIf((pe.vedtaksdata_beregningsdata_beregningufore_belopsendring_uforetrygdordineryk_belopgammelut().greaterThan(0))) {
                paragraph {
                    text(
                        bokmal { +"Vi viser til vedtak fra " + fritekst("vedtaksdato") + " der du fikk innvilget uføretrygd med en foreløpig beregning. Ut fra opplysninger vi har fått fra " + fritekst("land") + ", har vi nå gjort en endelig beregning av uføretrygden din. Den endelige beregningen har ført til endringer i uføretrygden din fra " + pe.vedtaksdata_virkningfom().format() + "." },
                        nynorsk { +"Vi viser til vedtak fra " + fritekst("vedtaksdato") + " der du fikk innvilget uføretrygd med en foreløpig beregning. Ut fra opplysninger vi har fått fra " + fritekst("land") + ", har vi nå gjort en endelig beregning av uføretrygden din. Den endelige beregningen har ført til endringer i uføretrygden din fra " + pe.vedtaksdata_virkningfom().format() + "." },
                    )
                }
            }

            showIf((pesysData.nyeInnvilgedeBarnetillegg.isNotEmpty())) {
                paragraph {

                    text(
                        bokmal { +"Du er innvilget barnetillegg i uføretrygden din for" },
                        nynorsk { +"Du er innvilga barnetillegg i uføretrygda di for" },
                    )
                    includePhrase(Felles.TextOrList(pesysData.nyeInnvilgedeBarnetillegg.map(BarnetilleggFormatter), 0))

                    showIf((barnetilleggFellesInnvilget and (((barnetilleggSerkullInnvilget and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().equalTo(0)) and (barnetilleggFellesInnvilget and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().equalTo(0))) or (barnetilleggSerkullInnvilget and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().equalTo(0) and not(barnetilleggFellesInnvilget)) or (barnetilleggFellesInnvilget and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().equalTo(0) and not(barnetilleggSerkullInnvilget))))) {
                        text(
                            bokmal { +" Tillegget blir ikke utbetalt fordi inntekten til deg og din ektefelle er over grensen for å få utbetalt barnetillegg." },
                            nynorsk { +" Tillegget blir ikkje utbetalt fordi inntekta di og inntekta til ektefellen din er over grensa for å få utbetalt barnetillegg." },
                        )
                    }

                    showIf(((barnetilleggSerkullInnvilget and not(barnetilleggFellesInnvilget)) and (((barnetilleggSerkullInnvilget and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().equalTo(0)) and (barnetilleggFellesInnvilget and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().equalTo(0))) or (barnetilleggSerkullInnvilget and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().equalTo(0) and not(barnetilleggFellesInnvilget)) or (barnetilleggFellesInnvilget and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().equalTo(0) and not(barnetilleggSerkullInnvilget))))) {
                        text(
                            bokmal { +" Tillegget blir ikke utbetalt fordi inntekten din er over grensen for å få utbetalt barnetillegg." },
                            nynorsk { +" Tillegget blir ikkje betalt ut fordi inntekta di er over grensa for å få utbetalt barnetillegg." },
                        )
                    }
                }
            }

            showIf(pesysData.nyeAvslagBarnetillegg.isNotEmpty()) {
                paragraph {
                    text(
                        bokmal { +"Vi har avslått barnetillegg i uføretrygden din for" },
                        nynorsk { +"Vi har avslått barnetillegg i uføretrygda di for" },
                    )
                    includePhrase(Felles.TextOrList(pesysData.nyeAvslagBarnetillegg.map(BarnetilleggFlereBarnFormatter), 0))
                }
                paragraph {
                    text(
                        bokmal { +"Se begrunnelse under avsnittet Derfor har vi avslått søknaden din om barnetillegg" },
                        nynorsk { +"Sjå grunngiving under avsnittet Derfor har vi avslått søknaden din om barnetillegg" },
                    )
                }
            }

            showIf(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) {
                paragraph {
                    text(
                        bokmal { +"Du er innvilget gjenlevendetillegg i uføretrygden din." },
                        nynorsk { +"Du er innvilga attlevandetillegg i uføretrygda di." },
                    )
                }
            }

            showIf((not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget()) and not(barnetilleggSerkullInnvilget) and not(barnetilleggFellesInnvilget) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()))) {
                paragraph {
                    text(
                        bokmal { +"Du får " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " i uføretrygd per måned før skatt." },
                        nynorsk { +"Du får " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " i uføretrygd per månad før skatt." },
                    )
                }
            }

            showIf(((barnetilleggSerkullInnvilget or barnetilleggFellesInnvilget) and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget()) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()))) {
                paragraph {
                    text(
                        bokmal { +"Du får " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " i uføretrygd og barnetillegg per måned før skatt." },
                        nynorsk { +"Du får " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " i uføretrygd og barnetillegg per månad før skatt." },
                    )
                }
            }

            showIf((not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget()) and not(barnetilleggSerkullInnvilget) and not(barnetilleggFellesInnvilget) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()))) {
                paragraph {
                    text(
                        bokmal { +"Du får " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " i uføretrygd og gjenlevendetillegg per måned før skatt." },
                        nynorsk { +"Du får " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " i uføretrygd og attlevandetillegg per månad før skatt." },
                    )
                }
            }

            showIf(((barnetilleggSerkullInnvilget or barnetilleggFellesInnvilget) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget()) and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()))) {
                paragraph {
                    text(
                        bokmal { +"Du får " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " i uføretrygd, barne- og gjenlevendetillegg per måned før skatt." },
                        nynorsk { +"Du får " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " i uføretrygd, barne- og attlevandetillegg per månad før skatt." },
                    )
                }
            }

            showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_fo") and pe.ut_forsorgeransvar_ingen_er_true() and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt())) {
                paragraph {
                    text(
                        bokmal { +"Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningsresultattilrevurderingtotalnetto().format() + " i uføretrygd, men den kommer ikke til utbetaling fordi du er under straffegjennomføring." },
                        nynorsk { +"Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningsresultattilrevurderingtotalnetto().format() + " i uføretrygd, men ho blir ikkje betalt ut fordi du er under straffegjennomføring." },
                    )
                }
            }

            showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt())) {
                paragraph {
                    text(
                        bokmal { +"Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningsresultattilrevurderingtotalnetto().format() + " i uføretrygd, men utbetalingen er redusert fordi du er innlagt på institusjon. I denne perioden vil du få utbetalt " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + "." },
                        nynorsk { +"Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningsresultattilrevurderingtotalnetto().format() + " i uføretrygd, men utbetalinga er redusert fordi du er innlagd på institusjon. I denne perioden får du betalt ut " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + "." },
                    )
                }
            }

            showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_fo") and pe.ut_forsorgeransvar_ingen_er_false() and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt())) {
                paragraph {
                    text(
                        bokmal { +"Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningsresultattilrevurderingtotalnetto().format() + " i uføretrygd, men utbetalingen er redusert fordi du er under straffegjennomføring. I denne perioden vil du få utbetalt " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + "." },
                        nynorsk { +"Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningsresultattilrevurderingtotalnetto().format() + " i uføretrygd, men utbetalinga er redusert fordi du er under straffegjennomføring. I denne perioden får du betalt ut " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + "." },
                    )
                }
            }

            showIf(((pe.grunnlag_persongrunnlagsliste_personbostedsland()).equalTo("nor") or (pe.grunnlag_persongrunnlagsliste_personbostedsland()).equalTo(""))) {
                paragraph {
                    text(
                        bokmal { +"Uføretrygden blir utbetalt senest den 20. hver måned. Du får din første utbetaling med nytt beløp i " + fritekst("måned og år") + "." },
                        nynorsk { +"Uføretrygda blir utbetalt seinast den 20. i kvar månad. Du får den første utbetalinga di med nytt beløp i " + fritekst("måned og år") + "." },
                    )
                }
            }

            title1 {
                text(
                    bokmal { +"Begrunnelse for vedtaket" },
                    nynorsk { +"Grunngiving for vedtaket" },
                )
            }

            showIf((pe.vedtaksdata_beregningsdata_beregningufore_belopsendring_uforetrygdordineryk_belopgammelut().equalTo(0) and pe.vedtaksdata_kravhode_kravgjelder().equalTo("slutt_bh_utl"))) {
                paragraph {
                    text(
                        bokmal { +"Vi kan på grunnlag av bestemmelsene i " + fritekst("Trygdeavtale") + " gi unntak fra folketrygdlovens vilkår om medlemskap fram til uføretidspunktet, ved å legge sammen perioder med medlemskap i folketrygden og medlemskap i et annet land Norge har trygdeavtale med." },
                        nynorsk { +"Vi kan på grunnlag av reglane i " + fritekst("Trygdeavtale") + " gi unntak frå vilkåra i folketrygdlova om medlemskap fram til uføretidspunktet, ved å leggje saman periodar med medlemstid i Noreg og medlemstid i eit anna land Noreg har trygdeavtale med." },
                    )
                }
            }

            showIf((pe.vedtaksdata_beregningsdata_beregningufore_belopsendring_uforetrygdordineryk_belopgammelut().equalTo(0) and pe.vedtaksdata_kravhode_kravgjelder().equalTo("slutt_bh_utl"))) {
                paragraph {
                    text(
                        bokmal { +"Du har vært medlem av folketrygden fra " + trygdetidfom.format() + " til " + trygdetidtom.format() + ". Vi har fått opplyst at du har vært medlem av den " + fritekst("nasjonalitet") + " trygdeordningen fra " + trygdetidfombilateral.format() + " til " + trygdetidtombilateral.format() + ". Uføretidspunktet ditt er satt til " + uforetidspunkt.formatMonthYear() + ". Du har derfor vært medlem av folketrygden og den " + fritekst("nasjonalitet") + " trygdeordningen sammenhengende i tre år eller mer fram til uføretidspunktet ditt. Fordi vi har lagt sammen perioder med medlemskap i folketrygden og i " + fritekst("land") + ", får du unntak fra vilkåret om medlemskap i folketrygden." },
                        nynorsk { +"Du har vore medlem av den norske folketrygda frå " + trygdetidfom.format() + " til " + trygdetidtom.format() + ". Vi har fått opplyst at du har vore medlem av den " + fritekst("Nasjonalitet") + " trygdeordninga frå " + trygdetidfombilateral.format() + " til " + trygdetidtombilateral.format() + ". Uføretidspunktet ditt er sett til " + uforetidspunkt.formatMonthYear() + ". Du har derfor vore medlem av den norske folketrygda og den " + fritekst("Nasjonalitet") + " trygdeordninga samanhengande i tre år eller meir fram til uføretidspunktet ditt. Fordi vi har lagt saman periodar med medlemstid i Noreg og i " + fritekst("Land") + ", får du unntak frå vilkåret om medlemskap i folketrygda." },
                    )
                }
            }

            showIf((pe.vedtaksbrev_vedtaksdata_vilkarsvedtak_vilkar_medlemskapforutettertrygdeavtaler_oppfyltvedsammenlegging() and pe.vedtaksdata_beregningsdata_beregningufore_belopsendring_uforetrygdordineryk_belopgammelut().equalTo(0) and pe.vedtaksdata_kravhode_kravgjelder().equalTo("slutt_bh_utl"))) {
                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter EØS-forordning 883/2004 artikkel 6 og folketrygdloven § 12-2." },
                        nynorsk { +"Vedtaket er gjort etter EØS-forordning 883/2004 artikkel 6 og folketrygdloven § 12-2." },
                    )
                }
            }

            showIf((trygdetideosland) and not(pe.vedtaksbrev_vedtaksdata_vilkarsvedtak_vilkar_medlemskapforutettertrygdeavtaler_oppfyltvedsammenlegging())) {
                paragraph {
                    text(
                        bokmal { +"Uføretrygden din har vært foreløpig beregnet etter folketrygdlovens regler. Etter EØS- avtalens regler har du rett til en alternativ beregning. " },
                        nynorsk { +"Uføretrygden din har vært foreløpig beregnet etter folketrygdlovens regler. Etter EØS- avtalens regler har du rett til en alternativ beregning. " },
                    )
                    text(
                        bokmal { +"Vi har sammenlignet hvor mye du får i uføretrygd beregnet både etter folketrygdlovens regler og EØS-avtalens regler. Du får den beregningen som er mest gunstig for deg. Uføretrygden din er derfor beregnet etter reglene i " },
                        nynorsk { +"Vi har sammenlignet hvor mye du får i uføretrygd beregnet både etter folketrygdlovens regler og EØS-avtalens regler. Du får den beregningen som er mest gunstig for deg. Uføretrygden din er derfor beregnet etter reglene i " },
                    )

                    showIf(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().equalTo("folketrygd")) {
                        text(
                            bokmal { +"folketrygden" },
                            nynorsk { +"folketrygden" },
                        )
                    }

                    showIf(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().equalTo("eos")) {
                        text(
                            bokmal { +"EØS- avtalen" },
                            nynorsk { +"EØS- avtalen" },
                        )
                    }
                    text(
                        bokmal { +". Du kan lese mer om hvordan uføretrygden din er beregnet i «Opplysninger om beregningen»." },
                        nynorsk { +". Du kan lese mer om hvordan uføretrygden din er beregnet i «Opplysninger om beregningen»." },
                    )
                }
            }

            showIf((pe.vedtaksbrev_vedtaksdata_vilkarsvedtak_vilkar_medlemskapforutettertrygdeavtaler_oppfyltvedsammenlegging() and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().equalTo("eos"))) {
                paragraph {
                    text(
                        bokmal { +"Uføretrygden din er beregnet etter EØS-avtalens regler. Du kan lese mer om hvordan uføretrygden din er beregnet i «Opplysninger om beregningen»." },
                        nynorsk { +"Uføretrygden din er beregnet etter EØS-avtalens regler. Du kan lese mer om hvordan uføretrygden din er beregnet i «Opplysninger om beregningen»." },
                    )
                }
            }

            showIf(not(trygdetideosland)) {
                paragraph {
                    text(
                        bokmal { +"Uføretrygden din er beregnet etter folketrygdlovens regler. Fordi du ikke har opparbeidet deg trygdetid i et annet EØS-land, har du ikke rett på en alternativ beregning etter EØS-avtalens regler. Du kan lese mer om hvordan uføretrygden din er beregnet i «Opplysninger om beregningen»." },
                        nynorsk { +"Uføretrygden din er beregnet etter folketrygdlovens regler. Fordi du ikke har opparbeidet deg trygdetid i et annet EØS-land, har du ikke rett på en alternativ beregning etter EØS-avtalens regler. Du kan lese mer om hvordan uføretrygden din er beregnet i «Opplysninger om beregningen»." },
                    )
                }
            }

            showIf((pe.vedtaksdata_beregningsdata_beregningufore_belopsendring_uforetrygdordineryk_belopgammelut().notEqualTo(0))) {
                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter EØS-forordning 883/2004 artikkel 52." },
                        nynorsk { +"Vedtaket er gjort etter EØS-forordning 883/2004 artikkel 52." },
                    )
                }
            }

            title1 {
                text(
                    bokmal { +"Vedtak fra utenlandske trygdemyndigheter" },
                    nynorsk { +"Vedtak fra utenlandske trygdemyndigheter" },
                )
            }

            showIf(trygdetideosland) {
                paragraph {
                    text(
                        bokmal { +"Vi har mottatt melding fra " + fritekst("land") + " om at de har innvilget søknaden din om uføretrygd. De har fattet vedtaket etter egne nasjonale regler, og du vil få utbetalt trygd direkte fra dem. Du kan lese mer om dette i det utenlandske vedtaket." },
                        nynorsk { +"Vi har mottatt melding fra " + fritekst("land") + " om at de har innvilget søknaden din om uføretrygd. De har fattet vedtaket etter egne nasjonale regler, og du vil få utbetalt trygd direkte fra dem. Du kan lese mer om dette i det utenlandske vedtaket." },
                    )
                }
            }

            showIf(not(trygdetideosland)) {
                paragraph {
                    text(
                        bokmal { +"Vi har mottatt melding fra " + fritekst("land") + " om at de har avslått søknaden din om uføretrygd. De har fattet vedtaket etter egne nasjonale regler. Du kan lese mer om dette i det utenlandske vedtaket." },
                        nynorsk { +"Vi har mottatt melding fra " + fritekst("land") + " om at de har avslått søknaden din om uføretrygd. De har fattet vedtaket etter egne nasjonale regler. Du kan lese mer om dette i det utenlandske vedtaket." },
                    )
                }
            }
            paragraph {
                text(
                    bokmal { +"Du finner samlet vedtak fra Norge og " + fritekst("land") + " i vedlegget «P1- Samlet melding om pensjonsvedtak». Vi har også lagt ved kopi av vedtak fra " + fritekst("utenlandsk trygdemyndighet") + "." },
                    nynorsk { +"Du finner samlet vedtak fra Norge og " + fritekst("land") + " i vedlegget «P1- Samlet melding om pensjonsvedtak». Vi har også lagt ved kopi av vedtak fra " + fritekst("utenlandsk trygdemyndighet") + "." },
                )
            }

            showIf(uforegrad.equalTo(100)) {
                includePhrase(TBU1201_Generated)
            }

            showIf((uforegrad.lessThan(100) and uforegrad.greaterThan(0))) {
                includePhrase(TBU1203_Generated)
            }
            includePhrase(TBU1204_Generated)

            showIf((pesysData.harVTA)) {
                includePhrase(TBU1206_Generated(pe))
            }.orShowIf((uforegrad.equalTo(100) and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieuinntekt().equalTo(0))) {
                includePhrase(TBU1205_Generated(pe))
            }

            showIf((((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforegrad()).lessThan(100) and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforegrad()).greaterThan(0)) or ((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieuinntekt()).greaterThan(0) and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforegrad()).equalTo(100)))) {
                includePhrase(TBU1207_Generated(pe))
            }

            includePhrase(TBU1208_Generated(pe))
            includePhrase(TBU1210_Generated(pe))

            paragraph {
                text(
                    bokmal { +"Reglene for bunnfradraget og hvordan inntekt reduserer uføretrygd, gjelder bare for den norske uføretrygden din. Har du spørsmål om bunnfradraget i et annet land, må du kontakte trygdemyndighetene i det landet det gjelder." },
                    nynorsk { +"Reglane for botnfrådraget og korleis inntekt reduserer uføretrygd, gjeld berre for den norske uføretrygda di. Har du spørsmål om botnfrådraget i eit anna land, må du kontakte trygdemyndigheitene i det landet det gjeld." },
                )
            }

            showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs")) {
                title1 {
                    text(
                        bokmal { +"Utbetaling av uføretrygd for deg som er innlagt på institusjon" },
                        nynorsk { +"Utbetaling av uføretrygd når du er innlagd på institusjon" },
                    )
                }
            }

            showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs") and (pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt() or pe.ut_forsorgeransvar_ingen_er_false() or pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter().notEqualTo(Kroner(0))))) {
                paragraph {
                    text(
                        bokmal { +"Vi reduserer ikke utbetalingen av uføretrygden din verken i innleggelsesmåneden eller de tre påfølgende månedene når du er innlagt på institusjon. Uføretrygden din blir deretter redusert og skal under oppholdet utgjøre 14 prosent av uføretrygden inntil institusjonsoppholdet avsluttes. Totalt sett skal utbetalingen likevel utgjøre minst 45 prosent av folketrygdens grunnbeløp, før skatt." },
                        nynorsk { +"Vi reduserer ikkje utbetalinga av uføretrygda di verken i innleggingsmånaden eller dei tre påfølgjande månadene når du er innlagd på institusjon. Uføretrygda di blir deretter redusert og skal under opphaldet utgjere 14 prosent av uføretrygda inntil institusjonsopphaldet blir avslutta. Totalt sett skal utbetalinga likevel utgjere minst 45 prosent av grunnbeløpet i folketrygda før skatt." },
                    )

                    showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget() or pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget())) {
                        text(
                            bokmal { +" Dersom du mottar " },
                            nynorsk { +" Dersom du får " },
                        )
                    }

                    showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget()) {
                        text(
                            bokmal { +"ektefelletillegg" },
                            nynorsk { +"ektefelletillegg" },
                        )
                    }

                    showIf(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) {
                        text(
                            bokmal { +"gjenlevendetillegg" },
                            nynorsk { +"attlevandetillegg" },
                        )
                    }

                    showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget() or pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget())) {
                        text(
                            bokmal { +" vil dette tillegget også bli redusert." },
                            nynorsk { +" vil dette tillegget også bli redusert." },
                        )
                    }
                }
            }

            showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs") and (pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt() or pe.ut_forsorgeransvar_ingen_er_false() or pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter().notEqualTo(Kroner(0))))) {
                paragraph {
                    text(
                        bokmal { +"Dersom du har faste og nødvendige utgifter til bolig, kan vi vurdere om uføretrygden din kan reduseres mindre. Du må sende inn dokumentasjon på dine utgifter til Nav. Forsørger du barn" + txtOgEllerEktefelle + " under innleggelsen på institusjonen, vil vi ikke redusere uføretrygden din." },
                        nynorsk { +"Dersom du har faste og nødvendige utgifter til bustad, vil vi vurdere en lågare reduksjon av uføretrygda di. Du må sende inn dokumentasjon på utgiftene dine til Nav. Viss du forsørgjer barn" + txtOgEllerEktefelle + " mens du er lagd inn på institusjonen, reduserer vi ikkje uføretrygda di." },
                    )
                }
            }

            showIf((not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()) and pe.ut_forsorgeransvar_ingen_er_true() and pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter().equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs"))) {
                paragraph {
                    text(
                        bokmal { +"Uføretrygden din er lavere enn 45 prosent av folketrygdens grunnbeløp. Du vil derfor ikke få redusert utbetaling av uføretrygden din når du er innlagt på institusjon." },
                        nynorsk { +"Uføretrygda di er lågare enn 45 prosent av grunnbeløpet i folketrygda. Du får derfor ikkje redusert utbetaling av uføretrygda di når du er innlagd på institusjon." },
                    )
                }
            }

            showIf((pe.ut_forsorgeransvar_ingen_er_false() and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs") and pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter().equalTo(0))) {
                paragraph {
                    text(
                        bokmal { +"Du forsørger barn" + txtOgEllerEktefelle + " under oppholdet ditt i institusjon. Vi har derfor kommet fram til at utbetalingen din ikke skal reduseres." },
                        nynorsk { +"Du forsørgjer barn" + txtOgEllerEktefelle + " under opphaldet ditt på institusjon. Vi har derfor kome fram til at utbetalinga di derfor ikkje skal reduserast." },
                    )
                }
            }

            showIf((not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()) and pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter().notEqualTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs") and pe.ut_forsorgeransvar_ingen_er_true())) {
                paragraph {
                    text(
                        bokmal { +"Du har dokumentert at du har faste og nødvendige utgifter til bolig under oppholdet ditt på institusjon. Vi har derfor kommet fram til at utbetalingen din ikke skal reduseres." },
                        nynorsk { +"Du har dokumentert at du har faste og nødvendige utgifter til bustad under opphaldet ditt på institusjon. Vi har kome fram til at utbetalinga di derfor ikkje skal reduserast." },
                    )
                }
            }

            showIf((pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter().notEqualTo(0) and pe.ut_forsorgeransvar_ingen_er_false() and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs"))) {
                paragraph {
                    text(
                        bokmal { +"Du har dokumentert at du har faste og nødvendige utgifter til bolig, og du forsørger barn" + txtOgEllerEktefelle + " under oppholdet ditt i institusjon. Vi har derfor kommet fram til at utbetalingen din ikke skal reduseres." },
                        nynorsk { +"Du har dokumentert at du har faste og nødvendige utgifter til bustad, og du forsørgjer barn" + txtOgEllerEktefelle + " under opphaldet ditt på institusjon. Vi har derfor kome fram til at utbetalinga di ikkje skal reduserast." },
                    )
                }
            }

            showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt() and pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter().notEqualTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs"))) {
                paragraph {
                    text(
                        bokmal { +"Du har dokumentert at du har faste og nødvendige utgifter til bolig under oppholdet ditt på institusjon. Vi har derfor kommet fram til at utbetalingen din skal reduseres til " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + "." },
                        nynorsk { +"Du har dokumentert at du har faste og nødvendige utgifter til bustad under opphaldet ditt på institusjon. Vi har kome fram til at utbetalinga di skal reduserast til " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + "." },
                    )
                }
            }

            showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt() and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs") and pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter().equalTo(0) and pe.ut_forsorgeransvar_ingen_er_true())) {
                paragraph {
                    text(
                        bokmal { +"Du forsørger ikke barn" + txtOgEllerEktefelle + ", og det er ikke dokumentert at du har faste og nødvendige utgifter til bolig under oppholdet ditt på institusjon. Vi har derfor kommet fram til at uføretrygden din skal reduseres til " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + "." },
                        nynorsk { +"Du forsørgjer ikkje barn" + txtOgEllerEktefelle + ", og det er ikkje dokumentert at du har faste og nødvendige utgifter til bustad under opphaldet ditt på institusjon. Vi har derfor kome fram til at uføretrygda di skal reduserast til " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + "." },
                    )
                }
            }

            showIf(instoppholdType.equalTo("reduksjon_fo")) {
                showIf(pe.ut_forsorgeransvar_ingen_er_false()) {
                    includePhrase(Ufoeretrygd.Straffegjennomfoering(pe, ektefelletilleggInnvilget, gjenlevendetilleggInnvilget))
                }

                showIf(pe.ut_forsorgeransvar_ingen_er_true()) {
                    title1(uniqueness = "straff_innv_endring") {
                        text(
                            bokmal { +"Utbetaling av uføretrygd for deg som er under straffegjennomføring" },
                            nynorsk { +"Utbetaling av uføretrygd når du er under straffegjennomføring" },
                        )
                    }
                    paragraph {
                        text(
                            bokmal { +"Du er under straffegjennomføring og uføretrygden din kommer derfor ikke til utbetaling. Vi vil sette i gang utbetalingene igjen når straffegjennomføringen er avsluttet." },
                            nynorsk { +"Du er under straffegjennomføring og uføretrygda di kommer derfor ikkje til utbetaling. Vi vil sette i gang utbetalingane igjen når straffegjennomføringa er avslutta." },
                        )
                    }
                }
            }

            showIf((barnetilleggSerkullInnvilget or barnetilleggFellesInnvilget)) {
                includePhrase(TBU3800_Generated)
            }

            showIf((pe.vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumutbt().greaterThan(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_gradertoppjustertifu()) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().notEqualTo("overgangsregler_2016") and pe.vedtaksdata_beregningsdata_beregningufore_beregningvirkningdatofom().legacyGreaterThanOrEqual(LocalDate.of(2016, 1, 1)))) {
                paragraph {
                    text(
                        bokmal { +"Uføretrygden og barnetillegget ditt kan til sammen ikke utgjøre mer enn 95 prosent av inntekten din før du ble ufør. 95 prosent av den inntekten du hadde før du ble ufør tilsvarer i dag en inntekt på " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_gradertoppjustertifu().format() + ". Uføretrygden og barnetillegget ditt er til sammen høyere enn dette. Årlig barnetillegg før reduksjon ut fra inntekt blir " },
                        nynorsk { +"Uføretrygda di og barnetillegget ditt kan til saman ikkje utgjere meir enn 95 prosent av inntekta di før du blei ufør. 95 prosent av den inntekta du hadde før du blei ufør, tilsvarer i dag ei inntekt på " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_gradertoppjustertifu().format() + ". Uføretrygda di og barnetillegget ditt er til saman høgare enn dette. Brutto årleg barnetillegg før reduksjon ut frå inntekt blir " },
                    )

                    showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoforreduksjonbt().greaterThan(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoetterreduksjonbt()) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoetterreduksjonbt().greaterThan(0))) {
                        text(
                            bokmal { +"derfor redusert til " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoetterreduksjonbt().format() + ". " },
                            nynorsk { +"derfor redusert til " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoetterreduksjonbt().format() + ". " },
                        )
                    }

                    showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoforreduksjonbt().greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoetterreduksjonbt().equalTo(0))) {
                        text(
                            bokmal { +"ikke utbetalt. " },
                            nynorsk { +"ikkje utbetalt." },
                        )
                    }

                    showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoforreduksjonbt().greaterThan(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoetterreduksjonbt()) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoetterreduksjonbt().greaterThan(0))) {
                        text(
                            bokmal { +"Har du inntekt ved siden av uføretrygden vil dette også kunne ha betydning for størrelsen på barnetillegget ditt." },
                            nynorsk { +"Har du inntekt ved sida av uføretrygda, kan dette også ha noko å seie for storleiken på barnetillegget ditt." },
                        )
                    }
                }
            }

            showIf((not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoforreduksjonbt().greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoetterreduksjonbt().equalTo(0)))) {

                showIf(((not(barnetilleggFellesInnvilget) and barnetilleggSerkullInnvilget))) {
                    includePhrase(TBU2338_Generated(pe))
                }

                showIf(barnetilleggFellesInnvilget) {
                    includePhrase(TBU2339_Generated(pe))
                }

                showIf((barnetilleggSerkullInnvilget or barnetilleggFellesInnvilget)) {
                    paragraph {
                        text(
                            bokmal { +"Endringer i " },
                            nynorsk { +"Endringar i " },
                        )

                        showIf(barnetilleggFellesInnvilget) {
                            text(
                                bokmal { +"inntektene til deg og din " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut() + " " },
                                nynorsk { +"inntektene til deg og " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_nn_entall() + " din " },
                            )
                        }

                        showIf((not(barnetilleggFellesInnvilget) and barnetilleggSerkullInnvilget)) {
                            text(
                                bokmal { +"inntekten din " },
                                nynorsk { +"inntekta di " },
                            )
                        }
                        text(
                            bokmal { +"kan ha betydning for barnetillegget ditt. Du kan enkelt melde fra om inntektsendringer under menyvalget «uføretrygd» på $NAV_URL." },
                            nynorsk { +"kan ha betydning for barnetillegget ditt. Du kan enkelt melde frå om inntektsendringar under menyvalet «uføretrygd» på $NAV_URL." },
                        )
                    }
                }

                showIf(barnetilleggFellesInnvilget or barnetilleggSerkullInnvilget) {
                    includePhrase(
                        InntektBarnetillegg(
                            btFellesInnvilget = barnetilleggFellesInnvilget,
                            btSerkullInnvilget = barnetilleggSerkullInnvilget,
                            grunnbelop = pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop(),
                            pe = pe,
                            periodisertInntekt = periodisertInntekt
                        )
                    )
                }

                showIf(((barnetilleggFellesInnvilget))) {
                    paragraph {
                        text(
                            bokmal { +"Inntekten din er " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbbrukersinntekttilavkortning().format() + " og inntekten til din " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut() + " er " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbinntektannenforelder().format() + ". " },
                            nynorsk { +"Inntekta di er " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbbrukersinntekttilavkortning().format() + ", og inntekta til " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_nn_entall() + " din er " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbinntektannenforelder().format() + ". " },
                        )

                        showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbbelopfratrukketannenforeldersinntekt().greaterThan(0))) {
                            text(
                                bokmal { +"Folketrygdens grunnbeløp på inntil " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop().format() + " er holdt utenfor inntekten til din " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut() + ". " },
                                nynorsk { +"Grunnbeløpet i folketrygda på inntil " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop().format() + " er halde utanfor inntekta til " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_nn_entall() + " din. " }
                            )
                        }

                        showIf(((not(barnetilleggSerkullInnvilget) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().equalTo(0) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().notEqualTo(0)))) {
                            showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinntektbruktiavkortning().greaterThan(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbfribelop())) {
                                text(
                                    bokmal { +"Til sammen er inntektene høyere enn fribeløpet ditt på " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbfribelop().format() + ". Barnetillegget ditt er derfor " },
                                    nynorsk { +"Til saman er inntektene høgare enn fribeløpet ditt på " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbfribelop().format() + ". Barnetillegget ditt er derfor " },
                                )
                            }.orShow {
                                text(
                                    bokmal { +"Til sammen er inntektene lavere enn fribeløpet ditt på " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbfribelop().format() + ". Barnetillegget ditt er derfor " },
                                    nynorsk { +"Til saman er inntektene lågare enn fribeløpet ditt på " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbfribelop().format() + ". Barnetillegget ditt er derfor " },
                                )
                            }
                        }

                        showIf(((not(barnetilleggSerkullInnvilget) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().equalTo(0) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().notEqualTo(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().equalTo(0)))) {
                            text(
                                bokmal { +"ikke " },
                                nynorsk { +"ikkje " },
                            )
                        }

                        showIf(((not(barnetilleggSerkullInnvilget) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().equalTo(0) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().notEqualTo(0)))) {
                            text(
                                bokmal { +"redusert. " },
                                nynorsk { +"redusert. " },
                            )
                        }

                        showIf(((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().notEqualTo(0) and not(barnetilleggSerkullInnvilget)))) {
                            text(
                                bokmal { +"Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget. " },
                                nynorsk { +"Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover. Dette har vi teke omsyn til når vi endra barnetillegget. " },
                            )
                        }

                        showIf(((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().notEqualTo(0) and not(barnetilleggSerkullInnvilget) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().equalTo(0)))) {
                            text(
                                bokmal { +"Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året. " },
                                nynorsk { +"Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året. " },
                            )
                        }
                    }
                }

                showIf((barnetilleggSerkullInnvilget and not(barnetilleggFellesInnvilget))) {
                    paragraph {
                        showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar().equalTo(0) and pe.ut_btsbinnvilget_ikke_btfbinnvilget()))) {
                            showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning().greaterThan(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop())) {
                                text(
                                    bokmal { +"Inntekten din på " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning().format() + " er høyere enn fribeløpet ditt på " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop().format() + ". Barnetillegget ditt er derfor " },
                                    nynorsk { +"Inntekta di på " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning().format() + " er høgare enn fribeløpet ditt på " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop().format() + ". Barnetillegget ditt er derfor " },
                                )
                            }.orShow {
                                text(
                                    bokmal { +"Inntekten din på " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning().format() + " er lavere enn fribeløpet ditt på " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop().format() + ". Barnetillegget ditt er derfor " },
                                    nynorsk { +"Inntekta di på " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning().format() + " er lågare enn fribeløpet ditt på " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop().format() + ". Barnetillegget ditt er derfor " },
                                )
                            }
                        }

                        showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar().equalTo(0) and pe.ut_btsbinnvilget_ikke_btfbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning().lessThanOrEqual(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop())))) {
                            text(
                                bokmal { +"ikke " },
                                nynorsk { +"ikkje " },
                            )
                        }

                        showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar().equalTo(0) and pe.ut_btsbinnvilget_ikke_btfbinnvilget()))) {
                            text(
                                bokmal { +"redusert ut fra inntekt. " },
                                nynorsk { +"redusert ut frå inntekt. " },
                            )
                        }

                        showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar().equalTo(0) and pe.ut_btsbinnvilget_ikke_btfbinnvilget()))) {
                            text(
                                bokmal { +"Inntekten din er " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning().format() + ". " },
                                nynorsk { +"Inntekta di er " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning().format() + ". " },
                            )
                        }

                        showIf(((pe.ut_btsbinnvilget_og_justbeloppa()))) {
                            text(
                                bokmal { +"Inntekten din er " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning().format() + ". Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget. " },
                                nynorsk { +"Inntekta di er " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning().format() + ". Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover. Dette har vi teke omsyn til når vi endra barnetillegget. " },
                            )
                        }

                        showIf(((pe.ut_btsbinnvilget_og_justbeloppa() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().equalTo(0)))) {
                            text(
                                bokmal { +"Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året." },
                                nynorsk { +"Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året." },
                            )
                        }
                    }
                }

                showIf((pe.ut_tbu1286_del1() or pe.ut_tbu1286_del2() or pe.ut_tbu1286_del3())) {
                    paragraph {
                        showIf((pe.ut_tbu1286_del1() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().notEqualTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar().equalTo(0))) {
                            showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning().greaterThan(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop())) {
                                text(
                                    bokmal { +"Inntekten din er høyere enn " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop().format() + ", som er fribeløpet for barnetillegget til " + pe.ut_barnet_barna_serkull() + " som ikke bor sammen med begge foreldrene. " },
                                    nynorsk { +"Inntekta di er høgare enn " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop().format() + ", som er fribeløpet for barnetillegget til " + pe.ut_barnet_barna_serkull() + " som ikkje bur saman med begge foreldra. " },
                                )
                            }.orShow {
                                text(
                                    bokmal { +"Inntekten din er lavere enn " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop().format() + ", som er fribeløpet for barnetillegget til " + pe.ut_barnet_barna_serkull() + " som ikke bor sammen med begge foreldrene. " },
                                    nynorsk { +"Inntekta di er lågare enn " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop().format() + ", som er fribeløpet for barnetillegget til " + pe.ut_barnet_barna_serkull() + " som ikkje bur saman med begge foreldra. " },
                                )
                            }
                        }

                        showIf((pe.ut_tbu1286_del1() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().notEqualTo(0) and ((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().greaterThan(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().equalTo(0)) or (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().equalTo(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().greaterThan(0))) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar().equalTo(0))) {
                            text(
                                bokmal { +"Dette barnetillegget er derfor " + pe.ut_ikke() + "redusert ut fra inntekt. " },
                                nynorsk { +"Dette barnetillegget er derfor " + pe.ut_ikke() + "redusert ut frå inntekt. " },
                            )
                        }

                        showIf((pe.ut_tbu1286_del1() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().notEqualTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().equalTo(0))) {
                            text(
                                bokmal { +"Til sammen er " },
                                nynorsk { +"Til saman er " },
                            )
                        }

                        showIf((pe.ut_tbu1286_del1() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().notEqualTo(0) and ((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().equalTo(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().equalTo(0)) or (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().greaterThan(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().greaterThan(0))) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().equalTo(0))) {
                            text(
                                bokmal { +"også " },
                                nynorsk { +"også " },
                            )
                        }

                        showIf((pe.ut_tbu1286_del1() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().notEqualTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().equalTo(0))) {
                            showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinntektbruktiavkortning().greaterThan(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbfribelop())) {
                                text(
                                    bokmal { +"inntektene til deg og din " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut() + " høyere enn " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbfribelop().format() + ", som er fribeløpet for barnetillegget til " + pe.ut_barnet_barna_felles() + " som bor med begge sine foreldre. " },
                                    nynorsk { +"inntektene til deg og " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_nn_entall() + " din høgare enn " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbfribelop().format() + ", som er fribeløpet for barnetillegget til " + pe.ut_barnet_barna_felles() + " som bur saman med begge foreldra sine. " },
                                )
                            }.orShow {
                                text(
                                    bokmal { +"inntektene til deg og din " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut() + " lavere enn " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbfribelop().format() + ", som er fribeløpet for barnetillegget til " + pe.ut_barnet_barna_felles() + " som bor med begge sine foreldre. " },
                                    nynorsk { +"inntektene til deg og " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_nn_entall() + " din lågare enn " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbfribelop().format() + ", som er fribeløpet for barnetillegget til " + pe.ut_barnet_barna_felles() + " som bur saman med begge foreldra sine. " },
                                )
                            }
                        }

                        showIf((pe.ut_tbu1286_del1() and ((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().greaterThan(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().equalTo(0)) or (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().equalTo(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().greaterThan(0))) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().notEqualTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().equalTo(0))) {
                            text(
                                bokmal { +"Dette barnetillegget er derfor " },
                                nynorsk { +"Dette barnetillegget er derfor " },
                            )
                        }

                        showIf((pe.ut_tbu1286_del1() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().equalTo(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbbrutto()) and ((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().greaterThan(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().equalTo(0)) or (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().equalTo(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().greaterThan(0))) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().notEqualTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().equalTo(0))) {
                            text(
                                bokmal { +"ikke " },
                                nynorsk { +"ikkje " },
                            )
                        }

                        showIf((pe.ut_tbu1286_del1() and ((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().greaterThan(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().equalTo(0)) or (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().equalTo(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().greaterThan(0))) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().notEqualTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().equalTo(0))) {
                            text(
                                bokmal { +"redusert ut fra inntekt. " },
                                nynorsk { +"redusert ut frå inntekt. " },
                            )
                        }

                        showIf(
                            (pe.ut_tbu1286_del1() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().notEqualTo(0) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().notEqualTo(0) and ((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().equalTo(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().equalTo(0)) or (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().greaterThan(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().greaterThan(0))) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar()
                                .equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().equalTo(0))
                        ) {
                            text(
                                bokmal { +"Barnetilleggene er derfor" },
                                nynorsk { +"Desse barnetillegga er derfor" },
                            )
                        }

                        showIf(
                            (pe.ut_tbu1286_del1() and (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().equalTo(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbbrutto()) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().equalTo(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbbrutto())) and (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().notEqualTo(0) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().notEqualTo(0)) and ((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().equalTo(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag()
                                .equalTo(0)) or (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().greaterThan(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().greaterThan(0))) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar().equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().equalTo(0))
                        ) {
                            text(
                                bokmal { +" ikke" },
                                nynorsk { +" ikkje" },
                            )
                        }

                        showIf(
                            (pe.ut_tbu1286_del1() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().notEqualTo(0) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().notEqualTo(0) and ((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().equalTo(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().equalTo(0)) or (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().greaterThan(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().greaterThan(0))) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar()
                                .equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().equalTo(0))
                        ) {
                            text(
                                bokmal { +" redusert ut fra inntekt. " },
                                nynorsk { +" redusert ut frå inntekt. " },
                            )
                        }

                        showIf((pe.ut_tbu1286_del1() and (pe.ut_tbu1286_del2() or pe.ut_tbu1286_del3()))) {
                            text(
                                bokmal { +" " },
                                nynorsk { +" " },
                            )
                        }

                        showIf(((pe.ut_tbu1286_del2()))) {
                            text(
                                bokmal { +"Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget for " + pe.ut_barnet_barna_felles() + " som bor med begge sine foreldre. " },
                                nynorsk { +"Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover. Dette har vi teke omsyn til når vi endra barnetillegget for " + pe.ut_barnet_barna_felles() + " som bur saman med begge foreldra sine. " },
                            )
                        }

                        showIf(((pe.ut_tbu1286_del2() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().equalTo(0)))) {
                            text(
                                bokmal { +"Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året. " },
                                nynorsk { +"Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året. " },
                            )
                        }

                        showIf(((pe.ut_tbu1286_del2() and pe.ut_tbu1286_del3()))) {
                            text(
                                bokmal { +" " },
                                nynorsk { +" " },
                            )
                        }

                        showIf(((pe.ut_tbu1286_del3()))) {
                            text(
                                bokmal { +"Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget for " + pe.ut_barnet_barna_serkull() + " som ikke bor sammen med begge foreldrene. " },
                                nynorsk { +"Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover. Dette har vi teke omsyn til når vi endra barnetillegget for " + pe.ut_barnet_barna_serkull() + " som ikkje bur saman med begge foreldra. " },
                            )
                        }

                        showIf(((pe.ut_tbu1286_del3() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().equalTo(0)))) {
                            text(
                                bokmal { +"Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året. " },
                                nynorsk { +"Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året. " },
                            )
                        }
                    }
                }

                showIf((barnetilleggSerkullInnvilget and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar().equalTo(0) and (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().notEqualTo(0) or not(barnetilleggFellesInnvilget)))) {
                    paragraph {
                        text(
                            bokmal { +"Barnetillegget " },
                            nynorsk { +"Barnetillegget " },
                        )

                        showIf(barnetilleggFellesInnvilget) {
                            text(
                                bokmal { +"for " + pe.ut_barnet_barna_serkull() + " som ikke bor sammen med begge foreldrene, " },
                                nynorsk { +"for " + pe.ut_barnet_barna_serkull() + " som ikkje bur saman med begge foreldra sine, " },
                            )
                        }
                        text(
                            bokmal { +"blir ikke utbetalt fordi du " },
                            nynorsk { +"blir ikkje utbetalt fordi du " },
                        )

                        showIf(barnetilleggFellesInnvilget) {
                            text(
                                bokmal { +"alene " },
                                nynorsk { +"åleine " },
                            )
                        }
                        text(
                            bokmal { +"har en samlet inntekt som er høyere enn " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_inntektstak().format() + ". Inntekten din er over grensen for å få utbetalt barnetillegg." },
                            nynorsk { +"har ei samla inntekt som er høgare enn " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_inntektstak().format() + ". Inntekta di er over grensa for å få utbetalt barnetillegg. " },
                        )
                    }
                }

                showIf((barnetilleggFellesInnvilget and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().equalTo(0) and (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().notEqualTo(0) or not(barnetilleggSerkullInnvilget)))) {
                    paragraph {
                        text(
                            bokmal { +"Barnetillegget" },
                            nynorsk { +"Barnetillegget " },
                        )

                        showIf(barnetilleggSerkullInnvilget) {
                            text(
                                bokmal { +" for " + pe.ut_barnet_barna_felles() + " som bor med begge sine foreldre," },
                                nynorsk { +"for " + pe.ut_barnet_barna_felles() + " som bur saman med begge foreldra sine, " },
                            )
                        }
                        text(
                            bokmal { +" blir ikke utbetalt fordi dere har en samlet inntekt som er høyere enn " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_inntektstak().format() + ". De samlede inntektene er over grensen for å få utbetalt barnetillegg." },
                            nynorsk { +"blir ikkje utbetalt fordi dei har ei samla inntekt som er høgare enn " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_inntektstak().format() + ". Dei samla inntektene er over grensa for å få utbetalt barnetillegg." },
                        )
                    }
                }

                showIf(((barnetilleggSerkullInnvilget and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar().equalTo(0)) and (barnetilleggFellesInnvilget and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().equalTo(0)))) {
                    includePhrase(TBU2490_Generated(pe))
                }

                showIf((barnetilleggSerkullInnvilget or barnetilleggFellesInnvilget)) {
                    includePhrase(TBU1288_Generated)
                }
            }

            showIf((barnetilleggFellesInnvilget or barnetilleggSerkullInnvilget)) {
                showIf((pe.ut_kravlinjekode_vedtakresultat_forekomst_bt_innv().greaterThan(0))) {
                    includePhrase(TBU5005_Generated)
                    includePhrase(TBU5007_Generated)
                }
            }

            showIf(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) {
                includePhrase(TBU1214_Generated)
            }

            showIf(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) {
                paragraph {
                    text(
                        bokmal { +"Du er innvilget uføretrygd med gjenlevendetillegg. Tillegget er beregnet etter ditt eget og din avdøde ektefelles beregningsgrunnlag og trygdetid. Tjener du mer enn bunnfradraget ditt, reduserer vi gjenlevendetillegget ditt med samme prosent som vi reduserer uføretrygden din med. " },
                        nynorsk { +"Du er innvilga uføretrygd med attlevandetillegg. Tillegget er rekna ut etter utrekningsgrunnlaget og trygdetida både for deg og for den avdøde ektefellen din. Tener du meir enn botnfrådraget ditt, reduserer vi attlevandetillegget ditt med same prosent som vi reduserer uføretrygda di med. " },
                    )

                    showIf(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_nyttgjenlevendetillegg()) {
                        text(
                            bokmal { +"Tillegget er tidsbegrenset til fem år fra virkningstidspunktet. " },
                            nynorsk { +"Tillegget er tidsbegrensa til fem år frå verknadstidspunktet. " },
                        )
                    }
                }
            }

            showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad().equalTo(uforegrad))) {
                paragraph {
                    text(
                        bokmal { +"Gjenlevendetillegget ditt er ikke redusert." },
                        nynorsk { +"Attlevandetillegget ditt er ikkje redusert." },
                    )
                }
            }

            showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad().lessThan(uforegrad) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget())) {
                includePhrase(TBU2368_Generated(pe))
            }

            showIf(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) {
                includePhrase(TBU1133_Generated)
            }

            showIf(pe.vedtaksdata_harLopendealderspensjon()) {
                includePhrase(Ufoeretrygd.KombinereUforetrygdAldersPensjon)
            }

            showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad().equalTo(uforegrad) and pesysData.vedtaksinfo.virkningsdatoTidligereMnd)) {
                includePhrase(Ufoeretrygd.EtterbetalingUforetrygd(pe.vedtaksdata_virkningfom()))
            }

            paragraph {
                text(
                    bokmal { +"<STRYK DERSOM DET IKKE PASSER>" },
                    nynorsk { +"<STRYK DERSOM DET IKKE PASSER>" },
                )
                text(
                    bokmal { +"Nav krever refusjon fra utenlandske trygdemyndigheter" },
                    nynorsk { +"Nav krever refusjon fra utenlandske trygdemyndigheter" },
                )
                text(
                    bokmal { +"" + fritekst("land") + " har etterbetalt " + fritekst("sum") + " kroner for perioden " + fritekst("fom") + " til og med " + fritekst("tom") + ". Fordi " + fritekst("begrunnelse") + " har disse pengene blitt sendt til Nav." },
                    nynorsk { +"" + fritekst("land") + " har etterbetalt " + fritekst("sum") + " kroner for perioden " + fritekst("fom") + " til og med " + fritekst("tom") + ". Fordi " + fritekst("begrunnelse") + " har disse pengene blitt sendt til Nav." },
                )
            }

            paragraph {
                text(
                    bokmal { +"Nav har rett til å kreve refusjon i etterbetaling fra utlandet etter EØS-forordningen 987/2009 artikkel 72." },
                    nynorsk { +"Nav har rett til å kreve refusjon i etterbetaling fra utlandet etter EØS-forordningen 987/2009 artikkel 72." },
                )
            }

            includePhrase(Ufoeretrygd.AvslagBarnetillegg(pesysData.nyeAvslagBarnetillegg))

            showIf((uforegrad.greaterThanOrEqual(50) and pe.vedtaksdata_beregningsdata_beregningufore_belopsendring_uforetrygdordineryk_belopgammelut().equalTo(0))) {
                paragraph {
                    text(
                        bokmal { +"Honnørkort" },
                        nynorsk { +"Honnørkort" },
                    )
                    text(
                        bokmal { +"Du har rett til honnørrabatt når du bruker offentlig transport på innenlandsreiser. Vi sender honnørkortet i posten hvis du får dette vedtaket digitalt." },
                        nynorsk { +"Du har rett til honnørrabatt når du bruker offentleg transportmiddel på innanlandsreiser. Vi sender honnørkortet i posten dersom du får dette vedtaket digitalt." },
                    )
                }
            }

            includePhrase(Ufoeretrygd.MeldeFraOmEndringer())
            includePhrase(Felles.RettTilAAKlage)
            includePhrase(Ufoeretrygd.RettTilNyVurdering)
            includePhrase(Felles.RettTilInnsyn(vedleggDineRettigheterOgPlikterUfoere))
            includePhrase(Ufoeretrygd.SjekkUtbetalingene)
            includePhrase(Ufoeretrygd.Skattekort)
            includePhrase(Felles.HarDuSpoersmaal.ufoeretrygd)
        }

        includeAttachmentIfNotNull(vedleggMaanedligUfoeretrygdFoerSkatt, pesysData.maanedligUfoeretrygdFoerSkatt)
        @OptIn(RedigerbartVedlegg::class)
        includeAttachmentRedigerbar(VedleggId("innvilgelseUTNorgeMedEndringBeregningVedlegg"),vedleggOpplysningerBruktIBeregningUTLegacy, pesysData.pe, pesysData.pe.inkluderopplysningerbruktiberegningen())
        includeAttachment(vedleggDineRettigheterOgPlikterUfore, pesysData.dineRettigheterOgPlikterUfore)
    }
}
