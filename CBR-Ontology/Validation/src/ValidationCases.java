import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;



public class ValidationCases {
	
private static String data_path = "C:\\Users\\Hugo\\Desktop\\Universidad\\SUPAERO\\S4\\CBR_Ontology\\Validation\\data\\";
private static String outputname  = "ValidationFile.csv";
private static String data_file_name  = "CleanedDATA V22-04-2021.csv";


	public static void main(String[] args) {
		
              
		List<List<String>> data_base = new ArrayList<>();
		
		
		//Creating a variable that will store the content of the .csv file containing the database in the shape of a table
		
		
		BufferedReader br;
		List<String> head_data =new ArrayList<>();
		
		try {
			br = new BufferedReader(new FileReader(data_path+data_file_name));
		   
		    String line;
		    head_data=Arrays.asList(br.readLine().split(";"));
		    //System.out.println(head_data.toString()); br.readLine().split(";");
		    //for(String s:head_data) {
		    	//System.out.println(s);	
		    	
		    //}

		    while ((line = br.readLine()) != null) {
		    	
		        String[] values = line.split(";");
		        
		        data_base.add(Arrays.asList(values));
		        

		
		        
		    }
		    
			
		    
		    
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		int numberofcases = data_base.size();
		
		int min = 0;
		int max = numberofcases-1;
	
		


            
			
			///////////////////Fill the list with the cases for the validation
		
		
		
		
		////////2 variables combination
		int random_int = (int)Math.floor(Math.random()*(max-min+1)+min);
		String[] head_out = { "Task", "w1", "Case study type", "w2", "Case study", "w3", "Online/Off-line", "w4", "Input for the model", "w5", "Input type", "w6", "Number of cases to retrieve", "Amalgamation function"};
			List<List<String>> output_list = new ArrayList <>();
			
			int[] head_index = {0,2,4,6,8, 10};
		
			for(int i=0; i<6; i++) {
				
				for(int j=i; j<6; j++) {
					if(i==j) {
						
						
					}else {
						for(int index=0; index<10;index++) {
					random_int=(int)Math.floor(Math.random()*(max-min+1)+min);
					
					//row_list.removeAll(row_list);
					
					
					String[] row_list= {"", "1", "", "1", "", "1", "", "1", "", "1", "", "1", "5", "euclidean"};
					row_list [0]="";
					row_list [1]="1";
					row_list [2]="";
					row_list [3]="1";
					row_list [4]="";
					row_list [5]="1";
					row_list [6]="";
					row_list [7]="1";
					row_list [8]="";
					row_list [9]="1";
					row_list [10]="";
					row_list [11]="1";
					row_list [12]="5";
					row_list [13]="euclidean";
					
                   
		              row_list[head_index[i]]= data_base.get(random_int).get(head_data.indexOf(head_out[head_index[i]]));
		              row_list[head_index[j]]= data_base.get(random_int).get(head_data.indexOf(head_out[head_index[j]]));
		            output_list.add(Arrays.asList(row_list));
					}
					}
		           
				}
				
				
			}
			

			
			
		///////////Three variables combination
			
			
for(int i=0; i<6; i++) {
				
				for(int j=i; j<6; j++) {
					if(i==j) {
						
						
					}else {
						
						
					for(int k=j; k<6; k++) {	
						if(k==j) {
							
							
						}else {	
							for(int index=0; index<10;index++) {
					random_int=(int)Math.floor(Math.random()*(max-min+1)+min);
					
					//row_list.removeAll(row_list);
					String[] row_list= {"", "1", "", "1", "", "1", "", "1", "", "1", "", "1", "5", "euclidean"};
					row_list [0]="";
					row_list [1]="1";
					row_list [2]="";
					row_list [3]="1";
					row_list [4]="";
					row_list [5]="1";
					row_list [6]="";
					row_list [7]="1";
					row_list [8]="";
					row_list [9]="1";
					row_list [10]="";
					row_list [11]="1";
					row_list [12]="5";
					row_list [13]="euclidean";
					
                   
		              row_list[head_index[i]]= data_base.get(random_int).get(head_data.indexOf(head_out[head_index[i]]));
		              row_list[head_index[j]]= data_base.get(random_int).get(head_data.indexOf(head_out[head_index[j]]));
		              row_list[head_index[k]]= data_base.get(random_int).get(head_data.indexOf(head_out[head_index[k]]));
		            output_list.add(Arrays.asList(row_list));
							}
						}
					}
					}
		           
				}
				
				
			}
			


///////////Four variables combination



for(int i=0; i<6; i++) {
	
	for(int j=i; j<6; j++) {
		if(i==j) {
			
			
		}else {
			
			
		for(int k=j; k<6; k++) {	
			if(k==j) {
				
				
			}else {	
				for(int l=k; l<6; l++) {		
					if(k==l) {
						
						
					}else {	
						for(int index=0; index<10;index++) {
		random_int=(int)Math.floor(Math.random()*(max-min+1)+min);
		
		//row_list.removeAll(row_list);
		String[] row_list= {"", "1", "", "1", "", "1", "", "1", "", "1", "", "1", "5", "euclidean"};
		row_list [0]="";
		row_list [1]="1";
		row_list [2]="";
		row_list [3]="1";
		row_list [4]="";
		row_list [5]="1";
		row_list [6]="";
		row_list [7]="1";
		row_list [8]="";
		row_list [9]="1";
		row_list [10]="";
		row_list [11]="1";
		row_list [12]="5";
		row_list [13]="euclidean";
		
       
          row_list[head_index[i]]= data_base.get(random_int).get(head_data.indexOf(head_out[head_index[i]]));
          row_list[head_index[j]]= data_base.get(random_int).get(head_data.indexOf(head_out[head_index[j]]));
          row_list[head_index[k]]= data_base.get(random_int).get(head_data.indexOf(head_out[head_index[k]]));
          row_list[head_index[l]]= data_base.get(random_int).get(head_data.indexOf(head_out[head_index[l]]));
        output_list.add(Arrays.asList(row_list));
						}
					}
				}
			}
		}
		}
       
	}
	
	
}
			
			



///////////Five variables combination



for(int i=0; i<6; i++) {

for(int j=i; j<6; j++) {
if(i==j) {
	
	
}else {
	
	
for(int k=j; k<6; k++) {	
	if(k==j) {
		
		
	}else {	
		for(int l=k; l<6; l++) {		
			if(k==l) {
				
				
			}else {	
				
				for(int m=l; m<6; m++) {		
					if(m==l) {
						
						
					}else {				
				
						for(int index=0; index<10;index++) {				
random_int=(int)Math.floor(Math.random()*(max-min+1)+min);

//row_list.removeAll(row_list);
String[] row_list= {"", "1", "", "1", "", "1", "", "1", "", "1", "", "1", "5", "euclidean"};
row_list [0]="";
row_list [1]="1";
row_list [2]="";
row_list [3]="1";
row_list [4]="";
row_list [5]="1";
row_list [6]="";
row_list [7]="1";
row_list [8]="";
row_list [9]="1";
row_list [10]="";
row_list [11]="1";
row_list [12]="5";
row_list [13]="euclidean";


row_list[head_index[i]]= data_base.get(random_int).get(head_data.indexOf(head_out[head_index[i]]));
row_list[head_index[j]]= data_base.get(random_int).get(head_data.indexOf(head_out[head_index[j]]));
row_list[head_index[k]]= data_base.get(random_int).get(head_data.indexOf(head_out[head_index[k]]));
row_list[head_index[l]]= data_base.get(random_int).get(head_data.indexOf(head_out[head_index[l]]));
row_list[head_index[m]]= data_base.get(random_int).get(head_data.indexOf(head_out[head_index[m]]));
output_list.add(Arrays.asList(row_list));
						}
					}
				}
			}
		}
	}
}
}

}


}
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			///////////////////////Writing the cases into the file
    

			
			try {
				PrintWriter pw = new PrintWriter(
						new File(data_path
								+ outputname));

				StringBuilder sb = new StringBuilder();

				
				
				for (int i = 0; i < head_out.length; i++) {
					if (i == head_out.length - 1) {
						sb.append(head_out[i]);
						sb.append("\r\n");
					} else {
						sb.append(head_out[i]);
						sb.append(";");
					}
				}
			
			
			for (int i = 0; i < output_list.size(); i++) {
				
				for (int j = 0; j < head_out.length; j++) {
					
					String replace = output_list.get(i).get(j).toString().replace(";",",");

					sb.append(replace);
					if (j == head_out.length - 1) {
						sb.append("\r\n");
					} else {
						sb.append(";");
					}
				}
			}
			
			pw.write(sb.toString());
			pw.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		


				 

				
			

		
		
		
	}

}
