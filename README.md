# Mini-Paint
This app about how to drawing on Canvas Objects

Alternatif code :
In the current app, the cumulative drawing information is cached in a bitmap. While this is a good solution, it is not the only possible way. How you store your drawing history depends on the app, and your various requirements. For example, if you are drawing shapes, you could save a list of shapes with their location and dimensions. For the MiniPaint app, you could save the path as a Path. Below is the general outline on how to do that, if you want to try it.

Remove all the code for extraCanvas and extraBitmap.
Add variables for the path so far, and the path being drawn currently.
// Path representing the drawing so far
private val drawing = Path()

// Path representing what's currently being drawn
private val curPath = Path()
In onDraw(), instead of drawing the bitmap, draw the stored and current paths.
// Draw the drawing so far
canvas.drawPath(drawing, paint)
// Draw any current squiggle
canvas.drawPath(curPath, paint)
// Draw a frame around the canvas
canvas.drawRect(frame, paint)
In touchUp() , add the current path to the previous path and reset the current path.
// Add the current path to the drawing so far
drawing.addPath(curPath)
// Rewind the current path for the next touch
curPath.reset()
Run your app, and yes, there should be no difference whatsoever.
