package ie.gmit.computing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Scrollbar;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import javax.swing.text.Highlighter.Highlight;
import javax.swing.text.Highlighter.HighlightPainter;


/* * This class contains all the relevant components  src,video,disser, readME
 * @author Siyi Kong
 *@author Xuezhen zeng
 */
public class Mainpage extends JFrame{
   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
   private JMenuBar bar;// menu bar
   private JMenuItem jnewItem;//new
   private JMenuItem jSaveItem;//save
   private JMenuItem jPreviewItem;//preview
   
   private JMenu menu;
   private JLabel keyword;
   private JTextArea xbrlArea;// XBRL taxonomies will be shown up here
   
   private JTextField userInput;// searching bar
   private JButton search;//button
   private JButton update;
   private JList<String> resultArea;
   //private JTextArea resultArea;// showing result
   private JTextArea des;//showing description
  
   /*********We probably need two panels as there are more than one components************/
   private JPanel left;
   private JPanel mid;
   private JPanel right;
   private JPanel right_up;
   private JPanel right_middle;
   private JPanel right_bot;
   
   private JRadioButton canEdit;
   private JRadioButton cannotEdit;
   
   private JScrollPane scrollPane;
   private JScrollPane scrollPaneforList;
   private JScrollBar scrollBar;
   private Cursor cursor;
   private DefaultListModel<String> model;
   private Dimension dimension;
   private Toolkit toolkit;
   
   private  int WIDTH;
   private  int HEIGHT;
   
   private String tempPath=null;
 
private JComboBox<String> comboBox;
   
  private DefaultComboBoxModel<String> comboxModel=new DefaultComboBoxModel<String>();
  
  List<String> collections=ParserClass.getInstance().getItemsForCombo();
  
/**
 * Constructor
 */
   @SuppressWarnings("unchecked")
public Mainpage(){
		setTitle("XBRL browser");
		
		
		cursor=Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR);
		toolkit=Toolkit.getDefaultToolkit();
		dimension=toolkit.getScreenSize();
		model=new DefaultListModel<String>();
		WIDTH=(int)dimension.getWidth();
		HEIGHT=(int)dimension.getHeight();
		
		setSize(WIDTH, HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout(0,0));
		
		
		init();
		addListener();// set actionListener to all menuitems 
		this.setJMenuBar(bar);
		setResizable(false);
		setVisible(true);
	}
	
   /**
    * Initialisation of all UI components
    */
   public void init(){
	    menu=new JMenu("Menu");
	    bar=new JMenuBar();
	    
		jnewItem=new JMenuItem("New");
		jSaveItem=new JMenuItem("Save");
		jPreviewItem=new JMenuItem("Preview");
		
		canEdit=new JRadioButton("Dynamic");
		cannotEdit=new JRadioButton("Static",true);
		
		menu.add(jnewItem);
		menu.add(jSaveItem);
		menu.add(jPreviewItem);
		
		bar.add(menu);
		left=new JPanel();
		
		xbrlArea=new JTextArea(470,61);//set the num of row and column
		
		xbrlArea.setEditable(true);
		this.scrollPane=new JScrollPane(xbrlArea);
		this.scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		left.setBackground(Color.yellow);
		xbrlArea.setBackground(Color.white);
		xbrlArea.setLineWrap(true);
		xbrlArea.setCaretPosition(0);
		xbrlArea.setCursor(cursor);
		
		left.add(scrollPane);// used left.add(xbrlArea) before
		
		right=new JPanel(new BorderLayout());// up, middle, down
		right_up=new JPanel();
		right_middle=new JPanel();
		
		right_middle.setMinimumSize(new Dimension(300, 300));
		right_bot=new JPanel();
		right.setBounds(40,10, 40,460);
		
		comboBox=new JComboBox<String>();
		 comboBox.setBounds(0,20, 333,0);
        comboBox.setModel(comboxModel);
		userInput=new JTextField(20);
		userInput.add(comboBox, BorderLayout.SOUTH);//add the comboBbox to its input
		userInput.setColumns(30);
		//userInput.requestFocus();
		
		
		search=new JButton("search");
		update=new JButton("Update");
		update.setSize(10, 12);
        mid=new JPanel(new BorderLayout());
       mid.add(update,BorderLayout.NORTH);
       mid.add(canEdit,BorderLayout.CENTER);
       mid.add(cannotEdit,BorderLayout.BEFORE_LINE_BEGINS);
       
		resultArea=new JList<String>(model);
		resultArea.setBackground(Color.gray);
		resultArea.setVisibleRowCount(20);
		resultArea.setBackground(Color.WHITE);
		//resultArea.setFixedCellWidth(95);
		scrollPaneforList=new JScrollPane();
		scrollPaneforList.setPreferredSize(new Dimension(100, 100));
		scrollPaneforList.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPaneforList.getViewport().add(resultArea,null);
		
		des=new JTextArea(8, 40);
		des.setBackground(Color.gray);
		des.setEditable(false);
		des.setLineWrap(true);
		
		keyword=new JLabel("Keywords: ");
		right_up.add(keyword);
		right_up.add(userInput);
		
		right_up.add(search);
		
		right_middle.add(scrollPaneforList);
		
		right_bot.add(des);
		
		// use the right main panel to add all
		right.add(right_up,BorderLayout.NORTH);
		right.add(scrollPaneforList,BorderLayout.CENTER);
		right.add(right_bot,BorderLayout.SOUTH);
		
		add(left,BorderLayout.WEST);//add leftpanel
		add(mid, BorderLayout.CENTER);
		add(right,BorderLayout.EAST);//add rightpanel
		
		
   }
	/**
	 * delegate all components to add ActionListener function 
	 */
	public void addListener(){
		ActionNew();
		ActionPreview();
		ActionSave();
		ActionSearch();
		ActionUpdate();
		ActionList();
		ActionKeyBoard();
		ActionRadiobtns();
		ActionCombom();
		
	}
	/**
	 * Add listener to combobox
	 */
	public void ActionCombom(){
      setAdjusting(comboBox, false);
		
		for(String str:collections){
			comboxModel.addElement(str);
		}
		comboBox.setSelectedItem(comboxModel);
		
		
		comboBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.out.println("it is change");
				if(isAdjusting(comboBox)==false){
					if(comboBox.getSelectedItem()!=null){
						System.out.println("yes, it is not empty and it is inside");
						userInput.setText(comboBox.getSelectedItem().toString());
					}
				}
			}
		});
		userInput.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				updateList();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				updateList();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				updateList();
			}
			
			public void updateList(){
		        setAdjusting(comboBox, true);
		        
		        comboxModel.removeAllElements();
		        
		        String content=userInput.getText();
		        
		        if(!content.isEmpty()){
		        	for(String str:collections){
		        		if(str.toLowerCase().startsWith(content.toLowerCase())){
		        			comboxModel.addElement(str);
		        		}
		        	}
		        }
		        System.out.println(comboxModel.getSize());
		        comboBox.setPopupVisible(comboxModel.getSize()>0);
		        setAdjusting(comboBox, false);

			}
		});
	}
	/**
	 * Check  the combobox's status 
	 * @param box
	 * @return
	 */
	public boolean isAdjusting(JComboBox box){
		if(box.getClientProperty("isAdjusted") instanceof Boolean){
			return (boolean) box.getClientProperty("isAdjusted");
		}
		
		return false;
	}
	/**
	 * Set the combobox's status
	 * @param box
	 * @param isAdjusting
	 */
	public void setAdjusting(JComboBox box, boolean isAdjusting){
		box.putClientProperty("isAdjusted", isAdjusting);
	}
	/**
	 * Add listener to radio buttons
	 */
	public void ActionRadiobtns(){
		if(cannotEdit.isSelected()){
			xbrlArea.setEditable(false);
		}
		canEdit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub				
				cannotEdit.setSelected(false);
				xbrlArea.setEditable(true);
			}
		});
		
		cannotEdit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub				
				canEdit.setSelected(false);
				xbrlArea.setEditable(false);
			}
		});
	}
	
	/**
	 * Add keyboard listner. In this one, when users press enter key, it will start searching work
	 */
	public void ActionKeyBoard(){
		userInput.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
			if(e.getKeyCode()==KeyEvent.VK_ENTER){
					startSearch();
				}
			}
		});
	}
	
	/**
	 * This method is used when you want to edit and update the original text shown on the left side
	 */
	public void ActionUpdate(){
      update.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(tempPath!=null){
					File file=new File(tempPath);// path for the file edited
                    //FileOutputStream fileOutputStream=new FileOutputStream(file);
                    try {
						FileWriter writer=new FileWriter(file);
						char[] buffer=xbrlArea.getText().toCharArray();
						
						writer.write(buffer, 0, buffer.length);
                        
						ParserClass.EmptyInstance();
						ParserClass.getInstance().startParsing(tempPath);
						
						JOptionPane.showMessageDialog(null, "File edited!!", "Notification", JOptionPane.INFORMATION_MESSAGE); 
						return;
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						System.out.println("Writing failed!");
					}
				}
			}
		});
	}
	
	/**
	 * The method is used to show searching result in the list
	 */
	public void ActionList(){
		
		resultArea.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				 
					//show the des
					String itemKey=(String) resultArea.getSelectedValue();
					if(itemKey!=null&&itemKey!=""){
						StringTokenizer tokenizer=new StringTokenizer(itemKey,"|");
						
						String exactItem=tokenizer.nextToken();//real value
						System.out.println(itemKey+":    "+exactItem);
						String itemValue=ParserClass.getInstance().getDes().get(exactItem);
						
						System.out.println("descripe: "+ParserClass.getInstance().getDes().size());
						System.out.println("value: "+itemValue);

						if(itemValue!=null){
							des.setText("");
							des.setText(itemValue);
							
							exactItem="";
						}
					}
					
			}
		});
		

	}
	
	/**
	 * This method is used to trigger the event of opening a filechooser
	 */
	public void ActionNew(){
   jnewItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JFileChooser fileChooser=new JFileChooser();
				FileNameExtensionFilter filter=new FileNameExtensionFilter(null, "txt","docx","xsd","xml");
				fileChooser.setFileFilter(filter);
				
				int reValue=fileChooser.showOpenDialog(null);
				if(reValue==JFileChooser.APPROVE_OPTION){
					// should write out all the texts in left panel
					String path=fileChooser.getSelectedFile().getAbsolutePath();
					tempPath=path;
					
					ParserClass.getInstance().startParsing(path);
					
					 try {
						showXbrl(path);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}// pass the file addr
					System.out.println("you choose "+fileChooser.getSelectedFile().getName());
			
			}
			}});
	}
	
	/**
	 * if it is xbrl taxonomy, we need to read it in buffer and show it
	 * @param path
	 * @throws IOException 
	 */
	public void showXbrl(String path) throws IOException{
		//File file=new File(path);
		 FileInputStream in=new FileInputStream(path);
			FileReader reader=new FileReader(path);
		System.out.println("file address is:"+path);
		StringBuffer contents=new StringBuffer();
		
		try {
			
			char[] buffer=new char[in.available()];
			while(reader.read(buffer)!=-1){
			  contents.append(buffer);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(IOException exception){
            exception.printStackTrace();
		}finally{
		  reader.close();
		  in.close();
		}
		
		String result=new String(contents);
	    StringTokenizer tokenizer=new StringTokenizer(result);
	    
	    	xbrlArea.setText(result);// the result for xbrl
	    	xbrlArea.setCaretPosition(xbrlArea.getText().length());
	    	scrollBar=scrollPane.getVerticalScrollBar();
	    	
	    	scrollBar.setValue(1);
		
	}
	/**
	 * This method is used to do save working
	 */
    public void ActionSave(){
    	
    	jSaveItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				JFileChooser chooser=new JFileChooser();
				FileNameExtensionFilter filter=new FileNameExtensionFilter("txt","txt");
				FileNameExtensionFilter filter2=new FileNameExtensionFilter("xsd","xsd");
				FileNameExtensionFilter filter3=new FileNameExtensionFilter("xml","xml");
				
				chooser.setFileFilter(filter);
				chooser.setFileFilter(filter2);
				chooser.setFileFilter(filter3);
				
				if(resultArea.getSelectedValuesList().size()==0){
					
					JOptionPane.showMessageDialog(null, "Please select at least one item you want to save", "Message", JOptionPane.INFORMATION_MESSAGE); 
					return;
				}
				
				int state=chooser.showSaveDialog(null);
				
				if(state==JFileChooser.APPROVE_OPTION){
					File file=chooser.getSelectedFile();
					String fileSuffix=chooser.getFileFilter().getDescription();
					try {
						if(!file.exists()){
							//file.renameTo("");
							file.createNewFile();
						}
							String filaName=file.getAbsolutePath();
							
							System.out.println("path:"+filaName);
							BufferedWriter writer=new BufferedWriter(new FileWriter(file,true));
						
							if(resultArea.getSelectedValuesList().size()==1){
								System.out.println("yes, the size is 1");
								StringBuilder builder=Tools.TransferToXmlWithOneItem((String)resultArea.getSelectedValue());//这里注意选中，然后存resultArea.getItems()
								String str=new String(builder);
								writer.write(str);
							}else if(resultArea.getSelectedValuesList().size()>1){
								System.out.println("yes, the size is greater than 1");
								StringBuilder builder=Tools.TransferToXml(resultArea.getSelectedValuesList());//这里注意选中，然后存resultArea.getItems()
								String str=new String(builder);
								writer.write(str);
							}
								
							writer.close();
					
					} catch (IOException e2) {
						// TODO: handle exception
						System.out.println(e2.getMessage());
					}
				}
			}
		});
		
	}

    /**
     * This method is used to review the text you have saved before, as well we the original one
     */
   public void ActionPreview(){
	  jPreviewItem.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			xbrlArea.setText("");// clean all the text
			
			JFileChooser fileChooser=new JFileChooser();
			FileNameExtensionFilter filter=new FileNameExtensionFilter(null, "txt","docx","xsd","xml");
			fileChooser.setFileFilter(filter);
			
			int reValue=fileChooser.showOpenDialog(null);
			if(reValue==JFileChooser.APPROVE_OPTION){
				String path=fileChooser.getSelectedFile().getAbsolutePath();
				
				tempPath=path;
				ParserClass.EmptyInstance();
				ParserClass.getInstance().startParsing(path);
				

				 try {
					showXbrl(path);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}// pass the file addr		
		}
		}
	});
    }

/**
 * This method is used to do search work
 */
   public void ActionSearch(){
	   search.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			startSearch();
		}
	});
   }
   
   /**
    * This method is used to compare the text you entered and the text from original file
    */
   public void startSearch(){
	   resultArea.removeAll();// remove all items in the list
		
		if(model!=null)
		model.removeAllElements();
		
		String content=userInput.getText().toLowerCase();//get users' input
		String xbrl=xbrlArea.getText();
		xbrl=xbrl.toLowerCase();
		if(xbrl.contains(content)||(!content.isEmpty())){
			System.out.println("yes it is contained");
			StringBuffer buffer=ParserClass.getInstance().getBuffer();
			String buf=new String(buffer);
			StringTokenizer tokenizer=new StringTokenizer(buf, ",");
			
			while(tokenizer.hasMoreTokens()){
				String temp=tokenizer.nextToken();
				temp=temp.toLowerCase();
				if(temp.startsWith(content)||temp.contains(content)){
					String simiString=hamming(content, temp);
					System.out.println(temp+"start with "+content);
					
					model.addElement(temp+"|"+simiString);			
				
				}
			}
			resultArea.setModel(model);
		}else {
			System.out.println("oop, it doesn't");
		}
   }
   /**
    * A customed algorithm for heuristic evaluation
    * @param userInput
    * @param Xbrltext
    * @return
    */
   public String similarity(String userInput,String Xbrltext){
	   float similarity=0.0f;
	   String simi;
	   float count=0.0f;
	   char[] user=userInput.toCharArray();
	   char[] xbrl=Xbrltext.toCharArray();

	   int length=xbrl.length;
	   int pos;// mark the first occurance
	   
	   pos=Xbrltext.indexOf(userInput);
	   
	   if(pos!=-1){
		   String subStr=Xbrltext.substring(pos, Xbrltext.length());
		   xbrl=subStr.toCharArray();
		   for(int i=0;i<user.length;i++){
				 
			   char c=user[i];
			   if(c==xbrl[i]){
				   count+=1.0;
			   }else {
				break;
			}
		   
	   }
	   }
	   
	   if(count==xbrl.length){
		   return "100%";
	   }
	   similarity=count/length;
	   NumberFormat format=NumberFormat.getInstance();
	   format.setMaximumIntegerDigits(2);
	   format.setMaximumFractionDigits(2);
	   
	   simi=format.format(similarity*100)+"%";
	   System.out.println(simi+"%");
	   return simi;
   }
   
   /**
    * Hamming distance algorithm
    * @param userInput
    * @param Xbrltext
    * @return
    */
   public String hamming(String userInput,String Xbrltext){
	   float similarity=0.0f;
	   float count=0.0f;
	   String simi;
	   char[] user=userInput.toCharArray();
	   char[] xbrl=Xbrltext.toCharArray();
	   
	   int shortest=Math.min(user.length,xbrl.length);
	   int longest=Math.max(user.length,xbrl.length);
	   
	   for(int i=0;i<shortest;i++){
		   if(user[i]!=xbrl[i])
			   count+=1;
	   }
	   
	   count+=longest-shortest;
	   
	   if(count==0.0f){
		   return "100%";
	   }
	   
	   similarity=(Xbrltext.length()-count)/Xbrltext.length();
	   NumberFormat format=NumberFormat.getInstance();
	   format.setMaximumIntegerDigits(2);
	   format.setMaximumFractionDigits(2);
	   
	   simi=format.format(similarity*100)+"%";
	   
	   return simi;
   }
  
   /**
    * Main class for execution
    * @param args
    */
	public static void main(String[] args) {
		new Mainpage();
	}

	

}
