package com.moha.hms.dao.custom.impl;

import com.moha.hms.dao.CrudUtil;
import com.moha.hms.dao.custom.RoomDao;
import com.moha.hms.entity.Room;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoomDaoImpl implements RoomDao {
    @Override
    public boolean create(Room room) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO room VALUES (?,?,?,?,?)",
                room.getId(), room.getName(), room.getBedCount(),
                room.getAvailability(),room.getDepartmentName());
    }

    @Override
    public List<Room> search(String id) throws SQLException, ClassNotFoundException {
        id = "%"+id+"%";
        ResultSet resultSet = CrudUtil.execute(
                "SELECT * FROM room WHERE property_id LIKE ? || name Like ?",id,id);
        List<Room> roomList = new ArrayList<>();
        while (resultSet.next()){
            roomList.add(new Room(
                    resultSet.getString(1), resultSet.getString(2),
                    Integer.parseInt(resultSet.getString(3)),resultSet.getString(4),
                    resultSet.getString(5)
            ));
        }
        return roomList;
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM room WHERE property_id=?",id);
    }

    @Override
    public boolean update(Room room) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE room SET " +
                "name=?,bed_count=?,availability=?,department_property_id=? WHERE property_id=?",
                room.getName(), room.getBedCount(),room.getAvailability(),
                room.getDepartmentName(),room.getId());
    }

    @Override
    public List<Room> loadAll() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM room");
        List<Room> roomList = new ArrayList<>();
        while (resultSet.next()){
            roomList.add(new Room(
                    resultSet.getString(1), resultSet.getString(2),
                    Integer.parseInt(resultSet.getString(3)),resultSet.getString(4),
                    resultSet.getString(5)
            ));
        }
        return roomList;
    }

    @Override
    public String getLastRoomId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute(
                "SELECT property_id FROM room ORDER BY CAST(SUBSTRING(property_id,3)AS UNSIGNED) DESC LIMIT 1");
        if (resultSet.next()){
            return resultSet.getString(1);
        }
        return null;
    }

    @Override
    public String getRoomName(String id) throws SQLException, ClassNotFoundException {
        ResultSet resultSet =  CrudUtil.execute("SELECT name FROM room WHERE property_id=?",id);
        String name = null;
        while (resultSet.next()){
            name = resultSet.getString(1);
        }
        return name;
    }
}
