package no.nav.pensjon.brev.maler.ufore.uforegrad

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.Barnetillegg
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.Beregning
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.Grunnlag
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.Grunnlag.Eps
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.Institusjonsopphold
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.Kravhode
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.Reduksjonsgrunnlag
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.Tillegg
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.Vedtak
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.VilkarsvurderingFullstendig
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.Kravhode.Arsak
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.Institusjonsopphold.JustertPeriodeCode
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.Reduksjonsgrunnlag.BarnetilleggRegelverkTypeCode
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.VilkarsvurderingFullstendig.InntektEtterUforhetBegrunnelse
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.VilkarsvurderingFullstendig.InntektForUforhetBegrunnelse
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.VilkarsvurderingFullstendig.YrkesskadeBegrunnelse
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.VilkarResultat
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.VilkarsvurderingFullstendig.InntektVedSkadetidspunktBegrunnelse
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.VilkarsvurderingFullstendig.VirkningstidspunktBegrunnelse
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.selectors.barnetillegg.*
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.selectors.barnetillegg.barnetilleggFellesbarn.*
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.selectors.barnetillegg.barnetilleggSerkullsbarn.*
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.selectors.beregning.belopHarOkt
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.selectors.beregning.beregningsresultatTilRevurderingTotalNetto
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.selectors.beregning.bunnfradrag
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.selectors.beregning.bunnfradragNesteAr
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.selectors.beregning.grunnbelop
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.selectors.beregning.inntektSkadetidspunkt
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.selectors.beregning.reduksjonsprosent
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.selectors.beregning.totalNetto
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.selectors.beregning.uforegrad
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.selectors.beregning.uforetidspunkt
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.selectors.beregning.utbetalingsgrad
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.selectors.beregning.virkFom
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.selectors.beregning.yrkesskadegrad
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.selectors.beregning.yrkesskadeBest
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.selectors.grunnlag.*
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.selectors.institusjonsopphold.*
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.selectors.kravhode.*
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.selectors.reduksjonsgrunnlag.*
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.selectors.tillegg.*
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.selectors.uforeVedtaksinfo.vedtakFattetDatoEllerIdag
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.selectors.uforeVedtaksinfo.virkningsdatoTidligereMnd
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.selectors.vedtak.*
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.selectors.vilkarsvurderingFullstendig.*
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.selectors.vilkarsvurderingFullstendig.yrkesskade.*
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.OkningUforegradV2Dto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.PeriodisertInntektBarnetillegg
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.selectors.okningUforegradV2Dto.pesysData
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.selectors.okningUforegradV2Dto.pesysData.*
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.selectors.beregning.anvendtTrygdetid
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_URL
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.legacy.*
import no.nav.pensjon.brev.maler.legacy.fraser.TBU2338_Generated_Ny
import no.nav.pensjon.brev.maler.legacy.fraser.TBU2339_Generated_Ny
import no.nav.pensjon.brev.maler.legacy.fraser.TBU3802_Generated_Ny
import no.nav.pensjon.brev.maler.legacy.fraser.TBU5005_Generated
import no.nav.pensjon.brev.maler.legacy.vedlegg.vedleggOpplysningerBruktIBeregningUTLegacy
import no.nav.pensjon.brev.maler.ufore.fraser.Ufoeretrygd
import no.nav.pensjon.brev.maler.ufore.fraser.Ufoeretrygd.InntektBarnetillegg
import no.nav.pensjon.brev.maler.ufore.vedlegg.vedleggDineRettigheterOgPlikterUfoere
import no.nav.pensjon.brev.maler.ufore.vedlegg.vedleggDineRettigheterOgPlikterUfore
import no.nav.pensjon.brev.maler.ufore.vedlegg.vedleggMaanedligUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.model.Brevkategori
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.TemplateRootScope.RedigerbartVedlegg
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.namedReference
import no.nav.pensjon.brev.template.saksbehandlervalg
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.VedleggId
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import java.time.LocalDate
import java.time.Month

@TemplateModelHelpers
object OkningUforegradRedigerbarV2 : RedigerbarTemplate<OkningUforegradV2Dto> {

    override val kode = Pesysbrevkoder.Redigerbar.UT_OKNING_UFOREGRAD_V2
    override val kategori = Brevkategori.VEDTAK_ENDRING_OG_REVURDERING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.UFOREP)
    override val featureToggle = FeatureToggles.okningUforegradV2.toggle

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - økning av uføregrad",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        val periodisertInntekt = saksbehandlervalg("periodisertInntekt", "Periodisert inntekt barnetillegg").enum<PeriodisertInntektBarnetillegg>()
        val visTekstOmRenter = saksbehandlervalg("visTekstOmRenter", "Informasjon om renter på etterbetaling").bool()

        title {
            text(
                bokmal { +"Nav har innvilget søknaden din om økt uføretrygd" },
                nynorsk { +"Nav har innvilga søknaden din om auka uføretrygd" },
            )
        }
        outline {
            val pe = pesysData.pe

            val beregning: Expression<Beregning> = pesysData.beregning
            val vedtak: Expression<Vedtak> = pesysData.vedtak
            val krav: Expression<Kravhode> = pesysData.krav
            val vilkarsvurdering: Expression<VilkarsvurderingFullstendig> = pesysData.vilkarsvurdering
            val instopphold: Expression<Institusjonsopphold?> = pesysData.institusjonsopphold
            val reduksjonsgrunnlag: Expression<Reduksjonsgrunnlag?> = pesysData.reduksjonsgrunnlag
            val grunnlag: Expression<Grunnlag> = pesysData.grunnlag
            val tillegg: Expression<Tillegg> = pesysData.tillegg
            val barnetillegg: Expression<Barnetillegg> = pesysData.barnetillegg

            fun Expression<OkningUforegradV2Dto.PesysData>.faktiskBunnfradrag() =
                ifElse(not (localDateNow.year.equalTo(vedtak.virkFom.year) and vedtak.virkFom.month.equalTo(Month.DECEMBER.value))
                        or beregning.bunnfradragNesteAr.equalTo(0),
                    beregning.bunnfradrag,
                    beregning.bunnfradragNesteAr
                )

            //Beregning
            val uforegradFraBeregning = beregning.uforegrad
            val yrkesskadegradFraBeregning = beregning.yrkesskadegrad.ifNull(0)
            val utbetalingsgrad = beregning.utbetalingsgrad

            val beregningVirkFom = beregning.virkFom
            val beregningUforetidspunkt = beregning.uforetidspunkt

            val uforetotalnetto = beregning.totalNetto
            val harBeløpØkt = beregning.belopHarOkt
            val beregningsresultattilrevurderingtotalnetto = beregning.beregningsresultatTilRevurderingTotalNetto.ifNull(Kroner(0))

            val bunnfradrag = beregning.bunnfradrag
            val bunnfradragFaktisk = pesysData.faktiskBunnfradrag()
            val reduksjonsprosent = beregning.reduksjonsprosent

            val yrkesskadebest = beregning.yrkesskadeBest
            val inntektSkadetidspunkt = beregning.inntektSkadetidspunkt.ifNull(Kroner(0))

            val grunnbelop = beregning.grunnbelop

            // reduksjonsgrunnlag
            val reduksjonsgrunnlagSumutbt = reduksjonsgrunnlag.safe { sumUtBt }.ifNull(Kroner(0))
            val reduksjonsgrunnlagGradertoppjustertifu = reduksjonsgrunnlag.safe { gradertOppjustertIFU }.ifNull(Kroner(0))
            val reduksjonsgrunnlagBarnetilleggRegelverkstype = reduksjonsgrunnlag.safe { barnetilleggRegelverkType }
            val reduksjonsgrunnlagSumbruttoetterreduksjonbt = reduksjonsgrunnlag.safe { sumBruttoEtterReduksjonBt }.ifNull(Kroner(0))
            val reduksjonsgrunnlagSumbruttoforreduksjonbt = reduksjonsgrunnlag.safe { sumBruttoFoerReduksjonBt }.ifNull(Kroner(0))
            val reduksjonsgrunnlagAndelYtelseAvOifu = reduksjonsgrunnlag.safe { andelYtelseAvOIFU }.ifNull(0.0)

            // tillegg
            val ektefelletilleggInnvilget = tillegg.ektefelletilleggInnvilget
            val gjenlevendetilleggInnvilget = tillegg.gjenlevendetilleggInnvilget
            val nyttGjenlevendetillegg = tillegg.nyttGjenlevendetillegg

            //inst
            val instopphanvendt = instopphold.safe { erAnvendt }.ifNull(false)
            val instoppholdtype = instopphold.safe { type }
            val forsorgeransvarIngenPerioder = instopphold.safe { ingenPerioderHarForsorgeransvar }.ifNull(true.expr())
            val forsorgeransvarAllePerioder = instopphold.safe { allePerioderHarForsorgeransvar }.ifNull(false.expr())
            val fasteUtgifterInst = instopphold.safe { fasteUtgifter }.ifNull(Kroner(0))

            //Beregning barnetillegg
            val btSerkullInnvilget = barnetillegg.btSerkullsbarn.safe { innvilget }.ifNull(false)
            val btFellesInnvilget = barnetillegg.btFellesbarn.safe { innvilget }.ifNull(false)
            val btSerkullNetto = barnetillegg.btSerkullsbarn.safe { netto }.ifNull(Kroner(0))
            val btFellesNetto = barnetillegg.btFellesbarn.safe { netto }.ifNull(Kroner(0))
            val btSerkullBrutto = barnetillegg.btSerkullsbarn.safe { brutto }.ifNull(Kroner(0))
            val barnetilleggserkull_btsbbrutto = barnetillegg.btSerkullsbarn.safe { brutto }.ifNull(Kroner(0))
            val btfbbrutto = barnetillegg.btFellesbarn.safe { brutto }.ifNull(Kroner(0))

            val btsbJusteringsbelopPerAr = barnetillegg.btSerkullsbarn.safe { justeringsbelopPerAr }.ifNull(Kroner(0))
            val btfbJusteringsbelopPerAr = barnetillegg.btFellesbarn.safe { justeringsbelopPerAr }.ifNull(Kroner(0))

            val btfbinntektannenforelder = barnetillegg.btFellesbarn.safe { inntektAnnenForelder }.ifNull(Kroner(0))
            val btfbbrukersinntekttilavkortning = barnetillegg.btFellesbarn.safe { brukersInntektTilAvkorting }.ifNull(Kroner(0))
            val btfbInntektstak = barnetillegg.btFellesbarn.safe { inntektstak }.ifNull(Kroner(0))
            val btfbbelopfratrukketannenforeldersinntekt = barnetillegg.btFellesbarn.safe { belopFratrukketAnnenForeldersInntekt }.ifNull(Kroner(0))
            val barnetilleggserkull_avkortningsinformasjon_inntektstak = barnetillegg.btSerkullsbarn.safe { inntektstak }.ifNull(Kroner(0))
            val btfbfradrag = barnetillegg.btFellesbarn.safe { fradrag }.ifNull(Kroner(0))
            val btsbfradrag = barnetillegg.btSerkullsbarn.safe { fradrag }.ifNull(Kroner(0))
            val btfbFribelop = barnetillegg.btFellesbarn.safe { fribelop }.ifNull(Kroner(0))
            val btsbfribelop = barnetillegg.btSerkullsbarn.safe { fribelop }.ifNull(Kroner(0))
            val btfbinntektbruktiavkorting = barnetillegg.btFellesbarn.safe { inntektBruktIAvkorting }.ifNull(Kroner(0))
            val btsbInntektBruktIAvkorting = barnetillegg.btSerkullsbarn.safe { inntektBruktIAvkorting }.ifNull(Kroner(0))
            val forekomstBtInnv = barnetillegg.antallForekomsterAvBarnetillegg

            //krav
            val kravOnsketvirkningsdato = krav.virkFom
            val kravMotattDato = krav.mottattDato
            val kravarsaktype = krav.arsak
            val konvertertup = pesysData.krav.konvertertFraUforepensjon

            //vedtak
            val vedtakvirkfom = vedtak.virkFom

            //vilkårsvurdering
            val uforetidspunkt = vilkarsvurdering.uforetidspunkt
            val uforegradFraVilkar = vilkarsvurdering.uforegrad

            val virkningstidspunkt = vilkarsvurdering.virkningstidspunkt

            val skadetidspunkt = vilkarsvurdering.yrkesskade.safe { skadetidspunkt }.ifNull(localDateNow)
            val yrkesskadegradFraVilkar = vilkarsvurdering.yrkesskade.safe { yrkesskadegrad }.ifNull(0)
            val yrkesskadebegrunnelse = vilkarsvurdering.yrkesskadeBegrunnelse
            val yrkesskaderesultat = vilkarsvurdering.yrkesskadeResultat

            val ifuinntekt = vilkarsvurdering.ifuInntekt
            val ifubegrunnelse = vilkarsvurdering.ifuBegrunnelse
            val ieuinntekt = vilkarsvurdering.ieuInntekt
            val ieuBegrunnelse = vilkarsvurdering.ieuBegrunnelse
            val unguforeresultat = vilkarsvurdering.ungUforResultat
            val nedsattInntektsevneResultat = vilkarsvurdering.nedsattInntektsevneResultat
            val virkningBegrunnelse = vilkarsvurdering.virkningstidspunktBegrunnelse

            //TODO: denne må formateres
            val mnVirkningstidspunkt = vilkarsvurdering.virkningstidspunkt.month

            val ivsbegrunnelse = vilkarsvurdering.yrkesskade.safe { inntektVedSkadetidspunktBegrunnelse }
            val ivsinntekt = vilkarsvurdering.yrkesskade.safe { inntektVedSkadetidspunkt }.ifNull(Kroner(0))

            //grunnlag
            val bostedutland = grunnlag.borIUtlandet

            // ymse
            val harLopendeAlderspensjon = pesysData.harAlderspensjonLopendeEllerTilBehandling

            //Sammenstiller andre data
            val btInnvilget = btFellesInnvilget or btSerkullInnvilget
            val btSerkullNetto0 = btSerkullNetto.equalTo(0)
            val btFellesNetto0 = btFellesNetto.equalTo(0)
            val btfbjusteringsbelopperar0 = btfbJusteringsbelopPerAr.equalTo(0)
            val kunBt = btInnvilget and not(ektefelletilleggInnvilget) and not(gjenlevendetilleggInnvilget)
            val kunGjt = not(btInnvilget) and not(ektefelletilleggInnvilget) and gjenlevendetilleggInnvilget
            val kunEt = ektefelletilleggInnvilget and not(btInnvilget) and not(gjenlevendetilleggInnvilget)
            val btOgGt = btInnvilget and gjenlevendetilleggInnvilget and not(ektefelletilleggInnvilget)
            val btOgEt = btInnvilget and ektefelletilleggInnvilget and not(gjenlevendetilleggInnvilget)
            val btsbInnvbtfbInnvJusteringsbelopSerkullIkke0 = btSerkullInnvilget and btFellesInnvilget and btsbJusteringsbelopPerAr.notEqualTo(0)
            val btsbInnvbtfbInnvJusteringsbelopFellesIkke0 = btSerkullInnvilget and btFellesInnvilget and btfbJusteringsbelopPerAr.notEqualTo(0)
            val btsbInnvbtfbInnvOgEnAvDeHarNettoUlik0OgJusteringsbelop0 = btSerkullInnvilget and btFellesInnvilget and ((btSerkullNetto.notEqualTo(0) and btsbJusteringsbelopPerAr.equalTo(0)) or (btFellesNetto.notEqualTo(0) and btfbJusteringsbelopPerAr.equalTo(0)))
            val btsbInnvilgetOgJusteringsbelop0 = btSerkullInnvilget and btsbJusteringsbelopPerAr.notEqualTo(0)
            val btsbInnvilgetIkkeBtfbinnvilget = btSerkullInnvilget and not(btFellesInnvilget)
            val sumUtBtHoyereLavere = reduksjonsgrunnlagSumutbt.greaterThan(reduksjonsgrunnlagGradertoppjustertifu)
            val barnetBarnaFellesEntallFlertall = ifElse(barnetillegg.btFellesbarn.safe { antallBarn }.ifNull(0).equalTo(1) and Expression.FromScope.Language.equalTo(English.expr()), "s".expr(), "".expr())

            //tekster
            val barnetBarnaSerkull =
                barnetillegg.btSerkullsbarn.safe { antallBarn }.ifNull(0).let {
                    ifElse(it.greaterThan(1), "barna".expr(),
                    ifElse(it.equalTo(1), "barnet".expr(), "".expr())
                    )
            }
            val epsBorMedNnEntall = ifElse(grunnlag.epsBrukerBorMed.equalTo(Eps.EKTEFELLE), "ektefellen".expr(),
                ifElse(grunnlag.epsBrukerBorMed.equalTo(Eps.PARTNER), "partnaren".expr(),
                    ifElse(grunnlag.epsBrukerBorMed.equalTo(Eps.SAMBOER), "sambuaren".expr(), "".expr())
                )
            )
            val epsBorMedBmEntall = ifElse(grunnlag.epsBrukerBorMed.equalTo(Eps.EKTEFELLE), "ektefelle".expr(),
                ifElse(grunnlag.epsBrukerBorMed.equalTo(Eps.PARTNER), "partner".expr(),
                    ifElse(grunnlag.epsBrukerBorMed.equalTo(Eps.SAMBOER), "samboer".expr(), "".expr())
                )
            )
            val barnetBarnaFelles =
                barnetillegg.btFellesbarn.safe { antallBarn }.ifNull(0).let {
                    ifElse(it.greaterThan(1), "barna".expr(),
                        ifElse(it.equalTo(1), "barnet".expr(), "".expr())
                    )
                }
            val ikkeHvisBtsbBruttoErLikNetto = ifElse(
                btSerkullNetto.equalTo(btSerkullBrutto),
                ifElse(Expression.FromScope.Language.equalTo(Bokmal.expr()), "ikke ".expr(), "ikkje ".expr()),
                "".expr()
            )
            val txtOgEllerEktefelle = ifElse(tillegg.ektefelletilleggInnvilget, " og/eller ektefelle".expr(), "".expr())


            //ikke pe
            val oifuKroner = pesysData.oifuVedVirkningstidspunkt.ifNull(Kroner(0))
            val oifuMerEnnIfu = oifuKroner.greaterThan(ifuinntekt)

            paragraph {
                text(
                    bokmal { +"Vi har innvilget søknaden din om økt uføretrygd som vi mottok " + kravMotattDato.format() + ". " },
                    nynorsk { +"Vi har innvilga søknaden din om auka uføretrygd som vi fekk " + kravMotattDato.format() + ". " },
                )
                showIf(kravarsaktype.isNotAnyOf(Arsak.OMGJ_ETTER_KLAGE, Arsak.OMGJ_ETTER_ANKE)) {
                    text(
                        bokmal { +"Uføregraden din øker fra " + pesysData.forrigeUforegrad.format() + " til " + uforegradFraBeregning.format() + " prosent fra " + virkningstidspunkt.format() + ". " },
                        nynorsk { +"Uføregraden din aukar frå " + pesysData.forrigeUforegrad.format() + " til " + uforegradFraBeregning.format() + " prosent frå " + virkningstidspunkt.format() + ". " },
                    )
                }.orShow {
                    text(
                        bokmal { +"Du har fått medhold i klagen din, og får " + uforegradFraBeregning.format() + " prosent uføretrygd fra " + vedtakvirkfom.format() + ". " },
                        nynorsk { +"Du har fått medhald i klaga di, og får " + uforegradFraBeregning.format() + " prosent uføretrygd frå " + vedtakvirkfom.format() + ". " },
                    )
                }
                showIf(((unguforeresultat).equalTo(VilkarResultat.OPPFYLT))) {
                    text(
                        bokmal { +"Hele uføretrygden din er innvilget med rettighet som ung ufør. " },
                        nynorsk { +"Heile uføretrygda di er innvilga med rett som ung ufør. " },
                    )
                }
            }

            showIf(((yrkesskadegradFraVilkar).equalTo((uforegradFraVilkar)))) {
                paragraph {
                    text(
                        bokmal { +"Vi har kommet fram til at hele uførheten din skyldes en godkjent yrkesskade eller yrkessykdom." },
                        nynorsk { +"Vi har kome fram til at heile uførleiken din kjem av ein godkjend yrkesskade eller yrkessjukdom." },
                    )
                }
            }

            //IF(FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_YrkesskadeGrad) < FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Uforegrad) AND FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_YrkesskadeGrad) > 0 AND (FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat) = "oppfylt" OR FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat) = "ikke_oppfylt") ) THEN      INCLUDE ENDIF
            showIf(((yrkesskadegradFraVilkar).lessThan((uforegradFraVilkar)) and (yrkesskadegradFraVilkar).greaterThan(0) and ((yrkesskaderesultat).equalTo(VilkarResultat.OPPFYLT) or (yrkesskaderesultat).equalTo(VilkarResultat.IKKE_OPPFYLT)))) {
                paragraph {
                    text(
                        bokmal { +"Vi har kommet fram til at " + yrkesskadegradFraVilkar.format() + " prosent av uførheten din skyldes en godkjent yrkesskade eller yrkessykdom." },
                        nynorsk { +"Vi har kome fram til at " + yrkesskadegradFraVilkar.format() + " prosent av uførleiken din kjem av ein godkjend yrkesskade eller yrkessjukdom." },
                    )
                }
            }

            showIf(((yrkesskaderesultat).equalTo(VilkarResultat.IKKE_OPPFYLT) and (yrkesskadebegrunnelse).equalTo(YrkesskadeBegrunnelse.STDBEGR_12_17_1_I_1))) {
                paragraph {
                    text(
                        bokmal { +"Vi har kommet fram til at uførheten din ikke skyldes en godkjent yrkesskade eller yrkessykdom." },
                        nynorsk { +"Vi har kome fram til at uførleiken din ikkje kjem av ein godkjend yrkesskade eller yrkessjukdom." },
                    )
                }
            }

            showIf(((yrkesskaderesultat).equalTo(VilkarResultat.IKKE_OPPFYLT)) and ((yrkesskadebegrunnelse).equalTo(YrkesskadeBegrunnelse.STDBEGR_12_17_1_I_2))) {
                paragraph {
                    text(
                        bokmal { +"Vi har kommet fram til at det er andre sykdomsforhold enn din godkjente yrkesskade eller yrkessykdom som er årsak til den økte uførheten din." },
                        nynorsk { +"Vi har kome fram til at det er andre sjukdomsforhold enn godkjend yrkesskade eller yrkessjukdom som er årsak til den auka uførleiken din." },
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

                    //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND  (((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0)   AND   (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0))   OR  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false)   OR  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false))  ) THEN      INCLUDE ENDIF
                    showIf((btFellesInnvilget and (((btSerkullInnvilget and btSerkullNetto0) and (btFellesInnvilget and btFellesNetto0)) or (btSerkullInnvilget and btSerkullNetto0 and not(btFellesInnvilget)) or (btFellesInnvilget and btFellesNetto0 and not(btSerkullInnvilget))))) {
                        text(
                            bokmal { +"Tillegget blir ikke utbetalt fordi inntekten til deg og din ektefelle er over grensen for å få utbetalt barnetillegg. " },
                            nynorsk { +"Tillegget blir ikkje utbetalt fordi inntekta di og inntekta til ektefellen din er over grensa for å få utbetalt barnetillegg. " },
                        )
                    }

                    //IF( (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false)  AND  (((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0)   AND   (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0))   OR  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false)   OR  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false))  ) THEN      INCLUDE ENDIF
                    showIf(((btSerkullInnvilget and not(btFellesInnvilget)) and (((btSerkullInnvilget and btSerkullNetto0) and (btFellesInnvilget and btFellesNetto0)) or (btSerkullInnvilget and btSerkullNetto0 and not(btFellesInnvilget)) or (btFellesInnvilget and btFellesNetto0 and not(btSerkullInnvilget))))) {
                        text(
                            bokmal { +"Tillegget blir ikke utbetalt fordi inntekten din er over grensen for å få utbetalt barnetillegg." },
                            nynorsk { +"Tillegget blir ikkje betalt ut fordi inntekta di er over grensa for å få utbetalt barnetillegg." },
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

            showIf(gjenlevendetilleggInnvilget) {
                paragraph {
                    text(
                        bokmal { +"Du er innvilget gjenlevendetillegg i uføretrygden din." },
                        nynorsk { +"Du er innvilga attlevandetillegg i uføretrygda di." },
                    )
                }
            }

            showIf((instoppholdtype.equalTo(JustertPeriodeCode.REDUKSJON_HS) and instopphanvendt)) {
                paragraph {
                    text(
                        bokmal { +"Vi har redusert utbetalingen av uføretrygden din, fordi du er innlagt på institusjon." },
                        nynorsk { +"Vi har redusert utbetalinga av uføretrygda di, fordi du er innlagd på institusjon." },
                    )
                }
            }

            showIf((instoppholdtype.equalTo(JustertPeriodeCode.REDUKSJON_HS) and not(instopphanvendt))) {
                paragraph {
                    text(
                        bokmal { +"Du er innlagt på institusjon, men vi kommer likevel ikke til å redusere utbetalingen av uføretrygden din." },
                        nynorsk { +"Du er innlagd på institusjon, men vi kjem likevel ikkje til å redusere utbetalinga av uføretrygda di." },
                    )
                }
            }

            showIf((instoppholdtype.equalTo(JustertPeriodeCode.REDUKSJON_FO) and instopphanvendt)) {
                paragraph {
                    text(
                        bokmal { +"Vi har redusert utbetalingen av uføretrygden din, fordi du er under straffegjennomføring." },
                        nynorsk { +"Vi har redusert utbetalinga av uføretrygda di, fordi du er under straffegjennomføring." },
                    )
                }
            }

            showIf((instoppholdtype.equalTo(JustertPeriodeCode.REDUKSJON_FO) and not(instopphanvendt))) {
                paragraph {
                    text(
                        bokmal { +"Du er under straffegjennomføring, men vi kommer likevel ikke til å redusere utbetalingen av uføretrygden din." },
                        nynorsk { +"Du er under straffegjennomføring, men vi kjem likevel ikkje til å redusere utbetalinga av uføretrygda di." },
                    )
                }
            }

            showIf(not(instopphanvendt)) {
                paragraph {
                    text(
                        bokmal { +"Du får " + uforetotalnetto.format() + " i uføretrygd" },
                        nynorsk { +"Du får " + uforetotalnetto.format() + " i uføretrygd" },
                    )
                    showIf(kunBt) {
                        text(bokmal { +" og barnetillegg" }, nynorsk { +" og barnetillegg" })
                    }.orShowIf(kunGjt) {
                        text(bokmal { +" og gjenlevendetillegg" }, nynorsk { +" og attlevandetillegg" })
                    }.orShowIf(btOgGt) {
                        text(bokmal { +", barne- og gjenlevendetillegg" }, nynorsk { +", barne- og attlevandetillegg" })
                    }.orShowIf(kunEt) {
                        text(bokmal { +" og ektefelletillegg" }, nynorsk { +" og ektefelletillegg" })
                    }.orShowIf(btOgEt) {
                        text(bokmal { +", barne- og ektefelletillegg" }, nynorsk { +", barne- og ektefelletillegg" })
                    }
                    text(
                        bokmal { +" per måned før skatt." },
                        nynorsk { +" per månad før skatt." },
                    )
                }
            }.orShowIf(instoppholdtype.equalTo(JustertPeriodeCode.REDUKSJON_FO) and forsorgeransvarIngenPerioder) {
                paragraph {
                    text(
                        bokmal { +"Du har rett til å få " + beregningsresultattilrevurderingtotalnetto.format() + " i uføretrygd, men den kommer ikke til utbetaling fordi du er under straffegjennomføring." },
                        nynorsk { +"Du har rett til å få " + beregningsresultattilrevurderingtotalnetto.format() + " i uføretrygd, men ho blir ikkje betalt ut fordi du er under straffegjennomføring." },
                    )
                }
            }.orShowIf(instoppholdtype.equalTo(JustertPeriodeCode.REDUKSJON_HS)) {
                paragraph {
                    text(
                        bokmal { +"Du har rett til å få " + beregningsresultattilrevurderingtotalnetto.format() + " i uføretrygd, men utbetalingen er redusert fordi du er innlagt på institusjon. I denne perioden vil du få utbetalt " + uforetotalnetto.format() + "." },
                        nynorsk { +"Du har rett til å få " + beregningsresultattilrevurderingtotalnetto.format() + " i uføretrygd, men utbetalinga er redusert fordi du er innlagd på institusjon. I denne perioden får du betalt ut " + uforetotalnetto.format() + "." },
                    )
                }
            }.orShowIf(instoppholdtype.equalTo(JustertPeriodeCode.REDUKSJON_FO) and forsorgeransvarAllePerioder) {
                paragraph {
                    text(
                        bokmal { +"Du har rett til å få " + beregningsresultattilrevurderingtotalnetto.format() + " i uføretrygd, men utbetalingen er redusert fordi du er under straffegjennomføring. I denne perioden vil du få utbetalt " + uforetotalnetto.format() + "." },
                        nynorsk { +"Du har rett til å få " + beregningsresultattilrevurderingtotalnetto.format() + " i uføretrygd, men utbetalinga er redusert fordi du er under straffegjennomføring. I denne perioden får du betalt ut " + uforetotalnetto.format() + "." },
                    )
                }
            }

            // TODO: Ny tekst for utland (som i innvilgelseUtland
            //IF(PE_Grunnlag_Persongrunnlagsliste_PersonBostedsland(1) = "" OR PE_Grunnlag_Persongrunnlagsliste_PersonBostedsland(1) = "nor") THEN      INCLUDE ENDIF
            showIf(bostedutland) {
                paragraph {
                    text(
                        bokmal { +"Uføretrygden blir utbetalt senest den 20. hver måned. Mottar du uføretrygden på en utenlandsk bankkonto kan utbetalingen bli forsinket. Du får din første utbetaling i " + fritekst("måned og år") + "." },
                        nynorsk { +"Uføretrygda blir betalt ut seinast den 20. kvar månad. Får du uføretrygda på ein utanlandsk bankkonto, kan utbetalinga bli forseinka. Du får den første utbetalinga di i " + fritekst("månad og år") + "." },
                    )
                }
            }.orShow {
                paragraph {
                    text(
                        bokmal { +"Uføretrygden blir utbetalt senest den 20. hver måned. Du får din første utbetaling med nytt beløp i " + fritekst("måned og år") + "." },
                        nynorsk { +"Uføretrygda blir utbetalt seinast den 20. i kvar månad. Du får den første utbetalinga di med nytt beløp i " + fritekst("Månad og år") + "." },
                    )
                }
            }

            title1 {
                text(
                    bokmal { +"Begrunnelse for vedtaket" },
                    nynorsk { +"Grunngiving for vedtaket" },
                )
            }

            paragraph {
                text(
                    bokmal { +"For å ha rett til økt uføretrygd må du oppfylle disse vilkårene:" },
                    nynorsk { +"For å ha rett til auka uføretrygd må du oppfylle desse vilkåra:" },
                )
                list {
                    item {
                        text(
                            bokmal { +"Du må være mellom 18 og 67 år." },
                            nynorsk { +"Du må vere mellom 18 og 67 år." },
                        )
                    }
                    item {
                        text(
                            bokmal { +"Du må ha vært medlem av folketrygden i de siste fem årene fram til uføretidspunktet, eller oppfylle en av unntaksreglene." },
                            nynorsk { +"Du må ha vore medlem av folketrygda i dei siste fem åra fram til uføretidspunktet, eller oppfylle ein av unntaksreglane." },
                        )
                    }
                    item {
                        text(
                            bokmal { +"Inntektsevnen din må ha blitt ytterligere nedsatt, og denne nedsettelsen må være varig." },
                            nynorsk { +"Inntektsevna di må ha blitt ytterlegare sett ned, og denne nedsetjinga må vere varig." },
                        )
                    }
                    item {
                        text(
                            bokmal { +"Sykdom eller skade må være hovedårsak til at inntektsevnen din har blitt ytterligere nedsatt." },
                            nynorsk { +"Sjukdom eller skade må vere hovudårsak til at inntektsevna di er blitt ytterlegare sett ned." },
                        )
                    }
                    item {
                        text(
                            bokmal { +"Du må ha gjennomført hensiktsmessig behandling og arbeidsrettede tiltak." },
                            nynorsk { +"Du må ha gjennomført formålstenleg behandling og arbeidsretta tiltak." },
                        )
                    }
                }
            }

            paragraph {
                text(
                    bokmal { +"Vi har kommet fram til at du oppfyller disse vilkårene." },
                    nynorsk { +"Vi har kome fram til at du oppfyller desse vilkåra." },
                )
            }

            showIf(kravarsaktype.equalTo(Arsak.OMGJ_ETTER_KLAGE)) {
                paragraph {
                    text(
                        bokmal { +"Søknaden din om økt uføretrygd er innvilget etter klage og vi anser klagen som ferdig behandlet. Dersom du ønsker å opprettholde klagen, må du gi tilbakemelding til Nav innen 3 uker." },
                        nynorsk { +"Søknaden din om auka uføretrygd er innvilga etter klage, og vi ser det slik at klaga er ferdig behandla. Dersom du ønskjer å halde fast på klaga, må du melde dette tilbake til Nav innan 3 veker." },
                    )
                }
            }

            paragraph {
                text(
                    bokmal { +"Vedtaket er gjort etter folketrygdloven " + pesysData.hjemler.format(HjemmelFormatter(true)) + "." },
                    nynorsk { +"Vedtaket er gjort etter folketrygdlova " + pesysData.hjemler.format(HjemmelFormatter(true)) + "." },
                )
            }

            showIf((yrkesskaderesultat.equalTo(VilkarResultat.OPPFYLT) or yrkesskaderesultat.equalTo(VilkarResultat.IKKE_OPPFYLT))) {
                title1 {
                    text(
                        bokmal { +"Uførhet som skyldes yrkesskade eller yrkessykdom" },
                        nynorsk { +"Uførleik som kjem av yrkesskade eller yrkessjukdom" },
                    )
                }
            }

            showIf((yrkesskadegradFraBeregning.equalTo(uforegradFraBeregning) and yrkesskadebegrunnelse.equalTo(YrkesskadeBegrunnelse.STDBEGR_12_17_1_O_1))) {
                paragraph {
                    text(
                        bokmal { +"Du har en godkjent yrkesskade eller yrkessykdom. Vi har ut fra sakens opplysninger vurdert at hele din nedsatte inntektsevne skyldes den godkjente yrkesskaden eller yrkessykdommen." },
                        nynorsk { +"Du har ein godkjend yrkesskade eller yrkessjukdom. Vi har ut frå opplysningane i saka vurdert at heile den nedsette inntektsevna di kjem av den godkjende yrkesskaden eller yrkessjukdommen." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad = PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagYrkesskadeBest = true AND PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeBegrunnelse(1) = "stdbegr_12_17_1_o_1" AND PE_Vedtaksbrev_Vedtaksdata_Kravhode_BrukerKonvertertUP = false) THEN      INCLUDE ENDIF
            showIf((yrkesskadegradFraBeregning.equalTo(uforegradFraBeregning) and yrkesskadebest and yrkesskadebegrunnelse.equalTo(YrkesskadeBegrunnelse.STDBEGR_12_17_1_O_1) and not(konvertertup))) {
                paragraph {
                    text(
                        bokmal { +"Dette betyr at uføretrygden din vil bli beregnet etter særbestemmelser som gir deg en høyere uføretrygd." },
                        nynorsk { +"Dette betyr at uføretrygda di blir rekna ut etter særreglar som gir deg ei høgare uføretrygd." },
                    )
                }
            }

            //IF(FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_YrkesskadeGrad) = FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Uforegrad) AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagYrkesskadeBest = false AND PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeBegrunnelse(1) = "stdbegr_12_17_1_o_1" AND PE_Vedtaksbrev_Vedtaksdata_Kravhode_BrukerKonvertertUP = false) THEN      INCLUDE ENDIF
            showIf(((yrkesskadegradFraVilkar).equalTo((uforegradFraVilkar)) and not(yrkesskadebest) and yrkesskadebegrunnelse.equalTo(YrkesskadeBegrunnelse.STDBEGR_12_17_1_O_1) and not(konvertertup))) {
                paragraph {
                    text(
                        bokmal { +"Dette betyr at uføretrygden din vil bli beregnet etter særbestemmelser, dersom dette er til fordel for deg." },
                        nynorsk { +"Dette betyr at uføretrygda di blir rekna ut etter særreglar, dersom dette er til fordel for deg." },
                    )
                }
                paragraph(uniqueness = "12_17_1_o_1") {
                    text(
                        bokmal { +"Inntekten din på skadetidspunktet er lavere enn beregningsgrunnlaget ditt, og uføretrygden din vil derfor ikke bli beregnet etter særbestemmelser for yrkesskade eller yrkessykdom." },
                        nynorsk { +"Inntekta di på skadetidspunktet er lågare enn berekningsgrunnlaget ditt, og uføretrygda di blir derfor ikkje berekna etter særreglar for yrkesskade eller yrkessjukdom." },
                    )
                }
            }

            //IF(FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_YrkesskadeGrad) < FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Uforegrad) AND FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_YrkesskadeGrad) > 0 AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat) = "oppfylt" AND PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeBegrunnelse(1) = "stdbegr_12_17_1_o_2") THEN      INCLUDE ENDIF
            showIf(((yrkesskadegradFraVilkar).lessThan((uforegradFraVilkar)) and (yrkesskadegradFraVilkar).greaterThan(0) and (yrkesskaderesultat).equalTo(VilkarResultat.OPPFYLT) and yrkesskadebegrunnelse.equalTo(YrkesskadeBegrunnelse.STDBEGR_12_17_1_O_2))) {
                paragraph(uniqueness = "12_17_1_o_2") {
                    text(
                        bokmal { +"Du har en godkjent yrkesskade eller yrkessykdom. Vi har ut fra sakens opplysninger vurdert om yrkesskaden eller yrkessykdommen er årsak til den økte uførheten din." },
                        nynorsk { +"Du har ein godkjend yrkesskade eller yrkessjukdom. Vi har ut frå opplysningane i saka vurdert om yrkesskaden eller yrkessjukdommen din er årsak til den auka uførleiken din." },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Vi har kommet fram til at " + yrkesskadegradFraVilkar.format() + " prosent av uførheten din skyldes godkjent yrkesskade eller yrkessykdom. " + fritekst("Konkret begrunnelse") + "." },
                        nynorsk { +"Vi har kome fram til at " + yrkesskadegradFraVilkar.format() + " prosent av uførleiken din kjem av ein godkjend yrkesskade eller yrkessjukdom. " + fritekst("Konkret begrunnelse") + "." },
                    )
                }

                showIf((yrkesskadebest and not(konvertertup))) {
                    paragraph {
                        text(
                            bokmal { +"Derfor vil denne delen av uføretrygden din bli beregnet etter særbestemmelser som gir deg en høyere uføretrygd." },
                            nynorsk { +"Derfor vil denne delen av uføretrygda di bli rekna ut etter særreglar som gjer deg ei høgare uføretrygd." },
                        )
                    }
                }

                showIf((not(yrkesskadebest) and not(konvertertup))) {
                    paragraph {
                        text(
                            bokmal { +"Denne delen av uføretrygden din vil bli beregnet etter særbestemmelser, dersom dette er til fordel for deg." },
                            nynorsk { +"Denne delen av uføretrygda di blir rekna ut etter særreglar dersom det er til fordel for deg." },
                        )
                    }
                    paragraph(uniqueness = "12_17_1_o_2") {
                        text(
                            bokmal { +"Inntekten din på skadetidspunktet er lavere enn beregningsgrunnlaget ditt, og uføretrygden din vil derfor ikke bli beregnet etter særbestemmelser for yrkesskade eller yrkessykdom." },
                            nynorsk { +"Inntekta di på skadetidspunktet er lågare enn berekningsgrunnlaget ditt, og uføretrygda di blir derfor ikkje berekna etter særreglar for yrkesskade eller yrkessjukdom." },
                        )
                    }
                }
            }

            showIf((yrkesskaderesultat.equalTo(VilkarResultat.OPPFYLT) and (yrkesskadebegrunnelse).equalTo(YrkesskadeBegrunnelse.STDBEGR_12_17_1_O_3))) {
                paragraph {
                    text(
                        bokmal { +"Du er tidligere innvilget uføretrygd etter særbestemmelser for yrkesskade eller yrkessykdom. Vi har ut fra opplysninger i saken din vurdert om yrkesskaden eller yrkessykdommen din også er årsak til at uførheten din har økt, eller om dette skyldes andre sykdomsforhold." },
                        nynorsk { +"Du er tidlegare innvilga uføretrygd etter særreglar for yrkesskade eller yrkessjukdom. Vi har ut frå opplysningar i saka di vurdert om yrkesskaden eller yrkessjukdommen din også er årsak til at uførleiken din har auka, eller om dette kjem av andre sjukdomsforhold." },
                    )
                }
                paragraph(uniqueness = "stdbegr_12_17_1_o_3") {
                    text(
                        bokmal { +"Vi har kommet fram til at det er andre sykdomsforhold som er årsak til den økte uførheten din. " + fritekst("Konkret begrunnelse") + "" },
                        nynorsk { +"Vi har kome fram til at det er andre sjukdomsforhold som er årsak til den auka uførleiken din. " + fritekst("Konkret begrunnelse") + "" },
                    )
                }
            }

            showIf(((yrkesskaderesultat).equalTo(VilkarResultat.OPPFYLT) and (ivsbegrunnelse).equalTo(InntektVedSkadetidspunktBegrunnelse.STDBEGR_12_17_3_1))) {
                paragraph {
                    text(
                        bokmal { +"Skadetidspunktet ditt har vi fastsatt til " + skadetidspunkt.format() + ". Din årlige inntekt på dette tidspunktet er fastsatt til " + ivsinntekt.format() + "." },
                        nynorsk { +"Vi har fastsett skadetidspunktet ditt til " + skadetidspunkt.format() + ". Den årlege inntekta di på dette tidspunktet er fastsett til " + ivsinntekt.format() + "." },
                    )
                }
            }

            showIf(((yrkesskaderesultat).equalTo(VilkarResultat.OPPFYLT) and (ivsbegrunnelse).equalTo(InntektVedSkadetidspunktBegrunnelse.STDBEGR_12_17_3_2))) {
                paragraph {
                    text(
                        bokmal { +"Skadetidspunktet ditt har vi fastsatt til " + skadetidspunkt.format() + ". Din årlige inntekt på dette tidspunktet er fastsatt til " + inntektSkadetidspunkt.format() + ". Vi har justert denne inntekten opp fordi den skal tilsvare inntekten du ville hatt som frisk på skadetidspunktet." },
                        nynorsk { +"Skadetidspunktet ditt har vi fastsett til " + skadetidspunkt.format() + ". Den årlege inntekta på dette tidspunktet er fastsett til " + inntektSkadetidspunkt.format() + ". Vi har justert denne inntekta opp fordi den skal svare til inntekta du ville hatt som frisk på skadetidspunktet." },
                    )
                }
            }

            showIf((yrkesskaderesultat.equalTo(VilkarResultat.IKKE_OPPFYLT) and yrkesskadebegrunnelse.equalTo(YrkesskadeBegrunnelse.STDBEGR_12_17_1_I_2))) {
                paragraph(uniqueness = "12_17_1_i_2") {
                    text(
                        bokmal { +"Du har en godkjent yrkesskade eller yrkessykdom. Vi har ut fra sakens opplysninger vurdert om yrkesskaden eller yrkessykdommen er årsak til den økte uførheten din." },
                        nynorsk { +"Du har ein godkjend yrkesskade eller yrkessjukdom. Vi har ut frå opplysningane i saka vurdert om yrkesskaden eller yrkessjukdommen din er årsak til den auka uførleiken din." },
                    )
                }
                paragraph(uniqueness = "12_17_1_i_2") {
                    text(
                        bokmal { +"Vi har kommet fram til at det er andre sykdomsforhold som er årsak til den økte uførheten din. " + fritekst("Konkret begrunnelse") + "" },
                        nynorsk { +"Vi har kome fram til at det er andre sjukdomsforhold som er årsak til den auka uførleiken din. " + fritekst("Konkret begrunnelse") + "" },
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad > 0 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagYrkesskadeBest = true AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeBegrunnelse) = "stdbegr_12_17_1_o_3" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat) = "oppfylt" AND PE_Vedtaksbrev_Vedtaksdata_Kravhode_BrukerKonvertertUP = false) THEN      INCLUDE ENDIF
            showIf((yrkesskadegradFraBeregning.lessThan(uforegradFraBeregning) and yrkesskadegradFraBeregning.greaterThan(0) and yrkesskadebest and (yrkesskadebegrunnelse).equalTo(YrkesskadeBegrunnelse.STDBEGR_12_17_1_O_3) and (yrkesskaderesultat).equalTo(VilkarResultat.OPPFYLT) and not(konvertertup))) {
                paragraph {
                    text(
                        bokmal { +"Deler av uføretrygden din vil fortsatt bli beregnet etter særbestemmelser, noe som gir deg en høyere uføretrygd." },
                        nynorsk { +"Deler av uføretrygda di vil fortsatt bli rekna ut etter særreglar, noko som gir deg ei høgare uføretrygd." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad > 0 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagYrkesskadeBest = false AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeBegrunnelse) = "stdbegr_12_17_1_o_3" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat) = "oppfylt" AND PE_Vedtaksbrev_Vedtaksdata_Kravhode_BrukerKonvertertUP = false) THEN      INCLUDE ENDIF
            showIf((yrkesskadegradFraBeregning.lessThan(uforegradFraBeregning) and yrkesskadegradFraBeregning.greaterThan(0) and not(yrkesskadebest) and (yrkesskadebegrunnelse).equalTo(YrkesskadeBegrunnelse.STDBEGR_12_17_1_O_3) and (yrkesskaderesultat).equalTo(VilkarResultat.OPPFYLT) and not(konvertertup))) {
                //[TBU3732NN, TBU3732]

                paragraph {
                    text(
                        bokmal { +"Deler av uføretrygden din vil fortsatt bli beregnet etter særbestemmelser, dersom dette er til fordel for deg." },
                        nynorsk { +"Deler av uføretrygda di vil fortsatt bli rekna ut etter særreglar, dersom dette er til fordel for deg." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad > 0 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagYrkesskadeBest = false AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeBegrunnelse) = "stdbegr_12_17_1_o_3" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat) = "oppfylt") THEN      INCLUDE ENDIF
            showIf((yrkesskadegradFraBeregning.lessThan(uforegradFraBeregning) and yrkesskadegradFraBeregning.greaterThan(0) and not(yrkesskadebest) and (yrkesskadebegrunnelse).equalTo(YrkesskadeBegrunnelse.STDBEGR_12_17_1_O_3) and (yrkesskaderesultat).equalTo(VilkarResultat.OPPFYLT))) {
                paragraph(uniqueness = "12_17_1_o_3") {
                    text(
                        bokmal { +"Inntekten din på skadetidspunktet er lavere enn beregningsgrunnlaget ditt, og uføretrygden din vil derfor ikke bli beregnet etter særbestemmelser for yrkesskade eller yrkessykdom." },
                        nynorsk { +"Inntekta di på skadetidspunktet er lågare enn berekningsgrunnlaget ditt, og uføretrygda di blir derfor ikkje berekna etter særreglar for yrkesskade eller yrkessjukdom." },
                    )
                }
            }

            showIf(((yrkesskaderesultat).equalTo(VilkarResultat.IKKE_OPPFYLT) and (yrkesskadebegrunnelse).equalTo(YrkesskadeBegrunnelse.STDBEGR_12_17_1_I_1))) {
                paragraph {
                    text(
                        bokmal { +"Du har ikke en godkjent yrkesskade eller yrkessykdom. Uføretrygden din vil derfor ikke bli beregnet etter særbestemmelser for yrkesskade eller yrkessykdom. " + fritekst("Konkret begrunnelse") + "." },
                        nynorsk { +"Du har ikkje ein godkjend yrkesskade eller yrkessjukdom. Uføretrygda di blir derfor ikkje innvilga etter reglar for yrkesskade eller yrkessjukdom. " + fritekst("Konkret begrunnelse") + "." },
                    )
                }
            }

            title1 {
                text(
                    bokmal { +"Dette er virkningstidspunktet ditt" },
                    nynorsk { +"Dette er verknadstidspunktet ditt" },
                )
            }
            showIf(((nedsattInntektsevneResultat).equalTo(VilkarResultat.OPPFYLT) and (virkningBegrunnelse).equalTo(VirkningstidspunktBegrunnelse.STDBEGR_22_12_1_1))) {
                paragraph {
                    text(
                        bokmal { +"Du har fått innvilget økt uføretrygd fra " + vedtakvirkfom.format() + ". Dette kaller vi virkningstidspunktet. Fram til dette vil du få arbeidsavklaringspenger." },
                        nynorsk { +"Du har fått innvilga auka uføretrygd frå " + vedtakvirkfom.format() + ". Dette kallar vi verknadstidspunktet. Fram til dette kjem du til å få arbeidsavklaringspengar." },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Du må huske å sende meldekort ut inneværende måned." },
                        nynorsk { +"Du må hugse å sende meldekort ut inneverande månad." },
                    )
                }
            }

            showIf(((nedsattInntektsevneResultat).equalTo(VilkarResultat.OPPFYLT) and (virkningBegrunnelse).equalTo(VirkningstidspunktBegrunnelse.STDBEGR_22_12_1_2))) {
                paragraph {
                    text(
                        bokmal { +"Du har fått innvilget økt uføretrygd fra " + vedtakvirkfom.format() + ". Dette kaller vi virkningstidspunktet. Arbeidsavklaringspengene utbetales fram til " + fritekst("dato for opphør") + " og uføretrygd utbetales for de gjenstående dagene i " + mnVirkningstidspunkt.format() + "." },
                        nynorsk { +"Du har fått innvilga auka uføretrygd frå " + vedtakvirkfom.format() + ". Dette kallar vi verknadstidspunktet. Arbeidsavklaringspengane blir betalte ut fram til " + fritekst("Dato for opphør") + ", og uføretrygd blir betalt ut for dei dagane som er att i " + mnVirkningstidspunkt.format() + "." },
                    )
                }
            }

            showIf(((virkningBegrunnelse).equalTo(VirkningstidspunktBegrunnelse.STDBEGR_22_12_1_3))) {
                paragraph {
                    text(
                        bokmal { +"Du har fått innvilget økt uføretrygd fra " + vedtakvirkfom.format() + ". Dette kaller vi virkningstidspunktet. Du vil få sykepenger fram til " + fritekst("dato for opphør") + ". I denne måneden får du utbetalt den delen av sykepengene med det beløp som overstiger uføretrygden." },
                        nynorsk { +"Du har fått innvilga auka uføretrygd frå " + vedtakvirkfom.format() + ". Dette kallar vi verknadstidspunktet. Du får sjukepengar fram til " + fritekst("Dato for opphør") + ". I denne månaden får du utbetalt den delen av sjukepengane som overstig uføretrygda." },
                    )
                }
            }

            showIf(((virkningBegrunnelse).equalTo(VirkningstidspunktBegrunnelse.STDBEGR_22_12_1_4))) {
                paragraph {
                    text(
                        bokmal { +"Du har fått innvilget økt uføretrygd fra " + vedtakvirkfom.format() + ". Dette kaller vi virkningstidspunktet. For å ha rett til uføretrygd må du ha fylt 18 år. Du får derfor uføretrygd fra måneden etter at du fyller 18 år." },
                        nynorsk { +"Du har fått innvilga auka uføretrygd frå " + vedtakvirkfom.format() + ". Dette kallar vi verknadstidspunktet. For å ha rett til uføretrygd må du ha fylt 18 år. Du får derfor uføretrygd frå månaden etter at du fyller 18 år." },
                    )
                }
            }

            showIf(((virkningBegrunnelse).equalTo(VirkningstidspunktBegrunnelse.STDBEGR_22_12_1_5))) {
                paragraph {
                    text(
                        bokmal { +"Du har fått innvilget økt uføretrygd fra " + vedtakvirkfom.format() + ". Dette kaller vi virkningstidspunktet. Ditt krav ble framsatt " + pesysData.kravFremsattDato.ifNull(kravMotattDato).format() + ". Dersom vilkårene for rett til uføretrygd var oppfylt før dette, kan uføretrygden innvilges opptil tre måneder før denne datoen. " + fritekst("Fritekst") },
                        nynorsk { +"Du har fått innvilga auka uføretrygd frå " + vedtakvirkfom.format() + ". Dette kallar vi verknadstidspunktet. Kravet ditt blei framsatt " + pesysData.kravFremsattDato.ifNull(kravMotattDato).format() + ". Dersom vilkåra for rett til uføretrygd var oppfylte før dette, kan vi innvilge uføretrygd opptil tre månader før denne datoen. " + fritekst("Fritekst") },
                    )
                }
            }

            showIf(((virkningBegrunnelse).equalTo(VirkningstidspunktBegrunnelse.STDBEGR_22_12_1_13))) {
                paragraph {
                    text(
                        bokmal { +"Du har fått innvilget økt uføretrygd fra " + vedtakvirkfom.format() + ". Dette kaller vi virkningstidspunktet. Du har rett til uføretrygd fra den måneden vilkårene er oppfylt." },
                        nynorsk { +"Du har fått innvilga auka uføretrygd frå " + vedtakvirkfom.format() + ". Dette kallar vi verknadstidpunktet. Du har rett til uføretrygd frå den månaden vilkåra er oppfylde." },
                    )
                }
            }

            title1 {
                text(
                    bokmal { +"Dette er uføretidspunktet ditt" },
                    nynorsk { +"Dette er uføretidspunktet ditt" },
                )
            }

            paragraph {
                text(
                    bokmal { +"Vi har tidligere fastsatt uføretidspunktet ditt til " + pesysData.forrigeUforetidspunkt.formatMonthYear() + ". Når uføregraden øker, fastsetter vi et nytt uføretidspunkt. Det nye uføretidspunktet ditt er " + uforetidspunkt.formatMonthYear() + "." },
                    nynorsk { +"Vi har tidlegare fastsett uføretidspunktet ditt til " + pesysData.forrigeUforetidspunkt.formatMonthYear() + ". Når uføregraden aukar, fastset vi eit nytt uføretidspunkt. Det nye uføretidspunktet ditt er " + uforetidspunkt.formatMonthYear() + "." },
                )
            }

            paragraph {
                text(
                    bokmal { +"Vi sammenligner beregningen av uføretrygden ut fra det tidspunktet som først ble fastsatt, og det nye uføretidspunktet. Du får det alternativet som gir deg høyest uføretrygd. Det er uføretidspunktet som bestemmer hvilke inntektsår vi legger til grunn for beregningen din." },
                    nynorsk { +"Vi samanliknar berekninga av uføretrygda ut frå det tidspunktet som først blei fastsett, og det nye uføretidspunktet. Du får det alternativet som gir deg høgast uføretrygd. Det er uføretidspunktet som bestemmer kva inntektsår vi legg til grunn for berekninga di." },
                )
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforetidspunkt <> FF_GetArrayElement_Date(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Uforetidspunkt)) THEN      INCLUDE ENDIF
            showIf((beregningUforetidspunkt.notEqualTo((uforetidspunkt)))) {
                paragraph {
                    text(
                        bokmal { +"Du får fortsatt beregnet uføretrygden din ut fra tidligere uføretidspunkt, fordi dette gir deg en høyere uføretrygd." },
                        nynorsk { +"Du får framleis rekna ut uføretrygda di ut frå tidlegare uføretidspunkt, fordi dette gir deg ei høgare uføretrygd." },
                    )
                }
            }

            //IF(DateDiff("d", PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforetidspunkt,   FF_GetArrayElement_Date(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Uforetidspunkt)) = 0) THEN      INCLUDE ENDIF
            showIf((beregningUforetidspunkt.equalTo(uforetidspunkt))) {
                paragraph {
                    text(
                        bokmal { +"Du får beregnet uføretrygden din ut fra nytt uføretidspunkt, fordi dette gir deg en høyere uføretrygd." },
                        nynorsk { +"Du får rekna ut uføretrygda di ut frå nytt uføretidspunkt, fordi dette gir deg ei høgare uføretrygd." },
                    )
                }
            }

            title1 {
                text(
                    bokmal { +"Slik har vi fastsatt uføregraden din" },
                    nynorsk { +"Slik har vi fastsett uføregraden din" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Vi har sammenliknet inntektsmulighetene dine før og etter at du ble ufør, og vurdert hvor mye inntektsevnen din er varig nedsatt." },
                    nynorsk { +"Vi har samanlikna inntektsmoglegheitene dine før og etter at du blei ufør, og vurdert kor mykje inntektsevna di er varig nedsett." },
                )
            }

            showIf(((ifubegrunnelse).equalTo(InntektForUforhetBegrunnelse.STDBEGR_12_8_2_1) and (ieuBegrunnelse).notEqualTo(InntektEtterUforhetBegrunnelse.STDBEGR_12_8_1_3))) {
                paragraph {
                    text(
                        bokmal { +"Inntekten din før du ble ufør er fastsatt til " + ifuinntekt.format() + " på uføretidspunktet " + uforetidspunkt.formatMonthYear() + ". " + fritekst("begrunnelse for fastsatt IFU") + ". " },
                        nynorsk { +"Inntekta di før du blei ufør, er fastsett til " + ifuinntekt.format() + " på uføretidspunktet " + uforetidspunkt.formatMonthYear() + ". " + fritekst("begrunnelse for fastsatt IFU") + ". " },
                    )
                    showIf(oifuMerEnnIfu) {
                        text(
                            bokmal { +"Oppjustert til virkningstidspunktet tilsvarer dette en inntekt på " + oifuKroner.format() + "." },
                            nynorsk { +"Oppjustert til verknadstidspunktet svarer dette til ei inntekt på " + oifuKroner.format() + "." },
                        )
                    }
                }
            }

            showIf(((ifubegrunnelse).equalTo(InntektForUforhetBegrunnelse.STDBEGR_12_8_2_3) and (ieuBegrunnelse).notEqualTo(InntektEtterUforhetBegrunnelse.STDBEGR_12_8_1_3))) {
                paragraph {
                    text(
                        bokmal { +"Du hadde begrenset yrkesaktivitet og inntekt før du ble ufør. Inntekten din før du ble ufør er fastsatt til " + ifuinntekt.format() + " på uføretidspunktet " + uforetidspunkt.formatMonthYear() + ". Dette er for å garantere deg et minstenivå på inntekt før uførhet. Dette minstenivået skal tilsvare 3,3 ganger grunnbeløpet." },
                        nynorsk { +"Du hadde avgrensa yrkesaktivitet og inntekt før du blei ufør. Inntekta di før du blei ufør er fastsett til " + ifuinntekt.format() + " på uføretidspunktet " + uforetidspunkt.formatMonthYear() + ". Dette er for å garantere deg eit minstenivå på inntekt før uførleik. Dette minstenivået skal svare til 3,3 gonger grunnbeløpet." },
                    )
                    showIf(oifuMerEnnIfu) {
                        text(
                            bokmal { +" Oppjustert til virkningstidspunktet tilsvarer dette en inntekt på " + oifuKroner.format() + "." },
                            nynorsk { +" Oppjustert til verknadstidspunktet svarer dette til ei inntekt på " + oifuKroner.format() + "." },
                        )
                    }
                }
            }

            showIf(((ifubegrunnelse).equalTo(InntektForUforhetBegrunnelse.STDBEGR_12_8_2_5) and (ieuBegrunnelse).notEqualTo(InntektEtterUforhetBegrunnelse.STDBEGR_12_8_1_3))) {
                paragraph {
                    text(
                        bokmal { +"Du hadde begrenset yrkesaktivitet og inntekt før du ble ufør. Inntekten din før du ble ufør er fastsatt til " + ifuinntekt.format() + " på uføretidspunktet " + uforetidspunkt.formatMonthYear() + ". Dette er for å garantere deg et minstenivå på inntekt før uførhet. Dette minstenivået skal tilsvare 3,5 ganger folketrygdens grunnbeløp." },
                        nynorsk { +"Du hadde avgrensa yrkesaktivitet og inntekt før du blei ufør. Inntekta di før du blei ufør er fastsett til " + ifuinntekt.format() + " på uføretidspunktet " + uforetidspunkt.formatMonthYear() + ". Dette er for å garantere deg eit minstenivå på inntekt før uførleik. Dette minstenivået skal svare til 3,5 gonger grunnbeløpet." },
                    )
                    showIf(oifuMerEnnIfu) {
                        text(
                            bokmal { +" Oppjustert til virkningstidspunktet tilsvarer dette en inntekt på " + oifuKroner.format() + "." },
                            nynorsk { +" Oppjustert til verknadstidspunktet svarer dette til ei inntekt på " + oifuKroner.format() + "." },
                        )
                    }
                }
            }

            //IF((FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_UngUforResultat,1) = true) AND (FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse,1) = "stdbegr_12_8_2_4")  AND (FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUBegrunnelse,1) <> "stdbegr_12_8_1_3"))  THEN      INCLUDE ENDIF
            showIf((unguforeresultat.equalTo(VilkarResultat.OPPFYLT)) and (ifubegrunnelse.equalTo(InntektForUforhetBegrunnelse.STDBEGR_12_8_2_4)) and (ieuBegrunnelse.notEqualTo(InntektEtterUforhetBegrunnelse.STDBEGR_12_8_1_3))) {
                paragraph {
                    text(
                        bokmal { +"Inntekten din før du ble ufør er fastsatt til " + ifuinntekt.format() + ". Vi har innvilget deg rettighet som ung ufør, og inntekten din før du ble ufør skal derfor tilsvare minst 4,5 ganger grunnbeløpet." },
                        nynorsk { +"Inntekta di før du blei ufør, er fastsett til " + ifuinntekt.format() + ". Vi har innvilga deg rettar som ung ufør, og inntekta di før du blei ufør skal derfor svare til minst 4,5 gonger grunnbeløpet." },
                    )
                    showIf(oifuMerEnnIfu) {
                        text(
                            bokmal { +" Oppjustert til virkningstidspunktet tilsvarer dette en inntekt på " + oifuKroner.format() + "." },
                            nynorsk { +" Oppjustert til verknadstidspunktet svarer dette til ei inntekt på " + oifuKroner.format() + "." },
                        )
                    }
                }
            }

            //IF((FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse) <> "" OR FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUBegrunnelse) <> "") AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad = 100  AND FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUInntekt) = 0  AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUBegrunnelse) <> "stdbegr_12_8_1_3"  AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse) <> "stdbegr_12_8_2_2"  AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse) <> "stdbegr_12_8_2_9"  ) THEN      INCLUDE ENDIF
            showIf((((ifubegrunnelse).notEqualTo(null) or (ieuBegrunnelse).notEqualTo(null)) and uforegradFraBeregning.equalTo(100) and (ieuinntekt).equalTo(0) and (ieuBegrunnelse).notEqualTo(InntektEtterUforhetBegrunnelse.STDBEGR_12_8_1_3) and (ifubegrunnelse).notEqualTo(InntektForUforhetBegrunnelse.STDBEGR_12_8_2_2) and (ifubegrunnelse).notEqualTo(InntektForUforhetBegrunnelse.STDBEGR_12_8_2_9))) {
                paragraph {
                    text(
                        bokmal { +"Du har ikke inntekt i dag, og vi har derfor fastsatt uføregraden din til " + uforegradFraBeregning.format() + " prosent." },
                        nynorsk { +"Du har ikkje inntekt i dag, og vi har derfor fastsett uføregraden din til " + uforegradFraBeregning.format() + " prosent." },
                    )
                }
            }

            //IF( ( FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse) <> "" OR FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUBegrunnelse) <> "" ) AND ((PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad > 0 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad < 100) OR ( PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad = 100 AND FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUInntekt) > 0 )) AND  FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUBegrunnelse) <> "stdbegr_12_8_1_3" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse) <> "stdbegr_12_8_2_2"  AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse) <> "stdbegr_12_8_2_9" ) THEN      INCLUDE ENDIF
            showIf((((ifubegrunnelse).notEqualTo(null) or (ieuBegrunnelse).notEqualTo(null)) and ((uforegradFraBeregning.greaterThan(0) and uforegradFraBeregning.lessThan(100)) or (uforegradFraBeregning.equalTo(100) and (ieuinntekt).greaterThan(0))) and (ieuBegrunnelse).notEqualTo(InntektEtterUforhetBegrunnelse.STDBEGR_12_8_1_3) and (ifubegrunnelse).notEqualTo(InntektForUforhetBegrunnelse.STDBEGR_12_8_2_2) and (ifubegrunnelse).notEqualTo(InntektForUforhetBegrunnelse.STDBEGR_12_8_2_9))) {
                paragraph {
                    text(
                        bokmal { +"Du har en inntekt på " + ieuinntekt.format() + ", og vi har derfor fastsatt uføregraden din til " + uforegradFraBeregning.format() + " prosent." },
                        nynorsk { +"Du har ei inntekt på " + ieuinntekt.format() + ", og vi har derfor fastsett uføregraden din til " + uforegradFraBeregning.format() + " prosent." },
                    )
                }
            }

            showIf((ieuBegrunnelse.equalTo(InntektEtterUforhetBegrunnelse.STDBEGR_12_8_1_3))) {
                paragraph {
                    text(
                        bokmal { +"Vi har fastsatt inntekten din før du ble ufør til " + ifuinntekt.format() + " på uføretidspunktet " + uforetidspunkt.formatMonthYear() + ". " + fritekst("begrunnelse for fastsatt IFU") + "." },
                        nynorsk { +"Vi har fastsett inntekta di før du blei ufør til " + ifuinntekt.format() + " på uføretidspunktet " + uforetidspunkt.formatMonthYear() + ". " + fritekst("begrunnelse for fastsatt IFU") + "." },
                    )
                    showIf(oifuMerEnnIfu) {
                        text(
                            bokmal { +" Oppjustert til virkningstidspunktet tilsvarer dette en inntekt på " + oifuKroner.format() + "." },
                            nynorsk { +" Oppjustert til verknadstidspunktet svarer dette til ei inntekt på " + oifuKroner.format() + "." },
                        )
                    }
                }
                paragraph {
                    text(
                        bokmal { +"Det er dokumentert at du har inntektsmuligheter som du ikke benytter. Disse tar vi med når vi fastsetter inntekten din etter at du ble ufør. Inntekten din etter at du ble ufør er fastsatt til " + ieuinntekt.format() + ", og uføregraden din er derfor fastsatt til " + uforegradFraBeregning.format() + " prosent." },
                        nynorsk { +"Det er dokumentert at du har inntektsmoglegheiter som du ikkje nyttar. Desse tek vi med når vi fastset inntekta di etter at du blei ufør. Inntekta di etter at du blei ufør, er fastsett til " + ieuinntekt.format() + ", og uføregraden din er derfor fastsett til " + uforegradFraBeregning.format() + " prosent." },
                    )
                }
            }

            showIf(((ifubegrunnelse).equalTo(InntektForUforhetBegrunnelse.STDBEGR_12_8_2_2) or (ifubegrunnelse).equalTo(InntektForUforhetBegrunnelse.STDBEGR_12_8_2_9))) {
                paragraph {
                    text(
                        bokmal { +"Vi har fastsatt inntekten din før du ble ufør til " + ifuinntekt.format() + ". Inntekten din før du ble ufør er fastsatt ut fra stillingsandelen din, og forventet inntekt på " + ieuinntekt.format() + ". Inntekten din etter at du ble ufør er derfor fastsatt til " + ieuinntekt.format() + " og uføregraden din blir " + uforegradFraBeregning.format() + " prosent." },
                        nynorsk { +"Vi har fastsett inntekta di før du blei ufør til " + ifuinntekt.format() + ". Inntekta di før du blei ufør, er fastsett ut frå stillingsdelen din og forventa inntekt på " + ieuinntekt.format() + ". Inntekta di etter at du blei ufør, er derfor fastsett til " + ieuinntekt.format() + ", og uføregraden din blir " + uforegradFraBeregning.format() + " prosent." },
                    )
                }
            }
            paragraph(uniqueness = "beregning") {
                text(
                    bokmal { +"Du kan lese mer om dette i vedlegget " },
                    nynorsk { +"Du kan lese meir om dette i vedlegget " },
                )
                namedReference(vedleggOpplysningerBruktIBeregningUTLegacy)
                text(bokmal { +"." }, nynorsk { +"." })
            }

            showIf(uforegradFraBeregning.equalTo(100)) {
                title1 {
                    text(
                        bokmal { +"Skal du kombinere uføretrygd og inntekt?" },
                        nynorsk { +"Skal du kombinere uføretrygd og inntekt?" },
                    )
                }
            }.orShow {
                title1 {
                    text(
                        bokmal { +"For deg som kombinerer uføretrygd og inntekt" },
                        nynorsk { +"For deg som kombinerer uføretrygd og inntekt" },
                    )
                }
            }

            paragraph {
                text(
                    bokmal { +"Du har mulighet til å ha inntekt ved siden av uføretrygden din. " },
                    nynorsk { +"Det er mogleg for deg å ha inntekt ved sida av uføretrygda di. " },
                )
            }
            showIf(!pesysData.harVTA) {
                paragraph {
                    text(
                        bokmal { +"Fra " + virkningstidspunkt.format() + " vil du få en ny venteperiode på 2 år med et fribeløp på 40 prosent av folketrygdens grunnbeløp (G), før fribeløpet igjen vil øke til 1 G. Fribeløpet kan du tjene" },
                        nynorsk { +"Frå " + virkningstidspunkt.format() + " vil du få ein ny venteperiode på 2 år med eit fribeløp på 40 prosent av folketrygdens grunnbeløp (G), før fribeløpet igjen vil auke til 1 G. Fribeløpet kan du tene" },
                    )
                    showIf(uforegradFraBeregning.notEqualTo(100)) {
                        text(
                            bokmal { +" i tillegg til oppjustert inntekt etter uførhet," },
                            nynorsk { +" i tillegg til oppjustert inntekt etter uførleik," },
                        )
                    }
                    text(
                        bokmal { +" uten at uføretrygden din blir redusert. Dette heter bunnfradrag. " },
                        nynorsk { +" utan at uføretrygda di blir redusert. Dette heiter botnfrådrag. " },
                    )
                    showIf(pesysData.vedtaksinfo.vedtakFattetDatoEllerIdag.month.notEqualTo(Month.DECEMBER.value)) {
                        text(
                            bokmal { +"Selv om fribeløpet endrer seg beholder du det nåværende bunnfradraget ditt ut inneværende kalenderår. Det nåværende bunnfradraget ditt er " + bunnfradrag.format() + ". " },
                            nynorsk { +"Sjølv om fribeløpet endrar seg, beheld du det noverande botnfrådraget ditt ut inneverande kalenderår. Det noverande botnfrådraget ditt er " + bunnfradrag.format() + ". " },
                        )
                    }.orShow {
                        text(
                            bokmal { +"Bunnfradraget ditt neste år er " + bunnfradragFaktisk.format() + ". " },
                            nynorsk { +"Botnfrådraget ditt neste år er " + bunnfradragFaktisk.format() + ". " },
                        )
                    }
                }
                showIf(pesysData.fribelopsperioder.isNotEmpty()) {
                    paragraph {
                        text(
                            bokmal { +"Slik beregner vi fribeløpet ditt i år: " },
                            nynorsk { +"Slik reknar vi ut fribeløpet ditt i år: " },
                        )
                    }
                    includePhrase(Fribelopsperioder(pesysData.fribelopsperioder, pesysData.fribelop))
                }
            }.orShow {
                showIf(pesysData.harVTA) {
                    paragraph {
                        text(
                            bokmal { +"Fordi du er i varig tilrettelagt arbeid kan du ha en årlig inntekt på folketrygdens grunnbeløp uten at uføretrygden din blir redusert. I dag er dette " + bunnfradrag.format() + ". Dette er bunnfradraget ditt." },
                            nynorsk { +"Fordi du er i varig tilrettelagt arbeid kan du ha ei årleg inntekt på folketrygdens grunnbeløp utan at uføretrygda di blir redusert. I dag er dette " + bunnfradrag.format() + ". Dette er botnfrådraget ditt." },
                        )
                    }
                }
            }
            paragraph {
                text(
                    bokmal { +"Vi bruker en fastsatt prosentandel når vi justerer uføretrygden din ut fra inntekt. Denne prosentandelen kaller vi reduksjonsprosent." },
                    nynorsk { +"Vi bruker ein fastsett prosentdel når vi justerer uføretrygda di ut frå inntekt. Denne prosentdelen kallar vi reduksjonsprosent. " },
                )
            }


            paragraph {
                text(
                    bokmal {
                        +"For deg utgjør reduksjonsprosenten " + reduksjonsprosent.format() + " prosent. Det er bare den delen av inntekten din som overstiger bunnfradraget ditt "
                        +"vi justerer uføretrygden din ut fra. Det betyr at et beløp som tilsvarer " + reduksjonsprosent.format() + " prosent av den inntekten du har over bunnfradraget ditt trekkes fra uføretrygden din."
                    },
                    nynorsk {
                        +"For deg utgjer reduksjonsprosenten " + reduksjonsprosent.format() + " prosent. Det er berre den delen av inntekta di som overstig botnfrådraget ditt "
                        +"vi justerer uføretrygda di ut frå. Det betyr at eit beløp som svarer til " + reduksjonsprosent.format() + " prosent av inntekta du har over botnfrådraget ditt blir trekt frå uføretrygda di."
                    },
                )
            }
            paragraph {
                text(
                    bokmal { +"Blir uføretrygden din redusert på grunn av inntekt beholder du likevel uføregraden din på " + uforegradFraBeregning.format() + " prosent. Du får utbetalt hele uføretrygden igjen dersom du tjener mindre enn bunnfradraget ditt." },
                    nynorsk { +"Blir uføretrygda di redusert på grunn av inntekt beheld du likevel uføregraden din på " + uforegradFraBeregning.format() + " prosent. Du får utbetalt heile uføretrygda att dersom du tener mindre enn botnfrådraget ditt." },
                )
            }

            showIf(bostedutland) {
                paragraph {
                    text(
                        bokmal { +"Bunnfradraget gjelder bare for den norske uføretrygden din. Har du spørsmål om bunnfradraget i et annet land, må du kontakte trygdemyndighetene i det landet det gjelder." },
                        nynorsk { +"Botnfrådraget gjeld berre for den norske uføretrygda di. Har du spørsmål om botnfrådraget i eit anna land, må du kontakte trygdestyresmaktene i det landet det gjeld." },
                    )
                }
            }

            showIf(instoppholdtype.equalTo(JustertPeriodeCode.REDUKSJON_HS)) {
                title1 {
                    text(
                        bokmal { +"Utbetaling av uføretrygd for deg som er innlagt på institusjon" },
                        nynorsk { +"Utbetaling av uføretrygd når du er innlagd på institusjon" },
                    )
                }
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs"  AND  (PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = true  OR  PE_UT_Forsorgeransvar_ingen_er_false() OR  PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_InstOpphFasteUtgifterperiodeListe_InstOpphFasteUtgifterperiode_FasteUtgifter = true)) THEN      INCLUDE ENDIF
            showIf((instoppholdtype.equalTo(JustertPeriodeCode.REDUKSJON_HS) and (instopphanvendt or forsorgeransvarAllePerioder or fasteUtgifterInst.notEqualTo(Kroner(0))))) {
                paragraph {
                    text(
                        bokmal { +"Vi reduserer ikke utbetalingen av uføretrygden din verken i innleggelsesmåneden eller de tre påfølgende månedene når du er innlagt på institusjon. Uføretrygden din blir deretter redusert og skal under oppholdet utgjøre 14 prosent av uføretrygden inntil institusjonsoppholdet avsluttes. Totalt sett skal utbetalingen likevel utgjøre minst 45 prosent av folketrygdens grunnbeløp, før skatt. " },
                        nynorsk { +"Vi reduserer ikkje utbetalinga av uføretrygda di verken i innleggingsmånaden eller dei tre påfølgjande månadene når du er innlagd på institusjon. Uføretrygda di blir deretter redusert og skal under opphaldet utgjere 14 prosent av uføretrygda inntil institusjonsopphaldet blir avslutta. Totalt sett skal utbetalinga likevel utgjere minst 45 prosent av grunnbeløpet i folketrygda før skatt. " },
                    )

                    showIf((ektefelletilleggInnvilget)) {
                        text(
                            bokmal { +"Dersom du mottar ektefelletillegg vil dette tillegget også bli redusert." },
                            nynorsk { +"Dersom du får ektefelletillegg vil dette tillegget også bli redusert." },
                        )
                    }.orShowIf((gjenlevendetilleggInnvilget)) {
                        text(
                            bokmal { +"Dersom du mottar gjenlevendetillegg vil dette tillegget også bli redusert." },
                            nynorsk { +"Dersom du får attlevandetillegg vil dette tillegget også bli redusert." },
                        )
                    }
                }
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs"  AND  (PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = true  OR  PE_UT_Forsorgeransvar_ingen_er_false() OR  PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_InstOpphFasteUtgifterperiodeListe_InstOpphFasteUtgifterperiode_FasteUtgifter = true)) THEN      INCLUDE ENDIF
            showIf((instoppholdtype.equalTo(JustertPeriodeCode.REDUKSJON_HS) and (instopphanvendt or forsorgeransvarAllePerioder or fasteUtgifterInst.notEqualTo(Kroner(0))))) {
                paragraph {
                    text(
                        bokmal { +"Dersom du har faste og nødvendige utgifter til bolig, kan vi vurdere om uføretrygden din kan reduseres mindre. Du må sende inn dokumentasjon på dine utgifter til Nav. Forsørger du barn" + txtOgEllerEktefelle + " under innleggelsen på institusjonen, vil vi ikke redusere uføretrygden din." },
                        nynorsk { +"Dersom du har faste og nødvendige utgifter til bustad, vil vi vurdere en lågare reduksjon av uføretrygda di. Du må sende inn dokumentasjon på utgiftene dine til Nav. Viss du forsørgjer barn" + txtOgEllerEktefelle + " mens du er lagd inn på institusjonen, reduserer vi ikkje uføretrygda di." }
                    )
                }
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false AND PE_UT_Forsorgeransvar_ingen_er_true() AND PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_InstOpphFasteUtgifterperiodeListe_InstOpphFasteUtgifterperiode_FasteUtgifter = 0  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs") THEN      INCLUDE ENDIF
            showIf((not(instopphanvendt) and forsorgeransvarIngenPerioder and fasteUtgifterInst.equalTo(0) and instoppholdtype.equalTo(JustertPeriodeCode.REDUKSJON_HS))) {
                paragraph {
                    text(
                        bokmal { +"Uføretrygden din er lavere enn 45 prosent av folketrygdens grunnbeløp. Du vil derfor ikke få redusert utbetaling av uføretrygden din når du er innlagt på institusjon." },
                        nynorsk { +"Uføretrygda di er lågare enn 45 prosent av grunnbeløpet i folketrygda. Du får derfor ikkje redusert utbetaling av uføretrygda di når du er innlagd på institusjon." },
                    )
                }
            }

            //IF(PE_UT_Forsorgeransvar_ingen_er_false() AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs" AND PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_InstOpphFasteUtgifterperiodeListe_InstOpphFasteUtgifterperiode_FasteUtgifter = 0) THEN      INCLUDE ENDIF
            showIf((forsorgeransvarAllePerioder and instoppholdtype.equalTo(JustertPeriodeCode.REDUKSJON_HS) and fasteUtgifterInst.equalTo(0))) {
                paragraph {
                    text(
                        bokmal { +"Du forsørger barn" + txtOgEllerEktefelle + " under oppholdet ditt i institusjon. Vi har derfor kommet fram til at utbetalingen din ikke skal reduseres." },
                        nynorsk { +"Du forsørgjer barn" + txtOgEllerEktefelle + " under opphaldet ditt på institusjon. Vi har derfor kome fram til at utbetalinga di ikkje skal reduserast." },
                    )
                }
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false  AND PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_InstOpphFasteUtgifterperiodeListe_InstOpphFasteUtgifterperiode_FasteUtgifter <> 0  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs"  AND PE_UT_Forsorgeransvar_ingen_er_true()) THEN      INCLUDE ENDIF
            showIf((not(instopphanvendt) and fasteUtgifterInst.notEqualTo(0) and instoppholdtype.equalTo(JustertPeriodeCode.REDUKSJON_HS) and forsorgeransvarIngenPerioder)) {
                paragraph {
                    text(
                        bokmal { +"Du har dokumentert at du har faste og nødvendige utgifter til bolig under oppholdet ditt på institusjon. Vi har derfor kommet fram til at utbetalingen din ikke skal reduseres." },
                        nynorsk { +"Du har dokumentert at du har faste og nødvendige utgifter til bustad under opphaldet ditt på institusjon. Vi har kome fram til at utbetalinga di derfor ikkje skal reduserast." },
                    )
                }
            }

            //IF(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_InstOpphFasteUtgifterperiodeListe_InstOpphFasteUtgifterperiode_FasteUtgifter <> 0 AND PE_UT_Forsorgeransvar_ingen_er_false() AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs") THEN      INCLUDE ENDIF
            showIf((fasteUtgifterInst.notEqualTo(0) and forsorgeransvarAllePerioder and instoppholdtype.equalTo(JustertPeriodeCode.REDUKSJON_HS))) {
                paragraph {
                    text(
                        bokmal { +"Du har dokumentert at du har faste og nødvendige utgifter til bolig, og du forsørger barn" + txtOgEllerEktefelle + " under oppholdet ditt i institusjon. Vi har derfor kommet fram til at utbetalingen din ikke skal reduseres." },
                        nynorsk { +"Du har dokumentert at du har faste og nødvendige utgifter til bustad, og du forsørgjer barn" + txtOgEllerEktefelle + " under opphaldet ditt på institusjon. Vi har derfor kome fram til at utbetalinga di ikkje skal reduserast." },
                    )
                }
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = true AND PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_InstOpphFasteUtgifterperiodeListe_InstOpphFasteUtgifterperiode_FasteUtgifter <> 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs") THEN      INCLUDE ENDIF
            showIf((instopphanvendt and fasteUtgifterInst.notEqualTo(0) and instoppholdtype.equalTo(JustertPeriodeCode.REDUKSJON_HS))) {
                paragraph {
                    text(
                        bokmal { +"Du har dokumentert at du har faste og nødvendige utgifter til bolig under oppholdet ditt på institusjon. Vi har derfor kommet fram til at utbetalingen din skal reduseres til " + uforetotalnetto.format() + "." },
                        nynorsk { +"Du har dokumentert at du har faste og nødvendige utgifter til bustad under opphaldet ditt på institusjon. Vi har kome fram til at utbetalinga di skal reduserast til " + uforetotalnetto.format() + "." },
                    )
                }
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = true  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs"  AND PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_InstOpphFasteUtgifterperiodeListe_InstOpphFasteUtgifterperiode_FasteUtgifter = 0  AND PE_UT_Forsorgeransvar_ingen_er_true()) THEN      INCLUDE ENDIF
            showIf((instopphanvendt and instoppholdtype.equalTo(JustertPeriodeCode.REDUKSJON_HS) and fasteUtgifterInst.equalTo(0) and forsorgeransvarIngenPerioder)) {
                paragraph {
                    text(
                        bokmal { +"Du forsørger ikke barn " + txtOgEllerEktefelle + ", og det er ikke dokumentert at du har faste og nødvendige utgifter til bolig under oppholdet ditt på institusjon. Vi har derfor kommet fram til at uføretrygden din skal reduseres til " + uforetotalnetto.format() + "." },
                        nynorsk { +"Du forsørgjer ikkje barn " + txtOgEllerEktefelle + ", og det er ikkje dokumentert at du har faste og nødvendige utgifter til bustad under opphaldet ditt på institusjon. Vi har derfor kome fram til at uføretrygda di skal reduserast til " + uforetotalnetto.format() + "." },
                    )
                }
            }

            showIf(instoppholdtype.equalTo(JustertPeriodeCode.REDUKSJON_FO)) {
                includePhrase(Ufoeretrygd.Straffegjennomfoering(
                    ektefelletilleggInnvilget = ektefelletilleggInnvilget,
                    gjenlevendetilleggInnvilget = gjenlevendetilleggInnvilget,
                    sisteForsorgeransvarErTrue = instopphold.safe { sistePeriodeHarForsorgeransvar }.ifNull(false),
                    forsteForsorgeransvarErTrue = instopphold.safe { forstePeriodeHarForsorgeransvar}.ifNull(false),
                ))
            }

            showIf((btInnvilget)) {
                title1 {
                    text(
                        bokmal { +"Slik påvirker inntekt barnetillegget ditt " },
                        nynorsk { +"Slik verkar inntekt inn på barnetillegget ditt " },
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumUTBT > PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_GradertOppjustertIFU AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType <> "overgangsregler_2016" AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningVirkningDatoFom >= DateValue("01/01/2016")) THEN      INCLUDE ENDIF
            showIf((reduksjonsgrunnlagSumutbt.greaterThan(reduksjonsgrunnlagGradertoppjustertifu) and reduksjonsgrunnlagBarnetilleggRegelverkstype.notEqualTo(BarnetilleggRegelverkTypeCode.OVERGANGSREGLER_2016) and beregningVirkFom.legacyGreaterThanOrEqual(LocalDate.of(2016, 1, 1)))) {
                paragraph {
                    text(
                        bokmal { +"Uføretrygden og barnetillegget ditt kan til sammen ikke utgjøre mer enn 95 prosent av inntekten din før du ble ufør. 95 prosent av den inntekten du hadde før du ble ufør tilsvarer i dag en inntekt på " + reduksjonsgrunnlagGradertoppjustertifu.format() + ". Uføretrygden og barnetillegget ditt er til sammen høyere enn dette. Årlig barnetillegg før reduksjon ut fra inntekt blir " },
                        nynorsk { +"Uføretrygda di og barnetillegget ditt kan til saman ikkje utgjere meir enn 95 prosent av inntekta di før du blei ufør. 95 prosent av den inntekta du hadde før du blei ufør, tilsvarer i dag ei inntekt på " + reduksjonsgrunnlagGradertoppjustertifu.format() + ". Uføretrygda di og barnetillegget ditt er til saman høgare enn dette. Brutto årleg barnetillegg før reduksjon ut frå inntekt blir " },
                    )

                    //IF( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoForReduksjonBT > PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoEtterReduksjonBT AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoEtterReduksjonBT > 0) THEN     INCLUDE END IF
                    showIf((reduksjonsgrunnlagSumbruttoforreduksjonbt.greaterThan(reduksjonsgrunnlagSumbruttoetterreduksjonbt) and reduksjonsgrunnlagSumbruttoetterreduksjonbt.greaterThan(0))) {
                        text(
                            bokmal { +"derfor redusert til " + reduksjonsgrunnlagSumbruttoetterreduksjonbt.format() + ". " },
                            nynorsk { +"derfor redusert til " + reduksjonsgrunnlagSumbruttoetterreduksjonbt.format() + ". " },
                        )
                    }

                    showIf((reduksjonsgrunnlagSumbruttoforreduksjonbt.greaterThan(0) and reduksjonsgrunnlagSumbruttoetterreduksjonbt.equalTo(0))) {
                        text(
                            bokmal { +"ikke utbetalt. " },
                            nynorsk { +"ikkje utbetalt. " },
                        )
                    }

                    //IF( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoForReduksjonBT > PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoEtterReduksjonBT AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoEtterReduksjonBT > 0) THEN     INCLUDE END IF
                    showIf((reduksjonsgrunnlagSumbruttoforreduksjonbt.greaterThan(reduksjonsgrunnlagSumbruttoetterreduksjonbt) and reduksjonsgrunnlagSumbruttoetterreduksjonbt.greaterThan(0))) {
                        text(
                            bokmal { +"Har du inntekt ved siden av uføretrygden vil dette også kunne ha betydning for størrelsen på barnetillegget ditt." },
                            nynorsk { +"Har du inntekt ved sida av uføretrygda, kan dette også ha noko å seie for storleiken på barnetillegget ditt." },
                        )
                    }
                }
            }

            showIf((reduksjonsgrunnlagBarnetilleggRegelverkstype.equalTo(BarnetilleggRegelverkTypeCode.OVERGANGSREGLER_2016) and reduksjonsgrunnlagAndelYtelseAvOifu.greaterThan(95.0))) {
                includePhrase(TBU3802_Generated_Ny(
                    virkningstidpunktAr = beregning.virkFom.year,
                    andelYtelseAvOifu = reduksjonsgrunnlagAndelYtelseAvOifu,
                    gradertOppjustertIfu = reduksjonsgrunnlagGradertoppjustertifu,
                    sumUtBtHoyereLavere = sumUtBtHoyereLavere,
                    sumBruttoForReduksjonBt = reduksjonsgrunnlagSumbruttoforreduksjonbt,
                    sumBruttoEtterReduksjonBt = reduksjonsgrunnlagSumbruttoetterreduksjonbt,
                    sumUtBt = reduksjonsgrunnlagSumutbt,
                    anvendtTrygdetid = beregning.anvendtTrygdetid,
                    btfbInnvilget = btFellesInnvilget,
                    epsBorMed = grunnlag.epsBrukerBorMed,
                ))
            }

            //IF (   NOT   (     PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoForReduksjonBT > 0     AND     PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoEtterReduksjonBT = 0   ) ) THEN   INCLUDE ENDIF
            showIf((not(reduksjonsgrunnlagSumbruttoforreduksjonbt.greaterThan(0) and reduksjonsgrunnlagSumbruttoetterreduksjonbt.equalTo(0)))) {

                //IF(  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true)   ) THEN      INCLUDE ENDIF
                showIf(((not(btFellesInnvilget) and btSerkullInnvilget))) {
                    includePhrase(TBU2338_Generated_Ny(
                        btsbNetto = btSerkullNetto,
                        beregningSivilstandAnvendt = grunnlag.anvendtSivilstand,
                        epsBorMed = grunnlag.epsBrukerBorMed,
                    ))
                }

                showIf(btFellesInnvilget) {
                    includePhrase(TBU2339_Generated_Ny(
                        btfbNetto = btFellesNetto,
                        btsbInnvilget = btSerkullInnvilget,
                        btfbInnvilget = btFellesInnvilget,
                        barnetBarnaFelles = barnetBarnaFelles,
                        barnetBarnaFellesEnEntallFlertall = barnetBarnaFellesEntallFlertall,
                        epsBorMed = grunnlag.epsBrukerBorMed,
                    ))
                }

                showIf((btInnvilget)) {
                    paragraph {
                        text(
                            bokmal { +"Endringer i " },
                            nynorsk { +"Endringar i " },
                        )

                        showIf(btFellesInnvilget) {
                            text(
                                bokmal { +"inntektene til deg og din " + epsBorMedBmEntall + " " },
                                nynorsk { +"inntektene til deg og " + epsBorMedNnEntall + " din " },
                            )
                        }

                        showIf((not(btFellesInnvilget) and btSerkullInnvilget)) {
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

                showIf(btInnvilget) {
                    includePhrase(
                        InntektBarnetillegg(
                            btFellesInnvilget = btFellesInnvilget,
                            btSerkullInnvilget = btSerkullInnvilget,
                            grunnbelop = grunnbelop,
                            pe = pe,
                            periodisertInntekt = periodisertInntekt
                        )
                    )
                }

                showIf(((btFellesInnvilget))) {
                    paragraph {
                        text(
                            bokmal { +"Inntekten din er " + btfbbrukersinntekttilavkortning.format() + " og inntekten til din " + epsBorMedBmEntall + " er " + btfbinntektannenforelder.format() + ". " },
                            nynorsk { +"Inntekta di er " + btfbbrukersinntekttilavkortning.format() + ", og inntekta til " + epsBorMedNnEntall + " din er " + btfbinntektannenforelder.format() + ". " },
                        )

                        showIf((btfbbelopfratrukketannenforeldersinntekt.greaterThan(0))) {
                            text(
                                bokmal { +"Folketrygdens grunnbeløp på inntil " + grunnbelop.format() + " er holdt utenfor inntekten til din " + epsBorMedBmEntall + ". " },
                                nynorsk { +"Grunnbeløpet i folketrygda på inntil " + grunnbelop.format() + " er halde utanfor inntekta til " + epsBorMedNnEntall + " din. " }
                            )
                        }

                        //IF(( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false  AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto <> 0 )) THEN      INCLUDE ENDIF
                        showIf(((not(btSerkullInnvilget) and btfbjusteringsbelopperar0 and btFellesNetto.notEqualTo(0)))) {
                            showIf(btfbinntektbruktiavkorting.greaterThan(btfbFribelop)) {
                                text(
                                    bokmal { +"Til sammen er inntektene høyere enn fribeløpet ditt på " + btfbFribelop.format() + ". Barnetillegget ditt er derfor " },
                                    nynorsk { +"Til saman er inntektene høgare enn fribeløpet ditt på " + btfbFribelop.format() + ". Barnetillegget ditt er derfor " },
                                )
                            }.orShow {
                                text(
                                    bokmal { +"Til sammen er inntektene lavere enn fribeløpet ditt på " + btfbFribelop.format() + ". Barnetillegget ditt er derfor " },
                                    nynorsk { +"Til saman er inntektene lågare enn fribeløpet ditt på " + btfbFribelop.format() + ". Barnetillegget ditt er derfor " },
                                )
                            }
                        }

                        //IF(( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto <> 0  AND  PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag = 0 )) THEN      INCLUDE ENDIF
                        showIf(((not(btSerkullInnvilget) and btfbjusteringsbelopperar0 and btFellesNetto.notEqualTo(0) and btfbfradrag.equalTo(0)))) {
                            text(
                                bokmal { +"ikke " },
                                nynorsk { +"ikkje " },
                            )
                        }

                        //IF(( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false  AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto <> 0 )) THEN      INCLUDE ENDIF
                        showIf(((not(btSerkullInnvilget) and btfbjusteringsbelopperar0 and btFellesNetto.notEqualTo(0)))) {
                            text(
                                bokmal { +"redusert. " },
                                nynorsk { +"redusert. " },
                            )
                        }

                        showIf(((btfbJusteringsbelopPerAr.notEqualTo(0) and not(btSerkullInnvilget)))) {
                            text(
                                bokmal { +"Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget. " },
                                nynorsk { +"Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover. Dette har vi teke omsyn til når vi endra barnetillegget. " },
                            )
                        }

                        //IF(( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr <> 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto  = 0 ))THEN     INCLUDE ENDIF
                        showIf(((btfbJusteringsbelopPerAr.notEqualTo(0) and not(btSerkullInnvilget) and btFellesNetto0))) {
                            text(
                                bokmal { +"Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året. " },
                                nynorsk { +"Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året." },
                            )
                        }
                    }
                }

                showIf((btSerkullInnvilget and not(btFellesInnvilget))) {
                    paragraph {
                        //IF(( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_UT_BTSBInnvilget_Ikke_BTFBInnvilget() )) THEN      INCLUDE ENDIF
                        showIf(((btSerkullNetto.greaterThan(0) and btsbJusteringsbelopPerAr.equalTo(0) and btsbInnvilgetIkkeBtfbinnvilget))) {
                            showIf(btsbInntektBruktIAvkorting.greaterThan(btsbfribelop)) {
                                text(
                                    bokmal { +"Inntekten din på " + btsbInntektBruktIAvkorting.format() + " er høyere enn fribeløpet ditt på " + btsbfribelop.format() + ". Barnetillegget ditt er derfor " },
                                    nynorsk { +"Inntekta di på " + btsbInntektBruktIAvkorting.format() + " er høgare enn fribeløpet ditt på " + btsbfribelop.format() + ". Barnetillegget ditt er derfor " },
                                )
                            }.orShow {
                                text(
                                    bokmal { +"Inntekten din på " + btsbInntektBruktIAvkorting.format() + " er lavere enn fribeløpet ditt på " + btsbfribelop.format() + ". Barnetillegget ditt er derfor " },
                                    nynorsk { +"Inntekta di på " + btsbInntektBruktIAvkorting.format() + " er lågare enn fribeløpet ditt på " + btsbfribelop.format() + ". Barnetillegget ditt er derfor " },
                                )
                            }
                        }

                        //IF(( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_UT_BTSBInnvilget_Ikke_BTFBInnvilget() AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBInntektBruktiAvkortning <= PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBfribelop )) THEN      INCLUDE ENDIF
                        showIf(((btSerkullNetto.greaterThan(0) and btsbJusteringsbelopPerAr.equalTo(0) and btsbInnvilgetIkkeBtfbinnvilget and btsbInntektBruktIAvkorting.lessThanOrEqual(btsbfribelop)))) {
                            text(
                                bokmal { +"ikke " },
                                nynorsk { +"ikkje " },
                            )
                        }

                        //IF(( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_UT_BTSBInnvilget_Ikke_BTFBInnvilget() )) THEN      INCLUDE ENDIF
                        showIf(((btSerkullNetto.greaterThan(0) and btsbJusteringsbelopPerAr.equalTo(0) and btsbInnvilgetIkkeBtfbinnvilget))) {
                            text(
                                bokmal { +"redusert ut fra inntekt. " },
                                nynorsk { +"redusert ut frå inntekt. " },
                            )
                        }

                        //IF(( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_UT_BTSBInnvilget_Ikke_BTFBInnvilget() )) THEN      INCLUDE ENDIF
                        showIf(((btSerkullNetto0 and btsbJusteringsbelopPerAr.equalTo(0) and btsbInnvilgetIkkeBtfbinnvilget))) {
                            text(
                                bokmal { +"Inntekten din er " + btsbInntektBruktIAvkorting.format() + ". " },
                                nynorsk { +"Inntekta di er " + btsbInntektBruktIAvkorting.format() + "." },
                            )
                        }

                        showIf(((btsbInnvilgetOgJusteringsbelop0))) {
                            text(
                                bokmal { +"Inntekten din er " + btsbInntektBruktIAvkorting.format() + ". Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget. " },
                                nynorsk { +"Inntekta di er " + btsbInntektBruktIAvkorting.format() + ". Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover. Dette har vi teke omsyn til når vi endra barnetillegget. " },
                            )
                        }

                        showIf(((btsbInnvilgetOgJusteringsbelop0 and btSerkullNetto0))) {
                            text(
                                bokmal { +"Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året." },
                                nynorsk { +"Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året." },
                            )
                        }
                    }
                }

                showIf((btsbInnvbtfbInnvOgEnAvDeHarNettoUlik0OgJusteringsbelop0 or btsbInnvbtfbInnvJusteringsbelopFellesIkke0 or btsbInnvbtfbInnvJusteringsbelopSerkullIkke0)) {
                    paragraph {
                        //IF(PE_UT_TBU1286_del1() AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto <> 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0) THEN      INCLUDE ENDIF
                        showIf((btsbInnvbtfbInnvOgEnAvDeHarNettoUlik0OgJusteringsbelop0 and btSerkullNetto.notEqualTo(0) and btsbJusteringsbelopPerAr.equalTo(0))) {
                            showIf(btsbInntektBruktIAvkorting.greaterThan(btsbfribelop)) {
                                text(
                                    bokmal { +"Inntekten din er høyere enn " + btsbfribelop.format() + ", som er fribeløpet for barnetillegget til " + barnetBarnaSerkull + " som ikke bor sammen med begge foreldrene. " },
                                    nynorsk { +"Inntekta di er høgare enn " + btsbfribelop.format() + ", som er fribeløpet for barnetillegget til " + barnetBarnaSerkull + " som ikkje bur saman med begge foreldra. " },
                                )
                            }.orShow {
                                text(
                                    bokmal { +"Inntekten din er lavere enn " + btsbfribelop.format() + ", som er fribeløpet for barnetillegget til " + barnetBarnaSerkull + " som ikke bor sammen med begge foreldrene. " },
                                    nynorsk { +"Inntekta di er lågare enn " + btsbfribelop.format() + ", som er fribeløpet for barnetillegget til " + barnetBarnaSerkull + " som ikkje bur saman med begge foreldra. " },
                                )
                            }
                        }

                        //IF( PE_UT_TBU1286_del1() AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto <> 0  AND  ((PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBfradrag > 0  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag = 0)  OR   (PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBfradrag = 0  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag > 0)) AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 ) THEN      INCLUDE ENDIF
                        showIf((btsbInnvbtfbInnvOgEnAvDeHarNettoUlik0OgJusteringsbelop0 and btSerkullNetto.notEqualTo(0) and ((btsbfradrag.greaterThan(0) and btfbfradrag.equalTo(0)) or (btsbfradrag.equalTo(0) and btfbfradrag.greaterThan(0))) and btsbJusteringsbelopPerAr.equalTo(0))) {
                            text(
                                bokmal { +"Dette barnetillegget er derfor " + ikkeHvisBtsbBruttoErLikNetto + "redusert ut fra inntekt. " },
                                nynorsk { +"Dette barnetillegget er derfor " + ikkeHvisBtsbBruttoErLikNetto + "redusert ut frå inntekt. " },
                            )
                        }

                        //IF( PE_UT_TBU1286_del1() AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto <> 0 AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 ) THEN      INCLUDE ENDIF
                        showIf((btsbInnvbtfbInnvOgEnAvDeHarNettoUlik0OgJusteringsbelop0 and btFellesNetto.notEqualTo(0) and btfbjusteringsbelopperar0)) {
                            text(
                                bokmal { +"Til sammen er " },
                                nynorsk { +"Til saman er " },
                            )
                        }

                        //IF( PE_UT_TBU1286_del1() AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto <>  0 AND ( (PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBfradrag = 0  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag = 0)   OR   (PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBfradrag > 0  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag > 0) ) AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 ) THEN      INCLUDE ENDIF
                        showIf((btsbInnvbtfbInnvOgEnAvDeHarNettoUlik0OgJusteringsbelop0 and btFellesNetto.notEqualTo(0) and ((btsbfradrag.equalTo(0) and btfbfradrag.equalTo(0)) or (btsbfradrag.greaterThan(0) and btfbfradrag.greaterThan(0))) and btfbjusteringsbelopperar0)) {
                            text(
                                bokmal { +"også " },
                                nynorsk { +"også " },
                            )
                        }

                        //IF( PE_UT_TBU1286_del1() AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto <> 0 AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 ) THEN      INCLUDE ENDIF
                        showIf((btsbInnvbtfbInnvOgEnAvDeHarNettoUlik0OgJusteringsbelop0 and btFellesNetto.notEqualTo(0) and btfbjusteringsbelopperar0)) {
                            showIf(btfbinntektbruktiavkorting.greaterThan(btfbFribelop)) {
                                text(
                                    bokmal { +"inntektene til deg og din " + epsBorMedBmEntall + " høyere enn " + btfbFribelop.format() + ", som er fribeløpet for barnetillegget til " + barnetBarnaFelles + " som bor med begge sine foreldre. " },
                                    nynorsk { +"inntektene til deg og " + epsBorMedNnEntall + " din høgare enn " + btfbFribelop.format() + ", som er fribeløpet for barnetillegget til " + barnetBarnaFelles + " som bur saman med begge foreldra sine. " },
                                )
                            }.orShow {
                                text(
                                    bokmal { +"inntektene til deg og din " + epsBorMedBmEntall + " lavere enn " + btfbFribelop.format() + ", som er fribeløpet for barnetillegget til " + barnetBarnaFelles + " som bor med begge sine foreldre. " },
                                    nynorsk { +"inntektene til deg og " + epsBorMedNnEntall + " din lågare enn " + btfbFribelop.format() + ", som er fribeløpet for barnetillegget til " + barnetBarnaFelles + " som bur saman med begge foreldra sine. " },
                                )
                            }
                        }

                        //IF( PE_UT_TBU1286_del1() AND ((PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBfradrag > 0  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag = 0)   OR   (PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBfradrag = 0  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag > 0))  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto <> 0 AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 ) THEN      INCLUDE ENDIF
                        showIf((btsbInnvbtfbInnvOgEnAvDeHarNettoUlik0OgJusteringsbelop0 and ((btsbfradrag.greaterThan(0) and btfbfradrag.equalTo(0)) or (btsbfradrag.equalTo(0) and btfbfradrag.greaterThan(0))) and btFellesNetto.notEqualTo(0) and btfbjusteringsbelopperar0)) {
                            text(
                                bokmal { +"Dette barnetillegget er derfor " },
                                nynorsk { +"Dette barnetillegget er derfor " },
                            )
                        }

                        //IF( PE_UT_TBU1286_del1() AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBbrutto  AND  ((PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBfradrag > 0  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag = 0)   OR   (PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBfradrag = 0  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag > 0))  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto <> 0 AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 ) THEN      INCLUDE ENDIF
                        showIf((btsbInnvbtfbInnvOgEnAvDeHarNettoUlik0OgJusteringsbelop0 and btFellesNetto.equalTo(btfbbrutto) and ((btsbfradrag.greaterThan(0) and btfbfradrag.equalTo(0)) or (btsbfradrag.equalTo(0) and btfbfradrag.greaterThan(0))) and btFellesNetto.notEqualTo(0) and btfbjusteringsbelopperar0)) {
                            text(
                                bokmal { +"ikke " },
                                nynorsk { +"ikkje " },
                            )
                        }

                        //IF( PE_UT_TBU1286_del1() AND ((PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBfradrag > 0  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag = 0)   OR   (PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBfradrag = 0  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag > 0))  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto <> 0 AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 ) THEN      INCLUDE ENDIF
                        showIf((btsbInnvbtfbInnvOgEnAvDeHarNettoUlik0OgJusteringsbelop0 and ((btsbfradrag.greaterThan(0) and btfbfradrag.equalTo(0)) or (btsbfradrag.equalTo(0) and btfbfradrag.greaterThan(0))) and btFellesNetto.notEqualTo(0) and btfbjusteringsbelopperar0)) {
                            text(
                                bokmal { +"redusert ut fra inntekt. " },
                                nynorsk { +"redusert ut frå inntekt. " },
                            )
                        }

                        //IF( PE_UT_TBU1286_del1() AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto <> 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto <> 0  AND  ((PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBfradrag = 0  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag = 0)   OR   (PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBfradrag > 0  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag > 0)) AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0  AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0  ) THEN      INCLUDE ENDIF
                        showIf((btsbInnvbtfbInnvOgEnAvDeHarNettoUlik0OgJusteringsbelop0 and btFellesNetto.notEqualTo(0) and btSerkullNetto.notEqualTo(0) and ((btsbfradrag.equalTo(0) and btfbfradrag.equalTo(0)) or (btsbfradrag.greaterThan(0) and btfbfradrag.greaterThan(0))) and btsbJusteringsbelopPerAr.equalTo(0) and btfbjusteringsbelopperar0)) {
                            text(
                                bokmal { +"Barnetilleggene er derfor" },
                                nynorsk { +"Barnetillegga er derfor" },
                            )
                        }

                        //IF( PE_UT_TBU1286_del1() AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBbrutto  AND  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBbrutto)  AND  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto <> 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto <> 0)  AND  ((PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBfradrag = 0  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag = 0)   OR   (PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBfradrag > 0  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag > 0)) AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0  AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 ) THEN      INCLUDE ENDIF
                        showIf(
                            (btsbInnvbtfbInnvOgEnAvDeHarNettoUlik0OgJusteringsbelop0 and (btFellesNetto.equalTo(btfbbrutto) and btSerkullNetto.equalTo(barnetilleggserkull_btsbbrutto)) and (btFellesNetto.notEqualTo(0) and btSerkullNetto.notEqualTo(0)) and ((btsbfradrag.equalTo(0) and btfbfradrag.equalTo(0)) or (btsbfradrag.greaterThan(0) and btfbfradrag.greaterThan(0))) and btsbJusteringsbelopPerAr
                                .equalTo(0) and btfbjusteringsbelopperar0)
                        ) {
                            text(
                                bokmal { +" ikke" },
                                nynorsk { +" ikkje" },
                            )
                        }

                        //IF( PE_UT_TBU1286_del1() AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto <> 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto <> 0  AND  ((PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBfradrag = 0  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag = 0)   OR   (PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBfradrag > 0  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag > 0)) AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0  AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0  ) THEN      INCLUDE ENDIF
                        showIf((btsbInnvbtfbInnvOgEnAvDeHarNettoUlik0OgJusteringsbelop0 and btFellesNetto.notEqualTo(0) and btSerkullNetto.notEqualTo(0) and ((btsbfradrag.equalTo(0) and btfbfradrag.equalTo(0)) or (btsbfradrag.greaterThan(0) and btfbfradrag.greaterThan(0))) and btsbJusteringsbelopPerAr.equalTo(0) and btfbjusteringsbelopperar0)) {
                            text(
                                bokmal { +" redusert ut fra inntekt. " },
                                nynorsk { +" redusert ut frå inntekt. " },
                            )
                        }

                        //IF( PE_UT_TBU1286_del1() AND  ( PE_UT_TBU1286_del2() OR PE_UT_TBU1286_del3() )) THEN      INCLUDE ENDIF
                        showIf((btsbInnvbtfbInnvOgEnAvDeHarNettoUlik0OgJusteringsbelop0 and (btsbInnvbtfbInnvJusteringsbelopFellesIkke0 or btsbInnvbtfbInnvJusteringsbelopSerkullIkke0))) {
                            text(
                                bokmal { +" " },
                                nynorsk { +" " },
                            )
                        }

                        showIf(((btsbInnvbtfbInnvJusteringsbelopFellesIkke0))) {
                            text(
                                bokmal { +"Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget for " + barnetBarnaFelles + " som bor med begge sine foreldre. " },
                                nynorsk { +"Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover. Dette har vi teke omsyn til når vi endra barnetillegget for " + barnetBarnaFelles + " som bur saman med begge foreldra sine. " },
                            )
                        }

                        showIf(((btsbInnvbtfbInnvJusteringsbelopFellesIkke0 and btFellesNetto0))) {
                            text(
                                bokmal { +"Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året. " },
                                nynorsk { +"Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året. " },
                            )
                        }

                        showIf(((btsbInnvbtfbInnvJusteringsbelopFellesIkke0 and btsbInnvbtfbInnvJusteringsbelopSerkullIkke0))) {
                            text(
                                bokmal { +" " },
                                nynorsk { +" " },
                            )
                        }

                        showIf(((btsbInnvbtfbInnvJusteringsbelopSerkullIkke0))) {
                            text(
                                bokmal { +"Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget for " + barnetBarnaSerkull + " som ikke bor sammen med begge foreldrene. " },
                                nynorsk { +"Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover. Dette har vi teke omsyn til når vi endra barnetillegget for " + barnetBarnaSerkull + " som ikkje bur saman med begge foreldra. " },
                            )
                        }

                        showIf(((btsbInnvbtfbInnvJusteringsbelopSerkullIkke0 and btSerkullNetto0))) {
                            text(
                                bokmal { +"Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året. " },
                                nynorsk { +"Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året." },
                            )
                        }
                    }
                }

                //IF( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto <> 0  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false) ) THEN      INCLUDE ENDIF
                showIf((btSerkullInnvilget and btSerkullNetto0 and btsbJusteringsbelopPerAr.equalTo(0) and (btFellesNetto.notEqualTo(0) or not(btFellesInnvilget)))) {
                    paragraph {
                        text(
                            bokmal { +"Barnetillegget " },
                            nynorsk { +"Barnetillegget " },
                        )

                        showIf(btFellesInnvilget) {
                            text(
                                bokmal { +"for " + barnetBarnaSerkull + " som ikke bor sammen med begge foreldrene, " },
                                nynorsk { +"for " + barnetBarnaSerkull + " som ikkje bur saman med begge foreldra sine, " },
                            )
                        }
                        text(
                            bokmal { +"blir ikke utbetalt fordi du " },
                            nynorsk { +"blir ikkje utbetalt fordi du " },
                        )

                        showIf(btFellesInnvilget) {
                            text(
                                bokmal { +"alene " },
                                nynorsk { +"åleine " },
                            )
                        }
                        text(
                            bokmal { +"har en samlet inntekt som er høyere enn " + barnetilleggserkull_avkortningsinformasjon_inntektstak.format() + ". Inntekten din er over grensen for å få utbetalt barnetillegg." },
                            nynorsk { +"har ei samla inntekt som er høgare enn " + barnetilleggserkull_avkortningsinformasjon_inntektstak.format() + ". Inntekta di er over grensa for å få utbetalt barnetillegg. " },
                        )
                    }
                }

                //IF( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto <> 0  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false) ) THEN      INCLUDE ENDIF
                showIf((btFellesInnvilget and btFellesNetto0 and btfbjusteringsbelopperar0 and (btSerkullNetto.notEqualTo(0) or not(btSerkullInnvilget)))) {
                    paragraph {
                        text(
                            bokmal { +"Barnetillegget" },
                            nynorsk { +"Barnetillegget " },
                        )

                        showIf(btSerkullInnvilget) {
                            text(
                                bokmal { +" for " + barnetBarnaFelles + " som bor med begge sine foreldre," },
                                nynorsk { +"for " + barnetBarnaFelles + " som bur saman med begge foreldra sine, " },
                            )
                        }
                        text(
                            bokmal { +" blir ikke utbetalt fordi dere har en samlet inntekt som er høyere enn " + btfbInntektstak.format() + ". De samlede inntektene er over grensen for å få utbetalt barnetillegg." },
                            nynorsk { +"blir ikkje utbetalt fordi dei har ei samla inntekt som er høgare enn " + btfbInntektstak.format() + ". Dei samla inntektene er over grensa for å få utbetalt barnetillegg." },
                        )
                    }
                }

                //IF( (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  AND  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0)  AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0)  )  THEN      INCLUDE ENDIF
                showIf(((btSerkullInnvilget and btSerkullNetto0 and btsbJusteringsbelopPerAr.equalTo(0)) and (btFellesInnvilget and btFellesNetto0 and btfbjusteringsbelopperar0))) {
                    paragraph {
                        text(
                            bokmal { +"Barnetillegget for " + barnetBarnaFelles + " som bor med begge sine foreldre, blir ikke utbetalt fordi de samlede inntektene er høyere enn " + btfbInntektstak.format() + ". Barnetillegget for " + barnetBarnaSerkull + " som ikke bor sammen med begge foreldrene, blir heller ikke utbetalt fordi inntekten din alene er høyere enn " + barnetilleggserkull_avkortningsinformasjon_inntektstak.format() + ". Inntektene er over grensen for å få utbetalt barnetillegg." },
                            nynorsk { +"Barnetillegget for " + barnetBarnaFelles + " som bur saman med begge foreldra sine, blir ikkje utbetalt fordi dei samla inntektene er høgare enn " + btfbInntektstak.format() + ". Barnetillegget for " + barnetBarnaSerkull + " som ikkje bur saman med begge foreldra, blir heller ikkje utbetalt fordi inntekta di åleine er høgare enn " + barnetilleggserkull_avkortningsinformasjon_inntektstak.format() + ". Inntektene er over grensa for å få utbetalt barnetillegg." },
                        )
                    }
                }

                showIf((btInnvilget)) {
                    paragraph(uniqueness = "barnetillegg") {
                        text(
                            bokmal { +"Du kan lese mer om beregningen av barnetillegg i vedlegget " },
                            nynorsk { +"Du kan lese meir om berekninga av barnetillegg i vedlegget " },
                        )
                        namedReference(vedleggOpplysningerBruktIBeregningUTLegacy)
                        text(bokmal { +"." }, nynorsk { +"." })
                    }
                }
            }

            //IF (PE_UT_KravLinjeKode_VedtakResultat_forekomst_bt_innv()) THEN INCLUDE ENDIF
            showIf(forekomstBtInnv.greaterThan(0)) {
                includePhrase(TBU5005_Generated)
                paragraph {
                    text(
                        bokmal { +"Hvis du planlegger å flytte eller oppholde deg i et annet land, må du kontakte oss slik at vi kan ta stilling til om du fortsatt har rett til barnetillegg. Dette gjelder også hvis barnet du forsørger skal oppholde seg i et annet land." },
                        nynorsk { +"Om du planlegg å flytte eller opphalde deg i eit anna land må du kontakte oss slik at vi kan ta stilling til om du fortsatt har rett til barnetillegg. Dette gjeld også om barnet du forsørgjer skal opphalde seg i eit anna land. " },
                    )
                }
            }

            showIf(gjenlevendetilleggInnvilget) {
                title1 {
                    text(
                        bokmal { +"For deg som mottar gjenlevendetillegg" },
                        nynorsk { +"For deg som mottar tillegg for attlevande ektefelle" },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Du er innvilget uføretrygd med gjenlevendetillegg. Tillegget er beregnet etter ditt eget og din avdøde ektefelles beregningsgrunnlag og trygdetid. Tjener du mer enn bunnfradraget ditt, reduserer vi gjenlevendetillegget ditt med samme prosent som vi reduserer uføretrygden din med. " },
                        nynorsk { +"Du er innvilga uføretrygd med attlevandetillegg. Tillegget er rekna ut etter utrekningsgrunnlaget og trygdetida både for deg og for den avdøde ektefellen din. Tener du meir enn botnfrådraget ditt, reduserer vi attlevandetillegget ditt med same prosent som vi reduserer uføretrygda di med. " },
                    )

                    showIf(nyttGjenlevendetillegg) {
                        text(
                            bokmal { +"Tillegget er tidsbegrenset til fem år fra virkningstidspunktet. " },
                            nynorsk { +"Tillegget er tidsbegrensa til fem år frå verknadstidspunktet. " },
                        )
                    }
                }
            }

            showIf(gjenlevendetilleggInnvilget) {
                paragraph(uniqueness = "gjenlevendetillegg") {
                    text(
                        bokmal { +"Du kan lese mer om dette i vedlegget " },
                        nynorsk { +"Du kan lese meir om dette i vedlegget " },
                    )
                    namedReference(vedleggOpplysningerBruktIBeregningUTLegacy)
                    text(bokmal { +"." }, nynorsk { +"." })
                }
            }
            showIf(harLopendeAlderspensjon) {
                includePhrase(Ufoeretrygd.KombinereUforetrygdAldersPensjon)
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopOkt = true AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad = PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_Kravhode_onsketVirkningsDato < PE_VedtakFattetDato_minus_1mnd) THEN      INCLUDE ENDIF
            showIf((harBeløpØkt and utbetalingsgrad.equalTo(uforegradFraBeregning) and pesysData.vedtaksinfo.virkningsdatoTidligereMnd)) {
                includePhrase(Ufoeretrygd.EtterbetalingUforetrygd(kravOnsketvirkningsdato, visTekstOmRenter))
            }

            showIf(bostedutland) {
                includePhrase(Ufoeretrygd.BeregningenDinKanBliEndret)
                paragraph {
                    text(
                        bokmal { +fritekst("STRYK TEKSTEN UNDER DERSOM DEN IKKE ER AKTUELL") },
                        nynorsk { +fritekst("STRYK TEKSTEN UNDER DERSOM DEN IKKE ER AKTUELL") },
                    )
                }
                title1 {
                    text(
                        bokmal { +"En utenlandsk myndighet krever refusjon" },
                        nynorsk { +"Eit utanlandsk organ krev refusjon" },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"" + fritekst("land") + " har varslet Nav at de kan ha utbetalt for mye penger til deg. De har mulighet til å kreve dette tilbake i etterbetalingen av den norske uføretrygden din. Vi vil holde tilbake etterbetalingen inntil vi har fått svar fra " + fritekst("land") + ". Har du spørsmål om dette, kan du ta kontakt med " + fritekst("nasjonalitet") + " myndigheter." },
                        nynorsk { +"" + fritekst("Land") + " har varsla Nav at dei kan ha betalt ut for mykje pengar til deg. Dei har høve til å krevje dette tilbake i etterbetalinga av den norske uføretrygda di. Vi vil halde tilbake etterbetalinga inntil vi har fått svar frå " + fritekst("Land") + ". Har du spørsmål om dette, kan du ta kontakt med " + fritekst("Nasjonalitet") + " styresmakter." },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Denne retten har " + fritekst("land") + " etter EØS-forordningen 987/2009 artikkel 72." },
                        nynorsk { +"Denne retten har " + fritekst("Land") + " etter EØS-forordninga 987/2009 artikkel 72." },
                    )
                }
            }
            includePhrase(Ufoeretrygd.AvslagBarnetillegg(pesysData.nyeAvslagBarnetillegg))

            includePhrase(Ufoeretrygd.MeldeFraOmEndringer())
            includePhrase(Felles.RettTilAAKlage)
            showIf(bostedutland) {
                paragraph {
                    text(
                        bokmal { +"Du kan sende klagen direkte til Nav eller gjennom " + fritekst("utenlandsk trygdemyndighet") + "." },
                        nynorsk { +"Du kan sende klaga direkte til Nav eller gjennom " + fritekst("Utenlandsk trygdemyndighet") + "." },
                    )
                }
            }
            includePhrase(Felles.RettTilInnsyn(vedleggDineRettigheterOgPlikterUfoere))
            includePhrase(Ufoeretrygd.SjekkUtbetalingene)
            includePhrase(Ufoeretrygd.Skattekort)
            includePhrase(Ufoeretrygd.SkattForDegSomBorIUtlandet(!bostedutland))
            includePhrase(Felles.HarDuSpoersmaal.ufoeretrygd)
        }

        includeAttachmentIfNotNull(vedleggMaanedligUfoeretrygdFoerSkatt, pesysData.maanedligUfoeretrygdFoerSkatt)
        @OptIn(RedigerbartVedlegg::class)
        includeAttachmentRedigerbar(VedleggId("okningUforegradBeregningVedlegg"), vedleggOpplysningerBruktIBeregningUTLegacy, pesysData.pe, pesysData.pe.inkluderopplysningerbruktiberegningen())
        includeAttachment(vedleggDineRettigheterOgPlikterUfore, pesysData.dineRettigheterOgPlikterUfore)
    }
}
