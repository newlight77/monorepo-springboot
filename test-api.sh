#!/bin/sh

#############################################
## Global variables
#############################################
if [ -x /usr/bin/tput ]; then
  red=`tput setaf 1` # error
  green=`tput setaf 2` # nice
  yellow=`tput setaf 3` # warning
  blue=`tput setaf 4` # info
  purple=`tput setaf 5` # command
  teal=`tput setaf 6` # detail
  white=`tput setaf 7` #
  reset=`tput sgr0`
fi


API_URL=http://localhost:8080/api
CLIENT_ID=local.frontend.https
USERNAME=newlight77+test1@gmail.com
PASSWORD=Tricefal1
TOKEN_URL=http://localhost:1080/auth/realms/local.app/protocol/openid-connect/token
TOKEN=

#############################################
## Functions
#############################################
logInfo() {
  echo ${reset} $@ ${reset}  2>&1 | tee -a ${LOG_FILE}
}

logError() {
  echo ${red} $@ ${reset}  2>&1 | tee -a ${LOG_FILE}
}

logWarning() {
  echo ${yellow} $@ ${reset}  2>&1 | tee -a ${LOG_FILE}
}

logCmd() {
  echo ${green} $@ ${reset}  2>&1 | tee -a ${LOG_FILE}
}

usage() {
  echo "Usage: $0 [options]"
  echo ""
  echo "${blue}Options:    ${reset}"
  echo "${blue}          -c, --classpath =[classpath]                           classpath${reset}"
  echo "${blue}          -k, --keycloak-configFile =[keycloak.json file path]     keycloak.json file path${reset}"
  echo "${blue}          -h,  --help                                            help ${reset}"
  echo "${blue}                                                                 ${reset}"
  echo "${blue}By default, this will run with --spring.profiles.active=test ${reset}"
  exit 1
}

signup() {
  curl --insecure --location --request POST "${API_URL}/signup" \
    --header 'Content-Type: application/json' \
    --data-raw '{
        "firstname": "kong", 
        "lastname": "to", 
        "username": "'${USERNAME}'",
        "password": "'${PASSWORD}'",
        "phoneNumber": "0659401130"
    }'
}

getToken() {
  curl --location --request POST "${TOKEN_URL}" \
    --header 'Content-Type: application/x-www-form-urlencoded' \
    --data-urlencode "client_id=${CLIENT_ID}" \
    --data-urlencode "username=${USERNAME}" \
    --data-urlencode "password=${PASSWORD}" \
    --data-urlencode "grant_type=password" | jq -r .access_token
}

helloApi() {
  result=$(curl ${API_URL}/hello)
  echo "${result}"
}

secureUserApi() {
  token=$1
  echo curl --location --request GET "${API_URL}/signup" \
    -H 'Authorization: bearer '${token}
}

uploadCv() {
  token=$1
  curl --location --request POST "${API_URL}/signup/upload/cv" \
    -H 'Authorization: bearer '${token} \
    --form 'file=@/Users/kong/Downloads/contabo-11.pdf'
}

#############################################
## Check arguments
#############################################
for i in "$@"
  do
    case $i in
      -a=*|--api-url=*)                API_URL="${i#*=}"   ;;
      -c=*|--client-id=*)              CLIENT_ID="${i#*=}"   ;;
      -u=*|--username=*)               USERNAME="${i#*=}"   ;;
      -p=*|--password=*)               PASSWORD="${i#*=}"   ;;
      -t=*|--token-url=*)              TOKEN_URL="${i#*=}"   ;;
      -h|--help)                       usage               ;;
      *)                               usage               ;;
    esac
done

#############################################
## Run
#############################################

helloApi
$(signup)
TOKEN=$(getToken)
echo $TOKEN
RESULT=$(secureUserApi ${TOKEN})
echo $RESULT
RESULT=$(uploadCv ${TOKEN})
echo $RESULT

