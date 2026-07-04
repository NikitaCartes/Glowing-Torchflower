plugins {
    id("java")
    id("net.neoforged.moddev") version "2.0.141"
    id("com.gradleup.shadow") version "9.4.2"
    id("me.modmuss50.mod-publish-plugin") version "0.8.4"
}

stonecutter {
    val (version, loader) = current.project.split('-', limit = 2)
    properties.tags(version, loader)
}

repositories {
    mavenCentral()
    maven("https://maven.neoforged.net/releases")
}

base.archivesName = "${property("mod_id")}-neoforge-mc${property("display_mc")}"
version = property("mod_version").toString()

java {
    toolchain { languageVersion.set(JavaLanguageVersion.of(25)) }
    withSourcesJar()
}

neoForge {
    version = property("neoforge_version").toString()
    mods {
        create("glowingtorchflower") {
            sourceSet(sourceSets.main.get())
        }
    }
}

val mixinExtras = "io.github.llamalad7:mixinextras-neoforge:${property("mixinextras_version")}"

val shaded: Configuration by configurations.creating
configurations {
    compileOnly { extendsFrom(shaded) }
    runtimeOnly { extendsFrom(shaded) }
}

dependencies {
    compileOnly(mixinExtras)
    annotationProcessor(mixinExtras)
    jarJar(mixinExtras) {
        version { strictly("[${property("mixinextras_version")},)"); prefer(property("mixinextras_version").toString()) }
    }

    shaded("org.spongepowered:configurate-core:${property("hocon_version")}")
    shaded("org.spongepowered:configurate-hocon:${property("hocon_version")}")
    shaded("org.apache.commons:commons-text:${property("commons_text_version")}")
    shaded("com.typesafe:config:${property("typesafe_config_version")}")
    shaded("io.leangen.geantyref:geantyref:${property("geantyref_version")}")
}

tasks.shadowJar {
    archiveClassifier.set("dev-shadow")
    configurations = listOf(shaded)
    mergeServiceFiles()

    relocate("org.spongepowered.configurate", "xyz.nikitacartes.glowingtorchflower.shadow.configurate")
    relocate("com.typesafe.config", "xyz.nikitacartes.glowingtorchflower.shadow.config")
    relocate("io.leangen.geantyref", "xyz.nikitacartes.glowingtorchflower.shadow.geantyref")
    relocate("net.kyori.option", "xyz.nikitacartes.glowingtorchflower.shadow.option")
    relocate("org.apache.commons.text", "xyz.nikitacartes.glowingtorchflower.shadow.commons.text")
    relocate("org.apache.commons.lang3", "xyz.nikitacartes.glowingtorchflower.shadow.commons.lang3")

    from(sourceSets.main.get().output)
}

tasks.jar {
    from("LICENSE")
    dependsOn(tasks.shadowJar)
    // Pull in the shaded (relocated) classes + resources, and drop the un-relocated main output.
    from(zipTree(tasks.shadowJar.get().archiveFile)) {
        exclude("META-INF/MANIFEST.MF", "META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA")
    }
    val mainOutputDirs = sourceSets.main.get().output.files
    exclude { element ->
        mainOutputDirs.any { element.file.toPath().startsWith(it.toPath()) }
    }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

val modExpansions = mapOf(
    "version" to project.version.toString(),
    "supported_minecraft_version" to property("supported_minecraft_version").toString(),
    "neoforge_version" to property("neoforge_version").toString()
)

tasks.processResources {
    inputs.properties(modExpansions)
    filesMatching("META-INF/neoforge.mods.toml") { expand(modExpansions) }
}

tasks.named("createMinecraftArtifacts") {
    dependsOn(tasks.named("stonecutterGenerate"))
}

tasks.register<Copy>("collectJars") {
    group = "build"
    from(tasks.jar.map { it.archiveFile })
    into(rootProject.layout.buildDirectory.dir("libs"))
    dependsOn("build")
}

publishMods {
    val modrinthToken = System.getenv("MODRINTH_TOKEN") ?: ""
    val curseforgeToken = System.getenv("CURSEFORGE_TOKEN") ?: ""
    val githubToken = System.getenv("GITHUB_TOKEN") ?: ""

    file = tasks.jar.get().archiveFile
    dryRun = modrinthToken.isEmpty() || curseforgeToken.isEmpty() || githubToken.isEmpty()
    displayName = "${property("display_name")} ${project.version}"
    version = project.version.toString()
    changelog = rootProject.file("RELEASE_NOTE.md").readText()
    type = STABLE
    modLoaders.add("neoforge")

    val targets = property("supported_versions").toString().split(",")
    modrinth {
        projectId = "1S4LxcvL"
        accessToken = modrinthToken
        targets.forEach(minecraftVersions::add)
    }
    curseforge {
        projectId = "827288"
        accessToken = curseforgeToken
        targets.forEach(minecraftVersions::add)
    }
    github {
        accessToken = githubToken
        parent(rootProject.tasks.named("publishGithub"))
    }
}
