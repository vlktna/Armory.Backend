package blur.tech.armory.dto.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "meeting_rooms")
public class MeetingRoomEntity {

    @Id
    @Column
    @GeneratedValue
    private Integer id;

    @Column()
    private Integer flor;

    @Column()
    private String type;

    @Column()
    private Double square;

    @Column()
    private Boolean videoconferencing;

    @Column()
    private Boolean microphones;

    @Column()
    private Boolean wifi;

    @Column()
    private Boolean led;

    @Column(name = "seating_capacity")
    private Integer seatingCapacity;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity admin;

    @OneToMany(mappedBy = "meetingRoomBooking", fetch = FetchType.LAZY)
    private List<BookingEntity> bookings;
}
