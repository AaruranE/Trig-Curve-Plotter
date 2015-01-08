// The "CurvePlotter" class.
import java.awt.*; //By Aaruran Elamurugaiyan
import hsa.Console;
public class CurvePlotter
{
    static Console c;
    static Console c1;

    static class graph
    {
	double amplitude, k, xShift, yShift; //b/c the user will only enter sinusoidal functions, these are the only characteristics that are needed
	String type; //the type will be either "sin" or "cos"
    }


    static graph userCosine = new graph ();
    static graph userSine = new graph ();
    //Constants for processing
    static final String SINE = "sin"; //globally declaring the two types of functions that will be used
    static final String COSINE = "cos";
    static final int N = (int) (Math.pow (10, 5)); //the number of sub-intervals in the area calculation
    static final double INCREMENT = Math.pow (10, -7); //the value by which we increment the POI, zeroes, max/min algorithms
    static final double TOLERANCE = 5 * Math.pow (10, -6); //the half-width of the interval used for testing if two values are approximately equal
    static final double DELTA_X = Math.pow (10, -10); //the half-width of the interval used for approximating the slope of a tangent

    //Constants for drawing
    static final double RESOLUTION = 5 * Math.pow (10, -5); //the difference between two consecutive doubles when graphing
    static final int TICK_WIDTH = 6; //the width of the tick marks
    static final int DECIMAL_PLACES = 3; //the number of decimal places each value is printed with
    static final double X_LEGEND = Math.PI / 6; //the x-coordinate of the legend in math coordinates
    static final double Y_LEGEND = -3.5; //the y-coordinate of the legend in math coordinates

    public static void main (String[] args)
    {
	c = new Console ();
	getParameters (c);
	drawEverything ();
	c1 = new Console ();
	String choice = "";
	while (!choice.equals ("q"))
	{
	    c1.println ("If you wish to determine the zeroes, enter z");
	    c1.println ("If you wish to determine the points of intersection, enter p");
	    c1.println ("If you wish to approximate the area underneath the graphs, enter a");
	    c1.println ("If you wish to determine the locations of the max/min of the function, enter m");
	    c1.println ("If you wish to determine the slope of a tangent, enter s");
	    c1.println ("If you wish to change your functions, enter c\nIf you wish to quit, enter q");
	    printlnFunction (userSine);
	    printlnFunction (userCosine);
	    c1.print ("Your choice: ");
	    choice = c1.readLine ();
	    c1.clear ();
	    if (choice.equals ("s"))
	    {
		c1.print ("Enter the angle of interest in degrees: ");
		double angle = Math.toRadians (c1.readDouble ()); //converting the value into radians
		double slopeS = (Function (userSine, angle + DELTA_X) - Function (userSine, angle - DELTA_X)) / (2 * DELTA_X); //a secant line over a small interval, for the sine graph
		double slopeC = (Function (userCosine, angle + DELTA_X) - Function (userCosine, angle - DELTA_X)) / (2 * DELTA_X); //a secant line over a small interval, for the cosine graph
		c1.print ("The slope of the tangent on your sine graph at ");
		c1.print (Math.toDegrees (angle), 0, 3);
		c1.print (" degrees is: ");
		c1.println (slopeS, 0, DECIMAL_PLACES);
		if (Math.abs (slopeS) < TOLERANCE)
		    c1.println ("This is an extremum on the sine graph.");
		c1.print ("The slope of the tangent on your cosine graph at ");
		c1.print (Math.toDegrees (angle), 0, 3);
		c1.print (" degrees is: ");
		c1.println (slopeC, 0, DECIMAL_PLACES);
		if (Math.abs (slopeC) < TOLERANCE)
		    c1.println ("This is an extremum on the cosine graph.");
	    }
	    else if (choice.equals ("p"))
	    {
		if (((Math.abs (userSine.k * userSine.xShift - userCosine.k * userCosine.xShift)) / (Math.PI / 2) % 2 == 1 && userSine.yShift == userCosine.yShift && userSine.amplitude == userCosine.amplitude && userSine.k == userCosine.k))  // checking for identical functions
		    c1.println ("There are infinitely many points of intersection");
		else
		{
		    int totalPOI = 0;
		    for (double i = 0 ; i < 2 * Math.PI ; i = i + INCREMENT)
		    {
			if (diff (userSine, userCosine, i) < TOLERANCE && diff (userSine, userCosine, i) < diff (userSine, userCosine, i + DELTA_X))
			{
			    c1.print ("(");
			    c1.print (Math.toDegrees (i), 0, DECIMAL_PLACES);
			    c1.print (", ");
			    c1.print (Function (userSine, i), 0, DECIMAL_PLACES);
			    c1.println (")");
			    totalPOI++;
			    i = i + (0.001);  //moving the value by a small amount to avoid repeated POI
			}
		    }
		    c1.println (totalPOI + " points of intersection found");
		}
	    }
	    else if (choice.equals ("z"))
	    {
		c1.println ("Zeroes of your SINE graph");
		printZeroesOf (userSine);
		c1.println ("Zeroes of your COSINE graph");
		printZeroesOf (userCosine);
	    }
	    else if (choice.equals ("a"))
	    {
		c1.println ("This program takes the x-axis in radians, but you should still enter the bounds in degrees.");
		double areaS = 0, areaC = 0;
		c1.print ("Enter your lower bound (in degrees): ");
		double lowerBound = Math.toRadians (c1.readDouble ());
		c1.print ("Enter your upper bound (in degrees): ");
		double upperBound = Math.toRadians (c1.readDouble ());
		double Dx = (upperBound - lowerBound) / N;
		for (double i = lowerBound ; i <= upperBound ; i = i + Dx)
		{
		    areaS += Math.abs (Function (userSine, i)); //during the loop, the following two values are just the sum's of the y-values
		    areaC += Math.abs (Function (userCosine, i));
		}
		areaS *= Dx; //This calculates the area
		areaC *= Dx;
		c1.print ("The area underneath your sine graph is: ");
		c1.println (areaS, 0, DECIMAL_PLACES);
		c1.print ("The area underneath your cosine graph is: ");
		c1.println (areaC, 0, DECIMAL_PLACES);
	    }
	    else if (choice.equals ("m"))
	    {
		findMaxAndMin (userSine);
		findMaxAndMin (userCosine);
	    }
	    else if (choice.equals ("c"))
	    {
		getParameters (c1);
		drawEverything ();
	    }
	}
	c1.println ("BYEBYE! I'll miss you :'(");
    } // main method


    public static graph derivativeOf (graph g)
    {
	graph gPrime = new graph ();
	gPrime.yShift = 0;
	gPrime.k = g.k;
	gPrime.xShift = g.xShift;
	gPrime.amplitude = g.amplitude * g.k;

	if (g.type.equals (SINE))
	{
	    gPrime.type = COSINE;
	}
	else if (g.type.equals (COSINE))
	{
	    gPrime.type = SINE;
	    gPrime.amplitude *= -1;
	}
	return gPrime;
    }


    public static double diff (graph g1, graph g2, double i)
    {
	double y = Function (g1, i) - Function (g2, i);
	return (Math.abs (y));
    }


    public static void drawEverything ()
    {
	c.clear ();
	drawAxes (); //drawing everything
	drawLegend (X_LEGEND, Y_LEGEND);
	drawFunction (userSine);
	drawFunction (userCosine);
    }


    public static void getParameters (Console z)
    {
	z.println ("Sine will be in blue, and cosine will be in green.");
	z.println ("PARAMETERS FOR THE SINE GRAPH\n----------");
	gatherInfoFor (userSine, z);
	userSine.type = SINE;
	z.println ("----------------------------------");
	z.println ("PARAMETERS FOR THE COSINE GRAPH\n----------");
	gatherInfoFor (userCosine, z);
	userCosine.type = COSINE;
    } //collecting User input


    public static void findMaxAndMin (graph g)
    {
	c1.print ("Maxima of the graph: ");
	printlnFunction (g);
	double jump = INCREMENT;
	for (double i = 0 ; i <= Math.PI * 2 ; i += jump)
	{
	    if (Math.abs (Function (g, i) - g.yShift - g.amplitude) < TOLERANCE && Math.abs (Function (g, i) - g.yShift - g.amplitude) < Math.abs (Function (g, i + jump) - g.yShift - g.amplitude))
	    {
		c1.print (Math.toDegrees (i), 0, DECIMAL_PLACES);
		c1.print ("\t");
		jump = Math.PI / (g.k * 2);
	    }
	}
	c1.print ("Minima of the graph: ");
	printlnFunction (g);
	for (double i = 0 ; i <= Math.PI * 2 ; i += jump)
	{
	    if (Math.abs (Function (g, i) - (g.yShift - g.amplitude)) < TOLERANCE && Math.abs (Function (g, i) - (g.yShift - g.amplitude)) < Math.abs (Function (g, i + jump) - (g.yShift - g.amplitude)))
	    {
		jump = Math.PI / (g.k * 2);
		c1.print (Math.toDegrees (i), 0, DECIMAL_PLACES);
		c1.print ("\t");
	    }
	}
    }


    public static void printZeroesOf (graph g)  // this will print the zeroes of a given function as they are found
    {
	double jump = INCREMENT;
	for (double i = 0 ; i <= Math.PI * 2 + INCREMENT ; i = i + jump)
	{
	    if (Math.abs (Function (g, i)) < TOLERANCE && Math.abs (Function (g, i)) < Math.abs (Function (g, i + INCREMENT)))
	    {
		c1.print (Math.toDegrees (i), 0, DECIMAL_PLACES);
		c1.print ("\t");
		jump = Math.PI / (2 * g.k);  //so that zeroes aren't repeated, the program skips over by a quarter of the period
	    } //however, we ensure to be 5 increments short of the zero, because of potential rounding errors
	}
	c1.println ();
    }


    public static void printlnFunction (graph g)  //this will write a given function out on the c1 console
    {
	double kDegrees = 360 / Math.toDegrees (2 * Math.PI / g.k);
	c1.print ("y=");
	c1.print (g.amplitude, 0, DECIMAL_PLACES);
	c1.print (g.type + "(");
	if (kDegrees != 1)
	    c1.print (kDegrees, 0, DECIMAL_PLACES);
	if (g.xShift < 0)
	{
	    c1.print ("(x + ");
	    c1.print (-Math.toDegrees (g.xShift), 0, DECIMAL_PLACES);
	    c1.print (")");
	}
	else if (g.xShift > 0)
	{
	    c1.print ("(x - ");
	    c1.print (Math.toDegrees (g.xShift), 0, DECIMAL_PLACES);
	    c1.print (")");
	}
	else
	    c1.print ("x");
	if (g.yShift > 0)
	{
	    c1.print (") + ");
	    c1.print (g.yShift, 0, 3);
	}
	else if (g.yShift < 0)
	{
	    c1.print (") - ");
	    c1.print (-g.yShift, 0, 3);
	}
	else
	    c1.print (")");
	c1.print ("\n");
    }


    public static void gatherInfoFor (graph g, Console z)  //This will collect the user input for the userSine, and userCosine
    {
	z.print ("Amplitude: ");
	g.amplitude = z.readDouble ();
	z.print ("Period (in degrees): ");
	g.k = Math.toRadians (z.readDouble ()); // to change the value to radians
	g.k = (2 * Math.PI) / g.k;
	z.print ("Horizontal shift rightwards (in degrees): ");
	g.xShift = Math.toRadians (z.readDouble ());
	z.print ("Vertical shift upwards: ");
	g.yShift = z.readDouble ();
    }


    public static double Function (graph f, double x)  // This will return the y-value of a given function at a particular x-value
    {
	double y;
	if (f.type.equals (SINE))
	    y = f.yShift + f.amplitude * (Math.sin (f.k * (x - f.xShift)));
	else
	    y = f.yShift + f.amplitude * (Math.cos (f.k * (x - f.xShift)));
	return y;
    }


    public static void drawFunction (graph f)  //This draws a given function on the c Console
    {
	if ((f.type).equals (SINE))
	    c.setColor (Color.blue);
	else if ((f.type).equals (COSINE))
	    c.setColor (Color.green);
	for (double i = 0 ; i < 2 * Math.PI ; i += RESOLUTION)
	    c.drawLine (transformX (i), transformY (Function (f, i)), transformX (i + RESOLUTION), transformY (Function (f, i + RESOLUTION)));
    }


    public static void drawLegend (double x, double y)
    { //draws the legend at (x,y) coordinates

	c.setColor (Color.blue);
	c.fillOval (transformX (x), transformY (y - 0.25), 15, 15);
	c.setColor (Color.green);
	c.fillOval (transformX (x), transformY (y - 0.6), 15, 15);
	c.setColor (Color.black);
	c.drawString ("LEGEND", transformX (x), transformY (y));
	c.drawString (" = Sine graph", transformX (x + 0.1), transformY (y - 0.45));
	c.drawString (" = Cosine graph", transformX (x + 0.1), transformY (y - 0.8));

    }


    public static void drawAxes ()  //This draws the axes, and the ticks, and writes the scale out
    {
	c.setColor (Color.black);
	c.drawLine (transformX (0), transformY (0), transformX (Math.PI * 2), transformY (0)); //Drawing x-axis
	for (int i = 0 ; i <= 36 ; i += 3)
	{
	    c.drawString ("" + i * 10 + "°", transformX (Math.toRadians (i * 10)) - 5, transformY (-0.25)); //labels on the x-axis
	    c.drawLine (transformX (Math.toRadians (i * 10)), transformY (0.05), transformX (Math.toRadians (i * 10)), transformY (-0.05)); //ticks on the x-axis
	}
	c.drawLine (transformX (0), transformY (3), transformX (0), transformY (-3)); //Drawing y-axis
	for (double i = 3 ; i >= -3 ; i--)
	{
	    c.drawLine (transformX (0), transformY (i), transformX (0) + TICK_WIDTH, transformY (i)); //Ticks on the y-axis
	    c.drawString ("" + i, transformX (0) + TICK_WIDTH, transformY (i) + 5); //labels on the y-axis
	}
    }


    public static int transformX (double x)  //these methods will transform the numbers into console coordinates
    {
	return (int) ((95) * (x) + 15); //because these methods are so well-defined, if I call these two methods everytime something is drawn, I don't need to know the precise scale
    }


    public static int transformY (double y)
    {
	return 250 - (int) (y * 55);
    }
} // CurvePlotter class
