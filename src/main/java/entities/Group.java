package entities;

import javax.persistence.*;

/**
 * @author Sebastian S.
 * */

@Entity(name = "WS_Group")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id = 0L;
    private String groupName = "";

    public Group() {
    }

    public Group(String groupName) {
        this.groupName = groupName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Group && ((Group) obj).groupName.equalsIgnoreCase(groupName);
    }
}
