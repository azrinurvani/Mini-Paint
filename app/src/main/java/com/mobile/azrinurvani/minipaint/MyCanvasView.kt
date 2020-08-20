package com.mobile.azrinurvani.minipaint

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.core.content.res.ResourcesCompat

//TODO: Step 2.1 Define a constant for the stroke width.
private const val STROKE_WIDTH = 12f

//TODO: Step 1.3 Create MyCanvasView class with context as parameter and extend by View(context)
class MyCanvasView(context : Context) : View(context) {

    //TODO: Step 1.8 Call them extraCanvas and extraBitmap.
    // These are your bitmap and canvas for caching what has been drawn before.
    private lateinit var extraCanvas: Canvas
    private lateinit var extraBitmap: Bitmap

    //TODO: Step 1.9 Define a class level variable backgroundColor, for the background color of the canvas
    // and initialize it to the colorBackground you defined earlier.
    private val backgroundColor = ResourcesCompat.getColor(resources,R.color.colorBackground,null)

    //TODO: Step 2.2 Define a variable drawColor for holding the color to draw with
    // and initialize it with the colorPaint resource you defined earlier.
    private val drawColor = ResourcesCompat.getColor(resources,R.color.colorPaint,null)

    //TODO: Step 2.3 Add a variable paint for a Paint object and initialize it as follows.
    // Set up the paint with which to draw
    private val paint = Paint().apply {
        color = drawColor
        // Smooths out edges of what is drawn without affecting shape.
        isAntiAlias = true
        // Dithering affects how colors with higher-precision than the device are down-sampled.
        isDither = true
        style = Paint.Style.STROKE // default: FILL
        strokeJoin = Paint.Join.ROUND // default: MITER
        strokeCap = Paint.Cap.ROUND // default: BUTT
        strokeWidth = STROKE_WIDTH //default: Hairline-width (really thin)
    }

    //TODO: Step 2.4 Add a variable path
    // and initialize it with a Path object to store the path that is being drawn when following the user's touch on the screen.
    private var path = Path()

    //TODO: Step 2.6 Add the missing motionTouchEventX and motionTouchEventY variables
    // for caching the x and y coordinates of the current touch event (the MotionEvent coordinates). Initialize them to 0f.
    private var motionTouchEventX = 0f
    private var motionTouchEventY = 0f

    //TODO: Step 2.8 Add variables to cache the latest x and y values. After the user stops moving and lifts their touch,
    // these are the starting point for the next path (the next segment of the line to draw).
    private var currentX = 0f
    private var currentY = 0f

    //TODO: Step 2.10 Add a touchTolerance variable and set it to ViewConfiguration.get(context).scaledTouchSlop.
    private val touchTolerance = ViewConfiguration.get(context).scaledTouchSlop

    //TODO: Step 3.1 Add a variable called frame that holds a Rect object.
    private lateinit var frame : Rect

    //TODO: Step 1.10 Override the onSizeChanged() method
    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)
        //TODO: Step 1.13 a new bitmap and canvas are created every time the function executes.
        // You need a new bitmap, because the size has changed. However, this is a memory leak, leaving the old bitmaps around.
        // To fix this, recycle extraBitmap before creating the next one.
        if (::extraBitmap.isInitialized) extraBitmap.recycle()

        //TODO: Step 1.11 Inside onSizeChanged(), create an instance of Bitmap with the new width and height,
        // which are the screen size, and assign it to extraBitmap also for extraCanvas
        extraBitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888) //ARGB_8888 stores each color in 4 bytes and is recommended.
        extraCanvas = Canvas(extraBitmap)

        //TODO: Step 1.12 Specify the background color in which to fill extraCanvas.
        extraCanvas.drawColor(backgroundColor)

        //TODO: Step 3.2 Add code to create the Rect that will be used for the frame, using the new dimensions and the inset.
        val inset = 40
        frame = Rect(inset,inset,width-inset,height-inset)
    }

    //TODO: Step 1.13 Override onDraw() and draw the contents of the cached extraBitmap on the canvas associated with the view.
    // The drawBitmap() Canvas method comes in several versions. In this code, you provide the bitmap,
    // the x and y coordinates (in pixels) of the top left corner, and null for the Paint, as you'll set that later.
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawBitmap(extraBitmap,0f,0f,null)

        //TODO: Step 3.3 After drawing the bitmap, draw a rectangle.
        // Draw a frame around the canvas.
        canvas?.drawRect(frame,paint)
    }

    //TODO: Step 2.5 Override the onTouchEvent() method to cache the x and y coordinates of the passed in event
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        motionTouchEventX = event?.x!!
        motionTouchEventY = event?.y

        when(event?.action){
            MotionEvent.ACTION_DOWN -> touchStart() //touching down on the screen
            MotionEvent.ACTION_MOVE -> touchMove()// moving on the screen
            MotionEvent.ACTION_UP -> touchUp()//releasing touch on the screen
        }
        return true

    }

    //TODO: Step 2.7 Create stubs for the three functions touchStart(), touchMove(), and touchUp().
    private fun touchStart() {
        //TODO: Step 2.9 Implement the touchStart() method as follows. Reset the path,
        // move to the x-y coordinates of the touch event (motionTouchEventX and motionTouchEventY)
        // and assign currentX and currentY to that value.
        path.reset()
        path.moveTo(motionTouchEventX,motionTouchEventY)
        currentX = motionTouchEventX
        currentY = motionTouchEventY

    }

    private fun touchMove() {
        //TODO: Step 2.11 Calculate the traveled distance (dx, dy), create a curve between the two points and store it in path,
        // update the running currentX and currentY tally, and draw the path.
        // Then call invalidate() to force redrawing of the screen with the updated path.
        val dx = Math.abs(motionTouchEventX - currentX)
        val dy = Math.abs(motionTouchEventY - currentY)
        if (dx >= touchTolerance || dy >= touchTolerance){
            // QuadTo() adds a quadratic bezier from the last point,
            // approaching control point (x1,y1), and ending at (x2,y2).
            path.quadTo(currentX,currentY,(motionTouchEventX + currentX)/2,(motionTouchEventY+currentY)/2)
            currentX = motionTouchEventX
            currentY = motionTouchEventY
            // Draw the path in the extra bitmap to cache it.
            extraCanvas.drawPath(path,paint)
        }
        invalidate() //Call invalidate() to (eventually call onDraw() and) redraw the view.
    }

    private fun touchUp() {
        //TODO: Step 2.12 Reset the path so it doesn't get drawn again.
        path.reset()
    }
}