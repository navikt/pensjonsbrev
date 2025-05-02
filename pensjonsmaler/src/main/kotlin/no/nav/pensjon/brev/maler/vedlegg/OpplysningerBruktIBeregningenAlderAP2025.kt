package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.api.model.GarantipensjonSatsType
import no.nav.pensjon.brev.api.model.MetaforceSivilstand
import no.nav.pensjon.brev.api.model.MetaforceSivilstand.*
import no.nav.pensjon.brev.api.model.vedlegg.Trygdetid
import no.nav.pensjon.brev.maler.fraser.common.Constants.DIN_PENSJON_URL
import no.nav.pensjon.brev.maler.fraser.common.GarantipensjonSatsTypeText
import no.nav.pensjon.brev.maler.fraser.common.KronerText
import no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningenalder.OpplysningerBruktIBeregningTabellAP2025
import no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningenalder.OpplysningerBruktIBeregningenTrygdetidTabeller
import no.nav.pensjon.brev.maler.vedlegg.OpplysningerBruktIBeregningenAlderAP2025Dto.Pensjonsopptjening.Merknad
import no.nav.pensjon.brev.maler.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.AlderspensjonVedVirkSelectors.garantipensjonInnvilget
import no.nav.pensjon.brev.maler.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.AlderspensjonVedVirkSelectors.uttaksgrad
import no.nav.pensjon.brev.maler.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.BeregnetPensjonPerManedVedVirkSelectors.brukersSivilstand
import no.nav.pensjon.brev.maler.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.BeregningKap20VedVirkSelectors.beholdningForForsteUttak
import no.nav.pensjon.brev.maler.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.BeregningKap20VedVirkSelectors.delingstallLevealder
import no.nav.pensjon.brev.maler.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.BeregningKap20VedVirkSelectors.redusertTrygdetid
import no.nav.pensjon.brev.maler.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.EpsVedVirkSelectors.borSammenMedBruker
import no.nav.pensjon.brev.maler.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.EpsVedVirkSelectors.borSammenMedBruker_safe
import no.nav.pensjon.brev.maler.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.EpsVedVirkSelectors.harInntektOver2G
import no.nav.pensjon.brev.maler.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.EpsVedVirkSelectors.mottarPensjon
import no.nav.pensjon.brev.maler.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.GarantipensjonVedVirkSelectors.beholdningForForsteUttak
import no.nav.pensjon.brev.maler.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.GarantipensjonVedVirkSelectors.delingstalletVed67Ar
import no.nav.pensjon.brev.maler.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.GarantipensjonVedVirkSelectors.garantipensjonSatsPerAr
import no.nav.pensjon.brev.maler.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.GarantipensjonVedVirkSelectors.nettoUtbetaltPerManed
import no.nav.pensjon.brev.maler.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.GarantipensjonVedVirkSelectors.nettoUtbetaltPerManed_safe
import no.nav.pensjon.brev.maler.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.GarantipensjonVedVirkSelectors.satsType
import no.nav.pensjon.brev.maler.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.PensjonsopptjeningKap20VedVirkSelectors.harDagpenger
import no.nav.pensjon.brev.maler.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.PensjonsopptjeningKap20VedVirkSelectors.harMerknadType
import no.nav.pensjon.brev.maler.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.PensjonsopptjeningKap20VedVirkSelectors.harOmsorgsopptjeningFOM2010
import no.nav.pensjon.brev.maler.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.PensjonsopptjeningKap20VedVirkSelectors.harOmsorgsopptjeningTOM2009
import no.nav.pensjon.brev.maler.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.PensjonsopptjeningKap20VedVirkSelectors.harUforepensjon
import no.nav.pensjon.brev.maler.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.PensjonsopptjeningKap20VedVirkSelectors.harUforepensjonKonvertertTilUforetrygd
import no.nav.pensjon.brev.maler.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.PensjonsopptjeningKap20VedVirkSelectors.harUforetrygd
import no.nav.pensjon.brev.maler.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.PensjonsopptjeningKap20VedVirkSelectors.pensjonsopptjeninger
import no.nav.pensjon.brev.maler.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.PensjonsopptjeningSelectors.aarstall
import no.nav.pensjon.brev.maler.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.PensjonsopptjeningSelectors.gjennomsnittligG
import no.nav.pensjon.brev.maler.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.PensjonsopptjeningSelectors.merknader
import no.nav.pensjon.brev.maler.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.PensjonsopptjeningSelectors.pensjonsgivendeinntekt
import no.nav.pensjon.brev.maler.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.TrygdetidsdetaljerKap20VedVirkSelectors.anvendtTT
import no.nav.pensjon.brev.maler.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.VedtakSelectors.sisteOpptejningsAr
import no.nav.pensjon.brev.maler.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.VilkaarsVedtakSelectors.avslattGarantipensjon
import no.nav.pensjon.brev.maler.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.alderspensjonVedVirk
import no.nav.pensjon.brev.maler.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.beregnetPensjonPerManedVedVirk
import no.nav.pensjon.brev.maler.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.beregningKap20VedVirk
import no.nav.pensjon.brev.maler.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.epsVedVirk
import no.nav.pensjon.brev.maler.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.erBeregnetSomEnsligPgaInstitusjonsopphold
import no.nav.pensjon.brev.maler.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.garantipensjonVedVirk
import no.nav.pensjon.brev.maler.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.pensjonsopptjeningKap20VedVirk
import no.nav.pensjon.brev.maler.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.trygdetidNorge
import no.nav.pensjon.brev.maler.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.trygdetidsdetaljerKap20VedVirk
import no.nav.pensjon.brev.maler.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.vedtak
import no.nav.pensjon.brev.maler.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.vilkarsVedtak
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

data class OpplysningerBruktIBeregningenAlderAP2025Dto(
    val alderspensjonVedVirk: AlderspensjonVedVirk,
    val beregnetPensjonPerManedVedVirk: BeregnetPensjonPerManedVedVirk,
    val beregningKap20VedVirk: BeregningKap20VedVirk,
    val vilkarsVedtak: VilkaarsVedtak,
    val vedtak: Vedtak,
    val garantipensjonVedVirk: GarantipensjonVedVirk?,
    val trygdetidsdetaljerKap20VedVirk: TrygdetidsdetaljerKap20VedVirk,
    val epsVedVirk: EpsVedVirk?,
    val erBeregnetSomEnsligPgaInstitusjonsopphold: Boolean,
    val trygdetidNorge: List<Trygdetid>,
    val trygdetidEOS: List<Trygdetid>,
    val trygdetidAvtaleland: List<Trygdetid>,
    val pensjonsopptjeningKap20VedVirk: PensjonsopptjeningKap20VedVirk,
) {

    data class PensjonsopptjeningKap20VedVirk(
        val harOmsorgsopptjeningFOM2010: Boolean,
        val harOmsorgsopptjeningTOM2009: Boolean,
        val harDagpenger: Boolean,
        val harUforetrygd: Boolean,
        val harUforepensjonKonvertertTilUforetrygd: Boolean,
        val harUforepensjon: Boolean,
        val harMerknadType: Boolean,
        val pensjonsopptjeninger: List<Pensjonsopptjening>,
    )

    data class Pensjonsopptjening(
        val aarstall: Int,
        val pensjonsgivendeinntekt: Kroner,
        val gjennomsnittligG: Kroner,
        val merknader: List<Merknad>,
    ) {
        enum class Merknad {
            DAGPENGER,
            OMSORGSOPPTJENING,
            UFORETRYGD,
            UFOREPENSJON,
        }
    }

    data class EpsVedVirk(
        val borSammenMedBruker: Boolean,
        val harInntektOver2G: Boolean,
        val mottarPensjon: Boolean,

        )

    data class Vedtak(
        val sisteOpptejningsAr: Int
    )

    data class TrygdetidsdetaljerKap20VedVirk(
        val anvendtTT: Int,
    )

    data class GarantipensjonVedVirk(
        val delingstalletVed67Ar: Double,
        val satsType: GarantipensjonSatsType,
        val garantipensjonSatsPerAr: Kroner,
        val nettoUtbetaltPerManed: Kroner,
        val beholdningForForsteUttak: Kroner,
    )

    data class VilkaarsVedtak(
        val avslattGarantipensjon: Boolean,
    )

    data class BeregningKap20VedVirk(
        val beholdningForForsteUttak: Kroner,
        val delingstallLevealder: Double,
        val redusertTrygdetid: Boolean,
    )

    data class AlderspensjonVedVirk(
        val uttaksgrad: Int,
        val beregningVirkDatoFom: LocalDate,
        val garantipensjonInnvilget: Boolean,
        val nettoUtbetaltPerManed: Kroner,
    )

    data class BeregnetPensjonPerManedVedVirk(
        val virkDatoFom: LocalDate,
        val brukersSivilstand: MetaforceSivilstand,
    )
}

@TemplateModelHelpers
val vedleggOpplysningerBruktIBeregningenAlderAP2025 =
    createAttachment<LangBokmalNynorskEnglish, OpplysningerBruktIBeregningenAlderAP2025Dto>(
        //vedleggBeregnAP2025Tittel_001
        title = newText(
            Bokmal to "Slik har vi beregnet pensjonen din",
            Nynorsk to "Slik har vi berekna pensjonen din",
            English to "This is how we have calculated your pension",
        ),
        includeSakspart = false
    ) {
        //vedleggBeregnInnlednAP2025_001
        paragraph {
            text(
                Bokmal to "I dette vedlegget finner du opplysninger som vi har brukt for å regne ut alderspensjonen din.",
                Nynorsk to "I dette vedlegget finn du opplysningar som vi har brukt for å rekne ut alderspensjonen din.",
                English to "In this attachment, you will find information that we have used to calculate your retirement pension.",
            )
        }
        paragraph {
            text(
                Bokmal to "Hvis du mener at opplysningene er feil, må du melde fra til Nav, fordi det kan ha betydning for størrelsen på pensjonen din.",
                Nynorsk to "Viss du meiner at opplysningane er feil, må du melde frå til Nav, fordi det kan ha noko å seie for storleiken på pensjonen din.",
                English to "If you believe any information is incorrect, you must notify Nav, as it may affect the amount of your pension.",
            )
        }
        //vedleggBeregnUttaksgrad_001
        paragraph {
            textExpr(
                Bokmal to "Uttaksgraden for alderspensjonen din er ".expr() + alderspensjonVedVirk.uttaksgrad.format() + " prosent.",
                Nynorsk to "Uttaksgraden for alderspensjonen din er ".expr() + alderspensjonVedVirk.uttaksgrad.format() + " prosent.",
                English to "The rate of your retirement pension is ".expr() + alderspensjonVedVirk.uttaksgrad.format() + " percent.",
            )
        }
        includePhrase(
            OpplysningerBruktIBeregningTabellAP2025(
                alderspensjonVedVirk = alderspensjonVedVirk,
                beregningKap20VedVirk = beregningKap20VedVirk,
                vilkarsVedtak = vilkarsVedtak,
                trygdetidsdetaljerKap20VedVirk = trygdetidsdetaljerKap20VedVirk,
                garantipensjonVedVirk = garantipensjonVedVirk,
            )
        )

        val garantipensjonInnvilget = alderspensjonVedVirk.garantipensjonInnvilget
        val avslattGarantipensjon = vilkarsVedtak.avslattGarantipensjon
        val redusertTrygdetid = beregningKap20VedVirk.redusertTrygdetid
        showIf(
            garantipensjonInnvilget
                    and (redusertTrygdetid or avslattGarantipensjon)
        ) {
            //vedleggBeregnPensjonsBeholdning_001
            title1 {
                text(
                    Bokmal to "Pensjonsbeholdning",
                    Nynorsk to "Pensjonsbehaldning",
                    English to "Accumulated pension capital",
                )
            }
            paragraph {
                text(
                    Bokmal to "Pensjonsbeholdningen ved uttaket er summen av all pensjonsopptjening fra tidligere år. Du kan tjene opp inntektspensjon fra og med det året du fyller 17. Den årlige pensjonsopptjeningen din er 18,1 prosent av samlet opptjeningsgrunnlag opp til 7,1 av folketrygdens gjennomsnittlige grunnbeløp (G) for dette året. Pensjonsbeholdningen er regulert hvert år fram til du begynner å ta ut alderspensjon.",
                    Nynorsk to "Pensjonsbehaldninga ved uttaket er summen av all pensjonsopptening frå tidlegare år. Du kan tene opp inntektspensjon frå og med det året du fyller 17. Den årlege pensjonsoppteninga di er 18,1 prosent av samla opptjeningsgrunnlag opp til 7,1 av folketrygdens gjennomsnittlege grunnbeløp (G). Pensjonsbehaldninga er regulert kvart år med lønnsveksten fram til du byrjar å ta ut alderspensjon.",
                    English to "The accumulated pension capital is the sum of all pension accruals from previous years. You can earn income pension starting from the year you turn 17. Your annual pension accrual is 18.1 percent of the total earnings basis up to 7.1 times the National Insurance average basic amount (G) for that year. The pension balance is adjusted annually until you begin to receive retirement pension.",
                )
            }
        }

        showIf(
            (garantipensjonInnvilget and garantipensjonVedVirk.nettoUtbetaltPerManed_safe.ifNull(Kroner(0))
                .greaterThan(0))
                    or (redusertTrygdetid and not(avslattGarantipensjon))
        ) {
            //vedleggBeregnPensjonsBeholdning_001
            title1 {
                text(
                    Bokmal to "Pensjonsbeholdning",
                    Nynorsk to "Pensjonsbehaldning",
                    English to "Accumulated pension capital",
                )
                showIf(redusertTrygdetid) {
                    text(
                        Bokmal to " og trygdetid",
                        Nynorsk to " og trygdetid",
                        English to " and Norwegian national insurance coverage",
                    )
                }
            }
            paragraph {
                text(
                    Bokmal to "Pensjonsbeholdningen ved uttaket er summen av all pensjonsopptjening fra tidligere år. Du kan tjene opp inntektspensjon fra og med det året du fyller 17. Den årlige pensjonsopptjeningen din er 18,1 prosent av samlet opptjeningsgrunnlag opp til 7,1 av folketrygdens gjennomsnittlige grunnbeløp (G) for dette året. Pensjonsbeholdningen er regulert hvert år fram til du begynner å ta ut alderspensjon.",
                    Nynorsk to "Pensjonsbehaldninga ved uttaket er summen av all pensjonsopptening frå tidlegare år. Du kan tene opp inntektspensjon frå og med det året du fyller 17. Den årlege pensjonsoppteninga di utgjer 18,1 prosent av samla opptjeningsgrunnlag opp til 7,1 av det gjennomsnittleg grunnbeløpet i folketrygda for dette året. Pensjonsbehaldninga er regulert kvart år fram til du byrjar å ta ut alderspensjon.",
                    English to "The accumulated pension capital is the sum of all pension accruals from previous years. You can earn income pension starting from the year you turn 17. Your annual pension accrual amounts to 18.1 percent of the total earnings basis up to 7.1 times the average National Insurance basic amount (G). The accumulated pension capital is adjusted annually until you begin to receive retirement pension.",
                )
            }

            //vedleggBeregnTrygdetidKap20_001
            paragraph {
                text(
                    Bokmal to "Trygdetid er perioder du har vært medlem i folketrygden. Som hovedregel er dette perioder du har bodd eller arbeidet i Norge fra fylte 16 år til og med det året du fyller 66 år. Trygdetid har betydning for beregning av garantipensjon. Full trygdetid er 40 år.",
                    Nynorsk to "Trygdetid er periodar du har vore medlem i folketrygda. Som hovudregel er dette periodar du har budd eller arbeidd i Noreg frå fylte 16 år til og med det året du fyller 66 år. Trygdetid har betydning for berekninga av garantipensjon. Full trygdetid er 40 år.",
                    English to "National insurance coverage refers to the periods during which you have been a member of the Norwegian National Insurance Scheme. As a general rule, these periods include the time you have lived or worked in Norway from the age of 16 until the year you turn 66. The period of national insurance coverage is relevant for calculating the guaranteed pension, full coverage requires 40 years.",
                )
            }
        }

        showIf(vedtak.sisteOpptejningsAr.lessThan(2023)) {
            //forklaringSisteOpptjeningsaar_001
            paragraph {
                text(
                    Bokmal to "Pensjonsopptjening",
                    Nynorsk to "Pensjonsopptening",
                    English to "Your pension accruals",
                )
                showIf(
                    (garantipensjonInnvilget
                            and garantipensjonVedVirk.nettoUtbetaltPerManed_safe.ifNull(Kroner(0)).greaterThan(0))
                            or (not(garantipensjonInnvilget) and redusertTrygdetid)
                ) {
                    text(
                        Bokmal to " og trygdetid",
                        Nynorsk to " og trygdetid",
                        English to " and your national insurance coverage",
                    )
                }
                textExpr(
                    Bokmal to " tas med i beregningen av alderspensjon fra og med året etter at skatteoppgjøret er klart. Dette gjelder selv om skatteoppgjøret ditt er klart tidligere. I beregningen er det derfor brukt pensjonsopptjening til og med ".expr()
                            + vedtak.sisteOpptejningsAr.format() + ".",
                    Nynorsk to " blir teke med i berekninga av alderspensjon frå og med året etter at skatteoppgjeret er klart. Dette gjeld sjølv om skatteoppgjeret ditt er klart tidlegare. I berekninga er det difor brukt pensjonsopptening til og med ".expr()
                            + vedtak.sisteOpptejningsAr.format() + ".",
                    English to " are taken into account when calculating retirement pension starting from the year after your tax assessment is finalised. This applies even if your tax assessment is completed earlier. In the calculation, pension accruals are used up to and including ".expr()
                            + vedtak.sisteOpptejningsAr.format() + ".",
                )
            }
            //forklaringSisteOpptjeningsaar_001
            paragraph {
                text(
                    Bokmal to "Du vil få et nytt vedtak fra oss når pensjonen din blir omregnet.",
                    Nynorsk to "Du vil få eit nytt vedtak frå oss når pensjonen din blir omrekna.",
                    English to "You will receive a new decision from us when your pension is calculated.",
                )
            }
        }
        //vedleggBeregnDelingstall_001
        title1 {
            text(
                Bokmal to "Delingstall",
                Nynorsk to "Delingstall",
                English to "Life expectancy adjustment divisor at withdrawal",
            )
        }
        paragraph {
            text(
                Bokmal to "Delingstallet uttrykker forventet levetid for ditt årskull på uttakstidspunktet. Alderen din ved uttak avgjør hvilket delingstall som gjelder for deg. Dette blir kalt levealdersjustering.",
                Nynorsk to "Delingstalet uttrykkjer forventa levetid for årskullet ditt på uttakstidspunktet. Alderen din ved uttak avgjer kva delingstal som gjeld for deg. Dette blir kalla levealdersjustering.",
                English to "The divisor expresses the life expectancy for people born in the same year (cohort) at a specific pension withdrawal date. Your age at retirement determines which divisor applies to you. This is called life expectancy adjustment.",
            )
        }
        //vedleggBeregnInntektspensjonOverskrift_001
        title1 {
            text(
                Bokmal to "Inntektspensjon",
                Nynorsk to "Inntektspensjon",
                English to "Income pension",
            )
        }
        showIf(alderspensjonVedVirk.uttaksgrad.equalTo(100)) {
            paragraph {
                textExpr(
                    Bokmal to "Din årlige inntektspensjon blir beregnet ved å dele pensjonsbeholdningen din på delingstallet ved uttak. Pensjonsbeholdningen din er på ".expr() +
                            beregningKap20VedVirk.beholdningForForsteUttak.format() + " kroner, og delingstallet ved uttak er "
                            + beregningKap20VedVirk.delingstallLevealder.format() + ".",

                    Nynorsk to "Den årlege inntektspensjonen din blir rekna ut ved å dele pensjonsbehaldninga di på delingstalet ved uttak. Pensjonsbehaldninga di er på ".expr() +
                            beregningKap20VedVirk.beholdningForForsteUttak.format() + " kroner, og delingstalet ved uttak er "
                            + beregningKap20VedVirk.delingstallLevealder.format() + ".",

                    English to "Your annual income pension is calculated by dividing your pension capital by the life expectancy adjustment divisor at the time of the withdrawal. Your accumulated pension capital is NOK ".expr() +
                            beregningKap20VedVirk.beholdningForForsteUttak.format() + ", and the divisor at the time of withdrawal is "
                            + beregningKap20VedVirk.delingstallLevealder.format() + ".",
                )
            }
        }.orShow {
            //vedleggBeregnInntektspensjonGradertUttak_001
            paragraph {
                textExpr(
                    Bokmal to "Din årlige inntektspensjon blir beregnet ved å dele pensjonsbeholdningen din på delingstallet ved uttak. Pensjonsbeholdningen din er på ".expr() +
                            beregningKap20VedVirk.beholdningForForsteUttak.format() + " kroner, og delingstallet ved uttak er " +
                            beregningKap20VedVirk.delingstallLevealder.format() + ". Siden du ikke tar ut full pensjon, vil du kun få utbetalt "
                            + alderspensjonVedVirk.uttaksgrad.format() + " prosent av dette beløpet.",

                    Nynorsk to "Den årlege inntektspensjonen din blir rekna ut ved å dele pensjonsbehaldninga di på delingstalet ved uttak. Pensjonsbehaldninga di er på ".expr() +
                            beregningKap20VedVirk.beholdningForForsteUttak.format() + " kroner, og delingstalet ved uttak er " +
                            beregningKap20VedVirk.delingstallLevealder.format() + ".  Sidan du ikkje tek ut full pensjon, vil du berre få utbetalt "
                            + alderspensjonVedVirk.uttaksgrad.format() + " prosent av dette beløpet.",

                    English to "Your annual income pension is calculated by dividing your accumulated pension capital by the life expectancy adjustment divisor at the time of the initial withdrawal. Your accumulated pension capital is NOK ".expr() +
                            beregningKap20VedVirk.beholdningForForsteUttak.format() + ", and the divisor at the time of withdrawal is " +
                            beregningKap20VedVirk.delingstallLevealder.format() + ". Since you are not withdrawing the full pension, you will only receive "
                            + alderspensjonVedVirk.uttaksgrad.format() + " percent of this amount.",
                )
            }
        }
        showIf(
            garantipensjonVedVirk.nettoUtbetaltPerManed_safe.equalTo(0)
                    and not(avslattGarantipensjon)
                    and redusertTrygdetid
        ) {
            //vedleggBeregnGarantipensjonIkkeUtbetales_001
            title1 {
                text(
                    Bokmal to "Garantipensjon",
                    Nynorsk to "Garantipensjon",
                    English to "Guaranteed pension",
                )
            }
            paragraph {
                text(
                    Bokmal to "Inntektspensjonen din er så høy at du får ikke utbetalt garantipensjon.",
                    Nynorsk to "Inntektspensjonen din er så høg at du får ikkje utbetalt garantipensjon.",
                    English to "You are not eligible for a guaranteed pension because your income pension exceeds the threshold.",
                )
            }
            paragraph {
                text(
                    Bokmal to "Garantipensjonen skal sikre et visst minimum på pensjonen. Størrelsen på garantipensjonen fastsettes ut fra en sats som er avhengig av din sivilstand. Satsen blir redusert når trygdetiden din er under 40 år.",
                    Nynorsk to "Garantipensjonen skal sikre eit visst minimum på pensjonen. Størrelsen på garantipensjonen fastsettast ut frå ein sats som er avhengig av din sivilstand. Satsen blir redusert når trygdetida di er under 40 år.",
                    English to "The guaranteed pension ensures a minimum level for the pension. The size of the guaranteed pension is based on a rate that depends on your marital status. The rate is reduced if your national insurance coverage is less than 40 years.",
                )
            }
        }


        //vedleggBeregnGarantipensjonOverskrift_001
        ifNotNull(garantipensjonVedVirk) { garantipensjonVedVirk ->
            showIf(
                garantipensjonInnvilget
                        and garantipensjonVedVirk.nettoUtbetaltPerManed.greaterThan(0)
            ) {
                title1 {
                    text(
                        Bokmal to "Garantipensjon",
                        Nynorsk to "Garantipensjon",
                        English to "Guaranteed pension",
                    )
                }
                showIf(alderspensjonVedVirk.uttaksgrad.equalTo(100)) {
                    //vedleggBeregnGarantipensjonUtbetaltFullUttak_001
                    paragraph {
                        textExpr(
                            Bokmal to "Din årlige garantipensjon blir beregnet ved å dele garantipensjonsbeholdningen din på delingstallet ved uttak. Garantipensjonsbeholdningen din er på ".expr() +
                                    garantipensjonVedVirk.beholdningForForsteUttak.format() + " kroner, og delingstallet ved uttak er " +
                                    beregningKap20VedVirk.delingstallLevealder.format() + ".",

                            Nynorsk to "Den årlege garantipensjonen din blir rekna ut ved å dele garantipensjonsbeholdninga di på delingstalet ved uttak. Garantipensjonsbeholdninga di er på ".expr() +
                                    garantipensjonVedVirk.beholdningForForsteUttak.format() + " kroner, og delingstalet ved uttak er " +
                                    beregningKap20VedVirk.delingstallLevealder.format() + ".",

                            English to "Your annual guaranteed pension is calculated by dividing your guaranteed pension capital by the life expectancy divisor at the time of the initial withdrawal. Your guaranteed pension capital is NOK ".expr() +
                                    garantipensjonVedVirk.beholdningForForsteUttak.format() + ", and the divisor at withdrawal is " +
                                    beregningKap20VedVirk.delingstallLevealder.format() + ".",
                        )
                    }
                }.orShow {
                    //vedleggBeregnGarantipensjonUtbetaltGradertUttak_001
                    paragraph {
                        textExpr(
                            Bokmal to "Din årlige garantipensjon blir beregnet ved å dele garantipensjonsbeholdningen din på delingstallet ved uttak. Garantipensjonsbeholdningen din er på ".expr() +
                                    garantipensjonVedVirk.beholdningForForsteUttak.format() +
                                    " kroner, og delingstallet ved uttak er " + beregningKap20VedVirk.delingstallLevealder.format() +
                                    ". Siden du ikke tar ut full pensjon, vil du kun få utbetalt " +
                                    alderspensjonVedVirk.uttaksgrad.format() + " prosent av dette beløpet.",

                            Nynorsk to "Den årlege garantipensjonen din blir rekna ut ved å dele garantipensjonsbeholdninga di på delingstalet ved uttak. Garantipensjonsbeholdninga di er på ".expr() +
                                    garantipensjonVedVirk.beholdningForForsteUttak.format() +
                                    " kroner, og delingstalet ved uttak er " + beregningKap20VedVirk.delingstallLevealder.format() +
                                    ". Sidan du ikkje tek ut full pensjon, vil du berre få utbetalt " +
                                    alderspensjonVedVirk.uttaksgrad.format() + " prosent av dette beløpet.",

                            English to "Your annual guaranteed pension is calculated by dividing your guaranteed pension capital by the life expectancy divisor at the time of the initial withdrawal. Your guaranteed pension capital is NOK ".expr() +
                                    garantipensjonVedVirk.beholdningForForsteUttak.format() +
                                    ", and the life expectancy divisor at withdrawal is " + beregningKap20VedVirk.delingstallLevealder.format() +
                                    ". Since you are not taking out full pension, you will only receive " +
                                    alderspensjonVedVirk.uttaksgrad.format() + " percent of this amount.",
                        )
                    }
                }

                //vedleggBeregnGarantipensjonsbeholdningInnledning_001
                title1 {
                    text(
                        Bokmal to "Garantipensjonsbeholdningen din er beregnet slik:",
                        Nynorsk to "Garantipensjonsbehaldninga di er rekna ut slik:",
                        English to "Your guaranteed pension capital is calculated as follows:",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Garantipensjonen skal sikre deg et visst minstenivå på pensjonen din. Garantipensjonen reduseres med inntektspensjonen. Størrelsen på garantipensjonen fastsettes ut fra en sats som er avhengig av din sivilstand. Satsen blir redusert hvis trygdetiden din er under 40 år.",
                        Nynorsk to "Garantipensjonen skal sikre deg eit visst minstenivå på pensjonen din. Garantipensjonen blir redusert med inntektspensjonen. Storleiken på garantipensjonen blir fastsett ut frå ein sats som er avhengig av sivilstanden din. Satsen blir redusert dersom trygdetida di er under 40 år.",
                        English to "The guaranteed pension ensures a certain minimum level for your pension. The guaranteed pension is reduced by the income pension. The size of the guaranteed pension is based on a rate that depends on your marital status. The rate is reduced if your national insurance coverage period is less than 40 years.",
                    )
                }
                val brukersSivilstand = beregnetPensjonPerManedVedVirk.brukersSivilstand
                showIf(brukersSivilstand.isOneOf(GIFT, PARTNER)) {
                    val erGift = beregnetPensjonPerManedVedVirk.brukersSivilstand.equalTo(GIFT)
                    paragraph {
                        showIf(erGift) {
                            //vedleggBeregnGift_001
                            text(
                                Bokmal to "Vi har lagt til grunn at du er gift.",
                                Nynorsk to "Vi har lagt til grunn at du er gift.",
                                English to "We have registered that you have a spouse.",
                            )
                        }.orShow {
                            //vedleggBeregnPartner_001
                            text(
                                Bokmal to "Vi har lagt til grunn at du er partner.",
                                Nynorsk to "Vi har lagt til grunn at du er partnar.",
                                English to "We have registered that you have a partner.",
                            )
                        }
                    }

                    ifNotNull(epsVedVirk) { epsVedVirk ->
                        paragraph {
                            showIf(erBeregnetSomEnsligPgaInstitusjonsopphold) {
                                showIf(erGift) {
                                    //vedleggBeregnGiftLeverAdskiltInstitusjon_001
                                    text(
                                        Bokmal to "Du og ektefellen din er registrert med forskjellig bosted da en av dere bor på institusjon. Pensjonen din er derfor beregnet som om du var enslig. Satsen vi bruker er derfor ",
                                        Nynorsk to "Du og ektefellen din er registrerte med forskjellig bustad da ein av dykk bur på institusjon. Pensjonen din er derfor berekna som om du var einsleg. Satsen vi brukar er difor ",
                                        English to "You and your spouse are registered with different residences as one of you is residing in an institution. Therefore, your pension has been calculated as if you were single. The rate we use is therefore the ",
                                    )
                                }.orShow {
                                    //vedleggBeregnPartnerLeverAdskiltInstituton_001
                                    text(
                                        Bokmal to "Du og partneren din er registrert med forskjellig bosted da en av dere bor på institusjon. Pensjonen din er derfor beregnet som om du var enslig. Satsen vi bruker er derfor ",
                                        Nynorsk to "Du og partnaren din er registrerte med forskjellig bustad da en dere bor på institusjon. Pensjonen din er derfor berekna som om du var einsleg. Satsen vi bruker er difor ",
                                        English to "You and your partner are registered with different residences since one of you resides in an institution. Therefore, your pension has been calculated as if you were single. The rate we use is therefore the ",
                                    )
                                }
                            }.orShowIf(not(epsVedVirk.borSammenMedBruker)) {
                                showIf(erGift) {
                                    //vedleggBeregnGiftLeverAdskilt_002
                                    text(
                                        Bokmal to "Du og ektefellen din er registrert med forskjellig bosted. Pensjonen din er derfor beregnet som om du var enslig. Satsen vi bruker er derfor ",
                                        Nynorsk to "Du og ektefellen din er registrerte med forskjellig bustad. Pensjonen din er derfor berekna som om du var einsleg. Satsen vi brukar er difor ",
                                        English to "You and your spouse are registered with different residences. Therefore, your pension has been calculated as if you were single. The rate we use is therefore the ",
                                    )
                                }.orShow {
                                    //vedleggBeregnPartnerLeverAdskilt_002
                                    text(
                                        Bokmal to "Du og partneren din er registrert med forskjellig bosted. Pensjonen din er derfor beregnet som om du var enslig. Satsen vi bruker er derfor ",
                                        Nynorsk to "Du og partnaren din er registrerte med forskjellig bustad. Pensjonen di er difor rekna som om du var einsleg. Satsen vi brukar er difor ",
                                        English to "You and your partner are registered with different residences. Therefore, your pension has been calculated as if you were single. The rate we use is therefore the ",
                                    )
                                }
                            }.orShowIf(epsVedVirk.mottarPensjon) {
                                showIf(erGift) {
                                    //vedleggBeregnEktefellePensjon_002
                                    text(
                                        Bokmal to "Vi har registrert at ektefellen din mottar uføretrygd, alderspensjon fra folketrygden eller AFP som det godskrives pensjonspoeng for. Satsen vi bruker er derfor ",
                                        Nynorsk to "Vi har registrert at ektefellen din får uføretrygd, alderspensjon frå folketrygda eller AFP som det blir godskrive pensjonspoeng for. Satsen vi brukar er difor ",
                                        English to "We have registered that your spouse is receiving disability benefit, a national insurance retirement pension or contractual early retirement pension (AFP) which earns pension points. The rate we use is therefore the ",
                                    )
                                }.orShow {
                                    //vedleggBeregnPartnerPensjon_002
                                    text(
                                        Bokmal to "Vi har registrert at partneren din mottar uføretrygd, alderspensjon fra folketrygden eller AFP som det godskrives pensjonspoeng for. Satsen vi bruker er derfor ",
                                        Nynorsk to "Vi har registrert at partnaren din får uføretrygd, alderspensjon frå folketrygda eller AFP som det blir godskrive pensjonspoeng for. Satsen vi brukar er difor ",
                                        English to "We have registered that your partner is receiving disability benefit, a national insurance retirement pension or contractual early retirement pension (AFP) which earns pension points. The rate we use is therefore the ",
                                    )
                                }
                            }.orShowIf(not(epsVedVirk.mottarPensjon)) {
                                showIf(epsVedVirk.harInntektOver2G) {
                                    showIf(erGift) {
                                        //vedleggBeregnEktefelleOver2G_002
                                        text(
                                            Bokmal to "Vi har registrert at ektefellen din har en inntekt som er høyere enn to ganger folketrygdens grunnbeløp (G). Satsen vi bruker er derfor ",
                                            Nynorsk to "Vi har registrert at ektefellen din har ei inntekt som er høgare enn to gonger grunnbeløpet i folketrygda (G). Satsen vi brukar er difor ",
                                            English to "We have registered that your spouse has an annual income that exceeds twice the national insurance basic amount (G). The rate we use is therefore the ",
                                        )
                                    }.orShow {
                                        text(
                                            Bokmal to "Vi har registrert at partneren din har en inntekt som er høyere enn to ganger folketrygdens grunnbeløp (G). Satsen vi bruker er derfor ",
                                            Nynorsk to "Vi har registrert at partnaren din har ei inntekt som er høgare enn to gonger grunnbeløpet i folketrygda (G). Satsen vi brukar er difor ",
                                            English to "We have registered that your partner has an annual income that exceeds twice the national insurance basic amount (G). The rate we use is therefore the ",
                                        )
                                    }
                                }.orShow {
                                    showIf(erGift) {
                                        text(
                                            Bokmal to "Vi har registrert at ektefellen din har en inntekt som er lavere enn to ganger folketrygdens grunnbeløp (G). Satsen vi bruker er derfor ",
                                            Nynorsk to "Vi har registrert at ektefellen din har ei inntekt som er lågare enn to gonger grunnbeløpet i folketrygda (G). Satsen vi brukar er difor ",
                                            English to "We have registered that your spouse has an annual income lower than twice the national insurance basic amount (G). The rate we use is therefore the ",
                                        )
                                    }.orShow {
                                        text(
                                            Bokmal to "Vi har registrert at partneren din har en inntekt som er lavere enn to ganger folketrygdens grunnbeløp (G). Satsen vi bruker er derfor ",
                                            Nynorsk to "Vi har registrert at partnaren din har ei inntekt som er lågare enn to gonger grunnbeløpet i folketrygda (G). Satsen vi brukar er difor ",
                                            English to "We have registered that your partner has an annual income lower than twice the national insurance basic amount (G). The rate we use is therefore the ",
                                        )
                                    }
                                }
                            }

                            includePhrase(GarantipensjonSatsTypeText(garantipensjonVedVirk.satsType))
                            text(Bokmal to ".", Nynorsk to ".", English to ".")
                        }
                    }
                }.orShowIf(brukersSivilstand.isOneOf(GLAD_EKT, SEPARERT)) {
                    paragraph {
                        //vedleggBeregnGift_001
                        text(
                            Bokmal to "Vi har lagt til grunn at du er gift.",
                            Nynorsk to "Vi har lagt til grunn at du er gift.",
                            English to "We have registered that you have a spouse.",
                        )
                    }
                    showIf(erBeregnetSomEnsligPgaInstitusjonsopphold) {
                        paragraph {                        //vedleggGiftLeverAdskilt&&Institusjonsopphold_002
                            text(
                                Bokmal to "Du og ektefellen din er registrert med forskjellig bosted da en av dere bor på institusjon. Pensjonen din er derfor beregnet som om du var enslig. Satsen vi bruker er derfor ",
                                Nynorsk to "Du og ektefellen din er registrerte med forskjellig bustad da ein av dykk bur på institusjon. Pensjonen din er derfor berekna som om du var einsleg. Satsen vi brukar er difor ",
                                English to "You and your spouse are registered with different residences as one of you is residing in an institution. Therefore, your pension has been calculated as if you were single. The rate we use is therefore the ",
                            )
                            includePhrase(GarantipensjonSatsTypeText(garantipensjonVedVirk.satsType))
                            text(Bokmal to ".", Nynorsk to ".", English to ".")
                        }
                    }.orShowIf(not(epsVedVirk.borSammenMedBruker_safe.ifNull(false))) {
                        //vedleggBeregnGiftLeverAdskilt_002
                        paragraph {
                            text(
                                Bokmal to "Du og ektefellen din er registrert med forskjellig bosted. Pensjonen din er derfor beregnet som om du var enslig. Satsen vi bruker er derfor ",
                                Nynorsk to "Du og ektefellen din er registrerte med forskjellig bustad. Pensjonen din er derfor berekna som om du var einsleg. Satsen vi brukar er difor ",
                                English to "You and your spouse are registered with different residences. Therefore, your pension has been calculated as if you were single. The rate we use is therefore the ",
                            )
                            includePhrase(GarantipensjonSatsTypeText(garantipensjonVedVirk.satsType))
                            text(Bokmal to ".", Nynorsk to ".", English to ".")
                        }
                    }
                }.orShowIf(brukersSivilstand.isOneOf(GLAD_PART, SEPARERT_PARTNER)) {
                    //vedleggBeregnPartner_001
                    paragraph {
                        text(
                            Bokmal to "Vi har lagt til grunn at du er partner.",
                            Nynorsk to "Vi har lagt til grunn at du er partnar.",
                            English to "We have registered that you have a partner.",
                        )
                    }
                    paragraph {
                        //vedleggBeregnPartnerLeverAdskilt_002
                        text(
                            Bokmal to "Du og partneren din er registrert med forskjellig bosted. Pensjonen din er derfor beregnet som om du var enslig. Satsen vi bruker er derfor ",
                            Nynorsk to "Du og partnaren din er registrerte med forskjellig bustad. Pensjonen di er difor rekna som om du var einsleg. Satsen vi brukar er difor ",
                            English to "You and your partner are registered with different residences. Therefore, your pension has been calculated as if you were single. The rate we use is therefore the ",
                        )
                        includePhrase(GarantipensjonSatsTypeText(garantipensjonVedVirk.satsType))
                        text(Bokmal to ".", Nynorsk to ".", English to ".")
                    }
                }.orShowIf(brukersSivilstand.isOneOf(SAMBOER_3_2, SAMBOER_1_5)) {
                    showIf(brukersSivilstand.equalTo(SAMBOER_3_2)) {
                        //vedleggBeregnSambo§20-9_001
                        paragraph {
                            text(
                                Bokmal to "Vi har lagt til grunn at du er samboer (jf. folketrygdloven § 20-9).",
                                Nynorsk to "Vi har lagt til grunn at du er sambuar (jf. folketrygdlova § 20-9).",
                                English to "We have registered that you have a cohabitant (cf. § 20-9 of the National Insurance Act).",
                            )
                        }
                    }.orShow {
                        //vedleggBeregnSambo§1-5_001
                        paragraph {
                            text(
                                Bokmal to "Vi har lagt til grunn at du er samboer (jf. folketrygdloven § 1-5).",
                                Nynorsk to "Vi har lagt til grunn at du er sambuar (jf. folketrygdlova § 1-5).",
                                English to "We have registered that you have a cohabitant (cf. § 1-5 of the National Insurance Act).",
                            )
                        }
                    }
                    ifNotNull(epsVedVirk) { epsVedVirk ->
                        showIf(
                            epsVedVirk.borSammenMedBruker
                                    and epsVedVirk.mottarPensjon
                                    and not(erBeregnetSomEnsligPgaInstitusjonsopphold)
                        ) {
                            //vedleggBeregnSamboPensjon_001
                            paragraph {
                                text(
                                    Bokmal to "Vi har registrert at samboeren din mottar uføretrygd, pensjon fra folketrygden eller AFP som det godskrives pensjonspoeng for.",
                                    Nynorsk to "Vi har registrert at sambuaren din får uføretrygd, pensjon frå folketrygda eller AFP som det blir godskrive pensjonspoeng for.",
                                    English to "We have registered that your cohabitant is receiving disability benefit, a national insurance pension or contractual early retirement pension (AFP) which earns pension points.",
                                )
                            }
                        }
                        showIf(
                            epsVedVirk.borSammenMedBruker
                                    and not(epsVedVirk.mottarPensjon)
                                    and not(erBeregnetSomEnsligPgaInstitusjonsopphold)
                        ) {
                            showIf(epsVedVirk.harInntektOver2G) {
                                //vedleggBeregnSamboOver2G_002
                                paragraph {
                                    text(
                                        Bokmal to "Vi har registrert at samboeren din har en inntekt som er høyere enn to ganger folketrygdens grunnbeløp (G). Satsen vi bruker er derfor ordinær sats.",
                                        Nynorsk to "Vi har registrert at sambuaren din har ei inntekt som er høgare enn to gonger grunnbeløpet i folketrygda (G). Satsen vi bruker er difor ordinær sats.",
                                        English to "We have registered that your cohabitant has an annual income that exceeds twice the national insurance basic amount (G). The rate we use is therefore the ordinary rate.",
                                    )
                                }
                            }.orShow {
                                //vedleggBeregnSamboUnder2G_002
                                paragraph {
                                    text(
                                        Bokmal to "Vi har registrert at samboeren din har en inntekt som er lavere enn to ganger folketrygdens grunnbeløp (G). Satsen vi bruker er derfor høy sats.",
                                        Nynorsk to "Vi har registrert at sambuaren din har ei inntekt som er lågare enn to gonger grunnbeløpet i folketrygda (G). Satsen vi bruker er difor høg sats.",
                                        English to "We have registered that your cohabitant has an annual income lower than twice the national insurance basic amount (G). The rate we use is therefore the high rate.",
                                    )
                                }
                            }
                        }
                    }
                }.orShowIf(brukersSivilstand.isOneOf(ENSLIG, ENKE, UKJENT)) {
                    paragraph {
                        //vedleggBeregnEnslig_002
                        text(
                            Bokmal to "Vi har lagt til grunn at du er enslig. Satsen vi bruker er derfor ",
                            Nynorsk to "Vi har lagt til grunn at du er einsleg. Satsen vi brukar er difor ",
                            English to "We have registered that you are single. The rate we use is therefore the ",
                        )
                        includePhrase(GarantipensjonSatsTypeText(garantipensjonVedVirk.satsType))
                        text(Bokmal to ".", Nynorsk to ".", English to ".")
                    }
                }
                showIf(beregningKap20VedVirk.redusertTrygdetid) {
                    //vedleggBeregnSatsRedusertTT_001
                    paragraph {
                        text(
                            Bokmal to "Satsen er redusert fordi trygdetiden din er under 40 år.",
                            Nynorsk to "Satsen er redusert fordi trygdetida di er under 40 år.",
                            English to "The rate is reduced because your national insurance coverage is less than 40 years.",
                        )
                    }
                }
                paragraph {
                    //vedleggBeregnGarantipensjonDelingstall67_001
                    text(
                        Bokmal to "Vi bruker delingstallet fastsatt for ditt årskull ved 67 år for å regne om denne satsen til en beholdningsstørrelse. Vi trekker deretter fra 80 prosent av pensjonsbeholdningen din ved uttak fra dette beløpet. Summen utgjør da garantipensjonsbeholdningen ved uttak.",
                        Nynorsk to "Vi brukar delingstalet fastsett for årskullet ditt ved 67 år for å rekne om denne satsen til ein behaldningsstorleik. Vi trekkjer deretter frå 80 prosent av pensjonsbehaldninga di ved uttak frå dette beløpet. Summen utgjer då garantipensjonsbehaldninga ved uttak.",
                        English to "We use the life expectancy divisor set for your cohort at 67 years to convert this rate into a capital balance. We then deduct 80 percent of your accumulated pension capital at withdrawal from this amount. The sum then constitutes the guaranteed pension capital at the time of the initial withdrawal.",
                    )
                }
                showIf(beregningKap20VedVirk.redusertTrygdetid) {
                    paragraph {
                        //vedleggBeregnGarantipensjonsbeholdningRedusertTT_001
                        text(
                            Bokmal to "Sats for garantipensjon x (trygdetid / 40 år full trygdetid) x delingstall ved 67 år - (80% av pensjonsbeholdning ved uttak) = garantipensjonsbeholdning",
                            Nynorsk to "Sats for garantipensjon x (trygdetid / 40 år full trygdetid) x delingstal ved 67 år - (80% av pensjonsbehaldning ved uttak) = garantipensjonsbehaldning",
                            English to "Guaranteed pension rate x (NI coverage / 40 years full NI coverage) x life expectancy adjustment divisor at 67 years - (80% of accumulated pension capital before initial withdrawal) = guaranteed pension capital",
                        )
                    }
                    //vedleggBeregnGarantipensjonsbeholdningRedusertTT_002
                    paragraph {
                        val norskText = garantipensjonVedVirk.garantipensjonSatsPerAr.format() +
                                " kr x (" + trygdetidsdetaljerKap20VedVirk.anvendtTT.format() +
                                " / 40) x " + garantipensjonVedVirk.delingstalletVed67Ar.format() +
                                " - (80% (" + beregningKap20VedVirk.beholdningForForsteUttak.format() +
                                " kr)) = " + garantipensjonVedVirk.beholdningForForsteUttak.format() + " kr"
                        textExpr(
                            Bokmal to norskText,
                            Nynorsk to norskText,
                            English to "NOK ".expr() + garantipensjonVedVirk.garantipensjonSatsPerAr.format() +
                                    " x (" + trygdetidsdetaljerKap20VedVirk.anvendtTT.format() +
                                    " / 40) x " + garantipensjonVedVirk.delingstalletVed67Ar.format() +
                                    " - (80% (NOK " + beregningKap20VedVirk.beholdningForForsteUttak.format()
                                    + ")) = NOK " + garantipensjonVedVirk.beholdningForForsteUttak.format(),
                        )
                    }
                }.orShow {
                    paragraph {
                        //vedleggBeregnGarantipensjonsbeholdningIkkeRedusertTT_001
                        text(
                            Bokmal to "Sats for garantipensjon x delingstall ved 67 år - (80% av pensjonsbeholdning ved uttak) = garantipensjonsbeholdning:",
                            Nynorsk to "Sats for garantipensjon x delingstal ved 67 år - (80% av pensjonsbehaldning ved uttak) = garantipensjonsbehaldning:",
                            English to "Guaranteed pension rate x life expectancy adjustment divisor at 67 years - (80% of accumulated pension capital before initial withdrawal) = guaranteed pension capital:",
                        )
                    }
                    paragraph {
                        //vedleggBeregnGarantipensjonsbeholdningIkkeRedusertTT_002
                        val norskText = garantipensjonVedVirk.garantipensjonSatsPerAr.format() +
                                " kr x " + garantipensjonVedVirk.delingstalletVed67Ar.format() +
                                " - (80% (" + beregningKap20VedVirk.beholdningForForsteUttak.format() +
                                " kr)) = " + garantipensjonVedVirk.beholdningForForsteUttak.format() + " kr"
                        textExpr(
                            Bokmal to norskText, Nynorsk to norskText,

                            English to "NOK ".expr() + garantipensjonVedVirk.garantipensjonSatsPerAr.format() +
                                    " x " + garantipensjonVedVirk.delingstalletVed67Ar.format() +
                                    " - (80% (NOK " + beregningKap20VedVirk.beholdningForForsteUttak.format() +
                                    ")) =  NOK " + garantipensjonVedVirk.beholdningForForsteUttak.format(),
                        )
                    }
                }
            }
        }


        showIf(beregningKap20VedVirk.redusertTrygdetid and not(avslattGarantipensjon)) {
            title1 {
                text(
                    Bokmal to "Trygdetid",
                    Nynorsk to "Trygdetid",
                    English to "Period of national insurance coverage",
                )
            }
            includePhrase(OpplysningerBruktIBeregningenTrygdetidTabeller.NorskTrygdetidInnledning)
            includePhrase(OpplysningerBruktIBeregningenTrygdetidTabeller.NorskTrygdetid(trygdetidNorge))
        }
        //vedleggBeregnPensjonsOpptjeningKap20_001
        title1 {
            text(
                Bokmal to "Pensjonsopptjening din",
                Nynorsk to "Pensjonsopptening di",
                English to "Your pension accrual",
            )
        }
        paragraph {
            text(
                Bokmal to "Tabellen under viser den pensjonsgivende inntekten din og om du har andre typer pensjonsopptjening registrert på deg i det enkelte år. Det er egne regler for pensjonsopptjening ved omsorgsarbeid, dagpenger ved arbeidsledighet eller uføretrygd.",
                Nynorsk to "Tabellen under viser den pensjonsgivande inntekta di og om du har andre typar pensjonsopptening registrert på deg i det enkelte år. Det er egne reglar for pensjonsopptening ved omsorgsarbeid, dagpengar ved arbeidsløyse eller uføretrygd.",
                English to "The table below shows your pensionable income, and you will be able to see your other types of pension accruals we have registered for each year. There are special rules for pension accrual in connection with care work, unemployment benefit or disability benefit. ",
            )
        }

        showIf(pensjonsopptjeningKap20VedVirk.harOmsorgsopptjeningFOM2010) {
            //vedleggBeregnAP2025Omsorgsopptjening_001
            paragraph {
                text(
                    Bokmal to "Opptjening fra omsorgsarbeid garanterer at du får pensjonsopptjening som tilsvarer inntekt inntil 4,5 ganger gjennomsnittlig G. Hvis pensjonsgivende inntekt eller annen opptjening er lavere i det aktuelle året, er du sikret denne opptjeningen.",
                    Nynorsk to "Opptening frå omsorgsarbeid garanterer at du får pensjonsopptening som utgjer ei inntekt inntil 4,5 gongar gjennomsnittleg G. Dersom pensjonsgivande inntekt eller anna opptening er lågare i det aktuelle året, er du sikra denne oppteninga.",
                    English to "Accrual from care work guarantees that you will receive pension accrual corresponding to income up to 4.5 times the average G. If pensionable income or other pension accrual is lower in the year in question, you are guaranteed this pension accrual from care work.",
                )
                showIf(pensjonsopptjeningKap20VedVirk.harOmsorgsopptjeningTOM2009) {
                    text(
                        Bokmal to " Fram til 2010 var garantien 4 ganger gjennomsnittlig G.",
                        Nynorsk to " Fram til 2010 var garantien 4 gongar gjennomsnittleg G.",
                        English to " Prior to 2010, the pension accrual from care work was 4 times the average G.",
                    )
                }
            }
        }.orShowIf(pensjonsopptjeningKap20VedVirk.harOmsorgsopptjeningTOM2009) {
            //vedleggBeregnAP2025OmsorgsopptjeningFor2010_001
            paragraph {
                text(
                    Bokmal to "Opptjening fra omsorgsarbeid garanterer at du får pensjonsopptjening som tilsvarer inntekt inntil 4 ganger gjennomsnittlig G. Hvis pensjonsgivende inntekt eller annen opptjening er lavere i det aktuelle året, er du sikret denne opptjeningen.",
                    Nynorsk to "Opptening frå omsorgsarbeid garanterer at du får pensjonsopptening som utgjer ei inntekt inntil 4 gongar gjennomsnittleg G. Dersom pensjonsgivande inntekt eller anna opptening er lågare i det aktuelle året, er du sikra denne oppteninga.",
                    English to "Accrual from care work guarantees that you will receive pension accrual corresponding to income up to 4 times the average G. If pensionable income or other pension accrual is lower in the year in question, you are guaranteed this pension accrual from care work.",
                )
            }
        }

        showIf(pensjonsopptjeningKap20VedVirk.harDagpenger) {
            paragraph {
                text(
                    Bokmal to "For perioder du har hatt dagpenger er det egne regler for pensjonsopptjening. Reglene gjelder fra og med 2010.",
                    Nynorsk to "For periodar du har hatt dagpengar er det egne reglar for pensjonsopptening. Reglane gjelder frå og med 2010.",
                    English to "For periods when you have had unemployment benefit, there are separate rules for pension accrual. The rules apply from 2010 onwards.",
                )
            }
        }

        showIf(pensjonsopptjeningKap20VedVirk.harUforetrygd) {
            paragraph {
                text(
                    Bokmal to "For perioder du har hatt uføretrygd er det egne regler for pensjonsopptjening.",
                    Nynorsk to "For periodar du har hatt uføretrygd er det egne reglar for pensjonsopptening.",
                    English to "For periods when you have received disability benefit, there are separate rules for pension accrual.",
                )
            }
        }

        showIf(pensjonsopptjeningKap20VedVirk.harUforepensjonKonvertertTilUforetrygd) {
            paragraph {
                text(
                    Bokmal to "For perioder du har hatt uførepensjon og uføretrygd er det egne regler for pensjonsopptjening.",
                    Nynorsk to "For periodar du har hatt uførepensjon eller uføretrygd er det egne reglar for pensjonsopptening.",
                    English to "For periods when you have received disability pension and disability benefit, there are separate rules for pension accrual.",
                )
            }
        }

        showIf(pensjonsopptjeningKap20VedVirk.harUforepensjon) {
            paragraph {
                text(
                    Bokmal to "For perioder du har hatt uførepensjon er det egne regler for pensjonsopptjening.",
                    Nynorsk to "For periodar du har hatt uførepensjon er det egne reglar for pensjonsopptening.",
                    English to "For periods when you have received disability pension, there are separate rules for pension accrual. ",
                )
            }
        }

        paragraph {
            text(
                Bokmal to "I nettjenesten Din pensjon på $DIN_PENSJON_URL får du oversikt over pensjonsopptjeningen din for hvert enkelt år. Nav mottar opplysninger om pensjonsgivende inntekt fra Skatteetaten. Ta kontakt med Skatteetaten hvis du mener at inntektene i tabellen er feil.",
                Nynorsk to "I nettenesta Din pensjon på $DIN_PENSJON_URL får du oversikt over pensjonsoppteninga di for kvart enkelt år. Nav får opplysningar om pensjonsgivande inntekt frå Skatteetaten. Ta kontakt med Skatteetaten dersom du meiner at inntektene i tabellen er feil.",
                English to "Our online service \"Din pensjon\" at $DIN_PENSJON_URL provides details on your accumulated rights for each year. Nav receives information about pensionable income from the Norwegian Tax Administration. Contact the tax authorities if you think that this income is wrong.",
            )
        }
        showIf(pensjonsopptjeningKap20VedVirk.harMerknadType) {
            pensjonsgivendeInntekt(true)
        }.orShow {
            pensjonsgivendeInntekt(false)
        }


    }

private fun OutlineOnlyScope<LangBokmalNynorskEnglish, OpplysningerBruktIBeregningenAlderAP2025Dto>.pensjonsgivendeInntekt(
    medMerknader: Boolean
) {
    paragraph {
        table({
            column {
                text(
                    Bokmal to "År",
                    Nynorsk to "År",
                    English to "Year",
                )
            }
            column {
                text(
                    Bokmal to "Pensjonsgivende inntekt (kr)",
                    Nynorsk to "Pensjonsgivande inntekt (kr)",
                    English to "Pensionable income (NOK)",
                )
            }
            column {
                text(
                    Bokmal to "Gjennomsnittlig G (kr)",
                    Nynorsk to "Gjennomsnittlig G (kr)",
                    English to "Average G (NOK)",
                )
            }
            if(medMerknader) {
                column(columnSpan = 2) {
                    text(
                        Bokmal to "Andre typer opptjeningsgrunnlag registrert",
                        Nynorsk to "Andre typer oppteningsgrunnlag registrert",
                        English to "Other types of accruals registered",
                    )
                }
            }
        }) {
            forEach(pensjonsopptjeningKap20VedVirk.pensjonsopptjeninger) {
                row {
                    cell { eval(it.aarstall.format()) }
                    cell { includePhrase(KronerText(it.pensjonsgivendeinntekt)) }
                    cell { includePhrase(KronerText(it.gjennomsnittligG)) }
                    if (medMerknader) {
                        cell {
                            forEach(it.merknader) { merknad ->
                                showIf(merknad.equalTo(Merknad.DAGPENGER)) {
                                    text(
                                        Bokmal to "Dagpenger ",
                                        Nynorsk to "Dagpenger ",
                                        English to "Unemployment benefit ",
                                    )
                                }.orShowIf(merknad.equalTo(Merknad.OMSORGSOPPTJENING)){
                                    text(
                                        Bokmal to "Omsorgsopptjening ",
                                        Nynorsk to "Omsorgsopptjening ",
                                        English to "Care work accrual ",
                                    )
                                }.orShowIf(merknad.equalTo(Merknad.UFOREPENSJON)){
                                    text(
                                        Bokmal to "Uførepensjon ",
                                        Nynorsk to "Uførepensjon ",
                                        English to "Disability pension ",
                                    )
                                }.orShowIf(merknad.equalTo(Merknad.UFORETRYGD)){
                                    text(
                                        Bokmal to "Uføretrygd ",
                                        Nynorsk to "Uføretrygd ",
                                        English to "Disability benefit ",
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
