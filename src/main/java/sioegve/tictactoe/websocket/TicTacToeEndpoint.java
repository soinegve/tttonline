package sioegve.tictactoe.websocket;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import sioegve.tictactoe.model.Message;
import sioegve.tictactoe.model.TicTacToe;

@ServerEndpoint(value = "/tictactoe/{username}", decoders = MessageDecoder.class, encoders = MessageEncoder.class)
public class TicTacToeEndpoint {
    private Session session;

    private static HashMap<String, TicTacToeEndpoint> users = new HashMap<>();
    private static final Set<String> usersWithNoPlayers = new CopyOnWriteArraySet<>();
    private static HashMap<String, String> opponents = new HashMap<>();
    private String userName1 ;
    private static HashMap<String, TicTacToe> games = new HashMap<>();



    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username)  {

        userName1 = username;
        this.session = session;
        users.put(username, this);

        System.out.println(Thread.currentThread().getId());
        Message message = new Message();
        message.setFrom(username);
        String player_message;

        if ( usersWithNoPlayers.isEmpty()) {
            usersWithNoPlayers.add(username);
            player_message = "We are waiting for an opponent to connect...";
        }
        else {
            String opponent = usersWithNoPlayers.iterator().next();

            opponents.put(username,opponent);
            opponents.put(opponent,username);

            usersWithNoPlayers.remove(opponent);

            player_message = "YouAre#O#X";

            String combinedKey = getGameKey(opponent,userName1);
            games.put(combinedKey,new TicTacToe());

            Message messageC = new Message();
            messageC.setContent("YouAre#X#O");
            try {
                users.get(opponent).session.getBasicRemote().sendObject(messageC);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (EncodeException e) {
                e.printStackTrace();
            }

        }

        message.setContent(player_message);


        try {
            this.session.getBasicRemote().sendObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (EncodeException e) {
            e.printStackTrace();
        }

        //broadcast(message);
    }

    @OnMessage
    public void onMessage(Session session, Message message)  {

        String x = message.getContent();
        if(x.contains("NextMove"))
        {
            String moves = x.split("#")[1];
            int row_index = Integer.parseInt(moves.split("_")[0]);
            int column_index = Integer.parseInt(moves.split("_")[1]);

            String opponent = opponents.get(userName1);

            String key = getGameKey(opponent,userName1);
            TicTacToe currentGame = games.get(key);

            currentGame.next_move(row_index,column_index);


            Message mesg = new Message();
            mesg.setContent("NextMove#"+row_index+"#"+column_index);

            try {
                users.get(opponent).session.getBasicRemote().sendObject(mesg);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (EncodeException e) {
                e.printStackTrace();
            }

            if(currentGame.game_over())
            {
                if(currentGame.getWinner()!=null)
                {
                    try {
                    mesg.setContent("GameOver#You won!");
                    session.getBasicRemote().sendObject(mesg);

                    mesg.setContent("GameOver#You lost:(");
                    users.get(opponent).session.getBasicRemote().sendObject(mesg);

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (EncodeException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    mesg.setContent("GameOver#There was a tie!");

                    try {
                        users.get(opponent).session.getBasicRemote().sendObject(mesg);
                        session.getBasicRemote().sendObject(mesg);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (EncodeException e) {
                        e.printStackTrace();
                    }
                }
            }


        }

    }

    private String getGameKey(String one, String two) {
        return one.compareTo(two)>0 ? one+"_"+two : two+"_"+one;
    }

    @OnClose
    public void onClose(Session session)  {

        users.remove(this.userName1);


        String opponent = opponents.get(this.userName1);
        String m = this.userName1 + " left abruptly - you won !!";
        Message message = new Message();
        message.setContent(m);

        try {
            users.get(opponent).session.getBasicRemote().sendObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (EncodeException e) {
            e.printStackTrace();
        }



    }

    @OnError
    public void onError(Session session, Throwable throwable) {

    }



}
