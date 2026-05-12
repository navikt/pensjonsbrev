package no.nav.pensjon.brev.maler.legacy.redigerbar

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoeretrygdDto
import no.nav.pensjon.brev.fixtures.redigerbar.createInnvilgelseUfoeretrygdDto
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.time.LocalDate

@Tag(TestTags.MANUAL_TEST)
class OkningUforegradTest {

    @Test
    fun testPdf() {
        LetterTestImpl(
            OkningUforegrad.template,
            Fixtures.create<InnvilgelseUfoeretrygdDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestPDF("UT_OKNING_UFOREGRAD")
    }

    @Test
    fun testHtml() {
        LetterTestImpl(
            OkningUforegrad.template,
            Fixtures.create<InnvilgelseUfoeretrygdDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestHtml("UT_OKNING_UFOREGRAD")
    }

    @Test
    fun `testPdf - nynorsk`() {
        LetterTestImpl(
            OkningUforegrad.template,
            Fixtures.create<InnvilgelseUfoeretrygdDto>(),
            Language.Nynorsk,
            Fixtures.fellesAuto
        ).renderTestPDF("UT_OKNING_UFOREGRAD_NYNORSK")
    }

    @Test
    fun `testPdf - ung ufor`() {
        val dto = createInnvilgelseUfoeretrygdDto().let { dto ->
            val pe = dto.pesysData.pe
            val vilkarsvedtaklist = pe.vedtaksbrev.vedtaksdata!!.vilkarsvedtaklist!!
            dto.copy(
                pesysData = dto.pesysData.copy(
                    pe = pe.copy(
                        vedtaksbrev = pe.vedtaksbrev.copy(
                            vedtaksdata = pe.vedtaksbrev.vedtaksdata!!.copy(
                                vilkarsvedtaklist = vilkarsvedtaklist.copy(
                                    vilkarsvedtak = vilkarsvedtaklist.vilkarsvedtak.mapIndexed { index, vv ->
                                        if (index == 0) {
                                            vv.copy(vilkar = vv.vilkar!!.copy(unguforresultat = "oppfylt"))
                                        } else vv
                                    }
                                )
                            )
                        )
                    )
                )
            )
        }
        LetterTestImpl(OkningUforegrad.template, dto, Language.Bokmal, Fixtures.fellesAuto)
            .renderTestPDF("UT_OKNING_UFOREGRAD_UNG_UFOR")
    }

    @Test
    fun `testPdf - omgjoring etter klage`() {
        val dto = createInnvilgelseUfoeretrygdDto().let { dto ->
            val pe = dto.pesysData.pe
            val vedtaksdata = pe.vedtaksbrev.vedtaksdata!!
            dto.copy(
                pesysData = dto.pesysData.copy(
                    pe = pe.copy(
                        vedtaksbrev = pe.vedtaksbrev.copy(
                            vedtaksdata = vedtaksdata.copy(
                                kravhode = vedtaksdata.kravhode!!.copy(kravarsaktype = "omgj_etter_klage")
                            )
                        )
                    )
                )
            )
        }
        LetterTestImpl(OkningUforegrad.template, dto, Language.Bokmal, Fixtures.fellesAuto)
            .renderTestPDF("UT_OKNING_UFOREGRAD_KLAGE")
    }

    @Test
    fun `testPdf - omgjoring etter anke med ung ufor`() {
        val dto = createInnvilgelseUfoeretrygdDto().let { dto ->
            val pe = dto.pesysData.pe
            val vedtaksdata = pe.vedtaksbrev.vedtaksdata!!
            val vilkarsvedtaklist = vedtaksdata.vilkarsvedtaklist!!
            dto.copy(
                pesysData = dto.pesysData.copy(
                    pe = pe.copy(
                        vedtaksbrev = pe.vedtaksbrev.copy(
                            vedtaksdata = vedtaksdata.copy(
                                kravhode = vedtaksdata.kravhode!!.copy(kravarsaktype = "omgj_etter_anke"),
                                vilkarsvedtaklist = vilkarsvedtaklist.copy(
                                    vilkarsvedtak = vilkarsvedtaklist.vilkarsvedtak.mapIndexed { index, vv ->
                                        if (index == 0) {
                                            vv.copy(vilkar = vv.vilkar!!.copy(unguforresultat = "oppfylt"))
                                        } else vv
                                    }
                                )
                            )
                        )
                    )
                )
            )
        }
        LetterTestImpl(OkningUforegrad.template, dto, Language.Bokmal, Fixtures.fellesAuto)
            .renderTestPDF("UT_OKNING_UFOREGRAD_ANKE_UNG_UFOR")
    }

    @Test
    fun `testPdf - hel yrkesskade lik uforegrad`() {
        val dto = createInnvilgelseUfoeretrygdDto().let { dto ->
            val pe = dto.pesysData.pe
            val vedtaksdata = pe.vedtaksbrev.vedtaksdata!!
            val vilkarsvedtaklist = vedtaksdata.vilkarsvedtaklist!!
            val beregningufore = vedtaksdata.beregningsdata!!.beregningufore!!
            dto.copy(
                pesysData = dto.pesysData.copy(
                    pe = pe.copy(
                        vedtaksbrev = pe.vedtaksbrev.copy(
                            vedtaksdata = vedtaksdata.copy(
                                vilkarsvedtaklist = vilkarsvedtaklist.copy(
                                    vilkarsvedtak = vilkarsvedtaklist.vilkarsvedtak.mapIndexed { index, vv ->
                                        if (index == 0) {
                                            vv.copy(
                                                beregningsvilkar = vv.beregningsvilkar!!.copy(yrkesskadegrad = 100),
                                                vilkar = vv.vilkar!!.copy(
                                                    yrkesskaderesultat = "oppfylt",
                                                    yrkesskadebegrunnelse = "stdbegr_12_17_1_o_1"
                                                )
                                            )
                                        } else vv
                                    }
                                ),
                                beregningsdata = vedtaksdata.beregningsdata!!.copy(
                                    beregningufore = beregningufore.copy(
                                        uforetrygdberegning = beregningufore.uforetrygdberegning!!.copy(yrkesskadegrad = 100),
                                        beregningytelseskomp = beregningufore.beregningytelseskomp!!.copy(
                                            uforetrygdordiner = beregningufore.beregningytelseskomp!!.uforetrygdordiner!!.copy(
                                                ytelsesgrunnlag = beregningufore.beregningytelseskomp!!.uforetrygdordiner!!.ytelsesgrunnlag!!.copy(
                                                    beregningsgrunnlagyrkesskadebest = true
                                                )
                                            )
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
        }
        LetterTestImpl(OkningUforegrad.template, dto, Language.Bokmal, Fixtures.fellesAuto)
            .renderTestPDF("UT_OKNING_UFOREGRAD_YRKESSKADE_HEL")
    }

    @Test
    fun `testPdf - delvis yrkesskade`() {
        val dto = createInnvilgelseUfoeretrygdDto().let { dto ->
            val pe = dto.pesysData.pe
            val vedtaksdata = pe.vedtaksbrev.vedtaksdata!!
            val vilkarsvedtaklist = vedtaksdata.vilkarsvedtaklist!!
            val beregningufore = vedtaksdata.beregningsdata!!.beregningufore!!
            dto.copy(
                pesysData = dto.pesysData.copy(
                    pe = pe.copy(
                        vedtaksbrev = pe.vedtaksbrev.copy(
                            vedtaksdata = vedtaksdata.copy(
                                vilkarsvedtaklist = vilkarsvedtaklist.copy(
                                    vilkarsvedtak = vilkarsvedtaklist.vilkarsvedtak.mapIndexed { index, vv ->
                                        if (index == 0) {
                                            vv.copy(
                                                beregningsvilkar = vv.beregningsvilkar!!.copy(yrkesskadegrad = 40),
                                                vilkar = vv.vilkar!!.copy(
                                                    yrkesskaderesultat = "oppfylt",
                                                    yrkesskadebegrunnelse = "stdbegr_12_17_1_o_2"
                                                )
                                            )
                                        } else vv
                                    }
                                ),
                                beregningsdata = vedtaksdata.beregningsdata!!.copy(
                                    beregningufore = beregningufore.copy(
                                        uforetrygdberegning = beregningufore.uforetrygdberegning!!.copy(yrkesskadegrad = 40)
                                    )
                                )
                            )
                        )
                    )
                )
            )
        }
        LetterTestImpl(OkningUforegrad.template, dto, Language.Bokmal, Fixtures.fellesAuto)
            .renderTestPDF("UT_OKNING_UFOREGRAD_YRKESSKADE_DELVIS")
    }

    @Test
    fun `testPdf - yrkesskade ikke oppfylt`() {
        val dto = createInnvilgelseUfoeretrygdDto().let { dto ->
            val pe = dto.pesysData.pe
            val vedtaksdata = pe.vedtaksbrev.vedtaksdata!!
            val vilkarsvedtaklist = vedtaksdata.vilkarsvedtaklist!!
            val beregningufore = vedtaksdata.beregningsdata!!.beregningufore!!
            dto.copy(
                pesysData = dto.pesysData.copy(
                    pe = pe.copy(
                        vedtaksbrev = pe.vedtaksbrev.copy(
                            vedtaksdata = vedtaksdata.copy(
                                vilkarsvedtaklist = vilkarsvedtaklist.copy(
                                    vilkarsvedtak = vilkarsvedtaklist.vilkarsvedtak.mapIndexed { index, vv ->
                                        if (index == 0) {
                                            vv.copy(
                                                beregningsvilkar = vv.beregningsvilkar!!.copy(yrkesskadegrad = 40),
                                                vilkar = vv.vilkar!!.copy(
                                                    yrkesskaderesultat = "ikke_oppfylt",
                                                    yrkesskadebegrunnelse = "stdbegr_12_17_1_i_2"
                                                )
                                            )
                                        } else vv
                                    }
                                ),
                                beregningsdata = vedtaksdata.beregningsdata!!.copy(
                                    beregningufore = beregningufore.copy(
                                        uforetrygdberegning = beregningufore.uforetrygdberegning!!.copy(yrkesskadegrad = 40)
                                    )
                                )
                            )
                        )
                    )
                )
            )
        }
        LetterTestImpl(OkningUforegrad.template, dto, Language.Bokmal, Fixtures.fellesAuto)
            .renderTestPDF("UT_OKNING_UFOREGRAD_YRKESSKADE_IKKE_OPPFYLT")
    }

    @Test
    fun `testPdf - barnetillegg felles og serkull`() {
        val dto = createInnvilgelseUfoeretrygdDto().let { dto ->
            val pe = dto.pesysData.pe
            val vedtaksdata = pe.vedtaksbrev.vedtaksdata!!
            val beregningufore = vedtaksdata.beregningsdata!!.beregningufore!!
            val ytelseskomp = beregningufore.beregningytelseskomp!!
            dto.copy(
                pesysData = dto.pesysData.copy(
                    pe = pe.copy(
                        vedtaksbrev = pe.vedtaksbrev.copy(
                            vedtaksdata = vedtaksdata.copy(
                                beregningsdata = vedtaksdata.beregningsdata!!.copy(
                                    beregningufore = beregningufore.copy(
                                        beregningytelseskomp = ytelseskomp.copy(
                                            barnetilleggfelles = ytelseskomp.barnetilleggfelles!!.copy(
                                                btfbinnvilget = true,
                                                btfbnetto = Kroner(1200),
                                                btfbfradrag = Kroner(500)
                                            ),
                                            barnetilleggserkull = ytelseskomp.barnetilleggserkull!!.copy(
                                                btsbinnvilget = true,
                                                btsbnetto = Kroner(800),
                                                btsbfradrag = Kroner(300)
                                            ),
                                            ektefelletillegg = ytelseskomp.ektefelletillegg!!.copy(etinnvilget = false),
                                            gjenlevendetillegg = ytelseskomp.gjenlevendetillegg!!.copy(gtinnvilget = false)
                                        ),
                                        uforetrygdberegning = beregningufore.uforetrygdberegning!!.copy(
                                            instoppholdtype = "",
                                            instopphanvendt = false
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
        }
        LetterTestImpl(OkningUforegrad.template, dto, Language.Bokmal, Fixtures.fellesAuto)
            .renderTestPDF("UT_OKNING_UFOREGRAD_BARNETILLEGG")
    }

    @Test
    fun `testPdf - kun ektefelletillegg`() {
        val dto = createInnvilgelseUfoeretrygdDto().let { dto ->
            val pe = dto.pesysData.pe
            val vedtaksdata = pe.vedtaksbrev.vedtaksdata!!
            val beregningufore = vedtaksdata.beregningsdata!!.beregningufore!!
            val ytelseskomp = beregningufore.beregningytelseskomp!!
            dto.copy(
                pesysData = dto.pesysData.copy(
                    nyeInnvilgedeBarnetillegg = emptyList(),
                    nyeAvslagBarnetillegg = emptyList(),
                    pe = pe.copy(
                        vedtaksbrev = pe.vedtaksbrev.copy(
                            vedtaksdata = vedtaksdata.copy(
                                beregningsdata = vedtaksdata.beregningsdata!!.copy(
                                    beregningufore = beregningufore.copy(
                                        beregningytelseskomp = ytelseskomp.copy(
                                            ektefelletillegg = ytelseskomp.ektefelletillegg!!.copy(etinnvilget = true),
                                            barnetilleggfelles = ytelseskomp.barnetilleggfelles!!.copy(btfbinnvilget = false),
                                            barnetilleggserkull = ytelseskomp.barnetilleggserkull!!.copy(btsbinnvilget = false),
                                            gjenlevendetillegg = ytelseskomp.gjenlevendetillegg!!.copy(gtinnvilget = false)
                                        ),
                                        uforetrygdberegning = beregningufore.uforetrygdberegning!!.copy(
                                            instoppholdtype = "",
                                            instopphanvendt = false
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
        }
        LetterTestImpl(OkningUforegrad.template, dto, Language.Bokmal, Fixtures.fellesAuto)
            .renderTestPDF("UT_OKNING_UFOREGRAD_EKTEFELLETILLEGG")
    }

    @Test
    fun `testPdf - gjenlevendetillegg`() {
        val dto = createInnvilgelseUfoeretrygdDto().let { dto ->
            val pe = dto.pesysData.pe
            val vedtaksdata = pe.vedtaksbrev.vedtaksdata!!
            val beregningufore = vedtaksdata.beregningsdata!!.beregningufore!!
            val ytelseskomp = beregningufore.beregningytelseskomp!!
            dto.copy(
                pesysData = dto.pesysData.copy(
                    nyeInnvilgedeBarnetillegg = emptyList(),
                    nyeAvslagBarnetillegg = emptyList(),
                    pe = pe.copy(
                        vedtaksbrev = pe.vedtaksbrev.copy(
                            vedtaksdata = vedtaksdata.copy(
                                beregningsdata = vedtaksdata.beregningsdata!!.copy(
                                    beregningufore = beregningufore.copy(
                                        beregningytelseskomp = ytelseskomp.copy(
                                            gjenlevendetillegg = ytelseskomp.gjenlevendetillegg!!.copy(
                                                gtinnvilget = true,
                                                nyttgjenlevendetillegg = true,
                                                gtnetto = Kroner(2500)
                                            ),
                                            ektefelletillegg = ytelseskomp.ektefelletillegg!!.copy(etinnvilget = false),
                                            barnetilleggfelles = ytelseskomp.barnetilleggfelles!!.copy(btfbinnvilget = false),
                                            barnetilleggserkull = ytelseskomp.barnetilleggserkull!!.copy(btsbinnvilget = false)
                                        ),
                                        uforetrygdberegning = beregningufore.uforetrygdberegning!!.copy(
                                            instoppholdtype = "",
                                            instopphanvendt = false
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
        }
        LetterTestImpl(OkningUforegrad.template, dto, Language.Bokmal, Fixtures.fellesAuto)
            .renderTestPDF("UT_OKNING_UFOREGRAD_GJENLEVENDETILLEGG")
    }

    @Test
    fun `testPdf - helseinstitusjon reduksjon anvendt med forsorgeransvar`() {
        val dto = createInnvilgelseUfoeretrygdDto().let { dto ->
            val pe = dto.pesysData.pe
            val vedtaksdata = pe.vedtaksbrev.vedtaksdata!!
            val beregningufore = vedtaksdata.beregningsdata!!.beregningufore!!
            val ytelseskomp = beregningufore.beregningytelseskomp!!
            dto.copy(
                pesysData = dto.pesysData.copy(
                    nyeInnvilgedeBarnetillegg = emptyList(),
                    nyeAvslagBarnetillegg = emptyList(),
                    pe = pe.copy(
                        vedtaksbrev = pe.vedtaksbrev.copy(
                            vedtaksdata = vedtaksdata.copy(
                                beregningsdata = vedtaksdata.beregningsdata!!.copy(
                                    beregningufore = beregningufore.copy(
                                        uforetrygdberegning = beregningufore.uforetrygdberegning!!.copy(
                                            instoppholdtype = "reduksjon_hs",
                                            instopphanvendt = true
                                        ),
                                        beregningytelseskomp = ytelseskomp.copy(
                                            ektefelletillegg = ytelseskomp.ektefelletillegg!!.copy(etinnvilget = false),
                                            barnetilleggfelles = ytelseskomp.barnetilleggfelles!!.copy(btfbinnvilget = false),
                                            barnetilleggserkull = ytelseskomp.barnetilleggserkull!!.copy(btsbinnvilget = false),
                                            gjenlevendetillegg = ytelseskomp.gjenlevendetillegg!!.copy(gtinnvilget = false)
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
        }
        LetterTestImpl(OkningUforegrad.template, dto, Language.Bokmal, Fixtures.fellesAuto)
            .renderTestPDF("UT_OKNING_UFOREGRAD_INST_HELSE")
    }

    @Test
    fun `testPdf - straffegjennomforing reduksjon anvendt`() {
        val dto = createInnvilgelseUfoeretrygdDto().let { dto ->
            val pe = dto.pesysData.pe
            val vedtaksdata = pe.vedtaksbrev.vedtaksdata!!
            val beregningufore = vedtaksdata.beregningsdata!!.beregningufore!!
            val ytelseskomp = beregningufore.beregningytelseskomp!!
            val grunnlag = pe.vedtaksbrev.grunnlag
            dto.copy(
                pesysData = dto.pesysData.copy(
                    nyeInnvilgedeBarnetillegg = emptyList(),
                    nyeAvslagBarnetillegg = emptyList(),
                    pe = pe.copy(
                        vedtaksbrev = pe.vedtaksbrev.copy(
                            vedtaksdata = vedtaksdata.copy(
                                beregningsdata = vedtaksdata.beregningsdata!!.copy(
                                    beregningufore = beregningufore.copy(
                                        uforetrygdberegning = beregningufore.uforetrygdberegning!!.copy(
                                            instoppholdtype = "reduksjon_fo",
                                            instopphanvendt = true
                                        ),
                                        beregningytelseskomp = ytelseskomp.copy(
                                            ektefelletillegg = ytelseskomp.ektefelletillegg!!.copy(etinnvilget = false),
                                            barnetilleggfelles = ytelseskomp.barnetilleggfelles!!.copy(btfbinnvilget = false),
                                            barnetilleggserkull = ytelseskomp.barnetilleggserkull!!.copy(btsbinnvilget = false),
                                            gjenlevendetillegg = ytelseskomp.gjenlevendetillegg!!.copy(gtinnvilget = false)
                                        )
                                    )
                                )
                            ),
                            grunnlag = grunnlag.copy(
                                persongrunnlagsliste = grunnlag.persongrunnlagsliste?.map { pg ->
                                    pg.copy(
                                        instopphreduksjonsperiodeliste = pg.instopphreduksjonsperiodeliste?.copy(
                                            instopphreduksjonsperiode = pg.instopphreduksjonsperiodeliste?.instopphreduksjonsperiode?.map {
                                                it.copy(forsorgeransvar = false)
                                            } ?: emptyList()
                                        )
                                    )
                                }
                            )
                        )
                    )
                )
            )
        }
        LetterTestImpl(OkningUforegrad.template, dto, Language.Bokmal, Fixtures.fellesAuto)
            .renderTestPDF("UT_OKNING_UFOREGRAD_INST_STRAFF")
    }

    @Test
    fun `testPdf - bosted utland med etterbetaling`() {
        val dto = createInnvilgelseUfoeretrygdDto().let { dto ->
            val pe = dto.pesysData.pe
            val vedtaksdata = pe.vedtaksbrev.vedtaksdata!!
            val beregningufore = vedtaksdata.beregningsdata!!.beregningufore!!
            val ytelseskomp = beregningufore.beregningytelseskomp!!
            val grunnlag = pe.vedtaksbrev.grunnlag
            dto.copy(
                pesysData = dto.pesysData.copy(
                    nyeInnvilgedeBarnetillegg = emptyList(),
                    nyeAvslagBarnetillegg = emptyList(),
                    pe = pe.copy(
                        vedtaksbrev = pe.vedtaksbrev.copy(
                            vedtaksdata = vedtaksdata.copy(
                                beregningsdata = vedtaksdata.beregningsdata!!.copy(
                                    beregningufore = beregningufore.copy(
                                        belopokt = true,
                                        uforetrygdberegning = beregningufore.uforetrygdberegning!!.copy(
                                            instoppholdtype = "",
                                            instopphanvendt = false
                                        ),
                                        beregningytelseskomp = ytelseskomp.copy(
                                            ektefelletillegg = ytelseskomp.ektefelletillegg!!.copy(etinnvilget = false),
                                            barnetilleggfelles = ytelseskomp.barnetilleggfelles!!.copy(btfbinnvilget = false),
                                            barnetilleggserkull = ytelseskomp.barnetilleggserkull!!.copy(btsbinnvilget = false),
                                            gjenlevendetillegg = ytelseskomp.gjenlevendetillegg!!.copy(gtinnvilget = false)
                                        )
                                    )
                                ),
                                kravhode = vedtaksdata.kravhode!!.copy(
                                    onsketvirkningsdato = LocalDate.of(2020, 1, 1)
                                ),
                                vedtakfattetdatominus1mnd = LocalDate.of(2025, 6, 1)
                            ),
                            grunnlag = grunnlag.copy(
                                persongrunnlagsliste = grunnlag.persongrunnlagsliste?.map { pg ->
                                    pg.copy(personbostedsland = "swe")
                                }
                            )
                        )
                    )
                )
            )
        }
        LetterTestImpl(OkningUforegrad.template, dto, Language.Bokmal, Fixtures.fellesAuto)
            .renderTestPDF("UT_OKNING_UFOREGRAD_UTLAND")
    }

    @Test
    fun `testPdf - 100 prosent uforegrad med IFU begrunnelse og OIFU oppjustering`() {
        val dto = createInnvilgelseUfoeretrygdDto().let { dto ->
            val pe = dto.pesysData.pe
            val vedtaksdata = pe.vedtaksbrev.vedtaksdata!!
            val beregningufore = vedtaksdata.beregningsdata!!.beregningufore!!
            val ytelseskomp = beregningufore.beregningytelseskomp!!
            val vilkarsvedtaklist = vedtaksdata.vilkarsvedtaklist!!
            dto.copy(
                pesysData = dto.pesysData.copy(
                    oifuVedVirkningstidspunkt = Kroner(500000),
                    nyeInnvilgedeBarnetillegg = emptyList(),
                    nyeAvslagBarnetillegg = emptyList(),
                    pe = pe.copy(
                        vedtaksbrev = pe.vedtaksbrev.copy(
                            vedtaksdata = vedtaksdata.copy(
                                vilkarsvedtaklist = vilkarsvedtaklist.copy(
                                    vilkarsvedtak = vilkarsvedtaklist.vilkarsvedtak.mapIndexed { index, vv ->
                                        if (index == 0) {
                                            vv.copy(
                                                beregningsvilkar = vv.beregningsvilkar!!.copy(
                                                    ifubegrunnelse = "stdbegr_12_8_2_1",
                                                    ieubegrunnelse = "",
                                                    ieuinntekt = Kroner(0)
                                                )
                                            )
                                        } else vv
                                    }
                                ),
                                beregningsdata = vedtaksdata.beregningsdata!!.copy(
                                    beregningufore = beregningufore.copy(
                                        uforetrygdberegning = beregningufore.uforetrygdberegning!!.copy(
                                            uforegrad = 100,
                                            instoppholdtype = "",
                                            instopphanvendt = false
                                        ),
                                        beregningytelseskomp = ytelseskomp.copy(
                                            ektefelletillegg = ytelseskomp.ektefelletillegg!!.copy(etinnvilget = false),
                                            barnetilleggfelles = ytelseskomp.barnetilleggfelles!!.copy(btfbinnvilget = false),
                                            barnetilleggserkull = ytelseskomp.barnetilleggserkull!!.copy(btsbinnvilget = false),
                                            gjenlevendetillegg = ytelseskomp.gjenlevendetillegg!!.copy(gtinnvilget = false)
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
        }
        LetterTestImpl(OkningUforegrad.template, dto, Language.Bokmal, Fixtures.fellesAuto)
            .renderTestPDF("UT_OKNING_UFOREGRAD_100_PROSENT_IFU")
    }

    @Test
    fun `testPdf - delvis uforegrad med IEU begrunnelse`() {
        val dto = createInnvilgelseUfoeretrygdDto().let { dto ->
            val pe = dto.pesysData.pe
            val vedtaksdata = pe.vedtaksbrev.vedtaksdata!!
            val beregningufore = vedtaksdata.beregningsdata!!.beregningufore!!
            val ytelseskomp = beregningufore.beregningytelseskomp!!
            val vilkarsvedtaklist = vedtaksdata.vilkarsvedtaklist!!
            dto.copy(
                pesysData = dto.pesysData.copy(
                    nyeInnvilgedeBarnetillegg = emptyList(),
                    nyeAvslagBarnetillegg = emptyList(),
                    pe = pe.copy(
                        vedtaksbrev = pe.vedtaksbrev.copy(
                            vedtaksdata = vedtaksdata.copy(
                                vilkarsvedtaklist = vilkarsvedtaklist.copy(
                                    vilkarsvedtak = vilkarsvedtaklist.vilkarsvedtak.mapIndexed { index, vv ->
                                        if (index == 0) {
                                            vv.copy(
                                                beregningsvilkar = vv.beregningsvilkar!!.copy(
                                                    uforegrad = 60,
                                                    ifubegrunnelse = "stdbegr_12_8_2_1",
                                                    ieubegrunnelse = "stdbegr_12_8_1_3",
                                                    ieuinntekt = Kroner(200000)
                                                )
                                            )
                                        } else vv
                                    }
                                ),
                                beregningsdata = vedtaksdata.beregningsdata!!.copy(
                                    beregningufore = beregningufore.copy(
                                        uforetrygdberegning = beregningufore.uforetrygdberegning!!.copy(
                                            uforegrad = 60,
                                            instoppholdtype = "",
                                            instopphanvendt = false
                                        ),
                                        beregningytelseskomp = ytelseskomp.copy(
                                            ektefelletillegg = ytelseskomp.ektefelletillegg!!.copy(etinnvilget = false),
                                            barnetilleggfelles = ytelseskomp.barnetilleggfelles!!.copy(btfbinnvilget = false),
                                            barnetilleggserkull = ytelseskomp.barnetilleggserkull!!.copy(btsbinnvilget = false),
                                            gjenlevendetillegg = ytelseskomp.gjenlevendetillegg!!.copy(gtinnvilget = false)
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
        }
        LetterTestImpl(OkningUforegrad.template, dto, Language.Bokmal, Fixtures.fellesAuto)
            .renderTestPDF("UT_OKNING_UFOREGRAD_DELVIS_IEU")
    }

    @Test
    fun `testPdf - lopende alderspensjon`() {
        val dto = createInnvilgelseUfoeretrygdDto().let { dto ->
            val pe = dto.pesysData.pe
            val vedtaksdata = pe.vedtaksbrev.vedtaksdata!!
            val beregningufore = vedtaksdata.beregningsdata!!.beregningufore!!
            val ytelseskomp = beregningufore.beregningytelseskomp!!
            dto.copy(
                pesysData = dto.pesysData.copy(
                    nyeInnvilgedeBarnetillegg = emptyList(),
                    nyeAvslagBarnetillegg = emptyList(),
                    pe = pe.copy(
                        functions = pe.functions.copy(pe_saksdata_sakapogup = true),
                        vedtaksbrev = pe.vedtaksbrev.copy(
                            vedtaksdata = vedtaksdata.copy(
                                harLopendealderspensjon = true,
                                beregningsdata = vedtaksdata.beregningsdata!!.copy(
                                    beregningufore = beregningufore.copy(
                                        uforetrygdberegning = beregningufore.uforetrygdberegning!!.copy(
                                            instoppholdtype = "",
                                            instopphanvendt = false
                                        ),
                                        beregningytelseskomp = ytelseskomp.copy(
                                            ektefelletillegg = ytelseskomp.ektefelletillegg!!.copy(etinnvilget = false),
                                            barnetilleggfelles = ytelseskomp.barnetilleggfelles!!.copy(btfbinnvilget = false),
                                            barnetilleggserkull = ytelseskomp.barnetilleggserkull!!.copy(btsbinnvilget = false),
                                            gjenlevendetillegg = ytelseskomp.gjenlevendetillegg!!.copy(gtinnvilget = false)
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
        }
        LetterTestImpl(OkningUforegrad.template, dto, Language.Bokmal, Fixtures.fellesAuto)
            .renderTestPDF("UT_OKNING_UFOREGRAD_LOPENDE_AP")
    }
}