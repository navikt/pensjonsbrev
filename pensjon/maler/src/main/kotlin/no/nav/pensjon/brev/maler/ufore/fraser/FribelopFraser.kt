package no.nav.pensjon.brev.maler.ufore.fraser

import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.Fribelopsperiode
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.selectors.fribelopsperiode.faktor
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.selectors.fribelopsperiode.fom
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.selectors.fribelopsperiode.gradsokning
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.selectors.fribelopsperiode.grunnbelop
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.selectors.fribelopsperiode.tom
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.selectors.fribelopsperiode.venteperiodeStartDato
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.text

class Fribelopsperioder(private val perioder: Expression<List<Fribelopsperiode>>) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        paragraph {
            table(header = {
                column(columnSpan = 2) { text(bokmal { +"Fra" }, nynorsk { +"Frå" }) }
                column(columnSpan = 2) { text(bokmal { +"Til" }, nynorsk { +"Til" }) }
                column(columnSpan = 2) { text(bokmal { +"Fribeløp" }, nynorsk { +"Fribeløp" }) }
                column(columnSpan = 3) { text(bokmal { +"Grunnbeløp" }, nynorsk { +"Grunnbeløp" }) }
                column(columnSpan = 4) { text(bokmal { +"Årsak til endring" }, nynorsk { +"Årsak til endring" }) }
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
                            text(
                                bokmal { +periode.grunnbelop.format() },
                                nynorsk { +periode.grunnbelop.format() }
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
    }
}