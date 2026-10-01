package no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere.beregning

import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.maler.legacy.ut_vilkargjelderpersonalder
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_minsteytelse_oppfyltungufor
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_minsteytelse_sats
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_anvendttrygdetid
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_mottarminsteytelse
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_unguforresultat
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.greaterThanOrEqual
import no.nav.pensjon.brev.template.dsl.expression.lessThan
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.notEqualTo
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.text

//[TBU028V-TBU020V]
data class SlikBeregnerViUPTilUT (
    val pe: Expression<PEgruppe10>
): OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        val uforegrad = pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()
        val anvendtTrygdetid = pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_anvendttrygdetid()
        val gradertMedFullTrygdetid = uforegrad.greaterThan(0) and uforegrad.lessThan(100) and anvendtTrygdetid.greaterThanOrEqual(40)
        val gradertMedRedusertTrygdetid = uforegrad.greaterThan(0) and uforegrad.lessThan(100) and anvendtTrygdetid.lessThan(40)
        val fulltUfoerMedFullTrygdetid = uforegrad.equalTo(100) and anvendtTrygdetid.greaterThanOrEqual(40)
        val fulltUfoerMedRedusertTrygdetid = uforegrad.equalTo(100) and anvendtTrygdetid.lessThan(40)

        title1 {
            text (
                bokmal { + "Slik regner vi om uførepensjonen din til uføretrygd" },
                nynorsk { + "Slik reknar vi om uførepensjonen din til uføretrygd" }
            )
        }

        showIf(gradertMedFullTrygdetid) {
            paragraph {
                text (
                    bokmal { + "Når vi regner om uførepensjonen din til uføretrygd, tar vi utgangspunkt i den retten du har til uførepensjon før skatt, i desember 2014. Vi regner om grunnpensjonen og din eventuelle tilleggspensjon til et årsbeløp. Årsbeløpet justerer vi ut fra gjennomsnittlig grunnbeløp i 2014. Har du en uføregrad som er lavere enn 100 prosent, vil vi oppjustere årsbeløpet til 100 prosent uføregrad." },
                    nynorsk { + "Når vi reknar om uførepensjonen din til uføretrygd, tek vi utgangspunkt i den retten du har til uførepensjon før skatt, i desember 2014. Vi reknar om grunnpensjonen og din eventuelle tilleggspensjon til eit årsbeløp. Årsbeløpet justerer vi ut frå gjennomsnittleg grunnbeløp i 2014. Har du ein uføregrad som er lågare enn 100 prosent, vil vi oppjustere årsbeløpet til 100 prosent uføregrad." }
                )
            }
        }

        showIf(gradertMedFullTrygdetid) {
            paragraph {
                text (
                    bokmal { + "For å ta hensyn til at skatten øker, blir årsbeløpet justert opp. Vi gjør dette etter fastsatte overgangsregler. Det oppjusterte årsbeløpet er beregningsgrunnlaget ditt. Uføretrygden din vil utgjøre 66 prosent av dette, justert for uføregraden din. Til slutt vil vi redusere uføretrygden ut fra eventuell inntekt du har utover inntektsgrensen. " },
                    nynorsk { + "For å ta hensyn til at skatten aukar, blir årsbeløpet justert opp. Vi gjer dette etter fastsette overgangsreglar. Det oppjusterte årsbeløpet er berekningsgrunnlaget ditt. Uføretrygda di vil utgjere 66 prosent av dette, justert for uføregraden din. Til slutt vil vi redusere uføretrygda ut frå eventuell inntekt du har utover inntektsgrensa. " }
                )

                showIf((not(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_mottarminsteytelse()))){
                    text (
                        bokmal { + "Dersom sivilstanden din endrer seg, vil det ikke medføre at uføretrygden endres." },
                        nynorsk { + "Dersom sivilstanden din endrar seg, vil det ikkje medføre at uføretrygda endrar seg." }
                    )
                }
            }
        }

        showIf(gradertMedRedusertTrygdetid) {
            paragraph {
                text (
                    bokmal { + "Når vi regner om uførepensjonen din til uføretrygd, tar vi utgangspunkt i den retten du har til uførepensjon før skatt, i desember 2014. Vi regner om grunnpensjonen og din eventuelle tilleggspensjon til et årsbeløp. Årsbeløpet justerer vi ut fra gjennomsnittlig grunnbeløp i 2014. Har du en uføregrad som er lavere enn 100 prosent, vil vi oppjustere årsbeløpet til 100 prosent uføregrad." },
                    nynorsk { + "Når vi reknar om uførepensjonen din til uføretrygd, tek vi utgangspunkt i den retten du har til uførepensjon før skatt, i desember 2014. Vi reknar om grunnpensjonen og din eventuelle tilleggspensjon til eit årsbeløp. Årsbeløpet justerer vi ut frå gjennomsnittleg grunnbeløp i 2014. Har du ein uføregrad som er lågare enn 100 prosent, vil vi oppjustere årsbeløpet til 100 prosent uføregrad." }
                )
            }
        }

        showIf(gradertMedRedusertTrygdetid) {
            paragraph {
                text (
                    bokmal { + "For å ta hensyn til at skatten øker, blir årsbeløpet ditt justert opp. Vi gjør dette etter fastsatte overgangsregler. Deretter justerer vi opp til 40 års trygdetid. Det oppjusterte årsbeløpet er beregningsgrunnlaget ditt. Uføretrygden din vil utgjøre 66 prosent av dette, justert for uføregraden og trygdetiden din. Til slutt vil vi redusere uføretrygden ut fra eventuell inntekt du har utover inntektsgrensen. " },
                    nynorsk { + "For å ta hensyn til at skatten aukar, blir årsbeløpet ditt justert opp. Vi gjer dette etter fastsette overgangsreglar. Deretter justerer vi opp til 40 års trygdetid. Det oppjusterte årsbeløpet er berekningsgrunnlaget ditt. Uføretrygda di vil utgjere 66 prosent av dette, justert for uføregraden og trygdetiden din. Til slutt vil vi redusere uføretrygda ut frå eventuell inntekt du har utover inntektsgrensa. " }
                )

                showIf((not(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_mottarminsteytelse()))){
                    text (
                        bokmal { + "Dersom sivilstanden din endrer seg, vil det ikke medføre at uføretrygden endres." },
                        nynorsk { + "Dersom sivilstanden din endrar seg, vil det ikkje medføre at uføretrygda endrar seg." }
                    )
                }
            }
        }

        showIf(fulltUfoerMedFullTrygdetid) {
            paragraph {
                text (
                    bokmal { + "Når vi regner om uførepensjonen din til uføretrygd, tar vi utgangspunkt i den retten du har til uførepensjon før skatt, i desember 2014. Vi regner om grunnpensjonen og din eventuelle tilleggspensjon til et årsbeløp. Vi justerer årsbeløpet ut fra gjennomsnittlig grunnbeløp i 2014." },
                    nynorsk { + "Når vi reknar om uførepensjonen din til uføretrygd, tek vi utgangspunkt i den retten du har til uførepensjon før skatt, i desember 2014. Vi reknar om grunnpensjonen og din eventuelle tilleggspensjon til eit årsbeløp. Vi justerer årsbeløpet ut frå gjennomsnittleg grunnbeløp i 2014." }
                )
            }
        }

        showIf(fulltUfoerMedFullTrygdetid) {
            paragraph {
                text (
                    bokmal { + "For å ta hensyn til at skatten øker, blir årsbeløpet ditt justert opp. Vi gjør dette etter fastsatte overgangsregler. Det oppjusterte årsbeløpet er beregningsgrunnlaget ditt. Uføretrygden din vil utgjøre 66 prosent av dette. Til slutt vil vi redusere uføretrygden ut fra eventuell inntekt du har utover inntektsgrensen. " },
                    nynorsk { + "For å ta hensyn til at skatten aukar, blir årsbeløpet ditt justert opp. Vi gjer dette etter fastsette overgangsreglar. Det oppjusterte årsbeløpet er berekningsgrunnlaget ditt. Uføretrygda di vil utgjere 66 prosent av dette. Til slutt vil vi redusere uføretrygda ut frå eventuell inntekt du har utover inntektsgrensa. " }
                )

                showIf((not(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_mottarminsteytelse()))){
                    text (
                        bokmal { + "Dersom sivilstanden din endrer seg, vil det ikke medføre at uføretrygden endres." },
                        nynorsk { + "Dersom sivilstanden din endrar seg, vil det ikkje medføre at uføretrygda endrar seg." }
                    )
                }
            }
        }

        showIf(fulltUfoerMedRedusertTrygdetid) {
            paragraph {
                text (
                    bokmal { + "Når vi regner om uførepensjonen din til uføretrygd, tar vi utgangspunkt i den retten du har til uførepensjon før skatt, i desember 2014. Vi regner om grunnpensjonen og din eventuelle tilleggspensjon til et årsbeløp. Vi justerer årsbeløpet ut fra gjennomsnittlig grunnbeløp i 2014." },
                    nynorsk { + "Når vi reknar om uførepensjonen din til uføretrygd, tek vi utgangspunkt i den retten du har til uførepensjon før skatt, i desember 2014. Vi reknar om grunnpensjonen og din eventuelle tilleggspensjon til eit årsbeløp. Vi justerer årsbeløpet ut frå gjennomsnittleg grunnbeløp i 2014." }
                )
            }
        }

        showIf(fulltUfoerMedRedusertTrygdetid) {
            paragraph {
                text (
                    bokmal { + "For å ta hensyn til at skatten øker, blir årsbeløpet ditt justert opp. Vi gjør dette etter fastsatte overgangsregler. Deretter justerer vi opp til 40 års trygdetid. Det oppjusterte årsbeløpet er beregningsgrunnlaget ditt. Uføretrygden din vil utgjøre 66 prosent av dette, justert for trygdetiden din. Til slutt vil vi redusere uføretrygden ut fra eventuell inntekt du har utover inntektsgrensen. " },
                    nynorsk { + "For å ta omsyn til at skatten aukar, blir årsbeløpet ditt justert opp. Vi gjer dette etter fastsette overgangsreglar. Deretter justerer vi opp til 40 års trygdetid. Det oppjusterte årsbeløpet er berekningsgrunnlaget ditt. Uføretrygda di vil utgjere 66 prosent av dette, justert for trygdetiden din. Til slutt vil vi redusere uføretrygda ut frå eventuell inntekt du har utover inntektsgrensa. " }
                )

                showIf((not(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_mottarminsteytelse()))){
                    text (
                        bokmal { + "Dersom sivilstanden din endrer seg, vil det ikke medføre at uføretrygden endres." },
                        nynorsk { + "Dersom sivilstanden din endrar seg, vil det ikkje medføre at uføretrygda endrar seg." }
                    )
                }
            }
        }

        showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_mottarminsteytelse())){
            paragraph {
                text (
                    bokmal { + "Du er sikret minsteytelse, fordi beregningen ut fra din egenopptjente inntekt er lavere enn minstenivået for uføretrygd. Størrelsen på minsteytelsen avhenger av om du er enslig eller bor sammen med ektefelle, partner eller samboer. Minste årlige ytelse er 2,28 ganger folketrygdens grunnbeløp for personer som lever sammen med ektefelle, partner eller er i et samboerforhold som har vart i minst 12 av de siste 18 månedene. Er du enslig utgjør minste årlige ytelse 2,48 ganger grunnbeløpet." },
                    nynorsk { + "Du er sikra minsteyting, fordi rekninga ut frå din eigenopptente inntekt er lågare enn minstenivået for uføretrygd. Storleiken på minsteytinga avheng av om du er einsleg eller bur saman med ektefelle, partner eller sambuar. Minste årlege yting er 2,28 gonger folketrygdens grunnbeløp for personar som lever saman med ektefelle, partner eller er i eit sambuarforhold som har vart i minst 12 av dei siste 18 månadene. Er du einsleg utgjer minste årlege yting 2,48 gonger grunnbeløpet." }
                )

                showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_minsteytelse_oppfyltungufor() and pe.ut_vilkargjelderpersonalder().lessThan(20) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_mottarminsteytelse())){
                    text (
                        bokmal { + " Er du innvilget rettighet som ung ufør, er minste årlige ytelse, fra fylte 20 år, 2,66 ganger folketrygdens grunnbeløp hvis du lever sammen med ektefelle, partner eller er i et samboerforhold som har vart i minst 12 av de siste 18 månedene. Er du enslig utgjør minste årlige ytelse 2,91 ganger grunnbeløpet." },
                        nynorsk { + " Er du innvilga rett til ung ufør, er minste årlege yting, frå fylte 20 år, 2,66 gonger folketrygdens grunnbeløp dersom du lever saman med ektefelle, partner eller er i eit sambuarforhold som har vart i minst 12 av dei siste 18 månadene. Er du einsleg utgjer minste årlege yting 2,91 gonger grunnbeløpet." }
                    )
                }
            }
        }

        showIf((pe.ut_vilkargjelderpersonalder().greaterThanOrEqual(20) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_mottarminsteytelse() and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_unguforresultat().equalTo("oppfylt"))){
            paragraph {
                text (
                    bokmal { + "Du er innvilget rettighet som ung ufør. Minste årlige ytelse er 2,709 ganger folketrygdens grunnbeløp hvis du lever sammen med ektefelle, partner eller er i et samboerforhold som har vart i minst 12 av de siste 18 månedene. Er du enslig utgjør minste årlige ytelse 2,959 ganger grunnbeløpet." },
                    nynorsk { + "Du er innvilga rett til ung ufør. Minste årlege yting er 2,709 gonger folketrygdens grunnbeløp dersom du lever saman med ektefelle, partner eller er i eit sambuarforhold som har vart i minst 12 av dei siste 18 månadene. Er du einsleg utgjer minste årlege yting 2,959 gonger grunnbeløpet." }
                )
            }
        }

        showIf((pe.ut_vilkargjelderpersonalder().greaterThanOrEqual(20) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_mottarminsteytelse() and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_unguforresultat().equalTo("oppfylt"))){
            paragraph {
                text (
                    bokmal { + "Du er innvilget rettighet som ung ufør. Minste årlige ytelse er 2,66 ganger folketrygdens grunnbeløp hvis du lever sammen med ektefelle, partner eller er i et samboerforhold som har vart i minst 12 av de siste 18 månedene. Fra 1. juli 2024 øker denne til 2,709. Er du enslig utgjør minste årlige ytelse 2,91 ganger grunnbeløpet. Fra 1. juli 2024 øker denne til 2,959." },
                    nynorsk { + "Du er innvilga rett til ung ufør. Minste årlege yting er 2,66 gonger folketrygdens grunnbeløp dersom du lever saman med ektefelle, partner eller er i eit sambuarforhold som har vart i minst 12 av dei siste 18 månadene. Frå 1. juli 2024 aukar denne til 2,709. Er du einsleg utgjer minste årlege yting 2,91 gonger grunnbeløpet. Frå 1. juli 2024 aukar denne til 2,959." }
                )
            }
        }

        showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_mottarminsteytelse())){
            paragraph {
                text (
                    bokmal { + "For deg vil minsteytelse utgjøre " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_minsteytelse_sats().format() + " ganger folketrygdens grunnbeløp. Er uføregraden din under 100 prosent, vil minsteytelsen bli justert ut fra uføregraden. Vi justerer også minsteytelsen ut fra trygdetid hvis du har mindre enn 40 års trygdetid. Du må melde fra til Nav dersom sivilstanden din endrer seg, fordi dette kan medføre at uføretrygden endres." },
                    nynorsk { + "For deg vil minsteyting utgjere " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_minsteytelse_sats().format() + " gonger folketrygdens grunnbeløp. Er uføregraden din under 100 prosent, vil minsteytinga bli justert ut frå uføregraden. Vi justerer også minsteytinga ut frå trygdetid dersom du har mindre enn 40 års trygdetid. Du må melde frå til Nav dersom sivilstanden din endrar seg, fordi dette kan medføre at uføretrygda endrar seg." }
                )
            }
        }

        showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_minsteytelse_oppfyltungufor() and pe.ut_vilkargjelderpersonalder().lessThan(20) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_mottarminsteytelse())){
            paragraph {
                text (
                    bokmal { + "Fra måneden du fyller 20 år, vil uføretrygden bli beregnet med rettighet som ung ufør. Beregningen vil da tilsvare " },
                    nynorsk { + "Frå månaden du fyller 20 år, vil uføretrygda bli rekna med rett til ung ufør. Rekninga vil då tilsvare " }
                )

                showIf((pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("bormed 1_5") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("bormed 3-2") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("bormed ektefelle") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("bormed registrert partner"))){
                    text (
                        bokmal { + "2,66" },
                        nynorsk { + "2,66" }
                    )
                }

                showIf((pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().notEqualTo("bormed 1_5") and pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().notEqualTo("bormed 3-2") and pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().notEqualTo("bormed ektefelle") and pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().notEqualTo("bormed registrert partner"))){
                    text (
                        bokmal { + "2,91" },
                        nynorsk { + "2,91" }
                    )
                }
                text (
                    bokmal { + " ganger folketrygdens grunnbeløp. Denne minsteytelsen vil også bli justert for uføregrad og trygdetid." },
                    nynorsk { + " gonger folketrygdens grunnbeløp. Denne minsteytinga vil også bli justert for uføregrad og trygdetid." }
                )
            }
        }
    }
}
