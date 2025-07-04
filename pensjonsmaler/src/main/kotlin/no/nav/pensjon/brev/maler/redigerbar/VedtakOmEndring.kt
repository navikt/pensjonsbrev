package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakOmEndringDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakOmEndringDtoSelectors.PesysDataSelectors.grunnbeloep
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakOmEndringDtoSelectors.PesysDataSelectors.maanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakOmEndringDtoSelectors.PesysDataSelectors.orienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakOmEndringDtoSelectors.pesysData
import no.nav.pensjon.brev.maler.fraser.common.Vedtak
import no.nav.pensjon.brev.maler.vedlegg.vedleggMaanedligPensjonFoerSkatt
import no.nav.pensjon.brev.maler.vedlegg.vedleggOrienteringOmRettigheterOgPlikter
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brev.template.namedReference
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object VedtakOmEndring : RedigerbarTemplate<VedtakOmEndringDto> {
    override val kode = Pesysbrevkoder.Redigerbar.PE_VEDTAK_OM_ENDRING
    override val kategori = TemplateDescription.Brevkategori.VEDTAK_ENDRING_OG_REVURDERING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = Sakstype.pensjon // Trur eg?

    override val template = createTemplate(
        name = kode.name,
        letterDataType = VedtakOmEndringDto::class,
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak om endring",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {
        title {
            text(
                Bokmal to "Vedtak om endring",
                Nynorsk to "Vedtak om endring"
            )
        }
        outline {
            paragraph {
                textExpr(
                    Bokmal to "Nav viser til varsel om inntektskontroll datert ".expr() + fritekst("dato") +". Vi har endret alderspensjonen din fordi inntekten til ektefellen din er høyere enn tidligere registrert. Endringen gjelder fra " + fritekst("dato") + ". Du får " + fritekst("sum") + " kroner i alderspensjon hver måned før skatt.",
                    Nynorsk to "Nav viser til varsel om inntektskontroll datert ".expr() + fritekst("dato") + ". Vi har endra alderspensjonen din fordi inntekta til ektefellen din er høgare enn tidlegare registrert. Endringa gjeld frå " + fritekst("dato") + ". Du får " + fritekst("sum") + " kroner i alderspensjon kvar månad før skatt."
                )
            }
            includePhrase(Vedtak.BegrunnelseOverskrift)
            paragraph {
                text(
                    Bokmal to "Vedtaket er gjort etter folketrygdlovens §§ 3-2, 21-3 og 21-4.",
                    Nynorsk to "Vedtaket er gjort etter folketrygdlovas §§ 3-2, 21-3 og 21-4."
                )
            }
            paragraph {
                textExpr(
                    Bokmal to "Folketrygdens grunnbeløp utgjør per i dag ".expr() + pesysData.grunnbeloep.format() + ". Grunnbeløpet justeres 1. mai hvert år.",
                    Nynorsk to "Grunnbeløpet i folketrygda utgjer per i dag ".expr() + pesysData.grunnbeloep.format () + ". Grunnbeløpet blir justert 1. mai kvart år."
                )
            }
            paragraph {
                text(
                    Bokmal to "En alderspensjonist skal ha redusert grunnpensjon når vedkommende har ektefelle, samboer eller partner har inntekt over to ganger grunnbeløpet eller mottar uføretrygd, alderspensjon eller AFP som det godskrives pensjonspoeng for. Når inntekten er over to ganger grunnbeløpet skal grunnpensjonen din justeres til 85 prosent før 1. september 2016 og til 90 prosent fra og med 1. september 2016.",
                    Nynorsk to "Ein alderspensjonist skal ha redusert grunnpensjon når vedkommande har ektefelle, sambuar eller partnar som har inntekt over to gongar grunnbeløpet eller mottar uføretrygd, alderspensjon eller AFP som det blir godskrive pensjonspoeng for. Når inntekta er over to gangar grunnbeløpet skal grunnpensjonen din verte justert til 85 prosent før 1. september 2016 og til 90 prosent frå og med 1. september 2016."
                )
            }
            paragraph {
                Bokmal to "Nav har rett til å kontrollere om vilkårene for retten til å motta en ytelse er oppfylt. Vi har som følge av dette foretatt en inntektskontroll basert på opplysninger mottatt fra deg og Skatteetaten for å vurdere om retten til full grunnpensjon fremdeles er oppfylt."
                Nynorsk to "Nav har rett til å kontrollere om vilkåra for retten til å motta ei yting er oppfylt. Vi har som følgje av dette gjort ein inntektskontroll basert på opplysningar mottatt frå deg og Skatteetaten for å vurdere om retten til full grunnpensjon framleis er oppfylt."
            }
            paragraph {
                textExpr(
                    Bokmal to fritekst("Da du søkte om alderspensjon, oppga du at / Da pensjonen din ble konvertert til alderspensjon ble det lagt til grunn at ektefellen/samboeren/partneren") + "din hadde inntekt under to ganger grunnbeløpet. Opplysninger fra Skatteetaten viser at " + fritekst("ektefellen/samboeren/partneren") + " din har hatt inntekt over to ganger grunnbeløpet i årene " + fritekst("(sett inn riktig år/perioder)") + ". Grunnpensjonen din " + fritekst("(og legg til eventuelle andre endringer)") + " skulle vært redusert fra " + fritekst("dato") + ".",
                    Nynorsk to fritekst("Då du søkte om alderspensjon, opplyste du at / Då pensjonen din vart konvertert til alderspensjon vart det lagt til grunn at ektefellen/sambuaren/partnaren") + " din hadde inntekt under to gangar grunnbeløpet. Opplysningar frå Skatteetaten viser at " + fritekst("ektefellen/sambuaren/partnaren") + " din har hatt inntekt over to gangar grunnbeløpet i åra " + fritekst("(set inn riktig år/periodar)") + ". Grunnpensjonen din " + fritekst("(og legg til eventuelle andre endringar)") + " skulle vore redusert frå " + fritekst("dato") + "."
                )
            }
            paragraph {
                text(
                    Bokmal to "Fra 1. januar 2018 har vi lagt til grunn en forventet inntekt som er over to ganger grunnbeløpet.",
                    Nynorsk to "Frå 1. januar 2018 har vi lagt til grunn ei forventa inntekt som er over to gangar grunnbeløpet."
                )
            }
            paragraph {
                text(
                    Bokmal to "I dette brevet forklarer vi hvilke rettigheter og plikter du har. Derfor er det viktig at du leser hele brevet og vedleggene.",
                    Nynorsk to "I dette brevet forklarer vi kva rettar og plikter du har. Derfor er det viktig at du les heile brevet og vedlegga."
                )
            }
            paragraph {
                text(
                    Bokmal to "I vedlegget ",
                    Nynorsk to "I vedlegget "
                )
                namedReference(vedleggMaanedligPensjonFoerSkatt)
                text(
                    Bokmal to " finner du en tabell som viser hvordan pensjonen din er satt sammen.",
                    Nynorsk to " finn du ein tabell som viser korleis pensjonen din er sett saman."
                )
            }
        }
        includeAttachment(vedleggOrienteringOmRettigheterOgPlikter, pesysData.orienteringOmRettigheterOgPlikterDto)
        includeAttachment(vedleggMaanedligPensjonFoerSkatt, pesysData.maanedligPensjonFoerSkattDto)
    }
}