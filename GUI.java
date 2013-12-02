import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.*;
import java.util.List;
import javax.swing.*;


public class GUI extends JPanel implements ActionListener {

    private File file;

    private JButton codeButton;
    private JButton cancelButton;
    private JLabel filesLabel;

    Selectable<int[]> port;

    private void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Huffman coder");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        // Add label with coding directory
        JPanel infoPane = new JPanel();
        infoPane.setLayout(new BoxLayout(infoPane, BoxLayout.PAGE_AXIS));

        JLabel directoryLabel = new JLabel(file.getAbsolutePath());
        infoPane.add(directoryLabel);

        // Add "files" label
        filesLabel = new JLabel(" ");
        infoPane.add(filesLabel);

        // Add progress bar
        JProgressBar progressBar = new JProgressBar();
        infoPane.add(progressBar);

        // Add "Code" and "Cancel" button vertically inside another Panel with box layout
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
        buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        buttonPane.add(Box.createHorizontalGlue());
        codeButton = new JButton("Code");
        codeButton.addActionListener(this);
        buttonPane.add(codeButton);

        buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        buttonPane.add(cancelButton);

        // Put everything together, using the content pane's BorderLayout.
        Container contentPane = frame.getContentPane();
        contentPane.add(infoPane, BorderLayout.CENTER);
        contentPane.add(buttonPane, BorderLayout.CENTER);

        // Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {

        //Handle open button action.
        if (e.getSource() == codeButton) {

            List<File> files = traverse(file, new LinkedList<File>());

            filesLabel.setText(files.size() + " files" );

            calculate(files);

            codeButton.setEnabled(false);
            //Handle save button action.
        }

        if (e.getSource() == cancelButton) {
            if (codeButton.isEnabled())
                System.exit(0);
            else
                codeButton.setEnabled(true);
        }
    }

    private List<File> traverse(File folder, List<File> files) {
        if ( folder.isDirectory() ) {
            File[] filesArray = folder.listFiles();
            for (File file: filesArray) {
                if ( folder.isDirectory() ) {
                    traverse(file, files);
                } else {
                    files.add(file);
                }
            }
            return files;
        } else {
            files.add(folder);
            return files;
        }
    }

    private int[] calculate( List<File> files ) {

        port = new Selectable<int[]>();

        int filesLeft = files.size();

        for (int i = 0; i < filesLeft; i++ ) {
            File tempFile = files.get(0);
            new HuffmanProcessor(tempFile, port);
        }

//        int[] freq = calculate();
//
//        for( int i = 0; i < freq.length; i++ ) {
//            System.out.println(i + " : " + freq[i] );
//        }

        int[] finalFrequency = null;

        while(filesLeft != 0) {

            try {
                int[] tempFreq = port.receive();


                if (finalFrequency == null) {
                    finalFrequency = tempFreq;
                } else {
                    for (int i = 0; i < tempFreq.length; i++) {
                        finalFrequency[i] = finalFrequency[i] + tempFreq[i];
                    }
                }

                filesLeft = filesLeft - 1;
                // Decrease label value;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        for (int i=0; i < finalFrequency.length; i++) {
            System.out.println(i + " : " + finalFrequency[i]);
        }

        return finalFrequency;
    }



    public GUI() {

        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        int returnVal = fc.showOpenDialog(GUI.this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = fc.getSelectedFile();
            //This is where a real application would open the file.
            System.out.println("Opening: " + file.getName() + "." );

            //Schedule a job for the event-dispatching thread:
            //creating and showing this application's GUI.
            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    createAndShowGUI();
                }
            });

        } else {

            System.out.println("Open command cancelled by user.");
            System.exit(0);
        }

    }

}
