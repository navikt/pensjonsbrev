package no.nav.pensjon.brev.ufore.maler.vedlegg.opplysningerbruktiberegning.fraser

import no.nav.pensjon.brev.ufore.api.model.vedlegg.OpplysningerBruktIBeregningUTLegacyDto.Barnetillegg
import no.nav.pensjon.brev.ufore.api.model.vedlegg.OpplysningerBruktIBeregningUTLegacyDto.Person
import no.nav.pensjon.brev.ufore.api.model.vedlegg.OpplysningerBruktIBeregningUTLegacyDto.Visningsflagg
import no.nav.pensjon.brev.ufore.api.model.vedlegg.SivilstatusVisning
import no.nav.pensjon.brev.ufore.api.model.vedlegg.selectors.opplysningerBruktIBeregningUTLegacyDto.person.*
import no.nav.pensjon.brev.ufore.api.model.vedlegg.selectors.opplysningerBruktIBeregningUTLegacyDto.visningsflagg.*
import no.nav.pensjon.brev.ufore.maler.fraser.Constants.NAV_URL
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.notEqualTo
import no.nav.pensjon.brev.template.dsl.expression.notNull
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text

/**
 * Portert fra legacy TBU052V_TBU073V_EtteroppgjoerAvUforetrygdOgBarnetillegg. Barnetillegg-innvilget
 * og partner-substantiv styres av [Visningsflagg]/[Person.sivilstatusVisning]; gating av selve
 * frasen gjøres i vedlegget.
 */
data class TBU052V_TBU073V_EtteroppgjoerAvUforetrygdOgBarnetillegg(
    val flagg: Expression<Visningsflagg>,
    val barnetilleggFelles: Expression<Barnetillegg?>,
    val barnetilleggSaerkull: Expression<Barnetillegg?>,
    val person: Expression<Person>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        title1 {
            text(
                bokmal { + "Etteroppgjør av uføretrygd" },
                nynorsk { + "Etteroppgjer av uføretrygd" },
            )
            showIf(barnetilleggFelles.notNull() or barnetilleggSaerkull.notNull()) {
                text(
                    bokmal { + " og barnetillegg" },
                    nynorsk { + " og barnetillegg" },
                )
            }
        }

        paragraph {
            text(
                bokmal { + "Hvert år når skattefastsettingen er klar mottar vi opplysninger om inntekten" },
                nynorsk { + "Kvart år når skattefastsettinga er klar, får vi opplysningar om inntekta" },
            )
            showIf(barnetilleggFelles.notNull()) {
                text(
                    bokmal { + " til deg og din " },
                    nynorsk { + " til deg og " },
                )
                ifNotNull(person.sivilstatusVisning) { sivilstatus ->
                    showIf(sivilstatus.equalTo(SivilstatusVisning.GIFT)) {
                        text(bokmal { + "ektefelle" }, nynorsk { + "ektefellen" })
                    }
                    showIf(sivilstatus.equalTo(SivilstatusVisning.PARTNER)) {
                        text(bokmal { + "partner" }, nynorsk { + "partnaren" })
                    }
                    showIf(sivilstatus.equalTo(SivilstatusVisning.SAMBOER_12_13) or sivilstatus.equalTo(SivilstatusVisning.SAMBOER_1_5)) {
                        text(bokmal { + "samboer" }, nynorsk { + "sambuaren" })
                    }
                }
                text(
                    bokmal { + "" },
                    nynorsk { + " din" },
                )
            }
            showIf(not(barnetilleggFelles.notNull())) {
                text(
                    bokmal { + " din" },
                    nynorsk { + " di" },
                )
            }
            text(
                bokmal { + " fra Skatteetaten. Vi bruker opplysningene fra skattefastsettingen til å beregne riktig utbetaling av uføretrygd" },
                nynorsk { + " frå Skatteetaten. Vi bruker opplysningane frå skattefastsettinga til å berekne riktig utbetaling av uføretrygd" },
            )
            showIf(barnetilleggFelles.notNull() or barnetilleggSaerkull.notNull()) {
                text(
                    bokmal { + " og barnetillegg" },
                    nynorsk { + " og barnetillegg" },
                )
            }
            text(
                bokmal { + " for året fastsettingen gjelder for. Har du fått for mye eller for lite utbetalt i uføretrygd" },
                nynorsk { + " for det året som fastsettinga gjeld for. Har du fått for mykje eller for lite utbetalt i uføretrygd" },
            )
            showIf(barnetilleggFelles.notNull() or barnetilleggSaerkull.notNull()) {
                text(
                    bokmal { + " og barnetillegg" },
                    nynorsk { + " og barnetillegg" },
                )
            }
            text(
                bokmal { + ", vil vi foreta et etteroppgjør. " },
                nynorsk { + ", vil vi gjere eit etteroppgjer. " },
            )
            ifNotNull(person.bostedsland) { bostedsland ->
                showIf(bostedsland.notEqualTo("nor") and bostedsland.notEqualTo("")) {
                    text(
                        bokmal { + "Har du meldt inn inntekt fra arbeid i et annet land enn Norge, og vi ikke mottar inntektsopplysninger fra Skatteetaten, gjør vi etteroppgjøret ut fra inntekten din fra utlandet. " },
                        nynorsk { + "Har du meldt inn inntekt frå arbeid i eit anna land enn Noreg, og vi ikkje får opplysningar om inntekt frå Skatteetaten, gjer vi eit etteroppgjer ut frå inntekta di frå utlandet. " },
                    )
                }
            }
            text(
                bokmal { + "Har du fått utbetalt for lite, vil vi utbetale dette beløpet til deg. Har du fått utbetalt for mye, må du betale dette tilbake." },
                nynorsk { + "Har du fått utbetalt for lite, vil vi utbetale dette beløpet til deg. Har du fått utbetalt for mykje, må du betale dette tilbake." },
            )
        }
        paragraph {
            text(
                bokmal { + "Det er viktig at du melder fra om inntektsendringer slik at uføretrygden" },
                nynorsk { + "Det er viktig at du melder frå om inntektsendringar slik at uføretrygda " },
            )
            showIf(barnetilleggFelles.notNull() or barnetilleggSaerkull.notNull()) {
                text(
                    bokmal { + " og barnetillegget" },
                    nynorsk { + "og barnetillegget " },
                )
            }
            text(
                bokmal { + " blir så riktig som mulig. Du kan enkelt melde fra om inntektsendringer under menyvalget " + "uføretrygd".quoted() + " når du logger deg inn på $NAV_URL." },
                nynorsk { + "blir riktig utbetalt. Du kan enkelt melde frå om inntektsendringar under menyvalet " + "uføretrygd".quoted() + " når du loggar deg inn på $NAV_URL." },
            )
        }
    }
}
