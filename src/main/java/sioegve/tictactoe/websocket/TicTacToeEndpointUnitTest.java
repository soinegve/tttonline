package sioegve.tictactoe.websocket;

import org.junit.jupiter.api.Test;

import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;


import static org.mockito.Mockito.*;

class TicTacToeEndpointUnitTest {


    @Test
    public void test()
    {


// mock creation
        Session session = mock(Session.class);
        Session session2 = mock(Session.class);

        RemoteEndpoint.Basic basic1 =  new MyBasic();
        RemoteEndpoint.Basic basic2 = new MyBasic();

        when(session.getBasicRemote()).thenReturn(basic1);
        when(session2.getBasicRemote()).thenReturn(basic2);


        new TicTacToeEndpoint().onOpen(session,"karolos");
        System.out.println(((MyBasic)basic1).getSentObject().toString());
        new TicTacToeEndpoint().onOpen(session2,"Elena");
        System.out.println(((MyBasic)basic1).getSentObject().toString());
        System.out.println(((MyBasic)basic2).getSentObject().toString());


        new TicTacToeEndpoint().onOpen(session2,"Elena");
        System.out.println(((MyBasic)basic1).getSentObject().toString());
        System.out.println(((MyBasic)basic2).getSentObject().toString());
    }

}