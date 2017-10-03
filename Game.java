import java.util.Scanner;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class Game {

    static int money;
    public int dice1;
    public int dice2;
    static boolean legendary;
    static ReentrantLock lock;

    public static void main(String args[]){
        Scanner input = new Scanner(System.in);

            System.out.println("Welcome to Giants and halflings, would you like to see the rules (Y/N)?\n");
            String answer = input.nextLine();

            if (answer.equals("Y")) {
                System.out.print("dealer rolls 1d10, or the \"Giant,\" setting a target number, or the \"Knee\"\n" +
                        "if dealer rolls a 1, the Giant \"Kicks\" and the house wins\n" +
                        "player rolls 2d6, or the \"Halflings,\" trying to match or exceed the Knee\n" +
                        "on a double 1 roll, a snake scares the Giant and bets push\n" +
                        "if the player rolls an 11/12, or the \"Maw,\" the Halflings are eaten and the house wins\n" +
                        "players who hit the Knee exactly may split the dice by doubling their bet. when splitting, players roll one additional die per split, and all the normal rules apply.\n" +
                        "the player may split a second time if they wish to become legendary \n" +
                        "if the Knee is 2-3, house pays 1:1\n" +
                        "Knee is 4-6, house pays 2:1\n" +
                        "Knee is 7-9, house pays 3:1\n" +
                        "Knee is 10, house pays 5:1\n\n");

            } else if (!answer.equals("Y") && !answer.equals("N")) {
                System.out.println("Please answer 'Y' or 'N' \n");
            }

        money = 100;
        int bet=0;

        int play=1;
        boolean pushed = false;
        while(play==1 && money>0){
            if(legendary == true)
                System.out.println("You are a legend\n");

            System.out.println("You have " + money + " gold");
            if(!pushed) {
                System.out.println("How much would you like to bet?");
                bet = input.nextInt();
                if(bet>money){
                    System.out.println("Doesn't look like you have the coin to back that up \n" +
                            "make a more reasonable bet ");
                    bet= input.nextInt();
                }
            }else{
                System.out.println("The current bet of "+ bet + " gold is pushed from the last round" +
                        "\n how much would you like to add to this?");
                bet+=input.nextInt();
            }


            money=money-bet;
            int dice1 =d6(); int dice2 =d6();
            //int knee=DealerRoll(); int halfling= dice1 + dice2;
            int knee =5; dice1=2; dice2=3; int halfling =5;

            System.out.println("The dealer rolls a " +knee);
            System.out.println("You rolled a " + halfling);

            if((halfling > knee) && (halfling!= 11) && (halfling!=12) && (knee != 1)){
                money = money+bet+(bet*payout(knee)); //money calculation
                System.out.println("You rolled better than the dealer! you win "+ bet*payout(knee) + " gold\n");
                pushed= false;

            }else if(halfling<knee){
                System.out.println("You rolled under the dealer you lose " + bet + " gold\n");
                pushed=false;

            }else if(halfling==2){
                System.out.println("Double 1 roll, bets are pushed to the next round\n");
                pushed=true;
            }else if(halfling==11 || halfling ==12){
                System.out.println("You were eaten by the giant, you lose " + bet + " gold\n");
                pushed=false;
            }else if(knee ==1){
                System.out.println("Bad luck the dealer rolled a 1, you lose " + bet + " gold\n");
                pushed=false;
            } else if(knee == halfling){
                System.out.println("You have met the dealers bet if you would like you may split your bet and roll for each dice individually\n" +
                        "would you like to split your bet(Y/N)?\n");
                answer=input.nextLine();
                if(answer.equals("N")){
                    money = money+bet+(bet*payout(knee)); //money calculation
                }else if(answer.equals("Y")){

                    money=money+game(dice1,bet);
                    money=money+game(dice2,bet); //split bet
                }
            }

            if(money<=0){
                System.out.println("Sorry lad but it seems you've run out of coin. \n" +
                        "better luck next time.");
                play=0;
            }
        }


    }

    public static int game(int dice1, int bet){

        Scanner input= new Scanner(System.in);

        bet=bet*2;

            System.out.println("You have " + money + " gold");

            System.out.println("The current bet is " + bet + " gold\n");

            money=money-bet;

            int dice2 =d6();

            int knee=DealerRoll(); int halfling= dice2+dice1;

            System.out.println("The dealer rolls a" +knee);
            System.out.println("You rolled a" + halfling);

            if((halfling > knee) && (halfling!= 11) && (halfling!=12)){
                 //money calculation
                System.out.println("You rolled better than the dealer! you win "+ bet*payout(knee) + " gold");
                return bet+(bet*payout(knee));

            }else if(halfling<knee){
                System.out.println("You rolled under the dealer you lose " + bet + " gold");
                money=money-bet;
                return 0;

            }else if(halfling==2){
                System.out.println("Double 1 roll, bets are pushed to the next round");
                return bet;

            }else if(halfling==11 || halfling ==12){
                System.out.println("You were eaten by the giant, you lose " + bet + " gold");
                money=money-bet;
                return 0;

            }else if(knee ==1){
                System.out.println("Bad luck the dealer rolled a 1, you lose " + bet + " gold");
                return 0;

            }else if(knee == halfling){
                System.out.println("You have met the dealers bet if you would like you may split your bet and roll for each dice individually\n" +
                "would you like to split your bet(Y/N)?\n");
                String answer=input.nextLine();
                if(answer.equals("N")){
                money = money+bet+(bet*payout(knee)); //money calculation

            }else if(answer.equals("Y")){

             money=money+game(dice1,bet);
             money=money+game(dice2,bet); //split bet

             }
    }
            if(money<=0){
                System.out.println("Sorry lad but it seems you've run out of coin. \n" +
                "better luck next time.");
            }
            return bet;
    }

    private static int DealerRoll(){
        return d10();
    }

    private static int d10(){
        Random r = new Random();
        return r.nextInt(10-1)+1;
    }

    private static int d6(){
        Random r = new Random();
        return r.nextInt(6-1)+1;
    }

    private static int payout(int knee){
        int rate=1;
        if(knee== 2 || knee ==3)
            rate=1;
        else if(knee== 4 || knee ==5 || knee ==6)
            rate =2;
        else if(knee== 7 || knee ==8 || knee ==9)
            rate=3;
        else if(knee==10)
            rate=5;
        else{
            rate =0;
        }

        return rate;
    }

}
