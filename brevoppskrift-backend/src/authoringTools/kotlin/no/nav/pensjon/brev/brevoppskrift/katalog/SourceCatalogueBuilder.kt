package no.nav.pensjon.brev.brevoppskrift.katalog

import no.nav.pensjon.brev.brevoppskrift.api.TemplateSource
import no.nav.pensjon.brev.brevoppskrift.draftObjectMapper
import no.nav.pensjon.brev.brevoppskrift.model.*
import org.jetbrains.kotlin.cli.create
import org.jetbrains.kotlin.cli.jvm.compiler.EnvironmentConfigFiles
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.com.intellij.openapi.util.Disposer
import org.jetbrains.kotlin.com.intellij.psi.PsiErrorElement
import org.jetbrains.kotlin.com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.kotlin.config.CommonConfigurationKeys
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.psi.*
import java.io.File
import java.security.MessageDigest

/** Build-only PSI. Reads Git objects at one commit, never a potentially dirty source checkout or a JAR. */
fun main(args: Array<String>) {
    require(args.size == 2) { "Usage: SourceCatalogueBuilder <repository> <output-resource>" }
    val snapshot = catalogueFromCommit(File(args[0]))
    val output = File(args[1])
    output.parentFile.mkdirs()
    draftObjectMapper.writerWithDefaultPrettyPrinter().writeValue(output, snapshot)
    draftObjectMapper.writerWithDefaultPrettyPrinter().writeValue(File(output.parentFile, "catalogue-summary.json"), catalogueSummary(snapshot))
    println("AUTHORING_CATALOGUE_SUPPORT ${draftObjectMapper.writeValueAsString(catalogueSummary(snapshot))}")
}

/** The same production entry point is exercised by the build-runtime regression test. */
fun catalogueFromCommit(root: File): CatalogueSnapshot {
    val input = readCommittedSources(root)
    return KotlinSourceImporter().use { it.build(input.commit, input.sources) }
}

data class CommittedSources(val commit: GitCommit, val sources: Map<RepositoryPath, String>)

fun readCommittedSources(
    root: File,
    executable: File = File(requireNotNull(System.getProperty("authoring.gitExecutable")) { "Missing authoring.gitExecutable" }),
): CommittedSources {
    require(executable.isAbsolute && executable.isFile && executable.canExecute()) { "Git executable must be an absolute path to an executable file." }
    fun git(vararg arguments: String): String {
        val process = ProcessBuilder(listOf(executable.absolutePath, "-C", root.absolutePath) + arguments).redirectError(ProcessBuilder.Redirect.INHERIT).start()
        val result = process.inputStream.bufferedReader(Charsets.UTF_8).readText()
        check(process.waitFor() == 0) { "Kunne ikke lese Git-objekter for kildekatalogen." }
        return result
    }
    val commit = GitCommit(git("rev-parse", "--verify", "HEAD^{commit}").trim())
    val paths = git("ls-tree", "-r", "--name-only", "-z", commit.value).split('\u0000')
        .filter { it.endsWith(".kt") && "/src/main/" in it && it.substringBefore('/') in setOf("ufoere", "pensjon", "alder", "planlegge-pensjon-maler", "brevbaker") }
    // One Git process, not one sandbox/proxy invocation per source file.
    require(paths.none { '\n' in it || '\r' in it }) { "Kildestier med linjeskift støttes ikke." }
    val process = ProcessBuilder(executable.absolutePath, "-C", root.absolutePath, "cat-file", "--batch")
        .redirectError(ProcessBuilder.Redirect.INHERIT).start()
    val sources = linkedMapOf<RepositoryPath, String>()
    try {
        process.outputStream.bufferedWriter(Charsets.UTF_8).use { queries ->
            process.inputStream.buffered().use { content ->
                paths.forEach { path ->
                    queries.write("${commit.value}:$path\n")
                    queries.flush()
                    val header = buildString {
                        while (true) {
                            val byte = content.read()
                            check(byte >= 0) { "Git avsluttet før kildefilen kunne leses." }
                            if (byte == '\n'.code) break
                            append(byte.toChar())
                        }
                    }.split(' ')
                    check(header.size == 3 && header[1] == "blob") { "Git-kilden mangler eller er ikke en fil: $path" }
                    val size = requireNotNull(header[2].toIntOrNull()) { "Ugyldig Git-objektstørrelse." }
                    val bytes = content.readNBytes(size)
                    check(bytes.size == size && content.read() == '\n'.code) { "Ufullstendig Git-kildefil: $path" }
                    sources[RepositoryPath(path)] = bytes.toString(Charsets.UTF_8)
                }
            }
        }
        check(process.waitFor() == 0) { "Kunne ikke lese Git-kildeobjekter." }
    } finally {
        if (process.isAlive) process.destroy()
    }
    return CommittedSources(commit, sources)
}

fun catalogueSummary(snapshot: CatalogueSnapshot): Map<String, Any> = mapOf(
    "commit" to snapshot.commit.value,
    "visibleTemplates" to snapshot.entries.size,
    "editableTemplates" to snapshot.entries.count { it.summary.editable },
    "modules" to TargetModule.entries.associate { module ->
        val entries = snapshot.entries.filter { it.summary.targetModule == module.name }
        module.name to mapOf(
            "visible" to entries.size,
            "editable" to entries.count { it.summary.editable },
            "disabled" to entries.count { !it.summary.editable },
            "languages" to DraftLanguage.entries.associate { language -> language.name to entries.count { language.name in it.summary.languages } },
            "diagnostics" to entries.flatMap { it.summary.diagnostics }.groupingBy { it.code }.eachCount().toSortedMap(),
        )
    },
)

@OptIn(org.jetbrains.kotlin.CoreEnvironmentDeprecation::class)
class KotlinSourceImporter : AutoCloseable {
    private val disposable = Disposer.newDisposable()
    private val environment = KotlinCoreEnvironment.createForProduction(
        disposable,
        CompilerConfiguration.create().apply { put(CommonConfigurationKeys.MODULE_NAME, "authoring-source-import") },
        EnvironmentConfigFiles.JVM_CONFIG_FILES,
    )
    private val factory = KtPsiFactory(environment.project, false)
    private var indexedFiles: Map<RepositoryPath, KtFile>? = null
    private var dependencySymbols: Map<String, List<RepositoryPath>> = emptyMap()
    private var dependencyPackages: Map<String, List<RepositoryPath>> = emptyMap()
    private val dependencyLinks = mutableMapOf<Pair<RepositoryPath, RepositoryPath>, Set<RepositoryPath>>()
    private val fileHashes = mutableMapOf<RepositoryPath, Sha256Digest>()

    override fun close() = Disposer.dispose(disposable)

    fun build(commit: GitCommit, sources: Map<RepositoryPath, String>): CatalogueSnapshot {
        require(Regex("[a-f0-9]{40,64}").matches(commit.value))
        fileHashes.clear()
        val files = sources.mapValues { (path, text) -> factory.createFile(path.value.substringAfterLast('/'), text) }
        val declarations = files.flatMap { (path, file) ->
            PsiTreeUtil.collectElementsOfType(file, KtClassOrObject::class.java).mapNotNull { declaration ->
                declaration.fqName?.asString()?.let { it to (path to declaration) }
            }
        }.toMap()
        val entries = mutableListOf<CatalogueEntry>()
        val registries = mapOf(
            TargetModule.UFOERE to "UfoereTemplates",
            TargetModule.PENSJON to "ProductionTemplates",
            TargetModule.ALDER to "AlderTemplates",
            TargetModule.PLANLEGGE_PENSJON to "PlanleggePensjonTemplates",
        )
        registries.forEach { (module, registryName) ->
            val registry = declarations.values.singleOrNull { it.second.name == registryName } ?: error("Register mangler: $registryName")
            val registryFile = files.getValue(registry.first)
            visibleRegistrations(registry.second).forEach { expression ->
                val reference = expression.text
                val resolved = resolve(reference, registryFile, declarations.keys)
                val declaration = resolved?.let { declarations[it] }
                if (declaration == null) {
                    val issue = ValidationIssue(registry.first.value, "UNRESOLVED_TEMPLATE", "Synlig mal $reference kunne ikke kobles entydig til Kotlin-kilde.")
                    val id = SourceId("${module.name}:$reference")
                    entries.add(CatalogueEntry(TemplateSource(id, reference, "UNKNOWN", module.name, emptyList(), commit, false, listOf(issue)),
                        SourceReference(id, commit, mapOf(registry.first to sha256(sources.getValue(registry.first)))), null, registry.first, emptyMap()))
                } else entries.add(importTemplate(module, commit, requireNotNull(resolved), declaration.first, declaration.second, registry.first, sources, files, declarations))
            }
        }
        check(entries.map { it.summary.id }.distinct().size == entries.size) { "Duplikate maler i registeret." }
        return CatalogueSnapshot(commit, entries.sortedBy { it.summary.id.value })
    }

    /** Only the two AllTemplates entry points establish visibility; private sets alone never do. */
    private fun visibleRegistrations(registry: KtClassOrObject): List<KtExpression> {
        val properties = registry.declarations.filterIsInstance<KtProperty>().associateBy { it.name }
        fun walk(expression: KtExpression, visiting: Set<String>, entryPoint: String): List<KtExpression> {
            fun unsupported(): Nothing = error(
                "Ukjent registeruttrykk: ${registry.name}.$entryPoint (${expression.javaClass.simpleName}: ${expression.text.take(120)})",
            )
            return when (expression) {
                is KtNameReferenceExpression -> {
                    val name = expression.getReferencedName()
                    check(name !in visiting) { "Syklisk registerreferanse: ${registry.name}.$name" }
                    val initializer = properties[name]?.initializer ?: unsupported()
                    walk(initializer, visiting + name, entryPoint)
                }
                is KtParenthesizedExpression -> walk(expression.expression ?: unsupported(), visiting, entryPoint)
                is KtReturnExpression -> walk(expression.returnedExpression ?: unsupported(), visiting, entryPoint)
                is KtBlockExpression -> walk(expression.statements.singleOrNull() ?: unsupported(), visiting, entryPoint)
                is KtDotQualifiedExpression -> if (expression.receiverExpression is KtThisExpression) {
                    walk(expression.selectorExpression ?: unsupported(), visiting, entryPoint)
                } else unsupported()
                is KtCallExpression -> {
                    if (expression.calleeExpression?.text != "setOf" || expression.lambdaArguments.isNotEmpty()) unsupported()
                    expression.valueArguments.map { argument ->
                        val template = argument.getArgumentExpression() ?: unsupported()
                        check(template is KtNameReferenceExpression || template is KtDotQualifiedExpression) {
                            "Registeret må liste malidentiteter eksplisitt: ${registry.name}.$entryPoint"
                        }
                        template
                    }
                }
                else -> unsupported()
            }
        }
        return listOf("hentAutobrevmaler", "hentRedigerbareMaler").flatMap { name ->
            val function = registry.declarations.filterIsInstance<KtNamedFunction>().singleOrNull { it.name == name }
                ?: error("Registerfunksjon mangler: ${registry.name}.$name")
            walk(requireNotNull(function.bodyExpression) { "Registerfunksjon mangler kropp: ${registry.name}.$name" }, emptySet(), name)
        }
    }

    private fun importTemplate(
        module: TargetModule,
        commit: GitCommit,
        qualifiedName: String,
        path: RepositoryPath,
        declaration: KtClassOrObject,
        registryPath: RepositoryPath,
        sources: Map<RepositoryPath, String>,
        files: Map<RepositoryPath, KtFile>,
        declarations: Map<String, Pair<RepositoryPath, KtClassOrObject>>,
    ): CatalogueEntry {
        val diagnostics = mutableListOf<ValidationIssue>()
        fun unsupported(expression: KtElement, message: String) {
            val line = sources.getValue(path).take(expression.textRange.startOffset).count { it == '\n' } + 1
            diagnostics.add(ValidationIssue("${path.value}:$line", "IMPORT_UNSUPPORTED", message))
        }
        val file = files.getValue(path)
        val id = SourceId("${module.name}:$qualifiedName")
        val name = requireNotNull(declaration.name)
        val templateSuper = declaration.superTypeListEntries.singleOrNull { it.typeReference?.text?.substringBefore('<') in setOf("AutobrevTemplate", "RedigerbarTemplate") }
        val type = if (templateSuper?.typeReference?.text?.startsWith("RedigerbarTemplate") == true) DocumentType.REDIGERBAR else DocumentType.AUTOBREV
        if (templateSuper == null) unsupported(declaration, "Malens supertype støttes ikke.")
        if (PsiTreeUtil.findChildOfType(file, PsiErrorElement::class.java) != null) unsupported(declaration, "Kotlin-filen har syntaksfeil.")
        val properties = declaration.declarations.filterIsInstance<KtProperty>().associateBy { it.name }
        val template = properties["template"]?.initializer as? KtCallExpression
        if (template?.calleeExpression?.text != "createTemplate") unsupported(declaration, "Mal må bruke createTemplate direkte.")
        val languageCall = template?.valueArguments?.find { it.getArgumentName()?.asName?.asString() == "languages" }?.getArgumentExpression() as? KtCallExpression
        val languageNames = languageCall?.valueArguments?.map { it.getArgumentExpression()?.text?.substringAfterLast('.') }
        val languages = languageNames.orEmpty().mapNotNull { language ->
            when (language) { "Bokmal" -> DraftLanguage.NB; "Nynorsk" -> DraftLanguage.NN; "English" -> DraftLanguage.EN; else -> null }
        }.toSet()
        if (languages.isEmpty() || languages.size != languageNames?.size) unsupported(declaration, "Språkkombinasjonen kan ikke importeres entydig.")
        val body = template?.lambdaArguments?.singleOrNull()?.getLambdaExpression()?.bodyExpression
        val slots = mutableListOf<SourceTextSlot>()
        val blocks = mutableListOf<Block>()
        var title: Translations = emptyMap()
        fun translations(call: KtCallExpression, blockId: BlockId?, inlineIndex: Int): Triple<Translations, FontType, List<SourceTextSlot>>? {
            val values = mutableMapOf<DraftLanguage, String>()
            val textSlots = mutableListOf<SourceTextSlot>()
            var font = FontType.PLAIN
            for (argument in call.valueArguments) {
                val expression = argument.getArgumentExpression()
                if (argument.getArgumentName()?.asName?.asString() == "fontType") {
                    font = FontType.entries.find { it.name == expression?.text?.substringAfterLast('.') } ?: return null
                    continue
                }
                val langCall = expression as? KtCallExpression ?: return null
                val language = when (langCall.calleeExpression?.text) { "bokmal" -> DraftLanguage.NB; "nynorsk" -> DraftLanguage.NN; "english" -> DraftLanguage.EN; else -> return null }
                val statements = langCall.lambdaArguments.singleOrNull()?.getLambdaExpression()?.bodyExpression?.statements ?: return null
                val parts = statements.singleOrNull()?.let { literalParts(it) } ?: return null
                val literals = parts.map {
                    SourceTextSlot(it.textRange.startOffset, it.textRange.endOffset, blockId, inlineIndex, language, requireNotNull(literalExpression(it)))
                }
                val string = literals.joinToString("") { it.originalText }
                if (values.put(language, string) != null) return null
                textSlots.addAll(literals)
            }
            if (values.keys != languages) return null
            return Triple(values, font, textSlots)
        }
        fun simpleContent(call: KtCallExpression, blockId: BlockId?): List<Inline>? {
            // PSI valueArguments also includes a trailing lambda; only parenthesized parameters
            // represent uniqueness/attributes here. The lambda itself is the supported DSL body.
            if (call.valueArgumentList?.arguments.orEmpty().isNotEmpty()) return null
            val content = call.lambdaArguments.singleOrNull()?.getLambdaExpression()?.bodyExpression?.statements ?: return null
            val inlines = mutableListOf<Inline>()
            val pendingSlots = mutableListOf<SourceTextSlot>()
            for (statement in content) {
                val nested = statement as? KtCallExpression ?: return null
                when (nested.calleeExpression?.text) {
                    "text" -> {
                        val translated = translations(nested, blockId, inlines.size) ?: return null
                        if (blockId == null && (content.size != 1 || translated.second != FontType.PLAIN)) return null
                        pendingSlots.addAll(translated.third)
                        inlines.add(Inline.Text(translated.first, translated.second))
                    }
                    "newline" -> if (blockId != null && nested.valueArguments.isEmpty() && nested.lambdaArguments.isEmpty()) inlines.add(Inline.NewLine) else return null
                    else -> return null
                }
            }
            if (inlines.isEmpty()) return null
            slots.addAll(pendingSlots)
            return inlines
        }
        body?.statements?.forEach { statement ->
            val call = statement as? KtCallExpression
            when (call?.calleeExpression?.text) {
                "title" -> {
                    val content = simpleContent(requireNotNull(call), null)
                    if (title.isNotEmpty() || content == null) unsupported(statement, "Tittel må ha én ren text-node med bokstavelig tekst på alle språk.")
                    else title = (content.single() as Inline.Text).text
                }
                "outline" -> {
                    val outlineRoot = requireNotNull(call)
                    val outline = outlineRoot.lambdaArguments.singleOrNull()?.getLambdaExpression()?.bodyExpression
                    if (outline == null || outlineRoot.valueArgumentList?.arguments.orEmpty().isNotEmpty()) unsupported(statement, "Outline-syntaks støttes ikke.")
                    outline?.statements?.forEachIndexed { index, item ->
                        val outlineCall = item as? KtCallExpression
                        val blockId = BlockId("source-block-$index")
                        when (val callee = outlineCall?.calleeExpression?.text) {
                            "paragraph", "title1", "title2", "title3" -> {
                                val content = simpleContent(requireNotNull(outlineCall), blockId)
                                if (content == null) {
                                    unsupported(item, "$callee inneholder uttrykk, kontrollflyt eller attributter uten verifisert redigeringsprofil; hele malen er skrivebeskyttet.")
                                    blocks.add(Block.LockedSource(blockId, "${id.value}:$index"))
                                } else blocks.add(if (callee == "paragraph") Block.Paragraph(blockId, content) else Block.Heading(blockId, callee.last().digitToInt(), content))
                            }
                            "includePhrase" -> {
                                // Original call remains in the immutable file; arguments cannot be edited or executed.
                                val phraseCall = requireNotNull(outlineCall)
                                if (phraseCall.valueArguments.size != 1 || phraseCall.lambdaArguments.isNotEmpty()) unsupported(item, "Frasekall har ukjent syntaks.")
                                blocks.add(Block.LockedSource(blockId, "${id.value}:phrase:$index"))
                            }
                            else -> {
                                unsupported(item, "Outline-konstruksjonen ${callee ?: item.javaClass.simpleName} støttes ikke. Originalen beholdes; redigering deaktiveres.")
                                blocks.add(Block.LockedSource(blockId, "${id.value}:unsupported:$index"))
                            }
                        }
                    }
                }
                else -> unsupported(statement, "Rotkonstruksjonen ${call?.calleeExpression?.text ?: statement.javaClass.simpleName} støttes ikke, inkludert vedlegg og valg.")
            }
        }
        if (title.isEmpty() || blocks.isEmpty()) unsupported(declaration, "Malens tittel eller innhold kunne ikke importeres.")
        val dtoType = templateSuper?.typeReference?.text?.substringAfter('<', "")?.substringBeforeLast('>')
        val dtoSymbol = dtoType?.let { resolve(it, file, declarations.keys) }
        val dtoDeclaration = dtoSymbol?.let { declarations[it] }
        val dtoFiles = mutableMapOf<RepositoryPath, String>()
        fun dataFields(clazz: KtClassOrObject, dataPath: RepositoryPath, prefix: String, visiting: Set<String> = emptySet()): List<DataField> {
            val symbol = clazz.fqName?.asString().orEmpty()
            if (symbol in visiting || visiting.size > 20) {
                diagnostics.add(ValidationIssue(dataPath.value, "UNSUPPORTED_TYPE", "Rekursiv eller for dyp DTO-type: $symbol"))
                return emptyList()
            }
            dtoFiles[dataPath] = sources.getValue(dataPath)
            val result = mutableListOf<DataField>()
            val parameters = (clazz as? KtClass)?.primaryConstructorParameters.orEmpty()
            parameters.forEach { parameter ->
                val fieldName = parameter.name ?: return@forEach
                val typeText = parameter.typeReference?.text.orEmpty()
                // These two exact empty contracts have no authorable fields; their identity stays in original Kotlin.
                if (fieldName == "pesysData" && typeText == "EmptyFagsystemdata") return@forEach
                if (fieldName == "saksbehandlerValg" && typeText == "SaksbehandlervalgIDSL" && clazz.name == "EmptyRedigerbarBrevdata") return@forEach
                val nullable = typeText.endsWith('?')
                val base = typeText.removeSuffix("?")
                val kind = when (base) {
                    "String" -> FieldKind.STRING; "Int" -> FieldKind.NUMBER; "Double" -> FieldKind.DOUBLE
                    "Boolean" -> FieldKind.BOOLEAN; "LocalDate" -> FieldKind.DATE; else -> null
                }
                if (kind != null) {
                    result.add(DataField(FieldId("$prefix-$fieldName"), fieldName, kind, nullable))
                } else {
                    val resolved = resolve(base, files.getValue(dataPath), declarations.keys)
                    val nested = resolved?.let { declarations[it] }
                    if (nested?.second is KtClass && (nested.second as KtClass).isEnum()) {
                        val values = nested.second.declarations.filterIsInstance<KtEnumEntry>().mapNotNull { it.name }
                        dtoFiles[nested.first] = sources.getValue(nested.first)
                        result.add(DataField(FieldId("$prefix-$fieldName"), fieldName, FieldKind.ENUM, nullable, enumValues = values, typeReference = resolved))
                    } else if (nested != null && !(nested.second as? KtClass)?.isInterface().orFalse()) {
                        val children = dataFields(nested.second, nested.first, "$prefix-$fieldName", visiting + symbol)
                        if (children.isNotEmpty()) result.add(DataField(FieldId("$prefix-$fieldName"), fieldName, FieldKind.OBJECT, nullable, children, typeReference = resolved))
                        else diagnostics.add(ValidationIssue(dataPath.value, "UNSUPPORTED_TYPE", "Tom eller ukjent objekttype for $fieldName: $typeText"))
                    } else diagnostics.add(ValidationIssue(dataPath.value, "UNSUPPORTED_TYPE", "Ingen verifisert typeprofil for $fieldName: $typeText. Ingen felt droppes i en redigerbar import."))
                }
            }
            if (clazz.declarations.filterIsInstance<KtProperty>().any { it.hasInitializer() || it.hasDelegate() || it.getter?.hasBody() == true }) {
                diagnostics.add(ValidationIssue(dataPath.value, "UNSUPPORTED_TYPE", "DTO har beregnede eller delegerte egenskaper uten redigeringsprofil."))
            }
            return result
        }
        val fields = if (dtoDeclaration != null) dataFields(dtoDeclaration.second, dtoDeclaration.first, "source-field") else {
            diagnostics.add(ValidationIssue(path.value, "UNSUPPORTED_TYPE", "DTO-kilden ${dtoType.orEmpty()} kunne ikke kobles til samme commit."))
            emptyList()
        }
        fun propertyValue(key: String): String? = properties[key]?.initializer?.text?.substringAfterLast('.')
        val letterMetadata = template?.valueArguments?.find { it.getArgumentName()?.asName?.asString() == "letterMetadata" }?.getArgumentExpression() as? KtCallExpression
        fun metadataValue(key: String): KtExpression? = letterMetadata?.valueArguments?.find { it.getArgumentName()?.asName?.asString() == key }?.getArgumentExpression()
        val metadata = DraftMetadata(name, type, module, languages,
            displayTitle = (metadataValue("displayTitle") as? KtStringTemplateExpression)?.let { literalExpression(it) },
            brevkode = propertyValue("kode"),
            kategori = if (type == DocumentType.REDIGERBAR) propertyValue("kategori") else null,
            brevkontekst = if (type == DocumentType.REDIGERBAR) propertyValue("brevkontekst") else null,
            sakstyper = (properties["sakstyper"]?.initializer as? KtCallExpression)?.valueArguments?.mapNotNull { it.getArgumentExpression()?.text?.substringAfterLast('.') }?.toSet().orEmpty(),
            distribusjonstype = metadataValue("distribusjonstype")?.text?.substringAfterLast('.'),
            brevtype = metadataValue("brevtype")?.text?.substringAfterLast('.'),
            featureToggle = properties["featureToggle"]?.initializer?.text,
        )
        if (metadata.brevkode == null) unsupported(declaration, "Brevkode mangler.")
        // Resolve source imports transitively; include whole lexical packages conservatively for
        // unqualified references and star imports. Generated selectors are anchored by their DTO.
        // Do not traverse the registry's imports: that would pull in every unrelated visible template.
        val dependencyPaths = dependencies(setOf(path) + dtoFiles.keys, registryPath, files)
        val hashes = (dependencyPaths + registryPath).sortedBy { it.value }.associateWith { fileHashes.getOrPut(it) { sha256(sources.getValue(it)) } }
        val reference = SourceReference(id, commit, hashes)
        val draft = Draft(DRAFT_FORMAT_VERSION, metadata, title, fields = fields, outline = blocks, source = reference)
        if (diagnostics.isEmpty()) diagnostics.addAll(DraftValidator.validate(draft))
        val editable = diagnostics.isEmpty()
        return CatalogueEntry(
            TemplateSource(id, name, type.name, module.name, languages.sortedBy { it.ordinal }.map { it.name }, commit, editable, diagnostics),
            reference,
            if (editable) draft else null,
            path,
            mapOf(path to sources.getValue(path), registryPath to sources.getValue(registryPath)) + dtoFiles,
            if (editable) slots else emptyList(),
            if (editable) setOf("EDIT_LITERAL_TEXT", "PRESERVE_DTO_IDENTITY", "LOCKED_PHRASE_CALLS", "PRESERVE_ALL_SOURCE_LANGUAGES") else emptySet(),
            dtoSymbol,
            file.packageFqName.asString(),
            dtoPaths = dtoFiles.keys - registryPath,
        )
    }

    private fun resolve(reference: String, file: KtFile, names: Set<String>): String? {
        if (reference in names) return reference
        val candidates = mutableSetOf<String>()
        val first = reference.substringBefore('.')
        file.importDirectives.forEach {
            val import = it.importedFqName?.asString() ?: return@forEach
            if (it.isAllUnder) candidates.add("$import.$reference")
            else if ((it.aliasName ?: import.substringAfterLast('.')) == first) candidates.add(import + reference.removePrefix(first))
        }
        candidates.add("${file.packageFqName.asString()}.$reference")
        // Nested DTO classes use their lexical containing class, not global short-name guesses.
        names.filter { it.startsWith(file.packageFqName.asString() + ".") && it.endsWith(".$reference") }
            .filter { candidate -> PsiTreeUtil.collectElementsOfType(file, KtClassOrObject::class.java).any { it.fqName?.asString() == candidate } }
            .forEach { candidates.add(it) }
        return candidates.filter { it in names }.singleOrNull()
    }

    private fun dependencies(initial: Set<RepositoryPath>, registryPath: RepositoryPath, files: Map<RepositoryPath, KtFile>): Set<RepositoryPath> {
        if (indexedFiles !== files) {
            dependencySymbols = files.flatMap { (path, file) ->
                PsiTreeUtil.collectElementsOfType(file, KtNamedDeclaration::class.java).mapNotNull {
                    it.fqName?.asString()?.let { name -> name to path }
                }
            }.groupBy({ it.first }, { it.second })
            dependencyPackages = files.entries.groupBy({ it.value.packageFqName.asString() }, { it.key })
            dependencyLinks.clear()
            indexedFiles = files
        }
        val pending = ArrayDeque(initial)
        val visited = linkedSetOf<RepositoryPath>()
        while (pending.isNotEmpty()) {
            val path = pending.removeFirst()
            if (!visited.add(path) || path == registryPath) continue
            val linked = dependencyLinks.getOrPut(path to registryPath) {
                val file = files.getValue(path)
                val links = dependencyPackages[file.packageFqName.asString()].orEmpty().filter { it != registryPath }.toMutableSet()
                file.importDirectives.forEach { directive ->
                    val imported = directive.importedFqName?.asString() ?: return@forEach
                    if (directive.isAllUnder) {
                        links.addAll(dependencyPackages[imported].orEmpty())
                    } else {
                        var candidate = imported
                        while (candidate.isNotEmpty()) {
                            val matches = dependencySymbols[candidate]
                            if (matches != null) {
                                links.addAll(matches)
                                break
                            }
                            candidate = candidate.substringBeforeLast('.', "")
                        }
                    }
                }
                links
            }
            pending.addAll(linked.filter { it !in visited })
        }
        return visited
    }

    private fun literalExpression(expression: KtExpression): String? = literalParts(expression)?.joinToString("") { literal ->
        literal.entries.joinToString("") { if (it is KtEscapeStringTemplateEntry) it.unescapedValue else it.text }
    }

    private fun literalParts(expression: KtExpression): List<KtStringTemplateExpression>? = when (expression) {
        is KtPrefixExpression -> if (expression.operationReference.text == "+") expression.baseExpression?.let { literalParts(it) } else null
        is KtBinaryExpression -> if (expression.operationReference.text == "+") {
            val left = expression.left?.let { literalParts(it) }
            val right = expression.right?.let { literalParts(it) }
            if (left != null && right != null) left + right else null
        } else null
        is KtStringTemplateExpression -> {
            if (expression.entries.any { it !is KtLiteralStringTemplateEntry && it !is KtEscapeStringTemplateEntry }) null
            else listOf(expression)
        }
        else -> null
    }
}

private fun Boolean?.orFalse(): Boolean = this == true
private fun sha256(value: String): Sha256Digest = Sha256Digest(MessageDigest.getInstance("SHA-256").digest(value.toByteArray(Charsets.UTF_8)).joinToString("") { "%02x".format(it) })
