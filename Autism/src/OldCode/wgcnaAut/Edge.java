package wgcnaAut;


public class Edge { 

    int from; 
    int to; 

    public Edge(int from, int to) {
        super(); 
        this.from = from; 
        this.to = to; 

    } 

    public int getFrom() { 
        return from; 
    } 

    public void setFrom(int from) {
        this.from = from; 
    } 

    public int getTo() { 
        return to; 
    } 

    public void setTo(int to) {
        this.to = to; 
    } 

    public String toString() { 
        return "" + from + "->" + to; 
    } 

    @Override 
    public boolean equals(Object obj) {
        Edge forEdge = (Edge) obj; 
        if ((forEdge.to == this.to) && (forEdge.from == this.from)) {
            return true; 
        } 
        return false; 

    } 

} 