package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.OmsorgLegacyDataDto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.OmsorgLegacyDataDtoSelectors.PesysDataSelectors.omsorgLegacyData
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.OmsorgLegacyDataDtoSelectors.pesysData
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.legacy.grunnlag_omsorggodskrgrunnlagliste_omsorggodskrgrunnlagar
import no.nav.pensjon.brev.maler.legacy.omsorg_vedtaksdata_kravhode_kravmottatdato
import no.nav.pensjon.brev.model.format
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
import no.nav.pensjon.brev.template.includePhrase
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object GodskrivingOmsorgspoengFoer1991 : RedigerbarTemplate<OmsorgLegacyDataDto> {

    // PE_IY_05_201
    override val kode = Brevkode.Redigerbar.GODSKRIVING_AV_PENSJONSOPPTJENING_FOR_OMSORG_BARN_FOER_1991
    override val kategori = TemplateDescription.Brevkategori.OMSORGSOPPTJENING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.OMSORG)

    override val template = createTemplate(
        name = kode.name,
        letterDataType = OmsorgLegacyDataDto::class,
        languages = languages(Bokmal, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Godskriving av pensjonsopptjening (omsorgsopptjening) fordi du har omsorg for små barn",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {
        title {
            //IF(PE_pebrevkode = "PE_IY_05_201" AND FF_GetArrayElement_Integer(PE_Grunnlag_OmsorgGodskrGrunnlagListe_OmsorgGodskrGrunnlagAr,1) <= 1991 AND Year(PE_PersonSak_Fodselsdato) >= 1948 AND Year(PE_PersonSak_Fodselsdato) <= 1953) THEN      INCLUDE ENDIF
            //[PE_IY_05_Overskrift1 2]

            text(
                Bokmal to "Godskriving av pensjonsopptjening for omsorg for barn under sju år før 1992 - melding om vedtak",
                English to "Accreditation of acquired rights for the care of children below the age of seven prior to 1992 - notification of decision",
            )
        }
        outline {
            //[PE_IY_05_TB1125,TB1131]

            paragraph {
                textExpr(
                    Bokmal to "NAV har innvilget din søknad mottatt ".expr() + pesysData.omsorgLegacyData.omsorg_vedtaksdata_kravhode_kravmottatdato()
                        .format() + " om pensjonsopptjening for omsorg for barn under sju år før 1992.",
                    English to "NAV has granted your application received on ".expr() + pesysData.omsorgLegacyData.omsorg_vedtaksdata_kravhode_kravmottatdato()
                        .format() + " for acquired rights for the care of children below the age of seven prior to 1992.",
                )
            }

            //IF(FF_GetArrayElement_Integer(PE_Grunnlag_OmsorgGodskrGrunnlagListe_OmsorgGodskrGrunnlagAr,1) <= 1991 AND Year(PE_PersonSak_Fodselsdato) >= 1948 AND Year(PE_PersonSak_Fodselsdato) <= 1953) THEN      INCLUDE ENDIF
            //[PE_IY_05_TB1125,TB1131]

            paragraph {
                text(
                    Bokmal to "Vedtaket er gjort etter AFP-tilskottsloven paragraf 6 og folketrygdloven paragraf 3-16.",
                    English to "The decision is based on the Contractual Pension Subsidy Act (\"AFP-tilskottsloven\") paragraph 6 and the National Insurace Act paragraph 3-16.",
                )
            }

            //IF(FF_GetArrayElement_Integer(PE_Grunnlag_OmsorgGodskrGrunnlagListe_OmsorgGodskrGrunnlagAr,1) <= 1991 AND Year(PE_PersonSak_Fodselsdato) >= 1948 AND Year(PE_PersonSak_Fodselsdato) <= 1953) THEN      INCLUDE ENDIF
            //[PE_IY_05_TB1125,TB1131]

            paragraph {
                text(
                    Bokmal to "Du har fått godskrevet pensjonsopptjening for følgende inntektsår:",
                    English to "You have been accredited acquired rights for the following year(s):",
                )
                list {
                    //[PE_IY_05_TB1125,TB1131]
                    forEach(pesysData.omsorgLegacyData.grunnlag_omsorggodskrgrunnlagliste_omsorggodskrgrunnlagar()) {
                        item {
                            textExpr(
                                Bokmal to it.format(),
                                English to it.format()
                            )
                        }
                    }
                }
            }
            //IF(PE_pebrevkode = "PE_IY_05_201" AND FF_GetArrayElement_Integer(PE_Grunnlag_OmsorgGodskrGrunnlagListe_OmsorgGodskrGrunnlagAr,1) <= 1991 AND Year(PE_PersonSak_Fodselsdato) >= 1948 AND Year(PE_PersonSak_Fodselsdato) <= 1953) THEN      INCLUDE ENDIF
            //[PE_IY_05_TB1126,TB1124,TB1127 2]

            title1 {
                text(
                    Bokmal to "Din rett til innsyn og klage",
                    English to "Your right to inspection and to file an appeal"
                )
        }

            paragraph {
                text(
                    Bokmal to "Du har som hovedregel rett til å se sakens dokumenter etter bestemmelsene i forvaltningsloven, paragraf 18.",
                    English to "As a main rule, you are entitled to see all case documents, pursuant to section 18 of the Public Administration Act.",
                )
            }
            //[PE_IY_05_TB1126,TB1124,TB1127 2]

            paragraph {
                text(
                    Bokmal to "Hvis du mener at vedtaket ikke er i samsvar med det du har søkt om, kan du klage på vedtaket etter bestemmelsene i folketrygdloven, paragraf 21-12. Fristen for å klage er seks uker fra du mottar dette brevet.",
                    English to "If you believe that the decision is not in accordance with what you applied for, you can appeal the decision, pursuant to section 21-12 of the National Insurance Act. The time limit for filing an appeal is six weeks from the date you received this letter.",
                )
            }
            //[PE_IY_05_TB1888,TB1181]

            includePhrase(Felles.HarDuSpoersmaal.alder)
        }
    }
}

