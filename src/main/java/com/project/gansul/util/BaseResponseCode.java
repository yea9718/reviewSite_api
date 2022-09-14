package com.project.gansul.util;

public enum BaseResponseCode {
    SUCCESS(1, "SUCCESS"),
    PARAMETER_VALUE_INVALID(-100, "error.parameter.invalid"),
    AUTH_LOGOUT_SUCCESS(9000, "message.logout.success"),
    AUTH_PASSWORD_RESET(9001, "message.password.reset"),
    AUTH_PASSWORD_CHANGED(9002, "message.password.changed"),
    AUTH_UNAUTHORIZED(-9001, "error.auth.unauthorized"),
    AUTH_EMAIL_UNVERIFIED(-9002, "error.auth.email.unverified"),
    AUTH_PASSWORD_UNMATCHED(-9003, "error.auth.password.unmatched"),
    AUTH_PASSWORD_TEMPORARY(-9004, "error.auth.password.temporary"),
    AUTH_TOKEN_REVOKED(-9005, "error.auth.token.revoked"),
    AUTH_NOT_ALLOW_RESET(-9006, "error.auth.password.notallowed"),
    AUTH_TOKEN_IS_INVALID(-9007, "error.auth.token.invalid"),
    ACCOUNT_DUPLICATED(-1001, "error.auth.usernmae.duplicated"),
    ACCOUNT_EMAIL_NOT_EXIST(-1002, "error.auth.email.no"),
    EMPLOYEE_ID_NOT_EXIST(-2000, "error.employee.id.no"),
    EMPLOYEE_EMAIL_DUPLICATED(-2001, "error.employee.email.duplicated"),
    EMPLOYEE_NUMBER_DUPLICATED(-2002, "error.employee.number.duplicated"),
    EMPLOYEE_EDUCATION_ID_NOT_EXIST(-2003, "error.employee.education.id.no"),
    EMPLOYEE_WORKEXPERIENCE_ID_NOT_EXIST(-2004, "error.employee.workexperience.id.no"),
    DEPARTMENT_NO_DEPARTMENT_BY_ID(-2005, "error.department.department.no"),
    SCHEDULE_NO_SCHEDULE_BY_ID(-2006, "error.schedule.schedule.no"),
    SCHEDULE_DUPLICATE_START_TIME(-2007, "error.schedule.duplicated.start"),
    SCHEDULE_DUPLICATE_END_TIME(-2008, "error.schedule.duplicated.end"),
    SCHEDULE_DUPLICATE_BOTH_TIME(-2008, "error.schedule.duplicated.both"),
    CUSTOMER_ID_NOT_EXIST( -4000, "error.customer.id.no"),
    CUSTOMER_NUMBER_DUPLICATED( -4002, "error.customer.number.duplicated"),
    CUSTOMER_GROUP_ID_NOT_EXIST( -4003, "error.customer.group.id.no"),
    CUSTOMER_GROUP_NUMBER_DUPLICATED( -4004, "error.customer.group.number.duplicated"),
    BASE_ID_NOT_EXIST( -4601, "error.base.id.no"),
    BASE_NUMBER_DUPLICATED( -4602, "error.base.number.duplicated"),
    BASE_GB_NOT_EXIST( -4603, "error.base.basegb.no"),
    FILE_UPLOAD_ERROR(-9101, "error.file.upload"),
    FILE_DATA_NOT_EXIST(-9102, "error.file.data.no"),
    COLUMN_LENGTH_DIFF(-9103, "error.column.length.difference"),
    EXCEL_TEMPLATE_NOT_EXIST(-9104, "error.excel.data.no"),
    FILE_NOT_FOUND(-9105, "error.file.not.found"),
    EXCEL_NOT_MATCH_COLUMN_COUNT(-9106, "error.excel.notmatch.column.count"),
    EXCEL_LOAD_ERROR(-9107, "error.excel.data.load"),
    EXCEL_FILE_FOR_FILE_ID(-9108, "error.excel.file.id.no"),
    SYSTEM_ERROR(-9999, "error.system.unknown");
    public final int CODE;
    public final String PHRASE;

    BaseResponseCode(int CODE, String PHRASE) {
        this.CODE = CODE;
        this.PHRASE = PHRASE;
    }
}
