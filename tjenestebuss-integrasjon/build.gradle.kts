import com.github.jengelman.gradle.plugins.shadow.ShadowJavaPlugin.SHADOW_JAR_TASK_NAME
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.github.jengelman.gradle.plugins.shadow.transformers.AppendingTransformer
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val javaTarget: String by System.getProperties()
val kotlinVersion: String by System.getProperties()
val ktorVersion: String by System.getProperties()
val hamkrestVersion: String by project
val logbackVersion: String by project
val logstashVersion: String by project
val micrometerVersion: String by project
val jupiterVersion: String by project

plugins {
	application
	kotlin("jvm")
	id("io.ktor.plugin")
}

group = "no.nav.pensjon.brev.tjenestebuss"
version = "0.0.1"

application {
	mainClass.set("no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.TjenestebussIntegrasjonApplicationKt")
}

// Merge cxf/bus-extensions.txt fra alle cxf-avhengigheter
tasks.named(SHADOW_JAR_TASK_NAME, ShadowJar::class.java) {
	transform(AppendingTransformer::class.java) {
		resource = "META-INF/cxf/bus-extensions.txt"
	}
}

ktor {
	fatJar {
		archiveFileName.set("app.jar")
	}
}

val cxfVersion = "3.6.3"
val esbVersion = "2023.11.01-10.31-1bc8315f412e"
val tjenestespesifikasjonerVersion = "1.858e92e"
dependencies {
	implementation("io.ktor:ktor-serialization-jackson:$ktorVersion")
	implementation("io.ktor:ktor-server-call-id:$ktorVersion")
	implementation("io.ktor:ktor-server-call-logging:$ktorVersion")
	implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
	implementation("io.ktor:ktor-server-core-jvm:$ktorVersion")
	implementation("io.ktor:ktor-server-netty-jvm:$ktorVersion")
	implementation("io.ktor:ktor-server-status-pages:$ktorVersion")
	implementation("io.ktor:ktor-client-auth:$ktorVersion")
	implementation("io.ktor:ktor-server-auth-jwt:$ktorVersion")
	implementation("io.ktor:ktor-client-cio:$ktorVersion")
	implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
	implementation("ch.qos.logback:logback-classic:$logbackVersion")
	implementation("net.logstash.logback:logstash-logback-encoder:$logstashVersion")

	implementation("no.nav.pensjon.pesys-esb-wsclient:pcom-esb-wsclient-legacy:$esbVersion")
	implementation("no.nav.pensjon.pesys-esb-wsclient:psak-esb-wsclient-legacy:$esbVersion")
	implementation("no.nav.tjenestespesifikasjoner:samhandler-tjenestespesifikasjon:$tjenestespesifikasjonerVersion")

	implementation("javax.xml.ws:jaxws-api:2.3.1")
	@Suppress("GradlePackageUpdate")
	implementation("com.sun.xml.messaging.saaj:saaj-impl:1.5.1") // needs to be correct version for apache cxf to function

	implementation("org.apache.cxf:cxf-rt-features-logging:$cxfVersion")
	implementation("org.apache.cxf:cxf-rt-frontend-jaxws:$cxfVersion")
	implementation("org.apache.cxf:cxf-rt-ws-policy:$cxfVersion")
	implementation("org.apache.cxf:cxf-rt-transports-http:$cxfVersion")

	// Metrics
	implementation("io.ktor:ktor-server-metrics:$ktorVersion")
	implementation("io.ktor:ktor-server-metrics-micrometer:$ktorVersion")
	implementation("io.micrometer:micrometer-registry-prometheus:$micrometerVersion")


	// Test
	testImplementation("org.junit.jupiter:junit-jupiter")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
	testImplementation(platform("org.junit:junit-bom:$jupiterVersion"))
	testImplementation("io.ktor:ktor-server-test-host:$ktorVersion")
	testImplementation("com.sun.xml.bind:jaxb-core:2.2.11")
	testImplementation("org.apache.cxf:cxf-rt-transports-http-jetty:$cxfVersion")
	testImplementation("com.natpryce:hamkrest:$hamkrestVersion")
}

repositories {
	maven {
		// Create a token at https://github.com/settings/tokens/new with package.read
		// Then create a gradle.properties file in $HOME/.gradle with the following:
		// gpr.user=<your github username>
		// gpr.token=<the token>
		url = uri("https://maven.pkg.github.com/navikt/pesys-esb-wsclient")
		credentials {
			username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
			password = project.findProperty("gpr.token") as String? ?: System.getenv("GITHUB_TOKEN")
		}
		metadataSources {
			artifact() //Look directly for artifact
		}
		content {
			includeGroup("no.nav.pensjon.pesys-esb-wsclient")
		}
	}
	maven {
		url = uri("https://maven.pkg.github.com/navikt/tjenestespesifikasjoner")
		credentials {
			username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
			password = project.findProperty("gpr.token") as String? ?: System.getenv("GITHUB_TOKEN")
		}
	}
}

sourceSets {
	main {
		resources {
			srcDir("secrets")
		}
	}
}

kotlin {
	compilerOptions {
		jvmTarget.set(JvmTarget.fromTarget(javaTarget))
		freeCompilerArgs.add("-Xjsr305=strict")
	}
}

tasks {
	compileJava {
		targetCompatibility = javaTarget
	}
	compileTestJava {
		targetCompatibility = javaTarget
	}
}
