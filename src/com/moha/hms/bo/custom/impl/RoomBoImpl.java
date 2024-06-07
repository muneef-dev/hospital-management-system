package com.moha.hms.bo.custom.impl;

import com.moha.hms.bo.custom.RoomBo;
import com.moha.hms.dao.DaoFactory;
import com.moha.hms.dao.custom.RoomDao;
import com.moha.hms.dto.RoomDto;
import com.moha.hms.entity.Room;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoomBoImpl implements RoomBo {

    RoomDao roomDao = DaoFactory.getInstance().getDao(DaoFactory.DaoType.ROOM);

    @Override
    public boolean createRoom(RoomDto roomDto) throws SQLException, ClassNotFoundException {
        return roomDao.create(new Room(
                roomDto.getId(), roomDto.getName(), roomDto.getBedCount(),
                roomDto.getAvailability(),roomDto.getDepartmentName()
        ));
    }

    @Override
    public List<RoomDto> searchRoom(String id) throws SQLException, ClassNotFoundException {
        List<RoomDto> roomDtoList = new ArrayList<>();
        for (Room room: roomDao.search(id)
             ) {
            roomDtoList.add(new RoomDto(
                    room.getId(), room.getName(), room.getBedCount(),
                    room.getAvailability(),room.getDepartmentName()
            ));
        }
        return roomDtoList;
    }

    @Override
    public boolean deleteRoom(String id) throws SQLException, ClassNotFoundException {
        return roomDao.delete(id);
    }

    @Override
    public boolean updateRoom(RoomDto roomDto) throws SQLException, ClassNotFoundException {
        return roomDao.update(new Room(
                roomDto.getId(), roomDto.getName(), roomDto.getBedCount(),
                roomDto.getAvailability(),roomDto.getDepartmentName()
        ));
    }

    @Override
    public List<RoomDto> loadAllRooms() throws SQLException, ClassNotFoundException {
        List<RoomDto> roomDtoList = new ArrayList<>();
        for (Room room : roomDao.loadAll()
             ) {
            roomDtoList.add(new RoomDto(
                    room.getId(), room.getName(), room.getBedCount(),
                    room.getAvailability(),room.getDepartmentName()
            ));
        }
        return roomDtoList;
    }

    @Override
    public String getLastRoomId() throws SQLException, ClassNotFoundException {
        return roomDao.getLastRoomId();
    }

    @Override
    public String getRoomName(String id) throws SQLException, ClassNotFoundException {
        return roomDao.getRoomName(id);
    }
}
