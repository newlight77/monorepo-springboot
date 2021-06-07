#!make

# Makefile for Demo Auth Serve
SHELL := /bin/sh

export DEBUG_ARG="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5080"

export ENV ?= local
#export BUILD = $(shell git describe --always)-$(shell date +%Y%m%d%H%M%S)
#export TAG = $(shell git describe --abbrev=0 --tags)
#BRANCH = $(shell git branch --show-current)
export VERSION ?= $(shell git describe --always)

include ./config/app.${ENV}.env
export

$(info version = $(VERSION))
$(info env = $(ENV))

env:
	@env

help: ## Display this help screen
	@grep -h -E '^[a-zA-Z0-9_-]+:.*?## .*$$' $(MAKEFILE_LIST) | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-20s\033[0m %s\n", $$1, $$2}'

kill:
	@tools/scripts/kill-process.sh --$(keyword)

kill-java:
	@tools/scripts/kill-process.sh --java --tricefal


clean:
	@./gradlew clean

clean-assemble:
	@./gradlew -g .gradle/caches clean assemble

package:
	@./gradlew -g .gradle/caches build -x test

clean-package:
	@./gradlew -g .gradle/caches clean build -x test

test:
	@./gradlew -g .gradle/caches check jacocoTestReport

code-analysis:
	#@./gradlew -g .gradle/caches sonarqube
	#@./gradlew sonarqube -Dsonar.qualitygate.wait=true
	@docker run --rm -e SONAR_HOST_URL=https://ci.tricefal.io/sonar -e SONAR_LOGIN="f1843a5c58e6658ed82e95e169868d014e1d04b1" -v ~/wks/src/tricefal/app-signup-backend:/usr/src sonarsource/sonar-scanner-cli
	#@docker run -e SONAR_URL=https://ci.tricefal.io/sonar -e SONAR_ANALYSIS_MODE=publish -e SONAR_TOKEN="f1843a5c58e6658ed82e95e169868d014e1d04b1" ciricihq/gitlab-sonar-scanner gitlab-sonar-scanner

boot: 	
	@./.run --env=localhost --run-mode=local

run:
	@./.run --env=local --run-mode=local
#	@java $(DEBUG_ARG) -jar application/signup/build/libs/app-signup-backend-0.0.1-SNAPSHOT.jar --spring.profiles.active=$SPRING_PROFILE

run-localhost:
	@./.run --env=localhost --run-mode=local
#	@java $(DEBUG_ARG) -jar application/signup/build/libs/app-signup-backend-0.0.1-SNAPSHOT.jar --spring.profiles.active=$SPRING_PROFILE

test-api:
	@./test-api.sh --api-url=http://localhost:8080/api --client-id=ci.frontend.https --token-url=https://ci.tricefal.io/auth/realms/ci.app/protocol/openid-connect/token --username=newlight77+${testId}@gmail.com

test-api-local:
	@./test-api.sh --api-url=http://localhost:8080/api --client-id=local.frontend.https --token-url=http://localhost:1080/auth/realms/local.app/protocol/openid-connect/token --username=newlight77+${testId}@gmail.com


dc-build: package
	@docker-compose build app-signup-backend

dc-build-push: package
	@docker system prune -f
	@docker-compose build app-signup-backend
	@docker-compose push app-signup-backend

dc-push:
	@docker-compose push app-signup-backend

dc-up:
	@docker-compose up -d

dc-up-with-keycloak-ci:
	@ENV=ci && docker-compose -f docker-compose.keycloak-ci.yml up -d

# local-full  = full docker local
dc-up-local-full:
	@ENV=local && docker-compose up -d

dc-up-local-keycloak:
	@ENV=local && docker-compose up -d keycloak

dc-up-localhost:
	@ENV=local && docker-compose -f docker-compose.localhost.yml up -d dbsignup keycloak dbkeycloak

dc-down:
	@docker-compose down

dc-down-clean:
	@docker-compose down
	@docker volume prune
	@docker network prune

dc-clean:
	@docker system prune
	@docker volume prune
	@docker network prune

dc-restart: down up


db-psql: ## Launch PostgreSQL Shell
	@docker-compose exec dbsignup psql -U $(POSTGRES_USER) $(POSTGRES_DB)

db-pgsh: ## Launch a shell inside PostgreSQL container
	@docker-compose exec dbsignup sh

db-pgdump: ## Dump the database
	@docker-compose exec dbsignup pg_dump -U $(POSTGRES_USER) $(POSTGRES_DB)


