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
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Org extends Activity {
	
	Button org_back,org_img,org_send;
	TextView org_user;
	EditText org_low,org_high,org_name;
	Socket socket = null;
	
	private List<String> province = new ArrayList<String>();
	private List<String> city = new ArrayList<String>();
	private List<String> place = new ArrayList<String>();
	private List<String> price = new ArrayList<String>();
    private Spinner org_province,org_city,org_price;    
    private ArrayAdapter<String> adapter_province,adapter_city,adapter_price;  
    String team_name="",team_province="",team_city="",time_low="",time_high="",team_price="";
	
	
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
		setContentView(R.layout.org);
		
		org_back = (Button) findViewById(R.id.org_back);
		org_send = (Button) findViewById(R.id.org_send);
		org_img = (Button) findViewById(R.id.org_img);
		org_user = (TextView) findViewById(R.id.org_user); 
		org_price = (Spinner)findViewById(R.id.org_price);
		org_province = (Spinner)findViewById(R.id.org_province);
		org_city = (Spinner)findViewById(R.id.org_city);
		org_name = (EditText)findViewById(R.id.org_name);
		DatePicker date_low=(DatePicker)findViewById(R.id.org_date_low);
		DatePicker date_high=(DatePicker)findViewById(R.id.org_date_high);
		
		
		user= (User)getApplication();
		
		if ((user.getUsername()).equals("?"))
			org_user.setText("��½");
		else
			org_user.setText(user.getUsername());
		
		
		//spinner��ʼ��
		province.add("����");    
		province.add("������");    
		province.add("�ӱ�");    
		province.add("����");    
		province.add("����");
		city.add("����");
		city.add("������");
		city.add("ʯ��ׯ");
		price.add("0-200Ԫ");
		price.add("200-500Ԫ");
		price.add("500-1000Ԫ");
		price.add("1000-1500Ԫ");
		price.add("1500-2500Ԫ");
		price.add("2500-4000Ԫ");
		price.add("4000Ԫ����");
		adapter_province = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, province); 
		adapter_province.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
		org_province.setAdapter(adapter_province);
		org_province.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){    
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
		org_city.setAdapter(adapter_city);
		org_city.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){    
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {  
            	team_city=adapter_city.getItem(arg2);
            	arg0.setVisibility(View.VISIBLE);
            }
            public void onNothingSelected(AdapterView<?> arg0) {    
                // TODO Auto-generated method stub      
                arg0.setVisibility(View.VISIBLE);    
            }   
		});
		
		
		Calendar calendar=Calendar.getInstance();
        int year=calendar.get(Calendar.YEAR);
        int monthOfYear=calendar.get(Calendar.MONTH);
        int dayOfMonth=calendar.get(Calendar.DAY_OF_MONTH);
        time_low=String.valueOf(year)+String.valueOf(monthOfYear)+String.valueOf(dayOfMonth);
        date_low.init(year, monthOfYear, dayOfMonth, new OnDateChangedListener(){
            public void onDateChanged(DatePicker view, int year,
                    int monthOfYear, int dayOfMonth) {
            	time_low=String.valueOf(year)+String.valueOf(monthOfYear)+String.valueOf(dayOfMonth);
            }
            
        });
        time_high=String.valueOf(year)+String.valueOf(monthOfYear)+String.valueOf(dayOfMonth);
        date_high.init(year, monthOfYear, dayOfMonth, new OnDateChangedListener(){
            public void onDateChanged(DatePicker view, int year,
                    int monthOfYear, int dayOfMonth) {
            	time_high=String.valueOf(year)+String.valueOf(monthOfYear)+String.valueOf(dayOfMonth);
            }
        });
        
        
        adapter_price = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, price); 
		adapter_price.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
		org_price.setAdapter(adapter_price);
		org_price.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){    
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {  
            	team_price=adapter_price.getItem(arg2);
            	if (team_price.equals("0-200Ԫ"))
            		team_price="1";
            	else if (team_price.equals("0-200Ԫ"))
            		team_price="2";
            	else if (team_price.equals("200-500Ԫ"))
            		team_price="3";
            	else if (team_price.equals("500-1000Ԫ"))
            		team_price="4";
            	else if (team_price.equals("1000-1500Ԫ"))
            		team_price="5";
            	else if (team_price.equals("1500-2500Ԫ"))
            		team_price="6";
            	else if (team_price.equals("2500-4000Ԫ"))
            		team_price="7";
            	else
            		team_price="8";
            	arg0.setVisibility(View.VISIBLE);
            }
            public void onNothingSelected(AdapterView<?> arg0) {    
                // TODO Auto-generated method stub      
                arg0.setVisibility(View.VISIBLE);    
            }   
		});
		
		
		org_send.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				Calendar cal = Calendar.getInstance();
				team_name=org_name.getText().toString();
				if (team_name.equals(""))
				{
					Toast.makeText(getApplicationContext(), "����д������Ϣ",Toast.LENGTH_SHORT).show();
				}
				else
				{
					new MyThread().start();
				}	
			}
		});
		
		
		org_back.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(Org.this,MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		org_img.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				if (user.getUsername()=="?")
				{
					Intent intent=new Intent(Org.this,Login.class);
					startActivity(intent);
					finish();
				}
				else
				{
					Intent intent=new Intent(Org.this,Mine.class);
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
			//������Ϣ
			Message msg = new Message();
			msg.what = 0x11;
			Bundle bundle = new Bundle();
			bundle.clear();
			try {
				//���ӷ����� ���������ӳ�ʱΪ5��
				socket = new Socket();
				socket.connect(new InetSocketAddress(user.getIP(), 9999), 5000);
				//��ȡ���������
				OutputStream ou = socket.getOutputStream();
				BufferedReader bff = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
				ou.write(("org"+"\n").getBytes("gbk"));
				ou.write((team_name+":"+time_low+":"+time_high+":"+team_price+":"+team_province+":"+team_city+"\n").getBytes("gbk"));
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
				Intent intent=new Intent(Org.this,Org_2.class);
				Bundle b=new Bundle();
				b.putString("team_name", team_name);
				b.putString("team_province", team_province);
				b.putString("team_city", team_city);
				b.putString("team_mem", buffer);
				intent.putExtras(b);
				startActivity(intent);
				finish();
			} catch (SocketTimeoutException aa) {
				bundle.putString("msg", "����������ʧ�ܣ����������Ƿ��");
				msg.setData(bundle);
				myHandler.sendMessage(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}