package com.ionesmile.mvpdemo.search;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ionesmile.mvpdemo.R;
import com.ionesmile.mvpdemo.data.manager.DataManager;

public class MainActivity extends AppCompatActivity implements SearchContract.View {

    private SearchContract.Presenter mPresenter;

    private EditText etSearchKey;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new SearchPresenter(this, new DataManager());

        etSearchKey = (EditText) findViewById(R.id.et_search_key);
        tvResult = (TextView) findViewById(R.id.tv_result);

        findViewById(R.id.btn_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchKey = etSearchKey.getText().toString();
                if (TextUtils.isEmpty(searchKey)) {
                    showToast("搜索关键字不能为空！");
                    return;
                }
                mPresenter.startSearch(searchKey);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.stop();
    }

    @Override
    public void onSearchSuccess(String result) {
        tvResult.setText(result);
    }

    @Override
    public void onSearchFailure(String message) {
        tvResult.setText(message);
    }

    @Override
    public void setPresenter(SearchContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
