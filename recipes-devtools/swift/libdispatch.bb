
SUMMARY = "Libdispatch"
HOMEPAGE = "https://github.com/apple/swift-corelibs-libdispatch"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${S}/LICENSE;md5=1cd73afe3fb82e8d5c899b9d926451d0"

require swift-version.inc
PV = "${SWIFT_VERSION}"

SRC_URI = "https://github.com/apple/swift-corelibs-libdispatch/archive/refs/tags/swift-${PV}-RELEASE.tar.gz \
           "
SRC_URI[sha256sum] = "0046ecab640475441251b1cceb3dd167a4c7729852104d7675bdbd75fced6b82"

DEPENDS = "ncurses"

S = "${WORKDIR}/swift-swift-${PV}-RELEASE"

inherit swift-cmake-base

TARGET_LDFLAGS += "-L${STAGING_DIR_TARGET}/usr/lib/swift/linux"

# Disable Swift parts
EXTRA_OECMAKE += "-DENABLE_SWIFT=NO"

# Ensure the right CPU is targeted
TARGET_CPU_NAME = "armv7-a"
cmake_do_generate_toolchain_file_append() {
    sed -i 's/set([ ]*CMAKE_SYSTEM_PROCESSOR .*[ ]*)/set(CMAKE_SYSTEM_PROCESSOR ${TARGET_CPU_NAME})/' ${WORKDIR}/toolchain.cmake
}

FILES_${PN} = "\
    ${libdir}/swift/linux/libswiftDispatch.so \
    ${libdir}/swift/linux/libBlocksRuntime.so \
    ${libdir}/swift/linux/libdispatch.so \
"

# TODO: these are installed into ${libdir}, but that seems wrong...
FILES_${PN}-dev = "\
    ${libdir}/swift/dispatch/cmake/dispatchConfig.cmake \
    ${libdir}/swift/dispatch/cmake/dispatchExports.cmake \
    ${libdir}/swift/dispatch/cmake/dispatchExports-noconfig.cmake \
    ${libdir}/swift/dispatch/module.modulemap \
    ${libdir}/swift/dispatch/introspection.h \
    ${libdir}/swift/dispatch/semaphore.h \
    ${libdir}/swift/dispatch/dispatch.h \
    ${libdir}/swift/dispatch/block.h \
    ${libdir}/swift/dispatch/base.h \
    ${libdir}/swift/dispatch/object.h \
    ${libdir}/swift/dispatch/group.h \
    ${libdir}/swift/dispatch/data.h \
    ${libdir}/swift/dispatch/io.h \
    ${libdir}/swift/dispatch/queue.h \
    ${libdir}/swift/dispatch/source.h \
    ${libdir}/swift/dispatch/time.h \
    ${libdir}/swift/dispatch/once.h \
    ${libdir}/swift/os/object.h \
    ${libdir}/swift/os/generic_win_base.h \
    ${libdir}/swift/os/generic_unix_base.h \
    ${libdir}/swift/Block/Block.h \
"