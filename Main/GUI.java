/*
 * 	2017. 10. 21
 * 	GUI.java
 * 	Created By D.Y. Jung
 */

package Main;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Libraries.CommBaudRate;
import Libraries.Communicator;
import Libraries.KeybindingController;

import javax.swing.JLabel;
import java.awt.Window.Type;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenuBar;
import javax.swing.JList;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JMenu;

public class GUI extends JFrame implements ChangeListener, ActionListener {

	//Communicator object 
	public Communicator communicator = null; 
	//KeybindingController object 
	public KeybindingController keybindingController = null; 
	
	private JPanel contentPane;
	public JComboBox cmbPort;
	public JComboBox cmbBaudRate;
	public JTextArea txtLog;
	public JButton btnConnect;
	public JButton btnDisconnect; 
	public JButton btnSend;
	
	private JMenuBar menuBar;

	//Variables
	public int nrOfLeds;
	public byte rValue;
	public byte gValue;
	public byte bValue;
	public byte intensityValue;
	public byte[] rgbMap = new byte[10];
	private JLabel lblTitle;
	private JLabel lblLog;
	private JTextField txtCmd;
	private JMenu mnNewMenu;

	
	/**
	 * Create the frame.
	 */
	public GUI() {
		initComponents(); 
		createObjects(); 
		communicator.searchForPorts();
		keybindingController.toggleControls(); 

	}
	
	private void createObjects() { 
		
		communicator = new Communicator(this); 
		keybindingController = new KeybindingController(this);
		
	} 

	private void initComponents() {

		setTitle("RX-TX");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 384);
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		mnNewMenu = new JMenu("파일(&F)");
		mnNewMenu.setMnemonic(KeyEvent.VK_F);	// Alt + F 키
		menuBar.add(mnNewMenu);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 47, 772, 231);
		contentPane.add(panel);
		panel.setLayout(null);
		
		cmbPort = new JComboBox();
		cmbPort.setBounds(12, 33, 102, 21);
		panel.add(cmbPort);
	
		cmbBaudRate = new JComboBox();
		CommBaudRate baudRate = new CommBaudRate();
		baudRate.initComponent(cmbBaudRate);
		cmbBaudRate.setBounds(12, 62, 102, 21);
		panel.add(cmbBaudRate);
		
		lblTitle = new JLabel("Select the 시리얼 COM Port");
		lblTitle.setBounds(12, 8, 391, 15);
		panel.add(lblTitle);
		
		lblLog = new JLabel("로그(Log)");
		lblLog.setBounds(411, 7, 223, 15);
		panel.add(lblLog);
		
		btnConnect = new JButton("Connect(연결)");
		btnConnect.setBounds(123, 32, 140, 23);
		panel.add(btnConnect);
		
		btnDisconnect = new JButton("Disconnect(끊기)");
		btnDisconnect.setBounds(263, 32, 140, 23);
		panel.add(btnDisconnect);
		
		btnConnect.setActionCommand("connect");
		btnDisconnect.setActionCommand("disconnect");
		
		btnConnect.addActionListener(this);
		btnDisconnect.addActionListener(this);
		
		btnSend = new JButton("전송(Send)");
		btnSend.setActionCommand("sendCmd");
		btnSend.addActionListener(this);
		btnSend.setBounds(263, 92, 140, 23);
		panel.add(btnSend);
		
		txtLog = new JTextArea();
		txtLog.setBounds(411, 32, 349, 189);
		

		JScrollPane scrollPane = new JScrollPane(txtLog);
		scrollPane.setBounds(411, 31, 349, 179);
		panel.add(scrollPane);
		
		txtCmd = new JTextField();
		txtCmd.setBounds(12, 93, 251, 21);
		panel.add(txtCmd);
		txtCmd.setColumns(10);
		
	
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if("connect".equals(e.getActionCommand())){
			communicator.connect();
			if (communicator.getConnected() == true) {
				if (communicator.initIOStream() == true) {
					communicator.initListener();
				}
			}
		}
		else if ("disconnect".equals(e.getActionCommand())) {
			communicator.disconnect();
		}
		else if ("led1".equals(e.getActionCommand())) {
			rgbMap[1] = rValue;
			rgbMap[2] = gValue;
			rgbMap[3] = bValue;
			communicator.updateFrame(rgbMap);

		}
		else if ("led2".equals(e.getActionCommand())) {
			rgbMap[4] = rValue;
			rgbMap[5] = gValue;
			rgbMap[6] = bValue;
			communicator.updateFrame(rgbMap);

		}
		else if ("led3".equals(e.getActionCommand())) {
			rgbMap[7] = rValue;
			rgbMap[8] = gValue;
			rgbMap[9] = bValue;
			communicator.updateFrame(rgbMap);

		}
		else if ("toggleMusic".equals(e.getActionCommand())) {
			if (rgbMap[1] == 0) {
				rgbMap[1] = 1;
			}
			else {
				rgbMap[1] = 0;
			}
			communicator.updateFrame(rgbMap);
		}
		else if ("sendCmd".equals(e.getActionCommand()) ) {
			byte[] array = txtCmd.getText().getBytes();
			
			for ( byte data :  array ) {
				communicator.writeData(data);
			}
		}

	}
	


	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new GUI().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}

	@Override
	public void stateChanged(ChangeEvent arg0) {
		
		/*
		JSlider source = (JSlider)e.getSource();
		if (!source.getValueIsAdjusting()) {
			if (source.equals(RSlider)){
				rValue = (byte)source.getValue();
				System.out.println("R:" + rValue);
				//communicator.updateRGB(rValue, gValue, bValue);
			}
			else if (source.equals(GSlider)) {
				gValue = (byte)source.getValue();
				System.out.println("G:" + gValue);
				//communicator.updateRGB(rValue, gValue, bValue);
			}
			else if (source.equals(BSlider)) {
				bValue = (byte)source.getValue();
				System.out.println("B:" + bValue);
				//communicator.updateRGB(rValue, gValue, bValue);
			}
			else {
				intensityValue = (byte)source.getValue();
				System.out.println("I:" + intensityValue);
				rgbMap[0] = intensityValue;
				communicator.updateFrame(rgbMap);
			}
		}
		*/
		
	}
}

