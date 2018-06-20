package taxi;

import utils.Index;

public class CustomerRequest extends Request {
    private int sourceI;
    private int sourceJ;
    private int destinationI;
    private int destinationJ;

    public CustomerRequest(int sourceI, int sourceJ, int destinationI, int destinationJ) {
        super();
        this.sourceI = sourceI;
        this.sourceJ = sourceJ;
        this.destinationI = destinationI;
        this.destinationJ = destinationJ;
        if (!(Index.checkIndex(sourceI) && Index.checkIndex(sourceJ) && Index.checkIndex(destinationI) && Index.checkIndex(destinationJ))) {
            throw new IllegalArgumentException();
        }
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
