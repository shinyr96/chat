package testG03.chat2;

//0610 �߰�
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatClient extends Frame implements ActionListener, Runnable {
	Button btn_exit;// �����ư
	Button btn_send;// ���۹�ư
	Button btn_connect;// ������ ���ӹ�ư

	TextArea txt_list;// ä�ó���
	TextField txt_server_ip;// ���� ������ �Է��ʵ�
	TextField txt_name;// �������̸�
	TextField txt_input;// ä���Է�â

	Socket client;// client��Ĺ
	BufferedReader br;// �Է� ����
	PrintWriter pw;// ���
	String server_ip;// ���� �������ּ�

	final int port = 7005;
	CardLayout cl;// ī�� ���̾ƿ�

	public ChatClient() {
		setTitle("ä�� Ŭ���̾�Ʈ");
		// closing
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
		// ī�巹�̾ƿ� �����ϴ� ��ü����
		cl = new CardLayout();
		setLayout(cl);
		Panel connect = new Panel();
		connect.setBackground(Color.lightGray);
		connect.setLayout(new BorderLayout());// connect�ǳ� ���̾ƿ� ����
		btn_connect = new Button("��������");
		btn_connect.addActionListener(this);
		txt_server_ip = new TextField("192.168.0.73",15);// �ڽ��� ip
		txt_name = new TextField("������", 15);// �������̸�
		Panel connect_sub = new Panel();
		connect_sub.add(new Label("����������(IP) : "));
		connect_sub.add(txt_server_ip);
		connect_sub.add(new Label("��ȭ�� : "));
		connect_sub.add(txt_name);

		// ä��ȭ�� ����
		Panel chat = new Panel();
		chat.setLayout(new BorderLayout());
		Label lblchat = new Label("ä������ȭ��", Label.CENTER);
		connect.add(lblchat, BorderLayout.NORTH);
		connect.add(connect_sub, BorderLayout.CENTER);
		connect.add(btn_connect, BorderLayout.SOUTH);

		// ä��â ȭ�鱸��
		txt_list = new TextArea();// ä�� ���� �����ֱ�
		txt_input = new TextField("", 25);// ä�� ���� �Է�
		btn_exit = new Button("����");// �����ư
		btn_send = new Button("����");// ���۹�ư
		btn_exit.addActionListener(this);// �����ư ���ű� ����
		btn_send.addActionListener(this);// ä�����۹�ư ���ű� ����
		txt_input.addActionListener(this);// ä���Է�â �ű� ����

		Panel chat_sub = new Panel();// ä��â sub�ǳ�(=�ǳ� ���� �ǳ�)
		chat_sub.add(txt_input);
		chat_sub.add(btn_send);
		chat_sub.add(btn_exit);

		Label lblchatTitle = new Label("ä�����α׷� v1.1", Label.CENTER);
		chat.add(lblchatTitle, BorderLayout.NORTH);
		chat.add(txt_list, BorderLayout.CENTER);
		chat.add(chat_sub, BorderLayout.SOUTH);

		// �����ӿ� �߰�
		add(connect, "����â");// connect = panel
		add(chat, "ä��â");
		cl.show(this, "����â");// ����â �����ֱ�//ä��â�� �ڿ� ���� �ִ���
		// cl.show(this, "ä��â");//ä��â �����ֱ�//����â�� �ڿ� ���� �ִ���
		setBounds(250, 250, 250, 300);// ��ġ , ũ�� ��������
		setVisible(true);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// System.out.println("����");
		Object obj = e.getSource();
		try {
			if (obj == btn_connect) {
				server_ip = txt_server_ip.getText();
				Thread th = new Thread(this);
				th.start();
				cl.show(this, "ä��â");// ī�巹�̾ƿ��� ä��â���� ȭ����ȯ
			} else if (obj == btn_exit) {
				System.exit(0);// ���α׷� ����
			} else if (obj == btn_send || obj == txt_input) {
				String msg = txt_input.getText();// ä�ó��� ��������
				pw.println(msg);
				pw.flush();
				txt_input.setText("");//���������
				txt_input.requestFocus();//Ŀ���� �̰��� �α�
				
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Override
	public void run() {
		System.out.println("����2");
		// ������ ����� ���� ����
		try {
			client = new Socket(server_ip, port);// ���ϻ���
			InputStream is = client.getInputStream();// �Է�
			OutputStream os = client.getOutputStream();// ���
			br = new BufferedReader(new InputStreamReader(is));
			pw = new PrintWriter(new OutputStreamWriter(os));
			String msg = txt_name.getText();// ��ȭ����
			pw.println(msg);// ��ȭ�� ����
			pw.flush();// ���� ��������
			txt_input.requestFocus();
			while (true) {
				msg = br.readLine();
				txt_list.append(msg + "\n");// ��ȭ�� �ٹٲ� �߰�
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new ChatClient();
	}

}
