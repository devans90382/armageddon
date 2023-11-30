import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.2.0"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.20"
    kotlin("plugin.spring") version "1.9.20"
    id("org.openapi.generator") version "7.0.1"
}

group = "com.devans"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

object Dependency {

    object Feign {
        private const val version = "13.1"
        const val Core = "io.github.openfeign:feign-core:$version"
        const val Jackson = "io.github.openfeign:feign-jackson:$version"
        const val OkHttp = "io.github.openfeign:feign-okhttp:$version"
        const val Slf4j = "io.github.openfeign:feign-slf4j:$version"
        const val Form = "io.github.openfeign.form:feign-form:3.8.0"
        const val Mock = "io.github.openfeign:feign-mock:$version"
        const val Micrometer = "io.github.openfeign:feign-micrometer:$version"
    }

    object Resilience4j {
        private const val version = "2.1.0"
        const val Feign = "io.github.resilience4j:resilience4j-feign:$version"
        const val CircuitBreaker = "io.github.resilience4j:resilience4j-circuitbreaker:$version"
        const val Micrometer = "io.github.resilience4j:resilience4j-micrometer:$version"
        const val RateLimiter = "io.github.resilience4j:resilience4j-ratelimiter:$version"
        const val Springboot2 = "io.github.resilience4j:resilience4j-spring-boot2:$version"
    }

    object AWS {
        private const val version = "1.12.425"
        const val DynamoDB = "com.amazonaws:aws-java-sdk-dynamodb:$version"
    }

    object Logging {
        private const val version = "3.0.5"
        const val KotlinLogger = "io.github.microutils:kotlin-logging-jvm:$version"
    }

    object Monitoring {
        private const val version = "1.12.0"
        const val Core = "io.micrometer:micrometer-core:$version"
        const val Prometheus = "io.micrometer:micrometer-registry-prometheus:$version"
    }

    object Testing {
        private const val version = "5.1.0"
        private const val coreVersion = "5.7.0"
        const val MockitoKotlin = "org.mockito.kotlin:mockito-kotlin:$version"
        const val MockitoCore = "org.mockito:mockito-core:$coreVersion"
    }

}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation(Dependency.AWS.DynamoDB)
    implementation(Dependency.Resilience4j.CircuitBreaker)
    implementation(Dependency.Resilience4j.Feign)
    implementation(Dependency.Resilience4j.Micrometer)
    implementation(Dependency.Logging.KotlinLogger)
    implementation(Dependency.Feign.Core)
    implementation(Dependency.Feign.OkHttp)
    implementation(Dependency.Feign.Jackson)
    implementation(Dependency.Feign.Micrometer)
    implementation(Dependency.Monitoring.Prometheus)

    compileOnly("org.projectlombok:lombok")

    annotationProcessor("org.projectlombok:lombok")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation(Dependency.Testing.MockitoKotlin)
}

val path = "$rootDir/src/main/kotlin/com/devans/profile"

sourceSets {
    main {
        java {
            srcDirs("$buildDir/generated/src/main/kotlin")
        }
    }
}

openApiGenerate {
    generatorName.value("kotlin-spring")
    library.value("spring-boot")
    inputSpec.set("$path/specs/profile.yaml")
    outputDir.set("$buildDir/generated")
    apiPackage.set("com.devans.profile.http.api")
    modelPackage.set("com.devans.profile.http.model")

    configOptions.value(
        mapOf(
            "dateLibrary" to "java8",
            "skipDefaultInterface" to "true",
            "useTags" to "true", // Use tags for the naming
            "serializationLibrary" to "jackson",
            "reactive" to "true",
            "interfaceOnly" to "true",
            "useBeanValidation" to "false",
            "enumPropertyNaming" to "UPPERCASE",
            "annotationLibrary" to "none",
            "documentationProvider" to "none"
        )
    )

    additionalProperties.value(
        mapOf(
            "removeEnumValuePrefix" to "false"
        )
    )
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.named("compileKotlin") {
    dependsOn("openApiGenerate")
}
