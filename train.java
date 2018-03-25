/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frcopy;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;
import java.awt.image.BufferedImage;
import java.io.File;
//import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import static javax.swing.JOptionPane.showMessageDialog;

/**
 *
 * @author Kush
 */
public class train {
    //double size=200;
    long startTime = System.currentTimeMillis();
    double[][] average=new double[280][280];//average image
    
    private Connection con;//to connect to mysql database
    private Statement st;//to execute queries
    private ResultSet rs;//to hold returned 
    
    private PreparedStatement pstmt;
    int eigenvecno;
    train() throws SQLException
    {   
         for (int x = 0; x < 280; ++x)
    {
    for (int y = 0; y < 280; ++y)
    {
        average[x][y]=0;
    }
    }  
        
        String selectSQL = "SELECT * FROM testset1 WHERE 1";
        // System.out.println("conn done");
        try {
            con= DriverManager.getConnection("jdbc:mysql://localhost:3306/facecopy","root","");
            st=con.createStatement();
           // System.out.println("conn done");
            
        } catch (SQLException ex) {
            Logger.getLogger(connection.class.getName()).log(Level.SEVERE, null, ex);
            showMessageDialog(null,"Please ensure your database connection first");
            exit(0);
        }

        try  {
           // Connection conn = DriverManager.getConnection();
                 pstmt = con.prepareStatement(selectSQL);
            // set parameter;
            //pstmt.setInt(1, candidateId);
            rs = pstmt.executeQuery();
            
            String filename;
// Blob blob = resultSet.getBlob(index);
//InputStream stream = blob.getBinaryStream(0, blob.length());

        //BufferedImage image = ImageIO.read(stream);
            // write binary stream into file
           // File file = new File(filename);
           // FileOutputStream output = new FileOutputStream(file);
           // System.out.println("Writing to file " + file.getAbsolutePath());
            int a=0;
            while (rs.next()) {
                //System.out.println(a);
                a++;}
            
         // a counts the number of images in testset 
            
            rs.beforeFirst();
            String name[]=new String[a];
            int faceid[]=new int[a];
            double[][][]images=new double[a][280][280];//images
            double[][][]diff=new double[a][280][280];//image-average
            double[][]diffarrange=new double[280*280][a];//diff arranged as column 
            
            int aa=0;
         
            
            while (rs.next()) {
                
               //  File fw = new File("F:\\face.jpg");
                // System.out.println("haha");
               Blob blob = rs.getBlob(3);
               name[aa]=rs.getString(2);
               faceid[aa]=rs.getInt(1);
               
              // System.out.println("haha");
                InputStream stream = blob.getBinaryStream();
                //System.out.println("haha");
                BufferedImage image = ImageIO.read(stream);
                
                
    for (int x = 0; x < 280; ++x)
    {
    for (int y = 0; y < 280; ++y)
    {
        int rgb = image.getRGB(x, y);
        
        int b = (rgb & 0xFF);
        
        
        images [aa][x][y]=b;
        
        average[x][y]=average[x][y]+images[aa][x][y];
    }
    }     
               
              //  ImageIO.write(image, "jpg", fw);
                aa++;
            }System.out.println(a+" ding bhayo");
            System.out.println(average[279][279]); 
    for (int x = 0; x < 280; ++x)
     {
        for (int y = 0; y < 280; ++y)
        {
            average[x][y]=average[x][y]/a;
          //   average[x][y]=(double)Math.round(average[x][y] * 1000000000d) / 1000000000d;
            
        }
     }
          System.out.println(average[279][279]);  
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
            //diffarrange[l][k]=(double)Math.round(diffarrange[l][k] * 100000d) / 100000d;
            l++;
            }
         }  
            k++;}
            
            
            Matrix M=new Matrix(diffarrange);
            Matrix MT=M.transpose();
            Matrix C=MT.times(M);
             double cn[][]=C.getArray();
           /* System.out.println(" ");System.out.println(" ");
           
            for(int i=0;i<3;i++)
             { 
                 
                 
                 for(int j=0;j<3;j++)
                 {  //System.out.println(" "); 
                     //cn[i][j]=(double)Math.round(cn[i][j] * 1000000000d) / 1000000000d;   
                     //eigvectuseful[i][j]=eigvect[i][j];
                 }
             }
                  */ 
            Matrix Cv=new Matrix(cn);
            C=Cv;
           
           EigenvalueDecomposition eigval =new EigenvalueDecomposition(C);
           double[] eigvalue = eigval.getRealEigenvalues();
        //   double[] eigpos=new double[eigvalue.length];
          
           
     
           
             Matrix eigvec=eigval.getV();
          double eigvect[][]=eigvec.getArray();
          
          
          
          
       //   int a[]={5,8,6,1,4,55,6,1,7,8,252,3,15,49,24,76};
          
        int index[]=new int[eigvalue.length];
        double tem;
        double temp[]=new double[eigvalue.length];
        System.arraycopy(eigvalue, 0, temp, 0, eigvalue.length);
        for(int i=0;i<eigvalue.length;i++)
        {
            for(int j=i+1;j<eigvalue.length;j++)
          {  
            if(temp[i]<temp[j])
            {
                tem=temp[i];
                temp[i]=temp[j];
                temp[j]=tem;
            }
          }
        }
        
        // System.out.println(a[i]);
        
         for(int i=0;i<eigvalue.length;i++)
        {   
            for(int j=0;j<eigvalue.length;j++)
          {  
            if(temp[i]==eigvalue[j])
            {
              index[i]=j;
             
            } 
            
          }
        }
          
          
          
          
          
          
          
          
          
          
          
          
          
          
          
          
          
          
                
          
          
          
          
          System.out.println(eigvalue.length);
          
         if((1*eigvalue.length)>70)
         {eigenvecno=70;
             
         }
         else
         {
         eigenvecno=eigvalue.length;}
         //eigenvecno=80;
         double eigvectusefull[][]=new double[a][eigenvecno];
         double eigvectuseful[][];
         for(int i=0;i<a;i++)
             {
                 for(int j=0;j<eigenvecno;j++)
                 {
                     eigvectusefull[i][j]=eigvect [i][index[j]];
                 }
             }
   System.out.println(eigenvecno);      
         //double weight[][]=new double[a][eigenvecno];
          Matrix eigvectuseful50=new Matrix(eigvectusefull);
          Matrix reqeigvector=M.times(eigvectuseful50);
            eigvectuseful=reqeigvector.getArray();  
            
            
            System.out.println("ÿetro laamo time lago");
            //System.out.println(eigvectuseful[0][8]);
            double normfact[]=new double[k];
            
            for(int i=0;i<eigenvecno;i++)
          { 
              normfact[i]=0;
          }
          for(int i=0;i<eigenvecno;i++)
          { 
            for(int j=0;j<(280*280);j++)
            {
               normfact[i]=normfact[i]+(eigvectuseful[j][i]*eigvectuseful[j][i]); 
            }
          }
          
//          System.out.println(normfact[7]);
         double problem=0;
          for(int i=0;i<(280*280);i++)
          { 
            for(int j=0;j<eigenvecno;j++)
            {
               eigvectuseful[i][j]=(eigvectuseful[i][j])/sqrt(normfact[j]); 
               
               //problem=problem+eigvectuseful[i][j];
            }
          }
            System.out.println(problem+" problems found");
          //Matrix M=new Matrix(diffarrange);
           // Matrix MT=M.transpose();
         Matrix eigusefuljama=new Matrix(eigvectuseful);
         Matrix eigT=eigusefuljama.transpose();
          double eigTnormal[][]=eigT.getArray();  
         Matrix weight=eigT.times(M);
         double weightvector[][]=weight.getArray();
                    System.out.println(weightvector[0][0]);
                    
                    
                    
                    
                    
                    
     
                                connection conn=new connection();
                                          
                    
                    
       
     conn.putaverage(average);    
      conn.putweight(weightvector,name,faceid);
            conn.puteigen(eigvectusefull);                
        File folder = new File("temporary");
       boolean deletion=conn.deleteDirectory(folder);            
                    
                    
                    
                    
                    
                    
                    
                    
                    
                    
          /*          
         // yaha bata checking matra ho 
         double[][] finalobt=new double[a][(280*280)];
          //System.out.println("1st "+eigTnormal[3][500]+" no turu "+weightvector[3][0]);
          //System.out.println("2nd "+eigTnormal[2][500]+" no turu "+weightvector[2][0]);
        for(int i=0;i<a;i++)
         {   
         for(int j=0;j<eigenvecno;j++)
         {
             for(int ku=0;ku<(280*280);ku++)
             {
                 finalobt[i][ku]=finalobt[i][ku]+eigTnormal[j][ku]*weightvector[j][i];
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
                      System.out.println("twaa  ");
/*
           try {
 BufferedImage theImage = new BufferedImage(280, 280, BufferedImage.TYPE_BYTE_GRAY);
 for (int kush=0;kush<a;kush++) 
 {
 for(int i=0; i<280; i++) {
         
        for(int j=0; j<280; j++) {
         double   grayn=round(img[kush][i][j]);//System.out.println(avg[i][j]);
         int gray=(int)grayn;
            int grayt = ((gray<<16) |(gray<<8) | gray  );
            theImage.setRGB(i,j,grayt);
                    }
    }
   // File output = new File("F://"+kush+".jpg");
   // ImageIO.write(theImage, "jpg", output);
 }
    //System.out.println("banaiyo");
           } catch (Exception e){
           System.out.println(e);
           }
  */         
          // double d=((int)img[0][20][204] );
      //     System.out.println("hell "+weightvector[0][0]+" no "+weightvector[0][1]+" yes"+weightvector[0][2]);
           
            // yaha samma
            long stopTime = System.currentTimeMillis();
            long elapsedTime = stopTime - startTime;
      System.out.println(elapsedTime);
       // System.out.println(a+" eti "+eigenvecno);
       //  System.out.println(eigvectuseful.length+" eti "+eigvectuseful[0].length);    
            
        } catch (SQLException | IOException e) {
            System.out.println("this is it "+e.getMessage());
        } finally {
            try { 
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        
        
//    boolean a=deleteDirectory(f);
    
    //temp folder delete hanne 
        //connection conn=new connection();
        
    
        
        /*
        
        
        
        
        
        
        
        
        
        
        
        
        */
        
        
        
    }
        
    
    
}
