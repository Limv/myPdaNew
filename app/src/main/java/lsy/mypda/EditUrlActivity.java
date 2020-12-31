package lsy.mypda;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditUrlActivity extends AppCompatActivity implements View.OnClickListener {
    private Button bt_sure,bt_cancel,bt_clear;
    private EditText et_url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_edit_url);
        bt_sure= (Button) findViewById(R.id.bt_sure);
        bt_cancel= (Button) findViewById(R.id.bt_cancel);
        et_url= (EditText) findViewById(R.id.et_url);
        bt_clear= (Button) findViewById(R.id.bt_clear);
        bt_sure.setOnClickListener(this);
        bt_cancel.setOnClickListener(this);
        bt_clear.setOnClickListener(this);
        et_url.setText(getIntent().getStringExtra("currentUrl"));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_sure:
                String url= et_url.getText().toString().trim();
                if(!Util.isVaildUrl(url)){
                    Toast.makeText(this,"请输入有效的网址!",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent i = new Intent();
                i.putExtra("url", et_url.getText().toString().trim());
                setResult(3, i);
                finish();
                break;
            case R.id.bt_cancel:
                finish();
                break;
            case R.id.bt_clear:
                et_url.setText("");
                break;
        }
    }

}
