plugins {
    id("dev.kikugie.stonecutter")
    id("me.modmuss50.mod-publish-plugin") version "0.8.4"
}

stonecutter active "26.2-fabric"

stonecutter parameters {
    // Loader flags: `//? if fabric { ... }` / `//? if neoforge { ... }`.
    constants.match(current.project.substringAfterLast('-'), "fabric", "neoforge")
}

stonecutter.tasks {
    order("publishMods")
}

// One GitHub release for the whole version matrix: this root task creates it (empty),
// and every node's publishGithub uploads its jar into it via `parent`.
publishMods {
    val githubToken = System.getenv("GITHUB_TOKEN") ?: ""
    val modVersion = findProperty("mod_version")?.toString()
        ?: file("stonecutter.properties.toml").readLines()
            .first { it.trim().startsWith("mod_version") }
            .substringAfter('=').trim().trim('"')

    dryRun = githubToken.isEmpty()
    version = modVersion
    displayName = modVersion
    changelog = rootProject.file("RELEASE_NOTE.md").readText()
    type = STABLE

    github {
        accessToken = githubToken
        repository = "NikitaCartes/Glowing-Torchflower"
        commitish = "fabric-26.1"
        tagName = modVersion
        allowEmptyFiles = true
    }
}
