package com.example.travel_helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {
	
	Button VIP,think,org,evaluate,invi,team,more;
	User user;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		VIP = (Button) findViewById(R.id.VIP);
		think = (Button) findViewById(R.id.think);
		org = (Button) findViewById(R.id.org);
		evaluate = (Button) findViewById(R.id.evaluate);
		invi = (Button) findViewById(R.id.invi);
		team = (Button) findViewById(R.id.team);
		more = (Button) findViewById(R.id.more);
		
		user= (User)getApplication();
		
		VIP.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(MainActivity.this,VIP.class);
				startActivity(intent);
				finish();
			}
		});
		
		think.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				if (user.getUsername().equals("?"))
				{
					Toast.makeText(getApplicationContext(), "ÇëµÇÂ¼",Toast.LENGTH_SHORT).show();
				}
				else
				{
					Intent intent=new Intent(MainActivity.this,Think.class);
					startActivity(intent);
					finish();
				}		
			}
		});
		
		invi.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				if (user.getUsername().equals("?"))
				{
					Toast.makeText(getApplicationContext(), "ÇëµÇÂ¼",Toast.LENGTH_SHORT).show();
				}
				else
				{
					Intent intent=new Intent(MainActivity.this,Write.class);
					Bundle b=new Bundle();
					b.putString("invi_kind","0");
					b.putString("invi_author",user.getId());
					intent.putExtras(b);
					startActivity(intent);
					finish();
				}
			}
		});
		
		evaluate.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(MainActivity.this,Invi.class);
				Bundle b=new Bundle();
				b.putString("invi_kind","1");
				intent.putExtras(b);
				startActivity(intent);
				finish();
			}
		});
		
		
		org.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				if (user.getUsername().equals("?"))
				{
					Toast.makeText(getApplicationContext(), "ÇëµÇÂ¼",Toast.LENGTH_SHORT).show();
				}
				else
				{
					Intent intent=new Intent(MainActivity.this,Org.class);
					startActivity(intent);
					finish();
				}
			}
		});
		
		
		team.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				if (user.getUsername().equals("?"))
				{
					Toast.makeText(getApplicationContext(), "ÇëµÇÂ¼",Toast.LENGTH_SHORT).show();
				}
				else
				{
					Intent intent=new Intent(MainActivity.this,Team.class);
					startActivity(intent);
					finish();
				}
			}
		});
		
		/*
		more.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(MainActivity.this,More.class);
				startActivity(intent);
				finish();
			}
		});
		*/
	}
}
