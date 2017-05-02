package com.example.deepak.radio;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

/**
 * Created by deepak on 11/19/2015.
 */
public class Bluer {

   Context activity;

    public Bluer(Context mContext) {
        activity=mContext;
    }

    public Bitmap blur(Bitmap image) {
        if (null == image) return null;

        Bitmap outputBitmap = Bitmap.createBitmap(image);
        final RenderScript renderScript = RenderScript.create(activity);
        Allocation tmpIn = Allocation.createFromBitmap(renderScript, image);
        Allocation tmpOut = Allocation.createFromBitmap(renderScript, outputBitmap);

        //Intrinsic Gausian blur filter
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        theIntrinsic.setRadius(25f);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);
        return outputBitmap;
    }
    public Bitmap blur1(Bitmap bkg) {

        float scaleFactor = 15;
        int radius = 20;
        int inputWidth = bkg.getWidth();
        int inputHeight = bkg.getHeight();

        Bitmap overlay = Bitmap.createBitmap((int)(inputWidth/scaleFactor),(int)(inputHeight/scaleFactor), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(overlay);

        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);

        canvas.drawBitmap(bkg, 0, 0, paint);

        //  bkg.recycle();
        overlay = FastBlur.doBlur(overlay, radius, true);
        return getResizedBitmap(overlay, inputWidth, inputHeight, true);

    }


    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth, boolean willDelete) {

        int width = bm.getWidth();

        int height = bm.getHeight();

        float scaleWidth = ((float) newWidth) / width;

        float scaleHeight = ((float) newHeight) / height;

        // CREATE A MATRIX FOR THE MANIPULATION

        Matrix matrix = new Matrix();

        // RESIZE THE BIT MAP

        matrix.postScale(scaleWidth, scaleHeight);

        // RECREATE THE NEW BITMAP

        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);

               if(willDelete)
                 bm.recycle();

        return resizedBitmap;
    }
}
