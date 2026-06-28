package no.nav.pensjon.brev.ufore.maler.vedlegg.opplysningerbruktiberegning.fraser

import no.nav.pensjon.brev.ufore.api.model.vedlegg.OpplysningerBruktIBeregningUTLegacyDto.Visningsflagg
import no.nav.pensjon.brev.ufore.api.model.vedlegg.selectors.opplysningerBruktIBeregningUTLegacyDto.visningsflagg.*
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

/**
 * Portert fra legacy TBU052V_TBU073V_ForDegSomMottarEktefelletillegg. Brevkode-/kravårsak-/
 * minsteytelse-betingelsene er trukket ut til [Visningsflagg].
 */
data class TBU052V_TBU073V_ForDegSomMottarEktefelletillegg(
    val flagg: Expression<Visningsflagg>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        showIf(flagg.visEktefelletilleggInnvilgetIkkeUnntak) {
            title1 {
                text(
                    bokmal { + "For deg som mottar ektefelletillegg" },
                    nynorsk { + "For deg som får ektefelletillegg" },
                )
            }
            paragraph {
                text(
                    bokmal { + "Ektefelletillegget blir utbetalt som et fast tillegg ved siden av uføretrygden. Tillegget blir ikke endret i perioden ektefelletillegget er innvilget." },
                    nynorsk { + "Ektefelletillegget blir utbetalt som eit fast tillegg ved sida av uføretrygda. Tillegget blir ikkje endra i den perioden ektefelletillegget er innvilga for." },
                )
            }
        }

        showIf(flagg.visEktefelletilleggBeregningSeksjon) {
            paragraph {
                showIf(flagg.visEktefelletilleggBeregningUP) {
                    text(
                        bokmal { + "Når vi beregner ektefelletillegget tar vi utgangspunkt i den årlige uførepensjonen du har rett til i desember 2014. Deretter regner vi ut tillegget ut fra fastsatte overgangsregler. " },
                        nynorsk { + "Når vi bereknar ektefelletillegget, tek vi utgangspunkt i den årlege uførepensjonen du har rett til i desember 2014. Deretter reknar vi ut tillegget ut frå fastsette overgangsreglar. " },
                    )
                }
                showIf(flagg.visEktefelletilleggInnvilgetIkkeUnntak) {
                    text(
                        bokmal { + "Du kan beholde ektefelletillegget ut vedtaksperioden, men det opphører senest 31. desember 2024." },
                        nynorsk { + "Du kan behalde ektefelletillegget ut vedtaksperioden, men det tek slutt seinast 31. desember 2024." },
                    )
                }
            }
        }

        showIf(flagg.visEktefelletilleggInnvilgetIkkeUnntak) {
            paragraph {
                text(
                    bokmal { + "Ektefelletillegget vil falle bort hvis du skiller deg, uføretrygden opphører eller hvis ektefellen din dør." },
                    nynorsk { + "Ektefelletillegget fell bort dersom du skil deg, uføretrygda tek slutt eller dersom ektefellen din døyr." },
                )
            }
        }

        showIf(flagg.visEktefelletilleggMinstepensjon60) {
            paragraph {
                text(
                    bokmal { + "Du har minstepensjon og tillegg for ektefelle som har fylt 60 år. Du har derfor rett til å motta uføretrygd som minst tilsvarer 3,76 ganger folketrygdens grunnbeløp. Dette grunnlaget justeres ut fra uføregraden og trygdetiden din, og du beholder dette ut vedtaksperioden for ektefelletillegget. Etter dette vil vi beregne uføretrygden etter ordinære regler." },
                    nynorsk { + "Du har minstepensjon og tillegg for ektefelle som har fylt 60 år. Du har derfor rett til å få uføretrygd som minst tilsvarer 3,76 gonger grunnbeløpet i folketrygda. Dette grunnlaget blir justert ut frå uføregraden din og trygdetida di, og du beheld dette ut vedtaksperioden for ektefelletillegget. Etter dette bereknar vi uføretrygda etter ordinære reglar." },
                )
            }
        }
    }
}
