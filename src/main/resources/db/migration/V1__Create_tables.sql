CREATE TABLE "user" (
                        id INT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
                        username VARCHAR(100) NOT NULL,
                        email VARCHAR(255) NOT NULL,
                        password VARCHAR(255) NOT NULL,
                        role VARCHAR(50) DEFAULT 'USER', -- роль пользователя (USER, ADMIN и т.д.)
                        status VARCHAR(20) DEFAULT 'ACTIVE', -- статус активации учетной записи (ACTIVE, PENDING и т.д.)
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE profile (
                         id INT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
                         user_id INT NOT NULL UNIQUE,
                         full_name VARCHAR(255),
                         gender VARCHAR(10),
                         date_of_birth DATE,
                         address TEXT,
                         phone_number VARCHAR(20),
                         avatar_url VARCHAR(255),
                         FOREIGN KEY (user_id) REFERENCES "user"(id)
);

CREATE TABLE Role (
                      id INT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
                      name VARCHAR(50) UNIQUE NOT NULL,
                      description TEXT,
                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE userRole (
                          user_id INT,
                          role_id INT,
                          PRIMARY KEY (user_id, role_id),
                          FOREIGN KEY (user_id) REFERENCES "user"(id),
                          FOREIGN KEY (role_id) REFERENCES Role(id)
);

CREATE TABLE UserActivationToken (
                                     id INT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
                                     user_id INT NOT NULL,
                                     token VARCHAR(255) NOT NULL,
                                     expiration_time TIMESTAMP NOT NULL,
                                     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                     FOREIGN KEY (user_id) REFERENCES "user"(id)
);