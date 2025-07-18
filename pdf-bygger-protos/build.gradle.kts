plugins {
    kotlin("jvm")
    id("java-library")
    alias(libs.plugins.protobuf)
}

group = "no.nav.pensjon.brev"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    api(libs.grpc.protobuf)
    api(libs.grpc.kotlin.stub)
    api(libs.kotlinx.coroutines.core)
    api(libs.protobuf.kotlin)
}

protobuf {
    protoc {
        artifact = libs.protoc.asProvider().get().toString()
    }
    plugins {
        create("grpc") {
            artifact = libs.protoc.gen.grpc.java.get().toString()
        }
        create("grpckt") {
            artifact = libs.protoc.gen.grpc.kotlin.get().toString() + ":jdk8@jar"
        }
    }
    generateProtoTasks {
        all().forEach {
            it.plugins {
                create("grpc")
                create("grpckt")
            }
            it.builtins {
                create("kotlin")
            }
        }
    }
}