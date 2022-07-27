package com.example.mymessenger.template;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.mymessenger.R;

public class ProgressBtn {
    Context context;
    View view;

    public ProgressBtn(Context context,View view){
        this.context=context;
        this.view=view;
    }
    ConstraintLayout layout=view.findViewById(R.id.pBtn_cLayout);
    ProgressBar pBar=view.findViewById(R.id.pBtn_pBar);
    TextView tView=view.findViewById(R.id.pBtn_tView);
    public void buttonActivated(){
        pBar.setVisibility(View.VISIBLE);
        tView.setText(R.string.PleaseWait);
        layout.setBackgroundColor(layout.getResources().getColor(R.color.green));
    }
    public void buttonFinished(){
        pBar.setVisibility(View.INVISIBLE);
        tView.setText("Done");
        layout.setBackgroundColor(layout.getResources().getColor(R.color.primary));
    }
    public void buttonDeactivate(){
        pBar.setVisibility(View.INVISIBLE);
        tView.setText("Sign In");
        layout.setBackgroundColor(layout.getResources().getColor(R.color.primaryVariant));
    }


}
