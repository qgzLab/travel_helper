import java.beans.Statement;  
import java.sql.Connection;  
import java.sql.DriverManager;  
import java.sql.PreparedStatement;  
import java.sql.ResultSet;  
import java.sql.SQLException;  
  
public class DBHelper {  
    private Connection conn = null;  
    PreparedStatement statement = null;  
    int id;
  
    // connect to MySQL  
    void connSQL() {  
        String url = "jdbc:mysql://127.0.0.1/demo";  
        String username = "root";  
        String password = "13785411747"; // 加载驱动程序以连接数据库   
        try {   
            Class.forName("com.mysql.jdbc.Driver" );   
            conn = DriverManager.getConnection( url,username, password );   
            }  
        //捕获加载驱动程序异常  
         catch ( ClassNotFoundException cnfex ) {  
             System.err.println(  
             "装载 JDBC/ODBC 驱动程序失败。" );  
             cnfex.printStackTrace();   
         }   
         //捕获连接数据库异常  
         catch ( SQLException sqlex ) {  
             System.err.println( "无法连接数据库" );  
             sqlex.printStackTrace();   
         }  
    }  
  
    // disconnect to MySQL  
    void deconnSQL() {  
        try {  
            if (conn != null)  
                conn.close();  
        } catch (Exception e) {  
            System.out.println("关闭数据库问题 ：");  
            e.printStackTrace();  
        }  
    }  
  
    // execute selection language  
    ResultSet selectSQL(String sql) {  
        ResultSet rs = null;  
        try {  
            statement = conn.prepareStatement(sql);  
            rs = statement.executeQuery(sql);  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
        return rs;  
    }  
  
    // execute insertion language  
    boolean insertSQL(String sql) {  
        try {  
            statement = conn.prepareStatement(sql);  
            statement.executeUpdate();  
            return true;  
        } catch (SQLException e) {  
            System.out.println("插入数据库时出错：");  
            e.printStackTrace();  
        } catch (Exception e) {  
            System.out.println("插入时出错：");  
            e.printStackTrace();  
        }  
        return false;  
    }  
    //execute delete language  
    boolean deleteSQL(String sql) {  
        try {  
            statement = conn.prepareStatement(sql);  
            statement.executeUpdate();  
            return true;  
        } catch (SQLException e) {  
            System.out.println("插入数据库时出错：");  
            e.printStackTrace();  
        } catch (Exception e) {  
            System.out.println("插入时出错：");  
            e.printStackTrace();  
        }  
        return false;  
    }  
    //execute update language  
    boolean updateSQL(String sql) {  
        try {  
            statement = conn.prepareStatement(sql);  
            statement.executeUpdate();  
            return true;  
        } catch (SQLException e) {  
            System.out.println("插入数据库时出错：");  
            e.printStackTrace();  
        } catch (Exception e) {  
            System.out.println("插入时出错：");  
            e.printStackTrace();  
        }  
        return false;  
    } 
    
    String finduser(int line)
    {
    	DBHelper db2 = new DBHelper();
    	db2.connSQL();
    	String select="select * from addr_book_user where id="+"'"+String.valueOf(line)+"'",ans="";
    	ResultSet resultSet = db2.selectSQL(select);
    	
    	try {  
            while (resultSet.next()) {  
                ans=resultSet.getString("username");
            }  
        } catch (SQLException e) {  
            System.out.println("显示时数据库出错。");  
            e.printStackTrace();  
        } catch (Exception e) {  
            System.out.println("显示出错。");  
            e.printStackTrace();  
        }
    	db2.deconnSQL();
    	return ans;
    }
    
    String login(String line)
    {
    	DBHelper db = new DBHelper();
    	db.connSQL();
    	String[] lines=line.split(":");
    	String ans="?";
    	int check=0;
    	String select="select * from addr_book_user where email="+"'"+lines[0]+"'"+" and password="+"'"+lines[1]+"'";
    	ResultSet resultSet = db.selectSQL(select);
    	
    	try {  
            while (resultSet.next()) {  
                ans=resultSet.getString("username")+":"+
                    resultSet.getString("email")+":"+
                    resultSet.getString("phone")+":"+
                    resultSet.getString("qq")+":"+
                    resultSet.getString("sex")+":"+
                    String.valueOf(resultSet.getInt("birth_year"))+":"+
                    String.valueOf(resultSet.getInt("birth_month"))+":"+
                    String.valueOf(resultSet.getInt("birth_day"))+":"+
                    resultSet.getString("live_province")+":"+
                    resultSet.getString("live_city")+":"+
                    //resultSet.getString("team")+":"+
                    //resultSet.getString("fond")+":"+
                    String.valueOf(resultSet.getInt("id"));
                	check=1;
            }  
        } catch (SQLException e) {  
            System.out.println("显示时数据库出错。");  
            e.printStackTrace();  
        } catch (Exception e) {  
            System.out.println("显示出错。");  
            e.printStackTrace();  
        }
    	if (check==1)
    	{
    		System.out.println("用户"+lines[0]+"请求登录");
			System.out.println("登陆成功！");
    	}
    	else
    	{
    		System.out.println("用户"+lines[0]+"请求登录");
			System.out.println("用户不存在！");
    	}
    	db.deconnSQL();
    	return ans;
    }
    
    String register(String line)
    {
    	DBHelper db = new DBHelper();
    	db.connSQL();
    	String[] lines=line.split(":");
    	int check=0;
    	
    	String select="select * from addr_book_user where email="+"'"+lines[2]+"'";
    	ResultSet resultSet = db.selectSQL(select);
    	
    	try {  
            while (resultSet.next()) {  
                	check=1;
            }  
        } catch (SQLException e) {  
            System.out.println("显示时数据库出错。");  
            e.printStackTrace();  
        } catch (Exception e) {  
            System.out.println("显示出错。");  
            e.printStackTrace();  
        }
    	if (check==1)
    	{
    		db.deconnSQL();
            return "用户已存在";
    	}
 
        String insert = "insert into addr_book_user(Username,Password,Email,Phone,Sex,Birth_year,Birth_month,Birth_day,Qq,Live_province,Live_city,img) VALUES" +
        		"("
        			+"'"+lines[0]+"'"+","
        			+"'"+lines[1]+"'"+","
        			+"'"+lines[2]+"'"+","
        			+"'"+lines[3]+"'"+","
        			+"'"+lines[4]+"'"+","
        			+"'"+lines[5]+"'"+","
        			+lines[6]+","
        			+lines[7]+","
        			+lines[8]+","
        			+"'"+lines[9]+"'"+","
        			+"'"+lines[10]+"'"+","
        			+"'"+""+"'"+
        		")";
        
        if (db.insertSQL(insert) == true) {   
            db.deconnSQL();
            return "注册成功";
        } 
        else
        {
        	db.deconnSQL();
        	return "输入不合法";
        }  
    }
    
    String invi0(String line)
    {
    	DBHelper db = new DBHelper();
    	db.connSQL();
    	String ans="?:发表第一篇日志吧~: \n";
    	
    	String select="select * from addr_book_diary where dia_author_id="+"'"+line+"'"; 
    	ResultSet resultSet = db.selectSQL(select);
    	try {  
            while (resultSet.next()) {
            	if (ans.equals("?:发表第一篇日志吧~: \n"))
            	{
            		ans="";
            	}
                ans+=String.valueOf(resultSet.getInt("id"))+":"+
                    resultSet.getString("dia_title")+":"+
                    resultSet.getString("dia_time")+"\n";
            }  
        } catch (SQLException e) {  
            System.out.println("显示时数据库出错。");  
            e.printStackTrace();  
        } catch (Exception e) {  
            System.out.println("显示出错。");  
            e.printStackTrace();  
        }
    	db.deconnSQL();
    	return ans;
    }
    
    String invi1(String line)//444444
    {
    	DBHelper db = new DBHelper();
    	db.connSQL();
    	String ans="?:发表第一篇攻略吧~: \n";
    	String select="select * from addr_book_guide where gui_flag=1"; 
    	ResultSet resultSet = db.selectSQL(select);
    	try {  
            while (resultSet.next()) {
            	if (ans.equals("?:发表第一篇攻略吧~: \n"))
            	{
            		ans="";
            	}
                ans+=String.valueOf(resultSet.getInt("id"))+":"+
                    resultSet.getString("gui_title")+":"+
                    resultSet.getString("gui_time")+"\n";
            }  
        } catch (SQLException e) {  
            System.out.println("显示时数据库出错。");  
            e.printStackTrace();  
        } catch (Exception e) {  
            System.out.println("显示出错。");  
            e.printStackTrace();  
        }
    	db.deconnSQL();
    	return ans;
    }
    
   
    String write(String line)
    {
    	DBHelper db = new DBHelper();
    	db.connSQL();  
    	String[] lines=line.split(":");
    	String insert="";
    	
    	if (lines[0].equals("0"))
    	{
    		insert = "insert into addr_book_diary(Dia_province,Dia_city,Dia_place,Dia_title,Dia_context,Dia_time,Dia_zan,Dia_author_id,img) VALUES" +
    		"("
        			+"'"+lines[4]+"'"+","
        			+"'"+lines[5]+"'"+","
        			+"'"+lines[6]+"'"+","
        			+"'"+lines[1]+"'"+","
        			+"'"+lines[2]+"'"+","
        			+"'"+lines[3]+"'"+","
        			+""+"0"+""+","
        			+""+lines[7]+""+","
        			+"'"+""+"'"+
        	")";
    		db.insertSQL(insert); 
    	}
    	else if (lines[0].equals("1"))
    	{
    		insert = "insert into addr_book_guide(Gui_province,Gui_city,Gui_place,Gui_title,Gui_context,Gui_author_id,Gui_time,Gui_flag,Gui_zan,img) VALUES" +
    		"("
        			+"'"+lines[5]+"'"+","
        			+"'"+lines[6]+"'"+","
        			+"'"+lines[7]+"'"+","
        			+"'"+lines[1]+"'"+","
        			+"'"+lines[2]+"'"+","
        			+"'"+lines[4]+"'"+","
        			+"'"+lines[3]+"'"+","
        			+""+"0"+""+","
        			+""+"0"+""+","
        			+"'"+""+"'"+
        	")";
    		db.insertSQL(insert);
    	}
    	else if (lines[0].equals("2"))
    	{
    		insert = "insert into addr_book_ans_guide(Ans_context,Ans_time,guide_id,Ans_author_id) VALUES" +
    	    		"("
    	        			+"'"+lines[1]+"'"+","
    	        			+"'"+lines[2]+"'"+","
    	        			+""+lines[3]+""+","
    	        			+""+lines[4]+""+
    	        	")";
    	    db.insertSQL(insert);
    	}
    	else if (lines[0].equals("3"))
    	{
    		insert = "insert into addr_book_ans_diary(Ans_context,Ans_time,diary_id,Ans_author_id) VALUES" +
    	    		"("
    	        			+"'"+lines[1]+"'"+","
    	        			+"'"+lines[2]+"'"+","
    	        			+""+lines[3]+""+","
    	        			+""+lines[4]+""+
    	        	")";
    	    db.insertSQL(insert);
    	}
        db.deconnSQL();
    	return "发表成功";
    }
    
    
    String read(String line)
    {
    	DBHelper db = new DBHelper();
    	db.connSQL();
    	String ans="?",get="",select="";
    	System.out.println(line);
    	String[] lines=line.split(":");
    	if (lines[1].equals("1"))
    	{
    		 select="select * from addr_book_guide where id="+lines[0];
    		 ResultSet resultSet = db.selectSQL(select);
    	    	try {  
    	            while (resultSet.next()) {  
    	                ans=resultSet.getString("gui_title")+":"+
    	                    resultSet.getString("gui_context")+":"+
    	                    resultSet.getString("gui_time")+":"+
    	                    finduser(resultSet.getInt("gui_author_id"))+":"+
    	                    resultSet.getString("gui_province")+":"+
    	                    resultSet.getString("gui_city")+":"+
    	                    resultSet.getString("gui_place")+":"+
    	                    String.valueOf(resultSet.getInt("gui_zan"))+":"+"\n";
    	            }  
    	        } catch (SQLException e) {  
    	            System.out.println("显示时数据库出错。");  
    	            e.printStackTrace();  
    	        } catch (Exception e) {  
    	            System.out.println("显示出错。");  
    	            e.printStackTrace();  
    	        }
    	    	select="select * from addr_book_ans_guide where guide_id="+lines[0];
    	    	resultSet = db.selectSQL(select);
    	    	try {  
                    while (resultSet.next()) {  
                        ans+=resultSet.getString("ans_context")+":"+
                            resultSet.getString("ans_time")+":"+
                            finduser(resultSet.getInt("ans_author_id"))+"\n";
                    }  
                } catch (SQLException e) {  
                    System.out.println("显示时数据库出错。");  
                    e.printStackTrace();  
                } catch (Exception e) {  
                    System.out.println("显示出错。");  
                    e.printStackTrace();  
                }
    		 
    	}
    	else if (lines[1].equals("0"))
    	{
   		 select="select * from addr_book_diary where id="+lines[0];
   		 ResultSet resultSet = db.selectSQL(select);
   	    	try {  
   	            while (resultSet.next()) {  
   	                ans=resultSet.getString("dia_title")+":"+
   	                    resultSet.getString("dia_context")+":"+
   	                    resultSet.getString("dia_time")+":"+
   	                    finduser(resultSet.getInt("dia_author_id"))+":"+
   	                    resultSet.getString("dia_province")+":"+
   	                    resultSet.getString("dia_city")+":"+
   	                    resultSet.getString("dia_place")+":"+
   	                    String.valueOf(resultSet.getInt("dia_zan"))+":"+"\n";
   	            }  
   	        } catch (SQLException e) {  
   	            System.out.println("显示时数据库出错。");  
   	            e.printStackTrace();  
   	        } catch (Exception e) {  
   	            System.out.println("显示出错。");  
   	            e.printStackTrace();  
   	        }
   	    	select="select * from addr_book_ans_diary where diary_id="+lines[0];
   	    	resultSet = db.selectSQL(select);
   	    	try {  
                   while (resultSet.next()) {  
                       ans+=resultSet.getString("ans_context")+":"+
                           resultSet.getString("ans_time")+":"+
                           finduser(resultSet.getInt("ans_author_id"))+"\n";
                   }  
               } catch (SQLException e) {  
                   System.out.println("显示时数据库出错。");  
                   e.printStackTrace();  
               } catch (Exception e) {  
                   System.out.println("显示出错。");  
                   e.printStackTrace();  
               }
   		 
   	}
    	ans+="写下自己的评论吧~: : \n";
    	db.deconnSQL();
    	return ans;
    }
   
    String think(String line)
    {
    	System.out.println(line);  
    	DBHelper db = new DBHelper();
    	db.connSQL();
    	String[] lines=line.split(":");
    	String ans=" :不存在符合条件的景点攻略: \n";
    	String select="select * from addr_book_guide where gui_city='"+lines[1]+"'";
    	ResultSet resultSet = db.selectSQL(select);
    	try {  
            while (resultSet.next()) {
            	if (ans.equals(" :不存在符合条件的景点攻略: \n"))
            	{
            		ans="";
            	}
        		ans+=String.valueOf(resultSet.getInt("id"))+":"+
                        resultSet.getString("gui_title")+":"+
                        resultSet.getString("gui_place")+"\n"; 
            }  
        } catch (SQLException e) {  
            System.out.println("显示时数据库出错。");  
            e.printStackTrace();  
        } catch (Exception e) {  
            System.out.println("显示出错。");  
            e.printStackTrace();  
        }
    	
    	String insert = "insert into addr_book_aim(province,city,start_t,end_t,price,user_id) VALUES" +
        		"("
            			+"'"+lines[0]+"'"+","
            			+"'"+lines[1]+"'"+","
            			+"'"+lines[2]+"'"+","
            			+"'"+lines[3]+"'"+","
            			+"'"+lines[4]+"'"+","
            			+""+lines[5]+""+
            	")";
        db.insertSQL(insert); 
    	db.deconnSQL();
    	return ans;
    }
    
    String loc(String line)
    {
    	DBHelper db = new DBHelper();
    	db.connSQL();
    	String[] lines=line.split(":");
    	String ans="",get="";
    	String select="select * from addr_book_place where id="+"'"+lines[1]+"'";
    	ResultSet resultSet = db.selectSQL(select);
    	try {  
            while (resultSet.next()) {
            		ans+=resultSet.getString("loc_place")+":"+
                            resultSet.getString("loc_province")+":"+
                            resultSet.getString("loc_city")+":"+
                            resultSet.getString("loc_price")+":"+
                            resultSet.getString("loc_intro")+":"+
                            "\n";
            }  
        } catch (SQLException e) {  
            System.out.println("显示时数据库出错。");  
            e.printStackTrace();  
        } catch (Exception e) {  
            System.out.println("显示出错。");  
            e.printStackTrace();  
        }
    	
    	select="select * from addr_book_user where id="+"'"+lines[0]+"'";
    	resultSet = db.selectSQL(select);
    	try {  
            while (resultSet.next()) {
            		get=resultSet.getString("fond");
            }  
        } catch (SQLException e) {  
            System.out.println("显示时数据库出错。");  
            e.printStackTrace();  
        } catch (Exception e) {  
            System.out.println("显示出错。");  
            e.printStackTrace();  
        }
    	if (get.equals("?"))
    		get="";
    	String update = "update addr_book_user set fond ='"+get+lines[1]+"&' where id= '"+lines[0]+"'";
    	db.updateSQL(update);
    	db.deconnSQL();
    	return ans;
    }
    
    String org(String line)
    {
    	System.out.println(line);
    	String select="";
    	DBHelper db = new DBHelper();
    	db.connSQL();
    	String[] lines=line.split(":");
    	String mem="";
    	String ans="不存在符合条件的队友: : : : : \n";
    	select="select * from addr_book_aim where price="+"'"+lines[3]+"' and city='"+lines[5]+"'";
    	ResultSet resultSet = db.selectSQL(select);
    	try {  
            while (resultSet.next()) {
            	if (ans.equals("不存在符合条件的队友: : : : : \n"))
            		ans="";
            	mem+=String.valueOf(resultSet.getInt("user_id"))+":";
            }  
        } catch (SQLException e) {  
            System.out.println("显示时数据库出错。");  
            e.printStackTrace();  
        } catch (Exception e) {  
            System.out.println("显示出错。");  
            e.printStackTrace();  
        }
    	
    	int i;
    	String[] mems=mem.split(":");
    	for (i=0;i<mems.length-1;i++)
    	{
    		select="select * from addr_book_user where id="+mems[i];
    		resultSet = db.selectSQL(select);
    		try {  
                while (resultSet.next()) {
                	ans+=resultSet.getString("username")+":"+
                			String.valueOf(2015-resultSet.getInt("birth_year"))+":"+
                			resultSet.getString("sex")+":"+
                			String.valueOf(resultSet.getInt("id"))+":"+
                			resultSet.getString("live_province")+":"+
                			resultSet.getString("live_city");
                }  
            } catch (SQLException e) {  
                System.out.println("显示时数据库出错。");  
                e.printStackTrace();  
            } catch (Exception e) {  
                System.out.println("显示出错。");  
                e.printStackTrace();  
            }
    		
    	}
    	db.deconnSQL();
    	return ans;
    }
    
    String org2(String line) //111
    {
    	String select="",get="";
    	DBHelper db = new DBHelper();
    	db.connSQL();
    	String[] lines=line.split(":");
    	String insert = "insert into addr_book_team(team_name,team_province,team_city,leader_id) VALUES" +
        		"("
            			+"'"+lines[0]+"'"+","
            			+"'"+lines[2]+"'"+","
            			+"'"+lines[3]+"'"+","
            			+"'"+lines[4]+"'"+
            	")";
        db.insertSQL(insert);
        
        select="select * from addr_book_team where team_name="+"'"+lines[0]+"' and leader_id='"+lines[4]+"'";
        ResultSet resultSet = db.selectSQL(select);
    	try {  
            while (resultSet.next()) {
            	id=resultSet.getInt("id");
            }  
        } catch (SQLException e) {  
            System.out.println("显示时数据库出错。");  
            e.printStackTrace();  
        } catch (Exception e) {  
            System.out.println("显示出错。");  
            e.printStackTrace();  
        }
    	
        String []l=lines[1].split("&");
        int i;
        for (i=1;i<l.length;i++)
        {
        	insert = "insert into addr_book_team_user1(team_id,user_id) VALUES" +
            		"("
                			+""+String.valueOf(id)+""+","
                			+"'"+l[i]+"'"+
                	")";
        	db.insertSQL(insert); 
        }
        /*
        insert = "insert into addr_book_team_user0(team_id,user_id) VALUES" +
        		"("
            			+""+String.valueOf(id)+""+","
            			+"'"+l[0]+"'"+
            	")";
        */
    	db.insertSQL(insert);
    	db.deconnSQL();
    	return String.valueOf(id);
    }
    
    String team(String line)//2222
    {
    	DBHelper db = new DBHelper();
    	String ans=" :您还没有队伍: : : : ",get="";
    	db.connSQL();
    	
    	String select="select * from addr_book_team where leader_id="+""+line+"";
    	ResultSet resultSet = db.selectSQL(select);
    	resultSet = db.selectSQL(select);
    	try {  
            while (resultSet.next()) {
            	if (ans.equals(" :您还没有队伍: : : : "))
            		ans=""; 
        		ans+=String.valueOf(resultSet.getInt("id"))+":"+
        			resultSet.getString("team_name")+":"+
        			resultSet.getString("team_province")+":"+
        			resultSet.getString("team_city")+":"+
        			String.valueOf(resultSet.getInt("leader_id"))+":"+"0"+"\n";
            }  
        } catch (SQLException e) {  
            System.out.println("显示时数据库出错。");  
            e.printStackTrace();  
        } catch (Exception e) {  
            System.out.println("显示出错。");  
            e.printStackTrace();  
        }
    	
    	
    	//team0
    	select="select * from addr_book_team_user0 where user_id="+""+line+"";
    	resultSet = db.selectSQL(select);
    	try {  
            while (resultSet.next()) {
        		get+=resultSet.getString("team_id")+":";
            }  
        } catch (SQLException e) {  
            System.out.println("显示时数据库出错。");  
            e.printStackTrace();  
        } catch (Exception e) {  
            System.out.println("显示出错。");  
            e.printStackTrace();  
        }
    	int i;
    	if (get.equals(""))
    	{	
    	}
    	else
    	{
    		String[] gets=get.split(":");
    		for (i=0;i<gets.length;i++)
    		{
    			select="select * from addr_book_team where id="+""+gets[i]+"";
    			resultSet = db.selectSQL(select);
            	try {  
                    while (resultSet.next()) {
                    	if (ans.equals(" :您还没有队伍: : : : "))
                    		ans=""; 
                		ans+=String.valueOf(resultSet.getInt("id"))+":"+
                			resultSet.getString("team_name")+":"+
                			resultSet.getString("team_province")+":"+
                			resultSet.getString("team_city")+":"+
                			String.valueOf(resultSet.getInt("leader_id"))+":"+"0"+"\n";			
                    }  
                } catch (SQLException e) {  
                    System.out.println("显示时数据库出错。");  
                    e.printStackTrace();  
                } catch (Exception e) {  
                    System.out.println("显示出错。");  
                    e.printStackTrace();  
                }
    		}
    		
    	}
    	
    	//team1
    	select="select * from addr_book_team_user1 where user_id="+""+line+"";
    	get="";
    	resultSet = db.selectSQL(select);
    	try {  
            while (resultSet.next()) {
        		get+=resultSet.getString("team_id")+":";
            }  
        } catch (SQLException e) {  
            System.out.println("显示时数据库出错。");  
            e.printStackTrace();  
        } catch (Exception e) {  
            System.out.println("显示出错。");  
            e.printStackTrace();  
        }
    	if (get.equals(""))
    	{	
    	}
    	else
    	{
    		String[] gets=get.split(":");
    		for (i=0;i<gets.length;i++)
    		{
    			select="select * from addr_book_team where id="+""+gets[i]+"";
    			resultSet = db.selectSQL(select);
            	try {  
                    while (resultSet.next()) {
                    	if (ans.equals(" :您还没有队伍: : : : "))
                    		ans="";
                		ans+=String.valueOf(resultSet.getInt("id"))+":"+
                			resultSet.getString("team_name")+":"+
                			resultSet.getString("team_province")+":"+
                			resultSet.getString("team_city")+":"+
                			String.valueOf(resultSet.getInt("leader_id"))+":"+"1"+"\n";
                				
                    }  
                } catch (SQLException e) {  
                    System.out.println("显示时数据库出错。");  
                    e.printStackTrace();  
                } catch (Exception e) {  
                    System.out.println("显示出错。");  
                    e.printStackTrace();  
                }
    		}
    		
    	}
    	 
    	//team2
    	select="select * from addr_book_team_user2 where user_id="+""+line+"";
    	get="";
    	resultSet = db.selectSQL(select);
    	try {  
            while (resultSet.next()) {
        		get+=resultSet.getString("team_id")+":";
            }  
        } catch (SQLException e) {  
            System.out.println("显示时数据库出错。");  
            e.printStackTrace();  
        } catch (Exception e) {  
            System.out.println("显示出错。");  
            e.printStackTrace();  
        }
    	if (get.equals(""))
    	{	
    	}
    	else
    	{
    		String[] gets=get.split(":");
    		for (i=0;i<gets.length;i++)
    		{
    			select="select * from addr_book_team where id="+""+gets[i]+"";
    			resultSet = db.selectSQL(select);
            	try {  
                    while (resultSet.next()) {
                    	if (ans.equals(" :您还没有队伍: : : : "))
                    		ans="";
                		ans+=String.valueOf(resultSet.getInt("id"))+":"+
                			resultSet.getString("team_name")+":"+
                			resultSet.getString("team_province")+":"+
                			resultSet.getString("team_city")+":"+
                			String.valueOf(resultSet.getInt("leader_id"))+":"+"2"+"\n";
                				
                    }  
                } catch (SQLException e) {  
                    System.out.println("显示时数据库出错。");  
                    e.printStackTrace();  
                } catch (Exception e) {  
                    System.out.println("显示出错。");  
                    e.printStackTrace();  
                }
    		}
    		
    	}
    	db.deconnSQL();
    	return ans;
    }
    
    String view(String line)//333333
    {
    	DBHelper db = new DBHelper();
    	String ans="您还没有组建队伍: : \n : : : : : \n";
    	int get=0,pp=-1;
    	ResultSet resultSet2;
    	
    	db.connSQL();
    	String select="select * from addr_book_team where id="+line;
    	ResultSet resultSet = db.selectSQL(select);
    	try {  
            while (resultSet.next()) {
            	if (ans.equals("您还没有组建队伍: : \n : : : : : \n"))
            	{
            		ans="";
            	}
            	ans+=resultSet.getString("team_name")+":"+
            		resultSet.getString("team_province")+":"+
            		resultSet.getString("team_city")+":"+
            		String.valueOf(resultSet.getInt("leader_id"))+"\n";
            	get=resultSet.getInt("leader_id");
            }  
        } catch (SQLException e) {  
            System.out.println("显示时数据库出错。");  
            e.printStackTrace();  
        } catch (Exception e) {  
            System.out.println("显示出错。");  
            e.printStackTrace();  
        }
    	
    	select="select * from addr_book_user where id="+String.valueOf(get);
    	resultSet = db.selectSQL(select);
    	try {  
            while (resultSet.next()) {
        		ans+=resultSet.getString("username")+":"+
        			String.valueOf(2015-Integer.parseInt(resultSet.getString("birth_year")))+":"+
        			resultSet.getString("sex")+":"+
        			String.valueOf(resultSet.getInt("id"))+":"+
        			resultSet.getString("live_province")+":"+
        			resultSet.getString("live_city")+":0\n";	
            }  
        } catch (SQLException e) {  
            System.out.println("显示时数据库出错。");  
            e.printStackTrace();  
        } catch (Exception e) {  
            System.out.println("显示出错。");  
            e.printStackTrace();  
        }
    	//team0
    	select="select * from addr_book_team_user0 where team_id="+line;
		resultSet = db.selectSQL(select);
    	try {  
            while (resultSet.next()) {
            	pp=resultSet.getInt("user_id");
            	select="select * from addr_book_user where id="+pp;
        		resultSet2 = db.selectSQL(select);
            	try {  
                    while (resultSet2.next()) {
                		ans+=resultSet2.getString("username")+":"+
                			String.valueOf(2015-Integer.parseInt(resultSet2.getString("birth_year")))+":"+
                			resultSet2.getString("sex")+":"+
                			String.valueOf(resultSet2.getInt("id"))+":"+
                			resultSet2.getString("live_province")+":"+
                			resultSet2.getString("live_city")+":1\n";
                    }  
                } catch (SQLException e) {  
                    System.out.println("显示时数据库出错。");  
                    e.printStackTrace();  
                } catch (Exception e) {  
                    System.out.println("显示出错。");  
                    e.printStackTrace();  
                }
            }  
        } catch (SQLException e) {  
            System.out.println("显示时数据库出错。");  
            e.printStackTrace();  
        } catch (Exception e) {  
            System.out.println("显示出错。");  
            e.printStackTrace();  
        }
    	
    	
    	//team1
    	pp=-1;
    	select="select * from addr_book_team_user1 where team_id="+line;
		resultSet = db.selectSQL(select);
    	try {  
            while (resultSet.next()) {
            	pp=resultSet.getInt("user_id");
            	select="select * from addr_book_user where id="+pp;
        		resultSet2 = db.selectSQL(select);
            	try {  
                    while (resultSet2.next()) {
                		ans+=resultSet2.getString("username")+":"+
                			String.valueOf(2015-Integer.parseInt(resultSet2.getString("birth_year")))+":"+
                			resultSet2.getString("sex")+":"+
                			String.valueOf(resultSet2.getInt("id"))+":"+
                			resultSet2.getString("live_province")+":"+
                			resultSet2.getString("live_city")+":2\n";	
                    }  
                } catch (SQLException e) {  
                    System.out.println("显示时数据库出错。");  
                    e.printStackTrace();  
                } catch (Exception e) {  
                    System.out.println("显示出错。");  
                    e.printStackTrace();  
                }
            }  
        } catch (SQLException e) {  
            System.out.println("显示时数据库出错。");  
            e.printStackTrace();  
        } catch (Exception e) {  
            System.out.println("显示出错。");  
            e.printStackTrace();  
        }
    	
    	//team2
    	pp=-1;
    	select="select * from addr_book_team_user2 where team_id="+line;
		resultSet = db.selectSQL(select);
    	try {  
            while (resultSet.next()) {
            	pp=resultSet.getInt("user_id");
            	select="select * from addr_book_user where id="+pp;
        		resultSet2 = db.selectSQL(select);
            	try {  
                    while (resultSet2.next()) {
                		ans+=resultSet2.getString("username")+":"+
                			String.valueOf(2015-Integer.parseInt(resultSet2.getString("birth_year")))+":"+
                			resultSet2.getString("sex")+":"+
                			String.valueOf(resultSet2.getInt("id"))+":"+
                			resultSet2.getString("live_province")+":"+
                			resultSet2.getString("live_city")+":3\n";	
                    }  
                } catch (SQLException e) {  
                    System.out.println("显示时数据库出错。");  
                    e.printStackTrace();  
                } catch (Exception e) {  
                    System.out.println("显示出错。");  
                    e.printStackTrace();  
                }
            }  
        } catch (SQLException e) {  
            System.out.println("显示时数据库出错。");  
            e.printStackTrace();  
        } catch (Exception e) {  
            System.out.println("显示出错。");  
            e.printStackTrace();  
        }
    	
    	db.deconnSQL();
    	return ans;
    }
    

    String mine(String line)
    {
    	DBHelper db = new DBHelper();
    	db.connSQL();
    	String ans="";
    	String select="select * from addr_book_user where id='"+line+"'"; 
    	ResultSet resultSet = db.selectSQL(select);
    	try {  
            while (resultSet.next()) {
                ans+=resultSet.getString("username")+":"+
                    resultSet.getString("email")+":"+
                    resultSet.getString("phone")+":"+
                    resultSet.getString("qq")+":"+
                    resultSet.getString("sex")+":"+
                    String.valueOf(resultSet.getInt("birth_year"))+"/"+
                    String.valueOf(resultSet.getInt("birth_month"))+"/"+
                    String.valueOf(resultSet.getInt("birth_day"))+":"+
                    resultSet.getString("live_province")+"-"+
                    resultSet.getString("live_city")+":"+"\n";
            }  
        } catch (SQLException e) {  
            System.out.println("显示时数据库出错。");  
            e.printStackTrace();  
        } catch (Exception e) {  
            System.out.println("显示出错。");  
            e.printStackTrace();  
        }
    	return ans;
    }
    
    String team_update(String line)
    {
    	DBHelper db = new DBHelper();
    	db.connSQL();
    	String[] lines=line.split(":");
    	String ans="",select="",insert="",delete="";
    	int s=-1;
    	insert = "insert into addr_book_team_user0(team_id,user_id) VALUES" +
        		"("
            			+"'"+lines[1]+"'"+","
            			+"'"+lines[0]+"'"+
            	")";
        db.insertSQL(insert);
        
        select="select * from addr_book_team_user1 where user_id='"+lines[0]+"' and team_id='"+lines[1]+"'";
        ResultSet resultSet = db.selectSQL(select);
    	try {  
            while (resultSet.next()) {
                s=resultSet.getInt("id");
            }  
        } catch (SQLException e) {  
            System.out.println("显示时数据库出错。");  
            e.printStackTrace();  
        } catch (Exception e) {  
            System.out.println("显示出错。");  
            e.printStackTrace();  
        }
    	
    	if (s!=-1)
    	{
    		delete = "delete from addr_book_team_user1 where id="+String.valueOf(s);
    		db.insertSQL(delete);
    	}
    	
    	
    	s=-1;
    	select="select * from addr_book_team_user2 where user_id='"+lines[0]+"' and team_id='"+lines[1]+"'";
        resultSet = db.selectSQL(select);
    	try {  
            while (resultSet.next()) {
                s=resultSet.getInt("id");
            }  
        } catch (SQLException e) {  
            System.out.println("显示时数据库出错。");  
            e.printStackTrace();  
        } catch (Exception e) {  
            System.out.println("显示出错。");  
            e.printStackTrace();  
        }
    	
    	if (s!=-1)
    	{
    		delete = "delete from addr_book_team_user2 where id="+String.valueOf(s);
    		db.insertSQL(delete);
    	}
    	return ans;
    }
    
    String user_update(String line)
    {
    	DBHelper db = new DBHelper();
    	db.connSQL();
    	String ans="";
    	int check=0;
    	String[] lines=line.split(":");
    	
    	String select="select * from addr_book_user where email="+"'"+lines[1]+"'";
    	ResultSet resultSet = db.selectSQL(select);
    	
    	try {  
            while (resultSet.next()) {  
                	check=1;
            }  
        } catch (SQLException e) {  
            System.out.println("显示时数据库出错。");  
            e.printStackTrace();  
        } catch (Exception e) {  
            System.out.println("显示出错。");  
            e.printStackTrace();  
        }
    	if (check==0)
    	{
    		db.deconnSQL();
            return "用户不存在";
    	}
    	
    	String update = "update addr_book_user set "+
    					"username ='"+lines[0]+"' ,"+
    					"email ='"+lines[1]+"' ,"+
    					"phone ='"+lines[2]+"' ,"+
    					"sex ='"+lines[3]+"' ,"+
    					"birth_year ="+lines[4]+" ,"+
    					"birth_month ="+lines[5]+" ,"+
    					"birth_day ="+lines[6]+" ,"+
    					"qq ='"+lines[7]+"' ,"+
    					"live_province ='"+lines[8]+"' ,"+
    					"live_city ='"+lines[9]+"' "+
    					"where id= "+lines[10]; 
    	System.out.println(update); 
    	if (db.updateSQL(update) == true) {  
            System.out.println("update successfully");  
            ans="修改成功";  
        }  
    	else
    	{ 
    		ans="输入不合法";
    	}
    	return ans;
    }
    String team_del(String line)
    {
    	DBHelper db = new DBHelper();
    	db.connSQL();
    	String[] lines=line.split(":");
    	String ans="",select="",insert="",delete="";
    	int s=-1;
    	
        select="select * from addr_book_team_user0 where user_id='"+lines[0]+"' and team_id='"+lines[1]+"'";
        ResultSet resultSet = db.selectSQL(select);
    	try {  
            while (resultSet.next()) {
                s=resultSet.getInt("id");
            }  
        } catch (SQLException e) {  
            System.out.println("显示时数据库出错。");  
            e.printStackTrace();  
        } catch (Exception e) {  
            System.out.println("显示出错。");  
            e.printStackTrace();  
        }
    	
    	if (s!=-1)
    	{
    		delete = "delete from addr_book_team_user0 where id="+String.valueOf(s);
    		db.insertSQL(delete);
    	}
    	
    	select="select * from addr_book_team_user1 where user_id='"+lines[0]+"' and team_id='"+lines[1]+"'";
        resultSet = db.selectSQL(select);
    	try {  
            while (resultSet.next()) {
                s=resultSet.getInt("id");
            }  
        } catch (SQLException e) {  
            System.out.println("显示时数据库出错。");  
            e.printStackTrace();  
        } catch (Exception e) {  
            System.out.println("显示出错。");  
            e.printStackTrace();  
        }
    	
    	if (s!=-1)
    	{
    		delete = "delete from addr_book_team_user1 where id="+String.valueOf(s);
    		db.insertSQL(delete);
    	}
    	
    	
    	s=-1;
    	select="select * from addr_book_team_user2 where user_id='"+lines[0]+"' and team_id='"+lines[1]+"'";
        resultSet = db.selectSQL(select);
    	try {  
            while (resultSet.next()) {
                s=resultSet.getInt("id");
            }  
        } catch (SQLException e) {  
            System.out.println("显示时数据库出错。");  
            e.printStackTrace();  
        } catch (Exception e) {  
            System.out.println("显示出错。");  
            e.printStackTrace();  
        }
    	
    	if (s!=-1)
    	{
    		delete = "delete from addr_book_team_user2 where id="+String.valueOf(s);
    		db.insertSQL(delete);
    	}
    	return ans;
    } 
    String invite(String line)
    {
    	System.out.println(line);
    	int i;
    	int check=0;
    	ResultSet resultSet=null;
    	DBHelper db = new DBHelper();
    	db.connSQL();
    	String insert="",select="";
    	String[] lines=line.split(":");
    	
    	
    	
    	for (i=1;i<lines.length;i++)
    	{
    		check=0;
    		select="select * from addr_book_team_user0 where user_id="+lines[i]+" and team_id="+lines[0];
        	resultSet = db.selectSQL(select);
        	try {  
                while (resultSet.next()) {
                	check=1;
                }  
            } catch (SQLException e) {  
                System.out.println("显示时数据库出错。");  
                e.printStackTrace();  
            } catch (Exception e) {  
                System.out.println("显示出错。");  
                e.printStackTrace();  
            }
        	
        	select="select * from addr_book_team_user1 where user_id="+lines[i]+" and team_id="+lines[0];
        	resultSet = db.selectSQL(select);
        	try {  
                while (resultSet.next()) {
                	check=1;
                }  
            } catch (SQLException e) {  
                System.out.println("显示时数据库出错。");  
                e.printStackTrace();  
            } catch (Exception e) {  
                System.out.println("显示出错。");  
                e.printStackTrace();  
            }
        	
        	select="select * from addr_book_team_user2 where user_id="+lines[i]+" and team_id="+lines[0];
        	resultSet = db.selectSQL(select);
        	try {  
                while (resultSet.next()) {
                	check=1;
                }  
            } catch (SQLException e) {  
                System.out.println("显示时数据库出错。");  
                e.printStackTrace();  
            } catch (Exception e) {  
                System.out.println("显示出错。");  
                e.printStackTrace();  
            }
        	
        	if (check==1)
        		continue;
    		insert = "insert into addr_book_team_user1(team_id,user_id) VALUES" +
    	    		"("
    	        			+""+lines[0]+""+","
    	        			+""+lines[i]+""+
    	        	")";
    	    		db.insertSQL(insert); 
    	}
    	db.deconnSQL();
    	return "?";
    }
    
    
    String send_invi(String line)
    {
    	System.out.println(line);
    	int i;
    	DBHelper db = new DBHelper();
    	db.connSQL();
    	ResultSet resultSet=null;
    	String insert="",select="";
    	String[] lines=line.split(":");
    	int check=0;
    	for (i=1;i<lines.length;i++)
    	{
    		check=0;
    		select="select * from addr_book_team_user0 where user_id="+lines[0]+" and team_id="+lines[i];
    		resultSet = db.selectSQL(select);
        	try {  
                while (resultSet.next()) {
                	check=1;
                }  
            } catch (SQLException e) {  
                System.out.println("显示时数据库出错。");  
                e.printStackTrace();  
            } catch (Exception e) {  
                System.out.println("显示出错。");  
                e.printStackTrace();  
            }
        	select="select * from addr_book_team_user1 where user_id="+lines[0]+" and team_id="+lines[i];
    		resultSet = db.selectSQL(select);
        	try {  
                while (resultSet.next()) {
                	check=1;
                }  
            } catch (SQLException e) {  
                System.out.println("显示时数据库出错。");  
                e.printStackTrace();  
            } catch (Exception e) {  
                System.out.println("显示出错。");  
                e.printStackTrace();  
            }
        	select="select * from addr_book_team_user2 where user_id="+lines[0]+" and team_id="+lines[i];
    		resultSet = db.selectSQL(select);
        	try {  
                while (resultSet.next()) {
                	check=1;
                }  
            } catch (SQLException e) {  
                System.out.println("显示时数据库出错。");  
                e.printStackTrace();  
            } catch (Exception e) {  
                System.out.println("显示出错。");  
                e.printStackTrace();  
            }
        	if (check==1)
        		continue;
        	else
        	{
        		insert = "insert into addr_book_team_user2(user_id,team_id) VALUES" +
        	    		"("
        	        			+""+lines[0]+""+","
        	        			+""+lines[i]+""+
        	        	")";
        	    db.insertSQL(insert);
        	}
    	}
    	db.deconnSQL();
    	return "?";
    }
    String find_mem(String line)
    {
    	System.out.println(line);
    	String select="";
    	DBHelper db = new DBHelper();
    	db.connSQL();
    	String ans="不存在符合条件的队友: : : : : \n";
    	select="select * from addr_book_user where username="+"'"+line+"'";
    	ResultSet resultSet = db.selectSQL(select);
    	try {  
            while (resultSet.next()) {
            	if (ans.equals("不存在符合条件的队友: : : : : \n"))
            		ans="";
            	ans+=resultSet.getString("username")+":"+
            			String.valueOf(2015-resultSet.getInt("birth_year"))+":"+
            			resultSet.getString("sex")+":"+
            			String.valueOf(resultSet.getInt("id"))+":"+
            			resultSet.getString("live_province")+":"+
            			resultSet.getString("live_city");
            }  
        } catch (SQLException e) {  
            System.out.println("显示时数据库出错。");  
            e.printStackTrace();  
        } catch (Exception e) {  
            System.out.println("显示出错。");  
            e.printStackTrace();  
        }
    	db.deconnSQL();
    	return ans;
    }
    String team_find(String line)
    {
    	System.out.println(line);
    	String select="";
    	DBHelper db = new DBHelper();
    	db.connSQL();
    	String ans="没有合适队伍: : \n";
    	String[] lines=line.split(":");
    	select="select * from addr_book_team where team_city="+"'"+lines[1]+"'";
    	ResultSet resultSet = db.selectSQL(select);
    	try {  
            while (resultSet.next()) {
            	if (ans.equals("没有合适队伍: : \n"))
            		ans="";
            	ans+=resultSet.getString("team_name")+":"+
            			String.valueOf(resultSet.getInt("id"))+":"+
            			resultSet.getString("team_province")+"-"+resultSet.getString("team_city");
            }  
        } catch (SQLException e) {  
            System.out.println("显示时数据库出错。");  
            e.printStackTrace();  
        } catch (Exception e) {  
            System.out.println("显示出错。");  
            e.printStackTrace();  
        }
    	db.deconnSQL();
    	return ans;
    }
    String zan(String line)
    {
    	DBHelper db = new DBHelper();
    	db.connSQL();
    	String ans="?",select="",update="";
    	int i=0;
    	System.out.println(line);
    	String[] lines=line.split(":");
    	if (lines[1].equals("1"))
    	{
    		 select="select * from addr_book_guide where id="+lines[0];
    		 ResultSet resultSet = db.selectSQL(select);
    	    	try {  
    	            while (resultSet.next()) {  
    	                i=resultSet.getInt("gui_zan");
    	            }  
    	        } catch (SQLException e) {  
    	            System.out.println("显示时数据库出错。");  
    	            e.printStackTrace();  
    	        } catch (Exception e) {  
    	            System.out.println("显示出错。");  
    	            e.printStackTrace();  
    	        }
    	    update = "update addr_book_guide set "+
					"gui_zan ="+String.valueOf(i+1)+" "+
					"where id="+lines[0]; 
    	    db.updateSQL(update);
    	}
    	else if (lines[1].equals("0"))
    	{
    		 select="select * from addr_book_diary where id="+lines[0];
	   		 ResultSet resultSet = db.selectSQL(select);
	   	    	try {  
	   	            while (resultSet.next()) {  
	   	                i=resultSet.getInt("dia_zan");
	   	            }  
	   	        } catch (SQLException e) {  
	   	            System.out.println("显示时数据库出错。");  
	   	            e.printStackTrace();  
	   	        } catch (Exception e) {  
	   	            System.out.println("显示出错。");  
	   	            e.printStackTrace();  
	   	        }
	    	update = "update addr_book_diary set "+
					"dia_zan ="+String.valueOf(i+1)+" "+
					"where id="+lines[0]; 
	    	db.updateSQL(update); 
   	}
    	ans+="写下自己的评论吧~: : \n";
    	db.deconnSQL();
    	return String.valueOf(i+1);
    }
}  