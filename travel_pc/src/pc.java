

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class pc {


	public static void main(String[] args) throws IOException {
		ServerSocket serivce = new ServerSocket(9999);
		try{
			String ip = InetAddress.getLocalHost().getHostAddress();
			System.out.println(ip);
			}catch (UnknownHostException e)        {System.out.println("异常：" + e);  e.printStackTrace(); }
		while (true) {
			//等待客户端连接
			Socket socket = serivce.accept();
			new Thread(new AndroidRunable(socket)).start();
		}
	}

}

class AndroidRunable implements Runnable {

	Socket socket = null;
	String ans="";
	DBHelper db=new DBHelper();

	public AndroidRunable(Socket socket) {
		this.socket = socket; 
	}

	@Override
	public void run() {
		String line = null;
		String file="";
		
		
		InputStream input;
		OutputStream output;
		try {
			input = socket.getInputStream();
			BufferedReader bff = new BufferedReader(
					new InputStreamReader(input));
			output = socket.getOutputStream();
			
			/*模拟日志*/
			String[] invi_time=new String[100];
			String[] invi_title=new String[100];
			String[] invi_context=new String[100];
			String[] invi_id=new String[100];
			String[] invi_kind=new String[100];
			int i,j;
			for (i=0;i<100;i++)
			{
				invi_time[i]="1994/5/"+String.valueOf(i);
				invi_title[i]="这是第"+String.valueOf(i)+"条日志";
				invi_context[i]="我们今天去了第"+String.valueOf(i)+"个景区";
				invi_id[i]="1000"+String.valueOf(i % 5);
				invi_kind[i]=String.valueOf(i % 2);
			}
			int check=0;
			String[] lines;
			while ((line = bff.readLine()) != null) {
				ans=line;
				
				if (check==0)
				{
					switch(line)
					{
						case "login":
						{
							check=1;
							break;
						}
						case "register":
						{
							check=2;
							break;
						}
						case "invi1":
						{
							check=3;
							break;
						}
						case "write":
						{
							check=4;
							break;
						}
						case "read":
						{
							check=5;
							break;
						}
						case "think":
						{
							check=6;
							break;
						}
						case "loc":
						{
							check=7;
							break;
						}
						case "invi0":
						{
							check=8;
							break;
						}
						case "invi2":
						{
							check=9;
							break;
						}
						case "org":
						{
							check=10;
							break;
						}
						case "org2":
						{
							check=11;
							break;
						}
						case "team":
						{
							check=12;
							break;
						}
						case "view":
						{
							check=13;
							break;
						}
						case "mine":
						{
							check=14;
							break;
						}
						case "team_update":
						{
							check=15;
							break;
						}
						case "user_update":
						{
							check=16;
							break;
						}
						case "team_del":
						{
							check=17;
							break;
						}
						case "invite":
						{
							check=18;
							break;
						}
						case "find_mem":
						{
							check=19;
							break;
						}
						case "team_find":
						{
							check=20;
							break;
						}
						case "send_invi":
						{
							check=21;
							break;
						}
						case "zan":
						{
							check=22;
							break;
						}
					}
					if (check!=0)
						continue;
				}
			
				switch(check)
				{
					case 1:
					{
						lines=line.split(":");
						String ans="";
						ans=db.login(line);
						System.out.println(ans);
						output.write((ans+"\n").getBytes("UTF-8"));
						output.flush();
						break;
					}
					case 2:
					{
						ans=db.register(line);
						if (ans.equals("1"))
						{
							lines=line.split(":");
							System.out.println("用户"+lines[0]+"请求注册");
							System.out.println(ans);
						}
						else
						{
							lines=line.split(":");
							System.out.println("用户"+lines[0]+"请求注册");
							System.out.println(ans);
						}
						output.write((ans+"\n").getBytes("UTF-8"));
						output.flush();
						break;
					}
					case 3: //id:title:time
					{
						ans=db.invi1(line);
						String[] anses=ans.split("\n");
						for (i=0;i<anses.length;i++)
						{
							output.write((anses[i]+"\n").getBytes("UTF-8"));
							System.out.println(String.valueOf(i)+":"+anses[i]);
						}
						output.flush();
						break;
					}
					case 4:
					{
						
						ans=db.write(line);
						output.write((ans+"\n").getBytes("UTF-8"));
						output.flush();
						break;
					}
					case 5:  //title:context:time:author+ans_context:ans_time:ans_author
					{
						ans=db.read(line);
						output.write((ans+"\n").getBytes("UTF-8"));
						output.flush();
						break;
					}
					case 6:
					{
						ans=db.think(line);
						output.write((ans+"\n").getBytes("UTF-8"));
						output.flush();
						break;
					}
					case 7:
					{
						ans=db.loc(line);
						output.write((ans+"\n").getBytes("UTF-8"));
						output.flush();
						break;
					}
					case 8:
					{
						ans=db.invi0(line);
						String[] anses=ans.split("\n");
						for (i=0;i<anses.length;i++)
						{
							output.write((anses[i]+"\n").getBytes("UTF-8"));
							
						}
						output.flush();
						break;
					}
					case 9:
					{
						break;
					}
					case 10:
					{
						
						ans=db.org(line);
						output.write((ans+"\n").getBytes("UTF-8"));
						output.flush();
						/*
						lines=line.split(":");
						System.out.println("查找年龄在"+lines[0]+"~"+lines[1]+"之间的来自"+lines[2]+lines[1]+"的队友");
						output.write(("name1:age1:sex1:1:河北省:石家庄市\n").getBytes("UTF-8"));
						output.write(("name2:age2:sex2:2:河南省:河南市\n").getBytes("UTF-8"));
						output.write(("name3:age3:sex3:3:日本省:东京市\n").getBytes("UTF-8"));
						output.write(("name4:age4:sex4:4:山东省:泰安市\n").getBytes("UTF-8"));
						*/
						break;
					}
					case 11:
					{
						/*
						
						output.write(("11\n").getBytes("UTF-8"));
						*/
						ans=db.org2(line);
						output.write((ans+"\n").getBytes("UTF-8"));
						output.flush();
						break;
					}
					case 12:
					{
						ans=db.team(line);
						output.write((ans+"\n").getBytes("UTF-8"));
						output.flush();
						break;
					}
					case 13:
					{
						System.out.println(line);
						ans=db.view(line);
						System.out.println(ans);
						output.write((ans+"\n").getBytes("UTF-8"));
						output.flush();
						break;
					}
					case 14:
					{
						ans=db.mine(line);
						System.out.println(ans);
						output.write((ans+"\n").getBytes("UTF-8"));
						output.flush();
						break;
					}
					case 15:
					{
						System.out.println(line);
						ans=db.team_update(line);
						System.out.println(ans);
						output.write((ans+"\n").getBytes("UTF-8"));
						output.flush();
						break;
					}
					case 16:
					{
						ans=db.user_update(line);
						output.write((ans+"\n").getBytes("UTF-8"));
						output.flush();
						break;
					}
					case 17:
					{
						ans=db.team_del(line);
						output.write((ans+"\n").getBytes("UTF-8"));
						output.flush();
						break;
					}
					case 18:
					{
						ans=db.invite(line);
						output.write((ans+"\n").getBytes("UTF-8"));
						output.flush();
						break;
					}
					case 19:
					{
						ans=db.find_mem(line);
						output.write((ans+"\n").getBytes("UTF-8"));
						output.flush();
						break;
					}
					case 20:
					{
						ans=db.team_find(line);
						output.write((ans+"\n").getBytes("UTF-8"));
						output.flush();
						break;
					}
					case 21:
					{
						ans=db.send_invi(line);
						output.write((ans+"\n").getBytes("UTF-8"));
						output.flush();
						break;
					}
					case 22:
					{
						ans=db.zan(line);
						output.write((ans+"\n").getBytes("UTF-8"));
						output.flush();
						break;
					}
				}
				
			}
			//System.out.println(ans);
			output.close();
			bff.close();
			input.close();
			socket.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}