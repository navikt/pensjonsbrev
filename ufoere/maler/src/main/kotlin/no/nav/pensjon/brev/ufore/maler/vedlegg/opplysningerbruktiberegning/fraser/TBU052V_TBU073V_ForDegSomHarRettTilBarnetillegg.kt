package no.nav.pensjon.brev.ufore.maler.vedlegg.opplysningerbruktiberegning.fraser

import no.nav.pensjon.brev.ufore.api.model.vedlegg.OpplysningerBruktIBeregningUTLegacyDto.Person
import no.nav.pensjon.brev.ufore.api.model.vedlegg.OpplysningerBruktIBeregningUTLegacyDto.Visningsflagg
import no.nav.pensjon.brev.ufore.api.model.vedlegg.selectors.opplysningerBruktIBeregningUTLegacyDto.person.*
import no.nav.pensjon.brev.ufore.api.model.vedlegg.selectors.opplysningerBruktIBeregningUTLegacyDto.visningsflagg.*
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.isNotEmpty
import no.nav.pensjon.brev.template.dsl.text

/**
 * Portert fra legacy TBU052V_TBU073V_ForDegSomHarRettTilBarnetillegg. Gates av [Visningsflagg.tbu501v].
 * Brevkode-/trygdetid-/yrkesskadebetingelsene er trukket ut til [Visningsflagg].
 */
data class TBU052V_TBU073V_ForDegSomHarRettTilBarnetillegg(
    val flagg: Expression<Visningsflagg>,
    val person: Expression<Person>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        showIf(flagg.tbu501v) {
            title1 {
                text(
                    bokmal { + "For deg som har rett til barnetillegg" },
                    nynorsk { + "For deg som har rett til barnetillegg" },
                )
            }
            paragraph {
                text(
                    bokmal { + "Du har rett til barnetillegg for barn født: " },
                    nynorsk { + "Du har rett til barnetillegg for barn fødd:" },
                )
            }
            showIf(person.foedselsdatoTilBarnTilleggErInnvilgetFor.isNotEmpty()) {
                forEach(person.foedselsdatoTilBarnTilleggErInnvilgetFor) {
                    paragraph {
                        text(
                            bokmal { + it.format() },
                            nynorsk { + it.format() },
                        )
                    }
                }
            }
            paragraph {
                text(
                    bokmal { + "Barnetillegget kan utgjøre opptil 40 prosent av folketrygdens grunnbeløp for hvert barn du forsørger. Du har rett til barnetillegg så lenge du forsørger barn som er under 18 år. Barnetillegget opphører når barnet fyller 18 år. " },
                    nynorsk { + "Barnetillegget kan utgjere opptil 40 prosent av grunnbeløpet i folketrygda for kvart barn du forsørgjer. Du har rett til barnetillegg så lenge du forsørgjer barn som er under 18 år. Barnetillegget opphøyrer når barnet fyller 18 år." },
                )
                showIf(flagg.visBarnetilleggRedusertKortTrygdetid) {
                    text(
                        bokmal { + "Hvor mye du får i barnetillegg er også avhengig av trygdetiden din. Fordi trygdetiden din er kortere enn 40 år, blir barnetillegget ditt redusert. " },
                        nynorsk { + " Kor mykje du får i barnetillegg, er også avhengig av trygdetida di. Fordi trygdetida di er kortare enn 40 år, blir barnetillegget ditt redusert." },
                    )
                }
            }
        }
    }
}
