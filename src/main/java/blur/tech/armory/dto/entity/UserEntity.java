package blur.tech.armory.dto.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@Entity
@Data
public class UserEntity {

    @Id
    @Column
    @GeneratedValue
    private Integer id;

    @Column
    private String email;

    @Column
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @JsonBackReference
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "admin")
    private List<MeetingRoomEntity> meetingRoomAdmin;

    @JsonBackReference
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_booking",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "booking_id"))
    private List<BookingEntity> memberOfBookings;

    @JsonBackReference
    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    private List<BookingEntity> ownerOfBookings;

}
