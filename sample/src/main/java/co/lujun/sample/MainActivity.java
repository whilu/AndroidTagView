package co.lujun.sample;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import co.lujun.androidtagview.ContainerLayout;

public class MainActivity extends AppCompatActivity {

    private ContainerLayout mContainerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mContainerLayout = (ContainerLayout) findViewById(R.id.containerLayout);
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0) {
                list.add("hello, " + i);
            }else if (i % 3 == 0){
                list.add("test, " + i);
            }else {
                list.add("wojuede" + i);
            }
        }
        mContainerLayout.setTags(list);
    }
}
