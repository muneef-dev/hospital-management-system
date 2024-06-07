package com.moha.hms.bo.custom.impl;

import com.moha.hms.bo.BoFactory;
import com.moha.hms.bo.custom.UserBo;
import com.moha.hms.bo.custom.UserRoleBo;
import com.moha.hms.dao.DaoFactory;
import com.moha.hms.dao.custom.UserDao;
import com.moha.hms.dao.custom.UserRoleDao;
import com.moha.hms.dto.UserDto;
import com.moha.hms.dto.UserRoleDto;
import com.moha.hms.entity.User;
import com.moha.hms.entity.UserRole;
import com.moha.hms.util.KeyGenerator;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserBoImpl implements UserBo {

    UserDao userDao = DaoFactory.getInstance().getDao(DaoFactory.DaoType.USER);
    UserRoleDao userRoleDao = DaoFactory.getInstance().getDao(DaoFactory.DaoType.USER_ROLE);

    private static final String DB_URL = "jdbc:mysql://localhost:3306/hms";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1234";
    private static final String DB_NAME = "hms";

    // SQL script to create the database and tables
    private static final String SQL_SCRIPT =
            "CREATE DATABASE IF NOT EXISTS `hms` /*!40100 DEFAULT CHARACTER SET utf8mb3 */ /*!80016 DEFAULT ENCRYPTION='N' */;\n" +
                    "USE `hms`;\n" +
                    "\n" +
                    "CREATE TABLE IF NOT EXISTS department (\n" +
                    "    property_id VARCHAR(45) PRIMARY KEY,\n" +
                    "    name VARCHAR(225) NOT NULL,\n" +
                    "    floor INT NULL,\n" +
                    "    manager VARCHAR(45) NULL\n" +
                    ");\n" +
                    "\n" +
                    "CREATE TABLE IF NOT EXISTS doctor (\n" +
                    "    property_id VARCHAR(45) PRIMARY KEY,\n" +
                    "    name VARCHAR(45) NOT NULL,\n" +
                    "    email VARCHAR(100) NULL,\n" +
                    "    specialist ENUM ('Cardiologist', 'Dermatologist', 'Gastroenterologist', 'Orthopedic', 'Pediatrician', 'Psychiatrist', 'Neurologist', 'Oncologist', 'General Practitioner', 'Other') NOT NULL,\n" +
                    "    contact_number VARCHAR(45) NULL,\n" +
                    "    department_property_id VARCHAR(45) NOT NULL,\n" +
                    "    FOREIGN KEY (department_property_id) REFERENCES department (property_id)\n" +
                    ");\n" +
                    "\n" +
                    "CREATE INDEX fk_doctor_department1_idx ON doctor (department_property_id);\n" +
                    "\n" +
                    "CREATE TABLE IF NOT EXISTS inventory_item (\n" +
                    "    property_id VARCHAR(45) PRIMARY KEY,\n" +
                    "    name VARCHAR(100) NULL,\n" +
                    "    qrcode LONGBLOB NULL,\n" +
                    "    category VARCHAR(255) NULL,\n" +
                    "    qty_on_hand INT NULL,\n" +
                    "    minimum_order_qty INT NULL,\n" +
                    "    supplier VARCHAR(255) NULL,\n" +
                    "    buying_price DOUBLE NULL,\n" +
                    "    selling_price DOUBLE NULL\n" +
                    ");\n" +
                    "\n" +
                    "CREATE TABLE IF NOT EXISTS nurse (\n" +
                    "    property_id VARCHAR(45) PRIMARY KEY,\n" +
                    "    name VARCHAR(45) NOT NULL,\n" +
                    "    email VARCHAR(100) NULL,\n" +
                    "    contact_number VARCHAR(45) NULL,\n" +
                    "    department_property_id VARCHAR(45) NOT NULL,\n" +
                    "    FOREIGN KEY (department_property_id) REFERENCES department (property_id)\n" +
                    ");\n" +
                    "\n" +
                    "CREATE INDEX fk_nurse_department1_idx ON nurse (department_property_id);\n" +
                    "\n" +
                    "CREATE TABLE IF NOT EXISTS patient (\n" +
                    "    property_id VARCHAR(45) PRIMARY KEY,\n" +
                    "    nic VARCHAR(100) NOT NULL,\n" +
                    "    qrcode LONGBLOB NULL,\n" +
                    "    name VARCHAR(45) NULL,\n" +
                    "    dob DATE NOT NULL,\n" +
                    "    age VARCHAR(45) NULL,\n" +
                    "    gender ENUM ('MALE', 'FEMALE') NULL,\n" +
                    "    address VARCHAR(150) NULL,\n" +
                    "    email VARCHAR(100) NULL,\n" +
                    "    contact_number VARCHAR(45) NULL,\n" +
                    "    emergency_contact_number VARCHAR(45) NULL\n" +
                    ");\n" +
                    "\n" +
                    "CREATE TABLE IF NOT EXISTS room (\n" +
                    "    property_id VARCHAR(45) PRIMARY KEY,\n" +
                    "    name VARCHAR(45) NULL,\n" +
                    "    bed_count INT NULL,\n" +
                    "    availability ENUM ('true', 'false') NULL,\n" +
                    "    department_property_id VARCHAR(45) NOT NULL,\n" +
                    "    FOREIGN KEY (department_property_id) REFERENCES department (property_id)\n" +
                    ");\n" +
                    "\n" +
                    "CREATE INDEX fk_room_department1_idx ON room (department_property_id);\n" +
                    "\n" +
                    "CREATE TABLE IF NOT EXISTS user_role (\n" +
                    "    property_id VARCHAR(45) PRIMARY KEY,\n" +
                    "    role_description VARCHAR(100) NULL,\n" +
                    "    role_name VARCHAR(45) NULL\n" +
                    ");\n" +
                    "\n" +
                    "CREATE TABLE IF NOT EXISTS user (\n" +
                    "    property_id VARCHAR(45) PRIMARY KEY,\n" +
                    "    email VARCHAR(45) NOT NULL,\n" +
                    "    name VARCHAR(45) NULL,\n" +
                    "    password VARCHAR(750) NULL,\n" +
                    "    user_role_property_id VARCHAR(45) NOT NULL,\n" +
                    "    UNIQUE (email),\n" +
                    "    FOREIGN KEY (user_role_property_id) REFERENCES user_role (property_id)\n" +
                    ");\n" +
                    "\n" +
                    "CREATE INDEX fk_user_user_role1_idx ON user (user_role_property_id);\n" +
                    "\n" +
                    "CREATE TABLE IF NOT EXISTS appointment (\n" +
                    "    property_id VARCHAR(45) PRIMARY KEY,\n" +
                    "    date DATE NULL,\n" +
                    "    time TIME NULL,\n" +
                    "    reason VARCHAR(255) NULL,\n" +
                    "    status ENUM ('ACTIVE', 'DISCHARGE') NULL COMMENT 'active or discharge',\n" +
                    "    patient_property_id VARCHAR(45) NOT NULL,\n" +
                    "    doctor_property_id VARCHAR(45) NOT NULL,\n" +
                    "    user_property_id VARCHAR(45) NOT NULL,\n" +
                    "    FOREIGN KEY (doctor_property_id) REFERENCES doctor (property_id),\n" +
                    "    FOREIGN KEY (patient_property_id) REFERENCES patient (property_id),\n" +
                    "    FOREIGN KEY (user_property_id) REFERENCES user (property_id)\n" +
                    ");\n" +
                    "\n" +
                    "CREATE INDEX fk_appointment_doctor1_idx ON appointment (doctor_property_id);\n" +
                    "CREATE INDEX fk_appointment_patient1_idx ON appointment (patient_property_id);\n" +
                    "CREATE INDEX fk_appointment_user1_idx ON appointment (user_property_id);\n" +
                    "\n" +
                    "CREATE TABLE IF NOT EXISTS diagnosis (\n" +
                    "    property_id VARCHAR(45) PRIMARY KEY,\n" +
                    "    description VARCHAR(255) NULL,\n" +
                    "    date DATE NULL,\n" +
                    "    patient_type ENUM ('IN', 'OUT') NULL,\n" +
                    "    appointment_property_id VARCHAR(45) NOT NULL,\n" +
                    "    patient_property_id VARCHAR(45) NOT NULL,\n" +
                    "    doctor_property_id VARCHAR(45) NOT NULL,\n" +
                    "    nurse_property_id VARCHAR(45) NOT NULL,\n" +
                    "    FOREIGN KEY (appointment_property_id) REFERENCES appointment (property_id),\n" +
                    "    FOREIGN KEY (doctor_property_id) REFERENCES doctor (property_id),\n" +
                    "    FOREIGN KEY (nurse_property_id) REFERENCES nurse (property_id),\n" +
                    "    FOREIGN KEY (patient_property_id) REFERENCES patient (property_id)\n" +
                    ");\n" +
                    "\n" +
                    "CREATE INDEX fk_diagnosis_appointment1_idx ON diagnosis (appointment_property_id);\n" +
                    "CREATE INDEX fk_diagnosis_doctor1_idx ON diagnosis (doctor_property_id);\n" +
                    "CREATE INDEX fk_diagnosis_nurse1_idx ON diagnosis (nurse_property_id);\n" +
                    "CREATE INDEX fk_diagnosis_patient1_idx ON diagnosis (patient_property_id);\n" +
                    "\n" +
                    "CREATE TABLE IF NOT EXISTS admission (\n" +
                    "    property_id VARCHAR(45) PRIMARY KEY,\n" +
                    "    admitting_date DATE NULL,\n" +
                    "    discharge_date DATE NULL,\n" +
                    "    patient_property_id VARCHAR(45) NOT NULL,\n" +
                    "    room_property_id VARCHAR(45) NOT NULL,\n" +
                    "    doctor_property_id VARCHAR(45) NOT NULL,\n" +
                    "    diagnosis_property_id VARCHAR(45) NOT NULL,\n" +
                    "    FOREIGN KEY (diagnosis_property_id) REFERENCES diagnosis (property_id),\n" +
                    "    FOREIGN KEY (doctor_property_id) REFERENCES doctor (property_id),\n" +
                    "    FOREIGN KEY (patient_property_id) REFERENCES patient (property_id),\n" +
                    "    FOREIGN KEY (room_property_id) REFERENCES room (property_id)\n" +
                    ");\n" +
                    "\n" +
                    "CREATE INDEX fk_admission_diagnosis1_idx ON admission (diagnosis_property_id);\n" +
                    "CREATE INDEX fk_admission_doctor1_idx ON admission (doctor_property_id);\n" +
                    "CREATE INDEX fk_admission_patient1_idx ON admission (patient_property_id);\n" +
                    "CREATE INDEX fk_admission_room1_idx ON admission (room_property_id);\n" +
                    "\n" +
                    "CREATE TABLE IF NOT EXISTS medication (\n" +
                    "    property_id VARCHAR(45) NOT NULL COMMENT 'diagnosis_has_inventory',\n" +
                    "    patient_property_id VARCHAR(45) NOT NULL,\n" +
                    "    patient_name VARCHAR(45) NULL,\n" +
                    "    sold_qty INT NULL,\n" +
                    "    total_cost DOUBLE NULL,\n" +
                    "    diagnosis_property_id VARCHAR(45) NOT NULL,\n" +
                    "    inventory_item_property_id VARCHAR(45) NOT NULL,\n" +
                    "    PRIMARY KEY (property_id, diagnosis_property_id, inventory_item_property_id),\n" +
                    "    FOREIGN KEY (diagnosis_property_id) REFERENCES diagnosis (property_id),\n" +
                    "    FOREIGN KEY (inventory_item_property_id) REFERENCES inventory_item (property_id)\n" +
                    ");\n" +
                    "\n" +
                    "CREATE INDEX fk_diagnosis_has_inventory_diagnosis1_idx ON medication (diagnosis_property_id);\n" +
                    "CREATE INDEX fk_diagnosis_has_inventory_inventory_item1_idx ON medication (inventory_item_property_id);\n" +
                    "\n" +
                    "CREATE TABLE IF NOT EXISTS bill (\n" +
                    "    property_id VARCHAR(45) PRIMARY KEY,\n" +
                    "    qrcode LONGBLOB NULL,\n" +
                    "    issued_date DATE NULL,\n" +
                    "    payment_status VARCHAR(45) NULL,\n" +
                    "    doctor_fee DOUBLE NULL,\n" +
                    "    medication_cost DOUBLE NULL,\n" +
                    "    labtest_cost DOUBLE NULL,\n" +
                    "    other_services_cost DOUBLE NULL,\n" +
                    "    patient_property_id VARCHAR(45) NOT NULL,\n" +
                    "    medication_property_id VARCHAR(45) NOT NULL,\n" +
                    "    user_property_id VARCHAR(45) NOT NULL,\n" +
                    "    FOREIGN KEY (medication_property_id) REFERENCES medication (property_id),\n" +
                    "    FOREIGN KEY (patient_property_id) REFERENCES patient (property_id),\n" +
                    "    FOREIGN KEY (user_property_id) REFERENCES user (property_id)\n" +
                    ");\n" +
                    "\n" +
                    "CREATE INDEX fk_bill_medication1_idx ON bill (medication_property_id);\n" +
                    "CREATE INDEX fk_bill_patient1_idx ON bill (patient_property_id);\n" +
                    "CREATE INDEX fk_bill_user1_idx ON bill (user_property_id);\n";

    // Adjust connection URL to directly connect to `hms` database

    @Override
    public void initializeSystem() {
//        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD){
//             Statement statement = connection.createStatement()) {
//
//            // Execute the SQL script without the `USE` statement
//            String sqlScript = SQL_SCRIPT.replace("USE `hms`;", "");
//            statement.execute(sqlScript);

            // Check if admin role exists and create default admin user if necessary
            try{
            if (!userRoleDao.isRoleExist("ADMIN")) {
                String adminRoleId = KeyGenerator.generateId();

                userRoleDao.create(new UserRole(
                        adminRoleId, "This is Admin", "ADMIN"));

                userDao.create(new User(
                        KeyGenerator.generateId(), "admin@gmail.com", "Admin", "A@1234", adminRoleId));

                new Alert(Alert.AlertType.INFORMATION,
                        "Default Admin account created automatically.").show();
            }
        } catch (SQLException | ClassNotFoundException ex) {
            new Alert(Alert.AlertType.ERROR, "Error initializing system: " + ex.getMessage()).show();
            throw new RuntimeException(ex);
        }
    }


    @Override
    public boolean createUser(UserDto userDto) throws SQLException, ClassNotFoundException {
        return userDao.create(new User(
                userDto.getId(), userDto.getEmail(),userDto.getName(), userDto.getPassword(),userDto.getRole()
        ));
    }

    @Override
    public List<UserDto> searchUser(String id) throws SQLException, ClassNotFoundException {
        List<UserDto> userDtoList = new ArrayList<>();
        for (User user: userDao.search(id)
             ) {
            userDtoList.add(new UserDto(
                    user.getId(), user.getEmail(),user.getName(), user.getPassword(),user.getRole()
            ));
        }
        return userDtoList;
    }

    @Override
    public boolean deleteUser(String id) throws SQLException, ClassNotFoundException {
        return userDao.delete(id);
    }

    @Override
    public boolean updateUser(UserDto userDto) throws SQLException, ClassNotFoundException {
        return userDao.update(new User(
                userDto.getId(), userDto.getEmail(),userDto.getName(), userDto.getPassword(),userDto.getRole()
        ));
    }

    @Override
    public List<UserDto> loadAllUsers() throws SQLException, ClassNotFoundException {
        List<UserDto> userDtoList = new ArrayList<>();
        for (User user : userDao.loadAll()
             ) {
            userDtoList.add(new UserDto(
                    user.getId(), user.getEmail(),user.getName(), user.getPassword(),user.getRole()
            ));
        }
        return userDtoList;
    }

    public List<UserDto> loadAllManagers(String id) throws SQLException, ClassNotFoundException {
        List<UserDto> userList = new ArrayList<>();
        for (User user : userDao.loadAllManagers(id)
             ) {
            userList.add(new UserDto(
                    user.getId(), user.getEmail(),user.getName(), user.getPassword(),user.getRole()
            ));
        }
        return userList;
    }

    @Override
    public Optional<UserDto> authenticateUser(String email, String password) throws SQLException, ClassNotFoundException {
        User user = userDao.getUserByEmail(email).get();
        if (user != null && user.getPassword().equals(password)) {
            UserDto userDto = new UserDto(
                    user.getId(), user.getEmail(), user.getName(), user.getPassword(), user.getRole()
            );
            return Optional.of(userDto);
        }
        return Optional.empty();
    }

    @Override
    public String getUserName(String id) throws SQLException, ClassNotFoundException {
        return userDao.getUserName(id);
    }

    @Override
    public List<UserDto> searchUserThroughEmail(String emailId) throws SQLException, ClassNotFoundException {
        List<UserDto> userDtoList = new ArrayList<>();
        for (User user: userDao.searchUserThroughEmail(emailId)
        ) {
            userDtoList.add(new UserDto(
                    user.getId(), user.getEmail(),user.getName(), user.getPassword(),user.getRole()
            ));
        }
        return userDtoList;
    }
}
