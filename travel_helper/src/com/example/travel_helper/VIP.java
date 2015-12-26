package com.example.travel_helper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class VIP extends Activity {
	
	Button vip_back,vip_img,vip_login,vip_register,vip_mine;
	TextView vip_user;
	User user;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vip);
		
		vip_back = (Button) findViewById(R.id.vip_back);
		vip_img = (Button) findViewById(R.id.vip_img);
		vip_login = (Button) findViewById(R.id.vip_login);
		vip_register = (Button) findViewById(R.id.vip_register);
		vip_mine = (Button) findViewById(R.id.vip_mine);
		vip_user = (TextView) findViewById(R.id.vip_user);
		
		user= (User)getApplication();
		
		/* 用户判别 */
		if ((user.getUsername()).equals("?"))
			vip_user.setText("登陆");
		else
			vip_user.setText(user.getUsername());
		
		vip_back.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(VIP.this,MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		vip_img.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				if (user.getUsername()=="?")
				{
					Intent intent=new Intent(VIP.this,Login.class);
					startActivity(intent);
					finish();
				}
				else
				{
					Intent intent=new Intent(VIP.this,Mine.class);
					Bundle b=new Bundle();
					b.putString("mine", user.getId());
					intent.putExtras(b);
					startActivity(intent);
					finish();
				}
			}
		});
		
		vip_login.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(VIP.this,Login.class);
				startActivity(intent);
				finish();
			}
		});
		
		vip_register.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				if (user.getUsername().equals("?"))
				{
					Intent intent=new Intent(VIP.this,Register.class);
					startActivity(intent);
					finish();
				}
				else
				{
					Toast.makeText(getApplicationContext(), "请登出之后再操作!",Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		vip_mine.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				if ((user.getUsername()).equals("?"))
				{
					Toast.makeText(getApplicationContext(), "请登录",Toast.LENGTH_SHORT).show();
				}
				else
				{
					Intent intent=new Intent(VIP.this,Mine.class);
					Bundle b=new Bundle();
					b.putString("mine", user.getId());
					intent.putExtras(b);
					startActivity(intent);
					finish();
				}
				
			}
		});
		
	}

}