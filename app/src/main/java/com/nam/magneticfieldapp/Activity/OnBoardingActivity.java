package com.nam.magneticfieldapp.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.nam.magneticfieldapp.Adapter.NoteAdapter;
import com.nam.magneticfieldapp.Module.Note;
import com.nam.magneticfieldapp.R;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

import static maes.tech.intentanim.CustomIntent.customType;

public class OnBoardingActivity extends AppCompatActivity implements View.OnClickListener {
    private ViewPager viewPager;
    private TextView tv_skip,tv_next;
    private LinearLayout layout_next;
    private ImageView iv_next;
    private NoteAdapter adapter;
    private List<Note> list=new ArrayList<>();
    private CircleIndicator circleIndicator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_broading);
        setStatusBar();
        createData();
        initView();
        setData();
    }

    private void setData() {
        tv_skip.setOnClickListener(this);
        layout_next.setOnClickListener(this);
        adapter=new NoteAdapter(list,getApplicationContext());
        viewPager.setAdapter(adapter);
        circleIndicator.setViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==list.size()-1){
                    tv_next.setText("START");
                    iv_next.setVisibility(View.GONE);
                }else {
                    tv_next.setText("NEXT");
                    iv_next.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void createData() {
        list.add(new Note(R.drawable.chart,"Giao diện thân thiện","Với chỉ số cường độ từ trường, mức cảm nhận từ trường, 3 tọa đọa hướng từ trường và biểu đồ đường thể hiện từ trường "));
        list.add(new Note(R.drawable.copm,"Xác định đúng hướng","Có thể xác định đúng hướng nhờ vào 3 thành phần X, Y, Z được xác định từ cảm biến. Dò tìm kim loại chính xác trong khoảng cách 30 cm"));
        list.add(new Note(R.drawable.key,"Dò tìm chính xác","Ứng dụng có thể phát hiện kim loại ẩn sâu dưới lớp che phủ: dò tìm đường dây mạch điện trong tường, miếng sắt nhỏ dưới đất, ..."));
    }

    private void initView() {
        viewPager=findViewById(R.id.viewpager_item);
        tv_skip=findViewById(R.id.tv_skip);
        tv_next=findViewById(R.id.tv_next);
        iv_next=findViewById(R.id.iv_next);
        layout_next=findViewById(R.id.layout_next);
        circleIndicator=findViewById(R.id.circle_indi);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_skip:
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                customType(OnBoardingActivity.this, "left-to-right");
                break;
            case R.id.layout_next:
                if(viewPager.getCurrentItem()<list.size()-1){
                    viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
                }else {
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    customType(OnBoardingActivity.this, "left-to-right");
                }
                break;
        }
    }
    private void setStatusBar(){
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);

        }
    }
}
