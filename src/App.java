import java.io.File;
import java.util.Comparator;
import java.util.Scanner;
import java.util.stream.IntStream;

public class App {
    public static void main(String[] args) {
        int[][] graph = getGraphFromFile("tsp_input.txt");
        Result result = getCircuit(graph);
        System.out.println(result);
    }

    static Result getCircuit(int[][] graph) {
        boolean[] visited = new boolean[graph.length];
        int[] path = new int[graph.length + 1];
        int cost = 0;

        int cityToVisit = 0;
        // Mark the start city as visited and add it to the path
        visited[0] = true;
        path[0] = cityToVisit;

        for (int step = 0; step < graph.length - 1; step++) {

            // Get the cost to each city from the current city
            int[] costs = graph[cityToVisit];

            // Sort the index of costs from the city
            int[] sorted = IntStream.range(0, costs.length)
                    .boxed().sorted(Comparator.comparingInt(i -> costs[i]))
                    .mapToInt(ele -> ele).toArray();

            // Greedily find the next city with the least cost from the current city
            int idx = 0;
            while (costs[sorted[idx]] == -1 || visited[sorted[idx]]) { idx++; }

            // Visit th next city
            cityToVisit = sorted[idx];
            visited[cityToVisit] = true;
            path[step] = cityToVisit;

            // Add the cost to the sum
            cost += costs[cityToVisit];
        }

        // Go back the starting city
        cost += graph[graph.length - 1][0];
        // Add the cost from the last city to the starting city to the sum
        path[graph.length] = 0;
        // Return the path and cost
        return new Result(path, cost);
    }

    static int[][] getGraphFromFile(String filePath) {
        /* Open the file */
        File file = new File(filePath);
        /* Initialize scanner to read from the opened file */
        Scanner scanner;
        try { scanner = new Scanner(file); }
        catch (Exception e) { throw new RuntimeException(e); }

        int graphSize = Integer.parseInt(scanner.nextLine().split(" ")[1]);
        int[][] graph = new int[graphSize][graphSize];

        for (int i = 0; i < graphSize; i++) {
            String line = scanner.nextLine();
            String[] strNums = line.split(" ");
            int[] cities = new int[strNums.length];
            for (int j = 0; j < strNums.length; j++) cities[j] = Integer.parseInt(strNums[j]);
            graph[i] = cities;
        }

        return graph;
    }

    static class Result {
        int[] path; int cost;
        public Result(int[] path, int cost) {
            this.path = path;
            this.cost = cost;
        }

        @Override
        public String toString() {
            String strPath = "";
            for (int i = 0; i < path.length; i++) {
                strPath += path[i];
                if (i != path.length - 1) strPath += "->";
            }

            return strPath + " Cost: " + cost;
        }
    }
}
