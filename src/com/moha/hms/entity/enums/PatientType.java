package com.moha.hms.entity.enums;

public enum PatientType {
    IN,OUT;

    public static PatientType fromString(String patientTypeStr) {
        for (PatientType patientType : values()) {
            if (patientType.name().equalsIgnoreCase(patientTypeStr)) {
                return patientType;
            }
        }
        throw new IllegalArgumentException("Invalid patientType: " + patientTypeStr);
    }
}
