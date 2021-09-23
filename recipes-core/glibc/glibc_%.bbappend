
# The pre-built binaries for aarch64 look for includes in the tripplet
# therefore, we install them

TRIPLET = "${TUNE_ARCH}-linux-gnu${ABIEXTENSION}"

do_install_append() {
    install -d ${D}${includedir}/${TRIPLET}
    cp -rv ${D}${includedir}/sys ${D}${includedir}/${TRIPLET}/
}

FILES_${PN}-dev += "${includedir}/${TRIPLET}"
