import java.io.*;
import java.awt.*;
import java.awt.event.*;

import Components.BaseFrame;
import Components.CustomComponents.FileSelector;

public class EncryptionApplication extends BaseFrame {
	Encryptor encryption;
	Decryptor decryption;

	FileSelector encSelectSource, encSelectDest;
	Label encSelectSourceAlert, encSelectDestAlert;
	Choice encSelectType;
	Label encSelectTypeAlert;
	public EncryptionApplication() {
		super("Encryption Application", 1000, 500);

		setLayout(new GridLayout(1, 2));
		encryption = new Encryptor(this);
		add(encryption);

		decryption = new Decryptor(this);
		add(decryption);
	}

	public static void main(String[] args) {
		new EncryptionApplication();
	}
}

class Encryptor extends Panel implements ActionListener{
	Panel source;
		FileSelector sourceFile;
		Label sourceAlert;

	Panel type;
		Choice encryptionChoice;

	Panel keyPanel;
		TextField encryptionKey;
		Label keyAlert;

	Panel destination;
		FileSelector destinationFile;
		Label destinationAlert;

	Panel submit;
		Button submitBtn;

	int key;

	Encryptor(Frame ref) {
		setLayout(new GridLayout(6, 1));
		add(new Label("Encryption"));
			
		source = new Panel(new GridLayout(2, 1, 10, 10));
				sourceFile = new FileSelector(ref);
				source.add(sourceFile);
				sourceAlert = new Label("");
				source.add(sourceAlert);
		add(source);
		type = new Panel(new GridLayout(2, 1, 10, 10));
				type.add(new Label("Select Encryption Type:"));
				encryptionChoice = new Choice();
				encryptionChoice.add("Caeser Cipher");
				encryptionChoice.add("Alphabetic Caeser Cipher");
				type.add(encryptionChoice);	
		add(type);

		keyPanel = new Panel(new FlowLayout(FlowLayout.CENTER));
			keyPanel.add(new Label("Key: "));
			encryptionKey = new TextField(25);
			keyPanel.add(encryptionKey);
			keyAlert = new Label("                                  ");
			keyPanel.add(keyAlert);
		add(keyPanel);

		destination = new Panel(new GridLayout(2, 1, 10, 10));
				destinationFile = new FileSelector(ref);
				destination.add(destinationFile);
				destinationAlert = new Label("                             ");
				destination.add(destinationAlert);
		add(destination);

		submit = new Panel(new FlowLayout(FlowLayout.RIGHT));
			submitBtn = new Button("Encrypt Data");
			submit.add(submitBtn);
			submitBtn.addActionListener(this);
		add(submit);
	}

	public void actionPerformed(ActionEvent ae) {
		if(! performValidation()){
			return;
		}
		if(encryptionChoice.getSelectedItem().equals("Caeser Cipher")) {
			performCaeserCipher();
		}	else if(encryptionChoice.getSelectedItem().equals("Alphabetic Caeser Cipher")) {
			performAlphabeticCaeserCipher();
		}
	}

	private boolean performValidation() {
		boolean validation = true;
		if(sourceFile.getFile().equals("")){
			setAlert(sourceAlert, "Source File has not been Selected!");
			validation = false;
		}
		if(destinationFile.getFile().equals("")){
			setAlert(destinationAlert, "Destination File has not been Selected!");
			validation = false;
		}
		if(encryptionKey.getText().equals("")){
			setAlert(keyAlert, "Key has not been Entered!");
			validation = false;
		}	else 	{
			if(encryptionChoice.getSelectedItem().equals("Caeser Cipher")) {
				key = Integer.parseInt(encryptionKey.getText());
				if((key % 255) == 0) {
					setAlert(keyAlert, "Key cannot be 0 or a multiple of 255");
					validation = false;
				}	else 	{
					key %= 255;
				}
			}	else if(encryptionChoice.getSelectedItem().equals("Alphabetic Caeser Cipher")) {
				key = Integer.parseInt(encryptionKey.getText());
				if((key % 26) == 0) {
					setAlert(keyAlert, "Key cannot be 0 or a multiple of 26");
					validation = false;
				}	else 	{
					key %= 26;
				}
			}
		}
		return validation;
	}

	private void performCaeserCipher() {
		String cipherText = "";
		try {
			File f = new File(sourceFile.getFile());
			FileInputStream fin = new FileInputStream(f);
			BufferedReader sc = new BufferedReader(new InputStreamReader(fin));
			int ch = sc.read();
			while(ch != -1) {
				cipherText += (char) (ch + key);
				ch = sc.read();
			}
			f = new File(destinationFile.getFile());
			FileOutputStream fout = new FileOutputStream(f);
			fout.write(cipherText.getBytes());
		}	catch(IOException e) {
			System.out.println(e);
		}
	}

	private void performAlphabeticCaeserCipher() {
		String cipherText = "";
		try {
			File f = new File(sourceFile.getFile());
			FileInputStream fin = new FileInputStream(f);
			BufferedReader sc = new BufferedReader(new InputStreamReader(fin));
			int ch = sc.read();
			while(ch != -1) {
				if(ch >= 65 && ch <= (92-key)) {
					cipherText += (char) (ch + key);
				}	else if(ch > (92 + key) && ch <= 92) {
					cipherText += (char) (ch - 26 + key);
				}	else if(ch >= 97 && ch <= 122-key) {
					ch -= 32;
					cipherText += (char) (ch + key);
				}	else if(ch > 122-key && ch <= 122) {
					ch -= 32;
					cipherText += (char) (ch - 26 + key);
				}
				ch = sc.read();
			}
			f = new File(destinationFile.getFile());
			FileOutputStream fout = new FileOutputStream(f);
			fout.write(cipherText.getBytes());
		}	catch(IOException e) {
			System.out.println(e);
		}
	}

	private void setAlert(Label label, String message) {
		label.setText(message);
		new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(3000);	
				}	catch(InterruptedException e) {
					System.out.println(e);
				}
				label.setText("");
			}
		}).start();
	}
}

class Decryptor extends Panel implements ActionListener {
	Panel source;
		FileSelector sourceFile;
		Label sourceAlert;

	Panel type;
		Choice encryptionChoice;

	Panel keyPanel;
		TextField encryptionKey;
		Label keyAlert;

	Panel destination;
		FileSelector destinationFile;
		Label destinationAlert;

	Panel submit;
		Button submitBtn;

	int key;

	Decryptor(Frame ref) {
		setLayout(new GridLayout(6, 1));
		add(new Label("Decryption"));
			
		source = new Panel(new GridLayout(2, 1, 10, 10));
				sourceFile = new FileSelector(ref);
				source.add(sourceFile);
				sourceAlert = new Label("");
				source.add(sourceAlert);
		add(source);
		type = new Panel(new GridLayout(2, 1, 10, 10));
				type.add(new Label("Select Encryption Type:"));
				encryptionChoice = new Choice();
				encryptionChoice.add("Caeser Cipher");
				encryptionChoice.add("Alphabetic Caeser Cipher");
				type.add(encryptionChoice);	
		add(type);

		keyPanel = new Panel(new FlowLayout(FlowLayout.CENTER));
			keyPanel.add(new Label("Key: "));
			encryptionKey = new TextField(25);
			keyPanel.add(encryptionKey);
			keyAlert = new Label("                                  ");
			keyPanel.add(keyAlert);
		add(keyPanel);

		destination = new Panel(new GridLayout(2, 1, 10, 10));
				destinationFile = new FileSelector(ref);
				destination.add(destinationFile);
				destinationAlert = new Label("                              ");
				destination.add(destinationAlert);
		add(destination);

		submit = new Panel(new FlowLayout(FlowLayout.RIGHT));
			submitBtn = new Button("Decrypt Data");
			submit.add(submitBtn);
			submitBtn.addActionListener(this);
		add(submit);
	}

	public void actionPerformed(ActionEvent ae) {
		if(! performValidation()){
			return;
		}
		if(encryptionChoice.getSelectedItem().equals("Caeser Cipher")) {
			decryptCaeserCipher();
		}	else if(encryptionChoice.getSelectedItem().equals("Alphabetic Caeser Cipher")) {
			decryptAlphabeticCaeserCipher();
		}
	}

	private boolean performValidation() {
		boolean validation = true;
		if(sourceFile.getFile().equals("")){
			setAlert(sourceAlert, "Source File has not been Selected!");
			validation = false;
		}
		if(destinationFile.getFile().equals("")){
			setAlert(destinationAlert, "Destination File has not been Selected!");
			validation = false;
		}
		if(encryptionKey.getText().equals("")){
			setAlert(keyAlert, "Key has not been Entered!");
			validation = false;
		}	else 	{
			if(encryptionChoice.getSelectedItem().equals("Caeser Cipher")) {
				key = Integer.parseInt(encryptionKey.getText());
				if((key % 255) == 0) {
					setAlert(keyAlert, "Key cannot be 0 or a multiple of 255");
					validation = false;
				}	else 	{
					key %= 255;
				}
			}	else if(encryptionChoice.getSelectedItem().equals("Alphabetic Caeser Cipher")) {
				key = Integer.parseInt(encryptionKey.getText());
				if((key % 26) == 0) {
					setAlert(keyAlert, "Key cannot be 0 or a multiple of 26");
					validation = false;
				}	else 	{
					key %= 26;
				}
			}
		}
		return validation;
	}

	private void setAlert(Label label, String message) {
		label.setText(message);
		new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(3000);	
				}	catch(InterruptedException e) {
					System.out.println(e);
				}
				label.setText("");
			}
		}).start();
	}

	private void decryptCaeserCipher() {
		String normalText = "";
		try {
			File f = new File(sourceFile.getFile());
			FileInputStream fin = new FileInputStream(f);
			BufferedReader sc = new BufferedReader(new InputStreamReader(fin));
			int ch = sc.read();
			while(ch != -1) {
				normalText += (char) (ch - key);
				ch = sc.read();
			}
			f = new File(destinationFile.getFile());
			FileOutputStream fout = new FileOutputStream(f);
			fout.write(normalText.getBytes());
		}	catch(IOException e) {
			System.out.println(e);
		}
	}

	private void decryptAlphabeticCaeserCipher() {
		String normalText = "";
		try {
			File f = new File(sourceFile.getFile());
			FileInputStream fin = new FileInputStream(f);
			BufferedReader sc = new BufferedReader(new InputStreamReader(fin));
			int ch = sc.read();
			while(ch != -1) {
				if(ch >= (65 + key) && ch <= 92) {
					normalText += (char) (ch - key);
				}	else if(ch >= (65) && ch < (65 + key)) {
					normalText += (char) (ch + 26 - key);
				}
				ch = sc.read();
			}
			f = new File(destinationFile.getFile());
			FileOutputStream fout = new FileOutputStream(f);
			fout.write(normalText.getBytes());
		}	catch(IOException e) {
			System.out.println(e);
		}
	}
}
