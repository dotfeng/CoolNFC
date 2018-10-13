package cn.edu.zju.coolnfc.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;


import cn.edu.zju.coolnfc.R;

public class HelpActivity extends Activity {	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		WebView view = (WebView) findViewById(R.id.webview);
		view.loadUrl("file:///android_asset/help.html");
		return;
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onBackPressed() {
		Intent output = new Intent();
		setResult(RESULT_OK, output);
		finish();
	}

	@Override
	public void onResume() {
		super.onResume();

		 
	}
}
