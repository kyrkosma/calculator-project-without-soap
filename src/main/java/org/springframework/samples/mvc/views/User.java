package org.springframework.samples.mvc.views;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "USERS")
public class User {

    @Id
    @SequenceGenerator(name = "GEN_USERS", sequenceName = "USER_ID_GENERATOR", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEN_USERS")
    private int id;

    @Column(name = "USERNAME", unique = true, nullable = false)
    @Size(min = 5, max = 15)
    private String username;

    @Column(name = "PASSWORD", nullable = false)
    @Size(min = 5, max = 20)
    private String password;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "HMEROMHNIA")
    private Date HMEROMHNIA;

    @Column(name = "IS_ACTIVE")
    @Min(0) @Max(1)
    private int isActive;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ROLE_ID", referencedColumnName = "id")
    private Role role;

    public int getId() {return id;}

    public void setId(int id) {this.id = id;}

    public String getUsername() {return username;}

    public void setUsername(String username) {this.username = username;}

    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}

    public Date getHMEROMHNIA() {return HMEROMHNIA;}

    public void setHMEROMHNIA(Date HMEROMHNIA) {this.HMEROMHNIA = HMEROMHNIA;}

    public int getIsActive() {return isActive;}

    public void setIsActive(int isActive) {this.isActive = isActive;}

    public Role getRole() {return role;}

    public void setRole(Role role) {this.role = role;}
}
