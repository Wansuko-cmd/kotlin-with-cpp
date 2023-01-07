plugins {
    `cpp-library`
}

library {
    targetMachines.add(machines.windows.x86_64)
    val javaProject = project(":java-api")
    publicHeaders.from(javaProject.file("${javaProject.buildDir}/generated/sources/headers/java/main"))
}

tasks.withType(CppCompile::class.java).configureEach {
    val include = "${System.getProperty("java.home")}/include"
    val args = targetPlatform.map { platform ->
        val os = platform.operatingSystem
        when {
            os.isMacOsX -> listOf("-I$include", "-I$include/darwin")
            os.isLinux -> listOf("-I$include", "-I$include/linux")
            os.isWindows -> listOf("/I$include", "/I$include${File.separator}windows")
            else -> listOf("-I$include")
        }
    }
    compilerArgs.addAll(args)
    dependsOn.add(":java-api:classes")
}
