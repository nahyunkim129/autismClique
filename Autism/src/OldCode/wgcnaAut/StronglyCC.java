package wgcnaAut;


import java.io.BufferedReader; 
import java.io.File; 
import java.io.FileReader; 
import java.io.StringReader; 
import java.util.ArrayList; 
import java.util.Set; 
import java.util.Stack; 
//xkwks
public class StronglyCC {

    Graph graph; 
    ArrayList<CNode> nodes; 

    Stack<CNode> stack = new Stack<CNode>(); 

    int currIndex = 0; 

    class CNode { 
        int label; 
        int dfsIndex = -1; 
        int lowlink = -1;
        boolean inStack = false;

        public CNode(int label) {
            this.label = label; 
        } 

        @Override 
        public String toString() { 
            // TODO Auto-generated method stub 
            return "label: " + label + " dfsIndex " + dfsIndex + " lowlink : " 
                    + lowlink + " stk : " + inStack; 
        } 
    } 

    void init() { 
        nodes = new ArrayList<CNode>(); 
        for (int i = 0; i < graph.getNodesCnt(); i++) {
            nodes.add(new CNode(i)); 
        } 
    } 

    void tarjanExec() { 
        int count = 0; 
        for (CNode node : nodes) 
            if (node.dfsIndex == -1) { 
                ArrayList<CNode> component = findComponent(node); 
                System.out.println(component);
                printComponent(component); 
                count++; 
            } 
        System.out.println("Total components " + count); 
    } 

    ArrayList<CNode> findComponent(CNode node) { 

        node.lowlink = currIndex; 
        node.dfsIndex = currIndex; 
        currIndex++; 
        node.inStack = true; 
        stack.push(node); 

        Set<Edge> arlNbrs = graph.getAdjacent(node.label); 

        for (Edge e : arlNbrs) { 
            CNode nodeTo = nodes.get(e.to); 
            if (nodeTo.dfsIndex == -1) { 
                findComponent(nodes.get(e.to)); 
                node.lowlink = Math.min(node.lowlink, nodeTo.lowlink); 
            } else if (inStack(nodeTo.label)) {
                node.lowlink = Math.min(node.lowlink, nodeTo.dfsIndex); 
            } 
        } 

        if (node.lowlink == node.dfsIndex) { 
            ArrayList<CNode> componentNodes = new ArrayList<CNode>(); 
            CNode stackNode = null; 
            while ((!stack.empty()) && ((stackNode = stack.pop()) != node)) { 
                stackNode.inStack = false; 
                componentNodes.add(stackNode); 
            } 
            node.inStack = false; 
            componentNodes.add(node); 
            //printComponent(componentNodes); 
            return componentNodes; 
        } 

        return null; 
    } 

    boolean inStack(int nodeLabel) {
        if (stack.search(nodes.get(nodeLabel)) != -1) { 
            return true; 
        } 
        return false; 
    } 

    void printComponent(ArrayList<CNode> component) { 
        StringBuilder strBuild = new StringBuilder(); 
        for (CNode cnode : component) { 
            strBuild.append(cnode.label + "-> "); 
        } 
        System.out.println("Component : " 
                + strBuild.substring(0, strBuild.length() - 3));
    } 

    public static void main(String[] args) {
        BufferedReader bufReader = null; 
        if (args.length > 0) {
            // Unit Test Mode 
            bufReader = new BufferedReader(new StringReader(
                    "1\n5\n6\n0 1\n0 2\n2 1\n2 3\n3 4\n4 2\n")); 
        } else { 
            File file = new File("input_test_10.txt"); 
            try { 
                bufReader = new BufferedReader(new FileReader(file));
            } catch (Exception e) { 
                e.printStackTrace(); 
                return; 
            } 
        } 
        StronglyCC ff = new StronglyCC(); 
        try { 
            int totalGraphs = ff.readTotalGraphCount(bufReader);
            System.out.println("Tarjan Strong Components ");
            for (int i = 0; i < totalGraphs; i++) {
                System.out.println("************** Start Graph " + (i + 1) 
                        + "******************************");
                ff.readNextGraph(bufReader); 
                ff.tarjanExec(); 

            } 
        } catch (Exception e) { 
            e.printStackTrace(); 
            System.err.println("Exiting : " + e); 
        } finally { 
            try { 
                bufReader.close(); 
            } catch (Exception f) { 

            } 
        } 
    } 

    int readTotalGraphCount(BufferedReader bufReader) throws Exception {
        return Integer.parseInt(bufReader.readLine()); 
    } 

    // Reads Input 
    void readNextGraph(BufferedReader bufReader) throws Exception {
        try { 
            graph = new Graph(Integer.parseInt(bufReader.readLine()),
                    Integer.parseInt(bufReader.readLine())); 

            init(); 

            for (int k = 0; k < graph.edgesCnt; k++) {

                String[] strArr = bufReader.readLine().split(" "); 
                int u = Integer.parseInt(strArr[0]);
                int v = Integer.parseInt(strArr[1]);

                Edge e = new Edge(u, v); 
                graph.addEdge(e); 

            } 
            // printGraphEdges( graph); 
        } catch (Exception e) { 
            e.printStackTrace(); 
            throw e; 
        } 
    } 

    void printGraphEdges(Graph graph) { 
        for (Edge e : graph) { 
            System.out.println("" + e.getFrom() + " -> " + e.getTo()); 
        } 
    } 

}