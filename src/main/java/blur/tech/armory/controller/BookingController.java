package blur.tech.armory.controller;

import blur.tech.armory.controller.models.Booking;
import blur.tech.armory.controller.models.BookingShort;
import blur.tech.armory.controller.models.MeetingRoom;
import blur.tech.armory.controller.models.User;
import blur.tech.armory.controller.models.request.BookingRequest;
import blur.tech.armory.controller.models.response.BookingResponse;
import blur.tech.armory.dto.entity.BookingEntity;
import blur.tech.armory.dto.entity.MeetingRoomEntity;
import blur.tech.armory.dto.entity.UserEntity;
import blur.tech.armory.dto.service.BookingService;
import blur.tech.armory.dto.service.MailSender;
import blur.tech.armory.dto.service.MeetingRoomService;
import blur.tech.armory.dto.service.UserService;
import blur.tech.armory.security.Token;
import com.sun.mail.smtp.SMTPSendFailedException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.time.Instant;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/booking")
@CrossOrigin(origins = {"*"}, allowedHeaders = {"*"})
public class BookingController {

    private final BookingService bookingService;
    private final UserService userService;
    private final MeetingRoomService meetingRoomService;
    private final Token tokenUtil;
    private MailSender mailSender;
    private GoogleController googleController;

    @PostMapping("/add")
    public RedirectView addRoom(@RequestHeader Map<String, String> headers,
                                @RequestBody BookingRequest bookingRequest) throws Exception {
        String token = headers.get("authorization");

        if (token != null) {
            BookingResponse bookingResponse = createBooking(bookingRequest, token);
            googleController.setBookingResponse(bookingResponse);
            return new RedirectView(googleController.authorize());
        }
        return null;
    }

    @PostMapping("/add-mobile")
    public ResponseEntity<BookingResponse> addRoomMobile(@RequestHeader Map<String, String> headers,
                                                         @RequestParam(value = "code") String code,
                                                         @RequestBody BookingRequest bookingRequest) throws Exception {
        String token = headers.get("authorization");
        BookingResponse bookingResponse = null;
        if (token != null) {
            bookingResponse = createBooking(bookingRequest, token);
            googleController.setBookingResponse(bookingResponse);

            if (code != null && !code.equals("")) {
                googleController.addEventToCalendarWithAuthToken(code);
            }
        }
        return new ResponseEntity(bookingResponse, HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping("/get-all-short")
    private ResponseEntity<List<BookingShort>> getAllShortForRoomByDate(
            @RequestParam Integer roomId,
            @RequestParam Integer year,
            @RequestParam Integer month,
            @RequestParam Integer day
    ) {

        List<BookingEntity> bookingEntityList = bookingService.find(roomId, year, month, day);
        List<BookingShort> bookingShortList = new LinkedList<>();
        for (BookingEntity bookingEntity : bookingEntityList) {
            bookingShortList.add(new BookingShort(
                    bookingEntity.getId(),
                    bookingEntity.getName(),
                    bookingEntity.getStartTime(),
                    bookingEntity.getEndTime()
            ));
        }

        return new ResponseEntity(bookingShortList, HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping("/get-mine")
    private ResponseEntity<List<Booking>> getMine(@RequestHeader Map<String, String> headers) {
        Date now = Date.from(Instant.now());
        String token = headers.get("authorization");

        List<BookingEntity> bookingEntityList = bookingService.getMineById(tokenUtil.getId(token));
        List<Booking> bookingList = new LinkedList<>();
        for (BookingEntity bookingEntity : bookingEntityList) {

            bookingList.add(new Booking(
                            bookingEntity.getId(),
                            bookingEntity.getName(),
                            bookingEntity.getStartTime(),
                            bookingEntity.getEndTime(),

                            new MeetingRoom(
                                    bookingEntity.getMeetingRoomBooking().getId(),
                                    bookingEntity.getMeetingRoomBooking().getFlor(),
                                    bookingEntity.getMeetingRoomBooking().getType(),
                                    bookingEntity.getMeetingRoomBooking().getSquare(),
                                    bookingEntity.getMeetingRoomBooking().getVideoconferencing(),
                                    bookingEntity.getMeetingRoomBooking().getMicrophones(),
                                    bookingEntity.getMeetingRoomBooking().getWifi(),
                                    bookingEntity.getMeetingRoomBooking().getLed(),
                                    bookingEntity.getMeetingRoomBooking().getSeatingCapacity(),
                                    new User(
                                            bookingEntity.getMeetingRoomBooking().getAdmin().getId(),
                                            bookingEntity.getMeetingRoomBooking().getAdmin().getFirstName(),
                                            bookingEntity.getMeetingRoomBooking().getAdmin().getLastName(),
                                            bookingEntity.getMeetingRoomBooking().getAdmin().getEmail(),
                                            bookingEntity.getMeetingRoomBooking().getAdmin().getRole()
                                    ),
                                    bookingEntity.getMeetingRoomBooking().getBookings().stream()
                                            .map(it -> new BookingShort(
                                                    it.getId(),
                                                    it.getName(),
                                                    it.getStartTime(),
                                                    it.getEndTime()))
                                            .collect(Collectors.toList())
                            )
                    )
            );

        }
        return new ResponseEntity<>(bookingList, HttpStatus.OK);
    }

    private BookingResponse createBooking(BookingRequest bookingRequest, String token) throws Exception {
        UserEntity newUser = this.userService.findByEmail(this.tokenUtil.extractUsername(token));
        BookingEntity newBooking = new BookingEntity();
        Date start = bookingRequest.getStartTime();
        Date end = bookingRequest.getEndTime();

        if (meetingRoomService.findById(bookingRequest.getMeetingRoomID()) == null) {
            throw new Exception("not available");
        }

        List<BookingEntity> oldBookings =
                bookingService.findAllByMeetingRoomBookingId(bookingRequest.getMeetingRoomID());

        for (BookingEntity booking : oldBookings) {
            if (booking.getStartTime().before(start) && booking.getEndTime().after(end)
                    || booking.getStartTime().after(start) && booking.getEndTime().before(end)
                    || booking.getStartTime().after(start) && booking.getStartTime().before(end)
                    || booking.getEndTime().after(start) && booking.getEndTime().before(end)
                    || booking.getEndTime() == end && booking.getStartTime() == start) {
                throw new Exception("not available");
            }
        }

        MeetingRoomEntity meetingRoomEntity = meetingRoomService.findById(bookingRequest.getMeetingRoomID());
        newBooking.setStartTime(bookingRequest.getStartTime());
        newBooking.setEndTime(bookingRequest.getEndTime());
        newBooking.setOwner(newUser);
        newBooking.setName(bookingRequest.getName());
        newBooking.setUserList(userService.findUserListByEmail(bookingRequest.getUserEmailList()));
        newBooking.setMeetingRoomBooking(meetingRoomEntity);
        newBooking.setName(bookingRequest.getName());
        bookingService.save(newBooking);

        try {
            mailSender.sendEmail(newBooking.getOwner().getEmail(), "Новое событие",
                    "Здравсвуйте!\n" + "Вы являетесь организатором нового события.\n\n" +
                            "Место: переговорная комната № " + newBooking.getMeetingRoomBooking().getId() +
                            "\nНачало: " + newBooking.getStartTime() +
                            "\nКонец: " + newBooking.getEndTime());

        } catch (SMTPSendFailedException e) {
            System.err.println("spam");
        }

        try {
            for (UserEntity user : newBooking.getUserList()) {
                String text = "Здравствуйте!\n" + "Вы были добавлены в список участников нового события.\n\n" +
                        "Место: переговорная комната № " + newBooking.getMeetingRoomBooking().getId() +
                        "\nНачало: " + newBooking.getStartTime() +
                        "\nКонец: " + newBooking.getEndTime();
                mailSender.sendEmail(user.getEmail(), "Новое событие", text);
            }

        } catch (SMTPSendFailedException e) {
            System.err.println("spam");
        }

        return new BookingResponse(
                newBooking.getId(),
                bookingRequest.getName(),
                start,
                end,
                bookingRequest.getMeetingRoomID(),
                bookingRequest.getUserEmailList()
        );

    }

}
