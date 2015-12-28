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
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Team_find extends Activity {
	
	Button team_find_back,team_find_img,team_find_send,team_find_find;
	TextView team_find_user,mem_id,mem_name,mem_img;
	Socket socket = null;
	String buffer = "",name="",mem="",team_province,team_city;
	ListView team_find_lv;
	EditText team_find_name;
	int[] go=new int[20];
	int j;
	String[] names=new String[20];
	Spinner team_find_province,team_find_city;
	private List<String> province = new ArrayList<String>();
	private List<String> city = new ArrayList<String>();
	private ArrayAdapter<String> adapter_province,adapter_city;
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
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("mem_id", lines[1]);
					map.put("mem_name", lines[0]);
					names[i]=lines[1];
					map.put("mem_intro", lines[2]);
					map.put("mem_img", "");
					listItem.add(map);
				}
				
				SimpleAdapter mSimpleAdapter = new SimpleAdapter(Team_find.this,listItem,R.layout.mem_item,new String[] {"mem_id","mem_name", "mem_intro"},new int[] {R.id.mem_id,R.id.mem_name,R.id.mem_intro});
				team_find_lv.setAdapter(mSimpleAdapter);
				team_find_lv.setOnItemClickListener(
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
		setContentView(R.layout.team_find);
		
		team_find_back = (Button) findViewById(R.id.team_find_back);
		team_find_send = (Button) findViewById(R.id.team_find_send);
		team_find_img = (Button) findViewById(R.id.team_find_img);
		team_find_find = (Button) findViewById(R.id.team_find_find);
		team_find_user = (TextView) findViewById(R.id.team_find_user);
		team_find_province = (Spinner)findViewById(R.id.team_find_province);
		team_find_city = (Spinner)findViewById(R.id.team_find_city);
		team_find_lv = (ListView) findViewById(R.id.team_find_lv);
		
		user= (User)getApplication();
		
		for (j=0;j<20;j++)
			go[j]=0;
		
		if ((user.getUsername()).equals("?"))
			team_find_user.setText("登陆");
		else
			team_find_user.setText(user.getUsername());
		
		province.add("不限");    
		province.add("黑龙江");    
		province.add("河北");    
		province.add("福建");    
		province.add("安徽");
		city.add("不限");
		city.add("哈尔滨");
		city.add("石家庄");
		adapter_province = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, province); 
		adapter_province.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
		team_find_province.setAdapter(adapter_province);
		team_find_province.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){    
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {  
            	team_province=adapter_province.getItem(arg2);
            	arg0.setVisibility(View.VISIBLE);
            }
            public void onNothingSelected(AdapterView<?> arg0) {    
                // TODO Auto-generated method stub      
                arg0.setVisibility(View.VISIBLE);    
            }   
		});
		
		adapter_city = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, city); 
		adapter_city.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
		team_find_city.setAdapter(adapter_city);
		team_find_city.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){    
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {  
            	team_city=adapter_city.getItem(arg2);
            	arg0.setVisibility(View.VISIBLE);
            }
            public void onNothingSelected(AdapterView<?> arg0) {    
                // TODO Auto-generated method stub      
                arg0.setVisibility(View.VISIBLE);    
            }   
		});
		
		team_find_send.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				mem=user.getId();
				for (j=0;j<20;j++)
					if (go[j]==1)
					{
						mem+=":"+names[j];
					}
				new MyThread().start();	
				Toast.makeText(getApplicationContext(), "发送成功！",Toast.LENGTH_SHORT).show();
				Intent intent=new Intent(Team_find.this,Team.class);
				startActivity(intent);
				finish();
			}
		});
		
		team_find_find.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				name=team_province+":"+team_city;
				new MyThread2().start();	
			}
		});
		
		
		team_find_back.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(Team_find.this,Team.class);
				startActivity(intent);
				finish();
			}
		});
		
		team_find_img.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(Team_find.this,Mine.class);
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
				ou.write(("send_invi"+"\n").getBytes("gbk"));
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
				ou.write(("team_find"+"\n").getBytes("gbk"));
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