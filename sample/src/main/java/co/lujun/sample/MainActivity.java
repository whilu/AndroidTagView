package co.lujun.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import co.lujun.androidtagview.ContainerLayout;
import co.lujun.androidtagview.TagView;

public class MainActivity extends AppCompatActivity {

    private ContainerLayout mContainerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mContainerLayout = (ContainerLayout) findViewById(R.id.containerLayout);
        mContainerLayout.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {
                Toast.makeText(MainActivity.this, "position:" + position + ", text:" + text,
                        Toast.LENGTH_SHORT).show();
            }
        });
        List<String> list = new ArrayList<String>();
        list.add("Java");
        list.add("C/C++");
        list.add("Python");
        list.add("Swift");
        list.add("你好，这是一个TAG示例。你好，这是一个TAG示例。你好，这是一个TAG示例。你好，这是一个TAG示例。");
        list.add("PHP");
        list.add("Python");
        list.add("JavaScript");
        list.add("Html");
        list.add("Hello, this is a TAG example.");
        list.add("Welcome to use AndroidTagView!");
        mContainerLayout.setTags(list);

        Button btnAddTag = (Button) findViewById(R.id.btn_add_tag);
        btnAddTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContainerLayout.addTag("This is a TAG u added!");
//                mContainerLayout.addTag("This is a TAG u added!", 4);
            }
        });
    }
}
