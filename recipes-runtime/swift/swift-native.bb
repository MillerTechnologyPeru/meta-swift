
DESCRIPTION = "Swift"
HOMEPAGE = "https://swift.org/download/#releases"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=2380e856fbdbc7ccae6bd699d53ec121"

SWIFT_VERSION = "5.1.5"

SOURCE_FILE_x86 = "swift-${SWIFT_VERSION}-RELEASE-ubuntu18.04.tar.gz"
SOURCE_FILE_ARM = "swift-${SWIFT_VERSION}-armv7-Ubuntu1804.tgz"
SOURCE_FILE_ARM64 = "swift-${SWIFT_VERSION}-aarch64-RELEASE-Ubuntu-18.04_2020-03-19.tar.gz"

SRC_URI = "https://swift.org/builds/swift-${SWIFT_VERSION}-release/ubuntu1804/swift-${SWIFT_VERSION}-RELEASE/${SOURCE_FILE_x86};unpack=0;name=x86 \
           file://fix_modulemap.sh \
           file://LICENSE.txt \
"

SRC_URI_append = " https://github.com/uraimo/buildSwiftOnARM/releases/download/${SWIFT_VERSION}/${SOURCE_FILE_ARM};unpack=0;name=arm"
SRC_URI_append = " https://github.com/futurejones/swift-arm64/releases/download/v${SWIFT_VERSION}-RELEASE/${SOURCE_FILE_ARM64};unpack=0;name=aarch64"

SRC_URI[x86.sha256sum] = "1e09efd1584ad380be6f2b513dccc1fe3bc7e328724c4e5c60024e7e3de9782a"
SRC_URI[arm.sha256sum] = "4ae5b2072d08bd12eddef781b0f0b9c7ff6afc9c6a4a0d1601e2bb6b7f7844d8"
SRC_URI[aarch64.sha256sum] = "7590bb80b3be9737cd893f75543535f377bcd29f0ba1ac8efa9d157623a27d49"

DEPENDS += " gcc-runtime"

INSANE_SKIP_${PN} += "ldflags staticdev dev-so dev-elf "
INHIBIT_PACKAGE_STRIP = "1"
INHIBIT_SYSROOT_STRIP = "1"
INHIBIT_PACKAGE_DEBUG_SPLIT  = "1"
EXCLUDE_FROM_SHLIBS = "1"

inherit native

S = "${WORKDIR}"

do_install() {
    echo "Installing ${DESCRIPTION} ..."

    install -d ${D}${bindir}

    # Install SPM in the rootfs
    tar -xzf ${WORKDIR}/${SOURCE_FILE_x86} --strip-components=1 -C ${D}${bindir}/../../
    rm -rf ${D}${bindir}/../../usr/include/unicode

    # Create the cross-compiling swiftc
    # ARM
    install -d ${D}${bindir}/../../opt/swift-arm
    tar -xzf ${WORKDIR}/${SOURCE_FILE_x86} --strip-components=1 -C ${D}${bindir}/../../opt/swift-arm
    # AARCH64
    install -d ${D}${bindir}/../../opt/swift-aarch64
    tar -xzf ${WORKDIR}/${SOURCE_FILE_x86} --strip-components=1 -C ${D}${bindir}/../../opt/swift-aarch64

    install -d ${D}${bindir}/../../opt/swift-arm-tmp
    tar -xzf ${WORKDIR}/${SOURCE_FILE_ARM} -C ${D}${bindir}/../../opt/swift-arm-tmp

    install -d ${D}${bindir}/../../opt/swift-aarch64-tmp
    tar -xzf ${WORKDIR}/${SOURCE_FILE_ARM64} -C ${D}${bindir}/../../opt/swift-aarch64-tmp


    # ARM
    echo "Copying swift arm std lib to SDK"
    cp -rav ${D}${bindir}/../../opt/swift-arm-tmp/usr/lib/swift/linux ${D}${bindir}/../../opt/swift-arm/usr/lib/swift/
    cp -rav ${D}${bindir}/../../opt/swift-arm-tmp/usr/lib/swift_static/linux ${D}${bindir}/../../opt/swift-arm/usr/lib/swift/

    # AARCH64
    echo "Copying swift arm64 std lib to SDK"
    cp -rav ${D}${bindir}/../../opt/swift-aarch64-tmp/usr/lib/swift/linux ${D}${bindir}/../../opt/swift-aarch64/usr/lib/swift/
    cp -rav ${D}${bindir}/../../opt/swift-aarch64-tmp/usr/lib/swift_static/linux ${D}${bindir}/../../opt/swift-aarch64/usr/lib/swift/

    echo "Fixing module map"
    ${WORKDIR}/fix_modulemap.sh ${D}${bindir}/../../opt/swift-arm/usr/lib/swift/linux/armv7/glibc.modulemap
    ${WORKDIR}/fix_modulemap.sh ${D}${bindir}/../../opt/swift-aarch64/usr/lib/swift/linux/aarch64/glibc.modulemap
}

do_populate_sysroot_append() {
    import os  
    import shutil 

    destdir = d.getVar('SYSROOT_DESTDIR')
    ddir = d.getVar('D')
    bindir = d.getVar('bindir')
    # print ("do_populate_sysroot_append --> Destdir " + destdir)
    # print ("do_populate_sysroot_append --> ddir " + ddir)
    # print ("do_populate_sysroot_append --> bindir " + bindir)

    shutil.copytree(ddir + bindir + "/../../opt/swift-arm", destdir + bindir + "/../../opt/swift-arm")
    shutil.copytree(ddir + bindir + "/../../opt/swift-aarch64", destdir + bindir + "/../../opt/swift-aarch64")
}

FILES_${PN} = "*"
