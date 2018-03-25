package frcopy;


import Jama.Matrix;
import static frcopy.recognize.eigen;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import static java.lang.Math.round;
import static java.lang.Math.sqrt;
import static java.lang.System.exit;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import static javax.swing.JOptionPane.showMessageDialog;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Kush
 */
public class inserttry {
   static ImageIcon icon;
   private Connection con;//to connect to mysql database
    private Statement st;//to execute queries
    private ResultSet rs;//to hold returned results
    private PreparedStatement pstmt;
    Blob blob;
static double eigen[][];
static double realeigen[][];
static double diffarrange[][];
static double weight[][];
static double average[][];
static double currentfaceweight[][];
static String namelist[];
static String name;
static double threshold=7000;
connection conn;
//double currentimage[][];

  public static void matrixretrieval() throws SQLException, IOException
  {
       connection conn = new connection();
       eigen=conn.geteigenmatrix();
       weight=conn.getweightmatrix();
       average=conn.getaveragematrix();
       diffarrange=conn.getdiffarrange(average);
       namelist=conn.getname();
       
       Matrix M=new Matrix(diffarrange);
       Matrix eigenprimary=new Matrix(eigen);
       
            Matrix eigensec=M.times(eigenprimary);
            realeigen=eigensec.getArray();
            
             
             int k=weight.length;
             System.out.println(k);
             double normfact[]=new double[k];
            
            for(int i=0;i<k;i++)
          { 
              normfact[i]=0;
          }
          for(int i=0;i<k;i++)
          { 
            for(int j=0;j<(280*280);j++)
            {
               normfact[i]=normfact[i]+(realeigen[j][i]*realeigen[j][i]); 
            }
          }
          
//          System.out.println(normfact[7]);
         double problem=0;
          for(int i=0;i<(280*280);i++)
          { 
            for(int j=0;j<k;j++)
            {
               realeigen[i][j]=(realeigen[i][j])/sqrt(normfact[j]); 
               
               problem=problem+realeigen[i][j];
            }
          }
             
           eigensec=new Matrix(realeigen);
            Matrix eigensecT=eigensec.transpose(); 
            realeigen=eigensecT.getArray();
             
          //   inserttry ins=new inserttry();
            // ins.form(weight,realeigen,average);
                 
  }
    
    public inserttry() throws SQLException, IOException{
        matrixretrieval();
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
            con= DriverManager.getConnection("jdbc:mysql://localhost:3306/face","root","");
            st=con.createStatement();
            System.out.println(" done");
            
        } catch (SQLException ex) {
            Logger.getLogger(connection.class.getName()).log(Level.SEVERE, null, ex);
            showMessageDialog(null,"Please ensure your database connection first");
            exit(0);
        }
        
        
         
double weightvector[][]=null;
        try
             {
        String selectSQL = "SELECT * FROM `weight` WHERE 1";
              pstmt = con.prepareStatement(selectSQL);
            rs = pstmt.executeQuery();
            
            int j=0;int a=0;
            while (rs.next()) {
                a++;}
            
         // a counts the number of weightvectors present 
            
            rs.beforeFirst();
            int eigenvecno;
            if((0.7*a)>50)
         {eigenvecno=50;
             
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
      int a=0;
        for(int i=0;i<(weight[0].length-1);i++)
                   {    double diff=0;double distance=0;
                       for(int j=0;j<weight.length-1;j++)
                       {
                           diff=diff+Math.pow((weight[j][i]-weight[j][i+1]), 2);
                           
                          
                        
                       }
                       distance=sqrt(diff);
                         a++;
                       System.out.println(distance);
                          System.out.println(a);
                   }
        
        
        
     a=8;
    int eigenvecno=a;
        double[][] finalobt=new double[a][(280*280)];
          //System.out.println("1st "+eigTnormal[3][500]+" no turu "+weightvector[3][0]);
          //System.out.println("2nd "+eigTnormal[2][500]+" no turu "+weightvector[2][0]);
        for(int i=0;i<a;i++)
         {   
         for(int j=0;j<eigenvecno;j++)
         {
             for(int ku=0;ku<(280*280);ku++)
             {
                 finalobt[i][ku]=finalobt[i][ku]+realeigen[j][ku]*weight[j][i];
             }
         }
         }
         
           
          // Matrix thisone=new Matrix(img1);
         //  Matrix first=thisone.transpose();
          // double thisonne[][]=thisone.getArray();
           double img[][][]=new double[a][280][280];
          
           
           for(int z=0;z<a;z++)
           {
           for(int i=0;i<280;i++)
         {
             for(int j=0;j<280;j++)
             {
                img[z][i][j]=finalobt[z][(i*280+j)]+average[i][j];
             }
         }
           }
          // System.out.println("  ");
           //System.out.println(images[1][1][1]+" "+img[1][1]);
             //         System.out.println("  ");

           try {
 BufferedImage theImage = new BufferedImage(280, 280, BufferedImage.TYPE_BYTE_GRAY);
    for(int i=0; i<280; i++) {
         
        for(int j=0; j<280; j++) {
         double   grayn=round(img[0][i][j]);//System.out.println(avg[i][j]);
         int gray=(int)grayn;
            int grayt = ((gray<<16) |(gray<<8) | gray  );
            theImage.setRGB(i,j,grayt);
                    }
    }
    File output = new File("F://img12.jpg");
    ImageIO.write(theImage, "jpg", output);
    //System.out.println("banaiyo");
           } catch (Exception e){}
           
          // double d=((int)img[0][20][204] );
      //     System.out.println("hell "+weightvector[0][0]+" no "+weightvector[0][1]+" yes"+weightvector[0][2]);
           
            // yaha samma
            long stopTime = System.currentTimeMillis();
           // long elapsedTime = stopTime - startTime;
    //  System.out.println(elapsedTime);
       // System.out.println(a+" eti "+eigenvecno);
       //  System.out.println(eigvectuseful.length+" eti "+eigvectuseful[0].length);    
     

        
        
//    boolean a=deleteDirectory(f);
    
    //temp folder delete hanne 
        //connection conn=new connection();
        
        
        /*
        double[][] finalobt=new double[36][(280*280)];
          //System.out.println("1st "+eigTnormal[3][500]+" no turu "+weightvector[3][0]);
          //System.out.println("2nd "+eigTnormal[2][500]+" no turu "+weightvector[2][0]);
         for(int i=0;i<36;i++)
         {   
         for(int j=0;j<(280*280);j++)
         {
             
                 finalobt[i][j]=0;
             }
         }
        
        for(int i=0;i<36;i++)
         {   
         for(int j=0;j<36;j++)
         {
             for(int ku=0;ku<(280*280);ku++)
             {
                 finalobt[i][ku]=finalobt[i][ku]+eigen[j][ku]*weight[j][i];
             }
         }
         }
         
           
          // Matrix thisone=new Matrix(img1);
         //  Matrix first=thisone.transpose();
          // double thisonne[][]=thisone.getArray();
           double img[][][]=new double[36][280][280];
          
           
           for(int z=0;z<36;z++)
           {
           for(int i=0;i<280;i++)
         {
             for(int j=0;j<280;j++)
             {
                img[z][i][j]=finalobt[z][(i*280+j)]+average[i][j];
             }
         }
           }
          // System.out.println("  ");
           //System.out.println(images[1][1][1]+" "+img[1][1]);
             //         System.out.println("  ");

           try {
 BufferedImage theImage = new BufferedImage(280, 280, BufferedImage.TYPE_BYTE_GRAY);
    for(int i=0; i<280; i++) {
         
        for(int j=0; j<280; j++) {
         double   grayn=round(img[6][i][j])+50;//System.out.println(avg[i][j]);
         int gray=(int)grayn;
            int grayt = ((gray<<16) |(gray<<8) | gray  );
            theImage.setRGB(i,j,grayt);
                    }
    }
    File output = new File("F://img17.jpg");
    ImageIO.write(theImage, "jpg", output);
    //System.out.println("banaiyo");
           } catch (Exception e){}
           
           double d=((int)img[0][20][204] );
      //     System.out.println("hell "+weightvector[0][0]+" no "+weightvector[0][1]+" yes"+weightvector[0][2]);
           
        
        /*
        double no[][]=null;
        try
        {
        String selectSQL = "SELECT * FROM `weight` WHERE 1";
        System.out.println("here i amjfkd");
          
           // Connection conn = DriverManager.getConnection();
                 pstmt = con.prepareStatement(selectSQL);
                 
            // set parameter;
            //pstmt.setInt(1, candidateId);
            rs = pstmt.executeQuery();
            
            
            String name;
            int faceid;
            int j=0;int a=0;
            while (rs.next()) {
              
                a++;}
            
         // a counts the number of images in testset 
            
            rs.beforeFirst();
            
            while (rs.next()) {
                
               //  File fw = new File("F:\\face.jpg");
                // System.out.println("haha");
                blob = rs.getBlob(4);
               //name=rs.getString(2);
              // faceid=rs.getInt(2);
                byte[] bdata = blob.getBytes(1, (int) blob.length());
            
            String s = new String(bdata);
             String[] words = s.split(" ");
             no = new double[words.length][a];
             for (int i = 0; i < (words.length); i++) {
    
      
        
        
       no[i][j]=Double.parseDouble(words[i]);   // j is the image number remember that
      // 
       System.out.println("tori");
       
       
}
               
            j++;
            }
   
    
    }
          
          catch (Exception e){
          System.out.println("error in query execution i suppose");}
       
        
        
        
        
        
        
        
        */
    
    }

    
}
