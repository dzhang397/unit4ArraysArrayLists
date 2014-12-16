import javax.swing.JFrame;
import java.util.Scanner;

/**
 * Class that contains the main method for the program and creates the frame containing the component.
 * 
 * @author @gcschmit
 * @version 19 July 2014
 */
public class RadarViewer
{
    /**
     * main method for the program which creates and configures the frame for the program
     *
     */
    public static void main(String[] args) throws InterruptedException
    {
        // create the radar, set the monster location, and perform the initial scan
        final int ROWS = 100;
        final int COLS = 100;
        
        int dxInput;
        int dyInput;
        int monsterRow;
        int monsterCol;
        
        Scanner input = new Scanner(System.in);
        
        System.out.print("Input dx and dy values? (yes/no): ");
        
        if(input.next().equals("yes"))
        {
            System.out.print("Input an integer dx value from -5 to 5: ");
            dxInput = input.nextInt();
            System.out.print("Input an integer dy value from -5 to 5: ");
            dyInput = input.nextInt();
        }
        else
        {
            dxInput = (int)(10 * Math.random()) - 5;
            dyInput = (int)(10 * Math.random()) - 5;
        }
        
        System.out.print("Input initial monster location? (yes/no): ");
        
        if(input.next().equals("yes"))
        {
            System.out.print("Input an integer dx value from 0 to 99: ");
            monsterRow = input.nextInt();
            System.out.print("Input an integer dy value from 0 to 99: ");
            monsterCol = input.nextInt();
        }
        else
        {        
            monsterRow = (int)(Math.random() * 100);
            monsterCol = (int)(Math.random() * 100);       
        }
        
        Radar radar = new Radar(ROWS, COLS, dxInput, dyInput, monsterRow, monsterCol);
        
        radar.setNoiseFraction(0.035);
        radar.scan();
        
        JFrame frame = new JFrame();
        
        frame.setTitle("Signals in Noise Lab");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // a frame contains a single component; create the radar component and add it to the frame
        RadarComponent component = new RadarComponent(radar);
        frame.add(component);
        
        // set the size of the frame to encompass the contained component
        frame.pack();
        
        // make the frame visible which will result in the paintComponent method being invoked on the
        //  component.
        frame.setVisible(true);
        
        // perform 250 scans of the radar wiht a slight pause between each
        // after each scan, instruct the Java Run-Time to redraw the window
        
       
        for(int i = 0; i < 250; i++)
        {
            Thread.sleep(10); // sleep 100 milliseconds (1/10 second)
            
            radar.scan();
            radar.setNewMonsterLocation();
            radar.scanForMonster();
            
            frame.repaint();
        }
        
        radar.displayDxDy();
        
        
        /* This block of code was used to determine the
        * greatest noiseFraction level for which this algorithm
        * would give a reasonable guess for (dx, dy)
        
        for(int i = 0; i < 11; i++)
        {
            for(int j = 0; j < 11; j++)
            {
                
                System.out.print("(" + i + ", " + j + ")" + ": " + radar.vectorDistribution[i][j] + " || ");
            }
            System.out.println(" ");
        }
        
        System.out.println("Real dx value: " + radar.dx);
        System.out.println("Real dy value: " + radar.dy);*/
    }
}
