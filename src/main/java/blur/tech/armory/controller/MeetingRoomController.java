package blur.tech.armory.controller;

import blur.tech.armory.controller.models.BookingShort;
import blur.tech.armory.controller.models.MeetingRoom;
import blur.tech.armory.controller.models.User;
import blur.tech.armory.controller.models.update.MeetingRoomUpdate;
import blur.tech.armory.dto.entity.MeetingRoomEntity;
import blur.tech.armory.dto.entity.UserEntity;
import blur.tech.armory.dto.entity.UserRole;
import blur.tech.armory.dto.service.MeetingRoomService;
import blur.tech.armory.dto.service.UserService;
import blur.tech.armory.security.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@CrossOrigin(origins = {"*"}, allowedHeaders = {"*"})
@RestController
@RequestMapping("/meeting-room")
public class MeetingRoomController {

    private final MeetingRoomService meetingRoomService;
    private final UserService userService;
    private final Token tokenUtil;

    @PostMapping("/update")
    public ResponseEntity<MeetingRoomUpdate> saveRoom(@RequestHeader Map<String, String> headers,
                                                      @RequestBody MeetingRoomUpdate meetingRoom) {
        String token = headers.get("authorization");
        UserEntity user = userService.findByEmail(tokenUtil.extractUsername(token));

        if (user.getRole() != UserRole.admin) {
            return new ResponseEntity(null, HttpStatus.CONFLICT);
        }

        MeetingRoomEntity meetingRoomEntity = meetingRoomService.updateMeetingRoom(meetingRoom);

        return new ResponseEntity(new MeetingRoomUpdate(
                meetingRoomEntity.getId(),
                meetingRoomEntity.getFlor(),
                meetingRoomEntity.getType(),
                meetingRoomEntity.getSquare(),
                meetingRoomEntity.getVideoconferencing(),
                meetingRoomEntity.getMicrophones(),
                meetingRoomEntity.getWifi(),
                meetingRoomEntity.getLed(),
                meetingRoomEntity.getSeatingCapacity()

        ), HttpStatus.OK);
    }

    @GetMapping("/get-all")
    @ResponseBody
    public ResponseEntity<List<MeetingRoom>> allRooms() {
        Date now = Date.from(Instant.now());
        List<MeetingRoomEntity> meetingRoomEntityList = meetingRoomService.findMeetingRoom();
        List<MeetingRoom> meetingRoomList = new LinkedList<>();

        for (MeetingRoomEntity meetingRoomEntity : meetingRoomEntityList) {
            MeetingRoom meetingRoom = new MeetingRoom();
            meetingRoom.setId(meetingRoomEntity.getId());
            meetingRoom.setFlor(meetingRoomEntity.getFlor());
            meetingRoom.setType(meetingRoomEntity.getType());
            meetingRoom.setSquare(meetingRoomEntity.getSquare());
            meetingRoom.setVideoconferencing(meetingRoomEntity.getVideoconferencing());
            meetingRoom.setWifi(meetingRoomEntity.getWifi());
            meetingRoom.setLed(meetingRoomEntity.getLed());
            meetingRoom.setSeatingCapacity(meetingRoomEntity.getSeatingCapacity());
            meetingRoom.setFlor(meetingRoomEntity.getFlor());

            meetingRoom.setAdmin(new User(
                    meetingRoomEntity.getAdmin().getId(),
                    meetingRoomEntity.getAdmin().getFirstName(),
                    meetingRoomEntity.getAdmin().getLastName(),
                    meetingRoomEntity.getAdmin().getEmail(),
                    meetingRoomEntity.getAdmin().getRole()
            ));
            List<BookingShort> bookingShortList = meetingRoomEntity.getBookings().stream()
                    .filter(it -> it.getEndTime().after(now))
                    .map(it -> new BookingShort(it.getId(), it.getName(), it.getStartTime(), it.getEndTime()))
                    .collect(Collectors.toList());

            meetingRoom.setBookingList(bookingShortList);

            meetingRoomList.add(meetingRoom);
        }

        return new ResponseEntity(meetingRoomList, HttpStatus.OK);
    }

}
