import java.util.ArrayList;
import java.util.List;

public class Line1 {
    private Port srcPort;
    private List <Port> dstPorts = new ArrayList<Port>(2);

    public Line1(Port srcPort, Port dstPort) {
        setSrcPort(srcPort);
        addDstPort(dstPort);
    }

    public Line1(int srcBlockID, int srcPortNo, int dstBlockID, int dstPortNo) {
        this(new Port(srcBlockID, srcPortNo), new Port(dstBlockID, dstPortNo));
    }

    public Line1() {}

    public Port getSrcPort() {
        return srcPort;
    }

    public void setSrcPort(Port srcPort) {
        this.srcPort = srcPort;
    }

    public void setSrcPort(int blockID, int portNo) {
        setSrcPort(new Port(blockID, portNo));
    }

    public List<Port> getDstPorts() {
        return dstPorts;
    }

    public void setDstPorts(List<Port> inPorts) {
        this.dstPorts = inPorts;
    }

    public void addDstPort(Port dstPort) {
        this.dstPorts.add(dstPort);
    }

    public void addDstPort(int blockID, int portNo) {
        addDstPort(new Port(blockID, portNo));
    }

    public String toString() {
        String msg = "---- Line ----\nSource -> " + srcPort.toString();
        for (int i = 0; i < dstPorts.size(); i++) {
            msg += "\nDestination -> " + dstPorts.get(i).toString();
        }

        return msg;
    }

}
