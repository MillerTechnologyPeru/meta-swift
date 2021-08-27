require icu-swift.inc

LIC_FILES_CHKSUM = "file://../license.html;md5=cc836a60ea65d0b261d2c5f95c725ef3"

def icu_download_version(d):
    pvsplit = d.getVar('PV', True).split('.')
    return pvsplit[0] + "_" + pvsplit[1]

def icu_directory_version(d):
    pvsplit = d.getVar('PV', True).split('.')
    return pvsplit[0] + "-" + pvsplit[1]

ICU_PV = "${@icu_download_version(d)}"
ICU_PV_DIR = "${@icu_directory_version(d)}"

# http://errors.yoctoproject.org/Errors/Details/20486/
ARM_INSTRUCTION_SET_armv4 = "arm"
ARM_INSTRUCTION_SET_armv5 = "arm"

BASE_SRC_URI = "https://github.com/unicode-org/icu/releases/download/release-${ICU_PV_DIR}/icu4c-${ICU_PV}-src.tgz"
SRC_URI = "${BASE_SRC_URI} \
           file://icu-pkgdata-large-cmd.patch \
          "

SRC_URI_append_class-target = "\
           file://0001-Disable-LDFLAGSICUDT-for-Linux.patch \
          "
SRC_URI[md5sum] = "4f558e73b7bd1fa93caf3d10479de41b"
SRC_URI[sha256sum] = "476287b17db6e0b7da230dce4b58e8e5669b1510847f82cab3647920f1374390"
