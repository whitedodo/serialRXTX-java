package Libraries;

import javax.swing.JComboBox;

public class CommBaudRate{
	
	public JComboBox initComponent( JComboBox cmbBaudRate ) {
		
		int i = 0;
		int n = 14;
		int rate = 0;
		
		while ( i <= n )
		{
			switch(i)
			{
				case 0:
					rate = 300;
					break;
					
				case 1:
					rate = 1200;
					break;
					
				case 2:
					rate = 2400;
					break;
					
				case 3:
					rate = 4800;
					break;
				
				case 4:
					rate = 9600;
					break;
					
				case 5:
					rate = 19200;
					break;
					
				case 6:
					rate = 38400;
					break;
					
				case 7:
					rate = 57600;
					break;
					
				case 8:
					rate = 74880;
					break;
				
				case 9:
					rate = 115200;
					break;
					
				case 10:
					rate = 230400;
					break;
					
				case 11:
					rate = 250000;
					break;
					
				case 12:
					rate = 500000;
					break;
					
				case 13:
					rate = 1000000;
					break;
				
				case 14:
					rate = 2000000;
					break;
				
				default:
					rate = 0;
					break;
			}
			
			cmbBaudRate.addItem(rate);
			
			i++;
		}
		
		return cmbBaudRate;
	}
	
	
}