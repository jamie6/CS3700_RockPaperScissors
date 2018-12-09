import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author Jamie
 */
public class Player
{
    private int currentGame = 0;
    private int games, sGames, portNo, recipientPortNo1, recipientPortNo2, points = 0;
    private String playerID, inetAddr;
    private int move;

    public static void main(String[] args)
    {
        Player p = new Player();
        p.initialize(args);
        p.run();
    }

    private void initialize(String[] args)
    {
        games = Integer.parseInt(args[0]); // the number of games to play
        sGames = games; // for server
        playerID = "Player " + args[1]; // player's username
        portNo = Integer.parseInt(args[2]); // my server port number
        inetAddr = args[3]; // which address to use
        recipientPortNo1 = Integer.parseInt(args[4]); // recipient1 svr portno
        recipientPortNo2 = Integer.parseInt(args[5]); // recipient2 svr portno
    }

    public void run()
    {
        Thread server = new Thread(new Server());
        Thread client = new Thread(new Client());

        server.start();
        
        try
        {
            Thread.sleep(1000);
        } catch (InterruptedException ex){}
        
        client.start();
    }

    public class Client implements Runnable
    {
        @Override
        public void run()
        {
            try
            {
                Socket socket;
                Scanner inputStream; // used for receiving from server
                PrintStream outputStream; // used for sending to server

				// let players connect to each other
                socket = new Socket(inetAddr, recipientPortNo1);
                outputStream = new PrintStream(socket.getOutputStream());
                outputStream.println(playerID);
                inputStream = new Scanner(socket.getInputStream());
                System.out.println( inputStream.nextLine().replace("\n", "") + " connected to " + playerID + ".");
                
                socket = new Socket(inetAddr, recipientPortNo2);
                outputStream = new PrintStream(socket.getOutputStream());
                outputStream.println(playerID);
                inputStream = new Scanner(socket.getInputStream());
                System.out.println(inputStream.nextLine().replace("\n", "") +  " connected to " + playerID + ".");

				// once connected, start the game
                while (games > 0)
                {
                    currentGame++;
                    move = RPS.getRandomMove(); // get random move of rock, paper, scissors
                    System.out.println(currentGame + ": " + playerID + " used " + RPS.getMoveString(move) + ".");

                    int tempPoints = 0;
                    
                    socket = new Socket(inetAddr, recipientPortNo1);
                    outputStream = new PrintStream(socket.getOutputStream());
                    outputStream.println(move);
                    inputStream = new Scanner(socket.getInputStream());
                    if ( inputStream.nextInt() == 1 ) 
                    {
                        points++;
                        tempPoints++;
                    }
                    
                    socket = new Socket(inetAddr, recipientPortNo2);
                    outputStream = new PrintStream(socket.getOutputStream());
                    outputStream.println(move);
                    inputStream = new Scanner(socket.getInputStream());
                    if ( inputStream.nextInt() == 1 )
                    {
                        points++;
                        tempPoints++;
                    }
                    
                    System.out.println(currentGame + ": " + playerID + " has earned " + tempPoints + " point(s).");
                    
                    games--;
                }
                System.out.println(playerID + " has a total of " + points + " point(s).");
            } catch (IOException ex)
            {
                System.out.println("Error in client: " + ex.getMessage());
            }
        }
    }

    public class Server implements Runnable
    {
        @Override
        public void run()
        {
            try
            {
                ServerSocket serverSocket = new ServerSocket(portNo); // only need to do this once
                Socket socket;
                Scanner inputStream;
                PrintStream outputStream;
                
				// other players trying to connect
                socket = serverSocket.accept();
                inputStream = new Scanner(socket.getInputStream());
                outputStream = new PrintStream(socket.getOutputStream());
                outputStream.println(playerID);
                
                socket = serverSocket.accept();
                inputStream = new Scanner(socket.getInputStream());
                outputStream = new PrintStream(socket.getOutputStream());
                outputStream.println(playerID);
                
				// players have connected and game started
                while (sGames > 0)
                {
                    socket = serverSocket.accept();
                    inputStream = new Scanner(socket.getInputStream());
                    int recipientMove1 = inputStream.nextInt();
                    outputStream = new PrintStream(socket.getOutputStream());
                    if (RPS.isWinner(recipientMove1, move))
                    {
                        outputStream.println(1);
                    }
                    else outputStream.println(0);
                    
                    socket = serverSocket.accept();
                    inputStream = new Scanner(socket.getInputStream());
                    int recipientMove2 = inputStream.nextInt();
                    outputStream = new PrintStream(socket.getOutputStream());
                    if (RPS.isWinner(recipientMove2, move))
                    {
                        outputStream.println(1);
                    }
                    else outputStream.println(0);
                    
                    sGames--;
                }
            } catch (IOException ex)
            {
                System.out.println("Error in server " + ex.toString());
                StackTraceElement[] e = ex.getStackTrace();
                for ( StackTraceElement s : e ) System.out.println(s);
            }
        }
    }
}
