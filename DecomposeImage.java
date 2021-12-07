import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import Jama.Matrix;
import Jama.SingularValueDecomposition;

public class DecomposeImage {

	private String header;
	private static int[][] img; // pixel values of the image
	private static char[][] binaryImg;
	private static int rowN, columnN; // the image has rowN rows and columnN
										// columns
	private int maxG;
	private int[][] OriginalImg;

	public static void main(String[] args) {
		File file = new File(args[0]);

		// read the image
		DecomposeImage decomposeImageObj = new DecomposeImage();
		decomposeImageObj.readImage(file);
		String fileName = file.getPath().substring(0,
				file.getPath().indexOf('.'));
		String fileExtension = ".txt";
		String outputPath = fileName.concat("USV").concat(fileExtension);
		String outputHeaderFile = fileName.concat("Header").concat(fileExtension);
		decomposeImageObj.matrixDecomposition(outputPath);
		decomposeImageObj.writeHeaderFile(outputHeaderFile);
		System.out.println("Done");
	}

	/**
	 * read the image file
	 * 
	 * @param file
	 *            : the image file
	 */
	public void readImage(File file) {
		header = "";

		try {
			Scanner input = new Scanner(file);

			if (input.hasNext("P2"))
				header += input.nextLine().trim();
			else
				throw new IOException();

			if (input.hasNext("#.*"))
				input.nextLine();
			columnN = input.nextInt();
			rowN = input.nextInt();
			maxG = input.nextInt();

			img = new int[rowN][columnN];
			for (int i = 0; i < rowN; i++) {
				for (int j = 0; j < columnN; j++) {
					img[i][j] = input.nextInt();
				}
			}
			input.close();
		} catch (FileNotFoundException e) {
			System.out.println("warning: file not found");
		} catch (IOException e) {
			System.out.println("warning: file format error");
		}
	}

	public void matrixDecomposition(String filePath) {

		// FileOutputStream fos;
		FileWriter fw;
		try {
			// fos = new FileOutputStream(

			fw = new FileWriter(filePath);
			
			BufferedWriter bw = new BufferedWriter(fw);

			double[][] doubleImage = convertingImageToDouble(img);
			Matrix A = Matrix.constructWithCopy(doubleImage);
			
					
			SingularValueDecomposition svd = A.transpose().svd();
			Matrix U = svd.getV();
			double[][] Uarray = U.getArray();
			for (int i = 0; i < rowN; i++) {
				for (int j = 0; j < rowN; j++) {
					if (i >= Uarray.length || j >= Uarray[0].length) {
						bw.write("0");
						bw.write(" ");
					} else {
						Double db = new Double(Uarray[i][j]);
						bw.write(db.toString());
						bw.write(" ");
					}

				}
				bw.write('\n');
			}
			Matrix S = svd.getS();
			// bw.write("S array :-" + '\n');
			int sRows = S.getRowDimension();
			int sCols = S.getColumnDimension();
			double[][] Sarray = S.transpose().getArray();
			for (int i = 0; i < rowN; i++) {
				for (int j = 0; j < columnN; j++) {

					if (i >= Sarray.length || j >= Sarray[0].length) {
						bw.write("0");
						bw.write(" ");
					} else {
						Double db = new Double(Sarray[i][j]);
						bw.write(db.toString());
						bw.write(" ");
					}

				}
				bw.write('\n');
			}

			Matrix V = svd.getU();

			double[][] Varray = V.getArray();
			for (int i = 0; i < columnN; i++) {
				for (int j = 0; j < columnN; j++) {

					if (i >= Varray.length || j >= Varray[0].length) {
						bw.write("0");
						bw.write(" ");
					} else {
						Double db = new Double(Varray[i][j]);
						bw.write(db.toString());
						bw.write(" ");
					}

				}
				bw.write('\n');
			}
			Matrix Afinal = V.times(S.transpose()).times(U.transpose());
			double[][] aArray = Afinal.getArray();

			for (int i = 0; i < aArray.length; i++) {
				for (int j = 0; j < aArray[0].length; j++) {
					if (aArray[i][j] < 0.000000000000001) {
						System.out.println("wrong");
					}
				}
			}
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void writeHeaderFile(String fileName) {
		FileWriter fw;
		try {
			fw = new FileWriter(fileName);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(String.valueOf(columnN));
			bw.write(" ");
			bw.write(String.valueOf(rowN));
			bw.write(" ");
			bw.write(String.valueOf(maxG));
			bw.close();
		} catch (IOException e) {
				e.printStackTrace();
		}

	}

	static double[][] convertingImageToDouble(int[][] image) {

		double[][] doubleImage = new double[image.length][image[0].length];

		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < image[0].length; j++) {
				doubleImage[i][j] = image[i][j];
			}
		}
		return doubleImage;

	}

	static int[][] convertImageToInt(double[][] image) {
		int[][] intImage = new int[image.length][image[0].length];

		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < image[0].length; j++) {
				intImage[i][j] = (int) image[i][j];
			}
		}
		return intImage;
	}

}