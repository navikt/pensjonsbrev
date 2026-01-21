package no.nav.pensjon.brev.alder.maler.info

import no.nav.pensjon.brev.alder.maler.Brevkategori
import no.nav.pensjon.brev.alder.maler.brev.FeatureToggles
import no.nav.pensjon.brev.alder.model.Aldersbrevkoder
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object BekreftelseAvUtsendtKravTilUtlandet : RedigerbarTemplate<EmptyRedigerbarBrevdata> {
    override val kode = Aldersbrevkoder.Redigerbar.INFO_BEKREFTELSE_UTSENDING_KRAV_TIL_UTLANDET

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Bekreftelse på krav sendt til utlandet",
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV
        )
    ) {
        title {
            text(
                bokmal { +"Bekreftelse på krav sendt til utlandet" },
                nynorsk { +"Stadfesting av krav sendt til utlandet" },
                english { +"Confirmation of claim sent to foreign social security authorities" },
            )
        }

        outline {
            paragraph {
                val landFritekst = fritekst("land")
                text(
                    bokmal {
                        +"Vi bekrefter å ha sendt søknad om alderspensjon på dine vegne til " + landFritekst + " trygdemyndigheter. "
                        +"Det er opp til trygdemyndighetene der å avgjøre når du tidligst kan ta ut utenlandsk alderspensjon."
                    },
                    nynorsk {
                        +"Vi stadfestar at vi har sendt søknad om alderspensjon på dine vegner til " + landFritekst + " trygdestyresmakter. "
                        +"Det er opp til trygdestyresmaktene der å avgjere når du tidlegast kan ta ut utanlandsk alderspensjon."
                    },
                    english {
                        +"We hereby confirm that we have sent a claim for retirement pension on your behalf to the " + landFritekst + " social security authorities."
                    }
                )
            }

            paragraph {
                val landFritekst = fritekst("land")
                text(
                    bokmal {
                        +"Dersom du ønsker mer informasjon om " + landFritekst + " alderspensjon eller saksbehandlingen der, kan du ta kontakt direkte med trygdemyndighetene:"
                    },
                    nynorsk {
                        +"Om du ønsker meir informasjon om " + landFritekst + " alderspensjon eller saksbehandlingstidene der, kan du ta kontakt direkte med trygdestyresmaktene:"
                    },
                    english {
                        +"If you want more information regarding retirement pension from " + landFritekst + " you can contact them directly:"
                    }
                )
            }

            paragraph {
                eval(fritekst("navn og adresse til utenlandsk trygdemyndighet"))
            }
        }
    }

    override val kategori = Brevkategori.INFORMASJONSBREV

    override val brevkontekst: TemplateDescription.Brevkontekst = TemplateDescription.Brevkontekst.ALLE

    override val sakstyper: Set<Sakstype> = setOf(Sakstype.ALDER)
}