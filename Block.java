public class Block implements Comparable<Block> {
    // Main Data fields
    private String type;
    private String name;
    private int ID;
    private int noOfInputPorts = 1;
    private int noOfOutputPorts = 1;
    private double xPos;
    private double yPos;
    private String shape = "rectangular";

    // Constuctors
    public Block() {
        // Default Constructor
    }

    public Block(int ID, String name, String type, int noOfInputPorts, int noOfOutputPorts, int xPos, int yPos) {
        setID(ID);
        setName(name);
        setType(type);
        setNoOfInputPorts(noOfInputPorts);
        setNoOfOutputPorts(noOfOutputPorts);
        setXPos(xPos);
        setYPos(yPos);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        if (type == null) {
            throw new IllegalArgumentException("Input can't be a null string.");
        }
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Input can't be a null string.");
        }
        this.name = name;
    }

    public int getID() {
        return ID;
    }

    public void setID(int iD) {
        ID = iD;
    }

    public int getNoOfInputPorts() {
        return noOfInputPorts;
    }

    public void setNoOfInputPorts(int noOfInputPorts) {
        if (noOfInputPorts < 0) {
            throw new IllegalArgumentException("Input must be an integer greater than or equal to 0.");
        }
        this.noOfInputPorts = noOfInputPorts;
    }

    public int getNoOfOutputPorts() {
        return noOfOutputPorts;
    }

    public void setNoOfOutputPorts(int noOfOutputPorts) {
        if (noOfOutputPorts < 0) {
            throw new IllegalArgumentException("Input must be an integer greater than or equal to 0.");
        }
        this.noOfOutputPorts = noOfOutputPorts;
    }

    public double getXPos() {
        return xPos;
    }

    public void setXPos(double xPos) {
        // if (xPos < 0) {
        //     xPos = -xPos;
        // } else {
        //     xPos = xPos * 2;
        // }

        this.xPos = xPos;
    }

    public double getYPos() {
        return yPos;
    }

    public void setYPos(double yPos) {
        // if (yPos < 0) {
        //     yPos = -yPos;
        // } else {
        //     yPos = yPos * 2;
        // }
        
        this.yPos = yPos;
    }

    public String toString() {
        return "---- Block Object ----\nShape: " + shape + "\nID: " + ID + "\nName: " + name + "\nType: " + type + "\nNo of ports: (" + noOfInputPorts + ", " + noOfOutputPorts + ")" + "\nCurrent Position: (" + xPos + ", " + yPos + ")";  
    }
    
    @Override
    public int compareTo(Block b) {
        return ID - b.ID;
    }

    
}
