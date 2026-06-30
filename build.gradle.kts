plugins {
    java
    alias(libs.plugins.mdg)
}

group = "io.github.fusionflux"
version = "1.0.3+mc${libs.versions.minecraft.get()}"

repositories {
    exclusiveContent {
        forRepositories(maven("https://maven.createmod.net")).filter {
            includeGroup("com.simibubi.create")
            includeGroup("dev.engine-room.flywheel")
            includeGroup("net.createmod.ponder")
        }

        forRepositories(maven("https://maven.ithundxr.dev/snapshots")).filter {
            includeModule("com.tterrag.registrate", "Registrate")
        }

//        forRepositories(maven("https://api.modrinth.com/maven")).filter {
//            includeGroup("maven.modrinth")
//        }

        forRepositories(maven("https://maven.ryanhcode.dev/releases")).filter {
            includeGroup("dev.ryanhcode.sable")
            includeGroup("dev.ryanhcode.sable-companion")
            includeGroup("dev.ryanhcode.offroad")
            includeGroup("dev.simulated_team.simulated")
            includeGroup("dev.eriksonn.aeronautics")
        }

        forRepositories(maven("https://maven.blamejared.com")).filter {
            includeGroup("foundry.veil")
            includeModule("gg.moonflower", "molang-compiler")
            includeModule("io.github.ocelot", "glsl-processor")
        }
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
    implementation(libs.create) {
        isTransitive = false
    }

    implementation(libs.ponder)
    implementation(libs.registrate)

    compileOnly(libs.flywheel.api)
    runtimeOnly(libs.flywheel)

    implementation(libs.bundles.simulated.suite) {
        isTransitive = false
    }

    compileOnly(libs.bundles.sable.libs)
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
