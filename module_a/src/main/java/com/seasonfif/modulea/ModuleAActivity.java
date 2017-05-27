package com.seasonfif.modulea;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ModuleAActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_module_a);

    setText();

    findViewById(R.id.tv).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Intent it = new Intent();
        //it.setClassName(ModuleAActivity.this, "com.seasonfif.moduleb.ModuleBActivity");
        it.setAction(Intent.ACTION_VIEW);
        it.setData(Uri.parse("moduleb://activity"));
        startActivity(it);
      }
    });
  }

  private void setText() {
    TextView tv = (TextView) findViewById(R.id.tv1);
    StringBuilder sb = new StringBuilder();
    Bundle bundle = getIntent().getExtras();
    if (null != bundle){
      sb.append(bundle.getString("name")).append("\n").append(bundle.getInt("id"));
      tv.setText(sb.toString());
    }
  }
}
