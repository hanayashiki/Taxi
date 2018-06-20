package taxi;

import utils.Index;

public class PathRequest extends Request {
    private String type;
    private int sourceI;

    private int sourceJ;
    private int destinationI;
    private int destinationJ;

    public PathRequest(String type, int sourceI, int sourceJ, int destinationI, int destinationJ) {
        if (!Index.checkIndex(sourceI) || !Index.checkIndex(sourceJ) || !Index.checkIndex(destinationI) ||
                !Index.checkIndex(destinationJ)) {
            throw new IllegalArgumentException();
        }
        if (!Index.checkAdjacency(sourceI, sourceJ, destinationI, destinationJ)) {
            throw new IllegalArgumentException();
        }
        this.type = type;
        this.sourceI = sourceI;
        this.sourceJ = sourceJ;
        this.destinationI = destinationI;
        this.destinationJ = destinationJ;
    }

    public String getType() {
        return type;
    }

    public int getSourceI() {
        return sourceI;
    }

    public int getSourceJ() {
        return sourceJ;
    }

    public int getDestinationI() {
        return destinationI;
    }

    public int getDestinationJ() {
        return destinationJ;
    }
}
