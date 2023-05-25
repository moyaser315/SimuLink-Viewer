import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainSystem {
    // Data Fields
    private List <Block> blocks = new ArrayList<Block>();   // A list to add the Block Objects of System Class
    private List <Line1> lines = new ArrayList<Line1>();  // A list to add the Line Objects (connection between blocks)
    private double currentXPos;
    private double currentYPos;
    private String reportName;
    private double zoomFactor;  // Optional


    // Constructors
    public MainSystem() {}

    public MainSystem(List<Block> blocks, List<Line1> lines, double currentXPos, double currentYPos) {
        setBlocks(blocks);
        setLines(lines);
        setCurrentXPos(currentXPos);
        setCurrentYPos(currentYPos);
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<Block> blocks) {
        this.blocks = blocks;
    }

    public void add(Block block) {
        blocks.add(block);
    }

    public List<Line1> getLines() {
        return lines;
    }

    public void setLines(List<Line1> lines) {
        this.lines = lines;
    }

    public void add(Line1 line) {
        lines.add(line);
    }

    public double getCurrentXPos() {
        return currentXPos;
    }

    public void setCurrentXPos(double currentXPos) {
        // if (currentXPos < 0) {
        //     currentXPos = -currentXPos;
        // } else {
        //     currentXPos = currentXPos * 2;
        // }

        this.currentXPos = currentXPos;
    }

    public double getCurrentYPos() {
        return currentYPos;
    }

    public void setCurrentYPos(double currentYPos) {
        // if (currentYPos < 0) {
        //     currentYPos = -currentYPos;
        // } else {
        //     currentYPos = currentYPos * 2;
        // }
        
        this.currentYPos = currentYPos;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public double getZoomFactor() {
        return zoomFactor;
    }

    public void setZoomFactor(double zoomFactor) {
        this.zoomFactor = zoomFactor;
    }

    public void printBlocks() {
        for (int i = 0; i < blocks.size(); i++) {
            System.out.println(blocks.get(i));
        }
    }

    public void printLines() {
        for (int i = 0; i < lines.size(); i++) {
            System.out.println(lines.get(i));
        }
    }

    public void sortBlockList() {
        Collections.sort(blocks);
    }
}
