package projo;

import java.awt.Component;
import java.awt.EventQueue;
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
import javax.swing.SwingConstants;

public class SMPD {

	Database db = new Database(0, 0, 0);
	Mainwindow mw = new Mainwindow();

	private final int FISHER_SELECTED = 0;
	private final int SFS_SELECTED = 1;

	private String filePath;

	private JFrame frmWelcome;
	private JButton btnSelectFile;
	private JCheckBox chckbxFisher;
	private JCheckBox chckbxSfs;
	private boolean loaded = false;
	private JComboBox<Integer> comboBox;

	private int methodSelection = -1;
	private int dimension;
	private JButton btnCmon;

	private void chooseFileButtonClicked(ActionEvent e) {
		JFileChooser fileChooser = new JFileChooser();
		int returnValue = fileChooser.showOpenDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			filePath = fileChooser.getSelectedFile().getAbsolutePath();
			db.load(filePath);
			System.out.println(db.getNoFeatures());
			loaded = true;
		}
		if (loaded) {
			comboBox.setEnabled(true);
			for (int i = 1; i <= db.getNoFeatures(); i++) {
				comboBox.addItem(i);
			}
		}
	}

	private void sfsClicked(ActionEvent ae) {
		chckbxFisher.setSelected(false);
		methodSelection = SFS_SELECTED;
		if(loaded) btnCmon.setEnabled(true);
	}

	private void fisherClicked(ActionEvent ae) {
		chckbxSfs.setSelected(false);
		methodSelection = FISHER_SELECTED;
		if(loaded) btnCmon.setEnabled(true);
		
	}
	
	private void compute(ActionEvent e) {
		System.out.println(dimension + " dimension");
		mw.go(dimension, methodSelection, filePath);
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
		frmWelcome.setTitle("Welcome");
		frmWelcome.getContentPane().setEnabled(true);
		frmWelcome.setBounds(100, 100, 634, 425);
		frmWelcome.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmWelcome.getContentPane().setLayout(new BoxLayout(frmWelcome.getContentPane(), BoxLayout.PAGE_AXIS));

		btnSelectFile = new JButton("Select file");
		btnSelectFile.setAlignmentY(Component.TOP_ALIGNMENT);
		btnSelectFile.setVerticalAlignment(SwingConstants.TOP);
		frmWelcome.getContentPane().add(btnSelectFile);

		chckbxSfs = new JCheckBox("SFS");
		chckbxSfs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				sfsClicked(ae);
			}
		});

		frmWelcome.getContentPane().add(chckbxSfs);

		chckbxFisher = new JCheckBox("Fisher");
		chckbxFisher.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				fisherClicked(ae);
			}
		});
		frmWelcome.getContentPane().add(chckbxFisher);

		btnCmon = new JButton("C'mon");
		btnCmon.setEnabled(false);
		btnCmon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				compute(e);
			}
		});
		
		frmWelcome.getContentPane().add(btnCmon);

		btnSelectFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				chooseFileButtonClicked(e);
			}
		});
		
		comboBox = new JComboBox<Integer>();
		frmWelcome.getContentPane().add(comboBox);
		comboBox.setEnabled(false);
		comboBox.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent ie) {
				if (ie.getStateChange() == ItemEvent.SELECTED) {
					dimension = (int) ie.getItemSelectable().getSelectedObjects()[0];
				}
			}
		});
	}

}
