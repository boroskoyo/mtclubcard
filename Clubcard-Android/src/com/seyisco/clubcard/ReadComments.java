package com.seyisco.clubcard;

import java.util.ArrayList;

import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ReadComments extends ListActivity {

	// Progress Dialog
	private ProgressDialog pDialog;

	// php read comments script

	// localhost :
	// testing on your device
	// put your local ip instead, on windows, run CMD > ipconfig
	// or in mac's terminal type ifconfig and look for the ip under en0 or en1
	// private static final String READ_COMMENTS_URL =
	// "http://xxx.xxx.x.x:1234/webservice/comments.php";

	// testing on Emulator:
	private static final String READ_COMMENTS_URL = "http://seyis.co/comments.php";

	// testing from a real server:
	// private static final String READ_COMMENTS_URL =
	// "http://www.mybringback.com/webservice/comments.php";

	// JSON IDS:
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_POSTS = "posts";
	private static final String TAG_ID = "id";
	private static final String TAG_NAME = "name";
	private static final String TAG_DISCOUNT = "discount";
	private static final String TAG_ADDRESS = "address";
	// it's important to note that the message is both in the parent branch of
	// our JSON tree that displays a "Post Available" or a "No Post Available"
	// message,
	// and there is also a message for each individual post, listed under the
	// "posts"
	// category, that displays what the user typed as their message.

	// An array of all of our comments
	private JSONArray mComments = null;
	// manages all of our comments in a list.
	private ArrayList<HashMap<String, String>> mCommentList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// note that use read_comments.xml instead of our single_post.xml
		setContentView(R.layout.read_comments);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// loading the comments via AsyncTask
		new LoadComments().execute();
	}


	/**
	 * Retrieves recent post data from the server.
	 */
	public void updateJSONdata() {

		// Instantiate the arraylist to contain all the JSON data.
		// we are going to use a bunch of key-value pairs, referring
		// to the json element name, and the content, for example,
		// message it the tag, and "I'm awesome" as the content..

		mCommentList = new ArrayList<HashMap<String, String>>();

		// Bro, it's time to power up the J parser
		JSONParser jParser = new JSONParser();
		// Feed the beast our comments url, and it spits us
		// back a JSON object. Boo-yeah Jerome.
		JSONObject json = jParser.getJSONFromUrl(READ_COMMENTS_URL);

		// when parsing JSON stuff, we should probably
		// try to catch any exceptions:
		try {

			// I know I said we would check if "Posts were Avail." (success==1)
			// before we tried to read the individual posts, but I lied...
			// mComments will tell us how many "posts" or comments are
			// available
			mComments = json.getJSONArray(TAG_POSTS);

			// looping through all posts according to the json object returned
			for (int i = 0; i < mComments.length(); i++) {
				JSONObject c = mComments.getJSONObject(i);

				// gets the content of each tag
				String title = c.getString(TAG_NAME);
				String content = c.getString(TAG_ADDRESS);
				String username = c.getString(TAG_DISCOUNT);
				
				// creating new HashMap
				HashMap<String, String> map = new HashMap<String, String>();

				map.put(TAG_NAME, title);
				map.put(TAG_ADDRESS, content);
				map.put(TAG_DISCOUNT, username);

				// adding HashList to ArrayList
				mCommentList.add(map);

				// annndddd, our JSON data is up to date same with our array
				// list
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Inserts the parsed data into the listview.
	 */
	private void updateList() {
		// For a ListActivity we need to set the List Adapter, and in order to do
		//that, we need to create a ListAdapter.  This SimpleAdapter,
		//will utilize our updated Hashmapped ArrayList, 
		//use our single_post xml template for each item in our list,
		//and place the appropriate info from the list to the
		//correct GUI id.  Order is important here.
		ListAdapter adapter = new SimpleAdapter(this, mCommentList,
				R.layout.single_post, new String[] { TAG_NAME, TAG_ADDRESS,
						TAG_DISCOUNT }, new int[] { R.id.companyNameField, R.id.companyDistrictField,
						R.id.discountRate }) {
			
			 @Override
		        public View getView(int position, View convertView, ViewGroup parent) {

		            View view = super.getView(position, convertView, parent);
		            
		            @SuppressWarnings("unchecked")
					HashMap < String, String > items = (HashMap < String, String > ) getListView()
					.getItemAtPosition(position);
		           		           
		            if (items.get(TAG_DISCOUNT).equalsIgnoreCase("%10")){             	 
		            	view.findViewById(R.id.discountRate).setBackgroundColor(Color.parseColor("#f93d3d"));
		            }
		            if (items.get(TAG_DISCOUNT).equalsIgnoreCase("%15")){             	 
		            	view.findViewById(R.id.discountRate).setBackgroundColor(Color.parseColor("#0288d1"));
		            }
		            if (items.get(TAG_DISCOUNT).equalsIgnoreCase("%20")){             	 
		            	view.findViewById(R.id.discountRate).setBackgroundColor(Color.parseColor("#32ab70"));
		            }
		            if (items.get(TAG_DISCOUNT).equalsIgnoreCase("%25")){             	 
		            	view.findViewById(R.id.discountRate).setBackgroundColor(Color.parseColor("#ff3f80"));
		            }
		            if (items.get(TAG_DISCOUNT).equalsIgnoreCase("%30")){             	 
		            	view.findViewById(R.id.discountRate).setBackgroundColor(Color.parseColor("#ffea3a"));
		            }
		            return view;
		        }
			
		};
		
	
		
		// I shouldn't have to comment on this one:
		setListAdapter(adapter);

		// Optional: when the user clicks a list item we 
		//could do something.  However, we will choose
		//to do nothing...
		ListView lv = getListView();	
				
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// This method is triggered if an item is click within our
				// list. For our example we won't be using this, but
				// it is useful to know in real life applications.
				

			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu (Menu menu){
        super.onCreateOptionsMenu(menu);
        MenuInflater menudef = getMenuInflater();
        menudef.inflate(R.menu.main, menu);
        return true;
        }
 
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
        case R.id.Settings:
            startActivity (new Intent("com.seyisco.clubcard.Settings"));
            return true;
        case R.id.Developers:
            startActivity (new Intent("com.seyisco.clubcard.Developers"));
            return true;
        }
     return false;
    }

	public class LoadComments extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ReadComments.this);
			pDialog.setMessage("Lütfen Bekleyiniz...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected Boolean doInBackground(Void... arg0) {
			updateJSONdata();
			return null;

		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			pDialog.dismiss();
			updateList();
		}
	}
}
