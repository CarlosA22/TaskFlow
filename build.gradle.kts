

// Configurações do projeto raiz
plugins {
    // Plugin do Android Gradle aplicado mas não configurado aqui
    id("com.android.application") version "8.13.1" apply false
    // Plugin do Kotlin aplicado mas não configurado aqui
    id("org.jetbrains.kotlin.android") version "2.0.20" apply false
    // Plugin KSP para Room (substituto do KAPT)
    id("com.google.devtools.ksp") version "2.0.20-1.0.25" apply false
    alias(libs.plugins.google.gms.google.services) apply false

}

// Tarefa para limpeza do projeto
tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
}