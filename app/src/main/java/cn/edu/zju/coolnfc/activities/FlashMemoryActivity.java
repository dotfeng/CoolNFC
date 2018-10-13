package cn.edu.zju.coolnfc.activities;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import cn.edu.zju.coolnfc.R;
import cn.edu.zju.coolnfc.reader.Ntag_I2C_Demo;
import cn.edu.zju.coolnfc.utils.FileChooser;

@SuppressLint("NewApi")
public class FlashMemoryActivity extends Activity {
	public static final int REQUEST_FILE_CHOOSER = 1;
	
	private PendingIntent pendingIntent;
	private NfcAdapter mAdapter;
	private TextView filePath;
	private TextView dataRateCallback;
	private static ProgressDialog dialog;
	private Ntag_I2C_Demo demo;
	private static flashTask task;
	private boolean isAppFW = true;
	private int indexFW = 0;
	private byte[] bytesToFlash = null;
	private static Context mContext;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flashmemory);
		
		// Get the context reference
		mContext = getApplicationContext();

		// Add Foreground dispatcher
		mAdapter = NfcAdapter.getDefaultAdapter(this);
		pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,	getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		((Button) findViewById(R.id.selectFlashStorage)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startFileChooser();
			}
		});
		((Button) findViewById(R.id.selectFlashApp)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CharSequence firmwares[] = new CharSequence[] {"Demo App", "LED Blinker"};
				AlertDialog.Builder builder = new AlertDialog.Builder(FlashMemoryActivity.this);
				builder.setTitle(getResources().getString(R.string.flash_app_select));
				builder.setItems(firmwares, new DialogInterface.OnClickListener() {
				    @Override
				    public void onClick(DialogInterface dialog, int which) {
				    	isAppFW = true;
				        indexFW = which;
				        switch(indexFW) {
						case 0:
							filePath.setText(getResources().getString(R.string.file_default_demo));
							break;
						case 1:
							filePath.setText(getResources().getString(R.string.file_default_blinker));
							break;
						default:
							break;
						}				    	
				    }
				});
				builder.show();
			}
		});
		filePath = (TextView) findViewById(R.id.file_path);
		dataRateCallback = (TextView) findViewById(R.id.flashfwdata_datarateCallback);
		return; // end onCreate
	}

	@SuppressLint("NewApi")
	@Override
	public void onPause() {
		super.onPause();
		if (mAdapter != null) {
			mAdapter.disableForegroundDispatch(this);
		}
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MainActivity.AUTH_REQUEST  && resultCode == RESULT_OK) {
            if(demo != null && demo.isReady()) {
                // Read the bin to be flashed
                try {
                    if(isAppFW) {
                        bytesToFlash = readFileAssets(indexFW);
                    } else {
                        String path = ((TextView) findViewById(R.id.file_path)).getText().toString();
                        bytesToFlash = readFileMemory(path);
                    }
                } catch (IOException e) {
                    e.printStackTrace();

                    // Set bytesToFlash to null so that the task is not started
                    bytesToFlash = null;
                }
                if(bytesToFlash == null || bytesToFlash.length == 0) {
                    Toast.makeText(mContext, "Error could not open File",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                // Launch the thread
                task = new flashTask();
                task.execute();
            }   
        }
    }

	@TargetApi(Build.VERSION_CODES.KITKAT)
	@Override
	public void onResume() {
		super.onResume();
		if (mAdapter != null) {
			mAdapter.enableForegroundDispatch(this, pendingIntent, null,
						null);
		}
	}

	private void startFileChooser() {
		FileChooser chooser = new FileChooser(this);
		chooser.setExtension("bin");
		chooser.setFileListener(new FileChooser.FileSelectedListener() {
		    @Override 
		    public void fileSelected(final File file) {
		        String path = file.getAbsolutePath();                        
	            filePath.setText(path);

	            // We do not use the default binary anymore
	            isAppFW = false;
		    }
		}).showDialog();
	}
	
	protected void onNewIntent(Intent nfcIntent) {
		// Set the initial auth parameters
		MainActivity.setAuthStatus(AuthActivity.AuthStatus.Disabled.getValue());
		MainActivity.setPassword(null);
		
		// Store the intent information
		MainActivity.setNfcIntent(nfcIntent);
		Tag tag = nfcIntent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		startDemo(tag, true);
	}

	// Retrieve the binary from assets folder
	private byte[] readFileAssets(int indexFW) throws IOException  {
		byte[] data = null;
		AssetManager assManager = getApplicationContext().getAssets();
		InputStream is = null;
		try {
			switch(indexFW) {
			case 0:
				is = assManager.open("demo.bin");
				break;
			case 1:
				is = assManager.open("blink.bin");
				break;
			default:
				break;
			}
			int byteCount = is.available();
			data = new byte[byteCount];
			is.read(data, 0, byteCount);
		} finally {
			if(is != null) {
                is.close();
            }
		}
		return data;
	}
	
	// Retrieve the memory from the Internal Storage
	private byte[] readFileMemory(String path) throws IOException {
		File file = new File(path);
		if(!file.exists()) {
            return null;
        }
		
		// Open file
		RandomAccessFile f = new RandomAccessFile(file, "r");
		try {
			// Get and check length
			long longlength = f.length();
			int length = (int) longlength;
			if (length != longlength) {
                throw new IOException("File size >= 2 GB");
            }
			// Read file and return data
			byte[] data = new byte[length];
			f.readFully(data);
			return data;
		} finally {
			f.close();
		}
	}

	private void startDemo(Tag tag, boolean getAuthStatus) {
		try {
			if(isAppFW) {
				bytesToFlash = readFileAssets(indexFW);
			} else {
				String path = ((TextView) findViewById(R.id.file_path)).getText().toString();
				bytesToFlash = readFileMemory(path);
			}
		} catch (IOException e) {
			e.printStackTrace();
			
			// Set bytesToFlash to null so that the task is not started
			bytesToFlash = null;
		}

		if(bytesToFlash == null || bytesToFlash.length == 0) {
			Toast.makeText(mContext, "Error could not open File",
					Toast.LENGTH_SHORT).show();
			return;
		}

		demo = new Ntag_I2C_Demo(tag, this, MainActivity.getPassword(), MainActivity.getAuthStatus());
		if (!demo.isReady())
			return;
		
		// Retrieve the Auth Status
		if(getAuthStatus)
			 MainActivity.setAuthStatus(demo.ObtainAuthStatus());
		
		// Flashing is only available when the tag is not protected
		if(MainActivity.getAuthStatus() == AuthActivity.AuthStatus.Disabled.getValue()
				|| MainActivity.getAuthStatus() == AuthActivity.AuthStatus.Unprotected.getValue()
				|| MainActivity.getAuthStatus() == AuthActivity.AuthStatus.Authenticated.getValue()) {
			// Launch the thread
			task = new flashTask();
			task.execute();
		} else {
			showAuthDialog();
		}
	}
	
	public void showAuthDialog() {
		Intent intent = null;
		intent = new Intent(this, AuthActivity.class);
		intent.putExtras(MainActivity.getNfcIntent());
		startActivityForResult(intent, MainActivity.AUTH_REQUEST);
	}

	private class flashTask extends AsyncTask<Intent, Integer, Boolean> {
		private long timeToFlashFirmware = 0;
		public ProgressDialog dialog;
		
		@Override
		protected void onPostExecute(Boolean success) {
			// Inform the user about the task completion
			flashCompleted(success, bytesToFlash.length, timeToFlashFirmware);
			
			// Action completed
			dialog.dismiss();
		}

		@Override
		protected Boolean doInBackground(Intent... nfc_intent) {
			long RegTimeOutStart = System.currentTimeMillis();
			
			// Flash the new firmware
			boolean success = demo.Flash(bytesToFlash);
			
			// Flash firmware time statistics
			timeToFlashFirmware = System.currentTimeMillis() - RegTimeOutStart;
						
			return success;
		}

		@Override
		protected void onPreExecute() {
			// Show the progress dialog on the screen to inform about the action
			dialog = new ProgressDialog(FlashMemoryActivity.this);
			dialog.setTitle("Flashing");
			dialog.setMessage("Writing sector ...");
			dialog.setCancelable(false);
			dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			dialog.setProgress(0);
			dialog.show();
		}
	}

	public void flashCompleted(boolean success, int bytes, long time) {
		if (success) {
			Toast.makeText(mContext, "Flash Completed", Toast.LENGTH_SHORT)
					.show();
			String readTimeMessage = "";
			
			// Transmission Results
			readTimeMessage = readTimeMessage.concat("Flash Firmware\n");
			readTimeMessage = readTimeMessage.concat("Speed (" + bytes + " Byte / "
					+ time + " ms): "
					+ String.format("%.0f", bytes / (time / 1000.0))
					+ " Bytes/s");
			
			// Make the board input layout visible
			((LinearLayout) findViewById(R.id.layoutFlashStatistics)).setVisibility(View.VISIBLE);
			dataRateCallback.setText(readTimeMessage);
		} else
			Toast.makeText(mContext, "Error during memory flash",
					Toast.LENGTH_SHORT).show();
	}

	public void showAboutDialog() {
		Intent intent = null;
		intent = new Intent(this, VersionInfoActivity.class);
		startActivity(intent);
	}
	
	public static void setFLashDialogMax(int max) {
		task.dialog.setMax(max);
	}
	
	public static void updateFLashDialog() {
		task.dialog.incrementProgressBy(1);
	}
}
