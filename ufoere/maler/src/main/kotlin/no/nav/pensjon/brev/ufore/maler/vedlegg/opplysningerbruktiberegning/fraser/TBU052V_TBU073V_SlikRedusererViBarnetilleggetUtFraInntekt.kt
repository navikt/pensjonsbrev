package no.nav.pensjon.brev.ufore.maler.vedlegg.opplysningerbruktiberegning.fraser

import no.nav.pensjon.brev.ufore.api.model.vedlegg.OpplysningerBruktIBeregningUTLegacyDto.Person
import no.nav.pensjon.brev.ufore.api.model.vedlegg.OpplysningerBruktIBeregningUTLegacyDto.Visningsflagg
import no.nav.pensjon.brev.ufore.api.model.vedlegg.SivilstatusVisning
import no.nav.pensjon.brev.ufore.api.model.vedlegg.selectors.opplysningerBruktIBeregningUTLegacyDto.person.*
import no.nav.pensjon.brev.ufore.api.model.vedlegg.selectors.opplysningerBruktIBeregningUTLegacyDto.visningsflagg.*
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.ParagraphOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.text

private const val SKATTEETATEN_URL = "skatteetaten.no"

/**
 * Portert fra legacy TBU052V_TBU073V_SlikRedusererViBarnetilleggetUtFraInntekt. Brevkode-/
 * justeringsbeløp-betingelsene er trukket ut til [Visningsflagg]; partner-substantivet velges ut fra
 * [Person.sivilstatusVisning].
 */
data class TBU052V_TBU073V_SlikRedusererViBarnetilleggetUtFraInntekt(
    val flagg: Expression<Visningsflagg>,
    val person: Expression<Person>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        title1 {
            text(
                bokmal { + "Slik reduserer vi barnetillegget ut fra inntekt" },
                nynorsk { + "Slik reduserer vi barnetillegget ut frå inntekt" },
            )
        }

        paragraph {
            text(
                bokmal { + "Størrelsen på barnetillegget er avhengig av inntekt." },
                nynorsk { + "Storleiken på barnetillegget er avhengig av inntekt." },
            )
        }
        paragraph {
            text(
                bokmal { + "Barnetillegget blir redusert ut fra personinntekt. Dette kan for eksempel være:" },
                nynorsk { + "Barnetillegget kan bli redusert ut frå personinntekt. Dette kan til dømes være:" },
            )
            list {
                item { text(bokmal { + "uføretrygd" }, nynorsk { + "uføretrygd" }) }
                item { text(bokmal { + "arbeidsinntekt" }, nynorsk { + "arbeidsinntekt" }) }
                item { text(bokmal { + "næringsinntekt" }, nynorsk { + "næringsinntekt" }) }
                item { text(bokmal { + "inntekt fra utlandet" }, nynorsk { + "inntekt frå utlandet" }) }
                item { text(bokmal { + "ytelser/pensjon fra Norge" }, nynorsk { + "ytingar/pensjon frå Noreg" }) }
                item { text(bokmal { + "pensjon fra utlandet" }, nynorsk { + "pensjon frå utlandet" }) }
            }
            text(
                bokmal { + "Du kan lese mer om personinntekt på $SKATTEETATEN_URL." },
                nynorsk { + "Du kan lese meir om personinntekt på $SKATTEETATEN_URL." },
            )
        }

        paragraph {
            text(
                bokmal { + "Det er inntekten " },
                nynorsk { + "Det er inntekta " },
            )
            showIf(flagg.barnetilleggFellesInnvilget) {
                tilDegOgDinPartner(person)
            }
            showIf(flagg.barnetilleggSaerkullInnvilget and not(flagg.barnetilleggFellesInnvilget)) {
                text(bokmal { + "din " }, nynorsk { + "di " })
            }
            text(
                bokmal { + "som avgjør hva du får utbetalt i barnetillegg i løpet av året. Er inntekten høyere enn fribeløpet blir barnetillegget redusert. " },
                nynorsk { + "som avgjer kva du får utbetalt i barnetillegg i løpet av året. Er inntekta høgare enn fribeløpet blir barnetillegget redusert. " },
            )
        }

        paragraph {
            showIf(flagg.barnetilleggFellesInnvilget) {
                text(
                    bokmal { + "For barn som bor sammen med begge sine foreldre, er fribeløpet 4,6 ganger folketrygdens grunnbeløp. " },
                    nynorsk { + "For barn som bur saman med begge foreldra sine, er fribeløpet 4,6 gonger grunnbeløpet i folketrygda. " },
                )
            }
            showIf(flagg.barnetilleggSaerkullInnvilget) {
                text(
                    bokmal { + "For barn som ikke bor sammen med begge sine foreldre, er fribeløpet 3,1 ganger folketrygdens grunnbeløp. " },
                    nynorsk { + "For barn som ikkje bur saman med begge foreldra, er fribeløpet 3,1 gonger grunnbeløpet i folketrygda. " },
                )
            }
            text(
                bokmal { + "Fribeløpet øker med 0,4 ganger folketrygdens grunnbeløp for hvert ekstra barn. " },
                nynorsk { + "Fribeløpet aukar med 0,4 gonger grunnbeløpet i folketrygda for kvart ekstra barn. " },
            )
        }

        showIf(flagg.visBarnetilleggIkkeUtbetaltPgaInntekt) {
            paragraph {
                text(
                    bokmal { + "Barnetillegget blir redusert med 50 prosent av inntekten som overstiger fribeløpet. Er inntekten " },
                    nynorsk { + "Barnetillegget blir redusert med 50 prosent av inntekta som overstig fribeløpet. Er inntekta " },
                )
                showIf(flagg.visBarnetilleggRedusertTilDegOgPartner) {
                    tilDegOgDinPartner(person)
                }
                showIf(flagg.visBarnetilleggRedusertDin) {
                    text(bokmal { + "din " }, nynorsk { + "di " })
                }
                text(
                    bokmal { + "over grensen for å få utbetalt barnetillegg, blir ikke barnetillegget utbetalt. " },
                    nynorsk { + "over grensa for å få utbetalt barnetillegg, blir ikkje barnetillegget utbetalt." },
                )
            }
        }
    }
}

private fun ParagraphOnlyScope<LangBokmalNynorsk, Unit>.tilDegOgDinPartner(person: Expression<Person>) {
    ifNotNull(person.sivilstatusVisning) { sivilstatus ->
        showIf(sivilstatus.equalTo(SivilstatusVisning.GIFT)) {
            text(bokmal { + "til deg og din ektefelle " }, nynorsk { + "til deg og ektefellen din " })
        }
        showIf(sivilstatus.equalTo(SivilstatusVisning.PARTNER)) {
            text(bokmal { + "til deg og din partner " }, nynorsk { + "til deg og partnaren din " })
        }
        showIf(sivilstatus.equalTo(SivilstatusVisning.SAMBOER_12_13) or sivilstatus.equalTo(SivilstatusVisning.SAMBOER_1_5)) {
            text(bokmal { + "til deg og din samboer " }, nynorsk { + "til deg og sambuaren din " })
        }
    }
}
