

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class RadarViewerTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class RadarViewerTest
{
    /**
     * Default constructor for test class RadarViewerTest
     */
    public RadarViewerTest()
    {
    }

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp()
    {
    }

    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @After
    public void tearDown()
    {
    }
    
    /**
     * Tests the algorithm for 16 diferent initial monster positions
     * and 25 different monster velocities
     */
    @Test
    public void testOutput()
    {
        final int ROWS = 100;
        final int COLS = 100;
        
        Radar radar = new Radar(ROWS, COLS, -5, -5, 0, 0);
        
        for(int i = 0; i < 250; i++)
        {
            radar.scan();
            radar.setNewMonsterLocation();
            radar.scanForMonster();
        }

        assertEquals(-5, radar.dx);
        assertEquals(-5, radar.dy);
        
        Radar radar1 = new Radar(ROWS, COLS, 0, 0, 50, 50);
        
        for(int i = 0; i < 250; i++)
        {
            radar1.scan();
            radar1.setNewMonsterLocation();
            radar1.scanForMonster();
        }

        assertEquals(0, radar1.dx);
        assertEquals(0, radar1.dy);
        
        Radar radar2 = new Radar(ROWS, COLS, 5, 5, 99, 99);
        
        for(int i = 0; i < 250; i++)
        {
            radar2.scan();
            radar2.setNewMonsterLocation();
            radar2.scanForMonster();
        }

        assertEquals(5, radar2.dx);
        assertEquals(5, radar2.dy);
        
    }
}
