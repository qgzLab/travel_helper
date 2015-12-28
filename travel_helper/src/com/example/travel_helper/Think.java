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
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class Think extends Activity {
	
	Button think_back,think_img,think_send;
	TextView think_user,place_id;
	ListView think_lv;
	String high,low;
	private List<String> province = new ArrayList<String>();
	private List<String> city = new ArrayList<String>();
	//private List<String> place = new ArrayList<String>();
	private List<String> price = new ArrayList<String>();
	private Spinner think_province,think_city,think_price,think_place; 
	private ArrayAdapter<String> adapter_province,adapter_city,adapter_price,adapter_place;
	String loc_province,loc_city,loc_price,loc_place;
	Socket socket = null;
	
	
	User user;
	
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
					//int path=getResources().getIdentifier("ic_launcher.png","drawable","com.example.travel_helper");
					//Drawable drawable=getResources().getDrawable(path);
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("place_id", lines[0]);
					map.put("place_name", lines[1]);
					map.put("place_price", lines[2]);
					//map.put("place_img", drawable);
					listItem.add(map);
				}
				SimpleAdapter mSimpleAdapter = new SimpleAdapter(Think.this,listItem,R.layout.place_item,new String[] {"place_id","place_name", "place_price"/*,"place_img"*/},new int[] {R.id.place_id,R.id.place_name,R.id.place_price/*,R.id.place_img*/});
				think_lv.setAdapter(mSimpleAdapter);
				think_lv.setOnItemClickListener(
					new OnItemClickListener() 
					{
						@Override
			            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) 
			            {
							Intent intent = new Intent(Think.this, Read.class);
							Bundle b=new Bundle();
							place_id = (TextView) arg1.findViewById(R.id.place_id);
							b.putString("read", place_id.getText().toString());
							b.putString("invi_kind","1");
							intent.putExtras(b);
							startActivity(intent);              
			            }
					});
			}
					
			}
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.think);
		
		think_back = (Button) findViewById(R.id.think_back);
		think_send = (Button) findViewById(R.id.think_send);
		think_img = (Button) findViewById(R.id.think_img);
		think_user = (TextView) findViewById(R.id.think_user);
		think_province = (Spinner)findViewById(R.id.think_province); 
		think_city = (Spinner)findViewById(R.id.think_city); 
		DatePicker date_low=(DatePicker)findViewById(R.id.date_low);
		DatePicker date_high=(DatePicker)findViewById(R.id.date_high);
		think_lv = (ListView) findViewById(R.id.think_lv);
		think_price = (Spinner) findViewById(R.id.think_price);
		//think_place = (Spinner) findViewById(R.id.think_place);
		
		province.add("北京");    
		province.add("黑龙江");    
		province.add("河北");    
		province.add("福建");    
		province.add("安徽");
		city.add("哈尔滨");
		city.add("石家庄");
		city.add("漳州");
		city.add("亳州");
		//place.add("太阳岛");
		//place.add("太行山");
		//place.add("海峡两岸");
		//place.add("萌萌家");
		price.add("0-200元");
		price.add("200-500元");
		price.add("500-1000元");
		price.add("1000-1500元");
		price.add("1500-2500元");
		price.add("2500-4000元");
		price.add("4000元以上");
		adapter_province = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, province); 
		adapter_province.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
		think_province.setAdapter(adapter_province);
		think_province.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){    
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {  
            	loc_province=adapter_province.getItem(arg2);
            	arg0.setVisibility(View.VISIBLE);
            }
            public void onNothingSelected(AdapterView<?> arg0) {    
                // TODO Auto-generated method stub      
                arg0.setVisibility(View.VISIBLE);    
            }   
		});
		
		adapter_city = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, city); 
		adapter_city.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
		think_city.setAdapter(adapter_city);
		think_city.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){    
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {  
            	loc_city=adapter_city.getItem(arg2);
            	arg0.setVisibility(View.VISIBLE);
            }
            public void onNothingSelected(AdapterView<?> arg0) {    
                // TODO Auto-generated method stub      
                arg0.setVisibility(View.VISIBLE);    
            }   
		});
		
		/*
		adapter_place = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, place); 
		adapter_place.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
		think_place.setAdapter(adapter_place);
		think_place.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){    
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {  
            	loc_place=adapter_place.getItem(arg2);
            	arg0.setVisibility(View.VISIBLE);
            }
            public void onNothingSelected(AdapterView<?> arg0) {    
                // TODO Auto-generated method stub      
                arg0.setVisibility(View.VISIBLE);    
            }   
		});
		*/
		
		adapter_price = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, price); 
		adapter_price.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
		think_price.setAdapter(adapter_price);
		think_price.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){    
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {  
            	loc_price=adapter_price.getItem(arg2);
            	if (loc_price.equals("0-200元"))
            		loc_price="1";
            	else if (loc_price.equals("0-200元"))
            		loc_price="2";
            	else if (loc_price.equals("200-500元"))
            		loc_price="3";
            	else if (loc_price.equals("500-1000元"))
            		loc_price="4";
            	else if (loc_price.equals("1000-1500元"))
            		loc_price="5";
            	else if (loc_price.equals("1500-2500元"))
            		loc_price="6";
            	else if (loc_price.equals("2500-4000元"))
            		loc_price="7";
            	else
            		loc_price="8";
            	arg0.setVisibility(View.VISIBLE);
            }
            public void onNothingSelected(AdapterView<?> arg0) {    
                // TODO Auto-generated method stub      
                arg0.setVisibility(View.VISIBLE);    
            }   
		});
		
		user= (User)getApplication();
		
		if ((user.getUsername()).equals("?"))
			think_user.setText("登陆");
		else
			think_user.setText(user.getUsername());
		
		Calendar calendar=Calendar.getInstance();
        int year=calendar.get(Calendar.YEAR);
        int monthOfYear=calendar.get(Calendar.MONTH);
        int dayOfMonth=calendar.get(Calendar.DAY_OF_MONTH);
        low=String.valueOf(year)+String.valueOf(monthOfYear)+String.valueOf(dayOfMonth);
        date_low.init(year, monthOfYear, dayOfMonth, new OnDateChangedListener(){
            public void onDateChanged(DatePicker view, int year,
                    int monthOfYear, int dayOfMonth) {
                low=String.valueOf(year)+String.valueOf(monthOfYear)+String.valueOf(dayOfMonth);
            }
            
        });
        high=String.valueOf(year)+String.valueOf(monthOfYear)+String.valueOf(dayOfMonth);
        date_high.init(year, monthOfYear, dayOfMonth, new OnDateChangedListener(){
            public void onDateChanged(DatePicker view, int year,
                    int monthOfYear, int dayOfMonth) {
                high=String.valueOf(year)+String.valueOf(monthOfYear)+String.valueOf(dayOfMonth);
            }
        });
		
		think_send.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				new MyThread().start();
			}
		});
		
		
		think_back.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(Think.this,MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		think_img.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				if (user.getUsername()=="?")
				{
					Intent intent=new Intent(Think.this,Login.class);
					startActivity(intent);
					finish();
				}
				else
				{
					Intent intent=new Intent(Think.this,Mine.class);
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
				ou.write(("think"+"\n").getBytes("gbk"));
				ou.write((loc_province+":"+loc_city+":"+low+":"+high+":"+loc_price+":"+user.getId()+"\n").getBytes("gbk"));
				ou.flush();
				socket.shutdownOutput();
				
				String line = null;
				
				String buffer="";
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