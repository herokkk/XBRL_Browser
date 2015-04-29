package ie.gmit.computing;

import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.StringTokenizer;
/**
 * This class is an util class
 * @author Siyi_Kong
 *
 */
public final class Tools {

	
	public static void addListItems(List list,String item){
		list.add(item);
	}
	
	/**
	 * It is used to get the text content of target file
	 * @param fileName
	 * @return
	 * @throws FileNotFoundException
	 */
	public static StringBuffer getSavedFileContent(String fileName) throws FileNotFoundException{
		StringBuffer buffer=new StringBuffer();
		
		FileReader reader=new FileReader(new File(fileName));
		char[] fileBuffer=new char[1024*1024];// estimated size
		
		try {
			reader.read(fileBuffer);
			buffer.append(fileBuffer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Exception occured while reading file");
		}
		return buffer;
	}
	
	/**
	 * used when users pick multiple items and write it to a file
	 * @param term
	 */
	public static StringBuilder TransferToXml(List<String> items){
		  ArrayList<String> list=new ArrayList<String>(items);// transfer the array to arrayList

		StringBuilder builder=new StringBuilder();
		
		
		for(int i=0;i<list.size();i++){
			
			
			
			String temp=list.get(i);
			
            StringTokenizer tokenizer=new StringTokenizer(temp,"|");
			
            
			String exactItem=tokenizer.nextToken();//real value
			
			String desc=ParserClass.getInstance().getDes().get(exactItem);
			builder.append("<link:definition>"+"\n"+
		                    exactItem+"\n"+
							"</link:definition>"+"\n"+
							"<link:usedOn>"+"\n"+
							desc+"\n"+
							"<link:usedOn>"+"\n");
			builder.append("\n");
		}
		return builder;
	}
	
	/**
	 * used when users pick only one item
	 * @param term
	 */
	public static StringBuilder TransferToXmlWithOneItem(String item){
	StringBuilder builder=new StringBuilder();

			if(item==null){
				return null;
			}
			String temp=item;
			
            StringTokenizer tokenizer=new StringTokenizer(temp,"|");
			
            
			String exactItem=tokenizer.nextToken();//real value
			
			String desc=ParserClass.getInstance().getDes().get(exactItem);
			builder.append("<link:definition>"+"\n"+
		                    exactItem+"\n"+
							"</link:definition>"+"\n"+
							"<link:usedOn>"+"\n"+
							desc+"\n"+
							"<link:usedOn>"+"\n");
			builder.append("\n");
		return builder;
	}
}
