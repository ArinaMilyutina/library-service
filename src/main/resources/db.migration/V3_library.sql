DO
$$
    BEGIN
        IF
            NOT EXISTS(SELECT 1 FROM pg_namespace WHERE nspname = 'library_service_schema') THEN
            CREATE SCHEMA library_service_schema;
        END IF;
    END
$$;

DO
$$
    BEGIN
        IF
            NOT EXISTS(SELECT 1
                       FROM information_schema.tables
                       WHERE table_schema = 'library_service_schema'
                         AND table_name = 'flyway_library_service_history') THEN
            CREATE TABLE library_service_schema.flyway_library_service_history
            (
                installed_rank INT           NOT NULL PRIMARY KEY,
                version        VARCHAR(50),
                description    VARCHAR(200)  NOT NULL,
                type           VARCHAR(20)   NOT NULL,
                script         VARCHAR(1000) NOT NULL,
                checksum       INT,
                installed_by   VARCHAR(100)  NOT NULL,
                installed_on   TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
                execution_time INT           NOT NULL,
                success        BOOLEAN       NOT NULL
            );
        END IF;
    END
$$;

CREATE TABLE library_service_schema.library
(
    id          SERIAL PRIMARY KEY,
    book_id     BIGINT    NOT NULL,
    user_id     BIGINT    NOT NULL,
    borrow_date TIMESTAMP NOT NULL,
    return_date TIMESTAMP
);