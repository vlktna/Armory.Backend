package blur.tech.armory.controller.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeetingRoom {
    private Integer id; 
    private Integer flor; 
    private String type;
    private Double square;
    private Boolean videoconferencing; 
    private Boolean microphones; 
    private Boolean wifi; 
    private Boolean led;
    private Integer seatingCapacity;
    private User admin;
    private List<BookingShort> bookingList;

}
