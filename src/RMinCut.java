import java.util.ArrayList;

class RMinCut {

    public static void main(String[] args) {
        ArrayList<ArrayList<Integer>> edges = readEdges(args);
        ArrayList<Integer> nodes = readVertices(args);

        // we calculate how many times we have to run Karger's algorithm
        int m = Tools.ceil(Math.log(0.01)/Math.log(  1 - (2.0/(edges.size()*edges.size()))  )  );

        // initially the minimal cut is the set of edges
        ArrayList<ArrayList<Integer>> min_cut = Tools.copy2D(edges);
        int min_cut_size = min_cut.size();

        for (int i = 0; i < m; i++) {
            // initializing a new graph object
            Graph graph = new Graph(Tools.copy2D(edges), Tools.copy1D(nodes));

            // finding the cut using Karger's algorithm
            ArrayList<ArrayList<Integer>> cut = findCut(graph, edges, nodes);

            if (cut.size() < min_cut_size){
                min_cut = cut;
                min_cut_size = min_cut.size();
            }
        }

        System.out.println("Number of iterations of Karger's algorithm: " + m);
        System.out.println("Min cut size: " + min_cut_size);
        System.out.println("Min cut: " + min_cut);
    }

    public static ArrayList<ArrayList<Integer>> readEdges(String[] args) {

        ArrayList<ArrayList<Integer>> edges = new ArrayList<>();

        int i = 0;  // index of which argument we are reading

        while (i+1 < args.length) {
            // read the two vertices
            int v_0 = Integer.parseInt(args[i]);
            int v_1 = Integer.parseInt(args[i+1]);

            // create an edge for the two vertices
            ArrayList<Integer> edge = new ArrayList<>();
            edge.add(v_0);
            edge.add(v_1);
            // add the edge to the list of edges
            edges.add(edge);

            // increase the index to read the next edge
            i = i + 2;
        }
    return edges;
    }

    public static ArrayList<Integer> readVertices(String[] args) {
        ArrayList<Integer> nodes = new ArrayList<>();

        int i = 0;  // index of which argument we are reading

        while (i+1 < args.length) {
            // read two vertices.
            // We read two vertices per iteration so that we ignore the last vertex if the number of args is odd.
            int v_0 = Integer.parseInt(args[i]);
            int v_1 = Integer.parseInt(args[i+1]);

            // we add the two vertices unless they are already present
            if (!nodes.contains(v_0)){
                nodes.add(v_0);
            }
            if (!nodes.contains(v_1)){
                nodes.add(v_1);
            }

            // increase the index to read the next two vertices
            i = i + 2;
        }
        return nodes;
    }

    public static ArrayList<ArrayList<Integer>> findCut(Graph graph, ArrayList<ArrayList<Integer>> edges,
                                                        ArrayList<Integer> nodes){
        while (graph.getNumberOfSupernodes() > 2){
            ArrayList<Integer> e = graph.chooseRandomEdge();
            graph.contractGraph(e);
        }

        return retrieveCutEdges(edges, nodes, graph.supernodes);
    }

    public static ArrayList<ArrayList<Integer>> retrieveCutEdges(ArrayList<ArrayList<Integer>> edges,
                                                                 ArrayList<Integer> nodes,
                                                                 ArrayList<Integer> supernodes){

        // find elements in one class
        ArrayList<Integer> A = new ArrayList<>();  // A is one set of the partition. i.e. V = A U (V\A)
        int A_value = supernodes.get(0);  // supernodes has only 2 values at this point
        for (int i=0; i<supernodes.size(); i++){
            if (supernodes.get(i).equals(A_value)){
                A.add(nodes.get(i));
            }
        }
        // find cut
        ArrayList<ArrayList<Integer>> cut = new ArrayList<>();
        for (int i=0; i<edges.size(); i++){
            int v_0 = edges.get(i).get(0);
            int v_1 = edges.get(i).get(1);
            if ((A.contains(v_0) && !A.contains(v_1)) || (!A.contains(v_0) && A.contains(v_1)) ){
                cut.add(edges.get(i));
            }
        }
        return cut;
    }
}

class Graph {
    ArrayList<ArrayList<Integer>> edges;  // List of edges
    ArrayList<Integer> nodes;  // List of nodes
    ArrayList<Integer> supernodes;  // List of supernodes

    public Graph(ArrayList<ArrayList<Integer>> list_edges, ArrayList<Integer> list_nodes){
        edges = list_edges;
        nodes = list_nodes;
        supernodes = list_nodes;  // initially the nodes and supernodes coincide
    }

    public ArrayList<Integer> chooseRandomEdge(){
        double x = Math.random()*edges.size();  // uniform random number between 0 and # of edges
        return edges.get(Tools.floor(x));
    }

    public void contractGraph(ArrayList<Integer> edge){
        int new_supernode = 1 + Tools.maxValue(supernodes);
        updateSupernodes(new_supernode, edge);
        updateEdges(new_supernode, edge);
    }

    public void updateSupernodes(int new_supernode, ArrayList<Integer> edge){
        int v_0 = edge.get(0);
        int v_1 = edge.get(1);

        for (int i = 0; i < supernodes.size(); i++) {
            if (supernodes.get(i).equals(v_0) || supernodes.get(i).equals(v_1)) {
                supernodes.set(i, new_supernode);
            }
        }
    }

    public void updateEdges(int new_supernode, ArrayList<Integer> contracted_edge){
        int v_0 = contracted_edge.get(0);
        int v_1 = contracted_edge.get(1);
        int i = 0;
        while (i < edges.size()) {
            ArrayList<Integer> edge = edges.get(i);
            if (edge.get(0).equals(v_0) || edge.get(0).equals(v_1)) {
                edge.set(0, new_supernode);
            }
            if (edge.get(1).equals(v_0) || edge.get(1).equals(v_1)) {
                edge.set(1, new_supernode);
            }
            // if an edge becomes a loop, it is removed
            if (edge.get(0).equals(edge.get(1))){
                edges.remove(i);
                i = i-1;
            }
            i = i + 1;
        }
    }

    public int getNumberOfSupernodes(){
        ArrayList<Integer> supernodes_copy = new ArrayList<Integer>(supernodes);
        int counter = 0;
        while (supernodes_copy.size()>0){
            counter = counter + 1;
            int new_value = supernodes_copy.get(0);

            // I remove all elements in supernodes_copy equal to new_value
            int i = 0;
            while (i < supernodes_copy.size()){
                if (supernodes_copy.get(i).equals(new_value)){
                    supernodes_copy.remove(i);
                    i = i - 1;
                }
                i = i +1;
            }

        }
        return  counter;
    }

}

class Tools {

    public static int floor(double x){
        return (int) x;
    }

    public static int ceil(double x){
        int f = floor(x);
        if(x-f > 0){
            f = f + 1;
        }
        return f;
    }

    public static int maxValue(ArrayList<Integer> values){
        int x = values.get(0);
        for (int i = 1; i < values.size(); i++) {
            if (values.get(i) > x) {
                x = values.get(i);
            }
        }
        return x;
    }

    public static ArrayList<ArrayList<Integer>> copy2D(ArrayList<ArrayList<Integer>> A){
        ArrayList<ArrayList<Integer>> B = new ArrayList<>();
        for (int i = 0; i<A.size(); i++){
            B.add(copy1D(A.get(i)));
        }
        return B;
    }

    public static ArrayList<Integer> copy1D(ArrayList<Integer> A) {
        ArrayList<Integer> B = new ArrayList<>();

        for (int i = 0; i < A.size(); i++) {
            B.add(A.get(i));
        }
        return B;
    }
}
