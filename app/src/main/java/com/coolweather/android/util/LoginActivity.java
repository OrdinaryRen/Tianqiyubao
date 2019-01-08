package com.coolweather.android.util;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.coolweather.android.MainActivity;
import com.coolweather.android.R;
import com.coolweather.android.db.SqliteDB;
import com.coolweather.android.db.User;

/**
 * Created by admin on 2017/11/6.
 */

public class LoginActivity extends AppCompatActivity {
    private TextView reg;
    private Button login ;
    private EditText editText1;
    private EditText editText2;
    private Button clearButton1;
    private Button clearButton2;
    private TextView state;
    private SharedPreferences pref ;
    private SharedPreferences.Editor editor ;
    private CheckBox rememberpass;
    private CheckBox checkbox ;

    @Override

    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar()!=null)
            getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        pref = PreferenceManager.getDefaultSharedPreferences(this);//获取到SharedPreferences对象
        rememberpass = (CheckBox)findViewById(R.id.remember_pass); //获取复选框
        state= (TextView) findViewById(R.id.state);
        reg= (TextView)findViewById(R.id.reg);
                editText1 = (EditText) findViewById(R.id.editText1);
                editText2 = (EditText) findViewById(R.id.editText2);
                clearButton1 = (Button) findViewById(R.id.button2);
                clearButton2 = (Button) findViewById(R.id.button3);
                editText1.setOnFocusChangeListener(new EditTextListener(clearButton1));
                editText2.setOnFocusChangeListener(new EditTextListener(clearButton2));
                clearButton1.setOnClickListener(new ClearButtonListener());
                clearButton2.setOnClickListener(new ClearButtonListener());

                // 对EditText进行编辑监听
                editText1.addTextChangedListener(new MyEditTextWatcher(editText1));
                editText2.addTextChangedListener(new MyEditTextWatcher(editText2));
                login = (Button) findViewById(R.id.button1);
        /**
         * 密文切换
         */
        checkbox = (CheckBox) findViewById(R.id.checkBox1);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //如果选中，显示密码
                    editText2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //否则隐藏密码
                    editText2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        /**
         * 注册代码
         */
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=editText1.getText().toString().trim();
                String pass=editText2.getText().toString().trim();
                User user=new User();
                user.setUsername(name);
                user.setUserpwd(pass);
                if (name.equals("")){
                    state.setText("用户名不能为空");
                }else  if (pass.equals("")) {
                    state.setText("密码不能为空");
                }
                else {
                    int result = SqliteDB.getInstance(getApplicationContext()).saveUser(user);
                    if (result == 1) {
                        state.setText("注册成功！");
                    } else if (result == -1) {
                        state.setText("用户名已经存在！");
                    } else {
                        state.setText("！");
                    }
                }
            }
        });

        boolean isRemember = pref.getBoolean("remember_password",false);//利用getBoolean（）来获取remember_password这个键对应的值，，一开始是false
        //将账号和密码设置到文本框中
        if(isRemember){
            String account = pref.getString("account","");
            String password = pref.getString("password","");
            editText1.setText(account);
            editText2.setText(password);
            rememberpass.setChecked(true);
        }
        /**
         * 登录代码
         */
        login.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View v) {
                                         String name = editText1.getText().toString().trim();
                                         String pass = editText2.getText().toString().trim();
                                         int result = SqliteDB.getInstance(getApplicationContext()).Quer(pass, name);
                                         if (result == 1) {
                                             editor = pref.edit() ;
                                             if (rememberpass.isChecked()){//是否勾选复选框
                                                 editor.putBoolean("remember_password",true);
                                                 editor.putString("account",name);
                                                 editor.putString("password",pass);
                                             }else{
                                                 editor.clear();//清除数据
                                             }
                                             editor.apply();//提交数据
                                             state.setText("登录成功！");
                                             Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                             startActivity(intent);
                                             finish();
                                         } else if (result == 0) {
                                             state.setText("用户名不存在！");

                                         } else if (result == -1) {
                                             state.setText("密码错误！");
                                         }
                                     }
                                 });
            }

            class MyEditTextWatcher implements TextWatcher {
                private CharSequence temp;
                private EditText editText;

                public MyEditTextWatcher(EditText editText) {
                    this.editText = editText;
                }

                @Override
                // int start开始的位置, int count被改变的旧内容数, int after改变后的内容数量
                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {
                    // 这里的s表示改变之前的内容，通常start和count组合，可以在s中读取本次改变字段中被改变的内容。而after表示改变后新的内容的数量。
                }

                @Override
                // int start开始的位置, int before改变前的内容数量, int count新增量
                public void onTextChanged(CharSequence s, int start, int before,
                                          int count) {
                    // 这里的s表示改变之后的内容，通常start和count组合，可以在s中读取本次改变字段中新的内容。而before表示被改变的内容的数量。
                    temp = s;
                }

                @Override
                // 表示最终内容
                public void afterTextChanged(Editable s) {
                    if (temp.length() > 0) {
                        // 设置清空按钮为可见
                        if (editText == editText1) {
                            clearButton1.setVisibility(View.VISIBLE);
                        } else if (editText == editText2) {
                            clearButton2.setVisibility(View.VISIBLE);
                        }
                    } else {
                        // 设置清空按钮不可见
                        if (editText == editText1) {
                            clearButton1.setVisibility(View.INVISIBLE);
                        } else if (editText == editText2) {
                            clearButton2.setVisibility(View.INVISIBLE);



                        }
                    }
                }
            }

            /**
             * 清空按钮点击事件
             *
             * @author
             */
            class ClearButtonListener implements View.OnClickListener {

                @Override
                public void onClick(View view) {
                    if (view == clearButton1) {
                        editText1.setText("");
                    } else if (view == clearButton2) {
                        editText2.setText("");
                    }
                }
            }

            /**
             * 焦点变更事件
             *
             * @author Auser
             */
            class EditTextListener implements View.OnFocusChangeListener {
                private Button clear;

                public EditTextListener(Button clear) {
                    this.clear = clear;
                }

                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    EditText textView = (EditText) v;
                    String hint;
                    if (hasFocus) {
                        // 当获取焦点时如果内容不为空则清空按钮可见
                        if (!textView.getText().toString().equals("")) {
                            clear.setVisibility(View.VISIBLE);
                        }
                        // if (textView == editText2) {
                        // // 设置输入格式为不可见的密码格式
                        // textView.setInputType(InputType.TYPE_CLASS_TEXT
                        // | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        // }
                        hint = textView.getHint().toString();
                        // 给TextView添加额外的数据
                        textView.setTag(hint);
                        textView.setHint("");
                    } else {
                        // 当失去焦点时清空按钮不可见
                        clear.setVisibility(View.INVISIBLE);
                        // if (textView == editText2) {
                        // // 设置输入格式为可见的密码格式
                        // textView.setInputType(InputType.TYPE_CLASS_TEXT
                        // | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        // }
                        // 取出之前添加的额外数据
                        hint = textView.getTag().toString();
                        textView.setHint(hint);
                    }

            }
        }
    }
