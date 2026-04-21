CREATE TABLE memberships (
    user_id UUID NOT NULL,
    household_id UUID NOT NULL,
    role VARCHAR(50) NOT NULL,

    CONSTRAINT pk_memberships
        PRIMARY KEY (user_id, household_id),

    CONSTRAINT fk_memberships_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_memberships_household
        FOREIGN KEY (household_id)
        REFERENCES households(id)
        ON DELETE CASCADE
);
