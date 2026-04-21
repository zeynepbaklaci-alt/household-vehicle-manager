CREATE TABLE vehicles (
    id UUID PRIMARY KEY,
    household_id UUID NOT NULL,
    plate VARCHAR(20) NOT NULL,
    brand VARCHAR(100),
    model VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT now(),

    CONSTRAINT fk_vehicle_household
        FOREIGN KEY (household_id)
        REFERENCES households(id)
        ON DELETE CASCADE
);
