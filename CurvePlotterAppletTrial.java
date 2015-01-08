// The "CurvePlotterApplet" class.
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class CurvePlotterAppletTrial extends Applet //By Aaruran Elamurugaiyan
{
    class graph
    {
	double amplitude, k, xShift, yShift; //b/c the user will only enter sinusoidal functions, these are the only characteristics that are needed
	String type; //the type will be either "sin" or "cos"
    }


    public graph userCosine = new graph ();
    public graph userSine = new graph ();

    //Constants for processing
    public int N = (int) (Math.pow (10, 5));      //the number of sub-intervals in the area calculation
    public double INCREMENT = Math.pow (10, -6);      //the value by which we increment the POI, zeroes, max/min algorithms
    public double TOLERANCE = 5 * Math.pow (10, -6);      //the half-width of the interval used for testing if two values are approximately equal
    public double DECIMAL_PLACES = 3;

    //Constants for drawing
    public String SINE = "sin";      //globally declaring the two types of functions that will be used
    public String COSINE = "cos";
    public double RESOLUTION = 5 * Math.pow (10, -3);      //the difference between two consecutive doubles when graphing
    public int TICK_WIDTH = 6; //the width of the tick marks
    public int X_ORIGIN = 0;
    public int Y_ORIGIN = 300;
    public int xOutput = transformX (7);
    public int yOutput = transformY (1.5);

    public Color buttons = new Color (0, 200, 200);
    public Color cSine = new Color (200, 200, 240);
    public Color cSine1 = new Color (230, 230, 250);
    public Color cCosine = new Color (200, 250, 200);
    public Color cCosine1 = new Color (230, 250, 230);
    public Color cOptions = new Color (255, 100, 100);
    public Color cOptions1 = new Color (250, 230, 230);
    public Color gridLines = new Color (125, 125, 125);
    public Color text = Color.white;
    public Color background = Color.black;
    //declaring Buttons
    public Button z = new Button ("Find Zeroes");
    public Button s = new Button ("Find tangent's slope");
    public Button p = new Button ("Find Points of Intersection");
    public Button a = new Button ("Find area");
    public Button m = new Button ("Find extrema");
    public Button update = new Button ("Update functions");
    public Button test = new Button ("Amplitude:");
    public Button polar = new Button ("POLAR COORDINATES");
    public Button rect = new Button ("RECTANGULAR COORDINATES");
    //Sine labels
    public Label userSine_Amplitude = new Label ("Sine Amplitude:\t");
    public Label userSine_k = new Label ("Sine period (°):");
    public Label userSine_xShift = new Label ("Sine horizontal shift (°): ");
    public Label userSine_yShift = new Label ("Sine vertical shift: ");
    public Label sineLabel = new Label ("The sine function is blue");
    //Sine textfields
    public TextField userSine_AmplitudeInfo = new TextField ("1");
    public TextField userSine_kInfo = new TextField ("360");
    public TextField userSine_xShiftInfo = new TextField ("0");
    public TextField userSine_yShiftInfo = new TextField ("0");
    //Cosine labels
    public Label userCosine_Amplitude = new Label ("Cosine Amplitude: ");
    public Label userCosine_k = new Label ("Cosine period (°): ");
    public Label userCosine_xShift = new Label ("Cosine horizontal shift (°): ");
    public Label userCosine_yShift = new Label ("Cosine vertical shift: ");
    public Label cosineLabel = new Label ("The cosine function is green");
    //Cosine textfields
    public TextField userCosine_AmplitudeInfo = new TextField ("1");
    public TextField userCosine_kInfo = new TextField ("360");
    public TextField userCosine_xShiftInfo = new TextField ("0");
    public TextField userCosine_yShiftInfo = new TextField ("0");
    //Booleans for inputs
    public boolean inputed;
    public boolean zeroes;
    public boolean area;
    public boolean slope;
    public boolean extrema;
    public boolean points;
    public boolean polarClick;
    //Labels and textfields for inputs
    public Label promptAngle = new Label ("Enter angle of interest (°): ");
    public TextField getAngle = new TextField ("90");
    public Label promptLowerBound = new Label ("Enter the lower bound (°): ");
    public TextField getLowerBound = new TextField ("90");
    public Label promptUpperBound = new Label ("Enter the upper bound (°): ");
    public TextField getUpperBound = new TextField ("180");


    public void init ()
    {
	//inputed = false;
	zeroes = false;
	area = false;
	slope = false;
	extrema = false;
	polarClick = false;

	userSine.type = SINE;
	userCosine.type = COSINE;

	setLayout (new BorderLayout ());
	JPanel north = new JPanel ();
	JPanel south = new JPanel ();

	north.setLayout (new GridLayout (7, 1));
	south.setLayout (new GridLayout (4, 3));

	north.add (z); //First four buttons of the JPanel
	z.setBackground (buttons);
	north.add (s);
	s.setBackground (buttons);
	north.add (p);
	p.setBackground (buttons);
	north.add (a);
	a.setBackground (buttons);
	north.add (sineLabel); //Labelling the sine graph
	sineLabel.setBackground (cSine);

	north.add (m); //The extrema button
	m.setBackground (buttons);

	north.add (cosineLabel); //Labelling the cosine graph
	cosineLabel.setBackground (cCosine);

	north.add (update); //The update functions button
	update.setBackground (buttons);

	north.add (userSine_Amplitude); //the 2nd row of north
	userSine_Amplitude.setBackground (cSine);
	north.add (userSine_AmplitudeInfo);
	userSine_AmplitudeInfo.setBackground (cSine1);
	north.add (userCosine_Amplitude);
	userCosine_Amplitude.setBackground (cCosine);
	north.add (userCosine_AmplitudeInfo);
	userCosine_AmplitudeInfo.setBackground (cCosine1);

	north.add (userSine_k); //the 3rd row of north
	userSine_k.setBackground (cSine);
	north.add (userSine_kInfo);
	userSine_kInfo.setBackground (cSine1);
	north.add (userCosine_k);
	userCosine_k.setBackground (cCosine);
	north.add (userCosine_kInfo);
	userCosine_kInfo.setBackground (cCosine1);

	north.add (userSine_xShift); //the 4th row of north
	userSine_xShift.setBackground (cSine);
	north.add (userSine_xShiftInfo);
	userSine_xShiftInfo.setBackground (cSine1);
	north.add (userCosine_xShift);
	userCosine_xShift.setBackground (cCosine);
	north.add (userCosine_xShiftInfo);
	userCosine_xShiftInfo.setBackground (cCosine1);

	north.add (userSine_yShift); //the 5th row of north
	userSine_yShift.setBackground (cSine);
	north.add (userSine_yShiftInfo);
	userSine_yShiftInfo.setBackground (cSine1);
	north.add (userCosine_yShift);
	userCosine_yShift.setBackground (cCosine);
	north.add (userCosine_yShiftInfo);
	userCosine_yShiftInfo.setBackground (cCosine1);

	south.add (promptAngle); //the south panel
	promptAngle.setBackground (cOptions);
	south.add (getAngle);
	getAngle.setBackground (cOptions1);
	south.add (promptLowerBound);
	promptLowerBound.setBackground (cOptions);
	south.add (getLowerBound);
	getLowerBound.setBackground (cOptions1);
	south.add (promptUpperBound);
	promptUpperBound.setBackground (cOptions);
	south.add (getUpperBound);
	getUpperBound.setBackground (cOptions1);
	south.add (polar);
	south.add (rect);
	polar.setBackground (new Color (150, 150, 150)); //Colouring the polar button
	rect.setBackground (new Color (150, 150, 50)); //Colouring the rectangular button

	add (north, BorderLayout.NORTH); //adding the panels
	add (south, BorderLayout.SOUTH);
    }


    public void paint (Graphics g)
    {
	setBackground (background);
	Font f = new Font ("Calibri", Font.PLAIN, 12); //setting the font for all further output
	g.setFont (f);
	getInfo ();
	if (polarClick == false)
	{
	    drawGrid (g);
	    if (inputed == true)
	    {

		for (double i = 0 ; i < 2 * Math.PI ; i += RESOLUTION)
		{
		    g.setColor (Color.blue);
		    g.drawLine (transformX (i), transformY (Function (userSine, i)), transformX (i + RESOLUTION), transformY (Function (userSine, i + RESOLUTION)));
		    g.setColor (Color.green);
		    g.drawLine (transformX (i), transformY (Function (userCosine, i)), transformX (i + RESOLUTION), transformY (Function (userCosine, i + RESOLUTION)));
		}
		inputed = false;
	    }

	    if (slope == true)
	    {
		double x;
		try
		{
		    x = Math.toRadians (Double.parseDouble (getAngle.getText ()));
		}
		catch (NumberFormatException e)
		{
		    x = Math.PI / 2;
		}
		double slopeSine = Function (derivativeOf (userSine), x);
		double slopeCosine = Function (derivativeOf (userCosine), x);
		g.setColor (text);
		g.drawString ("The slope of the tangent on sine is is " + round (slopeSine), xOutput, yOutput);
		g.drawString ("The slope of the tangent on cosine is is " + round (slopeCosine), xOutput, yOutput + 15);
		slope = false;
	    }

	    if (area == true)
	    {
		double areaS = 0, areaC = 0;
		double lowerBound, upperBound;
		try
		{
		    lowerBound = Math.toRadians (Double.parseDouble (getLowerBound.getText ()));
		}
		catch (NumberFormatException e)
		{
		    lowerBound = Math.PI / 2;
		}

		try
		{
		    upperBound = Math.toRadians (Double.parseDouble (getUpperBound.getText ()));
		}
		catch (NumberFormatException e)
		{
		    upperBound = Math.PI;
		}

		double Dx = (upperBound - lowerBound) / N;
		int lines = 0;
		for (double i = lowerBound ; i <= upperBound ; i = i + Dx)
		{
		    areaS += Math.abs (Function (userSine, i)); //during the loop, the following two values are just the sum's of the y-values
		    areaC += Math.abs (Function (userCosine, i));
		    lines++;

		    if (lines % (N / 100) == 0)
		    {
			g.setColor (new Color (0, 0, 150));
			g.drawLine (transformX (i), transformY (Function (userSine, i)), transformX (i), transformY (0));
		    }
		    if (lines % (N / 100) == 50)
		    {
			g.setColor (new Color (0, 150, 0));
			g.drawLine (transformX (i), transformY (Function (userCosine, i)), transformX (i), transformY (0));
		    }
		}

		areaS *= Dx; //This calculates the area
		areaC *= Dx;
		areaS = round (areaS);
		areaC = round (areaC);
		g.setColor (text);
		g.drawString ("The area under the sine graph is " + areaS, xOutput, yOutput);
		g.drawString ("The area under the cosine graph is " + areaC, xOutput, yOutput + 15);
		area = false;
	    }

	    if (extrema == true)
	    {
		g.setColor (text);
		g.drawString ("EXTREMA OF SINE", xOutput, yOutput - 15);
		g.drawString ("EXTREMA OF COSINE", xOutput + 150, yOutput - 15);
		zeroesOf (derivativeOf (userSine), g, xOutput, yOutput);
		zeroesOf (derivativeOf (userCosine), g, xOutput + 150, yOutput);
		extrema = false;
	    }

	    if (zeroes == true)
	    {
		g.setColor (text);
		g.drawString ("ZEROES OF SINE", xOutput, yOutput - 15);
		g.drawString ("ZEROES OF COSINE", xOutput + 150, yOutput - 15);
		zeroesOf (userSine, g, xOutput, yOutput);
		zeroesOf (userCosine, g, xOutput + 150, yOutput);
		zeroes = false;
	    }

	    if (points == true)
	    {
		g.setColor (text);
		if (((Math.abs (userSine.k * userSine.xShift - userCosine.k * userCosine.xShift)) / (Math.PI / 2) % 2 == 1 && userSine.yShift == userCosine.yShift && userSine.amplitude == userCosine.amplitude && userSine.k == userCosine.k))  // checking for identical functions
		    g.drawString ("There are infinitely many points of intersection", xOutput, yOutput);
		else
		{
		    int counter = 0;
		    int xDraw = xOutput;
		    for (double i = 0 ; i < 2 * Math.PI ; i = i + INCREMENT)
		    {
			if (diff (userSine, userCosine, i) < TOLERANCE && diff (userSine, userCosine, i) < diff (userSine, userCosine, i + INCREMENT))
			{
			    String s = "";
			    s = s + ("(") + round (Math.toDegrees (i)) + ("°, ") + round (Function (userSine, i)) + (")");
			    g.drawString (s, xDraw, yOutput + 15 * counter);

			    if (yOutput + 15 * counter >= 550)
			    {
				xDraw += 125;
				counter = 0;
				g.drawString (s, xDraw, yOutput + 15 * counter);
			    }
			    counter++;
			    i = i + (0.002);  //moving the value by a small amount to avoid repeated POI
			}
		    }
		    g.setColor (text);
		    g.drawString ("DONE", xOutput, yOutput * 15 + 15);
		}
		points = false;
	    }
	}
	if (polarClick == true)
	{ //These algorithms need to be adjusted for the polar grid.
	    drawPolarGrid (g);
	    for (double theta = 0 ; theta <= 2 * Math.PI ; theta = theta + RESOLUTION)
	    {
		g.setColor (Color.blue);
		int x1, y1, x2, y2;
		x1 = scalePolarX (Function (userSine, theta), theta);
		y1 = scalePolarY (Function (userSine, theta), theta);

		x2 = scalePolarX (Function (userSine, theta + RESOLUTION), theta + RESOLUTION);
		y2 = scalePolarY (Function (userSine, theta + RESOLUTION), theta + RESOLUTION);
		g.drawLine (x1, y1, x2, y2);

		g.setColor (Color.green);
		x1 = scalePolarX (Function (userCosine, theta), theta);
		y1 = scalePolarY (Function (userCosine, theta), theta);

		x2 = scalePolarX (Function (userCosine, theta + RESOLUTION), theta + RESOLUTION);
		y2 = scalePolarY (Function (userCosine, theta + RESOLUTION), theta + RESOLUTION);
		g.drawLine (x1, y1, x2, y2);
	    }

	    if (points == true)
	    {
		g.setColor (text);
		g.drawString ("It is incredibly difficult to find the points of intersection in polar coordinates, ", xOutput, yOutput);
		g.drawString ("because the same point can be described in many different ways.", xOutput, yOutput + 15);
		g.drawString ("Because it's so dang hard, I politely ask for you to do it yourself", xOutput, yOutput + 35);
		points = false;
	    }
	    if (zeroes == true)
	    {
		g.setColor (text);
		g.drawString ("ZEROES OF SINE", xOutput, yOutput - 15);
		zeroesOf (userSine, g, xOutput, yOutput);

		g.drawString ("ZEROES OF COSINE", xOutput + 150, yOutput - 15);
		zeroesOf (userCosine, g, xOutput + 150, yOutput);
		zeroes = false;
	    }
	    if (extrema == true)
	    {
		g.setColor (text);
		g.drawString ("EXTREMA OF SINE", xOutput, yOutput - 15);
		zeroesOf (derivativeOf (userSine), g, xOutput, yOutput);
		g.drawString ("EXTREMA OF COSINE", xOutput + 150, yOutput - 15);
		zeroesOf (derivativeOf (userCosine), g, xOutput + 150, yOutput);
		extrema = false;
	    }
	    if (slope == true)
	    {
		double theta;
		try
		{
		    theta = Math.toRadians (Double.parseDouble (getAngle.getText ()));
		}
		catch (NumberFormatException e)
		{
		    theta = Math.PI / 2;
		}
		//we differentiate using chain rule
		double slopeSine = 0, slopeCosine = 0;
		g.setColor (text);
		if (Math.abs (slopeSine) < Math.pow (10, 10))
		{
		    slopeSine = round (polarSlope (userSine, theta));
		    g.drawString ("The slope of the tangent on sine is is " + round (slopeSine), xOutput, yOutput);
		}
		else
		    g.drawString ("The tangent line on sine is vertical, so it's slope is undefined.", xOutput, yOutput);

		if (Math.abs (slopeCosine) < Math.pow (10, 14))
		{
		    slopeCosine = round (polarSlope (userCosine, theta));
		    g.drawString ("The slope of the tangent on cosine is is " + round (slopeCosine), xOutput, yOutput + 15);
		}
		else
		    g.drawString ("The tangent on cosine is vertical, so it's slope is undefined.", xOutput, yOutput + 15);
		slope = false;
	    }
	    if (area == true)
	    {
		double areaS = 0, areaC = 0;
		double lowerBound, upperBound;
		try
		{
		    lowerBound = Math.toRadians (Double.parseDouble (getLowerBound.getText ()));
		}
		catch (NumberFormatException e)
		{
		    lowerBound = Math.PI / 2;
		}
		try
		{
		    upperBound = Math.toRadians (Double.parseDouble (getUpperBound.getText ()));
		}
		catch (NumberFormatException e)
		{
		    upperBound = Math.PI;
		}

		double Dx = (upperBound - lowerBound) / N;
		int lines = 0;
		for (double i = lowerBound ; i <= upperBound ; i = i + Dx)
		{
		    areaS += 0.5 * (Math.pow (Math.abs (Function (userSine, i)), 2)); //during the loop, the following two values are just the sum's of the y-values
		    areaC += 0.5 * Math.pow (Math.abs (Function (userCosine, i)), 2);

		    if (lines % 500 == 0)
		    {
			g.setColor (new Color (0, 0, 150));
			g.drawLine (scalePolarX (Function (userSine, i), i), scalePolarY (Function (userSine, i), i), scalePolarX (0, 0), scalePolarY (0, 0));
		    }
		    if (lines % 500 == 250)
		    {
			g.setColor (new Color (0, 150, 0));
			g.drawLine (scalePolarX (Function (userCosine, i), i), scalePolarY (Function (userCosine, i), i), scalePolarX (0, 0), scalePolarY (0, 0));
		    }
		    lines++;
		}
		g.setColor (Color.black);
		areaS *= Dx; //This calculates the area
		areaC *= Dx;
		areaS = round (areaS);
		areaC = round (areaC);
		g.setColor (text);
		g.drawString ("The area under the polar sine graph is " + areaS, xOutput, yOutput);
		g.drawString ("The area under the polar cosine graph is " + areaC, xOutput, yOutput + 15);
		area = false;
		lines++;
	    }
	}
	g.setColor (text);
	g.drawString ("What do you require next of me, oh wise and powerful user?", xOutput, yOutput - 45);
    }


    public double polarSlope (graph f, double t)
    {
	double num = Math.sin (t) * Function (derivativeOf (f), t) + Math.cos (t) * Function (f, t);
	double den = Math.cos (t) * Function (derivativeOf (f), t) - Math.sin (t) * Function (f, t);
	double s;
	s = num / den;
	return s;
    }


    public int scalePolarY (double r, double theta)
    {
	return transformY ((r * Math.sin (theta)));
    }


    public int scalePolarX (double r, double theta)
    {
	return 500 - (transformY (r * Math.cos (theta))); //b/c the scale on the y-axis will be used as the radial scaling.
    }


    public void drawCircleCenter (Graphics g, int radius, int x, int y)
    {
	g.drawOval (x - radius, y - radius, 2 * radius, 2 * radius);
    }


    public void drawPolarGrid (Graphics g)
    {
	g.setColor (gridLines);
	for (int i = 10 ; i < 160 ; i = i + 20)
	    drawCircleCenter (g, i, scalePolarX (0, 0), scalePolarY (0, 0));
	for (double theta = 0 ; theta < 2 * Math.PI ; theta += Math.PI / 6)
	    g.drawLine (scalePolarX (3, theta), scalePolarY (3, theta), scalePolarX (-3, theta), scalePolarY (-3, theta));
    }



    public double diff (graph g1, graph g2, double i)
    {
	double y = Function (g1, i) - Function (g2, i);
	return (Math.abs (y));
    }


    public void zeroesOf (graph a, Graphics g, int xOut, int yOut)
    {
	double jump = INCREMENT;
	int counter = 0;
	int xDraw = xOut;
	boolean zeroesExist;
	zeroesExist = (Math.abs (a.amplitude) >= Math.abs (a.yShift));
	if (zeroesExist)
	{
	    for (double i = 0 ; i <= Math.PI * 2 + INCREMENT ; i = i + jump)
	    {
		if (Math.abs (Function (a, i)) < TOLERANCE && Math.abs (Function (a, i)) < Math.abs (Function (a, i + INCREMENT)))
		{

		    if (yOut + 15 * counter < 600)
		    {
			g.setColor (text);
			g.drawString (round (Math.toDegrees (i)) + "°", xDraw, yOut + 15 * counter);
			counter++;
		    }
		    else
		    {
			xDraw += 45;
			counter = 0;
			g.setColor (text);
			g.drawString (round (Math.toDegrees (i)) + "°", xDraw, yOut + 15 * counter);
			counter++;
		    }
		    jump = Math.PI / (2 * a.k);  //so that zeroes aren't repeated, the program skips over by a quarter of the period
		} //however, we ensure to be 5 increments short of the zero, because of potential rounding errors
	    }
	}

	else
	{
	    g.setColor (text);
	    g.drawString ("No zeroes exist", xDraw, yOut);
	}
    }


    public double round (double x)
    {
	double base = Math.pow (10, DECIMAL_PLACES);
	return (double) (Math.round (x * base) / base);
    }


    public graph derivativeOf (graph g)
    {
	graph gPrime = new graph ();
	gPrime.yShift = 0;
	gPrime.k = g.k;
	gPrime.xShift = g.xShift;
	gPrime.amplitude = g.amplitude * g.k;
	if (g.type.equals (SINE))
	    gPrime.type = COSINE;
	else if (g.type.equals (COSINE))
	{
	    gPrime.type = SINE;
	    gPrime.amplitude *= -1;
	}
	return gPrime;
    }


    public boolean action (Event evt, Object o)
    {
	if (evt.target == z)
	    zeroes = true;
	if (evt.target == s)
	    slope = true;
	if (evt.target == p)
	    points = true;
	if (evt.target == a)
	    area = true;
	if (evt.target == m)
	    extrema = true;
	if (evt.target == update)
	    getInfo ();
	if (evt.target == polar)
	    polarClick = true;
	if (evt.target == rect)
	    polarClick = false;
	repaint ();
	return true;
    }


    public void getInfo ()  //all the textfields have been guarded with a try and catch so that there are default options no matter what.
    {
	try
	{
	    userSine.amplitude = Double.parseDouble (userSine_AmplitudeInfo.getText ());
	}
	catch (NumberFormatException e)
	{
	    userSine.amplitude = 1;
	}
	try
	{
	    userSine.k = 2 * Math.PI / Math.toRadians (Double.parseDouble (userSine_kInfo.getText ()));
	}
	catch (NumberFormatException e)
	{
	    userSine.k = 1;

	}
	try
	{
	    userSine.xShift = -Math.toRadians (Double.parseDouble (userSine_xShiftInfo.getText ()));
	}
	catch (NumberFormatException e)
	{
	    userSine.xShift = 0;

	}
	try
	{
	    userSine.yShift = Double.parseDouble (userSine_yShiftInfo.getText ());
	}
	catch (NumberFormatException e)
	{
	    userSine.yShift = 0;

	}
	try
	{
	    userCosine.amplitude = Double.parseDouble (userCosine_AmplitudeInfo.getText ());
	}
	catch (NumberFormatException e)
	{
	    userCosine.amplitude = 1;

	}
	try
	{
	    userCosine.k = 2 * Math.PI / Math.toRadians (Double.parseDouble (userCosine_kInfo.getText ()));
	}
	catch (NumberFormatException e)
	{
	    userCosine.k = 1;

	}
	try
	{
	    userCosine.xShift = -Math.toRadians (Double.parseDouble (userCosine_xShiftInfo.getText ()));
	}
	catch (NumberFormatException e)
	{
	    userCosine.xShift = 0;

	}
	try
	{
	    userCosine.yShift = Double.parseDouble (userCosine_yShiftInfo.getText ());
	}
	catch (NumberFormatException e)
	{
	    userCosine.yShift = 0;
	}
	inputed = true;
    }



    public double Function (graph f, double x)
    {
	double y;
	if (f.type.equals (SINE))
	    y = f.yShift + f.amplitude * (Math.sin (f.k * (x - f.xShift)));
	else
	    y = f.yShift + f.amplitude * (Math.cos (f.k * (x - f.xShift)));
	return y;
    }


    public void drawGrid (Graphics g)
    {
	g.setColor (gridLines);
	g.drawLine (transformX (0), transformY (0), transformX (Math.PI * 2), transformY (0)); //Drawing x-axis
	for (int i = 3 ; i <= 36 ; i += 3)
	{
	    g.setColor (gridLines);
	    g.drawLine (transformX (Math.toRadians (i * 10)), transformY (-3), transformX (Math.toRadians (i * 10)), transformY (3));
	    g.setColor (text);
	    g.drawString (" " + i * 10 + "°", transformX (Math.toRadians (i * 10)) - 5, transformY (-0.25)); //labels on the x-axis
	    g.drawLine (transformX (Math.toRadians (i * 10)), transformY (0.05), transformX (Math.toRadians (i * 10)), transformY (-0.05)); //ticks on the x-axis
	}
	g.drawLine (transformX (0), transformY (3), transformX (0), transformY (-3)); //Drawing y-axis
	for (double i = 3 ; i >= -3 ; i--)
	{
	    if (i == 0)
		i--;
	    g.setColor (gridLines);
	    g.drawLine (transformX (0), transformY (i), transformX (6.28), transformY (i));
	    g.setColor (text);
	    g.drawLine (transformX (0), transformY (i), transformX (0) + TICK_WIDTH, transformY (i)); //Ticks on the y-axis
	    g.drawString ("  " + i, transformX (0) + TICK_WIDTH, transformY (i) + 5); //labels on the y-axis
	}
    }


    public int transformX (double x)    //these methods will transform the numbers into console coordinates
    {
	return (int) (X_ORIGIN + (550) * (x) / (2 * Math.PI)); //because these methods are so well-defined, if I call these two methods everytime something is drawn, I don't need to know the precise scale
    }


    public int transformY (double y)
    {
	return Y_ORIGIN - (int) (y * 50);
    }
} // CurvePlotterApplet class





