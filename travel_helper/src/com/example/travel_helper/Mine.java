package com.example.travel_helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

import com.example.travel_helper.Login.MyThread;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Mine extends Activity {
	
	Socket socket = null;
	Button mine_back,mine_img,mine_diary,mine_update;
	TextView mine_user,mine_username,mine_email,mine_phone,mine_qq,mine_sex,mine_birth,mine_live;
	String username,email,phone,qq,sex,birth,live,id,mine;
	User user;
	
	public Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String str;
			int i;
			if (msg.what == 0x11) {
				Bundle bundle = msg.getData();
				str=bundle.getString("msg");
				String[] mines=str.split(":");
				mine_username.setText(mines[0]);
				mine_email.setText(mines[1]);
				mine_phone.setText(mines[2]);
				mine_qq.setText(mines[3]);
				mine_sex.setText(mines[4]);
				mine_birth.setText(mines[5]);
				mine_live.setText(mines[6]);
				id=mine;
				
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mine);
		
		mine_back = (Button) findViewById(R.id.mine_back);
		mine_img = (Button) findViewById(R.id.mine_img);
		mine_diary = (Button) findViewById(R.id.mine_diary);
		mine_update = (Button) findViewById(R.id.mine_update);
		mine_user = (TextView) findViewById(R.id.mine_user);
		mine_username = (TextView) findViewById(R.id.mine_username);
		mine_email = (TextView) findViewById(R.id.mine_email);
		mine_phone = (TextView) findViewById(R.id.mine_phone);
		mine_qq = (TextView) findViewById(R.id.mine_qq);
		mine_sex = (TextView) findViewById(R.id.mine_sex);
		mine_birth = (TextView) findViewById(R.id.mine_birth);
		mine_live = (TextView) findViewById(R.id.mine_live);
		
		user= (User)getApplication();
		
		/* 用户判别 */
		if ((user.getUsername()).equals("?"))
		{
			mine_user.setText("登陆");
			mine_diary.setEnabled(false);
		}
		else
		{
			mine_user.setText("登出");
		}
			
		
		Bundle b = this.getIntent().getExtras();
		mine=b.getString("mine");
		if ((user.getId()).equals(mine))
		{
			mine_update.setEnabled(true);
		}
		else
		{
			mine_update.setEnabled(false);
			mine_update.setBackgroundColor(0xffffff);
		}
		
		new MyThread().start();
		
		mine_update.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(Mine.this,Register.class);
				startActivity(intent);
				finish();
			}
		});
		mine_back.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				if (mine.equals(user.getId()))
				{
					Intent intent=new Intent(Mine.this,VIP.class);
					startActivity(intent);
					finish();
				}
				else
				{
					finish();
				}
				
			}
		});
		
		mine_img.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				if (user.getUsername().equals("?"))
				{
					Intent intent=new Intent(Mine.this,Login.class);
					startActivity(intent);
					finish();
				}
				else
				{
					Toast.makeText(getApplicationContext(), "登出成功!",Toast.LENGTH_SHORT).show();
					Intent intent=new Intent(Mine.this,MainActivity.class);
					user.setUsername("?");
					startActivity(intent);
					finish();
				}
			}
		});
		
		mine_diary.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(Mine.this,Invi.class);
				Bundle b=new Bundle();
				b.putString("invi_kind","0");
				b.putString("invi_author",mine);
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
				
				ou.write(("mine"+"\n").getBytes("gbk"));
				ou.write((mine+"\n").getBytes("gbk"));
				ou.flush();
				socket.shutdownOutput();
				
				
				String line = null;
				String buffer="";
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