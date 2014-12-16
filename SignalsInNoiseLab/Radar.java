import java.lang.Math;
import java.util.Scanner;

/**
 * The model for radar scan and accumulator
 * 
 * @author @gcschmit
 * @version 19 July 2014
 */
public class Radar
{
    
    // stores whether each cell triggered detection for the current scan of the radar
    private boolean[][] currentScan;
    
    // stores the previous state of the radar
    private boolean[][] prevScan;
    
    // value of each cell is incremented for each scan in which that cell triggers detection 
    private int[][] accumulator;
    
    // location of the monster
    private int monsterLocationRow;
    private int monsterLocationCol;

    // probability that a cell will trigger a false detection (must be >= 0 and < 1)
    private double noiseFraction;
    
    // number of scans of the radar since construction
    private int numScans;
    
    // amount that monster moves in x and y directions
    public int dx;
    public int dy;  
    
    // 2D array which holds/counts the frequency of appearances of
    // all possible movement vectors of the monster
    
    public int[][] vectorDistribution = new int[11][11];
    
    /**
     * Constructor for objects of class Radar
     * 
     * @param   rows    the number of rows in the radar grid
     * @param   cols    the number of columns in the radar grid
     */
    public Radar(int rows, int cols, int dxInput, int dyInput, int monsterRow, int monsterCol)
    {
        this.dx = dxInput;
        this.dy = dyInput;
        this.monsterLocationRow = monsterRow;
        this.monsterLocationCol = monsterCol;
        
        // initialize instance variables
        currentScan = new boolean[rows][cols]; // elements will be set to false
        prevScan = new boolean[rows][cols]; // stores the previous currentScan array
        accumulator = new int[rows][cols]; // elements will be set to 0
        

        for(int i = 0; i < 11; i++)
        {
           for(int j = 0; j < 11; j++)
           {
               vectorDistribution[i][j] = 0;
            }
        }
         
        noiseFraction = 0.05;
        numScans= 0;
    }
    
    /**
     * Records the state of the radar in the 2D Array prevScan, 
     * Performs a scan of the radar and updates currentScan, 
     * Noise is injected into the grid and the accumulator is updated.
     * 
     */
    public void scan()
    {
        // creates a record of currentScan in prevScan
        for(int row = 0; row < currentScan.length; row++)
        {
            for(int col = 0; col < currentScan[0].length; col++)
            {
                prevScan[row][col] = currentScan[row][col];
            }
        }
        
        // clears currentScan
        for(int row = 0; row < currentScan.length; row++)
        {
            for(int col = 0; col < currentScan[0].length; col++)
            {
                currentScan[row][col] = false;
            }
        }
        
        // inject noise into the grid
        injectNoise();
        
        // udpate the accumulator
        for(int row = 0; row < currentScan.length; row++)
        {
            for(int col = 0; col < currentScan[0].length; col++)
            {
                if(currentScan[row][col] == true)
                {
                   accumulator[row][col]++;
                }
            }
        }
        
        // keep track of the total number of scans
        numScans++;
    }
    
     /**
     * Compares each highlighted pixel of prevScan with highlighted pixels of currentScan in a 5 pixel radius 
     * and increments the corresponding [dx][dy] in vectorDistribution 
     */
    
    public void scanForMonster()
    {        
        for(int row = 0; row < prevScan.length; row++)
        {
            for(int col = 0; col < prevScan[0].length; col++)
            {   
                if(prevScan[row][col] == true)
                {
                    for(int squarex = row - 5; squarex <= row + 5; squarex++)
                    {
                        for(int squarey = col - 5; squarey <= col + 5; squarey++)
                        {
                            if(currentScan[this.wrap(squarex)][this.wrap(squarey)] == true)
                            {   
                                int dxInt = (squarex - row);
                                int dyInt = (squarey - col);
                           
                                vectorDistribution[dxInt + 5][dyInt + 5]++;
                            }
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Displays the algorithm's guess for dx and dy,
     *  and the actual values of dx and dy
     * 
     */
    
    public String displayDxDy()
    {
        int maximum = 0;
        String dispDX = "";
        String dispDY = "";
        
        for(int i = 0; i < 11; i++)
        {
            for(int j = 0; j < 11; j++)
            {
                if(vectorDistribution[i][j] > maximum)
                {
                    maximum = vectorDistribution[i][j];
    
                    dispDX = String.valueOf(i - 5);
                    dispDY = String.valueOf(j - 5);
                }
            }
        }
        
        System.out.println("");
        System.out.println("Dx and Dy values: " + dispDX + ", " + dispDY);
        System.out.println("");
        System.out.println("Real dx value: " + dx);
        System.out.println("Real dy value: " + dy);
        
        return (dispDX + dispDY);
    }
    
    /**
     * Sets the location of the monster
     * 
     * @pre row and col must be integers
     */
    public void setNewMonsterLocation()
    {
        currentScan[monsterLocationRow][monsterLocationCol] = false;
        
        // increments the monster's position by dx and dy
        monsterLocationRow = this.wrap(monsterLocationRow + this.dx);
        monsterLocationCol = this.wrap(monsterLocationCol + this.dy);
        
        currentScan[monsterLocationRow][monsterLocationCol] = true;
    }
    
        /**
     * Alters coordinates outside of the bounds of the radar grid to wrap continuously
     * 
     * @param   coord   the coordinate to be modified
     * @pre coord must be an integer
     */
    public int wrap(int coord)
    {
        if(coord < 0)
        {
                coord += 100;
        }
        
        coord = (coord % 100);
        
        return coord;
    }

     /**
     * Sets the probability that a given cell will generate a false detection
     * 
     * @param   fraction    the probability that a given cell will generate a flase detection expressed
     *                      as a fraction (must be >= 0 and < 1)
     */
    public void setNoiseFraction(double fraction)
    {
        noiseFraction = fraction;
    }
    
    /**
     * Returns true if the specified location in the radar grid triggered a detection.
     * 
     * @param   row     the row of the location to query for detection
     * @param   col     the column of the location to query for detection
     * @return true if the specified location in the radar grid triggered a detection
     */
    public boolean isDetected(int row, int col)
    {
        return currentScan[row][col];
    }
    
    /**
     * Returns the number of times that the specified location in the radar grid has triggered a
     *  detection since the constructor of the radar object.
     * 
     * @param   row     the row of the location to query for accumulated detections
     * @param   col     the column of the location to query for accumulated detections
     * @return the number of times that the specified location in the radar grid has
     *          triggered a detection since the constructor of the radar object
     */
    public int getAccumulatedDetection(int row, int col)
    {
        return accumulator[row][col];
    }
    
    /**
     * Returns the number of rows in the radar grid
     * 
     * @return the number of rows in the radar grid
     */
    public int getNumRows()
    {
        return currentScan.length;
    }
    
    /**
     * Returns the number of columns in the radar grid
     * 
     * @return the number of columns in the radar grid
     */
    public int getNumCols()
    {
        return currentScan[0].length;
    }
    
    /**
     * Returns the number of scans that have been performed since the radar object was constructed
     * 
     * @return the number of scans that have been performed since the radar object was constructed
     */
    public int getNumScans()
    {
        return numScans;
    }
    
    /**
     * Sets cells as falsely triggering detection based on the specified probability
     * 
     */
    private void injectNoise()
    {
        for(int row = 0; row < currentScan.length; row++)
        {
            for(int col = 0; col < currentScan[0].length; col++)
            {
                // each cell has the specified probablily of being a false positive
                if(Math.random() < noiseFraction)
                {
                    currentScan[row][col] = true;
                }
            }
        }
    }
    
}
