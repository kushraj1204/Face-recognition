/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frcopy;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JFrame;

/**
 *
 * @author Kush
 */
public class FRcopy {
double[][] average=new double[280][280];//average image
    
    private Connection con;//to connect to mysql database
    private Statement st;//to execute queries
    private ResultSet rs;//to hold returned 
    
    private PreparedStatement pstmt;
    int eigenvecno;
    /**
     * @param args the command line arguments
     * @throws java.lang.InterruptedException
     * @throws java.sql.SQLException
     */
    public static void main(String[] args) throws InterruptedException, SQLException, IOException {
        
        connection db=new connection();
       
       // double[][] eigen=db.geteigenmatrix();
        double[][] weight = db.getweightmatrix();
        //double[][] average = db.getaveragematrix();
        
       /* if(eigen== null || weight==null || average==null)
        {  register f = new register();
              
              
		f.setVisible(true);
              f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        }*/
        if(weight==null)
        {  register f = new register();
              
              
		f.setVisible(true);
              f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        }
             else
        {
              recognize f = new recognize();
              // inserttry in=new inserttry();
              
		f.setVisible(true);
              f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        }
      //   inserttry in=new inserttry();
      //train a=new train();
		
        // TODO code application logic here
        
        /*register f = new register();
		f.setVisible(true);
              f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);*/
    }
    
}
