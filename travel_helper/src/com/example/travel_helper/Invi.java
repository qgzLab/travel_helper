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

public class Invi extends Activity {
	Socket socket = null;
	String buffer = "",invi_kind,id;
	User user;
	Button invi_back,invi_img,invi_send;
	TextView invi_id,invi_title,invi_time,invi_user,txt1;
	private ListView invi_lv;
	
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
					map.put("invi_id", lines[0]);
					map.put("invi_title", lines[1]);
					map.put("invi_time", lines[2]);
					listItem.add(map);
				}
				SimpleAdapter mSimpleAdapter = new SimpleAdapter(Invi.this,listItem,R.layout.invi_item,new String[] {"invi_id","invi_title", "invi_time"},new int[] {R.id.invi_id,R.id.invi_title,R.id.invi_time});
				invi_lv.setAdapter(mSimpleAdapter);
				invi_lv.setOnItemClickListener(
					new OnItemClickListener() 
					{
						@Override
			            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) 
			            {
							invi_id = (TextView) arg1.findViewById(R.id.invi_id);
							if (invi_id.getText().toString().equals("?"))
							{
								if (invi_kind.equals("0"))
								{
									if (user.getId().equals(id))
									{
										Intent intent = new Intent(Invi.this, Write.class);
										Bundle b=new Bundle();
										b.putString("invi_kind", invi_kind);
										intent.putExtras(b);
										startActivity(intent);
										finish();
									}
									else
									{
										invi_send.setText("无权限");
									}
								}
							}
							else
							{
								Intent intent = new Intent(Invi.this, Read.class);
								Bundle b=new Bundle();
								b.putString("read", invi_id.getText().toString());
								b.putString("invi_kind", invi_kind);
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
		setContentView(R.layout.invi);
		
		invi_lv = (ListView) findViewById(R.id.invi_lv);	
		invi_send=(Button) findViewById(R.id.invi_send);
		invi_back=(Button) findViewById(R.id.invi_back);
		invi_img=(Button) findViewById(R.id.invi_img);
		invi_user = (TextView) findViewById(R.id.invi_user);
		txt1 = (TextView) findViewById(R.id.txt1);
		
		user = (User)getApplication();
		
		if ((user.getUsername()).equals("?"))
			invi_user.setText("登陆");
		else
			invi_user.setText(user.getUsername());
	   
		Bundle b = this.getIntent().getExtras();
		invi_kind=b.getString("invi_kind");
		id=b.getString("invi_author");
		
		if (invi_kind.equals("1"))
		{
			invi_send.setText("发攻略");
		}
		
		new MyThread().start();
		
		invi_send.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				if ((user.getUsername()).equals("?"))
				{
					invi_send.setText("请先登录!");
				}
				else if (invi_kind.equals("0"))
				{
					if (user.getId().equals(id))
					{
						Intent intent = new Intent(Invi.this, Write.class);
						Bundle b=new Bundle();
						b.putString("invi_kind", invi_kind);
						intent.putExtras(b);
						startActivity(intent);
						finish();
					}
					else
					{
						invi_send.setText("无权限");
					}
				}
				else if (invi_kind.equals("1") || invi_kind.equals("2"))
				{
					Intent intent = new Intent(Invi.this, Write.class);
					Bundle b=new Bundle();
					b.putString("invi_kind", invi_kind);
					intent.putExtras(b);
					startActivity(intent);
					finish();
				}
				
			}
		});
		
		invi_back.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				if (invi_kind.equals("0"))
				{
					Intent intent=new Intent(Invi.this,Mine.class);
					Bundle b=new Bundle();
					b.putString("mine", id);
					intent.putExtras(b);
					startActivity(intent);
					finish();
				}
				else if (invi_kind.equals("1"))
				{
					Intent intent=new Intent(Invi.this,MainActivity.class);
					startActivity(intent);
					finish();
				}
			}
		});
		
		invi_img.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				if (user.getUsername().equals("?"))
				{
					Intent intent=new Intent(Invi.this,Login.class);
					startActivity(intent);
					finish();
				}
				else
				{
					Intent intent=new Intent(Invi.this,Mine.class);
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
				
				ou.write(("invi"+invi_kind+"\n").getBytes("gbk"));
				ou.write((id+"\n").getBytes("gbk"));
				ou.flush();
				socket.shutdownOutput();
				
				
				String line = null;
				buffer="";
				while ((line = bff.readLine()) != null) 
				{
					buffer=buffer+line; //id:title:time+
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
