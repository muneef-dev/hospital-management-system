package com.moha.hms.dao;

import com.moha.hms.dao.custom.impl.*;

public class DaoFactory {
    private static DaoFactory daoFactory;
    private DaoFactory(){}
    public enum DaoType{
        USER,PATIENT,USER_ROLE,DEPARTMENT,DOCTOR,NURSE,ROOM,ADMISSION,APPOINTMENT,BILL,DIAGNOSIS,INVENTORY_ITEM,
        MEDICATION
    }

    public static DaoFactory getInstance(){
        return (daoFactory==null) ? daoFactory= new DaoFactory() : daoFactory;
    }

    public <T> T getDao(DaoType daoType){
        switch (daoType){
            case PATIENT:
                return (T) new PatientDaoImpl();
            case USER:
                return (T) new UserDaoImpl();
            case USER_ROLE:
                return (T) new UserRoleDaoImpl();
            case DEPARTMENT:
                return (T) new DepartmentDaoImpl();
            case DOCTOR:
                return (T) new DoctorDaoImpl();
            case NURSE:
                return (T) new NurseDaoImpl();
            case ROOM:
                return (T) new RoomDaoImpl();
            case ADMISSION:
                return (T) new AdmissionDaoImpl();
            case APPOINTMENT:
                return (T) new AppointmentDaoImpl();
            case BILL:
                return (T) new BillDaoImpl();
            case DIAGNOSIS:
                return (T) new DiagnosisDaoImpl();
            case INVENTORY_ITEM:
                return (T) new InventoryItemDaoImpl();
            case MEDICATION:
                return (T) new MedicationDaoImpl();
            default:
                return null;
        }
    }
}
