package com.moha.hms.entity.enums;

public enum AppointmentStatus {
    ACTIVE,DISCHARGE;

    public static AppointmentStatus fromString(String appointmentStatusStr) {
        for (AppointmentStatus appointmentStatus : values()) {
            if (appointmentStatus.name().equalsIgnoreCase(appointmentStatusStr)) {
                return appointmentStatus;
            }
        }
        throw new IllegalArgumentException("Invalid appointmentStatus: " + appointmentStatusStr);
    }
}
