package blur.tech.armory.controller.models.update;

import blur.tech.armory.controller.models.MeetingRoom;
import blur.tech.armory.controller.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class BookingUpdate {
    private Integer id;
    private String name;
    private Date startTime;
    private Date endTime;
    private MeetingRoom meetingRoomBooking;
    private User owner;
    private List<User> userList;
}
