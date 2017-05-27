package com.seasonfif.swiftmoduledemo;

import android.content.Intent;
import com.seasonfif.modulea.ModuleAActivity;
import com.seasonfif.swiftmodule.annotation.Flags;
import com.seasonfif.swiftmodule.annotation.Key;
import com.seasonfif.swiftmodule.annotation.RequestCode;
import com.seasonfif.swiftmodule.annotation.TargetClass;

/**
 * 创建时间：2017年05月26日17:35 <br>
 * 作者：zhangqiang <br>
 * 描述：
 */

public interface ActivityServices {

  @TargetClass(ModuleAActivity.class)
  @RequestCode(100)
  @Flags(Intent.FLAG_ACTIVITY_NEW_TASK)
  void goToModuleAActivity(@Key("name") String name, @Key("id")int id);

  @TargetClass(ModuleAActivity.class)
  @RequestCode(100)
  @Flags(Intent.FLAG_ACTIVITY_NEW_TASK)
  void goToModuleAActivity(@Key("name") String name);

  void goToModuleBActivity();
}
