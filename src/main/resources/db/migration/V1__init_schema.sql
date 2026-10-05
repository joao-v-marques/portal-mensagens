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

INSERT INTO users (name, username, password_hash, role_id, sector_id)
VALUES ('Administrador do Sistema', 'admin', '$2a$10$h4f7H0W.Qectz4L9yg3Jsuqpdzcu3bRem2Ww8sxjv.xTiy0.ne74q', 1, 1);
