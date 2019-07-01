package testG03.chat2;
//0610 �߰�
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Frame;
import java.awt.TextArea;
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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class ChatServer extends Frame implements ActionListener {

	Button btnExit;
	TextArea ta;
	Vector vChatList;
	ServerSocket ss;
	Socket sockClient;

	public ChatServer() {
		setTitle("ä�ü���");
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
		vChatList = new Vector();
		btnExit = new Button("��������");
		btnExit.addActionListener(this);
		ta = new TextArea();
		add(ta, BorderLayout.CENTER);
		add(btnExit, BorderLayout.SOUTH);
		setBounds(250, 250, 200, 200);
		setVisible(true);

		// chatStart()�޼ҵ� ȣ��
		chatStart();
	}//ChatServer ������

	private void chatStart() {
		// ���� ����
		try {
			ss = new ServerSocket(7005);
			while(true) {
			sockClient = ss.accept();
			ta.append(sockClient.getInetAddress().getHostAddress() + "������\n");
			// �������� ip���
			ChatHandle threadChat = new ChatHandle();
			vChatList.add(threadChat);
			threadChat.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}//chatStart

	@Override
	public void actionPerformed(ActionEvent arg0) {
		dispose();

	}

	public static void main(String[] args) {
		new ChatServer();
	}

	class ChatHandle extends Thread {
		BufferedReader br = null;// �Է´��
		PrintWriter pw = null;// ��´��

		public ChatHandle() {
			try {
				InputStream is = sockClient.getInputStream();// �Է�
				br = new BufferedReader(new InputStreamReader(is));
				OutputStream os = sockClient.getOutputStream();// ���
				pw = new PrintWriter(new OutputStreamWriter(os));

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void sendAllClient(String msg) {
			int size = vChatList.size();
			for (int i = 0; i < size; i++) {
				ChatHandle chl = (ChatHandle) vChatList.elementAt(i);
				chl.pw.println(msg);
				chl.pw.flush();
			}
		}

		@Override
		public void run() {
			try {
				String name = br.readLine();
				sendAllClient(name + "�� ���� �����ϼ̽��ϴ�");
				while (true) {// ä�� ���� �ޱ�
					String msg = br.readLine();
					String str = sockClient.getInetAddress().getHostName();
					ta.append(msg + "\n");// ä�ó����� ta�� �߰�
					if (msg.equals("@@Exit")) {
						break;
					} else {
						sendAllClient(name + ":" + msg);
						//������ ��ο��� �޼��� ����
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				vChatList.remove(this);
				try {
					br.close();
					pw.close();
					sockClient.close();
				} catch (IOException e) {	}
			} // br�� �׿��ִ� ���� �ٴ����� �о�ͼ� name�� �ִ´�
			super.run();
		}//run
	}//ChatHandle
}//ChatServer
