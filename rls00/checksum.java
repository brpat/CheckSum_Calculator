//Brijesh Patel
//CIS 3360

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class checksum 
{	
	public static PrintWriter pw;
	static StringBuilder sb;
	
	public static void main(String[] args) throws IOException 
	{
		int i, charCount = 0, checkSum = 0;
		// was an int but some in some test cases there was overflow
		long checkSumSize = Integer.parseInt(args[1]);
		String fileName = args[0];
		char [] message = new char[512];
		
		// check for invalid checkSum size 
		if(!(checkSumSize == 8 || checkSumSize == 16 || checkSumSize == 32))
		{
			System.err.printf("Valid checksum sizes are 8, 16, or 32\n");
			System.exit(1);
		}
	
		pw = new PrintWriter(System.out);
		readFromFile(fileName);
		message = sb.toString().toCharArray();
		System.out.println();
		// print out original input text and if checkSum Size is 8
		// go ahead an calculate checkSum in same loop
		for(i = 0; i < message.length;)
		{	
			if(message[i] == '\0')
				break;
			
			for(int j = 0; j < 80 && i < message.length; j++)
			{
				pw.printf("%c", message[i]);
				// adds single characters together
				if(checkSumSize == 8)
					checkSum += message[i];
						
				// just grabs the count of the characters in message not pad	
				charCount++;
				i++;
				pw.flush();
			}
			System.out.println();
			
		}
		// take two characters at a time
		if(checkSumSize == 16)
		{
			for(i = 0; i < message.length;)
			{	
				if(i >= message.length)
					break;
				if(message.length % 2 != 0 && i == message.length - 1)
					break;
				int num = message[i];
				num = num << 8;
				num += message[i + 1];
				checkSum += num;
				i += 2 ;						
			}
			// pad with X
			if(message.length % 2 == 0)
			{	
				// 2648 is new line character plus X
				checkSum += 2648;
				pw.printf("%c\n", 'X');
				pw.flush();
				charCount += 2 ;
				checkSum %= (int)Math.pow(2, 16);	
			}
			else
			{
				System.out.println();
				long num = message[message.length - 1];
				num = num << 8;
				num += '\n';
				charCount++;
				checkSum += num; 
				checkSum &= 65535;
			}
	   }
		
		if(checkSumSize == 8)
		{
			System.out.printf("\n");
			//Newline Character
			checkSum += 10;
			charCount++;
			checkSum %= 256;
		}

		if(checkSumSize == 32)
		{	
			long num = 0; 
			int count = 0, needToPad = 0;
			int []ar = new int [512];
			int pad = 4 - (message.length % 4), ct = 0;

			for(i = 0;;)
			{	
				if(i + 3 >= message.length)
				{	
					needToPad = 1;
					int j = 0;
					while(i < message.length)
						{
							ar[j++] = message[i++]; count++;
						}
					break;
				}

				num = message[i];
				num = num << 8;

				num += message[i + 1];
				num = num << 8;

				num += message[i + 2];
				num = num << 8;

				num += message[i + 3];
				checkSum += num;
				num = 0;
				i += 4;
			}
			// if groups of four was not possible
			if(needToPad == 1)
			{	
				int padCount = 4 - count - 1; i = 0;
				ar[count++] = '\n';
				charCount++;

				while(count != 4)
					ar[count++] = (int)'X';

				num = ar[0];
				num = num << 8;
				num += ar[1];

				num = num << 8;
				num += ar[2];

				num = num << 8;
				num += ar[3];

				checkSum += num;
				long n = 4294967295L;
				checkSum &= n;
				charCount += padCount;

				while(i++ < padCount)
					System.out.print("X");

			}
			// checkSum plus /nXXX
			else
				{
					checkSum += 4448344152L;
					long n = 4294967295L;
					checkSum &= n;
					charCount += 4;
					System.out.print("XXX");
				}
			System.out.println();
		}	
		System.out.printf("%2d bit checksum is %8x for all %4d chars\n",
			checkSumSize, checkSum, charCount);	
		
		pw.println();		
			
	}
	// read Strings from a file and store them in a String Builder to later turn into an array
	public static void readFromFile(String fileName) throws IOException
	{
	    File file = new File(fileName);
	    FileReader fr = new FileReader(file);
	    sb = new StringBuilder();
 		BufferedReader br = new BufferedReader(fr);
 		String str = null;
 		
 		while((str = br.readLine()) != null)
 		     sb.append(str);
 		br.close();		
	}
}

