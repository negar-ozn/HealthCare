package com.healthcare.healthcare_audit.util.openApiDoc;

public final class ApiDescription {

    public static final String AUDIT = "Audit Service";
    public static final String AUDIT_API = "Audit APIs";
    public static final String NEW_AUDIT = "Creates new audit.";
    public static final String AUDIT_SUCCESS = "Audit created successfully.";
    public static final String ALL_LOGS = "Finds all logs.";
    public static final String LOGS_FOUND = "Logs found successfully.";
    public static final String LOGS_NOT_FOUND = "Logs not found.";
    public static final String LOGS_BY_RANGE_DATE = "Finds logs by range date.";
    public static final String LOGS_BY_RANGE_DATE_FOUND = "Logs by range date found successfully.";
    public static final String LOGS_BY_RANGE_DATE_NOT_FOUND = "Logs by range date not found.";
    public static final String LOGS_BY_AUDIT_ID = "Finds logs by audit id.";
    public static final String LOGS_BY_AUDIT_ID_FOUND = "Logs by audit id found successfully.";
    public static final String LOGS_BY_AUDIT_ID_NOT_FOUND = "Logs by audit id not found.";
    public static final String GET_LOGS = """
            Finds logs by a specific condition, such as:
                User Id,
                Action,
                Status,
                Entity name,
                or Entity Id.
            """;
    public static final String LOGS_BY_CONDITION_FOUND = "Logs found successfully.";
    public static final String LOGS_BY_CONDITION_NOT_FOUND = "Logs not found.";
    public static final String AUDIT_REQUEST = "Audit request";
    public static final String USER_ID = "User Id";
    public static final String USERNAME = "Username";
    public static final String ACTION = "Action";
    public static final String ENTITY_NAME = "Entity name";
    public static final String ENTITY_ID = "Entity Id";
    public static final String STATUS = "Status";
    public static final String IP_ADDRESS = "IP address";
    public static final String DETAILS = "Details";
    public static final String EMAIL_VERIFICATION_TOKEN = "Email verification token";
    public static final String PASSWORD_REST_TOKEN = "Password rest token";
}
