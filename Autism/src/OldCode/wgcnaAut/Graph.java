package wgcnaAut;


import java.util.ArrayList; 
import java.util.Iterator; 
import java.util.LinkedHashSet; 
import java.util.Set; 

public class Graph implements Iterable<Edge> {

    int nodesCnt; 
    int edgesCnt; 
    ArrayList<Set<Edge>> edges = new ArrayList<Set<Edge>>(); 

    public Graph(int nodesCnt, int edgesCnt) {
        super(); 
        this.nodesCnt = nodesCnt; 
        this.edgesCnt = edgesCnt; 

        for (int i = 0; i < nodesCnt; i++) {
            edges.add(new LinkedHashSet<Edge>()); 
        } 
    } 

    public int getNodesCnt() {
        return nodesCnt; 
    } 

    public void setNodesCnt(int nodesCnt) {
        this.nodesCnt = nodesCnt; 
    } 

    public int getEdgesCnt() {
        return edgesCnt; 
    } 

    public void setEdgesCnt(int edgesCnt) {
        this.edgesCnt = edgesCnt; 
    } 

    void addEdge(Edge e) { 

        edges.get(e.from).add(e); 
    } 

    void removeEdge(Edge e) { 
        edges.get(e.from).remove(e); 
    } 

    void removeNode(int v) {
        edges.get(v).clear(); 
        nodesCnt--; 

    } 

    /* 
     * Edge getAdjacent(int from, int start) { Set<Edge> arlEdges = 
     * edges.get(from); for (int i = start; i < arlEdges.size(); i++) { return 
     * arlEdges.get(i); 
     *  
     * } return null; } 
     */ 

    Set<Edge> getAdjacent(int node) { 
        LinkedHashSet<Edge> arlEdges = (LinkedHashSet) edges.get(node); 
        return (Set<Edge>) arlEdges.clone(); 
    } 

    public ArrayList<Set<Edge>> getEdges() { 
        return edges; 
    } 

    public void setEdges(ArrayList<Set<Edge>> edges) {
        this.edges = edges; 
    } 

    class EdgeIterator implements Iterator<Edge> {

        Edge next = null; 
        Iterator<Set<Edge>> bucketIterator = edges.iterator(); 
        Iterator<Edge> edgeIterator = null; 

        EdgeIterator() { 

            edgeIterator = bucketIterator.next().iterator(); 
        } 

        @Override 
        public boolean hasNext() {
            boolean has; 
            if (edgeIterator.hasNext()) 
                return true;
            while (has = (bucketIterator.hasNext() && (!(edgeIterator = bucketIterator 
                    .next().iterator()).hasNext()))) 
                ; 
            return edgeIterator.hasNext(); 
        } 

        @Override 
        public Edge next() { 
            Edge e = null; 
            if (edgeIterator.hasNext()) { 
                e = edgeIterator.next(); 
            } 
            while (e == null) { 
                if (!edgeIterator.hasNext()) 
                    return null;
                edgeIterator = bucketIterator.next().iterator(); 
                if (edgeIterator.hasNext()) { 
                    e = edgeIterator.next(); 
                } 
            } 
            return e; 
        } 

        @Override 
        public void remove() {
            // do nothing 

        } 
    } 

    @Override 
    public Iterator<Edge> iterator() { 

        return new EdgeIterator();
    } 

}  