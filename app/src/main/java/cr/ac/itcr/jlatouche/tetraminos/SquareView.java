package cr.ac.itcr.jlatouche.tetraminos;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by estuche on 06/03/18.
 */

public class SquareView extends View {

    private int color;

    public SquareView(Context context) {
        super(context);
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
