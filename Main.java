/**		 --------------------------------------------------------------
 * 		|		       				HiLCoE                             |                  |
 * 		|			School of Computer Science and Technology		   |
 *		|															   |
 * 		|		Welcome to JAVA-IMAGE Encryption program               |
 * 		|				CS224 : Java Lab assignment                    |
 * 		|					August 22, 2018 					   	   |
 * 		 --------------------------------------------------------------
 * 	Student Name : Beek Kefyalew			submitted to 		                     
 * 	Student ID : NP5622 					Email  : Elimangd@yahoo.com
 * 	bekidelta@gmail.com					Mobile : +251 911 108 779
 * 	
 * This program Encrypts image files with 128 bits ADVANCED ENCRYPTION STANDARD (AES) algorithm
 * It uses Java Cryptographic Extension (JCE) javax.crypto package API.
 * */


//basic packages to build graphical user interfaces
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import javax.swing.*;
//packages to use images from files
import javax.imageio.ImageIO;
import java.io.File;
//
import java.util.Random;
//java cryptographic extenaion Engines
import javax.crypto.*;	
import javax.crypto.spec.*;

//Main class which holds the main method
//inhrits from JFrame and Implements action listner
class Main extends JFrame implements ActionListener{

    private ImageRead panel; //Variable ImageRead class
    private ImageEncrypt encrypter; //variable Image Encrypt class
    private File fileName;

    public Main(){

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("JAVA-ImageCrypto");
        setLayout(new BorderLayout());
        panel = new ImageRead(); //Image Read object
        getContentPane().add(panel); //adds ImageRead object(panel)
        pack(); //fits the size
        setJMenuBar(MainMenu()); //calls the MainMenu method

        setSize(new Dimension(550, 550));
        encrypter = new ImageEncrypt();
    }

   /**Main Menu constructs the menu and adds action listner
    * */
    private JMenuBar MainMenu(){

        JMenuBar menuBar = new JMenuBar();
	//the three main menus
        JMenu file, menu, help;
        JMenuItem open, save, saveas, close,		//menu items 
                setkey, Encrypt, Decrypt, content, about;
 
        file = new JMenu("File");
        menu = new JMenu("JCrypt");
        help = new JMenu("Help");

        open = new JMenuItem("Open  ..", new ImageIcon("./icon/folder.png"));
        save = new JMenuItem("Save", new ImageIcon("./icon/save.png"));
        saveas = new JMenuItem("Save as   ..");
        close = new JMenuItem("Close ", new ImageIcon("./icon/close.png"));

        setkey = new JMenuItem("Set Pass Key", new ImageIcon("./icon/key.png"));
        Encrypt = new JMenuItem("Encrypt Image", new ImageIcon("./icon/lock.png"));
        Decrypt = new JMenuItem("Decrypt Image", new ImageIcon("./icon/unlock.png"));

        about = new JMenuItem("About", new ImageIcon("./icon/info.png"));

        //adds the items to their related menu containers      
        file.add(open); file.addSeparator(); file.add(save); file.add(saveas); file.addSeparator();file.add(close);
        menu.add(setkey); menu.addSeparator(); menu.add(Encrypt); menu.add(Decrypt);
        help.add(about);
        
        //Adding the jmenues to jmenu bar
        menuBar.add(file);      menuBar.add(menu);      menuBar.add(help);

        //Adding action listner for the menu items
        open.addActionListener(this);       setkey.addActionListener(this);     close.addActionListener(this);
        save.addActionListener(this);       Encrypt.addActionListener(this);    about.addActionListener(this);
        saveas.addActionListener(this);     Decrypt.addActionListener(this);    
            return menuBar;
    }

    /** set File methd for implementing encapsuletion
     * accesing private variables with public methods
     */
    public void setFile(File file){
        fileName = file;
    }

    /**		 Action listener
     * overiding the actionPerformed method of the abstract class ActionListner
     *  **/
    public void actionPerformed(ActionEvent action) {

        String text = action.getActionCommand();

        try{
            
            if(text == "Open  .."){ actionLoadImage(null); } //open menu item listner
            else if(text == "Save"){ actionSaveImage(fileName); } //overites the file with the new image
            else if(text == "Save as   .."){ actionSaveImage(null);} //passes null for file name to add name by user 
            else if(text == "Close "){ System.exit(0);} //exits the program
            else if(text == "Set Pass Key"){  
              actionKeyDialog(); //calls the actionDialog method which shows joption pane to accept key
            }
            else if(text == "Encrypt Image"){
                panel.setImage(encrypter.map(panel.getImage(),true,false)); //Encrypt action listner
            }
            else if(text == "Decrypt Image"){
                panel.setImage(encrypter.map(panel.getImage(),false,false)); //Decrypt action listner
            }
            else if(text == "About"){
				DisplayContactinfo();
				}

        }catch(Exception err)
        { System.out.println("ERROR:" + err);}
    }
    /** Set the key **/
    public void actionKeyDialog(){
        String key = new String(encrypter.getKey());

        key = (String)JOptionPane.showInputDialog(this,
                "Enter a 16 byte key (current key= " +
                        key.getBytes().length + " bytes)",key);

        while(key != null && key.getBytes().length != 16){

            key = (String)JOptionPane.showInputDialog(this,
                    "Enter a 16 byte key (current key= " +
                            key.getBytes().length + " bytes)",key);
        }

        if(key != null) encrypter.setKey(key.getBytes());
    }

    /** Load an image from a file
     */
    public void actionLoadImage(File imageFile){

        if(imageFile == null){
            JFileChooser fc = new JFileChooser(fileName);
            fc.setControlButtonsAreShown(false);
            fc.showOpenDialog(this);
            imageFile = fc.getSelectedFile();
        }
		//sets the file if it is not null
        if(imageFile != null){

            panel.setImage(imageFromFile(imageFile));
            setFile(imageFile);
        }
    }

    /** Load an image from a file
     */
    private BufferedImage imageFromFile(File file){

        BufferedImage img = null;
        try{
            img = ImageIO.read(file);
        }catch(Exception e){
            System.out.println("Error:" + e);
        }
        return img;
    }


    /** Save an image from a file
     * @param file the name of the file to save, use "null" to access a dialog
     */
    public void actionSaveImage(File imageFile){

        if(imageFile == null){
            JFileChooser filechooser = new JFileChooser(fileName);
            filechooser.showSaveDialog(this);
            imageFile = filechooser.getSelectedFile();
        }

        if(imageFile != null){
            try{
                ImageIO.write(panel.getImage(), "png", imageFile);
            }catch(Exception e){
                System.out.println("Error:" + e);
            }
            setFile(imageFile);
        }
    }





    public void DisplayContactinfo(){

        JFrame contact = new JFrame("Contact info");
        contact.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        contact.setSize(new Dimension(500, 430));
        contact.setLayout(new BorderLayout());
        contact.setDefaultCloseOperation(3);
        contact.setResizable(false);
      
        String About = 
  	"	\tHiLCoE\n"+
  	"\tSchool of Computer Science and Technology\n\n\n"+
  	"Welcome to JAVA-IMAGE Encryption program \n\n"+
	"CS224 : Java Lab assignment \n"+
	"August 22, 2018\n\n"+
  	"\tStudent Name : Beek Kefyalew \n" +            
  	"\tStudent ID : NP5622 			\n"+
  	"\tEmail : bekidelta@gmail.com\n"+
  	"\tphone : +251 961 007 216\n\n"+
  	"Submitted to : \n"+"\tEmail : Elimangd@yahoo.com\n\tMobile : +251 911 108 779\n"+
	"\n\nThis program Encrypts image files with 128 bits \nADVANCED ENCRYPTION STANDARD (AES) algorithm\n"+
	"and it can be easily convertable to AES 196 or AES 256 bit \n encryption as well as to DES 56 bits encryption \n"+
  "It uses Java Cryptographic Extension (JCE) \njavax.crypto package API.\n And soon will be added drag and drop action listner";
JTextArea cont1 = new JTextArea(About);
contact.add(new JScrollPane(cont1), BorderLayout.CENTER);
cont1.setEditable(false);

contact.setVisible(true);

    }
    /** Main function **/
    public static void main(String args[])
    {
        Main win = new Main();
        win.setVisible(true);
	//cli for setting image file with argument parameter
        if(args.length > 0){
            win.actionLoadImage(new File(args[0]));
        }
    }
}

//	ImageRead class draws image panel
// as well as override the paintcomponent which is called automatically in background
class ImageRead extends JPanel{

    private BufferedImage image;

    public ImageRead()
    {
        this.image = null;

        setFocusable(true);

        setLayout(null);
        setOpaque(true);

    }

    /** Sets the main Image **/
    public void setImage(BufferedImage image){

        this.image = image;
        repaint();
    }

    /** returns the image
     * image geter methd
     */
    public BufferedImage getImage(){
        return image;
    }

    /** Overwriding paint event for drawing 
     * 
     * the paintComponent is called internally when images change positions by 
     * resizing of jframe or changing focus. No method call for PaintComponent.
     * **/
    public void paintComponent(Graphics g) {
        g.setColor(new Color(34, 33, 33));
        g.fillRect(0,0,getSize().width,getSize().height);

        if(image != null){

            int center_x = getSize().width/2 - image.getWidth() /2;
            int center_y = getSize().height/2 - image.getHeight() /2;

            if(center_x < 10){ center_x = 10;}
            if(center_y < 10){ center_y = 10;}

            g.drawImage(image,center_x,center_y,null);
        }
    }
}


//The image encrypt class 
//this class holds methods for enrypting and decrypting images
class ImageEncrypt{

    private boolean verbose=false;
    private Random generator;

    private Cipher cipher;
    private SecretKeySpec skeySpec;

    /** Constructor */
    ImageEncrypt() {

        try{
            // Used for noise
            generator = new Random();

            KeyGenerator kgen = KeyGenerator.getInstance("AES"); //initiate with AES algorithm, can be changed to DES but the key size should be 56 bits
            kgen.init(128);
            /**initialize the encryption key with 128, 
            key could be 128, 196 or 256 using AES encryption algorithm
			*/
            SecretKey skey = kgen.generateKey();
            byte[] raw = skey.getEncoded();
            skeySpec = new SecretKeySpec(raw, "AES");

            cipher = Cipher.getInstance("AES/ECB/NoPadding");

        }catch(Exception e){ System.out.println("ERROR: " + e);}

    }

    /** Set the key **/
    public void setKey(byte [] key){

        skeySpec = new SecretKeySpec(key,"AES");
    }

    byte [] getKey(){ return skeySpec.getEncoded();}

    /** Encrypta and decrypt Image **/
    public BufferedImage map(BufferedImage image,boolean encrypt,boolean trick) throws Exception{


        // Test if the image is devisible by 2
        if(image.getWidth() % 2 != 0 || image.getHeight() % 2 != 0){
            throw(new Exception("Image size not multiple of 2 :("));
        }

        BufferedImage encImage = new BufferedImage(image.getWidth(),image.getHeight(),
                BufferedImage.TYPE_4BYTE_ABGR);

        if(encrypt){
            System.out.println("Encrypting Image ... trick=" + trick);//systemoutprintln was meant to be for a comand line users
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        }
        else{
            System.out.println("Decrypting Image ... trick=" + trick);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        }

        for(int x=0;x<image.getWidth(); x+=2){
            for(int y=0;y<image.getHeight(); y+=2){
                if(verbose) System.out.println("Block: (" + x+","+y+") -----");

                int counter =0;
                byte [] pixelBytes = new byte[16];
                // Loop through internal block
                for (int i=0;i<2;i++){
                    for (int j=0;j<2;j++){
                        int val = image.getRGB(x+i,y+j);
                       if(trick && encrypt) val +=x*y;
                        byte [] sub  = intToByteArray(val);

                        if(verbose){
                            System.out.println("Val: " + val + " Bytes: ");
                            printByteArray(sub);
                        }
                        for(int k=0;k<4;k++) pixelBytes[(counter)*4+k] = sub[k];
                        counter++;
                   }
                }

                // Cipher
                byte [] enc = cipher.doFinal(pixelBytes);
                if(verbose){
				    printByteArray(pixelBytes);
					printByteArray(enc);
                }
                counter =0;
         // Re-encode the new image
                for (int i=0;i<2;i++){
                  for (int j=0;j<2;j++){
                     byte [] sub = new byte[4];
					for(int k=0;k<4;k++) 
					sub[k] = enc[(counter)*4+k];

                int val = byteArrayToInt(sub);
                if(trick && !encrypt) val -=x*y;

                encImage.setRGB(x+i,y+j,val);

                counter++;
                    }
                }
            }
        }
        return encImage;
    }

    public static final byte[] intToByteArray(int value)
    {
        return new byte[] {
                (byte)(value >>> 24),
                (byte)(value >>> 16),
                (byte)(value >>> 8),
                (byte)value};
    }

    public static final int byteArrayToInt(byte [] b)
    {
        return (b[0] << 24)
                + ((b[1] & 0xFF) << 16)
                + ((b[2] & 0xFF) << 8)
                + (b[3] & 0xFF);
    }

    public static void printByteArray(byte [] array)
    {
        System.out.print("{");
        for(int i=0;i<array.length;i++)
            System.out.print(" " + array[i]);
        System.out.println(" }");
    }
}
