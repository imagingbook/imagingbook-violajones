#!/bin/bash

mvn clean 
mvn install -Dmaven.test.skip=true

rm -rf javadoc/*
mvn javadoc:aggregate -Dimagingbook.skipjavadoc=false
mvn javadoc:aggregate-jar -Dimagingbook.skipjavadoc=false

# mvn deploy -Dmaven.test.skip=true -Dimagingbook.skipjavadoc=false -Dimagingbook.gpgSkip=false
mvn deploy -Dmaven.test.skip=true -Dimagingbook.skipjavadoc=true -Dimagingbook.gpgSkip=true

echo ""
read -rsp $'Done. Press any key to quit...\n' -n1 key
