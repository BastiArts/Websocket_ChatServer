package repository;

import entities.Group;
import entities.Message;
import entities.Person;
import org.json.JSONObject;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.websocket.Session;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Sebastian S.
 * */

public class Repository {
    private static final String persistenceUnit = "WhatsApp_PU";

    private List<Person> users = new LinkedList<>();
    private List<Group> groups = new LinkedList<>();
    private EntityManager em = Persistence.createEntityManagerFactory(Repository.persistenceUnit).createEntityManager();

    private static Repository instance = null;

    private Repository() {

    }
    /**
     * Singleton, to keep it Thread-Safe ;)
     * */
    public static Repository getInstance() {
        if (instance == null) {
            instance = new Repository();
        }
        return instance;
    }

    /**
     * @param session  - Current session of the user
     * @param username - Username
     * @param password - Password
     *                 Used to login the user
     */
    public void login(Session session, String username, String password) {
        em.getTransaction().begin();

        Person user = em.find(Person.class, username);
        if (user == null) {
            user = registerUser(username, password);
        } else {
            System.out.println("User: " + username + " logged in!");
        }
        em.getTransaction().commit();

        user.setSession(session);
        if (!users.contains(user)) {
            users.add(user);
        }
    }

    /**
     * @param username - Username
     * @param password - Password
     *                 Used to register a not existing User
     */
    private Person registerUser(String username, String password) {
        Person user = new Person(username, password);
        em.persist(user);
        System.out.println("User registered!");
        return user;
    }

    /**
     * @param username - Username
     *                 Used to logout a User
     */
    public void logout(String username) {
        users.removeIf(u -> u.getUsername().equals(username));
        System.out.println("User " + username + " logged out!");
    }

    /**
     * @param groupName - GroupName
     *                  Creates a group
     */
    public void addGroup(Session session, String groupName) {
        em.getTransaction().begin();
        /* .getSingleResult throws an Exception if no Element is found! */
        List<Group> gList = em.createQuery("select g from WS_Group g where g.groupName = '" + groupName + "'", Group.class).getResultList();
        Group g = null;
        if (gList.size() > 0) {
            g = gList.get(0);
            System.out.println("Group " + groupName + " already exists!");
        } else {
            g = new Group(groupName);
            em.persist(g);
            System.out.println("Group " + groupName + " added.");
        }
        if (!this.groups.contains(g)) {
            this.groups.add(new Group(groupName));
        }
        em.getTransaction().commit();

        joinGroup(session, groupName);
    }

    /**
     * @param session   - Session
     * @param groupName - Group
     *                  Used to join a group
     */
    public void joinGroup(Session session, String groupName) {
        Group g = new Group(groupName);
        Person user = findUserBySession(session);
        if (!user.getGroups().contains(g)) {
            user.getGroups().add(g);
            System.out.println(user.getUsername() + " joined the group: " + groupName);
            return;
        }
        System.out.println(user.getUsername() + " is already a Member of group: " + groupName);
    }

    /**
     * @param session - Session
     * @param message - Message
     *                Send Message
     */
    public void processMessage(Session session, Message message) {

        message.setSender(findUserBySession(session));

        JSONObject returnString = new JSONObject()
                .put("typ", "data")
                .put("sender", message.getSender().getUsername())
                .put("group", message.getReceiver().getGroupName())
                .put("payload", message.getMessage());
        for (Person u : users) {
            if (u.getGroups().contains(message.getReceiver())) {
                u.getSession().getAsyncRemote().sendText(returnString.toString());
            }
        }
        System.out.println("[CHAT] Group: " + message.getReceiver().getGroupName()
                + " User: " + message.getSender().getUsername()
                + " Message: " + message.getMessage());
    }

    /**
     * @param session - Session
     *                Returns a List of groups
     */
    public void getGroups(Session session) {
        Person user = findUserBySession(session);
        JSONObject response = new JSONObject();
        response.put("typ", "groups").put("value", user.getGroups()
                .stream()
                .map(Group::getGroupName)
                .collect(Collectors.toList()));
        session.getAsyncRemote().sendText(response.toString());
    }

    /**
     * @param session - Session
     *                Searches the List
     */
    private Person findUserBySession(Session session) {
        if (users.stream().anyMatch(u -> u.getSession().equals(session))) {
            return users.stream().filter(u -> u.getSession().equals(session)).findFirst().get();
        }
        Person p = new Person();
        p.setSession(session);
        return p;
    }
}
