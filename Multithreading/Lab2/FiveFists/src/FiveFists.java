import sun.rmi.runtime.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * Created by anastasia on 3/29/17.
 */
public class FiveFists {
    enum Monastery {
        GUAN_IN, GUAN_IAN
    }

    private class Fight extends RecursiveTask<Integer> {
        Integer fighter1, fighter2;
        Integer winner;
        
        public Fight(int fighter1, int fighter2) {
            this.fighter1 = fighter1;
            this.fighter2 = fighter2;
        }

        private Integer fight() {
            if (buddhistTsiCapacity[fighter1] > buddhistTsiCapacity[fighter2]) {
                return fighter1;
            } else if (buddhistTsiCapacity[fighter2] > buddhistTsiCapacity[fighter1]) {
                return fighter2;
            }

            Random random = new Random();
            boolean firstWinner = random.nextBoolean();
            if (firstWinner) {
                return fighter1;
            }
            return fighter2;
        }

        @Override
        protected Integer compute() {

            if (fighter1.equals(fighter2))
                return fighter1;
            if (fighter1 + 1 == fighter2)
                return fight();

            Integer splittingPoint = (fighter2 - fighter1) / 2;
            Fight fights = new Fight(fighter1, splittingPoint);

            fights.fork();
            //fights[1].fork();

            fighter1 = splittingPoint;
            fighter2 = fight();

            try {
                fighter1 = fights.join();
            } catch (Throwable t) {
                throw t;
            }

            winner = fight();
            return winner;
        }
    }

    private Monastery[] buddhists;
    private int[] buddhistTsiCapacity;
    private ArrayList<Integer> currentCompetitors;

    public FiveFists() {
        initialize();
    }

    private void initialize() {
        Random random = new Random();
        int amount = random.nextInt(100) + 20;
        buddhists = new Monastery[amount];
        buddhistTsiCapacity = new int[amount];

        for (int i = 0; i < amount; i++) {
            boolean firstMonastery = random.nextBoolean();
            if (firstMonastery) {
                buddhists[i] = Monastery.GUAN_IN;
            } else {
                buddhists[i] = Monastery.GUAN_IAN;
            }

            buddhistTsiCapacity[i] = random.nextInt(10000);
        }

        currentCompetitors = new ArrayList<>(amount);
        for (int i = 0; i < amount; i++) {
            currentCompetitors.add(i);
        }
    }

    public void runTournament() {
        ForkJoinPool pool = new ForkJoinPool();
        Fight tournament = new Fight(0, buddhists.length - 1);
        pool.invoke(tournament);
        System.out.println(buddhists[currentCompetitors.get(tournament.winner)]);
    }

    public static void main(String[] args) {
        FiveFists fiveFists = new FiveFists();
        fiveFists.runTournament();
    }
}
