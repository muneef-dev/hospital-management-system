package com.moha.hms.bo;

import com.moha.hms.bo.custom.impl.*;

public class BoFactory {
    private static BoFactory boFactory;

    private BoFactory() {
    }

    public enum BoType {
        USER, PATIENT, USER_ROLE, DEPARTMENT, DOCTOR, NURSE,ROOM,ADMISSION,APPOINTMENT,BILL,DIAGNOSIS,INVENTORY_ITEM,
        MEDICATION
    }

    public static BoFactory getInstance() {
        return (boFactory == null) ? boFactory = new BoFactory() : boFactory;
    }

    public <T> T getBo(BoType boType) {
        switch (boType) {
            case PATIENT:
                return (T) new PatientBoImpl();
            case USER:
                return (T) new UserBoImpl();
            case USER_ROLE:
                return (T) new UserRoleBoImpl();
            case DEPARTMENT:
                return (T) new DepartmentBoImpl();
            case DOCTOR:
                return (T) new DoctorBoImpl();
            case NURSE:
                return (T) new NurseBoImpl();
            case ROOM:
                return (T) new RoomBoImpl();
            case ADMISSION:
                return (T) new AdmissionBoImpl();
            case APPOINTMENT:
                return (T) new AppointmentBoImpl();
            case BILL:
                return (T) new BillBoImpl();
            case DIAGNOSIS:
                return (T) new DiagnosisBoImpl();
            case INVENTORY_ITEM:
                return (T) new InventoryItemBoImpl();
            case MEDICATION:
                return (T) new MedicationBoImpl();
            default:
                return null;
        }
    }
}
