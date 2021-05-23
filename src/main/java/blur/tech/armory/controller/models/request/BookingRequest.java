package blur.tech.armory.controller.models.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class BookingRequest {
    private String name;
    private Date startTime;
    private Date endTime;
    private Integer meetingRoomID;
    private List<String> userEmailList;
}
