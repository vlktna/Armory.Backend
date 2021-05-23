package blur.tech.armory.dto.service;

import blur.tech.armory.controller.models.update.MeetingRoomUpdate;
import blur.tech.armory.dto.entity.MeetingRoomEntity;
import blur.tech.armory.dto.repository.BookingRepository;
import blur.tech.armory.dto.repository.MeetingRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class MeetingRoomService {
    private final MeetingRoomRepository meetingRoomRepository;
    private final BookingRepository bookingRepository;

    @Autowired
    public MeetingRoomService(MeetingRoomRepository meetingRoomRepository, BookingRepository bookingRepository) {
        this.meetingRoomRepository = meetingRoomRepository;
        this.bookingRepository = bookingRepository;
    }

    @Transactional
    public void save(MeetingRoomEntity meetingRoom) {
        meetingRoomRepository.save(meetingRoom);
    }

    public MeetingRoomEntity findById(Integer id) {
        return meetingRoomRepository.findById(id);
    }

    public List<MeetingRoomEntity> findMeetingRoom() {
        return meetingRoomRepository.findAll();
    }

    public MeetingRoomEntity updateMeetingRoom(MeetingRoomUpdate meetingRoom){
        if (meetingRoom.getFlor() != null){
            meetingRoomRepository.updateFlor(meetingRoom.getId(), meetingRoom.getFlor());
        }
        if (meetingRoom.getType() != null){
            meetingRoomRepository.updateType(meetingRoom.getId(), meetingRoom.getType());
        }
        if (meetingRoom.getSquare() != null){
            meetingRoomRepository.updateSquare(meetingRoom.getId(), meetingRoom.getSquare());
        }
        if (meetingRoom.getVideoconferencing() != null){
            meetingRoomRepository.updateVideoconferencing(meetingRoom.getId(), meetingRoom.getVideoconferencing());
        }
        if (meetingRoom.getMicrophones() != null){
            meetingRoomRepository.updateMicrophones(meetingRoom.getId(), meetingRoom.getMicrophones());
        }
        if (meetingRoom.getWifi() != null){
            meetingRoomRepository.updateWifi(meetingRoom.getId(), meetingRoom.getWifi());
        }
        if (meetingRoom.getLed() != null){
            meetingRoomRepository.updateLed(meetingRoom.getId(), meetingRoom.getLed());
        }
        if (meetingRoom.getMicrophones() != null){
            meetingRoomRepository.updateMicrophones(meetingRoom.getId(), meetingRoom.getMicrophones());
        }
        if (meetingRoom.getSeatingCapacity() != null){
            meetingRoomRepository.updateSeatingCapacity(meetingRoom.getId(), meetingRoom.getSeatingCapacity());
        }
        return meetingRoomRepository.findById(meetingRoom.getId());
    }
}
