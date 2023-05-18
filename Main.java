import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import javax.xml.xpath.XPathException;

public class Main extends Application {

    private static MainSystem mdlSystem;

    @Override
    public void start(Stage PrimaryStage) {

        List<Block> blocks = mdlSystem.getBlocks();
        List<Line1> lines = mdlSystem.getLines();

        Pane pane = new Pane();

        double[] arrx = new double[blocks.size()], arry = new double[blocks.size()];

        for (int i = 0; i < blocks.size(); i++) {
            arrx[i] = blocks.get(i).getXPos();
            arry[i] = blocks.get(i).getYPos();
        }

        Arrays.sort(arrx);
        Arrays.sort(arry);

        Map<Double, Double> mapx = new HashMap<>();
        Map<Double, Double> mapy = new HashMap<>();
        Map<Integer, Double> NewX = new HashMap<>();
        Map<Integer, Double> NewY = new HashMap<>();

        for (int i = 0; i < blocks.size(); i++) {
            if (!mapx.containsKey(arrx[i]))
                mapx.put(arrx[i], (double) i);
            if (!mapy.containsKey(arry[i]))
                mapy.put(arry[i], 1.0);
            else
                mapy.put(arry[i], mapy.get(arry[i]) - 1.5);
        }

        // Blocks
        for (int i = 0; i < blocks.size(); i++) {
            double addx = 100 * mapx.get(blocks.get(i).getXPos());
            double addy = 55 * mapy.get(blocks.get(i).getYPos());
            double xshift = arrx[0] - 100;

            String s = blocks.get(i).getName();

            Label l = new Label(), l2 = new Label(s);

            if (s.equals("Unit Delay"))
                l = new Label(" 1\n---\n z");
            else if (s.equals("Saturation"))
                l = new Label("      |   ---\n      |  /\n      |/\n----|----\n     /|\n    / |\n---  |");
            else if (s.equals("Add"))
                l = new Label("    +\n\n    +\n\n    +");
            else if (s.equals("Constant"))
                l = new Label("1");
            else if (s.equals("Scope"))
                l = new Label(" -----\n|       |\n -----");
            else
                l = new Label("");

            if (!s.equals("Add"))
                l.setAlignment(Pos.CENTER);

            l.setPrefWidth(120);
            l.setPrefHeight(120);
            l.setStyle("-fx-border-color: black; -fx-font-size : 12");
            l2.setStyle("-fx-font-size : 15");
            l.setTranslateX((blocks.get(i).getXPos() - xshift) / 2 + addx);
            l.setTranslateY(blocks.get(i).getYPos() + addy);
            l2.setTranslateX((blocks.get(i).getXPos() - xshift) / 2 + addx);
            l2.setTranslateY(blocks.get(i).getYPos() + addy + 120);

            NewX.put(blocks.get(i).getID(), (blocks.get(i).getXPos() - xshift) / 2 + addx);
            NewY.put(blocks.get(i).getID(), (blocks.get(i).getYPos() + addy));

            pane.getChildren().addAll(l, l2);
        }

        Map<Integer, Integer> INnum = new HashMap<>();
        for (int i = 0; i < lines.size(); i++) {
            List<Port> dstPorts = lines.get(i).getDstPorts();
            for (int y = 0; y < dstPorts.size(); y++) {
                int id = dstPorts.get(y).getBlockID();
                if (INnum.containsKey(id))
                    INnum.put(id, INnum.get(id) + 1);
                else
                    INnum.put(id, 1);
            }
        }

        // Lines
        for (int i = 0; i < lines.size(); i++) {
            int srcPort = lines.get(i).getSrcPort().getBlockID();
            List<Port> dstPorts = lines.get(i).getDstPorts();

            for (int y = 0; y < dstPorts.size(); y++) {

                int num = INnum.get(dstPorts.get(y).getBlockID());
                Double k = num > 1 ? 20 + (80.0 / num) * (dstPorts.get(y).getPortNo() - 1) : 60;

                double x1 = NewX.get(srcPort) + 120;
                double y1 = NewY.get(srcPort) + 60;
                double x2 = NewX.get(dstPorts.get(y).getBlockID());
                double y2 = NewY.get(dstPorts.get(y).getBlockID()) + k;

                Group root = new Group();
                int gap = 15;

                if (x1 < x2) { // right
                    Line line = new Line(x1, y1, x1 + gap, y1);
                    line.setStroke(Color.BLACK);
                    line.setStrokeWidth(2);
                    root.getChildren().add(line);
                    line = new Line(x1 + gap, y1, x1 + gap, y2);
                    line.setStroke(Color.BLACK);
                    line.setStrokeWidth(2);
                    root.getChildren().add(line);
                    line = new Line(x1 + gap, y2, x2, y2);
                    line.setStroke(Color.BLACK);
                    line.setStrokeWidth(2);
                    root.getChildren().add(line);
                } else if (x1 > x2 && y1 > y2) { // up left
                    Line line = new Line(x1, y1, x1 + gap, y1);
                    line.setStroke(Color.BLACK);
                    line.setStrokeWidth(2);
                    root.getChildren().add(line);
                    line = new Line(x1 + gap, y1, x1 + gap, y1 - 70);
                    line.setStroke(Color.BLACK);
                    line.setStrokeWidth(2);
                    root.getChildren().add(line);
                    line = new Line(x1 + gap, y1 - 70, x2 - gap, y1 - 70);
                    line.setStroke(Color.BLACK);
                    line.setStrokeWidth(2);
                    root.getChildren().add(line);
                    line = new Line(x2 - gap, y1 - 70, x2 - gap, y2);
                    line.setStroke(Color.BLACK);
                    line.setStrokeWidth(2);
                    root.getChildren().add(line);
                    line = new Line(x2 - gap, y2, x2, y2);
                    line.setStroke(Color.BLACK);
                    line.setStrokeWidth(2);
                    root.getChildren().add(line);
                } else if (x1 > x2 && y1 < y2) { // down left
                    Line line = new Line(x1, y1, x1 + gap, y1);
                    line.setStroke(Color.BLACK);
                    line.setStrokeWidth(2);
                    root.getChildren().add(line);
                    line = new Line(x1 + gap, y1, x1 + gap, y1 + 70);
                    line.setStroke(Color.BLACK);
                    line.setStrokeWidth(2);
                    root.getChildren().add(line);
                    line = new Line(x1 + gap, y1 + 70, x2 - gap, y1 + 70);
                    line.setStroke(Color.BLACK);
                    line.setStrokeWidth(2);
                    root.getChildren().add(line);
                    line = new Line(x2 - gap, y1 + 70, x2 - gap, y2);
                    line.setStroke(Color.BLACK);
                    line.setStrokeWidth(2);
                    root.getChildren().add(line);
                    line = new Line(x2 - gap, y2, x2, y2);
                    line.setStroke(Color.BLACK);
                    line.setStrokeWidth(2);
                    root.getChildren().add(line);
                }

                // Arrow
                double arrowSize = 10;
                double arrowTipX = x2 - arrowSize;
                double arrowBaseY = y2 - arrowSize / 2;
                Polygon arrow = new Polygon(
                        arrowTipX, arrowBaseY,
                        arrowTipX, arrowBaseY + arrowSize,
                        arrowTipX + arrowSize, arrowBaseY + arrowSize / 2);
                arrow.setFill(Color.BLACK);
                root.getChildren().add(arrow);
                pane.getChildren().add(root);
            }
        }
        Scene scene = new Scene(pane, 800, 600);
        PrimaryStage.setScene(scene);
        PrimaryStage.show();
    }

    public static void main(String[] args) throws XPathException {

        launch(args);

    }
}