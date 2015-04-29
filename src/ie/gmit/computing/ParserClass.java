package ie.gmit.computing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
/**
 * This is parser class
 * @author Siyi_Kong
 *
 */
public class ParserClass extends DefaultHandler{

 private String note=null;	
 SAXParser parser;
 DefaultHandler handler;
 StringBuffer buffer;
 static boolean flag=false;
 private Map<String, String> maps=new HashMap<String, String>();
 private String tempForParent;
 private static ParserClass parserClass = null;
 
 private List<String> collectionsOfAllinfo;
 /**
  * Constructor
  */
	public ParserClass() {
		// TODO Auto-generated constructor stub
		try {
			parser=SAXParserFactory.newInstance().newSAXParser();
			
			handler=new DefaultHandler();
			
			
			XMLReader reader=parser.getXMLReader();
			
		    buffer=new StringBuffer();
		     
		    collectionsOfAllinfo=new ArrayList<String>();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * start parsing the file choosen
	 * @param path
	 */
	public void startParsing(String path){
		 try {
			parser.parse(path, this);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		//super.startDocument();
		System.out.println("start parsing doc");
	}

	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.endDocument();
		flag=true;
		System.out.println("end parsing doc");
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		//System.out.println("this is the start of element"+localName+" : "+qName);
		if(qName.equals("link:definition")){
			//System.out.println("---<link:definition>---");
			;
		}
		note=qName;
		
		super.startElement(uri, localName, qName, attributes);
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		super.endElement(uri, localName, qName);
		note=null;
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		super.characters(ch, start, length);
		String content=new String(ch,start,length);
		
		if(note!=null){
		if(note.equals("link:definition")||note.equals("name")){
			//System.out.println("this is content "+content.toString());
			content=content.toLowerCase();// to lowercase
			tempForParent=content;
			collectionsOfAllinfo.add(content);
			buffer.append(content);
			buffer.append(",");// token
		}
		else if(note.equals("link:usedOn")||note.equals("label")){
			//if it is description
			if(!maps.containsKey(note)){
				maps.put(tempForParent,content);// put all the key-values into map, description
			}
		}
		}else {
			return;
		}
	}

	/**
	 * Get the buffer for the content of the file
	 * @return
	 */
	public StringBuffer getBuffer() {
		return buffer;
	}
	/**
	 * It is used to show description for each selected item in the list
	 * @return
	 */
	public Map<String, String> getDes(){
		return maps;
	}
	
	/**
	 * It is used by combobox , for the collection of datas
	 * @return
	 */
	public List<String> getItemsForCombo(){
		return collectionsOfAllinfo;
	}
	/**
	 * Singleton Instance
	 * @return
	 */
	public static ParserClass getInstance(){
		
		if(parserClass==null){
			parserClass=new ParserClass();
			return parserClass;
		}
		return parserClass;
	}
	/**
	 * Make the only instance of this class NULL
	 */
	public static void EmptyInstance(){
		parserClass=null;
	}
}
