package taxi;

public enum Adjacency {
    LEAF,   // not connected with (i+1, j), (i, j+1)
    RIGHT,   // connected with (i, j+1)
    DOWN,  // connected with (i+1, j)
    BOTH,    // connected with (i+1, j), (i, j+1)
    OTHER
}