package blur.tech.armory.dto.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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
