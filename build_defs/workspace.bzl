"""Macro to use for loading the J2CL repository"""

load("@bazel_skylib//lib:versions.bzl", "versions")
load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive", "http_jar")
load("@bazel_tools//tools/build_defs/repo:jvm.bzl", "jvm_maven_import_external")
load("@io_bazel_rules_closure//closure:repositories.bzl", "rules_closure_dependencies")
load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kotlin_repositories", "kt_register_toolchains")

_MAVEN_CENTRAL_URLS = ["https://repo1.maven.org/maven2/"]

def setup_j2cl_workspace(**kwargs):
    """Load all dependencies needed for J2CL."""

    versions.check("7.4.0")  # The version J2CL currently have a CI setup for.

    rules_closure_dependencies(
        omit_com_google_auto_common = True,
        **kwargs
    )

    jvm_maven_import_external(
        name = "com_google_auto_common",
        artifact = "com.google.auto:auto-common:1.1.2",
        artifact_sha256 = "bfe85e517250fc208afd2b031a2ba80f26529c92536484841b4a60661ca1e3f5",
        server_urls = _MAVEN_CENTRAL_URLS,
        licenses = ["notice"],
    )

    jvm_maven_import_external(
        name = "com_google_auto_service_annotations",
        artifact = "com.google.auto.service:auto-service-annotations:1.0-rc7",
        artifact_sha256 = "986dc826fa0a43bf9f04194c1a8667774f4f44190ec816b08554b47891ba5459",
        server_urls = _MAVEN_CENTRAL_URLS,
        licenses = ["notice"],
    )

    jvm_maven_import_external(
        name = "com_google_auto_service",
        artifact = "com.google.auto.service:auto-service:1.0-rc7",
        artifact_sha256 = "24f13c98baf5fb87e7502fd87130457941cda405ec6ff28d8ff964a8f58a82ed",
        server_urls = _MAVEN_CENTRAL_URLS,
        licenses = ["notice"],
    )

    # We cannot replace com_google_jsinterop_annotations so choose a different name
    http_archive(
        name = "com_google_jsinterop_annotations-j2cl",
        urls = ["https://github.com/google/jsinterop-annotations/archive/d2b3aee14b617a81570b788f981926a829ac892c.zip"],
        strip_prefix = "jsinterop-annotations-d2b3aee14b617a81570b788f981926a829ac892c",
        sha256 = "4164229681bcaf3d130b6c4f463bc345af69de5ad548120e9818f31c70142717",
    )

    http_archive(
      name = "bazel_common_javadoc",
      strip_prefix = "bazel-common-ebce2af3f0de560b649dcf98ef732a56b80e829c/tools/javadoc",
      urls = ["https://github.com/google/bazel-common/archive/ebce2af3f0de560b649dcf98ef732a56b80e829c.zip"],
      sha256 = "3f090bfb3c0c66e3c2d9ae229d184af1147e4c06223551aeb2ff292661371b9a",
    )

    jvm_maven_import_external(
        name = "com_google_j2objc_annotations",
        artifact = "com.google.j2objc:j2objc-annotations:1.3",
        artifact_sha256 = "21af30c92267bd6122c0e0b4d20cccb6641a37eaf956c6540ec471d584e64a7b",
        server_urls = _MAVEN_CENTRAL_URLS,
        licenses = ["notice"],
    )

    jvm_maven_import_external(
        name = "org_jspecify",
        artifact = "org.jspecify:jspecify:0.3.0",
        artifact_sha256 = "e1c7e1832b6095fcfcbe57485700c7330d53d4e57e2c5bbf9c71819b02e978be",
        server_urls = _MAVEN_CENTRAL_URLS,
        licenses = ["notice"],
    )

    jvm_maven_import_external(
        name = "org_apache_commons_collections",
        artifact = "commons-collections:commons-collections:3.2.2",
        artifact_sha256 = "eeeae917917144a68a741d4c0dff66aa5c5c5fd85593ff217bced3fc8ca783b8",
        server_urls = _MAVEN_CENTRAL_URLS,
        licenses = ["notice"],
    )

    jvm_maven_import_external(
        name = "org_apache_commons_lang2",
        artifact = "commons-lang:commons-lang:2.6",
        artifact_sha256 = "50f11b09f877c294d56f24463f47d28f929cf5044f648661c0f0cfbae9a2f49c",
        server_urls = _MAVEN_CENTRAL_URLS,
        licenses = ["notice"],
    )

    jvm_maven_import_external(
        name = "com_google_escapevelocity",
        artifact = "com.google.escapevelocity:escapevelocity:1.1",
        artifact_sha256 = "37e76e4466836dedb864fb82355cd01c3bd21325ab642d89a0f759291b171231",
        server_urls = _MAVEN_CENTRAL_URLS,
        licenses = ["notice"],
    )

    jvm_maven_import_external(
        name = "org_junit",
        artifact = "junit:junit:4.13.2",
        artifact_sha256 = "8e495b634469d64fb8acfa3495a065cbacc8a0fff55ce1e31007be4c16dc57d3",
        server_urls = _MAVEN_CENTRAL_URLS,
        licenses = ["notice"],
    )

    jvm_maven_import_external(
        name = "com_google_testing_compile",
        artifact = "com.google.testing.compile:compile-testing:0.15",
        artifact_sha256 = "f741c21d44ddf4580e99cfc537e76d1760d864637aec1e21d5341f672a165d4c",
        server_urls = _MAVEN_CENTRAL_URLS,
        licenses = ["notice"],
    )

    jvm_maven_import_external(
        name = "org_mockito",
        artifact = "org.mockito:mockito-all:1.9.5",
        artifact_sha256 = "b2a63307d1dce3aa1623fdaacb2327a4cd7795b0066f31bf542b1e8f2683239e",
        server_urls = _MAVEN_CENTRAL_URLS,
        licenses = ["notice"],
    )

    jvm_maven_import_external(
        name = "com_google_truth",
        artifact = "com.google.truth:truth:1.0",
        artifact_sha256 = "edaa12f3b581fcf1c07311e94af8766919c4f3d904b00d3503147b99bf5b4004",
        server_urls = _MAVEN_CENTRAL_URLS,
        licenses = ["notice"],
    )

    # TODO(b/135461024): for now J2CL uses a prepackaged version of javac. But in the future it
    # might be better to tie in to the Java platform in bazel and control the version there.
    jvm_maven_import_external(
        name = "com_sun_tools_javac",
        artifact = "com.google.errorprone:javac:9+181-r4173-1",
        artifact_sha256 = "1d8d347a0e1579f3fc86ac04d1974e489afc66357f0009ac9804a7ac30912ed6",
        server_urls = _MAVEN_CENTRAL_URLS,
        licenses = ["notice"],
    )

    # Eclipse JARs listed at
    # http://download.eclipse.org/eclipse/updates/4.16/R-4.16-202006040540/plugins/

    http_jar(
        name = "org_eclipse_jdt_content_type",
        url = "http://download.eclipse.org/eclipse/updates/4.16/R-4.16-202006040540/plugins/org.eclipse.core.contenttype_3.7.700.v20200517-1644.jar",
        sha256 = "af418cced47512a7cad606ea9a1114267bc224387abcedd639bae8d3a7fb10b9",
    )

    http_jar(
        name = "org_eclipse_jdt_jobs",
        url = "http://download.eclipse.org/eclipse/updates/4.16/R-4.16-202006040540/plugins/org.eclipse.core.jobs_3.10.800.v20200421-0950.jar",
        sha256 = "4d0042425dcc3655c08654351c08b1645ccb309ab5de45743455bfce4849e917",
    )

    http_jar(
        name = "org_eclipse_jdt_resources",
        url = "http://download.eclipse.org/eclipse/updates/4.16/R-4.16-202006040540/plugins/org.eclipse.core.resources_3.13.700.v20200209-1624.jar",
        sha256 = "ce021447dbea30a4e5ddb3f52534cd2794fb52855071b8dcf257b936ab162168",
    )

    http_jar(
        name = "org_eclipse_jdt_runtime",
        url = "http://download.eclipse.org/eclipse/updates/4.16/R-4.16-202006040540/plugins/org.eclipse.core.runtime_3.18.0.v20200506-2143.jar",
        sha256 = "b5aebc31d480efff38f910a6eab791c2de7b126a47d260252e097b5a27bd0165",
    )

    http_jar(
        name = "org_eclipse_jdt_equinox_common",
        url = "http://download.eclipse.org/eclipse/updates/4.16/R-4.16-202006040540/plugins/org.eclipse.equinox.common_3.12.0.v20200504-1602.jar",
        sha256 = "761f9175b9d294d122c1aa92048688f0b71dd81e808c64cbb245ca7539950716",
    )

    http_jar(
        name = "org_eclipse_jdt_equinox_preferences",
        url = "http://download.eclipse.org/eclipse/updates/4.16/R-4.16-202006040540/plugins/org.eclipse.equinox.preferences_3.8.0.v20200422-1833.jar",
        sha256 = "ca62478a40cffdfe9a10dcfb9f8fada760a93644a7de2c2d1897235f67f57b42",
    )

    http_jar(
        name = "org_eclipse_jdt_compiler_apt",
        url = "http://download.eclipse.org/eclipse/updates/4.16/R-4.16-202006040540/plugins/org.eclipse.jdt.apt.core_3.6.600.v20200529-1546.jar",
        sha256 = "0559677c8d0528fbdfa3a82b4a16661894a9b64a342e418809c64945bb5d3ef1",
    )

    http_jar(
        name = "org_eclipse_jdt_core",
        url = "http://download.eclipse.org/eclipse/updates/4.16/R-4.16-202006040540/plugins/org.eclipse.jdt.core_3.22.0.v20200530-2032.jar",
        sha256 = "af89d348c24917506675767fc1534a0d673355d334fbfadd264b9e45ccd9c34c",
    )

    http_jar(
        name = "org_eclipse_jdt_osgi",
        url = "http://download.eclipse.org/eclipse/updates/4.16/R-4.16-202006040540/plugins/org.eclipse.osgi_3.15.300.v20200520-1959.jar",
        sha256 = "a3544cde6924babf8aff8323f7452ace232d01d040e20d9f9f43027d7b945424",
    )

    http_jar(
        name = "org_eclipse_jdt_text",
        url = "http://download.eclipse.org/eclipse/updates/4.16/R-4.16-202006040540/plugins/org.eclipse.text_3.10.200.v20200428-0633.jar",
        sha256 = "83ce07ec2058d8d629feb4e269216e286560b0e4587dea883f4e16b64ea51cad",
    )

    jvm_maven_import_external(
        name = "org_ow2_asm_asm",
        artifact = "org.ow2.asm:asm:9.7",
        artifact_sha256 = "adf46d5e34940bdf148ecdd26a9ee8eea94496a72034ff7141066b3eea5c4e9d",
        server_urls = _MAVEN_CENTRAL_URLS,
        licenses = ["notice"],
    )

    kotlin_repositories(
        compiler_release = {
            "urls": [
                "https://github.com/JetBrains/kotlin/releases/download/v1.6.10/kotlin-compiler-1.6.10.zip",
            ],
            "sha256": "432267996d0d6b4b17ca8de0f878e44d4a099b7e9f1587a98edc4d27e76c215a",
        },
    )
    kt_register_toolchains()

    # Required by protobuf_java_util
    native.bind(
        name = "guava",
        actual = "@com_google_guava",
    )

    # Required by protobuf_java_util
    native.bind(
        name = "gson",
        actual = "@com_google_code_gson",
    )

    # Required by protobuf_java_util
    native.bind(
        name = "error_prone_annotations",
        actual = "@com_google_errorprone_error_prone_annotations",
    )

    # Required by protobuf_java_util
    native.bind(
        name = "jsr305",
        actual = "@com_google_code_findbugs_jsr305",
    )

    # Required by protobuf_java_util
    native.bind(
        name = "j2objc_annotations",
        actual = "@com_google_j2objc_annotations",
    )