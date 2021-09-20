//package no.nav.pensjon.brev.template
//
//import no.nav.pensjon.brev.template.base.DummyBase
//import org.junit.jupiter.api.Assertions.assertEquals
//import org.junit.jupiter.api.Test
//import org.junit.jupiter.api.assertThrows
//
//class LetterTest {
//
//    val returAdresse: Fagdelen.ReturAdresse = Fagdelen.ReturAdresse(
//        "NAV Familie- og pensjonsytelser Steinkjer",
//        "Postboks 6600 Etterstad",
//        "0607",
//        "OSLO"
//    )
//
//    object TestMaster : DummyBase() {
//        override val parameters = setOf(
//            RequiredParameter(ReturAdresse)
//        )
//    }
//
//    val title = newText(Language.Bokmal to "test")
//    val template = createTemplate("test", title, TestMaster, languages(Language.Bokmal)) { }
//
//    @Test
//    fun `constructor validates required parameters are present`() {
//        assertThrows<IllegalArgumentException> {
//            Letter(template, emptyMap(), Language.Bokmal)
//        }
//    }
//
//
//    @Test
//    fun `constructor validates that template supports language`() {
//        assertThrows<IllegalArgumentException> {
//            Letter(template, mapOf(ReturAdresse to returAdresse), Language.Nynorsk)
//        }
//    }
//
//    @Test
//    fun `can construct letter with supported language`() {
//        Letter(template, mapOf(ReturAdresse to returAdresse), Language.Bokmal)
//    }
//
//    @Test
//    fun `untypedArg fetching argument that is not part of template throws exception`() {
//        val letter = Letter(template, mapOf(KortNavn to "mitt kort navn", ReturAdresse to returAdresse), Language.Bokmal)
//
//        assertThrows<IllegalArgumentException> {
//            letter.untypedArg(KortNavn)
//        }
//    }
//
//    @Test
//    fun `requiredArg fetching argument that is part of base-template is successful`() {
//        val letter = Letter(template, mapOf(ReturAdresse to returAdresse), Language.Bokmal)
//
//        letter.untypedArg(ReturAdresse)
//    }
//
//    @Test
//    fun `requiredArg fetching argument that is part of template is successful`() {
//        val template = createTemplate("test", title, TestMaster, languages(Language.Bokmal)) {
//            parameters { required { KortNavn } }
//        }
//        val letter = Letter(template, mapOf(KortNavn to "mitt navn", ReturAdresse to returAdresse), Language.Bokmal)
//
//        letter.untypedArg(KortNavn)
//    }
//
//    @Test
//    fun `requiredArg passed arguments can be fetched`() {
//
//        val actual: Fagdelen.ReturAdresse =
//            Letter(template, mapOf(ReturAdresse to returAdresse), Language.Bokmal).requiredArg(ReturAdresse)
//
//        assertEquals(returAdresse, actual)
//    }
//
//    @Test
//    fun `requiredArg passed arguments with incorrect type throws exception`() {
//        val letter = Letter(template, mapOf(ReturAdresse to "returAdresse"), Language.Bokmal)
//
//        assertThrows<IllegalStateException> {
//            letter.requiredArg(ReturAdresse)
//        }
//    }
//
//    @Test
//    fun `requiredArg fetching missing required argument throws exception`() {
//        val args = mutableMapOf<Parameter, Any>(
//            ReturAdresse to returAdresse
//        )
//        val letter = Letter(template, args, Language.Bokmal)
//        args.remove(ReturAdresse)
//
//        assertThrows<IllegalStateException> {
//            letter.requiredArg(ReturAdresse)
//        }
//    }
//
//    @Test
//    fun `optionalArg passed arguments can be fetched`() {
//        val template = createTemplate("test", title, TestMaster, languages(Language.Bokmal)) {
//            parameters { optional { KortNavn } }
//        }
//        val letter = Letter(template, mapOf(ReturAdresse to returAdresse), Language.Bokmal)
//
//        assertEquals(returAdresse, letter.optionalArg(ReturAdresse))
//    }
//
//    @Test
//    fun `optionalArg will not fail for missing optional argument`() {
//        val template = createTemplate("test", title, TestMaster, languages(Language.Bokmal)) {
//            parameters { optional { KortNavn } }
//        }
//        val letter = Letter(template, mapOf(ReturAdresse to returAdresse), Language.Bokmal)
//
//        letter.optionalArg(KortNavn)
//    }
//
//    @Test
//    fun `optionalArg passed arguments with incorrect type throws exception`() {
//        val letter = Letter(template, mapOf(ReturAdresse to "returAdresse"), Language.Bokmal)
//
//        assertThrows<IllegalStateException> {
//            letter.requiredArg(ReturAdresse)
//        }
//    }
//
//}