package org.springframework.samples.mvc.views;

import javax.persistence.*;

@Entity
@Table(name = "ROLE")
public class Role {

    @Id
    @SequenceGenerator(name = "GEN_ROLES", sequenceName = "ROLE_ID_GENERATOR", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEN_ROLES")
    private int id;

    @Column(name = "NAME")
    private String name;

    /*@OneToOne(mappedBy = "role")
    private User user;
*/
    public Role() {}

    public Role(int id) {this.id = id;}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    /*public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }*/
}
