plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.kotlinSerialization)// Kotlin Serialization plugin for serializing Kotlin objects.
    application
}

group = "org.example.project"
version = "1.0.0"
application {
    mainClass.set("org.example.project.ApplicationKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=${extra["io.ktor.development"] ?: "false"}")
}

dependencies {
    // Core Project Dependencies
    implementation(projects.shared) // Shared module for code and resources reused across the project.

    // Logging
    implementation(libs.logback) // Logback logging framework for robust logging capabilities.

    // Ktor Server Dependencies
    implementation(libs.ktor.server.core) // Core components for building Ktor server applications.
    implementation(libs.ktor.server.netty) // Netty engine integration for handling server requests.
    implementation(libs.ktor.server.content.negotiation) // Content negotiation support for Ktor server.
    implementation(libs.ktor.server.cors) // CORS support to enable/disable resource sharing across domains.
    implementation(libs.ktor.server.status.pages) // Support for custom status pages in Ktor.

    // Ktor Client Dependencies
    implementation(libs.ktor.client.json) // JSON support for Ktor client to parse/serialize JSON data.
    implementation(libs.ktor.client.serialization) // Serialization support for Ktor client.
    implementation(libs.ktor.client.core) // Core components for Ktor client for making HTTP requests.
    implementation(libs.ktor.client.cio) // CIO engine for efficient asynchronous I/O operations.
    implementation(libs.ktor.client.content.negotiation) // Content negotiation support for Ktor client.

    // Serialization
    implementation(libs.ktor.serialization.kotlinx.json) // Kotlinx JSON serialization for Ktor.
    implementation(libs.kotlinx.serialization.json) // Kotlinx JSON serialization library for Kotlin.
    implementation(libs.ktor.serialization.gson) // Gson serialization support for Ktor.
    implementation(libs.jackson.module.kotlin) // Jackson module for serializing/deserializing Kotlin objects.

    // Database
    implementation(libs.postgresql) // PostgreSQL JDBC driver for database connectivity.

    // Date and Time
    implementation(libs.kotlinx.datetime.v060) // Kotlinx DateTime library for date and time manipulation.

    // Email
    implementation(libs.simplekotlinmail.core) // Core library for sending emails.
    implementation(libs.simplekotlinmail.client) // Email client for interacting with email servers.

    // Testing
    testImplementation(libs.ktor.server.tests) // Testing utilities for Ktor server applications.
    testImplementation(libs.kotlin.test.junit) // JUnit's integration for running Kotlin tests.
    implementation(libs.mockito.core) // Core library for creating mocks in tests.
    implementation(libs.mockito.inline) // Inline mocking support for final classes and methods.
    implementation(libs.mockito.kotlin) // Kotlin-friendly API extensions for Mockito.

}