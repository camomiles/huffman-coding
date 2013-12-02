import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.*;
import java.util.List;
import javax.swing.*;


/**
 * This class creates graphical user interface for Huffman coder application,
 * and in order to do that, it pops up JFileChooser dialog.
 *
 * @author Roman
 * @version 2/12/13
 */
public class GUI extends JPanel implements ActionListener {
    // File, specified by user.
    private File file;
    // Graphical user interface elements;
    private JButton codeButton;
    private JButton cancelButton;
    private JLabel filesLabel;
    private JProgressBar progressBar;
    //
    private List<File> files;

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
        progressBar = new JProgressBar();
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

    /**
     * This method is action listener method for buttons.
     */
    public void actionPerformed(ActionEvent e) {
        //Handle "Code" button action.
        if (e.getSource() == codeButton) {
            // Set progress bar into indeterminate state.
            progressBar.setIndeterminate(true);
            // Traverse all subdirectories (if any) and make list of files inside.
            files = makeFilesList(file, new LinkedList<File>());
            // Update label to show files count.
            filesLabel.setText(files.size() + " files" );
            // Set progress bar maximum value
            progressBar.setMaximum(files.size());
            // Set progress bar current value (0)
            progressBar.setValue(0);
            // Disable "Code" button.
            codeButton.setEnabled(false);
            // Run Huffman central processor as background job.
            Thread t = new Thread(new HuffmanCentralProcessor(files, filesLabel, progressBar));
            t.start();
        }
        // Handle "Cancel" button
        if (e.getSource() == cancelButton) {
            System.exit(0);
        }
    }

    /**
     * This method recursively traverses directory specified by user, and returns a list of files
     * for Central Processor to operate over.
     */
    private List<File> makeFilesList(File folder, List<File> files) {
        if ( folder.isDirectory() ) {
            File[] filesArray = folder.listFiles();
            for (File file: filesArray) {
                if ( folder.isDirectory() ) {
                    makeFilesList(file, files);
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
    /**
     * This is builder class for graphical user interface.
     */
    public GUI() {
        // Create new JFileChooser to choose file/folder to be encoded.
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        // Pop it up.
        int returnVal = fc.showOpenDialog(GUI.this);
        // Process the result.
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = fc.getSelectedFile();

            //Schedule a job for the event-dispatching thread:
            //creating and showing this application's GUI.
            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    createAndShowGUI();
                }
            });
        } else {
            // Exit if "Cancel" button clicked in JFileChooser dialog.
            System.exit(0);
        }

    }

}
