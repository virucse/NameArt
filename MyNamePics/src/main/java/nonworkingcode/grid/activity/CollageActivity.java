package nonworkingcode.grid.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.formationapps.nameart.R;
import com.formationapps.nameart.activity.BaseActivity;
import com.formationapps.nameart.helper.AdsHelper;
import com.xiaopo.flying.sticker.StickerView;

import formationapps.helper.stickers.StickerConst;
import formationapps.helper.stickers.StickerFragment;
import nonworkingcode.effects.util.EffectsUtils;
import nonworkingcode.grid.custominterface.ICollageClickListener;
import nonworkingcode.grid.panel.CollagePanel;
import nonworkingcode.grid.util.CollageConst;
import nonworkingcode.grid.util.CollageUtil;
import nonworkingcode.grid.views.CollageNormal;
import nonworkingcode.grid.views.FancyCollage;

/**
 * Created by Caliber Fashion on 12/9/2016.
 */

public class CollageActivity extends BaseActivity implements ICollageClickListener {
    private CollagePanel panel;
    private ImageButton ibSave, ibFlipX, ibFlipY;
    private View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.ib_col_flipX) {
                CollageConst.collageView.getView().setRotationX(
                        CollageConst.collageView.getView().getRotationX() == -180f ? 0 : -180f);
                CollageConst.collageView.getView().invalidate();
            } else if (v.getId() == R.id.ib_col_flipY) {
                CollageConst.collageView.getView().setRotationY(
                        CollageConst.collageView.getView().getRotationY() == -180f ? 0 : -180f);
                CollageConst.collageView.getView().invalidate();
            } else if (v.getId() == R.id.ib_col_save) {
                //StickerConst.invalidateStickers();
                //stickerView.setLocked(true);
                saveButtonClicked(CollageConst.collageView!=null?CollageConst.collageView.getView():null);
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CollageUtil.onCreate(this);
        setContentView(R.layout.collage_activity);

        logAnalyticEvent("COLLAGE_ACTIVITY");

        //load admob adview
        AdsHelper.loadAdmobBanner(this, (LinearLayout) findViewById(R.id.ad_container));
        //AdView adView=(AdView)findViewById(R.id.ad_view);
        //adView.loadAd(new AdRequest.Builder().build());

        CollageConst.init();
        //StickerConst.drawingLayout = null;
        setGrid(CollageConst.collages[CollageConst.getImageSize()][0]);
        this.panel = (CollagePanel) findViewById(R.id.panel_clmenu);
        this.panel.setIOnBackStateChangeListener(new CollagePanel.CollageOnBackStateChangeListener() {
            @Override
            public void canBack(boolean canBack) {
                //CollageUtil.hideAndShow(findViewById(R.id.top_bar_ac), canBack);
            }
        });
        initCollage();

        this.ibSave = (ImageButton) findViewById(R.id.ib_col_save);
        CollageUtil.setImage(this.ibSave, R.mipmap.ic_save);
        this.ibSave.setOnClickListener(buttonClickListener);
        //ibSave.setRotation(-180);

        ibFlipX = (ImageButton) findViewById(R.id.ib_col_flipX);
        CollageUtil.setImage(ibFlipX, R.mipmap.ic_flip_vertical, Color.WHITE);
        ibFlipX.setOnClickListener(buttonClickListener);
        ibFlipX.setVisibility(View.INVISIBLE);

        ibFlipY = (ImageButton) findViewById(R.id.ib_col_flipY);
        CollageUtil.setImage(ibFlipY, R.mipmap.ic_flip_horizontal, Color.WHITE);
        ibFlipY.setOnClickListener(buttonClickListener);
        ibFlipY.setVisibility(View.INVISIBLE);
    }

    private void initCollage() {
        if (CollageConst.collageView != null) {
            int imageCount = CollageConst.collageView.getImageListSize();
            for (int i = 0; i < imageCount; i++) {
                if (CollageConst.collageView != null) {
                    CollageConst.collageView.update(CollageConst.collageBitmaps[i], i);
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CollageUtil.REQ_REPLACE_IMAGE) {
            if (resultCode == RESULT_OK) {
                int replaced = data.getIntExtra(CollageUtil.EXTRA_INDEX, -1);
                //that means image has been changed
            }
        } else if (requestCode == CollageUtil.REQ_EDIT_IMAGE) {
            if (resultCode == RESULT_OK) {
                int index = data.getIntExtra(CollageUtil.EXTRA_INDEX, -1);
                if (index >= 0) {
                    CollageConst.collageView.update(EffectsUtils.getInstance().getBitmap(true, true), index);
                    CollageConst.effectApplied[index] = Boolean.valueOf(true);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (stickerView != null) {
            stickerView.setLocked(false);
        }
    }

    public void onBackPressed() {
        try {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag("text");
            if (fragment != null) {
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                CollageUtil.hideAndShow(findViewById(R.id.top_bar_ac), true);
                StickerConst.invalidateStickers();
                this.panel.resetLinSelector();
                return;
            }
            StickerConst.invalidateStickers();
            CollageConst.collageView.setSelectedAtPosition(false, -1);
            if (!panel.onBackPressed()) {
                //super.onBackPressed();
                showBackpressedDialog();
            }
        } catch (Exception e) {

        }
    }

    public void setGrid(int number) {
        CollageConst.gridIndex = number;
        RelativeLayout rel = (RelativeLayout) findViewById(R.id.rel_colz_work_view);
        if (CollageConst.collageView == null || ((CollageConst.gridIndex < 256 &&
                !(CollageConst.collageView instanceof CollageNormal)) ||
                (CollageConst.gridIndex >= 256 && !(CollageConst.collageView instanceof FancyCollage)))) {
            if (CollageConst.collageView != null) {
                if (StickerConst.drawingLayout != null) {
                    CollageConst.collageView.removeView(StickerConst.drawingLayout);
                }
                rel.removeView(CollageConst.collageView.getView());
            }
            if (CollageConst.gridIndex < 256) {
                CollageConst.collageView = new CollageNormal(this);
            } else {
                CollageConst.collageView = new FancyCollage(this);
            }
            RelativeLayout.LayoutParams layoutparams = new RelativeLayout.LayoutParams(CollageUtil.collageWidth,
                    CollageUtil.collageHeight);
            layoutparams.addRule(13, -1);
            CollageConst.collageView.setLayoutParams(layoutparams);
            rel.addView(CollageConst.collageView.getView());
            CollageConst.collageView.setGridNumber(CollageConst.gridIndex);
            CollageConst.collageView.setCornerRadious(CollageConst.cornerRadius);
            if (CollageConst.backgroundDrawable != null) {
                CollageConst.collageView.setSquareBackground(CollageConst.backgroundDrawable);
            } else if (CollageConst.backgroundColor >= 0) {
                CollageConst.collageView.setBackgroundTopColor(CollageConst.backgroundColor);
            }
            CollageConst.collageView.setCustomBorderId(CollageConst.frameBorderAssetId,
                    CollageConst.frameAssetsPath);
            if (StickerConst.drawingLayout != null) {
                CollageConst.collageView.addView(StickerConst.drawingLayout);
            }
        } else {
            CollageConst.collageView.setGridNumber(CollageConst.gridIndex);
        }
        if (stickerView == null) {
            stickerView = new StickerView(this);
            stickerView.setBackground(new ColorDrawable(Color.TRANSPARENT));
            stickerView.setShowBorder(true);
            stickerView.setShowIcons(true);
            RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(CollageUtil.collageWidth, CollageUtil.collageHeight);
            param.addRule(13, -1);
            stickerView.setLayoutParams(param);
            CollageConst.collageView.addView(stickerView);
            setStickerOperation();
        }
    }

    @Override
    public void onCollageItemClickAt(int i) {

    }

    @Override
    public void onCollageItemTapAt(int index, Point point) {
        if (findViewById(R.id.top_bar_ac).getVisibility() == View.VISIBLE) {
            CollageConst.collageView.setSelectedAtPosition(true, index);
            if (CollageConst.collageBitmaps[index] != null) {
                panel.onCollageEditAt(index);
            }
        }
    }

    @Override
    protected void onDestroy() {
        ((RelativeLayout) findViewById(R.id.rel_colz_work_view)).removeAllViews();
        CollageConst.destroy();
        CollageUtil.onDestroy();
        System.gc();
        super.onDestroy();
    }

    public void showText() {
        //getSupportFragmentManager().beginTransaction().add(R.id.rel_content, new TextFragment(), "text").commit();
    }

    public void showSticker() {
        getSupportFragmentManager().beginTransaction().add(R.id.rel_content, new StickerFragment(), "text").commit();
    }

    protected void onSave(int size, boolean exit) {
        // new SaveProcess().setSize(size).startSave(new C05953(exit));
    }
}
