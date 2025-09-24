import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val javaTarget: String by System.getProperties()

plugins {
	application
	kotlin("jvm")
	alias(libs.plugins.ktor) apply true
}

group = "no.nav.pensjon.brev.tjenestebuss"
version = "0.0.1"

application {
	mainClass.set("no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.TjenestebussIntegrasjonApplicationKt")
}

val cxfVersion = "3.6.3"
val tjenestespesifikasjonerVersion = "1.858e92e"
dependencies {
	implementation(libs.ktor.serialization.jackson)
	implementation(libs.ktor.server.callId)
	implementation(libs.ktor.server.callLogging)
	implementation(libs.ktor.server.content.negotiation)
	implementation(libs.ktor.server.core.jvm)
	implementation(libs.ktor.server.netty.jvm)
	implementation(libs.ktor.server.status.pages)
	implementation(libs.ktor.client.auth)
	implementation(libs.ktor.server.auth.jwt)
	implementation(libs.ktor.client.cio)
	implementation(libs.ktor.client.content.negotiation)
	implementation(libs.bundles.logging)

	implementation("no.nav.tjenestespesifikasjoner:samhandler-tjenestespesifikasjon:$tjenestespesifikasjonerVersion")

	implementation("javax.xml.ws:jaxws-api:2.3.1")
	@Suppress("GradlePackageUpdate")
	implementation("com.sun.xml.messaging.saaj:saaj-impl:1.5.1") // needs to be correct version for apache cxf to function

	implementation("org.apache.cxf:cxf-rt-features-logging:$cxfVersion")
	implementation("org.apache.cxf:cxf-rt-frontend-jaxws:$cxfVersion")
	implementation("org.apache.cxf:cxf-rt-ws-policy:$cxfVersion")
	implementation("org.apache.cxf:cxf-rt-transports-http:$cxfVersion")

	implementation(libs.bundles.metrics)

	// Test
	testImplementation(libs.junit.jupiter)
	testImplementation(libs.kotlin.test.junit)
	testImplementation(platform(libs.junit.bom))
	testImplementation(libs.ktor.server.test.host)
	testImplementation("com.sun.xml.bind:jaxb-core:2.2.11")
	testImplementation("org.apache.cxf:cxf-rt-transports-http-jetty:$cxfVersion")
	testImplementation(libs.hamkrest)
}

repositories {
	maven {
		url = uri("https://github-package-registry-mirror.gc.nav.no/cached/maven-release")
		metadataSources {
			artifact() //Look directly for artifact
		}
		content {
			includeGroup("no.nav.pensjon.pesys-esb-wsclient")
		}
	}
	maven {
		url = uri("https://github-package-registry-mirror.gc.nav.no/cached/maven-release")
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
    build {
        dependsOn(installDist)
    }
}
