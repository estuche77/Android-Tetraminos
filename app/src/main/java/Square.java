import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by estuche on 06/03/18.
 */

public class Square extends View {

    public int i;
    public int j;

    public Square(Context context, int i, int j) {
        super(context);
        this.i = i;
        this.j = j;
    }

    public Square(Context context, @Nullable AttributeSet attrs, int i, int j) {
        super(context, attrs);
        this.i = i;
        this.j = j;
    }

    public Square(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int i, int j) {
        super(context, attrs, defStyleAttr);
        this.i = i;
        this.j = j;
    }

    public Square(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes, int i, int j) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.i = i;
        this.j = j;
    }
}
