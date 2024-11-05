package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.PEDto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.PEDtoSelectors.PesysDataSelectors.pe
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.PEDtoSelectors.pesysData
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_URL
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.legacy.grunnlag_omsorggodskrgrunnlagliste_omsorggodskrgrunnlagar
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object PensjonsopptjeningForOmsorgsArbeidOrienteringOmGodskriving : RedigerbarTemplate<PEDto> {
    override val kategori: TemplateDescription.Brevkategori = TemplateDescription.Brevkategori.OMSORGSOPPTJENING
    override val brevkontekst: TemplateDescription.Brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper: Set<Sakstype> = setOf(Sakstype.OMSORG)
    override val kode = Brevkode.Redigerbar.UT_AVSLAG_UFOERETRYGD // TODO skal lage ny

    override val template = createTemplate(
        name = kode.name,
        letterDataType = PEDto::class,
        languages = languages(Bokmal, English),
        letterMetadata = LetterMetadata(
            displayTitle = "", // TODO
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG, // TODO
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV
        )
    )
    {
        title {
            text(
                Bokmal to "Pensjonsopptjening for omsorgsarbeid (omsorgsopptjening) - orientering om godskriving",
                English to "Acquired rights for care work - guidance about credits"
            )
        }

        outline {
            //[PE_IY_05_TB250,]

            paragraph {
                textExpr(
                    Bokmal to "Vi har registrert at du utfører omsorgsarbeid for barn med rett til forhøyet hjelpestønad etter sats 3 eller 4. Du har derfor fått godskrevet pensjonsopptjening for omsorgsarbeid for ".expr() + pesysData.pe.grunnlag_omsorggodskrgrunnlagliste_omsorggodskrgrunnlagar()
                        .format() + ". ",
                    English to "We have noted that you carry out care work with children and are entitled to the higher auxiliary benefit at rate 3 or 4. You have therefore been credited with acquired rights for care work in ".expr() + pesysData.pe.grunnlag_omsorggodskrgrunnlagliste_omsorggodskrgrunnlagar()
                        .format() + ". ",
                )
            }
            //[PE_IY_05_TB250,]

            paragraph {
                text(
                    Bokmal to "Hva er omsorgsopptjening?Omsorgsopptjeningen tilsvarte for hvert år før 2010 en inntekt på 4 ganger folketrygdens grunnbeløp. Fra og med 2010 tilsvarer omsorgsopptjeningen 4,5 ganger grunnbeløpet. Omsorgsopptjening kan bidra til at du får høyere pensjon enn du ellers ville fått. ",
                    English to "What are acquired rights for care work?In years before 2010, acquired rights for care work were calculated as an income of four times the national insurance basic amount (G) per year. From 2010, acquired rights for care work equal 4.5 times the national insurance basic amount. Acquired rights for care work may help ensure that you receive a higher pension than you would have done otherwise. ",
                )
            }
            //[PE_IY_05_TB250,]

            paragraph {
                text(
                    Bokmal to "Omsorgsopptjening gis ved at en inntekt tilsvarende 4 eller 4,5 ganger grunnbeløpet legges til grunn for opptjeningen for det aktuelle året. Annen opptjening kan ikke legges sammen med omsorgsopptjening. Dersom du har annen pensjonsopptjening i det aktuelle året som er høyere enn omsorgsopptjeningen legges denne opptjeningen til grunn. ",
                    English to "Acquired rights for care work are considered equivalent to the rights acquired by an income of four or 4.5 times the national insurance basic amount for the year. Other earnings may not be combined with acquired rights for care work. If, in the year in question, you accumulated pension rights in some other way and these pension rights are higher than the acquired rights for care work, the higher pension rights are used to calculate your pension.  ",
                )
            }
            //[PE_IY_05_TB250,]

            paragraph {
                text(
                    Bokmal to "I nettjenesten Din pensjon på $NAV_URL får du oversikt over hele pensjonsopptjeningen din. Her finner du også omsorgsopptjening som du har fått godskrevet. ",
                    English to "NAV has an online pension service \"Din pensjon\" on $NAV_URL which shows your pensionable income and how much pension you have accumulated. On this page you can also find details of the points you have been accredited for care work. ",
                )
            }
            //[PE_IY_05_TB250,]

            paragraph {
                text(
                    Bokmal to "Vilkårene for å få omsorgsopptjeningEn person som utfører ubetalt pleie- og omsorgsarbeid, kan ha rett til å få godskrevet omsorgsopptjening i folketrygden. Kravet er at pleie- og omsorgsarbeidet utgjør minst 22 timer i uken. Omsorgsarbeidet må også ha vart i til sammen minst seks måneder av et kalenderår. ",
                    English to "Conditions for receiving acquired rights for care workA person who carries out nursing and care work may be entitled to have acquired rights for care work credited with national insurance. The requirement is that nursing and care work amounts to at least 22 hours a week. In all, care work must also have amounted to at least six months of a calendar year. ",
                )
            }
            //[PE_IY_05_TB250,]

            paragraph {
                text(
                    Bokmal to "Når den pleietrengende får forhøyet hjelpestønad etter sats 3 eller 4, regner vi med at omsorgsarbeidet utgjør minst 22 timer pr. uke. En av foreldrene vil derfor ha rett til omsorgspoeng. ",
                    English to "When the person requiring care receives auxiliary benefit at rate 3 or 4, we calculate that care work amounts to at least 22 hours a week. One of the parents will therefore be entitled to acquired rights for care work.  ",
                )
            }
            //[PE_IY_05_TB250,]

            paragraph {
                text(
                    Bokmal to "Overføring av omsorgsopptjening til den andre forelderenOmsorgsopptjening blir automatisk godskrevet den forelderen som mottar barnetrygd for barnet. Foreldrene kan likevel be om at omsorgsopptjeningen blir godskrevet den andre forelderen hvis de er sammen om omsorgen for barnet. ",
                    English to "Transfer of acquired rights for care work to the other parentAcquired rights for care work will be credited automatically to the parent receiving child benefit for the child. Parents may however, request that acquired rights for care work are credited to the other parent if they are both caring for the child together. ",
                )
            }
            //[PE_IY_05_TB250,]

            paragraph {
                text(
                    Bokmal to "Hvis du ønsker at den andre forelderen skal godskrives omsorgsopptjeningen, må du melde fra til oss via nettjenesten Din pensjon på $NAV_URL eller på skjemaet som du finner på $NAV_URL: ”Overføring av omsorgsopptjening” (NAV 03-16.10). Kontakt oss gjerne på telefon $NAV_KONTAKTSENTER_TELEFON_PENSJON  hvis du trenger hjelp. ",
                    English to "If you would like the other parent to be credited with acquired rights for care work, you must notify us via the online service \"Din pensjon\" at $NAV_URL, or in writing, using the form \"Transferring acquired rights for care work\" (NAV 03-16.10). If you need any help, please call us at $NAV_KONTAKTSENTER_TELEFON_PENSJON. ",
                )
            }
            //[PE_IY_05_TB250,]

            paragraph {
                text(
                    Bokmal to "Automatisk godskriving eller søknad om omsorgsopptjeningOmsorgsopptjening blir godskrevet automatisk hvert år til og med kalenderåret retten til forhøyet hjelpestønad faller bort. ",
                    English to "Automatic crediting of, or application for, acquired rights for care workAcquired rights for care work will automatically be credited each year, up to and including the calendar year when the entitlement to higher rate auxiliary benefit ceases. ",
                )
            }
            //[PE_IY_05_TB250,]

            paragraph {
                text(
                    Bokmal to "Vær oppmerksom på at du selv må sette fram krav om omsorgspoeng hvis du fortsatt utfører ulønnet omsorgsarbeid etter at den forhøyede hjelpestønaden opphører.",
                    English to "Please note that you must submit the claim for acquired rights for care work yourself if you continue to carry out unpaid care work once the auxiliary benefit ceases.",
                )
            }

            includePhrase(Felles.HarDuSpoersmaal.pensjon)
        }
    }
}