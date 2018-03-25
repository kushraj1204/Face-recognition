/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frcopy;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import static java.lang.Math.sqrt;
import static java.lang.System.exit;
import java.sql.Blob;
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
public class connection {
    
    
    
     private Connection con;//to connect to mysql database
    private Statement st;//to execute queries
    private ResultSet rs;//to hold returned results
    private PreparedStatement pstmt;
    Blob blob;
    
    public connection() throws SQLException{
    try{
    Class.forName("com.mysql.jdbc.driver");
    }
    catch(Exception e){
       //  System.out.println("abc");
        System.out.println(e);
        //exit(0);
       // System.out.println("abc");
        
    }
        try {
            con= DriverManager.getConnection("jdbc:mysql://localhost:3306/facecopy","root","");
            st=con.createStatement();
            System.out.println(" done");
            
        } catch (SQLException ex) {
            Logger.getLogger(connection.class.getName()).log(Level.SEVERE, null, ex);
            showMessageDialog(null,"Please ensure your database connection first");
            exit(0);
        }
        avg();
    
    }

   public void putdata(String name, ArrayList<File> picfiles,int num) {

        
        try{
		   
		    st = con.createStatement();
		   
		  for(int i=0;i<=num;i++){
                      File imgfile = picfiles.get(i);
		  FileInputStream fin = new FileInputStream(imgfile);
		 System.out.println("123");
		   PreparedStatement pre =
		   con.prepareStatement("insert into testset1(Facename,FaceImage) values(?,?)");
		 
		  // pre.setDouble(1,"");
		   pre.setString(1,name);
		   pre.setBinaryStream(2,(InputStream)fin,(int)imgfile.length());
		   pre.executeUpdate();
		   System.out.println("Successfully inserted the file into the database!");

		   pre.close();
        }
		   con.close(); 
		}catch (Exception e1){
			System.out.println(e1.getMessage());
                        
		} 
        
        
        
        
        
        
        
    }

    private void avg() throws SQLException {
        int i,j,gray;
DatabaseMetaData dbm = con.getMetaData();
// check if "employee" table is there
ResultSet tables = dbm.getTables(null, null, "average", null);
if (tables.next()) {
  // Table exists
    
    
           System.out.println("table exists ");
}
else {
    String query="create table if not exists average( averageimage longblob, number INT(6) )";
         PreparedStatement pstmt = con.prepareStatement(query);
            // set parameter;
            //pstmt.setInt(1, candidateId);
            boolean t= pstmt.execute();
            
             float[][] avg=new float[280][280];
     
        try {
 BufferedImage theImage = new BufferedImage(250, 250, BufferedImage.TYPE_BYTE_GRAY);
System.out.println("chiryo");
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
    
       
             

// throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
    
 
    void putaverage(double[][] average) throws FileNotFoundException, SQLException {
           File theDir = new File("temporary");
if (!theDir.exists()) {
        boolean result = false;

    try{
        theDir.mkdir();
        result = true;
    } 
    catch(SecurityException se){
        //handle it
    }        
}
      try {
    PrintWriter writer = new PrintWriter(new File("temporary/average.txt"));

    for(int i=0; i<average.length; i++){
        for(int j=0; j<average[0].length; j++){

          //use this if your array has (int,double..)
              // writer.write(String.valueOf(a[i][j])+" "); //Here you parse the int from array to String.


           //use this if your array has String
             writer.write(average[i][j]+" "); //Its String so you dont have to use String.valueOf(something(int,double,...)
        }
       writer.println(); //leave one line 
    }

    writer.flush();  //flush the writer
    writer.close();  //close the writer      



   } catch (FileNotFoundException e) {      
     e.printStackTrace();
      
   }

      try{
        File imgfile = new File("temporary/average.txt");
		  FileInputStream fin = new FileInputStream(imgfile);
		 System.out.println("123");
		   PreparedStatement pre =
		   con.prepareStatement("insert into average (averageimage) values(?)");
		 
		  // pre.setDouble(1,"");
		  // pre.setString(2,name);
		   pre.setBinaryStream(1,(InputStream)fin,(int)imgfile.length());
		         pre.executeUpdate();
                       // System.out.println(a);
		   System.out.println("Successfully inserted the average file into the database!");
                   
                    pre =
		   con.prepareStatement("DELETE FROM `average` WHERE id NOT IN ( \n" +
                                        "  SELECT id \n" +
                                        "  FROM ( \n" +
                                        "    SELECT id \n" +
                                        "    FROM `average` \n" +
                                        "    ORDER BY id DESC \n" +
                                        "    LIMIT 1\n" +
                                        "  )  x\n" +
                                        ")");
		        pre.executeUpdate();
      }
      catch (Exception e1){
			System.out.println(e1.getMessage()+" unable");
                        
		} 
         deleteDirectory(theDir);
    
    
    
    
    
    }

    void putweight(double[][] weightvector,String[] name,int[] faceid) throws FileNotFoundException, SQLException {
         File theDir = new File("temporary");
if (!theDir.exists()) {
        boolean result = false;

    try{
        theDir.mkdir();
        result = true;
    } 
    catch(SecurityException se){
        //handle it
    }        
}
        PreparedStatement pre =con.prepareStatement("select * from weight where 1");
                 // System.out.println("ghanta jasto");
                  rs = pre.executeQuery();
                   
               int id=0;
            while (rs.next()) {
               id=rs.getInt(1);                           
            }
                  
            System.out.println(id);
        
        
        
        for(int k=0;k<weightvector[0].length;k++)
        {
              try {
    PrintWriter writer = new PrintWriter(new File("temporary/weightvector.txt"));

    for(int i=0; i<weightvector.length; i++){
        

          //use this if your array has (int,double..)
              // writer.write(String.valueOf(a[i][j])+" "); //Here you parse the int from array to String.


           //use this if your array has String
             writer.write(weightvector[i][k]+" "); //Its String so you dont have to use String.valueOf(something(int,double,...)
       
       //writer.println(); //leave one line 
    }

    writer.flush();  //flush the writer
    writer.close();  //close the writer      



   } catch (FileNotFoundException e) {      
     e.printStackTrace();
   }

              try{
                        
        File imgfile = new File("temporary/weightvector.txt");
		  FileInputStream fin = new FileInputStream(imgfile);
		
		   pre =
		   con.prepareStatement("insert into weight (name,faceid,weightvector) values(?,?,?)");
		 
		   pre.setDouble(2,faceid[k]);
		   pre.setString(1,name[k]);
		   pre.setBinaryStream(3,(InputStream)fin,(int)imgfile.length());
		         pre.executeUpdate();
                       // System.out.println(a);
		   System.out.println("Successfully inserted the weight file into the database!");
                   
                      pre =
		   con.prepareStatement("DELETE FROM `weight` WHERE id <= ?");
                      pre.setInt(1, id);
		        pre.executeUpdate();
                   
              }
              catch (Exception e1){
			System.out.println(e1.getMessage());
                        
		} 
         
    
    

        
        }
        
    boolean a=deleteDirectory(theDir);
    }

    void puteigen(double[][] eigTnormal) {
          File theDir = new File("temporary");
if (!theDir.exists()) {
        boolean result = false;

    try{
        theDir.mkdir();
        result = true;
    } 
    catch(SecurityException se){
        //handle it
    }        
} 
        
        
        
                    try {
    PrintWriter writer = new PrintWriter(new File("temporary/eigen.txt"));

    for(int i=0; i<eigTnormal.length; i++){
        for(int j=0; j<eigTnormal[i].length; j++){

          //use this if your array has (int,double..)
              // writer.write(String.valueOf(a[i][j])+" "); //Here you parse the int from array to String.


           //use this if your array has String
             writer.write(eigTnormal[i][j]+" "); //Its String so you dont have to use String.valueOf(something(int,double,...)
        }
       writer.println(); //leave one line 
    }

    writer.flush();  //flush the writer
    writer.close();  //close the writer      



   } catch (FileNotFoundException e) {      
   }       
      
                    
                    
                     try{
		   
		    st = con.createStatement();
		   System.out.println("here");
		  
                      File imgfile = new File("temporary/eigen.txt");
		  FileInputStream fin = new FileInputStream(imgfile);
		 
		   PreparedStatement pre =
		   con.prepareStatement("insert into eigenfaces(Eigenface) values(?)");
		 System.out.println("123");
		  // pre.setDouble(1,"");
		  // pre.setString(1,name);
		   pre.setBinaryStream(1,(InputStream)fin,(int)imgfile.length());
                   System.out.println("123");
		   pre.executeUpdate();
		   System.out.println("Successfully inserted the eigen file into the database!");

		   pre.close();
                    pre = con.prepareStatement("DELETE FROM `eigenfaces` WHERE id NOT IN ( \n" +
                                        "  SELECT id \n" +
                                        "  FROM ( \n" +
                                        "    SELECT id \n" +
                                        "    FROM `eigenfaces` \n" +
                                        "    ORDER BY id DESC \n" +
                                        "    LIMIT 1\n" +
                                        "  )  x\n" +
                                        ")");
		        pre.executeUpdate();
        
		  // con.close(); 
		}catch (Exception e1){
			System.out.println(e1.getMessage());
                        
		} 
         
    boolean a=deleteDirectory(theDir);
    }

    double[][] geteigenmatrix() {
        double[][] eig = null;
        int a=0;
         try
             {
        String selectSQL = "SELECT * FROM `testset1` WHERE 1";
              pstmt = con.prepareStatement(selectSQL);
            rs = pstmt.executeQuery();
            
            
            while (rs.next()) {
                a++;}
             }
         catch (Exception e){
          System.out.println("error in query execution i suppose");}
       
        
        
        
        
        
        
       try
        {
        String selectSQL = "SELECT * FROM `eigenfaces` WHERE 1";
        
                 pstmt = con.prepareStatement(selectSQL);
             
            rs = pstmt.executeQuery();
        
            while (rs.next()) {
              
                blob = rs.getBlob(2);
               
            }
           
            byte[] bdata = blob.getBytes(1, (int) blob.length());
            
            String s = new String(bdata);
         
            String[] words = s.split(" ");
             
        double[] no = new double[words.length-1];
for (int i = 0; i < (words.length-1); i++) {
      no[i]=Double.parseDouble(words[i]);
}
          int j=  (words.length-1)/a;
         
      eig=new double[a][j];
   
for(int i=0;i<a;i++)
{
    for(int k=0;k<j;k++)
    {   
        eig[i][k]=no[j*i+k];
    }
}
      
    }
          
          catch (Exception e){
          System.out.println("error in query execution i suppose");}
       
       
       
       
       
       
       
       
       
       
       
       
       
       
       
       
       
       
       
        return eig;
                      //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    double[][] getweightmatrix() {
double weightvector[][]=null;
        try
             {
        String selectSQL = "SELECT * FROM `weight` WHERE 1";
              pstmt = con.prepareStatement(selectSQL);
            rs = pstmt.executeQuery();
            
            int j=0;int a=0;
            while (rs.next()) {
                a++;}
            
            System.out.println("tang tung dhis");
            System.out.println(a);
         // a counts the number of weightvectors present 
            
            rs.beforeFirst();
            int eigenvecno;
            if((1*a)>70)
         {eigenvecno=70;
             
         }
         else
         {
         eigenvecno=a;}
            weightvector = new double[eigenvecno][a];
            while (rs.next()) {
              
                blob = rs.getBlob(4);
              
                byte[] bdata = blob.getBytes(1, (int) blob.length());
            
                String s = new String(bdata);
                String[] words = s.split(" ");
                
                
                for (int i = 0; i < (words.length); i++) { 
       weightvector[i][j]=Double.parseDouble(words[i]);   // j is the image number remember that
                    }
               
            j++;
            }
   
    
    }
          
          catch (SQLException | NumberFormatException e){
          System.out.println("error in query execution i suppose");}
        return weightvector;       
// throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    double[][] getaveragematrix() {
       
        
        double[][] avg = null;
        try
        {
        String selectSQL = "SELECT * FROM `average` WHERE 1";
            pstmt = con.prepareStatement(selectSQL);
            rs = pstmt.executeQuery();
                while (rs.next()) {

                blob = rs.getBlob(1);
              
            }
            
            byte[] bdata = blob.getBytes(1, (int) blob.length());
            String s = new String(bdata);
       
            String[] words = s.split(" ");
        double[] no = new double[words.length-1];
for (int i = 0; i < (words.length-1); i++) {
   // no[i]=0;
    no[i]=Double.parseDouble(words[i]);
}
int j=  (int) sqrt(words.length-1);
      avg=new double[j][j];
for(int i=0;i<j;i++)
{
    for(int k=0;k<j;k++)
    {   
        avg[i][k]=no[280*i+k];
    }
}
    
        }
          
          catch (Exception e){
          System.out.println("error in query execution i suppose");}
        return avg;
// throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    String[] getname() {
        String name[] = null;
        try
             {
        String selectSQL = "SELECT * FROM `weight` WHERE 1";
              pstmt = con.prepareStatement(selectSQL);
            rs = pstmt.executeQuery();
            
            int j=0;int a=0;
            while (rs.next()) {
                a++;}
            
         // a counts the number of weightvectors present 
            name=new String [a];
            rs.beforeFirst();
            
            while (rs.next()) {
                name[j]=rs.getString(2);
                j++;
              }
   
    
    }
          
          catch (SQLException | NumberFormatException e){
          System.out.println("error in query execution i suppose");}
        
        
        
        return name;//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

    double[][] getdiffarrange(double[][] average) throws SQLException, IOException {
        
//long startTime = System.currentTimeMillis();
    double diffarrange[][] = null;
//=new double[280][280];//average image
    
    
        
        String selectSQL = "SELECT * FROM testset1 WHERE 1";
        // System.out.println("conn done");
        
        try  {
         
                 pstmt = con.prepareStatement(selectSQL);
           
            rs = pstmt.executeQuery();
            
            String filename;

            int a=0;
            while (rs.next()) {
                //System.out.println(a);
                a++;}
            
         
            rs.beforeFirst();
          //  String name[]=new String[a];
           // int faceid[]=new int[a];
            double[][][]images=new double[a][280][280];//images
            double[][][]diff=new double[a][280][280];//image-average
            diffarrange=new double[280*280][a];//diff arranged as column 
            
            int aa=0;
         
            
            while (rs.next()) {
                
               //  File fw = new File("F:\\face.jpg");
                // System.out.println("haha");
               Blob bloba = rs.getBlob(3);
           //    name[aa]=rs.getString(2);
             //  faceid[aa]=rs.getInt(1);
               
              // System.out.println("haha");
                InputStream stream = bloba.getBinaryStream();
                //System.out.println("haha");
                BufferedImage image = ImageIO.read(stream);
                
                
    for (int x = 0; x < 280; ++x)
    {
    for (int y = 0; y < 280; ++y)
    {
        int rgb = image.getRGB(x, y);
        
        int b = (rgb & 0xFF);
        
        
        images [aa][x][y]=b;
      
    }
    }     
               
              //  ImageIO.write(image, "jpg", fw);
                aa++;
            }//System.out.println(a+" ding bhayo");
     
    for (int z = 0; z < a; ++z){
    for (int x = 0; x < 280; ++x){
    for (int y = 0; y < 280; ++y)
    {
        
        diff[z][x][y]=images[z][x][y]-average[x][y];
    }
    } 
    }
           
           
           
        int k=0;
            while(k<a)
            {//fun(k,d,a);
                int l=0;
            for(int i=0;i<280;i++)
        {
            for(int j=0;j<280;j++)
            { diffarrange[l][k]=diff[k][i][j];
            diffarrange[l][k]=(double)Math.round(diffarrange[l][k] * 100000d) / 100000d;
            l++;
            }
         }  
            k++;}
    
        }
        catch (SQLException | NumberFormatException e){
          System.out.println("error in query execution i suppose");}
        
        
   return diffarrange;
//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
}
/*Row size too large (> 8126). 
Changing some columns to TEXT or BLOB 
or using ROW_FORMAT=DYNAMIC or 
ROW_FORMAT=COMPRESSED may help.
In current row format, BLOB prefix of 768 bytes is stored inline.
  */