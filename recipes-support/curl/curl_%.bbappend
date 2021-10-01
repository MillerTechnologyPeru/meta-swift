# needed for swift
EXTRA_OECONF += " --enable-versioned-symbols "

# swift libraries depend on openssl
PACKAGECONFIG_append = " ssl "
PACKAGECONFIG_remove = " gnutls "
