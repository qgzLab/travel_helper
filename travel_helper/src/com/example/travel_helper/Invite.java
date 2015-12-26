package com.example.travel_helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Invite extends Activity {
	
	Button invite_back,invite_img,invite_send,invite_find;
	TextView invite_user,mem_id,mem_name,mem_img;
	Socket socket = null;
	String buffer = "",name="",mem="",team_id="";
	ListView invite_lv;
	EditText invite_name;
	int[] go=new int[20];
	int j;
	String[] names=new String[20];
	User user;
	
	public Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0x11) {
				Bundle bundle = msg.getData();
				String str=bundle.getString("msg");
				int i;
				ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String,Object>>();
				String[] mems=str.split("\n");
				for (i=0;i<mems.length;i++)
				{
					String lines[]=mems[i].split(":");
					if (user.getId().equals(lines[3]))
					{
					}
					else
					{
						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put("mem_id", lines[3]);
						map.put("mem_name", lines[0]);
						names[i]=lines[3];
						map.put("mem_intro", lines[2]+"-"+lines[1]+"岁-"+lines[4]+"-"+lines[5]);
						map.put("mem_img", "");
						listItem.add(map);
					}
				}
				
				SimpleAdapter mSimpleAdapter = new SimpleAdapter(Invite.this,listItem,R.layout.mem_item,new String[] {"mem_id","mem_name", "mem_intro"},new int[] {R.id.mem_id,R.id.mem_name,R.id.mem_intro});
				invite_lv.setAdapter(mSimpleAdapter);
				invite_lv.setOnItemClickListener(
					new OnItemClickListener() 
					{
						@Override
			            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) 
			            {
							mem_id = (TextView) arg1.findViewById(R.id.mem_id);
							mem_name = (TextView) arg1.findViewById(R.id.mem_name);
							mem_img = (TextView) arg1.findViewById(R.id.mem_img);
							if (mem_name.equals("不存在符合条件的队友"))
							{	
							}
							else
							{
								if (go[arg2]==0)
								{
									go[arg2]=1;
									mem_img.setBackgroundColor(android.graphics.Color.GREEN);
									mem_img.setText("邀请");
								}
								else
								{
									go[arg2]=0;
									mem_img.setBackgroundColor(android.graphics.Color.WHITE);
									mem_img.setText("");
								}	
							}
			            }
					});
			}
		}
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.invite);
		
		invite_back = (Button) findViewById(R.id.invite_back);
		invite_send = (Button) findViewById(R.id.invite_send);
		invite_img = (Button) findViewById(R.id.invite_img);
		invite_find = (Button) findViewById(R.id.invite_find);
		invite_user = (TextView) findViewById(R.id.invite_user);
		invite_name = (EditText) findViewById(R.id.invite_name);
		invite_lv = (ListView) findViewById(R.id.invite_lv);
		
		user= (User)getApplication();
	
		Bundle b = this.getIntent().getExtras();
		team_id=b.getString("invite");
		
		for (j=0;j<20;j++)
			go[j]=0;
		
		if ((user.getUsername()).equals("?"))
			invite_user.setText("登陆");
		else
			invite_user.setText(user.getUsername());
		
		invite_send.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				mem=team_id;
				for (j=0;j<20;j++)
					if (go[j]==1)
					{
						mem+=":"+names[j];
					}
				new MyThread().start();	
				Toast.makeText(getApplicationContext(), "邀请成功！",Toast.LENGTH_SHORT).show();
				Intent intent=new Intent(Invite.this,Team_view.class);
				Bundle b=new Bundle();
				b.putString("team_id", team_id);
				intent.putExtras(b);
				startActivity(intent);
				finish();
			}
		});
		
		invite_find.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				name=invite_name.getText().toString();
				new MyThread2().start();	
			}
		});
		
		
		invite_back.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		invite_img.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(Invite.this,Mine.class);
				Bundle b=new Bundle();
				b.putString("mine", user.getId());
				intent.putExtras(b);
				startActivity(intent);
				finish();
			}
		});	
	}
	
	class MyThread extends Thread {
		public MyThread() {
		}

		@Override
		public void run() {
			//定义消息
			Message msg = new Message();
			msg.what = 0x11;
			Bundle bundle = new Bundle();
			bundle.clear();
			try {
				//连接服务器 并设置连接超时为5秒
				socket = new Socket();
				socket.connect(new InetSocketAddress(user.getIP(), 9999), 5000);
				//获取输入输出流
				OutputStream ou = socket.getOutputStream();
				BufferedReader bff = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
				ou.write(("invite"+"\n").getBytes("gbk"));
				ou.write((mem+"\n").getBytes("gbk"));
				ou.flush();
				socket.shutdownOutput();
				
				String line = null;
				
				while ((line = bff.readLine()) != null) 
				{
				}
				bff.close();
				ou.close();
				socket.close();
			} catch (SocketTimeoutException aa) {
				bundle.putString("msg", "服务器连接失败！请检查网络是否打开");
				msg.setData(bundle);
				myHandler.sendMessage(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	class MyThread2 extends Thread {
		public MyThread2() {
		}

		@Override
		public void run() {
			//定义消息
			Message msg = new Message();
			msg.what = 0x11;
			Bundle bundle = new Bundle();
			bundle.clear();
			try {
				//连接服务器 并设置连接超时为5秒
				socket = new Socket();
				socket.connect(new InetSocketAddress(user.getIP(), 9999), 5000);
				//获取输入输出流
				OutputStream ou = socket.getOutputStream();
				BufferedReader bff = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
				ou.write(("find_mem"+"\n").getBytes("gbk"));
				ou.write((name+"\n").getBytes("gbk"));
				ou.flush();
				socket.shutdownOutput();
				
				String line = null;
				buffer="";
				while ((line = bff.readLine()) != null) 
				{
					buffer+=line+"\n";
				}
				bundle.putString("msg", buffer);
				msg.setData(bundle);
				myHandler.sendMessage(msg);
				
				bff.close();
				ou.close();
				socket.close();
			} catch (SocketTimeoutException aa) {
				bundle.putString("msg", "服务器连接失败！请检查网络是否打开");
				msg.setData(bundle);
				myHandler.sendMessage(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}