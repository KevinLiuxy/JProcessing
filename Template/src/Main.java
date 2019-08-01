import JProcessing.Processing;
import JProcessing.Processing.*;                                //Compatible with some Processing Libraries such as <Minim>
import java.util.ArrayList;                                     //    but they have to be Downloaded Manually

/**  ----- This is A Template of JProcessing Implementation in Java ----- <br>
 * <code>Processing</code> is a Application Programming Interface for Processing Coding Language to function in Java IDE,
 *     It is Designed to implement Processing Running Environment into Java.<p>
 * Class <code>Processing</code> Allows most Processing Codes to Run in Java Environment. It is an SubClass of
 *     <code>JFrame</code> and Contains the following nested Classes:
 *     <code>PImage</code>, <code>PFont</code>, <code>PGraphics</code>, <code>PVector</code>, <code>color</code>.
 *<p>
 * Note: This Class runs as A Processing Code Library that Allows Processing Codes to Run directly in Java Environment
 *           with minimal Modifications. You Can Paste Your Processing Code Down Bellow and Make it Run in Java. <p>
 * Note: Processing Functions like: P3D Graphics, Table, Array Functions, XML, PDF, Noise, PShape etc. are Not yet Available.
 *<p>
 * Debug: Java Use <code>double</code> As its Default Decimal Precision, while Processing Use <code>float</code> on Default,
 *            If Error Occurs when Dealing with Decimal Numbers, Add <code>f</code> After Numbers to Make it A float.
 *            For Example: <code>float n=0.5;</code> will cause an Error in Java,
 *                         and Needs to be Converted to: <code>float n=0.5f;</code><p>
 * Debug: If Variable Casting in Processing are Achieved Using Methods such as: <code>n=int(1.5)</code>, <code>m=float(PI/2.5)</code>
 *            They Have to be Converted to Java's Variable Casting Methods: <code>n=floor(1.5)</code>, <code>m=(float)(PI/2.5)</code>
 *<p>
 * Debug: The Default Encoded Character Set used in JProcessing is "GB18030", not "UTF-8". If problem Occurs in Character Encoding,
 *        Use <code>Processing.charset = "Name_of_the_Charset"</code> To change the Global Default Charset for JProcessing
 *<p>
 * Known Issue: Always call {@code loadPixels()} before Accessing <code>pixels[]</code> Array is Strictly Required,
 *                  otherwise NullPointerException will be Thrown. (This was not Mandatory in Processing IDE)<p>
 * Known Issue: {@code PImage.resize()} will Not Change the Size of the specified Image directly. Instead, the Resized Image
 *                  will be in the Return value. The Correct way to Resize is <code>yourImage = imageToResize.resize(x,y)</code>
 *<p>
 * Detail: The Return Type of {@code requestImage()} is <code>Future&lt;PImage&gt;</code>, not <code>PImage</code>.<p>
 * Detail: Theoretically, External Libraries Designed for Processing IDE should also Work with JProcessing
 *             after Manually Download and Include them in your Java Project. However, this is largely Untested.
 *             The Sound Library <code>ddf.minim</code> is Tested to Work.
 *<p>
 * Important: Unlike Processing IDE, Every Event Method in JProcessing including {@code setup()} and {@code draw()}
 *                has <code>protected</code> Access Privilege. Refer to the Format Listed Below when using them.
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
 *<p>
 * Based on JProcessing Version: 1.0 (initial release)
 * @see <a href="https://processing.org/reference/">Detailed Documentation on Processing Methods</a>
 * @see <a href="https://processing.org/examples">Examples Tested to Work with JProcessing</a>
 * @see <a href="https://processing.org/reference/libraries">Processing External Libraries</a>
 * @see javax.swing.JFrame
 * @see JProcessing.Processing
 */
//================================================================================================================================
public class Main extends Processing {                          //This is a Example of JProcessing GUI Implementation
    private Main(){ super(); }                                  //by directly Extending <Processing> Class in <Main>
    public static void main(String[] args){ new Main(); }
//================================================================================================================================

    // Global Variables can be Declared Here.
    //     However, Variables taking longer than 100ms to Initialize should Not be Initialized here,
    //     since the Allocated time for Variable Initialization is 100ms before <setup()> is Invoked

    public void setup(){

        // Your Code here will Run only Once when the Program Starts
    }
    public void draw(){

        // Your Code here will Run in Loops as each Frame being Updated
    }

    // You can Add Event Methods like <mouseMoved(){ }> or <keyPressed(){ }> here
    //     but Remember they Have to be Assigned with <protected> or <public> modifiers
}