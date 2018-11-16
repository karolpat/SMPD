package projo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

public class SMPD {

	Database db = new Database(0, 0, 0);
	Mainwindow mw = new Mainwindow();
	Classifier clsfr=new Classifier();

	private final int FISHER_SELECTED = 0;
	private final int SFS_SELECTED = 1;
	private final String[] CLASSIFIERS = { "NN", "kNN", "NM", "kNM" };
	private final int[] CONSECUTIVE_K = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

	private String filePath;

	private JFrame frmWelcome;
	private boolean loaded = false;

	private int methodSelection = -1;
	private int classifierSelected;
	private int kSelected = 1;
	private int percentage;
	private int[] bestFeatures;
	
	private int dimension;
	private JTabbedPane tabbedPane;
	private JPanel panel;
	private JButton btnSelectFile;
	private JCheckBox chckbxSfs;
	private JCheckBox chckbxFisher;
	private JButton btnCmon;
	private JComboBox<Integer> comboBox;
	private JPanel panel_1;
	private JComboBox<Integer> k;
	private JComboBox<String> classifier;
	private Label kLabel;
	private Label classifierLbl;
	private JButton selectFileBtn;
	private JButton trainBtn;
	private JTextArea textArea;
	private JButton executeBtn;

	private void chooseFileButtonClicked(ActionEvent e) {
		openFile();
		if (loaded) {
			comboBox.setEnabled(true);
			for (int i = 1; i <= db.getNoFeatures(); i++) {
				comboBox.addItem(i);
			}
		} else {
			System.out.println("Something failed while loading file");
		}
	}

	private void chooseFileButtonClickedClassifier(ActionEvent e) {

		openFile();
		if (loaded) {
			classifier.setEnabled(true);
			for (int i = 0; i < CLASSIFIERS.length; i++) {
				classifier.addItem(CLASSIFIERS[i]);
			}
		} else {
			System.out.println("Something failed while loading file");
		}
	}

	private void openFile() {

		JFileChooser fileChooser = new JFileChooser();
		int returnValue = fileChooser.showOpenDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			filePath = fileChooser.getSelectedFile().getAbsolutePath();
			db.load(filePath);
//			System.out.println(db.getNoFeatures());
			loaded = true;
		}
	}
	
	private void splitDataset(ActionEvent ae) {
		String input = textArea.getText();
		if(!input.isEmpty()) {
			try {
				percentage=Integer.parseInt(input);
				if(percentage<1 || percentage>99) {
					JOptionPane.showMessageDialog(frmWelcome, "Insert proper value 1-99.");
					executeBtn.setEnabled(false);
				}else {
					clsfr.splitObjects(percentage,filePath);
					executeBtn.setEnabled(true);
				}
				
			}catch (Exception e) {
				JOptionPane.showMessageDialog(frmWelcome, "Insert proper value 1-99.");
				executeBtn.setEnabled(false);
			}
		}else {
			JOptionPane.showMessageDialog(frmWelcome, "Insert value 1-99.");
			executeBtn.setEnabled(false);
		}
	}
	
	private void checkClassifier(ActionEvent e) {
		clsfr.classificate(percentage, kSelected, classifierSelected);
	}

	private void sfsClicked(ActionEvent ae) {
		chckbxFisher.setSelected(false);
		methodSelection = SFS_SELECTED;
		if (loaded)
			btnCmon.setEnabled(true);
	}

	private void fisherClicked(ActionEvent ae) {
		chckbxSfs.setSelected(false);
		methodSelection = FISHER_SELECTED;
		if (loaded)
			btnCmon.setEnabled(true);

	}

	private void compute(ActionEvent e) {
		long startTime = System.currentTimeMillis();
		bestFeatures=mw.go(dimension, methodSelection, filePath);
		long finishTime = System.currentTimeMillis();
		System.out.println("Execution time in milisec: " + (finishTime - startTime));
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SMPD window = new SMPD();
					window.frmWelcome.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SMPD() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmWelcome = new JFrame();
		frmWelcome.setIconImage(Toolkit.getDefaultToolkit()
				.getImage(SMPD.class.getResource("/com/sun/javafx/webkit/prism/resources/mediaMute.png")));
		frmWelcome.setTitle("Welcome");
		frmWelcome.getContentPane().setEnabled(true);
		frmWelcome.setBounds(100, 100, 656, 303);
		frmWelcome.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmWelcome.getContentPane().setLayout(new BoxLayout(frmWelcome.getContentPane(), BoxLayout.X_AXIS));

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frmWelcome.getContentPane().add(tabbedPane);

		panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		tabbedPane.addTab("Fishers", null, panel, null);

		btnSelectFile = new JButton("Select file");
		btnSelectFile.setVerticalAlignment(SwingConstants.TOP);
		btnSelectFile.setAlignmentY(0.0f);
		btnSelectFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				chooseFileButtonClicked(e);
			}
		});
		panel.add(btnSelectFile);

		chckbxSfs = new JCheckBox("SFS");
		chckbxSfs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				sfsClicked(ae);
			}
		});
		panel.add(chckbxSfs);

		chckbxFisher = new JCheckBox("Fisher");
		chckbxFisher.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				fisherClicked(ae);
			}
		});
		panel.add(chckbxFisher);

		btnCmon = new JButton("C'mon");
		btnCmon.setEnabled(false);
		btnCmon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				compute(e);
			}
		});
		panel.add(btnCmon);

		comboBox = new JComboBox<Integer>();
		comboBox.setEnabled(false);
		comboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent ie) {
				if (ie.getStateChange() == ItemEvent.SELECTED) {
					dimension = (int) ie.getItemSelectable().getSelectedObjects()[0];
				}
			}
		});
		panel.add(comboBox);

		panel_1 = new JPanel();
		panel_1.setMaximumSize(new Dimension(150, 150));
		tabbedPane.addTab("Classifiers", null, panel_1, null);
		panel_1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		selectFileBtn = new JButton("Select file");
		selectFileBtn.setHorizontalAlignment(SwingConstants.LEFT);
		selectFileBtn.setVerticalAlignment(SwingConstants.TOP);
		selectFileBtn.setAlignmentY(0.0f);
		selectFileBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				chooseFileButtonClickedClassifier(e);
			}
		});
		panel_1.add(selectFileBtn);

		classifierLbl = new Label("classifier:");
		panel_1.add(classifierLbl);

		classifier = new JComboBox();
		classifier.setToolTipText("Classifier");
		panel_1.add(classifier);
		classifier.setEnabled(false);
		classifier.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent ie) {
				if (ie.getStateChange() == ItemEvent.SELECTED) {
					classifierSelected = classifier.getSelectedIndex();
//					System.out.println(classifierSelected+" class");
					if (classifierSelected == 1 || classifierSelected == 3) {
						k.setEnabled(true);
						for (int i = 0; i < CONSECUTIVE_K.length; i++) {
							k.addItem(CONSECUTIVE_K[i]);
						}
					} else {
						k.setSelectedItem(1);
						k.setEnabled(false);
					}
				}
			}
		});

		kLabel = new Label("k:");
		panel_1.add(kLabel);

		k = new JComboBox();
		k.setToolTipText("k");
		k.setEnabled(false);
		k.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent ie) {
				if (ie.getStateChange() == ItemEvent.SELECTED) {
					kSelected = (int) k.getSelectedItem();
//					System.out.println(kSelected+" k");
				}
			}
		});
		panel_1.add(k);

		trainBtn = new JButton("Train");
		trainBtn.setVerticalAlignment(SwingConstants.TOP);
		trainBtn.setHorizontalAlignment(SwingConstants.LEFT);
		trainBtn.setAlignmentY(0.0f);
		panel_1.add(trainBtn);
		trainBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				splitDataset(e);
			}
		});

		textArea = new JTextArea(3, 15);
		panel_1.add(textArea);
		textArea.setBackground(Color.WHITE);
		textArea.setBounds(new Rectangle(0, 0, 150, 50));

		executeBtn = new JButton("C'mon");
		executeBtn.setEnabled(false);
		panel_1.add(executeBtn);
		executeBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				checkClassifier(e);
			}
		});
	}

}
