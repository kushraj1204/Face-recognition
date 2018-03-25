/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frcopy;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import static java.lang.System.exit;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import static javax.swing.JOptionPane.showMessageDialog;

/**
 *
 * @author Kush
 */
public class test{
    
    
     private Connection con;//to connect to mysql database
    private Statement st;//to execute queries
    private ResultSet rs;//to hold returned results
    public test() throws SQLException{
        int i,j,gray;
    try{
    Class.forName("com.mysql.jdbc.driver");
    }
    catch(Exception e){
         System.out.println("abc");
        System.out.println(e);
        //exit(0);
        System.out.println("abc");
        
    }
        try {
            con= DriverManager.getConnection("jdbc:mysql://localhost:3306/face","root","");
            st=con.createStatement();
            
        } catch (SQLException ex) {
            Logger.getLogger(connection.class.getName()).log(Level.SEVERE, null, ex);
            showMessageDialog(null,"Please ensure your database connection first");
            exit(0);
        }
          
        
DatabaseMetaData dbm = con.getMetaData();
// check if "employee" table is there
ResultSet tables = dbm.getTables(null, null, "average", null);
if (tables.next()) {
  // Table exists
    
    
            System.out.println("hello baby");
}
else {
    String query="create table if not exists average( averageimage longblob, number INT(6) )";
         PreparedStatement pstmt = con.prepareStatement(query);
            // set parameter;
            //pstmt.setInt(1, candidateId);
            boolean t= pstmt.execute();
            
             float[][] avg=new float[250][250];
     
        try {
 BufferedImage theImage = new BufferedImage(250, 250, BufferedImage.TYPE_BYTE_GRAY);
    for( i=0; i<250; i++) {
         
        for( j=0; j<250; j++) {
            gray=0;//System.out.println(avg[i][j]);
            avg[i][j] = ((gray<<16) |(gray<<8) | gray  );
            theImage.setRGB(i,j, (int) avg[i][j]);
                    }
    }
    File output = new File("GrayScale.jpg");
    ImageIO.write(theImage, "jpg", output);
    System.out.println("banaiyo");
    File imgfile = new File("GrayScale.jpg");
        //BufferedImage image = ImageIO.read(imgfile);
    FileInputStream fin = new FileInputStream(imgfile);
    String query1="insert into average (averageimage, number) values(?,?)";
          pstmt = con.prepareStatement(query1);
        
		   pstmt.setBinaryStream(1,(InputStream)fin,(int)imgfile.length());
                    pstmt.setInt(2,0);
		         pstmt.executeUpdate();
                       // System.out.println(a);
		   System.out.println("Successfully operated into the database!");
            // set parameter;
            //pstmt.setInt(1, candidateId);
          //  boolean t= pstmt.execute();
}

catch(Exception e) {}
     
  // Table does not exist
}
    
       
        

    
    }

        
        
        
    }
    

