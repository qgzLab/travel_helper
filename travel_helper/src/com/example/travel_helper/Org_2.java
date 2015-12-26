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
import android.widget.Toast;

public class Org_2 extends Activity {
	
	Button org_2_back,org_2_img,org_2_send;
	TextView org_2_user,org_2_name,org_2_dest,mem_id,mem_img,mem_name;
	Socket socket = null;
	String team_name,team_province,team_city,team_mem="",mem;
	ListView org_2_lv;
	
	User user;
	
	public Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0x11) {
				Bundle bundle = msg.getData();
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.org_2);
		
		org_2_back = (Button) findViewById(R.id.org_2_back);
		org_2_send = (Button) findViewById(R.id.org_2_send);
		org_2_img = (Button) findViewById(R.id.org_2_img);
		org_2_user = (TextView) findViewById(R.id.org_2_user);
		org_2_name = (TextView) findViewById(R.id.org_2_name);
		org_2_dest = (TextView) findViewById(R.id.org_2_dest);
		org_2_lv = (ListView) findViewById(R.id.org_2_lv);
		
		
		
		user= (User)getApplication();
		
		if ((user.getUsername()).equals("?"))
			org_2_user.setText("登陆");
		else
			org_2_user.setText(user.getUsername());
		
		Bundle b = this.getIntent().getExtras();
		team_name=b.getString("team_name");
		team_province=b.getString("team_province");
		team_city=b.getString("team_city");
		mem=b.getString("team_mem");
		team_mem=user.getId();
		
		org_2_name.setText(team_name);
		org_2_dest.setText(team_province+"-"+team_city);
		
		String row[]=mem.split("\n");
		int i;
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String,Object>>();
		for (i=0;i<row.length;i++)
		{
			String lines[]=row[i].split(":");
			if (user.getId().equals(lines[3]))
			{
			}
			else
			{
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("mem_id", lines[3]);
				map.put("mem_name", lines[0]);
				map.put("mem_intro", lines[2]+"-"+lines[1]+"岁-"+lines[4]+"-"+lines[5]);
				map.put("mem_img", "");
				listItem.add(map);
			}
		}
		SimpleAdapter mSimpleAdapter = new SimpleAdapter(Org_2.this,listItem,R.layout.mem_item,new String[] {"mem_id","mem_name", "mem_intro"},new int[] {R.id.mem_id,R.id.mem_name,R.id.mem_intro});
		org_2_lv.setAdapter(mSimpleAdapter);
		org_2_lv.setOnItemClickListener(
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
						team_mem=team_mem+"&"+mem_id.getText().toString();
						mem_img.setBackgroundColor(android.graphics.Color.RED);
						mem_img.setText("已选择");
					}
	            }
			});
		
		
		
		org_2_send.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "创建成功",Toast.LENGTH_SHORT).show();
				new MyThread().start();
			}
		});
		
		
		org_2_back.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(Org_2.this,Org.class);
				startActivity(intent);
				finish();
			}
		});
		
		org_2_img.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				if (user.getUsername()=="?")
				{
					Intent intent=new Intent(Org_2.this,Login.class);
					startActivity(intent);
					finish();
				}
				else
				{
					Intent intent=new Intent(Org_2.this,Mine.class);
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
				ou.write(("org2"+"\n").getBytes("gbk"));
				ou.write((team_name+":"+team_mem+":"+team_province+":"+team_city+":"+user.getId()+"\n").getBytes("gbk"));
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
				myHandler.sendMessage(msg);
				
				bff.close();
				ou.close();
				socket.close();
				
				Intent intent=new Intent(Org_2.this,Team_view.class);
				Bundle b=new Bundle();
				b.putString("team_id", buffer);
				intent.putExtras(b);
				startActivity(intent);
				finish();
				
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