package nonworkingcode.grid.custominterface;

import nonworkingcode.grid.background.IBgResource;

public interface IOnBackgroundChangeListener {
    void onBgBackOnClick(int i);

    void onBgResourceChanged(int i, IBgResource iBgResource);

    void onBgResourceChangedByPressItem(int i, IBgResource iBgResource);

    void onBgSeekbarChanged(int i, float f);
}
