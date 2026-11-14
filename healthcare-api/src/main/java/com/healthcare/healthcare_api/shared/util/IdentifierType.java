package com.healthcare.healthcare_api.shared.util;

import lombok.Getter;

@Getter
public enum IdentifierType {

    PATIENT("PAT"),
    APPOINTMENT("APT"),
    DOCTOR("DOC"),
    SCHEDULE("SCH");

    private final String prefix;

    IdentifierType(String prefix) {
        this.prefix = prefix;
    }

}
