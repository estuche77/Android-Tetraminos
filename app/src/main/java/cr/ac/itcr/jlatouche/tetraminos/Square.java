package cr.ac.itcr.jlatouche.tetraminos;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by estuche on 06/03/18.
 */

public class Square extends View {

    private int color;

    public Square(Context context, int i, int j) {
        super(context);
    }

    public Square(Context context, @Nullable AttributeSet attrs, int i, int j) {
        super(context, attrs);
    }

    public Square(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int i, int j) {
        super(context, attrs, defStyleAttr);
    }

    public Square(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes, int i, int j) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
        this.color = color;
    }

    public int getColor() {
        return color;
    }
}
