package co.lujun.sample;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;

public class MainActivity extends AppCompatActivity {

    private TagContainerLayout mTagContainerLayout1, mTagContainerLayout2, mTagContainerLayout3, mTagContainerLayout4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        List<String> list1 = new ArrayList<String>();
        list1.add("Java");
        list1.add("C++");
        list1.add("Python");
        list1.add("Swift");
        list1.add("你好，这是一个TAG。你好，这是一个TAG。你好，这是一个TAG。你好，这是一个TAG。");
        list1.add("PHP");
        list1.add("JavaScript");
        list1.add("Html");
        list1.add("Welcome to use AndroidTagView!");

        List<String> list2 = new ArrayList<String>();
        list2.add("China");
        list2.add("USA");
        list2.add("Austria");
        list2.add("Japan");
        list2.add("Sudan");
        list2.add("Spain");
        list2.add("UK");
        list2.add("Germany");
        list2.add("Niger");
        list2.add("Poland");
        list2.add("Norway");
        list2.add("Uruguay");
        list2.add("Brazil");

        String[] list3 = new String[]{"Persian", "波斯语", "فارسی", "Hello", "你好", "سلام"};
        String[] list4 = new String[]{"Adele", "Whitney Houston"};

        mTagContainerLayout1 = (TagContainerLayout) findViewById(R.id.tagcontainerLayout1);
        mTagContainerLayout2 = (TagContainerLayout) findViewById(R.id.tagcontainerLayout2);
        mTagContainerLayout3 = (TagContainerLayout) findViewById(R.id.tagcontainerLayout3);
        mTagContainerLayout4 = (TagContainerLayout) findViewById(R.id.tagcontainerLayout4);

        // Set custom click listener
        mTagContainerLayout1.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {
                Toast.makeText(MainActivity.this, "click-position:" + position + ", text:" + text,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTagLongClick(final int position, String text) {
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("long click")
                        .setMessage("You will delete this tag!")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mTagContainerLayout1.removeTag(position);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();
                dialog.show();
            }
        });

        // Custom settings
//        mTagContainerLayout1.setTagMaxLength(4);

        // Set the custom theme
//        mTagContainerLayout1.setTheme(ColorFactory.PURE_CYAN);

        // If you want to use your colors for TagView, remember set the theme with ColorFactory.NONE
//        mTagContainerLayout1.setTheme(ColorFactory.NONE);
//        mTagContainerLayout1.setTagBackgroundColor(Color.TRANSPARENT);
//        mTagContainerLayout1.setTagTextDirection(View.TEXT_DIRECTION_RTL);

        // support typeface
//        Typeface typeface = Typeface.createFromAsset(getAssets(), "iran_sans.ttf");
//        mTagContainerLayout.setTagTypeface(typeface);

        // adjust distance baseline and descent
//        mTagContainerLayout.setTagBdDistance(4.6f);

        // After you set your own attributes for TagView, then set tag(s) or add tag(s)
        mTagContainerLayout1.setTags(list1);
        mTagContainerLayout2.setTags(list2);
        mTagContainerLayout3.setTags(list3);
        mTagContainerLayout4.setTags(list4);

        final EditText text = (EditText) findViewById(R.id.text_tag);
        Button btnAddTag = (Button) findViewById(R.id.btn_add_tag);
        btnAddTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTagContainerLayout1.addTag(text.getText().toString());
                // Add tag in the specified position
//                mTagContainerLayout1.addTag(text.getText().toString(), 4);
            }
        });

        mTagContainerLayout1.setMaxLines(1);
    }
}
