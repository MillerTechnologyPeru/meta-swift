DESCRIPTION = "Swift"
HOMEPAGE = "https://swift.org/download/#releases"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=2380e856fbdbc7ccae6bd699d53ec121"

SWIFT_VERSION = "5.1.5"

SOURCE_FILE_ARM = "swift-${SWIFT_VERSION}-armv7-Ubuntu1804.tgz"
SOURCE_FILE_ARM64 = "swift-${SWIFT_VERSION}-aarch64-RELEASE-Ubuntu-18.04_2020-03-19.tar.gz"

RDEPENDS_${PN} = "libicuuc-swift libicui18n-swift"

RDEPENDS_${PN} += " libgcc libstdc++ glibc zlib util-linux-libuuid libatomic"

SRC_URI = "\
           file://fix_modulemap.sh \
           file://LICENSE.txt \
"

SRC_URI_append_arm = " https://github.com/uraimo/buildSwiftOnARM/releases/download/${SWIFT_VERSION}/${SOURCE_FILE_ARM};unpack=0;name=arm"
SRC_URI_append_aarch64 = " https://github.com/futurejones/swift-arm64/releases/download/v${SWIFT_VERSION}-RELEASE/${SOURCE_FILE_ARM64};unpack=0;name=aarch64"

SRC_URI[arm.sha256sum] = "4ae5b2072d08bd12eddef781b0f0b9c7ff6afc9c6a4a0d1601e2bb6b7f7844d8"
SRC_URI[aarch64.sha256sum] = "7590bb80b3be9737cd893f75543535f377bcd29f0ba1ac8efa9d157623a27d49"

S="${WORKDIR}"

INSANE_SKIP_${PN} += "ldflags staticdev dev-so dev-elf "
INSANE_SKIP_${PN}-dev += "ldflags staticdev dev-so dev-elf "
INHIBIT_PACKAGE_STRIP = "1"
INHIBIT_SYSROOT_STRIP = "1"
INHIBIT_PACKAGE_DEBUG_SPLIT  = "1"

do_install_prepend() {
    echo "Installing ${DESCRIPTION} ..."

    install -d ${D}${bindir}
}

do_install_arm() {
    tar -xvzf ${WORKDIR}/${SOURCE_FILE_ARM} -C ${D}${bindir}/../..

    echo "Fixing module map"
    ${WORKDIR}/fix_modulemap.sh ${D}${bindir}/../lib/swift/linux/armv7/glibc.modulemap
}

do_install_aarch64() {
    tar -xvzf ${WORKDIR}/${SOURCE_FILE_ARM64} -C ${D}${bindir}/../..

    echo "Fixing module map"
    ${WORKDIR}/fix_modulemap.sh ${D}${bindir}/../lib/swift/linux/aarch64/glibc.modulemap
}

do_install_append() {
    rm -rf ${D}${libdir}/python2.7
    rm -rf ${D}${libdir}/lldb/clang
    rm -rf ${D}${libdir}/lldb
    rm -rf ${D}${libdir}/liblldb*
    rm -rf ${D}${libdir}/lib_InternalSwiftSyntaxParser.so
    rm -rf ${D}${libdir}/swift/migrator
    rm -rf ${D}${libdir}/swift/pm
    rm -rf ${D}${libexecdir}
    rm -rf ${D}/CONTROL
    rm -rf ${D}${bindir}
    rm -rf ${D}${datadir}/icuswift
    rm -rf ${D}${prefix}/local
    rm -rf ${D}${libdir}/swift/linux/lib_InternalSwiftSyntaxParser.so
    # FIXME The prebuilt libraries depend on gcc instead of clang
    # Remove Clang libraries that are already installed in swift/linux
    rm -rf ${D}${includedir}/c++
    rm -rf ${D}${libdir}/lib*
}

SOLIBS = ".so"
FILES_SOLIBSDEV = ""

FILES_${PN} += "${libdir}/libIndexStore.so.7svn"
FILES_${PN}-dev_append = " ${libdir}/swift/Block"
FILES_${PN}-dev_append = " ${libdir}/swift/C*"
FILES_${PN}-dev_append = " ${libdir}/swift/dispatch"
FILES_${PN}-dev_append = " ${libdir}/swift/os"
FILES_${PN}-dev_append = " ${libdir}/swift/shims"
FILES_${PN}-dev_append = " ${libdir}/swift_static"

FILES_${PN}-dev_append_arm = " ${libdir}/swift/linux/armv7"
FILES_${PN}-dev_append_arm = " ${libdir}/swift/clang"

FILES_${PN}-dev_append_aarch64 = " ${libdir}/swift/linux/aarch64"
FILES_${PN}-dev_append_aarch64 = " ${libdir}/clang"
