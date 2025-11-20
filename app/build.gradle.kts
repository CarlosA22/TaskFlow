// Plugins aplicados ao módulo app
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0"
    alias(libs.plugins.google.gms.google.services)
}

android {
    // Namespace do aplicativo - usado para geração de R e BuildConfig
    namespace = "com.taskflow.app"
    // SDK de compilação - versão mais atual estável
    compileSdk = 34

    defaultConfig {
        // ID único do aplicativo na Play Store
        applicationId = "com.taskflow.app"
        // SDK mínimo suportado (Android 7.0+)
        minSdk = 24
        // SDK alvo - deve ser igual ao compileSdk
        targetSdk = 34
        // Código da versão (incrementar a cada release)
        versionCode = 1
        // Nome da versão mostrado ao usuário
        versionName = "1.0"

        // Configuração para testes instrumentados
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Configuração para Compose
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        // Configuração para build de release
        release {
            // Ativar obfuscação de código
            isMinifyEnabled = true
            // Ativar shrinking de recursos
            isShrinkResources = true
            // Usar arquivos de configuração do ProGuard
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        // Build de debug já vem configurado por padrão
    }

    // Configurações de compilação do Kotlin
    compileOptions {
        // Usar Java 8+ features
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        // Target JVM para Kotlin
        jvmTarget = "1.8"
    }

    // Ativar Jetpack Compose
    buildFeatures {
        compose = true
    }

    // Configurações específicas do Compose
    composeOptions {
        // Versão do compilador Compose compatível com BOM 2024.09.03
        kotlinCompilerExtensionVersion = "2.0.0"
    }

    // Configuração de empacotamento
    packaging {
        resources {
            // Excluir arquivos de licença duplicados
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // BOM do Compose - gerencia versões de todas as libs Compose
    implementation(platform("androidx.compose:compose-bom:2024.09.03"))

    // Core AndroidX - funcionalidades essenciais
    implementation("androidx.core:core-ktx:1.12.0")

    // Lifecycle - gerenciamento de ciclo de vida com Compose
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.6")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6")
    implementation("com.google.android.material:material:1.12.0")
    // Activity Compose - integração entre Activity e Compose
    implementation("androidx.activity:activity-compose:1.9.2")

    // Jetpack Compose UI - componentes de interface
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")

    // Material Design 3 - componentes visuais modernos
    implementation("androidx.compose.material3:material3")

    // Room - banco de dados local
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    implementation(libs.firebase.database)
    // Anotações do Room processadas pelo KSP
    ksp("androidx.room:room-compiler:2.6.1")

    // Coroutines - programação assíncrona
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // Navegação Compose (opcional)
    implementation("androidx.navigation:navigation-compose:2.7.5")

    // Dependências de teste
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.09.03"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")

    // Ferramentas de debug
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}