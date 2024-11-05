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
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object GodskrivingOmsorgspoengEtter1991 : RedigerbarTemplate<PEDto> {

    // PE_IY_05_201
    override val kode = Brevkode.Redigerbar.GODSKRIVING_AV_PENSJONSOPPTJENING_FOR_OMSORG_BARN_ETTER_1991
    override val kategori: TemplateDescription.Brevkategori =
        TemplateDescription.Brevkategori.OMSORGSOPPTJENING
    override val brevkontekst: TemplateDescription.Brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.OMSORG)

    override val template = createTemplate(
        name = kode.name,
        letterDataType = PEDto::class,
        languages = languages(Bokmal, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Godskriving av pensjonsopptjening (omsorgsopptjening) fordi du har omsorg for små barn",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV
        )
    ) {
        //IF(   PE_pebrevkode = "PE_IY_05_201"    AND      (FF_GetArrayElement_Integer(PE_Grunnlag_OmsorgGodskrGrunnlagListe_OmsorgGodskrGrunnlagAr,1) > 1991      OR Year(PE_PersonSak_Fodselsdato) < 1948      OR Year(PE_PersonSak_Fodselsdato) > 1953)    )THEN      INCLUDE ENDIF

        title {
            //[PE_IY_05_Overskrift1]

            text(
                Bokmal to "Godskriving av pensjonsopptjening (omsorgsopptjening) fordi du har omsorg for små barn",
                English to "Accredited pension earning (for care work) because you care for young children",
            )
        }
        outline {
            //[PE_IY_05_TB1125,TB1131]

            paragraph {
                text(
                    Bokmal to "Du har fått godskrevet omsorgsopptjening for følgende inntektsår fordi du har eller har hatt omsorg for små barn.",
                    English to "You have been accredited acquired rights for care work for the following years because you care for/have cared for young children.",
                )
                list {
                    //[PE_IY_05_TB1125,TB1131]
                    forEach(pesysData.pe.grunnlag_omsorggodskrgrunnlagliste_omsorggodskrgrunnlagar()) {
                        item {
                            textExpr(
                                Bokmal to it.format(),
                                English to it.format()
                            )
                        }
                    }
                }
            }

            //IF(   PE_pebrevkode = "PE_IY_05_201"    AND      (FF_GetArrayElement_Integer(PE_Grunnlag_OmsorgGodskrGrunnlagListe_OmsorgGodskrGrunnlagAr,1) > 1991      OR Year(PE_PersonSak_Fodselsdato) < 1948      OR Year(PE_PersonSak_Fodselsdato) > 1953)    )THEN      INCLUDE ENDIF
            //[PE_IY_05_TB1126,TB1124,TB1127]

            paragraph {
                text(
                    Bokmal to "Omsorgsopptjening blir godskrevet den som har den daglige omsorgen for barn under seks år (sju år før 2010). Det er opplysninger om utbetaling av barnetrygd som legges til grunn for hvem som godskrives omsorgsopptjening. Omsorgsopptjeningen blir automatisk godskrevet den forelderen som mottar barnetrygden.",
                    English to "Acquired rights for care work is credited to the person responsible for the day-to-day care of children under six years old (seven years prior to 2010). Decisions about the person to whom the rights are credited is based on information about child benefit payment. The acquired rights are automatically credited to the parent who receives the child benefit.",
                )
            }
            //[PE_IY_05_TB1126,TB1124,TB1127]

            title1 {
                text(
                    Bokmal to "Hva er omsorgsopptjening?",
                    English to "What are acquired rights for care work?"
                )
            }

            paragraph {
                text(
                    Bokmal to "Omsorgsopptjeningen tilsvarte for hvert år før 2010 en inntekt på 4 ganger folketrygdens grunnbeløp. Fra og med 2010 tilsvarer omsorgsopptjeningen 4,5 ganger grunnbeløpet. Omsorgsopptjening kan bidra til at du får høyere pensjon enn du ellers ville fått.",
                    English to "In years before 2010, acquired rights for care work were calculated as an income of four times the national insurance basic amount (G) per year. From 2010, acquired rights for care work equal 4.5 times the national insurance basic amount. Acquired rights for care work may help ensure that you receive a higher pension than you would have done otherwise.",
                )
            }
            //[PE_IY_05_TB1126,TB1124,TB1127]

            paragraph {
                text(
                    Bokmal to "Omsorgsopptjening gis ved at en inntekt tilsvarende 4 eller 4,5 ganger grunnbeløpet legges til grunn for opptjeningen for det aktuelle året. Annen opptjening kan ikke legges sammen med omsorgsopptjening. Dersom du har annen pensjonsopptjening i det aktuelle året som er høyere enn omsorgsopptjeningen legges denne opptjeningen til grunn.",
                    English to "Acquired rights for care work are considered equivalent to the rights acquired by an income of four or 4.5 times the national insurance basic amount for the year. Other earnings may not be combined with acquired rights for care work. If, in the year in question, you accumulated pension rights in some other way and these pension rights are higher than the acquired rights for care work, the higher pension rights are used to calculate your pension.",
                )
            }
            //[PE_IY_05_TB1126,TB1124,TB1127]

            paragraph {
                text(
                    Bokmal to "I nettjenesten Din pensjon på $NAV_URL får du oversikt over hele pensjonsopptjeningen din. Her finner du også omsorgsopptjening som du har fått godskrevet.",
                    English to "NAV has an online pension service \"Din pensjon\" on $NAV_URL which shows your pensionable income and how much pension you have accumulated. On this page you can also find details of the points you have been accredited for care work.",
                )
            }
            //[PE_IY_05_TB1126,TB1124,TB1127]

            title1 {
                text(
                    Bokmal to "Overføring av omsorgsopptjening til den andre forelderen",
                    English to "Transfer of acquired rights for care work to the other parent"
                )
            }

            paragraph {
                text(
                    Bokmal to "Det er bare en av foreldrene som får godskrevet omsorgsopptjening for hvert enkelt år, selv om det er flere små barn i familien. Foreldrene kan be om at omsorgsopptjeningen blir godskrevet den andre forelderen hvis de er sammen om omsorgen for barna.",
                    English to "Only one of the parents can be credited with acquired rights for care work each year even if there are several children under six years of age in the family. Parents can request that the other parent is credited with acquired rights for care work if they both care for the children together.",
                )
            }
            //[PE_IY_05_TB1126,TB1124,TB1127]

            paragraph {
                text(
                    Bokmal to "Hvis du ønsker at den andre forelderen skal godskrives omsorgsopptjeningen, må du melde fra til oss via nettjenesten Din pensjon på $NAV_URL eller på skjemaet som du finner på $NAV_URL: ”Overføring av omsorgsopptjening” (NAV 03-16.10). Kontakt oss gjerne på telefon $NAV_KONTAKTSENTER_TELEFON_PENSJON hvis du trenger hjelp.",
                    English to "If you would like the other parent to be credited with acquired rights for care work, you must notify us via the online service \"Din pensjon\" at $NAV_URL, or in writing, using the form \"Transferring acquired rights for care work\" (NAV 03-16.10). If you need any help, please call us at $NAV_KONTAKTSENTER_TELEFON_PENSJON.",
                )
            }

            //[PE_IY_05_TB1888,TB1181]

            includePhrase(Felles.BokmalEnglish.HarDuSpoersmaal.pensjon)
        }
    }
}

