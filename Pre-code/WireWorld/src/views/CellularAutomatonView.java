package views;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import models.CellularAutomaton;

import java.util.Map;

/**
 * Class responsible for displaying CellularAutomaton on Canvas
 * It used <b>delegation</b> architecture
 */
public class CellularAutomatonView<T extends Enum> {
    private static final double CELL_SIZE = 60.;

    private final CellularAutomaton<T> cellularAutomaton;
    private final Canvas canvas;
    private final Map<T, Paint> cellStateToColorMap;

    private Cell<T>[] cells;


    public CellularAutomatonView(CellularAutomaton<T> cellularAutomaton, Canvas canvas, Map<T, Paint> cellStateToColorMap) {
        this.cellularAutomaton = cellularAutomaton;
        this.canvas = canvas;

        if (cellularAutomaton.getPossibleCellValues().length != cellStateToColorMap.size())
            throw new IllegalArgumentException("cellStateToColorMap size should be the same as number of possible states of automaton");
        this.cellStateToColorMap = cellStateToColorMap;

        generateCells();
    }


    public void draw() {
        double w = getWidth();
        double h = getHeight();
        boolean r = canvas.isResizable();
        canvas.setHeight(h);
        canvas.setWidth(w);

        for (Cell cell : cells)
            cell.draw();
    }

    /**
     * Initial creation of cells.
     * Should be run whenever automaton changes shape.
     */
    private void generateCells() {
        cells = new Cell[getCellCount()];

        for (int r = 0; r < getRowCount(); r++) {
            for (int c = 0; c < getColumnCount(); c++) {
                final int index = r*getColumnCount() + c;

                cells[index] = new Cell(c*CELL_SIZE, r*CELL_SIZE, cellularAutomaton.getCells()[index]);
            }
        }
    }

    private void updateStates() {
        for (int i = 0; i < getCellCount(); i++) {
            cells[i].setState(cellularAutomaton.getCells()[i]);
        }
    }

    public int getCellCount() {
        return cellularAutomaton.getCellCount();
    }

    /**
     * Calculates number of cells in vertical dimension
     * @return number of cells in vertical dimension
     */
    public int getRowCount() {
        return cellularAutomaton.getHeight();
    }

    /**
     * Calculates number of cells in horizontal dimension
     * @return number of cells in horizontal dimension
     */
    public int getColumnCount() {
        return cellularAutomaton.getWidth();
    }

    /**
     * Calculates height of entire view
     * @return height of entire view
     */
    public double getHeight() {
        return CELL_SIZE * getRowCount();
    }

    /**
     * Calculates width of entire view
     * @return width of entire view
     */
    public double getWidth() {
        return CELL_SIZE * getColumnCount();
    }

    private class Cell<T> {
        private final double x;
        private final double y;
        private final double size = CELL_SIZE;
        private T state;

        public Cell(double x, double y, T state) {
            this.x = x;
            this.y = y;
            this.state = state;
        }

        public void draw() {
            GraphicsContext gc = canvas.getGraphicsContext2D();

            gc.setFill(cellStateToColorMap.get(state));
            gc.fillRect(x, y, size, size);
        }

        public void setState(T state) {
            this.state = state;
        }
    }
}
