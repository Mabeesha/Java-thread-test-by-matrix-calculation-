
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class MatrixMultiplication
{

	public static int[][]    A , B , C ;
	public static int        m , n , p ;
	public static int        numberOfThreads;


	public static void main(String[] args) throws InterruptedException
	{
		numberOfThreads = 1;
		if(args.length > 0){numberOfThreads = Integer.parseInt(args[0]);}//getting the no of threads
		
		//reading the file and get A
        try (BufferedReader br = new BufferedReader(new FileReader("Test1_A.txt"))) {
        	//getting the size
        	String line;
        	line = br.readLine();
        	String[] m_and_p = line.split(" ");
        	m = Integer.parseInt(m_and_p[0]); 
        	p = Integer.parseInt(m_and_p[1]);
        	A = new int[m][p];

        	//getting A
        	int row_No = 0;
            while ((line = br.readLine()) != null) {

                String[] row = line.split(" ");
                for(int column = 0;column< p; column++)
                {
                	A[row_No][column] = (int)Double.parseDouble(row[column]);
                }
                row_No++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

		//reading the file and get B
        try (BufferedReader br = new BufferedReader(new FileReader("Test1_B.txt"))) {
        	//getting the size
        	String line;
        	line = br.readLine();
        	String[] p_and_n = line.split(" ");
        	p = Integer.parseInt(p_and_n[0]); 
        	n = Integer.parseInt(p_and_n[1]);
        	B = new int[p][n];

        	//getting B
        	int row_No = 0;
            while ((line = br.readLine()) != null) {

                String[] row = line.split(" ");
                for(int column = 0;column< n; column++)
                {
                	B[row_No][column] = (int)Double.parseDouble(row[column]);
                }
                row_No++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
		C = new int[m][n];
		
		//calculating the the time taken with main thread
		long Starting_time = System.currentTimeMillis();
		CalculateC();
		long Calculation_time = System.currentTimeMillis() - Starting_time;
		System.out.println("Calculation time when "+1+" Thread(s) = "+Calculation_time+"mS");


		Thread[] T = new Thread[numberOfThreads];//creating tread array

		for(int i=0;i<numberOfThreads;i++)
		{
			final int startIndex_i = i*(m/numberOfThreads);
			final int endIndex_i;
			if(i == numberOfThreads-1)endIndex_i   = m-1;
			else{ endIndex_i   = i*(m/numberOfThreads) + ((m/numberOfThreads)-1);}


			T[i] = new Thread
			(
				new Runnable()
				{
                    @Override
					public void run()
					{
						for(int i=startIndex_i;i<=endIndex_i;i++)
						{
							for(int j=0; j<n ; j++)
							{	
								C[i][j]=0;
								for(int k =0;k<p;k++)
								{
									C[i][j] = C[i][j] + (A[i][k] * B[k][j]);
								}
							}
						}
					}
				}
			);
		}

		//calculating the time taken when using threads		
		Starting_time = System.currentTimeMillis();
		for(int i=0;i<numberOfThreads;i++)
		{
			T[i].start();
		}
		for(int i=0;i<numberOfThreads;i++)
		{
			T[i].join();
		}
		Calculation_time = System.currentTimeMillis() - Starting_time;
		System.out.println("Calculation time when "+numberOfThreads+" Thread(s) = "+Calculation_time+"mS");
	}

	public static void CalculateC()
	{
		for(int i=0;i<=m-1;i++)
		{
			for(int j=0; j<n ; j++)
			{
				C[i][j] = 0;
				for(int k =0;k<p;k++)
				{
					C[i][j] = C[i][j] + (A[i][k] * B[k][j]);
				}
			}
		}
	}
}