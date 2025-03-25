package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.api.model.AlderspensjonBeregnetEtter
import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkstype.*
import no.nav.pensjon.brev.api.model.MetaforceSivilstand.*
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.AlderspensjonGjeldendeSelectors.grunnpensjonSats
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.AlderspensjonGjeldendeSelectors.regelverkstype
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.BrukerSelectors.foedselsDato
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.BrukerSelectors.sivilstand
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.EPSgjeldendeSelectors.mottarPensjon_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.GjeldendeBeregnetPensjonSelectors.beregnetEtter
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.GjeldendeBeregnetPensjonSelectors.fullTrygdetid
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.GjeldendeBeregnetPensjonSelectors.grunnbeloep
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.GjeldendeBeregnetPensjonSelectors.grunnpensjon
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.InstitusjonsoppholdGjeldendeSelectors.aldersEllerSykehjem_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.InstitusjonsoppholdGjeldendeSelectors.ensligPaInst_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.InstitusjonsoppholdGjeldendeSelectors.epsPaInstitusjon_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.InstitusjonsoppholdGjeldendeSelectors.fengsel_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.InstitusjonsoppholdGjeldendeSelectors.helseinstitusjon_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.alderspensjonGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.bruker
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.epsGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.erBeregnetSomEnsligEllerEnke
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.erBeregnetSomEnsligPartner
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.gjeldendeBeregnetPensjonPerManed
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.institusjonsoppholdGjeldende
import no.nav.pensjon.brev.maler.fraser.vedlegg.maanedligPensjonFoerSkatt.TabellMaanedligPensjonKap19
import no.nav.pensjon.brev.maler.fraser.vedlegg.maanedligPensjonFoerSkatt.TabellMaanedligPensjonKap19og20
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr


@TemplateModelHelpers
val maanedligPensjonFoerSkatt = createAttachment<LangBokmalNynorskEnglish, MaanedligPensjonFoerSkattDto>(
    title = newText(
        Bokmal to "Dette er din månedlige pensjon før skatt",
        Nynorsk to "Dette er den månadlege pensjonen din før skatt",
        English to "This is your monthly pension before tax",
    ),
    includeSakspart = false, // TODO skal den ha saksinfo med?
    outline = {
        showIf(
            alderspensjonGjeldende.regelverkstype.isOneOf(
                AP1967,
                AP2011
            )
        ) {
            includePhrase(TabellMaanedligPensjonKap19(gjeldendeBeregnetPensjonPerManed))
        }.orShowIf(alderspensjonGjeldende.regelverkstype.isOneOf(AP2016)) {
            includePhrase(
                TabellMaanedligPensjonKap19og20(
                    beregnetPensjon = gjeldendeBeregnetPensjonPerManed,
                    brukersFoedselsdato = bruker.foedselsDato,
                    alderspensjonGjeldende = alderspensjonGjeldende
                )
            )
        }

        title1 {
            text(
                Bokmal to "Slik er pensjonen din satt sammen",
                Nynorsk to "Slik er pensjonen din sett saman",
                English to "This is how your pension is calculated",
            )
        }

        val beregnetSomEnsligPgaInstopphold = (institusjonsoppholdGjeldende.aldersEllerSykehjem_safe.ifNull(false)
                or institusjonsoppholdGjeldende.ensligPaInst_safe.ifNull(false)
                or institusjonsoppholdGjeldende.epsPaInstitusjon_safe.ifNull(false)
                or institusjonsoppholdGjeldende.fengsel_safe.ifNull(false)
                or institusjonsoppholdGjeldende.helseinstitusjon_safe.ifNull(false))

        showIf(bruker.sivilstand.isOneOf(GLAD_EKT, SEPARERT) or
                (bruker.sivilstand.isOneOf(GIFT) and beregnetSomEnsligPgaInstopphold)) {
            paragraph {
                text(
                    Bokmal to "Du og ektefellen din er registrert med forskjellig bosted, eller en av dere bor på institusjon. Pensjonen din er derfor beregnet som om du var enslig.",
                    Nynorsk to "Du og ektefellen din er registrerte med forskjellig bustad, eller ein av dykk bur på institusjon. Pensjonen din er derfor berekna som om du var einsleg.",
                    English to "You and your spouse are registered with different residences, or one of you is living in an institution. Therefore, your pension has been calculated as if you were single.",
                )
            }
        }

        showIf(bruker.sivilstand.isOneOf(GLAD_PART, SEPARERT_PARTNER) or
                (bruker.sivilstand.isOneOf(PARTNER) and beregnetSomEnsligPgaInstopphold)) {
            paragraph {
                text(
                    Bokmal to "Du og partneren din er registrert med forskjellig bosted, eller en av dere bor på institusjon. Pensjonen din er derfor beregnet som om du var enslig.",
                    Nynorsk to "Du og partnaren din er registrerte med forskjellig bustad, eller ein av dykk bur på institusjon. Pensjonen din er derfor berekna som om du var einsleg.",
                    English to "You and your partner are registered with different residences, or one of you is living in an institution. Therefore, your pension has been calculated as if you were single.",
                )
            }
        }

        ifNotNull(
            gjeldendeBeregnetPensjonPerManed.beregnetEtter,
            gjeldendeBeregnetPensjonPerManed.grunnpensjon
        ) { beregnetEtter, grunnpensjon ->
            showIf(gjeldendeBeregnetPensjonPerManed.fullTrygdetid and beregnetEtter.isOneOf(AlderspensjonBeregnetEtter.EGEN)) {

                // TODO er ikke regelverkstype sjekk redundant?
                showIf(alderspensjonGjeldende.regelverkstype.isOneOf(AP1967, AP2011, AP2016)) {
                    paragraph {
                        text(
                            Bokmal to "Grunnpensjon ",
                            Nynorsk to "Grunnpensjon ",
                            English to "The basic pension ",
                            FontType.BOLD
                        )
                        textExpr(
                            Bokmal to "fastsettes med utgangspunkt i folketrygdens grunnbeløp, som for tiden er ".expr() + gjeldendeBeregnetPensjonPerManed.grunnbeloep.format() + " kroner.",
                            Nynorsk to "blir fastsett med utgangspunkt i grunnbeløpet i folketrygda, som for tida er ".expr() + gjeldendeBeregnetPensjonPerManed.grunnbeloep.format() + " kroner.",
                            English to "is calculated on the basis of the National Insurance basic amount (G), which is currently NOK ".expr() + gjeldendeBeregnetPensjonPerManed.grunnbeloep.format() + ".",
                        )
                    }
                }


                showIf(bruker.sivilstand.isOneOf(ENSLIG, ENKE, GLAD_EKT, GLAD_PART, SEPARERT, SEPARERT_PARTNER, GIFT, PARTNER)
                        or (bruker.sivilstand.isOneOf(GIFT, PARTNER)
                        and beregnetSomEnsligPgaInstopphold)
                ) {
                    paragraph {
                        textExpr(
                            Bokmal to "Du får full grunnpensjon fordi du er enslig pensjonist. Det utgjør ".expr() + alderspensjonGjeldende.grunnpensjonSats.format() + " prosent av grunnbeløpet.",
                            Nynorsk to "Du får full grunnpensjon fordi du er einsleg pensjonist. Det utgjer ".expr() + alderspensjonGjeldende.grunnpensjonSats.format() + " prosent av grunnbeløpet.",
                            English to "As a single pensioner you will receive full basic pension. This is equivalent to ".expr() + alderspensjonGjeldende.grunnpensjonSats.format() + " percent of the National Insurance basic amount.",
                        )
                    }
                }

                showIf(epsGjeldende.mottarPensjon_safe.ifNull(false)
                    and epsGjeldende.mottarPensjon_safe.ifNull(false)
                    and not(beregnetSomEnsligPgaInstopphold)
                ){
                    paragraph {
                        textExpr(
                            Bokmal to "Grunnpensjonen er justert til ".expr() +
                                    alderspensjonGjeldende.grunnpensjonSats.format() + " prosent av beløpet fordi [_Script Script_Tekst_002_] din mottar uføretrygd, pensjon eller omstillingsstønad fra folketrygden eller AFP som det godskrives pensjonspoeng for.",

                            Nynorsk to "Grunnpensjonen er justert til ".expr() +
                                    alderspensjonGjeldende.grunnpensjonSats.format() + " prosent av beløpet fordi [_Script Script_Tekst_002_] din mottar uføretrygd, pensjon eller omstillingsstønad frå folketrygda eller AFP som det blir godskrive pensjonspoeng for.",

                            English to "The basic pension is adjusted to ".expr() +
                                    alderspensjonGjeldende.grunnpensjonSats.format() + " percent of this amount because your [_Script Script_Tekst_002_] is receiving disability benefit, a national insurance pension or adjustment allowance, or contractual early retirement pension (AFP) which earns pension points.",
                        )
                    }
                }

            }
        }


    }
)