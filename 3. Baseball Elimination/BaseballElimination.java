/* *****************************************************************************
 *  Topic:      Baseball Elimination Problem
 *              Given the standings in a sports division at some point during the
 *              season, determine which teams have been mathematically eliminated
 *              from winning their division.
 *  @author:    Ying Chu
 **************************************************************************** */

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.List;

public class BaseballElimination {
    private final int teamNum;
    private final List<String> teamList = new ArrayList<String>();
    private final int[][] games;
    private final int[] wins, loss, remain;
    private int flow;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In in = new In(filename);

        // get the number of teams
        this.teamNum = in.readInt();
        games = new int[teamNum][teamNum];
        wins = new int[teamNum];
        loss = new int[teamNum];
        remain = new int[teamNum];
        in.readLine();

        // get all the needed arrays
        for (int i = 0; i < teamNum; i++) {
            teamList.add(in.readString());
            wins[i] = in.readInt();
            loss[i] = in.readInt();
            remain[i] = in.readInt();
            for (int j = 0; j < teamNum; j++) {
                games[i][j] = in.readInt();
            }
            in.readLine();
        }
    }

    // number of teams
    public int numberOfTeams() {
        return teamNum;
    }

    // all teams
    public Iterable<String> teams() {
        return teamList;

    }

    // number of wins for given team
    public int wins(String team) {

        checkTeam(team);

        int teamid = teamList.indexOf(team);
        return remain[teamid];
    }

    // number of losses for given team
    public int losses(String team) {

        checkTeam(team);

        int teamid = teamList.indexOf(team);
        return loss[teamid];
    }

    // number of remaining games for given team
    public int remaining(String team) {

        checkTeam(team);

        int teamid = teamList.indexOf(team);
        return remain[teamid];

    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {

        checkTeam(team1);
        checkTeam(team2);

        int teamid1 = teamList.indexOf(team1);
        int teamid2 = teamList.indexOf(team2);
        return games[teamid1][teamid2];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {

        checkTeam(team);

        FordFulkerson maxflow = countMaxFlow(team);
        if (maxflow == null) { // trivial elimination
            return true;
        } else {
            return flow > maxflow.value();
        }
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {

        checkTeam(team);

        if (!isEliminated(team)) {
            return null;
        }

        int teamid = teamList.indexOf(team);
        FordFulkerson maxflow = countMaxFlow(team);
        int numOfGameVertice = teamNum * (teamNum - 1) / 2;
        ArrayList<String> certificate = new ArrayList<>();

        for (int i = 0; i < teamNum; i++) {
            if (teamid == i) continue;
            if (maxflow == null) { // trivial elimination
                if (wins[teamid] + remain[teamid] - wins[i] < 0) {
                    certificate.add(teamList.get(i));
                }
            }
            else if (maxflow.inCut(i + numOfGameVertice + 1)) {
                certificate.add(teamList.get(i));
            }
        }
         return certificate;
    }

    private FordFulkerson countMaxFlow(String team) {

        // count all numbers of the vertices
        int teamid = teamList.indexOf(team);
        int numOfGameVertice = teamNum * (teamNum - 1) / 2;
        int sumOfVertice = 2 + numOfGameVertice + teamNum;

        // start building the flow network
        int s = 0; // source
        int t = sumOfVertice - 1; // sink
        FlowNetwork network = new FlowNetwork(sumOfVertice);
        flow = 0;

        int index = 1;
        for (int i = 0; i < teamNum; i++) {
            if (i == teamid) {
                continue;
            }

            for (int j = i + 1; j < teamNum; j++) {
                if (j == teamid) {
                    continue;
                }

                network.addEdge(new FlowEdge(s, index, games[i][j]));
                network.addEdge(
                        new FlowEdge(index, i + numOfGameVertice + 1, Double.POSITIVE_INFINITY));
                network.addEdge(
                        new FlowEdge(index, j + numOfGameVertice + 1, Double.POSITIVE_INFINITY));
                index++;
                flow += games[i][j];
            }

            if (wins[teamid] + remain[teamid] - wins[i] < 0) {
                return null;
            } else {
                network.addEdge(new FlowEdge(i + numOfGameVertice + 1, t, wins[teamid] + remain[teamid] - wins[i]));
            }

        }

        FordFulkerson maxflow = new FordFulkerson(network, s, t);
        return maxflow;
    }

    private void checkTeam(String team) {
        if (team == null) {
            throw new IllegalArgumentException("team is invalid");
        }

        if (!teamList.contains(team)) {
            throw new IllegalArgumentException("team is not included");
        }
    }

}
