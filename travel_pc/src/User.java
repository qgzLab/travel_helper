import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class User{   
    
	public static void login(String line)
	{
		
		String[] lines=line.split(":");
    	System.out.println("�û� :"+lines[1]+"  �����¼!");
	}
	
    public static int username_password(String line) {  
    	try{
			FileReader fr = new FileReader("D:/andorid/workspcae/pc/name_password.txt");
			BufferedReader br = new BufferedReader(fr);
		    String strline =null;
		    while ((strline=br.readLine())!=null)
		    if (strline!=null)
		    {
		    	//strline+="\n";
		    	if (strline.equals(line))
		    	{
		    		fr.close();
				    br.close();
				    return 1;
		    	}
		    }
		    fr.close();
		    br.close();
			} catch(FileNotFoundException e) {e.printStackTrace();}
            catch(IOException e) {e.printStackTrace();}
    	String[] lines=line.split(":");
    	System.out.println("�û���:"+lines[1]+"   ע��ɹ���");
    	return 0;
    }
    
    public static int pay(String line) {  
    	String[] lines=line.split(":");
    	System.out.println("�û���:"+lines[1]+"��"+lines[4]+"֧����"+lines[3]+"Ԫ");
    	return 0;
    }
    
    public static int live_talk(String line) {  
    	String[] lines=line.split(":");
    	System.out.println("�û���:"+lines[2]+"   ��������!");
    	return 0;
    } 
    public static int live_talk_goods(String line) {  
    	String[] lines=line.split(":");
    	System.out.println("�û���:"+lines[3]+"   ��������!");
    	return 0;
    }
      
     
}  