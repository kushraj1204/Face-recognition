/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frcopy;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JOptionPane;


/**
 *
 * @author Kush
 */
public class insert {
    private Connection con;//to connect to mysql database
    private Statement st;//to execute queries
    private ResultSet rs;//to hold returned results
    int num;
    insert(String name) throws SQLException 
    {   
         File file = new File("temp\\");
       
        if (file.isDirectory()) { //checking if temp exists which saves our faces temporarily
           
            String[] files = file.list();

            if (files.length > 0) {
                num=files.length;
               // System.out.println("The " + file.getPath() + " is not empty!");
          
       if(      name == null)
       {   
            name= JOptionPane.showInputDialog("Enter the face name ");
       } else {
        File f = new File("temp\\");
        ArrayList<File> picfiles = new ArrayList<>(Arrays.asList(f.listFiles()));
        connection put=new connection();
         put.putdata(name,picfiles,num);
         boolean a=deleteDirectory(f);
      
       
       }
       
         }
        }
        
    }
    public static boolean deleteDirectory(File directory) {
    if(directory.exists()){
        File[] files = directory.listFiles();
        if(null!=files){
            for(int i=0; i<files.length; i++) {
                if(files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                }
                else {
                    files[i].delete();
                }
            }
        }
    }
    return(directory.delete());
}

   public void putdata(String name, ArrayList<File> picfiles,int num) {

        
        try{
		   
		    st = con.createStatement();
		   
		  for(int i=0;i<=num;i++){
                      File imgfile = picfiles.get(i);
		  FileInputStream fin = new FileInputStream(imgfile);
		 System.out.println("123");
		   PreparedStatement pre =
		   con.prepareStatement("insert into testset1 'Facename' 'FaceImage' values(?,?)");
		 
		  // pre.setDouble(1,"");
		   pre.setString(2,name);
		   pre.setBinaryStream(3,(InputStream)fin,(int)imgfile.length());
		         pre.executeUpdate();
                       // System.out.println(a);
		   System.out.println("Successfully inserted the file into the database!");
                   
             

		   pre.close();
        }
		   con.close(); 
		}catch (Exception e1){
			System.out.println(e1.getMessage());
                        
		} 
        
        
        
        
        
        
        
    }
    
}
