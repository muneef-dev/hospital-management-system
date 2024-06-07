package com.moha.hms.dao.custom;

import com.moha.hms.dao.CrudDao;
import com.moha.hms.entity.Doctor;
import com.moha.hms.entity.Room;

import java.sql.SQLException;

public interface RoomDao extends CrudDao<Room,String> {
    String getLastRoomId() throws SQLException, ClassNotFoundException;
    String getRoomName(String id) throws SQLException, ClassNotFoundException;
}
