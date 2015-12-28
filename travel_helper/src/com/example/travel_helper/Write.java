package com.example.travel_helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class Write extends Activity {
	
	Button write_back,write_img,write_send;
	TextView write_user;
	Socket socket = null;
	String invi_kind,id;
	String invi_title,invi_context,invi_time,invi_author,invi_answer,invi_province,invi_city,invi_place;
	EditText write_title,write_context;
	
	private List<String> province = new ArrayList<String>();
	private List<String> city = new ArrayList<String>();
	private List<String> place = new ArrayList<String>();
    private Spinner write_province,write_city,write_place;    
    private ArrayAdapter<String> adapter_province,adapter_city,adapter_place;   
	
	
	User user;
	
	public Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0x11) {
				Bundle bundle = msg.getData();
				write_send.setText(bundle.getString("msg")+"\n");
			}
		}
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.write);
		
		write_back = (Button) findViewById(R.id.write_back);
		write_send = (Button) findViewById(R.id.write_send);
		write_img = (Button) findViewById(R.id.write_img);
		write_user = (TextView) findViewById(R.id.write_user);
		write_title = (EditText) findViewById(R.id.write_title);
		write_context = (EditText) findViewById(R.id.write_context);
		write_province = (Spinner)findViewById(R.id.write_province); 
		write_city = (Spinner)findViewById(R.id.write_city); 
		write_place = (Spinner)findViewById(R.id.write_place); 
		
		user= (User)getApplication();
		
		if ((user.getUsername()).equals("?"))
			write_user.setText("登陆");
		else
			write_user.setText(user.getUsername());
		
		
		//spinner初始化
		province.add("北京");    
		province.add("黑龙江");    
		province.add("河北");    
		province.add("福建");    
		province.add("安徽");
		city.add("哈尔滨");
		city.add("石家庄");
		city.add("漳州");
		city.add("亳州");
		place.add("太阳岛");
		place.add("太行山");
		place.add("海峡两岸");
		place.add("萌萌家");
		adapter_province = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, province); 
		adapter_province.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
		write_province.setAdapter(adapter_province);
		write_province.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){    
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {  
            	invi_province=adapter_province.getItem(arg2);
            	arg0.setVisibility(View.VISIBLE);
            }
            public void onNothingSelected(AdapterView<?> arg0) {    
                // TODO Auto-generated method stub      
                arg0.setVisibility(View.VISIBLE);    
            }   
		});
		
		adapter_city = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, city); 
		adapter_city.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
		write_city.setAdapter(adapter_city);
		write_city.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){    
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {  
            	invi_city=adapter_city.getItem(arg2);
            	arg0.setVisibility(View.VISIBLE);
            }
            public void onNothingSelected(AdapterView<?> arg0) {    
                // TODO Auto-generated method stub      
                arg0.setVisibility(View.VISIBLE);    
            }   
		});
		
		
		adapter_place = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, place); 
		adapter_place.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
		write_place.setAdapter(adapter_place);
		write_place.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){    
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {  
            	invi_place=adapter_place.getItem(arg2);
            	arg0.setVisibility(View.VISIBLE);
            }
            public void onNothingSelected(AdapterView<?> arg0) {    
                // TODO Auto-generated method stub      
                arg0.setVisibility(View.VISIBLE);    
            }   
		});
		
		Bundle b = this.getIntent().getExtras();
		invi_kind=b.getString("invi_kind");
		if (invi_kind.equals("2") || invi_kind.equals("3"))
		{
			id=b.getString("id");
		}
		
		write_send.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				Calendar cal = Calendar.getInstance();
				invi_title=write_title.getText().toString();
				invi_context=write_context.getText().toString();
				invi_time=String.valueOf(cal.get(Calendar.YEAR))+"/"+String.valueOf(cal.get(Calendar.MONTH))+"/"+String.valueOf(cal.get(Calendar.DATE));
				invi_author=user.getUsername();
				invi_answer="";
				
				new MyThread().start();
				
			}
		});
		
		
		write_back.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				if (invi_kind.equals("2"))
				{
					Intent intent=new Intent(Write.this,Read.class);
					Bundle b=new Bundle();
					b.putString("read", id);
					b.putString("invi_kind", invi_kind);
					intent.putExtras(b);
					startActivity(intent);
					finish();
				}
				else
				{
					Intent intent=new Intent(Write.this,Invi.class);
					Bundle b=new Bundle();
					b.putString("invi_kind", invi_kind);
					b.putString("invi_author", user.getId());
					intent.putExtras(b);
					startActivity(intent);
					finish();
				}
			}
		});
		
		write_img.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				if (user.getUsername()=="?")
				{
					Intent intent=new Intent(Write.this,Login.class);
					startActivity(intent);
					finish();
				}
				else
				{
					Intent intent=new Intent(Write.this,Mine.class);
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
				ou.write(("write"+"\n").getBytes("gbk"));
				if (invi_kind.equals("0"))
				{
					ou.write((invi_kind+":"+invi_title+":"+invi_context+":"+invi_time+":"+invi_province+":"+invi_city+":"+invi_place+":"+user.getId()+"\n").getBytes("gbk"));
					ou.flush();
				}
				else if (invi_kind.equals("1"))
				{
					ou.write((invi_kind+":"+invi_title+":"+invi_context+":"+invi_time+":"+user.getId()+":"+invi_province+":"+invi_city+":"+invi_place+"\n").getBytes("gbk"));
					ou.flush();
				}
				else if (invi_kind.equals("2"))
				{
					ou.write((invi_kind+":"+invi_context+":"+invi_time+":"+id+":"+user.getId()+"\n").getBytes("gbk"));
					ou.flush();
				}
				else if (invi_kind.equals("3"))
				{
					ou.write((invi_kind+":"+invi_context+":"+invi_time+":"+id+":"+user.getId()+"\n").getBytes("gbk"));
					ou.flush();
				}
				
				socket.shutdownOutput();
				
				String line = null;
				
				String buffer="";
				while ((line = bff.readLine()) != null) 
				{
					 buffer+=line;	
				}
				bundle.putString("msg", buffer);
				msg.setData(bundle);
				myHandler.sendMessage(msg);
				
				if (buffer.equals("发表成功"))
				{
					if (invi_kind.equals("2") || invi_kind.equals("3"))
					{
						Intent intent=new Intent(Write.this,Read.class);
						Bundle b=new Bundle();
						b.putString("read", id);
						if (invi_kind.equals("2"))
							b.putString("invi_kind","1");
						else 
							b.putString("invi_kind","0");
						intent.putExtras(b);
						startActivity(intent);
						finish();
					}
					else
					{
						Intent intent=new Intent(Write.this,Invi.class);
						Bundle b=new Bundle();
						b.putString("invi_kind",invi_kind);
						b.putString("invi_author", user.getId());
						intent.putExtras(b);
						startActivity(intent);
						finish();
					}
					
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
}