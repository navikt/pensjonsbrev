package no.nav.pensjon.brev.maler.ufore.endring.bunnfradragNesteAr

import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.FribelopUtvikling
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.FribelopUtviklingKategori.FRIBELOP_04G_TIL_1G
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.FribelopUtviklingKategori.FRIBELOP_04G_TIL_1G_TIL_04G
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.FribelopUtviklingKategori.FRIBELOP_1G_FRA_JAN
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.FribelopUtviklingKategori.FRIBELOP_1G_TIL_04G
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.selectors.fribelopUtvikling.fribelopsokning
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.selectors.fribelopUtvikling.gradsokning
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.selectors.fribelopUtvikling.kategori
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.selectors.fribelopsokning._2arEtterGradsokning
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.selectors.fribelopsokning.dato
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.selectors.gradsokning.dato
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.expression.year
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

data class BegrensetFribelopScenarierOgForklarendeTekst(
    val virkningFom: Expression<LocalDate>,
    val fribelop: Expression<Kroner>,
    val fribelopUtvikling: Expression<FribelopUtvikling>,
    val bunnfradrag: Expression<Kroner>,
    val sokerMottarApIlaAret: Expression<Boolean>,
    val opphorDato: Expression<LocalDate?>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {

        showIf(fribelopUtvikling.kategori.isOneOf(FRIBELOP_04G_TIL_1G, FRIBELOP_04G_TIL_1G_TIL_04G, FRIBELOP_1G_TIL_04G)) {
            title1 {
                text(
                    bokmal { +"Slik beregner vi fribeløp og bunnfradrag" },
                    nynorsk { +"Slik bereknar vi fribeløp og botnfrådrag" }
                )
            }
            paragraph {
                text(
                    bokmal { +"Når fribeløpet endres i løpet av kalenderåret, beregnes det årlige bunnfradraget ut fra periodene du har med ulike fribeløp. Ditt fribeløp i " + virkningFom.year.format() + " er begrenset til " + fribelop.format() + ". " },
                    nynorsk { +"Når fribeløpet blir endra i løpet av kalenderåret, blir det årlege botnfrådraget berekna ut frå periodane du har med ulike fribeløp. Fribeløpet ditt i " + virkningFom.year.format() + " er avgrensa til " + fribelop.format() + ". " }
                )
            }
        }

        includePhrase(BegrensetFribelopHeader)
        showIf(fribelopUtvikling.kategori.equalTo(FRIBELOP_1G_FRA_JAN)) {
            ifNotNull(fribelopUtvikling.fribelopsokning) { fribelopsokning ->
                paragraph {
                    showIf(fribelopsokning._2arEtterGradsokning) {
                        text(
                            bokmal { +"I januar " + virkningFom.year.format() + " er det to år siden din uføregrad økte. Derfor øker fribeløpet ditt i utgangspunktet til 1 G. Fribeløpet begrenses imidlertid til " + fribelop.format() + ". " },
                            nynorsk { +"I januar " + virkningFom.year.format() + " er det to år sidan uføregraden din auka. Derfor aukar fribeløpet ditt i utgangspunktet til 1 G. Fribeløpet blir likevel avgrensa til " + fribelop.format() + ". " }
                        )
                    }.orShow {
                        text(
                            bokmal { +"I januar " + virkningFom.year.format() + " har du hatt uføretrygd i to år. Derfor øker fribeløpet ditt i utgangspunktet til 1 G. Fribeløpet begrenses imidlertid til " + fribelop.format() + ". " },
                            nynorsk { +"I januar " + virkningFom.year.format() + " har du hatt uføretrygd i to år. Derfor aukar fribeløpet ditt i utgangspunktet til 1 G. Fribeløpet blir likevel avgrensa til " + fribelop.format() + ". " }
                        )
                    }
                }
            }

        }.orShowIf(fribelopUtvikling.kategori.equalTo(FRIBELOP_1G_TIL_04G)) {
            ifNotNull(fribelopUtvikling.gradsokning) { gradsokning ->
                paragraph {
                    text(
                        bokmal { +"Fram til " + gradsokning.dato.format() + " er fribeløpet ditt 1 G. Da øker din uføregrad, og fribeløpet settes i utgangspunktet ned til 0,4 G. Fribeløpet begrenses imidlertid til " + fribelop.format() + ". " },
                        nynorsk { +"Fram til " + gradsokning.dato.format() + " er fribeløpet ditt 1 G. Då aukar uføregraden din, og fribeløpet blir i utgangspunktet sett ned til 0,4 G. Fribeløpet blir likevel avgrensa til " + fribelop.format() + ". " }
                    )
                }
            }
        }.orShowIf(fribelopUtvikling.kategori.equalTo(FRIBELOP_04G_TIL_1G_TIL_04G)) {
            ifNotNull(fribelopUtvikling.gradsokning, fribelopUtvikling.fribelopsokning) { gradsokning, fribelopsokning ->
                paragraph {
                    showIf(fribelopsokning._2arEtterGradsokning) {
                        text(
                            bokmal { +"Fram til " + fribelopsokning.dato.format() + " er fribeløpet ditt 0,4 G. Den " + fribelopsokning.dato.format() + " er det to år siden din uføregrad økte. Derfor øker fribeløpet ditt til 1 G. Den " + gradsokning.dato.format() + " øker din uføregrad, og fribeløpet settes i utgangspunktet ned til 0,4 G. Fribeløpet begrenses imidlertid til " + fribelop.format() + ". " },
                            nynorsk { +"Fram til " + fribelopsokning.dato.format() + " er fribeløpet ditt 0,4 G. Den " + fribelopsokning.dato.format() + " er det to år sidan uføregraden din auka. Derfor aukar fribeløpet ditt til 1 G. Den " + gradsokning.dato.format() + " aukar uføregraden din, og fribeløpet blir i utgangspunktet sett ned til 0,4 G. Fribeløpet blir likevel avgrensa til " + fribelop.format() + ". " }
                        )
                    }.orShow {
                        text(
                            bokmal { +"Fram til " + fribelopsokning.dato.format() + " er fribeløpet ditt 0,4 G. Den " + fribelopsokning.dato.format() + " har du hatt uføretrygd i to år. Derfor øker fribeløpet ditt til 1 G. Den " + gradsokning.dato.format() + " øker din uføregrad, og fribeløpet settes i utgangspunktet ned til 0,4 G. Fribeløpet begrenses imidlertid til " + fribelop.format() + ". " },
                            nynorsk { +"Fram til " + fribelopsokning.dato.format() + " er fribeløpet ditt 0,4 G. Den " + fribelopsokning.dato.format() + " har du hatt uføretrygd i to år. Derfor aukar fribeløpet ditt til 1 G. Den " + gradsokning.dato.format() + " aukar uføregraden din, og fribeløpet blir i utgangspunktet sett ned til 0,4 G. Fribeløpet blir likevel avgrensa til " + fribelop.format() + ". " }
                        )
                    }
                }
            }
        }.orShowIf(fribelopUtvikling.kategori.equalTo(FRIBELOP_04G_TIL_1G)) {
            ifNotNull(fribelopUtvikling.fribelopsokning) { fribelopsokning ->
                paragraph {
                    showIf(fribelopsokning._2arEtterGradsokning) {
                        text(
                            bokmal { +"Fram til " + fribelopsokning.dato.format() + " er fribeløpet ditt 0,4 G. Den " + fribelopsokning.dato.format() + " er det to år siden din uføregrad økte. Derfor øker fribeløpet ditt i utgangspunktet til 1 G. Fribeløpet begrenses imidlertid til " + fribelop.format() + ". " },
                            nynorsk { +"Fram til " + fribelopsokning.dato.format() + " er fribeløpet ditt 0,4 G. Den " + fribelopsokning.dato.format() + " er det to år sidan uføregraden din auka. Derfor aukar fribeløpet ditt i utgangspunktet til 1 G. Fribeløpet blir likevel avgrensa til " + fribelop.format() + ". " }
                        )
                    }.orShow {
                        text(
                            bokmal { +"Fram til " + fribelopsokning.dato.format() + " er fribeløpet ditt 0,4 G. Den " + fribelopsokning.dato.format() + " har du hatt uføretrygd i to år. Derfor øker fribeløpet ditt i utgangspunktet til 1 G. Fribeløpet begrenses imidlertid til " + fribelop.format() + ". " },
                            nynorsk { +"Fram til " + fribelopsokning.dato.format() + " er fribeløpet ditt 0,4 G. Den " + fribelopsokning.dato.format() + " har du hatt uføretrygd i to år. Derfor aukar fribeløpet ditt i utgangspunktet til 1 G. Fribeløpet blir likevel avgrensa til " + fribelop.format() + ". " }
                        )
                    }
                }
            }
        }

        includePhrase(BegrensetFribelopTekst(bunnfradrag))
    }
}

class BegrensetFribelopForklarendeTekst(private val bunnfradrag: Expression<Kroner>) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        includePhrase(BegrensetFribelopHeader)
        includePhrase(BegrensetFribelopTekst(bunnfradrag))
    }
}

private object BegrensetFribelopHeader : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        title1 {
            text(
                bokmal { +"Begrensning i fribeløpet og bunnfradraget ditt" },
                nynorsk { +"Avgrensing i fribeløpet og botnfrådraget ditt" }
            )
        }
    }
}

private class BegrensetFribelopTekst(private val bunnfradrag: Expression<Kroner>) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        paragraph {
            text(
                bokmal { +"Du får ikke utbetalt uføretrygd dersom arbeidsinntekten din overstiger 80 prosent av inntekten du hadde før du ble ufør. Dette kaller vi inntektstak. " },
                nynorsk { +"Du får ikkje utbetalt uføretrygd dersom arbeidsinntekta di overstig 80 prosent av inntekta du hadde før du blei ufør. Dette kallar vi inntektstak. " }
            )
        }
        paragraph {
            text(
                bokmal { +"Ifølge reglene kan ikke bunnfradraget være høyere enn inntektstaket. Ditt bunnfradrag er høyere enn inntektstaket, og derfor begrenser vi bunnfradraget ditt til inntektstaket på " + bunnfradrag.format() + ". " },
                nynorsk { +"Ifølgje reglane kan ikkje botnfrådraget vere høgare enn inntektstaket. Botnfrådraget ditt er høgare enn inntektstaket, og derfor avgrensar vi botnfrådraget ditt til inntektstaket på " + bunnfradrag.format() + ". " }
            )
        }
        paragraph {
            text(
                bokmal { +"Hvis du et kalenderår har fått utbetalt uføretrygd og samtidig hatt inntekt over inntektstaket, vil vi kreve tilbake uføretrygden din for det aktuelle året. " },
                nynorsk { +"Dersom du eit kalenderår har fått utbetalt uføretrygd og samtidig hatt inntekt over inntektstaket, vil vi krevje tilbake uføretrygda di for det aktuelle året. " }
            )
        }
    }
}