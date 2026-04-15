package no.nav.pensjon.brev.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseGjenlevendepensjonBosattNorgeEtterUtlandDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseGjenlevendepensjonBosattNorgeEtterUtlandDtoSelectors.PesysDataSelectors.kravMottattDato
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseGjenlevendepensjonBosattNorgeEtterUtlandDtoSelectors.pesysData
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.navKontaktsenterPensjon
import no.nav.pensjon.brev.maler.legacy.vedlegg.vedleggFolketrygdenGjenlevendepensjon
import no.nav.pensjon.brev.model.Brevkategori
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

//PE_GP_04_025 Vedtak innvilgelse av gjenlevendepensjon utland
//Brevgruppe 2

@TemplateModelHelpers
object InnvilgelseGjenlevendepensjonBosattNorgeEtterUtland : RedigerbarTemplate<InnvilgelseGjenlevendepensjonBosattNorgeEtterUtlandDto> {

    override val featureToggle = FeatureToggles.brevmalInnvilgelseGjenlevendepensjonBosattNorgeEtterUtland.toggle

    override val kode = Pesysbrevkoder.Redigerbar.GP_INNVILGELSE_BOSATT_NORGE_ETTER_UTLAND
    override val kategori = Brevkategori.SLUTTBEHANDLING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.GJENLEV)

    override val template = createTemplate(
        languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - innvilgelse av ytelse til gjenlevende (bosatt Norge etter utland)",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {
        //PE_GP_04_025_overskrift
        title {
            text(
                bokmal { +"Gjenlevendepensjon - melding om endelig vedtak" },
                nynorsk { +"Gjenlevandepensjon - melding om endeleg vedtak" },
                english { +"Survivor's pension - notification of final decision" }
            )
        }
        outline {
            //PE_GP_04_025_tabell1
            paragraph {
                text(
                    bokmal { +"Nav viser til din søknad om gjenlevendepensjon mottatt " + pesysData.kravMottattDato.format() },
                    nynorsk { +"Nav viser til søknaden din om gjenlevandepensjon motteken " + pesysData.kravMottattDato.format() },
                    english { +"Nav refers to your application for a survivor's pension, received on " + pesysData.kravMottattDato.format() }
                )
                text(
                    bokmal {
                        +". Pensjonen har blitt utbetalt som en foreløpig ytelse basert på avdødes norske trygdetid og poengopptjening i påvente av opplysninger fra "
                        +fritekst("land") + "."
                    },
                    nynorsk {
                        +". Pensjonen har blitt utbetalt som ei foreløpig yting basert på den avdøde si norske trygdetid og poengopptening i påvente av opplysningar frå "
                        +fritekst("land") + "."
                    },
                    english {
                        +". Your pension has been paid as a preliminary benefit based on the deceased's period of national insurance cover and pension rights earned in Norway. We are awaiting information from "
                        +fritekst("land") + "."
                    }
                )
            }
            paragraph {
                text(
                    bokmal { +"Opplysningene fra " + fritekst("land") + " medfører ingen endring i pensjonen." },
                    nynorsk { +"Opplysningane frå " + fritekst("land") + " fører ikkje til endringar i pensjonen." },
                    english { +"The information from " + fritekst("land") + " does not result in any changes to your pension." }
                )
            }
            paragraph {
                text(
                    bokmal { +"For detaljer om pensjonen din, samt informasjon om rettigheter og plikter som pensjonsmottaker, viser vi til tidligere vedtaksbrev." },
                    nynorsk { +"For detaljar om pensjonen din, samt informasjon om rettar og plikter som pensjonsmottaker, viser vi til tidlegare vedtaksbrev." },
                    english { +"For details about your pension, as well as information regarding your rights and duties as a pensioner, please refer to our earlier decision letter." }
                )
            }
            //PE_GP_04_025_dine_rettigheter
            title1 {
                text(
                    bokmal { +"Dine rettigheter" },
                    nynorsk { +"Dine rettar" },
                    english { +"Your rights" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Du har som hovedregel rett til å se sakens dokumenter etter bestemmelsene i forvaltningsloven paragraf 18." },
                    nynorsk { +"Du har som hovudregel rett til å sjå dokumenta i saka etter reglane i forvaltningslova paragraf 18." },
                    english { +"In general, you are entitled to see all the documents in the case, pursuant to section 18 of the Public Administration Act." },
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Hvis du mener at vedtaket ikke er i samsvar med det du har søkt om, kan du klage på vedtaket etter bestemmelsene i folketrygdloven paragraf 21-12. "
                        +"Fristen for å klage er seks uker fra du mottar dette brevet."
                    },
                    nynorsk {
                        +"Dersom du meiner at vedtaket ikkje er i samsvar med det du har søkt om, kan du klage på vedtaket etter reglane i folketrygdlova paragraf 21‑12. "
                        +"Fristen for å klage er seks veker frå du får dette brevet."
                    },
                    english {
                        +"If you believe that the decision is not in accordance with what you applied for, you can appeal the decision, pursuant to the provisions in section 21-12 of the National Insurance Act. "
                        +"The time limit for filing an appeal is six weeks from the date you received this letter."
                    },
                )
            }
            paragraph {
                text(
                    bokmal { +"Se vår nettside $NAV_URL, eller ta kontakt med Nav dersom du ønsker mer informasjon." },
                    nynorsk { +"Sjå nettsida vår $NAV_URL, eller ta kontakt med Nav dersom du ønskjer meir informasjon." },
                    english {
                        +"Please contact Nav at +47 $navKontaktsenterPensjon if you would like more information. "
                        +"Remember that you can find more information about the regulations at $NAV_URL."
                    },
                )
            }
        }
        includeAttachment(vedleggFolketrygdenGjenlevendepensjon)
    }
}