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
	@tools/scripts/kill-process.sh --java --oneprofile


clean: ## gradle clean
	@./gradlew clean

clean-assemble: ## gradle clean and assemble
	@./gradlew -g .gradle/caches clean assemble

package: ## gradle build without test
	@./gradlew -g .gradle/caches build -x test

clean-package: ## gradle clean and buid without test
	@./gradlew -g .gradle/caches clean build -x test

test: ## running all test and generate jacoco reports
	@./gradlew -g .gradle/caches check jacocoTestReport

merge-coverage: ## move all jacocoTestReport.xml from multiple modules under coverage
	@./util/ci/prepare-coverage.sh

code-analysis: ## analysing code using sonarqube plugin for gradle
	#@./gradlew -g .gradle/caches sonarqube
	#@./gradlew sonarqube -Dsonar.qualitygate.wait=true
	#@docker run --rm -e SONAR_HOST_URL=https://ci.oneprofile.io/sonar -e SONAR_LOGIN="f1843a5c58e6658ed82e95e169868d014e1d04b1" -v ~/wks/src/oneprofile/app-signup-backend:/usr/src sonarsource/sonar-scanner-cli
	#@docker run -e SONAR_URL=https://ci.oneprofile.io/sonar -e SONAR_ANALYSIS_MODE=publish -e SONAR_TOKEN="f1843a5c58e6658ed82e95e169868d014e1d04b1" ciricihq/gitlab-sonar-scanner gitlab-sonar-scanner

boot: dc-up-infra-localhost ## starting springboot using localhost env : keycloak serving as localhost
# 	@sleep 15
	@./.run --env=localhost --run-mode=boot

run: ## running jar using local env, to be used inside docker, and requires keycloak+db by docker compose
	@./.run --env=local --run-mode=local
#	@java $(DEBUG_ARG) -jar application/signup/build/libs/app-signup-backend-0.0.1-SNAPSHOT.jar --spring.profiles.active=local

run-localhost: dc-up-infra-localhost ## running jar using localhost env, and requires keycloak+db by docker compose
# 	@sleep 15
	@./.run --env=localhost --run-mode=local
#	@java $(DEBUG_ARG) -jar application/signup/build/libs/app-signup-backend-0.0.1-SNAPSHOT.jar --spring.profiles.active=local

test-api-use-dev: ## test the api on localhost using dev keycloak instance
	@./test-api.sh --api-url=http://localhost:8080/api --client-id=dev.frontend.https --token-url=https://dev.oneprofile.io/auth/realms/dev.app/protocol/openid-connect/token --username=newlight77+${testId}@gmail.com

test-api-use-local: ## test the api using localhost instance
	@./test-api.sh --api-url=http://localhost:8080/api --client-id=local.frontend.https --token-url=http://localhost:2080/auth/realms/local.app/protocol/openid-connect/token --username=newlight77+${testId}@gmail.com


dc-build: package ## build docker images
	@docker-compose build app-signup-backend

dc-build-push: package ## build and push docker images to container registry
	@docker system prune -f
	@docker-compose build app-signup-backend
	@docker-compose push app-signup-backend

dc-push: ## push docker images to container registry
	@docker-compose push app-signup-backend

dc-up: ## full docker local
	@docker-compose up -d

dc-up-use-dev: ## full docker using dev env (remote server) serving keycloak with dev config
	@ENV=dev && docker-compose -f docker-compose.use-dev.yml up -d

dc-up-infra-localhost: ## spin up docker containers : dbsignup keycloak dbkeycloak
	@docker-compose -f docker-compose.localhost.yml up -d dbsignup keycloak dbkeycloak

dc-down: ## docker compose down
	@docker-compose down

dc-down-clean: ## docker compose down and prun
	@docker-compose down
	@docker volume prune
	@docker network prune

dc-clean: ## docker prune
	@docker system prune
	@docker volume prune
	@docker network prune

dc-restart: dc-down dc-up


db-psql: ## Launch PostgreSQL Shell
	@docker-compose exec dbsignup psql -U $(POSTGRES_USER) $(POSTGRES_DB)

db-pgsh: ## Launch a shell inside PostgreSQL container
	@docker-compose exec dbsignup sh

db-pgdump: ## Dump the database
	@docker-compose exec dbsignup pg_dump -U $(POSTGRES_USER) $(POSTGRES_DB)
