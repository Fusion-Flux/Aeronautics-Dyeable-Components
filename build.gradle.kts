import java.util.regex.Pattern

plugins {
    java
    alias(libs.plugins.mdg)
}

group = "io.github.fusionflux"
version = "1.0.3+mc${libs.versions.minecraft.get()}"

repositories {
    flatDir {
        dir("libs")
    }
}

neoForge {
    // Specify the version of NeoForge to use.
    version = libs.versions.neoforge.get()

    parchment {
        mappingsVersion = libs.versions.parchment.get()
        minecraftVersion = libs.versions.minecraft.get()
    }

    mods.register("aeronautics_dyeable_components") {
        sourceSet(sourceSets.main.get())
    }

    runs {
        create("client") {
            client()
        }

        create("server") {
            server()

            gameDirectory = project.file("run/server")
        }

        create("data") {
            data()
            programArguments.addAll(
                            "--mod", "aeronautics_dyeable_components",
                            "--all",
                            "--output", project.file("src/generated/resources/").absolutePath,
                            "--existing", project.file("src/main/resources/").absolutePath,
                            "--existing-mod", "offroad"
                        )
        }

        configureEach {
            jvmArgument("-Dmixin.debug.export=true")
        }
    }
}

sourceSets.main {
    resources.srcDir("src/generated/resources")
}

dependencies {
    file("libs").listFiles().forEach {
        val regex = Pattern.compile("(.+)-([0-9].*).jar")
        val matcher = regex.matcher(it.name)
        if (matcher.find()) {
            val module = matcher.group(1)
            val modVersion = matcher.group(2)
            implementation("ignored:$module:$modVersion")
        }
    }
}

java.toolchain.languageVersion = JavaLanguageVersion.of(21)

tasks.processResources {
    val properties = mapOf(
        "version" to project.version,
        "minecraft_version" to libs.versions.minecraft.get(),
        "neo_version" to libs.versions.neoforge.get(),
    )

    inputs.properties(properties)

    filesMatching("META-INF/neoforge.mods.toml") {
        expand(properties)
    }
}
