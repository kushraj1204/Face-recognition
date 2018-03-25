/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frcopy;

import Jama.Matrix;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import static java.lang.Math.sqrt;
import static java.lang.System.exit;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;


public class recognize extends javax.swing.JFrame {
static ImageIcon icon;
static double eigen[][];
static double realeigen[][];
static double diffarrange[][];
static double weight[][];
static double average[][];
static double currentfaceweight[][];
static String namelist[];
static String name;
static double threshold=5400;
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
             System.out.println("hello");
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
             
             //inserttry ins=new inserttry();
            // ins.form(weight,realeigen,average);
             
             
             
   System.out.println("hello");    
  }

   
  
  
   double[][] imageacquire() {
    int width = 280;    //width of the image
    int height = 280;   //height of the image
    BufferedImage image = null;
    double[][] face=new double[width][height];
    File f = null;
    //File fw = null;
   
    //read image
    try{
      f = new File("face.jpg"); //image file path 
      //image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
      image = ImageIO.read(f);
      while(image==null)
      {image = ImageIO.read(f);}
       BufferedImage outputImage = new BufferedImage(280, 280, image.getType());
 
        // scales the input image to the output image
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(image.getScaledInstance(280, 280, Image.SCALE_SMOOTH), 0, 0, 280, 280, null);
        g2d.dispose();
      image=outputImage;
      face=makeGray(image);
     // System.out.println("Reading complete.");
//      ImageIO.write(image, "jpg", fw);
    }catch(IOException e){
      System.out.println("Error: "+e);
    }
    return face;
  }
  
  public static double[][] makeGray(BufferedImage img)
{       double face[][]=new double[280][280];
    for (int x = 0; x < img.getWidth(); ++x)
    {
    for (int y = 0; y < img.getHeight(); ++y)
    {
        int rgb = img.getRGB(x, y);
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = (rgb & 0xFF);

        int grayLevel = (int) (0.2126*r + 0.7152*g + 0.0722*b);
        face[x][y]=grayLevel;
        //int gray = (grayLevel << 16) + (grayLevel << 8) + grayLevel; 
        //img.setRGB(x, y, gray);
    }
    }
    /*ColorConvertOp colorConvert = 
        new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
    colorConvert.filter(img,img);*/
        return face;
}



    private static BufferedImage ConvertMat2Image(Mat frame) {
	
		
		MatOfByte mem = new MatOfByte();
		
		Imgcodecs.imencode(".jpg", frame, mem);
		
		byte[] byteArray = mem.toArray();
		BufferedImage buff = null;
		try {
			InputStream in = new ByteArrayInputStream(byteArray);
			buff = ImageIO.read(in);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return buff;
	}
    
    public recognize() throws SQLException, IOException {
        matrixretrieval();
        initComponents(); 
       new Thread(new Runnable() {
    public void run() {
        try {
            dothis();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(recognize.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}).start(); 
        
    }
    
    public void dothis() throws SQLException, IOException
    {
             System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
				CascadeClassifier cascadeFaceClassifier = new CascadeClassifier(
				"C:\\Users\\kush\\Downloads\\Compressed\\OpenCvObjectDetection-master\\haarcascades\\haarcascade_frontalface_default.xml");
		
		VideoCapture videoDevice = new VideoCapture();
		videoDevice.open(0);
		if (videoDevice.isOpened()) {
                                  	do {		
				Mat frameCapture = new Mat();
                                
                               
				videoDevice.read(frameCapture);
				Graphics g = jPanel1.getGraphics();
				MatOfRect faces = new MatOfRect();
				cascadeFaceClassifier.detectMultiScale(frameCapture, faces);								
				Rect rectCrop=null;
				for (Rect rect : faces.toArray()) {
					
				
                                        Imgproc.rectangle(frameCapture, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
							new Scalar(0, 100, 0),3);
                                        rectCrop = new Rect(rect.x, rect.y, rect.width, rect.height);
                                        Mat faceImage = frameCapture.submat(rect);
                                       Imgcodecs.imwrite("face.jpg", faceImage);
                                       double[][] currentface=new double[280][280];
                                       double[][] currentfacedifference=new double[280][280];
                                       double [][] currentfacediffarranged=new double[280*280][1];
                                       currentface=imageacquire();
                                        for (int x = 0; x < 280; ++x){
                                        for (int y = 0; y < 280; ++y)
                                        {

                                            currentfacedifference[x][y]=currentface[x][y]-average[x][y];
                                        }
                                        } 
                                        
                                        for (int x = 0; x < 280; ++x){
                                        for (int y = 0; y < 280; ++y)
                                        {

                                            currentfacediffarranged[(280*x)+y][0]=currentfacedifference[x][y];
                                        }
                                        } 
                                       // System.out.println();
                                         Matrix diffarrange=new Matrix(currentfacediffarranged);
                                        Matrix eigT=new Matrix(realeigen);
                                        if(eigT.getColumnDimension()== diffarrange.getRowDimension())
                                        {  
                                        Matrix weightofthis=eigT.times(diffarrange);
                                        currentfaceweight=weightofthis.getArray();
                                        
                                         name=findname();
                                        
                                        
                                        
                                       // know thisface=new know();
                                     //   String facename=thisface.facename();
                                        
                                        
                                        // a lot of calls has to be made here ISD perhaps :D
                                        
                                        
                                        }
                                        
                                        else{
                                        name="unknown face";
                                        matrixretrieval();
                                        }
                                        
                                        Imgproc.putText(frameCapture, name, new Point(rect.x,rect.y-5), 1, 2, new Scalar(0,0,255));								
					
			
                                }
                                
				
				Image img2=ConvertMat2Image(frameCapture);
                                
                                icon = new ImageIcon(img2); 
                                jLabel1.setIcon(icon);
                               

		
				//System.out.println(String.format("%s (FACES)detected.", faces.toArray().length));
      }while(true);
			}
		 else {
			System.out.println("Camera not working properly");
			return;
		}
       
      
    
    }
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 740, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 452, Short.MAX_VALUE)
                .addContainerGap())
        );

        jButton1.setLabel("Register new face");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setLabel("Exit");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Refresh");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(51, 51, 51)
                .addComponent(jButton3)
                .addGap(41, 41, 41)
                .addComponent(jButton2)
                .addGap(257, 257, 257))
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton1)
                    .addComponent(jButton3))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
           JFrame frame = new register();
		frame.setVisible(true);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        exit(0);
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
    try {
        matrixretrieval();
    } catch (SQLException | IOException ex) {
        Logger.getLogger(recognize.class.getName()).log(Level.SEVERE, null, ex);
    }
    }//GEN-LAST:event_jButton3ActionPerformed

    
     //* @param args the command line arguments
   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables

       public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(recognize.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(recognize.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(recognize.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(recognize.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new recognize().setVisible(true);
                } catch (SQLException | IOException ex) {
                    Logger.getLogger(recognize.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    private String findname() {
            String naam;
                
                   for(int i=0;i<weight[0].length;i++)
                   {    double diff=0;double distance=0;
                       for(int j=0;j<weight.length;j++)
                       {
                           diff=diff+Math.pow((currentfaceweight[j][0]-weight[j][i]), 2);
                       }
                           distance=sqrt(diff);
                           //System.out.println(distance);
                           naam=namelist[i];
                           if(distance<=threshold)
                           {  // System.out.println(distance);
                               return naam;
                           }
                       
                   }
                   naam="unknown face";
                
                return naam;
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
