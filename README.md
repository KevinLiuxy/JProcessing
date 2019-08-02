# JProcessing
<h5>JProcessing is a Processing Code API for Java. It Enables Processing Code to be Programmed and Run directly in Java IDE.</h5>
<h3>Introduction</h3>
<p>
Processing is a programming language focused mainly on visual arts and animations in GUI programming. It provides a variety of simple methods for 2D graphics rendering, image/font displaying, as well as file operations. Compared to traditional programming languages such as C and Java, Processing is a much more efficient and easier way to go for GUI application programming. If you are unfamiliar with Processing language, please refer to <a href="https://processing.org/">Processing's official website</a> for more information.
<p>
You will need to get Processing's dedicated IDE to program in Processing. It is an open sourced programming tool you can get from <a href="https://processing.org/download/">here</a>. However, if the time comes when you want to implement Processing GUI and use its methods in an existing Java project, or some of the functionality of your project is only supported in Java IDE, or maybe for some other reasons you just have to program in Java, and need simple methods to work with GUI and animation, then JProcessing will be for you.

<h3>What is JProcessing</h3>
<p>
JProcessing is a .jar library. It is a Java API designed to implement Processing methods and functionality to Java programming environment. JProcessing contains the most essential methods of Processing in areas of 2D rendering, input/output, Math, PFont, PImage, and PVector. The main class JProcessing.Processing is the Processing GUI window (it is a subclass of JFrame), and contains Processing methods &lt;void setup()&gt;, &lt;void draw()&gt; and other input and event methods such as &lt;mousePressed()&gt;, &lt;keyReleased()&gt; etc. They work abstractly and are designed to be overridden in subclasses.
<p>
After including JProcessing in your Java project, you can then program and run Processing code in Java normally. Most of the JProcessing methods perform the same functionality as of within Processing IDE, with a few exceptions. You can copy Processing code and paste them directly into Java IDE with JProcessing, and then run it. Most of them will run just fine (after a few small modifications that is).

<h3>How to use JProcessing</h3>
<p>
To implement Processing features in Java, first you need to include JProcessing jar library in your Java project. Then, you need to create a subclass of JProcessing.Processing. Your Processing codes can then be put inside it. Override &lt;setup()&gt;, &lt;draw()&gt and other methods whenever they're needed. Finally, initialize a new object from your JProcessing.Processing subclass to start your GUI, and everything should work properly after that according to your code.
<p>
The templates of JProcessing implementation are included in the files (definitely do check them out), together with detailed documentation providing the information that will help you better use and understand JProcessing library. The known issues and debugging guides are also included and it will be essential to read them. The PDF version of the documentation is named "Template Manual.pdf". In the case of if anything goes wrong while using JProcessing, there's a great chance that the issue and its solution is already documented in the manual. So please read it carefully when any issue occurs.
<p>
If you are confused about certain method(s), you can check out <a href="https://processing.org/reference/"> the full documentation of Processing methods</a> on the Processing website. Alternatively, the Javadoc of JProcessing is also included in the folder. It contains documentation for all JProcessing classes and fields, and more importantly, the difference between how Processing IDE and JProcessing works. To my apologies, most of the methods of JProcessing are not documented in its Javadoc (since it's truly time-consuming). You can just Google: "Method_Name" + "Processing", which will lead you to the documentation page of any Processing method on its official website.

<h3>About Development</h3>
<p>
Yes, the source code is included and you are free to use, share and edit them. You are more than welcomed to contribute as well. Note that the source code for the official Processing IDE is also on the GitHub. I would say those are also worthy checking out if you are interested in developing Processing (links <a href="https://github.com/processing">here</a>). Note that JProcessing does not referred or related to code from Processing IDE by any means. It is a stand-along project started completely from scratch.
<p>
I personally positioning JProcessing as a simple add-on in java that allows the basic function of Processing to be used in Java programming. The main goal is to make it simple (single jar file), small (only includes essential and frequently used functions), and convenient to use. Since otherwise, it might be better of working directly on Processing IDE for adding advanced and complex features. I am also intending to include these features in the future: P3D Graphics, class Table, class PShape, XML & PDF file operation, generate Perlin noise, and more image processing features.
<h6>
I am currently a university student. JProcessing is the project I worked on during spare time purely out of interest. So update is not guaranteed in the future. Please contact me if you find any bugs or have any other concerns, and I will try to resolve them as soon as possible.</h6>
<h5>Have Fun!</h5>
