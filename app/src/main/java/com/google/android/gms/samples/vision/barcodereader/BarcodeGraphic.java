/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.android.gms.samples.vision.barcodereader;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.nfc.Tag;
import android.content.Context;
import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.samples.vision.barcodereader.ui.camera.GraphicOverlay;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.text.Text;

/**
 * Graphic instance for rendering barcode position, size, and ID within an associated graphic
 * overlay view.
 */
public class BarcodeGraphic extends GraphicOverlay.Graphic {

    private int mId;

    private static final int COLOR_CHOICES[] = {    //색 선택 파트
            Color.YELLOW,       //주변
            Color.GREEN,        //타겟
            Color.BLACK           //바깥
    };

    private static int mCurrentColorIndex = 0;

    private Paint mRectPaint;
    private Paint mRectPaintT;
    private Paint mRectPaintX;
    private Paint mTextPaint;
    private volatile Barcode mBarcode;

    BarcodeGraphic(GraphicOverlay overlay) {        //1번째
        super(overlay);

        mCurrentColorIndex = (mCurrentColorIndex + 1) % COLOR_CHOICES.length;
        final int selectedColor = COLOR_CHOICES[0];
        final int selectedColorT = COLOR_CHOICES[1];
        final int selectedColorX = COLOR_CHOICES[2];

        mRectPaint = new Paint();
        mRectPaint.setColor(selectedColor);
        mRectPaint.setStyle(Paint.Style.FILL);
        mRectPaint.setStrokeWidth(8.0f);

        mRectPaintT = new Paint();
        mRectPaintT.setColor(selectedColorT);
        mRectPaintT.setStyle(Paint.Style.FILL);
        mRectPaintT.setStrokeWidth(8.0f);

        mRectPaintX = new Paint();
        mRectPaintX.setColor(selectedColorX);
        mRectPaintX.setStyle(Paint.Style.FILL);
        mRectPaintX.setStrokeWidth(8.0f);

        mTextPaint = new Paint();
        mTextPaint.setColor(selectedColor);
        mTextPaint.setTextSize(80.0f);
        //Log.e("바코드: ",BarcodeCaptureActivity.BarcodeObject);
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public Barcode getBarcode() {
        Log.e("겟바코드: ","");
        return mBarcode;
    }

    void updateItem(Barcode barcode) {                 //3번째
        mBarcode = barcode;
        postInvalidate();
        Log.e("update item",barcode.displayValue);
    }

    @Override
    public void draw(Canvas canvas) {               //5번째
        Barcode barcode = mBarcode;
        final String value = barcode.rawValue;

            // Draws the bounding box around the barcode.

        if(value.equals("타겟")){
            Log.e("타겟 잡음",barcode.displayValue);
            RectF rect = new RectF(barcode.getBoundingBox());
            rect.left = translateX(rect.left)-20.0f;
            rect.top = translateY(rect.top)-20.0f;
            rect.right = translateX(rect.right)+20.0f;
            rect.bottom = translateY(rect.bottom)+20.0f;

            canvas.drawRect(rect, mRectPaintT);
//            canvas.drawText(barcode.rawValue, rect.centerX(), rect.centerY(), mTextPaint);
        }

        else if(value.equals("주변타겟")){
            Log.e("주변 타겟 잡음",barcode.displayValue);
            RectF rect = new RectF(barcode.getBoundingBox());
            rect.left = translateX(rect.left);
            rect.top = translateY(rect.top);
            rect.right = translateX(rect.right);
            rect.bottom = translateY(rect.bottom);

            canvas.drawRect(rect, mRectPaint);
//            canvas.drawText(barcode.rawValue, rect.centerX(), rect.centerY(), mTextPaint);//
        }

        else {
            Log.e("타겟 바깥 잡음", barcode.displayValue);
            RectF rect = new RectF(barcode.getBoundingBox());
            rect.left = translateX(rect.left);
            rect.top = translateY(rect.top);
            rect.right = translateX(rect.right);
            rect.bottom = translateY(rect.bottom);

            canvas.drawRect(rect, mRectPaintX);
        }


        if (barcode == null) {//
            return;
        }
    }
}
