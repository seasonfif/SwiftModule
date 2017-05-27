package com.seasonfif.moduleb;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ModuleBActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_module_b);
    findViewById(R.id.tv).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Intent it = new Intent();
        //it.setClassName(ModuleAActivity.this, "com.seasonfif.modulea.ModuleAActivity");
        it.setAction(Intent.ACTION_VIEW);
        it.setData(Uri.parse("modulea://activity"));
        startActivity(it);
      }
    });
  }
}
