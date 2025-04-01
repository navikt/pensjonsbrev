package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmGjenlevenderettigheterDto
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
class InformasjonOmGjenlevenderettigheter : RedigerbarTemplate<InformasjonOmGjenlevenderettigheterDto> {
    override val kategori: TemplateDescription.Brevkategori = TemplateDescription.Brevkategori.INFORMASJONSBREV
    override val brevkontekst: TemplateDescription.Brevkontekst = TemplateDescription.Brevkontekst.ALLE
    override val sakstyper: Set<Sakstype> = setOf(Sakstype.BARNEP, Sakstype.UFOREP, Sakstype.GJENLEV, Sakstype.ALDER)

    // 000069 / DOD_INFO_RETT_MAN
    override val kode = Pesysbrevkoder.Redigerbar.PE_INFORMASJON_OM_GJENLEVENDERETTIGHETER
    override val template = createTemplate(
        name = kode.name,
        letterDataType = InformasjonOmGjenlevenderettigheterDto::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Informasjon om gjenlevenderettigheter",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV
        ),
    ) {
        title {
            text(
                Bokmal to "Informasjon om gjenlevenderettigheter",
                Nynorsk to "Informasjon om attlevanderettar",
                English to "Information about survivor's rights",
            )
        }
        outline {
            paragraph {
                val fritekst = fritekst("avdød navn")
                textExpr(
                    Bokmal to "Vi skriver til deg fordi vi har mottatt melding om at ".expr() + fritekst + " er død, og du kan ha rettigheter etter avdøde.",
                    Nynorsk to "Vi skriv til deg fordi vi har fått melding om at ".expr() + fritekst + " er død, og du kan ha rettar etter avdøde.",
                    English to "We are writing to you because we have received notice that ".expr() + fritekst + " has died ,and you may have rights as a surviving spouse."
                )
            }

            title2 {
                text(

                    Nynorsk to "Kven kan ha rett til ytingar etter avdøde?",
                    English to "Who may be entitled to benefits as a surviving spouse?"
                )
            }
            paragraph {
                text(
                    Nynorsk to "Du kan lese meir om dette på ${Constants.NAV_URL}.",
                    English to "You can read more about this at ${Constants.NAV_URL}."
                )
            }

            title2 {
                text(
                    Nynorsk to "For deg som har barn under 18 år",
                English to "If you have children under the age of 18"
                )
            }
            paragraph {
                text(
                    Bokmal to "Forsørger du barn under 18 år, kan du ha rett til utvidet barnetrygd. I tillegg kan barn ha rett til barnepensjon. Du finner søknadskjema og mer informasjon om dette på ${Constants.NAV_URL}.",
                    Nynorsk to "Forsørgjer du barn under 18 år, kan du ha rett til utvida barnetrygd. I tillegg kan barn ha rett til barnepensjon. Du finn søknadsskjema og meir informasjon om dette på ${Constants.NAV_URL}.",
                    English to "If you provide for children under the age of 18, you may be entitled to extended child benefits. In addition, children may be entitled to a children's pension. You will find the application form and more information about this at ${Constants.NAV_URL}"
                )
            }

            title2 {
                text(
                    Nynorsk to "Rettar når avdøde har budd eller arbeidd i utlandet",
                    English to "Rights if the deceased has lived or worked abroad"
                )
            }
            paragraph {
                text(

                    Nynorsk to "Dersom avdøde har budd eller arbeidd i utlandet, kan dette få noko å seie for kor mykje du får utbetalt i pensjon. Noreg har trygdesamarbeid med ei rekkje land gjennom EØS-avtalen og andre avtalar. Derfor kan du også ha rett til pensjon frå andre land. Vi kan hjelpe deg med søknad til land Noreg har trygdeavtale med.",
                    English to "If the deceased has lived or worked abroad, this may affect the amount of your pension. Norway has social security cooperates with a number of countries through the EEA Agreement and other social security agreements. You may therefore also be entitled to a pension from other countries. We can help assist you with your application to apply to countries with which Norway has a social security agreement."
                )
            }

            title2 {
                text(
                    Nynorsk to "Andre pensjonsordningar",
                    English to "Other pension schemes"
                )
            }

            paragraph {
                text(
                    Nynorsk to "Dersom avdøde hadde ei privat eller offentleg pensjonsordning og du har spørsmål om dette, kan du kontakte arbeidsgivaren til den avdøde. Du kan også ta kontakt med pensjonsordninga eller forsikringsselskapet.",
                    English to "If the deceased was a member of a private or publicpension scheme and you have questions about this, you can contact the deceased's employer. You can also contact the pension scheme or insurance company."
                )
            }

            includePhrase(Felles.HarDuSpoersmaal.alder)

        }
    }
}