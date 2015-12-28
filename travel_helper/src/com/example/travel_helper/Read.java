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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class Read extends Activity {
	
	Button read_back,read_img,read_send,read_zan;
	TextView read_user,read_title,read_context,read_time,read_author,read_num;
	Socket socket = null;
	String id,invi_kind;
	ListView read_lv;
	
	
	User user;
	
	public Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0x11) {
				ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String,Object>>();
				Bundle bundle = msg.getData();
				String[] lines;
				int i;
				lines=bundle.getString("msg").split("\n");
				
				//主要内容
				String[] main;
				main=lines[0].split(":");
				read_title.setText(main[0]);
				read_context.setText(main[1]);
				if (invi_kind.equals("1"))
				{
					read_time.setText(main[2]+"于"+main[4]+" "+main[5]+" "+main[6]);
				}
				else if (invi_kind.equals("0"))
				{
					read_time.setText(main[2]);
				}
				read_author.setText(main[3]);
				read_num.setText("("+main[7]+")");
				
				//评论内容  ans_context:ans_time:ans_author
				for (i=1;i<lines.length;i++)
				{
					main=lines[i].split(":");
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("ans_context", main[0]);
					map.put("ans_time", main[1]);
					map.put("ans_author", main[2]);
					listItem.add(map);
				}
				SimpleAdapter mSimpleAdapter = new SimpleAdapter(Read.this,listItem,R.layout.ans_item,new String[] {"ans_context", "ans_time","ans_author"},new int[] {R.id.ans_context,R.id.ans_time,R.id.ans_author});
				read_lv.setAdapter(mSimpleAdapter);
			}
		}
	};
	
	public Handler myHandler2 = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0x11) {
				Bundle bundle = msg.getData();
				String s=bundle.getString("msg");
				
				read_num.setText("("+s+")");
			}
		}
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.read);
		
		read_back = (Button) findViewById(R.id.read_back);
		read_send = (Button) findViewById(R.id.read_send);
		read_zan = (Button) findViewById(R.id.read_zan);
		read_img = (Button) findViewById(R.id.read_img);
		read_user = (TextView) findViewById(R.id.read_user);
		read_title = (TextView) findViewById(R.id.read_title);
		read_context = (TextView) findViewById(R.id.read_context);
		read_time = (TextView) findViewById(R.id.read_time);
		read_author = (TextView) findViewById(R.id.read_author);
		read_num = (TextView) findViewById(R.id.read_num);
		read_lv = (ListView) findViewById(R.id.read_lv);
		
		
		user= (User)getApplication();
		
		if ((user.getUsername()).equals("?"))
			read_user.setText("登陆");
		else
			read_user.setText(user.getUsername());
		
		Bundle b = this.getIntent().getExtras();
		id=b.getString("read");
		invi_kind=b.getString("invi_kind");
		
		new MyThread().start();
		
		read_send.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				if ((user.getUsername()).equals("?"))
				{
					Toast.makeText(getApplicationContext(), "请登录",Toast.LENGTH_SHORT).show();
				}
				else
				{
					Intent intent=new Intent(Read.this,Write.class);
					Bundle b=new Bundle();
					if (invi_kind.equals("0"))
						b.putString("invi_kind", "3");
					else if (invi_kind.equals("1"))
						b.putString("invi_kind", "2");
					b.putString("id", id);
					intent.putExtras(b);
					startActivity(intent);
					finish();
				}
			}
		});
		
		
		read_back.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		read_zan.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "点赞成功!",Toast.LENGTH_SHORT).show();
				new MyThread2().start();
			}
		});
		
		read_img.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				if (user.getUsername().equals("?"))
				{
					Intent intent=new Intent(Read.this,Login.class);
					startActivity(intent);
					finish();
				}
				else
				{
					Intent intent=new Intent(Read.this,Mine.class);
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
				ou.write(("read"+"\n").getBytes("gbk"));
				ou.write((id+":"+invi_kind+"\n").getBytes("gbk"));
				ou.flush();
				socket.shutdownOutput();
				
				String line = null;
				
				String buffer="";
				while ((line = bff.readLine()) != null)
				{
					 buffer+=line+"\n";	//title:context:time:author:province:city:place+ans_context:ans_time:ans_author
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
				ou.write(("zan"+"\n").getBytes("gbk"));
				ou.write((id+":"+invi_kind+"\n").getBytes("gbk"));
				ou.flush();
				socket.shutdownOutput();
				
				String line = null;
				
				String buffer="";
				while ((line = bff.readLine()) != null)
				{
					 buffer=line;
				}
				bundle.putString("msg", buffer);
				msg.setData(bundle);
				myHandler2.sendMessage(msg);
				
				bff.close();
				ou.close();
				socket.close();
			} catch (SocketTimeoutException aa) {
				bundle.putString("msg", "服务器连接失败！请检查网络是否打开");
				msg.setData(bundle);
				myHandler2.sendMessage(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}