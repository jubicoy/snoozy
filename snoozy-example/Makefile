SHELL := /bin/bash

LOCAL_DB_NAME := snoozy-example-db

.PHONY: start
start:
	JOOQ_URL=jdbc:mysql://127.0.0.1:3306/test \
						JOOQ_USER=test \
						JOOQ_PASSWORD=test \
						JOOQ_DIALECT=MYSQL \
						java -jar target/snoozy-example-*.jar

.PHONY: bootstrap-local
bootstrap-local: start-local-db

.PHONY: clean-local
clean-local: stop-local-db

.PHONY: start-local-db
start-local-db:
	docker run -d --name $(LOCAL_DB_NAME) \
		-P \
		-e "MYSQL_USER=test" \
		-e "MYSQL_PASSWORD=test" \
		-e "MYSQL_DATABASE=test" \
		-e "MYSQL_ROOT_PASSWORD=test" \
		-p 3306:3306 \
		mariadb:10.3 mysqld --skip-name-resolve

.PHONY: stop-local-db
stop-local-db:
	-docker stop $(LOCAL_DB_NAME)
	-docker rm $(LOCAL_DB_NAME)

