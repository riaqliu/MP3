
//Usually you will require both swing and awt packages
// even if you are working with just swings.
import javax.swing.*;

import java.awt.*;

public class gui {
    public static void main(String args[]) {

            //Creating the Frame
            JFrame frame = new JFrame("Chat Frame");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 700);

            //Creating the MenuBar and adding components
            JMenuBar mb = new JMenuBar();
            JMenu m1 = new JMenu("FILE");
            mb.add(m1);
            JMenuItem m11 = new JMenuItem("Open");
            m1.add(m11);

            //Creating the panel at bottom and adding components
            JPanel panel = new JPanel(); // the panel is not visible in output
            JButton start = new JButton("Start");
            start.setBackground(Color.WHITE);
            panel.add(start);

            

            // Center Areas
            JPanel tMid = new JPanel();
            tMid.setBackground(Color.black);
            
            // JPanel tMidA = new JPanel();
            
            // JPanel tMidB = new JPanel();




            //Adding Components to the frame.
            frame.getContentPane().add(BorderLayout.SOUTH, panel);
            frame.getContentPane().add(BorderLayout.NORTH, mb);
            frame.getContentPane().add(BorderLayout.CENTER, tMid);
            frame.setVisible(true);
    }
}