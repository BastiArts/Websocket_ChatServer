package entities;

import javax.persistence.*;

/**
 * @author Sebastian S.
 * */

@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id = 0L;

    @OneToOne
    private Person sender = new Person();

    @OneToOne
    private Group receiver = new Group();
    private String message = "";

    public Message() {
    }

    public Message(Person sender, Group receiver, String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Person getSender() {
        return sender;
    }

    public void setSender(Person sender) {
        this.sender = sender;
    }

    public Group getReceiver() {
        return receiver;
    }

    public void setReceiver(Group receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
