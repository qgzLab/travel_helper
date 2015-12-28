package com.example.travel_helper;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import android.app.Application;
import android.os.Environment;

public class User extends Application{  
    private String userid="",username="?",password="1",live_province="",live_city="",phone="",email="",qq="",sex=""; 
    private String IP="1"; 
    int birth_year=0,birth_month=0,birth_day=0;
    String[] team,diary,fond;
    private String SDPATH=Environment.getExternalStorageDirectory().getPath();
    
    
    public String getIP(){ 
    	try{
    		FileReader fr = new FileReader(SDPATH + "//" + "bank_IP.txt");
    		BufferedReader br = new BufferedReader(fr);
    	    String strline =null;
    	    while ((strline=br.readLine())!=null)
    	    if (strline!=null)
    	    {
    	    	if (IP.equals("1"))
    	    		IP=strline;
    	    }
    	    fr.close();
    	    br.close();
    		}catch(FileNotFoundException e) {e.printStackTrace();}
             catch(IOException e) {e.printStackTrace();}
        return this.IP;  
    } 
    public String getId(){  
        return this.userid;  
    }
    public String getUsername(){  
        return this.username;  
    }
    private String getPassword(){  
        return this.password;  
    }
    public String getQQ(){  
        return this.qq;  
    }
    public String getEmail(){  
        return this.email;  
    }
   
    public String getSex(){  
        return this.sex;  
    }
    public String getPhone(){  
        return this.phone;  
    }
    public String getLive_province(){  
        return this.live_province;  
    }
    public String getLive_city(){  
        return this.live_city;  
    }
    public int getBirth_year(){  
        return this.birth_year;  
    }
    public int getBirth_month(){  
        return this.birth_month;  
    }
    public int getBirth_day(){  
        return this.birth_day;  
    }
    public String[] getTeam(){  
        return this.team;  
    }
    public String[] getFond(){  
        return this.fond;  
    }
   
    
    
    public void setId(String s){  
        this.userid= s;  
    }
    public void setUsername(String s){  
        this.username= s;  
    } 
    public void setPassword(String s){  
        this.password= s;  
    }
    public void setQQ(String s){  
        this.qq= s;  
    }
    public void setEmail(String s){  
        this.email= s;  
    }
    public void setPhone(String s){  
        this.phone= s;  
    }
    public void setSex(String s){  
        this.sex= s;  
    }
    public void setLive_province(String s){  
        this.live_province= s;  
    }
    public void setLive_city(String s){  
        this.live_city= s;  
    }
    public void setBirth_year(int s){  
        this.birth_year= s;  
    }
    public void setBirth_month(int s){  
        this.birth_month= s;  
    }
    public void setBirth_day(int s){  
        this.birth_day= s;  
    }
    public void setFond(String s){ 
    	if (s.equals("?"))
    	{
    		this.fond=null;
    	}
        this.fond= s.split(":");  
    }
    public void setTeam(String s){ 
    	if (s.equals("?"))
    	{
    		this.team=null;
    	}
        this.team= s.split(":");  
    } 
    
    
  
}  