plugins {
  id("java")
  id("org.jetbrains.kotlin.jvm") version "1.9.0"
  id("org.jetbrains.intellij") version "1.15.0"
}

group = "com.moonkite"
version = "1.2"

repositories {
  mavenCentral()
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html

intellij {
  version.set("2022.2.5")
  type.set("CL") // CLion
  plugins.set(listOf(/* Plugin Dependencies */))
}

intellij {
  version.set("2022.2.5")
  type.set("GW") // Gateway
  plugins.set(listOf(/* Plugin Dependencies */))
}

intellij {
  version.set("2022.2.5")
  type.set("GO") // GoLand
  plugins.set(listOf(/* Plugin Dependencies */))
}

intellij {
  version.set("2022.2.5")
  type.set("IC") // IntelliJ IDEA Community Edition
  plugins.set(listOf(/* Plugin Dependencies */))
}

intellij {
  version.set("2022.2.5")
  type.set("IU") // IntelliJ IDEA Ultimate Edition
  plugins.set(listOf(/* Plugin Dependencies */))
}

intellij {
  version.set("2022.2.5")
  type.set("PS") // PhpStorm
  plugins.set(listOf(/* Plugin Dependencies */))
}

intellij {
  version.set("2022.2.5")
  type.set("PY") // PyCharm Professional Edition
  plugins.set(listOf(/* Plugin Dependencies */))
}

intellij {
  version.set("2022.2.5")
  type.set("PC") // PyCharm Community Edition
  plugins.set(listOf(/* Plugin Dependencies */))
}



tasks {
  // Set the JVM compatibility versions
  withType<JavaCompile> {
    sourceCompatibility = "17"
    targetCompatibility = "17"
  }
  withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
  }

  patchPluginXml {
    sinceBuild.set("211")
    untilBuild.set("242.*")
  }

  signPlugin {
    certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
    privateKey.set(System.getenv("PRIVATE_KEY"))
    password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
  }

  publishPlugin {
    token.set(System.getenv("PUBLISH_TOKEN"))
  }


}

dependencies {
  implementation("com.google.code.gson:gson:2.8.9")
}

