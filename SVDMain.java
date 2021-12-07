import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import Jama.Matrix;
import Jama.SingularValueDecomposition;

public class SVDMain 
{	
	static public void printMatrix(Matrix m)
	   {
	        double[][] d = m.getArray();
	        for(int row = 0; row < d.length; row++)
	        {
	            for(int col = 0; col < d[row].length; col++)
	            {
	                System.out.printf("%6.4f\t", m.get(row, col));		                
	            }
	            System.out.println();
	        }
	        System.out.println();
	    }
 
		public static void AsciiToBinary(String inputPGM) throws IOException
		{
		  Scanner sc=new Scanner(new FileReader(inputPGM));
		  DataOutputStream out = new DataOutputStream(new FileOutputStream(inputPGM.substring(0,inputPGM.indexOf("."))+"_b.pgm"));
		  sc.nextLine();	
		  sc.nextLine();		  
	  
		    int width=sc.nextInt();	  
		    byte[] bytes_w = new byte[2];
			bytes_w[0]=(byte) (width & 0xFF);
			bytes_w[1]=(byte)((width >> 8) & 0xFF);
			out.write(bytes_w[1]);
            out.write(bytes_w[0]);
            
            int height=sc.nextInt();          
            byte[] bytes_h = new byte[2];
			bytes_h[0]=(byte)(height & 0xFF);
			bytes_h[1]=(byte)((height >> 8) & 0xFF);
			out.write(bytes_h[1]);
            out.write(bytes_h[0]); 
            
            int max_pixel=sc.nextInt();
  		    out.write(max_pixel);
         
		  for(int i=0;i<height;i++)
		  {
			  for(int j=0;j<width;j++)
			  {
				  int d=sc.nextInt();
				  out.write(d);
			  }
		  }
		  sc.close();
		  out.close();
		}

		public static void BinaryViewable(String inputPGM) throws IOException
		{
			Scanner sc=new Scanner(new FileReader(inputPGM));
			DataOutputStream out = new DataOutputStream(new FileOutputStream(inputPGM.substring(0,inputPGM.indexOf("."))+"_Viewb.pgm"));
			sc.nextLine();
			sc.nextLine();
			out.write("P5\n".getBytes());
			int width=sc.nextInt();
			int height=sc.nextInt();
			out.write((width + " " + height + "\n").getBytes());

			int max_pixel=sc.nextInt();
			out.write((max_pixel + "\n").getBytes());

			for(int i=0;i<height;i++)
			{
				for(int j=0;j<width;j++)
				{

					int d=sc.nextInt();
					out.write(d);
				}
			}
			sc.close();
			out.close();
		}



		public static void BinarytoAscii(String inputBin) throws IOException
		{
		  DataInputStream in = new DataInputStream(new FileInputStream(inputBin));
		  PrintWriter bw=null;
		  bw = new PrintWriter(inputBin.substring(0,inputBin.indexOf("."))+"_2_ASCII.pgm");
		  bw.write("P2\n");			 
		  byte b1=in.readByte();
		  byte b2=in.readByte();
  		  int high = (Byte)b1 >= 0 ? (Byte)b1 : 256 + (Byte)b1;
		  int low = (Byte)b2 >= 0 ? (Byte)b2: 256 + (Byte)b2;
		  int width = low | (high << 8);			  
		  System.out.println("width="+width);
		  bw.write(width+"\t");
		  byte b3=in.readByte();
		  byte b4=in.readByte();
  		  int high1 = (Byte)b3 >= 0 ? (Byte)b3 : 256 + (Byte)b3;
		  int low1 = (Byte)b4 >= 0 ? (Byte)b4: 256 + (Byte)b4;
		  int height = low1 | (high1 << 8);			  
		  System.out.println("height="+height);
		  bw.write(height+"\n");
		  int size=(width*height)+1;
		  byte bs[]=new byte[size];
		  in.read(bs);
		  
		// read each byte for max pixel
		  Byte b=bs[0];
		  int max_pixel=(b& 0xff);
		  System.out.println("max_pixel="+max_pixel);
		  bw.write(max_pixel+"\n");
		  
		  int p=0,q=0;
		  System.out.println("rows:="+width +"and"+"cols" + height);
		  
		  for(int k=1;k<height*width;k++)
		  {
			q=q+1;
			if(q==height)
			{
				bw.write("\n");
				q=0;
			}
			  int d=bs[k]&0xff;
			 // System.out.println(d+"value is");
			  bw.write(d+"\t");
		  }
		  bw.flush();
		  bw.close();
		}
		
	   public static void SVD_File_to_Byte(String f,String f1,String low_rank) throws Exception
		   {			 
			  Scanner sc=new Scanner(new FileReader(f)); 
			  int width_limit=sc.nextInt();
			  int height_limit=sc.nextInt();
			  int maxpixel=sc.nextInt();
			  sc.close();
			   
			  int lowrank=Integer.parseInt(low_rank);
			  
			  DataInputStream in = new DataInputStream(new FileInputStream(f));
			  
			  DataOutputStream out = new DataOutputStream(new FileOutputStream(f.substring(0, f.indexOf(".")) + lowrank + ".pgm.SVD"));
			  
			    byte[] bytes_w = new byte[2];
				bytes_w[0]=(byte) (width_limit & 0xFF);
				bytes_w[1]=(byte)((width_limit >> 8) & 0xFF);
				out.write(bytes_w[1]);
	            out.write(bytes_w[0]);
	            
	                    
	            byte[] bytes_h = new byte[2];
				bytes_h[0]=(byte)(height_limit & 0xFF);
				bytes_h[1]=(byte)((height_limit >> 8) & 0xFF);
				out.write(bytes_h[1]);
	            out.write(bytes_h[0]); 
	            
	            
	  		    out.write(maxpixel);
	  		    
			  int low_rank1=lowrank;
			  bytes_w[0]=(byte) (low_rank1 & 0xFF);
				bytes_w[1]=(byte)((low_rank1 >> 8) & 0xFF);
				out.write(bytes_w[1]);
	            out.write(bytes_w[0]);
			  
			 	  
						  
			  Matrix U=new Matrix(new double[height_limit][height_limit]);
			  Matrix S=new Matrix(new double[height_limit][width_limit]);
			  Matrix V=new Matrix(new double[width_limit][width_limit]);
			  
			  Scanner sc1=new Scanner(new FileReader(f1));		  
			  // Set U matrix from SVD.txt
			  for(int i=0;i<height_limit;i++)
			  {
				  for(int j=0;j<height_limit;j++)
				  {
					  //String str=sc1.nextDouble();
					  //double d=Double.parseDouble(str);
					  U.set(i, j, sc1.nextDouble());		              
			      }					  
		              System.out.println();
			   }
			        System.out.println();
						        
		     // Set S matrix from SVD.txt      
			  for(int i=0;i<height_limit;i++)
			  {
				  for(int j=0;j<width_limit;j++)
				  {
				//	  String str=sc1.next();
					//   double d=Double.parseDouble(str);
					   S.set(i, j, sc1.nextDouble());							 
				  }
				  System.out.println();
			   }
			  
    		  System.out.println("S matrix");
			  printMatrix(S);  
			  
			  // Set V matrix from SVD.txt
			  for(int i=0;i<width_limit;i++)
			  {
				  for(int j=0;j<width_limit;j++)
				  {
					  //String str=sc1.next();
					  //double d=Double.parseDouble(str);
					  V.set(i, j, sc1.nextDouble());					  
				  }
				  System.out.println();
			  }	
			  System.out.println();
			  
		//	 System.out.println("V matrix");
		//	 printMatrix(V);  
		  
	  		
         // U  matrix convert each value to byte and write to byte
			  for(int i=0;i<U.getRowDimension();i++)
			  {
				  for(int j=0;j<lowrank;j++)
				  {
					  double d=U.get(i,j);
					  int decimal_b4r=(int)d;
		              int decimal_aftr=(int)((d-decimal_b4r)*100);
		              out.write(decimal_b4r);
		              out.write(decimal_aftr);
		            //System.out.println(d+" "+decimal_b4r+" "+decimal_aftr);					  
				  }					  
		              System.out.println();
			   }
			        System.out.println();
			
			    
	     // S  matrix convert each value to byte and write to byte
			   for(int i=0;i<lowrank;i++)
			  {
				  for(int j=0;j<lowrank;j++)
				  {
					  if(i==j){
					  double d=S.get(i,j);
					  int d1=(int)d;
					  byte[] bytes = new byte[2];
					  bytes[0]=(byte)d1;
					  bytes[1]=(byte)(d1>>>8);
					  int decimal_aftr=(int)((d-d1)*100);
					  out.write(bytes[1]);
		              out.write(bytes[0]);
		              out.write(decimal_aftr);
		            // System.out.println(d+"##### *********** "+decimal_aftr);
					  }
				  }
		      }	
			  
			// V  matrix 
			// V  matrix convert each value to byte and write to byte
			  for(int i=0;i<V.getRowDimension();i++)
			  {
				  for(int j=0;j<lowrank;j++)
				  {
					  double d=V.get(i,j);
					  int decimal_b4r=(int)d;
		              int decimal_aftr=(int)((d-decimal_b4r)*100);
		              out.write(decimal_b4r);
		              out.write(decimal_aftr);
		            //  System.out.println(d+"vv "+decimal_b4r+"vv "+decimal_aftr);
				  }
			  }
			  out.close();
		   } 
	   
	   

		   public static void SVD_File_to_PGM(String f) throws Exception
		   {
			  DataInputStream input = new DataInputStream(new FileInputStream(f));
					 	  
			  PrintWriter bw=null;
				 bw = new PrintWriter(f.substring(0, f.indexOf("."))+"_SVD.pgm");
				 bw.write("P2\n");
			  	 
			  byte b1=input.readByte();
			  byte b2=input.readByte();
	  		  int high = (Byte)b1 >= 0 ? (Byte)b1 : 256 + (Byte)b1;
			  int low = (Byte)b2 >= 0 ? (Byte)b2: 256 + (Byte)b2;
			  int width = low | (high << 8);			  
			  System.out.println(width+"=");
			  bw.write(width+"\t");
			  
			  byte b3=input.readByte();
			  byte b4=input.readByte();
	  		  int high1 = (Byte)b3 >= 0 ? (Byte)b3 : 256 + (Byte)b3;
			  int low1 = (Byte)b4 >= 0 ? (Byte)b4: 256 + (Byte)b4;
			  int height = low1 | (high1 << 8);			  
			  System.out.println(height+"=");
			  bw.write(height+"\n");
			  
			  byte b5=input.readByte();
			  int max_pixel=(b5& 0xff);	
			  bw.write(max_pixel+" ");
			  
			  byte b6=input.readByte();
			  byte b7=input.readByte();
	  		  int high2 = (Byte)b6 >= 0 ? (Byte)b6 : 256 + (Byte)b6;
			  int low2 = (Byte)b7 >= 0 ? (Byte)b7: 256 + (Byte)b7;
			  int lowrank = low2 | (high2 << 8);			  
			  System.out.println(lowrank+"=");
					  
			  
			  int size_svd=(2*width*lowrank)+(2*height*lowrank)+(3*lowrank);
			  byte bs[]=new byte[size_svd];
			  
//			  Byte b=bs[0];
//			  int max_pixel1=(b& 0xff);
//			  System.out.println(max_pixel1+">>>>>>>>>>>>>>>>>>>>>>>>><<<<<>>>>>>>>>>>>>>>>>>>>>>.");
//			  bw.write(max_pixel1+"\n");	 			
			  input.read(bs);
			  
			  float value=0;
			  int width_new,height_new,max_pixel_new=0;
			  Matrix U_new=new Matrix(new double[height][lowrank]);
			  Matrix S_new=new Matrix(new double[lowrank][lowrank]);
			  Matrix V_new=new Matrix(new double[width][lowrank]);
			  Matrix USV_new=new Matrix(new double[height][width]);
			  
			 		  
			// U  matrix read each byte from SVD.SVD and Set U matrix
			         int pu=0,qu=0,it=0;
		    		 for(int k=0;k<(height*lowrank*2);k=k+2)
				    {
						  int i=((Byte)bs[k]).intValue();
						  int j=((Byte)bs[k+1]).intValue();
						  value=i+((float)j/100); 
						//  System.out.println(value+"!!!!!!!!!!!UUUUU!!!!!!!!!!!!!!!");
						  U_new.set(pu, qu, value);
						  if(qu==lowrank-1)
					    	{
					    		pu++;
					    		qu=0;
					    	}
						  else{
							  qu=qu+1;
						  }						  
						//  System.out.println(pu+"^^^^^^^"+qu);
				    }  
					  				       
		    		
			//  System.out.println("U matrix low rank approximation");
		//	  printMatrix(U_new);
			  
			// S  matrix read each byte from SVD.SVD and Set S matrix
			  int ps=0,qs=0;
			   for(int k=(height*lowrank*2);k<((height*lowrank*2)+(lowrank*3));ps++,qs++,k=k+3)
			      {
						  	int i=((Byte)bs[k]).intValue();
						  	int j=((Byte)bs[k+1]).intValue();
						  	int s=((Byte)bs[k+2]).intValue();
						  	
						  	int high3 = (Byte)bs[k] >= 0 ? (Byte)bs[k] : 256 + (Byte)bs[k];
							int low3= (Byte)bs[k+1] >= 0 ? (Byte)bs[k+1]: 256 + (Byte)bs[k+1];
							int  val = low3 | (high3 << 8);							  
							float val1=val+((float)s/100);
						 //   System.out.println(val1+">>>>>>>>>>>>><<<<<<<<<<<<.");											  							  	
						  	S_new.set(ps, qs, val1);
						  		
			      }
			
			//  System.out.println("S matrix low rank approximation");
		//	 printMatrix(S_new);
			  
			// V  matrix read each byte from SVD.SVD and Set V matrix
			  int pv=0,qv=0;
			   for(int k=((height*lowrank*2)+(lowrank*3));k<(((height*lowrank*2)+(lowrank*3))+(width*lowrank*2));k=k+2)
				{
						  int i=((Byte)bs[k]).intValue();
						  int j=((Byte)bs[k+1]).intValue();
						  value=i+((float)j/100); 
	     			   //   System.out.println(value+"!!!!!!!!!!!!VvVVV!!!!!!!!!!!!!!");
						  V_new.set(pv, qv, value);		
						  if(qv==lowrank-1)
					    	{
					    		pv++;
					    		qv=0;
					    	}	
						  else{
							  qv=qv+1;
						  }
			      }
			   
			//   printMatrix(V_new);
				  
			  
			
			// Multiplying the matrix after low rank
			  USV_new=U_new.times(S_new).times(V_new.transpose());
			  System.out.println("New SVD matrix");
			  printMatrix(USV_new);
			 
			// Write each value byte to file SVD.pgm					      	
						for(int i=0;i<height;i++)
						{
							for(int j=0;j<width;j++)
							{
								double d=USV_new.get(i, j);
								if(d<0){
								  USV_new.set(i, j, 0);
								}
								bw.write((int)USV_new.get(i, j)+" ");
							}					
							bw.write("\n");
						}
						bw.flush();
						bw.close();
					  
				  } 
		   
		   
		 public static void main(String[] args) throws Exception 
		 { 
			int lowrank;
		    Scanner sc=new Scanner(new FileReader(args[1]));
		    
		    System.out.println("1.Convert Ascii to Binary");
			System.out.println("2.Viewable Binary File");
   			System.out.println("3.Convert Binary to Ascii");
   			System.out.println("4.Generating SVD for low rank approximation");
   			System.out.println("5.Read SVD and generate pgm file");
   			
   			Scanner sc_choice = new Scanner(System.in);
   	        System.out.println("Enter options Step by step:");
   	       
   	        int choice=Integer.parseInt(args[0]);
   	        
   			switch(choice)
   			{
   			case 1:AsciiToBinary(args[1]);
   					break;
	        case 2:BinaryViewable(args[1]);
					break;
   			case 3:BinarytoAscii(args[1]);
   					break;
   			case 4:SVD_File_to_Byte(args[1],args[2],args[3]);
   				   break;
   			case 5:SVD_File_to_PGM(args[1]);
   					break;
   			}   	
   	         sc.close();
   	         sc_choice.close();
		 }
}
		  
	