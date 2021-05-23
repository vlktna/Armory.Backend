package blur.tech.armory.dto.service;

import blur.tech.armory.dto.entity.BookingEntity;
import blur.tech.armory.dto.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Transactional
    public void save(BookingEntity booking) {
        bookingRepository.save(booking);
    }

    public List<BookingEntity> findAllByMeetingRoomBookingId(Integer id) {
        return bookingRepository.findAllByMeetingRoomBookingId(id);
    }

    public List<BookingEntity> getMineById(Integer id) {
        return bookingRepository.findManyByOwnerAndParticipation(id);
    }

    public List<BookingEntity> findAll() {
        return bookingRepository.findAll();
    }

    public List<BookingEntity> find(
            Integer roomId,
            Integer year,
            Integer month,
            Integer day
    ) {
        return bookingRepository.findManyBookingsByNickNameAndDate(roomId, "" + year + "-" + month + "-" + day);
    }

}
