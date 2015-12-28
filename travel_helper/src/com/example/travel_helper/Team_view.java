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
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class Team_view extends Activity {
	Socket socket = null,socket2 = null;
	User user;
	Button view_back,view_img,view_send;
	TextView view_user,view_name,view_dest,view_id;
	private ListView view_lv;
	String team_id,buffer,leader_id,note="";
	int[] kind=new int[100];
	int a;
	
	
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
				String lines[];
				lines=row[0].split(":");
				view_name.setText(lines[0]);
				view_dest.setText(lines[1]+"-"+lines[2]);
				leader_id=lines[3];
				for (i=1;i<row.length;i++)
				{
					lines=row[i].split(":");
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("mem_id", lines[3]);
					if (lines[3].equals(user.getId()))
						a=Integer.parseInt(lines[6]);
					if (lines[6].equals("0"))
						map.put("mem_name", lines[0]+"【组长】");
					else if (lines[6].equals("1"))
						map.put("mem_name", lines[0]+"【组员】");
					else if (lines[6].equals("2"))
						map.put("mem_name", lines[0]+"【待审核】");
					else if (lines[6].equals("3"))
						map.put("mem_name", lines[0]+"【待审核】");
					map.put("mem_intro", lines[2]+"-"+lines[1]+"岁-"+lines[4]+"-"+lines[5]);
					kind[i-1]=Integer.parseInt(lines[6]);
					listItem.add(map);
				}
				SimpleAdapter mSimpleAdapter = new SimpleAdapter(Team_view.this,listItem,R.layout.mem_item,new String[] {"mem_name","mem_intro", "mem_id"},new int[] {R.id.mem_name,R.id.mem_intro,R.id.mem_id});
				view_lv.setAdapter(mSimpleAdapter);
				
				view_lv.setOnItemClickListener(
						new OnItemClickListener() 
						{
							@Override
				            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) 
				            {
								view_id = (TextView) arg1.findViewById(R.id.mem_id);
								AlertDialog.Builder builder = new AlertDialog.Builder(Team_view.this);
								builder.setTitle("请操作");
								View view = LayoutInflater.from(Team_view.this).inflate(R.layout.dialog_creat, null);
								builder.setView(view);
								builder.setPositiveButton("查看信息", new DialogInterface.OnClickListener()
				                {
									@Override
				                    public void onClick(DialogInterface dialog, int which)
				                    {
										if (a==2 || a==3)
										{
											Toast.makeText(getApplicationContext(), "您还没有此权限，成为正式组员之后才可查看详细信息！",Toast.LENGTH_SHORT).show();
										}
										else
										{
											Intent intent = new Intent(Team_view.this, Mine.class);
											Bundle b=new Bundle();
											b.putString("mine", view_id.getText().toString());
											intent.putExtras(b);
											startActivity(intent);
										}
				                    } 
									
				                });
								note="取消";
								if ((user.getId()).equals(leader_id))
								{
									switch (kind[arg2])
									{
									case 0: note="取消";
									break;
									case 1: note="剔除";
									break;
									case 2: note="剔除";
									break;
									case 3: note="审核通过";
									break;
									}
										
								}
								else
								{
									if (kind[arg2]==2 && view_id.getText().toString().equals(user.getId()))
									{
										note="确认邀请";
									}
									else if (kind[arg2]==1 && view_id.getText().toString().equals(user.getId()))
									{
										note="退出";
									}
									else if (kind[arg2]==3 && view_id.getText().toString().equals(user.getId()))
									{
										note="取消审核";
									}
								}
								
								if (note.equals("退出") || note.equals("剔除"))
								{
									builder.setNegativeButton(note, new DialogInterface.OnClickListener()
					                {
					                    @Override
					                    public void onClick(DialogInterface dialog, int which)
					                    {
					                    	Intent intent = new Intent(Team_view.this, Teamdel.class);
											Bundle b=new Bundle();
											b.putString("note", view_id.getText().toString()+":"+team_id);
											intent.putExtras(b);
											startActivity(intent);
											finish();
					                    }
					                });
								}
								else if (note.equals("取消"))
								{
									builder.setNegativeButton(note, new DialogInterface.OnClickListener()
					                {
					                    @Override
					                    public void onClick(DialogInterface dialog, int which)
					                    {
					                    }
					                });
								}
								else
								{
									builder.setNegativeButton(note, new DialogInterface.OnClickListener()
					                {
					                    @Override
					                    public void onClick(DialogInterface dialog, int which)
					                    {
					                    	Toast.makeText(getApplicationContext(), "审核成功！",Toast.LENGTH_SHORT).show();
					                    	Intent intent = new Intent(Team_view.this, Update.class);
											Bundle b=new Bundle();
											b.putString("note", view_id.getText().toString()+":"+team_id);
											intent.putExtras(b);
											startActivity(intent);
											finish();
					                    }
					                });
								}
				                
				                builder.show();
				            }
						});
						
			}
					
			}
		};

	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.team_view);
		
		view_lv = (ListView) findViewById(R.id.view_lv);	
		view_back=(Button) findViewById(R.id.view_back);
		view_send=(Button) findViewById(R.id.view_send);
		view_img=(Button) findViewById(R.id.view_img);
		view_user = (TextView) findViewById(R.id.view_user);
		view_name = (TextView) findViewById(R.id.view_name);
		view_dest = (TextView) findViewById(R.id.view_dest);
		
		user = (User)getApplication();
		
		if ((user.getUsername()).equals("?"))
			view_user.setText("登陆");
		else
			view_user.setText(user.getUsername());
		
		Bundle b = this.getIntent().getExtras();
		team_id=b.getString("team_id");
	   
		new MyThread().start();
		
		
		
		view_back.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(Team_view.this,Team.class);
				startActivity(intent);
				finish();
			}
		});
		
		view_send.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(Team_view.this,Invite.class);
				Bundle b=new Bundle();
				b.putString("invite", team_id);
				intent.putExtras(b);
				startActivity(intent);
				finish();
			}
		});
		
		view_img.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				if (user.getUsername().equals("?"))
				{
					Intent intent=new Intent(Team_view.this,Login.class);
					startActivity(intent);
					finish();
				}
				else
				{
					Intent intent=new Intent(Team_view.this,Mine.class);
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
				
				ou.write(("view"+"\n").getBytes("gbk"));
				ou.write((team_id+"\n").getBytes("gbk"));
				ou.flush();
				socket.shutdownOutput();
				
				
				String line = null;
				buffer="";
				while ((line = bff.readLine()) != null) 
				{
					buffer=buffer+line;
					buffer=buffer+"\n";
				}
								
				bff.close();
				ou.close();
				socket.close();
				bundle.putString("msg", buffer);
				msg.setData(bundle);
				myHandler.sendMessage(msg);
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
