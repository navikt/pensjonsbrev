package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.Sakstype.ALDER
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.VedtaksBegrunnelse.*
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagPgaForLiteOpptjeningAP2016Dto
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagPgaForLiteOpptjeningAP2016DtoSelectors.PesysDataSelectors.avtalelandNavn
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagPgaForLiteOpptjeningAP2016DtoSelectors.PesysDataSelectors.erEOSland
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagPgaForLiteOpptjeningAP2016DtoSelectors.PesysDataSelectors.harAvtaleland
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagPgaForLiteOpptjeningAP2016DtoSelectors.PesysDataSelectors.vedtaksBegrunnelse
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagPgaForLiteOpptjeningAP2016DtoSelectors.pesysData
import no.nav.pensjon.brev.maler.fraser.common.Vedtak
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.expression.isNotAnyOf
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import kotlin.math.E


@TemplateModelHelpers
object AvslagPgaForLiteOpptjeningAP2016 : RedigerbarTemplate<AvslagPgaForLiteOpptjeningAP2016Dto> {

    override val kode = Pesysbrevkoder.Redigerbar.PE_AP_AVSLAG_FOR_LITE_OPPTJENING_AP2016
    override val kategori = TemplateDescription.Brevkategori.FOERSTEGANGSBEHANDLING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper: Set<Sakstype> = setOf(ALDER)


    override  val template = createTemplate(
        name = kode.name,
        letterDataType = AvslagPgaForLiteOpptjeningAP2016Dto::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Varsel - tilbakekreving av feilutbetalt",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {
        val avslagsBegrunnelse = pesysData.vedtaksBegrunnelse
        val erEOSland = pesysData.erEOSland
        val avtalelandNavn = pesysData.avtalelandNavn.ifNull(then = "avtalelandNavn")
        val harAvtaleland = pesysData.harAvtaleland

        title {
            text(
                Bokmal to "Nav har avslått søknaden din om alderspensjon",
                Nynorsk to "Nav har avslått søknaden din om alderspensjon",
                English to "Nav has declined your application for retirement pension",
            )
        }
        outline {
            includePhrase(Vedtak.Overskrift)

            showIf(avslagsBegrunnelse.isOneOf(UNDER_1_AR_TT)) {
                // avslagAP2016Under1aar
                paragraph {
                    text(
                        Bokmal to "For å ha rett til alderspensjon må du ha bodd eller arbeidet i Norge i minst ett år eller ha tjent opp inntektspensjon. "
                                + "Det har du ikke, og derfor har vi avslått søknaden din.",
                        Nynorsk to "For å ha rett til alderspensjon må du ha budd eller arbeidd i Noreg i minst eit år eller ha tent opp inntektspensjon. "
                                + "Det har du ikkje, og derfor har vi avslått søknaden din.",
                        English to "To be eligible for retirement pension, you must have been registered as living in Norway for at least one year or have had a pensionable income. "
                                + "You do not meet any of these requirements, therefore we have declined your application.",
                    )
                }
                // avslagUnder1aarTT
                paragraph {
                    textExpr(
                        Bokmal to "Våre opplysninger viser at du har bodd eller arbeidet i Norge i ".expr()
                                + fritekst("antall dager / måneder") + ". Våre opplysninger viser at du ikke har bodd eller arbeidet i Norge.",
                        Nynorsk to "Våre opplysningar viser at du har budd eller arbeidd i Noreg i ".expr()
                                + fritekst("antall dagar / månader") + ". Våre opplysningar viser at du ikkje har budd eller arbeidd i Noreg.",
                        English to "We have registered that you have been living or working in Norway for ".expr()
                                + fritekst("number of days / months") + ". We have no record of you living or working in Norway.",
                    )
                }
                showIf(erEOSland) {
                    // avslagAP2016Under1aarHjemmelEOS
                    paragraph {
                        text(
                            Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 19-2, 20-5 til 20-8, 20-10 og EØS-avtalens forordning 883/2004 artikkel 57.",
                            Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 19-2, 20-5 til 20-8, 20-10 og EØS-avtalens forordning 883/2004 artikkel 57.",
                            English to "This decision was made pursuant to the provisions of §§ 19-2, 20-5 to 20-8 and 20-10 of the National Insurance Act and Article 57 of Regulation (EC) 883/2004.",
                        )
                    }
                }.orShow {
                    // avslagAP2016Under1aarHjemmelAvtale
                    paragraph {
                        textExpr(
                            Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 19-2, 20-5 til 20-8, 20-10 og artikkel ".expr()
                                    + fritekst("Legg in aktuelle artikler om sammenlegging og eksport") + " i trygdeavtalen med ".expr()
                                    + avtalelandNavn + ".",
                            Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 19-2, 20-5 til 20-8, 20-10 og artikkel ".expr()
                                    + fritekst("Legg in aktuelle artikler om sammenlegging og eksport") + " i trygdeavtalen med ".expr()
                                    + avtalelandNavn + ".",
                            English to "This decision was made pursuant to the provisions of §§ 19-2, 20-5 to 20-8 and 20-10 of the National Insurance Act and Article ".expr()
                                    + fritekst("Legg in aktuelle artikler om sammenlegging og eksport") + " of the Social Security Agreement with ".expr()
                                    + avtalelandNavn + "."
                        )
                    }
                }
            }

            showIf(avslagsBegrunnelse.isOneOf(UNDER_3_AR_TT) and not(harAvtaleland)) {
                // avslagAP2016Under3aar
                paragraph {
                    text(
                        Bokmal to "For å ha rett til alderspensjon må du ha minst tre års trygdetid eller ha tjent opp inntektspensjon. Det har du ikke, og derfor har vi avslått søknaden din.",
                        Nynorsk to "For å ha rett til alderspensjon må du ha minst tre års trygdetid eller ha tent opp inntektspensjon. Det har du ikkje, og derfor har vi avslått søknaden din.",
                        English to "To be eligible for retirement pension, you must have at least three years of national insurance coverage or have had a pensionable income. "
                                + "You do not meet these requirements, therefore we have declined your application.",
                    )
                }
                // avslagUnder3aarTT
                paragraph {
                    textExpr(
                        Bokmal to "Våre opplysninger viser at du har bodd eller arbeidet i Norge i ".expr() + fritekst("angi trygdetid") + ". Våre opplysninger viser at du ikke har bodd eller arbeidet i Norge.",
                        Nynorsk to "Våre opplysningar viser at du har budd eller arbeidd i Noreg i ".expr() + fritekst("angi trygdetid") + ". I følgje våre opplysningar har du ikkje budd eller arbeidd i Noreg.",
                        English to "We have registered that you have been living or working in Norway for ".expr() + fritekst("angi trygdetid") + ". We have no record of you living or working in Norway.",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 19-2, 20-5 til 20-8 og 20-10.",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 19-2, 20-5 til 20-8 og 20-10.",
                        English to "This decision was made pursuant to the provisions of §§ 19-2, 20-5 to 20-8 and 20-10 of the National Insurance Act.",
                    )
                }
            }

            showIf(avslagsBegrunnelse.isOneOf(UNDER_3_AR_TT) and erEOSland) {
                // avslagUnder3aarTTEOS
                paragraph {
                    textExpr(
                        Bokmal to "Vi har fått opplyst at du har ".expr() + fritekst("angi antall") + " måneder opptjeningstid i annet EØS-land."
                                + "Den samlede trygdetiden din i Norge og annet EØS-land er ". expr() + fritekst("anggi samlet trygdetid i Norge og avtaleland") + ".",
                        Nynorsk to "Vi har fått opplyst at du har ".expr() + fritekst("angi antall") + " månader oppteningstid i anna EØS-land."
                                + "Den samla trygdetida di i Noreg og anna EØS-land er ".expr() + fritekst("angi samlet trygdetid i Norge og avtaleland) + ".",
                        English to "We have been informed that you have ".expr() + fritekst("angi antall") + " months of national insurance coverage in an other EEA country."
                                + "Your total national insurance coverage in Norway and an other EEA country is ".expr() + "anggi samlet trygdetid i Norge og avtaleland") + "."
                    )
                }
            }
        }
    }
}