package no.nav.pensjon.brev.ufore.maler.info

import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgEnum
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.ifElse
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.saksbehandlervalg
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.AlltidValgbareVedlegg.SKJEMA_FOR_BANKOPPLYSNINGER
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_KRAV_SENDT_UTLAND
import no.nav.pensjon.brev.ufore.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.ufore.api.model.maler.Sakstype
import no.nav.pensjon.brev.ufore.maler.Brevkategori
import no.nav.pensjon.brev.ufore.maler.FeatureToggles
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VIKTIG

@TemplateModelHelpers
object KravSendtUtland : RedigerbarTemplate<EmptyRedigerbarBrevdata> {

    override val kode = UT_KRAV_SENDT_UTLAND
    override val kategori = Brevkategori.INFORMASJONSBREV
    override val brevkontekst = TemplateDescription.Brevkontekst.SAK
    override val sakstyper = setOf(Sakstype.UFOREP)
    override val valgbareVedlegg = setOf(SKJEMA_FOR_BANKOPPLYSNINGER)

    override val featureToggle = FeatureToggles.innhentingOpplysningerForside.toggle

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Krav alderspensjon sendt til utlandet",
            distribusjonstype = VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV
        ),
    )
    {
        val kravgjelder = saksbehandlervalg("kravgjelder", "Kravet gjelder").enum<KravGjelderEnum>()
        val kravgjelderTxt = ifElse(
            kravgjelder.equalTo(KravGjelderEnum.ALDERSPENSJON),
            "alderspensjon".expr(),
            "uføretrygd".expr()
        )

        title {
            text(
                bokmal { + "Kravet ditt om " + kravgjelderTxt + " er sendt til " + fritekst("land") },
                nynorsk { + "Kravet ditt om " + kravgjelderTxt + " er sendt til " + fritekst("land") },
            )
        }
        outline {
            showIf(kravgjelder.equalTo(KravGjelderEnum.ALDERSPENSJON)) {
                paragraph {
                    text(
                        bokmal { +"Vi viser til ditt krav om alderspensjon fra " + fritekst("land") + " mottatt hos oss " + fritekst("dato") + ". Det er denne datoen som blir kravdatoen i avtalelandet. " },
                        nynorsk { +"Vi viser til ditt krav om alderspensjon frå " + fritekst("land") + " motteke hos oss " + fritekst("dato") + ". Det er denne datoen som blir kravdatoen i avtalelandet. " },
                    )
                }

                paragraph {
                    text(
                        bokmal { +"Kravet er sendt til " + fritekst("land") + " i dag. " },
                        nynorsk { +"Kravet er sendt til " + fritekst("land") + " i dag. " },
                    )
                }

                title1 {
                    text(
                        bokmal { +"Hva skjer i avtalelandet?" },
                        nynorsk { +"Kva skjer i avtalelandet?" },
                    )
                }

                paragraph {
                    text(
                        bokmal { +"Kravet behandles av trygdemyndigheter i " + fritekst("land") + " etter deres nasjonale lovgivning. Når vedtaket er fattet, sendes det til deg og Nav. " },
                        nynorsk { +"Kravet blir behandla av trygdemyndigheiter i " + fritekst("land") + " etter deira nasjonale lovgiving. Når vedtaket er fatta, blir det sendt til deg og Nav. " },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Vi kan dessverre ikke si noe om andre lands saksbehandlingstid utover at den er svært varierende og kan være lang. " },
                        nynorsk { +"Vi kan dessverre ikkje seie noko om andre lands saksbehandlingstid utover at den er svært varierande og kan vere lang. " },
                    )
                }
            }.orShowIf(kravgjelder.equalTo(KravGjelderEnum.UFØRETRYGD)) {
                title1 {
                    text(
                        bokmal { +"Krav om uføreytelse fra folketrygden i Norge" },
                        nynorsk { +"Krav om uføreyting frå folketrygda i Noreg" },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Vi viser til ditt krav om uføreytelse av " + fritekst("dato") + " og foreløpig vedtak om uføreytelse av " + fritekst("dato") + ". " },
                        nynorsk { +"Vi viser til ditt krav om uføreyting av " + fritekst("dato") + " og førebels vedtak om uføreyting av " + fritekst("dato") + ". " },
                    )
                }

                title1 {
                    text(
                        bokmal { +"Krav om alderspensjon fra annet land" },
                        nynorsk { +"Krav om alderspensjon frå anna land" },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Etter reglene i EØS-avtalen er et krav om uføreytelse i Norge også å anse som et krav om uføreytelse i andre EØS-land du kan ha opparbeidet deg trygdetid i. Det fremgår av kravet ditt at du har bodd og/eller arbeidet i " + fritekst("land") + ". Vi ser at du på søknadstidspunktet har nådd pensjonsalder for alderspensjon i " + fritekst("land") + ". Vi har derfor sendt krav om alderspensjon istedenfor kravet om uføreytelse til trygdemyndighetene i " + fritekst("land") + ". Kravdatoen i " + fritekst("land") + " vil bli den samme som kravdatoen i Norge. " },
                        nynorsk { +"Etter reglane i EØS-avtalen er eit krav om uføreyting i Noreg også å rekne som eit krav om uføreyting i andre EØS-land du kan ha opparbeidd deg trygdetid i. Det går fram av kravet ditt at du har budd og/eller arbeidd i " + fritekst("land") + ". Vi ser at du på søknadstidspunktet har nådd pensjonsalder for alderspensjon i " + fritekst("land") + ". Vi har derfor sendt krav om alderspensjon i staden for kravet om uføreyting til trygdemyndigheitene i " + fritekst("land") + ". Kravdatoen i " + fritekst("land") + " vil bli den same som kravdatoen i Noreg. " },
                    )
                }

                title1 {
                    text(
                        bokmal { +"Hva skjer i avtalelandet?" },
                        nynorsk { +"Kva skjer i avtalelandet?" },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Hvis du har opparbeidet trygdetid i " + fritekst("land") + ", vil trygdemyndigheter i dette landet fatte vedtak om pensjonsytelse etter egne nasjonale regler og sende vedtaket direkte til deg med kopi til oss. " },
                        nynorsk { +"Dersom du har opparbeidd trygdetid i " + fritekst("land") + ", vil trygdemyndigheiter i dette landet fatte vedtak om pensjonsyting etter eigne nasjonale reglar og sende vedtaket direkte til deg med kopi til oss. " },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Hvis du ikke har opparbeidet trygdetid i " + fritekst("land") + ", vil vi motta beskjed om dette, uten at du nødvendigvis mottar beskjed. Vi vil uansett informere deg om det svaret vi får. " },
                        nynorsk { +"Dersom du ikkje har opparbeidd trygdetid i " + fritekst("land") + ", vil vi motta beskjed om dette, utan at du nødvendigvis mottek beskjed. Vi vil uansett informere deg om det svaret vi får. " },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Vi kan dessverre ikke si noe om andre lands saksbehandlingstid utover at den er svært varierende og kan være lang. " },
                        nynorsk { +"Vi kan dessverre ikkje seie noko om andre lands saksbehandlingstid utover at den er svært varierande og kan vere lang. " },
                    )
                }

                title1 {
                    text(
                        bokmal { +"Endelig beregning hos oss" },
                        nynorsk { +"Endeleg berekning hos oss" },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Vi vil foreta en endelig beregning av uføreytelsen din og fatte et endelig vedtak når alle nødvendige opplysninger om trygdetid og/eller vedtak er mottatt fra avtalelandet. Dette gjelder uansett om du har opparbeidet deg rettigheter i avtalelandet eller ikke. Vi vil sende deg et brev om utfallet i saken. " },
                        nynorsk { +"Vi vil gjere ei endeleg berekning av uføreytinga di og fatte eit endeleg vedtak når alle nødvendige opplysningar om trygdetid og/eller vedtak er mottekne frå avtalelandet. Dette gjeld uansett om du har opparbeidd deg rettar i avtalelandet eller ikkje. Vi vil sende deg eit brev om utfallet i saka. " },
                    )
                }
            }

            includePhrase(Felles.MeldFraOmEndringer)
            includePhrase(Felles.HarDuSporsmal)
        }
    }
}

enum class KravGjelderEnum(override val displayText: String) : SaksbehandlerValgEnum {
    ALDERSPENSJON("Alderspensjon"),
    UFØRETRYGD("Uføretrygd"),
}