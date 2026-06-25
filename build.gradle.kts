import java.net.URI

plugins {
    java
    alias(libs.plugins.mdg)
}

group = "io.github.fusionflux"
version = "1.0.0+mc${libs.versions.minecraft.get()}"

repositories {
    flatDir {
        dir("libs")
    }

    exclusiveContent {
        forRepository {
            maven {
                name = "Modrinth"
                url = URI("https://api.modrinth.com/maven")
            }
        }
        filter {
            includeGroup("maven.modrinth")
        }
    }

    // Aeronautics etc. want version numbering systems other than Modrinth's, thus sourcing from other Maven servers

    // Create, pretty self-explanatory
    maven {
        name = "CreateMaven"
        url = URI("https://maven.createmod.net")
    }

    // Everything Simulated
    maven {
        name = "RyanHCode"
        url = URI("https://maven.ryanhcode.dev/releases")
    }

    // Veil
    maven {
        name = "BlameJared"
        url = URI("https://maven.blamejared.com/")
    }

    // Curios API
    maven {
        name = "TheIllusiveC4"
        url = URI("https://maven.theillusivec4.top/")
    }
    // Also one hell of a user name

    // CC: Tweaked
    maven {
        name = "SquidDev"
        url = URI("https://maven.squiddev.cc")
        content {
            includeGroup("cc.tweaked")
        }
    }

    // Registrate
    maven {
        name = "ithundxr"
        url = URI("https://maven.ithundxr.dev/snapshots")
    }
    // Ironically, the author of this mod, TTERAG, has their own Maven server, yet hasn't hosted it on there
    // And finding the specific snapshot Aero wants definitely didn't take me the better part of an hour
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
    implementation("dev.ryanhcode.sable:sable-neoforge-1.21.1:2.0.3")
    implementation("dev.simulated_team.simulated:simulated-neoforge-1.21.1:1.3.0")
    implementation("dev.eriksonn.aeronautics:aeronautics-neoforge-1.21.1:1.3.0")
    implementation("dev.ryanhcode.offroad:offroad-neoforge-1.21.1:1.3.0")
    implementation("com.tterrag.registrate:Registrate:MC1.21-1.3.0+67")
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
