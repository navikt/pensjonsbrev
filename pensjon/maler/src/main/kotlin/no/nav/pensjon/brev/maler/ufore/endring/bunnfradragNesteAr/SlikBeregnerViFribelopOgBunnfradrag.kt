package no.nav.pensjon.brev.maler.ufore.endring.bunnfradragNesteAr

import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.FribelopUtvikling
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.FribelopUtviklingKategori.FRIBELOP_04G_TIL_1G
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.FribelopUtviklingKategori.FRIBELOP_04G_TIL_1G_TIL_04G
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.FribelopUtviklingKategori.FRIBELOP_1G_FRA_JAN
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.FribelopUtviklingKategori.FRIBELOP_1G_TIL_04G
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.Fribelopsperiode
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.selectors.fribelopUtvikling.fribelopsokning
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.selectors.fribelopUtvikling.gradsokning
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.selectors.fribelopUtvikling.kategori
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.selectors.fribelopsokning._2arEtterGradsokning
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.selectors.fribelopsokning.dato
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.selectors.gradsokning.dato
import no.nav.pensjon.brev.maler.ufore.fraser.Fribelopsperioder
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.notNull
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.expression.year
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

data class SlikBeregnerViFribelopOgBunnfradrag(
    val vedtakVirkFom: Expression<LocalDate>,
    val fribelop: Expression<Kroner>,
    val grunnbelop: Expression<Kroner>,
    val fribelopUtvikling: Expression<FribelopUtvikling>,
    val sokerMottarApIlaAret: Expression<Boolean>,
    val opphorsdato: Expression<LocalDate?>,
    val fribelopsperioder: Expression<List<Fribelopsperiode>>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        showIf(fribelopUtvikling.kategori.equalTo(FRIBELOP_1G_FRA_JAN)) {
            title1 {
                text(
                    bokmal { +"Slik beregner vi fribeløp og bunnfradrag" },
                    nynorsk { +"Slik bereknar vi fribeløp og botnfrådrag" },
                )
            }
            paragraph {
                ifNotNull(fribelopUtvikling.fribelopsokning) { fribelopsokning ->
                    showIf(fribelopsokning._2arEtterGradsokning) {
                        text(
                            bokmal { +"I januar " + vedtakVirkFom.year.format() + " er det to år siden din uføregrad økte. Derfor øker fribeløpet ditt til en ganger folketrygdens grunnbeløp (G). " },
                            nynorsk { +"I januar " + vedtakVirkFom.year.format() + " er det to år sidan uføregraden din auka. Derfor aukar fribeløpet ditt til ein gong grunnbeløpet i folketrygda (G). " }
                        )
                    }.orShow {
                        text(
                            bokmal { +"I januar " + vedtakVirkFom.year.format() + " har du hatt uføretrygd i to år. Derfor øker fribeløpet ditt til en ganger folketrygdens grunnbeløp (G). " },
                            nynorsk { +"I januar " + vedtakVirkFom.year.format() + " har du hatt uføretrygd i to år. Derfor aukar fribeløpet ditt til ein gong grunnbeløpet i folketrygda (G). " }
                        )
                    }
                }
            }
            includePhrase(GrunnbelopFrase(grunnbelop))
        }.orShowIf(fribelopUtvikling.kategori.isOneOf(FRIBELOP_1G_TIL_04G, FRIBELOP_04G_TIL_1G_TIL_04G, FRIBELOP_04G_TIL_1G)) {
            title1 {
                text(
                    bokmal { +"Slik beregner vi fribeløp og bunnfradrag" },
                    nynorsk { +"Slik bereknar vi fribeløp og botnfrådrag" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Når fribeløpet endres i løpet av kalenderåret, beregnes det årlige bunnfradraget ut fra periodene du har med ulike fribeløp. Ditt fribeløp i " + vedtakVirkFom.year.format() + " blir " + fribelop.format() + ". Se tabellen under. " },
                    nynorsk { +"Når fribeløpet blir endra i løpet av kalenderåret, blir det årlege botnfrådraget berekna ut frå periodane du har med ulike fribeløp. Fribeløpet ditt i " + vedtakVirkFom.year.format() + " blir " + fribelop.format() + ". Sjå tabellen under. " }
                )
            }
            title2 {
                text(
                    bokmal { +"Slik beregner vi periodisert fribeløp" },
                    nynorsk { +"Slik bereknar vi periodisert fribeløp" }
                )
            }
            paragraph {
                showIf(fribelopUtvikling.kategori.equalTo(FRIBELOP_1G_TIL_04G)) {
                    ifNotNull(fribelopUtvikling.gradsokning) { gradsokning ->
                        text(
                            bokmal { +"Fram til " + gradsokning.dato.format() + " er fribeløpet ditt en ganger folketrygdens grunnbeløp (G). Da øker din uføregrad, og fribeløpet settes ned til 40 prosent av grunnbeløpet. " },
                            nynorsk { +"Fram til " + gradsokning.dato.format() + " er fribeløpet ditt ein gong grunnbeløpet i folketrygda (G). Då aukar uføregraden din, og fribeløpet blir sett ned til 40 prosent av grunnbeløpet. " }
                        )
                        showIf(not(sokerMottarApIlaAret or opphorsdato.notNull())) {
                            text(
                                bokmal { +"Etter to år vil fribeløpet bli satt opp igjen til en ganger grunnbeløpet. " },
                                nynorsk { +"Etter to år vil fribeløpet bli sett opp igjen til ein gong grunnbeløpet. " }
                            )
                        }
                    }
                }.orShowIf(fribelopUtvikling.kategori.equalTo(FRIBELOP_04G_TIL_1G_TIL_04G)) {
                    ifNotNull(fribelopUtvikling.gradsokning, fribelopUtvikling.fribelopsokning) { gradsokning, fribelopsokning ->
                        showIf(fribelopsokning._2arEtterGradsokning) {
                            text(
                                bokmal { +"Fram til " + fribelopsokning.dato.format() + " er fribeløpet ditt 40 prosent av folketrygdens grunnbeløp (G). Den " + fribelopsokning.dato.format() + " er det to år siden din uføregrad økte. Derfor øker fribeløpet ditt til en ganger grunnbeløpet. Den " + gradsokning.dato.format() + " øker din uføregrad, og fribeløpet settes ned igjen til 40 prosent av grunnbeløpet. " },
                                nynorsk { +"Fram til " + fribelopsokning.dato.format() + " er fribeløpet ditt 40 prosent av grunnbeløpet i folketrygda (G). Den " + fribelopsokning.dato.format() + " er det to år sidan uføregraden din auka. Derfor aukar fribeløpet ditt til ein gong grunnbeløpet. Den " + gradsokning.dato.format() + " aukar uføregraden din, og fribeløpet blir sett ned igjen til 40 prosent av grunnbeløpet. " }
                            )
                        }.orShow {
                            text(
                                bokmal { +"Fram til " + fribelopsokning.dato.format() + " er fribeløpet ditt 40 prosent av folketrygdens grunnbeløp (G). Den " + fribelopsokning.dato.format() + " har du hatt uføretrygd i to år. Derfor øker fribeløpet ditt til en ganger grunnbeløpet. Den " + gradsokning.dato.format() + " øker din uføregrad, og fribeløpet settes ned igjen til 40 prosent av grunnbeløpet. " },
                                nynorsk { +"Fram til " + fribelopsokning.dato.format() + " er fribeløpet ditt 40 prosent av grunnbeløpet i folketrygda (G). Den " + fribelopsokning.dato.format() + " har du hatt uføretrygd i to år. Derfor aukar fribeløpet ditt til ein gong grunnbeløpet. Den " + gradsokning.dato.format() + " aukar uføregraden din, og fribeløpet blir sett ned igjen til 40 prosent av grunnbeløpet. " }
                            )
                        }
                        showIf(not(sokerMottarApIlaAret or opphorsdato.notNull())) {
                            text(
                                bokmal { +"Etter to år vil fribeløpet bli satt opp igjen til en ganger grunnbeløpet. " },
                                nynorsk { +"Etter to år vil fribeløpet bli sett opp igjen til ein gong grunnbeløpet. " }
                            )
                        }
                    }
                }.orShowIf(fribelopUtvikling.kategori.equalTo(FRIBELOP_04G_TIL_1G)) {
                    ifNotNull(fribelopUtvikling.fribelopsokning) { fribelopsokning ->
                        showIf(fribelopsokning._2arEtterGradsokning) {
                            text(
                                bokmal { +"Fram til " + fribelopsokning.dato.format() + " er fribeløpet ditt 40 prosent av folketrygdens grunnbeløp (G). Den " + fribelopsokning.dato.format() + " er det to år siden din uføregrad økte. Derfor øker fribeløpet ditt til en ganger grunnbeløpet. " },
                                nynorsk { +"Fram til " + fribelopsokning.dato.format() + " er fribeløpet ditt 40 prosent av grunnbeløpet i folketrygda (G). Den " + fribelopsokning.dato.format() + " er det to år sidan uføregraden din auka. Derfor aukar fribeløpet ditt til ein gong grunnbeløpet. " }
                            )
                        }.orShow {
                            text(
                                bokmal { +"Fram til " + fribelopsokning.dato.format() + " er fribeløpet ditt 40 prosent av folketrygdens grunnbeløp (G). Den " + fribelopsokning.dato.format() + " har du hatt uføretrygd i to år. Derfor øker fribeløpet ditt til en ganger grunnbeløpet. " },
                                nynorsk { +"Fram til " + fribelopsokning.dato.format() + " er fribeløpet ditt 40 prosent av grunnbeløpet i folketrygda (G). Den " + fribelopsokning.dato.format() + " har du hatt uføretrygd i to år. Derfor aukar fribeløpet ditt til ein gong grunnbeløpet. " }
                            )
                        }
                    }
                }
            }

            includePhrase(GrunnbelopFrase(grunnbelop))
            includePhrase(Fribelopsperioder(fribelopsperioder))
        }
    }
}