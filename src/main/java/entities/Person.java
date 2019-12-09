package entities;

import javax.persistence.*;
import javax.websocket.Session;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Sebastian S.
 * */

@Entity
public class Person {
    @Id
    private String username = "";
    private String password = "";

    @Transient
    private Session session;

    @OneToMany(cascade = CascadeType.PERSIST)
    private List<Group> groups = new LinkedList<>();

    public Person() {
    }

    public Person(String username, String password, Session session) {
        this.username = username;
        this.password = password;
        this.session = session;
    }

    public Person(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Person) {
            if (!this.password.isEmpty()) {
                return ((Person) obj).username.equalsIgnoreCase(this.username)
                        && ((Person) obj).password.equals(this.password);
            } else {
                return ((Person) obj).username.equalsIgnoreCase(this.username);
            }
        }
        return false;
    }
}
