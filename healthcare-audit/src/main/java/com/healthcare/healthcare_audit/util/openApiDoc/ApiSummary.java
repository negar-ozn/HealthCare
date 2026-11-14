package com.healthcare.healthcare_audit.util.openApiDoc;

public final class ApiSummary {

    public static final String NEW_AUDIT = "New audit";
    public static final String ALL_LOGS = "Find all logs";
    public static final String LOGS_BY_RANGE_DATE = "Find logs by range date";
    public static final String LOGS_BY_AUDIT_ID = "Find logs by audit id";
    public static final String GET_LOGS = """
            Find logs by a specific condition, such as:
                User Id,
                Action,
                Status,
                Entity name,
                or Entity Id.
            """;
}
