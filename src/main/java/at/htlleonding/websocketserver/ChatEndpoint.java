package at.htlleonding.websocketserver;

import entities.Group;
import entities.Message;
import org.json.JSONObject;
import repository.Repository;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

/**
 * @author Sebastian S.
 * */

@ServerEndpoint("/whatsapp")
public class ChatEndpoint {


    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Client connected");

    }

    @OnMessage
    public void onMessage(Session session, String message) {
        try{
            JSONObject req = new JSONObject(message);
            if(req.has("typ")){
                switch (req.getString("typ")){
                    // {"typ": "login", "username": "basti", "password": "123"}
                    case "login":
                        Repository.getInstance().login(session, req.getString("username"), req.getString("password"));
                        break;
                    // {"typ": "logout", "username": "basti"}
                    case "logout":
                        Repository.getInstance().logout(req.getString("username"));
                        break;
                    // {"typ": "addGroup", "group": "Schule"}
                    case "addGroup":
                        Repository.getInstance().addGroup(session, req.getString("group"));
                        break;
                    // {"typ": "joinGroup", "group": "Schule"}
                    case "joinGroup":
                        Repository.getInstance().joinGroup(session, req.getString("group"));
                        break;
                    // {"typ": "data", "group": "Schule", "payload": "Hallo"}
                    case "data":
                        Message msg = new Message();
                        msg.setMessage(req.getString("payload"));
                        msg.setReceiver(new Group(req.getString("group")));
                        Repository.getInstance().processMessage(session, msg);
                        break;
                    // {"typ":"groups"}
                    case "groups":
                        Repository.getInstance().getGroups(session);
                        break;
                    default:
                        System.out.println("Type: " + req.getString("typ") + " not supported!");
                        break;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Whoops error :(");
        }
    }

    @OnClose
    public void onClose(Session session) {

    }

    @OnError
    public void onError(Throwable t) {
        t.printStackTrace();
    }
}