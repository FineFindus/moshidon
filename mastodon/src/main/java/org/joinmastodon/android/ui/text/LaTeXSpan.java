package org.joinmastodon.android.ui.text;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.style.ReplacementSpan;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import ru.noties.jlatexmath.JLatexMathDrawable;

public class LaTeXSpan extends ReplacementSpan{
	public final String src;
	private final JLatexMathDrawable drawable;
	private static final int TEXT_SIZE=45;

	public LaTeXSpan(String latex, @ColorInt int color){
		this.src=latex;
		//TODO: possible improvement by using just TeXIconBuilder
		drawable=JLatexMathDrawable.builder(src).align(JLatexMathDrawable.ALIGN_CENTER).color(color).textSize(TEXT_SIZE).build();
	}

	@Override
	public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fm){
        if (drawable == null)
            return (int) paint.measureText(text, start, end);

		Rect bounds = drawable.getBounds();
		if (fm != null) {
			int textHeight = fm.descent - fm.ascent;
			int halfDifference = (drawable.getIntrinsicHeight() - textHeight) / 2;
			fm.ascent -= halfDifference;
			fm.top -= halfDifference;
			fm.descent += halfDifference;
			fm.bottom += halfDifference;
		}
		return bounds.right;
    }

	@Override
	public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint){
        if (drawable == null) {
            canvas.drawText(text, start, end, x, y, paint);
        } else {
			//TODO: improve layout,
			//sometimes this can lead to cases, where single variables are not correctly inline with the text baseline
            float centerY = (top + bottom) / 2f;
            float baseline = centerY + (drawable.getIntrinsicHeight() / 2f) - drawable.getBounds().bottom;

            canvas.save();
            canvas.translate(x, baseline);
            drawable.draw(canvas);
            canvas.restore();
        }
    }

}
