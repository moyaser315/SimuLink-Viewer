public class Port {
    private int blockID;
    private int portNo;

    public Port(int blockID, int portNo) {
        setBlockID(blockID);
        setPortNo(portNo);
    }

    public Port() {}

    public int getBlockID() {
        return blockID;
    }

    public void setBlockID(int blockID) {
        this.blockID = blockID;
    }

    public int getPortNo() {
        return portNo;
    }

    public void setPortNo(int portNo) {
        if (portNo <= 0) {
            throw new IllegalArgumentException("Port Number need to be greater than 0.");
        }
        
        this.portNo = portNo;
    }    

    public String toString() {
        return String.format("Port: (BlockID = %d, PortNo = %d)", blockID, portNo);
    }
}