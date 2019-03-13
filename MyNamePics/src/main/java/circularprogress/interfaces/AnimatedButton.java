package circularprogress.interfaces;

/**
 * Created by hinovamobile on 23/12/16.
 */

public interface AnimatedButton {
    void startAnimation();

    void revertAnimation();

    void revertAnimation(final OnAnimationEndListener onAnimationEndListener);

    void dispose();
}
