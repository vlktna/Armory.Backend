package blur.tech.armory.dto.repository;

import blur.tech.armory.dto.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, Long> {

    @Query(value = "SELECT b.*\n" +
            "FROM bookings b\n" +
            "         LEFT JOIN user_booking ub on b.id = ub.booking_id\n" +
            "         LEFT JOIN users u on u.id = ub.user_id\n" +
            "WHERE b.owner_id\n" +
            "    = ?1\n" +
            "   OR u.id = ?1", nativeQuery = true)
    List<BookingEntity> findManyByOwnerAndParticipation(Integer id);

    BookingEntity findById(Integer id);

    List<BookingEntity> findAllByMeetingRoomBookingId(Integer id);

    @Transactional
    @Modifying
    @Query("update BookingEntity b set b.name = :name where b.id = :id")
    void updateName(Integer id, String name);

    @Transactional
    @Modifying
    @Query("update BookingEntity b set b.startTime = :startTime where b.id = :id")
    void updateStartTime(Integer id, Date startTime);

    @Transactional
    @Modifying
    @Query("update BookingEntity b set b.endTime = :endTime where b.id = :id")
    void updateEndTime(Integer id, Date endTime);


    @Query(value = "SELECT * FROM bookings WHERE date(start_time) = date(?2) AND room_id = ?1", nativeQuery = true)
    List<BookingEntity> findManyBookingsByNickNameAndDate(Integer roomId, String date);
}
