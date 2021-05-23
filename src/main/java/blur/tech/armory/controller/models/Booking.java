package blur.tech.armory.controller.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class Booking {
    private Integer id;
    private String name;
    private Date startTime;
    private Date endTime;
    private MeetingRoom meetingRoomBooking;
}
