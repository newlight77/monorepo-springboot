#!make

# Makefile for Demo Auth Serve
SHELL := /bin/sh

export DEBUG_ARG="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5080"

#export BUILD = $(shell git describe --always)-$(shell date +%Y%m%d%H%M%S)
#export TAG = $(shell git describe --abbrev=0 --tags)
#BRANCH = $(shell git branch --show-current)
export VERSION ?= $(shell git describe --always)

include ./config/core.env
export

$(info version = $(VERSION))

env:
	@env

help: ## Display this help screen
	@grep -h -E '^[a-zA-Z0-9_-]+:.*?## .*$$' $(MAKEFILE_LIST) | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-20s\033[0m %s\n", $$1, $$2}'

clean: core-clean git-clean

kill:
	@tools/scripts/kill-process.sh --$(keyword)

kill-java:
	@tools/scripts/kill-process.sh --java --tricefal

git-clean:
	@git clean -fXd -e \!node_modules -e \!node_modules/**/*

git-sub:
	@git submodule update --init --recursive



core-clean: ## Run tests
	@./gradlew clean

core-clean-assemble:
	@./gradlew -g .gradle/caches clean assemble

core-package:
	@./gradlew -g .gradle/caches build -x test

core-clean-package:
	@./gradlew -g .gradle/caches clean build -x test

core-test: ## Run tests
	@./gradlew -g .gradle/caches check jacocoTestReport

core-code-analysis: ## Run sonarqube
	#@./gradlew -g .gradle/caches sonarqube
	#@./gradlew sonarqube -Dsonar.qualitygate.wait=true
	@docker run --rm -e SONAR_HOST_URL=https://ci.tricefal.io/sonar -e SONAR_LOGIN="f1843a5c58e6658ed82e95e169868d014e1d04b1" -v ~/wks/src/tricefal/tricefal/core:/usr/src sonarsource/sonar-scanner-cli
	#@docker run -e SONAR_URL=https://ci.tricefal.io/sonar -e SONAR_ANALYSIS_MODE=publish -e SONAR_TOKEN="f1843a5c58e6658ed82e95e169868d014e1d04b1" ciricihq/gitlab-sonar-scanner gitlab-sonar-scanner

core-boot: dc-up-infra
	@./.run -b

core-run: dc-up-infra
	@./.run
#	@java $(DEBUG_ARG) -jar core/application/build/libs/core-application-0.0.1-SNAPSHOT.jar --spring.profiles.active=$SPRING_PROFILE

core-test-api:
	@./test-api.sh -u=newlight77+test1@gmail.com --api-url=http://localhost:8080/api/keycloak


dc-build: ## build docker image for spring-boot
	@docker-compose -f docker-compose.build.yml build core

dc-up: ## Run all containers
	@docker-compose -f docker-compose.yml up -d dbkeycloak keycloak dbcore core

dc-up-keycloak: ## Run keycloak containers
	@docker-compose -f docker-compose.yml up -d dbkeycloak keycloak

dc-up-infra: ## Run infrastructure containers
	@docker-compose -f docker-compose.yml up -d dbkeycloak keycloak dbcore

dc-down: ## Stop all containers
	@docker-compose -f docker-compose.yml down

dc-clean:
	@docker system prune
	@docker volume prune
	@docker network prune

dc-restart: down up ## Restart all containers


db-psql: ## Launch PostgreSQL Shell
	@docker-compose -f docker-compose.yml exec postgres psql -U $(POSTGRES_USER) $(POSTGRES_DB)

db-pgsh: ## Launch a shell inside PostgreSQL container
	@docker-compose -f docker-compose.yml exec postgres sh

db-pgdump: ## Dump the database
	@docker-compose -f docker-compose.yml exec postgres pg_dump -U $(POSTGRES_USER) $(POSTGRES_DB)


