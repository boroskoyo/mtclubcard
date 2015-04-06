package com.seyisco.clubcard;

import android.app.Activity;
import android.os.Bundle;

public class Settings extends Activity{
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// note that use read_comments.xml instead of our single_post.xml
		setContentView(R.layout.settings);
	}

}
