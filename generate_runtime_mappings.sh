#!/bin/bash
# Use this script to regenerate the runtime mappings.
# Runtime mappings are necessary to generate the JNA mappings for the objc_msgSend() method
# with all of the different parameter permutations.  A single mapping is insufficient
# because JNA requires you to explicitly declare whether a Structure parameter is passed by
# value.

php src/main/php/generate_mappings.php > src/main/java/ca/weblite/objc/RuntimeMappings.java