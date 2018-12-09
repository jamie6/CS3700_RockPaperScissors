/**
 *
 * @author Jamie
 */
public class RPS // Rock Paper Scissors
{
    final public static int ROCK = 0;
    final public static int PAPER = 1;
    final public static int SCISSORS = 2;
    private static java.util.Random random = new java.util.Random();

    public static boolean isWinner(int you, int opponent)
    {
        return (you == ROCK && opponent == SCISSORS)
                || (you == PAPER && opponent == ROCK)
                || (you == SCISSORS && opponent == PAPER);
    }
    
    public static String getMoveString(int move)
    {
        switch (move)
        {
            case ROCK:
                return "ROCK";
            case PAPER:
                return "PAPER";
            case SCISSORS:
                return "SCISSORS";
            default:
                return "NUKE";
        }
    }
    
    public static int getRandomMove()
    {
        return random.nextInt(3);
    }
}
