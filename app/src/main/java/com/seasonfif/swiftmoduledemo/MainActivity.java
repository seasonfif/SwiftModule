package com.seasonfif.swiftmoduledemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.seasonfif.swiftmodule.SwiftModule;

public class MainActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    findViewById(R.id.tv).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        //Intent it = new Intent(MainActivity.this, ModuleAActivity.class);
        /*Intent it = new Intent();
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        it.setClassName(MainActivity.this, "com.seasonfif.modulea.ModuleAActivity");
        it.setAction(Intent.ACTION_VIEW);
        it.setData(Uri.parse("modulea://activity"));
        startActivity(it);
        //startActivityForResult();
        Bundle bundle = new Bundle();
        it.putExtras(bundle);*/
        SwiftModule.createRouterService(MainActivity.this, ActivityServices.class).goToModuleAActivity("seasonfif", 10);
      }
    });

    findViewById(R.id.intent).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Intent intent = SwiftModule.createRouterService(MainActivity.this, ActivityServices.class).goToModuleOther("seasonfif", 11);
        if (intent == null){

        }
      }
    });

    findViewById(R.id.scheme).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        SwiftModule.createRouterService(MainActivity.this, ActivityServices.class).goToModuleOtherByScheme("seasonfif", 12);
      }
    });

  }
}
