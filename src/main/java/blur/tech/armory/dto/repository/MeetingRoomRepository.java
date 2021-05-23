package blur.tech.armory.dto.repository;

import blur.tech.armory.dto.entity.MeetingRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface MeetingRoomRepository extends JpaRepository<MeetingRoomEntity, Long> {
    List<MeetingRoomEntity> findAllByType(String type);

    MeetingRoomEntity findById(Integer id);

    @Transactional
    @Modifying
    @Query("update MeetingRoomEntity mr set mr.flor = :flor where mr.id = :id")
    void updateFlor(Integer id, Integer flor);

    @Transactional
    @Modifying
    @Query("update MeetingRoomEntity mr set mr.type = :type where mr.id = :id")
    void updateType(Integer id, String type);

    @Transactional
    @Modifying
    @Query("update MeetingRoomEntity mr set mr.square = :square where mr.id = :id")
    void updateSquare(Integer id, Double square);

    @Transactional
    @Modifying
    @Query("update MeetingRoomEntity mr set mr.videoconferencing = :videoconferencing where mr.id = :id")
    void updateVideoconferencing(Integer id, Boolean videoconferencing);

    @Transactional
    @Modifying
    @Query("update MeetingRoomEntity mr set mr.microphones = :microphones where mr.id = :id")
    void updateMicrophones(Integer id, Boolean microphones);

    @Transactional
    @Modifying
    @Query("update MeetingRoomEntity mr set mr.wifi = :wifi where mr.id = :id")
    void updateWifi(Integer id, Boolean wifi);

    @Transactional
    @Modifying
    @Query("update MeetingRoomEntity mr set mr.led = :led where mr.id = :id")
    void updateLed(Integer id, Boolean led);

    @Transactional
    @Modifying
    @Query("update MeetingRoomEntity mr set mr.seatingCapacity = :seatingCapacity where mr.id = :id")
    void updateSeatingCapacity(Integer id, Integer seatingCapacity);

}
