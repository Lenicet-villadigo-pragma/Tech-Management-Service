CREATE TABLE IF NOT EXISTS technology (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(90) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY name_tecnology_unique (name)
);
