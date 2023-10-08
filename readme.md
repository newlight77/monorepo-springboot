# Monorepo kotlin/springboot


image:https://gitlab.com/oneprofie/app-signup-backend/badges/master/pipeline.svg[link="https://gitlab.com/oneprofile/app-signup-backend/-/commits/master",title="pipeline status"]
image:https://gitlab.com/oneprofile/app-signup-backend/badges/master/coverage.svg[link="https://gitlab.com/oneprofile/app-signup-backend/-/commits/master",title="coverage report"]

## Pre-requisites:

- [keycloak](https://<server>/auth/realms/dev.app/account/)[reset password]

## Run

```sh
make clean
make package
make test
make code-analysis
make boot
make run

# using docker compose
make dc-build
make dc-up
```

## Open-API
- [API Documentation](https://<server>/api/swagger-ui.html)
