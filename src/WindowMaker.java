import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Scanner;

public class WindowMaker {

    JFrame window = new JFrame();
    JFormattedTextField tokenEnter = new JFormattedTextField();
    JButton send = new JButton("sender");

    public void make(){

        window.setSize(new Dimension(100,100));
        tokenEnter.setCaretColor(new Color(1,1,1));
        window.setVisible(true);
        window.add(BorderLayout.NORTH,tokenEnter);
        window.add(send);
    }

    JPanel panel = new JPanel();
    JLabel debugLabel = new JLabel();
    String debugText;


    public void debug()
    {

        Scanner sout = new Scanner(System.in);
        window.setSize(new Dimension(400,900));
        window.setVisible(true);
        panel.add(debugLabel);
        window.add(panel);
        debugText += "<html>"  + sout.nextLine() ;
        debugText += "<br/>";

        window.add( new JScrollPane(  )  );

        //window.pack();
        while (true)
        {
            System.out.println("123 ");
            debugLabel.setText(debugText);

        }

    }
}
