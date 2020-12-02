package com.nam.magneticfieldapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.nam.magneticfieldapp.Module.Note;
import com.nam.magneticfieldapp.R;

import java.util.List;

public class NoteAdapter extends PagerAdapter {
    private List<Note> list;
    private Context context;
    private LayoutInflater layoutInflater;
    public NoteAdapter(List<Note> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_onboarding, container, false);
        ImageView imageView=view.findViewById(R.id.iv_note);
        TextView title=view.findViewById(R.id.tv_tittle);
        TextView content=view.findViewById(R.id.tv_content);
        imageView.setImageResource(list.get(position).getImage());
        title.setText(list.get(position).getTitle());
        content.setText(list.get(position).getContent());
        container.addView(view,0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
