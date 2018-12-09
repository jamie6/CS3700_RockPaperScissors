import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;

/**
 *
 * @author Jamie
 */
public class MainClass
{
    /**
     * @param args the number games to play
     */
    public static void main(String[] args)
    {
		try
		{
			if ( args.length == 1 )
			{
				int games = Integer.parseInt(args[0]);
				
				int portNoA = 8250;
				int portNoB = 7250;
				int portNoC = 5500;

				Thread player1 = new Thread(new NewPlayer(games, "A", portNoA, "127.0.0.1", portNoB, portNoC));
				Thread player2 = new Thread(new NewPlayer(games, "B", portNoB, "127.0.0.2", portNoA, portNoC));
				Thread player3 = new Thread(new NewPlayer(games, "C", portNoC, "127.0.0.3", portNoA, portNoB));

				player1.start();
				player2.start();
				player3.start();
			}
			else
				System.out.println("usage: java MainClass [number of games]");
		}
		catch (Exception e )
		{
			e.printStackTrace();
		}
    }

    public static class NewPlayer implements Runnable
    {

        private int games, portNo, recipientPortNo1, recipientPortNo2;
        private String playerID, inetAddr;

        public NewPlayer(int games, String playerID, int portNo, String inetAddr, int recipientPortNo1, int recipientPortNo2)
        {
            this.games = games;
            this.playerID = playerID;
            this.portNo = portNo;
            this.inetAddr = inetAddr;
            this.recipientPortNo1 = recipientPortNo1;
            this.recipientPortNo2 = recipientPortNo2;
        }

        @Override
        public void run()
        {
            try
            {
                Process process = Runtime.getRuntime().exec("java Player " + games + " " + playerID + " " + portNo + " " + inetAddr + " " + recipientPortNo1 + " " + recipientPortNo2);
                BufferedReader inputStream = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String output = null;
                while ((output = inputStream.readLine()) != null)
                { // prints out all of the system.out. from the processes
                    System.out.println(output);
                }
            } catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }
}
