package nonworkingcode.brusheffects;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.formationapps.nameart.R;
import com.formationapps.nameart.activity.BaseActivity;

import yuku.ambilwarna.AmbilWarnaDialog;

/**
 * Created by caliber fashion on 11/2/2017.
 */

public class BrushSetting extends Dialog {
    public BrushSetting(@NonNull Context context) {
        super(context);
        init();
    }

    public BrushSetting(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        init();
    }

    protected BrushSetting(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    private void init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        setContentView(R.layout.brush_setting);
        RecyclerView rv = (RecyclerView) this.findViewById(R.id.recycler_brush_setting);
        rv.setHasFixedSize(true);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(lm);
        //rv.addItemDecoration(new DividerItemDecoration(getContext(),LinearLayoutManager.VERTICAL));
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(new MyAdapter(BaseActivity.brushThumb));

    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private int[] BRUSH_ICON;
        private LoadBrush mLoadBrush;

        public MyAdapter(int[] brushIcon) {
            BRUSH_ICON = brushIcon;
            mLoadBrush = LoadBrush.getInstanse(getContext());
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.brush_adapter_item, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            holder.brushIcon.setImageResource(BRUSH_ICON[position]);

            holder.mSwitch.setTag("" + position);
            String key = mLoadBrush.getBrushEffect(position).getFileName();
            final boolean randomColor = BrushPref.getIsBrushRandomColor(getContext(), key);
            holder.mSwitch.setChecked(randomColor);
            holder.mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int pos = Integer.parseInt("" + buttonView.getTag());
                    String key = mLoadBrush.getBrushEffect(pos).getFileName();
                    if (isChecked) {
                        BrushPref.setIsBrushRandomColor(getContext(), key, true);
                        holder.selectColor.getChildAt(holder.selectColor.getChildCount() - 1)
                                .setVisibility(View.VISIBLE);
                    } else {
                        BrushPref.setIsBrushRandomColor(getContext(), key, false);
                        holder.selectColor.getChildAt(holder.selectColor.getChildCount() - 1)
                                .setVisibility(View.INVISIBLE);
                    }
                }
            });

            holder.selectColor.setTag("" + position);
            holder.selectColor.getChildAt(0).setBackgroundColor(BrushPref.getColor(getContext(), key));
            if (randomColor) {
                holder.selectColor.getChildAt(holder.selectColor.getChildCount() - 1).setVisibility(View.VISIBLE);
            } else {
                holder.selectColor.getChildAt(holder.selectColor.getChildCount() - 1).setVisibility(View.INVISIBLE);
            }
            holder.selectColor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int pos = Integer.parseInt("" + v.getTag());
                    final String key = mLoadBrush.getBrushEffect(pos).getFileName();
                    if (BrushPref.getIsBrushRandomColor(getContext(), key)) {
                        Toast.makeText(getContext(), "Please off Auto Color first...", Toast.LENGTH_LONG).show();
                        return;
                    }
                    int color = BrushPref.getColor(getContext(), key);
                    new AmbilWarnaDialog(getContext(), color, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                        @Override
                        public void onCancel(AmbilWarnaDialog dialog) {
                        }

                        @Override
                        public void onOk(AmbilWarnaDialog dialog, int color) {
                            BrushPref.setColor(getContext(), key, color);
                            holder.selectColor.getChildAt(0).setBackgroundColor(color);
                        }
                    }).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return BRUSH_ICON.length;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            public ImageView brushIcon;
            public SwitchCompat mSwitch;
            public RelativeLayout selectColor;

            public MyViewHolder(View itemView) {
                super(itemView);
                brushIcon = (ImageView) itemView.findViewById(R.id.imageview_brush_setting_item);
                mSwitch = (SwitchCompat) itemView.findViewById(R.id.switch_setting_item);
                selectColor = (RelativeLayout) itemView.findViewById(R.id.select_c_brush_sett_item);
            }
        }
    }
}
