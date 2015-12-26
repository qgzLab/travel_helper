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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class Team extends Activity {
	Socket socket = null;
	User user;
	Button team_back,team_img,team_send;
	TextView team_id,team_user;
	String id,buffer="";
	private ListView team_lv;
	
	
	public Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String,Object>>();
			String str="";
			int i;
			if (msg.what == 0x11) {
				Bundle bundle = msg.getData();
				str=bundle.getString("msg");
				String row[]=str.split("\n");
				for (i=0;i<row.length;i++)
				{
					String lines[]=row[i].split(":");
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("team_id", lines[0]);
					map.put("team_name", lines[1]);
					map.put("team_dest", lines[2]+"-"+lines[3]);
					map.put("team_lead", lines[4]);
					if (lines[5].equals("0"))
					{
						if (lines[4].equals(user.getId()))
							map.put("team_intro", "【组长】");
						else
							map.put("team_intro", "【组员】");
					}
					else if (lines[5].equals("1"))
						map.put("team_intro", "【请审核】");
					else if (lines[5].equals("2"))
						map.put("team_intro", "【待审核】");
					listItem.add(map);
				}
				SimpleAdapter mSimpleAdapter = new SimpleAdapter(Team.this,listItem,R.layout.team_item,new String[] {"team_id","team_name", "team_dest","team_lead","team_intro"},new int[] {R.id.team_item_id,R.id.team_item_name,R.id.team_item_dest,R.id.team_item_leader,R.id.team_item_intro});
				team_lv.setAdapter(mSimpleAdapter);
				team_lv.setOnItemClickListener(
					new OnItemClickListener() 
					{
						@Override
			            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) 
			            {
							team_id = (TextView) arg1.findViewById(R.id.team_item_id);
							if (team_id.getText().toString().equals(" "))
							{
								Intent intent=new Intent(Team.this,Org.class);
								startActivity(intent);
								finish();
							}
							else
							{
								Intent intent = new Intent(Team.this, Team_view.class);
								Bundle b=new Bundle();
								b.putString("team_id", team_id.getText().toString());
								intent.putExtras(b);
								startActivity(intent);  
							}          
			            }
					});
			}
					
			}
		};

	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.team);
		
		team_lv = (ListView) findViewById(R.id.team_lv);	
		team_back=(Button) findViewById(R.id.team_back);
		team_img=(Button) findViewById(R.id.team_img);
		team_send=(Button) findViewById(R.id.team_send);
		team_user = (TextView) findViewById(R.id.team_user);
		
		
		user = (User)getApplication();
		
		if ((user.getUsername()).equals("?"))
			team_user.setText("登陆");
		else
			team_user.setText(user.getUsername());
	   
		new MyThread().start();
		
		
		team_back.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(Team.this,MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		team_send.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(Team.this,Team_find.class);
				startActivity(intent);
				finish();
			}
		});
		
		team_img.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				if (user.getUsername().equals("?"))
				{
					Intent intent=new Intent(Team.this,Login.class);
					startActivity(intent);
					finish();
				}
				else
				{
					Intent intent=new Intent(Team.this,Mine.class);
					Bundle b=new Bundle();
					b.putString("mine", user.getId());
					intent.putExtras(b);
					startActivity(intent);
					finish();
				}
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
				
				ou.write(("team"+"\n").getBytes("gbk"));
				ou.write((user.getId()+"\n").getBytes("gbk"));
				ou.flush();
				socket.shutdownOutput();
				
				
				String line = null;
				buffer="";
				while ((line = bff.readLine()) != null) 
				{
					buffer=buffer+line;
					buffer=buffer+"\n";
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
