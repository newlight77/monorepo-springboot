
mkdir -p coverage/unit
mkdir -p coverage/features

rm -f coverage/unti/jacoco.exec
rm -f coverage/features-api/jacoco.exec
rm -f coverage/features-domain/jacoco.exec

cp apps/application/coverage/lcov.info coverage/unit/job-nestjs.unit.lcov.info
cp apps/job-nestjs/coverage/coverage-final.json coverage/unit/job-nestjs.unit.coverage-final.json
cp apps/job-nestjs/coverage/features-domain/lcov.info coverage/features/job-nestjs.domain.cucumber.lcov.info
# cp apps/job-nestjs/coverage/features-api/lcov.info coverage/features/job-nestjs.api.cucumber.lcov.info

cp packages/shared-context/authentication/coverage/lcov.info coverage/unit/authentication.unit.lcov.info
cp packages/shared-context/authentication/coverage/coverage-final.json coverage/unit/authentication.unit.coverage-final.json

cp packages/shared-lib/logger/coverage/lcov.info coverage/unit/logger.unit.lcov.info
cp packages/shared-lib/logger/coverage/coverage-final.json coverage/unit/logger.unit.coverage-final.json

cp packages/shared-spi/redis-client/coverage/lcov.info coverage/unit/redis-client.unit.lcov.info
cp packages/shared-spi/redis-client/coverage/coverage-final.json coverage/unit/redis-client.unit.coverage-final.json
