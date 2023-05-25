// Imports
import java.io.*;
import java.nio.file.Files;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import java.util.regex.*;
import javax.xml.parsers.*;
import javax.xml.xpath.*;

public class SystemParser {
    private File file;  // File to parse
    private Document doc;   // XML Document
    private MainSystem mainSys = new MainSystem(); // mainSys of mdl file with found Blocks and Lines
    private XPath xpath = XPathFactory.newInstance().newXPath();    // XPath expression creator
    XPathExpression expr;

    // Constructors
    public SystemParser() {}
    
    /**
     * Constructor of .mdl file parser that:
     * 1. Searches for <System><\System> tag inside the given file.
     * 2. Filter this tag out of all text
     * 3. Parses the <System><\System> tag and extract important information
     * @param file: 
     */
    public SystemParser(File file) throws XPathException {
        if (!Pattern.compile(".+\\.mdl$").matcher(file.getName()).matches()) { // If the file extension isn't '.mdl'
            throw new IllegalArgumentException("The file passed isn't '.mdl' file.");
        }

        if (file.length() == 0) { // If file is empty or doesn't exist
            throw new IllegalArgumentException("The file provided is empty");
        }
        this.file = file;

        // Getting the file text ready for parsing
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(getSystemTagString(this.file)));

        // Parse the XML text
        try {
            doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is); // parsing the file into Document object
            doc.getDocumentElement().normalize(); // Process text nodes deep down the tree
        } catch (Exception e) {
            System.out.println("Failed to parse '.mdl' file. Terminating program...");
            System.exit(0);
        }
        // System.out.println(doc.getDocumentElement().getTextContent());
        
        // Parse XML
        parseXML();
    }

    public SystemParser(String filePath) throws XPathException {
        this(new File(filePath));
    }

    /**
     * 
     * @return A class-like construct of the <System> tag found in the '.mdl' file 
     */
    public MainSystem getMDLSystem() {
        return mainSys;
    }

    // Helper functions
    private void parseXML() throws XPathException {
        addCurrentLoc();
        addReportName();
        addZoomFactor();
        addBlocks();
        mainSys.sortBlockList();
        addLines();
    }


    private String getNodeText(String nodeXPATH) throws XPathExpressionException {
        expr = xpath.compile(nodeXPATH);
        Node node = (Node)expr.evaluate(doc, XPathConstants.NODE);
    
        try {
            return node.getTextContent();
        } catch (NullPointerException e) {
            throw new NullPointerException("Can't find the specified node.");
        }
    }

    private NodeList getNodeList(String nodeXPATH) throws XPathExpressionException {
        expr = xpath.compile(nodeXPATH);
        NodeList nodes = (NodeList)expr.evaluate(doc, XPathConstants.NODESET);
    
        if (nodes.getLength() == 0) {
            throw new NullPointerException("Can't find the corresponding node.");
        } else {
            return nodes;
        }
    }

    private void addBlocks() throws XPathExpressionException {
        NodeList blockNodes = doc.getElementsByTagName("Block");
        for (int i = 0; i < blockNodes.getLength(); i++) {
            Element blockElement = (Element) blockNodes.item(i);    // Get each block Node
            Block block = new Block();  // Create a Block to store the data

            // Parse its attributes
            block.setName(blockElement.getAttribute("Name").strip());
            block.setID(Integer.parseInt(blockElement.getAttribute("SID")));
            block.setType(blockElement.getAttribute("BlockType").strip());
            // System.out.println(blockElement.getAttribute("Name"));

            // Get the position
            String[] position = getNodeText("//Block[" + (i + 1) + "]/P[@Name='Position']").replaceFirst("\\[", "").replaceFirst("]", "").split(",");
            block.setXPos(Double.parseDouble(position[0]));
            block.setYPos(Double.parseDouble(position[1]));

            // Get the number of ports
            try {
                String[] ports = getNodeText("//Block[" + (i + 1) + "]/P[@Name='Ports']").replaceFirst("\\[", "").replaceFirst("]", "").split(",");
                block.setNoOfInputPorts(Integer.parseInt(ports[0].strip()));
                block.setNoOfOutputPorts(Integer.parseInt(ports[1].strip()));
            } catch (NullPointerException e) {
                // System.out.println(e.getMessage());
            } catch (IndexOutOfBoundsException e) {
                // System.out.println("Number of output ports wasn't found");
            }
            
            mainSys.add(block);
        }
    }
    

    /**
     * Adds current view location of user to mainSys
     * @throws XPathException
     */
    private void addCurrentLoc() throws XPathException {
        // Get current location
        String[] numbers = getNodeText("//P[@Name='Location']").replaceFirst("\\[", "").replaceFirst("]", "").split(",");

        // Add it to mainSys
        mainSys.setCurrentXPos(Double.parseDouble(numbers[0]));
        mainSys.setCurrentYPos(Double.parseDouble(numbers[1]));
    }

    private void addReportName() throws XPathException {
        mainSys.setReportName(getNodeText("//P[@Name='ReportName']").strip());
    }

    private void addZoomFactor() throws XPathException {
        mainSys.setZoomFactor(Double.parseDouble(getNodeText("//P[@Name='ZoomFactor']")));
    }

    private void addLines() throws XPathExpressionException {
        NodeList lineNodes = doc.getElementsByTagName("Line");
        int noOflines = lineNodes.getLength();
        lineNodes = null;   // Line Node terminated

        for (int i = 0; i < noOflines; i++) {
            Line1 line = new Line1();
            // Get src Port
            String src = getNodeText(String.format("//Line[%d]/P[@Name='Src']", (i + 1))).strip();
            line.setSrcPort(Character.getNumericValue(src.charAt(0)) , Character.getNumericValue(src.charAt(src.length() - 1)));

            // Get Dst Ports
            NodeList dsts = getNodeList(String.format("//Line[%d]//P[@Name='Dst']", (i + 1)));
            for (int j = 0; j < dsts.getLength(); j++) {
                String dst = dsts.item(j).getTextContent().strip();
                line.addDstPort(Character.getNumericValue(dst.charAt(0)), Character.getNumericValue(dst.charAt(dst.length() - 1)));
            }

            mainSys.add(line);
        }
    }

    /**
     * Filter out the System node out of all the text in the .mdl file
     * @param file The .mdl file to parse
     * @return String representation of the System tag found in file
     */
    private String getSystemTagString(File file) {
        String fileData = ""; // To store the text retrieved from file

        try {
            fileData = new String(Files.readAllBytes(file.toPath()));   // Try to read the file text
            // System.out.println(fileData);
        } catch (IOException e) {   // If failed
            System.out.println("Couldn't read file as String. Terminating program..."); // Print this
            System.exit(0); // Then terminate program
        }

        // Search for the System tag in the file text
        Pattern pattern = Pattern.compile("<System>.*?</System>", Pattern.DOTALL);  // Regex Pattern
        Matcher matcher = pattern.matcher(fileData);    // Matcher object to find the Pattern
        if (matcher.find()) {   // If pattern was found
            return matcher.group(); // return it
        } else {
            throw new RuntimeException("The System Tag wasn't found in the given file.");
        }

    }
}
