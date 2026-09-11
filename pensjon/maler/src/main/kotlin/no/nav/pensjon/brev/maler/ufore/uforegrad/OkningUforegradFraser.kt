package no.nav.pensjon.brev.maler.ufore.uforegrad

import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.OkningUforegradDto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.selectors.okningUforegradDto.fribelopsperiode.faktor
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.selectors.okningUforegradDto.fribelopsperiode.fom
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.selectors.okningUforegradDto.fribelopsperiode.gradsokning
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.selectors.okningUforegradDto.fribelopsperiode.tom
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.selectors.okningUforegradDto.fribelopsperiode.venteperiodeStartDato
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.BrevbakerType

class OkningUforegradFraser(private val perioder: Expression<List<OkningUforegradDto.Fribelopsperiode>>, private val vektetFribelop: Expression<Double>, private val vektetFribelopKr: Expression<BrevbakerType.Kroner>) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        paragraph {
            table(header = {
                column { text(bokmal { +"Fra" }, nynorsk { +"Frå" }) }
                column { text(bokmal { +"Til" }, nynorsk { +"Til" }) }
                column { text(bokmal { +"Fribeløp" }, nynorsk { +"Fribeløp" }) }
                column { text(bokmal { +"Årsak til endring" }, nynorsk { +"Årsak til endring" }) }
            }) {
                forEach(perioder) { periode ->
                    row {
                        cell {
                            text(
                                bokmal { +periode.fom.format(true) },
                                nynorsk { +periode.fom.format(true) }
                            )
                        }
                        cell {
                            text(
                                bokmal { +periode.tom.format(true) },
                                nynorsk { +periode.tom.format(true) }
                            )
                        }
                        cell {
                            text(
                                bokmal { +periode.faktor.format() + " G" },
                                nynorsk { +periode.faktor.format() + " G" }
                            )
                        }
                        cell {
                            showIf(periode.faktor.equalTo(1.0)) {
                                text(
                                    bokmal { +"Venteperiode over" },
                                    nynorsk { +"Venteperiode over" }
                                )
                            }.orShowIf(periode.gradsokning.equalTo(true)) {
                                text(
                                    bokmal { +"Gradsøkning " + periode.venteperiodeStartDato.format(true) },
                                    nynorsk { +"Gradsøkning " + periode.venteperiodeStartDato.format(true) }
                                )
                            }.orShow {
                                text(
                                    bokmal { +"Innvilgelse " + periode.venteperiodeStartDato.format(true) },
                                    nynorsk { +"Innvilgelse " + periode.venteperiodeStartDato.format(true) }
                                )
                            }
                        }
                    }
                }
            }
        }
        paragraph {
            text(
                bokmal { +"Når fribeløpet endres i løpet av året, beregnes et gjennomsnitt av periodene du har hatt med ulikt fribeløp. Gjennomsnittlig fribeløp i år blir " + vektetFribelop.format() + " G, som er " + vektetFribelopKr.format() + "." },
                nynorsk { +"Når fribeløpet endrar seg i løpet av året, vert det rekna ut eit gjennomsnitt av periodane du har hatt med ulikt fribeløp. Gjennomsnittleg fribeløp i år vert " + vektetFribelop.format() + " G, som er " + vektetFribelopKr.format() + "." },
            )
        }
    }
}