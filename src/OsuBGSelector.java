import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.apache.commons.io.FileUtils;

public class OsuBGSelector extends JPanel implements ActionListener {
	
	boolean fileChosen = false;
	JButton openButton,runButton;
	JFileChooser filechooser;
	JTextArea log;
	File file;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public OsuBGSelector()
	{
		super(new BorderLayout());
		
		
		Font font = new Font("Verdana", Font.BOLD, 14);
		
		//Create the log first as we will use this for error reporting
		log = new JTextArea(5,20);
		log.setMargin(new Insets(5,5,5,5));
		log.setEditable(false);
		log.setFont(font);
		JScrollPane logScrollPane = new JScrollPane(log);
		
		//Get working directory and print it out to user in the log
		File currentDir = new File("").getAbsoluteFile();
		log.append("Currently in: " + currentDir + "\n" + "NOTE: Please make sure that this is INSIDE of your '.../osu!/songs' folder!" + "\n");
		//Create file chooser
		filechooser = new JFileChooser();
		
		filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		//Create the open button
		openButton = new JButton("Select Background...", createImageIcon("/openImage.png"));
		openButton.addActionListener(this);
		
		//Create run button
		runButton = new JButton("Replace all backgrounds...");
		runButton.addActionListener(this);
		runButton.setPreferredSize(openButton.getPreferredSize());
		
		//Create a panel for the buttons to be separate from the log
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(openButton);
		buttonPanel.add(runButton);
		
		//Add buttons and log to panel
		add(buttonPanel, BorderLayout.PAGE_START);
		add(logScrollPane, BorderLayout.CENTER);
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE); 
                createAndShowGUI();
            }
        });
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		//Handle open button action.
        if (e.getSource() == openButton) {
            int returnVal = filechooser.showOpenDialog(OsuBGSelector.this);
 
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                file = filechooser.getSelectedFile();
                fileChosen = true;
                //This is where a real application would open the file.
                log.append("Opening: " + file.getName() + "."+"\n");
            } else {
                log.append("Open command cancelled by user."+"\n");
            }
            log.setCaretPosition(log.getDocument().getLength());
 
        //Handle save button action.
        } else if (e.getSource() == runButton)
        {
        	if(fileChosen == true)
        	{
        		File currentDir = new File("").getAbsoluteFile();
        		try {
					getFolders(currentDir);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        	}
        	else
        	{
        		log.append("No file chosen! Please choose one before you begin." + "\n");
        	}
        }
           
	}
	
	public void getFolders(final File folder) throws IOException {
	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	        	for (final File fileEntryBG : fileEntry.listFiles()) {
	        		if (fileEntryBG.isFile())
	        		{
	        			if (fileEntryBG.getName().contains(".jpg"))
	        			{
	        				log.append("Found background with .jpg extention" + " at: " + fileEntry.getAbsolutePath() + "\n");
	        				fileEntryBG.renameTo(new File(fileEntryBG.getAbsolutePath() + ".bak"));
	        				File DestinationDir = new File(fileEntry.getPath());
	        				System.out.println(DestinationDir);
							FileUtils.copyFileToDirectory(file, DestinationDir);
	        			}
	        			if (fileEntryBG.getName().contains(".png"))
	        			{
	        				log.append("Found background with .png extention" + " at: " + fileEntry.getAbsolutePath() + "\n");
	        				fileEntryBG.renameTo(new File(fileEntryBG.getAbsolutePath() + ".bak"));
	        				File DestinationDir = new File(fileEntry.getPath());
	        				System.out.println(DestinationDir);
							FileUtils.copyFileToDirectory(file, DestinationDir);
							fileEntryBG.renameTo(new File(fileEntryBG.getAbsolutePath() + ".png"));
	        			}
	        			else
	        			{
	        				log.append("No background found in: " + fileEntry.getAbsolutePath() + ".\n");
	        				
	        				File DestinationDir = new File(fileEntry.getPath());
	        				System.out.println(DestinationDir);
							FileUtils.copyFileToDirectory(file, DestinationDir);
							
	        			}
	       
	        		}
	        	}
	        	
	            getFolders(fileEntry);
	        }
	    }
	}

	
	
	private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Osu! Background Replacer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        //Set icon to osu !!!
        frame.setIconImage(new ImageIcon("resources/osu.png").getImage());
        
        //Add content to the window.
        frame.add(new OsuBGSelector());
 
        //Display the window.
        frame.setVisible(true);
    }
	
	protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = OsuBGSelector.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

}
