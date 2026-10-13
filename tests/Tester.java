import org.junit.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


public class Tester {
    //1
    @Test
    public void testFieldConstructorInitializesDeadCells() {
        Field f = new Field(3,4);
        assertEquals(3, f.getRows());
        assertEquals(4, f.getCols());
        Cell[][] cells = f.getCells();
        for (Cell[] row : cells)
            for (Cell c : row)
                assertEquals(0, c.getCell());
    }
    //2
    @Test
    public void testGetCellsReturnsDeepCopy() {
        Field f = new Field(2,2);
        Cell[][] c = f.getCells();
        c[0][0].setCell(1);
        assertEquals(0, f.getCells()[0][0].getCell());
    }
    //3
    @Test
    public void testSetCellsAppliesNewState() {
        Field f = new Field(2,2);
        Cell[][] newCells = { { new Cell(1), new Cell(0) }, { new Cell(0), new Cell(1) } };
        f.setCells(newCells);
        assertEquals(1, f.getCells()[0][0].getCell());
        assertEquals(1, f.getCells()[1][1].getCell());
    }
    //4
    @Test
    public void testLoadingShapeHandlesFileWithoutTrailingNewline() throws Exception {
        Field f = new Field(3,3);
        Path tmp = Files.createTempFile("shape", ".txt");
        Files.write(tmp, Collections.singletonList("101"));
        f.loadingShape(tmp.toString(), 0, 0);
        Cell[][] cells = f.getCells();
        assertEquals(1, cells[0][0].getCell());
        assertEquals(0, cells[0][1].getCell());
        assertEquals(1, cells[0][2].getCell());
        Files.deleteIfExists(tmp);
    }
    //5
    @Test
    public void testLoadingShapeWithStartBeyondBoundsWrapsCorrectly() throws Exception {
        Field f = new Field(3,3);
        Path tmp = Files.createTempFile("shapeEdge", ".txt");
        Files.write(tmp, Collections.singletonList("1"));
        f.loadingShape(tmp.toString(), 10, 10); // 10%3 == 1
        Cell[][] cells = f.getCells();
        assertEquals(1, cells[10 % 3][10 % 3].getCell());
        Files.deleteIfExists(tmp);
    }
    //6
    @Test
    public void testCellGetSet() {
        Cell c = new Cell(0);
        assertEquals(0, c.getCell());
        c.setCell(1);
        assertEquals(1, c.getCell());
    }
    //7
    @Test
    public void testCellDrawDoesNotThrow() {
        Cell c = new Cell(1);
        BufferedImage img = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        Graphics g = img.getGraphics();
        assertDoesNotThrow(() -> c.draw(g, 0, 0, 5));
    }
    //8
    @Test
    public void testGetRowsAndCols() {
        Field f = new Field(5,7);
        assertEquals(5, f.getRows());
        assertEquals(7, f.getCols());
    }
    //9
    @Test
    public void testGetSizeCellCalculation() {
        Field f = new Field(10, 10);

        BufferedImage img = new BufferedImage(400, 400, BufferedImage.TYPE_INT_RGB);
        Graphics g = img.getGraphics();

        f.drawField(g);

        int size = f.getSizeCell();
        assertTrue("sizeCell должен быть больше 0 после drawField()", size > 0);
    }
    //10
    @Test
    public void testCountLiveNeighborsWithWrapAround() {
        Field f = new Field(3,3);
        Cell[][] c = f.getCells();
        c[2][2].setCell(1);
        f.setCells(c);

        Evolution evo = new Evolution(f);
        int neighbors = evo.countLiveNeighbors(0,0);
        assertEquals(1, neighbors);
    }
    //11
    @Test
    public void testEvolveBirthOfCell() {
        Field f = new Field(3,3);
        Cell[][] c = f.getCells();
        c[0][1].setCell(1);
        c[1][0].setCell(1);
        c[1][2].setCell(1);
        f.setCells(c);

        Evolution evo = new Evolution(f);
        evo.evolve();
        assertEquals(1, f.getCells()[1][1].getCell());
    }
    //12
    @Test
    public void testEvolveDeathByUnderpopulation() {
        Field f = new Field(3,3);
        Cell[][] c = f.getCells();
        c[1][1].setCell(1);
        f.setCells(c);

        Evolution evo = new Evolution(f);
        evo.evolve();
        assertEquals(0, f.getCells()[1][1].getCell());
    }
    //13
    @Test
    public void testEvolveDeathByOverpopulation() {
        Field f = new Field(3,3);
        Cell[][] c = f.getCells();
        c[1][1].setCell(1);
        c[0][1].setCell(1);
        c[1][0].setCell(1);
        c[1][2].setCell(1);
        c[2][1].setCell(1);
        f.setCells(c);

        Evolution evo = new Evolution(f);
        evo.evolve();
        assertEquals(0, f.getCells()[1][1].getCell());
    }
    //14
    @Test
    public void testEvolveCellSurvivesWithTwoOrThreeNeighbors() {
        Field f = new Field(3,3);
        Cell[][] c = f.getCells();
        c[1][1].setCell(1);
        c[0][1].setCell(1);
        c[1][0].setCell(1);
        f.setCells(c);

        Evolution evo = new Evolution(f);
        evo.evolve();
        assertEquals(1, f.getCells()[1][1].getCell());
    }
    //15
    @Test
    public void testDrawFieldComputesSizeCellAndDoesNotThrow() {
        Field f = new Field(10, 10);
        BufferedImage img = new BufferedImage(GraphicWindow.WIDTH, GraphicWindow.HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics g = img.getGraphics();

        try {
            f.drawField(g);
        } catch (Exception e) {
            fail("drawField threw exception: " + e.getMessage());
        }

        int size = f.getSizeCell();
        assertTrue("sizeCell должен быть положительным после drawField()", size > 0);
    }
    //16
    @Test
    public void testDrawLivingCellsRendersLivingCellColor() {
        Field f = new Field(5,5);

        Cell[][] c = f.getCells();
        c[2][3].setCell(1);
        f.setCells(c);

        BufferedImage img = new BufferedImage(GraphicWindow.WIDTH, GraphicWindow.HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics g = img.getGraphics();
        f.drawField(g);

        try {
            f.drawLivingCells(g);
        } catch (Exception e) {
            fail("drawLivingCells threw exception: " + e.getMessage());
        }

        int size = f.getSizeCell();
        assertTrue(size > 0);

        int px = 3 * size + size / 2;
        int py = 2 * size + size / 2;

        int rgb = img.getRGB(px, py);
        int expected = new Color(0x29B817).getRGB();
        assertEquals("В центре живой клетки должен быть цвет 0x29B817", expected, rgb);
    }
    //17
    @Test
    public void testCheckingCompletionGameReturnsTrueWhenNoLivingCells() {
        Field f = new Field(3,3); // все клетки по умолчанию мёртвы
        GameCompletion gc = new GameCompletion(f);

        boolean result = gc.checkingСompletionGame();
        assertTrue("checkingСompletionGame должен вернуть true для пустого поля", result);
    }
    //18
    @Test
    public void testCheckingCompletionGameDetectsNoChange() {
        Field f = new Field(3,3);
        Cell[][] c = f.getCells();
        c[1][1].setCell(1);
        f.setCells(c);

        GameCompletion gc = new GameCompletion(f);

        boolean first = gc.checkingСompletionGame();
        assertFalse("первый вызов не должен завершить игру", first);

        boolean second = gc.checkingСompletionGame();
        assertTrue("второй вызов должен обнаружить, что конфигурация не изменилась", second);
    }
    //19
    @Test
    public void testCheckingCompletionDetectsRepeatConfiguration() {
        Field f = new Field(5,5);

        Cell[][] c = f.getCells();
        c[2][1].setCell(1);
        c[2][2].setCell(1);
        c[2][3].setCell(1);
        f.setCells(c);

        GameCompletion gc = new GameCompletion(f);
        Evolution evo = new Evolution(f);

        assertFalse(gc.checkingСompletionGame());

        evo.evolve();
        assertFalse(gc.checkingСompletionGame());

        evo.evolve();
        boolean result = gc.checkingСompletionGame();
        assertTrue("Должен обнаружиться повтор конфигурации (blinker вернулся к исходному)", result);
    }
    //20
    @Test
    public void testGraphicWindowPaintDoesNotThrow() {
        Field f = new Field(4,4);
        GraphicWindow gw = new GraphicWindow(f);

        BufferedImage img = new BufferedImage(GraphicWindow.WIDTH, GraphicWindow.HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics g = img.getGraphics();

        try {
            gw.paint(g); 
        } catch (Exception e) {
            fail("GraphicWindow.paint threw exception: " + e.getMessage());
        }
    }

}