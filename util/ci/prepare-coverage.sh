#!/bin/bash

mkdir -p coverage/unit
#mkdir -p coverage/features

rm -f coverage/unit/jacoco.exec
#rm -f coverage/features-api/jacoco.exec
#rm -f coverage/features-domain/jacoco.exec


jacocoFiles=(
"shared/util/build/jacoco/jacocoTestReport.xml"
"shared/sso-keycloak-adapter/build/jacoco/jacocoTestReport.xml"
"application/signup/build/jacoco/jacocoTestReport.xml"
"infrastructure/encryption/build/jacoco/jacocoTestReport.xml"
"infrastructure/notification/build/jacoco/jacocoTestReport.xml"
"infrastructure/signup/build/jacoco/jacocoTestReport.xml"
"infrastructure/storage/build/jacoco/jacocoTestReport.xml"
"infrastructure/cgu/build/jacoco/jacocoTestReport.xml"
"infrastructure/mission/build/jacoco/jacocoTestReport.xml"
"infrastructure/pricer-reference/build/jacoco/jacocoTestReport.xml"
"infrastructure/keycloak-client/build/jacoco/jacocoTestReport.xml"
"infrastructure/freelance/build/jacoco/jacocoTestReport.xml"
"infrastructure/company/build/jacoco/jacocoTestReport.xml"
"domain/pricer/build/jacoco/jacocoTestReport.xml"
"domain/notification/build/jacoco/jacocoTestReport.xml"
"domain/signup/build/jacoco/jacocoTestReport.xml"
"domain/mission/build/jacoco/jacocoTestReport.xml"
"domain/freelance/build/jacoco/jacocoTestReport.xml"
"domain/company/build/jacoco/jacocoTestReport.xml"
)


for file in "${jacocoFiles[@]}"
do
    filename=$(echo $file | sed "s/\//\-/g");
    echo ./coverage/unit/$filename
    cp $file ./coverage/unit/$filename
done
