package dataProtection;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;

public class GUI extends JFrame
{
	private final ClickController controller = new ClickController();

	/******* GUI components for main window *********/
	private JButton encryptButton;
	private JButton decryptButton;
	private JButton ppButton;
	private JButton settingsButton;
	JPanel initialWinContainer;
	/******* GUI components for main window *********/
	
	/******* GUI components for settings *********/
	private JLabel encryptionSettingsLabel;
	private JLabel decryptionSettingsLabel;
	private JLabel ppSettingsLabel;
	private JLabel dialogSettingsLabel;

	JPanel settingsWinContainer;
	JRadioButton keepOriginalBtn;
	JRadioButton deleteOriginalBtn;
	JRadioButton multipleEncryptionBtn;
	JRadioButton singleEncryptionBtn;
	
	JRadioButton keepEncryptedBtn; 
	JRadioButton deleteEncryptedBtn;
	JRadioButton multipleDecryptionBtn; 
	JRadioButton singleDecryptionBtn;
	
	JRadioButton keepPPOriginalBtn;
	JRadioButton deletePPOriginalBtn;
	JRadioButton multiplePPBtn;
	JRadioButton singlePPBtn;
	
	JCheckBox encryptionConfirmationCheck;
	JCheckBox decryptionConfirmationCheck;
	JCheckBox ppConfirmationCheck;
	
	JButton saveSettingsBtn;
	/******* GUI components for settings *********/

	/******* GUI components for JProgressPanel's Frame *********/
	JProgressBar progressBar;
	JPanel progressPanel;
	JFrame progressFrame; 
	/******* GUI components for JProgressPanel's Frame *********/
	
	GUI() 
	{
		super("Data Protection");
		setInitialWindow();
	}

	void setInitialWindow()
	{
		encryptButton = new JButton("Encrypt", new ImageIcon(this.getClass().getResource("/icons/lock-icon.png")));
		encryptButton.setActionCommand("encrypt");
		decryptButton = new JButton("Decrypt", new ImageIcon(this.getClass().getResource("/icons/unlock-icon.png")));
		decryptButton.setActionCommand("decrypt");
		ppButton = new JButton("Password Protect", new ImageIcon(this.getClass().getResource("/icons/key-icon.png")));
		ppButton.setActionCommand("pp");
		settingsButton = new JButton("Settings", new ImageIcon(this.getClass().getResource("/icons/settings-icon.png")));
		settingsButton.setActionCommand("settings");
		initialWinContainer = new JPanel();
		initialWinContainer.add(encryptButton);
		initialWinContainer.add(decryptButton);
		initialWinContainer.add(ppButton);
		initialWinContainer.add(settingsButton);
		initialWinContainer.setBackground(new Color(209,209,209));
		add(initialWinContainer);
		
		encryptButton.addActionListener(controller);
		decryptButton.addActionListener(controller);
		ppButton.addActionListener(controller);
		settingsButton.addActionListener(controller);
	}
	
	void setSettingsWindow()
	{
		initializeSettingLabels();
		initializeSettingToggleButtons();
		setSettingToggleButtonsListener();
		makeSettingRadioButtonsGroups();
		setFontSizeForSettings();

		JPanel encryptionSettingsPanel = new JPanel();
		setEncryptionSettingsPanel(encryptionSettingsPanel);
		
		JPanel decryptionSettingsPanel = new JPanel();
		setDecryptionSettingsPanel(decryptionSettingsPanel);
		
		JPanel ppSettingsPanel = new JPanel();
		setPasswordProtectionSettingsPanel(ppSettingsPanel);
		
		JPanel dialogSettingsPanel = new JPanel();
		setDialogSettingsPanel(dialogSettingsPanel);
		
		JPanel bottomButtonsPanel = new JPanel();
		setBottomButtonsPanel(bottomButtonsPanel);		

		settingsWinContainer = new JPanel();
		settingsWinContainer.setLayout(new GridLayout(4,2,10,20));
		settingsWinContainer.setBorder(new EmptyBorder(25,25,0,0));
		settingsWinContainer.add(encryptionSettingsPanel);
		settingsWinContainer.add(decryptionSettingsPanel);
		settingsWinContainer.add(ppSettingsPanel);
		settingsWinContainer.add(dialogSettingsPanel);
		settingsWinContainer.add(bottomButtonsPanel);
		add(settingsWinContainer);
	}
	
	private void initializeSettingLabels()
	{
		encryptionSettingsLabel = new JLabel("Set encryption settings");
		encryptionSettingsLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
		decryptionSettingsLabel = new JLabel("Set decryption settings");
		decryptionSettingsLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
		ppSettingsLabel = new JLabel("Set password protection settings");
		ppSettingsLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
		dialogSettingsLabel = new JLabel("Set message dialog settings");
		dialogSettingsLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
	}
	
	private void initializeSettingToggleButtons()
	{
		keepOriginalBtn = new JRadioButton("Keep original files after encryption");
		deleteOriginalBtn = new JRadioButton("Delete original files after encryption");
		multipleEncryptionBtn = new JRadioButton("Allow multiple files to be encrypted at a time");
		singleEncryptionBtn = new JRadioButton("Allow only single file to be encrypted at a time");
		
		keepEncryptedBtn = new JRadioButton("Keep encrypted files after decryption");
		deleteEncryptedBtn = new JRadioButton("Delete encrypted files after decryption");
		multipleDecryptionBtn = new JRadioButton("Allow multiple files to be decrypted at a time");
		singleDecryptionBtn = new JRadioButton("Allow only single file to be decrypted at a time");
		// pp --> 'password protection'
		keepPPOriginalBtn = new JRadioButton("Keep original files after password protection");
		deletePPOriginalBtn = new JRadioButton("Delete original files after password protection");
		multiplePPBtn = new JRadioButton("Allow multiple files to be password protected at a time");
		singlePPBtn = new JRadioButton("Allow only single file to be password protected at a time");
		
		encryptionConfirmationCheck = new JCheckBox("Ask for confirmation before encrypting files");
		decryptionConfirmationCheck = new JCheckBox("Ask for confirmation before decrypting files");
		ppConfirmationCheck = new JCheckBox("Ask for confirmation before password protecting files");
	}
	
	private void setSettingToggleButtonsListener()
	{
		JToggleButton[] settingButtons = {keepOriginalBtn, deleteOriginalBtn, multipleEncryptionBtn, singleEncryptionBtn,
				keepEncryptedBtn, deleteEncryptedBtn, multipleDecryptionBtn, singleDecryptionBtn, keepPPOriginalBtn, 
				deletePPOriginalBtn, multiplePPBtn, singlePPBtn, encryptionConfirmationCheck, decryptionConfirmationCheck,
				ppConfirmationCheck};

		for(JToggleButton button: settingButtons) {
			button.addItemListener(controller);
		}
	}
	
	private void makeSettingRadioButtonsGroups()
	{
		ButtonGroup btnGroup1a = new ButtonGroup();
		btnGroup1a.add(keepOriginalBtn);
		btnGroup1a.add(deleteOriginalBtn);
		ButtonGroup btnGroup1b = new ButtonGroup();
		btnGroup1b.add(multipleEncryptionBtn);
		btnGroup1b.add(singleEncryptionBtn);
		ButtonGroup btnGroup2a = new ButtonGroup();
		btnGroup2a.add(keepEncryptedBtn);
		btnGroup2a.add(deleteEncryptedBtn);
		ButtonGroup btnGroup2b = new ButtonGroup();
		btnGroup2b.add(multipleDecryptionBtn);
		btnGroup2b.add(singleDecryptionBtn);
		ButtonGroup btnGroup3a = new ButtonGroup();
		btnGroup3a.add(keepPPOriginalBtn);
		btnGroup3a.add(deletePPOriginalBtn);
		ButtonGroup btnGroup3b = new ButtonGroup();
		btnGroup3b.add(multiplePPBtn);
		btnGroup3b.add(singlePPBtn);
	}
	
	private void setFontSizeForSettings()
	{
		JComponent[] components = {encryptionSettingsLabel, decryptionSettingsLabel, ppSettingsLabel,
				dialogSettingsLabel, keepOriginalBtn, deleteOriginalBtn, multipleEncryptionBtn,
				singleEncryptionBtn, keepEncryptedBtn, deleteEncryptedBtn, multipleDecryptionBtn,
				singleDecryptionBtn, keepPPOriginalBtn, deletePPOriginalBtn, multiplePPBtn, singlePPBtn,
				encryptionConfirmationCheck, decryptionConfirmationCheck, ppConfirmationCheck};
		for(JComponent component:components) {
			if(component instanceof JLabel)
				component.setFont(new Font("sans-serif", Font.PLAIN, 20));
			else
				component.setFont(new Font("sans-serif", Font.PLAIN, 14));
		}
	}
	
	private void setEncryptionSettingsPanel(JPanel panel)
	{
		deleteOriginalBtn.setBorder(new EmptyBorder(4,4,10,0));
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(encryptionSettingsLabel);
		panel.add(keepOriginalBtn);
		panel.add(deleteOriginalBtn);
		panel.add(multipleEncryptionBtn);
		panel.add(singleEncryptionBtn);
	}

	private void setDecryptionSettingsPanel(JPanel panel)
	{
		deleteEncryptedBtn.setBorder(new EmptyBorder(4,4,10,0));
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(decryptionSettingsLabel);
		panel.add(keepEncryptedBtn);
		panel.add(deleteEncryptedBtn);
		panel.add(multipleDecryptionBtn);
		panel.add(singleDecryptionBtn);
	}
	
	private void setPasswordProtectionSettingsPanel(JPanel panel)
	{
		deletePPOriginalBtn.setBorder(new EmptyBorder(4,4,10,0));
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(ppSettingsLabel);
		panel.add(keepPPOriginalBtn);
		panel.add(deletePPOriginalBtn);
		panel.add(multiplePPBtn);
		panel.add(singlePPBtn);
	}

	private void setDialogSettingsPanel(JPanel panel)
	{
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(dialogSettingsLabel);		
		panel.add(encryptionConfirmationCheck);		
		panel.add(decryptionConfirmationCheck);		
		panel.add(ppConfirmationCheck);
	}
	
	private void setBottomButtonsPanel(JPanel panel)
	{
		
		JButton defaultSettingsBtn = new JButton("Apply Default");
		saveSettingsBtn = new JButton("Apply Changes");
		JPanel buttonPanel1 = new JPanel();
		buttonPanel1.setLayout(new FlowLayout(FlowLayout.LEFT));
		buttonPanel1.add(defaultSettingsBtn);
		buttonPanel1.add(saveSettingsBtn);
		
		JButton backBtn = new JButton("Back");
		JPanel buttonPanel2 = new JPanel();
		buttonPanel2.setLayout(new FlowLayout(FlowLayout.LEFT));
		buttonPanel2.add(backBtn);
		
		defaultSettingsBtn.setActionCommand("default");
		defaultSettingsBtn.addActionListener(controller);
		saveSettingsBtn.setActionCommand("apply");
		saveSettingsBtn.addActionListener(controller);
		backBtn.setActionCommand("back");
		backBtn.addActionListener(controller);
		
		panel.setLayout(new BorderLayout());
		panel.add(buttonPanel1, BorderLayout.NORTH);
		panel.add(buttonPanel2, BorderLayout.CENTER);
	}
	
	void makeProgressFrame(int minimumProgress, int maximumProgress)
	{
		JLabel progressLabel = new JLabel("In progress...");
		progressLabel.setBorder(new EmptyBorder(10, 0, 10, 10));
		progressBar = new JProgressBar(minimumProgress, maximumProgress);
		progressBar.setStringPainted(true);
		progressBar.setBorderPainted(true);
		progressPanel = new JPanel(new BorderLayout());
		progressPanel.add(progressLabel, BorderLayout.NORTH);
		progressPanel.add(progressBar, BorderLayout.SOUTH);
		
		progressFrame = new JFrame();
		progressFrame.setLayout(new FlowLayout());
		progressFrame.add(progressPanel);
		progressFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  // do not close the application
		
		progressFrame.setLocationRelativeTo(this);
		progressFrame.setSize(250, 120);
		progressFrame.setVisible(true);
	}
	
	void disableMainActionButtons()
	{
		JButton[] mainActionButtons = {encryptButton, decryptButton, ppButton, settingsButton};
		for(JButton button:mainActionButtons) {
			button.setEnabled(false);
		}
	}

	void enableMainActionButtons()
	{
		JButton[] mainActionButtons = {encryptButton, decryptButton, ppButton, settingsButton};
		for(JButton button:mainActionButtons) {
			button.setEnabled(true);
		}
	}
	
}
