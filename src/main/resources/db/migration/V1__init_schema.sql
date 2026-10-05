CREATE TABLE roles (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

    name TEXT NOT NULL UNIQUE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE
);

INSERT INTO roles (name)
VALUES
    ('ADMINISTRATOR'),
    ('EMPLOYEE');

CREATE TABLE user_sectors (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

    name TEXT NOT NULL UNIQUE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE
);

INSERT INTO user_sectors (name)
VALUES
    ('TIC'),
    ('Atendimento');

CREATE TABLE users (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

    name VARCHAR(255),
    username VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role_id INT NOT NULL, -- fk roles
    sector_id INT, -- fk sectors

    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    inserted_by INT, -- fk users

    CONSTRAINT fk_user_role FOREIGN KEY (role_id) REFERENCES roles(id),
    CONSTRAINT fk_user_sector FOREIGN KEY (sector_id) REFERENCES user_sectors(id),
    CONSTRAINT fk_user_inserted_by FOREIGN KEY (inserted_by) REFERENCES users(id)
);

