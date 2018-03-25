/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frcopy;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Kush
 */
public class testa {
    
    
    testa() throws IOException
    {
    int i,j,gray;float[][] avg=new float[280][280];
     BufferedImage theImage = new BufferedImage(250, 250, BufferedImage.TYPE_BYTE_GRAY);
         for( i=0; i<250; i++) {
         
        for( j=0; j<250; j++) {
            gray=512;//System.out.println(avg[i][j]);
            avg[i][j] = ((gray<<16) |(gray<<8) | gray  );
            theImage.setRGB(i,j, (int) avg[i][j]);
                    }
    }
    File output = new File("GrayScale.jpg");
    ImageIO.write(theImage, "jpg", output);
    }
    
}
/*double mindistance=0;
                   for(int i=0;i<weight[0].length;i++)
                   {    double diff=0;double distance=0;
                       for(int j=0;j<weight.length;j++)
                       {
                           diff=diff+Math.pow((currentfaceweight[j][0]-weight[j][i]), 2);
                       }
                           distance=sqrt(diff);
                           if(mindistance==0)
                           {mindistance=distance;}
                           else
                           {
                                if(distance<=mindistance)
                                {
                                        mindistance=distance;
                                        naam=namelist[i];
                                }
                           }
                           //System.out.println(distance);
                          // naam=namelist[i];
                           
                       
                   }if(mindistance>threshold)
                           {  // System.out.println(distance);
                               naam="unknown face";
                           }
                   
                
                return naam;
*/