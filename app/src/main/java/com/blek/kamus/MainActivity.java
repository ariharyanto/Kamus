package com.blek.kamus;

import android.annotation.*;
import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.os.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import android.text.*;
import android.view.*;
import android.widget.*;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity {
	
	private SQLiteDatabase db = null;
    private Cursor kamusCursor = null;
    private EditText txtIndonesia;
	private TextView txtJawa;
    private DataIndo datakamus = null;
	private static final int TIME_DELAY = 2000;
    private static long back_pressed;
	public static final String INDONESIA = "indonesia";
    public static final String JAWA = "jawa";
	private Button buttonTerjemahan;
	private CardView cv_blake;
	private ImageView java_image;
	private ClipboardManager myClipboard;
	private ClipData myClip;
	
	

	private void whatsn(boolean is24r){
		AlertDialog.Builder builder =
		new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
		builder.setTitle(getString(R.string.whatsn));
		builder.setMessage((Html.fromHtml(getString(R.string.whatsn_body))));
		builder.setPositiveButton(getString(R.string.close), new DialogInterface.OnClickListener() {	
			public void onClick(DialogInterface dialog, int which) {				
				return;		
				}
			});
				
				AlertDialog alert = builder.create();
				alert.show();
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
		
		datakamus = new DataIndo(this);
        db = datakamus.getWritableDatabase();
        datakamus.createTable(db);
        datakamus.generateData(db);
        txtIndonesia = (EditText) findViewById(R.id.txtIndonesia);
        txtJawa = (TextView) findViewById(R.id.txtJawa);
		
		buttonTerjemahan = (Button) findViewById(R.id.btnTerjemah);
        buttonTerjemahan.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String result = "";
					String indonesianword = txtIndonesia.getText().toString();
					kamusCursor = db.rawQuery("SELECT ID, INDONESIA, JAWA "
											  + "FROM kamus where INDONESIA='" + indonesianword
											  + "' ORDER BY INDONESIA", null);

					if (kamusCursor.moveToFirst()) {
						result = kamusCursor.getString(2);
						for (; !kamusCursor.isAfterLast(); kamusCursor.moveToNext()) {
							result = kamusCursor.getString(2);
						}
					}
					if (result.equals("")) {
						result = "😴";
					}
					txtJawa.setText(result);

				}});
	
	
	cv_blake = (CardView) findViewById(R.id.cv_result);
		cv_blake.setOnLongClickListener(new View.OnLongClickListener() {

				
				@SuppressLint("NewApi")
				public boolean onLongClick(View v)
				{
					String result = txtJawa.getText().toString();
					myClip = ClipData.newPlainText("text", result);
					myClipboard.setPrimaryClip(myClip);
					Toast.makeText(getApplicationContext(), "Text Copied", 
								   Toast.LENGTH_SHORT).show(); 
				
					return false;
				}
			
				
});

		java_image = (ImageView) findViewById(R.id.image_action);
		java_image.setOnLongClickListener(new View.OnLongClickListener() {

				@Override
				public boolean onLongClick(View p1)
				{
					return false;
				}
			});
}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { 
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_main, menu);
		return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
     int id = item.getItemId(); 
	 if (id == R.id.action_about) {	
	AlertDialog.Builder builder =
	new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
	builder.setTitle("KamusJawa v1.0.4");
	builder.setMessage((Html.fromHtml(getString(R.string.about_body))));
	builder.setPositiveButton(getString(R.string.whatsn), new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {			
			whatsn(false);		
			}
		});	
			
			AlertDialog alert = builder.create();
			alert.show();
			}return false;
	}
	
	public void onBackPressed() {
        if (back_pressed + TIME_DELAY > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            Toast.makeText(getBaseContext(), (getString(R.string.back)),
						   Toast.LENGTH_SHORT).show();
        }
        back_pressed = System.currentTimeMillis();
    }

}
