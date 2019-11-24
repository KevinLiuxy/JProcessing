package JProcessing;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;
import java.util.Timer;
import java.util.concurrent.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/**
 * <code>Processing</code> is a Application Programming Interface for Processing Coding Language to function in Java IDE,
 *     It is Designed to implement Processing Running Environment into Java.<br>
 *     (Programmed Referring to Processing version: 3+)
 *<p>
 * Class <code>Processing</code> enables most Processing Codes to Run in Java Environment. It is an SubClass of
 *     <code>JFrame</code>. All Methods and Functions within <code>JProcessing</code> Are Programmed According to
 *     the Processing IDE and Offer Identical Functionality. The full Documentation on all Processing Methods are on
 *     the Processing IDE Official Website (links down below).
 *<p>
 * Note: This Class runs as A Processing Code Library that Allows Processing Codes to Run directly in Java Environment with
 *           minimal Modifications. Please Refer to the Included Template to see how you can Implement it into your Java Codes.
 * Note: The Default Encoded Character Set used in JProcessing is "GB18030", not "UTF-8". You can use
 *            <code>Processing.charset = "Name_of_the_Charset"</code> To change the Global Default Charset for JProcessing
 *<p>
 * Known Issue: Always call {@code loadPixels()} before Accessing <code>pixels[]</code> Array is Strictly Required,
 *                  otherwise NullPointerException will be Thrown. (This was not Mandatory in Processing IDE)
 *<p>
 * Known Issue: {@code PImage.resize()} will Not Change the Size of the specified Image directly. Instead, the Resized Image
 *                  will be in the Return value. The Correct way to Resize is <code>yourImage = imageToResize.resize(x,y)</code>
 *<p>
 * Detail: The Return Type of {@code requestImage()} is <code>Future&lt;PImage&gt;</code>, not <code>PImage</code>.
 *<p>
 * Detail: Theoretically, External Libraries Designed for Processing IDE should also Work with JProcessing
 *             after Manually Download and Include them in your Java Project. However, this is largely Untested.
 *             The Sound Library <code>ddf.minim</code> is Tested to Work.
 *<p>
 * Important: Unlike Processing IDE, Every Event Method in JProcessing including {@code setup()} and {@code draw()}
 *                has <code>protected</code> Access Privilege. Refer to the Format Listed Below when Overriding them.
 *<p>
 * Todo: Processing Functions like: P3D Graphics, Table, Array Functions, XML, PDF, Noise, PShape etc. are Not yet Available.<br>
 * Todo: Further Testing on Processing Libraries are Required to ensure their Compatibility
 *<p>
 *            ----- Overridable Functions are Listed Below -----     <br>
 * <code>protected void setup(){ }                                 //<br>
 * protected void draw(){ }                                        //<br>
 * protected void mousePressed(MouseEvent e){ }                    //Mouse Button Pressed Event<br>
 * protected void mousePressed(){ }                                //<br>
 * protected void mouseReleased(MouseEvent e){ }                   //Mouse Button Released Event<br>
 * protected void mouseReleased(){ }                               //<br>
 * protected void mouseClicked(MouseEvent e){ }                    //Mouse Button Clicked Event<br>
 * protected void mouseClicked(){ }                                //<br>
 * protected void mouseMoved(MouseEvent e){ }                      //Mouse Move Event<br>
 * protected void mouseMoved(){ }                                  //<br>
 * protected void mouseDragged(MouseEvent e){ }                    //Mouse Drag Event<br>
 * protected void mouseDragged(){ }                                //<br>
 * protected void mouseWheel(MouseWheelEvent e){ }                 //Mouse Wheel Event<br>
 * protected void keyPressed(KeyEvent e){ }                        //<br>
 * protected void keyPressed(){ }                                  //Key Pressed Event<br>
 * protected void keyReleased(KeyEvent e){ }                       //<br>
 * protected void keyReleased(){ }                                 //Key Released Event<br>
 * protected void keyTyped(KeyEvent e){ }                          //<br>
 * protected void keyTyped(){ }                                    //Key Typed Event<br>
 * protected void windowResized(){ }                               //Window Resize Event<br>
 * protected void windowMoved(){ }                                 //Window Move Event<br>
 * protected void windowClosing(){ }                               //Window Closing Event<br>
 * protected void focusGained(){ }                                 //Window Gain Focus Event<br>
 * protected void focusLost(){ }                                   //Window Lose Focus Event<br>
 * protected void iconified(){ }                                   //Window Iconify Event<br>
 * protected void deiconified(){ }                                 //Window Deiconify Event
 *</code><p>
 *   ----- Default Key Functions (for Debugging), Override {@code keyPressed()} to Disable -----<br>
 * Exit Program:         ESC , END <br>
 * Pause and Resume:     PAUSE <br>
 * Frame Rate Presets:   F1 (10fps) , F2 (30fps) , F3 (60fps) , F4 (96fps) , F5 (160fps)
 * @author Kevin Liu
 * @version 1.0 (initial release)
 * @see javax.swing.JFrame
 * @see <a href="https://processing.org/">Processing IDE Official Website</a>
 * @see <a href="https://processing.org/reference">Detailed Documentation on Processing Methods</a>
 * @see <a href="https://processing.org/examples">Examples Tested to Work with JProcessing</a>
 * @see <a href="https://processing.org/reference/libraries">Processing External Libraries</a>
 */
//================================================================================================================================
@SuppressWarnings({"unused","WeakerAccess"})
public class Processing extends JFrame {                        //Processing Class
    //  Basic GUI Parameters:
//--------------------------------------------------------------------------------------------------------------------------------
    private final GUIPanel mainPanel=new GUIPanel();            //Display Panel
    private final BufferedImage frameBuffer[]={ new BufferedImage(screenWidth,screenHeight,BufferedImage.TYPE_INT_ARGB),
            new BufferedImage(screenWidth,screenHeight,BufferedImage.TYPE_INT_ARGB) };
    private Graphics2D GFX;                                     //Frame Buffer (Double Buffered) and its Graphics
    private boolean antiAliasing=true;                          //If Anti-Aliasing is Enabled
    private java.util.Timer displayTimer;                       //Frame Timer
    private long startTime;                                     //Start Time of Program in Milliseconds
    private boolean enabled=false;                              //If <void draw(){ }> is Running in Loops
    private byte startState=1;                                  //Processing State (Running is 0,
    //    Start With Rendering is 1, Start Without Rendering is 2)
//  Display Parameters:
//--------------------------------------------------------------------------------------------------------------------------------
    /**Width of the main display screen (Read-Only)*/
    public static final short screenWidth=(short)Toolkit.getDefaultToolkit().getScreenSize().width;
    /**Height of the main display screen (Read-Only)*/
    public static final short screenHeight=(short)Toolkit.getDefaultToolkit().getScreenSize().height;
    /**Width of the GUI window not including borders (Read-Only)*/
    public short width=(short)(screenHeight*8/9);
    /**Height of the GUI window not including borders (Read-Only)*/
    public short height=(short)(screenHeight/2);
    /**Width of GUI window's borders in total (Read-Only)*/
    public byte borderWidth;
    /**Height of GUI window's borders in total (Read-Only)*/
    public byte borderHeight;
    /**A pixel map of the frame buffer, equals <code>null</code> before calling {@code loadPixels()}*/
    public color pixels[];
    private short pixelWidth, pixelHeight;                      //Dimension of the Pixel Array
    private short frameTime=16;                                 //Target Frame Time (Default is 16)
    private int frameStartTime;                                 //Time at the Invoke of <setup()> in Millisecond
    /**Frame rate of GUI currently displaying at (Read-Only)*/
    public float frameRate=60;
    /**Number of frames rendered in total*/
    public int frameCount=0;
    /**Whether the GUI window is currently being focused or not*/
    public boolean focused=true;
    boolean visible=true;                                       //If GUI Window is set Visible

//  Input and Math Variables:
//--------------------------------------------------------------------------------------------------------------------------------
    /**If a key is currently being pressed*/
    public boolean keyPressed=false;
    /**Character representation of the key last being pressed*/
    public char key='\0';
    /**ASCII code of the key last being pressed*/
    public short keyCode=0;
    /**The backspace character*/
    protected static final char BACKSPACE='\b';
    /**The tab character*/
    protected static final char TAB='\t';
    /**The newline character*/
    protected static final char ENTER='\n';
    /**The return character*/
    protected static final char RETURN='\r';
    /**The escape character*/
    protected static final char ESC='\27';
    /**The delete character*/
    protected static final char DELETE='\127';
    /**The keycode for alt (=18 or <code>KeyEvent.VK_ALT</code>)*/
    protected static final byte ALT=18;
    /**The keycode for ctrl (=17 or <code>KeyEvent.VK_CONTROL</code>)*/
    protected static final byte CONTROL=17;
    /**The keycode for shift (=16 or <code>KeyEvent.VK_SHIFT</code>)*/
    protected static final byte SHIFT=16;
    /**The keycode for up (=38 or <code>KeyEvent.VK_UP</code>)*/
    protected static final byte UP=38;
    /**The keycode for down (=40 or <code>KeyEvent.VK_DOWN</code>)*/
    protected static final byte DOWN=40;
    /**The keycode for left (=37 or <code>KeyEvent.VK_LEFT</code>)<br>
     * Text alignment -- align text to the left*/
    protected static final byte LEFT=37;
    /**The keycode for right (=39 or <code>KeyEvent.VK_RIGHT</code>)<br>
     * Text alignment -- align text to the right*/
    protected static final byte RIGHT=39;
    /**If a mouse button is currently being pressed*/
    public boolean mousePressed=false;
    /**Numerical representation of mouse button last being pressed*/
    public byte mouseButton=0;
    /**Current X location of the mouse cursor*/
    public short mouseX=0;
    /**Current Y location of the mouse cursor*/
    public short mouseY=0;
    /**X location of the mouse cursor in the previous frame*/
    public short pmouseX=0;
    /**Y location of the mouse cursor in the previous frame*/
    public short pmouseY=0;

    private static final Random randomGenerator=new Random();   //Random Number Generator
    /**The PI constant (=3.1415927 or 180°)*/
    protected static final float PI=(float)Math.PI;
    /**The PI constant divide by 2 (=1.5707964 or 90°)*/
    protected static final float HALF_PI=(float)(Math.PI/2);
    /**The PI constant divide by 4 (=0.7853982 or 45°)*/
    protected static final float QUARTER_PI=(float)(Math.PI/4);
    /**The PI constant multiply by 2 (=6.2831855 or 360°)*/
    protected static final float TWO_PI=(float)(Math.PI*2);
    /**The TAU constant (=6.2831855 or <code>TWO_PI</code>)*/
    protected static final float TAU=(float)(Math.PI*2);
    /**The natural logarithm constant (=2.7182817 or <code>exp(1)</code>)*/
    protected static final float E=(float)Math.E;

    //  Graphics and Translation Parameters:
//--------------------------------------------------------------------------------------------------------------------------------
    private Color fill=Color.white;                             //fill Color
    private Color stroke=Color.black;                           //Stroke Color
    private short strokeWeight=1;                               //Stroke Width

    /**Stroke cap mode -- ends unclosed paths and dash segments with a round decoration that has a radius
     *                    equal to half of the line width.
     * Stroke join mode -- joins path segments by rounding off the corner at a radius of half the line width.*/
    public static final short ROUND=(short)BasicStroke.CAP_ROUND;
    /**Stroke cap mode -- end unclosed paths and dash segments with a square projection that extends beyond
     *                    the end of the segment to half of the line width.*/
    public static final short SQUARE=(short)BasicStroke.CAP_SQUARE;
    /**Stroke cap mode -- end unclosed paths and dash segments with no added decoration.*/
    public static final short PROJECT=(short)BasicStroke.CAP_BUTT;
    /**Stroke join mode -- joins path segments by extending their outside edges until they meet.*/
    public static final short MITER=(short)BasicStroke.JOIN_MITER;
    /**Stroke join mode -- joins path segments by connecting the outer corners of their wide outlines with a straight segment.*/
    public static final short BEVEL=(short)BasicStroke.JOIN_BEVEL;
    private short capMode=ROUND;                                //Stroke Style
    private short joinMode=MITER;

    /**Shape mode -- use the first two parameters of a rectangle, ellipse, or <code>PImage</code> as the location of
     *               the center of their shape, and use the last two parameters as their width and height.<br>
     * Text alignment -- align text to the center*/
    public static final byte CENTER=0;
    /**Shape mode -- use the first two parameters of a rectangle, ellipse, or <code>PImage</code> as the location of
     *               the top-left corner of their shape, and use the last two parameters as their width and height.*/
    public static final byte CORNER=1;
    /**Shape mode -- use the first two parameters of a rectangle, ellipse, or <code>PImage</code> as the location of
     *               the top-left corner of their shape, and use the last two parameters as
     *               the location of the bottom-right corner of their shape.*/
    public static final byte CORNERS=2;
    /**Shape mode -- use the first two parameters of a rectangle, ellipse, or <code>PImage</code> as the location of
     *               the center of their shape, and use the last two parameters as half of their width and height.*/
    public static final byte RADIUS=3;
    private byte rectMode=CORNER;                               //Rectangle Mode
    private byte ellipseMode=CENTER;                            //Ellipse Mode

    /**Arc mode -- arc remains open with no path segments connecting the two ends of the arc*/
    public static final short OPEN=(short)Arc2D.OPEN;
    /**Arc mode -- arc closed by drawing a straight line from the start of the arc to the end of the arc*/
    public static final short CHORD=(short)Arc2D.CHORD;
    /**Arc mode -- arc closed by drawing straight line from the start and end of the arc to the center of the full ellipse*/
    public static final short PIE=(short)Arc2D.PIE;
    /**Polygon mode -- draw opened polygon in which endpoint of the polygon doesn't connect back to where it starts*/
    public static final byte OPENED=1;
    /**Polygon mode -- draw closed polygon in which endpoint of the polygon connects back to where it starts*/
    public static final byte CLOSE=2;
    /**Polygon mode -- draw closed polygon in which endpoint of the polygon connects back to where it starts*/
    public static final byte CLOSED=2;
    /**Polygon mode -- draw point on every coordinate*/
    public static final byte POINTS=3;
    /**Polygon mode -- draw line between every other coordinates*/
    public static final byte LINES=4;
    /**Polygon mode -- draw triangle among every three coordinates*/
    public static final byte TRIANGLES=5;
    /**Polygon mode -- draw triangle strip where the last two coordinates of the previous drawn triangle is reused*/
    public static final byte TRIANGLE_STRIP=7;
    /**Polygon mode -- draw triangle fan where the first and last coordinates of the previous drawn triangle is reused*/
    public static final byte TRIANGLE_FAN=8;
    /**Polygon mode -- draw quadrilateral among every four coordinates*/
    public static final byte QUADS=6;
    /**Polygon mode -- draw quadrilateral strip where the the last two coordinates of the previous drawn quadrilateral is reused*/
    public static final byte QUAD_STRIP=9;
    private byte polygonMode=0;                                 //Polygon Mode (Not Drawing is 0)
    private final ArrayList<Short> vertexX=new ArrayList<>(10);
    private final ArrayList<Short> vertexY=new ArrayList<>(10);
    private final ArrayList<Boolean> curved=new ArrayList<>(5);
    private final ArrayList<Float> bezierVertexX=new ArrayList<>(10);
    private final ArrayList<Float> bezierVertexY=new ArrayList<>(10);
    //Matrix Transformation and Previous Style History
    private final ArrayList<AffineTransform> transform=new ArrayList<>(4);
    private final ArrayList<Font> styleFont=new ArrayList<>(1);
    private final ArrayList<byte[]> styleByte=new ArrayList<>(1);
    private final ArrayList<short[]> styleShort=new ArrayList<>(1);
    private final ArrayList<float[]> styleFloat=new ArrayList<>(1);
    private final ArrayList<Color[]> styleColor=new ArrayList<>(1);
    private final ArrayList<Boolean> styleTinted=new ArrayList<>(1);

    /**Mouse cursor -- the default arrow cursor type*/
    public static final byte ARROW=Cursor.DEFAULT_CURSOR;
    /**Mouse cursor -- the cross-hair cursor type*/
    public static final byte CROSS=Cursor.CROSSHAIR_CURSOR;
    /**Mouse cursor -- the hand cursor type*/
    public static final byte HAND=Cursor.HAND_CURSOR;
    /**Mouse cursor -- the move cursor type*/
    public static final byte MOVE=Cursor.MOVE_CURSOR;
    /**Mouse cursor -- the text cursor type*/
    public static final byte TEXT=Cursor.TEXT_CURSOR;
    /**Mouse cursor -- the wait cursor type*/
    public static final byte WAIT=Cursor.WAIT_CURSOR;           //Empty Cursor
    private static final Cursor noCursor=Toolkit.getDefaultToolkit().createCustomCursor(new
            BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB),new Point(),null);
//  PImage and PFont Parameters:
//--------------------------------------------------------------------------------------------------------------------------------
    /**Color mode -- specify input color in HSB color mode*/
    public static final short HSB=(short)BufferedImage.TYPE_CUSTOM;
    /**Color mode -- specify input color in RGB color mode<br>
     * Image format -- represents an image with 8-bit RGB color components packed into integer pixels.*/
    public static final short RGB=(short)BufferedImage.TYPE_INT_RGB;
    /**Image format -- represents an image with 8-bit RGBA color components packed into integer pixels.*/
    public static final short ARGB=(short)BufferedImage.TYPE_INT_ARGB;
    /**Image format -- represents a unsigned byte gray-scale image, non-indexed.*/
    public static final short ALPHA=(short)BufferedImage.TYPE_BYTE_GRAY;
    //PImage Formats
    private short colorMode=RGB;                                //Color Mode
    private float RMax=255, GMax=255, BMax=255, AMax=255;       //Color Range (Default is 0~255)
    private byte imageMode=CORNER;                              //Image Mode
    private boolean tinted=false;                               //Image Color Filter
    private float tintR, tintG, tintB, tintA;

    private Font font=createFont("Calibri",36);      //Text Font
    /**Text alignment -- default text alignment setting*/
    public static final byte BASELINE=1;
    /**Text alignment -- align text to the top*/
    public static final byte TOP=2;
    /**Text alignment -- align text to the bottom*/
    public static final byte BOTTOM=3;                          //Text Alignment (together with LEFT, RIGHT, CENTER)
    private byte alignModeX=BASELINE;
    private byte alignModeY=BASELINE;                           //Text Alignment Mode
    /**Global character encoding set, default is "GB18030"*/
    public static String charset="GB18030";
    private float leading;                                      //Text Leading

//  Processing Default Constructor:
//--------------------------------------------------------------------------------------------------------------------------------
    /**Construct a new GUI window of JProcessing
     *<p>
     * After JProcessing has been constructed and initialized, a new thread will be started,
     * in which {@code setup()} is called after 100ms of delay. Then, a timer will be started
     * to invoke {@code draw()} at the frequency of the targeted frameRate.<br>
     *<p>
     * As the secondary thread sleeping, the original thread will start to initialize variables
     * defined inside the subclass of <code>JProcessing</code>.
     * @throws HeadlessException if GraphicsEnvironment.isHeadless() returns true
     */
    public Processing(){                                        //Initialize Display Window
        super("  JProcessing  ");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(mainPanel);                              //Setup Graphics Parameter
        mainPanel.setBounds(0,0,screenWidth,screenHeight);
        setGraphics(frameBuffer[0].createGraphics());
        drawBackground(new Color(0xF0F0F0));
        addListener();
        leading=textAscent()+textDescent();
        //Run Processing in Separate Thread to
        new Thread(()->{                                        //    Initialize Variables declared in <Main> Class
            delay(100);
            startTime=System.currentTimeMillis();
            setup();                                            //Evoke <void setup(){ }>
            size(width,height);
            location((screenWidth-width)/2,(screenHeight-height)/2);
            frameStartTime=millis();
            if (visible) setVisible(true);
            if (startState==1){
                startState=0;
                loop();                                         //Start Rendering
            }else if(startState==2){
                startState = 0;
                redraw();
            }
        }).start();
    }

//  Input and Event Functions (These Functions are Designed to be Overridden):
//--------------------------------------------------------------------------------------------------------------------------------
    /**Processing {@code void setup()} function, invoked after <code>JProcessing</code> being initialized.<p>
     * {@code setup()} will be invoked only once at 100ms after the initialization of new <code>JProcessing</code>,
     *     before the {@code draw()} is invoked. Data loading, variable initialization, and environmental property
     *     definition should be done in {@code setup()}.<p>
     * Under normal circumstances, {@code setup()} should not be manually invoked again after its initial execution.
     * @see <a href="https://processing.org/reference/setup_.html">&lt;void setup()&gt; in Processing IDE</a>
     */
    @SuppressWarnings("WeakerAccess") protected void setup(){}
    /**
     * Processing {@code void draw()} function, invoked before the update of every frame.<p>
     * {@code draw()} will be invoked repeatedly at the frequency up to the GUI's frame rate specified by {@code frameRate(rate)}.
     *     if the task in {@code draw()} takes too long to finish, the frame rate will be lowered to the rate of completion of
     *     {@code draw()} in each second.<p>
     * Only Functions that need to run at every frame should be put inside {@code draw()}, such as graphics rendering,
     *     image displaying, mouse or key input detecting etc. Time consuming tasks such as image loading should not be done in
     *     {@code draw()}, otherwise will lower the frame rate dramatically. Instead, they should done in {@code setup()} or
     *     asynchronously in another thread.<p>
     * If a new frame is needed to be updated manually, use {@code redraw()} to render a single frame. It is very uncommon to
     *     find situations where {@code draw()} needs to be executed without the actual update of contents on the screen.
     *     In that case, {@code draw()} can be invoked manually but it will not be thread safe. In that case, it is
     *     recommended to add <code>synchronized</code> modifier when overriding {@code draw()}.
     * @see <a href="https://processing.org/reference/draw_.html">&lt;void draw()&gt; in Processing IDE</a>
     * @see Processing#redraw()
     */
    @SuppressWarnings("WeakerAccess") protected void draw(){ noLoop(); }

    /**Processing {@code void mousePressed(event)} function, invoked when a mouse button has been pressed.<p>
     * invokes {@code protected void mousePressed()} in <code>JProcessing</code>.
     * @param e MouseEvent
     */
    @SuppressWarnings("all") protected void mousePressed(MouseEvent e){ mousePressed(); }
    /**Processing {@code void mousePressed()} function, invoked when a mouse button has been pressed.
     */
    @SuppressWarnings("WeakerAccess") protected void mousePressed(){}                      //Mouse Button Pressed Event
    /**
     * Processing {@code void mouseReleased(event)} function, invoked when a mouse button has been released.<p>
     * invokes {@code protected void mouseReleased()} in <code>JProcessing</code>.
     * @param e MouseEvent
     */
    @SuppressWarnings("all") protected void mouseReleased(MouseEvent e){ mouseReleased(); }
    /**Processing {@code void mouseReleased()} function, invoked when a mouse button has been released.
     */
    @SuppressWarnings("WeakerAccess") protected void mouseReleased(){}                     //Mouse Button Released Event
    /**
     * Processing {@code void mouseClicked(event)} function, invoked when a mouse button has been clicked.<p>
     * invokes {@code protected void mouseClicked()} in <code>JProcessing</code>.
     * @param e MouseEvent
     */
    @SuppressWarnings("all") protected void mouseClicked(MouseEvent e){ mouseClicked(); }
    /**Processing {@code void mouseClicked()} function, invoked when a mouse button has been clicked.
     */
    @SuppressWarnings("WeakerAccess") protected void mouseClicked(){}                      //Mouse Button Clicked Event
    /**
     * Processing {@code void mouseMoved(event)} function, invoked when mouse cursor moves across the GUI window.<p>
     * invokes {@code protected void mouseMoved()} in <code>JProcessing</code>.
     * @param e MouseEvent
     */
    @SuppressWarnings("all") protected void mouseMoved(MouseEvent e){ mouseMoved(); }
    /**Processing {@code void mouseMoved()} function, invoked when mouse cursor moves across the GUI window.
     */
    @SuppressWarnings("WeakerAccess") protected void mouseMoved(){}                        //Mouse Move Event
    /**
     * Processing {@code void mouseDragged(event)} function, invoked when mouse cursor drags across the GUI window.<p>
     * invokes {@code protected void mouseDragged()} in <code>JProcessing</code>.
     * @param e MouseEvent
     */
    @SuppressWarnings("all") protected void mouseDragged(MouseEvent e){ mouseDragged(); }
    /**Processing {@code void mouseDragged()} function, invoked when mouse cursor drags across the GUI window.
     */
    @SuppressWarnings("WeakerAccess") protected void mouseDragged(){}                      //Mouse Drag Event
    /**
     * Processing {@code void mouseWheel(event)} function, invoked when mouse wheel rolls.
     * @param e MouseWheelEvent
     */
    @SuppressWarnings("all") protected void mouseWheel(MouseWheelEvent e){}                //Mouse Wheel Event
    /**
     * Processing {@code void keyPressed(event)} function, invoked when a key has been pressed on the keyboard.<p>
     * invokes {@code protected void keyPressed()} in <code>JProcessing</code>.
     * @param e KeyEvent
     */
    @SuppressWarnings("all") protected void keyPressed(KeyEvent e){ keyPressed(); }        //Key Pressed Event
    /**
     * Processing {@code void keyReleased(event)} function, invoked when a key has been released on the keyboard.<p>
     * invokes {@code protected void keyReleased()} in <code>JProcessing</code>.
     * @param e KeyEvent
     */
    @SuppressWarnings("all") protected void keyReleased(KeyEvent e){ keyReleased(); }      //Key Released Event
    /**
     * Processing {@code void keyTyped(event)} function, invoked when a key has been typed on the keyboard.<p>
     * invokes {@code protected void keyTyped()} in <code>JProcessing</code>.
     * @param e KeyEvent
     */
    @SuppressWarnings("all") protected void keyTyped(KeyEvent e){ keyTyped(); }            //Key Typed Event
    /**Processing {@code void keyTyped()} function, invoked when a key has been typed on the keyboard.
     */
    @SuppressWarnings("WeakerAccess") protected void keyTyped(){}

    /**Invoked when the size of GUI window has been changed. Designed to be overridden*/
    @SuppressWarnings("WeakerAccess") protected void windowResized(){}                     //Window Resize Event
    /**Invoked when the location of GUI window on screen has been changed. Designed to be overridden*/
    @SuppressWarnings("WeakerAccess") protected void windowMoved(){}                       //Window Move Event
    /**Invoked when the close button of GUI window has been pressed. Designed to be overridden*/
    @SuppressWarnings("WeakerAccess") protected void windowClosing(){}                     //Window Closing Event
    /**Invoked when GUI window gains focus. Designed to be overridden*/
    @SuppressWarnings("WeakerAccess") protected void focusGained(){}                       //Window Gain Focus Event
    /**Invoked when GUI window lost focus. Designed to be overridden*/
    @SuppressWarnings("WeakerAccess") protected void focusLost(){}                         //Window Lose Focus Event
    /**Invoked when GUI window has been minimized to tray icon. Designed to be overridden*/
    @SuppressWarnings("WeakerAccess") protected void iconified(){}                         //Window Iconify Event
    /**Invoked when GUI window has been restored from tray icon. Designed to be overridden*/
    @SuppressWarnings("WeakerAccess") protected void deiconified(){}                       //Window Deiconify Event

//  Default Key Behaviors (This Functions are Designed to be Overridden):
//--------------------------------------------------------------------------------------------------------------------------------
    /**Processing {@code void keyPressed()} function, invoked when a key has been pressed on the keyboard.<p>
     * Default key-press functions for debugging are:<br>
     * Exit program: ESC, END <br>
     * Pause and resume rendering: PAUSE <br>
     * Set frame rate: F1 (10fps), F2 (30fps), F3 (60fps), F4 (96fps), F5 (160fps)
     */
    @SuppressWarnings("WeakerAccess") protected void keyPressed(){
        switch(keyCode){                                        //Default Key Pressed Behaviors (will Disable if Overridden)
            case KeyEvent.VK_ESCAPE: case KeyEvent.VK_END:
                System.exit(0);
                break;                                          //Terminate Program
            case KeyEvent.VK_PAUSE:
                if(enabled)                                     //Start & End Loop
                    noLoop();
                else
                    loop();
                break;
            case KeyEvent.VK_F1:                                //Set Target Frame Rate to 10
                frameTime(97);
                break;
            case KeyEvent.VK_F2:                                //Set Target Frame Rate to 30
                frameTime(32);
                break;
            case KeyEvent.VK_F3:                                //Set Target Frame Rate to 60
                frameTime(16);
                break;
            case KeyEvent.VK_F4:                                //Set Target Frame Rate to 96
                frameTime(8);
                break;
            case KeyEvent.VK_F5:                                //Set Target Frame Rate to 160
                frameTime(5);
                break;
        }
    }
    /**Processing {@code void keyReleased()} function, invoked when a key has been released on the keyboard.<p>
     * Default key-release function for debugging is:<br>
     * Exit program while running in full screen: ESC
     */                                                         //Default Key Released Behaviors (will Disable if Overridden)
    @SuppressWarnings("WeakerAccess") protected void keyReleased(){
        if (isUndecorated() && keyCode==KeyEvent.VK_ESCAPE) System.exit(0);
    }
    //JFrame Inherited Functions
    @Override @Deprecated public final void repaint(){ redraw(); }
    @Override @Deprecated public final void repaint(long time){ redraw(); }
    @Override @Deprecated public final void repaint(int x, int y, int width, int height){ redraw(); }
    @Override @Deprecated public final void repaint(long time, int x, int y, int width, int height){ redraw(); }
    @Override @Deprecated public final void setBackground(Color c){ super.setBackground(c); }
    @Override @Deprecated public final void setBounds(Rectangle r){ super.setBounds(r); }
    @Override @Deprecated public final void setBounds(int x, int y, int w, int h){ super.setBounds(x,y,w,h); }
    @Override @Deprecated public final void update(Graphics g){ redraw(); }

    //================================================================================================================================
//  Internal Functions (Not Accessible from Outside):
//--------------------------------------------------------------------------------------------------------------------------------
    private void addListener(){                                 //Add Component Listener and Key Listener to Processing
        addComponentListener(new ComponentAdapter(){
            @Override public final void componentResized(ComponentEvent e){
                width=(short)(getWidth()-borderWidth);          //Add Component Listener
                height=(short)(getHeight()-borderHeight);
                Processing.this.windowResized();
            }
            @Override public final void componentMoved(ComponentEvent e){ Processing.this.windowMoved(); }
        });
        addKeyListener(new KeyListener(){                       //Add Keyboard Listener
            @Override public final void keyPressed(KeyEvent e){
                keyPressed=true;
                key=e.getKeyChar();
                keyCode=(short)e.getKeyCode();
                Processing.this.keyPressed(e);
            }
            @Override public final void keyReleased(KeyEvent e){
                keyPressed=false;
                key=e.getKeyChar();
                keyCode=(short)e.getKeyCode();
                Processing.this.keyReleased(e);
            }
            @Override public final void keyTyped(KeyEvent e){ Processing.this.keyTyped(e); }
        });
        mainPanel.addMouseListener(new MouseAdapter(){          //Add Mouse Event Listener
            @Override public final void mousePressed(MouseEvent e){
                mousePressed=true;
                if (e.getButton()==MouseEvent.BUTTON1)
                    mouseButton=LEFT;
                else if (e.getButton()==MouseEvent.BUTTON3)
                    mouseButton=RIGHT;
                else
                    mouseButton=CENTER;
                Processing.this.mousePressed(e);
            }
            @Override public final void mouseReleased(MouseEvent e){
                mousePressed=false;
                Processing.this.mouseReleased(e);
            }
            @Override public final void mouseClicked(MouseEvent e){ Processing.this.mouseClicked(e); }
        });
        mainPanel.addMouseMotionListener(new MouseMotionListener(){
            @Override public final void mouseMoved(MouseEvent e){
                mouseX=(short)e.getX();                         //Add Mouse Motion Listener
                mouseY=(short)e.getY();
                Processing.this.mouseMoved(e);
            }
            @Override public final void mouseDragged(MouseEvent e){
                mouseX=(short)e.getX();
                mouseY=(short)e.getY();
                Processing.this.mouseDragged(e);
            }
        });
        addMouseWheelListener(Processing.this::mouseWheel);
        addWindowListener(new WindowAdapter(){                  //Add Window Event Listener
            @Override public final void windowClosing(WindowEvent e){ Processing.this.windowClosing(); }
            @Override public final void windowIconified(WindowEvent e){ Processing.this.iconified(); }
            @Override public final void windowDeiconified(WindowEvent e){ Processing.this.deiconified(); }
        });
        addFocusListener(new FocusListener(){                   //Add Window Focus Listener
            @Override public final void focusGained(FocusEvent e){
                focused=true;
                Processing.this.focusGained();
            }
            @Override public final void focusLost(FocusEvent e){
                focused=false;
                Processing.this.focusLost();
            }
        });
    }

    private class GUIPanel extends JPanel{                      //Override <void paint(g){ }> in GUIPanel
        @Override
        public final void paint(Graphics g){                    //Display Frame on Window
            super.paint(g);
            g.drawImage(frameBuffer[(frameCount+1)%2],0,0,null);
            g.dispose();
        }
    }
    private void timerRestart(){                                //Restart Frame Timer
        if (startState==0) {
            displayTimer = new Timer();                         //Frame Rendering Task
            displayTimer.scheduleAtFixedRate(new TimerTask(){
                @Override public final void run() { startDraw(); }
            }, 0, frameTime);
        }
    }
    private synchronized void frameRateReschedule(short delay){ //Reschedule Frame Rate
        if(enabled && frameTime!=delay){
            displayTimer.cancel();
            delay(delay);
            timerRestart();
        }
    }
    private synchronized void startDraw(){                      //Start Frame Rendering
        draw();                                                 //Evoke <void draw(){ }>
        if (visible) mainPanel.repaint();
        pmouseX=mouseX;
        pmouseY=mouseY;
        while (transform.size()>0) popMatrix();                 //Reset Transformation and Rendering Style
        while (styleFont.size()>0) popStyle();
        frameRate=108.749f/max((millis()-frameStartTime),frameTime)+frameRate*0.891251f;
        frameStartTime=millis();                                //Record Frame Rate anf Frame Count
        frameCount++;
        setGraphics(frameBuffer[frameCount%2].createGraphics());//Double Buffer <GUIPanel>
        GFX.drawImage(frameBuffer[(frameCount+1)%2],0,0,null);
    }

    private void setGraphics(@NotNull Graphics2D g){            //Setup Graphics for Frame Buffer
        GFX=g;
        GFX.setFont(font);
        if (antiAliasing) smooth();
    }
    private synchronized void drawBackground(Color c){          //Draw Frame Background
        GFX.setPaint(c);
        GFX.fillRect(0,0,screenWidth,screenHeight);
        GFX.setPaint(fill);
    }
    private synchronized void drawShape(Shape s){               //Set Fill Color and Paint Shape
        if (fill.getAlpha()>0){
            GFX.setPaint(fill);
            GFX.setStroke(new BasicStroke(0));
            GFX.fill(s);
        }
        drawLine(s);
    }@SuppressWarnings("MagicConstant")
    private synchronized void drawLine(Shape s){                //Set Stroke Color and Draw Border
        if (strokeWeight<=0 || stroke.getAlpha()==0) return;
        GFX.setPaint(stroke);
        GFX.setStroke(new BasicStroke(strokeWeight,capMode,joinMode));
        GFX.draw(s);
    }

    @Contract("_, _, _, _ -> new") @NotNull                     //Create new RGB Color
    private Color createColor(float R, float G, float B, float A){
        if (colorMode==RGB)
            return new Color(round(constrain(R,0,RMax)/RMax*255), round(constrain(G,0,GMax)/GMax*255),
                    round(constrain(B,0,BMax)/BMax*255), round(constrain(A,0,AMax)/AMax*255));
        //Create new HSB Color
        int c=Color.HSBtoRGB(constrain(R/RMax,0,1), constrain(G/GMax,0,1),
                constrain(B/BMax,0,1));
        return new Color(c&0xFFFFFF|round(constrain(A,0,AMax)/AMax*255)<<24,true);
    }

    @Contract("_, _, _ -> new") @NotNull                        //Get Part of Image
    private static color get(@NotNull BufferedImage img, int x, int y){ return new color(img.getRGB(x,y),true); }
    @SuppressWarnings("MagicConstant")
    private static PImage get(@NotNull BufferedImage img, int x, int y, int w, int h){
        return toPImage(img.getSubimage(x,y,w,h),w,h,(short)img.getType());
    }                                                           //Get every Pixel of Image
    private static color[] loadPixels(@NotNull BufferedImage img){
        color c[]=new color[img.getWidth()*img.getHeight()];
        for (short indexY=0; indexY<img.getHeight(); indexY++)
            for (short indexX=0; indexX<img.getWidth(); indexX++)
                c[indexY*img.getWidth()+indexX]=get(img,indexX,indexY);
        return c;
    }
    @SuppressWarnings("MagicConstant")
    private static PImage resize(BufferedImage img, int w, int h){
        if (w<=0)                                               //Resize Image
            w = round(img.getWidth()/(float)img.getHeight()*h);
        else if (h<=0)
            h = round(img.getHeight()/(float)img.getWidth()*w);
        return toPImage(img,w,h,(short)img.getType());
    }
    private static PImage tintImage(BufferedImage img, int minW, int minH,
                                    float r, float g, float b, float a, PImage out){
        if (out==null){                                         //Tint Image
            out = createImage(minW, minH, ARGB);
            if (minW < img.getWidth() || minH < img.getHeight()) img = resize(img, minW, minH);
        }else
            img = out;
        minW = img.getWidth();
        minH = img.getHeight();
        //Utilize Multiple Threads to Tint Image
        ExecutorService renderTask=Executors.newSingleThreadExecutor();
        ArrayList<Future<int[]>> pixels=new ArrayList<>(minH);
        for (short index=0; index<minH; index++)
            pixels.add(renderTask.submit(new tintImage(img.getRGB
                    (0,index,minW,1,null,0,minW),r,g,b,a)));
        try{
            for (short index=0; index<minH; index++)
                out.setRGB(0,index,minW,1,pixels.get(index).get(),0,minW);
        }catch (InterruptedException | ExecutionException e){
            println(e+"\nError: Failed to Tint Image.");
        }
        return out;
    }
    private static class tintImage implements Callable<int[]>{  //Image Tinting Task
        private final int img[];                                //Pixel Map of Partial Image
        private final float r, g, b, a;                         //Image Tinting Factor
        private tintImage(int[] img, float r, float g, float b, float a){
            this.img=img;
            this.r=r;
            this.g=g;
            this.b=b;
            this.a=a;
        }
        @Override
        public final int[] call(){                              //Tint Single Line of Image
            for (short index=0; index<img.length; index++)
                img[index]=round(((img[index]>>16)&0xFF)*r)<<16|round(((img[index]>>8)&0xFF)*g)<<8|
                        round((img[index]&0xFF)*b)|round(((img[index]>>24)&0xFF)*a)<<24;
            return img;
        }
    }                                                           //Load Image in new Thread
    private static class asyncLoadImage implements Callable<PImage>{
        private final String path;
        private asyncLoadImage(String path){ this.path=path; }
        @Override public final PImage call() { return loadImage(path); }
    }                                                           //Cast BufferedImage to PImage
    private static PImage toPImage(BufferedImage img, int w, int h,
                                   @MagicConstant(intValues={RGB,ARGB,ALPHA}) short type){
        PImage pimg=createImage(w,h,type);
        pimg.getGraphics().drawImage(img,0,0,w,h,null);
        return pimg;
    }
    //Split String by Multiple Chars
    private static ArrayList<String> splitSting(@NotNull String[] str, String c){
        ArrayList<String> newLine=new ArrayList<>(str.length*4);
        for (String line:str) newLine.addAll(Arrays.asList(line.split(c.substring(0,1))));
        if (c.length()==1) return newLine;
        return splitSting(newLine.toArray(new String[]{}),c.substring(1));
    }                                                           //Text Wrapping
    private ArrayList<String> wrapText(@NotNull String str, float w){
        ArrayList<String> line=new ArrayList<>(8);
        for (short index,breakPoint=(short)str.length(); textWidth(str)>w||str.contains("\n")||str.contains("\r");
             breakPoint=(short)str.length()){
            for (index=0; index<str.length() && textWidth(str.substring(0,index+1))<=w; index++){
                if (!Character.isAlphabetic(str.charAt(index))) breakPoint=(short)(index+1);
                if (str.charAt(index)=='\n' || str.charAt(index)=='\r'){
                    index++;
                    break;
                }
            }
            if (breakPoint>=index) breakPoint=index;
            line.add(str.substring(0,breakPoint));
            str=str.substring(breakPoint);
        }
        line.add(str);
        return line;
    }@Contract("_ -> new") @NotNull
    private static File toFile(@NotNull String path){           //Format File from String
        if (path.length()>0 && path.charAt(0)=='/') return new File(path);
        if (path.length()>2 && (path.substring(1,3).equals(":/")||path.substring(1,3).equals(":\\")))
            return new File(path);
        return new File(System.getProperty("user.dir")+"/"+path);
    }@Nullable
    private static URL toURL(@NotNull String path){             //Format URL from String
        try{
            if (path.contains("://")) return new URL(path);
            if (path.length()>0 && path.charAt(0)=='/') return new URL("file://"+path);
            if (path.length()>2 && (path.substring(1,3).equals(":/")||path.substring(1,3).equals(":\\")))
                return new URL("file:///"+path);
            return new URL("file:///"+System.getProperty("user.dir")+"/"+path);
        }catch(MalformedURLException e){
            println(e+"\nError: Bad URL Format.");
            return null;
        }
    }

    //================================================================================================================================
//  System and Input Output Functions:
//--------------------------------------------------------------------------------------------------------------------------------
    public static void exit(){ System.exit(0); }
    public static void exit(int status){ System.exit(status); } //Exit Program

    public static void delay(long millis){                      //Delay in Execution
        try{
            Thread.sleep(millis);
        }catch (InterruptedException e){
            println(e+"\nError: Delay Interrupted.");
        }
    }
    public void thread(String name){                            //Start new Thread by Method Name
        for (Method m:getClass().getDeclaredMethods()){
            if (m.getName().equals(name)){
                new Thread(()->{
                    try{
                        m.setAccessible(true);
                        m.invoke(this);
                    }catch(IllegalAccessException | InvocationTargetException e){
                        println(e+"\nError: Cannot Start new Thread with Method <"+
                                m.getReturnType()+" "+m.getName()+"()>");
                    }
                }).start();
            }
        }
    }                                                           //Get current Date and Time
    public final int millis(){ return (int)(System.currentTimeMillis()-startTime); }
    public static byte second(){ return (byte)Calendar.getInstance().get(Calendar.SECOND); }
    public static byte minute(){ return (byte)Calendar.getInstance().get(Calendar.MINUTE); }
    public static byte hour(){ return (byte)Calendar.getInstance().get(Calendar.HOUR_OF_DAY); }
    public static byte day(){ return (byte)Calendar.getInstance().get(Calendar.DAY_OF_MONTH); }
    public static byte month(){ return (byte)Calendar.getInstance().get(Calendar.MONTH); }
    public static short year(){ return (short)Calendar.getInstance().get(Calendar.YEAR); }
    public static void randomSeed(long seed){ randomGenerator.setSeed(seed); }
    //Seed Random Generator
    @NotNull public static String sketchPath(){ return System.getProperty("user.dir"); }
    @NotNull public static String sketchPath(String str){ return System.getProperty("user.dir").concat(str); }

    public static void link(String uri){                        //Get Program Directory
        try{
            if (Desktop.isDesktopSupported()) Desktop.getDesktop().browse(new URI(uri));
        }catch (URISyntaxException e){                          //Open External Links
            println(e+"\nError: Bad URI Format.");
        }catch (IOException e){
            println(e+"\nError: Failed to Open ["+uri+"]");
        }
    }
    public static void launch(String path){                     //Launch External Program or File
        try{
            if (Desktop.isDesktopSupported()) Desktop.getDesktop().open(toFile(path));
        }catch (IOException | IllegalArgumentException e){
            println(e+"\nError: Failed to Launch ["+toFile(path)+"]");
        }
    }
    @Nullable                                                   //Create InputStream from URL
    public static InputStream createInput(@NotNull String path){
        try{
            return Objects.requireNonNull(toURL(path)).openStream();
        }catch (IOException e){
            println(e+"\nError: Cannot Open Input Stream in ["+toURL(path)+"]");
            return null;
        }
    }@Nullable                                                  //Create OutputStream from URL
    public static OutputStream createOutput(@NotNull String path){
        try{
            return Objects.requireNonNull(toURL(path)).openConnection().getOutputStream();
        }catch (IOException e){
            println(e+"\nError: Cannot Create Output Stream to ["+toURL(path)+"]");
            return null;
        }
    }@Nullable                                                  //Create Buffered Reader from File
    public static BufferedReader createReader(@NotNull String path){
        try{
            return new BufferedReader(new FileReader(toFile(path)));
        }catch (FileNotFoundException e){
            println(e+"\nError: Failed to Establish BufferedReader form File ["+toFile(path)+"]");
            return null;
        }
    }@Nullable                                                  //Create Print Writer to File
    public static PrintWriter createWriter(@NotNull String path){
        try{
            return new PrintWriter(toFile(path),charset);
        }catch (FileNotFoundException | UnsupportedEncodingException e){
            println(e+"\nError: Failed to Establish PrintWriter to File ["+toFile(path)+"]");
            return null;
        }
    }                                                           //Copy Array Content
    @SuppressWarnings("SuspiciousSystemArraycopy")
    protected static void arrayCopy(Object src, Object dst, int length){
        System.arraycopy(src,0,dst,0,length); }
    @SuppressWarnings("SuspiciousSystemArraycopy")
    protected static void arrayCopy(Object src, int srcOffset, Object dst, int dstOffset, int length){
        System.arraycopy(src,srcOffset,dst,dstOffset,length);
    }
    //Print Variable to Console
    public static void print(@NotNull Object c){ System.out.print(c.toString()); }
    public static void println(@NotNull Object c){ System.out.println(c.toString()); }
    public final void printMatrix(){ System.out.println(GFX.getTransform()); }
    //Print Array to Console
    public static void printArray(@NotNull boolean array[]){
        for (int index=0; index<array.length; index++) println("["+index+"] "+array[index]); }
    public static void printArray(@NotNull byte array[]){
        for (int index=0; index<array.length; index++) println("["+index+"] "+array[index]); }
    public static void printArray(@NotNull short array[]){
        for (int index=0; index<array.length; index++) println("["+index+"] "+array[index]); }
    public static void printArray(@NotNull int array[]){
        for (int index=0; index<array.length; index++) println("["+index+"] "+array[index]); }
    public static void printArray(@NotNull long array[]){
        for (int index=0; index<array.length; index++) println("["+index+"] "+array[index]); }
    public static void printArray(@NotNull float array[]){
        for (int index=0; index<array.length; index++) println("["+index+"] "+array[index]); }
    public static void printArray(@NotNull double array[]){
        for (int index=0; index<array.length; index++) println("["+index+"] "+array[index]); }
    public static void printArray(@NotNull char array[]){
        for (int index=0; index<array.length; index++) println("["+index+"] "+array[index]); }
    public static void printArray(@NotNull String array[]){
        for (int index=0; index<array.length; index++) println("["+index+"] "+array[index]); }
    public static void printArray(@NotNull ArrayList<?> array){
        for (int index=0; index<array.size(); index++) println("["+index+"] "+array.get(index).toString()); }
    public static void printArray(@NotNull PVector array[]){
        for (int index=0; index<array.length; index++) println("["+index+"] "+array[index].toString()); }

    //  String Functions:
//--------------------------------------------------------------------------------------------------------------------------------
    @Nullable public static String[] loadStrings(@NotNull String path){ return loadStrings(path,charset); }
    @Nullable public static String[] loadStrings(@NotNull String path, String charset){
        try{                                                    //Load String Array from File
            return Files.readAllLines(toFile(path).toPath(),Charset.forName(charset)).toArray(new String[]{});
        }catch (IOException e){
            println(e+"\nError: Cannot Load Strings From File ["+toFile(path)+"]");
            return null;
        }
    }@Nullable
    public static byte[] loadBytes(@NotNull String path){       //Load Binary Data From File
        try{
            return Files.readAllBytes(toFile(path).toPath());
        }catch (IOException e){
            println(e+"\nError: Cannot Read From File ["+toFile(path)+"]");
            return null;
        }
    }
    public static void saveStrings(@NotNull String path, String[] data){
        saveStrings(path, data, charset); }                     //Wirte String Array to File
    public static void saveStrings(@NotNull String path, String[] data, String charset){
        try{
            Files.write(toFile(path).toPath(),Arrays.asList(data),Charset.forName(charset));
        }catch (IOException e){
            println(e+"\nError: Cannot Save Strings to File ["+toFile(path)+"]");
        }
    }                                                           //Write Binary Data to File
    public static void saveBytes(@NotNull String path, byte[] data){
        try{
            Files.write(toFile(path).toPath(),data);
        }catch (IOException e){
            println(e+"\nError: Cannot Write to File ["+toFile(path)+"]");
        }
    }

    @NotNull protected static String[] split(@NotNull String str, char c){ return str.split(String.valueOf(c)); }
    @NotNull public static String[] splitTokens(@NotNull String str){ return splitTokens(str,'\0'); }
    @NotNull public static String[] splitTokens(@NotNull String str, char c){
        return splitSting(new String[]{str},c+"\f\n\r\t ").toArray(new String[]{});
    }                                                           //Split String
    @Contract(pure = true) @NotNull
    protected static String trim(@NotNull String str){ return str.trim(); }
    @Contract("_ -> param1")
    public static String[] trim(@NotNull String[] str){         //Trim Leading and Tailing Whitespace in String
        for (int index=0; index<str.length; index++) str[index]=str[index].trim();
        return str;
    }
    public static String join(@NotNull String[] str, char separator){ return join(str, String.valueOf(separator)); }
    public static String join(@NotNull String[] str, String separator){
        String line="";                                         //Merge String with Specified Separator
        for (int index=0; index<str.length-1; index++) line=line.concat(str[index]).concat(separator);
        if (str.length>0) line=line.concat(str[str.length-1]);
        return line;
    }

    @Contract(pure = true)                                      //Convert between Decimal number and Binary String
    @NotNull public static String binary(int n){ return Integer.toBinaryString(n); }
    @NotNull public static String binary(@NotNull Color c){ return Integer.toBinaryString(c.getRGB()); }
    @NotNull public static String binary(@NotNull Color c, int digits){ return binary(c.getRGB(),digits); }
    @NotNull public static String binary(int n, int digits){
        String str=Integer.toBinaryString(n);
        return str.substring(max(str.length()-digits,0));
    }
    public static int unbinary(String value){ return Integer.parseUnsignedInt(value,2); }
    @Contract(pure = true)                                      //Convert between Decimal number and Hexadecimal String
    @NotNull public static String hex(int n){ return Integer.toHexString(n).toUpperCase(); }
    @NotNull public static String hex(@NotNull Color c){ return Integer.toHexString(c.getRGB()).toUpperCase(); }
    @NotNull public static String hex(@NotNull Color c, int digits){ return hex(c.getRGB(),digits); }
    @NotNull public static String hex(int n, int digits){
        String str=Integer.toHexString(n);
        return str.substring(max(str.length()-digits,0)).toUpperCase();
    }
    public static int unhex(String value){ return Integer.parseUnsignedInt(value,16); }

    //  Math Functions:
//--------------------------------------------------------------------------------------------------------------------------------
    public static float random(){ return randomGenerator.nextFloat(); }            //Generate Random Number
    public static <value extends Number> float random(@NotNull value high){
        return randomGenerator.nextFloat()*high.floatValue();
    }
    public static <value extends Number> float random(@NotNull value low, @NotNull value high){
        return lerp(low.floatValue(),high.floatValue(),randomGenerator.nextFloat());
    }
    public static float randomGaussian(){ return (float)randomGenerator.nextGaussian(); }
    //Square and Square Root Calculation
    @Contract(pure = true) protected static float sq(double n){ return (float)Math.pow(n,2); }
    @Contract(pure = true) protected static float sqrt(double n){ return (float)Math.sqrt(n); }
    //Power and Exponential Calculation
    @Contract(pure = true) protected static float pow(double n, double e){ return (float)Math.pow(n,e); }
    protected static float exp(double n){ return (float)Math.exp(n); }          //General Root Calculation
    public static float root(double base, double n){ return (float)Math.pow(Math.E,Math.log(base)/n); }
    //Logarithmic Calculation
    @Contract(pure = true) protected static float log(double n){ return (float)Math.log(n); }
    public static float log(double base, double n){ return (float)(Math.log(n)/Math.log(base)); }
    @Contract(pure = true) protected static float ln(double n){ return (float)Math.log(n); }
    //Round to Integer
    @Contract(pure = true) protected static int floor(float n){ return (int)n; }
    @Contract(pure = true) protected static long floor(double n){ return (long)n; }
    @Contract(pure = true) protected static int ceil(float n){ return (int)(n+1); }
    @Contract(pure = true) protected static long ceil(double n){ return (long)(n+1); }
    @Contract(pure = true) protected static int round(float n){ return Math.round(n); }
    @Contract(pure = true) protected static long round(double n){ return Math.round(n); }
    //Absolute Value Calculation
    @Contract(pure = true) protected static int abs(int n){ return Math.abs(n); }
    @Contract(pure = true) protected static long abs(long n){ return Math.abs(n); }
    @Contract(pure = true) protected static float abs(float n){ return Math.abs(n); }
    @Contract(pure = true) protected static double abs(double n){ return Math.abs(n); }
    //Trigonometric Calculation
    @Contract(pure = true) protected static float sin(double n){ return (float)Math.sin(n); }
    @Contract(pure = true) protected static float cos(double n){ return (float)Math.cos(n); }
    @Contract(pure = true) protected static float tan(double n){ return (float)Math.tan(n); }
    @Contract(pure = true) protected static float csc(double n){ return 1/(float)Math.sin(n); }
    @Contract(pure = true) protected static float sec(double n){ return 1/(float)Math.cos(n); }
    @Contract(pure = true) protected static float cot(double n){ return 1/(float)Math.tan(n); }
    //Reversed Trigonometric Calculation
    @Contract(pure = true) protected static float asin(double n){ return (float)Math.asin(n); }
    @Contract(pure = true) protected static float acos(double n){ return (float)Math.acos(n); }
    @Contract(pure = true) protected static float atan(double n){ return (float)Math.atan(n); }
    @Contract(pure = true) protected static float atan2(double x, double y){ return (float)Math.atan2(x,y); }
    //Radian and Degree Conversion
    @Contract(pure = true) protected static float radians(double d){ return (float)Math.toRadians(d); }
    @Contract(pure = true) protected static float degrees(double r){ return (float)Math.toDegrees(r); }
    //Numerical Compare
    @Contract(pure = true) protected static int min(int n1, int n2){ return Math.min(n1,n2); }
    @Contract(pure = true) protected static long min(long n1, long n2){ return Math.min(n1,n2); }
    @Contract(pure = true) protected static float min(float n1, float n2){ return Math.min(n1,n2); }
    @Contract(pure = true) protected static double min(double n1, double n2){ return Math.min(n1,n2); }
    @Contract(pure = true) public static byte min(@NotNull byte[] list){
        byte min=list[0];
        for (short index=1; index<list.length; index++) min=(byte)min(list[index],min);
        return min;
    }
    @Contract(pure = true) public static short min(@NotNull short[] list){
        short min=list[0];
        for (short index=1; index<list.length; index++) min=(short)min(list[index],min);
        return min;
    }
    @Contract(pure = true) public static int min(@NotNull int[] list){
        int min=list[0];
        for (short index=1; index<list.length; index++) min=min(list[index],min);
        return min;
    }
    @Contract(pure = true) public static long min(@NotNull long[] list){
        long min=list[0];
        for (short index=1; index<list.length; index++) min=min(list[index],min);
        return min;
    }
    @Contract(pure = true) public static float min(@NotNull float[] list){
        float min=list[0];
        for (short index=1; index<list.length; index++) min=min(list[index],min);
        return min;
    }
    @Contract(pure = true) public static double min(@NotNull double[] list){
        double min=list[0];
        for (short index=1; index<list.length; index++) min=min(list[index],min);
        return min;
    }
    @Contract(pure = true) protected static int max(int n1, int n2){ return Math.max(n1,n2); }
    @Contract(pure = true) protected static long max(long n1, long n2){ return Math.max(n1,n2); }
    @Contract(pure = true) protected static float max(float n1, float n2){ return Math.max(n1,n2); }
    @Contract(pure = true) protected static double max(double n1, double n2){ return Math.max(n1,n2); }
    @Contract(pure = true) public static byte max(@NotNull byte[] list){
        byte max=list[0];
        for (short index=1; index<list.length; index++) max=(byte)max(list[index],max);
        return max;
    }
    @Contract(pure = true) public static short max(@NotNull short[] list){
        short max=list[0];
        for (short index=1; index<list.length; index++) max=(short)max(list[index],max);
        return max;
    }
    @Contract(pure = true) public static int max(@NotNull int[] list){
        int max=list[0];
        for (short index=1; index<list.length; index++) max=max(list[index],max);
        return max;
    }
    @Contract(pure = true) public static long max(@NotNull long[] list){
        long max=list[0];
        for (short index=1; index<list.length; index++) max=max(list[index],max);
        return max;
    }
    @Contract(pure = true) public static float max(@NotNull float[] list){
        float max=list[0];
        for (short index=1; index<list.length; index++) max=max(list[index],max);
        return max;
    }
    @Contract(pure = true) public static double max(@NotNull double[] list){
        double max=list[0];
        for (short index=1; index<list.length; index++) max=max(list[index],max);
        return max;
    }
    //Constrain Value
    public static int constrain(int n, int min, int max){ return min(max(n,min),max); }
    public static long constrain(long n, long min, long max){ return min(max(n,min),max); }
    public static float constrain(float n, float min, float max){ return min(max(n,min),max); }
    public static double constrain(double n, double min, double max){ return min(max(n,min),max); }

    @Contract(pure = true) public static float map(float n, float from, float to, float low, float high){
        return (n-from)/(to-from)*(high-low)+low;               //Map Value against Set Range
    }
    @Contract(pure = true) public static double map(double n, double from, double to, double low, double high){
        return (n-from)/(to-from)*(high-low)+low;
    }                                                           //Normalize Number and Map Percentage
    @Contract(pure = true) public static float norm(float n, float from, float to){ return (n-from)/(to-from); }
    @Contract(pure = true) public static double norm(double n, double from, double to){ return (n-from)/(to-from); }
    @Contract(pure = true) public static float lerp(float from, float to, float pct){ return pct*(to-from)+from; }
    @Contract(pure = true) public static double lerp(double from, double to, double pct){ return pct*(to-from)+from; }
    //Calculate Magnitude of a Line or a Vector
    public static float dist(double x1, double y1, double x2, double y2){ return sqrt(sq(x1-x2)+sq(y1-y2)); }
    public static float dist(double x1, double y1, double z1, double x2, double y2, double z2){
        return sqrt(sq(x1-x2)+sq(y1-y2)+sq(z1-z2)); }
    public static float mag(double x, double y){ return sqrt(sq(x)+sq(y)); }
    public static float mag(double x, double y, double z){ return sqrt(sq(x)+sq(y)+sq(z)); }

    //  GUI Functions:
//--------------------------------------------------------------------------------------------------------------------------------
    public void size(int x, int y){ if(!isUndecorated()) setSize(x,y); }
    @Override                                                   //Resize Window (Disabled in Fullscreen)
    public final void setSize(int x, int y){
        pack();
        borderWidth=(byte)(getInsets().left+getInsets().right);
        borderHeight=(byte)(getInsets().top+getInsets().bottom);
        super.setSize(x+borderWidth,y+borderHeight);
        width=(short)(getWidth()-borderWidth);
        height=(short)(getHeight()-borderHeight);
    }
    public final void fullScreen(){                             //Enter Fullscreen Mode
        if(startState>0){
            setResizable(false);
            setUndecorated(true);
            setAlwaysOnTop(true);
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            width = screenWidth;
            height = screenHeight;
        }
    }@Override                                                  //Set Window Location (Disabled in Fullscreen)
    public final void setLocation(int x, int y){ super.setLocation(x-borderWidth/2,y-borderHeight/2); }
    public void location(int x, int y){ if(!isUndecorated()) setLocation(x,y); }

    public <rate extends Number> void frameRate(rate r){        //Set Target Frame Rate (1/60~200 fps)
        short delay=frameTime;
        frameTime=(short)max(round(1000/r.floatValue()),4);
        frameRateReschedule(delay);
    }
    public <time extends Number> void frameTime(time t){        //Set Target Frame Time (5~60000 ms)
        short delay=frameTime;
        frameTime=(short)max(round(t.floatValue()),4);
        frameRateReschedule(delay);
    }                                                           //Set Anti-Aliasing Mode
    public final void smooth(){
        antiAliasing=true;
        GFX.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
    }
    public final void noSmooth(){
        antiAliasing=false;
        GFX.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF);
    }
    /**Processing {@code redraw()} function, execute the code within {@code draw()} one time and then manually update a frame.<p>
     * This should only be used when the automatic frame update is disabled by {@code noloop()}, otherwise it will have no effect.
     *     Upon calling, the {@code draw()} method will be immediately invoked, then a new frame will be rendered
     *     after the completion of any previous ongoing render task.<p>
     * Do not call this method within {@code draw()}, otherwise the program will run into infinite loops.
     * @see <a href="https://processing.org/reference/redraw_.html">&lt;redraw()&gt; in Processing IDE</a>
     */
    public void redraw(){ if (startState==0 && !enabled) startDraw(); }

    public void loop(){                                         //Start Drawing
        if (!enabled){
            if (startState==0){
                enabled = true;
                timerRestart();
            }else{
                startState=1;
            }
        }
    }
    public void noLoop(){                                       //Stop Drawing
        if (enabled){
            enabled=false;
            displayTimer.cancel();
        }else if (startState==1){
            startState=2;
        }
    }
    //Get Rendered Frame
    public PImage get(){ return get(frameBuffer[frameCount%2],0,0,width,height); }
    public color get(int x, int y){ return get(frameBuffer[frameCount%2],x,y); }
    public PImage get(int x, int y, int w, int h){ return get(frameBuffer[frameCount%2],x,y,w,h); }                                                           //Set Pixel to Frame
    public void set(int x, int y, int c){ frameBuffer[frameCount%2].setRGB(x,y,c); }
    public void set(int x, int y, Color c){ frameBuffer[frameCount%2].setRGB(x,y,c.getRGB()); }
    @Deprecated protected final void set(int x, int y, BufferedImage img){ GFX.drawImage(img,x,y,null); }
    //Set Single Pixel or Image on Frame
    public void copy(int x, int y, int w, int h, int dx, int dy, int dw, int dh){
        GFX.drawImage(get(x,y,w,h),dx,dy,dw,dh,null);
    }                                                           //Copy part of Image to Frame
    public void copy(@NotNull BufferedImage source, int x, int y, int w, int h, int dx, int dy, int dw, int dh){
        GFX.drawImage(get(source,x,y,w,h),dx,dy,dw,dh,null);
    }
    public void loadPixels(){                                   //Obtain every Pixel on Frame
        pixelWidth=width;
        pixelHeight=height;
        pixels=loadPixels(frameBuffer[frameCount%2]);
    }
    public void updatePixels(){                                 //Load saved Pixel Array to Frame
        for (short indexY=0; indexY<min(pixelHeight,height); indexY++)
            for (short indexX=0; indexX<min(pixelWidth,width); indexX++)
                set(indexX,indexY,pixels[indexY*pixelWidth+indexX]);
    }

    //  Graphics Functions:
//--------------------------------------------------------------------------------------------------------------------------------
    @NotNull public final <value extends Number> color color(Color c, @NotNull value a){
        if (colorMode==RGB)                                     //Create specific Color
            return color(red(c),green(c),blue(c),a);
        else
            return color(hue(c),saturation(c),brightness(c),a);
    }
    @NotNull public final <value extends Number> color color(@NotNull value c){ return color(c,AMax); }
    @NotNull public final <value extends Number> color color(@NotNull value c, @NotNull value a){
        if (colorMode==RGB)
            return color(c,c,c,a);
        else
            return color(c,0,c,a);
    }
    @NotNull public final <value extends Number> color color(@NotNull value r, @NotNull value g, @NotNull value b){
        return color(r,g,b,AMax);
    }@Contract("_, _, _, _ -> new") @NotNull
    public final <value extends Number> color color(@NotNull value r, @NotNull value g,
                                                    @NotNull value b, @NotNull value a){
        return new color(createColor(r.floatValue(),g.floatValue(),b.floatValue(),a.floatValue()).getRGB());
    }

    public void background(Color c){ drawBackground(c); }       //Set Background Color
    public <value extends Number> void background(Color c, value a){ drawBackground(color(c,a)); }
    public <value extends Number> void background(value c){ drawBackground(color(c,AMax)); }
    public <value extends Number> void background(value c, value a){ drawBackground(color(c,a)); }
    public <value extends Number> void background(value r, value g, value b){ drawBackground(color(r,g,b,AMax)); }
    public <value extends Number> void background(value r, value g, value b, value a){ drawBackground(color(r,g,b,a)); }
    public void background(BufferedImage img){ image(img,0,0,width,height); }

    public void clear(){                                        //Clear Background
        frameBuffer[frameCount%2]=new BufferedImage(screenWidth,screenHeight,BufferedImage.TYPE_INT_ARGB);
        setGraphics(frameBuffer[frameCount%2].createGraphics());
    }
    public void fill(Color c){ fill=c; }                        //Set Paint Color
    public <value extends Number> void fill(Color c, value a){ fill=color(c,a); }
    public <value extends Number> void fill(value c){ fill=color(c,AMax); }
    public <value extends Number> void fill(value c, value a){ fill=color(c,a); }
    public <value extends Number> void fill(value r, value g, value b){ fill=color(r,g,b,AMax); }
    public <value extends Number> void fill(value r, value g, value b, value a){ fill=color(r,g,b,a); }
    public void noFill(){ fill=color(0,0,0,0); }    //Set No Paint Color

    public void stroke(Color c){ stroke=c; }                    //Set Stroke Color
    public <value extends Number> void stroke(Color c, value a){ stroke=color(c,a); }
    public <value extends Number> void stroke(value c){ stroke=color(c,AMax); }
    public <value extends Number> void stroke(value c, value a){ stroke=color(c,a); }
    public <value extends Number> void stroke(value r, value g, value b){ stroke=color(r,g,b,AMax); }
    public <value extends Number> void stroke(value r, value g, value b, value a){ stroke=color(r,g,b,a); }
    //Set No Stroke
    public void noStroke(){ stroke=color(stroke,0); }
    public void strokeCap(@MagicConstant(intValues={SQUARE,PROJECT,ROUND}) short type){ capMode=type; }
    public void strokeJoin(@MagicConstant(intValues={MITER,BEVEL,ROUND}) short type){ joinMode=type; }
    public <weight extends Number> void strokeWeight(weight w){ strokeWeight=(short)round(max(w.floatValue(),0)); }
    //Set Stroke Style and Width
    public void cursor(){ if (getCursor()==noCursor) setCursor(new Cursor(ARROW)); }
    public void cursor(@MagicConstant(intValues={ARROW,CROSS,HAND,MOVE,TEXT,WAIT}) byte type){
        setCursor(new Cursor(type));                            //Set Cursor Type
    }
    public void cursor(BufferedImage img){ cursor(img,0,0); }
    public void cursor(BufferedImage img, int x, int y){
        setCursor(Toolkit.getDefaultToolkit().createCustomCursor(img,new Point(x,y),null)); }
    public void noCursor(){ setCursor(noCursor); }              //Set No Cursor

    public void translate(double x, double y){ GFX.translate(x,y); }                                                           //Apply Rotational Transform
    public void rotate(double radians){ GFX.rotate(radians); }  //Apply Graphics Matrix Transform
    public void scale(double r){ GFX.scale(r,r); }
    public void scale(double x, double y){ GFX.scale(x,y); }
    public void shearX(double x){ GFX.shear(x,0); }
    public void shearY(double y){ GFX.shear(0,y); }
    public void shear(double x, double y){ GFX.shear(x,y); }

    public void applyMatrix(AffineTransform matrix){ GFX.transform(matrix); }
    public void applyMatrix(double m00, double m01, double m02, double m10, double m11, double m12){
        AffineTransform matrix=new AffineTransform();           //Apply Transformation Matrix
        matrix.setTransform(m00,m10,m01,m11,m02,m12);
        GFX.transform(matrix);
    }
    public void resetMatrix(){ GFX.transform(new AffineTransform()); }

    public final void pushMatrix(){ transform.add(0,GFX.getTransform()); }
    public final void popMatrix(){                              //Save and Restore Matrix Transformation
        if (transform.size()>0)
            GFX.setTransform(transform.remove(0));
        else
            GFX.setTransform(new AffineTransform());
    }
    public final synchronized void pushStyle(){                 //Save GUI and Rendering Style
        styleFont.add(0,font);
        styleByte.add(0,new byte[]{imageMode, rectMode, ellipseMode, alignModeX, alignModeY});
        styleShort.add(0,new short[]{strokeWeight, capMode, joinMode, colorMode});
        styleFloat.add(0,new float[]{RMax, GMax, BMax, AMax, tintR, tintG, tintB, tintA, leading});
        styleColor.add(0,new Color[]{fill, stroke});
        styleTinted.add(0,tinted);
    }
    public final synchronized void popStyle(){                  //Restore previous GUI and Rendering Style
        if(styleFont.size()==0) exit();
        textFont(styleFont.remove(0));
        byte b[]=styleByte.remove(0);
        short s[]=styleShort.remove(0);
        float f[]=styleFloat.remove(0);
        Color c[]=styleColor.remove(0);
        fill=c[0]; stroke=c[1];
        imageMode=b[0]; rectMode=b[1]; ellipseMode=b[2];
        alignModeX=b[3]; alignModeY=b[4];
        strokeWeight=s[0];
        capMode=s[1]; joinMode=s[2];
        colorMode=s[3];
        RMax=f[0]; GMax=f[1]; BMax=f[2]; AMax=f[3];
        tinted=styleTinted.remove(0);
        tintR=f[4]; tintG=f[5]; tintB=f[6]; tintA=f[7];
        leading=f[8];
    }
    //Set Color Mode
    public void colorMode(@MagicConstant(intValues={RGB,HSB}) short type){
        if (type!=colorMode){
            colorMode=type;
            RMax=BMax=GMax=255;                                 //Restore Default Color Scale
        }
    }
    public <value extends Number> void colorMode(@MagicConstant(intValues={RGB,HSB}) short type, value max){
        colorMode(type,max,max,max,max); }
    public <value extends Number> void colorMode(@MagicConstant(intValues={RGB,HSB}) short type,
                                                 value max1, value max2, value max3){
        colorMode(type,max1,max2,max3,AMax); }
    public <value extends Number> void colorMode(@MagicConstant(intValues={RGB,HSB}) short type,
                                                 value max1, value max2, value max3, value maxA){
        colorMode=type;
        RMax=max1.floatValue();
        GMax=max2.floatValue();
        BMax=max3.floatValue();
        AMax=maxA.floatValue();
    }                                                           //Set Rectangle Render Mode
    public void rectMode(@MagicConstant(intValues={CENTER,CORNER,CORNERS,RADIUS}) byte type){ rectMode=type; }
    public void ellipseMode(@MagicConstant(intValues={CENTER,CORNER,CORNERS,RADIUS}) byte type){ ellipseMode=type; }
    //Set Ellipse Render Mode
//  Geometry Rendering Functions:
//--------------------------------------------------------------------------------------------------------------------------------
    public void point(double x, double y){ point((float)x,(float)y); }
    public synchronized void point(float x, float y){           //Draw Point
        GFX.setPaint(stroke);
        GFX.setStroke(new BasicStroke(0));
        GFX.fill(new Ellipse2D.Float(x-strokeWeight/2f,y-strokeWeight/2f,strokeWeight,strokeWeight));
    }
    //Draw Line
    public void line(double x1, double y1, double x2, double y2){ drawLine(new Line2D.Double(x1,y1,x2,y2)); }
    public void line(float x1, float y1, float x2, float y2){ drawLine(new Line2D.Float(x1,y1,x2,y2)); }
    //Draw Rectangle
    public void rect(double x, double y, double w, double h){ rect((float)x,(float)y,(float)w,(float)h); }
    public void rect(float x, float y, float w, float h){
        if (rectMode==CENTER){
            x-=w/2;
            y-=h/2;
        }else if (rectMode==CORNERS){
            w=w-x;
            h=h-y;
        }else if (rectMode==RADIUS){
            x-=w;
            y-=h;
            w*=2;
            h*=2;
        }
        drawShape(new Rectangle2D.Float(x,y,w,h));
    }                                                           //Draw Round Rectangle
    public void roundRect(double x, double y, double w, double h, double cw, double ch){
        roundRect((float)x,(float)y,(float)w,(float)h,(float)cw,(float)ch);
    }
    public void roundRect(float x, float y, float w, float h, float cw, float ch){
        if (rectMode==CENTER){
            x-=w/2;
            y-=h/2;
        }else if (rectMode==CORNERS){
            w-=x;
            h-=y;
        }else if (rectMode==RADIUS){
            x-=w;
            y-=h;
            w*=2;
            h*=2;
        }
        drawShape(new RoundRectangle2D.Float(x,y,w,h,cw,ch));
    }
    //Draw Triangle
    public void triangle(double x1, double y1, double x2, double y2, double x3, double y3){
        triangle((float)x1,(float)y1,(float)x2,(float)y2,(float)x3,(float)y3);
    }
    public void triangle(float x1, float y1, float x2, float y2, float x3, float y3){
        drawShape(new Polygon(new int[]{round(x1),round(x2),round(x3)},
                new int[]{round(y1),round(y2),round(y3)},3));
    }
    //Draw Quadrilateral
    public void quad(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4){
        quad((float)x1,(float)y1,(float)x2,(float)y2,(float)x3,(float)y3,(float)x4,(float)y4);
    }
    public void quad(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4){
        drawShape(new Polygon(new int[]{round(x1),round(x2),round(x3),round(x4)},
                new int[]{round(y1),round(y2),round(y3),round(y4)},4));
    }
    //Draw Ellipse
    public void ellipse(double x, double y, double w, double h){ ellipse((float)x,(float)y,(float)w,(float)h); }
    public void ellipse(float x, float y, float w, float h){
        if (ellipseMode==CENTER){
            x-=w/2;
            y-=h/2;
        }else if (ellipseMode==CORNERS){
            w-=x;
            h-=y;
        }else if (ellipseMode==RADIUS){
            x-=w;
            y-=h;
            w*=2;
            h*=2;
        }
        drawShape(new Ellipse2D.Float(x,y,w,h));
    }
    //Draw Arc or Pie
    public void arc(double x, double y, double w, double h, double start, double end){
        if (fill.getAlpha()==0)
            arc((float)x,(float)y,(float)w,(float)h,(float)start,(float)end,OPEN);
        else
            arc((float)x,(float)y,(float)w,(float)h,(float)start,(float)end,PIE);
    }
    public void arc(float x, float y, float w, float h, float start, float end){
        if (fill.getAlpha()==0)
            arc(x,y,w,h,start,end,OPEN);
        else
            arc(x,y,w,h,start,end,PIE);
    }                                                           //Draw Arc or Pie with Specific Type
    public void arc(double x, double y, double w, double h, double start, double end, short type){
        arc((float)x,(float)y,(float)w,(float)h,(float)start,(float)end,type);
    }
    public void arc(float x, float y, float w, float h, float start, float end, short type){
        if (ellipseMode==CENTER){
            x-=w/2;
            y-=h/2;
        }
        drawShape(new Arc2D.Float(x,y,w,h,-degrees(start),-degrees(end-start),type));
    }
    //Draw Quadratic and Cubic Bezier Curve
    public void bezier(double x1, double y1, double x2, double y2, double x3, double y3){
        drawLine(new QuadCurve2D.Double(x1,y1,x2,y2,x3,y3));
    }
    public void bezier(float x1, float y1, float x2, float y2, float x3, float y3){
        drawLine(new QuadCurve2D.Float(x1,y1,x2,y2,x3,y3));
    }
    public void bezier(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4){
        drawLine(new CubicCurve2D.Double(x1,y1,x2,y2,x3,y3,x4,y4));
    }
    public void bezier(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4){
        drawLine(new CubicCurve2D.Float(x1,y1,x2,y2,x3,y3,x4,y4));
    }

    public final void beginShape(){ beginShape(OPENED); }       //Start Drawing Polygon
    public final void beginShape(@MagicConstant(intValues=
            {OPENED,CLOSED,POINTS,LINES,TRIANGLES,TRIANGLE_FAN,TRIANGLE_STRIP,QUADS,QUAD_STRIP}) byte type){
        polygonMode=type;
        vertexX.clear();                                        //Reset Polygon Parameters
        vertexY.clear();
        curved.clear();
        bezierVertexX.clear();
        bezierVertexY.clear();
    }                                                           //Add Vertex to Polygon
    public synchronized <value extends Number> void vertex(value x, value y){
        if (vertexX.size()>0) curved.add(false);
        vertexX.add(x.shortValue());
        vertexY.add(y.shortValue());
    }
    public synchronized <value extends Number> void bezierVertex(value x1, value y1, value x2,
                                                                 value y2, value x3, value y3){
        vertexX.add(x3.shortValue());                           //Add Bezier Vertex to Polygon
        vertexY.add(y3.shortValue());
        if (vertexX.size()==0) return;
        curved.add(true);
        bezierVertexX.add(x1.floatValue());
        bezierVertexX.add(x2.floatValue());
        bezierVertexY.add(y1.floatValue());
        bezierVertexY.add(y2.floatValue());
    }
    public synchronized final void endShape(@MagicConstant(intValues={CLOSE}) byte type){
        polygonMode=type;                                       //Finish Drawing Polygon
        endShape();
    }
    public synchronized final void endShape(){
        switch(polygonMode){
            case CLOSE: case OPENED:                            //Draw Polygon or Polyline
                Path2D path=new Path2D.Float();
                path.moveTo(vertexX.remove(0),vertexY.remove(0));
                while (vertexX.size()>0)
                    if (curved.remove(0))
                        path.curveTo(bezierVertexX.remove(0),bezierVertexY.remove(0),
                                bezierVertexX.remove(0),bezierVertexY.remove(0),
                                vertexX.remove(0),vertexY.remove(0));
                    else
                        path.lineTo(vertexX.remove(0),vertexY.remove(0));

                if (polygonMode==CLOSE) path.closePath();
                drawShape(path);
                break;
            case POINTS:                                        //Draw Point Array
                while (vertexX.size()>0) point(vertexX.remove(0),vertexY.remove(0));
                break;
            case LINES:                                         //Draw Line Array
                while (vertexX.size()>0)
                    line(vertexX.remove(0),vertexY.remove(0),
                            vertexX.remove(0),vertexY.remove(0));
                break;
            case TRIANGLES:                                     //Draw Triangle Array
                while (vertexX.size()>0)
                    triangle(vertexX.remove(0),vertexY.remove(0),vertexX.remove(0),
                            vertexY.remove(0),vertexX.remove(0),vertexY.remove(0));
                break;
            case TRIANGLE_STRIP:                                //Draw Triangle Strip
                while (vertexX.size()>=3)
                    triangle(vertexX.remove(0),vertexY.remove(0),
                            vertexX.get(0),vertexY.get(0),vertexX.get(1),vertexY.get(1));
                break;
            case TRIANGLE_FAN:                                  //Draw Triangle Fan
                while (vertexX.size()>=3)
                    triangle(vertexX.get(0),vertexY.get(0),vertexX.remove(1),vertexY.remove(1),
                            vertexX.get(1),vertexY.get(1));
                break;
            case QUADS:                                         //Draw Quadrilateral Array
                while (vertexX.size()>0)
                    quad(vertexX.remove(0),vertexY.remove(0),vertexX.remove(0),
                            vertexY.remove(0),vertexX.remove(0),vertexY.remove(0),
                            vertexX.remove(0),vertexY.remove(0));
                break;
            case QUAD_STRIP:                                    //Draw Quadrilateral Strip
                while (vertexX.size()>=4)
                    quad(vertexX.remove(0),vertexY.remove(0),vertexX.remove(0),
                            vertexY.remove(0),vertexX.get(1),vertexY.get(1),vertexX.get(0),vertexY.get(0));
                break;
        }
        vertexX.clear();
        vertexY.clear();
        polygonMode=0;
    }

//================================================================================================================================
//  Processing Color:
//--------------------------------------------------------------------------------------------------------------------------------
    /**<code>color</code> is the Color Class for <code>JProcessing</code>.
     *<p>
     * <code>color</code> is a Subclass of <code>Color</code> with the only Difference being the Constructors.
     *     In <code>JProcessing</code>, all Methods Accepts its Superclass <code>color</code> as their Parameters,
     *     Leaving the main Purpose of <code>color</code> Class as Providing Compatibility for Processing Codes.
     *<p>
     * In Processing IDE, <code>color</code> uses <code>Integer</code> to Store its data.
     *     It can be directly Assigned with an Integer in the Format of:<br>
     *     <code>color c = 0xFFFFFF</code>.
     *<p>
     * However, <code>color</code> in <code>JProcessing</code> works quite Differently from their Equivalents
     *     in Processing IDE. In <code>JProcessing</code>, Color data is stored in Objects.
     *     The only Allowed way to Initialize a new Color is through:<br>
     *     <code>color c = new color(parameters)</code>, or <code>color c = Processing.color(parameters)</code>.
     *<p>
     * Note that the Constructors of <code>color</code> class will be Unaffected by the present Color Mode (since class
     *     <code>color</code> is <code>static</code>), while the Method <code>Processing.color(parameters)</code> will.
     * @since 1.0
     * @see Color
     * @see <a href="https://processing.org/reference/color_.html">color in Processing IDE</a>
     */
    public static class color extends Color {                   //Processing <color> Class
        /**
         * Creates an sRGB color with specified combined RGBA value consisting of the alpha component in bits 24-31,
         *     the red component in bits 16-23, the green component in bits 8-15, and the blue component in bits 0-7.
         *<p>
         * The color is set to alpha-enabled. The default alpha value if not being specified is 255.
         * @param rgba the combined RGBA color components
         */
        public color(int rgba){ super(rgba,true); }
        /**
         * Creates an sRGB color with specified combined RGBA value consisting of the alpha component in bits 24-31,
         *     the red component in bits 16-23, the green component in bits 8-15, and the blue component in bits 0-7.
         *<p>
         * The default alpha value is 255. If <code>alpha</code> is set to false, the color will be always opaque.
         * @param alpha true if the color can have alpha value, false otherwise
         * @param rgba the combined RGBA color components
         */
        public color(int rgba, boolean alpha){ super(rgba, alpha); }
        /**
         * Creates an sRGB color with the specified red, green, blue values in the range of 0~255 (inclusive).
         *<p>
         * If any of the color component exceeds 255 or is negative, it will be treated as 255 or 0 respectively.
         * @param <value> any type of Number
         * @param r the red component
         * @param g the green component
         * @param b the blue component
         */
        public <value extends Number> color(@NotNull value r, @NotNull value g, @NotNull value b){
            super(constrain(round(r.floatValue()),0,255),constrain(round(g.floatValue()),0,255),
                    constrain(round(b.floatValue()),0,255));
        }                                                       //Initialize color
        /**
         * Creates an sRGB color with the specified red, green, blue, and alpha values in the range of 0~255 (inclusive).
         *<p>
         * If any of the color component exceeds 255 or is negative, it will be treated as 255 or 0 respectively.
         * @param <value> any type of Number
         * @param r the red component
         * @param g the green component
         * @param b the blue component
         * @param a the alpha component
         */
        public <value extends Number> color(@NotNull value r, @NotNull value g, @NotNull value b, @NotNull value a){
            super(constrain(round(r.floatValue()),0,255),constrain(round(g.floatValue()),0,255),
                    constrain(round(b.floatValue()),0,255),constrain(round(a.floatValue()),0,255));
        }
        @Override @NotNull
        public String toString(){
            return "color[0x" + Integer.toHexString(getRGB()) + "], [r="+getRed()+",g="+getGreen()+",b="+getBlue()+"]";
        }
    }                                                           //Get Components of Color
    @Contract(pure = true) public final float red(int c){ return ((c>>16)&0xFF)/255f*RMax; }
    public final float red(@NotNull Color c){ return c.getRed()/255f*RMax; }
    @Contract(pure = true) public final float green(int c){ return ((c>>8)&0xFF)/255f*GMax; }
    public final float green(@NotNull Color c){ return c.getGreen()/255f*GMax; }
    @Contract(pure = true) public final float blue(int c){ return (c&0xFF)/255f*BMax; }
    public final float blue(@NotNull Color c){ return c.getBlue()/255f*BMax; }
    @Contract(pure = true) public final float alpha(int c){ return ((c>>24)&0xFF)/255f*AMax; }
    public final float alpha(@NotNull Color c){ return c.getAlpha()/255f*AMax; }
    public final float hue(int c){
        return RMax*Color.RGBtoHSB((c>>16)&0xFF,(c>>8)&0xFF,c&0xFF,null)[0]; }
    public final float hue(@NotNull Color c){ return hue(c.getRGB()); }
    public final float saturation(int c){
        return GMax*Color.RGBtoHSB((c>>16)&0xFF,(c>>8)&0xFF,c&0xFF,null)[1]; }
    public final float saturation(@NotNull Color c){ return saturation(c.getRGB()); }
    public final float brightness(int c){
        return BMax*Color.RGBtoHSB((c>>16)&0xFF,(c>>8)&0xFF,c&0xFF,null)[2]; }
    public final float brightness(@NotNull Color c){ return brightness(c.getRGB()); }

    @Contract("_, _, _ -> new") @NotNull                        //Pick Color between 2 Colors
    public static <value extends Number> color lerpColor(@NotNull Color c1, @NotNull Color c2, @NotNull value pct){
        return new color(lerp(c1.getRed(),c2.getRed(),pct.floatValue()),
                lerp(c1.getGreen(),c2.getGreen(),pct.floatValue()),
                lerp(c1.getBlue(),c2.getBlue(),pct.floatValue()),
                lerp(c1.getAlpha(),c2.getAlpha(),pct.floatValue()));
    }

//  Processing PImage and Image Functions:
//--------------------------------------------------------------------------------------------------------------------------------
    /**<code>PImage</code> is the Image Class for <code>JProcessing</code>.
     *<p>
     * <code>PImage</code> is A Subclass of <code>BufferedImage</code>. Besides the Ability to Store Image Files,
     *     <code>PImage</code> in <code>JProcessing</code> also offers Methods to Load and Save Images from Hard Drive
     *     as well as to Edit and Resize Images.
     *<p>
     * The Dimension of a <code>PImage</code> can be Obtained by Variable <code>width</code> and <code>height</code>.
     *     However, their Value will be Updating through a Image <code>TileObserver</code>, thus not being Thread-safe.
     *     A short Delay is Expected from the Change of Size of a <code>PImage</code> to the change of
     *     <code>width</code> and <code>height</code> value of that same <code>PImage</code>.
     *<p>
     * In Processing IDE, the <code>pixels[]</code> Array can be Accessed even before Calling {@code loadPixels()},
     *     Although if done so <code>pixels[]</code> Array will not Hold any Data from the Original Image. But in
     *     <code>JProcessing</code> However, This will Not be Possible. Calling {@code loadPixels()} will be Mandatory
     *     before Accessing the <code>pixels[]</code> Array, otherwise Array <code>pixels[]</code> will Not be Initialized.<br>
     * This is a Known Issue of <code>JProcessing</code>. The Reason of Intentionally leaving it Unfixed is due to the fact that
     *     the Usage of <code>pixels[]</code> Array in Processing Code Programming is considerably Rare. Pre-allocating Space for
     *     <code>pixels[]</code> Array will effectively Double the Memory Usage of every <code>PImage</code>. It is quite
     *     Inefficient and Resource Consuming.
     *<p>
     * Another Known Issue being the {@code .resize()} Method will Not change the actual Size of the <code>PImage</code> directly.
     *     Instead, the Resized Image will be Returned.
     *<p>
     * Image Loading and Real-time Processing can be Slow especially when dealing with High-resolution Images. It is Advised
     *     that if Possible, all such Methods should be Done in {@code Processing.setup()} or Asynchronously. Avoid using them
     *     in {@code Processing.draw()} where the Method will be Invoked Repeatedly by every Frame.
     *<p>
     * {@code .requestImage(path)} and {@code .save(path)} Methods will Utilize Multi-threading to Load and Save Image.
     *     Each time they are Invoked a new Thread will be started to Process their operations.
     *<p>
     * Note that Although the Instructions on the Processing Website Forbids the Construction of <code>PImage</code> Class
     *     using Syntax {@code new PImage(w,h)}, such Method is totally Allowed in <code>JProcessing</code> and will
     *     have the same result of Calling Method {@code createImage(w,h)}.
     *<p>
     * Todo: Improve Real-time Image Processing Performance (with Multi-threading) and Optimize current Multi-thread Efficiency
     * Todo: Add additional Image Processing Functions such as {@code filter()} and {@code blend()}
     *<p>
     * @since 1.0
     * @see BufferedImage
     * @see <a href="https://processing.org/reference/PImage.html">PImage in Processing IDE</a>
     */
    public static class PImage extends BufferedImage {          //Processing <PImage> Class
        /**Width of the <code>PImage</code> (Read-Only)*/
        public short width;
        /**Height of the <code>PImage</code> (Read-Only)*/
        public short height;
        /**A color map of every pixel on the PImage, initialized and updated by calling {@code loadPixels()}.<p>
         * Accessing the array before running {@code loadPixels()} will cause <code>NullPointerException</code>.*/
        public color pixels[];
        private short pixelWidth, pixelHeight;                  //Pixel Array Dimension

        /**Create a new <code>PImage</code> with type <code>ARGB</code> and
         *     dimensions specified by <code>w</code> and <code>h</code>.
         *<p>
         * This constructor works exactly the same as method {@code createImage(w,h)}.
         * @param w width of the image
         * @param h Height of the image
         */
        public PImage(int w, int h){
            super(w,h,ARGB);                                    //Initialize PImage
            addListener(w,h);
        }
        /**
         * Create a new <code>PImage</code> with type specified by <code>type</code> and
         *     dimensions specified by <code>w</code> and <code>h</code>.
         *<p>
         * This constructor works exactly the same as method {@code createImage(w,h,type)}.
         * @param w width of the image
         * @param h Height of the image
         * @param type Format of the Image<br>
         *                 <pre>Should be one of the followings: <code>Processing.RGB</code>,
         *                 <code>Processing.ARGB</code>, <code>Processing.ALPHA</code></pre>
         */
        public PImage(int w, int h, @MagicConstant(intValues={RGB,ARGB,ALPHA}) short type){
            super(w,h,type);
            addListener(w,h);
        }                                                       //Add Image Tile Observer
        private void addListener(int w, int h){
            width=(short)w;
            height=(short)h;
            addTileObserver((source, x, y, writable)->{
                width=(short)getWidth();
                height=(short)getHeight();
            });
        }

        public void loadPixels(){                               //Obtain every Pixel on Image
            pixelWidth=width;
            pixelHeight=height;
            pixels=Processing.loadPixels(this);
        }
        public void updatePixels(){                             //Apply Pixel Array to Image
            for (short indexY=0; indexY<min(pixelHeight,height); indexY++)
                for (short indexX=0; indexX<min(pixelWidth,width); indexX++)
                    set(indexX,indexY,pixels[indexY*pixelWidth+indexX]);
        }
        /**Resize the <code>PImage</code>.<p>
         * Different from Processing IDE, the dimension of the original <code>PImage</code> will be unaffected.
         * @param w new width of the <code>PImage</code>
         * @param h new height of the <code>PImage</code>
         * @return the resized instance of <code>PImage</code>
         */
        public PImage resize(int w, int h){ return Processing.resize(this,w,h); }
        //Get and Set Pixels on Image
        public color get(int x, int y){ return Processing.get(this,x,y); }
        public PImage get(int x, int y, int w, int h){ return Processing.get(this,x,y,w,h); }
        public void set(int x, int y, Color c){ setRGB(x,y,c.getRGB()); }

        public PImage mask(BufferedImage img){                  //Image Alpha Channel Blending
            return mask(Processing.resize(img,width,height).
                    getRGB(0,0,width,height,null,0,width));
        }
        public PImage mask(int[] mask){
            for (short indexY = 0; indexY < getHeight(); indexY++)
                for (short indexX = 0; indexX < getWidth(); indexX++)
                    setRGB(indexX, indexY, ((255-(mask[indexY*getWidth()+indexX]&0xFF))<<24)|
                            (getRGB(indexX,indexY)&0xFFFFFF));
            return this;
        }                                                       //Filter Image with Color
        public PImage tint(Color c){ return tint(c.getRed(),c.getGreen(),c.getBlue(),c.getAlpha()); }
        public <value extends Number> PImage tint(Color c, value a){
            return tint(c.getRed(),c.getGreen(),c.getBlue(),a); }
        public <value extends Number> PImage tint(value c){ return tint(c,c,c,255); }
        public <value extends Number> PImage tint(value c, value a){ return tint(c,c,c,a); }
        public <value extends Number> PImage tint(value r, value g, value b){ return tint(r,g,b,255); }
        public <value extends Number> PImage tint(value r, value g, value b, value a){
            return tintImage(null,0,0,constrain(r.floatValue()/255,0,1),
                    constrain(g.floatValue()/255,0,1),
                    constrain(b.floatValue()/255,0,1),
                    constrain(a.floatValue()/255,0,1),this);
        }
        //Copy part of Image
        public void copy(int x, int y, int w, int h, int dx, int dy, int dw, int dh){
            getGraphics().drawImage(get(x,y,w,h),dx,dy,dw,dh,null);
        }
        public void copy(BufferedImage source, int x, int y, int w, int h, int dx, int dy, int dw, int dh){
            getGraphics().drawImage(Processing.get(source,x,y,w,h),dx,dy,dw,dh,null);
        }
        public void save(@NotNull String path){
            final String format;                                //Save Image to File
            if (path.lastIndexOf('.',path.length()-2)>0)
                format=path.substring(path.lastIndexOf('.',path.length()-2)+1);
            else
                format="png";

            new Thread(()->{                                    //Write Image in new Thread
                try{
                    if (format.equals("jpg") || format.equals("jpeg") && this.getType()==ARGB){
                        BufferedImage img=new BufferedImage(width, height, RGB);
                        img.getGraphics().drawImage(this,0,0,null);
                        ImageIO.write(img,format,toFile(path)); //Discard Alpha-data when Saving JPG
                    }else
                        ImageIO.write(this,format,toFile(path));
                }catch (IOException | NullPointerException e){
                    println(e+"\nError: Cannot Save Image at ["+toFile(path)+"]");
                }
            }).start();
        }
    }
    @Contract("_, _ -> new") @Override @NotNull                 //Create new PImage
    public final PImage createImage(int w, int h){ return new PImage(w,h); }
    @Contract("_, _, _ -> new") @NotNull
    protected static PImage createImage(int w, int h, @MagicConstant(intValues={RGB,ARGB,ALPHA}) short type){
        return new PImage(w,h,type);
    }
    @Nullable
    public static PImage loadImage(@NotNull String path){
        try{                                                    //Load Image from File
            BufferedImage img=ImageIO.read(Objects.requireNonNull(toURL(path)));
            if (img.getType()==BufferedImage.TYPE_BYTE_GRAY || img.getType()==BufferedImage.TYPE_USHORT_GRAY)
                return toPImage(img,img.getWidth(),img.getHeight(),ALPHA);
            return toPImage(img,img.getWidth(),img.getHeight(),ARGB);
        }catch (IOException | NullPointerException e){
            println(e+"\nError: Failed to Load Image at ["+toURL(path)+"]");
            return null;
        }
    }@NotNull                                                   //Load Image Asynchronously
    public static Future<PImage> requestImage(@NotNull String path){
        return Executors.newSingleThreadExecutor().submit(new asyncLoadImage(path));
    }
    //Set Image Render Mode
    public void imageMode(@MagicConstant(intValues={CENTER,CORNER,CORNERS}) byte type){ imageMode=type; }

    public void image(BufferedImage img, double x, double y){ image(img,(float)x,(float)y); }
    public void image(BufferedImage img, float x, float y){     //Draw PImage
        if (imageMode==CORNERS)
            image(img,x,y,img.getWidth()+x,img.getHeight()+y);
        else
            image(img,x,y,img.getWidth(),img.getHeight());
    }
    public void image(BufferedImage img, double x, double y, double w, double h){
        image(img,(float)x,(float)y,(float)w,(float)h);
    }
    public void image(BufferedImage img, float x, float y, float w, float h){
        if (imageMode==CENTER){
            x-=w/2;
            y-=h/2;
        }else if (imageMode==CORNERS){
            w=w-x;
            h=h-y;
        }
        if (tinted)
            GFX.drawImage(tintImage(img,round(w),round(h),tintR,tintG,tintB,tintA,null),
                    round(x),round(y),round(w),round(h),null);
        else
            GFX.drawImage(img,round(x),round(y),round(w),round(h),null);
    }
    //Set Image Color Filter
    public void tint(Color c){
        if (c.equals(color.white))
            tinted=false;                                       //Set no Color Filtering
        else{
            tinted=true;
            tintR=constrain(c.getRed()/255f,0,1);
            tintG=constrain(c.getGreen()/255f,0,1);
            tintB=constrain(c.getBlue()/255f,0,1);
            tintA=constrain(c.getAlpha()/255f,0,1);
        }
    }
    public <value extends Number> void tint(Color c, value a){ tint(color(c,a)); }
    public <value extends Number> void tint(value c){ tint(color(c,AMax)); }
    public <value extends Number> void tint(value c, value a){ tint(color(c,a)); }
    public <value extends Number> void tint(value r, value g, value b){ tint(color(r,g,b,AMax)); }
    public <value extends Number> void tint(value r, value g, value b, value a){ tint(color(r,g,b,a)); }
    public void noTint(){ tinted=false; }

//  Processing PFont and Text Display Functions:
//--------------------------------------------------------------------------------------------------------------------------------
    /**<code>PFont</code> is the Font Class for <code>JProcessing</code>.
     *<p>
     * <code>PFont</code> is A Subclass of <code>Font</code>, with the only Difference being the Constructors and one additional
     * Method to List all Available Fonts. In Processing IDE, <code>PFont</code> is used as the Only Supported Font Class for
     * its Methods. While in <code>JProcessing</code>, all Font related Methods Accepts the original <code>Font</code> Class
     * as their Parameter, Leaving the main Purpose of <code>PFont</code> as Providing Compatibility for Processing Codes.
     *<p>
     * One of the Issue in Processing IDE being that the Font will get Blurry when a <code>PFont</code> was Initially Constructed
     *     in a small Size and then being Displayed to the screen by a Larger Size. This is because that Processing IDE uses
     *     Image to Store each font Letter.(more details on their website links down below) This Issue does not Exist in
     *     <code>JProcessing</code>, since all Fonts are stored in Vector Data.
     *<p>
     * Note that Although the Instructions on the Processing Website Forbids the Construction of <code>PFont</code> Class
     *     using Syntax {@code new PFont(name,size)}, such Method is totally Allowed in <code>JProcessing</code> and will
     *     have the same result of Calling Method {@code createFont(name,size)}.
     * @since 1.0
     * @see Font
     * @see <a href="https://processing.org/reference/PFont.html">PFont in Processing IDE</a>
     */
    public static class PFont extends Font {                    //Processing <PFont> Class
        /**Creates a new <code>PFont</code> from the specified font name and point size.
         *<p>
         * The style of the font wil be set to plain.<br>
         * This constructor works exactly the same as method {@code createFont(name,size)}.
         * @param <size> any type of number
         * @param name the font name, this can be a font face name or a font family name
         * @param size the point size of the Font
         */
        @SuppressWarnings("JavaDoc")
        public <size extends Number> PFont(String name, @NotNull size size){
            super(name,PLAIN,round(size.floatValue()));         //Initialize new PFont
        }
        /**
         * Creates a new <code>PFont</code> from the specified font name, style and point size.
         *<p>
         * The font name lookup is case insensitive.
         * @param <size> any type of number
         * @param name the font name, this can be a font face name or a font family name
         * @param style the style constant for the Font<br>
         *                  <pre>Should be one of the followings: <code>PFont.PLAIN</code>,
         *                  <code>PFont.BOLD</code>, <code>PFont.ITALIC</code></pre>
         * @param size the point size of the Font
         */
        @SuppressWarnings("JavaDoc")
        public <size extends Number> PFont(String name, @MagicConstant(intValues={PLAIN,BOLD,ITALIC})
                int style, @NotNull size size){
            super(name,style,round(size.floatValue()));
        }

        /**List all available fonts supported by the platform <code>JProcessing</code> running on<p>
         * @return array of available fonts by their name in <code>String[]</code>
         */
        public static String[] list(){                          //Return List of Available Fonts
            return GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        }
    }
    @Contract("_, _ -> new") @NotNull                           //Create new PFont
    protected static <size extends Number> PFont createFont(String name, size size){ return new PFont(name,size); }
    @Nullable
    public static PFont loadFont(@NotNull String path){         //Load Font from File
        try{
            return createFont(Font.createFont(Font.TRUETYPE_FONT,toFile(path)).getName(),12);
        }catch (IOException | FontFormatException e){
            println(e+"\nError: Failed to Load Font at ["+toFile(path)+"]");
            return null;
        }
    }
    public <size extends Number> void textFont(Font font, size size){ textFont(font.deriveFont(size.floatValue())); }
    public void textFont(Font font){                            //Set Text Font
        this.font=font;
        GFX.setFont(font);
        leading=textAscent()+textDescent();
    }                                                           //Set Font Size
    public <size extends Number> void textSize(size size){ textFont(font,size); }
    public void textAlign(@MagicConstant(intValues={LEFT,CENTER,RIGHT,TOP,BOTTOM,BASELINE}) byte align){
        alignModeX=align;                                       //Set Text Alignment
        alignModeY=BASELINE;
    }
    public void textAlign(@MagicConstant(intValues={LEFT,CENTER,RIGHT,BASELINE}) byte alignX,
                          @MagicConstant(intValues={TOP,CENTER,BOTTOM,BASELINE}) byte alignY){
        alignModeX=alignX;
        alignModeY=alignY;
    }                                                           //Set Text Leading
    public <value extends Number> void textLeading(value leading){ this.leading=leading.floatValue(); }
    //Get Text Ascent and Descent
    public final float textAscent(){ return GFX.getFontMetrics().getAscent(); }
    public final float textDescent(){ return GFX.getFontMetrics().getDescent(); }
    public final float textWidth(char c){ return textWidth(new char[]{c}); }
    public final float textWidth(char[] c){                     //Get Text Width on Screen
        return (float)GFX.getFont().getStringBounds(c,0,c.length, GFX.getFontRenderContext()).getWidth();
    }
    public final float textWidth(String str){
        return (float)GFX.getFont().getStringBounds(str,GFX.getFontRenderContext()).getWidth();
    }
    public final float textHeight(char c){ return textHeight(new char[]{c}); }
    public final float textHeight(char[] c){                    //Get Text Height on Screen
        return (float)GFX.getFont().getStringBounds(c,0,c.length, GFX.getFontRenderContext()).getHeight();
    }
    public float textHeight(String str){
        return (float)GFX.getFont().getStringBounds(str,GFX.getFontRenderContext()).getHeight();
    }
    //Display Text on Screen
    public <value extends Number> void text(value n, value x, value y){ text(n.toString(),x,y); }
    public <value extends Number> void text(value n, value x, value y, value w, value h){ text(n.toString(),x,y,w,h); }
    public <value extends Number> void text(char c, value x, value y){ text(new String(new char[]{c}),x,y); }
    public <value extends Number> void text(char c, value x, value y, value w, value h){
        text(new String(new char[]{c}),x,y,w,h); }
    public <value extends Number> void text(char c[], value x, value y){ text(new String(c),x,y); }
    public <value extends Number> void text(char c[], value x, value y, value w, value h){
        text(new String(c),x,y,w,h); }
    public <value extends Number> void text(char c[], int start, int stop, value x, value y){
        text(new String(c).substring(start,stop+1),x,y); }
    public <value extends Number> void text(char c[], int start, int stop, value x, value y, value w, value h){
        text(new String(c).substring(start,stop+1),x,y,w,h);
    }                                                           //Display Text in Free Area
    public synchronized <value extends Number> void text(String str, value x, value y){
        ArrayList<String> line=wrapText(str,Float.POSITIVE_INFINITY);
        GFX.setPaint(fill);
        for(short index=0; index<line.size(); index++){         //Align Text
            pushMatrix();
            if (alignModeX==CENTER)
                translate((-textWidth(line.get(index)))/2,0);
            else if (alignModeX==RIGHT)
                translate(-textWidth(line.get(index)),0);
            if (alignModeY==TOP)
                translate(0,textAscent());
            else if (alignModeY==CENTER)
                translate(0,textAscent()/2);
            translate(0,leading*index);
            GFX.drawString(line.get(index),x.floatValue(),y.floatValue());
            popMatrix();
        }
    }                                                           //Display Text in Fixed Area
    public <value extends Number> void text(String str, value x, value y, value w, value h){
        ArrayList<String> line=wrapText(str,w.floatValue());    //Setup Text Box
        BufferedImage label=new BufferedImage(round(w.floatValue()), round(h.floatValue()),ARGB);

        for(short index=0; index<line.size(); index++){
            Graphics2D g=label.createGraphics();
            g.setFont(GFX.getFont());                           //Align Text
            g.setPaint(fill);
            if (antiAliasing)
                g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            if (alignModeX==CENTER)
                g.translate((w.floatValue()-textWidth(line.get(index)))/2,0);
            else if (alignModeX==RIGHT)
                g.translate(w.floatValue()-textWidth(line.get(index)),0);
            if (alignModeY==CENTER)
                g.translate(0,leading*(-(line.size()-1)/2f)+(h.floatValue()-textAscent()-1.25f*textDescent())/2);
            else if (alignModeY==BOTTOM)
                g.translate(0,leading*(-line.size()+1)-textAscent()-1.5f*textDescent()+h.floatValue());
            g.translate(0,leading*index+textAscent());
            g.drawString(line.get(index),0,0);
            g.dispose();
        }
        GFX.drawImage(label,round(x.floatValue()),round(y.floatValue()),null);
    }

//  Processing PGraphics Graphics Renderer:
//--------------------------------------------------------------------------------------------------------------------------------
    /**<code>PGraphics</code> is a Stand-alone Offscreen Graphics Rendering Context Inherits all Functionality of JProcessing.
     *<p>
     * A <code>JProcessing</code> alike GUI Rendering Context, directly Extending Class <code>Processing</code>.<br>
     * Unlike <code>JProcessing.Processing</code>, <code>PGraphics</code> is designed to Draw and Store rendered Result in
     *     an Offscreen frameBuffer instead of Outputting them directly on Screen. The Rendered Image can either
     *     be Drawn to the GUI window using {@code Image(PGraphics,x,y)}, {@code Image(PGraphics,x,y,w,h)},
     *     or Obtained as <code>PImage</code> using {@code .get()}.
     *<p>
     * <code>PGraphics</code> in <code>JProcessing</code> works Primarily Identical as of in Processing IDE. However, as
     *     the Instructions on the Processing Website Forbidding the Construction of <code>PGraphics</code> Class using Syntax
     *     {@code new PGraphics(w,h)}, such Method is totally Allowed in <code>JProcessing</code> and will have the
     *     same result of Calling Method {@code createGraphics(w,h)}.
     *<p>
     * Note that <code>PGraphics</code> is set not Active and will Not update its frame According to the Frame Rate.
     *     {@code setup()}, {@code draw()}, {@code redraw()}, and {@code loop()} have been marked as Deprecated and
     *     <code>final</code> to Prevent Calling or Overriding such Methods which would otherwise result in Unexpected Behaviors.
     * @since 1.0
     * @see <a href="https://processing.org/reference/PGraphics.html">PGraphics in Processing IDE</a>
     */
    public static class PGraphics extends Processing {          //Processing <PGraphics> Class
        private BufferedImage frame;                            //Rendered Frame

        /**Create a new <code>PGraphics</code> graphics context with resolution specified by <code>w</code> and <code>h</code>.
         *<p>
         * This is done by creating a new invisible borderless <code>JProcessing</code> window at the size bounded by
         *     <code>w</code> and <code>h</code>. The <code>JProcessing</code> GUI window is set deactivated using
         *     {@code noLoop()}. The rendered result will be stored in frame buffer and not be displayed on screen.
         *<p>
         * This constructor works exactly the same as method {@code createGraphics(w,h)}.
         * @param w width of the graphics context
         * @param h height of the graphics context
         */
        public PGraphics(int w, int h){
            super();                                            //Initialize new Graphics Renderer
            setUndecorated(true);
            setSize(constrain(w,0,screenWidth),constrain(h,0,screenHeight));
            clear();
            visible=false;
        }                                                       //Deprecated to Prevent Override and Invoke
        @Override @Deprecated protected final void setup(){ noLoop(); }
        @Override @Deprecated protected final void draw(){ }
        @Override @Deprecated public final void redraw(){ }
        @Override @Deprecated public final void loop(){ }
        //Clear Rendered Frame
        public void beginDraw(){ image(frame,0,0,width,height);  }
        public void endDraw(){ frame=get(); }                   //Output Rendered Frame from Frame Buffer
    }
    @Contract("_, _ -> new") @NotNull                           //Create new Graphics Renderer
    protected static PGraphics createGraphics(int w, int h){ return new PGraphics(w,h); }
    //Draw Rendered Frame
    public void image(PGraphics g, float x, float y){ image(g.frame,x,y); }
    public void image(PGraphics g, double x, double y){ image(g.frame,x,y); }
    public void image(PGraphics g, float x, float y, float w, float h){ image(g.frame,x,y,w,h); }
    public void image(PGraphics g, double x, double y, double w, double h){ image(g.frame,x,y,w,h); }

//  Processing PVector and Vector Mathematics:
//--------------------------------------------------------------------------------------------------------------------------------
    /**<code>PVector</code> is a Class to Describe a Multi-dimensional Euclidean Vector.
     *<p>
     * Vector is Defined as A Quantity having Direction as well as Magnitude.<br>
     * The Class <code>PVector</code> Stores Components of a Vector in a <code>float</code> Array.
     *     It also Provides Methods for Vector Math such as Dot Product, Cross Product, Calculating Direction and Magnitude.
     *<p>
     * Unlike in Processing IDE, <code>PVector</code> Class in <code>JProcessing</code> are not Limited to Represent a 2 or 3
     *     Dimensional Vector. Instead, Vector with any Dimension can be Stored in <code>PVector</code> and
     *     Performs Vector Calculations.<br>
     * The Magnitude of a PVector in the first 3 Dimensions can be Set and Assigned directly using <code>x</code>, <code>y</code>,
     *     and <code>z</code>. Vector with Dimension more than 3 should use {@code .set(float[])}, {@code .set(double[])}, and
     *     {@code .array()} to Set or Obtain its Magnitude.
     *<p>
     * The Dimension of the Vector can be Specified in Constructor {@code PVector(Number)}. The Max Dimension Allowed is 32768.
     * @since 1.0
     * @see <a href="https://processing.org/reference/PVector.html">PVector in Processing IDE</a>
     */
    public static class PVector {                               //Processing <PVector> Class
        private float vector[]={};                              //Component of Vector in every Direction
        /**X component of the <code>PVector</code>, or 0 if the vector has a dimension less than 1*/
        public float x=0;
        /**Y component of the <code>PVector</code>, or 0 if the vector has a dimension less than 2*/
        public float y=0;
        /**Z component of the <code>PVector</code>, or 0 if the vector has a dimension less than 3*/
        public float z=0;

        /**Construct a 3 dimensional vector with a magnitude of 0.*/
        public PVector(){ vector=new float[]{0,0,0}; }          //Initialize new PVector
        /**
         * Construct a multi-dimensional vector with a dimension specified by <code>n</code> and a magnitude of 0.
         * @param <dimension> any type of number
         * @param n dimension of the vector*/
        public <dimension extends Number> PVector(@NotNull dimension n){
            vector=new float[n.shortValue()];
            for (short index=0; index<vector.length; index++) vector[index]=0;
            updateComponent();
        }
        /**Construct a 2 dimensional vector with a magnitude specified by <code>x</code> and <code>y</code>.
         * @param <value> any type of number
         * @param x the X component of the vector
         * @param y the Y component of the vector*/
        public <value extends Number> PVector(@NotNull value x, @NotNull value y){ set(x,y); }
        /**
         * Construct a 3 dimensional vector with a magnitude specified by <code>x</code>, <code>y</code>, and <code>z</code>.
         * @param <value> any type of number
         * @param x the X component of the vector
         * @param y the Y component of the vector
         * @param z the Z component of the vector*/
        public <value extends Number> PVector(@NotNull value x, @NotNull value y, @NotNull value z){ set(x,y,z); }
        /**
         * Construct a multi-dimensional vector using the data from a <code>float</code> array as its components.
         * @param component an array representing all components of a vector*/
        public PVector(float[] component){ set(component); }
        /**
         * Construct a multi-dimensional vector using the data from a <code>double</code> array as its components.<p>
         * Since all components of PVector are stored in the type of <code>float</code>, all entries of <code>component</code>
         *     will be converted to <code>float</code> value, and the extra precisions will be lost.<br>
         * @param component an array representing all components of a vector*/
        public PVector(double[] component){ set(component); }

        public <value extends Number> PVector set(value x){     //Set Magnitude of Vector
            if (dimension()<1) vector=new float[1];
            vector[0]=this.x=x.floatValue();
            return this;
        }@SuppressWarnings("UnusedReturnValue")
        public <value extends Number> PVector set(value x, value y){
            if (dimension()<2) vector=new float[2];
            vector[0]=this.x=x.floatValue();
            vector[1]=this.y=y.floatValue();
            return this;
        }@SuppressWarnings("UnusedReturnValue")
        public <value extends Number> PVector set(value x, value y, value z){
            if (dimension()<3) vector=new float[3];
            vector[0]=this.x=x.floatValue();
            vector[1]=this.y=y.floatValue();
            vector[2]=this.z=z.floatValue();
            return this;
        }@SuppressWarnings("UnusedReturnValue")
        public PVector set(float[] component){
            if (dimension()<component.length)
                vector=new float[component.length];
            else
                copy();
            arrayCopy(component,vector,component.length);
            return updateComponent();
        }@SuppressWarnings("UnusedReturnValue")
        public PVector set(double[] component){
            copy();
            vector=new float[max(dimension(),component.length)];
            for (short index=0; index<component.length; index++) vector[index]=(float)component[index];
            return updateComponent();
        }
        public final PVector set(@NotNull PVector v){
            if (dimension()<v.dimension())
                vector=new float[v.dimension()];
            else
                copy();
            arrayCopy(v.copy().vector,vector,v.dimension());
            return updateComponent();
        }@Contract(" -> this")
        private PVector updateComponent(){                      //Update x, y, z Component from <vector>
            if (dimension()>0) x=vector[0];
            if (dimension()>1) y=vector[1];
            if (dimension()>2) z=vector[2];
            return this;
        }@Contract(" -> new") @NotNull
        public final PVector copy(){                            //Update <vector> using Components and Return Vector
            if (dimension()>2) vector[2]=z;
            if (dimension()>1) vector[1]=y;
            if (dimension()>0) vector[0]=x;
            return new PVector(vector);
        }
        //Generate Unit Vector with Random Direction
        @NotNull public static PVector random2D(){ return fromAngle(random(TWO_PI)); }
        @NotNull public static PVector random3D(){ return fromAngle(random(TWO_PI),random(TWO_PI)); }
        @Contract("_ -> new") @NotNull                          //Generate Unit Vector in Specific Direction
        public static <angle extends Number> PVector fromAngle(@NotNull angle radians){
            return new PVector(cos(radians.floatValue()),sin(radians.floatValue()));
        }@Contract("_, _ -> new") @NotNull
        public static <angle extends Number> PVector fromAngle(@NotNull angle radiansXY, @NotNull angle radiansZ){
            return new PVector(cos(radiansZ.floatValue())*cos(radiansXY.floatValue()),
                    cos(radiansZ.floatValue())*sin(radiansXY.floatValue()),sin(radiansZ.floatValue()));
        }

        public <value extends Number> PVector add(value x){     //Vector Addition
            return set(add(this,new PVector(new float[]{x.floatValue()}))); }
        public <value extends Number> PVector add(value x, value y){ return set(add(this,new PVector(x,y))); }
        public <value extends Number> PVector add(value x, value y, value z){
            return set(add(this,new PVector(x,y,z))); }
        public PVector add(float[] v){ return set(add(this,new PVector(v))); }
        public PVector add(double[] v){ return set(add(this,new PVector(v))); }
        public PVector add(PVector v){ return set(add(this,v)); }
        public static PVector add(@NotNull PVector v1, @NotNull PVector v2){
            v2.copy();
            PVector result=(new PVector(max(v1.dimension(),v2.dimension()))).set(v1);
            for (short index=0; index<v2.dimension(); index++) result.vector[index]+=v2.vector[index];
            return result.updateComponent();
        }

        public <value extends Number> PVector sub(value x){     //Vector Subtraction
            return set(sub(this,new PVector(new float[]{x.floatValue()}))); }
        public <value extends Number> PVector sub(value x, value y){ return set(sub(this,new PVector(x,y))); }
        public <value extends Number> PVector sub(value x, value y, value z){
            return set(sub(this,new PVector(x,y,z))); }
        public PVector sub(float[] v){ return set(sub(this,new PVector(v))); }
        public PVector sub(double[] v){ return set(sub(this,new PVector(v))); }
        public PVector sub(PVector v){ return set(sub(this,v)); }
        public static PVector sub(@NotNull PVector v1, @NotNull PVector v2){
            v2.copy();
            PVector result=new PVector(max(v1.dimension(),v2.dimension())).set(v1);
            for (short index=0; index<v2.dimension(); index++) result.vector[index]-=v2.vector[index];
            return result.updateComponent();
        }
        //Scalar Multiplication
        public <value extends Number> PVector mult(value n){ return set(mult(this,n)); }
        public static <value extends Number> PVector mult(PVector v, value n){
            v=v.copy();
            for (short index=0; index<v.dimension(); index++) v.vector[index]*=n.floatValue();
            return v.updateComponent();
        }                                                       //Scalar Division
        public <value extends Number> PVector div(value n){ return set(mult(this,1/n.floatValue())); }
        public static <value extends Number> PVector div(PVector v, @NotNull value n){
            return mult(v,1/n.floatValue());
        }
        //Vector Dot Product
        public <value extends Number> float dot(value x){ return dot(this,new PVector(new float[]{x.floatValue()})); }
        public <value extends Number> float dot(value x, value y){ return dot(this,new PVector(x,y)); }
        public <value extends Number> float dot(value x, value y, value z){ return dot(this,new PVector(x,y,z)); }
        public float dot(float[] v){ return dot(this,new PVector(v)); }
        public float dot(double[] v){ return dot(this,new PVector(v)); }
        public float dot(PVector v){ return dot(this,v); }
        public static float dot(@NotNull PVector v1, @NotNull PVector v2){
            v1.copy();
            v2.copy();
            float result=0;
            for (short index=0; index<min(v1.dimension(),v2.dimension()); index++)
                result+=v1.vector[index]*v2.vector[index];
            return result;
        }
        //Vector Cross Product
        public <value extends Number> PVector cross(value x){ return cross(new PVector(new float[]{x.floatValue()})); }
        public <value extends Number> PVector cross(value x, value y){ return cross(new PVector(x,y)); }
        public <value extends Number> PVector cross(value x, value y, value z){ return cross(new PVector(x,y,z)); }
        public PVector cross(PVector v){
            PVector result=cross(this,v);
            if (result!=null) set(result);
            return result;
        }
        public static PVector cross(@NotNull PVector v1, @NotNull PVector v2){
            if (max(v1.dimension(),v2.dimension())>3){
                println(new ArithmeticException("cross product dimension > 3")+"\nError: Cannot Perform Product.");
                return new PVector();
            }
            v1=new PVector(3).set(v1);
            v2=new PVector(3).set(v2);
            v1.vector[0]=v1.y*v2.z-v1.z*v2.y;
            v1.vector[1]=v1.z*v2.x-v1.x*v2.z;
            v1.vector[2]=v1.x*v2.y-v1.y*v2.x;
            return v1.updateComponent();
        }
        //Map Vector Magnitude using Percentage
        public <value extends Number> PVector lerp(value x, value pct){
            return set(lerp(this,new PVector(new float[]{x.floatValue()}),pct)); }
        public <value extends Number> PVector lerp(value x, value y, value pct){
            return set(lerp(this,new PVector(x,y),pct)); }
        public <value extends Number> PVector lerp(value x, value y, value z, value pct){
            return set(lerp(this,new PVector(x,y,z),pct)); }
        public <value extends Number> PVector lerp(float[] v, value pct){
            return set(lerp(this,new PVector(v),pct)); }
        public <value extends Number> PVector lerp(double[] v, value pct){
            return set(lerp(this,new PVector(v),pct)); }
        public <value extends Number> PVector lerp(PVector v, value pct){ return set(lerp(this,v,pct)); }
        public static <value extends Number> PVector lerp(@NotNull PVector v1, @NotNull PVector v2, value pct){
            v2.copy();
            PVector result=new PVector(max(v1.dimension(),v2.dimension())).set(v1);
            for (short index=0; index<v2.dimension(); index++)
                result.vector[index]=Processing.lerp(result.vector[index],v2.vector[index],pct.floatValue());
            return result.updateComponent();
        }

        public short dimension(){ return (short)vector.length; }//Dimension of Vector
        public float mag(){ return sqrt(magSq()); }
        public float magSq(){                                   //Magnitude of Vector
            float magnitude=0;
            for (float index: copy().vector) magnitude+=index*index;
            return magnitude;
        }                                                       //Distance between Vectors
        public float dist(PVector v){ return sub(v).mag(); }
        public static float dist(PVector v1, PVector v2){ return sub(v2,v1).mag(); }
        @Deprecated public final float heading2D(){ return heading(); }
        public float heading(){                                 //Calculate Angle of Direction in 2D
            PVector v=new PVector(2).set(this);
            return (HALF_PI*3-atan2(v.vector[0],v.vector[1]))%TWO_PI-PI;
        }                                                       //Calculate Angle Between 2 Vectors
        public float angleBetween(PVector v){ return acos(dot(v)/this.mag()/v.mag()); }
        public static float angleBetween(PVector v1, PVector v2){ return acos(dot(v1,v2)/v1.mag()/v2.mag()); }
        //Obtain Unit Vector
        public PVector normalize(){ return set(div(mag())); }
        public static PVector normalize(PVector v){ return div(v,v.mag()); }
        //Set or Limit Magnitude of Vector
        public <value extends Number> PVector setMag(value magnitude){ return set(mult(magnitude.floatValue()/mag())); }
        public <value extends Number> PVector limit(value max){ return set(mult(min(max.floatValue()/mag(),1))); }
        public <angle extends Number> PVector rotate(angle radians){ return set(rotate(this, radians)); }
        public static <angle extends Number> PVector rotate(PVector v, @NotNull angle radians){
            v=new PVector(2).set(v);
            float angle=(v.heading()+radians.floatValue());     //2D Rotation
            return mult(new PVector(cos(angle),sin(angle)),v.mag());
        }

        public float[] array(){ return copy().vector; }         //Get Array of Dimensions
        @Override @NotNull
        public String toString(){                     //Generate String Representation of PVector
            copy();
            String str="[ ";
            for (short index=0; index<dimension()-1; index++) str=str.concat(vector[index]+", ");
            if (dimension()>0) str=str.concat(Float.toString(vector[dimension()-1]));
            return str.concat("]");
        }
    }
    /**Generate linearly spaced row vector represented by a <code>float</code> array.<p>
     * By default, this method will return a array of 100 evenly spaced value between x1 and x2.
     * This is the same as calling {@code linspace(start, end, 100)}.
     * @param <value> any type of number
     * @param start the start value x1
     * @param end the end value x2
     * @return a row vector in <code>float[]</code> of 100 evenly spaced values between x1 and x2.
     */
    public static <value extends Number> float[] linspace(value start, value end){ return linspace(start,end,100); }
    /**
     * Generate linearly spaced row vector represented by a <code>float</code> array.<p>
     * @param <value> any type of number
     * @param start the start value x1
     * @param end the end value x2
     * @param n number of points to generate
     * @return a row vector in <code>float[]</code> of <code>n</code> evenly spaced values between x1 and x2.
     */
    public static <value extends Number> float[] linspace(value start, value end, int n){
        float vector[]=new float[n];
        for (int index=0; index<n; index++){
            vector[index]=map(index,0,n-1,start.floatValue(),end.floatValue());
        }
        return vector;
    }
}