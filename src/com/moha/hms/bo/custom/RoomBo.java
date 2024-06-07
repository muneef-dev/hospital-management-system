package com.moha.hms.bo.custom;

import com.moha.hms.bo.SuperBo;
import com.moha.hms.dto.RoomDto;

import java.sql.SQLException;
import java.util.List;

public interface RoomBo extends SuperBo {
    boolean createRoom(RoomDto roomDto) throws SQLException, ClassNotFoundException;
    List<RoomDto> searchRoom(String id) throws SQLException, ClassNotFoundException;
    boolean deleteRoom(String id) throws SQLException, ClassNotFoundException;
    boolean updateRoom(RoomDto roomDto) throws SQLException, ClassNotFoundException;
    List<RoomDto> loadAllRooms() throws SQLException, ClassNotFoundException;
    String getLastRoomId() throws SQLException, ClassNotFoundException;
    String getRoomName(String id) throws SQLException, ClassNotFoundException;
}
