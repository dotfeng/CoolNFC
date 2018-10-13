package cn.edu.zju.coolnfc.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;


import cn.edu.zju.coolnfc.R;
import cn.edu.zju.coolnfc.activities.MainActivity;
import cn.edu.zju.coolnfc.activities.ReadMemoryActivity;
import cn.edu.zju.coolnfc.activities.RegisterConfigActivity;
import cn.edu.zju.coolnfc.activities.RegisterSessionActivity;
import cn.edu.zju.coolnfc.activities.ResetMemoryActivity;

public class ConfigFragment extends Fragment implements OnClickListener {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_config, container, false);
		
		((ImageView) v.findViewById(R.id.configSessionRegister)).setOnClickListener(this);
		((ImageView) v.findViewById(R.id.configConfigRegister)).setOnClickListener(this);
		((ImageView) v.findViewById(R.id.readMemory)).setOnClickListener(this);
		((ImageView) v.findViewById(R.id.resetMemory)).setOnClickListener(this);
		
		return v;
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		
		switch(v.getId()) {
			case R.id.readMemory:
				intent= new Intent(getActivity(), ReadMemoryActivity.class);
				break;
				
			case R.id.resetMemory:
				intent  = new Intent(getActivity(), ResetMemoryActivity.class);
				break;
		
			case R.id.configSessionRegister:
				intent  = new Intent(getActivity(), RegisterSessionActivity.class);
				break;
				
			case R.id.configConfigRegister:
				intent = new Intent(getActivity(), RegisterConfigActivity.class);
				break;
		}
		
		if(MainActivity.getmIntent() != null)
			intent.putExtras(MainActivity.getmIntent());
		
		startActivity(intent);
	}
}
