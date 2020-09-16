package com.exnovation.wishster.Models;

import android.app.Activity;
import android.view.inputmethod.InputMethodManager;
import android.widget.RadioButton;
import android.widget.TextView;

public class ValueSet {
    public ValueSet(TextView v,String msg) {
        if (!(msg==null))
        v.setText(msg);
    }

        public   ValueSet(Activity activity) {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) activity.getSystemService(
                            Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), 0);
        }

    public  ValueSet(RadioButton m,RadioButton f,RadioButton t,String msg)
    {
        if (!(msg==null))
        {
            msg=msg.toLowerCase().trim();
            if ( msg.toLowerCase().indexOf("male") != -1 ) {

                m.setChecked(true);
                f.setChecked(false);
                t.setChecked(false);

            } else if ( msg.toLowerCase().indexOf("female") != -1 ) {

                m.setChecked(false);
                f.setChecked(true);
                t.setChecked(false);

            }
            else if ( msg.toLowerCase().indexOf("other") != -1 ) {
                m.setChecked(false);
                f.setChecked(false);
                t.setChecked(true);
            }
            else {
                m.setChecked(false);
                f.setChecked(false);
                t.setChecked(false);
            }
        }
    }
}
