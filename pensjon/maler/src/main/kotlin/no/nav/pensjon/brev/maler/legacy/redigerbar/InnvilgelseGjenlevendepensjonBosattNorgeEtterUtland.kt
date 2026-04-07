package no.nav.pensjon.brev.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseGjenlevendepensjonBosattNorgeEtterUtlandDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseGjenlevendepensjonBosattNorgeEtterUtlandDtoSelectors.PesysDataSelectors.kravMottattDato
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseGjenlevendepensjonBosattNorgeEtterUtlandDtoSelectors.pesysData
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.generated.TBU2212_Generated
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlageGjenlevendepensjon
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

    override val featureToggle = FeatureToggles.brevmalAvslagGjenlevendepensjonUtland.toggle

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
        title {
            text(
                bokmal { +"Gjenlevendepensjon - melding om vedtak" },
                nynorsk { +"Gjenlevendepensjon - melding om vedtak" },
                english { +"Survivor's pension - notification of decision" }
            )
        }
        outline {
            paragraph {
                text(
                    bokmal { +"Nav viser til din søknad om gjenlevendepensjon mottatt " + pesysData.kravMottattDato.format() },
                    nynorsk { +"Nav viser til søknaden din om attlevandepensjon motteken " + pesysData.kravMottattDato.format() },
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
                    bokmal { +"Opplysningene fra " +fritekst("land") + " medfører ingen endring i pensjonen." },
                    nynorsk { +"Opplysningane frå " +fritekst("land") + " fører ikkje til endringar i pensjonen." },
                    english { +"The information from " +fritekst("land") + " does not result in any changes to your pension." }
                )
            }
            paragraph {
                text(
                    bokmal { +"For detaljer om pensjonen din, samt informasjon om rettigheter og plikter som pensjonsmottaker, viser vi til tidligere vedtaksbrev." },
                    nynorsk { +"For detaljar om pensjonen din, samt informasjon om rettar og plikter som pensjonsmottaker, viser vi til tidlegare vedtaksbrev." },
                    english { +"For details about your pension, as well as information regarding your rights and duties as a pensioner, please refer to our earlier decision letter." }
                )
            }
            includePhrase(TBU2212_Generated(vedleggDineRettigheterOgMulighetTilAaKlageGjenlevendepensjon))
            includePhrase(Felles.RettTilAAKlage)
            includePhrase(Felles.RettTilInnsyn(vedleggDineRettigheterOgMulighetTilAaKlageGjenlevendepensjon))
        }
        includeAttachment(vedleggDineRettigheterOgMulighetTilAaKlageGjenlevendepensjon)
    }
}