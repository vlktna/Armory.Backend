package blur.tech.armory.controller.models.update;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MeetingRoomUpdate {
    private int id;
    private Integer flor;
    private String type;
    private Double square;
    private Boolean videoconferencing;
    private Boolean microphones;
    private Boolean wifi;
    private Boolean led;
    private Integer seatingCapacity;
}